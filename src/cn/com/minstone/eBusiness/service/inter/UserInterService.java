package cn.com.minstone.eBusiness.service.inter;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;

import cn.com.minstone.eBusiness.dao.UserDao;
import cn.com.minstone.eBusiness.dao.inter.UserInterDao;
import cn.com.minstone.eBusiness.model.EbUserInfo;
import cn.com.minstone.eBusiness.model.RoleType;
import cn.com.minstone.eBusiness.model.SmsTbl;
import cn.com.minstone.eBusiness.service.BaseService;
import cn.com.minstone.eBusiness.util.Config;
import cn.com.minstone.eBusiness.util.GovNetUtil;
import cn.com.minstone.eBusiness.util.MCubeAppConfig;
import cn.com.minstone.eBusiness.util.TimeUtil;
import cn.com.minstone.jfinal.ext.util.Logger;

public class UserInterService extends BaseService {

	private Controller ctrl;
	private UserInterDao dao;
	
	private static final Logger logger = new Logger(UserInterService.class);

	public UserInterService(Controller ctrl) {
		this.ctrl = ctrl;
		this.dao = new UserInterDao();
	}
	
	/**
	 * 根据userCode获取userInfo
	 * @param userCode
	 * @return
	 */
	public EbUserInfo getUserByCR(String userCode) {
		EbUserInfo userInfo = dao.findByCR(userCode);
		return userInfo;
	}
	
	/**
	 * 根据userCode获取userInfo
	 * @param userCode
	 * @return
	 */
	public EbUserInfo getLoginByCR(String userCode) {
		EbUserInfo userInfo = dao.findLoginByCR(userCode);
		return userInfo;
	}
	
	/**
	 * 根据userCode获取userInfo
	 * @param userCode
	 * @return
	 */
	public EbUserInfo getUserByCode(String userCode) {
		EbUserInfo userInfo = dao.findByCode(userCode);
		return userInfo;
	}
	
	/**
	 * 根据userCode和departId获取userInfo
	 * @param userCode
	 * @param departId
	 * @return
	 */
	public EbUserInfo getUserByCD(String userCode, String departId) {
		EbUserInfo userInfo = dao.findByCD(userCode, departId);
		return userInfo;
	}
	
	/**
	 * 根据userCode和departId获取审批接口的userInfo
	 * @param userCode
	 * @param departId
	 * @return
	 */
	public EbUserInfo getUserByCDQ(String userCode, String departId) {
		EbUserInfo userInfo = GovNetUtil.getUserByCode(userCode);//先获取了用户的基本信息
		if(userInfo.getIsMDept().intValue() == 1){
			//IS_MDEPT 是否多角色 1 是、0 否
/*************************** 处理办事员用户多角色问题 *******************************/
			String departName = userInfo.getDepartName();//部门名称
			String myDepatName = userInfo.getMyDepartName();//
			/**************** 如果部门名称中含有字符串“,”，则说明当前用户是属于多角色用户 *****************/
			if((departName != null && departName.contains(",")) || (myDepatName != null)) {
				String[] _IDs = userInfo.getMyDepartId().split(",");
				String[] _Names = userInfo.getMyDepartName().split(",");
				for (int i = 0; i < _IDs.length; i++) {
					String dpt_ID = _IDs[i];
					if(dpt_ID.equals(departId)){
						//判断用户角色
						String dName = _Names[i].replace("\n", "");
						userInfo.setDepartName(dName);
						userInfo.setDepartId(new BigDecimal(dpt_ID));
						break;//如果是当前部门则跳出循环
						//6 VIP服务专员管理员+单位首席权限、7 VIP服务专员+单位首席权限
					}
				}
			}
/*****************************************************************************/
		}
		userInfo.set("USER_ID", "userinfo_seq.nextval");
		userInfo.setIsDelet(new BigDecimal(1));
		userInfo.setPassword("11");//先暂时默认为11
		if(dao.addUserInfo(userInfo)){
			return userInfo;
		}else{
			return null;
		}
	}
	
