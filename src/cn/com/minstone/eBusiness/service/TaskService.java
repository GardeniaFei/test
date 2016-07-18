package cn.com.minstone.eBusiness.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.jfinal.plugin.activerecord.Page;

import cn.com.minstone.eBusiness.dao.TaskDao;
import cn.com.minstone.eBusiness.model.DepartModel;
import cn.com.minstone.eBusiness.model.EbBusinessType;
import cn.com.minstone.eBusiness.model.EbCompTask;
import cn.com.minstone.eBusiness.model.EbDepart;
import cn.com.minstone.eBusiness.model.EbItem;
import cn.com.minstone.eBusiness.model.EbSpcProgram;
import cn.com.minstone.eBusiness.model.EbTask;
import cn.com.minstone.eBusiness.model.EbTaskDistribute;
import cn.com.minstone.eBusiness.model.EbUserInfo;
import cn.com.minstone.eBusiness.model.EbTaskList;
import cn.com.minstone.eBusiness.util.GovNetUtil;

public class TaskService extends BaseService {

	private TaskDao dao;

	public TaskService() {
		this.dao = new TaskDao();
	}

	/**
	 * 获取所有的特别程序
	 * 
	 * @return list
	 */
	public List<EbSpcProgram> getAllProgramsList() {
		List<EbSpcProgram> list = dao.findAllProgramsList();
		UserService uService = new UserService(null);

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
	 * 根据企业名称筛选特别程序
	 * 
	 * @param bussName
	 * @return list
	 */
	public List<EbSpcProgram> getByBussNameList(String bussName) {
		List<EbSpcProgram> list = dao.findByBussNameList(bussName);
		UserService uService = new UserService(null);

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
		UserService uService = new UserService(null);

		if (esp.getBuildUserId() != null) { // 签收用户
			esp.setBuildUser(uService.findById(esp.getBuildUserId().toString()));
		}

		if (esp.getUserId() != null) { // 审批用户
			esp.setUserInfo(uService.findById(esp.getUserId().toString()));
		}

		return esp;
	}
	
	/**
	 * 获取所有的任务
	 * @return list
	 */
	public List<EbTask> getAllTasksList() {
		UserService uService = new UserService(null);
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
	
	/**
	 * 根据企业名称筛选任务
	 * @param bussName
	 * @return list
	 */
	public List<EbTask> getTasksByBussNameList(String bussName) {
		UserService uService = new UserService(null);
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
	 * 更具任务完成状态获取未完成任务
	 * @param page
	 * @return
	 */
	public Page<EbTask> getTasksByCompStatus0(int page){
		Page<EbTask> listPage = dao.findTasksByComStatus0(page);
		return listPage;
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
		UserService uService = new UserService(null);

		if(task.getUserId() != null) {	//创建人用户
			task.setBuildUser(uService.findById(task.getUserId() + ""));
		}
		if(task.getSignUserId() != null) {	//签收人用户
			task.setSignUser(uService.findById(task.getSignUserId() + ""));
		}

		return task;
	}
	
	/**
	 * 根据任务id获取任务清单
	 * @param taskId
	 * @return list
	 */
	public List<EbTaskDistribute> getDistribListLi(String taskId) {
		if(taskId == null || taskId.equals("") || "null".equalsIgnoreCase(taskId)) {
			return null;
		}
		UserService uService = new UserService(null);
		DepartService dService = new DepartService();
		ItemService iService = new ItemService();
		
		List<EbTaskDistribute> list = dao.findDtsByTaskIdList(taskId);
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
	 * 根据任务id获取任务清单
	 * @param taskId 
	 * @param deptId
	 * @return list
	 */
	public List<EbTaskDistribute> getDistrsByDBIDList(String taskId, String deptId) {
		if(deptId == null || deptId.equals("") || "null".equalsIgnoreCase(deptId)) {
			return getDistribListLi(taskId);
		}
		UserService uService = new UserService(null);
		DepartService dService = new DepartService();
		ItemService iService = new ItemService();
		
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
	 * @param dataSrc 数据来源
	 */
	public boolean addItemIdForType(String typeId,String itemId, int dataSrc){
		EbTaskList taskListOld = new EbTaskList();
		EbTaskList tasklsInfo = new EbTaskList();
		boolean result = false;
		
		//先判断是否有该事项
		taskListOld = dao.findTypeConfigBytId(itemId, typeId);
		
		if(taskListOld != null){
			if (taskListOld.getIsDelet().intValue() == 1) {
				return false;
			}else {
				taskListOld.set("IS_DELET", 1);
				result = taskListOld.update();
				return result;
			}
		}else{
			//如果没有该事项就添加
			tasklsInfo.set("T_ID","task_list_seq.nextval");
			tasklsInfo.set("TYPE_ID", typeId);
			tasklsInfo.set("ITEM_ID", itemId);
			tasklsInfo.set("IS_DELET", "1");
			EbItem item = null;
			if(dataSrc == 1) {
				item = GovNetUtil.getItemInfo(itemId);
				tasklsInfo.set("ITEM_TYPE", dataSrc);
			}else {
				ItemService itemService = new ItemService();
				item = itemService.getItemById(itemId);
				tasklsInfo.set("ITEM_TYPE", 0);
			}
			
			if(item != null) {
				String itemName = item.getItemName();
				String deptName = item.getDepartName();
				String deptId = item.getDepartId() + "";
				tasklsInfo.set("ITEM_NAME", itemName);
				tasklsInfo.set("DEPART_ID", new BigDecimal(deptId));
				tasklsInfo.set("DEPART_NAME", deptName);
			}
			
			tasklsInfo.set("REFRESH_TIME", System.currentTimeMillis()+"");
			if(tasklsInfo.save()){
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
	public boolean deletBsType(String itemId,String typeId,String tId){
		EbTaskList tType = dao.findTypeConfigBytId(itemId,typeId,tId);
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
	 * 获取已落户企业的已完成事项列表
	 * @param page
	 * @param pageNumb 每页显示行数
	 * @return
	 */
	public Page<EbCompTask> getCompTaskPage(int page ,int pageNumb){
		return dao.findCompItem(page, pageNumb);
	}
}
