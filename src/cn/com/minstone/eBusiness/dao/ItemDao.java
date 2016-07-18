package cn.com.minstone.eBusiness.dao;

import java.util.List;

import com.jfinal.plugin.activerecord.Page;

import cn.com.minstone.eBusiness.model.EbItem;
import cn.com.minstone.eBusiness.model.EbTaskDistribute;
import cn.com.minstone.eBusiness.model.EbTaskList;
import cn.com.minstone.eBusiness.util.PathUtil;

public class ItemDao extends BaseDao {

//	private static final String TABLE_STRING = "eb_item";
	
	private static final String selSQL = "select * from eb_item";
	
	/**
	 * 获取所有的事项
	 * @return list
	 */
	public List<EbItem> findAllItemsList() {
		return EbItem.dao.find(selSQL+" t where t.is_delet = 1");
	}
	
	/**
	 * 获取所有的事项
	 * @param page
	 * @return Page
	 */
	public Page<EbItem> findAllItems(int page) {
		String sql = " from eb_item t where t.is_delet = 1";
		Page<EbItem> itemPage = EbItem.dao
			.paginate(
				page, 
				PathUtil.MIN_PAGE_SIZE, 
				"select *", 
				sql);
		return itemPage;
	}
	
	/**
	 * 根据所属部门id筛选事项
	 * @param deptId
	 * @return list
	 */
	public List<EbItem> findByDeptIdList(String deptId) {
		if(deptId == null || "".equals(deptId) || "null".equalsIgnoreCase(deptId)) {
			return findAllItemsList();
		}else {
			return EbItem.dao.find(selSQL + " t where t.depart_id = ? and t.is_delet = 1", deptId);
		}
	}
	
	/**
	 * 根据部门Id和事项名称查找事项
	 * @param deptId
	 * @param itemName
	 * @return
	 */
	public EbItem findItemByDidName(String deptId,String itemName){
		return EbItem.dao.findFirst(selSQL + " t where t.depart_id = ? and item_name = ? and t.is_delet = 1", deptId,itemName);
	}
	
	/**
	 * 根据所属部门id筛选事项
	 * @param deptId
	 * @return Page
	 */
	public Page<EbItem> findByDeptId(String deptId,int page) {
		if(deptId == null || "".equals(deptId) || "null".equalsIgnoreCase(deptId)) {
			return findAllItems(page);
		}else {
			String sql = " from eb_item t where t.is_delet = 1 and t.depart_id = " + deptId;
			Page<EbItem> itemPage = EbItem.dao
				.paginate(
					page, 
					PathUtil.MIN_PAGE_SIZE, 
					"select *", 
					sql);
			return itemPage;
		}
	}
	
	/**
	 * 根据事项id筛选事项
	 * @param deptId
	 * @return
	 */
	public EbItem findItemById(String itemId) {
		return EbItem.dao.findFirst(selSQL + " t where t.item_id = ? and t.is_delet = 1 ", itemId);
	}
	
	
	/**
	 * 根据企业类型Id获取其对应的所有事项Id
	 * @param type_id
	 * @return list
	 */
	public List<EbTaskList> findItemByTypeIdList(String typeId){
		return EbTaskList.dao.find("select item_id from EB_TASK_LIST where type_id = ? and is_delet = ?",typeId,"1");
		
	}
	
	/**
	 * 根据企业类型Id获取其对应的所有事项Id
	 * @param type_id
	 * @return Page
	 */
	public Page<EbTaskList> findItemByTypeId(String typeId,int page){
		String sql = " from EB_TASK_LIST where type_id = "+typeId+" and is_delet = 1";
		Page<EbTaskList> itemPage = EbTaskList.dao
			.paginate(
				page, 
				PathUtil.MIN_PAGE_SIZE, 
				"select *", //item_id
				sql);
		return itemPage;
	}
	
	/**
	 * 获取所有的企业类型事项配置
	 * @return list
	 */
	public List<EbTaskList> findAllTaskList() {
		String sql = "select * from eb_task_list";
		return EbTaskList.dao.find(sql);
	}
	
	/**
	 * 获取所有还可办理的任务清单事项
	 * @return
	 */
	public List<EbTaskDistribute> findTDsEnable(String deptId) {
		if(deptId == null || "".equals(deptId) || "null".equalsIgnoreCase(deptId)) {
			return null;
		}else {
			return EbTaskDistribute.dao.find("select * from eb_task_distribute where status = 1 and depart_id = ?", deptId);
		}
	}
	
	/**
	 * 获取所有的超时办理的事项分发列表
	 * @return
	 */
	public List<EbTaskDistribute> findAllOTDistributes() {
		return EbTaskDistribute.dao.find("selelct * from eb_task_distribute where transaction_status = 2 order by task_id desc");
	}
	
	/**
	 * 获取所有的正在办理的事项分发列表
	 * @return
	 */
	public List<EbTaskDistribute> findAllTDsIndo() {
		return EbTaskDistribute.dao.find("selelct * from eb_task_distribute where transaction_status = 1 order by task_id desc");
	}
}
