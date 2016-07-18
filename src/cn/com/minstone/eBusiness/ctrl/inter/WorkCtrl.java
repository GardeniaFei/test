package cn.com.minstone.eBusiness.ctrl.inter;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.com.minstone.eBusiness.MobileInter;
import cn.com.minstone.eBusiness.model.EbBusiness;
import cn.com.minstone.eBusiness.model.EbBusinessType;
import cn.com.minstone.eBusiness.model.EbDepart;
import cn.com.minstone.eBusiness.model.EbItem;
import cn.com.minstone.eBusiness.model.EbNews;
import cn.com.minstone.eBusiness.model.EbSpcProgram;
import cn.com.minstone.eBusiness.model.EbTask;
import cn.com.minstone.eBusiness.model.EbTaskDistribute;
import cn.com.minstone.eBusiness.model.EbUserInfo;
import cn.com.minstone.eBusiness.model.RoleType;
import cn.com.minstone.eBusiness.service.inter.BussInterService;
import cn.com.minstone.eBusiness.service.inter.DeptInterService;
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

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.render.JsonRender;

/**
 * 办事员版controller
 * @author admin
 *
 */
@Before(MobileInter.class)
public class WorkCtrl extends Controller {
	
	private Logger logger = new Logger(WorkCtrl.class);

