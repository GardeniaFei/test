package cn.com.minstone.eBusiness.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jfinal.plugin.activerecord.Page;

import cn.com.minstone.eBusiness.dao.ItemDao;
import cn.com.minstone.eBusiness.model.EbDepart;
import cn.com.minstone.eBusiness.model.EbItem;
import cn.com.minstone.eBusiness.model.EbTaskDistribute;
import cn.com.minstone.eBusiness.model.EbTaskList;
import cn.com.minstone.eBusiness.util.GovNetUtil;
import cn.com.minstone.eBusiness.util.PushUtil;
import cn.com.minstone.eBusiness.util.TimeUtil;

public class ItemService extends BaseService {
	
	private ItemDao dao;
	
	public ItemService() {
		this.dao = new ItemDao();
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
	 * 根据部门Id和事项名称查找事项
	 * @param deptId 部门Id
	 * @param itemName 事项名称
	 * @return
	 */
	public EbItem getItemByDIdItemName(String deptId,String itemName){
		return dao.findItemByDidName(deptId,itemName);
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
	 * 删除事项
	 * @param itemId
	 * @return
	 */
	public boolean deletItemById(String itemId) {
		if(itemId == null || itemId.equals("") || "null".equalsIgnoreCase(itemId)) {
			return false;
		}
		EbItem item = dao.findItemById(itemId);
		boolean result = false;
		if(item!=null){
			boolean setRefreshTime = EbItem.dao.findById(itemId).set("refresh_time", new Date().getTime()+"").update();
			if(setRefreshTime){
				result = EbItem.dao.findById(itemId).set("is_delet", "0").update();
			}else{
				result = false;
			}
		}
		return result;
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
	 * 获取所有超时办理事项
	 * @param deptId
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public List<EbTaskDistribute> getOverTimeTDsByDeptId() {
		List<EbTaskDistribute> otTDs = new ArrayList<EbTaskDistribute>();
		List<EbTaskDistribute> tds = dao.findAllTDsIndo();	//获取所有办理中的事项任务

		long currentTime = System.currentTimeMillis();	//取得当前时间毫秒数
		for (int i = 0; i < tds.size(); i++) {
			EbTaskDistribute etd = tds.get(i);
			long signTime = new Date(etd.getSignTime()).getTime();
			
			//计算签收时间到当前时间相差的工作日间隔
			long days = TimeUtil.cacularWorkDays(signTime, currentTime) / (24 * 3600000);
			
			EbItem item = dao.findItemById(etd.getItemId() + "");	//查找事项id对应的事项
			
			long cday = days - item.getTimeLimit().longValue();		//当前相差工作日比事项办理期限的工作日大则为超时了
			if(cday > 0) {
				
				etd.setItem(item);
				etd.setOvTime(cday);
				otTDs.add(etd);
			}
		}
		
		//TODO
		Map<String, String> map = new HashMap<String, String>();
		int count = 0;
		
		//存储超时办理事项所属部门集合（去重）
		String[] strAry = new String[]{};
		for (int i = 0; i < otTDs.size(); i++) {
			EbTaskDistribute distr = otTDs.get(i);
			if(!map.containsValue(distr.getDepartId()+"")) {//判断map中是否含有该值
				map.put(i + "", distr.getDepartId()+"");
				strAry[count] = i+"";
				count += 1;
			}
		}
		
		//每个部门超时办理事项的数量
		int[] ary = new int[strAry.length];
		for (int i = 0; i < ary.length; i++) {
			ary[i] = 0;
		}
		
		//计算超时办理事项部门的 超时事项数量
		for (int i = 0; i < strAry.length; i++) {
			for (int j = 0; j < otTDs.size(); j++) {
				EbTaskDistribute distr = otTDs.get(j);
				
				if(strAry[i].equalsIgnoreCase(distr.getDepartId()+"")) {//判断map中是否含有该值
					ary[i] += 1;
				}
			}
			
			//当有事项超时时，需要推送事项办理超时提醒的消息
			try {
				String content = "{\"messageType\":1,\"content\":" + ary[i] + "}";
//				PushUtil.pushTransmissionToList(map.get(strAry[i]), content);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return otTDs;
	}
	
	/**
	 * 添加事项
	 */
	public boolean addItem(String departId, String itemName,String timeLimit,String object,
			String condition,String material,String annex,String itemFlow,String flowAnnex,
			String transaction_window,String item_charge,String item_questions,String gist_law, String departName){
		EbItem item = new EbItem();
		item.setDepartId(new BigDecimal(departId));
		item.setItemName(itemName);
		item.setTimeLimit(new BigDecimal(timeLimit));
		item.setTransactionObject(object);
		item.setNeedCondition(condition);
		item.setNeedMaterial(material);
		item.setMaterialAnnex(annex);
		item.setItemFlow(itemFlow);
		item.setFlowAnnex(flowAnnex);
		item.setTransactionWindow(transaction_window);
		item.setItemCharge(item_charge);
		item.setItemQuestions(item_questions);
		item.setGistLaw(gist_law);
		item.setItemType(new BigDecimal(0));//事项类型，来自接口数据1，来自后台0
		item.setIsDelet(new BigDecimal(1));//是否删除
		
		item.setDepartName(departName);
		
		//获取当前时间
		item.setRefreshTime(System.currentTimeMillis()+"");
		item.set("ITEM_ID","iteminfo_seq.nextval");
		
		boolean result = false;
		if(item.save()){
			result = true;
		}
		
		return result;
	}
	
	/**
	 * 修改事项
	 * @return boolean
	 */
	public boolean resetItem(String itemId,String departId, String itemName,String timeLimit,String object,
			String condition,String material,String annex,String itemFlow,String flowAnnex,
			String transaction_window,String item_charge,String item_questions,String gist_law, String departName){
		
		EbItem item = getItemById(itemId);
		
		item.setDepartId(new BigDecimal(departId));
		item.setItemName(itemName);
		item.setTimeLimit(new BigDecimal(timeLimit));
		item.setTransactionObject(object);
		item.setNeedCondition(condition);
		item.setNeedMaterial(material);
		item.setMaterialAnnex(annex);
		item.setItemFlow(itemFlow);
		item.setFlowAnnex(flowAnnex);
		item.setTransactionWindow(transaction_window);
		item.setItemCharge(item_charge);
		item.setItemQuestions(item_questions);
		item.setGistLaw(gist_law);
		
		item.setDepartName(departName);
		
		//获取当前时间
		item.setRefreshTime(System.currentTimeMillis()+"");
		
		boolean result = false;
		if(item.update()){
			result = true;
		}
		return result;
	}
	
	/**
	 * 删除事项
	 * @param itemId
	 * @return boolean
	 */
	public boolean deletItem(String itemId){
		return false;
	}
	
	/**
	 * 获取所有的超时办理事项分发列表
	 * @return
	 */
	public List<EbTaskDistribute> getAllOTDistributes() {
		List<EbTaskDistribute> list = dao.findAllOTDistributes();
		return list;
	}
}
