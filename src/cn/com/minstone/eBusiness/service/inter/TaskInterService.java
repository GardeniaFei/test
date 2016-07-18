package cn.com.minstone.eBusiness.service.inter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import com.jfinal.plugin.activerecord.Page;

import cn.com.minstone.eBusiness.dao.inter.TaskInterDao;
import cn.com.minstone.eBusiness.model.DepartModel;
import cn.com.minstone.eBusiness.model.EbBusiness;
import cn.com.minstone.eBusiness.model.EbBusinessType;
import cn.com.minstone.eBusiness.model.EbDepart;
import cn.com.minstone.eBusiness.model.EbItem;
import cn.com.minstone.eBusiness.model.EbSpcProgram;
import cn.com.minstone.eBusiness.model.EbTask;
import cn.com.minstone.eBusiness.model.EbTaskDistribute;
import cn.com.minstone.eBusiness.model.EbUserInfo;
import cn.com.minstone.eBusiness.model.EbTaskList;
import cn.com.minstone.eBusiness.service.BaseService;
import cn.com.minstone.eBusiness.util.Config;
import cn.com.minstone.eBusiness.util.GovNetUtil;
import cn.com.minstone.eBusiness.util.LogUtil;
import cn.com.minstone.eBusiness.util.MD5Util;
import cn.com.minstone.eBusiness.util.PushUtil;
import cn.com.minstone.eBusiness.util.StringUtil;
import cn.com.minstone.eBusiness.util.TimeUtil;

public class TaskInterService extends BaseService {

	private TaskInterDao dao;

	public TaskInterService() {
		this.dao = new TaskInterDao();
	}
	
