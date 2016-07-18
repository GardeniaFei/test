package cn.com.minstone.eBusiness.util;

public class NullUtil {
	
	public static int ifNull(String str, int defValue){
		int result = defValue;
		if(str != null && str.trim().length() > 0 && !str.equalsIgnoreCase("null")){
			try{
				result = Integer.parseInt(str);
			}catch(NumberFormatException e){
				e.printStackTrace();
			}
		}
		
		return result;
	}
	
	public static String ifNull(String str, String defValue){
		String result = defValue;
		if(str != null && str.trim().length() > 0 && !str.equals("null")){
			result = str;
		}
		return result;
	}
	
	public static boolean isEmpty(String str){
		return str == null || str.trim().equalsIgnoreCase("null") || str.equals("");
	}

}
