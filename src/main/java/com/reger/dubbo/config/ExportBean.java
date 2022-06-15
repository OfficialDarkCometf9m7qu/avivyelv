package com.reger.dubbo.config;


import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.config.spring.ServiceBean;
import com.reger.dubbo.annotation.Export;

public class ExportBean {

    public static AbstractBeanDefinition build(Export export,String beanName,Class<?> interfaces) {
    	BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.rootBeanDefinition(ServiceBean.class);
    	beanDefinitionBuilder.addPropertyReference("ref", beanName);
    	beanDefinitionBuilder.addPropertyValue("interface", interfaces);
    	if(StringUtils.hasText(export.provider())) {
    		beanDefinitionBuilder.addPropertyReference("provider", export.provider());
    	}
    	if(StringUtils.hasText(export.monitor())) {
    		beanDefinitionBuilder.addPropertyReference("monitor", export.monitor());
    	}
    	if(StringUtils.hasText(export.application())) {
    		beanDefinitionBuilder.addPropertyReference("application", export.application());
    	}
    	if(StringUtils.hasText(export.module())) {
    		beanDefinitionBuilder.addPropertyReference("module", export.module());
    	} 
    	if(export.registry().length>0) {
        	beanDefinitionBuilder.addPropertyValue("registries",toRuntimeBeanReferences(export.registry()));
    	}
    	if(export.protocol().length>0) {
        	beanDefinitionBuilder.addPropertyValue("protocols",toRuntimeBeanReferences(export.protocol()));
    	}
    	beanDefinitionBuilder.addPropertyValue("export", export.export());
    	beanDefinitionBuilder.addPropertyValue("deprecated", export.deprecated());
    	beanDefinitionBuilder.addPropertyValue("dynamic", export.dynamic());
    	beanDefinitionBuilder.addPropertyValue("executes", export.executes());
    	beanDefinitionBuilder.addPropertyValue("register", export.register());
    	beanDefinitionBuilder.addPropertyValue("weight", export.weight());
    	beanDefinitionBuilder.addPropertyValue("delay", export.delay());
    	beanDefinitionBuilder.addPropertyValue("connections", export.connections());
    	beanDefinitionBuilder.addPropertyValue("callbacks", export.callbacks());
    	beanDefinitionBuilder.addPropertyValue("retries", export.retries());
    	beanDefinitionBuilder.addPropertyValue("actives", export.actives());
    	beanDefinitionBuilder.addPropertyValue("async", export.async());
    	beanDefinitionBuilder.addPropertyValue("sent", export.sent());
    	beanDefinitionBuilder.addPropertyValue("timeout", export.timeout());
    	if(StringUtils.hasText(export.version())) {
    		beanDefinitionBuilder.addPropertyValue("version", export.version());
    	} 
    	if(StringUtils.hasText(export.group())) {
    		beanDefinitionBuilder.addPropertyValue("group", export.group());
    	} 
    	if(StringUtils.hasText(export.path())) {
    		beanDefinitionBuilder.addPropertyValue("path", export.path());
    	} 
    	if(StringUtils.hasText(export.token())) {
    		beanDefinitionBuilder.addPropertyValue("token", export.token());
    	} 
    	if(StringUtils.hasText(export.path())) {
    		beanDefinitionBuilder.addPropertyValue("path", export.path());
    	} 
    	if(StringUtils.hasText(export.document())) {
    		beanDefinitionBuilder.addPropertyValue("document", export.document());
    	} 
    	if(StringUtils.hasText(export.accesslog())) {
    		beanDefinitionBuilder.addPropertyValue("accesslog", export.accesslog());
    	} 
    	if(StringUtils.hasText(export.stub())) {
    		beanDefinitionBuilder.addPropertyValue("stub", export.stub());
    	} 
    	if(StringUtils.hasText(export.cluster())) {
    		beanDefinitionBuilder.addPropertyValue("cluster", export.cluster());
    	} 
    	if(StringUtils.hasText(export.proxy())) {
    		beanDefinitionBuilder.addPropertyValue("proxy", export.proxy());
    	} 
    	if(StringUtils.hasText(export.ondisconnect())) {
    		beanDefinitionBuilder.addPropertyValue("ondisconnect", export.ondisconnect());
    	} 
    	if(StringUtils.hasText(export.onconnect())) {
    		beanDefinitionBuilder.addPropertyValue("onconnect", export.onconnect());
    	} 
    	if(StringUtils.hasText(export.owner())) {
    		beanDefinitionBuilder.addPropertyValue("owner", export.owner());
    	} 
    	if(StringUtils.hasText(export.layer())) {
    		beanDefinitionBuilder.addPropertyValue("layer", export.layer());
    	} 
    	if(StringUtils.hasText(export.loadbalance())) {
    		beanDefinitionBuilder.addPropertyValue("loadbalance", export.loadbalance());
    	} 
    	if(StringUtils.hasText(export.mock())) {
    		beanDefinitionBuilder.addPropertyValue("mock", export.mock());
    	} 
    	if(StringUtils.hasText(export.validation())) {
    		beanDefinitionBuilder.addPropertyValue("validation", export.validation());
    	} 
    	if(StringUtils.hasText(export.cache())) {
    		beanDefinitionBuilder.addPropertyValue("cache", export.cache());
    	} 
    	if(export.filter().length>0) {
        	beanDefinitionBuilder.addPropertyValue("filter",toStr(export.filter()));
    	}
    	if(export.listener().length>0) {
        	beanDefinitionBuilder.addPropertyValue("listener",toStr(export.listener()));
    	}
    	if(export.parameters().length>0) {
        	beanDefinitionBuilder.addPropertyValue("listener",toMap(export.parameters()));
    	}
    	beanDefinitionBuilder.setInitMethodName("export");
    	return beanDefinitionBuilder.getBeanDefinition();
	}

