package com.blzeecraft.virtualmenu.core.conf.transition;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target(FIELD)
public @interface ObjectType {
	
	Class<? extends SubConf> value();

}
