package cn.com.minstone.eBusiness.service;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.jfinal.plugin.activerecord.Db;

import cn.com.minstone.eBusiness.model.EbTaskList;
import cn.com.minstone.eBusiness.model.EbUserInfo;
import cn.com.minstone.eBusiness.model.RoleType;
import cn.com.minstone.eBusiness.util.GovNetUtil;
import cn.com.minstone.eBusiness.util.TestUtil;

public class UserServiceTest {
	
	UserService service;

	@Before
	public void setUp() throws Exception {
		TestUtil.init();
		service = new UserService(null);
	}

	@Test
	public void testUserService() {
//		EbTaskList taskList =  new EbTaskList();
//		taskList.set("T_ID","'task_list_seq.nextval'");
//		taskList.set("TYPE_ID", "101");
//		taskList.set("ITEM_ID", "24");
//		
//		int success = Db.update("insert into Eb_task_list (T_ID, TYPE_ID, ITEM_ID) values(task_list_seq.nextval,101,24)");
////		
//		assertEquals(success, 1);
//		assertTrue(taskList.save());
		GovNetUtil.getDeptInfo("32");
	}

	@Test
	public void testLogin() {
		fail("Not yet implemented");
	}

	@Test
	public void testHasLogin() {
		fail("Not yet implemented");
	}

	@Test
	public void testHasLoginWithSid() {
		fail("Not yet implemented");
	}

	@Test
	public void testFilterUsers() {
		fail("Not yet implemented");
	}

	@Test
	public void testDeleteUserById() {
		fail("Not yet implemented");
	}

	@Test
	public void testUpdateUser() {
		fail("Not yet implemented");
	}

	@Test
	public void testAddUserInfo() {
//		int success = service.addUserInfo("123456", "11", "ming", RoleType.BUSINESS, "1213", "http://wwww");
		
		
//		assertEquals(success, 1);
		
		
//		List<EbUserInfo> users = EbUserInfo.dao.find("select * from eb_user_info where USER_ACCOUNT = ?", "123456");

		List<EbUserInfo> users = EbUserInfo.dao.find("select * from eb_user_info");
		if (users != null) {
			System.out.println(users.size());
		}
		assertTrue(users.size() != 0);
		
		
		
	}
	
	@Test
	public void testAddDeptInfo() {
		
	}

}
