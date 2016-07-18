package cn.com.minstone.eBusiness.dao;

import java.util.ArrayList;
import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import cn.com.minstone.eBusiness.model.EbCompTask;
import cn.com.minstone.eBusiness.model.EbDepart;
import cn.com.minstone.eBusiness.model.EbDeptEvainfo;
import cn.com.minstone.eBusiness.model.EbSpcProgram;
import cn.com.minstone.eBusiness.model.EbTask;
import cn.com.minstone.eBusiness.model.EbTaskList;
import cn.com.minstone.eBusiness.model.EbTaskDistribute;
import cn.com.minstone.eBusiness.util.PathUtil;
import cn.com.minstone.eBusiness.util.TimeUtil;

public class TaskDao extends BaseDao {

//  private static final String TABLE_STRING = "EB_SPC_PROGRAM";
	
	private static final String selSQL = "select * from EB_SPC_PROGRAM";
	
	private static final String TABLE_TASK_LIST = "EB_TASK_LIST";
	
	/**
	 * 获取所有的特别程序列表
	 * @return list
	 */
	public List<EbSpcProgram> findAllProgramsList() {
		return EbSpcProgram.dao.find(selSQL + " order by program_id");
	}
	
	/**
	 * 获取所有的特别程序列表
	 * @return Page
	 */
	public Page<EbSpcProgram> findAllPrograms(int page) {
		String sql = " from EB_SPC_PROGRAM order by program_id";
		Page<EbSpcProgram> spcPage = EbSpcProgram.dao
			.paginate(
				page, 
				PathUtil.MIN_PAGE_SIZE, 
				"select *", 
				sql);
		return spcPage;
	}
	
	/**
	 * 根据企业名称获取特别程序列表 
	 * select * from EB_SPC_PROGRAM t 
	 * 		where t.business_name like '%电商%' order by program_id;
	 * @param bussName	企业名称
	 * @return list 
	 */
	public List<EbSpcProgram> findByBussNameList(String bussName) {
		if(bussName == null || "".equals(bussName)) {
			return EbSpcProgram.dao.find(selSQL);
		}
		return EbSpcProgram.dao.find(selSQL + " s where s.business_name "
				+ "like '%" + bussName + "%' order by program_id");
	}
	
	/**
	 * 根据企业名称获取特别程序列表 
	 * select * from EB_SPC_PROGRAM t 
	 * 		where t.business_name like '%电商%' order by program_id;
	 * @param bussName	企业名称
	 * @return Page 
	 */
	public Page<EbSpcProgram> findByBussName(String bussName,int page) {
		if(bussName == null || "".equals(bussName)) {
			return findAllPrograms(page);
		}
		String sql = " from EB_SPC_PROGRAM s where s.business_name like '%" + bussName + "%' order by program_id";
		Page<EbSpcProgram> spcPage = EbSpcProgram.dao
			.paginate(
				page, 
				PathUtil.MIN_PAGE_SIZE, 
				"select *", 
				sql);
		return spcPage;
	}

	/**
	 * 根据id筛选特别程序
	 * @param programId
	 * @return Object
	 */
	public EbSpcProgram findById(String programId) {
		return EbSpcProgram.dao.findFirst(selSQL + " s where s.program_id = ?", programId);
	}
	
	/**
	 * 获取所有的任务
	 * @return list
	 */
	public List<EbTask> findAllTasksList() {
		return EbTask.dao.find("select * from eb_task order by task_id");
	}
	
	/**
	 * 获取所有的任务
	 * @param page
	 * @return Page
	 */
	public Page<EbTask> findAllTasks(int page) {
		String sql = " from eb_task order by task_id";
		Page<EbTask> taskPage = EbTask.dao
			.paginate(
				page, 
				PathUtil.MIN_PAGE_SIZE, 
				"select *", 
				sql);
		return taskPage;
	}
	
	/**
	 * 根据企业名称获取任务列表
	 * @param bussName 
	 * @param page
	 * @return Page
	 */
	public Page<EbTask> findTasksByBussName(String bussName,int page) {
		if(bussName == null || "".equals(bussName)) {
			return findAllTasks(page);
		}
		String sql = " from eb_task t where t.business_name like '%" + bussName + "%' order by task_id";
		Page<EbTask> taskPage = EbTask.dao
			.paginate(
				page, 
				PathUtil.MIN_PAGE_SIZE, 
				"select *", 
				sql);
		return taskPage;
	}
	
	/**
	 * 根据未完成状态获取任务列表
	 * @param page
	 * @return Page
	 */
	public Page<EbTask> findTasksByComStatus0(int page) {
		String sql = " from eb_task t where t.comp_status = 0  order by sign_status";
		Page<EbTask> taskPage = EbTask.dao
			.paginate(
				page, 
				PathUtil.MIN_PAGE_SIZE, 
				"select *", 
				sql);
		return taskPage;
	}
	
	/**
	 * 根据企业名称获取任务列表
	 * @param bussName 
	 * @return list
	 */
	public List<EbTask> findTasksByBussNameList(String bussName) {
		if(bussName == null || "".equals(bussName)) {
			return findAllTasksList();
		}
		return EbTask.dao.find("select * from eb_task t where t.business_name like '%" + bussName + "%' order by task_id");
	}
	
	/**
	 * 根据任务id筛选任务
	 * @param taskId 
	 * @return Object
	 */
	public EbTask findTaskById(String taskId) {
		return EbTask.dao.findFirst("select * from eb_task t where t.task_id = ?", taskId);
	}
	
