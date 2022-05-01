package com.shenhua.pdfboxctl.basement;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class StringUtil {

	private StringUtil() {
	}

	/**
	 * 字符串分隔符
	 */
	public static final String SEPARATOR = String.valueOf((char) 29);

	/**
	 * 判断为空
	 * 
	 * true : 为空 false: 不为空
	 * 
	 * @param text
	 * @return boolean
	 */
	public static boolean isEmpty(String text) {
		if (null == text || "".equals(text) || text.trim().isEmpty())
			return true;
		return false;
	}

	/**
	 * 支持obj
	 *
	 * @param str
	 *            判断字符串是否为空
	 * @return true is empty ,false is not empty
	 */
	@SuppressWarnings("rawtypes")
	public static boolean isEmpty(Object str) {
		if (str == null)
			return true;
		if (str instanceof String) {
			return String.valueOf(str).trim().isEmpty();
		} else if (str instanceof Map) {
			return ((Map) str).isEmpty();
		} else if (str instanceof Collection) {
			Collection coll = (Collection) str;
			return coll.isEmpty();
		} else if (str.getClass().isArray()) {
			return Array.getLength(str) == 0;
		} else if (str instanceof Long) {
			Long l = (Long) str;
			return l == 0;
		}
		return false;
	}

	/**
	 * 判断不为空
	 * 
	 * @param text
	 * @return boolean
	 */
	public static boolean isNotEmpty(String text) {
		return !isEmpty(text);
	}

	/**
	 * 替换字符串中regex匹配所有的内容为replace
	 * 
	 * @param text
	 * @param regex
	 * @param replace
	 * @return String
	 */
	public static String replaceAll(String text, String regex, String replace) {
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(text);
		StringBuffer sb = new StringBuffer();
		while (m.find()) {
			m.appendReplacement(sb, replace);
		}
		m.appendTail(sb);
		return sb.toString();
	}

	/**
	 * 获得包含中文的字符串长度方法(一个中文为2个字符)
	 * 
	 * @param text
	 * @return int
	 */
	public static int len(String text) {
		if (isEmpty(text))
			return 0;
		int len = 0;
		for (int i = 0; i < text.length(); i++) {
			if (text.charAt(i) > 127 || text.charAt(i) == 94) {
				len += 2;
			} else {
				len++;
			}
		}
		return len;
	}

	/**
	 * 判断字符串是否是乱码
	 *
	 * @param text 字符串
	 * @return 是否是乱码
	 */
	public static boolean isMessyCode(String text) {
		Pattern p = Pattern.compile("\\s*|t*|r*|n*");
		Matcher m = p.matcher(text);
		String after = m.replaceAll("");
		String temp = after.replaceAll("\\p{P}", "");
		char[] ch = temp.trim().toCharArray();
		float chLength = ch.length;
		float count = 0;
		for (int i = 0; i < ch.length; i++) {
			char c = ch[i];
			if (!Character.isLetterOrDigit(c)) {
				if (!isChinese(c)) {
					count = count + 1;
				}
			}
		}
		float result = count / chLength;
		if (result > 0.4) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 判断一个字符串是否包含字符串数组中的某个字符串
	 * @param strs
	 * @param s
	 * @return
	 */
	public static boolean isHave(String s, String[] strs) {
		//此方法有两个参数，第一个是要查找的字符串，第二个是要查找的字符串数组
		for (int i = 0; i < strs.length; i++) {
			if (s.contains(strs[i])) {
				return true;// 查找到了就返回真，不在继续查询
			}
		}
		return false;// 没找到返回false
	}

	/**
	 * 判断一个数组中是否包含某个字符串
	 * @param strs
	 * @param s
	 * @return
	 */
	public static boolean isHave(String[] strs, String s) {
		//此方法有两个参数，第一个是要查找的字符串，第二个是要查找的字符串数组
		for (int i = 0; i < strs.length; i++) {
			if (strs[i].contains(s)) {
				return true;// 查找到了就返回真，不在继续查询
			}
		}
		return false;// 没找到返回false
	}

	/**
	 * 判断字符是否是中文
	 *
	 * @param c
	 *            字符
	 * @return 是否是中文
	 */
	public static boolean isChinese(char c) {
		Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
		if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
				|| ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
				|| ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
				|| ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
			return true;
		}
		return false;
	}

	/**
	 * 驼峰转换下划线
	 * 例如：he4lloWorld->he4llo_word
	 * @param property
	 * @return
	 */
	public static String propertyToColumn(String property){
		if (property == null || property.isEmpty()){
			return "";
		}
		StringBuilder column = new StringBuilder();
		column.append(property.substring(0,1).toLowerCase());
		for (int i = 1; i < property.length(); i++) {
			String s = property.substring(i, i + 1);
			// 在小写字母前添加下划线
			if(!Character.isDigit(s.charAt(0)) && s.equals(s.toUpperCase())){
				column.append("_");
			}
			// 其他字符直接转成小写
			column.append(s.toLowerCase());
		}

		return column.toString();
	}

	/**
	 * 下划线转驼峰
	 * @param name
	 * @return
	 */
	public static String camelName(String name) {
		StringBuilder result = new StringBuilder();
		// 快速检查
		if (isEmpty(name)) {
			// 没必要转换
			return "";
		} else if (!name.contains("_")) {
			// 不含下划线，仅将首字母小写
			return name.substring(0, 1).toLowerCase() + name.substring(1);
		}
		// 用下划线将原始字符串分割
		String camels[] = name.split("_");
		for (String camel :  camels) {
			// 跳过原始字符串中开头、结尾的下换线或双重下划线
			if (camel.isEmpty()) {
				continue;
			}
			// 处理真正的驼峰片段
			if (result.length() == 0) {
				// 第一个驼峰片段，全部字母都小写
				result.append(camel.toLowerCase());
			} else {
				// 其他的驼峰片段，首字母大写
				result.append(camel.substring(0, 1).toUpperCase());
				result.append(camel.substring(1).toLowerCase());
			}
		}
		return result.toString();
	}

	/**
	 * 带有默认值的为空判断
	 * 
	 * @param text
	 * @param def
	 * @return
	 */
	public static String defIfEmpty(String text, String def) {
		return isEmpty(text) ? def : text;
	}

	/**
	 * 得到32位的uuid
	 * 
	 * @return
	 */
	public static String get32UUID() {
		return UUID.randomUUID().toString().toLowerCase().trim().replaceAll("-", "");
	}
}
