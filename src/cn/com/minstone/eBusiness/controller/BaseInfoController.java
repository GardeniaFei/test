package cn.com.minstone.eBusiness.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.minstone.eBusiness.AdminLoginInter;
import cn.com.minstone.eBusiness.model.EbBusiness;
import cn.com.minstone.eBusiness.model.EbDepart;
import cn.com.minstone.eBusiness.model.EbUserInfo;
import cn.com.minstone.eBusiness.model.RoleType;
import cn.com.minstone.eBusiness.service.BusinessService;
import cn.com.minstone.eBusiness.service.DepartService;
import cn.com.minstone.eBusiness.service.UserService;
import cn.com.minstone.eBusiness.service.inter.UserInterService;
import cn.com.minstone.eBusiness.util.Config;
import cn.com.minstone.eBusiness.util.GovNetUtil;
import cn.com.minstone.eBusiness.util.LogUtil;
import cn.com.minstone.eBusiness.util.MCubeAppConfig;
import cn.com.minstone.eBusiness.util.NullUtil;
import cn.com.minstone.eBusiness.util.PathUtil;
import cn.com.minstone.eBusiness.util.StringUtil;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.render.FreeMarkerRender;

@Before(AdminLoginInter.class)
public class BaseInfoController extends Controller {
	
	/**
	 * 初始化用户管理界面获取所有的用户
	 */
	public void initUsers() {
		int page = this.getParaToInt("page", 1);
		
		int dataSrc = this.getParaToInt("dataSource", 1);
		
		Page<EbUserInfo> usersPage = null;
		List<EbUserInfo> users = null;
		BusinessService bussService = new BusinessService();
		if(dataSrc == 2) {
			//本地库中的用户、只有领导和企业用户
			usersPage = new UserService(this).getAllUser(page);
			users = usersPage.getList();

			for(int i = 0; i < users.size(); i++) {
				EbUserInfo info = users.get(i);
				//本地用户应该是没有所属单位的
				if(info.getBusinessId() != null && info.getBusinessId().intValue() != 0) {
					info.setBussInfo(bussService.findById(info.getBusinessId() + ""));
				}
			}
		}else if(dataSrc == 1) {
			Map<String, Integer> map = new HashMap<String, Integer>();
			users = GovNetUtil.getEbUserInfos("", page, PathUtil.MIN_PAGE_SIZE + "", map);
			if (users == null || users.size() <= 0) {
				map.put("pages", 0);
				map.put("total", 0);
			}
/****************2015-11-08 by feini******************/
			//在每个办事员的model添加是否为部门签收人
			if(users.size()>0){
				for(int j=0; j<users.size(); j++){
					EbUserInfo userInfo = users.get(j);
					String deptName = userInfo.getDepartName();//获取部门名称
					String zwbName = Config.ZWB_NAME;//此为区级政务办名称
					userInfo.setRoleType(new BigDecimal(1));//用户角色（1区级办事人员、2领导、3企业、4市级办事员、5区级政务办
					if(deptName.equals(zwbName)){
						userInfo.setRoleType(new BigDecimal(5));//5区级政务办
					}
					//设置多部门名称分解判断是否为区级政务办
					String isMDept = userInfo.getIsMDept()+"";//IS_MDEPT 是否多角色 1 是、0 否
					if(isMDept.equals("1")){
						String MDeptName = userInfo.getDepartName();
						String[] mdptName =  MDeptName.split(",");
						for(int i = 0; i < mdptName.length; i++){
							String dptName = mdptName[i];
							if(dptName.equals(zwbName)){
								userInfo.setRoleType(new BigDecimal(5));//5区级政务办
								break;
							}
						}
					}
					//判断用户权限
					EbUserInfo u = new UserService(this).findByCode(userInfo.getUserAccount());
					if(u == null){
						//该用户信息没有存在本地库中，权限为单位跟办人
						userInfo.setAuthority(new BigDecimal(4));
					}else{
						//取出该用户的保存的权限
						userInfo.setAuthority(u.getAuthority());//（1VIP服务专员管理员、2VIP服务专员权限 、3单位首席权限、4部门跟办人权限、5市级服务专员权限）
					}
//					Record r = Db.findFirst("select * from eb_depart t where t.depart_id=? and t.user_code =?", userInfo.getDepartId(), userInfo.getUserAccount());
//					if(r == null){
//						//该用户不为签收人，或者该用户部门不存在
//						userInfo.setIs_sign_Man("0");
//					}else{
//						userInfo.setIs_sign_Man("1");//1为非签收人
//					}
//	/**********************2015-11-20 feini 更改多部门情况*********************************/	
//					//多个部门办事员设置签收人判断
//					String isMDept = userInfo.getIsMDept()+"";//IS_MDEPT 是否多角色 1 是、0 否
//					if(isMDept.equals("1")){
//						String MDeptId = userInfo.getMyDepartId();
//						String[] mdptId =  MDeptId.split(",");
//						for(int i = 0; i < mdptId.length; i++){
//							String dptid = mdptId[i];
//							
//							Record rm = Db.findFirst("select * from eb_depart t where t.depart_id=? and t.user_code =?", dptid, userInfo.getUserAccount());
//							if(rm != null){
//								//只要查到一个部门签收人记录，就不用再循环了
//								userInfo.setIs_sign_Man("1");//1为签收人
//								break;
//							}else{
//								userInfo.setIs_sign_Man("0");
//							}
//						}
//					}
	/*******************************************************/	
				}
			}
/************************************/	
			usersPage = new Page<EbUserInfo>(users, page, PathUtil.MIN_PAGE_SIZE, 
					map.get("pages"), map.get("total"));
		}
		
		Map<String, Integer> map2 = new HashMap<String, Integer>();
		List<EbDepart> depts = GovNetUtil.getDeparts(1, "9999", map2, null);
		this.setAttr("deptList", depts);
		
		//获取所有的市级部门
		DepartService dservice = new DepartService();
		List<EbDepart> cityDptList = dservice.getCityDept();
		this.setAttr("cityDptList", cityDptList);
		
		System.out.println("用户数据总量：" + users.size());
		
		this.setAttr("usersPage",usersPage);
		this.setAttr("userList", users);
		this.setAttr("dataSrc", dataSrc);
		
		
		render("/WEB-INF/jsp/userManage.html");
	}

