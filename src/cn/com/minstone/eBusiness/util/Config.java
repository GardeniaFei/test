package cn.com.minstone.eBusiness.util;

import com.jfinal.core.Controller;


public class Config {
	
	private static Config instance;
	
	public final static String ZWB = "zhengwuban";
	public final static String ZSJ = "cityzhengwuban";
	public final static String BASE_CONN = "baseConn";//数据库连接池
	public final static String VERSION = "version";//当前系统版本
	public final static String GOV_URL = "govUrl";//审批接口地址
	public final static String TITLE = "Title";//系统名称
	public final static String ADMIN_CODE = "ADMIN_ID";//管理员登录code
	public final static String GOV_USER = "govUser";//审批接口用户
	public final static String GOV_PWD = "govPwd";//审批接口用户密码
	public final static String APP_LOAD_URL = "appLoadUrl";//app应用下载地址
	public final static String DEFAULT_PWD = "userDefaultPwd";//用户账号默认密码
	
	//消息推送字段
	public final static String APP_ID = "appId";//客户端应用唯一ID
	public final static String APP_KEY = "appkey";//鉴定身份密钥
	public final static String MASTER = "master";//鉴权码
	
	public final static String DEBUG = "Debug";//是否为调试模式
	public final static String PUBLISH_CONN = "publishConn";//版本发布连接池
	
	public final static String ZWB_NAME = "zwb_name";//政务办名称
	public final static String ZSJ_NAME = "zsj_name";//招商局名称
	public final static String ZWB_NAME_CITY = "zwb_name_city";//市级政务办名称
	
	public final static String PRIN_USER="prin_user";//分中心登录用户密码
	public final static String PRIN_PWD="prin_pwd";
	
	public final static String JDBCURL = "jdbcUrl";
	public final static String JDBCPASSWORD = "jdbcPassword";
	public final static String INITALPOOLSIZE = "initialPoolSize";
	public final static String MAXPOOLSIZE = "maxPoolSize";
	public final static String MIXPOOLSIZE = "mixPoolSize";
	
	public static Config getInstance(){
		if(instance==null){
			instance = new Config();
		}
		return instance;
	}
	
	/**
	 * 图片保存的当地服务器
	 */
	public final static String IMG_URL = "imgUrl";
	
	/**
	 * 内网地址
	 */
	public final static String BASE_URL = "baseUrl";
	/**
	 * 获取管理员登录用户id
	 * @param ctrl
	 * @return
	 */
	public static String getSessionAdminID(Controller ctrl){
		return ctrl.getSessionAttr("ADMIN_ID");
	}
	
	/**
	 * 设置管理员id
	 * @param ctrl
	 * @param adminId
	 */
	public static void setSessionAdminID(Controller ctrl, String adminId){
		ctrl.setSessionAttr("ADMIN_ID", adminId);
	}
	
	/**
	 * 获取用户登录的userID
	 * @param ctrl
	 * @return
	 */
	public static String getSessionUserID(Controller ctrl){
		return ctrl.getSessionAttr("USER_ID");
	}
		
	/**
	 * 设置用户userID
	 * @param ctrl
	 * @param userId
	 */
	public static void setSessionUserID(Controller ctrl, String userId){
		ctrl.setSessionAttr("USER_ID", userId);
	}
	
	/**
	 * 获取用户登录的密码password
	 * @param ctrl
	 * @return
	 */
	public static String getSessionPwd(Controller ctrl){
		return ctrl.getSessionAttr("PASSWORD");
	}
		
	/**
	 * 设置用户密码password
	 * @param ctrl
	 * @param pwd
	 */
	public static void setSessionPWD(Controller ctrl, String pwd){
		ctrl.setSessionAttr("PASSWORD", pwd);
	}
	
	/**
	 * 获取用户登录的姓名userName
	 * @param ctrl
	 * @return
	 */
	public static String getSessionUserName(Controller ctrl){
		return ctrl.getSessionAttr("USER_NAME");
	}
		
