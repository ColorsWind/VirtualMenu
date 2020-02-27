package com.blzeecraft.virtualmenu.core;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * 用来标记一个类或者一个方法是线程安全的.
 * @author colors_wind
 *
 */
@Retention(RUNTIME)
@Target({ TYPE, METHOD })
public @interface ThreadSafe {

}