	/**
	 * 筛选用户
	 */
	public void filterUser() {
		String roleType = getPara("roleType", "");
		String departId = getPara("departId", "");	//单位流水号
		int dataSrc = this.getParaToInt("dataSource", 1);	//数据来源 1 审批接口、 2 本地库
		String phoneNum = this.getPara("phoneNum", "").trim();
		
		String departIdByCity = getPara("departIdByCity","");//市级单位号
		String userName = this.getPara("userName");
		String bussName = this.getPara("bussName", "");
		String userAccount = this.getPara("userAccount");
		//筛选用户
		int page = this.getParaToInt("page", 1);
		
		Page<EbUserInfo> usersPage = null;
		List<EbUserInfo> users = null;
		BusinessService bussService = new BusinessService();
		UserService userService = new UserService(this);
		
		if(dataSrc == 2) {
			//本地库中的用户、只有领导和企业用户和市级办事员
			if(roleType.equals("4")) {
				//如果是办事员，则本地库中有市级办事员数据
//				departIdByCity市级部门Id
				usersPage = userService.filterCityUser(page, "4", phoneNum, userName, departIdByCity);
				users = usersPage.getList();
				
//				users = new ArrayList<EbUserInfo>();
//				usersPage = new Page<EbUserInfo>(users, 1, 1, 1, PathUtil.MIN_PAGE_SIZE);
//			}else if(roleType.equals("1") || roleType.equals("5") ) {
//				//没有区级
//				usersPage = new UserService(this).filterUsersNotType1(page, phoneNum, userName, bussName);
//				users = usersPage.getList();
//	
//				for(int i = 0; i < users.size(); i++) {
//					EbUserInfo info = users.get(i);
//					//本地用户应该是没有所属单位的
//					if(info.getBusinessId() != null && info.getBusinessId().intValue() != 0) {
//						info.setBussInfo(bussService.findById(info.getBusinessId() + ""));
//					}
//				}
			}else{
				//如果是企业或领导用户
				usersPage = userService.filterOtherUsers(roleType, userName, page, phoneNum,bussName);
				if(usersPage != null){
					users = usersPage.getList();
				}
				if(users != null){
					if(users.size() > 0){
						for(int i = 0; i < users.size(); i++) {
							EbUserInfo info = users.get(i);
							//本地用户应该是没有所属单位的
							if(info.getBusinessId() != null && info.getBusinessId().intValue() != 0) {
								info.setBussInfo(bussService.findById(info.getBusinessId() + ""));
							}
						}
					}
				}
			}
		}else if(dataSrc == 1) {
			if(!NullUtil.isEmpty(userAccount)) {
				users = new ArrayList<EbUserInfo>();
				EbUserInfo user = userService.filterAreaUsersByCode(userAccount);
				
				if (user == null) {
					user = GovNetUtil.getUserByCode(userAccount);
				}
				
				if(user != null) {
					users.add(user);
					//在每个办事员的model添加是否为部门签收人
//					if(users.size()>0){
//						for(int j=0; j<users.size(); j++){
//							EbUserInfo userInfo = users.get(j);
//							Record r = Db.findFirst("select * from eb_depart t where t.depart_id=? and t.user_code =?", userInfo.getDepartId(), userInfo.getUserAccount());
//							if(r == null){
//								//该用户不为签收人，或者该用户部门不存在
//								userInfo.setIs_sign_Man("0");
//							}else{
//								userInfo.setIs_sign_Man("1");//1为非签收人
//							}
//						}
//					}
					usersPage = new Page<EbUserInfo>(users, page, PathUtil.MIN_PAGE_SIZE, 
							1, 1);
				}else {
					//在每个办事员的model添加是否为部门签收人
//					if(users.size()>0){
//						for(int j=0; j<users.size(); j++){
//							EbUserInfo userInfo = users.get(j);
//							Record r = Db.findFirst("select * from eb_depart t where t.depart_id=? and t.user_code =?", userInfo.getDepartId(), userInfo.getUserAccount());
//							if(r == null){
//								//该用户不为签收人，或者该用户部门不存在
//								userInfo.setIs_sign_Man("0");
//							}else{
//								userInfo.setIs_sign_Man("1");//1为非签收人
//							}
//						}
//					}
					usersPage = new Page<EbUserInfo>(users, page, PathUtil.MIN_PAGE_SIZE, 
							0, 0);
				}
			}else if(roleType.equals("1") || roleType.equals("") || roleType.equals("5")) {
				//如果是办事人员则从审批接口中进行筛选（审批接口中只有办事人员）
				Map<String, Integer> map = new HashMap<String, Integer>();
				users = GovNetUtil.getEbUserInfos(departId, page, PathUtil.MIN_PAGE_SIZE + "", map);	//根据单位流水号筛选用户
				if(users == null || users.size() <= 0) {
					map.put("pages", 0);
					map.put("total", 0);
				}
/****************2015-11-08 by feini******************/
				//在每个办事员的model添加是否为部门签收人
				if(users.size()>0){
					for(int j=0; j<users.size(); j++){
						EbUserInfo userInfo = users.get(j);
						String deptName = userInfo.getDepartName();//获取部门名称
						String zwbName = MCubeAppConfig.getInstance().getZwb_name();//此为后台设置的区级政务办名称
						userInfo.setAuthority(new BigDecimal(4));//权限标识（1VIP服务专员管理员、2VIP服务专员权限 、3单位首席权限、4部门跟办人权限、5市级服务专员权限）
						userInfo.setRoleType(new BigDecimal(1));//用户角色（1区级办事人员、2领导、3企业、4市级办事员、5区级政务办
						if(deptName.equals(zwbName)){
							userInfo.setRoleType(new BigDecimal(5));//5区级政务办
						}
						//设置多部门名称分解判断是否为区级政务办
						String isMDept = userInfo.getIsMDept()+"";//IS_MDEPT 是否多角色 1 是、0 否
						if(isMDept.equals("1")){
							String MDeptName = userInfo.getDepartName();
							String[] mdptName =  MDeptName.split(",");
							for(int i = 0; i < mdptName.length; i++){
								String dptName = mdptName[i];
								if(dptName.equals(zwbName)){
									userInfo.setRoleType(new BigDecimal(5));//5区级政务办
									break;
								}
							}
						}
						//判断用户权限
						EbUserInfo u = new UserService(this).findByCode(userInfo.getUserAccount());
						if(u == null){
							//该用户信息没有存在本地库中，权限为单位跟办人
							userInfo.setAuthority(new BigDecimal(4));
						}else{
							//取出该用户的保存的权限
							userInfo.setAuthority(u.getAuthority());//（1VIP服务专员管理员、2VIP服务专员权限 、3单位首席权限、4部门跟办人权限、5市级服务专员权限）
						}
//						Record r = Db.findFirst("select * from eb_depart t where t.depart_id=? and t.user_code =?", userInfo.getDepartId(), userInfo.getUserAccount());
//						if(r == null){
//							//该用户不为签收人，或者该用户部门不存在
//							userInfo.setIs_sign_Man("0");
//						}else{
//							userInfo.setIs_sign_Man("1");//1为非签收人
//						}
//		/**********************2015-11-20 feini 更改多部门情况*********************************/	
//						//多个部门办事员设置签收人判断
//						String isMDept = userInfo.getIsMDept()+"";//IS_MDEPT 是否多角色 1 是、0 否
//						if(isMDept.equals("1")){
//							String MDeptId = userInfo.getMyDepartId();
//							String[] mdptId =  MDeptId.split(",");
//							for(int i = 0; i < mdptId.length; i++){
//								String dptid = mdptId[i];
//								
//								Record rm = Db.findFirst("select * from eb_depart t where t.depart_id=? and t.user_code =?", dptid, userInfo.getUserAccount());
//								if(rm != null){
//									//只要查到一个部门签收人记录，就不用再循环了
//									userInfo.setIs_sign_Man("1");//1为非签收人
//									break;
//								}
//							}
//						}
		/*******************************************************/	
					}
				}
/*******************************************************/	
				usersPage = new Page<EbUserInfo>(users, page, PathUtil.MIN_PAGE_SIZE, 
						map.get("pages"), map.get("total"));
			}
		}
		
		Map<String, Integer> map2 = new HashMap<String, Integer>();
		List<EbDepart> depts = GovNetUtil.getDeparts(1, "9999", map2, null);
		this.setAttr("deptList", depts);
		
		//获取所有的市级部门
		DepartService dservice = new DepartService();
		List<EbDepart> cityDptList = dservice.getCityDept();
		this.setAttr("cityDptList", cityDptList);
		 
		this.setAttr("usersPage",usersPage);
		this.setAttr("userList", users);
		this.setAttr("roleType", roleType);
		this.setAttr("phoneNum", phoneNum);
		this.setAttr("userName", userName);
		this.setAttr("bussName", bussName);
		this.setAttr("userAccount", userAccount);
		this.setAttr("departIdByCity", departIdByCity);
		if(dataSrc == 1) {
			this.setAttr("departId", departId);
		}
		this.setAttr("dataSrc", dataSrc);
		
		render("/WEB-INF/jsp/userManage.html");
	}
	