	/**
	 * 设置用户姓名userName
	 * @param ctrl
	 * @param userName
	 */
	public static void setSessionUserName(Controller ctrl, String userName){
		ctrl.setSessionAttr("USER_NAME", userName);
	}
	
	/**
	 * 获取用户登录的账号userAccount
	 * @param ctrl
	 * @return
	 */
	public static String getSessionUserAccount(Controller ctrl){
		return ctrl.getSessionAttr("USER_ACCOUNT");
	}
		
	/**
	 * 设置用户账号userAccount
	 * @param ctrl
	 * @param userAccount
	 */
	public static void setSessionUserAccount(Controller ctrl, String userAccount){
		ctrl.setSessionAttr("USER_ACCOUNT", userAccount);
	}
	
	/**
	 * 获取用户登录的账号userPhoto
	 * @param ctrl
	 * @return
	 */
	public static String getSessionUserPhoto(Controller ctrl){
		return ctrl.getSessionAttr("USER_PHOTO");
	}
		
	/**
	 * 设置用户账号userPhoto
	 * @param ctrl
	 * @param userPhoto
	 */
	public static void setSessionUserPhoto(Controller ctrl, String userPhoto){
		ctrl.setSessionAttr("USER_PHOTO", userPhoto);
	}
	
	/**
	 * 获取用户登录的账号userTel
	 * @param ctrl
	 * @return
	 */
	public static String getSessionUserTel(Controller ctrl){
		return ctrl.getSessionAttr("USER_TEL");
	}
		
	/**
	 * 设置用户账号userTel
	 * @param ctrl
	 * @param userTel
	 */
	public static void setSessionUserTel(Controller ctrl, String userTel){
		ctrl.setSessionAttr("USER_TEL", userTel);
	}
	
	/**
	 * 获取session中sid
	 * @param ctrl
	 * @return
	 */
	public static String getSessionSid(Controller ctrl) {
		return ctrl.getSessionAttr("SID");
	}
	
	/**
	 * 设置session sid
	 * @param ctrl
	 * @param sid
	 */
	public static void setSessionSid(Controller ctrl, String sid) {
		ctrl.setSessionAttr("SID", sid);
	}
	
	/**
	 * 获取session中select_auth
	 * @param ctrl
	 * @return
	 */
	public static String getSessionSelect_auth(Controller ctrl) {
		return ctrl.getSessionAttr("select_auth");
	}
	
	/**
	 * 设置session select_auth
	 * @param ctrl
	 * @param select_auth用户选择的登录角色权限
	 */
	public static void setSessionSelect_auth(Controller ctrl, String select_auth) {
		ctrl.setSessionAttr("select_auth", select_auth);
	}
	
	/**
	 * 获取session中sid
	 * @param ctrl
	 * @return
	 */
	public static String getSessionRoleType(Controller ctrl) {
		return ctrl.getSessionAttr("ROLE_TYPE");
	}
	
	/**
	 * 设置session sid
	 * @param ctrl
	 * @param roleType
	 */
	public static void setSessionRoleType(Controller ctrl, String roleType) {
		ctrl.setSessionAttr("ROLE_TYPE", roleType);
	}
	
	/**
	 * 获取session中是否登录标识
	 * @param ctrl
	 * @return
	 */
	public static String getSessionIsLogin(Controller ctrl) {
		return ctrl.getSessionAttr("isLogin");
	}
	
	/**
	 * 设置session中是否登录标识
	 * @param ctrl
	 * @param isLogin
	 */
	public static void setSessionIsLogin(Controller ctrl, String isLogin) {
		ctrl.setSessionAttr("isLogin", isLogin);
	}
	
	/**
	 * 获取政务办部门id
	 * @param ctrl
	 * @return
	 */
	public static String getSessionZWBID(Controller ctrl){
		return ctrl.getSessionAttr("ZWB_ID");
	}
	
	/**
	 * 设置政务办部门id
	 * @param ctrl
	 * @param ZWBID
	 */
	public static void setSessionZWBID(Controller ctrl, String ZWBID){
		ctrl.setSessionAttr("ZWB_ID", ZWBID);
	}
}
	
