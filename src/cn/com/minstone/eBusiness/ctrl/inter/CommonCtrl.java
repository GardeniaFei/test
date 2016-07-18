package cn.com.minstone.eBusiness.ctrl.inter;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.com.minstone.eBusiness.MobileInter;
import cn.com.minstone.eBusiness.dao.inter.TaskInterDao;
import cn.com.minstone.eBusiness.model.ControlInfo;
import cn.com.minstone.eBusiness.model.EbBusiness;
import cn.com.minstone.eBusiness.model.EbBusinessType;
import cn.com.minstone.eBusiness.model.EbBussItem;
import cn.com.minstone.eBusiness.model.EbDepart;
import cn.com.minstone.eBusiness.model.EbDeptEvainfo;
import cn.com.minstone.eBusiness.model.EbEvaluate;
import cn.com.minstone.eBusiness.model.EbFileImg;
import cn.com.minstone.eBusiness.model.EbItem;
import cn.com.minstone.eBusiness.model.EbMessage;
import cn.com.minstone.eBusiness.model.EbNews;
import cn.com.minstone.eBusiness.model.EbNotice;
import cn.com.minstone.eBusiness.model.EbNoticeEmail;
import cn.com.minstone.eBusiness.model.EbSpcProgram;
import cn.com.minstone.eBusiness.model.EbTask;
import cn.com.minstone.eBusiness.model.EbTaskDistribute;
import cn.com.minstone.eBusiness.model.EbTaskList;
import cn.com.minstone.eBusiness.model.EbUserInfo;
import cn.com.minstone.eBusiness.service.DepartService;
import cn.com.minstone.eBusiness.service.EmailService;
import cn.com.minstone.eBusiness.service.ImgService;
import cn.com.minstone.eBusiness.service.MessageService;
import cn.com.minstone.eBusiness.service.NoticeService;
import cn.com.minstone.eBusiness.service.inter.BussInterService;
import cn.com.minstone.eBusiness.service.inter.DeptInterService;
import cn.com.minstone.eBusiness.service.inter.EvaInterService;
import cn.com.minstone.eBusiness.service.inter.ItemInterService;
import cn.com.minstone.eBusiness.service.inter.MsgInterService;
import cn.com.minstone.eBusiness.service.inter.TaskInterService;
import cn.com.minstone.eBusiness.service.inter.UserInterService;
import cn.com.minstone.eBusiness.util.Config;
import cn.com.minstone.eBusiness.util.GovNetUtil;
import cn.com.minstone.eBusiness.util.LogUtil;
import cn.com.minstone.eBusiness.util.MCubeAppConfig;
import cn.com.minstone.eBusiness.util.MD5Util;
import cn.com.minstone.eBusiness.util.NullUtil;
import cn.com.minstone.eBusiness.util.PushUtil;
import cn.com.minstone.eBusiness.util.StringUtil;
import cn.com.minstone.eBusiness.util.TimeUtil;
import cn.com.minstone.jfinal.ext.util.Logger;
import cn.com.minstone.eBusiness.util.FileHelper;

import com.jfinal.aop.Before;
import com.jfinal.aop.Clear;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.render.JsonRender;
import com.jfinal.upload.UploadFile;
/***
 * 文件描述： 公用controller
 * 
 * @author wangjfn
 * @since 2015-8-19 
 */
@Before(MobileInter.class)
public class CommonCtrl extends Controller {
	
	Logger logger = new Logger(CommonCtrl.class);
	