	/**
	 * 根据用户id删除用户
	 */
	public void deleteInfo() {
		String userId = getPara("userId");
		UserService userService = new UserService(this);
		boolean deleResult = userService.deleteUserById(userId);
		
		if(deleResult){
			//删除成功，直接跳转到事项配置显示页面
			setAttr("dmessage", "1");
		}else{
			//删除失败
			setAttr("dmessage","0");
		}
		renderJson();
//		initUsers();
	}
	
	/**
	 * 根据用户账号判断是否重复
	 */
	public void checkRepeatCode() {
		String userCode = getPara("userCode");
		UserService userService = new UserService(this);
		EbUserInfo userInfo = userService.findByCode(userCode);
		if(userInfo==null){
			//该用户账号不存在，可以添加
			this.setAttr("message", "1");
		}else{
			this.setAttr("message", "0");
		}
		
		renderJson();
	}
	
	/**
	 * 跳转到修改用户信息界面
	 */
	public void resetView() {
		String userId = getPara("userId");
		System.out.println("用户id：" + userId);
		
		UserService userService = new UserService(this);
		
		DepartService deptService = new DepartService();
		BusinessService bussService = new BusinessService();
		
		EbUserInfo userInfo = userService.findById(userId);
		if(userInfo.getDepartId() != null && userInfo.getDepartId().intValue() != 0) {
			userInfo.setDeptInfo(deptService.getDeptById(
					userInfo.getDepartId() + ""));
		}
		if(userInfo.getBusinessId() != null && userInfo.getBusinessId().intValue() != 0) {
			userInfo.setBussInfo(bussService.findById(userInfo.getBusinessId() + ""));
		}
		
		if(userInfo.getRoleType().intValue() == RoleType.BUSINESS) {
			List<EbBusiness> bussList = bussService.getAllBussInfoList();
			this.setAttr("bussList", bussList);
		}
		this.setAttr("userInfo", userInfo);
		
		render("/WEB-INF/jsp/resetUser.html");
	}
	
