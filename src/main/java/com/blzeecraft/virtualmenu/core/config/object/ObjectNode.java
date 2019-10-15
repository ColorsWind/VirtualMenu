package com.blzeecraft.virtualmenu.core.config.object;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.blzeecraft.virtualmenu.core.config.template.GlobalConfTemplate;
import com.blzeecraft.virtualmenu.core.config.template.ITemplate;

@Documented
@Retention(RUNTIME)
@Target(FIELD)
public @interface ObjectNode {
	
	String[] key() default {};
	
	Class<? extends Enum<?>>[] enumKey() default {};
	
	DataType type() default DataType.RAW;
	
	Class<? extends ITemplate<?>>[] classType() default {};
	


}