	/**
	 * 根据departId获取userInfo并去重
	 * @param userCode
	 * @param departId
	 * @return
	 */
	public List<EbUserInfo> getUserByD(String departId) {
		List<EbUserInfo> userInfo = dao.findByD(departId);
		return userInfo;
	}

	/**
	 * 根据userid获取userInfo
	 * @param userCode
	 * @return
	 */
	public EbUserInfo getUserByUId(String userId) {
		EbUserInfo userInfo = dao.findByUserId(userId);
		return userInfo;
	}
	
	/**
	 * @return 登录结果
	 *         <ul>
	 *         <li>-1 用户不存在</li>
	 *         <li>0 密码错误</li>
	 *         <li>1 登录成功</li>
	 *         <li>2内部服务器错误</li>
	 *         </ul>
	 * */
	public int login(String userCode, String password) {
		EbUserInfo user = dao.findByUserCode(userCode);
		if (user == null) {
			return -1;
		} else if (!user.getPassword().equals(password)) {
			return 0;
		} else {
			/**
			 * 用户名密码验证成功 1.生成sid 2.设置登录状态
			 */
			// TODO 产生sid
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
			String strDate = format.format(System.currentTimeMillis());
			String sid = "EASYBUSINESSUSER" + strDate;

			user.setSid(sid);
			boolean success = dao.updateUserInfo(user);
			if (!success) {
				return 2;
			}

			ctrl.setSessionAttr("isLogin", "1");

			return 1;
		}
	}

	/**
	 * 根据session判断是否登录、主要用户网页端
	 * 
	 * @return
	 */
	public boolean hasLogin() {
		return ctrl.getSessionAttr("isLogin");
	}

	/**
	 * 根据sid判断是否登录、主要用于app端
	 * 
	 * @param sid
	 * @return
	 */
	public boolean hasLoginWithSid(String sid) {
		String obj = ctrl.getSessionAttr("isLogin");
		if (obj == null) {
			EbUserInfo info = new UserDao().findBySid(sid);
			if (info == null) {
				return false;
			} else {
				ctrl.setSessionAttr("isLogin", "1");
			}
		}
		return true;
	}
	
	/**
	 * 获取所有用户
	 * @param page 页数
	 * @return Page<EbUserInfo>
	 */
	public Page<EbUserInfo> getAllUser(int page) {
		return dao.findAllUsers(page);
	}
	
	/**
	 * 获取所有用户
	 * @return List<EbUserInfo>
	 */
	public List<EbUserInfo> getAllUserList() {
		return dao.findAllUsersList();
	}

	/**
	 * 根据用户权限获取用户列表
	 * @param authority
	 * @return List<EbUserInfo>
	 */
	public List<EbUserInfo> getAllUserByAuth(String authority) {
		return dao.findAllUserByAuth(authority);
	}
	
	/**
	 * 筛选用户
	 * 
	 * @param roleType
	 * @param departId
	 * @param page
	 * @return Page
	 */
	public Page<EbUserInfo> filterUsers(String roleType, String departId, int page) {
		return dao.findByRD(roleType, departId, page);
	}
	
	/**
	 * 筛选用户
	 * 
	 * @param roleType
	 * @param departId
	 * @return list
	 */
	public List<EbUserInfo> filterUsersList(String roleType, String departId) {
		return dao.findByRDList(roleType, departId);
	}
	
	/**
	 * 根据企业id获取企业用户
	 * @param bussId
	 * @return
	 */
	public List<EbUserInfo> findByBussid(String bussId){
	   return dao.findByBussid(bussId);
	}
	/**
	 * 检查用户账号是否重复
	 * @param userCode
	 * @return EbUserInfo
	 */
	public EbUserInfo findByCode(String userCode){
		return dao.findByCode(userCode);
	}
	