    public static AbstractBeanDefinition build(Service service,String beanName,Class<?> interfaces) {
    	BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.rootBeanDefinition(ServiceBean.class);
    	beanDefinitionBuilder.addPropertyReference("ref", beanName);
    	beanDefinitionBuilder.addPropertyValue("interface", interfaces);
    	if(StringUtils.hasText(service.provider())) {
    		beanDefinitionBuilder.addPropertyReference("provider", service.provider());
    	}
    	if(StringUtils.hasText(service.monitor())) {
    		beanDefinitionBuilder.addPropertyReference("monitor", service.monitor());
    	}
    	if(StringUtils.hasText(service.application())) {
    		beanDefinitionBuilder.addPropertyReference("application", service.application());
    	}
    	if(StringUtils.hasText(service.module())) {
    		beanDefinitionBuilder.addPropertyReference("module", service.module());
    	} 
    	if(service.registry().length>0) {
        	beanDefinitionBuilder.addPropertyValue("registries",toRuntimeBeanReferences(service.registry()));
    	}
    	if(service.protocol().length>0) {
        	beanDefinitionBuilder.addPropertyValue("protocols",toRuntimeBeanReferences(service.protocol()));
    	}
    	beanDefinitionBuilder.addPropertyValue("export", service.export());
    	beanDefinitionBuilder.addPropertyValue("deprecated", service.deprecated());
    	beanDefinitionBuilder.addPropertyValue("dynamic", service.dynamic());
    	beanDefinitionBuilder.addPropertyValue("executes", service.executes());
    	beanDefinitionBuilder.addPropertyValue("register", service.register());
    	beanDefinitionBuilder.addPropertyValue("weight", service.weight());
    	beanDefinitionBuilder.addPropertyValue("delay", service.delay());
    	beanDefinitionBuilder.addPropertyValue("connections", service.connections());
    	beanDefinitionBuilder.addPropertyValue("callbacks", service.callbacks());
    	beanDefinitionBuilder.addPropertyValue("retries", service.retries());
    	beanDefinitionBuilder.addPropertyValue("actives", service.actives());
    	beanDefinitionBuilder.addPropertyValue("async", service.async());
    	beanDefinitionBuilder.addPropertyValue("sent", service.sent());
    	beanDefinitionBuilder.addPropertyValue("timeout", service.timeout());
    	if(StringUtils.hasText(service.version())) {
    		beanDefinitionBuilder.addPropertyValue("version", service.version());
    	} 
    	if(StringUtils.hasText(service.group())) {
    		beanDefinitionBuilder.addPropertyValue("group", service.group());
    	} 
    	if(StringUtils.hasText(service.path())) {
    		beanDefinitionBuilder.addPropertyValue("path", service.path());
    	} 
    	if(StringUtils.hasText(service.token())) {
    		beanDefinitionBuilder.addPropertyValue("token", service.token());
    	} 
    	if(StringUtils.hasText(service.path())) {
    		beanDefinitionBuilder.addPropertyValue("path", service.path());
    	} 
    	if(StringUtils.hasText(service.document())) {
    		beanDefinitionBuilder.addPropertyValue("document", service.document());
    	} 
    	if(StringUtils.hasText(service.accesslog())) {
    		beanDefinitionBuilder.addPropertyValue("accesslog", service.accesslog());
    	} 
    	if(StringUtils.hasText(service.stub())) {
    		beanDefinitionBuilder.addPropertyValue("stub", service.stub());
    	} 
    	if(StringUtils.hasText(service.cluster())) {
    		beanDefinitionBuilder.addPropertyValue("cluster", service.cluster());
    	} 
    	if(StringUtils.hasText(service.proxy())) {
    		beanDefinitionBuilder.addPropertyValue("proxy", service.proxy());
    	} 
    	if(StringUtils.hasText(service.ondisconnect())) {
    		beanDefinitionBuilder.addPropertyValue("ondisconnect", service.ondisconnect());
    	} 
    	if(StringUtils.hasText(service.onconnect())) {
    		beanDefinitionBuilder.addPropertyValue("onconnect", service.onconnect());
    	} 
    	if(StringUtils.hasText(service.owner())) {
    		beanDefinitionBuilder.addPropertyValue("owner", service.owner());
    	} 
    	if(StringUtils.hasText(service.layer())) {
    		beanDefinitionBuilder.addPropertyValue("layer", service.layer());
    	} 
    	if(StringUtils.hasText(service.loadbalance())) {
    		beanDefinitionBuilder.addPropertyValue("loadbalance", service.loadbalance());
    	} 
    	if(StringUtils.hasText(service.mock())) {
    		beanDefinitionBuilder.addPropertyValue("mock", service.mock());
    	} 
    	if(StringUtils.hasText(service.validation())) {
    		beanDefinitionBuilder.addPropertyValue("validation", service.validation());
    	} 
    	if(StringUtils.hasText(service.cache())) {
    		beanDefinitionBuilder.addPropertyValue("cache", service.cache());
    	} 
    	if(service.filter().length>0) {
        	beanDefinitionBuilder.addPropertyValue("filter",toStr(service.filter()));
    	}
    	if(service.listener().length>0) {
        	beanDefinitionBuilder.addPropertyValue("listener",toStr(service.listener()));
    	}
    	if(service.parameters().length>0) {
        	beanDefinitionBuilder.addPropertyValue("listener",toMap(service.parameters()));
    	}
    	beanDefinitionBuilder.setInitMethodName("export");
    	return beanDefinitionBuilder.getBeanDefinition();
	}
    private static Map<String,String> toMap(String[] strs) {
    	Map<String,String> relust=new HashMap<>(strs.length);
    	for (String string : strs) {
    		int index = string.indexOf("=");
    		if(index==-1) {
    			relust.put(string, null);
    		}else {
        		relust.put(string.substring(0, index), string.substring(index+1));
    		}
		}
    	return relust;
	}
    
    private static String toStr(String[] strs) {
    	String str=null;
    	for (String string : strs) {
    		if(str==null) {
    			str=string;
    		}else {
    			str+=","+string;
    		}
		}
    	return str;
	}
    
	private static ManagedList<RuntimeBeanReference> toRuntimeBeanReferences(String... beanNames) {
		ManagedList<RuntimeBeanReference> runtimeBeanReferences = new ManagedList<RuntimeBeanReference>();
		if (!ObjectUtils.isEmpty(beanNames)) {
			for (String beanName : beanNames) {
				runtimeBeanReferences.add(new RuntimeBeanReference(beanName));
			}
		}
		return runtimeBeanReferences;
	}

}
