package cn.com.minstone.eBusiness.util;

public class LogUtil {

	public static boolean flag = true;
	
	public static void log(String content) {
		if (flag) {
			System.out.println(content);
		}
	}
	
	public static void tag(String content) {
		System.out.println(content);
	}
}
