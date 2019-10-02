package com.blzeecraft.virtualmenu.core.config;

import java.util.HashMap;

import lombok.NonNull;
import lombok.val;
import lombok.experimental.UtilityClass;

@UtilityClass
public class LineParser {
	
	public static LineConfig parse(@NonNull String s) {
		if (!s.startsWith("{")) {
			throw new InvalidLineFormatException("格式错误(缺少\'{\', 正确示例}s={\'Testing\'}");
		}
		if(!s.endsWith("}")) {
			throw new InvalidLineFormatException("格式错误(缺少\'}\', 正确示例:}s={\'Testing\'}");
		}
		return parseLine(s.substring(1, s.length() -1));

	}

	public static LineConfig parseLine(@NonNull String s) {
		char[] chars = s.toCharArray();
		boolean isKey = true; //是否正在读取Key
		boolean isTransferr = false; //下一个字符是否转移
		boolean single_quotes = false; //单引号之间
		boolean double_quotes = false; //双引号之间
		StringBuilder input = new StringBuilder(); //用于临时储存输入字符
		String key = null; //用于临时储存key
		val values = new HashMap<String, String>();
		for(int i=0;i<chars.length;i++) {
			char c = chars[i];
			if(isTransferr) { //已转义
				isTransferr = false;
				input.append(c);
			} else if ('\\' == c){ //开始转移
				isTransferr = true;
			} else if ('\"' == c) { //双引号具有优先权
				double_quotes = !double_quotes;
			} else if (double_quotes) { //在双引号之间
				input.append(c);
			} else if ('\'' == c) { //后判定单引号
				single_quotes = !single_quotes;
			} else if (single_quotes) { //在单引号之间
				input.append(c);
			} else if (!isKey && ',' == c) { //每项的结尾
				isKey = true;
				values.put(key, input.toString());
				input = new StringBuilder();
			} else if (isKey && '=' == c) { //值的开始
				isKey = false;
				key = input.toString();
				input = new StringBuilder();
			} else { //正常的值
				input.append(c);
			}
		}
		//尾部处理,检查是否处理完毕
		if (double_quotes) {
			throw new InvalidLineFormatException("格式错误(缺少配对的双引号), 正确示例{s=\"Tesing\"}") ;
		}
		if (single_quotes) {
			throw new InvalidLineFormatException("格式错误(缺少配对的单引号), 正确示例{s=\'Tesing\'}") ;
		}
		if (isKey) {
			throw new InvalidLineFormatException("格式错误(缺少\"=\"), 正确示例{s=Tesing}") ;
		}
		//尾部处理,将最后一对key-value放入map
		values.put(key, input.toString());
		return new LineConfig(values);
	}
	
	
	

}
