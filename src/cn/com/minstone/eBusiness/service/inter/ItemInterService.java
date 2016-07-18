package cn.com.minstone.eBusiness.service.inter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.jfinal.plugin.activerecord.Page;

import cn.com.minstone.eBusiness.dao.inter.ItemInterDao;
import cn.com.minstone.eBusiness.model.EbItem;
import cn.com.minstone.eBusiness.model.EbTaskDistribute;
import cn.com.minstone.eBusiness.model.EbTaskList;
import cn.com.minstone.eBusiness.service.BaseService;
import cn.com.minstone.eBusiness.util.TimeUtil;

public class ItemInterService extends BaseService {
	
	private ItemInterDao dao;
	
	public ItemInterService() {
		this.dao = new ItemInterDao();
	}
	
	/**
	 * 获取所有的事项
	 * @return list
	 */
	public List<EbItem> getAllItemsList() {
		return dao.findAllItemsList();
	}
	
	/**
	 * 获取所有的事项
	 * @param page
	 * @return Page
	 */
	public Page<EbItem> getAllItems(int page) {
		return dao.findAllItems(page);
	}
	
	/**
	 * 根据部门id筛选事项分发列表
	 * @param deptId
	 * @return list
	 */
	public List<EbTaskList> getItemsByDtId(String deptId, String typeId) {
		if(deptId == null || deptId.equals("") || "null".equalsIgnoreCase(deptId)) {
			return null;
		}
		
		return dao.findByTaskList(deptId, typeId);
	}
	
	/**
	 * 根据部门id筛选事项
	 * @param deptId
	 * @return list
	 */
	public List<EbItem> getItemsByDeptId(String deptId) {
		if(deptId == null || deptId.equals("") || "null".equalsIgnoreCase(deptId)) {
			return null;
		}
		return dao.findByDeptIdList(deptId);
	}
	
	/**
	 * 根据部门id筛选事项
	 * @param deptId
	 * @param page
	 * @return Page
	 */
	public Page<EbItem> getItemsByDeptId(String deptId,int page) {
		if(deptId == null || deptId.equals("") || "null".equalsIgnoreCase(deptId)) {
			return dao.findAllItems(page);
		}
		return dao.findByDeptId(deptId,page);
	}
	
	/**
	 * 根据事项id筛选事项
	 * @param itemId
	 * @return
	 */
	public EbItem getItemById(String itemId) {
		if(itemId == null || itemId.equals("") || "null".equalsIgnoreCase(itemId)) {
			return null;
		}
		return dao.findItemById(itemId);
	}
	

	/**
	 * 根据企业类型ID获取事项列表
	 * @param typeId
	 * @return list
	 */
	public List<EbTaskList> findByTypeIDList(String typeId) {
		if(typeId == null || typeId.equals("") || "null".equalsIgnoreCase(typeId)) {
			return null;
		}
		return dao.findItemByTypeIdList(typeId);
	}
	
	/**
	 * 根据企业类型ID获取事项列表
	 * @param typeId
	 * @param page
	 * @return Page
	 */
	public Page<EbTaskList> findByTypeID(String typeId,int page) {
		if(typeId == null || typeId.equals("") || "null".equalsIgnoreCase(typeId)) {
			return null;
		}
		return dao.findItemByTypeId(typeId,page);
	}
	
	/**
	 * 获取所有类型事项配置
	 * @return
	 */
	public List<EbTaskList> findTaskList(){
		return dao.findAllTaskList();
	}
	
	/**
	 * 根据部门id获取所有超时办理事项
	 * @param deptId
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public List<EbTaskDistribute> getOverTimeTDs(String deptId) {
		List<EbTaskDistribute> otTDs = new ArrayList<EbTaskDistribute>();
		List<EbTaskDistribute> tds = dao.findTDsEnable(deptId);
		
		for (int i = 0; i < tds.size(); i++) {
			EbTaskDistribute etd = tds.get(i);
			long currentTime = System.currentTimeMillis();
			long signTime = new Date(etd.getSignTime()).getTime();
			
			//计算签收时间到当前时间相差的工作日间隔
			long days = TimeUtil.cacularWorkDays(signTime, currentTime) / (24 * 3600000);
			
			EbItem item = dao.findItemById(etd.getItemId() + "");
			
			long cday = days - item.getTimeLimit().longValue();
			if(cday > 0) {
				etd.setItem(item);
				etd.setOvTime(cday);
				otTDs.add(etd);
			}
		}
		
		return otTDs;
	}
	
	/**
	 * 根据部门id和办理状态获取该状态的事项分发列表
	 * @param departId
	 * @param status
	 * @return
	 */
	public List<EbTaskDistribute> getItemsByDId_Status(String departId, int status) {
		if(departId == null || departId.equals("") || departId.equalsIgnoreCase("null")) {
			return null;
		}else if (status < 0 || status > 4) {
			return null;
		}else {
			return dao.findItemsByDTID_Status(departId, status);
		}
	}
}
