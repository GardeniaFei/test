package cn.com.minstone.eBusiness.ctrl;


import cn.com.minstone.eBusiness.AdminLoginInter;
import cn.com.minstone.eBusiness.dao.admin.AdminDao;
import cn.com.minstone.eBusiness.dao.admin.UserDao;
import cn.com.minstone.eBusiness.model.BaseAdmin;
import cn.com.minstone.eBusiness.model.BaseUser;
import cn.com.minstone.eBusiness.util.Config;
import cn.com.minstone.eBusiness.util.PathUtil;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;

public class AdminCtrl extends Controller {

	/**
	 * 进入管理员登录页面
	 * */
	public void loginShow(){
		render(PathUtil.getIndexPath("login.html"));
	}
	
	/**
	 * 登录并检查是否是管理员
	 * @throws Exception 
	 * */
	public void login() throws Exception{
		String adminName = getPara("admin_name");
		String adminPwd = getPara("admin_pwd");
		
		BaseUser user = UserDao.dao.findUser(adminName);
		if(user!=null){
			String userPwd = user.getUSER_PWD();
			if(userPwd!=null){
			  if(userPwd.equals(adminPwd)){
				//判断该登录用户是否是管理员
				boolean isAdmin = false;
				BaseAdmin  admin = AdminDao.dao.findAdmin(adminName);
				if(admin==null){
					setAttr("success", false);
					setAttr("message", "该用户没有权限！");
				}else{
					if(admin.getC_USER_CODE()!=null){
						 String i_adm = admin.getI_USER_ADM()+"";
						 isAdmin = i_adm.equals("1");
					}else{
						 isAdmin = false;
					}
					if(isAdmin){
						Config.setSessionAdminID(this, adminName);
						setAttr("success", true);
					}else{
						setAttr("success", false);
						setAttr("message", "该用户没有权限！");
					}
				}
			}else{
				setAttr("success", false);
				setAttr("message", "密码错误！ ");
			}
		  }else{
				setAttr("success", false);
				setAttr("message", "密码错误！ ");
			}
		}else{
			setAttr("success", false);
			setAttr("message", "该用户不存在！");
		}
		renderJson();
	}
	
	/**
	 * 管理员修改密码页面显示
	 */
	@Before(AdminLoginInter.class)
	public void resetPwdShow(){
		render(PathUtil.getIndexPath("resetAdminPwd.html"));
	}
	
	/**
	 * 管理员修改密码
	 */
	public void resetAdminPwd(){
		String adminPwd = getPara("pwd");
		String adminCode = Config.getSessionAdminID(this);
		
		boolean result = UserDao.dao.resetPwd(adminCode, adminPwd);
		
		if(result){
			loginShow();
		}else{
			this.setAttr("message", "修改失败，请重试！");
			resetPwdShow();
		}
		
	}
	
	/**
	 * 管理员旧密码确认
	 */
	public void CheckOldPwd(){
		String adminOldPwd = getPara("oldPwd");
		String adminCode = Config.getSessionAdminID(this);
		
		boolean result = UserDao.dao.checkPwd(adminCode, adminOldPwd);
		
		if(result){
			this.setAttr("message", "1");
		}else{
			this.setAttr("message", "0");
		}
		renderJson();
	}
	
	/**
	 * 系统退出
	 */
	@Before(AdminLoginInter.class)
	public void adminOff(){
		//移除登录session
		removeSessionAttr(Config.ADMIN_CODE);
		loginShow();
	}
}
