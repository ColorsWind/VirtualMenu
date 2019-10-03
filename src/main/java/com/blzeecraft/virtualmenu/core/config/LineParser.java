package com.blzeecraft.virtualmenu.core.config;

import java.util.HashMap;

import lombok.NonNull;
import lombok.val;
import lombok.experimental.UtilityClass;

/**
 * 用于解析单行配置
 * @author colors_wind
 *
 */
@UtilityClass
public class LineParser {
	//常量池 单行配置解析器提示信息
	public static final String CORRECT_FORMAT_EXAMPLE = "正确示例tell{s=Testing}";
	public static final String ERROR_FORMAT_LEFT_BRACES = "格式错误(缺少\'{\'), " + CORRECT_FORMAT_EXAMPLE;
	public static final String ERROR_FORMAT_RIGHT_BRACES = "格式错误(缺少\'}\'), " + CORRECT_FORMAT_EXAMPLE;
	public static final String ERROR_FORMAT_EQUAL = "格式错误(缺少\'=\'), " + CORRECT_FORMAT_EXAMPLE;
	
	
	public static LineConfig parseEnclose(@NonNull String s) throws InvalidLineFormatException {
		if (!s.startsWith("{")) {
			throw new InvalidLineFormatException(ERROR_FORMAT_LEFT_BRACES);
		}
		if(!s.endsWith("}")) {
			throw new InvalidLineFormatException(ERROR_FORMAT_RIGHT_BRACES);
		}
		return parseLine(s.substring(1, s.length() -1));
	}
	
	

	public static LineConfig parseLine(@NonNull String s) throws InvalidLineFormatException {
		char[] chars = s.toCharArray();
		boolean isKey = true; //是否正在读取Key
		boolean isTransferr = false; //下一个字符是否转移
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
		if (isKey) {
			throw new InvalidLineFormatException(ERROR_FORMAT_EQUAL) ;
		}
		//尾部处理,将最后一对key-value放入map
		values.put(key, input.toString());
		return new LineConfig(values);
	}
	
	
	

}
