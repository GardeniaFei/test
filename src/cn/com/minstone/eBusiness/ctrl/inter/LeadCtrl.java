package cn.com.minstone.eBusiness.ctrl.inter;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import cn.com.minstone.eBusiness.MobileInter;
import cn.com.minstone.eBusiness.model.EbAttention;
import cn.com.minstone.eBusiness.model.EbBusiness;
import cn.com.minstone.eBusiness.model.EbDepart;
import cn.com.minstone.eBusiness.model.EbDeptEvainfo;
import cn.com.minstone.eBusiness.model.EbEvaluate;
import cn.com.minstone.eBusiness.model.EbItem;
import cn.com.minstone.eBusiness.model.EbMessage;
import cn.com.minstone.eBusiness.model.EbTask;
import cn.com.minstone.eBusiness.model.EbTaskDistribute;
import cn.com.minstone.eBusiness.model.EbUserInfo;
import cn.com.minstone.eBusiness.model.RoleType;
import cn.com.minstone.eBusiness.service.inter.BussInterService;
import cn.com.minstone.eBusiness.service.inter.DeptInterService;
import cn.com.minstone.eBusiness.service.inter.EvaInterService;
import cn.com.minstone.eBusiness.service.inter.ItemInterService;
import cn.com.minstone.eBusiness.service.inter.MsgInterService;
import cn.com.minstone.eBusiness.service.inter.TaskInterService;
import cn.com.minstone.eBusiness.service.inter.UserInterService;
import cn.com.minstone.eBusiness.util.Config;
import cn.com.minstone.eBusiness.util.GovNetUtil;
import cn.com.minstone.eBusiness.util.PushUtil;
import cn.com.minstone.eBusiness.util.StringUtil;
import cn.com.minstone.eBusiness.util.TimeUtil;
import cn.com.minstone.jfinal.ext.util.Logger;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.render.JsonRender;

@Before(MobileInter.class)
public class LeadCtrl extends Controller {
	
