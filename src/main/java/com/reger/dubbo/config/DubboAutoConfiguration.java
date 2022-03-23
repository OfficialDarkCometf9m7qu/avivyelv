package com.reger.dubbo.config;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.bind.PropertiesConfigurationFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.util.SocketUtils;
import org.springframework.util.StringUtils;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ConsumerConfig;
import com.alibaba.dubbo.config.ModuleConfig;
import com.alibaba.dubbo.config.MonitorConfig;
import com.alibaba.dubbo.config.ProtocolConfig;
import com.alibaba.dubbo.config.ProviderConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.config.ServiceConfig;
import com.alibaba.dubbo.config.spring.beans.factory.annotation.InjectAnnotationBeanPostProcessor;
import com.alibaba.dubbo.config.spring.beans.factory.annotation.ReferenceAnnotationBeanPostProcessor;
import com.alibaba.dubbo.config.spring.util.BeanRegistrar;
import com.reger.dubbo.properties.DubboProperties;
import com.reger.dubbo.rpc.filter.ConsumerFilter;
import com.reger.dubbo.rpc.filter.ConsumerFilterBean;
import com.reger.dubbo.rpc.filter.ProviderFilter;
import com.reger.dubbo.rpc.filter.ProviderFilterBean;

public class DubboAutoConfiguration extends AnnotationBean
		implements EnvironmentAware, ApplicationContextAware, CommandLineRunner {

	public DubboAutoConfiguration() {
		super();
	}

	private final static Logger logger = LoggerFactory.getLogger(DubboAutoConfiguration.class);

	private static final long serialVersionUID = 1L;

	private ConfigurableEnvironment environment;
	private ApplicationContext applicationContext;

	@Override
	public void setEnvironment(Environment environment) {
		super.setEnvironment(environment);
		this.environment = (ConfigurableEnvironment) environment;
	}

	private <T> T getPropertiesConfigurationBean(String targetName, Class<T> types) {
		PropertiesConfigurationFactory<T> factory = new PropertiesConfigurationFactory<T>(types);
		factory.setPropertySources(environment.getPropertySources());
		factory.setConversionService(environment.getConversionService());
		factory.setIgnoreInvalidFields(true);
		factory.setIgnoreUnknownFields(true);
		factory.setIgnoreNestedProperties(false);
		factory.setTargetName(targetName);
		try {
			factory.bindPropertiesToTarget();
			return factory.getObject();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private List<ProtocolConfig> getProtocols(DubboProperties dubboProperties) {
		List<ProtocolConfig> protocols = dubboProperties.getProtocols();
		if (protocols == null) {
			protocols = new ArrayList<ProtocolConfig>();
		}
		if (dubboProperties.getProtocol() != null) {
			protocols.add(dubboProperties.getProtocol());
		}
		return protocols;
	}

	private List<RegistryConfig> getRegistrys(DubboProperties dubboProperties) {
		List<RegistryConfig> registryConfigs = dubboProperties.getRegistrys();
		if (registryConfigs == null) {
			registryConfigs = new ArrayList<RegistryConfig>();
		}
		if (dubboProperties.getProtocol() != null) {
			registryConfigs.add(dubboProperties.getRegistry());
		}
		return registryConfigs;
	}

	private List<RegistryConfig> getRegistry(List<RegistryConfig> registrys, String environmentName) {
		String value = environment.getProperty(environmentName);
		if (StringUtils.isEmpty(value)) {
			return null;
		}
		String[] vals = value.split(",");
		List<RegistryConfig> ret = new ArrayList<RegistryConfig>();
		for (String val : vals) {
			for (RegistryConfig registryConfig : registrys) {
				if (val.trim().equals(registryConfig.getId())) {
					ret.add(registryConfig);
				}
			}
		}
		return ret;
	}

	private List<ProtocolConfig> getProtocol(List<ProtocolConfig> protocols, String environmentName) {
		String value = environment.getProperty(environmentName);
		if (StringUtils.isEmpty(value)) {
			return protocols;
		}
		String[] vals = value.split(",");
		List<ProtocolConfig> ret = new ArrayList<ProtocolConfig>();
		for (String val : vals) {
			for (ProtocolConfig protocolConfig : protocols) {
				if (val.trim().equals(protocolConfig.getId())) {
					ret.add(protocolConfig);
				}
			}
		}
		return ret;
	}

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		DubboProperties dubboProperties = this.getPropertiesConfigurationBean(DubboProperties.targetName,
				DubboProperties.class);
		ApplicationConfig application = dubboProperties.getApplication();
		MonitorConfig monitor = dubboProperties.getMonitor();
		ModuleConfig module = dubboProperties.getModule();
		ProviderConfig provider = dubboProperties.getProvider();
		ConsumerConfig consumer = dubboProperties.getConsumer();
		List<ProtocolConfig> protocols = this.getProtocols(dubboProperties);
		List<RegistryConfig> registryConfigs = this.getRegistrys(dubboProperties);
		List<ReferenceConfig<?>> references = dubboProperties.getReferences();
		List<ServiceConfig<?>> services = dubboProperties.getServices();
		this.registerRegistry(registryConfigs, beanFactory);

		String basePackage = dubboProperties.getBasePackage();

		if (provider != null) {
			provider.setProtocols(this.getProtocol(protocols, "spring.dubbo.provider.protocol"));
			provider.setRegistries(this.getRegistry(registryConfigs, "spring.dubbo.provider.registry"));
		}

		if (consumer != null) {
			consumer.setRegistries(this.getRegistry(registryConfigs, "spring.dubbo.consumer.registry"));
		}

		this.registerThis(basePackage, beanFactory);
		this.registerApplication(application, beanFactory);
		this.registerProtocols(protocols, beanFactory);
		this.registerMonitor(monitor, beanFactory);
		this.registerModule(module, beanFactory);
		this.registerProvider(provider, beanFactory);
		this.registerConsumer(consumer, beanFactory);
		this.registerReferences(references, beanFactory);
		this.registerServices(services, beanFactory);
		super.postProcessBeanFactory(beanFactory);
		super.postProcessAnnotationPackageService();
	}

	@Override
	public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
		super.postProcessBeanDefinitionRegistry(registry);
		BeanRegistrar.registerInfrastructureBean(registry, InjectAnnotationBeanPostProcessor.BEAN_NAME,
				InjectAnnotationBeanPostProcessor.class);
		BeanRegistrar.registerInfrastructureBean(registry, ReferenceAnnotationBeanPostProcessor.BEAN_NAME,
				ReferenceAnnotationBeanPostProcessor.class);
	}

	private void registerConsumer(ConsumerConfig consumer, ConfigurableListableBeanFactory beanFactory) {
		if (consumer != null) {
			String beanName = consumer.getId();
			if (StringUtils.isEmpty(beanName)) {
				beanName = "consumerConfig";
			}
			String filter = consumer.getFilter();
			if (StringUtils.isEmpty(filter)) {
				filter = "regerConsumerFilter";
			} else {
				filter = filter.trim() + ",regerConsumerFilter";
			}
			logger.debug("添加consumerFilter后的Filter, {}", filter);
			consumer.setFilter(filter);
			beanFactory.registerSingleton(beanName, consumer);
		} else {
			logger.debug("dubbo 没有配置默认的消费者参数");
		}
	}

	private void registerProvider(ProviderConfig provider, ConfigurableListableBeanFactory beanFactory) {
		if (provider != null) {
			String beanName = provider.getId();
			if (StringUtils.isEmpty(beanName)) {
				beanName = "providerConfig";
			}
			String filter = provider.getFilter();
			if (StringUtils.isEmpty(filter)) {
				filter = "regerProviderFilter";
			} else {
				filter = filter.trim() + ",regerProviderFilter";
			}
			logger.debug("添加consumerFilter后的Filter, {}", filter);
			provider.setFilter(filter);
			beanFactory.registerSingleton(beanName, provider);
		} else {
			logger.debug("dubbo 没有配置默认的生成者参数");
		}
	}

	private void registerModule(ModuleConfig module, ConfigurableListableBeanFactory beanFactory) {
		if (module != null) {
			String beanName = module.getId();
			if (StringUtils.isEmpty(beanName)) {
				beanName = "moduleConfig";
			}
			beanFactory.registerSingleton(beanName, module);
		} else {
			logger.debug("dubbo 没有配置模块信息");
		}
	}

	private void registerReferences(List<ReferenceConfig<?>> references, ConfigurableListableBeanFactory beanFactory) {
		if (references == null || references.isEmpty()) {
			return;
		}
		for (int index = 0; index < references.size(); index++) {
			ReferenceConfig<?> referenceConfig = references.get(index);
			String beanName = referenceConfig.getId();
			if (StringUtils.isEmpty(beanName)) {
				beanName = "referenceConfig" + index;
			}
			beanFactory.registerSingleton(beanName, referenceConfig);
			beanFactory.registerSingleton(beanName, referenceConfig.get());
			logger.debug("注册服务调用信息{} 完毕", beanName);
		}
	}

	private void registerServices(List<ServiceConfig<?>> services, ConfigurableListableBeanFactory beanFactory) {
		if (services == null || services.isEmpty()) {
			return;
		}
		for (int index = 0; index < services.size(); index++) {
			ServiceConfig<?> serviceConfig = services.get(index);
			String beanName = serviceConfig.getId();
			if (StringUtils.isEmpty(beanName)) {
				beanName = "serviceConfig" + index;
			}
			beanFactory.registerSingleton(beanName, serviceConfig);
			serviceConfig.export();
			logger.debug("注册发布服务{} 完毕", beanName);
		}
	}

	private void registerMonitor(MonitorConfig monitorConfig, ConfigurableListableBeanFactory beanFactory) {
		if (monitorConfig != null) {
			String beanName = monitorConfig.getId();
			if (StringUtils.isEmpty(beanName)) {
				beanName = "monitorConfig";
			}
			beanFactory.registerSingleton(beanName, monitorConfig);
		} else {
			logger.debug("dubbo 没有配置服务监控中心");
		}
	}

	private void registerRegistry(List<RegistryConfig> registryConfigs, ConfigurableListableBeanFactory beanFactory) {
		if (registryConfigs == null || registryConfigs.isEmpty()) {
			logger.warn("dubbo 没有配置服务注册中心");
		} else {
			for (int index = 0; index < registryConfigs.size(); index++) {
				RegistryConfig registryConfig = registryConfigs.get(index);
				String beanName = registryConfig.getId();
				if (StringUtils.isEmpty(beanName)) {
					beanName = "registryConfig-" + index;
				}
				beanFactory.registerSingleton(beanName, registryConfig);
			}
		}
	}

	private void registerThis(String annotationPackages, ConfigurableListableBeanFactory beanFactory) {
		if (StringUtils.isEmpty(annotationPackages)) {
			logger.warn(" dubbo没有配置注解服务所在的目录");
		}
		this.setPackage(annotationPackages);
		super.setId("dubboAnnotationPackageS");
	}

	private void registerApplication(ApplicationConfig applicationConfig, ConfigurableListableBeanFactory beanFactory) {
		if (applicationConfig != null) {
			String name = applicationConfig.getId();
			if (StringUtils.isEmpty(name)) {
				name = "application";
			}
			beanFactory.registerSingleton(name, applicationConfig);
		} else {
			logger.warn("dubbo 没有配置服务名信息");
		}
	}

	private void registerProtocols(List<ProtocolConfig> protocols, ConfigurableListableBeanFactory beanFactory) {
		if (protocols == null || protocols.isEmpty()) {
			logger.info("dubbo 没有配置协议,将使用默认协议");
			return;
		}
		for (int index = 0; index < protocols.size(); index++) {
			ProtocolConfig protocol = protocols.get(index);
			String beanName = protocol.getId();
			if (StringUtils.isEmpty(beanName)) {
				beanName = "protocolConfig" + index;
			}
			if (protocol.getPort() == null || protocol.getPort() == 0) {
				protocol.setPort(SocketUtils.findAvailableTcpPort(53600, 53688));
			}
			beanFactory.registerSingleton(beanName, protocol);
			logger.debug("注册协议信息{}完毕", beanName);
		}
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	@Override
	public void run(String... args) throws Exception {
		try {
			ConsumerFilter consumerFilter = applicationContext.getBean(ConsumerFilter.class);
			ConsumerFilterBean.setConsumerFilter(consumerFilter);
		} catch (NoSuchBeanDefinitionException e) {
			logger.debug("没有ConsumerFilter");
		}
		try {
			ProviderFilter providerFilter = applicationContext.getBean(ProviderFilter.class);
			ProviderFilterBean.setProviderFilter(providerFilter);
		} catch (NoSuchBeanDefinitionException e) {
			logger.debug("没有ProviderFilter");
		}
	}

}
