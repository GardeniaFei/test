package cn.com.minstone.eBusiness.service;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.jfinal.plugin.activerecord.Db;

import cn.com.minstone.eBusiness.util.TestUtil;
import cn.com.minstone.eBusiness.util.TimeUtil;

public class ItemServiceTest {
	
	ItemService itemService;

	@Before
	public void setUp() throws Exception {
		TestUtil.init();
		itemService = new ItemService();
	}

	@Test
	public void test() {
//		for(int i = 1 ; i < 100000; i++){
//			itemService.addItem("4600", "测试事项", "5", "", "", "", "", "", "", "", "", "", "", "区规划局（合并）");
//		}
		
		String sql = "insert into EB_ITEM(FLOW_ANNEX, " +
				"MATERIAL_ANNEX, " +
				"ITEM_TYPE, " +
				"ITEM_ID, " +
				"GIST_LAW, " +
				"REFRESH_TIME, " +
				"DEPART_NAME, " +
				"DEPART_ID, " +
				"TRANSACTION_WINDOW, " +
				"ITEM_QUESTIONS, " +
				"IS_DELET, " +
				"ITEM_NAME, " +
				"NEED_CONDITION, " +
				"TIME_LIMIT, " +
				"TRANSACTION_OBJECT, " +
				"ITEM_CHARGE, " +
				"ITEM_FLOW, " +
				"NEED_MATERIAL) values(?, ?, ?, iteminfo_seq.nextval, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		
		Object[][] objs = new Object[100000][17];
		for(int  i = 0 ; i < objs.length; i++){
			objs[i] = new Object[]{"",
					"", 
					"0", 
					"", 
					String.valueOf(System.currentTimeMillis()),
					"区规划局（合并）",  
					"4600", 
					"", 
					"", 
					"1",  
					"测试事项", 
					"", 
					"5", 
					"", 
					"", 
					"", 
					""};
		}
		
		Db.batch(sql, objs, objs.length);
		
	}
	
	@Test
	public void cacularTest() {
		long days = TimeUtil.calcWorkDaysByString("2015-09-08", "2015-09-23", "yyyy-MM-dd");
		
//		int num = (int) (days / (24 * 3600000));
		System.out.println("相隔时间为：" + days / (24 * 3600000));
	}

}