	/**
	 * 根据部门id获取部门信息
	 */
	public void findDeptById() {
		String departId = getPara("departId");
		EbDepart dept = new DepartService().getDeptById(departId);
		this.setAttr("dept", dept);
		renderJson();
	}

	/**
	 * 跳转到添加用户页面
	 */
	public void toAddView() {
		System.out.print("输出企业名称");
		BusinessService bservice = new BusinessService();
		List<EbBusiness> list = bservice.getAllBussInfoList();
		
		System.out.print("输出企业名称" + list.size());
		this.setAttr("bussInfos", list);
		render("/WEB-INF/jsp/addUser.html");
	}

	/**
	 * 添加用户
	 */
	public void addUser() {
		String userAccount = getPara("userAccount", "").trim();	//用户账号
		String password = getPara("password", "").trim();		//密码
		String userName = getPara("userName", "").trim();		//用户姓名
		int roleType = getParaToInt("roleType");	//用户类型 2 领导 、 3 企业
		String businessId = getPara("businessId", "").trim();	//所属企业id
		String photoUrl = getPara("photoUrl", "").trim();		//用户头像
		String userEmail = this.getPara("idEmail", "").trim();
		LogUtil.log("参数信息：账号 = " + userAccount + " 密码 = " + password + 
				" 用户姓名 = " + userName + " 用户类型 = " + roleType + " 所属企业 = " + businessId + 
				" 用户头像 = " + photoUrl + "邮箱：" + userEmail);
		
		if (!StringUtil.valiatePhone(userAccount)) {
			setAttr("success", false);
			setAttr("message", "手机号码格式错误！");
		}else if (userEmail != null && !userEmail.equals("") && !StringUtil.valiateEmail(userEmail)) {
			setAttr("success", false);
			setAttr("message", "邮箱格式错误！");
		}else {
			UserService service = new UserService(this);
			BusinessService buService = new BusinessService();
			EbBusiness bussInfo = buService.getBussById(businessId);
			String bussName = "";
			if(bussInfo != null) {
				bussName = bussInfo.getBusinessName();
			}
			
			int result = service.addUserInfo(userAccount, password, userName, 
					roleType, businessId, photoUrl, userEmail, bussName);
			
			if(result == -1) {
				setAttr("success", false);
				setAttr("message", "缺少数据！添加失败");
			}else if(result == 0) {
				setAttr("success", false);
				setAttr("message", "数据库添加失败");
			}else {
				String content = "已为您添加易企办用户，账号名是您的手机号码，密码是" + MCubeAppConfig.getInstance().getDefaultPwd() + 
						"，访问地址" + 
						MCubeAppConfig.getInstance().getAppLoadUrl() + "下载易企办应用";
				new UserInterService(this).addSMSInfo(userAccount, userName, 
						MCubeAppConfig.getInstance().getDefaultPwd(), content);
				
				setAttr("success", true);
				setAttr("message", "添加成功");
			}
		}
		
		initUsers();
//		this.renderJson();
	}
	
