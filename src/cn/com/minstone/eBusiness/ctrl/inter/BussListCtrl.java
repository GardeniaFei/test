package cn.com.minstone.eBusiness.ctrl.inter;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import cn.com.minstone.eBusiness.model.EbAttention;
import cn.com.minstone.eBusiness.model.EbBusiness;
import cn.com.minstone.eBusiness.model.EbTask;
import cn.com.minstone.eBusiness.model.EbTaskDistribute;
import cn.com.minstone.eBusiness.model.EbUserInfo;
import cn.com.minstone.eBusiness.service.inter.BussInterService;
import cn.com.minstone.eBusiness.service.inter.TaskInterService;
import cn.com.minstone.eBusiness.service.inter.UserInterService;
import cn.com.minstone.eBusiness.util.Config;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.render.JsonRender;

public class BussListCtrl extends Controller {

	/**
	 * 检查字符串是否为空，为空则传递“”
	 * 
	 * @param str
	 * @return
	 */
	private String getCheckedString(String str) {
		if (str != null) {
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
	 * 9企业列表
	 */
	public void bussList() {
		int page = this.getParaToInt("page", 1);
		String userId = Config.getSessionUserID(this);

		if (userId == null || "".equals(userId)) {
			faultResult("请登录！");
		} else {
			// 判断用户身份
			String roleforUser = "";
			String userdept = "";
			String authority = "";
			UserInterService userSer = new UserInterService(this);
			EbUserInfo user = userSer.findById(userId);
			if (user != null) {
				roleforUser = user.getRoleType() + "";// 1区级办事人员、2领导、3企业、4市级办事员、5区级政务办
				userdept = user.getDepartId() + "";// 获取用户部门
				authority = user.getAuthority() + "";// 权限标识（1VIP服务专员管理员、2VIP服务专员权限 、3单位首席权限、
				//4部门跟办人权限、5市级服务专员权限、6 VIP服务专员管理员+单位首席权限、7 VIP服务专员+单位首席权限
			}
			if (roleforUser.equals("2")) {
				// 如果用户为领导，则显示区级政务办签收的所有企业
				leadBuss(userId, page);
			} else if (roleforUser.equals("4")) {
				// 如果用户为市级办事员
					// 用户为其他部门只能看到自己有事项的企业+
				if(authority.equals("5")||authority.equals("3")){//市级首席和市级政务办
					bussByOrderdept(userdept, userId, page);
				}else if(authority.equals("4")){
					bussBydeptAuth(userdept, userId, page);
				}
			}else if (roleforUser.equals("1")) {
				// 如果用户为区级办事员
					// 用户为其他部门只能看到自己有事项的企业
				if(authority.equals("3")){
					bussByOrderdept(userdept, userId, page);
				}else if(authority.equals("4")){
					bussBydeptAuth(userdept, userId, page);
				}
			}else if (roleforUser.equals("5")) {
				if (authority.equals("1") || authority.equals("2") || authority.equals("6") || authority.equals("7")) {
					// 用户为区级政务办并有wip权限，只要增加了企业就能看到
					allBuss(userId, page);
				}else{//只能看到自己相关的数据
					bussByOrderdept(userdept, userId, page);
				}
			}
			
		}
		render(new JsonRender().forIE());
	}

	/**
	 * 部门跟办人看到自己是办理人的事项的企业
	 * @param userdept
	 * @param userId
	 * @param page
	 */
	private void bussBydeptAuth(String userdept, String userId, int page) {
		boolean success = false;
		String message = "数据为空";

		TaskInterService taskSer = new TaskInterService();

		List<EbTaskDistribute> taskDlist = taskSer.getDtsBydeptUserAllLi(page,userdept,userId);

		if (taskDlist.size() > 0) {
			List<Hashtable<String, String>> result = new ArrayList<Hashtable<String, String>>();
			for (int i = 0; i < taskDlist.size(); i++) {
				EbTaskDistribute distri = taskDlist.get(i);
				Hashtable<String, String> resultHash = new Hashtable<String, String>();

				String taskId = distri.getTaskId() + "";
				EbTask task = taskSer.getTaskById(taskId);
				if (task != null) {
					success = true;
					message = "成功!";
					resultHash.put("businessId",getCheckedString(task.getBusinessId() + ""));// 企业id
					resultHash.put("businessName",getCheckedString(task.getBusinessName()));// 企业名称
					resultHash.put("isAttention", "");// 办事员，为空
					
					EbBusiness buss = new BussInterService().findById(task.getBusinessId() + "");
					if(buss != null) {
						resultHash.put("settleStatus", buss.getSettleStatus() + "");//企业落户状态 1 已落户 0 正在办理
						//企业vip服务专员
						resultHash.put("serviceAdmin",getCheckedString(buss.getService_admin()));
					}
					result.add(resultHash);
				}
			}
			this.setAttr("result", result);
		}
		setAttr("current_page", 1);
		setAttr("total_pages", 1);
		setAttr("total_numb", taskDlist.size());
		setAttr("success", success);
		setAttr("message", message);
	}

	/**
	 * 办事员，招商办和政务办的能看到所有企业
	 * 
	 * @param userId
	 * @param page
	 */
	public void allBussByUId(String userId, int page) {
		BussInterService bussSer = new BussInterService();
		Page<EbBusiness> bussPage = bussSer.getBussByUId(userId, page);
		List<EbBusiness> bussList = bussPage.getList();

		boolean success = false;
		String message = "数据为空";

		if (bussList != null && !bussList.isEmpty()) {
			success = true;
			message = "成功!";
			List<Hashtable<String, String>> result = new ArrayList<Hashtable<String, String>>();
			for (int i = 0; i < bussList.size(); i++) {
				EbBusiness buss = bussList.get(i);
				Hashtable<String, String> resultHash = new Hashtable<String, String>();
				// 企业Id
				resultHash.put("businessId",
						getCheckedString(buss.getBusinessId() + ""));
				// //企业名称
				resultHash.put("businessName",
						getCheckedString(buss.getBusinessName() + ""));
				//企业vip服务专员
				resultHash.put("serviceAdmin",
						getCheckedString(buss.getService_admin()));
				resultHash.put("isAttention", "");// 办事员没有关注
				resultHash.put("settleStatus", buss.getSettleStatus() + "");//企业落户状态 1 已落户 0 正在办理

				result.add(resultHash);
			}
			this.setAttr("result", result);
		}
		setAttr("current_page", bussPage.getPageNumber());
		setAttr("total_pages", bussPage.getTotalPage());
		setAttr("total_numb", bussPage.getTotalRow());
		setAttr("success", success);
		setAttr("message", message);
	}

	/**
	 * 领导用户能查看到的企业
	 * 
	 * @param userId
	 * @param page
	 */
	public void leadBuss(String userId, int page) {
		BussInterService bussSer = new BussInterService();
		Page<EbBusiness> bussPage = bussSer.getAllBussSign(page);
//		List<EbBusiness> bussList = bussPage.getList();

		List<EbBusiness> bussList = bussSer.getAllBussSignli();
		
		boolean success = false;
		String message = "数据为空";

		if (bussList != null && !bussList.isEmpty()) {
			success = true;
			message = "成功!";
			List<Hashtable<String, String>> result = new ArrayList<Hashtable<String, String>>();
			for (int i = 0; i < bussList.size(); i++) {
				EbBusiness buss = bussList.get(i);

				Hashtable<String, String> resultHash = new Hashtable<String, String>();
				// 企业Id
				resultHash.put("businessId",
						getCheckedString(buss.getBusinessId() + ""));
				// //是否被当前用户关注,默认为0
				BussInterService attenSer = new BussInterService();
				EbAttention atten = attenSer.getAttenByUBid(userId,
						buss.getBusinessId() + "");
				if (atten != null) {
					resultHash.put("isAttention", "1");// 是否被关注 0 未关注、1已关注
				} else {
					resultHash.put("isAttention", "0");// 是否被关注 0 未关注、1已关注
				}
				//企业vip服务专员
				resultHash.put("serviceAdmin",
						getCheckedString(buss.getService_admin()));
				//企业名称
				resultHash.put("businessName",
						getCheckedString(buss.getBusinessName() + ""));
				resultHash.put("settleStatus", buss.getSettleStatus() + "");//企业落户状态 1 已落户 0 正在办理

				result.add(resultHash);
			}
			this.setAttr("result", result);
		}
//		setAttr("current_page", bussPage.getPageNumber());
//		setAttr("total_pages", bussPage.getTotalPage());
		setAttr("total_numb", bussPage.getTotalRow());
		setAttr("success", success);
		setAttr("message", message);
	}

	/**
	 * 办事员，招商办和政务办的能看到所有企业
	 * 
	 * @param userId
	 * @param page
	 */
	public void allBuss(String userId, int page) {
		BussInterService bussSer = new BussInterService();
		Page<EbBusiness> bussPage = bussSer.getAllBussInfo(page);
//		List<EbBusiness> bussList = bussPage.getList();
		List<EbBusiness> bussList = bussSer.getAllBussInfoList();
				
		boolean success = false;
		String message = "数据为空";

		if (bussList != null && !bussList.isEmpty()) {
			success = true;
			message = "成功!";
			List<Hashtable<String, String>> result = new ArrayList<Hashtable<String, String>>();
			for (int i = 0; i < bussList.size(); i++) {
				EbBusiness buss = bussList.get(i);
				Hashtable<String, String> resultHash = new Hashtable<String, String>();
				// 企业Id
				resultHash.put("businessId",
						getCheckedString(buss.getBusinessId() + ""));
				// //企业名称
				resultHash.put("businessName",
						getCheckedString(buss.getBusinessName() + ""));
				//企业vip服务专员
				resultHash.put("serviceAdmin",
						getCheckedString(buss.getService_admin()));
				resultHash.put("isAttention", "");// 办事员没有关注
				resultHash.put("settleStatus", buss.getSettleStatus() + "");//企业落户状态 1 已落户 0 正在办理

				result.add(resultHash);
			}
			this.setAttr("result", result);
		}
//		setAttr("current_page", bussPage.getPageNumber());
//		setAttr("total_pages", bussPage.getTotalPage());
		setAttr("total_numb", bussPage.getTotalRow());
		setAttr("success", success);
		setAttr("message", message);
	}

	/**
	 * 其他部门办事员只能看到和自己部门相关的事项
	 * 
	 * @param userdept
	 * @param userId
	 * @param page
	 */
	public void bussByOrderdept(String userdept, String userId, int page) {
		boolean success = false;
		String message = "数据为空";

		TaskInterService taskSer = new TaskInterService();
		Page<EbTaskDistribute> taskDPage;

		// taskDPage = taskSer.getDistribBydeptAll(page, userdept);
		List<EbTaskDistribute> taskDlist = taskSer.getDistribBydeptAllli(page,
				userdept);
		// List<EbTaskDistribute> taskDlist = taskDPage.getList();

		if (taskDlist.size() > 0) {
			List<Hashtable<String, String>> result = new ArrayList<Hashtable<String, String>>();
			for (int i = 0; i < taskDlist.size(); i++) {
				System.out.print("页数翻页1：" + taskDlist.size());
				EbTaskDistribute distri = taskDlist.get(i);
				Hashtable<String, String> resultHash = new Hashtable<String, String>();

				String taskId = distri.getTaskId() + "";
				EbTask task = taskSer.getTaskById(taskId);
				if (task != null) {
					success = true;
					message = "成功!";
					resultHash.put("businessId",
							getCheckedString(task.getBusinessId() + ""));// 企业id
					resultHash.put("businessName",
							getCheckedString(task.getBusinessName()));// 企业名称
					resultHash.put("isAttention", "");// 办事员，为空
					
					EbBusiness buss = new BussInterService().findById(task.getBusinessId() + "");
					if(buss != null) {
						resultHash.put("settleStatus", buss.getSettleStatus() + "");//企业落户状态 1 已落户 0 正在办理
						//企业vip服务专员
						resultHash.put("serviceAdmin",
								getCheckedString(buss.getService_admin()));
					}
					result.add(resultHash);
				}
			}
			this.setAttr("result", result);
		}
		setAttr("current_page", 1);
		setAttr("total_pages", 1);
		// setAttr("total_numb", taskDPage.getTotalRow());
		setAttr("total_numb", taskDlist.size());
		setAttr("success", success);
		setAttr("message", message);
	}
}