	Logger logger = new Logger(LeadCtrl.class);

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
	 * 12企业评价列表(办事员领导用，所有企业)
	 */
	public void evaluateList(){
		//拦截器判断用户身份是否为办事员
		boolean success = false;
		String message = "数据为空";
		
		String userId = Config.getSessionUserID(this);
		
		EbUserInfo userInfo = new UserInterService(this).getUserByUId(userId);
		
		int page = this.getParaToInt("page", 1);
		
		//获取所有的企业评价
		EvaInterService eService = new EvaInterService();
		Page<EbEvaluate> bussEvalListPage = eService.getAllEV(page);
		List<EbEvaluate> bussEvalList = bussEvalListPage.getList();
		
		if(bussEvalList != null && !bussEvalList.isEmpty()){
			success = true;
			message = "成功!";
			List<Hashtable<String, Object>> result = new ArrayList<Hashtable<String, Object>>();
			for(int i=0; i<bussEvalList.size(); i++){
				EbEvaluate eval = bussEvalList.get(i);
				Hashtable<String, Object> resultHash = new Hashtable<String, Object>();
				//企业Id
				resultHash.put("evaluateId", getCheckedString(eval.getEvaluateId() + ""));
				//企业Id
				resultHash.put("businessId", getCheckedString(eval.getBusinessId()+""));
				//企业名称
				resultHash.put("businessName", getCheckedString(eval.getBusinessName()+""));
				
				//企业落户时间
				BussInterService bussInter = new BussInterService();
				EbBusiness bussInfo = bussInter.getBussById(eval.getBusinessId()+"");
				resultHash.put("settleTime", TimeUtil.changeTimeStyle(bussInfo.getSettleTime()+""));
				
				//企业综合评价
				resultHash.put("compGrade", getCheckedString(eval.getCompGrade()+""));
				
				//企业对各部门的评价数组
				List<EbDeptEvainfo> deptEval = new ArrayList<EbDeptEvainfo>();
				if(userInfo.getRoleType().intValue() == RoleType.LEADER) {
					deptEval = eService.getDeptEvExmedList(eval.getEvaluateId()+"");
				} else {
					deptEval = eService.getDeptEvSourceList(eval.getEvaluateId()+"");
				}
				List<Hashtable<String, Object>> deptEvalResult = new ArrayList<Hashtable<String, Object>>();
				
				if(deptEval.size()==0){
					resultHash.put("departEvaluate","");
				}else{
					for(int j=0; j<deptEval.size(); j++){
						EbDeptEvainfo dEInfo = new EbDeptEvainfo();
						dEInfo = deptEval.get(j);
						
						Hashtable<String, Object> deptHash = new Hashtable<String, Object>();
						
						//部门Id
						deptHash.put("departId", getCheckedString(dEInfo.getDepartId()+""));
						//部门名称
						deptHash.put("departName", getCheckedString(dEInfo.getDepartName()+""));
						//部门评分
						deptHash.put("departGrade", getCheckedString(dEInfo.getGrade()+""));
						//评价时间
						deptHash.put("evaluateTime", TimeUtil.changeTimeStyle(dEInfo.getTime()+""));
						//评价内容
						deptHash.put("content", getCheckedString(dEInfo.getContent()+""));
						
						//部门办理的该企业的事项,根据部门Id和对应的企业id,查询任务管理和任务分发表
						List<String> items = new ArrayList<String>();
						
						//任务管理表根据企业Id获取任务Id
						TaskInterService taskSer = new TaskInterService();
						EbTask task = taskSer.getTaskByBussId(eval.getBusinessId()+"");
						if(task == null){
							deptHash.put("items", "");
						}else{
							//任务分发表根据任务Id和部门Id获取事项id
							List<EbTaskDistribute> taskDists = taskSer.getDistrsByDBIDList(task.getTaskId()+"", dEInfo.getDepartId()+"");
							if(taskDists.size()>0){
								for(int k=0; k<taskDists.size();k++){
									EbTaskDistribute taskDist = new EbTaskDistribute();
									taskDist = taskDists.get(k);
									
									//根据任务分发表的事项Id获取事项名称
									ItemInterService itemser = new ItemInterService();
									EbItem item = itemser.getItemById(taskDist.getItemId()+"");
									if(item == null) {
										item = GovNetUtil.getItemInfo(taskDist.getItemId()+"");
									}
									if(item!=null){
										items.add(item.getItemName());
									}
								}
							}
						}
						deptHash.put("items",items);
						deptEvalResult.add(deptHash);
					}
					resultHash.put("departEvaluate",deptEvalResult);
				}
				result.add(resultHash);
			}
			setAttr("result", result);
		}
		setAttr("current_page", bussEvalListPage.getPageNumber());
		setAttr("total_pages", bussEvalListPage.getTotalPage());
		setAttr("total_numb",bussEvalListPage.getTotalRow());
		setAttr("success", success);
		setAttr("message", message);
		render(new JsonRender().forIE());
	}
	
	/**
	 * 14我的批示（企业列表）领导用
	 */
	public void projectList(){
		String userId = Config.getSessionUserID(this);
		
//		int page = this.getParaToInt("page", 1);
		
		//根据当前页数，获取当前用户的所有留言
		MsgInterService messageSer = new MsgInterService();
//		Page<EbMessage> messListPage = messageSer.getMessageByUId(page, userId);
		List<EbMessage> messList = messageSer.getMessageByUIdLead(userId);
		
		boolean success = false;
		String message = "数据为空";
		
		if(messList != null && !messList.isEmpty()){
			success = true;
			message = "成功!";
			List<Hashtable<String, String>> result = new ArrayList<Hashtable<String, String>>();
			for(int i=0; i<messList.size(); i++){
				EbMessage messge = messList.get(i);
				
				Hashtable<String, String> resultHash = new Hashtable<String, String>();
				resultHash.put("businessId", getCheckedString(messge.getBusinessId()+""));
				
				String bussName = "";
				BussInterService bussSer = new BussInterService();
				EbBusiness buss = bussSer.findById(messge.getBusinessId()+"");
				if(buss!=null){
					bussName = buss.getBusinessName();
				}
				resultHash.put("businessName",getCheckedString(bussName));
				
				result.add(resultHash);
			}
			setAttr("result", result);
		}
		setAttr("success", success);
		setAttr("message", message);
		render(new JsonRender().forIE());
	}
	
