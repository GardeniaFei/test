package cn.com.minstone.eBusiness.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class FileHelper {

	/**
	 * 将文件转换成Byte数组
	 * @throws IOException 
	 * */
	public static byte[] toBytes(File file) throws IOException{
		byte[] bs = new byte[(int) file.length()];
		FileInputStream fis = new FileInputStream(file);
		fis.read(bs);
		fis.close();
		return bs;
	}
	
}
