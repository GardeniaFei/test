package cn.com.minstone.eBusiness.dao.admin;



import cn.com.minstone.eBusiness.model.BaseUser;


public class UserDao {
	

	public final static UserDao dao = new UserDao();
	
	/**
	 * 管理员账号查找
	 */
	public BaseUser findUser(String adminName) {
		BaseUser user = BaseUser.dao.use("base").findFirst("select * from base_user where USER_CODE = '" + adminName +"'");
		return user;
	}
	
	/**
	 * 管理员密码修改
	 */
	public boolean resetPwd(String adminCode,String pwd) {
		BaseUser user = findUser(adminCode);
		user.setUSER_PWD(pwd);
		boolean result = user.update();
		return result;
	}
	
	/**
	 * 管理员密码确认检查
	 */
	public boolean checkPwd(String adminCode,String pwd){
		BaseUser user = findUser(adminCode);
		
		boolean result = user.getUSER_PWD().equals(pwd);
		
		return result;
	}
}