	/**
	 * 根据用户账号和区市级用户筛选用户
	 * @param userCode
	 * @param roleType
	 * @return EbUserInfo
	 */
	public EbUserInfo findByCode(String userCode, int roleType){
		return dao.findByCode(userCode, roleType);
	}
	
	/**
	 * 根据用户账号和企业id筛选企业用户
	 * @param userCode
	 * @param businessId
	 * @return EbUserInfo
	 */
	public EbUserInfo findByCoBID(String userCode, String businessId){
		return dao.findByCoBID(userCode, businessId);
	}

	/**
	 * 根据用户id删除用户
	 * 
	 * @param userId
	 * @return
	 */
	public boolean deleteUserById(String userId) {
		if(userId == null || "".equals(userId) || "null".equalsIgnoreCase(userId)) {
			return false;
		}
		//假删除
		return dao.findByUserId(userId).set("is_delet", "0").update();
		
		//真删除
//		EbUserInfo userInfo = dao.findByUserId(userId);
//		return dao.deleteUserInfo(userInfo);
	}
	
	/**
	 * 根据用户id获取用户
	 * @param userId
	 * @return
	 */
	public EbUserInfo findById(String userId) {
		if(userId == null || "".equals(userId) || "null".equalsIgnoreCase(userId)) {
			return null;
		}
		return dao.findByUserId(userId);
	}
	
	/**
	 * 根据用户id修改用户密码
	 * @param userId
	 * @param pwd
	 * @return boolean
	 */
	public boolean resetPwdById(String userId,String pwd){
		return dao.findByUserId(userId).set("PASSWORD", pwd).update();
	}
	
	/**
	 * 根据用户id修改用户姓名
	 * @param userId
	 * @param userName
	 * @return boolean
	 */
	public boolean resetNameById(String userId,String userName){
		return dao.findByUserId(userId).set("USER_NAME", userName).update();
	}

	/**
	 * 修改用户信息
	 * @param userId	用户id
	 * @param businessId	所属企业
	 * @param userName	用户姓名
	 * @param password	密码
	 * @param photoUrl	用户头像
	 * @return 1 成功	0 失败  2 用户名为空 3 密码为空 
	 */
	public int updateUser(String userId,String roleType, String businessId, String userName, String password, String photoUrl) {
		
		EbUserInfo userInfo = dao.findByUserId(userId);
		if(Integer.parseInt(roleType) == RoleType.BUSINESS) {
			if(businessId == null || "".equals(businessId) || "null".equalsIgnoreCase(businessId)) {
				return -1;
			}else if(!userInfo.getBusinessId().toString().equalsIgnoreCase(businessId)) {
				userInfo.set("BUSINESS_ID", businessId);
			}
		}
		
		if(userName == null || "".equals(userName) || "null".equalsIgnoreCase(userName)) {
			return -1;	//用户名为空
		}else if(!userInfo.getUserName().equals(userName)) {
			userInfo.setUserName(userName);
		}
		
		if(password == null || "".equals(password) || "null".equalsIgnoreCase(password)) {
			return -1;	//密码为空
		}else {
			userInfo.setPassword(password);
		}
		
		if(photoUrl != null && !"".equals(photoUrl) && !"null".equalsIgnoreCase(photoUrl)) {
			if(!photoUrl.equals(userInfo.getUserPhoto())) {
				userInfo.setUserPhoto(photoUrl);
			}
		}
		
		if(dao.updateUserInfo(userInfo)) {
			return 1;	//修改成功
		}else {
			return 0;	//修改失败
		}
	}