	/** 
	 * 计算事项分发表中的事项办理是否超时办理
	*/
	public void cacularItemTime() {
		TaskInterDao tDao = new TaskInterDao();
		List<String> deptIds = tDao.findDeptIds();
		long curTime = System.currentTimeMillis();
		MsgInterService msgService = new MsgInterService();
		
		for (int i = 0; i < deptIds.size(); i++) {
			List<EbTaskDistribute> distrList = tDao.findDtsBydeptId(deptIds.get(i));
			EbDepart deptInfo = GovNetUtil.getDeptInfo(deptIds.get(i));
			if(distrList != null && distrList.size() > 0 && deptInfo != null) {
				int total = 0;
				int totalForPre = 0;
				for (int j = 0; j < distrList.size(); j++) {
					EbTaskDistribute distrInfo = distrList.get(j);
					EbItem itemInfo = null;
					if(distrInfo.getItemType().intValue() == 0) {//系统后台
						itemInfo = new ItemInterService().getItemById(distrInfo.getItemId() + "");
					}else if(distrInfo.getItemType().intValue() == 1) {//审批接口
						itemInfo = GovNetUtil.getItemInfo(distrInfo.getItemId() + "");
					}
					if (itemInfo != null) {
						int timeLimit = 15;
						if(itemInfo.getTimeLimit() != null) {
							timeLimit = itemInfo.getTimeLimit().intValue();
						}
						long start = Long.parseLong(distrInfo.getSignTime());
						long days = TimeUtil.getWorkDaysBylong(start, curTime);
						long deday = days/24/3600/1000;
						if(deday - timeLimit > 0) {//已超时
							try {
								distrInfo.setTransactionStatus(new BigDecimal("2"));
								boolean flag = distrInfo.update();
								
								if(flag) {
									total++;
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}else if((timeLimit - deday ==0) || (timeLimit - deday ==1)){
/************************2015-11-26 feini********************************/	
							totalForPre++;
/************************2015-11-26 feini********************************/		
						}
					}
				}
				if(total > 0) {
					//TODO 消息推送
					try {
						boolean flg = msgService.addNews(null, null, "10", "", null, 
								curTime + "", null, null, "0", deptInfo.getDepartId() + "", 
								deptInfo.getDepartName());
						if(flg) {
							String newsContent = deptInfo.getDepartName() + "（单位）有" + total + "个办件超时办理，请尽快办理。";
							List<String> strAlias = new ArrayList<String>();
							strAlias.add(deptIds.get(i));
							strAlias.add(Config.ZWB);
							
							Hashtable<String, String> table = new Hashtable<String, String>();
							table.put("newsType", "10");
							table.put("content", newsContent);
							table.put("status", "0");
							PushUtil.pushNotifyToTagert(strAlias, StringUtil.getJson(table));
						}else {
							LogUtil.log("消息插入数据库失败");
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
/************************2015-11-26 feini********************************/	
				if(totalForPre >0){
					//剩余时间为1和0的时候进行消息推送提醒
					//TODO 消息推送
					try {
//						boolean flg = msgService.addNews(null, null, "10", "", null, 
//								curTime + "", null, null, "0", deptInfo.getDepartId() + "", 
//								deptInfo.getDepartName());
//						if(flg) {
							String newsContent = deptInfo.getDepartName() + "（单位）有" + totalForPre + "个办件即将超时办理，请尽快办理。";
							List<String> strAlias = new ArrayList<String>();
							strAlias.add(deptIds.get(i));//针对部门消息提醒
							strAlias.add(Config.ZWB);
							
							Hashtable<String, String> table = new Hashtable<String, String>();
							table.put("newsType", "10");
							table.put("content", newsContent);
							table.put("status", "0");
							PushUtil.pushNotifyToTagert(strAlias, StringUtil.getJson(table));
//						}else {
//							LogUtil.log("消息插入数据库失败");
//						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
/************************2015-11-26 feini********************************/	
			}
		}
	}

	/**
	 * 获取所有的特别程序
	 * 
	 * @return list
	 */
	public List<EbSpcProgram> getAllProgramsList() {
		List<EbSpcProgram> list = dao.findAllProgramsList();
		UserInterService uService = new UserInterService(null);

		for (int i = 0; i < list.size(); i++) {
			EbSpcProgram esp = list.get(i);

			if (esp.getBuildUserId() != null) { // 签收用户
				esp.setBuildUser(uService.findById(esp.getBuildUserId()
						.toString()));
			}

			if (esp.getUserId() != null) { // 审批用户
				esp.setUserInfo(uService.findById(esp.getUserId().toString()));
			}
		}

		return list;
	}
	
	/**
	 * 获取所有的特别程序
	 * @param page
	 * @return Page
	 */
	public Page<EbSpcProgram> getAllPrograms(int page) {
		Page<EbSpcProgram> listPage = dao.findAllPrograms(page);

		return listPage;
	}
	
	/**
	 * 根据部门id获取部门特别程序
	 * @param page
	 * @return Page
	 */
	public Page<EbSpcProgram> getProgramsByDeptId(int page,String deptId) {
		Page<EbSpcProgram> listPage = dao.findProgramsByDeptId(page,deptId);

		return listPage;
	}

	/**
	 * 根据企业名称筛选特别程序
	 * 
	 * @param bussName
	 * @return list
	 */
	public List<EbSpcProgram> getByBussNameList(String bussName) {
		List<EbSpcProgram> list = dao.findByBussNameList(bussName);
		UserInterService uService = new UserInterService(null);

		for (int i = 0; i < list.size(); i++) {
			EbSpcProgram esp = list.get(i);

			if (esp.getBuildUserId() != null) { // 签收用户
				esp.setBuildUser(uService.findById(esp.getBuildUserId()
						.toString()));
			}

			if (esp.getUserId() != null) { // 审批用户
				esp.setUserInfo(uService.findById(esp.getUserId().toString()));
			}
		}

		return list;
	}
	
	/**
	 * 根据企业名称筛选特别程序
	 * @param page
	 * @param bussName
	 * @return page
	 */
	public Page<EbSpcProgram> getByBussName(String bussName,int page) {
		Page<EbSpcProgram> listPage = dao.findByBussName(bussName,page);
		
		return listPage;
	}

	/**
	 * 根据id筛选特别程序
	 * 
	 * @param programId
	 * @return
	 */
	public EbSpcProgram getById(String programId) {
		if(programId == null || programId.equals("") || "null".equalsIgnoreCase(programId)) {
			return null;
		}
		
		EbSpcProgram esp = dao.findById(programId);
		UserInterService uService = new UserInterService(null);

		if (esp.getBuildUserId() != null) { // 签收用户
			esp.setBuildUser(uService.findById(esp.getBuildUserId().toString()));
		}

		if (esp.getUserId() != null) { // 审批用户
			esp.setUserInfo(uService.findById(esp.getUserId().toString()));
		}

		return esp;
	}
	
	/**
	 * 根据任务分发id筛选特别程序
	 * 
	 * @param distriId
	 * @return
	 */
	public EbSpcProgram getByDistriId(String distriId) {
		if(distriId == null || distriId.equals("") || "null".equalsIgnoreCase(distriId)) {
			return null;
		}
		EbSpcProgram esp = dao.findByDistriId(distriId);
		UserInterService uService = new UserInterService(null);

		if(esp != null){
			if (esp.getBuildUserId() != null) { // 新建用户
				esp.setBuildUser(uService.findById(esp.getBuildUserId().toString()));
			}
	
			if (esp.getUserId() != null) { // 审批用户
				esp.setUserInfo(uService.findById(esp.getUserId().toString()));
			}
			return esp;
		}else{
			return null;
		}
	}
	
	/**
	 * 新建特别程序
	 * @param userInfo 用户信息
	 * @param distrInfo 事项分发信息
	 * @param bussInfo 企业信息
	 * @param spcReason 特别程序原因
	 * @param delayTime 延时期限
	 * @param remedyProgram 补救方案、办理方式
	 * @param conment 备注
	 * @return boolean
	 */
	public boolean addSpcPragram(EbUserInfo userInfo, EbTaskDistribute distrInfo, EbBusiness bussInfo, 
			String spcReason, String delayTime, String remedyProgram, String conment, EbSpcProgram spc){
		
		spc.set("PROGRAM_ID", "program_seq.nextval");
		spc.setBusinessId(bussInfo.getBusinessId());//相关企业ID
		spc.setBusinessName(bussInfo.getBusinessName());//相关企业名称
		spc.setTransactionWay(remedyProgram);//补救方案、办理方式
		
		if(delayTime != null && MD5Util.isNumeric(delayTime)) {
			spc.set("time_limit", Integer.parseInt(delayTime));//办理期限
		}
		spc.setBuildTime(System.currentTimeMillis() + "");//新建日期
		spc.setAddress(bussInfo.getBussAddress());//企业地址
		spc.setContactPhone(bussInfo.getContactPhone());//联系电话、联系方式
		spc.setContactName(bussInfo.getContactName());//联系人
		spc.setReason(spcReason);//新建原因
		spc.setConment(conment);//备注
		spc.set("examine_status", Integer.parseInt("0"));//审批状态（ 0 待审批 、 1 已批准 、 2未批准）
		spc.setItemId(distrInfo.getItemId());//事项ID
		spc.setItemName(distrInfo.getItemName());//事项名称
		spc.setBuildUserId(userInfo.getUserId());//新建用户ID
		spc.setDepartId(distrInfo.getDepartId());//部门id
		spc.setDepartName(distrInfo.getDepartName());//部门名称
		spc.setDistrId(distrInfo.getDistrId());//获取分发任务Id
		
		boolean flag = false;
		try {
			flag = spc.save();
		} catch (Exception e) {
			e.printStackTrace();
			flag = spc.save();
		}
		
		return flag;
	}
	
	/**
	 * 获取所有的任务
	 * @return list
	 */
	public List<EbTask> getAllTasksList() {
		UserInterService uService = new UserInterService(null);
		List<EbTask> list = dao.findAllTasksList();
		for(int i = 0; i < list.size(); i++ ) {
			EbTask task = list.get(i);
			System.out.println("签收状态：" + task.getSignStatus());
			if(task.getUserId() != null) {	//创建人用户
				task.setBuildUser(uService.findById(task.getUserId() + ""));
			}
			if(task.getSignUserId() != null) {	//签收人用户
				task.setSignUser(uService.findById(task.getSignUserId() + ""));
			}
		}
		return list;
	}
	
	/**
	 * 获取所有的任务
	 * @param page
	 * @return Page
	 */
	public Page<EbTask> getAllTasks(int page) {
		Page<EbTask> listPage = dao.findAllTasks(page);
		return listPage;
	}
	
	public List<EbTask> getTaskBySignStatus(String status){
		return dao.findTaskBySignStatus(status);
	}
	
	/**
	 * 添加任务
	 * @param userId
	 * @param typeId
	 * @param businessName
	 * @return
	 */
	public boolean addTask(String userId,String typeId,String businessName){
		EbTask task = new EbTask(); 
		boolean result = false;
		
		BussInterService bussSer = new BussInterService();
		EbBusiness bussInfo = bussSer.findByBussNameOne(businessName);
		if(bussInfo!=null){
			String businessId = bussInfo.getBusinessId()+"";
			task.set("task_id", "task_seq.nextval");
			task.setBusinessId(new BigDecimal(businessId));
			task.setBusinessName(businessName);
			task.setTypeId(new BigDecimal(typeId));//企业类型
			task.setUserId(new BigDecimal(userId));//任务创建用户
			EbUserInfo userInfo = new UserInterService(null).findById(userId);
			if(userInfo != null) {
				task.setUserCode(userInfo.getUserAccount());
			}
			
			task.setCompStatus(new BigDecimal(0));//完成状态（0 未落户/未完成 、 1 已落户/已完成）
			task.setSignStatus(new BigDecimal(0));//签收状态（0 未签收、1 已签收、2 被退回：事项任务签收时有该状态）
			task.setAddTime(System.currentTimeMillis()+"");//添加时间
			boolean flag = false;
			try {
				flag = task.save();
			} catch (Exception e) {
				e.printStackTrace();
				flag = task.save();
			}
			
			
			if(flag){
				result = true;
			}
		}
		return result;
	}
	
	/**
	 * 根据企业名称筛选任务
	 * @param bussName
	 * @return list
	 */
	public List<EbTask> getTasksByBussNameList(String bussName) {
		UserInterService uService = new UserInterService(null);
		List<EbTask> list = dao.findTasksByBussNameList(bussName);
		for(int i = 0; i < list.size(); i++ ) {
			EbTask task = list.get(i);
			if(task.getUserId() != null) {	//创建人用户
				task.setBuildUser(uService.findById(task.getUserId() + ""));
			}
			if(task.getSignUserId() != null) {	//签收人用户
				task.setSignUser(uService.findById(task.getSignUserId() + ""));
			}
		}
		return list;
	}
	
	/**
	 * 根据企业名称筛选任务
	 * @param bussName
	 * @param page
	 * @return Page
	 */
	public Page<EbTask> getTasksByBussName(String bussName,int page) {
		Page<EbTask> listPage = dao.findTasksByBussName(bussName,page);
		return listPage;
	}
	
	/**
	 * 根据任务完成状态获取未完成任务
	 * @param page
	 * @return
	 */
	public Page<EbTask> getTasksByCompStatus0(int page){
		Page<EbTask> listPage = dao.findTasksByComStatus0(page);
		return listPage;
	}
	
	/**
	 * 根据任务签收状态获取未完成任务
	 * @param page
	 * @return
	 */
	public Page<EbTask> getTasksBySignStatus1(int page){
		Page<EbTask> listPage = dao.findTasksBySignStatus(page);
		return listPage;
	}
	
	/**
	 * 根据企业Id获取任务
	 * @param bussId
	 * @return EbTask
	 */
	public EbTask getTaskByBussId(String bussId){
		if(bussId == null || bussId.equals("") || "null".equalsIgnoreCase(bussId)) {
			return null;
		}
		return dao.findTasksByBussId(bussId);
		
	}
	
	/**
	 * 根据id筛选任务
	 * 
	 * @param taskId
	 * @return
	 */
	public EbTask getTaskById(String taskId) {
		if(taskId == null || taskId.equals("") || "null".equalsIgnoreCase(taskId)) {
			return null;
		}
		
		EbTask task = dao.findTaskById(taskId);
		UserInterService uService = new UserInterService(null);
		
		if(task!=null){
			if(task.getUserId() != null) {	//创建人用户
				task.setBuildUser(uService.findById(task.getUserId() + ""));
			}
			if(task.getSignUserId() != null) {	//签收人用户
				task.setSignUser(uService.findById(task.getSignUserId() + ""));
			}
		}

		return task;
	}
	
	/**
	 * 根据任务id获取任务清单
	 * @param taskId
	 * @return list
	 */
	public List<EbTaskDistribute> getDistribListLi(String taskId) {
		List<EbTaskDistribute> list = new ArrayList<EbTaskDistribute>();
		if(taskId == null || taskId.equals("") || "null".equalsIgnoreCase(taskId)) {
			return list;
		}
		UserInterService uService = new UserInterService(null);
		DeptInterService dService = new DeptInterService();
		ItemInterService iService = new ItemInterService();
		
		list = dao.findDtsByTaskIdList(taskId);
		for (int i = 0; i < list.size(); i++) {
			EbTaskDistribute tDistr = list.get(i);
			
			int itemType = tDistr.getItemType().intValue();
			EbItem item = null;
			if(itemType == 1) {
				item = GovNetUtil.getItemInfo(tDistr.getItemId() + "");
			}else {
				item = iService.getItemById(tDistr.getItemId() + "");
			}
			if(item != null) {
				tDistr.setItem(item);	//事项
			}
			
			String signUserId = tDistr.getSignUserId() + "";
			if(signUserId != null && !signUserId.equals("") 
					&& !signUserId.equalsIgnoreCase("null")) {
				EbUserInfo signUser = uService.findById(signUserId);
				tDistr.setSignUser(signUser);	//签收用户
			}
			
			EbDepart dept = dService.getDeptById(tDistr.getDepartId() + "");
			if(dept == null) {
				dept = GovNetUtil.getDeptInfo(tDistr.getDepartId() + "");
			}
			tDistr.setDept(dept);	//事项所属部门
			
			String distrUserId = tDistr.getUserId() + "";
			if(distrUserId != null && !distrUserId.equals("") && !distrUserId.equalsIgnoreCase("null")) {
				EbUserInfo distrUser = uService.findById(distrUserId);
				tDistr.setDistrUser(distrUser);	//分发人、创建人
			}
			String transUserId = tDistr.getTransactorId() + "";
			if(transUserId != null && !transUserId.equals("") && !transUserId.equalsIgnoreCase("null")) {
				EbUserInfo transUser = uService.findById(transUserId);
				tDistr.setTransUser(transUser);	//办理人
			}
		}
		
		return list;
	}
	
	/**
	 * 根据任务id获取任务清单
	 * @param taskId
	 * @param page
	 * @return Page
	 */
	public Page<EbTaskDistribute> getDistribList(String taskId,int page) {
		if(taskId == null || taskId.equals("") || "null".equalsIgnoreCase(taskId)) {
			return null;
		}
		
		Page<EbTaskDistribute> listPage = dao.findDtsByTaskId(taskId,page);
		
		return listPage;
	}
	
	/**
	 * 根据任务id排序，任务办理状态获取任务清单
	 * @param status
	 * @param page
	 * @return Page
	 */
	public Page<EbTaskDistribute> getDistribByStatusList(String status,int page) {
		Page<EbTaskDistribute> listPage = dao.findDtsByStatus(status,page);
		
		return listPage;
	}
	
	/**
	 * 根据任务id排序，办理部门任务办理状态获取任务清单
	 * @param status未开始和办理中，正在办理包括：已逾期，已暂停
	 * @param page
	 * @return Page
	 */
	public Page<EbTaskDistribute> getDistribByStatusAll2(String status,int page,String dept) {
		Page<EbTaskDistribute> listPage = dao.findDtsByStatusAll2(status,page,dept);
		
		return listPage;
	}
	
	/**
	 * 根据任务id排序，办理部门任务办理状态获取任务清单
	 * @param status
	 * @param page
	 * @return Page
	 */
	public Page<EbTaskDistribute> getDistribByStatusAll(String status,int page,String dept) {
		Page<EbTaskDistribute> listPage = dao.findDtsByStatusAll(status,page,dept);
		
		return listPage;
	}
	
	/**
	 * 根据任务id排序，办理部门任务获取任务清单
	 * @param dept
	 * @param page
	 * @return Page
	 */
	public Page<EbTaskDistribute> getDistribBydeptAll(int page,String dept) {
		Page<EbTaskDistribute> listPage = dao.findDtsBydeptAll(page,dept);
		
		return listPage;
	}
	
	/**
	 * 根据任务id排序，办理部门任务获取任务清单
	 * @param dept
	 * @param page
	 * @return List
	 */
	public List<EbTaskDistribute> getDistribBydeptAllli(int page,String dept) {
		List<EbTaskDistribute> listPage = dao.findDtsBydeptAllLi(dept);
		return listPage;
	}
	
	/**
	 * 根据任务id排序，办理部门和办理人任务获取任务清单
	 * @param dept
	 * @param page
	 * @return List
	 */
	public List<EbTaskDistribute> getDtsBydeptUserAllLi(int page,String dept,String transactor_id) {
		List<EbTaskDistribute> listPage = dao.findDtsBydeptUserAllLi(dept,transactor_id);
		return listPage;
	}
	
	
	/**
	 * 根据任务id排序，任务办理状态获取任务清单
	 * @param taskId
	 * @param status未开始和未签收
	 * @return list
	 */
	public List<EbTaskDistribute> getDistribByStatusLi(String status,String taskId) {
		return dao.findDtsByStatusli(status,taskId);
	}
	
	/**
	 * 根据任务id获取任务清单
	 * @param taskId
	 * @return list
	 */
	public List<EbTaskDistribute> getDistrById(String taskId) {
		return dao.findDistrTasksById(taskId);
	}
	
	/**
	 * 根据任务id排序，任务办理状态以及事项办理部门获取任务清单
	 * @param taskId
	 * @param status
	 * @param deptId
	 * @return list
	 */
	public List<EbTaskDistribute> getDistribByStatusDept(String status,String taskId,String deptId) {
		return dao.findDtsByStatusdept(status,taskId,deptId);
	}
	
	/**
	 * 根据任务办理状态获取任务Id不重复的任务清单
	 * @param status
	 * @return list
	 */
	public List<EbTaskDistribute> getDistriTIdByStatus(String status,String deptId) {
		return dao.findDtsTidByStatus(status,deptId);
	}
	
	/**
	 * 任务分发
	 * @param businessId
	 * @param departId
	 * @param itemId
	 * @param userId
	 * @param taskId
	 * @param itemType
	 * @param itemName
	 * @param departName
	 * @param distrTime
	 * @param areaType 区域类型 0 区级、1 市级
	 * @return
	 */
	public boolean distributeTask(String businessId, String departId, String itemId, String userId, String taskId, 
			String itemType, String itemName, String departName, String distrTime, int areaType){
		EbTaskDistribute taskDistri = new EbTaskDistribute();
		
		boolean result = false;
		boolean result2 = false;
		
		BussInterService bussSer = new BussInterService();
		EbBusiness buss = bussSer.getBussById(businessId);
		
		taskDistri.set("distr_id", "DISTR_SEQ.nextval");//插入数据序列号插入不了
		taskDistri.set("task_id", new BigDecimal(taskId));
		taskDistri.set("item_id", new BigDecimal(itemId));
		taskDistri.set("depart_id", new BigDecimal(departId));
		taskDistri.set("sign_status",new BigDecimal("0"));//签收状态（0 未签收、1 已签收、2 被退回：事项任务签收时有该状态）
		//任务分发人信息
		taskDistri.set("user_id", new BigDecimal(userId));
		EbUserInfo userInfo = new UserInterService(null).findById(userId);
		if(userInfo != null) {
			taskDistri.set("distr_user_code", userInfo.getUserAccount());
		}
		taskDistri.set("status", new BigDecimal("1"));
		taskDistri.set("distrib_time", distrTime);
		taskDistri.set("transaction_status",new BigDecimal("6"));
		taskDistri.set("item_type", new BigDecimal(itemType));
		taskDistri.set("ITEM_NAME", itemName);
		taskDistri.set("DEPART_NAME", departName);
//		taskDistri.set("CONTROL_SEQ", controlSeq);//区级办件号
		
		try {
			result = taskDistri.save();
		} catch (Exception e) {
			e.printStackTrace();
			result = taskDistri.save();
		}

		LogUtil.log("数据保存情况" + result);
		if (!result) {
			LogUtil.log("数据保存失败!");
		}
		//企业表分发的状态更改
		if(buss != null){
			buss.setDistributedStatus(new BigDecimal(1));
			result2 = buss.update();
		}
		
		return result&&result2;
	}
	
	/**
	 * 部门首席签收任务
	 * @param distrId 事项分发id
	 * @param itemId 事项id
	 * @param userId 用户id
	 * @param taskId 任务id
	 * @return boolean
	 */
	public boolean signTaskByDistri(String distrId, String itemId, EbUserInfo userInfo,String taskId, String time){
		EbTaskDistribute dist = dao.findDistrByTIId(taskId, itemId, distrId);
		boolean result = false;
		if(dist!=null){
			dist.set("sign_status",  new BigDecimal("1"));
			dist.set("sign_user_id", userInfo.getUserId());
			dist.setSignUserId(userInfo.getUserId());
			dist.set("transaction_status",  new BigDecimal("6"));//办理状态（0 首席已分发未开始、1 办理中、2 已逾期、3 办理完结、4 被退回 、5暂停、6首席未分发）
			dist.set("sign_time",  time);
			dist.setSignUserCode(userInfo.getUserAccount());
			
			if(dist.update()){
				result = true;
			}
		}
		return result;
	}
	
	/**
	 * 部门跟办人签收任务
	 * @param distrId 事项分发id
	 * @param itemId 事项id
	 * @param userId 用户id
	 * @param taskId 任务id
	 * @return boolean
	 */
	public boolean signTaskByDistriByG(String distrId, String itemId, EbUserInfo userInfo,String taskId, String time){
		EbTaskDistribute dist = dao.findDistrByTIId(taskId, itemId, distrId);
		boolean result = false;
		if(dist != null){
			String trans_user_id = dist.getTransactorId()+"";
			if(trans_user_id.equals(userInfo.getUserId()+"")){
				if(dist!=null){
					dist.set("transaction_status",  new BigDecimal("1"));//办理状态（0 首席已分发未开始、1 办理中、2 已逾期、3 办理完结、4 被退回 、5暂停、6首席未分发）
					dist.set("sign_time", time);
					
					if(dist.update()){
						result = true;
					}
				}
			}
		}
		return result;
	}
	
	/**
	 * 新增部门事项
	 * @param departId
	 * @param departName
	 * @param itemName
	 * @param limitTime
	 * @return itemId
	 */
	public String addItem(String departId, String departName, String itemName, String limitTime) {
		if (departId == null || departId.equals("") || departId.equalsIgnoreCase("null")) {
			return null;
		}else if(departName == null || departName.equals("") || departName.equalsIgnoreCase("null")) {
			return null;
		}else if(itemName == null || itemName.equals("") || itemName.equalsIgnoreCase("null")) {
			return null;
		}else if(limitTime == null || limitTime.equals("") || limitTime.equalsIgnoreCase("null")) {
			return null;
		}else if(!MD5Util.isNumeric(limitTime)) {
			return null;
		}else {
			EbItem item = new EbItem();
			item.set("item_id", "iteminfo_seq.nextval");
			item.setDepartId(new BigDecimal(departId));
			item.setDepartName(departName);
			item.setItemName(itemName);
			item.setTimeLimit(new BigDecimal(limitTime));
			item.setItemType(new BigDecimal("0"));
			item.setIsDelet(new BigDecimal("1"));
			item.setRefreshTime(System.currentTimeMillis() + "");
			
			boolean flag = item.save();
			if(flag) {
				return item.getItemId() + "";
			}
		}
		
		return null;
	}
	
	/**
	 * 根据任务id获取任务清单
	 * @param taskId 
	 * @param deptId
	 * @return list
	 */
	public List<EbTaskDistribute> getDistrsByDBIDList(String taskId, String deptId) {
		if(deptId == null || deptId.equals("") || "null".equalsIgnoreCase(deptId)) {
			return getDistribListLi(taskId);
		}
		UserInterService uService = new UserInterService(null);
		DeptInterService dService = new DeptInterService();
		ItemInterService iService = new ItemInterService();
		
		List<EbTaskDistribute> list = dao.findDtsByDBIDList(taskId, deptId);
		for (int i = 0; i < list.size(); i++) {
			EbTaskDistribute tDistr = list.get(i);
			EbItem item = iService.getItemById(tDistr.getItemId() + "");
			tDistr.setItem(item);	//事项
			EbUserInfo signUser = uService.findById(tDistr.getSignUserId() + "");
			tDistr.setSignUser(signUser);	//签收用户
			EbDepart dept = dService.getDeptById(tDistr.getDepartId() + "");
			tDistr.setDept(dept);	//事项所属部门
			EbUserInfo distrUser = uService.findById(tDistr.getUserId() + "");
			tDistr.setDistrUser(distrUser);	//分发人、创建人
			EbUserInfo transUser = uService.findById(tDistr.getTransactorId() + "");
			tDistr.setTransUser(transUser);	//办理人
		}
		
		return list;
	}
	
	/**
	 * 根据任务id获取任务清单
	 * @param taskId 
	 * @param deptId
	 * @return Page
	 */
	public Page<EbTaskDistribute> getDistrsByDBID(String taskId, String deptId,int page) {
		if(deptId == null || deptId.equals("") || "null".equalsIgnoreCase(deptId)) {
			return getDistribList(taskId,page);
		}
		
		Page<EbTaskDistribute> listPage = dao.findDtsByDBID(taskId, deptId, page);
		return listPage;
	}
	
	/**
	 * 根据任务Id，事项Id获取到任务分发表
	 * @param taskId
	 * @param itemId
	 * @param transactorId 办里人
	 * @return
	 */
	public EbTaskDistribute getDistrByTIUId(String taskId,String itemId,String transactorId){
		return dao.findDistrByTIUId(taskId, itemId, transactorId);
	}
	
	/**
	 * 根据任务Id，事项Id获取到任务分发表
	 * @param taskId
	 * @param itemId
	 * @param distrId
	 * @return
	 */
	public EbTaskDistribute getDistrByTIId(String taskId,String itemId, String distrId){
		return dao.findDistrByTIId(taskId, itemId, distrId);
	}
	
	/**
	 * 根据任务Id，事项Id获取到任务分发表
	 * @param taskId
	 * @param itemId
	 * @param distrId
	 * @return
	 */
	public EbTaskDistribute getDistrByTIId(String taskId,String itemId){
		return dao.findDistrByTIId(taskId, itemId);
	}
	
	/**
	 * 根据任务Id，事项Id获取到任务分发表
	 * @param distrId
	 * @return
	 */
	public EbTaskDistribute getDistrByDId(String distrId){
		if(distrId == null || distrId.equals("") || distrId.equalsIgnoreCase("null")) {
			return null;
		}
		return dao.findDistrByDId(distrId);
	}
	
	
	/**
	 * 根据任务id获取部门列表id和名称
	 * @param taskId 
	 * @return List<Map<String, String>>
	 */
	public List<DepartModel> getDeptIds(String taskId) {
		if(taskId == null || taskId.equals("") || "null".equalsIgnoreCase(taskId)) {
			return null;
		}
		List<DepartModel> depts = new ArrayList<DepartModel>();
		List<String> list = dao.findDeptIdsByTaskId(taskId);
		for (int i = 0; i < list.size(); i++) {
			DepartModel dm = new DepartModel();
			dm.setDepartId(list.get(i));
			dm.setDepartName(dao.findDeptNameById(list.get(i)));
			depts.add(dm);
		}
		
		return depts;
	}
	
	/**
	 * 根据企业类型Id添加事项配置对应表EB_TASL_LIST
	 * @param typeId 企业类型ID
	 * @param itemId 事项id
	 */
	public boolean addItemIdForType(String typeId,String itemId){
		EbTaskList taskListOld = new EbTaskList();
		EbTaskList taskList = new EbTaskList();
		boolean result = false;
		
		taskListOld = dao.findTypeConfigBytId(itemId, typeId);
		
		if(taskListOld != null){
			return result;
		}else{
			taskList.set("T_ID","task_list_seq.nextval");
			taskList.set("TYPE_ID", typeId);
			taskList.set("ITEM_ID", itemId);
			taskList.set("IS_DELET", "1");
			if(taskList.save()){
				boolean setRefreshTime = EbBusinessType.dao.findById(typeId).set("refresh_time", new Date().getTime()+"").update();
				if(setRefreshTime){
					result = true;
				}else{
					result = false;
				}
				
			}else{
				result = false;
			}
			return result;
		}
	}
	
	/**
	 * 删除企业类型信息
	 * @param t_id 企业类型配置表自增键
	 * 
	 */
	public boolean deletBsType(String itemId,String typeId){
		EbTaskList tType = dao.findTypeConfigBytId(itemId,typeId);
		boolean result = false;
		if(tType!=null){
			boolean setRefreshTime = EbBusinessType.dao.findById(typeId).set("refresh_time", new Date().getTime()+"").update();
			if(setRefreshTime){
				result = EbTaskList.dao.findById(tType.getT_id()).set("is_delet", "0").update();
			}else{
				result = false;
			}
		}
		return result;
	}
	
	/**
	 * 根据任务签收状态获取未完成任务
	 * @param page
	 * @return
	 */
	public Page<EbTask> getTasksByUId(String signUserId, int page){
		Page<EbTask> listPage = dao.findTasksBySignUId(signUserId, page);
		return listPage;
	}
	
	/**
	 * 获取单位首席相关联的事项分发列表
	 * @param status
	 * @param page
	 * @param departId
	 * @return Page
	 */
	public Page<EbTaskDistribute> getDistrByDeptIdAndStatus(int status, int page, String departId) {
		Page<EbTaskDistribute> listPage = dao.findDistrByDIdAndStatus(status, page, departId);
		return listPage;
	}
	
	/**
	 * 获取跟办人相关联的事项分发列表
	 * @param status
	 * @param page
	 * @param userId
	 * @return Page
	 */
	public Page<EbTaskDistribute> getDistrByTranIdAndStatus(String status, int page, String userId) {
		Page<EbTaskDistribute> listPage = dao.findDistrByTranIdAndStatus(status, page, userId);
		return listPage;
	}
	
	/**
	 * 获取市级服务专员相关联的事项分发列表
	 * @param status
	 * @param page
	 * @param userId
	 * @return Page
	 */
	public Page<EbTaskDistribute> getDistrByCtUIdAndStatus(String status, int page, String userId,String userdept) {
		Page<EbTaskDistribute> listPage = dao.findDistrByCtUIdAndStatus(status, page, userId,userdept);
		return listPage;
	}
	
	/**
	 * 获取所有的区级事项分发数据
	 * @return list
	 */
	public List<EbTaskDistribute> getDistrsByAreaType() {
		return dao.findDistrsByAreaType();
	}
	
	/**
	 * 根据来源状态，时间获取事项
	 * @param type 事项来源（必传）办件来源：1区级，0市级
	 * @param statu （必传） 0总数，1正在办理，2办理超时，3已办结，
	 * @param start_time 筛选时间1
	 * @param end_time 筛选时间2
	 * @return
	 */
	public List<EbTaskDistribute> getDistrsByTST(int type,int statu,long start_time,long end_time){
		return dao.findDistrsByTST(type, statu, start_time, end_time);
	}
}