	/**
	 * 修改用户信息(已经取消该功能)
	 */
	public void resetUser() {
		String password = getPara("password");		//密码
		String userName = getPara("userName");		//用户姓名
		String roleType = getPara("roleType");	//用户类型 2 领导 、 3 企业
		String businessId = getPara("businessId");	//所属企业id
		String photoUrl = getPara("photoUrl");		//用户头像
		String userId = getPara("userId");
		
		UserService uService = new UserService(this);
		int result = uService.updateUser(userId, roleType, businessId, userName, password, photoUrl);
		
		if(result == -1) {
			setAttr("success", false);
			setAttr("message", "缺少数据！修改失败");
		}else if(result == 0) {
			setAttr("success", false);
			setAttr("message", "数据库修改失败");
		}else {
			setAttr("success", true);
			setAttr("message", "修改成功");
		}
		
		initUsers();
	}
	
	/**
	 * 重置用户密码（企业和领导）
	 */
	public void resetPwd(){
		String pwd = getPara("pwd");
		String userId = getPara("userId");
		
		UserService uService = new UserService(this);
		boolean result = uService.resetPwdById(userId, pwd);
		if(result){
			this.setAttr("message", "1");//重置密码成功
		}else{
			this.setAttr("message", "0");//重置密码失败
		}
		renderJson();
	}
	