	/**
	 * 增加用户
	 * 
	 * @param userAccount
	 *            用户账号 必传
	 * @param password
	 *            密码 必传
	 * @param userName
	 *            用户姓名 必传
	 * @param roleType
	 *            用户角色 必传,  不能添加办事员，即0
	 *            {@link RoleType#BUSINESS}
	 *            {@link RoleType#LEADER}
	 * @param businessId
	 *            所属企业id
	 * @param photoUrl
	 *            用户头像
	 * @return <ul><li>1 成功 0 失败  -1 缺少参数 </li></ul>
	 */
	public int addUserInfo(String userAccount, String password,
			String userName, int roleType, String businessId,
			String photoUrl, String bussName, String userEmail) {

		EbUserInfo userInfo = new EbUserInfo();
		
		userInfo.set("USER_ID", "userinfo_seq.nextval"); // 用户id
		
		if (userAccount != null && !userAccount.equalsIgnoreCase("null") && !"".equals(userAccount)) {
			userInfo.set("USER_ACCOUNT", userAccount); // 账号
		} else {
			return -1;	//必填项
		}
		if (password != null && !"".equals(password) && !password.equalsIgnoreCase("null")) {
			userInfo.set("PASSWORD", password); // 密码
		} else {
			return -1;
		}
		if (userName != null && !userName.equalsIgnoreCase("null") && !"".equals(userName)) {
			userInfo.set("USER_NAME", userName); // 用户姓名
		} else {
			return -1;
		}
		if (roleType != RoleType.TRANSACTOR) {
			userInfo.set("ROLE_TYPE", roleType); // 用户角色
		} else {
			logger.error("不能添加 办事员用户，只能添加企业用户和领导用户");
			return -1;
		}
		if (photoUrl != null && !"".equals(photoUrl) && !photoUrl.equalsIgnoreCase("null")) {
			userInfo.set("USER_PHOTO", photoUrl);
		}
		if (businessId != null && !businessId.equalsIgnoreCase("null") && !"".equals(businessId) && roleType == RoleType.BUSINESS) {
			userInfo.set("BUSINESS_ID", businessId);
		}
		if (bussName != null && !"".equals(bussName) && !bussName.equalsIgnoreCase("null") && roleType == RoleType.BUSINESS) {
			userInfo.setBusinessName(bussName);//用户所属企业名称
		}
		userInfo.setUserEmail(userEmail);

		if(roleType != RoleType.TRANSACTOR && dao.addUserInfo(userInfo)) {
			return 1;	//增加成功
		} else {
			return 0;	//添加失败
		}
	}
	
	/**
	 * 
	 * @param contactPhone
	 * @param contactName
	 * @param password
	 * @return
	 */
	public int addSMSInfo(String contactPhone, String contactName, String password, String content) {

		SmsTbl smsTbl = new SmsTbl();
		
		smsTbl.set("SMS_SEQ", "sms_table_seq.nextval"); // 短信id
		
		if (contactPhone != null && !"".equals(contactPhone)) {
			smsTbl.set("MOBL_NMBR", contactPhone); // 电话号码
		} else {
			return -1;	//必填项
		}
		if (contactName != null && !"".equals(contactName)) {
			smsTbl.set("SEND_USER", contactName); // 发送用户
		} else {
			return -1;
		}
		
		java.sql.Date strDate = TimeUtil.getSqlDate(new java.util.Date(), "yyyy/MM/dd HH:mm:ss");
		
		smsTbl.set("SMS_MSG", content); // 短信信息
		
		smsTbl.set("SAVE_TIME", strDate);
		smsTbl.set("SEND_STAT", new BigDecimal(0));
		smsTbl.set("SEND_NMBR", new BigDecimal(0));
		smsTbl.set("SMS_TYPE", new BigDecimal(0));
		smsTbl.set("ATTA_INFO2", System.currentTimeMillis() + "");

		try {
			if(smsTbl.save()) {
				return 1;	//增加成功
			} else {
				return 0;	//添加失败
			}
		} catch (Exception e) {
			e.printStackTrace();
			if(smsTbl.save()) {
				return 1;	//增加成功
			} else {
				return 0;	//添加失败
			}
		}
	}
	
	/**
	 * 更新用户数据
	 * @param info
	 * @return
	 */
	public boolean updateUser(EbUserInfo info) {
		return dao.updateUserInfo(info);
	}

}
