package cn.com.minstone.eBusiness.util;

import java.util.Hashtable;
import java.util.Map;
import java.util.regex.Pattern;

import org.json.JSONObject;

import cn.com.minstone.jfinal.ext.util.Logger;

public class StringUtil {

	/**
	 * 判断字符串是否为空，包括null 和 空字符串 ""
	 * @param str 输入的字符串
	 * @return true 如果字符串为空，false字符串不为空
	 * */
	public static boolean isBlank(String str){
		if(str == null || str.trim().equals("") || str.trim().equalsIgnoreCase("null")) {
			return true;
		}
		return false;
	}
	
	/**
	 * 消息推送获取json字符串
	 * @param map
	 * @return
	 */
	public static String getJson(Map<String, String> map) {
//		Hashtable<String, String> map = new Hashtable<String, String>();
//		map.put("newsType", newsType);
//		map.put("content", content);
		String strJson = "";
		try {
			JSONObject json = new JSONObject(map);
			strJson = json.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return strJson;
	}

	/**
	 * 消息推送获取json字符串
	 * @param newsType
	 * @param content
	 * @return
	 */
	public static String getJson(String newsType, String content) {
		Hashtable<String, String> map = new Hashtable<String, String>();
		map.put("newsType", newsType);
		map.put("content", content);
		String strJson = "";
		try {
			JSONObject json = new JSONObject(map);
			strJson = json.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return strJson;
	}

	/**
	 * 简单验证手机号码
	 * @param phoneNum
	 * @return
	 */
	public static boolean valiatePhone(String phoneNum) {
		String PHONE_REGEX = "^(\\+?\\d{2}-?)?(1[0-9])\\d{9}$";
		
		return Pattern.compile(PHONE_REGEX).matcher(phoneNum).matches();
	}
	
	/**
	 * 验证邮箱地址
	 * @param email
	 * @return
	 */
	public static boolean valiateEmail(String email) {
		String EMAIL_REGEX = "^[a-z0-9!#$%&'*+/=?^_`{|}~-]+" +
	                    "(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+" +
	                    "[a-z0-9](?:[a-z0-9-]*[a-z0-9])?$";
		
		return Pattern.compile(EMAIL_REGEX).matcher(email.toLowerCase()).matches();
	}
	
	/**
	 * 将字符串尝试转换成int类型
	 * @param str 输入的字符串
	 * @return int
	 * */
	public static int parseIndex(String str){
		int result = 1;
		try{
			result = Integer.parseInt(str);
			if (result <= 0) {
				result = 1;
			}
		}catch(Exception e){
			logger.error("int类型转换出错，pageIndex不为int类型");
		}
		return result;
	}
	
	public static Logger logger = new Logger(StringUtil.class);
}
