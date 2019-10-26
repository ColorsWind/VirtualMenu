package com.blzeecraft.virtualmenu.core.config.node;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.blzeecraft.virtualmenu.core.config.deserializer.IDeserializer;

@Documented
@Retention(RUNTIME)
@Target({TYPE, FIELD})
public @interface DeserializeBy {

	Class<? extends IDeserializer<?>> value();
}