	/**
	 * 15我的批示（批示列表）
	 * @throws ParseException 
	 */
	public void instructionList() throws ParseException{
		String instrTime = getPara("instrTime");
		String businessId = getPara("businessId");
		String userId = Config.getSessionUserID(this);
//		userId = "103";
 		int page = this.getParaToInt("page", 1);
		//判断筛选时间
		long start_time = 0;
		//当前时间毫秒数
		long end_time = new Date().getTime();
		if(instrTime.equals("week")){
			//本周周一时间毫秒数
			start_time = TimeUtil.getCurrentMondayTime();
		}else if(instrTime.equals("month")){
			Date nowDate = new Date();
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM");
			String date = df.format(nowDate)+"-01";
			start_time =  TimeUtil.getMinYearDateTime(date);
//			start_time = TimeUtil.getMinMonthDateTime();
		}else if(instrTime.equals("year")){
			Date nowDate = new Date();
			SimpleDateFormat df = new SimpleDateFormat("yyyy");
			String date = df.format(nowDate)+"-01-01";
			start_time = TimeUtil.getMinYearDateTime(date);
		}
		System.out.print("初始毫秒数：" + start_time + "; 当前日期毫秒数:"+end_time);
				
		MsgInterService messageSer = new MsgInterService();
		Page<EbMessage> messPage = messageSer.getMessageByUIdBId(page, userId, businessId,start_time,end_time);
		List<EbMessage> messList = messPage.getList();
				
		boolean success = false;
		String message = "数据为空";
				
		if(messList != null && !messList.isEmpty() ){
			success = true;
			message = "成功!";
			List<Hashtable<String, Object>> result = new ArrayList<Hashtable<String, Object>>();
			for(int i=0; i<messList.size(); i++){
				EbMessage messge = messList.get(i);
						
				Hashtable<String, Object> resultHash = new Hashtable<String, Object>();
				//领导用户id
				resultHash.put("userId", getCheckedString(messge.getUserId()+""));
				//领导用户名称
				resultHash.put("userName", getCheckedString(messge.getUserName()));
				//批示时间
				resultHash.put("instrTime", TimeUtil.changeTimeStyle(messge.getMessageTime()));
				
				//项目名称
				BussInterService bussSer = new BussInterService();
				EbBusiness buss = bussSer.findById(messge.getBusinessId()+"");
				String projectName = "";
				if(buss!=null){
					projectName = buss.getProjectName();
					if(projectName == null){
						projectName = buss.getBusinessName()+"项目";
					}else if("".equals(projectName)){
						projectName = buss.getBusinessName()+"项目";
					}
				}
				
				resultHash.put("projectName",getCheckedString(projectName));
				//批示内容
				resultHash.put("instrContent", getCheckedString(messge.getMessageContent()));
				
				//批示回复列表replyList
				//获取留言id号
				String messgeId = messge.getMessageId()+"";
				resultHash.put("messageId", getCheckedString(messgeId));
				//获取企业id号
				String bussId = messge.getBusinessId()+"";
				resultHash.put("businessId",getCheckedString(bussId));
				//根据留言Id号和企业id号找到对应的回复列表
				MsgInterService replySer = new MsgInterService();
				List<EbMessage> replyList = replySer.getReplyByMIdBIdList(messgeId, bussId);
				
				if(replyList != null && !replyList.isEmpty()){
					List<Hashtable<String, String>> replyResult = new ArrayList<Hashtable<String, String>>();
					for(int j=0; j<replyList.size(); j++){
						EbMessage reply = replyList.get(j);
								
						Hashtable<String, String> replyHash = new Hashtable<String, String>();
						//回复Id
						replyHash.put("paramId", getCheckedString(reply.getMessageId()+""));
						//回复内容
						replyHash.put("replyContent", getCheckedString(reply.getMessageContent()));
						//回复时间
						replyHash.put("replayTime", TimeUtil.changeTimeStyle(reply.getMessageTime()));
						//回复用户Id
						replyHash.put("replyUserId", getCheckedString(reply.getUserId()+""));
						//回复用户名
						//先找到用户所在部门名称
						DeptInterService depSer = new DeptInterService();
						EbDepart dept = depSer.getDeptById(reply.getDepartId()+"");
						if(dept == null) {
							dept = GovNetUtil.getDeptInfo(reply.getDepartId()+"");
						}
						if(dept==null){
							replyHash.put("replyUserName",getCheckedString(reply.getUserName()));
						}else{
							replyHash.put("replyUserName", dept.getDepartName()+getCheckedString(reply.getUserName()));
						}
						replyResult.add(replyHash);
					}
					resultHash.put("replyList",replyResult);
				}else{
					resultHash.put("replyList","");
				}
				result.add(resultHash);
			}
			setAttr("result", result);
		}
		setAttr("current_page", messPage.getPageNumber());
		setAttr("total_pages", messPage.getTotalPage());
		setAttr("total_numb",messPage.getTotalRow());
		setAttr("success", success);
		setAttr("message", message);
		render(new JsonRender().forIE());
	}
	