	/**
	 * 根据任务id获取任务清单
	 * @param taskId
	 * @return list
	 */
	public List<EbTaskDistribute> findDtsByTaskIdList(String taskId) {
		return EbTaskDistribute.dao.find("select * from eb_task_distribute d where d.task_id = ? order by item_id", taskId);
	}
	
	/**
	 * 根据任务id获取任务清单
	 * @param taskId
	 * @param page
	 * @return Page
	 */
	public Page<EbTaskDistribute> findDtsByTaskId(String taskId,int page) {
		String sql = " from eb_task_distribute d where d.task_id = "+ taskId +" order by item_id";
		Page<EbTaskDistribute> taskPage = EbTaskDistribute.dao
			.paginate(
				page, 
				PathUtil.MIN_PAGE_SIZE, 
				"select *", 
				sql);
		return taskPage;
	}
	
	/**
	 * 根据任务id和事项所属部门获取任务清单
	 * @param taskId
	 * @param deptId
	 * @return list
	 */
	public List<EbTaskDistribute> findDtsByDBIDList(String taskId, String deptId) {
		return EbTaskDistribute.dao.find("select * from eb_task_distribute d where d.task_id = ? and d.depart_id = ? order by item_id", taskId, deptId);
	}
	
	/**
	 * 根据任务id和事项所属部门获取任务清单
	 * @param taskId
	 * @param deptId
	 * @param page
	 * @return Page
	 */
	public Page<EbTaskDistribute> findDtsByDBID(String taskId, String deptId,int page) {
		String sql = " from eb_task_distribute d where d.task_id = "+ taskId +" and d.depart_id = "+ deptId +" order by item_id";
		Page<EbTaskDistribute> taskPage = EbTaskDistribute.dao
			.paginate(
				page, 
				PathUtil.MIN_PAGE_SIZE, 
				"select *", 
				sql);
		return taskPage;
	}
	
	/**
	 * 根据任务id获取部门ids
	 * @param taskId
	 * @return
	 */
	public List<String> findDeptIdsByTaskId(String taskId) {
		String str = "select distinct depart_id from eb_task_distribute where task_id = ? order by depart_id";
		List<Record> rs = Db.find(str, taskId);
		List<String> aList = new ArrayList<String>();
		for(Record record : rs){
			aList.add(record.getBigDecimal("depart_id").toString());
		}
		return aList;
	}
	
	/**
	 * 根据部门id获取部门名称
	 * @param deptId 
	 * @return String
	 */
	public String findDeptNameById(String deptId) {
		if(deptId == null || "".equals(deptId) || "null".equalsIgnoreCase(deptId)) {
			return null;
		}
		Record dept = Db.findFirst("select depart_name from eb_depart where depart_id = ? order by depart_id", deptId);
		
		if(dept != null){
			return dept.getStr("depart_name");
		}else{
			return null;
		}
//		return Db.findFirst("select depart_name from eb_depart where depart_id = ? order by depart_id", 
//				deptId).getStr("depart_name");
		
	}
	
	/**
	 * 根据itemId和typeId查找
	 * @param typeId 企业类型
	 * @param itemId 事项类型
	 */
	public EbTaskList findTypeConfigBytId(String itemId,String typeId,String tId){
		return EbTaskList.dao.findFirst("select * from EB_TASK_LIST where type_id = ? and item_id = ? and t_id = ?" ,typeId  , itemId, tId);
	}
	/**
	 * 根据itemId和typeId查找
	 * @param typeId 企业类型
	 * @param itemId 事项类型
	 */
	public EbTaskList findTypeConfigBytId(String itemId,String typeId){
		return EbTaskList.dao.findFirst("select * from EB_TASK_LIST where type_id = ? and item_id = ?" ,typeId  , itemId);
	}
	
	/**
	 * 已完成事项列表分页
	 * @param page
	 * @param pageNumb每页显示多少行数据
	 * @return
	 */
	public Page<EbCompTask> findCompItem(int page ,int pageNumb){
		String sql = "select t.business_name,t.business_id,t.task_id,";
		sql += "d.item_name,d.item_id,d.transaction_time,d.distr_id,d.transaction_status ";
		
		String sql1 = " from Eb_Task t, eb_task_distribute d";
		sql1 += " where t.task_id = d.task_id and t.comp_status = 1 and d.transaction_status = 3";
		sql1 += "order by d.transaction_time DESC";
		
		Page<Record> recordPage = Db
				.paginate(
						page, 
						pageNumb, 
						sql, 
						sql1);
		List<Record> records =recordPage.getList();
		List<EbCompTask> taskLi = new ArrayList<EbCompTask>();
		if(records.size()>0)
		for(int i = 0; i < records.size(); i ++){
			Record record = records.get(i);
			EbCompTask task = new EbCompTask();
			task.setBussName(record.get("business_name")+""); 
			task.setBussId(record.get("business_id")+"");
			task.setDistr_id(record.get("distr_id")+"");
			task.setItem_id(record.get("item_id")+"");
			task.setItem_name(record.get("item_name")+"");
			task.setTask_id(record.get("task_id")+"");
			task.setTrans_status(record.get("transaction_status")+"");
			
			String times = TimeUtil.getTimeStyle(record.get("transaction_time")+"", "yyyy-MM-dd");
			task.setComp_time(times);
			taskLi.add(task);
		}
		
		Page<EbCompTask>  comtaskPage = new Page<EbCompTask>(taskLi, page, pageNumb, recordPage.getTotalPage(), recordPage.getTotalRow());
		return comtaskPage;
	}
}
