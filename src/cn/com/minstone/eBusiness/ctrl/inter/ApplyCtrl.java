package cn.com.minstone.eBusiness.ctrl.inter;

import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import org.apache.commons.httpclient.util.DateUtil;

import cn.com.minstone.eBusiness.model.EbAttention;
import cn.com.minstone.eBusiness.model.EbBusiness;
import cn.com.minstone.eBusiness.model.EbBusinessType;
import cn.com.minstone.eBusiness.model.EbEvaluate;
import cn.com.minstone.eBusiness.model.EbItem;
import cn.com.minstone.eBusiness.model.EbTask;
import cn.com.minstone.eBusiness.model.EbTaskDistribute;
import cn.com.minstone.eBusiness.model.EbUserInfo;
import cn.com.minstone.eBusiness.service.inter.BussInterService;
import cn.com.minstone.eBusiness.service.inter.EvaInterService;
import cn.com.minstone.eBusiness.service.inter.ItemInterService;
import cn.com.minstone.eBusiness.service.inter.TaskInterService;
import cn.com.minstone.eBusiness.service.inter.UserInterService;
import cn.com.minstone.eBusiness.util.Config;
import cn.com.minstone.eBusiness.util.GovNetUtil;
import cn.com.minstone.eBusiness.util.MCubeAppConfig;
import cn.com.minstone.eBusiness.util.MD5Util;
import cn.com.minstone.eBusiness.util.TimeUtil;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.render.JsonRender;

public class ApplyCtrl extends Controller{

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
	
	private void faultResult(String error) {
		this.setAttr("success", false);
		this.setAttr("result", null);
		this.setAttr("errorMsg", error);
	}
	
	/**
	 * 11事项列表
	 */
	public void itemList(){
		int page = this.getParaToInt("page", 1);
		String userId = Config.getSessionUserID(this);
		String status = getPara("status");//筛选条件 0 关注企业、1 办理中、2 超时、3 办理完结、
		
		if(userId == null || "".equals(userId)){
			faultResult("请登录！");
		}else{
			if(status == null || "".equals(status)){
				faultResult("缺少参数！");
			}else{
				//判断用户身份
				String roleType = "";
				String userdept = "";
				String authority = "";
				UserInterService userSer = new UserInterService(this);
				EbUserInfo user = userSer.findById(userId);
				if(user != null){
					//用户角色（1区级办事人员、2领导、3企业、4市级办事员、5区级政务办）
					roleType = user.getRoleType() + "";
					userdept = user.getDepartId() + "";//获取用户部门
					//权限标识（1VIP服务专员管理员、2VIP服务专员权限 、3单位首席权限、4部门跟办人权限
					// 、5市级服务专员权限、6 VIP服务专员管理员+单位首席权限、7 VIP服务专员+单位首席权限）（办事员才有权限）
					authority = user.getAuthority() + "";
					if(authority.equals("6") ||authority.equals("7") ){
						String auth = Config.getSessionSelect_auth(this);
						authority = auth;
					}
				}
				System.out.print("测试当前登录用户类型为：" + roleType);
				
				if (status.equals("1") && roleType.equals("1")) {
					//办理状态：正在办理、区级办事员
					if (authority.equals("3")) {
						//区级单位首席
						//列表展示属于本部门所需办理的事项列表
						listItemByDepart(userdept, status, page);
					}else if (authority.equals("4")) {
						//部门跟办人
						listItemByTrId(userId, status, page);
					}
				}
				
				if (status.equals("1") && roleType.equals("5")) {
					//办理状态：正在办理、区级政务办
					if (authority.equals("2")) {
						//角色：区政务办的VIP服务专员
						//列表展示未签收企业和已签收企业待分发任务的事项列表（即企业列表）
						listSignStatusBuss(user.getUserAccount(), page);
					}else if(authority.equals("1")){
						//正在办理的所有企业列表
						//角色：Vip服务专员管理员
						allBuss(page);
					}
				}
				
				if (status.equals("1") && roleType.equals("4")) {
					//办理状态：正在办理、市级办事员
					if (authority.equals("5")) {
						//市级服务专员
						listItemByCtUId(userId, status, page,userdept);
					}else if (authority.equals("3")) {
						//市级单位首席
						//列表展示属于本部门所需办理的事项列表
						listItemByDepart(userdept, status, page);
					}else if (authority.equals("4")) {
						//部门跟办人
						listItemByTrId(userId, status, page);
					}
				}
				
				/*************领导用户*****************/
				if (status.equals("0") && roleType.equals("2")) {
					//关注企业列表、领导用户
					attentBuss(userId, page);
				}else if(status.equals("1") && roleType.equals("2")){
					//领导查看正在办理中的事项列表
					leardApply(userId, page, status, this);
				}
//				else if(status.equals("2")&& roleType.equals("2")){
//					workBuss(userId, page, status,roleType,authority,userdept);
//				}
				if(status.equals("2")){
					workBuss(userId, page, status,roleType,authority,userdept);
				}
//				if(status.equals("0") && roleforUser.equals("2")){
//					//关注企业列表
//					attentBuss(userId, page);
//				}else if(status.equals("1") && roleforUser.equals("1")){
//					//办理中、正在办理事项列表
//					if(authority.equals("3")){
//						//其他部门办事员获取的事项列表
//						workApply3(userId,userdept,authority, page, status,this);
//					}else{
//						//招商局或政务办办事员
//						workApply(userId, userdept, authority, page, status, this);
//					}
//				}else 
//					else if(status.equals("2") || status.equals("3")){
//					//超时或办理完结的事项列表
//					workBuss(userId, page, status,roleforUser,authority,userdept);
//				}else{
//					faultResult("参数传递错误！");
//				}
			}
		}
		render(new JsonRender().forIE());
	}
	
