package com.blzeecraft.virtualmenu.config;

import com.blzeecraft.virtualmenu.logger.ILog;

public interface IConfig extends ILog {

	/**
	 * 读取配置文件后进行处理
	 * 这个方法会在{@link ConfigReader} 设置完字段的值后条用
	 */
	void apply();
}
