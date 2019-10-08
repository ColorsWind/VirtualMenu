package com.blzeecraft.virtualmenu.core.config.root;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.blzeecraft.virtualmenu.core.config.map.ITemplate;

@Documented
@Retention(RUNTIME)
@Target(FIELD)
public @interface SpecificNode {

	String key();
	
	Class<? extends ITemplate<?>> template(); 

}