	/**
	 * 23.督办（领导保存留言信息到留言表）
	 */
	public void superviseHandle(){
		String businessId = getPara("businessId");//企业名称
		String itemId = getPara("itemId");//留言的具体事项
		String content = getPara("content");//得到的留言内容
		String distrId = this.getPara("distrId");
		String userId = Config.getSessionUserID(this);//登录的领导用户
		
		MsgInterService msgService = new MsgInterService();
		BussInterService busSer = new BussInterService();
		UserInterService userSer = new UserInterService(this);
		TaskInterService taskSer = new TaskInterService();
		
		boolean success = false;
		String message = "督办不成功，请重试！";
		
		if(businessId == null || businessId.trim().equals("") || "null".equalsIgnoreCase(businessId)) {
			message ="企业id不能为空，留言失败！";
		}else {
			if(itemId == null || itemId.trim().equals("") || "null".equalsIgnoreCase(itemId)) {
				message ="事项Id不能为空，留言失败！";
			}else {
				if(content == null || content.trim().equals("") || "null".equalsIgnoreCase(content)) {
					message ="留言内容不能为空，留言失败！";
				}else {
					EbUserInfo user = userSer.findById(userId);
					boolean flag = false;
					if(user!=null){
						String userName = user.getUserName();
						EbBusiness buss = busSer.getBussById(businessId);
						if(buss!=null){
							//这个项目的这个事项的办理部门
							String itemDeptId = "";
							EbTask task = taskSer.getTaskByBussId(buss.getBusinessId()+"");
							if(task!=null){
								String taskId = task.getTaskId()+"";
								EbTaskDistribute taskD = taskSer.getDistrByTIId(taskId, itemId, distrId);
								if(taskD!=null){
									itemDeptId = taskD.getDepartId()+"";
									String businessName = buss.getBusinessName();
									flag = msgService.addMessageToItem(businessId, content, userId, itemDeptId, 
											itemId,businessName,userName);
								}else{
									message ="该事项不存在，督办失败！";
								}

								if(flag) {
									success = true;
									message ="督办成功";
									EbUserInfo userInfo = userSer.findById(userId);
									EbBusiness bussInfo = busSer.findById(businessId);
									
									//TODO 消息推送
									try {
										String newsTime = System.currentTimeMillis() + "";
										String newsContent = taskD.getItemName();
										if(userInfo.getRoleType().intValue() == 1) {
											newsContent = "政务办" + userInfo.getUserName() + "督办了" + bussInfo.getBusinessName() + 
													"（企业）所需办理的" + taskD.getItemName() + "（事项），督办内容：" + content;
										}else if (userInfo.getRoleType().intValue() == 2) {
											newsContent = userInfo.getUserName() + "领导督办了" + bussInfo.getBusinessName() + 
													"（企业）所需办理的" + taskD.getItemName() + "（事项），督办内容：" + content;
										}
										boolean flg = msgService.addNews(userInfo, bussInfo, "9", newsContent, 
												content, newsTime, taskD.getItemId() + "", taskD.getItemName(), "9", 
												taskD.getDepartId() + "", taskD.getDepartName());
										
										if(flg) {
											if(userInfo.getRoleType().intValue() == 1) {
												newsContent = "政务办" + userInfo.getUserName() + "督办了" + bussInfo.getBusinessName() + 
														"（企业）所需办理的" + taskD.getItemName() + "（事项），督办内容：" + content;
											}else if (userInfo.getRoleType().intValue() == 2) {
												newsContent = userInfo.getUserName() + "领导督办了" + bussInfo.getBusinessName() + 
														"（企业）所需办理的" + taskD.getItemName() + "（事项），督办内容：" + content;
											}
											List<String> strAlias = new ArrayList<String>();
											if(taskD.getDepartName().contains("政务办")) {
												strAlias.add(Config.ZWB);
											}else if (taskD.getDepartName().contains("招商局")) {
												strAlias.add(Config.ZSJ);
											}else {
												strAlias.add(taskD.getDepartId() + "");
											}
											
											PushUtil.pushNotifyToTagert(strAlias, StringUtil.getJson("9", newsContent));
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
				}
			}
		}
		Hashtable<String, String> result= new Hashtable<String, String>();
		result.put("message", message);
		
		this.setAttr("result", result);
		setAttr("success", success);
		render(new JsonRender().forIE());
	}
	
	/**
	 * 35.取消企业关注（领导）
	 */
	public void cancle_attent_business(){
		String businessId = getPara("businessId");//企业名称
		String userId = Config.getSessionUserID(this);//登录的领导用户
		boolean success = false;
		String message = "取消不成功，请重试！";
		
		BussInterService attenSer = new BussInterService(); 
		EbAttention atten = attenSer.getAttenByUBid(userId, businessId);
		if(atten!=null){
			atten.set("is_delet",Integer.parseInt("0"));
			if(atten.update()){
				message = "取消关注成功";
				success = true;
			}
		}
		Hashtable<String, String> result= new Hashtable<String, String>();
		result.put("message", message);
		
		this.setAttr("result", result);
		setAttr("success", success);
		render(new JsonRender().forIE());
	}
	
	/**
	 * 36.增加企业关注（领导）
	 */
	public void attent_business(){
		String businessId = getPara("businessId");//企业名称
		String userId = Config.getSessionUserID(this);//登录的领导用户
		boolean success = false;
		String message = "关注不成功，请重试！";
		
		BussInterService attenSer = new BussInterService();
		EbAttention atten = attenSer.getAttenByUBID(userId, businessId);
		if(atten != null){
			if (atten.getIsDelet().intValue() == 1) {
				message = "该企业已被您关注，无需再关注！";
			}else if (atten.getIsDelet().intValue() == 0) {
				atten.setIsDelet(new BigDecimal("1"));
				
				success = atten.update();
				message = "关注成功";
			}
		}else{//领导关注企业中没有该企业，添加一条新的数据
			if(businessId != null && userId != null){
				 
				boolean flag = attenSer.addAtten(userId, businessId);
				if(flag){
					message = "关注成功";
					success = true;
				}else {
					message = "数据插入失败,关注失败";
				}
			}else{
				message = "参数不完整或领导未登录，请重试！";
			}
		}
		
		Hashtable<String, String> result= new Hashtable<String, String>();
		result.put("message", message);
		
		this.setAttr("result", result);
		setAttr("success", success);
		render(new JsonRender().forIE());
	}
}
