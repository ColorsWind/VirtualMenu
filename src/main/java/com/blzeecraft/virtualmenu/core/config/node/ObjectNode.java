package com.blzeecraft.virtualmenu.core.config.node;

import static com.blzeecraft.virtualmenu.core.config.node.DataType.OBJECT;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Documented
@Retention(RUNTIME)
@Target(FIELD)
public @interface ObjectNode {
	
	String[] key();
	
	DataType type() default OBJECT;

}