	/**
	 * 检查字符串是否为空，为空则传递“”
	 * @param str
	 * @return
	 */
	private String getCheckedString(String str){
		if(str != null){
			return str;
		}
		return "";
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
	/**
	 * 27.消息列表
	 */
	public void newsList(){
		boolean success = false;
		String message = "数据为空";
		int page = this.getParaToInt("page", 1);
		
		List<Hashtable<String, String>> result = new ArrayList<Hashtable<String, String>>();
	    MsgInterService mesSer = new MsgInterService();
	    
	    Page<EbNews> newsPage = mesSer.getAllNews(page);
	    List<EbNews> newsList = newsPage.getList();
	    if(newsList.size()>0){
	    	success = true;
			message = "成功!";
	    	for(int i=0;i<newsList.size();i++){
	    		EbNews newss =newsList.get(i);
	    		
	    		Hashtable<String, String> resultHash = new Hashtable<String, String>();
	    		resultHash.put("newsType", getCheckedString(newss.getNewsType()+""));//消息类型13为添加了通知公告
	    		resultHash.put("newsId", getCheckedString(newss.getNewsId()+""));//消息id
	    		resultHash.put("status", getCheckedString(newss.getStatus()+""));//事项处理状态（0 未开始、1 办理中、2 已逾期 、3 办理完结、4 被退回）
	    		resultHash.put("businessId", getCheckedString(newss.getBusinessId()+""));//相关企业id
	    		resultHash.put("businessName", getCheckedString(newss.getBusinessName()+"")); //企业名称
	    		resultHash.put("instrTime", TimeUtil.changeTimeStyle(newss.getNewsTime()+""));//消息发起时间
	    		resultHash.put("itemId", getCheckedString(newss.getItemId()+""));//事项id
	    		//事项名称
	    		ItemInterService itemSer = new ItemInterService();
	    		EbItem item = itemSer.getItemById(newss.getItemId()+"");
	    		if (item == null) {
					item = GovNetUtil.getItemInfo(newss.getItemId() + "");
				}
	    		if(item != null){
	    			resultHash.put("itemName", getCheckedString(item.getItemId()+""));
	    		}
	    		resultHash.put("content", getCheckedString(newss.getCoupleBack()+""));//消息内容
	    		result.add(resultHash);
	    	}
	    	 setAttr("result", result);
	    }
		setAttr("current_page", newsPage.getPageNumber());
		setAttr("total_pages", newsPage.getTotalPage());
		setAttr("total_numb",newsPage.getTotalRow()-1);
		setAttr("success", success);
		setAttr("message", message);
		render(new JsonRender().forIE());
	}
	
	/**
	 * 28完成事项（标记事项）
	 */
	public void finishItem(){
		zwb_id();
		String taskId = getPara("taskId");//任务Id
		String itemId = getPara("itemId");//事项id
		String distrId = this.getPara("distrId");
		
		boolean success = false;
		String message = "标记失败";
		
		String transactor_id = Config.getSessionUserID(this);//办理人Id
		
		if((taskId!=null) && (itemId!=null)){
			
			UserInterService userIS = new UserInterService(this);
			EbUserInfo userInfo = userIS.findById(transactor_id);
			
			//根据任务id和事项id,以及用户id，标记事项完成
			TaskInterService taskSer = new TaskInterService();
			EbTaskDistribute task =  taskSer.getDistrByTIId(taskId, itemId, distrId);
			if(task != null){
				MsgInterService msgService = new MsgInterService();
				
				String status = task.getTransactionStatus()+"";//任务状态（当前任务是否还可办理）0 不能在处理   1 能处理
//				Record r = Db.findFirst("select * from eb_depart t where t.depart_id=? and t.user_code =?", task.getDepartId(), userInfo.getUserAccount());
				Record r = Db.findFirst("select * from eb_task_distribute t where t.status=? and t.depart_id=? and t.transactor_id =? and t.item_id=? and t.task_id =?", 
						task.getStatus(),task.getDepartId(), transactor_id,task.getItemId(),task.getTaskId());

				if(r == null){
					message = "当前办理事项用户非跟办人！";
				}else {
					if(!status.equals("3") && !status.equals("4")){
						task.setTransactionStatus(new BigDecimal("3"));//办理状态（0 未开始、1 办理中、2 已逾期、3 办理完结、4 被退回 ）
//						task.setTransactorId(new BigDecimal(transactor_id));//办理人id
//						task.setTransUserCode(userInfo.getUserAccount());
						task.set("transaction_time", System.currentTimeMillis()+"");//办理时间
						if(task.update()){
							message = "审批完成";
							success = true;
							
							//产生反馈消息，完成事项
							EbTask tInfo = taskSer.getTaskById(taskId);
							EbBusiness bsInfo = null;
							if (tInfo != null) {
								bsInfo = new BussInterService().getBussById(tInfo.getBusinessId() + "");
							}
							
							//TODO 消息推送
							try {
								String newsTime = System.currentTimeMillis() + "";
								boolean flg = msgService.addNews(userInfo, bsInfo, "1", task.getItemName(), "", 
										newsTime, itemId, task.getItemName(), "3", task.getDepartId() + "", 
										task.getDepartName());
								if(flg) {
									String content = bsInfo.getBusinessName() + "（企业）所需办理的事项：" + task.getItemName() + 
											"已被" + userInfo.getDepartName() + "（单位）办理完成。";
									List<String> strAlias = new ArrayList<String>();
									strAlias.add(Config.ZWB);
									
									Hashtable<String, String> map = new Hashtable<String, String>();
									map.put("newsType", "1");
									map.put("content", content);
									map.put("status", "3");
									map.put("businessId", getCheckedString(bsInfo.getBusinessId() + ""));
									map.put("businessName", getCheckedString(bsInfo.getBusinessName()));
									PushUtil.pushNotifyToTagert(strAlias, StringUtil.getJson(map));
									
									content = "您所需办理的事项：" + task.getItemName() + "已被" + userInfo.getDepartName() + 
											"办理完成。";
									List<String> strAlias1 = new ArrayList<String>();
									strAlias1.add(bsInfo.getBusinessId() + "");
									
									Hashtable<String, String> table = new Hashtable<String, String>();
									table.put("newsType", "1");
									table.put("content", content);
									table.put("status", "3");
									table.put("businessId", getCheckedString(bsInfo.getBusinessId() + ""));
									table.put("businessName", getCheckedString(bsInfo.getBusinessName()));
									PushUtil.pushNotifyToTagert(strAlias1, StringUtil.getJson(table));
								}else {
									logger.error("消息插入数据库失败");
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
							
							//判断事项是否全办理完
							boolean flag = true;
							//根据任务id查询所有办理状态不为4（被退回）的事项分发列表
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
										
										//TODO 消息推送
										try {
											String newsTime = System.currentTimeMillis() + "";
//											String newsContent = bussInfo.getBusinessName() + "（企业）所需办理的所有事项都已办理完成，进入到落户 状态";
											String newsContent = bussInfo.getBusinessName() + "（企业）相关的所有企业落户事项已办理完结，请查看";
											boolean flg = msgService.addNews(userInfo, bussInfo, "12", newsContent, "", 
													newsTime, null, null, "0", Config.getSessionZWBID(this), 
													MCubeAppConfig.getInstance().getZwb_name());
											if(flg) {
												List<String> strAlias = new ArrayList<String>();
//												List<EbUserInfo> leaders = userIS.filterUsersList("2", null);
//												if(strAlias != null) {
//													for (int i = 0; i < leaders.size(); i++) {
//														strAlias.add(leaders.get(i).getUserId() + "");
//													}
//												}
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
						}else{
							message = "数据更新失败，事项未完成!";
						}
					}else{
						message = "该事项已完成或被退回，不能办理。";
					}
			     }
			}
		}else{
			message = "传递参数不完整";
		}
		Hashtable<String, String> result = new Hashtable<String, String>();
		result.put("message", message);
		
		setAttr("result", result);
		setAttr("success", success);
		render(new JsonRender().forIE());
	}
	
	/**
	 * 29.新增企业
	 */
	public void addBusiness(){
		zwb_id();
		String businessName  = getPara("businessName");//企业名称
		String projectName = getPara("projectName");//企业项目名称
		String contactName = getPara("contactName");//联系人姓名
		String contactPhone = getPara("contactPhone");//联系人电话
		String typeId = getPara("typeId");//企业类型（选择默认模板109）
		String projectIntro = getPara("projectIntro");//项目简介
		
		String bussInto = this.getPara("businessIntro", "");//企业简介 
		String registCapital = this.getPara("registCapital");//注册资金
		String operateScope = this.getPara("operateScope");//许可经营范围
		String email = this.getPara("email");//邮箱
		String webAddress = this.getPara("webAddress");//企业网址
		String bussAddress = this.getPara("bussAddress");//企业地址
		
		String userId = Config.getSessionUserID(this);
		String user_authority = "4";
		String service_admin  = getPara("service_admin");//vip服务专员userCode
		EbUserInfo u = new UserInterService(this).getUserByUId(userId);//获取登录用户信息
		if(u != null){
			user_authority = u.getAuthority()+"";
		}
		System.out.print("登录用户权限："+user_authority);
		
		boolean success = false;
		String message = "新增失败";
		if(businessName == null || businessName.equals("") || businessName.equalsIgnoreCase("null")) {
			message = "企业名称不能为空！";
		}else if(contactName == null || contactName.equals("") || contactName.equalsIgnoreCase("null")) {
			message = "联系人姓名不能为空！";
		}else if(contactPhone == null || contactPhone.equals("") || contactPhone.equalsIgnoreCase("null")) {
			message = "联系电话不能为空！";
		}else if(typeId == null || typeId.equals("") || typeId.equalsIgnoreCase("null")) {
			message = "未选择默认模板！";
		}else if (!StringUtil.valiatePhone(contactPhone)) {
			message = "请输入正确的联系电话";
		}else if (registCapital == null || registCapital.equals("") || registCapital.equalsIgnoreCase("null")) {
			message = "必须填写注册资金！";
		}else if(operateScope == null || operateScope.equals("") || operateScope.equalsIgnoreCase("null")) {
			message = "必须填写许可经营范围！";
		}else if(service_admin == null || service_admin.equals("") || service_admin.equalsIgnoreCase("null")) {
			message = "必须选择vip服务专员！";
//		}else if(email != null && !email.equals("") && !email.equalsIgnoreCase("null")) {
//			if(!StringUtil.valiateEmail(email)){
//				message = "请输入正确的Email格式！";
//			}
		}else if(user_authority.equals("3") || user_authority.equals("4")|| user_authority.equals("5")) {
			message = "用户无新增企业权限！";//（1VIP服务专员管理员、2VIP服务专员权限 、3单位首席权限、4部门跟办人权限、5市级服务专员权限）
		
		}else {
			EbUserInfo uInfo = new UserInterService(this).findByCode(contactPhone);
			if(uInfo == null) {
				BussInterService bussSer = new BussInterService();
				//0 失败 1 成功 -1 失败缺少参数
				int flag = bussSer.addBussInfo(businessName, projectName, contactName, contactPhone, 
						typeId, userId, projectIntro, bussInto, registCapital, operateScope, email, webAddress, bussAddress,service_admin);
				
				boolean flag2 = false;
				//添加任务
				if(flag == 1) {//企业新增成功就创建一个任务
					TaskInterService taskSer = new TaskInterService();
					flag2 = taskSer.addTask(userId, typeId, businessName);
				}
				
				if(flag==1 && flag2){
					//新增企业成功进行添加企业用户
					EbBusiness bussInfo = bussSer.findByBussNameOne(businessName);
					if(bussInfo != null) {
						UserInterService userService = new UserInterService(this);
						
						EbUserInfo userInfo = userService.findByCode(contactPhone);
						if(userInfo == null) {
							
							String psw = MCubeAppConfig.getInstance().getDefaultPwd();
							if(psw == null || psw.equals("") || psw.equalsIgnoreCase("null")) {
								psw = "123456";
							}
							int flg = userService.addUserInfo(contactPhone, psw, contactName, RoleType.BUSINESS, 
									bussInfo.getBusinessId() + "", "", bussInfo.getBusinessName(), email);
							
							int flg2 = 0;
							if (flg == 1) {
								//企业账号创建成功
								String content = "尊敬的企业客户，你已在海珠企业招商系统中注册了企业用户你的账户为：" + contactPhone + 
										"默认密码为：" + psw + ",您可以在" + MCubeAppConfig.getInstance().getAppLoadUrl() + 
										"下载海珠易企办APP登录查询您的办理进度。";
								flg2 = userService.addSMSInfo(contactPhone, contactName, psw, content);
							}
							
							if (flg == 1 && flg2 == 1) {
								success = true;
								message = "新增成功";
								//TODO 推送消息
								MsgInterService msgService = new MsgInterService();
								EbUserInfo user = userService.findById(userId);
								if (user != null && bussInfo != null) {
									try {
										String newsContent = user.getDepartName() + user.getUserName() + "添加了新的企业" + bussInfo.getBusinessName();
										String newsTime = System.currentTimeMillis() + "";
//										boolean bool = msgService.addNews(user, bussInfo, "3", newsContent, null, newsTime, 
//												null, null, "0", user.getDepartId()+"", user.getDepartName());
										String zwb_dpatId = Config.getSessionZWBID(this);
										String zwb_name = MCubeAppConfig.getInstance().zwb_name;
										boolean bool = msgService.addNews(user, bussInfo, "3", newsContent, null, newsTime, 
												null, null, "0", zwb_dpatId, zwb_name);
										if(bool) {
											List<String> strAlias = new ArrayList<String>();
											List<EbUserInfo> leaders = userService.filterUsersList("2", null);
											if(strAlias != null) {
												for (int i = 0; i < leaders.size(); i++) {
													strAlias.add(leaders.get(i).getUserId() + "");
												}
											}
											strAlias.add(Config.ZWB);
											
											Hashtable<String, String> map = new Hashtable<String, String>();
											map.put("newsType", "3");
											map.put("content", newsContent);
											map.put("status", "0");
											map.put("businessId", getCheckedString(bussInfo.getBusinessId() + ""));
											PushUtil.pushNotifyToTagert(strAlias, StringUtil.getJson(map));
											logger.info("消息发送成功");
										}else {
											logger.error("消息数据存储失败！");
										}
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							}else if (flg == 1 && flg2 == 0) {
								success = true;
								message = "企业添加成功，账户短信创建失败";
							}else {
								success = true;
								message = "企业添加成功，企业用户创建失败！";
							}
						}else {
							success = true;
							message = "企业添加成功，该手机号已被注册为其他企业用户";
						}
					}
				}else if(flag==0){
					success = false;
					message = "新增失败";
				}else if(flag==(-1)){
					success = false;
					message = "缺少参数";
				}else if(flag==1 && !flag2){
					message = "新建任务失败";
				}else if(!flag2){
					message = "新建任务失败";
				}
			}else {
				message = "该联系电话已被注册，请填写其他联系电话！";
			}
		}
		
		Hashtable<String, String> result = new Hashtable<String, String>();
		result.put("message", message);
		
		setAttr("result", result);
		setAttr("success", success);
		setAttr("errorMsg", message);
		render(new JsonRender().forIE());
	}
	
	/**
	 * 30.签收企业
	 */
	public void signBusiness(){
		String businessId = getPara("businessId");
		String userId = Config.getSessionUserID(this);
		boolean success = false;
		String message = "签收失败";
		
		if(businessId == null){
			message = "请传递企业ID号！";
		}else{
			BussInterService bussSer = new BussInterService();
			EbBusiness buss = bussSer.getBussById(businessId);
//			UserInterService userService = new UserInterService(this);
			
			boolean flag = false;
			if(buss!=null){
				String status = buss.getSignStatus()+"";//签收状态（ 0 未签收、1 已签收、2 被退回：事项任务签收时有该状态
				if(status.equals("0")){
					buss.setSignStatus(new BigDecimal(1));
					//设置企业签收用户信息，由于签收人是办事人员，而办事员登录时其用户信息就存入到了本地数据库中了
					//所以用户信息直接从本地数据库中获取
					buss.setUserId(new BigDecimal(userId));
					UserInterService userService = new UserInterService(this);
					EbUserInfo userInfo = userService.findById(userId);
					if(userInfo != null) {
						String vipAdmin = buss.getService_admin();
						String userAccount = userInfo.getUserAccount();
						if(vipAdmin.equals(userAccount)){//判断是否是对应企业的vip服务专员签收
							buss.setUserCode(userInfo.getUserAccount());
							buss.setUserName(userInfo.getUserName());
							buss.set("sign_time", new Date().getTime());
							if(buss.update()){
								TaskInterService taskSer = new TaskInterService();
								EbTask task = taskSer.getTaskByBussId(businessId);
								if(task!=null){
									task.setSignStatus(new BigDecimal(1));
									task.setSignUserId(new BigDecimal(userId));
									task.set("sign_time", new Date().getTime());
									try {
										if(task.update()){
											flag = true;
											//签收企业成功，发送短信
											signBussSMS(task, buss);
										}
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
								
								MsgInterService msgService = new MsgInterService();
								String newsTime = System.currentTimeMillis() + "";
								boolean flg = msgService.addNews(userInfo, buss, "2", "签收了" + buss.getBusinessName(), 
										"", newsTime, null, null, "0", userInfo.getDepartId()+"", userInfo.getDepartName());
								
								if(flg) {
									logger.info("消息存储成功");
									
									List<String> strAlias = new ArrayList<String>();
									strAlias.add(buss.getBusinessId() + "");
	//								List<EbUserInfo> leaders = userService.filterUsersList("2", null);
	//								if(strAlias != null) {
	//									for (int i = 0; i < leaders.size(); i++) {
	//										strAlias.add(leaders.get(i).getUserId() + "");
	//									}
	//								}
									String content = userInfo.getDepartName() + userInfo.getUserName() + "签收了企业：" + 
											buss.getBusinessName();
									try {
										
										Hashtable<String, String> map = new Hashtable<String, String>();
										map.put("newsType", "2");
										map.put("content", content);
										map.put("status", "0");
										map.put("businessId", getCheckedString(buss.getBusinessId() + ""));
										PushUtil.pushNotifyToTagert(strAlias, StringUtil.getJson(map));
									} catch (Exception e) {
										e.printStackTrace();
										logger.error("消息发送错误！");
									}
								}else {
									logger.error("消息存储失败！");
								}
							}
						}else{
							message = "企业配置vip服务专员非此服务专员";
						}
					}
				}else{
					message = "已签收该企业";
				}
			}else{
				message = "该企业不存在";
			}
			if(flag){
				success = true;
				message = "签收成功";
			}
		}
		Hashtable<String, String> result = new Hashtable<String, String>();
		result.put("message", message);
		
		setAttr("result", result);
		setAttr("success", success);
		render(new JsonRender().forIE());
	}
	
	/**
	 * 签收企业处理短息
	 * @param task
	 * @param buss
	 */
	private void signBussSMS(EbTask task, EbBusiness buss) {
		UserInterService userService = new UserInterService(this);
		EbUserInfo user = userService.findByCoBID(buss.getContactPhone(), buss.getBusinessId() + "");
		if(user != null) {
			//用户是否被禁用
			if (user.getIsDelet().intValue() == 0) {
				user.setIsDelet(new BigDecimal("1"));
				if (user.update()) {
					String content = "您的企业已被政务办签收，准备发送到各个部门办理";
					userService.addSMSInfo(buss.getContactPhone(), 
							buss.getContactName(), "11", content);
				}
			}
		}else {
			String psw = MCubeAppConfig.getInstance().getDefaultPwd();
			if(psw == null || psw.equals("") || psw.equalsIgnoreCase("null")) {
				psw = "123456";
			}
			int flg = userService.addUserInfo(buss.getContactPhone(), psw, buss.getContactName(), 
					RoleType.BUSINESS, buss.getBusinessId() + "", "", buss.getBusinessName(), buss.getBussEmail());
			if (flg == 1) {
				String content = "您的企业已被政务办签收，准备发送到各个部门办理";
				userService.addSMSInfo(buss.getContactPhone(), 
						buss.getContactName(), psw, content);
			}
		}
	}
	
	private void faultResult(String error) {
		this.setAttr("success", false);
		this.setAttr("result", null);
		this.setAttr("errorMsg", error);
	}
	
	/**
	 * 31.分发任务
	 */
	public void distributeTask(){
		String bussId = this.getPara("businessId");
		String typeId = this.getPara("typeId");//企业类型id
		String streva = this.getPara("taskList");
		String userId = Config.getSessionUserID(this);
		
		String strCompEva = "{\"taskList\":" + streva + "}";
		EbUserInfo userInfo = new UserInterService(this).findById(userId);//分发人是否有权限
		int userAuth = 4;//用户权限：2VIP服务专员权限、7 VIP服务专员+单位首席权限
		if(userInfo != null){
			userAuth = userInfo.getAuthority().intValue();
		}
		
		if(bussId == null || "".equals(bussId) || "null".equalsIgnoreCase(bussId)) {
			faultResult("必须传递企业id，分发失败！");
		}else if(userAuth != 2 && userAuth != 7) {
			faultResult("用户无权限分发任务！");
		}else{
			boolean distflag = false;
			BussInterService bussSer = new BussInterService();
			bussSer.updateType(bussId, typeId);
			EbBusiness buss = bussSer.getBussById(bussId);
			ItemInterService itemService = new ItemInterService();
			if(buss!=null){
				String distStatus = buss.getDistributedStatus()+"";//是否分发任务 0 未分发、1 已分发
				if(distStatus.equals("0")){
					distflag = true;
				}
			}
			if(distflag){
				String taskId ="";
				TaskInterService taskSer = new TaskInterService();
				MsgInterService msgService = new MsgInterService();
				EbTask task = taskSer.getTaskByBussId(bussId);
				if(task!= null){
					taskId = task.getTaskId()+"";//获取该任务的id
				
					boolean flag = false;
					String errorMsg = "分发失败";
					try {
						JSONObject evalue = new JSONObject(strCompEva);
						JSONArray taskEvas = (JSONArray) evalue.optJSONArray("taskList");
						for(int i = 0; i < taskEvas.length(); i++) {
							String deptId = taskEvas.optJSONObject(i).optString("departId");
							if (deptId == null || deptId.equals("") || deptId.equalsIgnoreCase("null")) {
								continue;
							}
							//事项id列表
							JSONArray items = taskEvas.optJSONObject(i).optJSONArray("items");
							if(items.length()>0){
								int total = 0;
								String distrTime = System.currentTimeMillis() + "";
								for(int j=0; j<items.length(); j++){
									String itemId = items.optString(j);
									
									EbItem item = itemService.getItemById(itemId);
									boolean flag2 = false;
									//进行任务分发，数据存入任务分发表中
									if(item == null) {
										item = GovNetUtil.getItemInfo(itemId);
										if (item == null) {
											continue;
										}
										flag2 = taskSer.distributeTask(bussId, deptId, itemId, userId, 
												taskId, item.getItemType().toString(), item.getItemName(), 
												item.getDepartName(), distrTime, 0);
									}else {
										flag2 = taskSer.distributeTask(bussId, deptId, itemId, userId, 
												taskId, item.getItemType().toString(), item.getItemName(), 
												item.getDepartName(), distrTime, 1);
									}
									if(flag2){
										total++;
										//给消息表中插入数据
										String newsContent = item.getItemName();
										String newsTime = System.currentTimeMillis() + "";
										try {
											msgService.addNews(userInfo, buss, "4", newsContent, null, 
													newsTime, itemId, item.getItemName(), "5", item.getDepartId() + "", item.getDepartName());
										} catch (Exception e) {
											e.printStackTrace();
										}
									}
								}
								if(total == items.length()){
									flag = true;
								}
							}
							//TODO 推送通知消息给客户端
							try {
								List<String> strAlias = new ArrayList<String>();
								strAlias.add(deptId + "");
								String content = "您有新任务，" + buss.getBusinessName() + "，请签收任务";
								
								Hashtable<String, String> map = new Hashtable<String, String>();
								map.put("newsType", "4");
								map.put("content", content);
								map.put("status", "5");
								map.put("businessId", getCheckedString(buss.getBusinessId() + ""));
								PushUtil.pushNotifyToTagert(strAlias, StringUtil.getJson(map));
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						if(flag) {
							this.setAttr("success", true);
							Hashtable<String, Object> result = new Hashtable<String, Object>();
							result.put("message", "分发成功！");
							this.setAttr("result", result);
							this.setAttr("errorMsg", "");
						}else {
							faultResult(errorMsg);
						}
					} catch (JSONException e) {
						faultResult("分发任务传递数据格式错误，分发失败！");
						e.printStackTrace();
					} catch (Exception e) {
						faultResult("异常错误，分发失败！");
						e.printStackTrace();
					}
				}else {
					faultResult("未查询到该企业对应的任务id");
				}
			 }else {
					faultResult("该企业已分发任务！");
				}
		}
		render(new JsonRender().forIE());
	}
	
	/**
	 * 32.新增事项
	 */
	public void addTask(){
		String businessId = getPara("businessId");
		String taskId = getPara("taskId");
		String streva = getPara("taskList");
		String strCompEva = "{\"taskList\":" + streva + "}";
		
		String user_id = Config.getSessionUserID(this);
		if(businessId == null || "".equals(businessId) || "null".equalsIgnoreCase(businessId)) {
			faultResult("必须传递企业id，新增失败！");
		}else{
			if(taskId == null || "".equals(taskId) || "null".equalsIgnoreCase(taskId)){
				faultResult("必须传递任务id，新增失败！");
			}else{
				boolean flag = false;
				String errorMsg = "新增失败";
				
				EbUserInfo userInfo = new UserInterService(this).findById(user_id);
				TaskInterService taskSer = new TaskInterService();
				ItemInterService itemService = new ItemInterService();
				MsgInterService msgService = new MsgInterService();
				EbBusiness bussInfo = new BussInterService().getBussById(businessId);
				try {
					JSONObject evalue = new JSONObject(strCompEva);
					JSONArray taskEvas = (JSONArray) evalue.optJSONArray("taskList");
					for(int i = 0; i < taskEvas.length(); i++) {
						String deptId = taskEvas.optJSONObject(i).optString("departId");
						if (deptId == null || deptId.equals("") || deptId.equalsIgnoreCase("null")) {
							continue;
						}
						//事项id列表
						JSONArray items = taskEvas.optJSONObject(i).optJSONArray("items");
						int total = 0;
						String distrTime = System.currentTimeMillis() + "";
						for(int j=0;j<items.length();j++){
							String itemId = items.optString(j);
/*****************2015-11-11 feini*************************/
							//判断该事项是否已经存在
//							Record r = Db.findFirst("select * from eb_task_distribute t where t.status=1 and t.depart_id=? and t.item_id=? and t.task_id =?", 
//									deptId,itemId,taskId);	
//							if(r != null){
//								continue;
//							}
/*********************************************************/
							EbItem item = itemService.getItemById(itemId);
							boolean flag2 = false;
							//新增的事项  要存入任务分发表中
							if(item == null) {
								item = GovNetUtil.getItemInfo(itemId);
								if (item == null) {
									continue;
								}
								flag2 = taskSer.distributeTask(businessId, deptId, itemId, user_id, 
										taskId, item.getItemType().toString(), item.getItemName(), 
										item.getDepartName(), distrTime, 0);
							}else {
								flag2 = taskSer.distributeTask(businessId, deptId, itemId, user_id, 
										taskId, item.getItemType().toString(), item.getItemName(), 
										item.getDepartName(), distrTime, 1);
							}
//							boolean flag2 = taskSer.distributeTask(businessId, deptId, itemId, user_id, taskId);
							if(flag2){
								total++;
								//给消息表中插入数据
								String newsContent = item.getItemName();
								String newsTime = System.currentTimeMillis() + "";
								try {
									msgService.addNews(userInfo, bussInfo, "4", newsContent, null, 
											newsTime, itemId, item.getItemName(), "5", item.getDepartId() + "", item.getDepartName());
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						}
						if(total == items.length()){
							flag = true;
						}else if(total > 0){
							errorMsg="有事项重复，不能将所有事项添加传递！";
						}else{
							errorMsg="事项已经存在不能传递！";
						}
						//TODO 推送通知消息给客户端
						try {
							String content = "您有新的任务，" + bussInfo.getBusinessName() + "，请签收任务";
							List<String> strAlias = new ArrayList<String>();
							strAlias.add(deptId + "");
							
							Hashtable<String, String> map = new Hashtable<String, String>();
							map.put("newsType", "4");
							map.put("content", content);
							map.put("status", "5");
							map.put("businessId", getCheckedString(bussInfo.getBusinessId() + ""));
							PushUtil.pushNotifyToTagert(strAlias, StringUtil.getJson(map));
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					if(flag) {
						this.setAttr("success", true);
						Hashtable<String, Object> result = new Hashtable<String, Object>();
						result.put("message", "新增成功！");
						this.setAttr("result", result);
						this.setAttr("errorMsg", "");
					}else {
						faultResult(errorMsg);
					}
				} catch (JSONException e) {
					faultResult("分发任务传递数据格式错误，分发失败！");
					e.printStackTrace();
				} catch (Exception e) {
					faultResult("异常错误，分发失败！");
					e.printStackTrace();
				}
			}
		}
		render(new JsonRender().forIE());
	}
	
	/**
	 * 33.新建特别程序
	 */
	public void addProgram(){
		String distrId = this.getPara("distrId");//事项分发id
		String spcReason = this.getPara("spcReason");//新建特别程序原因
		String delayTime = this.getPara("delayTime");//延期时限（单位/工作日）
		String remedyProgram = this.getPara("remedyProgram");//补救方案
		String conment = this.getPara("conment");//备注
		String bussId = this.getPara("businessId");//企业ID
		String buildUserId = Config.getSessionUserID(this);//新建用户ID
		
		boolean success = false;
		String message = "新建特殊任务失败";
		
		UserInterService userService = new UserInterService(this);
		TaskInterService spcTser = new TaskInterService();
		BussInterService bussService = new BussInterService();
		
		EbUserInfo userInfo = userService.findById(buildUserId);
		EbTaskDistribute distrInfo = spcTser.getDistrByDId(distrId);
		EbBusiness bussInfo = bussService.findById(bussId);
		MsgInterService msgService = new MsgInterService();
		
		if(spcReason == null || "".equals(spcReason) || spcReason.equalsIgnoreCase("null")) {
			faultResult("必须填写新增特殊程序原因！");
			logger.error("必须填写新增特殊程序原因！");
		}else if (userInfo != null && distrInfo != null && bussInfo != null) {
//			Record r = Db.findFirst("select * from eb_task_distribute t where t.status=? and t.depart_id=? and t.Sign_user_code =? and t.item_id=? and t.task_id =?", 
//					distrInfo.getStatus(),distrInfo.getDepartId(), userInfo.getUserAccount(),distrInfo.getItemId(),distrInfo.getTaskId());	
			
			boolean flag = false;
//			if(r == null){
//				faultResult ( "当前办理用户非原事项签收人！");
//			}else {
				EbSpcProgram spc = new EbSpcProgram();
				flag = spcTser.addSpcPragram(userInfo, distrInfo, bussInfo, spcReason, 
						delayTime, remedyProgram, conment, spc); //0 失败  1 成功  
				if (flag) {

					//给消息表中插入数据
					String newsContent = userInfo.getDepartName() + userInfo.getUserName() + "为" + 
							bussInfo.getBusinessName() + "（企业）所需办理的" + distrInfo.getItemName() + 
							"（事项）启动了特别程序。";
					String newsTime = System.currentTimeMillis() + "";
					try {
						msgService.addNews(userInfo, bussInfo, "5", newsContent, spcReason, 
								newsTime, distrInfo.getItemId() + "", distrInfo.getItemName(), "6", distrInfo.getDepartId() + "", distrInfo.getDepartName());
						
//						List<EbUserInfo> leaders = userService.filterUsersList("2", "");//获取所有的领导用户
						
						List<String> strAlias = new ArrayList<String>();
						strAlias.add(Config.ZWB);
//						for (int i = 0; i < leaders.size(); i++) {
//							strAlias.add(leaders.get(i).getUserId() + "");
//						}
						newsContent = userInfo.getDepartName() + userInfo.getUserName() + "为" + 
								bussInfo.getBusinessName() + "（企业）所需办理的" + distrInfo.getItemName() + 
								"（事项）启动了特别程序，原因：" + spcReason;
						
						Hashtable<String, String> map = new Hashtable<String, String>();
						map.put("newsType", "5");
						map.put("content", newsContent);
						map.put("status", "6");
						map.put("programId", getCheckedString(spc.getProgramId() + ""));
						PushUtil.pushNotifyToTagert(strAlias, StringUtil.getJson(map));
						
//						newsContent = "您所需办理的" + distrInfo.getItemName() + "（事项）已被" + 
//								userInfo.getDepartName() + userInfo.getUserName() + "启动了特别程序，原因：" + spcReason;
//						strAlias.clear();
//						strAlias.add(bussInfo.getBusinessId() + "");
//						
//						Hashtable<String, String> table = new Hashtable<String, String>();
//						table.put("newsType", "5");
//						table.put("content", newsContent);
//						table.put("status", "6");
//						table.put("programId", getCheckedString(spc.getProgramId() + ""));
//						PushUtil.pushNotifyToTagert(strAlias, StringUtil.getJson(table));
						
					} catch (Exception e) {
						e.printStackTrace();
					}
					
					success = true;
					message = "新建成功！";
					logger.info("新建成功！");
					Hashtable<String, String> result = new Hashtable<String, String>();
					result.put("message", message);
					
					setAttr("result", result);
					setAttr("success", success);
				}else {
					faultResult("数据存入失败，特别程序新建失败");
					logger.error("数据存入失败，特别程序新建失败");
				}
//			}//签收人判断
		}else {
			faultResult("缺少参数或未找到相关信息！");
			logger.error("缺少参数或未找到相关信息！");
		}
		
		render(new JsonRender().forIE());
	}
	
	/**
	 * 37.任务撤回
	 */
	public void backTask(){
		String taskId  = this.getPara("taskId");
		String streva = this.getPara("taskList");
		String transactorId = Config.getSessionUserID(this);
		String backReason = this.getPara("backReason");
		String strCompEva = "{\"taskList\":" + streva + "}";

		TaskInterService taskSer = new TaskInterService();
		EbTask tInfo = taskSer.getTaskById(taskId);
		EbUserInfo userInfo = new UserInterService(this).findById(transactorId);
		EbBusiness bussInfo = null;
		if(tInfo != null) {
			bussInfo = new BussInterService().findById(tInfo.getBusinessId() + "");
		}
		if(taskId  == null || "".equals(taskId ) || "null".equalsIgnoreCase(taskId )) {
			faultResult("必须传递任务id，撤回失败！");
		}else if (userInfo == null || bussInfo == null) {
			faultResult("当前用户或相关企业不存在");
		}else{
			if(backReason==null || "".equals(backReason)){
				faultResult("必须传递撤回原因，撤回失败！");
			}else{
				boolean flag = false;
//				String errorMsg = "撤回任务失败";
				MsgInterService msgService = new MsgInterService();
				
				try {
					JSONObject evalue = new JSONObject(strCompEva);
					JSONArray taskEvas = (JSONArray) evalue.optJSONArray("taskList");
					for(int i = 0; i < taskEvas.length(); i++) {
						//事项id列表
						JSONArray items = taskEvas.optJSONObject(i).optJSONArray("items");
						int total = 0;
						for(int j=0;j<items.length();j++){
							JSONObject obj = items.optJSONObject(j);
							if(obj != null) {
								String distrId = obj.optString("distrId");
								String itemId = obj.optString("itemId");
								EbTaskDistribute taskD = taskSer.getDistrByTIId(taskId, itemId, distrId);
								
								if(taskD!=null){
									boolean flag2 = false;
									
									//先查看事项办理状态，已签收则判断是否为签收人，未签收则能直接退回
									String signStatus = taskD.getSignStatus()+"";//签收状态（0 未签收、1 已签收、2 被退回：事项任务签收时有该状态）
									if(signStatus.equals("1")){
	//									Record r = Db.findFirst("select * from eb_task_distribute t where t.depart_id=? and t.Sign_user_code =?", taskD.getDepartId(), userInfo.getUserAccount());
										Record r = Db.findFirst("select * from eb_task_distribute t where t.status=? and t.depart_id=? and t.Sign_user_code =? and t.item_id=? and t.task_id =?", 
												taskD.getStatus(),taskD.getDepartId(), userInfo.getUserAccount(),taskD.getItemId(),taskD.getTaskId());
										
										if(r == null){
											faultResult("当前办理事项用户非签收人！");
										}else {
											taskD.set("transactor_id", Integer.parseInt(transactorId));//办理人id
											taskD.set("transaction_time", System.currentTimeMillis()+"");//办理时间
											taskD.set("return_reason", backReason);//被退回原因
											taskD.set("transaction_status", Integer.parseInt("4"));//办理状态（0 未开始、1 办理中、2 已逾期、3 办理完结、4 被退回 ）
											taskD.set("sign_status", Integer.parseInt("2"));//签收状态（0 未签收、1 已签收、2 被退回：事项任务签收时有该状态）
											taskD.set("status", Integer.parseInt("0"));//任务状态（当前任务是否还可办理）0 不能在处理   1 能处理
											if(taskD.update()){
												flag2 = true;
											}
										}
									}else if(signStatus.equals("0")){
										taskD.set("transactor_id", Integer.parseInt(transactorId));//办理人id
										taskD.set("transaction_time", System.currentTimeMillis()+"");//办理时间
										taskD.set("return_reason", backReason);//被退回原因
										taskD.set("transaction_status", Integer.parseInt("4"));//办理状态（0 未开始、1 办理中、2 已逾期、3 办理完结、4 被退回 ）
										taskD.set("sign_status", Integer.parseInt("2"));//签收状态（0 未签收、1 已签收、2 被退回：事项任务签收时有该状态）
										taskD.set("status", Integer.parseInt("0"));//任务状态（当前任务是否还可办理）0 不能在处理   1 能处理
										if(taskD.update()){
											flag2 = true;
										}
									}else{
										faultResult("当前办理事项已退回！");
									}
									if(flag2){
										total++;
										//TODO 消息推送
										try {
											String content = userInfo.getDepartName() + userInfo.getUserName() + "退回了" + 
													bussInfo.getBusinessName() + "所需办理的事项：" + taskD.getItemName();
											String newsTime = System.currentTimeMillis() + "";
											boolean flg = msgService.addNews(userInfo, bussInfo, "1", content, backReason, 
													newsTime, itemId, taskD.getItemName(), "4", taskD.getDepartId() + "", 
													taskD.getDepartName());
											
/*****************************2015-11-17 feini***************************/
											//任务分发人，政务办所有签收人获得该消息提醒
											String distriUserId = taskD.getUserId()+"";//任务分发人
											String distriDept = "";
											EbUserInfo distriUser = new UserInterService(this).findById(distriUserId);
											if(distriUser != null){
												distriDept = distriUser.getDepartId()+"";
											}
											boolean addDistriFlag = msgService.addNews(distriUser, bussInfo, "1", content,
													null, newsTime, itemId, taskD.getItemName(), "4", distriDept, "政务办");
/*****************************2015-11-17  end***************************/
											
											if(flg && addDistriFlag) {
												content = bussInfo.getBusinessName() + "所需办理的事项：" + taskD.getItemName() + 
														"已被" + userInfo.getDepartName() + "单位退回，原因：" + backReason;
												List<String> strAlias = new ArrayList<String>();
												strAlias.add(Config.ZWB);
												
												Hashtable<String, String> map = new Hashtable<String, String>();
												map.put("newsType", "1");
												map.put("content", content);
												map.put("status", "4");
												map.put("businessId", getCheckedString(bussInfo.getBusinessId() + ""));
												map.put("businessName", getCheckedString(bussInfo.getBusinessName()));
												PushUtil.pushNotifyToTagert(strAlias, StringUtil.getJson(map));
												
//												if(taskD.getSignStatus().intValue() == 1) {
//													content = "您所需办理的事项：" + taskD.getItemName() + "已被" + userInfo.getDepartName() + 
//															"单位退回，原因：" + backReason;
//													List<String> strAlias1 = new ArrayList<String>();
//													strAlias1.add(bussInfo.getBusinessId() + "");
//													
//													Hashtable<String, String> table = new Hashtable<String, String>();
//													table.put("newsType", "1");
//													table.put("content", content);
//													table.put("status", "4");
//													table.put("businessId", getCheckedString(bussInfo.getBusinessId() + ""));
//													table.put("businessName", getCheckedString(bussInfo.getBusinessName()));
//													PushUtil.pushNotifyToTagert(strAlias1, StringUtil.getJson(table));
//												}
											}else {
												logger.error("消息插入数据库失败");
											}
										} catch (Exception e) {
											e.printStackTrace();
										}
									}
								}
							}else {
								continue;
							}
						}
						if(total == items.length()){
							flag=true;
						}else if((0 < total) && (total < items.length())){
							faultResult("当前办理事项未完全退回成功！");
						}else if(total == 0){
//							faultResult("当前办理事项退回全部失败！");
						}
						if(flag) {
							this.setAttr("success", true);
							Hashtable<String, Object> result = new Hashtable<String, Object>();
							result.put("message", "退回成功！");
							this.setAttr("result", result);
							this.setAttr("errorMsg", "");
						}
					}
				} catch (JSONException e) {
					faultResult("任务退回传递数据格式错误，退回失败！");
					e.printStackTrace();
				} catch (Exception e) {
					faultResult("异常错误，退回失败！");
					e.printStackTrace();
				}
			}
		}
		render(new JsonRender().forIE());
	}
	
	/**
	 * 39我的消息（办事员)
	 */
	public void mymessage() {
		int pageIndex = this.getParaToInt("pageIndex", 1);
		int pageColunm = this.getParaToInt("pageColunm", 10);
		String userId = Config.getSessionUserID(this);
		UserInterService userService = new UserInterService(this);
		EbUserInfo userInfo = userService.findById(userId);
		boolean success = false;
		if(userInfo == null) {
			faultResult("未找到用户信息！");
		}else if(userInfo.getDepartId() == null || userInfo.getDepartId().equals("") 
				|| userInfo.getDepartId().toString().equalsIgnoreCase("null")) {
			faultResult("无法查询到相关信息！");
		}else {
			MsgInterService msgService = new MsgInterService();
//			ItemInterService itemService = new ItemInterService();
			List<Hashtable<String, Object>> result = new ArrayList<Hashtable<String,Object>>();
			Page<EbNews> newsPage = msgService.getNewsByDeptId(userInfo.getDepartId() + "", pageIndex, pageColunm);
			List<EbNews> list = new ArrayList<EbNews>();
			if(newsPage != null) {
				list = newsPage.getList();
			}
			
			if(list.size() <= 0) {
				faultResult("未查询到数据信息！");
			}else {
				success = true;
				for(int i = 0; i < list.size(); i++) {
					EbNews news = list.get(i);
					boolean flagType = true;
					String newsType2 = news.getNewsType()+"";//通知公告判断
					String status2 = news.getStatus()+"";//0部门不可以看到、1部门可以看到
					if(newsType2.equals(13)&&status2.equals(0)){
						flagType = false;
					}
				if(flagType)	{
					Hashtable<String, Object> table = new Hashtable<String, Object>();
					
					table.put("newsId", getCheckedString(news.getNewsId() + ""));//消息ID
					table.put("userId", getCheckedString(news.getUserId() + ""));//消息创建人ID
					table.put("userCode", getCheckedString(news.getUserCode()));//消息创建人code
					table.put("userName", getCheckedString(news.getUserName()));//消息创建人姓名
					table.put("newsType", news.getNewsType() + "");//消息类型
					table.put("newsContent", getCheckedString(news.getNewsContent()));//消息内容
					table.put("coupleBack", getCheckedString(news.getCoupleBack()));//反馈内容
					table.put("time", TimeUtil.getTimeStyle(news.getNewsTime(), "yyyy-MM-dd HH:mm"));//消息时间
					table.put("businessId", getCheckedString(news.getBusinessId() + ""));//关联企业ID
					table.put("businessName", getCheckedString(news.getBusinessName()));//关联企业名称
					table.put("itemId", getCheckedString(news.getItemId() + ""));//相关事项ID
					table.put("itemName", getCheckedString(news.getItemName()));//相关事项名称
					table.put("status", news.getStatus() + "");//状态
					table.put("departId", getCheckedString(news.getDepartId() + ""));//关联部门id
					table.put("departName", getCheckedString(news.getDepartName()));//关联部门名称
					
/**************************2015-11-15 feini 传递特别程序id******************************************/
					String status = news.getStatus()+"";
					String newsType = news.getNewsType()+"";
					if((status.equals("6") && newsType.equals("5")) || ((status.equals("1")||status.equals("2")) && newsType.equals("6"))){
						//新建特别程序消息提醒
						TaskInterService taskSer = new TaskInterService();
						EbTask task = taskSer.getTaskByBussId(news.getBusinessId()+"");
						if(task != null){
							String taskId = task.getTaskId()+"";
							String itemId = news.getItemId() + "";
							EbTaskDistribute taskD = taskSer.getDistrByTIId(taskId, itemId);
							if(taskD != null){
								String distrId = taskD.getDistrId()+"";
								EbSpcProgram spc = taskSer.getByDistriId(distrId);
								if(spc != null){
									String spcId = spc.getProgramId()+"";
									table.put("programId",getCheckedString(spcId));
								}
							}
						}
					}
/********************************************************************/
				
					result.add(table);
				}//判断通知公告部分
			}
				
				this.setAttr("success", success);
				this.setAttr("result", result);
				this.setAttr("errorMsg", "");
				this.setAttr("total_numb", newsPage.getTotalRow());
				this.setAttr("total_Page", newsPage.getTotalPage());
				this.setAttr("corrunt_Page", newsPage.getPageNumber());
			}
		}

		render(new JsonRender().forIE());
	}
	
	/**
	 * 40.审批特别程序（办事员）
	 */
	public void examine_spc_program(){
		String programId = getPara("programId");//特别程序id
		String examineStatus = getPara("examineStatus");//审批状态 （1 批准 、 2不批准）
		String opinion = getPara("opinion", "无");//审批意见
		
		String userId = Config.getSessionUserID(this);
		
		if((programId==null) || programId.equals("") || "null".equalsIgnoreCase(programId)){
			faultResult( "请传递特别程序id");		
		}else{
			if((examineStatus==null) || examineStatus.equals("") || "null".equalsIgnoreCase(examineStatus)){
				faultResult( "请传递审批状态 参数");	
			}else{
				if((opinion==null) || opinion.equals("") || "null".equalsIgnoreCase(opinion)){
					faultResult( "请传递审批意见");	
				}else{
					boolean success = false;
					String message = "批示不成功，请重试！";
					TaskInterService spcSer = new TaskInterService();
					EbSpcProgram spc = spcSer.getById(programId);
					Hashtable<String, String> result= new Hashtable<String, String>();
					if(spc!=null){
						
						spc.setExamineStatus(new BigDecimal(examineStatus));
						spc.setOpinion(opinion);
						spc.setExaminTime(System.currentTimeMillis()+"");
						spc.setUserId(new BigDecimal(Config.getSessionUserID(this)));
						if(spc.update()){
							success = true;
							message = "审批完成";
							
/**************2015-11-12 feini 使状态变为已暂停***********************/
							if(examineStatus.equals("1")){
								boolean flagForDistri = false;
								EbTaskDistribute taskd = spcSer.getDistrByDId(spc.getDistrId()+"");
								if(taskd != null){
									taskd.setTransactionStatus(new BigDecimal(5));//办理状态（0 未开始、1 办理中、2 已逾期、3 办理完结、4 被退回 、5暂停）
									try {
										flagForDistri = taskd.update();
									} catch (Exception e) {
										e.printStackTrace();
										flagForDistri = taskd.update();
									}
									if(!flagForDistri){
										message = "审批完成,但无法更改原事项！";
									}
								}else{
									message = "审批完成,但查询不到原事项！";
								}
							}
/*********************************************************************/
							MsgInterService msgService = new MsgInterService();
							UserInterService userService = new UserInterService(this);
							EbUserInfo userInfo = userService.findById(userId);
							EbBusiness bussInfo = new BussInterService().findById(spc.getBusinessId() + "");

							if(userInfo == null || bussInfo == null) {
								logger.error("用户信息或企业信息未找到");
							}else {
								//给消息表中插入数据
								String newsContent = userInfo.getDepartName() + userInfo.getUserName() + "审批了" + 
										bussInfo.getBusinessName() + "所需办理的" + spc.getItemName() + "（事项）关联的特别程序";
								String newsTime = System.currentTimeMillis() + "";
								try {
									msgService.addNews(userInfo, bussInfo, "6", newsContent, opinion, 
											newsTime, spc.getItemId() + "", spc.getItemName(), examineStatus, spc.getDepartId() + "", spc.getDepartName());
									
//									List<EbUserInfo> leaders = userService.filterUsersList("2", "");//获取所有的领导用户
									
									List<String> strAlias = new ArrayList<String>();
									strAlias.add(spc.getDepartId() + "");
//									for (int i = 0; i < leaders.size(); i++) {
//										strAlias.add(leaders.get(i).getUserId() + "");
//									}
									
									String strStatus = "";
									if(examineStatus.equals("1")) {
										strStatus = "批准";
									}else if(examineStatus.equals("2")) {
										strStatus = "不批准";
									}
									newsContent = bussInfo.getBusinessName() + "企业的" + spc.getItemName() + 
											"（特别程序）已被" + userInfo.getDepartName() + userInfo.getUserName() + 
											"审批完成，审批状态：" + strStatus + "，审批意见：" + opinion;
									
									Hashtable<String, String> map = new Hashtable<String, String>();
									map.put("newsType", "6");
									map.put("content", newsContent);
									map.put("status", examineStatus);
									map.put("programId", getCheckedString(spc.getProgramId() + ""));
									PushUtil.pushNotifyToTagert(strAlias, StringUtil.getJson(map));
									
//									newsContent = "您的" + spc.getItemName() + "（特别程序）已被" + userInfo.getDepartName() + 
//											userInfo.getUserName() + "审批完成，审批状态：" + strStatus + "，审批意见：" + opinion;
//									strAlias.clear();
//									strAlias.add(bussInfo.getBusinessId() + "");
//									
//									Hashtable<String, String> table = new Hashtable<String, String>();
//									table.put("newsType", "6");
//									table.put("content", newsContent);
//									table.put("status", examineStatus);
//									table.put("programId", getCheckedString(spc.getProgramId() + ""));
//									PushUtil.pushNotifyToTagert(strAlias, StringUtil.getJson(table));
									
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						}
						result.put("message", message);
					}
					this.setAttr("result", result);
					setAttr("success", success);
				}
			}
		}
		render(new JsonRender().forIE());
	}
	
	/**
	 * 42.签收任务,事项签收
	 */
	public void signTask(){
		String bussId = this.getPara("businessId");
		String streva = this.getPara("taskList");
		String user_id = Config.getSessionUserID(this);
		String strCompEva = "{\"taskList\":" + streva + "}";
		LogUtil.log("测试获取jdson:"+strCompEva);
		if(bussId == null || "".equals(bussId) || "null".equalsIgnoreCase(bussId)) {
			faultResult("必须传递企业id，签收失败！");
		}else{
			String taskId ="";
			EbUserInfo userInfo = new UserInterService(this).findById(user_id);
			TaskInterService taskSer = new TaskInterService();
			MsgInterService msgService = new MsgInterService();
			BussInterService bsService = new BussInterService();
			EbTask task = taskSer.getTaskByBussId(bussId);
			if(task!= null && userInfo != null){
				taskId = task.getTaskId()+"";//获取该任务的id
				
				boolean flag = false;
				String errorMsg = "签收失败";
				try {
					JSONObject evalue = new JSONObject(strCompEva);
					String coupleStr = evalue.optString("coupleBack");
					JSONArray taskEvas = (JSONArray) evalue.optJSONArray("taskList");
					String strTime = System.currentTimeMillis() + "";
					EbBusiness bussInfo = bsService.findById(bussId);
					
					for(int i = 0; i < taskEvas.length(); i++) {
						//事项id列表
						JSONArray items = taskEvas.optJSONObject(i).optJSONArray("items");
						if(items.length()>0){
							int total = 0;
							for(int j=0;j<items.length();j++){
								JSONObject obj = items.optJSONObject(j);
								if(obj != null) {
									String distrId = obj.optString("distrId");//事项分发id
									String itemId = obj.optString("itemId");//事项id
									
									boolean flag2 = false;
									//进行事项签收
									int tranStatus = 6;//首席未分发
									int sign_status = 0;//事项未签收
									EbTaskDistribute distrInfo = taskSer.getDistrByDId(distrId);
									if(distrInfo != null) {
										tranStatus = distrInfo.getTransactionStatus().intValue();
										sign_status = distrInfo.getSignStatus().intValue();
									}
									//（1VIP服务专员管理员、2VIP服务专员权限 、3单位首席权限、4部门跟办人权限、5市级服务专员权限、6 VIP服务专员管理员+单位首席权限、7 VIP服务专员+单位首席权限）
									int autho = userInfo.getAuthority().intValue();
									if(autho == 3 || autho == 6 || autho == 7 ){//市级服务专员签收任务
										if(userInfo.getDepartName().equals(MCubeAppConfig.getInstance().getZwb_name_city())){
											//市级服务专员都有首席和跟办人的权限
											if(tranStatus == 0){//首席已分发未开始，作为跟办人签收事项
												flag2 = taskSer.signTaskByDistriByG(distrId, itemId, userInfo, taskId, strTime);
											}else if(sign_status == 0){//首席未分发，作为首席签收事项
												flag2 = taskSer.signTaskByDistri(distrId, itemId, userInfo, taskId, strTime);
											}
										}else{
											flag2 = taskSer.signTaskByDistri(distrId, itemId, userInfo, taskId, strTime);//部门首席签收部门任务事项
										}
									}else if(autho == 4){
										flag2 = taskSer.signTaskByDistriByG(distrId, itemId, userInfo, taskId, strTime);//部门跟办人签收部门任务事项
									}else if(autho == 5){
										//市级服务专员都有首席和跟办人的权限
										if(tranStatus == 0){//首席已分发未开始，作为跟办人签收事项
											flag2 = taskSer.signTaskByDistriByG(distrId, itemId, userInfo, taskId, strTime);
										}else if(sign_status == 0){//首席未分发，作为首席签收事项
											flag2 = taskSer.signTaskByDistri(distrId, itemId, userInfo, taskId, strTime);
										}
									}
									
									if(flag2){
										total++;
										//存档消息
										try {
//											EbTaskDistribute distrInfo = taskSer.getDistrByDId(distrId);
											if(distrInfo != null) {
												boolean flg = msgService.addNews(userInfo, bussInfo, "1", distrInfo.getItemName(), coupleStr, 
														strTime, itemId, distrInfo.getItemName(), "1", distrInfo.getDepartId() + "", 
														distrInfo.getDepartName());
												if(flg) {
													EbItem item = new ItemInterService().getItemById(itemId);
													if(item == null) {
														item = GovNetUtil.getItemInfo(itemId);
													}
													String day = "15";
													if(item != null) {
														day = item.getTimeLimit() + "";
													}
													
													String content = "您分发的任务，已被" + distrInfo.getDepartName() + 
															"单位签收。";
													List<String> strAlias = new ArrayList<String>();
													strAlias.add(Config.ZWB);
													
													Hashtable<String, String> map = new Hashtable<String, String>();
													map.put("newsType", "1");
													map.put("content", content);
													map.put("status", "1");
													map.put("businessId", getCheckedString(bussInfo.getBusinessId() + ""));
													PushUtil.pushNotifyToTagert(strAlias, StringUtil.getJson(map));
													
//													content = "您所需办理的事项：" + distrInfo.getItemName() + "，" + distrInfo.getDepartName() + 
//															"单位已经开始办理，预计" + day + "个工作日内完成。";
//													List<String> strAlias1 = new ArrayList<String>();
//													strAlias1.add(bussInfo.getBusinessId() + "");
//													
//													Hashtable<String, String> table = new Hashtable<String, String>();
//													table.put("newsType", "1");
//													table.put("content", content);
//													table.put("status", "1");
//													table.put("businessId", getCheckedString(bussInfo.getBusinessId() + ""));
//													PushUtil.pushNotifyToTagert(strAlias1, StringUtil.getJson(table));
												}else {
													logger.error("消息插入数据库失败");
												}
											}else {
												logger.error("事项分发类没有找到");
											}
										} catch (Exception e) {
											e.printStackTrace();
										}
									}
								}else {
									continue;
								}
							}
							if(total==items.length()){
								flag = true;
							}
						}
					}
					if(flag) {
						this.setAttr("success", true);
						Hashtable<String, Object> result = new Hashtable<String, Object>();
						result.put("message", "签收成功！");
						this.setAttr("result", result);
						this.setAttr("errorMsg", "");
					}else {
						faultResult(errorMsg);
					}
				} catch (JSONException e) {
					faultResult("签收任务传递数据格式错误，分发失败！");
					e.printStackTrace();
				} catch (Exception e) {
					faultResult("异常错误，签收失败！");
					e.printStackTrace();
				}
			}else {
				faultResult("未查询到该企业对应的任务id");
			}
		}
		render(new JsonRender().forIE());
	}
	
	/**
	 * 新增部门事项
	 */
	public void addItem() {
		String deptId = this.getPara("departId");	//部门id必填
		String deptName = this.getPara("departName"); //部门名称必填
		String itemName = this.getPara("itemName"); //事项名称必填
		String limitTime = this.getPara("limitTime"); //事项办理期限必填且为数字
		
		if (deptId == null || deptId.equals("") || deptId.equalsIgnoreCase("null")) {
			faultResult("必须传递部门id！");
		}else if(deptName == null || deptName.equals("") || deptName.equalsIgnoreCase("null")) {
			faultResult("必须传递部门名称！");
		}else if(itemName == null || itemName.equals("") || itemName.equalsIgnoreCase("null")) {
			faultResult("必须传递事项名称！");
		}else if(limitTime == null || limitTime.equals("") || limitTime.equalsIgnoreCase("null")) {
			faultResult("必须传递事项办理期限！");
		}else if(!MD5Util.isNumeric(limitTime)) {
			faultResult("办理期限必须为数字！");
		}else {
			String itemId = new TaskInterService().addItem(deptId, deptName, itemName, limitTime);
			if(itemId == null || itemId.equals("") || itemId.equalsIgnoreCase("null")) {
				faultResult("新增部门事项失败！");
			}else {
				this.setAttr("success", true);
				Hashtable<String, String> result = new Hashtable<String, String>();
				result.put("itemId", itemId);
				result.put("itemName", itemName);
				result.put("departId", deptId);
				result.put("departName", deptName);
				result.put("limitTime", limitTime);
				result.put("message", "新增部门事项成功！");
				
				this.setAttr("result", result);
				this.setAttr("errorMsg", "");
			}
		}
		
		render(new JsonRender().forIE());
	}
	
	/**
	 * 41新任务列表（办事员）
	 */
	public void new_task_list(){
		String userId = Config.getSessionUserID(this);
		UserInterService userService = new UserInterService(this);
		EbUserInfo userInfo = userService.findById(userId);
		if(userInfo == null) {
			faultResult("未找到用户信息！");
		}else if(userInfo.getDepartId() == null || userInfo.getDepartId().equals("") 
				|| userInfo.getDepartId().toString().equalsIgnoreCase("null")) {
			faultResult("无法查询到相关信息！");
		}else {
			//先查询该用户的对应部门是政务办，则显示任务表状态
			String auth = userInfo.getAuthority()+"";//权限标识（ 1 招商局权限 、 2 政务办权限 、3 其他部门）（办事员才有权限）
			TaskInterService taskSer = new TaskInterService();
			BussInterService bussSer = new BussInterService();
			List<Hashtable<String, String>> result = new ArrayList<Hashtable<String, String>>();
			boolean success = false;
			String errorMsg = "数据为空";
			
			if(auth.equals("2")){
				List<EbTask> taskNotSign = taskSer.getTaskBySignStatus("0");//未签收企业
				List<EbTask> taskSign = taskSer.getTaskBySignStatus("1");//已签收企业
				if(taskSign.size()>0){
					for(int i=0;i<taskSign.size();i++){
						EbTask task = taskSign.get(i);
						String bussId = task.getBusinessId()+"";
						EbBusiness buss = bussSer.getBussById(bussId);
						if(buss!=null){
							String distStatus = buss.getDistributedStatus()+"";//是否分发任务(0未分发，1已分发)
							
							if(distStatus.equals("0")){
								taskNotSign.add(task);//如果任务未分发，则添加入消息提醒列表
							}
						}
					}
				}
				if(taskNotSign.size()>0){
					//将任务未分发和未签收的列表显示出来
					success = true;
					errorMsg = "成功";
					
					for(int j=0;j<taskNotSign.size();j++){
						EbTask tasknew = taskNotSign.get(j);
						Hashtable<String, String> resultHash = new Hashtable<String, String>();
						
/**************2015-11-12 feini 增加企业类型*********************/
						String bussId = tasknew.getBusinessId()+"";
						EbBusiness buss = bussSer.getBussById(bussId);
						String typeId = "";//企业类型id
						String typeName = "";//企业类型名称
						if(buss != null){
							typeId = buss.getTypeId()+"";
							EbBusinessType bussType = bussSer.getBsTypeById(typeId);
							if(bussType != null){
								typeName = bussType.getTypeName();
							}
						}
						resultHash.put("typeId", getCheckedString(typeId));
						resultHash.put("typeName", getCheckedString(typeName));
/***************************************************************/
						
			    		resultHash.put("businessId", getCheckedString(tasknew.getBusinessId()+""));//企业id
			    		resultHash.put("taskId", getCheckedString(tasknew.getTaskId()+""));//任务Id
			    		resultHash.put("signStatus", getCheckedString(tasknew.getSignStatus()+""));//签收状态 0 未签收、1 已签收
			    		resultHash.put("distrStatus","0");//分发状态 0 未分发、1 已分发
			    		resultHash.put("distrTaskStatus", "0");////是否已签收任务 0 未签收、1 已签收
			    		resultHash.put("businessName", getCheckedString(tasknew.getBusinessName()));//企业名称
			    		
			    		result.add(resultHash);
					}
				}
/****************2015-11-12 feini 事项已签收分发的***********************************************/
				//事项已分发的新任务列表，但是事项未签收

				String deptId = userInfo.getDepartId()+"";
				List<EbTaskDistribute> taskDistri = taskSer.getDistriTIdByStatus("0",deptId);//已分发，但是事项未签收
				
				if(taskDistri.size()>0){
					//将任务清单未签收的列表显示出来
					success = true;
					errorMsg = "成功";
					
					for(int j=0;j<taskDistri.size();j++){
						EbTaskDistribute taskdi = taskDistri.get(j);
						Hashtable<String, String> resultHash = new Hashtable<String, String>();
						
						EbTask bussfortask = taskSer.getTaskById(taskdi.getTaskId()+"");
						if(bussfortask!=null){
/**************2015-11-12 feini 增加企业类型*********************/
							String bussId = bussfortask.getBusinessId()+"";
							EbBusiness buss = bussSer.getBussById(bussId);
							String typeId = "";//企业类型id
							String typeName = "";//企业类型名称
							if(buss != null){
								typeId = buss.getTypeId()+"";
								EbBusinessType bussType = bussSer.getBsTypeById(typeId);
								if(bussType != null){
									typeName = bussType.getTypeName();
								}
							}
							resultHash.put("typeId", getCheckedString(typeId));
							resultHash.put("typeName", getCheckedString(typeName));
/******************************增加企业类型end*********************************/
							resultHash.put("businessId", getCheckedString(bussfortask.getBusinessId()+""));//企业id
				    		resultHash.put("businessName", getCheckedString(bussfortask.getBusinessName()));//企业名称
						}else{
							resultHash.put("businessId", "");//企业id
				    		resultHash.put("businessName", "");//企业名称
				    		resultHash.put("typeId", "");
							resultHash.put("typeName", "");
						}
						
			    		resultHash.put("taskId", getCheckedString(taskdi.getTaskId()+""));//任务Id
			    		resultHash.put("signStatus", "1");//签收状态 0 未签收、1 已签收
			    		resultHash.put("distrStatus","1");//分发状态 0 未分发、1 已分发
			    		resultHash.put("distrTaskStatus", "0");////是否已签收任务 0 未签收、1 已签收
			    		
			    		result.add(resultHash);
					}
				}
/************************事项已签收分发的end***************************************/
			}else if(auth.equals("3") || auth.equals("1")){
				//该用户部门为其他部门，则显示任务清单表状态
				String deptId = userInfo.getDepartId()+"";
				List<EbTaskDistribute> taskDistri = taskSer.getDistriTIdByStatus("0",deptId);//已分发，但是事项未签收
				
				if(taskDistri.size()>0){
					//将任务清单未签收的列表显示出来
					success = true;
					errorMsg = "成功";
					
					for(int j=0;j<taskDistri.size();j++){
						EbTaskDistribute taskdi = taskDistri.get(j);
						Hashtable<String, String> resultHash = new Hashtable<String, String>();
						
						EbTask bussfortask = taskSer.getTaskById(taskdi.getTaskId()+"");
						if(bussfortask!=null){
/**************2015-11-12 feini 增加企业类型*********************/
							String bussId = bussfortask.getBusinessId()+"";
							EbBusiness buss = bussSer.getBussById(bussId);
							String typeId = "";//企业类型id
							String typeName = "";//企业类型名称
							if(buss != null){
								typeId = buss.getTypeId()+"";
								EbBusinessType bussType = bussSer.getBsTypeById(typeId);
								if(bussType != null){
									typeName = bussType.getTypeName();
								}
							}
							resultHash.put("typeId", getCheckedString(typeId));
							resultHash.put("typeName", getCheckedString(typeName));
/***************************************************************/
							resultHash.put("businessId", getCheckedString(bussfortask.getBusinessId()+""));//企业id
				    		resultHash.put("businessName", getCheckedString(bussfortask.getBusinessName()));//企业名称
						}else{
							resultHash.put("businessId", "");//企业id
				    		resultHash.put("businessName", "");//企业名称
				    		resultHash.put("typeId", "");
							resultHash.put("typeName", "");
						}
						
			    		resultHash.put("taskId", getCheckedString(taskdi.getTaskId()+""));//任务Id
			    		resultHash.put("signStatus", "1");//签收状态 0 未签收、1 已签收
			    		resultHash.put("distrStatus","1");//分发状态 0 未分发、1 已分发
			    		resultHash.put("distrTaskStatus", "0");////是否已签收任务 0 未签收、1 已签收
			    		
			    		result.add(resultHash);
					}
				}
			}
			setAttr("result", result);
			setAttr("errorMsg", errorMsg);
			setAttr("success", success);
		}
		render(new JsonRender().forIE());
	}
	
	/**
	 * 任务重发
	 */
	public void retryTask() {
		String distrId = this.getPara("distrId");
		String userId = Config.getSessionUserID(this);
		
		TaskInterService tService = new TaskInterService();
		EbTaskDistribute distrInfo = tService.getDistrByDId(distrId);
		if(distrInfo == null) {
			faultResult("未找到该事项分发数据");
		} else {
			EbUserInfo userInfo = new UserInterService(this).findById(userId);
			if(userInfo == null) {
				faultResult("当前用户不存在，不能分发");
			}else {
				distrInfo.setSignStatus(new BigDecimal("0"));
				distrInfo.setTransactionStatus(new BigDecimal("0"));
				String time = System.currentTimeMillis() + "";
				distrInfo.setUserId(userInfo.getUserId());
				distrInfo.setStatus(new BigDecimal("1"));
				distrInfo.set("distrib_time", time);
				distrInfo.setDistrUserCode(userInfo.getUserAccount());
				if(distrInfo.update()) {
					this.setAttr("success", true);
					Hashtable<String, String> result = new Hashtable<String, String>();
					result.put("message", "事项分发成功");
					this.setAttr("result", result);

					EbTask taskInfo = tService.getTaskById(distrInfo.getTaskId() + "");
					EbBusiness bussInfo = new BussInterService().findById(taskInfo.getBusinessId() + "");
					MsgInterService msgService = new MsgInterService();
					
					if(bussInfo != null && taskInfo != null) {
						//给消息表中插入数据
						String newsContent = userInfo.getDepartName() + userInfo.getUserName() + "重新分发了" + 
								distrInfo.getItemName() + "（事项）";
						String newsTime = System.currentTimeMillis() + "";
						try {
							msgService.addNews(userInfo, bussInfo, "4", newsContent, null, 
									newsTime, distrInfo.getItemId() + "", distrInfo.getItemName(), "5", distrInfo.getDepartId() + "", distrInfo.getDepartName());
							
							List<String> strAlias = new ArrayList<String>();
							if(distrInfo.getDepartName().contains(MCubeAppConfig.getInstance().zwb_name)) {
								strAlias.add(Config.ZWB);
							}else if(distrInfo.getDepartName().contains(MCubeAppConfig.getInstance().zsj_name)) {
								strAlias.add(Config.ZSJ);
							}else {
								strAlias.add(distrInfo.getDepartId() + "");
							}
							
							newsContent = "您有新任务：" + bussInfo.getBusinessName() + "企业的" + distrInfo.getItemName() + 
									"（事项）已被" + userInfo.getDepartName() + userInfo.getUserName() + 
									"重发任务，请签收任务";
							
							Hashtable<String, String> map = new Hashtable<String, String>();
							map.put("newsType", "4");
							map.put("content", newsContent);
							map.put("status", "5");
							map.put("businessId", getCheckedString(bussInfo.getBusinessId() + ""));
							PushUtil.pushNotifyToTagert(strAlias, StringUtil.getJson(map));
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}else {
					faultResult("数据库更新失败，事项分发失败！");
				}
			}
		}
		render(new JsonRender().forIE());
	}
	
	/**
	 * 45特殊任务继续办理
	 */
	public void reset_spc_program(){
		String programId = getPara("programId");//特别程序id
		
		String transactor_id = Config.getSessionUserID(this);//当前登录用户Id
		UserInterService userIS = new UserInterService(this);
		EbUserInfo userInfo = userIS.findById(transactor_id);
		
		if((programId==null) || programId.equals("") || "null".equalsIgnoreCase(programId)){
			faultResult( "请传递特别程序id");
		}else{
			if(userInfo == null){
				faultResult( "找不到当前登录用户信息");
			}else{
				String userAuth = userInfo.getAuthority()+"";//权限标识（ 1 招商局权限 、 2 政务办权限 、3 其他部门）（办事员才有权限）
				if(userAuth.equals("2")){
					TaskInterService taskSer = new TaskInterService();
					EbSpcProgram spc = taskSer.getById(programId);
					if(spc == null){
						faultResult( "该特别程序信息不存在");
					}else{
						String spcExamineStatus = spc.getExamineStatus()+"";
						if(spcExamineStatus.equals("1")){
							String taskDId = spc.getDistrId()+"";
							EbTaskDistribute taskD = taskSer.getDistrByDId(taskDId);
							
							if(taskD == null){
								faultResult( "该特殊任务原任务不存在");
							}else{
								String tranStatus = taskD.getTransactionStatus()+"";//办理状态（0 未开始、1 办理中、2 已逾期、3 办理完结、4 被退回 、5暂停）
								if(tranStatus.equals("5")){
									boolean success = false;
									String message = "继续办理失败";
									boolean flag = false;
									boolean flagForSpc = false;
									
									//原任务更改办理状态
									taskD.setTransactionStatus(new BigDecimal(1));
									flag = taskD.update();
									
									//特殊任务更改状态
									spc.setExamineStatus(new BigDecimal(4));//审批状态（ 0 待审批 、 1 已批准 、 2未批准、4已继续办理）
									flagForSpc =spc.update();
									
									if(flag && flagForSpc){
										success = true;
										message = "继续办理操作成功!";
										
										MsgInterService msgService = new MsgInterService();
										EbBusiness bussInfo = new BussInterService().findById(spc.getBusinessId() + "");
										
										//给消息表中插入数据
										String newsContent = userInfo.getDepartName() + userInfo.getUserName() + 
												"重启了" + bussInfo.getBusinessName() + "所需办理的事项：" + taskD.getItemName();
										String newsTime = System.currentTimeMillis() + "";
										try {
											msgService.addNews(userInfo, bussInfo, "1", newsContent, null, 
													newsTime, taskD.getItemId() + "", taskD.getItemName(), "8", 
													taskD.getDepartId() + "", taskD.getDepartName());
											
											List<String> strAlias = new ArrayList<String>();
											strAlias.add(taskD.getDepartId() + "");
//											if(taskD.getDepartName().contains("政务办")) {
//												strAlias.add(Config.ZWB);
//											}
											newsContent = userInfo.getDepartName() + userInfo.getUserName() + "重新启动了" + bussInfo.getBusinessName() + 
													"（企业）所需办理的" + taskD.getItemName() + "（事项），请尽快办理。";
											
											Hashtable<String, String> map = new Hashtable<String, String>();
											map.put("newsType", "1");
											map.put("content", newsContent);
											map.put("status", "8");
											map.put("businessId", getCheckedString(bussInfo.getBusinessId() + ""));
											map.put("businessName", getCheckedString(bussInfo.getBusinessName()));
											PushUtil.pushNotifyToTagert(strAlias, StringUtil.getJson(map));
											
//											strAlias.clear();
//											strAlias.add(bussInfo.getBusinessId() + "");
//											newsContent = userInfo.getDepartName() + userInfo.getUserName() + "重新启动了您所需办理的" + 
//													taskD.getItemName() + "（事项），相关办事部门已尽快办理。";
//											
//											Hashtable<String, String> table = new Hashtable<String, String>();
//											table.put("newsType", "1");
//											table.put("content", newsContent);
//											table.put("status", "8");
//											table.put("businessId", getCheckedString(bussInfo.getBusinessId() + ""));
//											table.put("businessName", getCheckedString(bussInfo.getBusinessName()));
//											PushUtil.pushNotifyToTagert(strAlias, StringUtil.getJson(table));
										} catch (Exception e) {
											e.printStackTrace();
										}
									}
									setAttr("success", success);
									Hashtable<String, String> result = new Hashtable<String, String>();
									result.put("message", message);
									setAttr("result", result);
								}else{
									faultResult("当前办理事项状态非暂停！");
								}
							}
						}else{
							faultResult("当前特殊状态未被批准！");
						}
					}
				}else{
					faultResult("当前用户非政务办人员，不能办理！");
				}
			}
		}
		render(new JsonRender().forIE());
	}
	
	/**
	 * 46.部门跟办人列表
	 */
	public void deptUserList(){
		String deptId = this.getPara("departId");//获取部门Id
		Map<String, Integer> map = new HashMap<String, Integer>();
		List<EbUserInfo> dUserList = GovNetUtil.getEbUserInfos(deptId, 1, "99999", map);
		
		boolean success = false;
		String message = "数据为空";

		if (dUserList != null && !dUserList.isEmpty()) {
			//这里取出的是区级的单位
			success = true;
			message = "成功!";
			List<Hashtable<String, String>> result = new ArrayList<Hashtable<String, String>>();
			for (int i = 0; i < dUserList.size(); i++) {
				EbUserInfo dUser = dUserList.get(i);
				Hashtable<String, String> resultHash = new Hashtable<String, String>();
				// 用户账号
				resultHash.put("userCode",getCheckedString(dUser.getUserAccount()));
				// 用户名称
				resultHash.put("userName",getCheckedString(dUser.getUserName()));
				resultHash.put("departId", getCheckedString(dUser.getDepartId()+""));// 办事员没有关注
				resultHash.put("departName",getCheckedString( dUser.getDepartName() + ""));//企业落户状态 1 已落户 0 正在办理

				result.add(resultHash);
			}
			this.setAttr("result", result);
		}else{
			//取出市级单位用户列表
			UserInterService uSer = new UserInterService(this);
			List<EbUserInfo> dUserList2 = uSer.getUserByD(deptId);
			if (dUserList2 != null && !dUserList2.isEmpty()) {
				//这里取出的是区级的单位
				success = true;
				message = "成功!";
				List<Hashtable<String, String>> result = new ArrayList<Hashtable<String, String>>();
				for (int i = 0; i < dUserList2.size(); i++) {
					EbUserInfo dUser = dUserList2.get(i);
					Hashtable<String, String> resultHash = new Hashtable<String, String>();
					// 用户账号
					resultHash.put("userCode",getCheckedString(dUser.getUserAccount()));
					// 用户名称
					resultHash.put("userName",getCheckedString(dUser.getUserName()));
					resultHash.put("departId", getCheckedString(dUser.getDepartId()+""));
					resultHash.put("departName",getCheckedString(dUser.getDepartName() + ""));//企业落户状态 1 已办结 0 正在办理

					result.add(resultHash);
				}
				this.setAttr("result", result);
			}
		}
		setAttr("success", success);
		setAttr("message", message);
		render(new JsonRender().forIE());
	}
	
	/**
	 * 47 VIP服务专员列表
	 */
	public void vipUserList(){
		boolean success = false;
		String message = "数据为空";
		
		UserInterService userSer = new UserInterService(this);
		List<EbUserInfo> vipUserList = userSer.getAllUserByAuth("2");
		if(vipUserList.size()>0){
			success = true;
			message = "成功!";
			List<Hashtable<String, String>> result = new ArrayList<Hashtable<String, String>>();
			for (int i = 0; i < vipUserList.size(); i++) {
				EbUserInfo dUser = vipUserList.get(i);
				Hashtable<String, String> resultHash = new Hashtable<String, String>();
				// 用户账号
				resultHash.put("userCode",getCheckedString(dUser.getUserAccount()));
				// 用户名称
				resultHash.put("userName",getCheckedString(dUser.getUserName()));
				resultHash.put("authority",getCheckedString(dUser.getAuthority()+""));//用户权限
				resultHash.put("departId", getCheckedString(dUser.getDepartId()+""));
				resultHash.put("departName",getCheckedString( dUser.getDepartName() + ""));//企业落户状态 1 已办结 0 正在办理

				result.add(resultHash);
			}
			this.setAttr("result", result);
		}
		
		setAttr("success", success);
		setAttr("message", message);
		render(new JsonRender().forIE());
	}
	
	/**
	 * 48 VIP服务专员配置
	 */
	public void vipUserConfig(){
		String businessId = this.getPara("businessId");
		String userCode = this.getPara("userCode");//必须对应用户表的user_account
		
		boolean success = false;
		String message = "设置失败";
		Hashtable<String, Object> result = new Hashtable<String, Object>();
		
		String service_admin = "";
		BussInterService bussSer = new BussInterService();
		EbBusiness buss = bussSer.getBussById(businessId);
		if(buss != null){
			service_admin = buss.getService_admin();
			if(service_admin == null || "".equals(service_admin) || "null".equals(service_admin)){
				buss.setService_admin(userCode);
				if(buss.update()){
					success = true;
					message = "设置成功";
				}
			}
		}
		result.put("message", message);
		
		setAttr("success", success);
		setAttr("result", result);
		render(new JsonRender().forIE());
	}
	
	/**
	 * 49 事项配置部门跟办人
	 * @throws JSONException 
	 */
	public void itemConfigUser() throws JSONException{
		String streva = this.getPara("taskList");
		String strCompEva = "{\"taskList\":" + streva + "}";
		String taskId = this.getPara("taskId");//任务Id
		
		boolean success = false;
		String message = "配置事项跟办人失败";
		String areaMsg = "启动办件失败！";
		Hashtable<String, Object> result = new Hashtable<String, Object>();
		
		if((taskId==null) || taskId.equals("") || "null".equalsIgnoreCase(taskId)){
			message="请传递任务id";
		}else{
			//拆分任务
			JSONObject evalue = new JSONObject(strCompEva);
			JSONArray taskEvas = (JSONArray) evalue.optJSONArray("taskList");
			if(taskEvas.length()>0){
					int total = 0;//统计总数
					int total_gov = 0;//启动成功的数量
					int gov_numb = 0;//是区级事项的数量
					for(int j=0;j<taskEvas.length();j++){
						JSONObject obj = taskEvas.optJSONObject(j);
						if(obj != null) {
							String distrId = obj.optString("distrId");//事项分发id
							String itemId = obj.optString("itemId");//事项id
							String itemUserCode = obj.optString("itemUserCode");//部门跟办人
							
		/*************************分配事项单个的跟班人*****************************/
							//根据任务id和事项id,以及用户id，标记事项完成
							TaskInterService taskSer = new TaskInterService();
							EbTaskDistribute distrInfo =  taskSer.getDistrByTIId(taskId, itemId, distrId);
							
							EbTask taskInfo = taskSer.getTaskById(taskId);
							String businessId = "";
							if (taskInfo != null) {
								businessId = taskInfo.getBusinessId() + "";
							}
							
							BussInterService bussSer = new BussInterService();
							EbBusiness buss = bussSer.getBussById(businessId);
							
							if(distrInfo != null){
								UserInterService userIS = new UserInterService(this);
								EbUserInfo userInfo = userIS.getUserByCD(itemUserCode,distrInfo.getDepartId()+"");//取出这个部门的用户信息
								if(userInfo != null){
									distrInfo.setTransactorId(userInfo.getUserId());
									distrInfo.setTransUserCode(itemUserCode);
									distrInfo.setTransactionStatus(new BigDecimal(0));//办理状态（0 首席已分发未开始、1 办理中、2 已逾期、3 办理完结、4 被退回 、5暂停、6首席未分发）
									
									if(distrInfo.update()){
										if (distrInfo.getItemType().intValue() == 1) {
											//区级办件流程
											String controlSeq = "";
											gov_numb++;
											if (buss != null) {
												/**************启动办件**************************/
												String userCode = "m_" + buss.getContactPhone();
												//启动办件、生成办件流水号
												controlSeq = GovNetUtil.startItem(itemId, userCode);
											}
											if (!NullUtil.isEmpty(controlSeq)) {
												
												/************************更新办件申请人信息********************/
												boolean updateControlFlag = false;
												updateControlFlag = GovNetUtil.updateControlUser(controlSeq, buss.getBusinessName(), 
														buss.getContactName(), "", "", buss.getContactPhone(), "");
												
												if (updateControlFlag) {
													/**************提交办件*******************/
													if(GovNetUtil.commitControl(controlSeq)) {
														distrInfo.setControlSeq(controlSeq);
														distrInfo.setTransactionStatus(new BigDecimal(1));
														if(distrInfo.update()) {
															LogUtil.log("提交办件成功");
															areaMsg = "提交办件成功";
															total_gov++;
														}
													}
												}else{
													LogUtil.log("更新办件人信息失败");
													areaMsg = "更新办件人信息失败";
												}
											}else{
												LogUtil.log("启动办件失败");
												areaMsg = "启动办件失败";
											}
										}
										//message ="配置成功";
										total++;
									}
								}else{
									//没有在本地则从审批接口读取获取
									userInfo = userIS.getUserByCDQ(itemUserCode, distrInfo.getDepartId()+"");
									if(userInfo != null){
										distrInfo.setTransactorId(userInfo.getUserId());
										distrInfo.setTransUserCode(itemUserCode);
										distrInfo.setTransactionStatus(new BigDecimal(0));//办理状态（0 首席已分发未开始、1 办理中、2 已逾期、3 办理完结、4 被退回 、5暂停、6首席未分发）
										if(distrInfo.update()){
											//生成办件
											if (distrInfo.getItemType().intValue() == 1) {
												//区级办件流程
												String controlSeq = "";
												gov_numb++;
												if (buss != null) {
													/**************启动办件**************************/
													String userCode = "m_" + buss.getContactPhone();
													//启动办件、生成办件流水号
													controlSeq = GovNetUtil.startItem(itemId, userCode);
												}
												if (!NullUtil.isEmpty(controlSeq)) {
													
													/************************更新办件申请人信息********************/
													boolean updateControlFlag = false;
													updateControlFlag = GovNetUtil.updateControlUser(controlSeq, buss.getBusinessName(), 
															buss.getContactName(), "", "", buss.getContactPhone(), "");
													
													if (updateControlFlag) {
														/**************提交办件*******************/
														if(GovNetUtil.commitControl(controlSeq)) {
															distrInfo.setControlSeq(controlSeq);
															distrInfo.setTransactionStatus(new BigDecimal(1));
															if(distrInfo.update()) {
																LogUtil.log("提交办件成功");
																areaMsg = "提交办件成功";
																total_gov++;
															}
														}
													}else{
														LogUtil.log("更新办件人信息失败");
														areaMsg = "更新办件人信息失败";
													}
												}else{
													LogUtil.log("启动办件失败");
													areaMsg = "启动办件失败";
												}
											}
											//message ="配置成功";
											total++;
										}
									}
								}
							}
	/**************************分配事项单个的跟办人end****************************/
					}
				}//事项for循环结束
					if(total==taskEvas.length()){
						success = true;
						message ="配置成功";
					}
					if(gov_numb >0){
						if(gov_numb==total_gov){
							areaMsg="启动办件成功";
						}else{
							areaMsg="启动办件失败";
						}
					}else{
						areaMsg="";//无启动办件则不反回任何信息
					}
			}
		}
		result.put("message", message);
		result.put("areaMsg", areaMsg);
		
		setAttr("success", success);
		setAttr("result", result);
		render(new JsonRender().forIE());
	}
	

	
	/**
	 * 添加市级单位
	 */
	public void addDepart() {
		String departName = this.getPara("departName");
		String workTel = this.getPara("workTel");
		String workAddress = this.getPara("workAddress");
		
		if (!NullUtil.isEmpty(departName)) {
			DeptInterService deptService = new DeptInterService();
			EbDepart deptInfo = deptService.getDeptByName(departName);
			if (deptInfo != null) {
				this.setAttr("success", false);
				this.setAttr("return", "");
				this.setAttr("errorMsg", "该单位名称已存在无法添加！");
				render(new JsonRender().forIE());
				return;
			}else {
				deptInfo = new EbDepart();
				Record rc = null;
				try {
					rc = Db.findFirst("select dept_seq.nextval depart_id from dual");
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (rc == null) {
					this.setAttr("success", false);
					this.setAttr("return", "");
					this.setAttr("errorMsg", "服务器异常！");
					render(new JsonRender().forIE());
					return;
				}
				SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHH");
				String data = df.format(new Date());
				String locDeptId = data + rc.get("DEPART_ID");
				deptInfo.setDepartId(new BigDecimal(locDeptId));
				
				if (!NullUtil.isEmpty(departName)) {
					deptInfo.setDepartName(departName);
				}
				
				if (!NullUtil.isEmpty(workTel)) {
					deptInfo.setWorkTel(workTel);
				}
				
				if (!NullUtil.isEmpty(workAddress)) {
					deptInfo.setWorkAddress(workAddress);
				}
				
				deptInfo.setDepartType(new BigDecimal(1));
				deptInfo.setRefreshTime(System.currentTimeMillis() + "");
				deptInfo.setUserCode("");
				deptInfo.setIsDelet(new BigDecimal(1));//是否被禁用1否
				
				boolean flag = false;
				try {
					flag = deptInfo.save();
				} catch (Exception e) {
					e.printStackTrace();
					flag = deptInfo.save();
				}
				
				if (flag) {
					this.setAttr("success", true);
					this.setAttr("errorMsg", "添加成功");
					Hashtable<String, String> result = new Hashtable<String, String>();
					result.put("departId", deptInfo.getDepartId() + "");
					result.put("departName", departName);
					result.put("workTel", getCheckedString(workTel));
					result.put("workAddress", getCheckedString(workAddress));
					result.put("departType", deptInfo.getDepartType() + "");
					this.setAttr("result", result);
					render(new JsonRender().forIE());
					return;
				}else {
					this.setAttr("success", false);
					this.setAttr("return", "");
					this.setAttr("errorMsg", "添加失败服务器异常！");
					render(new JsonRender().forIE());
					return;
				}
			}
		}else {
			this.setAttr("success", false);
			this.setAttr("return", "");
			this.setAttr("errorMsg", "单位名称不能为空！");
			render(new JsonRender().forIE());
			return;
		}
	}
}
