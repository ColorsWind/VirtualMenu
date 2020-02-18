package com.blzeecraft.virtualmenu.core.command;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * 用于标记子命令只能由玩家发出.
 * @author colors_wind
 * @date 2020-02-14
 */
@Retention(RUNTIME)
@Target(METHOD)
public @interface PlayerOnly {}
