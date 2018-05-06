package com.xtzhangbinbin.jpq.utils;

/**
 * 数据敏感信息屏蔽相关帮助类
 * 
 * @author LiuKun
 * 
 */
public class DataConcealUtil {

	/**
	 * 脱敏占位符最大个数
	 */
	public static int SIZE = 4;
	/**
	 * 替换敏感信息符号
	 */
	public static String SYMBOL = "*";

	/**
	 * 隐藏敏感字符处理
	 * 
	 * @param value
	 * @return
	 */
	public static String toConceal(String value) {
		if (null == value || "".equals(value)) {
			return value;
		}
		int l = value.length();
		int a = l / 2;
		int b = a - 1;
		int c = l % 2;
		StringBuffer sb = new StringBuffer(l);
		if (l <= 2) {
			if (c == 1) {
				return SYMBOL;
			}
			// sb.append(SYMBOL);
			// sb.append(str.charAt(l - 1));
			sb.append(value.charAt(0));
			sb.append(SYMBOL);
		} else {
			if (b <= 0) {
				sb.append(value.substring(0, 1));
				sb.append(SYMBOL);
				sb.append(value.substring(l - 1, l));
			} else if (b >= SIZE / 2 && SIZE + 1 != l) {
				int e = (l - SIZE) / 2;
				sb.append(value.substring(0, e));
				for (int i = 0; i < SIZE; i++)
					sb.append(SYMBOL);
				if ((c == 0 && SIZE % 2 == 0) || (c != 0 && SIZE % 2 != 0))
					sb.append(value.substring(l - e, l));
				else
					sb.append(value.substring(l - (e + 1), l));
			} else {
				int d = l - 2;
				sb.append(value.substring(0, 1));
				for (int i = 0; i < d; i++)
					sb.append(SYMBOL);
				sb.append(value.substring(l - 1, l));
			}
		}
		return sb.toString();
	}

	public static void main(String[] args) {
		String[] strs = { null, "", "1", "12", "123", "1234", "12345",
				"123456", "1234567", "12345678", "123456789", "1234567890",
				"12345678901", "你", "你好", "你好吗", "你们好吗", "你们都好吗", "你们都很好吗",
				"你们都是很好吗", "你们会都是很好吗", "你们真的都是很好吗", "你们真的都是很好吗?", "姓二名", "姓名",
				"雷刘天下" };
		long begin = System.currentTimeMillis();
		for (int a = 0; a < 10000000; a++) {
			for (String str : strs) {
				if (a == 0) {
					System.out.println(str);
					System.out.println(toConceal(str));
					System.out.println("------------------------------");
				} else {
					toConceal(str);
				}
			}
		}
		long end = System.currentTimeMillis();
		System.out.println("耗时(ms):" + (end - begin));
		// 41873ms / (10000000 * 23) ≈ 1.82 ms
	}
}