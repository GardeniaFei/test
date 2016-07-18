package cn.com.minstone.eBusiness.util;

public class PathUtil {
	
	public static int MIN_PAGE_SIZE = 10;
	
	public static int SHOW_PAGE_SIZE = 5;

	private final static String INDEX_PATH = "/WEB-INF/jsp";
	
	public static String getIndexPath(String fileName){
		return INDEX_PATH + "/" + fileName;
	}
	
	public static String getShowPath(String fileName){
		return INDEX_PATH + "/show/" + fileName;
	}
	
}