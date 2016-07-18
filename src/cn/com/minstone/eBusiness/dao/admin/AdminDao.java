package cn.com.minstone.eBusiness.dao.admin;


import cn.com.minstone.eBusiness.model.BaseAdmin;


public class AdminDao {
	
	public final static AdminDao dao = new AdminDao();
	
	/**
	 * 管理员表认证
	 */
	public BaseAdmin findAdmin(String adminCode) {
		
		BaseAdmin  admin = BaseAdmin.dao.findFirst("select * from baserole_admin where C_USER_CODE = '" + adminCode +"'");
		return admin;
	}
}
