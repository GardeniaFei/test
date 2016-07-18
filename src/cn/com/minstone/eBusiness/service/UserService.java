package cn.com.minstone.eBusiness.service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.List;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;

import cn.com.minstone.eBusiness.dao.UserDao;
import cn.com.minstone.eBusiness.model.EbUserInfo;
import cn.com.minstone.eBusiness.model.RoleType;
import cn.com.minstone.eBusiness.util.Config;
import cn.com.minstone.eBusiness.util.GovNetUtil;
import cn.com.minstone.eBusiness.util.MCubeAppConfig;
import cn.com.minstone.jfinal.ext.util.Logger;

public class UserService extends BaseService {

	private Controller ctrl;
	private UserDao dao;
	
	private static final Logger logger = new Logger(UserService.class);

	public UserService(Controller ctrl) {
		this.ctrl = ctrl;
		this.dao = new UserDao();
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
			String sid = "EASYBUSINESS" + strDate;

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
		String obj = ctrl.getSessionAttr("isLogin");//session已过期
		if (obj == null) {
			EbUserInfo info = new UserDao().findBySid(sid);
			//判断该sid是否有当天登录对应的用户，如果没有返回false如果有则设置登录session为1
			if (info == null) {
				return false;
			} else {
				ctrl.setSessionAttr("isLogin", "1");
				Config.setSessionUserID(ctrl, info.getUserId() + "");
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
	 * 筛选用户
	 * 
	 * @param roleType
	 * @param departId
	 * @param page
	 * @param phoneNum
	 * @return Page
	 */
	public Page<EbUserInfo> filterUsers(String roleType, String departId, int page, String phoneNum) {
		return dao.findByRD(roleType, departId, page, phoneNum);
	}
	
	/**
	 * 筛选用户领导或企业
	 * 
	 * @param roleType
	 * @param userName
	 * @param page
	 * @param phoneNum
	 * @return Page
	 */
	public Page<EbUserInfo> filterOtherUsers(String roleType, String userName, int page, String phoneNum,String bussName) {
		return dao.findOtherByRD(roleType, userName, page, phoneNum,bussName);
	}
	
	/**
	 * 筛选用户类型不为1（办事员）的用户
	 * @param roleType
	 * @param page
	 * @param phoneNum
	 * @param userName
	 * @param bussName
	 * @return
	 */
	public Page<EbUserInfo> filterUsersNotType1(int page, String phoneNum, String userName, String bussName) {
		return dao.findByType(page, phoneNum, userName, bussName);
	}
	
	/**
	 * 筛选用户类型,部门Id,用户名，电话的用户
	 * @param roleType 用户类型
	 * @param page
	 * @param phoneNum
	 * @param userName
	 * @param deptCityId 市级部门单位Id
	 * @return
	 */
	public Page<EbUserInfo> filterCityUser(int page,String roleType, String phoneNum, String userName, String deptCityId) {
		return dao.findByCityType(page,roleType, phoneNum, userName, deptCityId);
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
	 * 根据用户code筛选区级办事人员
	 * 
	 * @param userAccount
	 * @return 
	 */
	public EbUserInfo filterAreaUsersByCode(String userAccount) {
		return dao.findAreaUsersByCode(userAccount);
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
	 * 根据区级政务办人员Vip权限
	 * @param userId
	 * @param isManage是否要设为管理员（1是，0否，2取消vip）
	 * @return boolean
	 */
	public boolean setVip(String userId,String isManage){
		EbUserInfo user = dao.findByUserCode(userId);
		if(user != null){
			//先判断是否为区级政务办人员
			String userRole = user.getRoleType()+"";//（1区级办事人员、2领导、3企业、4市级办事员\5区级政务办）
			int auth = user.getAuthority().intValue();
			if(userRole.equals("5")){
				if(isManage.equals("1")){//是vip服务管理员
					if(auth ==3){
						return user.set("authority", 6).update();
					}else{
						return user.set("authority", 1).update();
					}
				}else if(isManage.equals("0")){//是vip服务专员
					if(auth ==3){
						return user.set("authority", 7).update();
					}else{
						return user.set("authority", 2).update();
					}
				}else if(isManage.equals("2")){//取消vip
					if(auth ==6 || auth == 7){
						return user.set("authority", 3).update();
					}else{
						return user.set("authority", 4).update();
					}
					//1VIP服务专员管理员、2VIP服务专员权限 、3单位首席权限、4部门跟办人权限、5市级服务专员权限、6 VIP服务专员管理员+单位首席权限、7 VIP服务专员+单位首席权限）
				}else{
					return false;
				}
			}else{
				return false;
			}
		}else{
			//本地没有该办事员信息则从审批数据读取
			EbUserInfo user2 = GovNetUtil.getUserByCode(userId);
			if(user2 != null){
				//先判断是否为区级政务办人员
				String userRole = user2.getRoleType()+"";//（1区级办事人员、2领导、3企业、4市级办事员\5区级政务办）
				if(userRole.equals("5")){
					if(isManage.equals("1")){
						user2.set("USER_ID", "userinfo_seq.nextval");
						user2.setIsDelet(new BigDecimal(1));
						user2.setPassword("11");//默认密码
						return user2.set("authority", 1).save();
						//保存设置了VIP的用户信息到本地数据库
					}else if(isManage.equals("0")){
						user2.setPassword("11");//默认密码
						user2.set("USER_ID", "userinfo_seq.nextval");
						user2.setIsDelet(new BigDecimal(1));
						return user2.set("authority", 2).save();
						//（1VIP服务专员管理员、2VIP服务专员权限 、3单位首席权限、4部门跟办人权限、5市级服务专员权限）（办事员才有权限）
					}else if(isManage.equals("2")){
						user2.setPassword("11");//默认密码
						user2.set("USER_ID", "userinfo_seq.nextval");
						user2.setIsDelet(new BigDecimal(1));
						return user2.set("authority", 4).save();
					}else{
						return false;
					}
				}else{
					//用户为多部门的时候
					boolean saveFlag = false;
					int is_MDept = user2.getIsMDept().intValue();//IS_MDEPT 是否多角色 1 是、0 否
					if(is_MDept == 1){
/*************************** 处理办事员用户多角色问题 *******************************/
						String departName = user2.getDepartName();//部门名称
						String myDepatName = user2.getMyDepartName();//
						/**************** 如果部门名称中含有字符串“,”，则说明当前用户是属于多角色用户 *****************/
						if((departName != null && departName.contains(",")) || (myDepatName != null)) {
							String[] _IDs = user2.getMyDepartId().split(",");
							String[] _Names = user2.getMyDepartName().split(",");
							for (int i = 0; i < _IDs.length; i++) {
								String dpt_ID = _IDs[i];
								String dName = _Names[i].replace("\n", "");
								if(dName.equals(MCubeAppConfig.getInstance().getZwb_name())){
									//判断用户角色是否为政务办的
									user2.setDepartName(dName);
									user2.setDepartId(new BigDecimal(dpt_ID));
									user2.setRoleType(new BigDecimal(5));
									//权限设置
									if(isManage.equals("1")){
										user2.set("USER_ID", "userinfo_seq.nextval");
										user2.setIsDelet(new BigDecimal(1));
										user2.setPassword("11");//默认密码
										saveFlag = user2.set("authority", 1).save();
										//保存设置了VIP的用户信息到本地数据库
									}else if(isManage.equals("0")){
										user2.setPassword("11");//默认密码
										user2.set("USER_ID", "userinfo_seq.nextval");
										user2.setIsDelet(new BigDecimal(1));
										saveFlag = user2.set("authority", 2).save();
										//（1VIP服务专员管理员、2VIP服务专员权限 、3单位首席权限、4部门跟办人权限、5市级服务专员权限）（办事员才有权限）
									}else if(isManage.equals("2")){
										user2.setPassword("11");//默认密码
										user2.set("USER_ID", "userinfo_seq.nextval");
										user2.setIsDelet(new BigDecimal(1));
										saveFlag = user2.set("authority", 4).save();
									}
									break;
									//6 VIP服务专员管理员+单位首席权限、7 VIP服务专员+单位首席权限
								}
							}
						}
/*****************************************************************************/
					}
					
					return saveFlag;
				}
			}else{
				return false;
			}
		}
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
	 * @param userEmail
	 *            email
	 * @return <ul><li>1 </li></ul>
	 */
	public int addUserInfo(String userAccount, String password,
			String userName, int roleType, String businessId,
			String photoUrl, String userEmail, String bussName) {

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
		if (!"".equals(businessId) && roleType == RoleType.BUSINESS) {
			userInfo.set("BUSINESS_ID", businessId);
		}
		if (businessId != null && !businessId.equalsIgnoreCase("null") && !"".equals(businessId) && roleType == RoleType.BUSINESS) {
			userInfo.set("BUSINESS_ID", businessId);
		}
		if (bussName != null && !"".equals(bussName) && !bussName.equalsIgnoreCase("null") && roleType == RoleType.BUSINESS) {
			userInfo.setBusinessName(bussName);//用户所属企业名称
		}
		if (!"".equals(photoUrl)) {
			System.out.println("添加用户头像" + photoUrl);
			userInfo.set("USER_PHOTO", photoUrl);
		}
		userInfo.setUserEmail(userEmail);

		if(roleType != RoleType.TRANSACTOR && dao.addUserInfo(userInfo)) {
			return 1;	//增加成功
		} else {
			return 0;	//添加失败
		}
	}
	
	/**
	 * 添加市级办事员用户
	 * @param userAccount
	 * @param password
	 * @param userName
	 * @param roleType
	 * @param deptId
	 * @param photoUrl
	 * @param userEmail
	 * @param departName
	 * @return
	 */
	public int addCityUserInfo(String userAccount,String password,String userName,int roleType, String deptId,String photoUrl,String userEmail,String departName){
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
		if (!"".equals(photoUrl)) {
			System.out.println("添加用户头像" + photoUrl);
			userInfo.set("USER_PHOTO", photoUrl);
		}
		
		userInfo.setUserEmail(userEmail);
		userInfo.setAuthority(new BigDecimal(4));//（1VIP服务专员管理员、2VIP服务专员权限 、3单位首席权限、4部门跟办人权限、5市级服务专员权限）
		if(departName.equals(MCubeAppConfig.getInstance().getZwb_name_city())){//判断是否等于市级政务办部门名称
			userInfo.setAuthority(new BigDecimal(5));
		}
		userInfo.setRoleType(new BigDecimal(roleType));//（1区级办事人员、2领导、3企业、4市级办事员、5区级政务办）
		
		if (deptId != null && !deptId.equalsIgnoreCase("null") && !"".equals(deptId)) {
			userInfo.setDepartId(new BigDecimal(deptId));//部门Id
		} else {
			return -1;	//必填项
		}
		if (departName != null && !departName.equalsIgnoreCase("null") && !"".equals(departName)) {
			userInfo.setDepartName(departName);//部门名称
		} else {
			return -1;	//必填项
		}
		
		if(dao.addUserInfo(userInfo)) {
			return 1;	//增加成功
		} else {
			return 0;	//添加失败
		}
	}

}