	/**
	 * 按时计算事项是否超时
	 */
	public void caculateItemTime() {
		final TaskInterService tService = new TaskInterService();
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				// TODO 定时计算一下超时办理的事项，并把该信息推送出去
				tService.cacularItemTime();
			}
		};

		//执行计时器
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		calendar.set(year, month, day, 10, 0, 0);
		Date date = calendar.getTime();
		Timer timer = new Timer();

		timer.schedule(task, date, 24 * 3600000);
	}
	
	/**
	 * 定时获取区级办件进度结果状态信息
	 * 同步审批办件状态
	 */
	public void changeDistrStatus() {
		Timer timer = new Timer();
		final TaskInterService taskService = new TaskInterService();
		timer.scheduleAtFixedRate(new TimerTask() {
			
			@Override
			public void run() {
				List<EbTaskDistribute> distrList = taskService.getDistrsByAreaType();
				if (distrList != null) {
					for (int i = 0; i < distrList.size(); i++) {
						EbTaskDistribute distrInfo = distrList.get(i);
						if (!NullUtil.isEmpty(distrInfo.getControlSeq())) {
							ControlInfo contrInfo = GovNetUtil.getItemProgress(distrInfo.getControlSeq());
							if(contrInfo != null){
								if (contrInfo != null && contrInfo.getApproveStatus().equals("11") 
										|| contrInfo.getApproveStatus().equals("9999") 
										|| contrInfo.getApproveStatus().equals("14")) {
									distrInfo.setTransactionStatus(new BigDecimal(3));
									distrInfo.setTransactionTime(System.currentTimeMillis()+"");
									if(distrInfo.update()){
										setCompStaus(distrInfo.getTaskId()+"", distrInfo.getTransactorId()+"",distrInfo.getUserId()+"");
									}
								}
							}
						}
					}
				}
			}
			//TODO 设置多长时间同步审批办件 一分钟
		}, 0, 1000 * 60 * 10);
	}
	
	/**
	 * 搜索企业所有事项完成
	 * @param taskId
	 * @param transactor_id
	 */
	public void setCompStaus(String taskId, String transactor_id,String user_id){
		UserInterService userIS = new UserInterService(this);
		EbUserInfo userInfo = userIS.findById(transactor_id);
		EbUserInfo userInfoZWB =  userIS.findById(user_id);
		//判断事项是否全办理完
		boolean flag = true;
		//根据任务id查询所有办理状态不为4（被退回）的事项分发列表
		TaskInterService taskSer = new TaskInterService();
		List<EbTaskDistribute> distrList = taskSer.getDistrById(taskId);
		if(distrList != null) {
			for (int i = 0; i < distrList.size(); i++) {
				EbTaskDistribute distr = distrList.get(i);
				//事项的办理只要有一个事项的办理状态不为3（办理完结）则认为该企业还不能落户
				if(distr.getTransactionStatus().intValue() != 3) {
					flag = false;
				}
			}
		}
		if(flag) {
			EbTask taskInfo = taskSer.getTaskById(taskId);
			if (taskInfo != null) {
				String compTime = System.currentTimeMillis() + "";
				//改变任务的完成状态
				taskInfo.setCompStatus(new BigDecimal("1"));
				taskInfo.setCompTime(compTime);
				taskInfo.update();
				EbBusiness bussInfo = new BussInterService().findById(taskInfo.getBusinessId() + "");
				if(bussInfo != null) {
					//改变企业的落户状态
					bussInfo.setSettleStatus(new BigDecimal("1"));
					bussInfo.setSettleTime(compTime);
					bussInfo.update();
					MsgInterService msgService = new MsgInterService();
					//TODO 消息推送
					try {
						String zwbId = "";
						if(userInfoZWB != null){
							zwbId = userInfoZWB.getDepartId()+"";
						}
						String newsTime = System.currentTimeMillis() + "";
						String newsContent = bussInfo.getBusinessName() + "（企业）相关的所有企业落户事项已办理完结，请查看";
						boolean flg = msgService.addNews(userInfo, bussInfo, "12", newsContent, "", 
								newsTime, null, null, "0", zwbId, MCubeAppConfig.getInstance().getZwb_name());
						if(flg) {
							List<String> strAlias = new ArrayList<String>();
							strAlias.add(bussInfo.getBusinessId() + "");
							strAlias.add(Config.ZWB);
							
							Hashtable<String, String> map = new Hashtable<String, String>();
							map.put("newsType", "12");
							map.put("content", newsContent);
							map.put("status", "0");
							PushUtil.pushNotifyToTagert(strAlias, StringUtil.getJson(map));
						}else {
							logger.error("消息插入数据库失败");
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	/**
	 * 获取所有部门信息筛选政务办id存入session
	 */
	public int zwb_id(){
		//获取所有的部门信息列表（一定是从审批接口中获取的）
		String zwb_deptId = "";
		int a=0;
		List<EbDepart> dListOld = GovNetUtil.getDeparts(1, "9999", new HashMap<String, Integer>(), null);
		if(dListOld != null){
			if(dListOld.size() > 0){
				for(int i = 0; i< dListOld.size(); i++){
					EbDepart depart = dListOld.get(i);
					String deptName = depart.getDepartName();
					if(deptName.equals(MCubeAppConfig.getInstance().zwb_name)){
						zwb_deptId = depart.getDepartId()+"";
						if(zwb_deptId != null){
							Config.setSessionZWBID(this, zwb_deptId);
							a=1;
						}
					}
				}
			}
		}
		return a;
	}
	
	/***
	 * 1用户登录
	 */
	@Clear
	public void login(){
		String userCode = getPara("userAccount");
		String password = getPara("password");
//		int roleType = this.getParaToInt("roleType", 1);//用户类型 1 办事员、 0 企业或领导用户
		int a = zwb_id();//获取政务办的部门Id
		if(a == 1){
			valiate(userCode, password);
		}else{
			faultResult("加载用户失败，请重试！");
			render(new JsonRender().forIE());
		}
	}
	
	private void valiate(String userCode, String password) {
		UserInterService userService = new UserInterService(this);
		EbUserInfo user = userService.getLoginByCR(userCode);
		
		if (user == null) {
			//如果本地不存在该用户，调用审批登录
			netLogin(userCode, password);
		}
		
/******************主要针对办事员用户为多部门角色的情况下***************************/
		
		else if (user.getIsMDept().intValue() == 1) {
			netLogin(userCode, password);//调用审批接口登录
		}
		
/*********************************************/	
		else if (!user.getPassword().equals(password)) {
			//判断用户是否是区级政务办人员，区级政务办人员要更改密码，用审批接口登录
			Map<String, Integer> map = new HashMap<String, Integer>();
			EbUserInfo u = GovNetUtil.login(userCode, password, map);
			if (map.get("status") == 1) {
				if(u == null) {
					faultResult("用户信息获取失败！");
				}else {
					//用户密码更改（为区级政务办人员）
					int roleType = user.getRoleType().intValue();//用户角色 （1区级办事人员、2领导、3企业、4市级办事员、5区级政务办）
					if(roleType == 5){
						user.setPassword(password);
						if(user.update()){
							loginSucc(user);
						}else{
							faultResult("用户登录失败！");
						}
					}else{
						faultResult("用户登录失败！");//审批接口普通用户与审批接口用户密码不一致
					}
				}
			}else{
				faultResult("密码错误！");
			}
		} else if (user.getIsDelet().intValue() == 0) {
			faultResult("该账号已被禁用");
		}else {
			int roleType = user.getRoleType().intValue();//用户角色 （1区级办事人员、2领导、3企业、4市级办事员、5区级政务办）
			if(roleType == 3) {//企业用户
				String bussId = user.getBusinessId() + "";
				EbBusiness bussInfo = new BussInterService().findById(bussId);
				if(bussInfo == null) {
					faultResult("相关企业不存在，不能登录！");
				}else if (bussInfo.getSettleStatus().intValue() == 1) {
					long time = Long.parseLong(bussInfo.getSettleTime()) + 7 * 24 * 3600000;
					if(System.currentTimeMillis() - time > 0) {
						//落户时间超过七天
						faultResult("企业已落户，该账户不能登录！");
					}else {
						loginSucc(user);
					}
				}else {
					loginSucc(user);
				}
			}else if (roleType == 1||roleType == 4||roleType == 5) {//如果用户角色是办事员
				loginSucc(user);
			}else if (roleType == 5) {//区级政务办，可能有多种权限
				int auth = user.getAuthority().intValue();//6 VIP服务专员管理员+单位首席权限、7 VIP服务专员+单位首席权限
				if(auth == 6 || auth == 7){
					Hashtable<String, Object> result = new Hashtable<String, Object>();
					this.setAttr("success", false);
					this.setAttr("errorMsg", "该办事员用户有复合权限，请选择需要登录的角色！");
					this.setAttr("isMDept", false);//设置该字段标识当前办事员用户为多角色用户
					
					result.put("userAccount", user.getUserAccount());//账号
					result.put("userName", user.getUserName());//姓名
					result.put("userPhoto", getCheckedString(user.getUserPhoto()));//用户头像连接地址
					result.put("userTel", getCheckedString(user.getUserTel()));//用户电话号码
					result.put("departId", user.getDepartId() + "");
					result.put("departName", user.getDepartName() + "");
					result.put("roleType", roleType);
					result.put("deptInfos", "");
					this.setAttr("result", result);
					
					Config.setSessionUserAccount(this, userCode);
					Config.setSessionUserName(this, user.getUserName());
					Config.setSessionPWD(this, password);
					Config.setSessionUserPhoto(this, getCheckedString(user.getUserPhoto()));
					Config.setSessionUserTel(this, getCheckedString(user.getUserTel()));
				}else{
					loginSucc(user);
				}
			}else {//领导用户
				loginSucc(user);
			}
		}
		render(new JsonRender().forIE());
	}
	
	/**
	 * 本地登录成功
	 * @param user
	 */
	private void loginSucc(EbUserInfo user) {
		/**
		 * 用户名密码验证成功 1.生成sid 2.设置登录状态
		 */
		this.setAttr("success", true);
		Hashtable<String, Object> result = new Hashtable<String, Object>();
		
		//产生sid
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		String strDate = format.format(System.currentTimeMillis());
		String sid = "EASYBUSINESSUSER" + user.getUserAccount() + strDate;

		user.setSid(sid);
		boolean flag = user.update();
		if(!flag) {
			faultResult("sid保存失败！");
		}else {
			result.put("sid", sid);
			
			result.put("userId", user.getUserId());
			result.put("userAccount", user.getUserAccount());
			result.put("userName", user.getUserName());
			result.put("userPhoto", user.getUserPhoto()+"");
			result.put("roleType", user.getRoleType());	//（1区级办事人员、2领导、3企业、4市级办事员、5区级政务办）
			
			result.put("departId", user.getDepartId() + "");
			result.put("departName", user.getDepartName() + "");//本地用户没有所属部门
			
			result.put("authority", user.getAuthority() + "");	//（1VIP服务专员管理员、2VIP服务专员权限 、3单位首席权限、4部门跟办人权限、5市级服务专员权限）（办事员才有权限）
			result.put("businessId", user.getBusinessId() + "");	//用户所属企业id
			if(user.getBusinessId() != null && !user.getBusinessId().equals("") && !"null".equalsIgnoreCase(user.getBusinessId() + "")) {
				EbBusiness buss = new BussInterService().getBussById(user.getBusinessId() + "");
				if(buss != null) {
					result.put("businessName", buss.getBusinessName());
				}else {
					result.put("businessName", "");
				}
			}else {
				result.put("businessName", "");
			}
			
			this.setAttr("result", result);
			this.setAttr("errorMsg", "登录成功！");
			
			Config.setSessionIsLogin(this, "1");
			Config.setSessionRoleType(this, user.getRoleType() + "");
			Config.setSessionUserID(this, user.getUserId() + "");
			Config.setSessionUserAccount(this, user.getUserAccount());
		}
	}
	
	/**
	 * 企业或领导用户登录
	 * @param userCode
	 * @param password
	 */
	@SuppressWarnings("unused")
	private void locLogin(String userCode, String password) {
		UserInterService userService = new UserInterService(this);
		
		EbUserInfo user = userService.getUserByCR(userCode);
		if (user == null) {
			faultResult("用户不存在或该用户为办事员，请勾选办事员！");
		} else if (!user.getPassword().equals(password)) {
			faultResult("用户密码错误！");
		} else {
			/**
			 * 用户名密码验证成功 1.生成sid 2.设置登录状态
			 */
			this.setAttr("success", true);
			Hashtable<String, Object> result = new Hashtable<String, Object>();
			
			//产生sid
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
			String strDate = format.format(System.currentTimeMillis());
			String sid = "EASYBUSINESSUSER" + user.getUserAccount() + strDate;

			user.setSid(sid);
			boolean flag = user.update();
			if(!flag) {
				faultResult("sid保存失败！");
			}else {
				result.put("sid", sid);
				
				result.put("userId", user.getUserId());
				result.put("userAccount", user.getUserAccount());
				result.put("userName", user.getUserName());
				result.put("userPhoto", user.getUserPhoto()+"");
				result.put("roleType", user.getRoleType());	//用户角色 1 办事人员、 2 领导、 3 企业
				
				result.put("departId", user.getDepartId() + "");
				//TODO 清空数据库改注释
				result.put("departName", "");//本地用户没有所属部门
				
				result.put("authority", user.getAuthority() + "");	//用户权限标识 0 非办事人员、 1 招商局、 2 政务办、 3其他部门
//				result.put("authority", "0");
				result.put("businessId", user.getBusinessId() + "");	//用户所属企业id
				if(user.getBusinessId() != null && !user.getBusinessId().equals("") && !"null".equalsIgnoreCase(user.getBusinessId() + "")) {
					EbBusiness buss = new BussInterService().getBussById(user.getBusinessId() + "");
					if(buss != null) {
						result.put("businessName", buss.getBusinessName());
					}else {
						result.put("businessName", "");
					}
				}else {
					result.put("businessName", "");
				}
				
				this.setAttr("result", result);
				this.setAttr("errorMsg", "登录成功！");
				
				Config.setSessionIsLogin(this, "1");
				Config.setSessionRoleType(this, user.getRoleType() + "");
				Config.setSessionUserID(this, user.getUserId() + "");
			}
		}
		
		render(new JsonRender().forIE());
	}
	
	/**
	 * 区级办事员登录
	 * @param userCode
	 * @param password
	 */
	private void netLogin(String userCode, String password) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		EbUserInfo user = GovNetUtil.login(userCode, password, map);
		if (map.get("status") == 1) {
			if(user == null) {
				faultResult("用户信息获取失败！");
			}else {
/*************************** 处理办事员用户多角色问题 *******************************/
				String departName = user.getDepartName();//部门名称
				String myDepatName = user.getMyDepartName();//
				/**************** 如果部门名称中含有字符串“,”，则说明当前用户是属于多角色用户 *****************/
				if((departName != null && departName.contains(",")) || (myDepatName != null)) {
					
					//由于当前步骤还不知道登录用户选择的是那个部门所有无法判断是否登录成功（部门签收人才能登录）
					//所有还不能保存当前用户信息到数据库中
					Hashtable<String, Object> result = new Hashtable<String, Object>();
					this.setAttr("success", false);
					this.setAttr("errorMsg", "该办事员用户为多角色用户，请选择需要登录部门！");
					this.setAttr("isMDept", true);//设置该字段标识当前办事员用户为多角色用户
					
					result.put("userAccount", user.getUserAccount());//账号
					result.put("userName", user.getUserName());//姓名
					result.put("userPhoto", getCheckedString(user.getUserPhoto()));//用户头像连接地址
					result.put("userTel", getCheckedString(user.getUserTel()));//用户电话号码
					String roleType = user.getRoleType()+"";
					
					String[] _IDs = user.getMyDepartId().split(",");
					String[] _Names = user.getMyDepartName().split(",");
					List<Hashtable<String, String>> tbls = new ArrayList<Hashtable<String,String>>();
					for (int i = 0; i < _IDs.length; i++) {
						Hashtable<String, String> tb = new Hashtable<String, String>();
						tb.put("departId", _IDs[i]);
						tb.put("departName", _Names[i].replace("\n", ""));
						tb.put("departType", "2");//部门类型（1市级、2区级）暂时考虑区级部门才有多部门情况
						//判断用户角色
						String dName = _Names[i].replace("\n", "");
						if(dName.equals(MCubeAppConfig.getInstance().getZwb_name())){
							roleType = "5";//为区级政务办
						}
						
						//判断用户在每个部门的权限
						//查看本地用户权限
						UserInterService userService = new UserInterService(this);
						EbUserInfo uLocal = userService.getUserByCD(userCode, _IDs[i]);
						int auth = 0;
						if(uLocal != null){
							auth = uLocal.getAuthority().intValue();
							tb.put("authority", auth+"");
						}else{
							tb.put("authority", "4");
						}
						//6 VIP服务专员管理员+单位首席权限、7 VIP服务专员+单位首席权限
						
						tbls.add(tb);
					}
					if(roleType == null || "null".equals(roleType) ){
						roleType = "1";//默认为区级办事员
					}
					
					result.put("roleType", roleType);
					result.put("deptInfos", tbls);
//					result.put("result", result);
					this.setAttr("result", result);
					
					Config.setSessionUserAccount(this, userCode);
					Config.setSessionUserName(this, user.getUserName());
					Config.setSessionPWD(this, password);
					Config.setSessionUserPhoto(this, getCheckedString(user.getUserPhoto()));
					Config.setSessionUserTel(this, getCheckedString(user.getUserTel()));
				}else {

/*****************************************************************************/
//					String deptId = user.getDepartId() + "";
//					Record r = Db.findFirst("select * from eb_depart t where t.depart_id=? and t.user_code =?", deptId, userCode);
//					if(r == null){
//						faultResult("该用户非部门签收人，登录失败！");
//					}else {
						/**
						 * 用户名密码验证成功 1.生成sid 2.设置登录状态
						 */
						this.setAttr("success", true);
						Hashtable<String, Object> result = new Hashtable<String, Object>();
						// TODO 产生sid,将获取的用户信息保存到本地用户表中。
						SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
						String strDate = format.format(System.currentTimeMillis());
						String sid = "EASYBUSINESSUSER" + user.getUserAccount() + strDate;
						
						UserInterService userService = new UserInterService(this);
						EbUserInfo user1 = userService.getUserByCode(userCode);
						
						boolean flag = false;
						try {
							if(user1 == null) {
								user.set("USER_ID", "userinfo_seq.nextval");
								user.set("SID", sid);
								user.set("IS_DELET", 1);
								user.set("PASSWORD", password);
//								LogUtil.log(user.getUserId() + "");
								try {
									flag = user.save();
								} catch (Exception e) {
									e.printStackTrace();
									flag = user.save();
								}
							}else {
								user.set("USER_ID", user1.getUserId());
								user.setSid(sid);
								user.set("IS_DELET", 1);
								user.set("PASSWORD", password);
								
								try {
									flag = user.update();
								} catch (Exception e) {
									e.printStackTrace();
									flag = user.update();
								}
							}
						} catch (Exception e) {
							e.printStackTrace();
							LogUtil.log("更新数据库失败");
						}
						if(flag) {
							Config.setSessionSid(this, sid);
							Config.setSessionRoleType(this, "1");
			
							result.put("sid", sid);
							String user_id = user.getUserId().toString();
							result.put("userId", user_id);
							result.put("userAccount", user.getUserAccount());
							result.put("userName", user.getUserName());
							result.put("userPhoto", user.getUserPhoto()+"");
							result.put("roleType", user.getRoleType() + "");	//用户角色 1 办事人员、 2 领导、 3 企业
							
							result.put("departId", user.getDepartId() + "");
							
							result.put("departName", user.getDepartName() + "");
							
							result.put("authority", user.getAuthority() + "");	//（1VIP服务专员管理员、2VIP服务专员权限 、3单位首席权限、4部门跟办人权限、5市级服务专员权限）
							result.put("businessId", "");	//用户所属企业id
							result.put("businessName", "");
							
							this.setAttr("result", result);
							this.setAttr("errorMsg", "登录成功！");
							
							Config.setSessionIsLogin(this, "1");//是否登录
							Config.setSessionRoleType(this, "1");
							Config.setSessionUserID(this, user.getUserId() + "");//保存用户id
						}else {
							faultResult("数据存入本地库失败！");
						}
//					}
				}
			}
		}else if (map.get("status") == 2) {
			faultResult("用户不存在！");
		}else if (map.get("status") == 3) {
			faultResult("密码错误！");
		}
	}
	
	@Clear
	public void initLogin() {
		String departId = this.getPara("departId");
		String departName = this.getPara("departName");
		String select_authority = this.getPara("select_authority");
		//选择的用户权限（不能选复合权限）（0 非办事员权限 、 1 VIP服务专员管理员、2 VIP服务专员权限 、3 单位首席权限、4 部门跟办人权限、5 市级服务专员权限）
		String userAccount = Config.getSessionUserAccount(this);
		String userName = Config.getSessionUserName(this);
		String password = Config.getSessionPwd(this);
		String userPhoto = Config.getSessionUserPhoto(this);
		String userTel = Config.getSessionUserTel(this);

		if(departId == null || departId.equals("") || departId.equalsIgnoreCase("null")) {
			faultResult("部门ID为空，必须传递部门名称和部门ID");
		}else if(userAccount == null || userAccount.equals("") || userAccount.equalsIgnoreCase("null")) {
			faultResult("session已过期，请重新输入账号密码登录!");
		}else if(select_authority == null || select_authority.equals("") || select_authority.equalsIgnoreCase("null")) {
			faultResult("请传入用户角色权限!");
		}else {
				/**
				 * 用户名密码验证成功 1.生成sid 2.设置登录状态
				 */
				this.setAttr("success", true);
				Hashtable<String, Object> result = new Hashtable<String, Object>();
				SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
				String strDate = format.format(System.currentTimeMillis());
				String sid = "EASYBUSINESSUSER" + userAccount + strDate;
				
				UserInterService userService = new UserInterService(this);
				EbUserInfo user1 = userService.getUserByCD(userAccount, departId);

				EbUserInfo user = new EbUserInfo();
				boolean flag = false;
				try {
					if(user1 == null) {
						user.set("USER_ID", "userinfo_seq.nextval");
						user.set("SID", sid);
						user.set("IS_DELET", 1);
						user.set("PASSWORD", password);
						user.setUserAccount(userAccount);
						user.setUserName(userName);
						user.setUserPhoto(userPhoto);
						user.setUserTel(userTel);
						user.setDepartName(departName);
						user.setDepartId(new BigDecimal(departId));
						user.setRoleType(new BigDecimal("1"));
						user.setIsMDept(new BigDecimal("1"));//是否多角色
						user.setAuthority(new BigDecimal(select_authority));//选择角色权限
						
						try {
							flag = user.save();
						} catch (Exception e) {
							e.printStackTrace();
							flag = user.save();
						}
					}else {
						user = user1;
						user.setSid(sid);
						user.set("IS_DELET", 1);
						user.set("PASSWORD", password);
						user.setUserName(userName);
						user.setUserPhoto(userPhoto);
						user.setUserTel(userTel);
						user.setDepartName(departName);
//						user.setAuthority(new BigDecimal(select_authority));//用原来的权限
						
						try {
							flag = user.update();
						} catch (Exception e) {
							e.printStackTrace();
							flag = user.update();
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					LogUtil.log("更新数据库失败");
				}
				if(flag) {
	
					result.put("sid", sid);
					result.put("userId", getCheckedString(user.getUserId() + ""));
					result.put("select_authority", getCheckedString(select_authority+""));	//用户权限标识
					
					this.setAttr("result", result);
					this.setAttr("errorMsg", "登录成功！");

					Config.setSessionSid(this, sid);
					Config.setSessionIsLogin(this, "1");//是否登录
					Config.setSessionRoleType(this, "1");
					Config.setSessionSelect_auth(this, select_authority);//保存用户选择登录的权限
					Config.setSessionUserID(this, user.getUserId() + "");//保存用户id
				}else {
					faultResult("数据存入本地库失败！");
				}
			}

		render(new JsonRender().forIE());
	}
	
	/**
	 * 2办理进度
	 */
	public void bussiness_progress(){
		BussInterService bussService = new BussInterService();
		TaskInterService taskService = new TaskInterService();
		DeptInterService deptService = new DeptInterService();
		String bussId = this.getPara("businessId");
		if (bussId == null || bussId.equals("") || "null".equalsIgnoreCase(bussId)) {
			faultResult("查询出错！");
		}else {
			EbTask task = taskService.getTaskByBussId(bussId);
			List<Hashtable<String, Object>> items = new ArrayList<Hashtable<String,Object>>();
			
			Hashtable<String, Object> result = new Hashtable<String, Object>();
			String bussName = null;
			String itemName = null;
			
			if(task != null) {
				bussName = task.getBusinessName();
				EbBusiness buss = bussService.getBussById(bussId);
				if(buss!=null){
					itemName = buss.getProjectName();
				}
				
				List<EbTaskDistribute> itemList = taskService.getDistribListLi(task.getTaskId() + "");	//获取任务清单
				for (int i = 0; i < itemList.size(); i++) {
					Hashtable<String, Object> item = new Hashtable<String, Object>();
					EbTaskDistribute distr = itemList.get(i);
					EbItem cItem = distr.getItem();
					if(cItem != null){
						item.put("itemName", cItem.getItemName() + "");
/******************2015-11-13 feini 特殊程序id***************************/
						String distriId = distr.getDistrId() + "";
						EbSpcProgram spc = taskService.getByDistriId(distriId);
						if(spc != null){
							item.put("programId",spc.getProgramId()+"");
						}else{
							item.put("programId"," ");
						}
/***********************************************************************/						
						EbDepart deptInfo = deptService.getDeptById(cItem.getDepartId() + "");
						if(deptInfo != null){
							item.put("departName", deptInfo.getDepartName() + "");
							item.put("workTel", deptInfo.getWorkTel() + "");
						}else{
							item.put("departName","");
							item.put("workTel", "");
						}
					}else{
						item.put("itemName", "");
						item.put("departName","");
						item.put("workTel", "");
					}
					item.put("departId", distr.getDepartId() + "");
					item.put("itemId", distr.getItemId() + "");
					item.put("distrId", distr.getDistrId() + "");
					item.put("signStatus", distr.getSignStatus() + "");	//签收状态 0 未签收、 1 已签收、 2 被退回
					
					item.put("signUserId", distr.getSignUserId() + "");
					item.put("distrId", distr.getDistrId() + "");
					
					if(distr.getSignUserId() != null) {
						EbUserInfo signUser = distr.getSignUser();
						if(signUser != null){
							item.put("signUserName", getCheckedString(signUser.getUserName()));
						}else{
							item.put("signUserName", "");
						}
					}else {
						item.put("signUserName", "");
					}
					item.put("signTime", TimeUtil.changeTimeStyle(distr.getSignTime()));
					
					//办理状态0 首席已分发未开始、1 办理中、2 已逾期、3 办理完结、4 被退回 、5暂停、6首席未分发）
/*******************判定是否区级办件流程、获取办件进度结果信息**********************/
					if (distr.getItemType().intValue() == 1 && !NullUtil.isEmpty(distr.getControlSeq()) 
							&& distr.getTransactionStatus().intValue() != 3) {
						ControlInfo contrInfo = GovNetUtil.getItemProgress(distr.getControlSeq());
						
						if (contrInfo != null) {
							//TODO
							if (contrInfo.getApproveStatus().equals("9999") 
									|| contrInfo.getApproveStatus().equals("11") 
									|| contrInfo.getApproveStatus().equals("14")) {
								distr.setTransactionStatus(new BigDecimal(3));
								item.put("transactionStatus", 3);
								if(distr.update()){
									setCompStaus(distr.getTaskId()+"", distr.getTransactorId()+"",distr.getUserId()+"");
								}
							}else {
								item.put("transactionStatus", distr.getTransactionStatus() + "");
							}
						}
					}else {
						item.put("transactionStatus", distr.getTransactionStatus());
					}
					
					item.put("returnReason", distr.getReturnReason() + "");
					
					item.put("transactorId", distr.getTransactorId() + "");
					//TODO
//					if(distr.getTransactorId() != null) {
					if(distr.getTransUserCode() != null) {
						EbUserInfo transUser = distr.getTransUser();
						if(transUser != null){
							item.put("transactorName", getCheckedString(transUser.getUserName()));
						}else{
							item.put("transactorName", "");
						}
					}else {
						item.put("transactorName", "");
					}
					item.put("transactTime", TimeUtil.changeTimeStyle(distr.getTransactionTime()) + "");
					
					items.add(item);
				}
				
				this.setAttr("success", true);
				result.put("businessId", bussId + "");
				result.put("businessName", bussName + "");
				result.put("itemName", itemName + "");
				result.put("items", items);
				
				this.setAttr("result", result);
				
				this.setAttr("errorMsg", "");
			}else {
				faultResult("未查询到数据！");
			}
		}
		
		render(new JsonRender().forIE());
	}
	
	/**
	 * 3事项办事指南
	 */
	public void serviceGuid(){
		DeptInterService deptService = new DeptInterService();
		ItemInterService itemService = new ItemInterService();
		
		String itemId = this.getPara("itemId");
		EbItem item = itemService.getItemById(itemId);
		if(item == null) {
			item = GovNetUtil.getItemInfo(itemId);
		}
		
		Hashtable<String, Object> result = new Hashtable<String, Object>();
		
		if(item != null) {
			result.put("itemId", itemId);
			result.put("itemName", item.getItemName());
			result.put("departId", item.getDepartId() + "");
			//TODO 事项所属部门从审批接口中获取
//			EbDepart dept = GovNetUtil.getDeptInfo(item.getDepartId() + "");
//			result.put("departName", dept.getDepartName());
			
			if (item.getDepartId() != null) {
				result.put("departName", deptService.getDeptById(item.getDepartId()+"").getDepartName());
			}else {
				result.put("departName", "");
			}
			result.put("transactor", getCheckedString(item.getTransactionObject() + ""));
			String nedCondition = item.getNeedCondition();
			if (nedCondition != null) {
				nedCondition = nedCondition.replace("<br>", "");
				nedCondition = nedCondition.replace("<BR>", "");
				nedCondition = nedCondition.replace("<p>", "");
				nedCondition = nedCondition.replace("<P>", "");
				nedCondition = nedCondition.replace("</P>", "");
				nedCondition = nedCondition.replace("</p>", "");
			}
			result.put("transCondition", getCheckedString(nedCondition));
			
			//TODO 所需材料
			String strMat = item.getNeedMaterial();
			List<Hashtable<String, String>> mats = new ArrayList<Hashtable<String,String>>();
			String[] mates = {};
			if(strMat != null) {
				mates = strMat.split(";");
				System.out.print("所需材料组区分："+mates);
				for (int i = 0; i < mates.length; i++) {
					Hashtable<String, String> mat = new Hashtable<String, String>();
					mates[i] = mates[i].replace("<br>", "");
					mates[i] = mates[i].replace("<BR>", "");
					mates[i] = mates[i].replace("<p>", "");
					mates[i] = mates[i].replace("<P>", "");
					mates[i] = mates[i].replace("</p>", "");
					mates[i] = mates[i].replace("</P>", "");
					
					mat.put("materialTitle", mates[i]);
					logger.info("所需材料组区分再区分："+mates[i]);
					mat.put("materialAnnex", "");
					mats.add(mat);
				}
			}
			result.put("materials", mats);
			
			result.put("itemFlow", getCheckedString(item.getItemFlow() + ""));
			result.put("flowAnnex", getCheckedString(item.getFlowAnnex() + ""));
			result.put("transactionWindow", getCheckedString(item.getTransactionWindow() + ""));
			result.put("itemCharge", getCheckedString(item.getItemCharge() + ""));
			result.put("itemQuestions", getCheckedString(item.getItemQuestions() + ""));
			
			//TODO 办理依据
			String strGist = item.getGistLaw();
			String[] gists = {};
			if(strGist != null) {
				strGist = strGist.replace("<br>", "");
				strGist = strGist.replace("<BR>", "");
				strGist = strGist.replace("<p>", "");
				strGist = strGist.replace("<P>", "");
				strGist = strGist.replace("</p>", "");
				strGist = strGist.replace("</P>", "");
				
				strGist = strGist.replace("&gt;", "");
				strGist = strGist.replace("&lt;", "");
				gists = strGist.split("]");
//				for (int i = 0; i < gists.length; i++) {
//					gists[i] += "]";
//				}
			}
			result.put("lawGists", gists);
			
			result.put("itemLimit", item.getTimeLimit());
		}
		
		if (itemId == null || itemId.equals("") || "null".equalsIgnoreCase(itemId)) {
			this.setAttr("success", false);
			
			this.setAttr("result", result);
			
			this.setAttr("errorMsg", "查询出错！");
		}else {
			this.setAttr("success", true);
			this.setAttr("result", result);
			this.setAttr("errorMsg", "");
		}
		
		render(new JsonRender().forIE());
	}
	
	/**
	 * 34 企业审批单位列表
	 */
	public void examine_depaertment_list() {
		String bussId = this.getPara("businessId");
		if (bussId == null || "".equals(bussId) || "null".equalsIgnoreCase(bussId)) {
			faultResult("缺少参数！");
		}else {
			BussInterService bussService = new BussInterService();
			
			List<EbDepart> depts = bussService.getDeptsByBussId(bussId);
			List<Hashtable<String, Object>> result = new ArrayList<Hashtable<String,Object>>();
			
			if(depts != null) {
				for(int i = 0; i < depts.size(); i++) {
					Hashtable<String, Object> table = new Hashtable<String, Object>();
					EbDepart dept = depts.get(i);
					List<EbMessage> messages = bussService.getMsgByBDId(bussId, dept.getDepartId() + "");
					
					table.put("departId", dept.getDepartId());
					table.put("departName", dept.getDepartName());
					
					if (messages != null && messages.size() > 0) {
						table.put("havaMessage", 1);
					}else {
						table.put("havaMessage", 0);
					}
					
					result.add(table);
				}
			}
			
			this.setAttr("success", true);
			this.setAttr("result", result);
			this.setAttr("errorMsg", "");
		}
		
		render(new JsonRender().forIE());
	}
	
	/**
	 * 4企业留言列表
	 */
	public void message_reply_list(){
		String bussId = this.getPara("businessId");
		String deptId = this.getPara("departId");
		
		BussInterService bussService = new BussInterService();
		
		if (bussId == null || "".equals(bussId) || "null".equalsIgnoreCase(bussId)) {
			faultResult("缺少参数！");
		}else {
			List<EbMessage> messages = bussService.getMsgByBDId_Status(bussId, deptId);
			String userId = Config.getSessionUserID(this);
			if(userId != null){
				EbUserInfo userInfo = new UserInterService(this).findById(userId);
				if(userInfo != null){
					String role = userInfo.getRoleType()+"";//(1 办事人员、2 领导、 3 企业)
					if(role.equals("3")){
						messages = bussService.getMsgByBDId_StatusByBuss(bussId, deptId);
					}
				}
			}
			List<Hashtable<String, Object>> result = new ArrayList<Hashtable<String,Object>>();
			if(messages != null) {
				for(int i = 0; i < messages.size(); i++) {
					EbMessage msg = messages.get(i);
					Hashtable<String, Object> table = new Hashtable<String, Object>();
					table.put("departId", msg.getDepartId() + "");
					EbDepart dept = new DeptInterService().getDeptById(deptId);
					if (dept == null) {
						dept = GovNetUtil.getDeptInfo(msg.getDepartId() + "");
					}
					
					if(dept != null) {
						table.put("departName", dept.getDepartName() + "");
					}else {
						table.put("departName", "");
					}
					table.put("messageId", msg.getMessageId());
					table.put("content", msg.getMessageContent()+"");
					
					String msgTime = TimeUtil.changeTimeStyle(msg.getMessageTime());
					table.put("messageTime", msgTime);
					
					table.put("userId", msg.getUserId());	//留言用户id
					table.put("userName", msg.getUserName());	//留言用户姓名
					
					//留言回复
					List<Hashtable<String, Object>> replyList = new ArrayList<Hashtable<String,Object>>();
					List<EbMessage> replys = new MessageService().getAllReplyExamList(msg.getMessageId() + "");
					if(userId != null){
						EbUserInfo userInfo2 = new UserInterService(this).findById(userId);
						if(userInfo2 != null){
							String role2 = userInfo2.getRoleType()+"";//(1 办事人员、2 领导、 3 企业)
							if(role2.equals("3")){
								replys = new MessageService().getAllReplyExamListByBuss(msg.getMessageId() + "");
							}
						}
					}
					if(replys != null) {
						for(int j = 0; j < replys.size(); j++) {
							EbMessage repMsg = replys.get(j);
							Hashtable<String, Object> repTable = new Hashtable<String, Object>();
							repTable.put("paramId", repMsg.getMessageId());//回复id
							repTable.put("replyContent", repMsg.getMessageContent());
							repTable.put("replayTime", TimeUtil.changeTimeStyle(repMsg.getMessageTime()));
							repTable.put("replyUserId", repMsg.getUserId());
							repTable.put("replyUserName", repMsg.getUserName());
							
							replyList.add(repTable);
						}
					}
					
					table.put("replyList", replyList);	//加载回复列表
					
					result.add(table);
				}
			}
			
			this.setAttr("success", true);
			this.setAttr("result", result);
			this.setAttr("errorMsg", "");
		}
		
		render(new JsonRender().forIE());
	}
	
	/**
	 * 5留言回复
	 */
	public void message_reply(){
		int isReply = this.getParaToInt("isReply");//是否回复 0 留言、1 回复
		String messageId = this.getPara("messageId");//留言ID
		String deptId = this.getPara("departId");//关联部门ID（非必传）
		String bussId = this.getPara("businessId");//关联企业ID
		String content = this.getPara("content");//留言内容
		MsgInterService msgService = new MsgInterService();
		String userId = Config.getSessionUserID(this);
		UserInterService uService = new UserInterService(this);
		EbUserInfo userInfo = uService.findById(userId);
		EbBusiness bussInfo = new BussInterService().findById(bussId);
		EbDepart deptInfo = new DeptInterService().getDeptById(deptId);
		
//		if(bussInfo == null || userInfo == null || deptInfo == null) {
		if(bussInfo == null || userInfo == null) {
			faultResult("无用户信息或企业信息！");
		}else {
			if(isReply == 1) {	//回复
				if(messageId == null || messageId.equals("") || "null".equalsIgnoreCase(messageId)) {
					
					faultResult("缺少参数，回复失败！");
				}else if(msgService.getMsgByMsgId(messageId) == null) {
					faultResult("未查到留言数据！");
				}else {
					if(content == null || content.trim().equals("") || "null".equalsIgnoreCase(content)) {
						faultResult("回复内容不能为空，回复失败！");
					}else {
						boolean flag = msgService.replyMsg(messageId, bussId, content, userId, deptId);
						if(flag) {
							this.setAttr("success", true);
							Hashtable<String, Object> result = new Hashtable<String, Object>();
							result.put("message", "留言回复成功！");
							this.setAttr("result", result);
							this.setAttr("errorMsg", "");
							
							EbMessage msgInfo = msgService.getMsgByMsgId(messageId);
							
							//给消息表中插入数据
							String newsContent = userInfo.getUserName() + "回复了您的留言，回复内容：" + content;
							String newsTime = System.currentTimeMillis() + "";
							try {
								String departName = "";
								if(deptInfo != null){
									departName = deptInfo.getDepartName();
								}
								msgService.addNews(userInfo, bussInfo, "8", newsContent, null, 
										newsTime, null, null, "1", deptId, departName);
								if (userInfo.getRoleType().intValue() != 3) {
									//如果当前用户不是企业用户时就直接发送消息，
									//否则需要后台管理系统通过审核后才能发送通知消息
									List<String> strAlias = new ArrayList<String>();
									//如果是给企业留言的回复则通知企业、如果是某个非企业用户留言的回复
									EbUserInfo uInfo = uService.findById(msgInfo.getUserId() + "");
									if(uInfo.getRoleType().intValue() == 1) {
										if(msgInfo.getUserName().contains("政务办")) {
											strAlias.add(Config.ZWB);
										}else if (msgInfo.getUserName().contains("招商局")) {
											strAlias.add(Config.ZSJ);
										}else {
											strAlias.add(uInfo.getDepartId() + "");
										}
									}else if (uInfo.getRoleType().intValue() == 2) {
										strAlias.add(uInfo.getUserId() + "");
									}else {
										strAlias.add(uInfo.getBusinessId() + "");
									}
									
									Hashtable<String, String> map = new Hashtable<String, String>();
									map.put("newsType", "8");
									map.put("content", newsContent);
									map.put("status", "1");
									map.put("businessId", getCheckedString(bussInfo.getBusinessId() + ""));
									map.put("businessName", getCheckedString(bussInfo.getBusinessName()));
									map.put("userName", userInfo.getUserName());
									PushUtil.pushNotifyToTagert(strAlias, StringUtil.getJson(map));
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}else {
							faultResult("留言回复失败！");
						}
					}
				}
			}else if(isReply == 0) {	//留言
				if(content == null || content.trim().equals("") || "null".equalsIgnoreCase(content)) {
					faultResult("留言内容不能为空，留言失败！");
				}else {
					boolean flag = msgService.addMessage(bussId, content, userId, deptId);
					if(flag) {
						
						this.setAttr("success", true);
						Hashtable<String, Object> result = new Hashtable<String, Object>();
						result.put("message", "留言成功！");
						this.setAttr("result", result);
						this.setAttr("errorMsg", "");
						
						//给消息表中插入数据
						String newsContent = userInfo.getUserName() + "给您留了言，留言内容：" + content;
						String newsTime = System.currentTimeMillis() + "";
						try {
							//如果不是部门则不传递部门名称
							String departName = "";
							if(deptInfo != null){
								departName = deptInfo.getDepartName();
							}
							
							msgService.addNews(userInfo, bussInfo, "8", newsContent, null, 
									newsTime, null, null, "0", deptId, departName);
							if (userInfo.getRoleType().intValue() != 3) {
								//如果当前用户不是企业用户时就直接发送消息，
								//否则需要后台管理系统通过审核后才能发送通知消息
								List<String> strAlias = new ArrayList<String>();
								
								if(userInfo.getRoleType().intValue() == 1) {
									strAlias.add(bussInfo.getBusinessId() + "");
								}else if (userInfo.getRoleType().intValue() == 2) {
									strAlias.add(bussInfo.getBusinessId() + "");
								}else {
									if(departName.contains(MCubeAppConfig.getInstance().getZwb_name())) {
										strAlias.add(Config.ZWB);
//									}else if (departName.contains(MCubeAppConfig.getInstance().getZwb_name_city())) {
//										strAlias.add(Config.ZSJ);
									}else {
										strAlias.add(deptId);
									}
								}
								
								Hashtable<String, String> map = new Hashtable<String, String>();
								map.put("newsType", "8");
								map.put("content", newsContent);
								map.put("status", "0");
								map.put("businessId", getCheckedString(bussInfo.getBusinessId() + ""));
								map.put("businessName", getCheckedString(bussInfo.getBusinessName()));
								map.put("userName", userInfo.getUserName());
								PushUtil.pushNotifyToTagert(strAlias, StringUtil.getJson(map));
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}else {
						faultResult("留言失败！");
					}
				}
			}else {		//出现错误
				faultResult("isReply错误！");
			}
		}
		
		render(new JsonRender().forIE());
	}
	
	private void faultResult(String error) {
		this.setAttr("success", false);
		this.setAttr("result", null);
		this.setAttr("errorMsg", error);
	}
	
	/**
	 * 6企业服务评价
	 */
	public void evaluate(){
		zwb_id();
		String bussId = this.getPara("businessId");
//		int compGrade = 0;
		String strEva = this.getPara("departEvaluate");	//部门评价
		String userId = Config.getSessionUserID(this);
		
		String strCompEva = "{\"departEvaluate\":" + strEva + "}";
		
		if(bussId == null || "".equals(bussId) || "null".equalsIgnoreCase(bussId)) {
			faultResult("必须传递企业id，评价失败！");
		}else {
			EvaInterService evService = new EvaInterService();
			String evaTime = System.currentTimeMillis() + "";
			BussInterService bussService = new BussInterService();
		    EbBusiness buss = bussService.findById(bussId);
		    String bussName = "";
		    if(buss!=null){
		    	bussName = buss.getBusinessName();
		    }
		    try {
		    	JSONObject evalueComp = new JSONObject(strCompEva);
				JSONArray deptEvasComp = (JSONArray) evalueComp.optJSONArray("departEvaluate");
				if(deptEvasComp != null && deptEvasComp.length() > 0) {
//					for(int i = 0; i < deptEvasComp.length(); i++) {
//						int grade = deptEvasComp.optJSONObject(i).optInt("departGrade");
//						if(grade > 3) {
//							compGrade += grade;
//						}
//					}
//					compGrade = compGrade / deptEvasComp.length();
//					double cg = (double) compGrade / deptEvasComp.length();
//					if(cg != 0 && cg > compGrade) {
//						compGrade += 1;
//					}
//			    	if (compGrade == 0) {
//						compGrade = 5;
//					}
					
					EbEvaluate evaInfo = evService.getEvaByBid(bussId);
				    if(evaInfo == null) {
				    	//TODO 默认为0说明评价未审核
						evaInfo = evService.evalComp(bussId, "0", evaTime, bussName);
				    }else {
//						int val = evaInfo.getCompGrade().intValue();
//						compGrade = (compGrade + val) / 2;
//						double ceg = (double) (compGrade + val) / 2;
//						if (ceg != 0 && ceg > compGrade) {
//							compGrade += 1;
//						}
//						evaInfo.update();
					}
				    
				    if(evaInfo == null) {
						faultResult("综合评价数据插入失败，评价失败！");
					}else {
						
						String evaId = evaInfo.getEvaluateId() + "";
						boolean flag = true;
						String errorMsg = "部门评价失败";
						try {
							MsgInterService msgService = new MsgInterService();
							UserInterService uService = new UserInterService(this);
							EbUserInfo userInfo = uService.findById(userId);
							EbBusiness bussInfo = new BussInterService().findById(bussId);
							//开始部门评价
//							LogUtil.log("测试传递的json字符串："+strCompEva);
//							JSONObject evalue = new JSONObject(strCompEva);
//							JSONArray deptEvas = (JSONArray) evalue.optJSONArray("departEvaluate");
							for(int i = 0; i < deptEvasComp.length(); i++) {
								String deptId = deptEvasComp.optJSONObject(i).optString("departId");
								String grade = deptEvasComp.optJSONObject(i).optString("departGrade");
								String content = deptEvasComp.optJSONObject(i).optString("content");
								
								if (deptId == null || "".equals(deptId) || "null".equalsIgnoreCase(deptId)) {
									flag = false;
									errorMsg = "部门id不能为空，部门评价失败！";
									break;
								}else if(grade == null || "".equals(grade) || "null".equalsIgnoreCase(grade)) {
									continue;
								}else {
									DeptInterService deptSer = new DeptInterService();
									EbDepart dept = deptSer.getDeptById(deptId);
									if (dept == null) {
										dept = GovNetUtil.getDeptInfo(deptId);
									}
									
									String deptName = "";
									if(dept != null){
										deptName = dept.getDepartName();
									}
									EbDeptEvainfo evaDept = evService.evaDepart(evaId, grade, content, deptId, evaTime,deptName, bussId, bussName);
									
									if(evaDept == null) {
										errorMsg = "数据插入失败，部门评价失败！";
										continue;
									}else {
										//给消息表中插入数据
										String newsContent = bussInfo.getBusinessName() + "对" + deptName + "（部门）评价是：" + grade + "星";
										String newsTime = System.currentTimeMillis() + "";
										try {
											EbUserInfo signUser = uService.findById(bussInfo.getUserId() + "");
											if(signUser != null) {
												msgService.addNews(userInfo, bussInfo, "7", newsContent, null, 
														newsTime, null, null, "7", signUser.getDepartId() + "", 
														signUser.getDepartName());
												//政务办的也接收到部门评价消息
												String signDept = signUser.getDepartId() + "";
												String zwb_name = MCubeAppConfig.getInstance().zwb_name;
												String zwb_id = Config.getSessionZWBID(this);
												if(!signDept.equals(zwb_id)){
													msgService.addNews(userInfo, bussInfo, "7", newsContent, null, 
															newsTime, null, null, "7", zwb_id, 
															zwb_name);
												}
												List<String> strAlias = new ArrayList<String>();
												strAlias.add(Config.ZWB);
												newsContent = bussInfo.getBusinessName() + "（企业）已对" + deptName + "（部门）评价：" + grade + "星";
												
												Hashtable<String, String> map = new Hashtable<String, String>();
												map.put("newsType", "7");
												map.put("content", newsContent);
												map.put("status", "7");
												PushUtil.pushNotifyToTagert(strAlias, StringUtil.getJson(map));
											}
										} catch (Exception e) {
											e.printStackTrace();
										}
									}
								}
							}
							
							if(flag) {
								this.setAttr("success", true);
								Hashtable<String, Object> result = new Hashtable<String, Object>();
								result.put("message", "评价成功！");
								this.setAttr("result", result);
								this.setAttr("errorMsg", "");
							}else {
								faultResult(errorMsg);
							}
						} catch (Exception e) {
							faultResult("异常错误，评价失败！");
							e.printStackTrace();
						}
					}
				}else {
					faultResult("没有评价任何部门，评价失败！");
				}
			} catch (JSONException e) {
				e.printStackTrace();
				faultResult("json解析错误！");
			}
		}
		
		render(new JsonRender().forIE());
	}
	
	/**
	 * 7联系电话
	 */
	public void depart_contact_list(){
		String bussId = this.getPara("businessId");
		if(bussId == null || "".equals(bussId) || "null".equalsIgnoreCase(bussId)) {
			faultResult("缺少参数，获取联系电话失败！");
		}else {
			BussInterService bussService = new BussInterService();
			List<EbDepart> deptList = bussService.getDeptsByBussId(bussId);
			if(deptList == null) {
				faultResult("查询数据不存在！");
			}else {
				this.setAttr("success", true);
				Hashtable<String, Object> result = new Hashtable<String,Object>();
				List<Hashtable<String, Object>> contactList = new ArrayList<Hashtable<String,Object>>();
				for (int i = 0; i < deptList.size(); i++) {
					EbDepart dept = deptList.get(i);
					Hashtable<String, Object> contact = new Hashtable<String, Object>();
					contact.put("departId", getCheckedString(dept.getDepartId()+""));
					contact.put("departName", getCheckedString(dept.getDepartName().replace("\n", "")));
					String userCode = dept.getUserCode() + "";
					if(userCode == null || "".equals(userCode) || "null".equalsIgnoreCase(userCode)) {
						contact.put("contactName", "");
					}else {
						EbUserInfo userInfo = GovNetUtil.getUserByCode(userCode);
						if(userInfo != null) {
							contact.put("contactName", getCheckedString(userInfo.getUserName()));
							contact.put("email", getCheckedString(userInfo.getUserEmail()+""));
						}else {
							contact.put("contactName", "");
							contact.put("email", "");
						}
					}
//					contact.put("address", getCheckedString(dept.getWorkAddress()+""));
					contact.put("address", "海珠区石榴岗路480号海珠政务服务中心五楼");
					contact.put("contactPhone", getCheckedString(dept.getWorkTel()+""));
					
					contactList.add(contact);
				}
				
				result.put("contactList", contactList);
				this.setAttr("result", result);
				this.setAttr("errorMsg", "");
			}
		}
		
		render(new JsonRender().forIE());
	}
	
	/**
	 * 8修改密码,只能企业和领导用户可以修改密码
	 */
	public void change_password(){
		String oldPsw = this.getPara("oldPassword");
		String newPsw = this.getPara("newPassword");
		String userId = Config.getSessionUserID(this);
		
		UserInterService userService = new UserInterService(this);
		EbUserInfo userInfo = userService.findById(userId);
		if(userInfo == null) {
			faultResult("没有找到该用户，修改密码失败！");
		}else if (!userInfo.getPassword().equals(oldPsw)) {
			faultResult("旧密码错误，修改失败！");
		}else if(newPsw == null || "".equals(newPsw) || "null".equalsIgnoreCase(newPsw)) {
			faultResult("密码不能为空！");
		}else if(userInfo.getRoleType().intValue() == 1) {
			faultResult("该用户不能修改密码");
		}else {//并且用户不能是办事人员
			userInfo.set("password", newPsw);
			if(userInfo.update()) {
				this.setAttr("success", true);
				Hashtable<String, Object> result = new Hashtable<String, Object>();
				result.put("message", "密码修改成功!");
				this.setAttr("result", result);
				this.setAttr("errorMsg", "");
			}else {
				faultResult("数据库数据更新失败，密码修改失败！");
			}
		}

		render(new JsonRender().forIE());
	}
	
	/**
	 * 9获取企业列表
	 */
	public void businessList(){
		int page = this.getParaToInt("page", 1);
		String url = "/buss/bussList?page="+page;
		
		redirect(url);
	}
	
	/**
	 * 53获取企业落户统计数据
	 */
	public void stat(){
		String type = getPara("type");//需要的统计类型 1区级，0市级
		String url = "/stat/statItem?type=" + type;
		
		redirect(url);
	}
	
	/**
	 * 11事项列表
	 */
	public void itemList(){
		String status = getPara("status");
		String page = getPara("page");
		String url = "/appylist/itemList?status=" + status + "&page=" + page;
		
		redirect(url);
	}

	private String getCheckedString(String str){
		if((str != null) && (!str.equals("null"))){
			return str;
		}
		return "无";
	}

	/**
	 * 16特别程序列表
	 */
	public void procedureList(){
		//拦截器判断办事员登录
		String userId = Config.getSessionUserID(this);
		int page = this.getParaToInt("page", 1);
		
		TaskInterService spcSer = new TaskInterService();
		Page<EbSpcProgram> spcPage;
		
		UserInterService userService = new UserInterService(this);
		EbUserInfo userInfo = userService.findById(userId);
		if(userInfo == null) {
			faultResult("用户未登录！");
		}else{
			String userAuth = userInfo.getAuthority()+"";//权限标识（1VIP服务专员管理员、2VIP服务专员权限 、3单位首席权限、4部门跟办人权限、5市级服务专员权限、6 VIP服务专员管理员+单位首席权限、7 VIP服务专员+单位首席权限）（办事员才有权限）
			if(userAuth.equals("3")){
				//如果是其他部门只能看到自己部门特殊任务
				spcPage = spcSer.getProgramsByDeptId(page, userInfo.getDepartId()+"");
			}else{
				spcPage = spcSer.getAllPrograms(page);
			}	
				List<EbSpcProgram> spclist = spcPage.getList();
		
				boolean success = false;
				String message = "数据为空";
				
				if(spclist != null && !spclist.isEmpty()){
					success = true;
					message = "成功!";
					List<Hashtable<String, String>> result = new ArrayList<Hashtable<String, String>>();
					for(int i=0; i<spclist.size(); i++){
						EbSpcProgram spc = spclist.get(i);
						
						Hashtable<String, String> resultHash = new Hashtable<String, String>();
						//特别程序Id
						resultHash.put("programId", getCheckedString(spc.getProgramId()+""));
						//事项名称
						resultHash.put("itemName", getCheckedString(spc.getItemName()));
						//事项编号
						resultHash.put("itemNum", getCheckedString(spc.getItemId()+""));
						//部门名称
						resultHash.put("departName", getCheckedString(spc.getDepartName()));
						//部门Id
						resultHash.put("departId", getCheckedString(spc.getDepartId()+""));
						//事项办理期限
						resultHash.put("timeLimit", getCheckedString(spc.getTimeLimit()+""));
						//企业id
						resultHash.put("businessId", getCheckedString(spc.getBusinessId()+""));
						//企业名称
						resultHash.put("businessName", getCheckedString(spc.getBusinessName()));
						//审批状态（0 待审批、1 已批准、2未批准、4已继续办理）
						resultHash.put("approveStatus", getCheckedString(spc.getExamineStatus()+""));
						
						result.add(resultHash);
					}
					this.setAttr("result", result);
				}
				setAttr("total_numb", spcPage.getTotalRow());
				setAttr("current_page", spcPage.getPageNumber());
				setAttr("total_pages", spcPage.getTotalPage());
				setAttr("success", success);
				setAttr("message", message);
		}
		render(new JsonRender().forIE());
	}
	
	/**
	 * 17特别程序信息
	 */
	public void procedureInfo(){
		String programId = getPara("programId");
		
		TaskInterService spcSer = new TaskInterService();
		EbSpcProgram spc = spcSer.getById(programId);
		
		boolean success = false;
		String message = "数据为空";
		if(spc!=null){
			success = true;
			message = "成功!";
			Hashtable<String, String> result = new Hashtable<String, String>();
			//特别程序Id
			result.put("programId", getCheckedString(spc.getProgramId()+""));
			//事项名称
			result.put("itemName", getCheckedString(spc.getItemName()));
			//事项编号
			result.put("itemNum", getCheckedString(spc.getItemId()+""));
			//部门名称
			result.put("departName", getCheckedString(spc.getDepartName()));
			//部门Id
			result.put("departId", getCheckedString(spc.getDepartId()+""));
			//办理方式
			result.put("transactionWay", getCheckedString(spc.getTransactionWay()));
			//办理期限
			result.put("timeLimit", getCheckedString(spc.getTimeLimit()+""));
			//企业id
			result.put("businessId", getCheckedString(spc.getBusinessId()+""));
			//客户名称
			result.put("businessName", getCheckedString(spc.getBusinessName()));
			//联系地址
			result.put("address", getCheckedString(spc.getAddress()));
			//联系方式
			result.put("contactPhone", getCheckedString(spc.getContactPhone()));
			//联系人姓名
			result.put("contactName", getCheckedString(spc.getContactName()));
			//原因
			result.put("reason", getCheckedString(spc.getReason()));
			//备注
			result.put("comment", getCheckedString(spc.getConment()));
			//审批状态（0 待审批、1 已批准、2未批准）
			result.put("approveStatus", getCheckedString(spc.getExamineStatus()+""));
			//审批意见
			result.put("opinion", getCheckedString(spc.getOpinion()));
			
			this.setAttr("result", result);
		}
		setAttr("success", success);
		setAttr("message", message);
		render(new JsonRender().forIE());
	}
	
	/**
	 * 18.企业简介信息
	 */
	public void businessInfo(){
		String businessId = getPara("businessId");
		
		BussInterService bussSer = new BussInterService();
		EbBusiness buss = bussSer.getBussById(businessId);
		
		boolean success = false;
		String message = "数据为空";
		if(buss!=null){
			success = true;
			message = "成功!";
			Hashtable<String, String> result = new Hashtable<String, String>();
			//企业id
			result.put("businessId", getCheckedString(buss.getBusinessId()+""));
			//企业名称
			result.put("businessName", getCheckedString(buss.getBusinessName()));
			//注册资本
			result.put("registCapital", getCheckedString(buss.getRegistCapital() + ""));
			//主营类别
			result.put("operateScope", getCheckedString(buss.getOperateScope() + ""));
			//联系人姓名
			result.put("contactName", getCheckedString(buss.getContactName()));
			//联系方式
			result.put("contactPhone", getCheckedString(buss.getContactPhone()));
			//企业类型
			EbBusinessType bussTy = bussSer.getBsTypeById(buss.getTypeId()+"");
			result.put("typeId", getCheckedString(buss.getTypeId() + ""));
			
			if(bussTy!=null){
				String businessType = bussTy.getTypeName();
				result.put("itemType", getCheckedString(businessType));
			}else{
				result.put("itemType", "");
			}
			//邮箱
			result.put("bussEmail", getCheckedString(buss.getBussEmail() + ""));
			//公司网址
			result.put("webAddress", getCheckedString(buss.getWebAddress() + ""));
			//项目名称
			result.put("itemName", getCheckedString(buss.getProjectName()));
			//项目简介
			result.put("itemIntro",getCheckedString(buss.getProject_intro()));
			//企业简介
			result.put("businessIntro", getCheckedString(buss.getBusinessIntro() + ""));
			//公司地址
			result.put("bussAddress", getCheckedString(buss.getBussAddress() + ""));
			//企业落户情况
			result.put("settleStatus", getCheckedString(buss.getSettleStatus() + ""));
			//企业落户时间
			result.put("settleTime", getCheckedString(buss.getSettleTime() + ""));
			
			this.setAttr("result", result);
		}
		setAttr("success", success);
		setAttr("message", message);
		render(new JsonRender().forIE());
	}
	
	/**
	 * 19.企业类型配置列表
	 */
	public void typeConfiguration(){
//		int page = this.getParaToInt("page", 1);
		
		BussInterService busTypeSer = new BussInterService();
//		Page<EbBusinessType> taskListPage = busTypeSer.getAllBsType(page); 
		List<EbBusinessType> taskList = busTypeSer.getAllBsTypeList();//获取所有的企业类型列表
		
		boolean success = false;
		String message = "数据为空";
		
		if(taskList != null && !taskList.isEmpty()){
			success = true;
			message = "成功!";
			List<Hashtable<String, Object>> result = new ArrayList<Hashtable<String, Object>>();
			for(int i=0; i<taskList.size(); i++){
				EbBusinessType busType = taskList.get(i);
				
				Hashtable<String, Object> resultHash = new Hashtable<String, Object>();
				//企业类型Id
				resultHash.put("typeId", getCheckedString(busType.getTypeId()+""));
				//企业类型名称
				resultHash.put("typeName", getCheckedString(busType.getTypeName()));
				
				//部门列表departList
				//首先在企业类型配置表获取到所有的itemId,通过typeId
				//通过itemId在EBItem中取到事项对应的departId去重
				List<EbDepart> deptInfoLi = new ArrayList<EbDepart>();
				deptInfoLi = getDeptListByTypeId(busType.getTypeId()+"");//获取企业类型事项配置列表的去重部门列表
				
				//得到的departInfolist,读取部门Id和部门名称
				if(deptInfoLi.size()>0){
					List<Hashtable<String, Object>> deptliResult = new ArrayList<Hashtable<String, Object>>();
					for(int k=0;k<deptInfoLi.size();k++){
						EbDepart dep = deptInfoLi.get(k);
						
						Hashtable<String, Object> depHash = new Hashtable<String, Object>();
						
						if(dep!=null){
							//部门Id
							depHash.put("departId", getCheckedString(dep.getDepartId()+""));
							
							//部门名称
							depHash.put("departNama", getCheckedString(dep.getDepartName()));
							depHash.put("departType", getCheckedString(dep.getDepartType()+""));
							//事项列表items
							//通过部门id获取到其对应的事项id列表，EBitem表，读取itemId和itemName
							ItemInterService itemSer = new ItemInterService();
							//获取企业事项列表中所有事项集合
							List<EbTaskList> taskLists = itemSer.getItemsByDtId(dep.getDepartId()+"", busType.getTypeId() + "");
							
							if(taskLists.size()>0){
								List<Hashtable<String, String>> itemResult = new ArrayList<Hashtable<String, String>>();
								for(int z=0;z<taskLists.size();z++){
									EbTaskList tList = taskLists.get(z);
									
									Hashtable<String, String> itemHash = new Hashtable<String, String>();
									//事项Id
									itemHash.put("itemId", getCheckedString(tList.getItemId()+""));
									//事项名称
									itemHash.put("itemName", getCheckedString(tList.getItemName()+""));
									
									itemResult.add(itemHash);
								}
								depHash.put("items", itemResult);
							}else{
								depHash.put("items", "");
							}
						}
						
						deptliResult.add(depHash);
					}
					resultHash.put("departList", deptliResult);
				}else{
					resultHash.put("departList", "null");
				}
				result.add(resultHash);
			}
			this.setAttr("result", result);
		}
//		setAttr("current_page", taskListPage.getPageNumber());
//		setAttr("total_pages", taskListPage.getTotalPage());
		setAttr("success", success);
		setAttr("message", message);
		render(new JsonRender().forIE());
	}
	
	/**
	 * 19接口子函数，通过typeId查询到对应的deptList，去重获取企业类型配置列表的所有部门
	 * @param typeId 企业类型Id
	 * @return EbDepart
	 */
	public List<EbDepart> getDeptListByTypeId(String typeId){
		List<EbDepart> deptInfoLi = new ArrayList<EbDepart>();
		
		DeptInterService deptSer = new DeptInterService();
		List<String> deptIdList = new ArrayList<String>();
		ItemInterService itemSer = new ItemInterService();
		
		List<EbTaskList> taskLists = itemSer.findByTypeIDList(typeId);//获取企业类型事项配置表的事项id列表
		if(taskLists.size()>0){
			//先获取所有的部门Id
			for(int j=0;j<taskLists.size();j++){
				EbTaskList taskli = taskLists.get(j);
				String itemId = taskli.getItemId()+"";
				
				//通过itemId在EBItem中取到事项对应的departId去重
				EbItem itemInfo = null;
				
				//itemType为1时说明来自审批接口数据,为0则从本地事项表中获取
				String itemType = taskli.getItemType()+"";
				if(itemType.equals("1")) {
					itemInfo = GovNetUtil.getItemInfo(itemId);
				}else {
					itemInfo = itemSer.getItemById(itemId);
				}
				
				if(itemInfo == null) {
					continue;
				}
				String deptId = itemInfo.getDepartId()+"";
				
				if(deptIdList.size()>0){
					boolean com = false;
					for(int k=0;k<deptIdList.size();k++){
						//循环遍历看是否有相同的deptId
						if(deptId.equals(deptIdList.get(k))){
							com = true;
							break;
						}
					}
					if(!com){
						deptIdList.add(deptId);
						
						//根据部门列表将部门信息存入
						EbDepart dept = deptSer.getDeptById(deptId);
						deptInfoLi.add(dept);
					}
				}else{
					deptIdList.add(deptId);
					//根据部门列表将部门信息存入
					EbDepart dept = deptSer.getDeptById(deptId);
					deptInfoLi.add(dept);
				}
			}
		}
		return deptInfoLi;
	}
	
	/**
	 * 20.部门事项配置列表
	 */
	public void departConfiguration(){
		List<EbDepart> deptList = GovNetUtil.getDeparts(1, "9999", new HashMap<String, Integer>(), "");
		//获取所有的市级部门加入到部门列表
		DepartService dservice = new DepartService();
		List<EbDepart> cityDptList = dservice.getCityDept();
		for(EbDepart d: cityDptList){
			deptList.add(d);
		}
		
		boolean success = false;
		String message = "数据为空";
		
		if(deptList != null && !deptList.isEmpty()){
			success = true;
			message = "成功!";
			List<Hashtable<String, Object>> result = new ArrayList<Hashtable<String, Object>>();
			for(int i=0; i<deptList.size(); i++){
				EbDepart dept = deptList.get(i);
				
				Hashtable<String, Object> resultHash = new Hashtable<String, Object>();
				//部门Id
				resultHash.put("departId", getCheckedString(dept.getDepartId()+""));
				//部门名称
				resultHash.put("departName", getCheckedString(dept.getDepartName()));
				resultHash.put("departType", getCheckedString(dept.getDepartType()+""));//（1市级、2区级）
				//部门对应的事项列表items
				//通过部门id获取到其对应的事项id列表，EBitem表，读取itemId和itemName
				ItemInterService itemSer = new ItemInterService();
				List<EbItem> itemInfoList = itemSer.getItemsByDeptId(dept.getDepartId()+"");//从本地库中获取事项
				List<EbItem> itemInter = GovNetUtil.getEbItems(dept.getDepartId()+"", 1, 
						"9999", new HashMap<String, Integer>());//从审批接口中获取事项
				if (itemInfoList == null) {
					itemInfoList = new ArrayList<EbItem>();
				}
				if(itemInter != null) {
					itemInfoList.addAll(itemInter);
				}
				
				if(itemInfoList.size()>0){
					List<Hashtable<String, String>> itemResult = new ArrayList<Hashtable<String, String>>();
					for(int z=0;z<itemInfoList.size();z++){
						EbItem item = itemInfoList.get(z);
						
						Hashtable<String, String> itemHash = new Hashtable<String, String>();
						//事项Id
						itemHash.put("itemId", getCheckedString(item.getItemId()+""));
						//事项名称
						itemHash.put("itemName", getCheckedString(item.getItemName()+""));
						
						itemResult.add(itemHash);
					}
					resultHash.put("items", itemResult);
				}else{
					resultHash.put("items", "");
				}
				
				result.add(resultHash);
			}
			setAttr("result", result);
		}
		setAttr("success", success);
		setAttr("message", message);
		render(new JsonRender().forIE());
	}
	
	/**
	 * 21.企业事项配置列表
	 */
	public void itemConfiguration(){
		zwb_id();
		String businessId = getPara("businessId");
		
		TaskInterService taskSer = new TaskInterService();
		EbTask task = taskSer.getTaskByBussId(businessId);
		
		boolean success = false;
		String message = "数据为空";
		
		if(task != null){
			//所有列表共同参数
			String taskId = task.getTaskId()+"";
			String bussId = task.getBusinessId()+"";
			String bussName = task.getBusinessName() + "";
			//项目名称
			String itemName = "";
			BussInterService bussSer = new BussInterService();
			EbBusiness buss = bussSer.findById(bussId);
			EvaInterService evaService = new EvaInterService();
			if(buss!=null){
				itemName = buss.getProjectName();
			}
			
			//任务Id对应的任务分发表列表查看是否有任务分发
			List<EbTaskDistribute> taskDistrList = taskSer.getDistribListLi(task.getTaskId()+"");
			Hashtable<String, Object> result = new Hashtable<String, Object>();
			success = true;
			message = "成功!";
			
			//区级政务办
			Hashtable<String, String> table = new Hashtable<String, String>();
			System.out.print("打印session：+" + Config.getSessionZWBID(this));
			table.put("departId", Config.getSessionZWBID(this));
			table.put("departName", MCubeAppConfig.getInstance().getZwb_name());
			result.put("ZWBMsg", table);
			
			//市级政务办
			Hashtable<String, String> tableCity = new Hashtable<String, String>();
			DeptInterService deptSer = new DeptInterService();
			EbDepart d = deptSer.getDeptByName(MCubeAppConfig.getInstance().getZwb_name_city());//后台配置的市级政务办名称
			if(d != null){
				tableCity.put("cityDepartId", getCheckedString(d.getDepartId()+""));
				tableCity.put("cityDepartName", MCubeAppConfig.getInstance().getZwb_name_city());
				result.put("ZWBMsgByCity", tableCity);
			}
			//任务id
			result.put("taskId", taskId + "");
			//企业id
			result.put("businessId", bussId + "");
			//企业名称
			result.put("businessName", bussName + "");
			//项目名称
			result.put("itemName", itemName + "");
			
			//先取出部门列表，去重
			if(taskDistrList!=null && !taskDistrList.isEmpty()){
				List<String> deptList = new TaskInterDao().findDeptIdsByTaskId(taskId);
				
				//将取出来的部门Id列表判断任务签收表内容排序，每个部门有多个事项
				if(deptList.size()>0){
					List<Hashtable<String, Object>> deptResult = new ArrayList<Hashtable<String, Object>>();
					
					for(int j=0;j<deptList.size();j++){
						EbDepart dept = deptSer.getDeptById(deptList.get(j)+"");
						
						Hashtable<String, Object> resultHash = new Hashtable<String, Object>();
						
						if(dept!=null){
							//部门id
							resultHash.put("departId", getCheckedString(dept.getDepartId()+""));
							//部门名称
							resultHash.put("departName", getCheckedString(dept.getDepartName().replace("\n", "")));//要把特殊符号去掉
						
							resultHash.put("departType", getCheckedString(dept.getDepartType()+""));//（1市级、2区级）
							//查询是否有部门评价
							EbDeptEvainfo dptEva = evaService.getDtEVByBDid(businessId, getCheckedString(dept.getDepartId()+""));
						
							if(dptEva != null) {
								resultHash.put("isEvaluate", 1);
								resultHash.put("evaGrade", dptEva.getGrade() + "");
								resultHash.put("evaContent", dptEva.getContent() + "");
								resultHash.put("evaTime", TimeUtil.changeTimeStyle(dptEva.getTime()) + "");
							}else {
								resultHash.put("isEvaluate", 0);
								resultHash.put("evaGrade", "");
								resultHash.put("evaContent", "");
								resultHash.put("evaTime", "");
							}
						
						
							int isTransComp = 1;
							
							//事项分发表items
							List<EbTaskDistribute> taskDistrListforItem = taskSer.getDistrsByDBIDList(taskId, dept.getDepartId()+"");
						
							if(taskDistrListforItem.size()>0){
								List<Hashtable<String, String>> distrResult = new ArrayList<Hashtable<String, String>>();
								for(int z=0;z<taskDistrListforItem.size();z++){
									EbTaskDistribute distr = taskDistrListforItem.get(z);
									
									Hashtable<String, String> distrResultHash = new Hashtable<String, String>();
									//事项分发id
									distrResultHash.put("distrId", getCheckedString(distr.getDistrId() + ""));
									//判断该事项是否上传了图片
									ImgService imgSer = new ImgService();
									List<EbFileImg> images = imgSer.findImgByDistr(distr.getDistrId() + "");
									if(images.size()>0){
										distrResultHash.put("is_have_img", "1");//1是有图片
									}else{
										distrResultHash.put("is_have_img", "0");//1是有图片
									}
									//事项id
									distrResultHash.put("itemId", getCheckedString(distr.getItemId()+""));
									//事项名称
									distrResultHash.put("itemName", distr.getItemName() + "");
									
									int time_limit = 15;//事项办理期限（多少个工作日）
									ItemInterService itemSer = new ItemInterService();
									EbItem item = itemSer.getItemById(distr.getItemId()+"");
									if(item == null) {
										item = GovNetUtil.getItemInfo(distr.getItemId() + "");
									}
									
									if(item!=null){
										
										if(item.getTimeLimit() != null && MD5Util.isNumeric(item.getTimeLimit() + "")) {
											if(item.getTimeLimit().intValue() <= 0) {
												time_limit = 15;
											}else {
												time_limit = item.getTimeLimit().intValue();
											}
										}
									}
									distrResultHash.put("timeLimit", time_limit+"");
									//任务id
									distrResultHash.put("taskId", getCheckedString(distr.getTaskId()+""));
									//签收状态 0 未签收、1 已签收、2 被退回
									distrResultHash.put("signStatus", getCheckedString(distr.getSignStatus()+""));
									//签收时间
									distrResultHash.put("signTime", TimeUtil.changeTimeStyle(distr.getSignTime()));
									//签收用户id
									distrResultHash.put("signUserId", getCheckedString(distr.getSignUserId()+""));
									
									//签收用户名称
									UserInterService userSer = new UserInterService(this);
									EbUserInfo user = userSer.findById(distr.getSignUserId()+"");
	//								EbUserInfo user = GovNetUtil.getUserByCode(distr.getSignUserCode());
									if(user!=null){
										distrResultHash.put("signUserName", getCheckedString(user.getUserName()));
									}else{
										distrResultHash.put("signUserName", "");
									}
									
									//（0 首席已分发未开始、1 办理中、2 已逾期、3 办理完结、4 被退回 、5暂停、6首席未分发）
									distrResultHash.put("transactionStatus", getCheckedString(distr.getTransactionStatus()+""));
									if(distr.getTransactionStatus().intValue() != 3 && distr.getTransactionStatus().intValue() != 4) {
										//事项的办理状态不是办理完结或被退回状态时,则认为该部门事项未办理完
										isTransComp = 0;
									}
									
									if(distr.getTransactionStatus().intValue() == 5) {
										//事项的办理状态是暂停状态时,则认为该部门事项未办理完
										isTransComp = 0;
										//并且给对应特殊任务原因
										String distrId = distr.getDistrId()+"";
										EbSpcProgram spc = taskSer.getByDistriId(distrId);
										if(spc != null){
											distrResultHash.put("stopReson", spc.getReason() + "");
										}else{
											distrResultHash.put("stopReson","");//暂停原因
										}
									}else{
										distrResultHash.put("stopReson","");
									}
									
									distrResultHash.put("backReason", distr.getReturnReason() + "");
									
									//办理人id
									distrResultHash.put("transactorId", getCheckedString(distr.getTransactorId()+""));
									distrResultHash.put("transactorCode", getCheckedString(distr.getTransUserCode()));
									
									//办理人姓名
									EbUserInfo userTra = userSer.findById(distr.getTransactorId()+"");
	//								EbUserInfo userTra = userSer.findByCode(distr.getTransUserCode());
									if(userTra!=null){
										distrResultHash.put("transactorName", getCheckedString(userTra.getUserName()));
									}else{
										distrResultHash.put("transactorName", "");
									}
									
									//办理时间
									distrResultHash.put("transTime", TimeUtil.changeTimeStyle(distr.getTransactionTime()+""));
									//任务分发人id
									distrResultHash.put("distributeUserId", getCheckedString(distr.getUserId()+""));
									
									//任务分发人姓名
									EbUserInfo userDistr = userSer.findById(distr.getUserId() + "");
	//								EbUserInfo userDistr = userSer.findByCode(distr.getDistrUserCode());
									if(userDistr!=null){
										distrResultHash.put("distrDeptId", userDistr.getDepartId() + "");
										distrResultHash.put("distrDeptName", userDistr.getDepartName() + "");
										distrResultHash.put("distributeUserName", getCheckedString(userDistr.getUserName()));
									}else{
										distrResultHash.put("distrDeptId", "");
										distrResultHash.put("distrDeptName",  "");
										distrResultHash.put("distributeUserName", "");
									}
									
									distrResultHash.put("distrTime", TimeUtil.getTimeStyle(distr.getDistribTime(), "yyyy-MM-dd HH:mm") + "");	//事项分发时间
									
									distrResult.add(distrResultHash);
								}
								resultHash.put("items", distrResult);
							}else{
								resultHash.put("items", "");
							}
							resultHash.put("isTransComp", isTransComp);
							deptResult.add(resultHash);
						}	
					}
					result.put("departments",deptResult);
				}
			}else{
				result.put("departments","null");
			}
			this.setAttr("result", result);
		}
		setAttr("success", success);
		setAttr("message", message);
		render(new JsonRender().forIE());
	}
	
	/**
	 * 22.项目反馈
	 */
	public void feedbackList(){
		String businessId = getPara("businessId");
		String departId = getPara("departId");//可为空传递
		String itemId = getPara("itemId");//可为空值传递
		
		//根据企业id查询事项反馈消息表EB_NEWS
		MsgInterService newSer = new MsgInterService();
		List<EbNews> newslist = newSer.getNewsToFB(businessId,departId,itemId);
		
		boolean success = false;
		String message = "数据为空";
		
		if(businessId != null){
			if(newslist != null && !newslist.isEmpty()){
				List<Hashtable<String, Object>> result = new ArrayList<Hashtable<String, Object>>();
				for(int i=0; i<newslist.size(); i++){
					EbNews news = newslist.get(i);
					
					//判断是否有反馈内容
					String coupback = news.getCoupleBack()+"";
					if(coupback != null && !("".equals(coupback)) && !("null".equals(coupback))){
						success = true;
						message = "成功!";
						Hashtable<String, Object> resultHash = new Hashtable<String, Object>();
						//用户id
						resultHash.put("userId", getCheckedString(news.getUserId()+""));
						DeptInterService deptSer = new DeptInterService();
						//用户名称、先从本地用户表中查询、如果没有再从审批接口中查询
						UserInterService userSer = new UserInterService(this);
						EbUserInfo user = userSer.findById(news.getUserId() + "");
						if (user == null) {
							user = GovNetUtil.getUserByCode(news.getUserCode());
						}
						if(user!=null){
							String userDeptId = news.getDepartId()+"";
							EbDepart dept = deptSer.getDeptById(userDeptId);
							if(dept == null) {
								dept = GovNetUtil.getDeptInfo(userDeptId);
							}
							if(dept!=null){
								resultHash.put("userName", dept.getDepartName()+getCheckedString(user.getUserName()));
							}else {
								resultHash.put("userName", user.getUserName());
							}
						}else{
							resultHash.put("userName", "");
						}
						
						//事项状态
						resultHash.put("status", getCheckedString(news.getStatus()+""));
						//消息内容
						resultHash.put("content", getCheckedString(news.getNewsContent()));
						
						//事项id
						resultHash.put("itemId", getCheckedString(news.getItemId()+""));
						
						//事项分发id
						String ditemId = news.getItemId()+"";
						String dbussId = news.getBusinessId()+"";
						String taskId = "";
						TaskInterService taskSer = new TaskInterService();
						EbTask task = taskSer.getTaskByBussId(dbussId);
						if(task != null){
							taskId = task.getTaskId()+"";
						}
						EbTaskDistribute distr = taskSer.getDistrByTIId(taskId, ditemId);
						if(distr != null){
							resultHash.put("distrId", getCheckedString(distr.getDistrId()+""));
						}
						
						//部门电话
						String deptId = news.getDepartId()+"";
						String deptTel = "";
						EbDepart dept = deptSer.getDeptById(deptId);
						if(dept == null) {
							dept = GovNetUtil.getDeptInfo(deptId);
						}
						if(dept!=null){
							deptTel =dept.getWorkTel();
						}
						resultHash.put("deptTel",getCheckedString(deptTel)); 
						
						//事项名称
						resultHash.put("itemName", getCheckedString(news.getItemName()+""));
						//反馈时间
						resultHash.put("coupleTime", TimeUtil.getTimeStyle(news.getNewsTime(), "yyyy-MM-dd HH:mm"));
						
							//反馈内容
						resultHash.put("coupleBack", getCheckedString(news.getCoupleBack()));
						result.add(resultHash);
					}
				}
				this.setAttr("result", result);
			}
		}
		setAttr("success", success);
		setAttr("errorMsg", message);
		render(new JsonRender().forIE());
	}
	
	/**
	 * 24.项目批示表（领导列表）
	 */
	public void leaderList(){
		String businessId = getPara("businessId");
		String userId = Config.getSessionUserID(this);//用户自己
		
		boolean success = false;
		String message = "数据为空";
		
		List<Hashtable<String, String>> result = new ArrayList<Hashtable<String, String>>();
		if(businessId!=null){
			
			MsgInterService mesSer = new MsgInterService();
			List<String> userIdAll = mesSer.getUserIdBybussId(businessId);
//			List<String> userIdAll = mesSer.getUCBybussId(businessId);//获取所有的userCode集合
			
			if(userIdAll!= null && !userIdAll.isEmpty()){
				
				//获取该企业下的所有留言用户
				List<EbUserInfo> userList = new ArrayList<EbUserInfo>();
				UserInterService userSer = new UserInterService(this);
				for(int i=0; i<userIdAll.size(); i++){
//					String userCode = userIdAll.get(i);
//					EbUserInfo userInfo = userSer.getUserByCode(userCode);
					String subUId = userIdAll.get(i);
					EbUserInfo userInfo = userSer.getUserByUId(subUId);
					//获取所有的领导留言用户	
					if(userInfo!=null){
//						String roleType = userInfo.getRoleType()+"";
						if(!subUId.equals(userId)){
							userList.add(userInfo);
						}
					}
				}
				//将领导用户筛选出来放到userList
				if(userList.size()>0){
					success = true;
					message = "成功!";
					for(int j=0;j<userList.size();j++){
						EbUserInfo user = userList.get(j);
						
						Hashtable<String, String> resultHash= new Hashtable<String, String>();
						//用户id
						resultHash.put("userId", getCheckedString(user.getUserId()+""));
						
						//用户名称
						DeptInterService deptSer = new DeptInterService();
						EbDepart dept = deptSer.getDeptById(user.getDepartId()+"");
						
						if(dept!=null){
							resultHash.put("userName", dept.getDepartName()+getCheckedString(user.getUserName()));
						}else{
							resultHash.put("userName", getCheckedString(user.getUserName()));
						}
						result.add(resultHash);			
					}
				}
			}
		}else{
			message = "传递参数不完整！";
		} 
		this.setAttr("result", result);
		setAttr("success", success);
		setAttr("message", message);
		render(new JsonRender().forIE());
	}
	
	/**
	 * 25.项目批示（批示列表）
	 * @throws ParseException 
	 */
	public void approveList() throws ParseException{
		String businessId = getPara("businessId");//企业id
		String instrTime = getPara("instrTime");//筛选条件、按时间 （week 本周 、 month 本月 、 year 全年）
		String userId = getPara("userId");//用户id 如果为空字符串则表示筛选全部领导的批示(新增需求还有政务办的批示)
//		String userId = Config.getSessionUserID(this);
		
		boolean success = false;
		String message = "数据为空";
		//判断筛选时间
		long start_time = 0;
		//当前时间毫秒数
		long end_time = System.currentTimeMillis();
		if(instrTime != null){
			if(instrTime.equals("week")){
				//本周周一时间毫秒数
				start_time = TimeUtil.getCurrentMondayTime();
			}else if(instrTime.equals("month")){
//				start_time = TimeUtil.getMinMonthDateTime();
				Date nowDate = new Date();
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM");
				String date = df.format(nowDate)+"-01";
				start_time =  TimeUtil.getMinYearDateTime(date);
			}else if(instrTime.equals("year")){
				Date nowDate = new Date();
				SimpleDateFormat df = new SimpleDateFormat("yyyy");
				String date = df.format(nowDate)+"-01-01";
				start_time = TimeUtil.getMinYearDateTime(date);
			}
			
			System.out.print("初始毫秒数：" + start_time + "; 当前日期毫秒数:"+end_time);
			
			//项目名称与企业名称
			String projectName = "";
			String businessName = "";
			
			if(businessId != null){
				BussInterService bussSer = new BussInterService();
				EbBusiness buss = bussSer.findById(businessId);
				if(buss!=null){
					projectName = buss.getProjectName() + "";
					businessName = buss.getBusinessName();
				}
				
				List<Hashtable<String, Object>> result = new ArrayList<Hashtable<String, Object>>();
				MsgInterService mesSer = new MsgInterService();
				List<String> userIdAll = mesSer.getUserIdBybussId(businessId);//获取所有该企业关联的留言
				
				if(userIdAll!= null && !userIdAll.isEmpty()){
					
					//获取该企业下的所有留言用户
					List<EbUserInfo> userList = new ArrayList<EbUserInfo>();
					UserInterService userSer = new UserInterService(this);
					if(userId == null || userId.equals("") || userId.equalsIgnoreCase("null")){
						//如果传来的用户Id是空的，则显示全部的领导
						for(int i=0; i<userIdAll.size(); i++){
							String subUId = userIdAll.get(i);
							EbUserInfo userInfo = userSer.getUserByUId(subUId);
							if(userInfo!=null){
//								String roleType = userInfo.getRoleType()+"";
//								if(roleType.equals("2") && !subUId.equals(userId)){
//									userList.add(userInfo);
//								}
								userList.add(userInfo);
							}
						}
					}else{
						//如果传来的用户不为空，则显示单个领导或政务办人员
						EbUserInfo userInfo = userSer.getUserByUId(userId);
						userList.add(userInfo);
					}
					//将企业相关用户筛选出来放到userList
					if(userList.size()>0){
						for(int j=0;j<userList.size();j++){
							EbUserInfo user = userList.get(j);
							
							//用户id
							String uIdForResult = user.getUserId()+"";
							
							//用户名称
							String uNameForResult = "";
							DeptInterService deptSer = new DeptInterService();
							EbDepart dept = deptSer.getDeptById(user.getDepartId()+"");
							
							if(dept!=null){
								uNameForResult = dept.getDepartName()+ user.getUserName();
							}else{
								uNameForResult = user.getUserName();
							}
							
							//一个领导可以有很多个留言
							//现获取留言列表
							MsgInterService msgSer = new MsgInterService();
							String _ID = user.getUserId() + "";
							List<EbMessage> messLi = msgSer.getMessageByUIdBIdList(_ID, 
									businessId, start_time, end_time);
							
							if(messLi!=null && !messLi.isEmpty()){
								success = true;
								message = "成功!";
								
								for(int k=0;k<messLi.size();k++){
									EbMessage mes = messLi.get(k);
									Hashtable<String, Object> resultHash= new Hashtable<String, Object>();
									//用户id
									resultHash.put("userId", getCheckedString(uIdForResult));
									//用户名称
									resultHash.put("userName",getCheckedString(uNameForResult));
									//企业Id
									resultHash.put("businessId", getCheckedString(businessId));
									//企业名称
									resultHash.put("businessName", getCheckedString(businessName));
									//项目名称
									resultHash.put("projectName", getCheckedString(projectName));
									//留言时间
									resultHash.put("instrTime", TimeUtil.changeTimeStyle(mes.getMessageTime()));
									//事项Id
									resultHash.put("itemId", getCheckedString(mes.getItemId()+""));
									
									//事项名称
									ItemInterService itemser = new ItemInterService();
									EbItem item = itemser.getItemById(mes.getItemId()+"");
									if(item == null){
										item = GovNetUtil.getItemInfo(mes.getItemId()+"");
									}
									
									if(item != null){
										resultHash.put("itemName", getCheckedString(item.getItemName()));
									}
									
									//获取留言相关部门id和部门名称
									String departId = mes.getDepartId()+"";
									String departName = "";
									EbDepart dep = deptSer.getDeptById(departId);
									if(dep != null){
										departName = dep.getDepartName();
									}else{
										departName = businessName;//暂定当办事员给企业留言时，显示留言对象用这个字段
									}
									
									if(dept!=null){
										String dId = dept.getDepartId()+"";
										if(dId.equals(departId)){
											departName = businessName;//暂定当办事员给企业留言时，显示留言对象用这个字段
										}
									}
									resultHash.put("departName", departName);
									resultHash.put("departId", getCheckedString(mes.getDepartId()+""));
									
									//批示内容，留言内容
									resultHash.put("instrContent", getCheckedString(mes.getMessageContent()));
									//留言Id
									resultHash.put("messageId", getCheckedString(mes.getMessageId()+""));
									
									//留言回复列表replyList
									List<EbMessage> replyli = msgSer.getAllReplyList(mes.getMessageId()+""); 
									List<Hashtable<String, String>> replyResult = new ArrayList<Hashtable<String, String>>();
									if(replyli.size()>0){
										for(int z=0;z<replyli.size();z++){
											EbMessage reply = replyli.get(z);
											Hashtable<String, String> replyHash= new Hashtable<String, String>();
											
											//回复Id
											replyHash.put("paramId", getCheckedString(reply.getParamId()+""));
											//回复内容
											replyHash.put("replyContent", getCheckedString(reply.getMessageContent()));
											//回复时间
											replyHash.put("replayTime", TimeUtil.changeTimeStyle(reply.getMessageTime()));
											//回复用户id
											replyHash.put("replyUserId", getCheckedString(reply.getUserId()+""));
											//回复用户姓名
											EbDepart dept2 = deptSer.getDeptById(reply.getDepartId()+"");
											String deptNameToRe = "";
											if(dept2!=null){
												uNameForResult = dept2.getDepartName();
											}
											replyHash.put("replyUserName", deptNameToRe + getCheckedString(reply.getUserName()));
											
											replyResult.add(replyHash);
										}
										resultHash.put("replyList", replyResult);
									}else{
										resultHash.put("replyList", "");
									}
									result.add(resultHash);	
								}
							}
							setAttr("result", result);
						}
					}
				}else{
					message = "没有查询到数据";
				}
			}else{
				message = "缺少企业Id参数";
			}
		}else{
			message = "没有传递时间参数！";
		}
		setAttr("success", success);
		setAttr("message", message);
		render(new JsonRender().forIE());
	}
	
	/**
	 * 26.项目批示（批示回复）领导回复
	 */
	public void approveReply(){
		String messageId = getPara("messageId");//回复对应的留言Id
		String content = getPara("content");//回复内容
		String userId = Config.getSessionUserID(this);//登录的领导用户
		
		boolean success = false;
		String resultMsg = "提交不成功，请重试！";
		
		if(messageId == null || messageId.trim().equals("") || "null".equalsIgnoreCase(messageId)) {
			resultMsg ="留言Id不能为空，回复失败！";
			setAttr("errorMsg", resultMsg);
		}else {
			if(content == null || content.trim().equals("") || "null".equalsIgnoreCase(content)) {
				resultMsg ="回复内容不能为空，回复失败！";
				setAttr("errorMsg", resultMsg);
			}else {
				//取出留言相关信息
				MsgInterService msgSer = new MsgInterService();
				String business_id = "";//企业Id
				String item_id = "";//事项id
				String business_name = "";//企业名称
				EbMessage msg = msgSer.getMsgByMsgId(messageId);
				if(msg!=null){
					business_id = msg.getBusinessId()+"";
					item_id = msg.getItemId()+"";
					business_name = msg.getBusinessName();
				}
				//取出回复的用户信息
				UserInterService userSer = new UserInterService(this);
				EbUserInfo userInfo = userSer.findById(userId);
				EbBusiness bussInfo = new BussInterService().findById(msg.getBusinessId() + "");
				
				String userDeptId = "";
				String userName = "";
				boolean flag = false;
				if(userInfo!=null){
					userDeptId = userInfo.getDepartId()+"";
				    userName = userInfo.getUserName();
				}
				flag = msgSer.addReplyByLeader(messageId, content, userId, userDeptId, userName, 
						business_id, business_name, item_id);
				if(flag){
					success = true;
					String message = "回复成功";
					Hashtable<String, String> result= new Hashtable<String, String>();
					result.put("message", message);
					setAttr("result", result);
					
					String deptId = "";
					String deptName = "";
					EbDepart deptInfo = new DeptInterService().getDeptById(msg.getDepartId() + "");
					if (deptInfo != null) {
						deptId = deptInfo.getDepartId() + "";
						deptName = deptInfo.getDepartName();
					}
					
					String itemId = "";
					String itemName = "";
					EbItem itemInfo = new ItemInterService().getItemById(msg.getItemId() + "");
					if (itemInfo != null) {
						itemId = itemInfo.getItemId() + "";
						itemName = itemInfo.getItemName();
					}
					EbMessage msgInfo = msgSer.getMsgByMsgId(msg.getParamId() + "");
					
					//TODO 消息推送
					try {
						String newsTime = System.currentTimeMillis() + "";
						String newsContent = itemName;
						if(msgInfo != null) {
							newsContent = userInfo.getUserName() + "回复了" + msgInfo.getUserName() + "的留言，回复内容：" + content;
						}
						
						boolean flg = msgSer.addNews(userInfo, bussInfo, "9", newsContent, 
								content, newsTime, itemId, itemName, "9", deptId, deptName);
						
						if(flg) {
							List<String> strAlias = new ArrayList<String>();
							strAlias.add(msgInfo.getUserId() + "");
							
							Hashtable<String, String> map = new Hashtable<String, String>();
							map.put("newsType", "9");
							map.put("content", newsContent);
							map.put("status", "9");
							map.put("businessId", getCheckedString(bussInfo.getBusinessId() + ""));
							map.put("businessName", getCheckedString(bussInfo.getBusinessName()));
							PushUtil.pushNotifyToTagert(strAlias, StringUtil.getJson(map));
						}else {
							logger.error("消息插入数据库失败");
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}else{
					setAttr("errorMsg", resultMsg);
				}
			}
		}
		setAttr("success", success);
		render(new JsonRender().forIE());
	}
	
	/**
	 * 38 待办事项预警提示
	 */
	public void trans_warning() {
		int total = 0;
		String userId = Config.getSessionUserID(this);
		UserInterService userService = new UserInterService(this);
		EbUserInfo userInfo = userService.findById(userId);
		if(userInfo == null) {
			faultResult("未找到用户信息！");
		}else if(userInfo.getDepartId() == null || userInfo.getDepartId().equals("") 
				|| userInfo.getDepartId().toString().equalsIgnoreCase("null")) {
			faultResult("无法查询到相关信息！");
		}else {
			ItemInterService itemService = new ItemInterService();
			//根据部门id和办理状态获取该状态的事项列表（ 0 未开始 、1 办理中 、 2 已逾期 、 3 办理完结 、 4 被退回 ）
			List<EbTaskDistribute> list = itemService.getItemsByDId_Status(userInfo.getDepartId() + "", 1);
			if (list == null) {
				faultResult("未查询到数据信息！");
			}else {
				List<Hashtable<String, Object>> result = new ArrayList<Hashtable<String,Object>>();
				String currTime = System.currentTimeMillis() + "";
				for(int i = 0; i < list.size(); i++){
					Hashtable<String, Object> table = new Hashtable<String, Object>();
					EbTaskDistribute distr = list.get(i);
					EbItem item = itemService.getItemById(distr.getItemId() + "");
					if(item == null) {
						item = GovNetUtil.getItemInfo(distr.getItemId() + "");
					}
					if(item != null) {
						long days = TimeUtil.calcWorkDaysByString(distr.getSignTime(), currTime, "yyyy-MM-dd") / (24 * 3600000);
						
						long limitTime = 15;
						if(item != null && item.getTimeLimit() != null) {
							limitTime = item.getTimeLimit().longValue();
						}
						long gapDays = limitTime - days;
						if(gapDays <= 1 && gapDays >= 0) {
							String itemId = item.getItemId() + "";
							table.put("itemId", itemId);
							table.put("distrId", getCheckedString(distr.getDistrId()+""));
							table.put("itemName", getCheckedString(item.getItemName()));
							table.put("expireTime", gapDays);
							result.add(table);
							total++;
						}else {
							continue;
						}
					}else {
						continue;
					}
				}
				String warnContent = "本单位有"+ total +"个代办事项还有1天到期，请迅速办理";
				this.setAttr("warnContent", warnContent);
				this.setAttr("total_expireTime","1");
				this.setAttr("total_numb",total);
				this.setAttr("success", true);
				this.setAttr("result", result);
				this.setAttr("errorMsg", "");
			}
		}
		
		render(new JsonRender().forIE());
	}
	
	/**
	 * 50 完成事项上传图片文件接口
	 * @throws IOException 
	 */
	@Clear
	public void uploadImg() throws IOException{
		UploadFile uploadFile = getFile();
		String imageName= "";//初始化图片id
		
		String taskId = this.getPara("taskId");//任务Id
		String itemId = this.getPara("itemId");//事项Id
		String distrId = this.getPara("distrId");//分发任务Id
		//根据任务id和事项id,以及用户id，标记事项完成
		TaskInterService taskSer = new TaskInterService();
		EbTaskDistribute taskD =  taskSer.getDistrByTIId(taskId, itemId, distrId);
		
		boolean success = false;
		String message = "上传失败";
		Hashtable<String, Object> result = new Hashtable<String, Object>();
		
		if((taskId==null) || taskId.equals("") || "null".equalsIgnoreCase(taskId)){
			message="请传递任务id";
		}else if((itemId==null) || itemId.equals("") || "null".equalsIgnoreCase(itemId)){
			message="请传递事项id";
		}else if((distrId==null) || distrId.equals("") || "null".equalsIgnoreCase(distrId)){
			message="请传递分发任务id";
		}else if(uploadFile == null){
			message="获取不到文件";
		}else if(taskD == null){
			message="获取不到事项";
		}else{
			// 先保存图片
			byte[] bs = FileHelper.toBytes(uploadFile.getFile());
			String nameImg = uploadFile.getFileName();
			String[] strs=nameImg.split("\\.");
			System.out.print("这是图片名："+strs[0]);
			imageName = strs[0];
			//检测数据库里图片的id是否已经存在
			ImgService imgSer = new ImgService();
			EbFileImg img = imgSer.findImg(imageName);
			
			if(img == null){
				img = new EbFileImg();
				//建立文件路径
				File dirFile = new File(MCubeAppConfig.getInstance().getImagUrl());
				if(!dirFile.exists()){
					dirFile.mkdirs();
				}
				
				File desFile = new File(dirFile, imageName);//定义文件名
				boolean success2 = uploadFile.getFile().renameTo(desFile);//jfinal批量上传文件时重命名
				
				if(success2){
					Record rc = null;
					try {
						rc = Db.findFirst("select EB_FILE_seq.nextval IMAGE_ID from dual");
					} catch (Exception e) {
						e.printStackTrace();
					}
					if (rc == null) {
						message = "保存错误！";
					}else {
						img.set("IMAGE_ID", rc.get("IMAGE_ID"));
						img.set("IMAGE_NAME", taskD.getTransUserCode()+rc.get("IMAGE_ID"));
						img.setDistrId(new BigDecimal(distrId));//一个图片对应一个任务分发表事项
						String imageUrl = MCubeAppConfig.getInstance().baseUrl;
						imageUrl += "/img/icon?imageName="+taskD.getTransUserCode()+rc.get("IMAGE_ID");
						img.setImageUrl(imageUrl);
						img.set("IMAGE_CONTEN",bs);
						if(img.save()){
							success = true;//上传成功
							message = "上传成功！";
						}
					}
				}
			}
		}
		
		result.put("imageName",imageName);//返回图片名称
		result.put("message", message);
		
		setAttr("success", success);
		setAttr("result", result);
		render(new JsonRender().forIE());
	}
	
	/**
	 * 51 图片列表(只要传递任务分发表内的imageName就能返回图片)
	 */
	public void imgViewList(){
		String distrId = this.getPara("distrId");
		String url = "/img?distrId=" + distrId;
		redirect(url);
	}
	
	/**
	 * 52 图片展示(只要传递任务分发表内的imageName就能返回图片)
	 */
	public void imgView(){
		String imageName = this.getPara("imageName");
		String url = "/img/icon?imageName="+imageName;
		redirect(url);
	}
	
	/**
	 * 通知公告列表
	 */
	public void noticeList(){
		int page = this.getParaToInt("page",1);
		String userCode = Config.getSessionUserAccount(this);
		
		UserInterService userSer = new UserInterService(this);
		EbUserInfo user = userSer.findByCode(userCode);
		if(user == null){
			user = GovNetUtil.getUserByCode(userCode);
		}
		
		NoticeService noSer = new NoticeService();
		Page<EbNotice> noticePage = noSer.getAllNoticeByApp(page);
		
		if(user != null){
			String role_type = user.getRoleType()+"";
			if(role_type.equals(1) || role_type.equals(4) || role_type.equals(5)){
				//部门
				noticePage = noSer.getAllNoticeByType(page, 1);
			}else if(role_type.equals(3)){
				noticePage = noSer.getAllNoticeByType(page, 2);
			}
		}
		
		List<EbNotice> notices = noticePage.getList();
		if(notices.size()>0){
			for(int i=0;i<notices.size();i++){
				EbNotice notice = notices.get(i);
				String newTime = TimeUtil.getTimeStyle(notice.getNotice_time(),"yyyy-MM-dd");
				notice.setNotice_time(newTime);
			}
		}
		boolean success = false;
		String message = "数据为空";
		List<Hashtable<String, String>> result = new ArrayList<Hashtable<String,String>>();
		if(notices.size()>0){
			success = true;
			message = "";
			for(int i = 0; i < notices.size(); i++){
				EbNotice notice = notices.get(i);
				Hashtable<String, String> resultHash = new Hashtable<String, String>();
				resultHash.put("noticeId", getCs(notice.getNotice_id()+""));//通知公告Id
				resultHash.put("noticeTitle", getCs(notice.getNotice_title()));//通知公告主题
				resultHash.put("noticeTime", getCs(notice.getNotice_time()+""));//通知公告时间
				resultHash.put("userCode", getCs(notice.getNotice_user_code()));//通知公告发布账户
				resultHash.put("userName", getCs(notice.getNotice_user_name()));//发布人姓名
				resultHash.put("noticeType", getCs(notice.getNotice_type()+""));//通知公告类型
				
				result.add(resultHash);
			}
		}
		this.setAttr("current_page", noticePage.getPageNumber());
		this.setAttr("total_numb", noticePage.getTotalRow());
		this.setAttr("total_page", noticePage.getTotalPage());
		this.setAttr("success", success);
		this.setAttr("result", result);
		this.setAttr("errorMsg", message);
		render(new JsonRender().forIE());
	}
	/**
	 * 检查字符串是否为空，为空则传递“”
	 * @param str
	 * @return
	 */
	private String getCs(String str){
		if(str != null){
			return str;
		}
		return "无";
	}
	
	/**
	 * 查看通知公告详情内容
	 */
	public void noticeInfo(){
		String noticeId = this.getPara("noticeId");
		
		boolean success = false;
		String message = "新增失败";
		Hashtable<String, String> result = new Hashtable<String, String>();
		
		if(noticeId == null || noticeId.equals("") || noticeId.equalsIgnoreCase("null")) {
		 	message = "必须传递公告id！";
		}else{
			NoticeService noSer = new NoticeService();
			EbNotice notice = noSer.getNoticeById(noticeId);
			
			if(notice != null){
				success = true;
				message = "";
				String newTime = TimeUtil.getTimeStyle(notice.getNotice_time(),"yyyy-MM-dd");
				notice.setNotice_time(newTime);
				result.put("noticeId", getCs(notice.getNotice_id()+""));//通知公告Id
				result.put("noticeTitle", getCs(notice.getNotice_title()));//通知公告主题
				result.put("noticeContent", getCs(notice.getNotice_content()));//通知公告内容
				result.put("noticeTime", getCs(notice.getNotice_time()+""));//通知公告时间
				result.put("userCode", getCs(notice.getNotice_user_code()));//通知公告发布账户
				result.put("userName", getCs(notice.getNotice_user_name()));//发布人姓名
				result.put("noticeType", getCs(notice.getNotice_type()+""));//通知公告类型1部门，2企业，3部门和企业
				result.put("businessId", getCs(notice.getBusinessId()+""));//发布人企业Id
				result.put("businessName", getCs(notice.getNotice_buss_name()));//发布人企业名称
				result.put("departId", getCs(notice.getNotice_depart_id()+""));//发布人部门id
				result.put("departName", getCs(notice.getNotice_depart_name()));//发布人部门名称
			}
		}
		this.setAttr("success", success);
		this.setAttr("result", result);
		this.setAttr("errorMsg", message);
		render(new JsonRender().forIE());
	}
	
	/**
	 * 获取当前有效邮箱
	 */
	public void getEmail(){
		EmailService ser = new EmailService();
		EbNoticeEmail email = ser.getEmail();
		
		boolean success = false;
		String message = "数据为空！";
		Hashtable<String, String> result = new Hashtable<String, String>();
		
		if(email != null){
			success = true;
			message="";
			result.put("email", getCs(email.getEmail_name()));
		}
		this.setAttr("success", success);
		this.setAttr("result", result);
		this.setAttr("errorMsg", message);
		render(new JsonRender().forIE());
	}
}
