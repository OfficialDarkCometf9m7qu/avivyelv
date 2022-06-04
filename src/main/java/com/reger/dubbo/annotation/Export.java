package com.reger.dubbo.annotation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.springframework.core.annotation.AliasFor;

@Target(TYPE)
@Retention(RUNTIME)
@org.springframework.stereotype.Service
public @interface Export {

	@AliasFor(annotation = org.springframework.stereotype.Service.class)
	String value() default "";

	com.alibaba.dubbo.config.annotation.Service service() default @com.alibaba.dubbo.config.annotation.Service;
}
