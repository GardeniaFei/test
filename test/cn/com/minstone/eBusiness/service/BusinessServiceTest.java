package cn.com.minstone.eBusiness.service;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import cn.com.minstone.eBusiness.model.EbAttention;
import cn.com.minstone.eBusiness.model.EbBusiness;
import cn.com.minstone.eBusiness.util.TestUtil;

public class BusinessServiceTest {
	
	BusinessService service;

	@Before
	public void setUp() throws Exception {
		TestUtil.init();
		service = new BusinessService();
	}

	/**
	 * 获取所有的企业
	 */
	@Test
	public void testGetAllBussInfo() {
		List<EbBusiness> infos = service.getAllBussInfoList();
		System.err.println(infos.size());
		assertTrue(infos.size() != 0);
	}
	
	@Test
	public void testTable() {
		EbAttention.dao.find("select * from eb_attention");
	}
	
	@Test
	public void findByName() {
		System.out.println("findByName");
		List<EbBusiness> list = service.findByBussName("联想");
		System.out.println("list = " + list.toString());
	}
	
	/**
	 * 添加企业
	 */
	@Test
	public void testAddBussInfo() {
		int success = service.addBussInfo("立白集团", "立白集团洗衣液", "李四", "13574552336", "123");
		
		assertEquals(success, 1);
		
		List<EbBusiness> list = service.findByBussName("立白集团");
		
		assertTrue(list.size() != 0);
	}
}