	/**
	 * 市级服务专员关联权限
	 * @param userId
	 * @param status
	 * @param userdept
	 * @param pageIndex
	 */
	public void listItemByCtUId(String userId, String status, int pageIndex,String userdept) {
		boolean success = false;
		String message = "数据为空";
		
		TaskInterService taskService = new TaskInterService();
		BussInterService bService = new BussInterService();
		EvaInterService evaSer = new EvaInterService();
		Page<EbTaskDistribute> pageDistrs = taskService.getDistrByCtUIdAndStatus("6", pageIndex, userId,userdept);
		List<EbTaskDistribute> distrList = new ArrayList<EbTaskDistribute>();
		if(pageDistrs != null) {
			success = true;
			message = "成功";
			distrList = pageDistrs.getList();
			this.setAttr("total_pages", pageDistrs.getTotalPage());
			this.setAttr("total_numb", pageDistrs.getTotalRow());
		}else {
			this.setAttr("total_pages", 0);
			this.setAttr("total_numb", 0);
		}
		
		if(distrList.size() > 0) {
			List<Hashtable<String, String>> result = new ArrayList<Hashtable<String, String>>();
			for(int i=0; i < distrList.size(); i++){
				EbTaskDistribute distri = distrList.get(i);
				Hashtable<String, String> resultHash = new Hashtable<String, String>();
				
				String taskId = distri.getTaskId() + "";
				EbTask task = taskService.getTaskById(taskId);
				if(task !=null ){
					resultHash.put("businessId", getCheckedString(task.getBusinessId() + ""));//企业id
					resultHash.put("businessName", getCheckedString(task.getBusinessName()));//企业名称
					
					resultHash.put("isAttention", "");//非领导用户
					
					//企业落户状态
					resultHash.put("settleStatus", getCheckedString(task.getCompStatus() + ""));
					resultHash.put("settleTime", TimeUtil.changeTimeStyle(task.getCompTime()));//落户时间、完成时间
					//企业签收状态 0 未签收、1 已签收
					resultHash.put("signStatus", getCheckedString(task.getSignStatus() + ""));
					
					//获取到对应的企业判断事项任务分发状态
					EbBusiness buss = bService.getBussById(task.getBusinessId() + "");
					if(buss != null){
						resultHash.put("distributeStatus", getCheckedString(buss.getDistributedStatus()+""));//任务分发状态 0 未分发、1 已分发
					}else{
						resultHash.put("distributeStatus", "");//任务分发状态 0 未分发、1 已分发
					}
					
					//如果任务未分发，则不显示事项（不过单位首席看到的必须是事项已分发的状态）
					resultHash.put("itemId", getCheckedString(distri.getItemId() + ""));//事项id
					resultHash.put("distrId", getCheckedString(distri.getDistrId() + ""));//任务分发id
					resultHash.put("departId", getCheckedString(distri.getDepartId() + ""));//事项所属部门id
					
//					String itemName = "";//事项名称
					int time_limit = 15;//事项办理期限（多少个工作日）
					ItemInterService itemSer = new ItemInterService();
					EbItem item = itemSer.getItemById(distri.getItemId()+"");
					if (item == null) {
						item = GovNetUtil.getItemInfo(distri.getItemId() + "");
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
					
					String compGrade = "";//企业综合评价
					EbEvaluate eva = evaSer.getEVsByBussId(task.getBusinessId()+"");
					if(eva!=null){
						compGrade = eva.getCompGrade()+"";
					}
					
					resultHash.put("itemName", distri.getItemName());//事项名称
					resultHash.put("departName", getCheckedString(distri.getDepartName()));
					resultHash.put("itemSignStatus", distri.getSignStatus() + "");//事项签收状态
					resultHash.put("transactionStatus", distri.getTransactionStatus()+"");//办理状态 0 未开始、1 办理中、2 已逾期、3 办理完结、4 被退回
					resultHash.put("compGrade", compGrade);//企业综合评价
					
					//办理中的事项如果已办理的话
					int signStatus = distri.getSignStatus().intValue();//签收状态（0 未签收、1 已签收、2 被退回：事项任务签收时有该状态）
					int tranStatus = distri.getTransactionStatus().intValue();
					//办理状态（0 首席已分发未开始、1 办理中、2 已逾期、3 办理完结、4 被退回 、5暂停、6首席未分发）
					if(signStatus == 1 && tranStatus == 1){
						long sign_time = Long.parseLong(distri.getSignTime());//事项签收时间
						long nowTime = new Date().getTime();
						long days = TimeUtil.getWorkDaysBylong(sign_time, nowTime);//从签收到现在的天数
						long deday = days/24/3600/1000;
						//（2已逾期、3 已完结）
						if(status.equals("2")){
							int overTime = (int)deday - time_limit;
							resultHash.put("overTime", overTime+"");//超时时间(单位/天)
							resultHash.put("remainTime", "");//剩余时间（单位/天)
						}else if(status.equals("3")){
							resultHash.put("overTime", "");//超时时间(单位/天)
							resultHash.put("remainTime", "");//剩余时间（单位/天)
						}
					}
					result.add(resultHash);
				}else {
					success = false;
					message = "查询出错";
				}
			}
			setAttr("result", result);
		}
		
		this.setAttr("current_page", pageIndex);
		this.setAttr("success", success);
		this.setAttr("message", message);
	}
	
	/**
	 * 跟办人关联事项列表
	 * @param userId
	 * @param status
	 * @param pageIndex
	 */
	public void listItemByTrId(String userId, String status, int pageIndex) {
		boolean success = false;
		String message = "数据为空";
		
		TaskInterService taskService = new TaskInterService();
		BussInterService bService = new BussInterService();
		EvaInterService evaSer = new EvaInterService();
		Page<EbTaskDistribute> pageDistrs = taskService.getDistrByTranIdAndStatus(status, pageIndex, userId);
		List<EbTaskDistribute> distrList = new ArrayList<EbTaskDistribute>();
		if(pageDistrs != null) {
			success = true;
			message = "成功";
			distrList = pageDistrs.getList();
			this.setAttr("total_pages", pageDistrs.getTotalPage());
			this.setAttr("total_numb", pageDistrs.getTotalRow());
		}else {
			this.setAttr("total_pages", 0);
			this.setAttr("total_numb", 0);
		}
		
		if(distrList.size() > 0) {
			List<Hashtable<String, String>> result = new ArrayList<Hashtable<String, String>>();
			for(int i=0; i < distrList.size(); i++){
				EbTaskDistribute distri = distrList.get(i);
				Hashtable<String, String> resultHash = new Hashtable<String, String>();
				
				String taskId = distri.getTaskId() + "";
				EbTask task = taskService.getTaskById(taskId);
				if(task !=null ){
					resultHash.put("businessId", getCheckedString(task.getBusinessId() + ""));//企业id
					resultHash.put("businessName", getCheckedString(task.getBusinessName()));//企业名称
					
					resultHash.put("isAttention", "");//非领导用户
					
					//企业落户状态
					resultHash.put("settleStatus", getCheckedString(task.getCompStatus() + ""));
					resultHash.put("settleTime", TimeUtil.changeTimeStyle(task.getCompTime()));//落户时间、完成时间
					//企业签收状态 0 未签收、1 已签收
					resultHash.put("signStatus", getCheckedString(task.getSignStatus() + ""));
					
					//获取到对应的企业判断事项任务分发状态
					EbBusiness buss = bService.getBussById(task.getBusinessId() + "");
					if(buss != null){
						resultHash.put("distributeStatus", getCheckedString(buss.getDistributedStatus()+""));//任务分发状态 0 未分发、1 已分发
					}else{
						resultHash.put("distributeStatus", "");//任务分发状态 0 未分发、1 已分发
					}
					
					//如果任务未分发，则不显示事项（不过单位首席看到的必须是事项已分发的状态）
					resultHash.put("itemId", getCheckedString(distri.getItemId() + ""));//事项id
					resultHash.put("departId", getCheckedString(distri.getDepartId() + ""));//事项所属部门id
					
//					String itemName = "";//事项名称
					int time_limit = 15;//事项办理期限（多少个工作日）
					ItemInterService itemSer = new ItemInterService();
					EbItem item = itemSer.getItemById(distri.getItemId()+"");
					if (item == null) {
						item = GovNetUtil.getItemInfo(distri.getItemId() + "");
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
					
					String compGrade = "";//企业综合评价
					EbEvaluate eva = evaSer.getEVsByBussId(task.getBusinessId()+"");
					if(eva!=null){
						compGrade = eva.getCompGrade()+"";
					}
					long sign_time = Long.parseLong(distri.getSignTime());//事项签收时间
					long nowTime = new Date().getTime();
					long days = TimeUtil.getWorkDaysBylong(sign_time, nowTime);//从签收到现在的天数
					long deday = days/24/3600/1000;
					
					resultHash.put("itemName", distri.getItemName());//事项名称
					resultHash.put("departName", getCheckedString(distri.getDepartName()));
					resultHash.put("itemSignStatus", distri.getSignStatus() + "");//事项签收状态
					resultHash.put("transactionStatus", distri.getTransactionStatus()+"");//办理状态 0 未开始、1 办理中、2 已逾期、3 办理完结、4 被退回
					resultHash.put("compGrade", compGrade);//企业综合评价
					
					//（2已逾期、3 已完结）
					if(status.equals("2")){
						int overTime = (int)deday - time_limit;
						resultHash.put("overTime", overTime+"");//超时时间(单位/天)
						resultHash.put("remainTime", "");//剩余时间（单位/天)
					}else if(status.equals("3")){
						resultHash.put("overTime", "");//超时时间(单位/天)
						resultHash.put("remainTime", "");//剩余时间（单位/天)
					}
					result.add(resultHash);
				}else {
					success = false;
					message = "查询出错";
				}
			}
			setAttr("result", result);
		}
		
		this.setAttr("current_page", pageIndex);
		this.setAttr("success", success);
		this.setAttr("message", message);
	}
	
	/**
	 * 获取单位首席的事项列表
	 * @param departId
	 * @param status
	 * @param pageIndex
	 */
	public void listItemByDepart(String departId, String status, int pageIndex) {
		boolean success = false;
		String message = "数据为空";
		
		TaskInterService taskService = new TaskInterService();
		BussInterService bService = new BussInterService();
		EvaInterService evaSer = new EvaInterService();
		Page<EbTaskDistribute> pageDistrs = taskService.getDistrByDeptIdAndStatus(6, pageIndex, departId);
		List<EbTaskDistribute> distrList = new ArrayList<EbTaskDistribute>();
		if(pageDistrs != null) {
			success = true;
			message = "成功";
			distrList = pageDistrs.getList();
			this.setAttr("total_pages", pageDistrs.getTotalPage());
			this.setAttr("total_numb", pageDistrs.getTotalRow());
		}else {
			this.setAttr("total_pages", 0);
			this.setAttr("total_numb", 0);
		}
		
		if(distrList.size() > 0) {
			List<Hashtable<String, String>> result = new ArrayList<Hashtable<String, String>>();
			for(int i=0; i < distrList.size(); i++){
				EbTaskDistribute distri = distrList.get(i);
				Hashtable<String, String> resultHash = new Hashtable<String, String>();
				
				String taskId = distri.getTaskId() + "";
				EbTask task = taskService.getTaskById(taskId);
				if(task !=null ){
					resultHash.put("businessId", getCheckedString(task.getBusinessId() + ""));//企业id
					resultHash.put("businessName", getCheckedString(task.getBusinessName()));//企业名称
					
					resultHash.put("isAttention", "");//非领导用户
					
					//企业落户状态
					resultHash.put("settleStatus", getCheckedString(task.getCompStatus() + ""));
					resultHash.put("settleTime", TimeUtil.changeTimeStyle(task.getCompTime()));//落户时间、完成时间
					//企业签收状态 0 未签收、1 已签收
					resultHash.put("signStatus", getCheckedString(task.getSignStatus() + ""));
					
					//获取到对应的企业判断事项任务分发状态
					EbBusiness buss = bService.getBussById(task.getBusinessId() + "");
					if(buss != null){
						resultHash.put("distributeStatus", getCheckedString(buss.getDistributedStatus()+""));//任务分发状态 0 未分发、1 已分发
					}else{
						resultHash.put("distributeStatus", "");//任务分发状态 0 未分发、1 已分发
					}
					
					//如果任务未分发，则不显示事项（不过单位首席看到的必须是事项已分发的状态）
					resultHash.put("itemId", getCheckedString(distri.getItemId() + ""));//事项id
					resultHash.put("departId", getCheckedString(distri.getDepartId() + ""));//事项所属部门id
					
//					String itemName = "";//事项名称
					int time_limit = 15;//事项办理期限（多少个工作日）
					ItemInterService itemSer = new ItemInterService();
					EbItem item = itemSer.getItemById(distri.getItemId()+"");
					if (item == null) {
						item = GovNetUtil.getItemInfo(distri.getItemId() + "");
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
					
					String compGrade = "";//企业综合评价
					EbEvaluate eva = evaSer.getEVsByBussId(task.getBusinessId()+"");
					if(eva!=null){
						compGrade = eva.getCompGrade()+"";
					}
					
					resultHash.put("itemName", distri.getItemName());//事项名称
					resultHash.put("departName", getCheckedString(distri.getDepartName()));
					resultHash.put("itemSignStatus", distri.getSignStatus() + "");//事项签收状态
					resultHash.put("transactionStatus", distri.getTransactionStatus()+"");
					resultHash.put("compGrade", compGrade);//企业综合评价
					
					//办理中的事项如果已办理的话
					int signStatus = distri.getSignStatus().intValue();//签收状态（0 未签收、1 已签收、2 被退回：事项任务签收时有该状态）
					int tranStatus = distri.getTransactionStatus().intValue();
					//办理状态（0 首席已分发未开始、1 办理中、2 已逾期、3 办理完结、4 被退回 、5暂停、6首席未分发）
					if(signStatus == 1 && tranStatus == 1){
						long sign_time = Long.parseLong(distri.getSignTime());//事项签收时间
						long nowTime = new Date().getTime();
						long days = TimeUtil.getWorkDaysBylong(sign_time, nowTime);//从签收到现在的天数
						long deday = days/24/3600/1000;
						//（2已逾期、3 已完结）
						if(status.equals("2")){
							int overTime = (int)deday - time_limit;
							resultHash.put("overTime", overTime+"");//超时时间(单位/天)
							resultHash.put("remainTime", "");//剩余时间（单位/天)
						}else if(status.equals("3")){
							resultHash.put("overTime", "");//超时时间(单位/天)
							resultHash.put("remainTime", "");//剩余时间（单位/天)
						}
					}
					result.add(resultHash);
				}else {
					success = false;
					message = "查询出错";
				}
			}
			setAttr("result", result);
		}
		
		this.setAttr("current_page", pageIndex);
		this.setAttr("success", success);
		this.setAttr("message", message);
		
	}
	
	/**
	 * 列举VIP服务专员用户需要办理的事项，即签收企业和分发任务的企业列表
	 * @param userCode
	 * @param userDept
	 * @param authority
	 * @param pageIndex
	 * @param status
	 * @param ctrl
	 */
	public void listSignStatusBuss(String userCode, int pageIndex) {
		boolean success = false;
		String message = "数据为空";
		
		BussInterService bussService = new BussInterService();
		Page<EbBusiness> pageBuss = bussService.filterBsInfosByVipCode(userCode, pageIndex);
		List<EbBusiness> bussList = new ArrayList<EbBusiness>();
		if(pageBuss != null) {
			success = true;
			message = "成功";
			bussList = pageBuss.getList();
			this.setAttr("total_pages", pageBuss.getTotalPage());
			this.setAttr("total_numb", pageBuss.getTotalRow());
		}else {
			this.setAttr("total_pages", 0);
			this.setAttr("total_numb", 0);
		}
		List<Hashtable<String, String>> result = new ArrayList<Hashtable<String, String>>();
		if(bussList.size() > 0) {
			for (int i = 0; i < bussList.size(); i++) {
				Hashtable<String, String> resultHash = new Hashtable<String, String>();
				EbBusiness bussInfo = bussList.get(i);
				if (bussInfo != null) {
					resultHash.put("businessId", bussInfo.getBusinessId() + "");
					resultHash.put("businessName", bussInfo.getBusinessName());
					resultHash.put("addTime", TimeUtil.getTimeStyle(bussInfo.getAddTime(), "yyyy-MM-dd HH:mm"));
					resultHash.put("isAttention", "");
					resultHash.put("settleStatus", bussInfo.getSettleStatus() + "");
					resultHash.put("settleTime", getCheckedString(bussInfo.getSettleTime()));
					resultHash.put("signStatus", bussInfo.getSignStatus() + "");
					resultHash.put("distributeStatus", bussInfo.getDistributedStatus() + "");
					result.add(resultHash);
				}
			}
			this.setAttr("result", result);
		}
		
		this.setAttr("current_page", pageBuss.getPageNumber());
		this.setAttr("success", success);
		this.setAttr("message", message);
	}
	
	/**
	 * vip服务专员管理员看到所有企业的状态列表
	 * @param page
	 */
	public void allBuss(int page){
		boolean success = false;
		String message = "数据为空";
		
		BussInterService bussService = new BussInterService();
		Page<EbBusiness> pageBuss = bussService.findBsInfosByVIP(page);
		List<EbBusiness> bussList = new ArrayList<EbBusiness>();
		if(pageBuss != null) {
			success = true;
			message = "成功";
			bussList = pageBuss.getList();
			this.setAttr("total_pages", pageBuss.getTotalPage());
			this.setAttr("total_numb", pageBuss.getTotalRow());
		}else {
			this.setAttr("total_pages", 0);
			this.setAttr("total_numb", 0);
		}
		List<Hashtable<String, String>> result = new ArrayList<Hashtable<String, String>>();
		if(bussList.size() > 0) {
			for (int i = 0; i < bussList.size(); i++) {
				Hashtable<String, String> resultHash = new Hashtable<String, String>();
				EbBusiness bussInfo = bussList.get(i);
				if (bussInfo != null) {
					resultHash.put("businessId", bussInfo.getBusinessId() + "");
					resultHash.put("businessName", bussInfo.getBusinessName());
					resultHash.put("addTime", TimeUtil.getTimeStyle(bussInfo.getAddTime(), "yyyy-MM-dd HH:mm"));
					resultHash.put("isAttention", "");
					resultHash.put("settleStatus", bussInfo.getSettleStatus() + "");
					resultHash.put("settleTime", getCheckedString(bussInfo.getSettleTime()));
					resultHash.put("signStatus", bussInfo.getSignStatus() + "");
					resultHash.put("distributeStatus", bussInfo.getDistributedStatus() + "");
					resultHash.put("service_admin", bussInfo.getService_admin() + "");//多传一个vip服务跟班人code
					result.add(resultHash);
				}
			}
			this.setAttr("result", result);
		}
		
		
		this.setAttr("current_page", pageBuss.getPageNumber());
		this.setAttr("success", success);
		this.setAttr("message", message);
	}
	
	/**
	 * 11事项列表，关注企业 status=0 领导用户
	 * @param userId 当前登录用户
	 * @param page 显示页数
	 */
	public void attentBuss(String userId,int page){
		boolean success = false;
		String message = "数据为空";
//		TaskInterService taskSer = new TaskInterService();
		BussInterService attenSer = new BussInterService(); 
		//筛选领导关注企业列表
		Page<EbAttention> attentPage = attenSer.getAllAttentByUserId(userId, page);
		List<EbAttention> attentlist = attentPage.getList();
		
		if(attentlist.size()>0){
			List<Hashtable<String, String>> result = new ArrayList<Hashtable<String, String>>();
			for(int i=0;i<attentlist.size();i++){
				EbAttention atten = attentlist.get(i);
				Hashtable<String, String> table = new Hashtable<String, String>();
				
				String businessId = atten.getBusinessId()+"";//关注企业id
				//设置企业类型id，根据企业id获取企业信息
				EbBusiness bussInfo = attenSer.findById(businessId + "");
				EbBusinessType bussType = null;
				String addTime = "";
				String businessName = "";
				String isAttention = atten.getIsDelet() + "";//是否被关注 0 未关注、1已关注
				if(bussInfo != null) {
					businessName = bussInfo.getBusinessName();//企业名称
					addTime = TimeUtil.getTimeStyle(bussInfo.getAddTime(), "yyyy-MM-dd HH:mm");
					bussType = attenSer.getBsTypeById(bussInfo.getTypeId() + "");
					
					table.put("businessId", getCheckedString(businessId));
					table.put("businessName", getCheckedString(businessName));
					table.put("addTime", addTime);
					table.put("typeId", getCheckedString(bussInfo.getTypeId() + ""));
					if(bussType != null) {
						table.put("typeName", getCheckedString(bussType.getTypeName()));
					}else {
						table.put("typeName", "");
					}
					table.put("isAttention", isAttention);
					table.put("settleStatus", getCheckedString(bussInfo.getSettleStatus() + ""));
					table.put("settleTime", getCheckedString(TimeUtil.getTimeStyle(bussInfo.getAddTime(), "yyyy-MM-dd HH:mm")));
					table.put("signStatus", getCheckedString(bussInfo.getSignStatus() + ""));
					table.put("distributeStatus", getCheckedString(bussInfo.getDistributedStatus() + ""));
					
					String compGrade = "";//企业综合评价
					EvaInterService evaSer = new EvaInterService();
					EbEvaluate eva = evaSer.getEVsByBussId(businessId);
					if(eva!=null){
						compGrade = eva.getCompGrade()+"";
					}
					table.put("compGrade", getCheckedString(compGrade));
					result.add(table);
				}
			}
			success = true;
			setAttr("result", result);
		}else{
			success = true;
		}//关注列表 循环结束
		setAttr("current_page", attentPage.getPageNumber());
		setAttr("total_pages", attentPage.getTotalPage());
		setAttr("total_numb", attentPage.getTotalRow());
		setAttr("success", success);
		setAttr("message", message);
	}
	
	/**
	 * 存储企业状态相关信息
	 * @param businessId
	 * @param businessName
	 * @param resultHash
	 * @param addTime
	 * @param bussType
	 * @param isAttention
	 * @param bussInfo
	 */
	private void loadData(String businessId, String businessName, Hashtable<String, String> resultHash, 
			String addTime, EbBusinessType bussType, String isAttention, EbBusiness bussInfo) {

		String settleStatus = bussInfo.getSettleStatus() + "";//落户状态（0 未落户/未完成 、 1 已落户/已完成）
		String settleTime = bussInfo.getSettleTime() + "";//落户时间、完成时间
		String signStatus = bussInfo.getSignStatus()+"";//企业签收状态 0 未签收、1 已签收
		String distributeStatus = bussInfo.getDistributedStatus() + "";//任务分发状态 0 未分发、1 已分发
		
		resultHash.put("businessId", getCheckedString(businessId));
		resultHash.put("businessName", getCheckedString(businessName));
		resultHash.put("addTime", addTime);//企业添加时间
		if(bussType != null) {
			resultHash.put("typeId", bussType.getTypeId() + "");//企业类型id
			resultHash.put("typeName", bussType.getTypeName());//企业类型名称
		}else {
			resultHash.put("typeId", "");
			resultHash.put("typeName", "");
		}
		
		resultHash.put("isAttention", getCheckedString(isAttention));//关注状态
		resultHash.put("settleStatus", getCheckedString(settleStatus));//落户状态（0 未落户/未完成 、 1 已落户/已完成）
		resultHash.put("settleTime", TimeUtil.changeTimeStyle(settleTime));//落户时间
		resultHash.put("signStatus", getCheckedString(signStatus));//企业签收状态 0 未签收、1 已签收
		resultHash.put("distributeStatus", getCheckedString(distributeStatus));//任务分发状态 0 未分发、1 已分发
	}
	
	/**
	 * 11事项列表，办理企业（超时和办理完结）
	 * @param userId 当前登录用户
	 * @param page 显示页数
	 * @param status 事项状态 办理状态2 已逾期、3 办理完结
	 * @param roleforUse
	 * @param authority 用户权限
	 * @param userdept 
	 */
	public void workBuss(String userId,int page,String status,String roleforUse,String authority,String userdept){
		boolean success = false;
		String message = "数据为空";
		
		BussInterService bService = new BussInterService();
		TaskInterService taskSer = new TaskInterService();
		Page<EbTaskDistribute> taskDPage;
		if(authority.equals("3")||authority.equals("4")||authority.equals("5")){
			//权限为其他部门，则只能看到自己部门的清单
			taskDPage = taskSer.getDistribByStatusAll(status,page, userdept);
		}else{
			//权限为招商局和政务办，可以看到所有的，领导也可以
			taskDPage = taskSer.getDistribByStatusList(status, page);
		}
		
		List<EbTaskDistribute> taskDlist = taskDPage.getList();
		
		if(taskDlist.size()>0){
			List<Hashtable<String, String>> result = new ArrayList<Hashtable<String, String>>();
			for(int i=0;i<taskDlist.size();i++){
				EbTaskDistribute distri = taskDlist.get(i);
				Hashtable<String, String> resultHash = new Hashtable<String, String>();
				
				String taskId = distri.getTaskId()+"";
				EbTask task = taskSer.getTaskById(taskId);
				if(task!=null){
					success = true;
					message = "成功!";
					resultHash.put("businessId", getCheckedString(task.getBusinessId()+""));//企业id
					resultHash.put("businessName", getCheckedString(task.getBusinessName()));//企业名称
					
					//设置企业类型id
					EbBusiness bussInfo = bService.findById(task.getBusinessId() + "");
					EbBusinessType bussType = null;
					if(bussInfo != null) {
						bussType = bService.getBsTypeById(bussInfo.getTypeId() + "");
					}
					if(bussType != null) {
						resultHash.put("typeId", bussType.getTypeId() + "");
						resultHash.put("typeName", bussType.getTypeName());
					}else {
						resultHash.put("typeId", "");
						resultHash.put("typeName", "");
					}
					
					BussInterService attenSer = new BussInterService(); 
					if(roleforUse.equals("2")){
						EbAttention atten = attenSer.getAttenByUBid(userId, task.getBusinessId()+"");
						if(atten!=null){
							resultHash.put("isAttention","1");//是否被关注 0 未关注、1已关注
						}else{
							resultHash.put("isAttention","0");//是否被关注 0 未关注、1已关注
						}
					}else{
						resultHash.put("isAttention","");//办事员，为空
					}
					
					resultHash.put("settleStatus", getCheckedString(task.getCompStatus()+""));//落户状态 0 未落户/未完成 、 1 已落户/已完成
					resultHash.put("settleTime", TimeUtil.changeTimeStyle(task.getCompTime()));//落户时间、完成时间
					resultHash.put("signStatus", getCheckedString(task.getSignStatus()+""));//企业签收状态 0 未签收、1 已签收
					
					BussInterService bussSer = new BussInterService();
					EbBusiness buss = bussSer.getBussById(task.getBusinessId()+"");//获取到对应的企业判断分发状态
					if(buss!=null){
						resultHash.put("distributeStatus", getCheckedString(buss.getDistributedStatus()+""));//任务分发状态 0 未分发、1 已分发
					}else{
						resultHash.put("distributeStatus", "");//任务分发状态 0 未分发、1 已分发
					}
					//如果任务未分发，则不显示事项
					resultHash.put("itemId", getCheckedString(distri.getItemId()+""));//事项id
					
//					String itemName = "";//事项名称
					int time_limit = 15;//事项办理期限（多少个工作日）
					ItemInterService itemSer = new ItemInterService();
					EbItem item = itemSer.getItemById(distri.getItemId()+"");
					if (item == null) {
						item = GovNetUtil.getItemInfo(distri.getItemId() + "");
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
					
					String compGrade = "";//企业综合评价
					EvaInterService evaSer = new EvaInterService();
					EbEvaluate eva = evaSer.getEVsByBussId(task.getBusinessId()+"");
					if(eva!=null){
						compGrade = eva.getCompGrade()+"";
					}
					long sign_time = Long.parseLong(distri.getSignTime());//事项签收时间
					long nowTime = new Date().getTime();
					long days = TimeUtil.getWorkDaysBylong(sign_time, nowTime);//从签收到现在的天数
					long deday = days/24/3600/1000;
					
					resultHash.put("itemName", distri.getItemName());//事项名称
					resultHash.put("departName", getCheckedString(distri.getDepartName()));
					resultHash.put("itemSignStatus", distri.getSignStatus() + "");//事项签收状态
					resultHash.put("transactionStatus", distri.getTransactionStatus()+"");//办理状态 0 未开始、1 办理中、2 已逾期、3 办理完结、4 被退回
					resultHash.put("compGrade", compGrade);//企业综合评价
					
					//（2已逾期、3 已完结）
					if(status.equals("2")){
						int overTime = (int)deday - time_limit;
						resultHash.put("overTime", overTime+"");//超时时间(单位/天)
						resultHash.put("remainTime", "");//剩余时间（单位/天)
					}else if(status.equals("3")){
						resultHash.put("overTime", "");//超时时间(单位/天)
						resultHash.put("remainTime", "");//剩余时间（单位/天)
					}
					result.add(resultHash);
				}
			}
			setAttr("result", result);
		}else{
			success = true;
		}
		
		//计算企业量
		int totalNumb = 0;
		if(status.equals("2")){
			if(authority.equals("3")){
				//其他部门
				totalNumb = bussNumb(2, 2, userdept);
			}else{
				totalNumb = bussNumb(1, 2, "");
			}
		}else if(status.equals("3")){
			//办理完结
			if(authority.equals("3")){
				//其他部门
				List<Record> records = Db.use("base").find("select distinct task_id from EB_TASK_DISTRIBUTE where " +
						"transaction_status=3 and depart_id = " + userdept);
				totalNumb = records.size();
			}else{
				List<Record> records = Db.use("base").find("select * from EB_TASK where comp_status=1");
				totalNumb = records.size();
			}
		}
		setAttr("total_numb", totalNumb);
		setAttr("current_page", taskDPage.getPageNumber());
		setAttr("total_pages", taskDPage.getTotalPage());
		setAttr("success", success);
		setAttr("message", message);
	}
	
	/**
	 * 11事项列表，办理企业,总体正在办理任务（未开始和办理中1）
	 * @param userId 当前登录用户为其他部门办事员，只能看到自己部门的事项
	 * @param page 显示页数
	 * @param status 办理状态0 未开始、1 办理中，正在办理包括：已逾期，已暂停
	 */
	public void workApply3(String userId,String userdept,String authority,int page,String status,Controller ctrl){
		boolean success = false;
		String message = "数据为空";
		
		BussInterService bService = new BussInterService();
		TaskInterService taskSer = new TaskInterService();
		Page<EbTaskDistribute> taskDPage;
		taskDPage = taskSer.getDistribByStatusAll2(status,page, userdept);//获取办理中和未开始的事项，正在办理包括：已逾期，已暂停
		List<EbTaskDistribute> taskDlist = taskDPage.getList();
		
		if(taskDlist.size()>0){
			List<Hashtable<String, String>> result = new ArrayList<Hashtable<String, String>>();
			for(int i=0;i<taskDlist.size();i++){
				EbTaskDistribute distri = taskDlist.get(i);
				Hashtable<String, String> resultHash = new Hashtable<String, String>();
				
				String taskId = distri.getTaskId()+"";
				EbTask task = taskSer.getTaskById(taskId);
				if(task!=null){
					success = true;
					message = "成功!";
					resultHash.put("businessId", getCheckedString(task.getBusinessId()+""));//企业id
					resultHash.put("businessName", getCheckedString(task.getBusinessName()));//企业名称
					System.out.print("打印正在办理的任务分发"+task.getBusinessName()+"事项名称："+ distri.getItemName()+"办理状态："+distri.getTransactionStatus());
					//设置企业类型id
					EbBusiness bussInfo = bService.findById(task.getBusinessId() + "");
					EbBusinessType bussType = null;
					if(bussInfo != null) {
						bussType = bService.getBsTypeById(bussInfo.getTypeId() + "");
					}
					if(bussType != null) {
						resultHash.put("typeId", bussType.getTypeId() + "");
						resultHash.put("typeName", bussType.getTypeName());
					}else {
						resultHash.put("typeId", "");
						resultHash.put("typeName", "");
					}
					
					resultHash.put("isAttention","");//办事员，为空
					resultHash.put("settleStatus", getCheckedString(task.getCompStatus()+""));//落户状态 0 未落户/未完成 、 1 已落户/已完成
					resultHash.put("settleTime", TimeUtil.changeTimeStyle(task.getCompTime()));//落户时间、完成时间
					resultHash.put("signStatus", getCheckedString(task.getSignStatus()+""));//企业签收状态 0 未签收、1 已签收
					
					BussInterService bussSer = new BussInterService();
					EbBusiness buss = bussSer.getBussById(task.getBusinessId()+"");//获取到对应的企业判断分发状态
					if(buss!=null){
						resultHash.put("distributeStatus", getCheckedString(buss.getDistributedStatus()+""));//任务分发状态 0 未分发、1 已分发
					}else{
						resultHash.put("distributeStatus", "");//任务分发状态 0 未分发、1 已分发
					}
					//如果任务未分发，则不显示事项
					resultHash.put("itemId", getCheckedString(distri.getItemId()+""));//事项id
					
//					String itemName = "";//事项名称
					int time_limit = 15;//事项办理期限（多少个工作日）
					ItemInterService itemSer = new ItemInterService();
					EbItem item = itemSer.getItemById(distri.getItemId()+"");
					if(item == null) {
						item = GovNetUtil.getItemInfo(distri.getItemId() + "");
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
					
					String compGrade = "";//企业综合评价
					EvaInterService evaSer = new EvaInterService();
					EbEvaluate eva = evaSer.getEVsByBussId(task.getBusinessId()+"");
					if(eva!=null){
						compGrade = eva.getCompGrade()+"";
					}
					resultHash.put("itemName", distri.getItemName());//事项名称
					resultHash.put("departName", getCheckedString(distri.getDepartName()));
					resultHash.put("itemSignStatus", distri.getSignStatus() + "");//事项签收状态
					resultHash.put("transactionStatus", distri.getTransactionStatus()+"");//办理状态 0 未开始、1 办理中、2 已逾期、3 办理完结、4 被退回
					resultHash.put("compGrade", compGrade);//企业综合评价
					
					if(distri.getSignTime()!=null){
						long sign_time = Long.parseLong(distri.getSignTime());//事项签收时间
						long nowTime = new Date().getTime();
						long days = TimeUtil.getWorkDaysBylong(sign_time, nowTime);//从签收到现在的天数
						long deday = days/24/3600/1000;
						if(distri.getSignStatus().toString().equals("1")){
							if(distri.getTransactionStatus().toString().equals("2")){
								//事项逾期
								int overTime = (int)deday - time_limit;
								resultHash.put("overTime", overTime+"");//超时时间(单位/天)
								resultHash.put("remainTime", "");//剩余时间（单位/天)
							}else{
								int remainTime = time_limit - (int)deday;
								resultHash.put("remainTime", remainTime+"");//剩余时间（单位/天)
								resultHash.put("overTime", "");//超时时间(单位/天)
							}
						}
					}else{
						//事项未开始
						resultHash.put("remainTime","");//剩余时间（单位/天)
						resultHash.put("overTime", "");//超时时间(单位/天)
					}
					result.add(resultHash);
				}
			}
			setAttr("result", result);
		}else{
			success = true;
		}
		//计算企业量
		int totalNumb = bussNumb(2, 1, userdept);
		
		setAttr("current_page", taskDPage.getPageNumber());
		setAttr("total_pages", taskDPage.getTotalPage());
		setAttr("total_numb", totalNumb);
		setAttr("success", success);
		setAttr("message", message);
	}
	
	/**
	 * 11事项列表，办理企业,总体正在办理任务（未开始和办理中1）
	 * @param userId 当前登录用户为办事员，招商局和政务办
	 * @param page 显示页数
	 * @param status 办理状态0 未开始、1 办理中
	 */
	public void workApply(String userId, String userdept, String authority, int page, String status, Controller ctrl){
		boolean success = false;
		String message = "数据为空";
		
		BussInterService bService = new BussInterService();
		TaskInterService taskSer = new TaskInterService();
		Page<EbTask> taskPage = taskSer.getTasksByCompStatus0(page);
		List<EbTask> taskList = taskPage.getList();
		if(taskList.size() > 0){
			
			List<Hashtable<String, String>> result = new ArrayList<Hashtable<String, String>>();
			
			for(int i = 0; i < taskList.size(); i++){
				message = "成功";
				success = true;
				
				Hashtable<String, String> resultHash = new Hashtable<String, String>();
				EbTask task = taskList.get(i);
				String signStatus = task.getSignStatus() + "";//任务签收状态
				
				resultHash.put("taskId", getCheckedString(task.getTaskId()+""));//任务id
				resultHash.put("businessId", getCheckedString(task.getBusinessId()+""));//企业id
				resultHash.put("businessName", getCheckedString(task.getBusinessName()));//企业名称
				//设置企业类型id
				EbBusiness bussInfo = bService.findById(task.getBusinessId() + "");
				EbBusinessType bussType = null;
				if(bussInfo != null) {
					bussType = bService.getBsTypeById(bussInfo.getTypeId() + "");
				}
				if(bussType != null) {
					resultHash.put("typeId", bussType.getTypeId() + "");
					resultHash.put("typeName", bussType.getTypeName());
				}else {
					resultHash.put("typeId", "");
					resultHash.put("typeName", "");
				}
				
				resultHash.put("isAttention", "");//是否被关注，办事员不传
				resultHash.put("settleStatus", "0");//落户状态 0 未落户/未完成 、 1 已落户/已完成
				resultHash.put("settleTime", "");//落户时间、完成时间
				resultHash.put("signStatus", signStatus);//企业签收状态 0 未签收、1 已签收
				
				if(signStatus.equals("0")){
					//任务未签收（只有政务办事员才能看到）权限标识（ 1 招商局权限 、 2 政务办权限 、3 其他部门）（办事员才有权限
					resultHash.put("distributeStatus", "0");//任务分发状态 0 未分发、1 已分发
					result.add(resultHash);
				}else if(signStatus.equals("1")){
					//任务已签收（任务签收后领导就能看到）
					System.out.print("测试已签收企业显示" + task.getBusinessName());
					
					boolean flag = false;//任务分发状态
					BussInterService bussSer = new BussInterService();
					EbBusiness buss = bussSer.getBussById(task.getBusinessId() + "");//获取到对应的企业判断分发状态
					if(buss != null){
						String distriStatus = buss.getDistributedStatus() + "";
						resultHash.put("distributeStatus", getCheckedString(buss.getDistributedStatus() + ""));//任务分发状态 0 未分发、1 已分发
						
						if(distriStatus.equals("1")){
							flag = true;//任务已分发,且用户权限为其他部门
						}else if(distriStatus.equals("0") && (authority.equals("2") || authority.equals("1"))){
							flag = false;//任务未分发，用户权限为政务办事员，招商局办事员
							result.add(resultHash);
						}
					}
					if(flag){
						//任务已经分发，查找其他部门的任务清单
						List<EbTaskDistribute> taskDisli = new ArrayList<EbTaskDistribute>();
						if(authority.equals("3")){
							taskDisli = taskSer.getDistribByStatusDept(status, task.getTaskId() + "", userdept);
						}else{
							//直接跳到这里，政务办和招商局
							taskDisli = taskSer.getDistribByStatusLi(status, task.getTaskId() + "");
						}
						
						if(taskDisli.size()>0){
							for(int j = 0; j < taskDisli.size(); j++){
								EbTaskDistribute distri = taskDisli.get(j);
								
								Hashtable<String, String> resultHashItem = new Hashtable<String, String>();
								
								resultHashItem.put("taskId", getCheckedString(task.getTaskId()+""));//任务id
								resultHashItem.put("businessId", getCheckedString(task.getBusinessId()+""));//企业id
								resultHashItem.put("businessName", getCheckedString(task.getBusinessName()));//企业名称
								resultHashItem.put("isAttention", "");//是否被关注，办事员不传
								resultHashItem.put("settleStatus", "0");//落户状态 0 未落户/未完成 、 1 已落户/已完成
								resultHashItem.put("settleTime", "");//落户时间、完成时间
								resultHashItem.put("signStatus", signStatus);//企业签收状态 0 未签收、1 已签收
								
								//办理状态 0 未开始、1 办理中、2 已逾期、3 办理完结、4 被退回、5已暂停
								resultHashItem.put("transactionStatus", getCheckedString(distri.getTransactionStatus()+""));
								
								resultHashItem.put("distributeStatus", getCheckedString(buss.getDistributedStatus()+""));//任务分发状态 0 未分发、1 已分发
//								System.out.print("测试任务分发事项存在"+task.getBusinessName()+taskDisli.size()+distri.getItemName());
								
								resultHashItem.put("itemId", getCheckedString(distri.getItemId()+""));//事项id
								resultHashItem.put("distriId", getCheckedString(distri.getDistrId()+""));//分发清单id
//								String itemName = "";//事项名称
								int time_limit = 15;//事项办理期限（多少个工作日）
								ItemInterService itemSer = new ItemInterService();
								EbItem item = itemSer.getItemById(distri.getItemId()+"");
								if(item == null) {
									item = GovNetUtil.getItemInfo(distri.getItemId() + "");
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
								
								String compGrade = "";//企业综合评价
								EvaInterService evaSer = new EvaInterService();
								EbEvaluate eva = evaSer.getEVsByBussId(task.getBusinessId()+"");
								if(eva!=null){
									compGrade = eva.getCompGrade()+"";
								}
								resultHashItem.put("itemName", distri.getItemName() + "");//事项名称
								resultHashItem.put("departName", getCheckedString(distri.getDepartName()));
								resultHashItem.put("itemSignStatus", distri.getSignStatus() + "");//事项签收状态
								resultHashItem.put("transactionStatus", distri.getTransactionStatus()+"");//办理状态 0 未开始、1 办理中、2 已逾期、3 办理完结、4 被退回、5已暂停
								resultHashItem.put("compGrade", compGrade);//企业综合评价
								
								if(distri.getSignTime()!=null){
									long sign_time = Long.parseLong(distri.getSignTime());//事项签收时间
									long nowTime = System.currentTimeMillis();	//当前时间
									long days = TimeUtil.getWorkDaysBylong(sign_time, nowTime);//从签收到现在的天数
									long deday = days/24/3600/1000;
									if(distri.getSignStatus().toString().equals("1")){
										if(distri.getTransactionStatus().toString().equals("2")){
											//事项逾期
											int overTime = (int)deday - time_limit;
											resultHashItem.put("overTime", overTime+"");//超时时间(单位/天)
											resultHashItem.put("remainTime", "");//剩余时间（单位/天)
										}else{
											int remainTime = time_limit - (int)deday;
											resultHashItem.put("remainTime", remainTime+"");//剩余时间（单位/天)
											resultHashItem.put("overTime", "");//超时时间(单位/天)
										}
									}
								}else{
									resultHashItem.put("remainTime", "");//剩余时间（单位/天)
									resultHashItem.put("overTime", "");//超时时间(单位/天)
								}
								
								System.out.print("测试任务分发事项存在"+task.getBusinessName()+distri.getItemName()+"---");
								result.add(resultHashItem);
							}
						}
					}
//					result.add(resultHash);
				}
//				result.add(resultHash);//总体事项列表
			}
			setAttr("result",result);
		}
		//计算企业量
		int totalNumb = bussNumb(1, 1, "");
		
		setAttr("current_page",taskPage.getPageNumber());
		setAttr("total_pages",taskPage.getTotalPage());
		setAttr("total_numb",totalNumb);
		setAttr("success", success);
		setAttr("message", message);
	}
	
	/**
	 * 11事项列表，办理企业,总体正在办理任务（未开始和办理中1）
	 * @param userId 当前登录用户为领导
	 * @param page 显示页数
	 * @param status 办理状态0 未开始、1 办理中
	 */
	public void leardApply(String userId,int page,String status,Controller ctrl){
		boolean success = false;
		String message = "数据为空";
		
		TaskInterService taskSer = new TaskInterService();
		Page<EbTask> taskPage = taskSer.getTasksBySignStatus1(page);//获取已签收的任务
		List<EbTask> taskList = taskPage.getList();
		if(taskList.size()>0){
			List<Hashtable<String, String>> result = new ArrayList<Hashtable<String, String>>();
			for(int i=0;i<taskList.size();i++){
				message = "成功";
				success = true;
					
				Hashtable<String, String> resultHash = new Hashtable<String, String>();
				EbTask task = taskList.get(i);
				
				resultHash.put("businessId", getCheckedString(task.getBusinessId()+""));//企业id
				resultHash.put("businessName", getCheckedString(task.getBusinessName()));//企业名称
				
				BussInterService attenSer = new BussInterService(); 
				EbAttention atten = attenSer.getAttenByUBid(userId, task.getBusinessId()+"");
				if(atten!=null){
					resultHash.put("isAttention","1");//是否被关注 0 未关注、1已关注
				}else{
					resultHash.put("isAttention","0");//是否被关注 0 未关注、1已关注
				}
				//任务已签收（任务签收后领导就能看到）
				resultHash.put("settleStatus", "0");//落户状态 0 未落户/未完成 、 1 已落户/已完成
				resultHash.put("settleTime", "");//落户时间、完成时间
				resultHash.put("signStatus", "1");//企业签收状态 0 未签收、1 已签收
				
				boolean flag = false;//任务分发状态
				BussInterService bussSer = new BussInterService();
				EbBusiness buss = bussSer.getBussById(task.getBusinessId()+"");//获取到对应的企业判断分发状态
				if(buss!=null){
					String distriStatus = buss.getDistributedStatus()+"";
					resultHash.put("distributeStatus", getCheckedString(buss.getDistributedStatus()+""));//任务分发状态 0 未分发、1 已分发
					if(distriStatus.equals("1")){
						flag = true;//任务已分发
					}else{
						flag = false;
					}
				}
				if(flag){
					//任务已经分发，查找任务清单
					List<EbTaskDistribute> taskDisli = taskSer.getDistribByStatusLi(status,task.getTaskId()+"");
					if(taskDisli.size()>0){
						for(int j = 0;j<taskDisli.size();j++){
							EbTaskDistribute distri = taskDisli.get(j);
							
							resultHash.put("itemId", getCheckedString(distri.getItemId()+""));//事项id
							
							int time_limit = 15;//事项办理期限（多少个工作日）
							ItemInterService itemSer = new ItemInterService();
							EbItem item = itemSer.getItemById(distri.getItemId()+"");
							if (item == null) {
								item = GovNetUtil.getItemInfo(distri.getItemId() + "");
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
							
							String compGrade = "";//企业综合评价
							EvaInterService evaSer = new EvaInterService();
							EbEvaluate eva = evaSer.getEVsByBussId(task.getBusinessId()+"");
							if(eva!=null){
								compGrade = eva.getCompGrade()+"";
							}
							
							resultHash.put("itemName", distri.getItemName() + "");//事项名称
							resultHash.put("departName", getCheckedString(distri.getDepartName()));
							resultHash.put("itemSignStatus", distri.getSignStatus() + "");//事项签收状态
							resultHash.put("transactionStatus", distri.getTransactionStatus()+"");//办理状态 0 未开始、1 办理中、2 已逾期、3 办理完结、4 被退回
							resultHash.put("compGrade", compGrade);//企业综合评价
							
							int transStatus = distri.getTransactionStatus().intValue();
							
							if(transStatus == 1){
								long sign_time = Long.parseLong(distri.getSignTime());//事项签收时间
								long nowTime = new Date().getTime();
								long days = TimeUtil.getWorkDaysBylong(sign_time, nowTime);//从签收到现在的天数
								long deday = days/24/3600/1000;
								
								int remainTime = time_limit - (int)deday;
								resultHash.put("remainTime", remainTime+"");//剩余时间（单位/天)
								resultHash.put("overTime", "");//超时时间(单位/天)
							}else if(transStatus == 2){
								long sign_time = Long.parseLong(distri.getSignTime());//事项签收时间
								long nowTime = new Date().getTime();
								long days = TimeUtil.getWorkDaysBylong(sign_time, nowTime);//从签收到现在的天数
								long deday = days/24/3600/1000;
								//事项逾期
								int overTime = (int)deday - time_limit;
								resultHash.put("overTime", overTime+"");//超时时间(单位/天)
								resultHash.put("remainTime", "");//剩余时间（单位/天)
							}
						}
					}
				}
				result.add(resultHash);
			}
			setAttr("result",result);
		}
		//计算企业量
		int totalNumb = bussNumb(1, 1, "");
				
		setAttr("current_page",taskPage.getPageNumber());
		setAttr("total_pages",taskPage.getTotalPage());
		setAttr("total_numb",totalNumb);
		setAttr("success", success);
		setAttr("message", message);
	}
	
	/**
	 * 查看相应条件的企业数量
	 * @param userAuth当前登录用户类型1政务办、领导，2其他部门办事员
	 * @param status 企业状态：1正在办理，2已逾期
	 */
	public int bussNumb(int userAuth,int status,String deptId){
		int numb = 0;
		if(status == 1){
			//正在办理，包括逾期
			if(userAuth == 2){
				//如果当前用户为其他部门的，只能看到和自己部门相关的企业
				List<Record> records = Db.use("base").find("select distinct task_id from EB_TASK_DISTRIBUTE where " +
						"transaction_status IN(0, 1, 2, 5) and depart_id = " + deptId);
				numb = records.size();
			}else{
				//看到全部企业
//				select * from EB_TASK where comp_status=0
//				List<Record> records = Db.use("base").find("select distinct task_id from EB_TASK_DISTRIBUTE where " +
//						"transaction_status IN(0, 1, 2, 5)");
				List<Record> records = Db.use("base").find("select * from EB_TASK where comp_status=0");
				numb = records.size();
			}
		}else if(status == 2){
			//已逾期
			if(userAuth == 2){
				//如果当前用户为其他部门的，只能看到和自己部门相关的企业
				List<Record> records = Db.use("base").find("select distinct task_id from EB_TASK_DISTRIBUTE where " +
						"transaction_status =2 and depart_id = " + deptId);
				numb = records.size();
			}else{
				//看到全部企业
				List<Record> records = Db.use("base").find("select distinct task_id from EB_TASK_DISTRIBUTE where " +
						"transaction_status =2");
				numb = records.size();
			}
		}
		return numb;
	}
	
}
