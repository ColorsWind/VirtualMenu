package com.blzeecraft.virtualmenu.core.command;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * 用于标记子命令使用帮助.
 * @author colors_wind
 * @date 2020-02-14
 */
@Retention(RUNTIME)
@Target(METHOD)
public @interface Usage {
	/**
	 * 获取命令帮助.
	 * @return 命令帮助, 若不手动指定则隐藏该命令帮助.
	 */
	String value() default "";

}
