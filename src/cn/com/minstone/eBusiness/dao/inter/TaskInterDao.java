package cn.com.minstone.eBusiness.dao.inter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.DbPro;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.thoughtworks.xstream.converters.basic.BigDecimalConverter;

import cn.com.minstone.eBusiness.model.EbSpcProgram;
import cn.com.minstone.eBusiness.model.EbTask;
import cn.com.minstone.eBusiness.model.EbTaskList;
import cn.com.minstone.eBusiness.model.EbTaskDistribute;
import cn.com.minstone.eBusiness.util.PathUtil;

public class TaskInterDao extends BaseInterDao {

//  private static final String TABLE_STRING = "EB_SPC_PROGRAM";
	
	private static final String selSQL = "select * from EB_SPC_PROGRAM";
	
//	private static final String TABLE_TASK_LIST = "EB_TASK_LIST";
	
	/**
	 * 获取正在办理事项的去重部门ids
	 * @return
	 */
	public List<String> findDeptIds() {
		String str = "select distinct depart_id from eb_task_distribute where transaction_status = 1";
		List<Record> rs = null;
		try {
			rs = Db.find(str);//DbPro.use().find(str);
		} catch (Exception e) {
			e.printStackTrace();
		}
		List<String> aList = new ArrayList<String>();
		if (rs != null) {
			for(Record record : rs){
				aList.add(record.getBigDecimal("depart_id").toString());
			}
		}
		return aList;
	}
	
	/**
	 * 根据部门id获取部门下办理的事项分发列表
	 * @param deptId
	 * @return
	 */
	public List<EbTaskDistribute> findDtsBydeptId(String deptId){
		String sql = "select * from eb_task_distribute where depart_id = ? and transaction_status = 1 order by sign_time";
		return EbTaskDistribute.dao.find(sql, deptId);
	}
	