	/**
	 * 重置用户姓名（企业和领导）
	 */
	public void resetUserName(){
		String userName = getPara("userName");
		String userId = getPara("userId");
		
		UserService uService = new UserService(this);
		boolean result = uService.resetNameById(userId, userName);
		if(result){
			this.setAttr("message", "1");//修改姓名成功
		}else{
			this.setAttr("message", "0");//修改姓名失败
		}
		renderJson();
		
	}
	
	/**
	 * 跳转到添加市级用户页面
	 */
	public void toAddCityView() {
		//获取所有的市级部门
		DepartService dservice = new DepartService();
		List<EbDepart> cityDptList = dservice.getCityDept();
		
		this.setAttr("cityDepts", cityDptList);
		render("/WEB-INF/jsp/addCityUser.html");
	}
	
	/**
	 * 添加市级单位用户
	 */
	public void addCityUser(){
		String userAccount = getPara("userAccount", "").trim();	//用户账号
		String password = getPara("password", "").trim();		//密码
		String userName = getPara("userName", "").trim();		//用户姓名
		int roleType = 4;	//（1区级办事人员、2领导、3企业、4市级办事员、5区级政务办）
		String photoUrl = getPara("photoUrl", "").trim();		//用户头像
		String userEmail = this.getPara("idEmail", "").trim();
		String departId = getPara("departId");//市级部门id
		String departName = "";//部门名称
		DepartService dSer = new DepartService();
		EbDepart d = dSer.getDeptById(departId);
		if(d != null){
			departName = d.getDepartName();
		}
		
		if (!StringUtil.valiatePhone(userAccount)) {
			setAttr("success", false);
			setAttr("message", "手机号码格式错误！");
		}else if (userEmail != null && !userEmail.equals("") && !StringUtil.valiateEmail(userEmail)) {
			setAttr("success", false);
			setAttr("message", "邮箱格式错误！");
		}else {
			UserService service = new UserService(this);
			
			int result = service.addCityUserInfo(userAccount, password, userName, 
					roleType, departId, photoUrl, userEmail, departName);
			
			if(result == -1) {
				setAttr("success", false);
				setAttr("message", "缺少数据！添加失败");
			}else if(result == 0) {
				setAttr("success", false);
				setAttr("message", "数据库添加失败");
			}else {
				String content = "已为您添加易企办用户，账号名是您的手机号码，密码是" + MCubeAppConfig.getInstance().getDefaultPwd() + 
						"，访问地址" + 
						MCubeAppConfig.getInstance().getAppLoadUrl() + "下载易企办应用";
				new UserInterService(this).addSMSInfo(userAccount, userName, 
						MCubeAppConfig.getInstance().getDefaultPwd(), content);
				
				setAttr("success", true);
				setAttr("message", "添加成功");
			}
		}
		initUsers();
	}

	/**
	 * 设置区级办事员VIP权限
	 */
	public void setVIP(){
		String userId = this.getPara("userId");
		String isManage = this.getPara("isManage");//是否是管理员（1是，0否,2取消VIP）
		
		UserService uService = new UserService(this);
		boolean result = uService.setVip(userId, isManage);
		if(result){
			this.setAttr("message", "1");//设置Vip权限成功
		}else{
			this.setAttr("message", "0");//设置失败
		}
		renderJson();
	}
	

}
