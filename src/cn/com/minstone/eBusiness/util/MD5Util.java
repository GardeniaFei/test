package cn.com.minstone.eBusiness.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Pattern;

public class MD5Util {
	
//	public static void main(String[] args) {
//		strToMD5L32();
//	}
	/**
	 * 判断字符串是否为数值类型
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		return pattern.matcher(str).matches();
	}

	/**
	 * 根据服务器端给定的secret值计算32位MD5值
	 * @return
	 */
	public static String strToMD5L32(String signDate) {
		String secret = MCubeAppConfig.getInstance().getGovPwd();	//密钥，由服务器端提供
//		String secret = "e667d29d50b7c7139cd0ace015b190fa";
//		String secret = "15d0ce7b0a2096e1ca171521898f1682";//现场
		
		String result = null;
		if(secret == null || secret.equals("") || "null".equalsIgnoreCase(secret)) {
			return result;
		}
		
		secret = signDate + secret;
		
		LogUtil.log("MD5Util:strToMD5L32(String); 输出加了日期的secret为：" + secret);
		
		try {
			//首先进行实例化和初始化
			MessageDigest md = MessageDigest.getInstance("MD5");
			//得到一个操作系统默认的字节编码格式的字节数组
			byte[] bts = secret.getBytes();
			//对得到的字节数组进行处理
			md.update(bts);
			//进行哈希计算并返回结果
			byte[] btResult = md.digest();
			//进行哈希计算后得到的数据的长度
			StringBuffer sb = new StringBuffer();
			for (byte b : btResult) {
				int bt = b&0xff;
				if(bt < 16) {
					sb.append(0);
				}
				sb.append(Integer.toHexString(bt));
			}
			result = sb.toString();
			
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
		LogUtil.log("MD5Util:strToMD5L32(String); 通过加密后的MD5值是：" + result);
		return result;
	}
	
	
}