	/**
	 * 根据企业id获取企业对于的任务
	 * @param businessId
	 * @return
	 */
	public EbTask findTaskByBussId(String businessId) {
		return EbTask.dao.findFirst("select * from eb_task where business_id = ?", businessId);
	}
	
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
		String sql = " from EB_SPC_PROGRAM order by build_time desc";
		Page<EbSpcProgram> spcPage = EbSpcProgram.dao
			.paginate(
				page, 
				PathUtil.MIN_PAGE_SIZE, 
				"select *", 
				sql);
		return spcPage;
	}
	
	/**
	 * 获取所有的特别程序列表
	 * @return Page
	 */
	public Page<EbSpcProgram> findProgramsByDeptId(int page,String deptId) {
		String sql = " from EB_SPC_PROGRAM s where s.depart_id=" + deptId +
				" order by s.build_time desc";
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
		if(bussName == null || "".equals(bussName) || "null".equalsIgnoreCase(bussName)) {
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
		if(bussName == null || "".equals(bussName) || "null".equalsIgnoreCase(bussName)) {
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
	 * 根据任务分发id筛选特别程序
	 * @param distriId
	 * @return Object
	 */
	public EbSpcProgram findByDistriId(String distriId) {
		return EbSpcProgram.dao.findFirst(selSQL + " s where s.distr_id = ?", distriId);
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
	
	public List<EbTask> findTaskBySignStatus(String status){
		String sql = "select * from eb_task where sign_status = ? order by task_id";
		return EbTask.dao.find(sql,status);
	}
	
	/**
	 * 根据企业名称获取任务列表
	 * @param bussName 
	 * @param page
	 * @return Page
	 */
	public Page<EbTask> findTasksByBussName(String bussName,int page) {
		if(bussName == null || "".equals(bussName) || "null".equalsIgnoreCase(bussName)) {
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
	 * 根据未完成状态和已签收获取任务列表
	 * @param page
	 * @return Page
	 */
	public Page<EbTask> findTasksBySignStatus(int page){
		String sql = " from eb_task t where t.comp_status = 0 and t.sign_status = 1 order by t.sign_time desc";
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
		if(bussName == null || "".equals(bussName) || "null".equalsIgnoreCase(bussName)) {
			return findAllTasksList();
		}
		return EbTask.dao.find("select * from eb_task t where t.business_name like '%" + bussName + "%' order by t.task_id");
	}
	
	/**
	 * 根据企业ID获取任务列表
	 * @param bussId
	 * @return EbTask
	 */
	public EbTask findTasksByBussId(String bussId) {
		return EbTask.dao.findFirst("select * from eb_task t where t.business_id = ?", bussId);
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
		String sql = "select * from (select d.*, decode(transaction_status, 6, 1, 0, 2, 1, 3, 3, 4, 4, 5, 5, 6) order_no from eb_task_distribute d where d.transaction_status in (6, 0, 1, 3, 4, 5, 2) and d.task_id = ? order by transaction_status) order by order_no asc, transaction_time desc";
//		return EbTaskDistribute.dao.find("select * from eb_task_distribute d where d.transaction_status in(6,0,1,3,4,5,2) and d.task_id = ? order by transaction_status", taskId);
		return EbTaskDistribute.dao.find(sql, taskId);
	}
	
	/**
	 * 根据办理人Id ，任务id，事项id获取任务分类
	 * @param taskId
	 * @param itemId
	 * @param transactorId
	 * @return
	 */
	public EbTaskDistribute findDistrByTIUId(String taskId,String itemId,String transactorId){
		String sql = "select * from eb_task_distribute d where d.task_id = ? and d.item_id = ? and d.transactor_id = ? and d.status = 1";
		return EbTaskDistribute.dao.findFirst(sql, taskId,itemId,transactorId);
	}
	
	/**
	 * 根据任务id，事项id、任务分发id获取任务分发类
	 * @param taskId
	 * @param itemId
	 * @param distrId
	 * @return
	 */
	public EbTaskDistribute findDistrByTIId(String taskId,String itemId, String distrId){
		String sql = "select * from eb_task_distribute d where d.task_id = ? and d.item_id = ? "
				+ "and d.status = 1 and d.distr_id = ?";
		return EbTaskDistribute.dao.findFirst(sql, taskId,itemId, distrId);
	}
	
	/**
	 * 根据任务id，事项id、获取任务分发类
	 * @param taskId
	 * @param itemId
	 * @param distrId
	 * @return
	 */
	public EbTaskDistribute findDistrByTIId(String taskId,String itemId){
		String sql = "select * from eb_task_distribute d where d.task_id = ? and d.item_id = ? "
				+ "and d.status = 1";
		return EbTaskDistribute.dao.findFirst(sql, taskId,itemId);
	}
	
	/**
	 * 根据任务id，事项id、任务分发id获取任务分发类
	 * @param distrId
	 * @return
	 */
	public EbTaskDistribute findDistrByDId(String distrId){
		String sql = "select * from eb_task_distribute d where d.distr_id = ?";
		return EbTaskDistribute.dao.findFirst(sql, distrId);
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
	
	public Page<EbTaskDistribute> findDtsByStatus(String status,int page){
		String sql = " from eb_task_distribute d where d.transaction_status = "+ status +" order by task_id";
		Page<EbTaskDistribute> taskPage = EbTaskDistribute.dao
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
//		String sql = " from eb_task t where t.comp_status = 0 order by t.task_id desc";
		String sql = " from eb_task t where t.comp_status = 0 and (t.sign_status = 0 or t.sign_status = 1) " + 
				"order by t.sign_time desc, t.add_time desc";
		
		Page<EbTask> taskPage = EbTask.dao
			.paginate(
				page, 
				PathUtil.MIN_PAGE_SIZE, 
				"select *", 
				sql);
		return taskPage;
	}
	
	/**
	 * 未开始和办理中的部门的任务清单
	 * @param status正在办理包括：已逾期，已暂停
	 * @param page
	 * @param deptId
	 * @return
	 */
	public Page<EbTaskDistribute> findDtsByStatusAll2(String status,int page,String deptId){
//		String sql = " from eb_task_distribute d where d.transaction_status IN (0,1) and d.depart_id = " +deptId+
//				" order by task_id";
		String sql = " from EB_TASK_DISTRIBUTE t where t.transaction_status IN(0, 1, 2, 5) and t.depart_id = " + deptId + 
				" order by t.sign_time desc, t.distrib_time desc";//t.sign_time nulls last将空值放在最后
		
		Page<EbTaskDistribute> taskPage = EbTaskDistribute.dao
			.paginate(
				page, 
				PathUtil.MIN_PAGE_SIZE, 
				"select *", 
				sql);
		return taskPage;
	}
	
	/**
	 * 根据办理状态的部门的任务清单
	 * @param status
	 * @param page
	 * @param deptId
	 * @return
	 */
	public Page<EbTaskDistribute> findDtsByStatusAll(String status,int page,String deptId){
		String sql = " from eb_task_distribute d where d.transaction_status = "+ status +" and d.depart_id = " +deptId+" order by task_id";
		Page<EbTaskDistribute> taskPage = EbTaskDistribute.dao
			.paginate(
				page, 
				PathUtil.MIN_PAGE_SIZE, 
				"select *", 
				sql);
		return taskPage;
	}
	
	/**
	 * 根据部门id筛选任务清单的企业列表
	 * @param page
	 * @param deptId
	 * @return
	 */
	public Page<EbTaskDistribute> findDtsBydeptAll(int page,String deptId){
		String sql = " from eb_task_distribute where depart_id = " +deptId+" order by task_id";
		
		Page<EbTaskDistribute> taskPage = EbTaskDistribute.dao
			.paginate(
				page, 
				PathUtil.MIN_PAGE_SIZE, 
				"select distinct task_id", 
				sql);
		return taskPage;
	}
	
	/**
	 * 根据部门id筛选任务清单的企业列表
	 * @param page
	 * @param deptId
	 * @return
	 */
	public List<EbTaskDistribute> findDtsBydeptAllLi(String deptId){
		String sql = "select distinct task_id from eb_task_distribute where depart_id = " +deptId+" order by task_id";
		return EbTaskDistribute.dao.find(sql);
	}
	
	/**
	 * 根据部门id和办理人id筛选任务清单的企业列表
	 * @param page
	 * @param deptId
	 * @return
	 */
	public List<EbTaskDistribute> findDtsBydeptUserAllLi(String deptId,String transactor_id){
		String sql = "select distinct task_id from eb_task_distribute where depart_id = ? and transactor_id =? order by task_id";
		return EbTaskDistribute.dao.find(sql,deptId,transactor_id);
	}
	
	/**
	 * 根据任务id排序，任务办理状态获取任务清单
	 * @param taskId
	 * @param status
	 * @return list
	 */
	public List<EbTaskDistribute> findDtsByStatusli(String status,String taskId){
		return EbTaskDistribute.dao.find("select *from eb_task_distribute d where d.transaction_status IN (0,1,2,5) and d.task_id = ? order by d.task_id",taskId);
	}

	/**
	 * 根据任务id获取任务清单
	 * @param taskId
	 * @return list
	 */
	public List<EbTaskDistribute> findDistrTasksById(String taskId){
		return EbTaskDistribute.dao.find("select * from eb_task_distribute d where d.task_id = ? "
				+ "and d.transaction_status != 4 order by distrib_time desc", taskId);
	}
	/**
	 * 根据任务id排序，任务办理状态以及事项办理部门获取任务清单
	 * @param taskId
	 * @param status
	 * @param deptId
	 * @return list
	 */
	public List<EbTaskDistribute> findDtsByStatusdept(String status,String taskId,String deptId){
		return EbTaskDistribute.dao.find("select *from eb_task_distribute d where d.transaction_status = ? and d.task_id = ? and depart_id = ? order by task_id",status,taskId,deptId);
	}
	
	/**
	 * 根据任务签收状态获取任务Id不重复的任务清单
	 * @param status签收状态（0 未签收、1 已签收、2 被退回：事项任务签收时有该状态）
	 * @return list
	 */
	public List<EbTaskDistribute> findDtsTidByStatus(String status,String deptId){
		return EbTaskDistribute.dao.find("select distinct task_id from eb_task_distribute where sign_status = ? and depart_id = ? order by task_id",status,deptId);
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
		return Db.findFirst("select depart_name from eb_depart where depart_id = ? order by depart_id", 
				deptId).getStr("depart_name");
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
	 * 根据任务签收用户id获取任务列表
	 * @param signUserId
	 * @param page
	 * @return Page
	 */
	public Page<EbTask> findTasksBySignUId(String signUserId, int page){
		String sql = " from eb_task t where t.sign_user_id = " + signUserId + " order by t.sign_time desc";
		Page<EbTask> taskPage = EbTask.dao
			.paginate(
				page, 
				PathUtil.MIN_PAGE_SIZE, 
				"select *", 
				sql);
		return taskPage;
	}
	
	/**
	 * 获取单位首席相关联的事项分发列表
	 * @param status
	 * @param page
	 * @param departId
	 * @return
	 */
	public Page<EbTaskDistribute> findDistrByDIdAndStatus(int status, int page, String departId){
		String sql = " from eb_task_distribute d where d.transaction_status = " + status + 
				" and d.depart_id = " + departId + " order by d.distrib_time desc";
		Page<EbTaskDistribute> taskPage = EbTaskDistribute.dao
			.paginate(
				page, 
				PathUtil.MIN_PAGE_SIZE, 
				"select *", 
				sql);
		return taskPage;
	}
	
	/**
	 * 获取跟办人相关联的事项分发列表
	 * @param status
	 * @param page
	 * @param userId
	 * @return
	 */
	public Page<EbTaskDistribute> findDistrByTranIdAndStatus(String status, int page, String userId){
		String sql = "";
		if(status.equals("1")){
			//正在办理中的状态，包含未签收的
			sql = " from eb_task_distribute d where d.transaction_status in (0,1)"+  
					" and d.transactor_id = " + userId + " order by d.distrib_time desc";
		}else{
		    sql = " from eb_task_distribute d where d.transaction_status = " + status + 
					" and d.transactor_id = " + userId + " order by d.distrib_time desc";
		}
		
		Page<EbTaskDistribute> taskPage = EbTaskDistribute.dao
			.paginate(
				page, 
				PathUtil.MIN_PAGE_SIZE, 
				"select *", 
				sql);
		return taskPage;
	}
	
	/**
	 * 获取市级服务专员相关联的事项分发列表（可以查看市级所有事项）
	 * @param status（6、1）
	 * @param page
	 * @param userId
	 * @return
	 */
	public Page<EbTaskDistribute> findDistrByCtUIdAndStatus(String status, int page, String userId,String userdept){
//		String sql = " from eb_task_distribute d where d.transaction_status = " + status + 
//				" and d.sign_user_id = " + userId + " or d.user_id = " + userId + " order by d.distrib_time desc";
		BigDecimal deptId = new BigDecimal(userdept);
		String sql = " from eb_task_distribute d where d.transaction_status in (6,1,0) "+ 
				" and d.depart_id = " + deptId + " and item_type = 0 order by d.distrib_time desc";
		Page<EbTaskDistribute> taskPage = EbTaskDistribute.dao
			.paginate(
				page, 
				PathUtil.MIN_PAGE_SIZE, 
				"select *", 
				sql);
		return taskPage;
	}
	
	/**
	 * 根据所有区级事项分发数据
	 * @return list
	 */
	public List<EbTaskDistribute> findDistrsByAreaType(){
		return EbTaskDistribute.dao.find("select * from eb_task_distribute where transaction_status != ? and item_type = ? order by task_id", 3, 1);
	}
	
	/**
	 * 根据来源状态，时间获取事项
	 * @param type 事项来源（必传）办件来源：1区级，0市级
	 * @param statu （必传） 0总数，1正在办理，2办理超时，3已办结，
	 * @param start_time 筛选时间1
	 * @param end_time 筛选时间2
	 * @return
	 */
	public List<EbTaskDistribute> findDistrsByTST(int type,int statu,long start_time,long end_time){
		String sql = "select * from eb_task_distribute where item_type = ? ";
		if(statu == 1){
			sql += " and transaction_status in (0,1,6) ";
		}else if(statu == 2){
			sql += " and transaction_status in (2) ";
		}else if(statu == 3){
			sql += " and transaction_status in (3) ";
		}else{
			sql += " and transaction_status in (0,1,2,3,6) ";
		}
		
		if(start_time >0 && end_time>0){
			sql += " and distrib_time >= "+ start_time + " and distrib_time <= "+ end_time;
		}
		return EbTaskDistribute.dao.find(sql,type);
	}
}
