package cn.com.minstone.eBusiness.service;

import org.junit.Before;
import org.junit.Test;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

import cn.com.minstone.eBusiness.model.EbTaskDistribute;
import cn.com.minstone.eBusiness.service.inter.TaskInterService;
import cn.com.minstone.eBusiness.util.TestUtil;
import junit.framework.TestCase;

public class TaskInterServiceTest extends TestCase {

	TaskInterService service;

	@Before
	public void setUp() throws Exception {
		TestUtil.init();
		service = new TaskInterService();
	}

	@Test
	public void testDistributeTask() {
		//fail("Not yet implemented");
		  System.out.print("检查进入保存错误");
		  EbTaskDistribute taskDistri =  new EbTaskDistribute();
		  taskDistri.set("DISTR_ID","DISTR_SEQ.nextval");
		  taskDistri.set("task_id", "100");
		  taskDistri.set("item_id", "102");
		  taskDistri.set("depart_id", "102");
		  taskDistri.set("sign_status","0");
		  taskDistri.set("user_id", "100");
		  taskDistri.set("status", "1");
		  taskDistri.set("distrib_time", "1443423837330");
		  taskDistri.set("transaction_status","0");
		  taskDistri.set("SIGN_USER_ID","21");
		  
		  boolean success = false;
		  if(taskDistri.save()){
			  success = true;
		  }
		  
//		  int success = Db.update("insert into EB_TASK_DISTRIBUTE(TRANSACTION_STATUS, DEPART_ID, " +
//		    "SIGN_STATUS, USER_ID, DISTRIB_TIME, DISTR_ID, SIGN_USER_ID, ITEM_ID, TASK_ID, STATUS) values(0, 102, 0, 100, 1443423837330," +
//		    "DISTR_SEQ.nextval, 21, 102, 100, 1)");
		  System.out.print("检查保存错误");
//		  assertEquals(success, 1);
		  assertEquals(success, true);
	}

}
