package cn.com.minstone.eBusiness.service;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import cn.com.minstone.eBusiness.util.MD5Util;
import cn.com.minstone.eBusiness.util.TimeUtil;

public class testLogin {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test() {
		String signDate = TimeUtil.getCurrentTime();
		
		System.out.println(signDate);
		System.out.println(MD5Util.strToMD5L32(signDate));
	}

}
