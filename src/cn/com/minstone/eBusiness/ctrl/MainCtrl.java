package cn.com.minstone.eBusiness.ctrl;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;





import cn.com.minstone.eBusiness.AdminLoginInter;
import cn.com.minstone.eBusiness.model.DepartModel;
import cn.com.minstone.eBusiness.model.EbBusiness;
import cn.com.minstone.eBusiness.model.EbBusinessType;
import cn.com.minstone.eBusiness.model.EbBussItem;
import cn.com.minstone.eBusiness.model.EbDepart;
import cn.com.minstone.eBusiness.model.EbDeptEvainfo;
import cn.com.minstone.eBusiness.model.EbEvaluate;
import cn.com.minstone.eBusiness.model.EbFileImg;
import cn.com.minstone.eBusiness.model.EbItem;
import cn.com.minstone.eBusiness.model.EbMessage;
import cn.com.minstone.eBusiness.model.EbNews;
import cn.com.minstone.eBusiness.model.EbNotice;
import cn.com.minstone.eBusiness.model.EbTask;
import cn.com.minstone.eBusiness.model.EbTaskDistribute;
import cn.com.minstone.eBusiness.model.EbTaskList;
import cn.com.minstone.eBusiness.model.EbUserInfo;
import cn.com.minstone.eBusiness.service.BusinessService;
import cn.com.minstone.eBusiness.service.DepartService;
import cn.com.minstone.eBusiness.service.EvaluateService;
import cn.com.minstone.eBusiness.service.ImgService;
import cn.com.minstone.eBusiness.service.ItemService;
import cn.com.minstone.eBusiness.service.MessageService;
import cn.com.minstone.eBusiness.service.TaskService;
import cn.com.minstone.eBusiness.service.UserService;
import cn.com.minstone.eBusiness.service.inter.BussInterService;
import cn.com.minstone.eBusiness.service.inter.BussItemService;
import cn.com.minstone.eBusiness.service.inter.DeptInterService;
import cn.com.minstone.eBusiness.service.inter.ItemInterService;
import cn.com.minstone.eBusiness.service.inter.MsgInterService;
import cn.com.minstone.eBusiness.service.inter.TaskInterService;
import cn.com.minstone.eBusiness.service.inter.UserInterService;
import cn.com.minstone.eBusiness.util.Config;
import cn.com.minstone.eBusiness.util.GovNetUtil;
import cn.com.minstone.eBusiness.util.LogUtil;
import cn.com.minstone.eBusiness.util.MD5Util;
import cn.com.minstone.eBusiness.util.NullUtil;
import cn.com.minstone.eBusiness.util.PathUtil;
import cn.com.minstone.eBusiness.util.PushUtil;
import cn.com.minstone.eBusiness.util.StringUtil;
import cn.com.minstone.eBusiness.util.TimeUtil;
import cn.com.minstone.eBusiness.model.EbSpcProgram;

import com.jfinal.aop.Before;
import com.jfinal.aop.Clear;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.render.JsonRender;
import com.mchange.v1.util.ArrayUtils;

@Before(AdminLoginInter.class)
public class MainCtrl extends Controller {
	
	/**
	 * 用户管理
	 */
	public void index(){
//		forwardAction();
		redirect("/user/initUsers");
	}
	/**
	 * 企业管理
	 */
	@Clear
	public void companyManage(){
		//获取企业筛选列表
		BusinessService bService = new BusinessService();
		List<EbBusinessType> typeList = bService.getAllBsTypeList();
		this.setAttr("typeList", typeList);
		
		//获取所有企业列表
		int page = this.getParaToInt("page", 1);

		Page<EbBusiness> bussListPage = bService.getAllBussInfo(page);
		List<EbBusiness> bussList = bussListPage.getList();
		
		//获取page前代码
//		List<EbBusiness> bussList = bService.getAllBussInfoList();
		for(int i = 0; i < bussList.size(); i++) {
			EbBusiness info = bussList.get(i);
			EbBusinessType ebType = bService.getBsTypeById(info.getTypeId() + "");
			info.setBussType(ebType);
			
			if(info.getSettleTime() != null){
			    String time = info.getSettleTime();
			    
			    long oldTime = Long.parseLong(time);
			    Date newTime = new Date(oldTime);
			    
			    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			    time = df.format(newTime);
			    
			    info.setSettleTime(time);
			   }
			
			if(info.getSignTime() != null){
			    String time = info.getSignTime();
			    
			    long oldTime = Long.parseLong(time);
			    Date newTime = new Date(oldTime);
			    
			    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			    time = df.format(newTime);
			    
			    info.setSignTime(time);
			   }
			
		}
		
		this.setAttr("bussPage", bussListPage);
		this.setAttr("bussList", bussList);
		
		render(PathUtil.getIndexPath("companyManage.html"));
	}
	
	/**
	 * 筛选企业
	 */
	public void filterBuss() {
		String typeId = getPara("bussType");
		String bussName = getPara("businessName");
		String contactName = getPara("contactName");
		String contactPhone = getPara("contactPhone");
		BusinessService bService = new BusinessService();
	    
		List<EbBusinessType> typeList = bService.getAllBsTypeList();
		this.setAttr("typeList", typeList);
		
		//获取企业Page
		int page = this.getParaToInt("page", 1);

		Page<EbBusiness> bussListPage = bService.filterBuss(typeId, bussName, contactName, contactPhone, page);
		List<EbBusiness> bussList = bussListPage.getList();
//		List<EbBusiness> bussList = bService.filterBuss(typeId, bussName);
		for(int i = 0; i < bussList.size(); i++) {
			EbBusiness info = bussList.get(i);
			EbBusinessType ebType = bService.getBsTypeById(info.getTypeId() + "");
			info.setBussType(ebType);
			
			if(info.getSettleTime() != null){
			    String time = info.getSettleTime();
			    
			    long oldTime = Long.parseLong(time);
			    Date newTime = new Date(oldTime);
			    
			    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			    time = df.format(newTime);
			    
			    info.setSettleTime(time);
			   }
			
			if(info.getSignTime() != null){
			    String time = info.getSignTime();
			    
			    long oldTime = Long.parseLong(time);
			    Date newTime = new Date(oldTime);
			    
			    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			    time = df.format(newTime);
			    
			    info.setSignTime(time);
			   }
		}
		
		this.setAttr("bussPage", bussListPage);
		this.setAttr("bussList", bussList);
		this.setAttr("typeId", typeId);
		this.setAttr("bussName", bussName);
		this.setAttr("contactName", contactName);
		this.setAttr("contactPhone", contactPhone);
		render(PathUtil.getIndexPath("companyManage.html"));
	}
	
	/**
	 * 查看企业简介
	 */
	public void toIntroView() {
		String bussId = getPara("businessId");
		BusinessService bService = new BusinessService();
		EbBusiness info = bService.getBussById(bussId);
		info.setBussType(bService.getBsTypeById(info.getTypeId() + ""));
		
		UserService uService = new UserService(null);
		if(info.getUserId() != null && info.getUserId().intValue() != 0) {
			info.setSignUser(uService.findById(info.getUserId() + ""));
		}
		
		this.setAttr("bussInfo", info);
		render(PathUtil.getIndexPath("companyInfo.html"));
	}
	
	/**
	 * 查询企业所新增的事项
	 */
	public void bItemList(){
		String bussId = getPara("businessId");
		int page = getParaToInt("page",1);
		BussItemService bitemSer = new BussItemService();
		Page<EbBussItem> bItemPage = bitemSer.findBItemPage(bussId, page);
		List<EbBussItem> bItems = bItemPage.getList();
		if(bItems.size()>0){
			for(int i=0;i<bItems.size();i++){
				EbBussItem bitem = bItems.get(i);
				String newTime = TimeUtil.getTimeStyle(bitem.getCreat_time(),"yyyy-MM-dd");
				bitem.setCreat_time(newTime);
			}
		}
		
		this.setAttr("bItemPage", bItemPage);
		this.setAttr("bItems", bItems);
		
		BusinessService bService = new BusinessService();
		EbBusiness info = bService.getBussById(bussId);
		if(info != null){
			this.setAttr("bussName", info.getBusinessName());
		}
		this.setAttr("bussId", bussId);
		
		render(PathUtil.getIndexPath("bitemList.html"));
	}
	
	/**
	 * 事项管理
	 */
	public void applyManage(){
		//使用page
		int page = this.getParaToInt("page", 1);
		int dataSrc = this.getParaToInt("dataSource", 1);	//数据来源 1 审批接口、 2 本地库
		String deptId = this.getPara("departId", "");
		
		DepartService deptService = new DepartService();
		ItemService itemService = new ItemService();

		//选择的部门设置
		Map<String, Integer> map2 = new HashMap<String, Integer>();
		List<EbDepart> deptList = GovNetUtil.getDeparts(1, "9999", map2, "");
		this.setAttr("deptList", deptList);

		Page<EbItem> itemsPage = null; //itemService.getAllItems(page);
		List<EbItem> items = null; //itemsPage.getList();
		if(dataSrc == 1) {
			//从审批接口中获取事项列表
			Map<String, Integer> map = new HashMap<String, Integer>();
			items = GovNetUtil.getEbItems("", page, PathUtil.MIN_PAGE_SIZE + "", map);
			itemsPage = new Page<EbItem>(items, page, PathUtil.MIN_PAGE_SIZE, 
					map.get("pages"), map.get("total"));
		}else if(dataSrc == 2) {
			
			itemsPage = itemService.getAllItems(page);
			items = itemsPage.getList();
			for(int i = 0; i < items.size(); i++) {
				EbItem item = items.get(i);
				item.setDept(deptService.getDeptById(item.getDepartId() + ""));
				
				if(item.getRefreshTime() != null){
				    String time = item.getRefreshTime();
				    
				    long oldTime = Long.parseLong(time);
				    Date newTime = new Date(oldTime);
				    
				    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				    time = df.format(newTime);
				    
				    item.setRefreshTime(time);
				}
			}
		}
		
		//事项list
//		List<EbItem> items = itemService.getAllItems();
		
		this.setAttr("itemsPage", itemsPage);
		this.setAttr("items", items);
		this.setAttr("dataSrc", dataSrc);
		this.setAttr("departId", deptId);
		
		render(PathUtil.getIndexPath("apply.html"));
	}
	
	/**
	 * 筛选事项
	 */
	public void filterItems() {
		//使用page
		int page = this.getParaToInt("page", 1);
		int dataSrc = this.getParaToInt("dataSource", 1);	//数据来源 1 审批接口、 2 本地库
		String departId = this.getPara("departId", "");
		
		DepartService deptService = new DepartService();
		ItemService itemService = new ItemService();

		//选择的区级部门设置
		Map<String, Integer> map2 = new HashMap<String, Integer>();
		List<EbDepart> deptList = GovNetUtil.getDeparts(1, "9999", map2, "");
		this.setAttr("deptList", deptList);
		
		//获取所有的市级部门（只能添加市级的事项）
		DepartService dservice = new DepartService();
		List<EbDepart> cityDptList = dservice.getCityDept();
		this.setAttr("cityDptList", cityDptList);

		Page<EbItem> itemsPage = null; //itemService.getAllItems(page);
		List<EbItem> items = null; //itemsPage.getList();
		if(dataSrc == 1) {
			//从审批接口中获取事项列表
			Map<String, Integer> map = new HashMap<String, Integer>();
			items = GovNetUtil.getEbItems(departId, page, PathUtil.MIN_PAGE_SIZE + "", map);
			itemsPage = new Page<EbItem>(items, page, PathUtil.MIN_PAGE_SIZE, 
					map.get("pages"), map.get("total"));
		}else if(dataSrc == 2) {
			this.setAttr("deptList", cityDptList);
			itemsPage = itemService.getItemsByDeptId(departId,page);
			items = itemsPage.getList();
			for(int i = 0; i < items.size(); i++) {
				EbItem item = items.get(i);
				item.setDept(deptService.getDeptById(item.getDepartId() + ""));
				
				if(item.getRefreshTime() != null){
				    String time = item.getRefreshTime();
				    
				    long oldTime = Long.parseLong(time);
				    Date newTime = new Date(oldTime);
				    
				    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				    time = df.format(newTime);
				    
				    item.setRefreshTime(time);
				}
			}
		}
		
		this.setAttr("itemsPage", itemsPage);
		this.setAttr("items", items);
		this.setAttr("dataSrc", dataSrc);
		this.setAttr("departId", departId);
		
		render(PathUtil.getIndexPath("apply.html"));
	}
	
	/**
	 * 添加事项页面显示
	 */
	public void addApplyShow(){
		//选择的部门设置
//		Map<String, Integer> map2 = new HashMap<String, Integer>();
//		List<EbDepart> deptList = GovNetUtil.getDeparts(1, "9999", map2, "");
		
		//获取所有的市级部门（只能添加市级的事项）
		DepartService dservice = new DepartService();
		List<EbDepart> cityDptList = dservice.getCityDept();
		this.setAttr("deptList", cityDptList);
		
		render(PathUtil.getIndexPath("addItem.html"));
	}
	
	/**
	 * 添加事项
	 */
	public void addApply(){
		String item_name = this.getPara("item_name");//事项名称（ 必填）
		String time_limit = this.getPara("time_limit");//办理时限 （必填）
		String object = this.getPara("object");//办理对象
		String condition = this.getPara("condition");//办理条件
		String material = this.getPara("material");//所需材料
		String material_annex = this.getPara("material_annex");//材料附件URL
		String item_flow = this.getPara("item_flow");//办理流程
		String flow_annex = this.getPara("flow_annex");//流程附件URL
		String transaction_window = this.getPara("transaction_window");//办事窗口
		String item_charge = this.getPara("item_charge");//收费标准
		String item_questions = this.getPara("item_questions");//常见问题
		String gist_law = this.getPara("gist_law");//办理依据
		String departId = this.getPara("departId");//所属部门 （必填）
		
		ItemService itemService = new ItemService();
		
		EbDepart deptInfo = GovNetUtil.getDeptInfo(departId);
		if(deptInfo != null) {
			boolean result = itemService.addItem(departId, item_name, time_limit, object,
					condition, material, material_annex, item_flow, flow_annex, transaction_window, 
					item_charge, item_questions, gist_law, deptInfo.getDepartName());
			
			if(result){
				applyManage();
			}else{
				this.setAttr("message", "事项添加本地保存失败！");
				addApplyShow();
			}
		}else {
			//到本地查询部门
			DepartService dSer = new DepartService();
			EbDepart deptInfoLocal = dSer.getDeptById(departId);
			
			if(deptInfoLocal != null){
				boolean result = itemService.addItem(departId, item_name, time_limit, object,
						condition, material, material_annex, item_flow, flow_annex, transaction_window, 
						item_charge, item_questions, gist_law, deptInfoLocal.getDepartName());
				
				if(result){
					applyManage();
				}else{
					this.setAttr("message", "事项添加本地保存失败！");
					addApplyShow();
				}
			}else{
				this.setAttr("message", "未查询到该部门，添加事项失败！");
				addApplyShow();
			}
		}
	}
	
	/**
	 * 修改事项页面显示
	 */
	public void resetApplyShow(){
		DepartService deptService = new DepartService();
		
		List<EbDepart> deptList = deptService.getAllDeptList();
		this.setAttr("deptList", deptList);
		
		String itemId = getPara("itemId");
		ItemService itemService = new ItemService();
		EbItem item = itemService.getItemById(itemId);
		if(item!=null){
			this.setAttr("item", item);
		}
		
		render(PathUtil.getIndexPath("resetItem.html"));
	}
	
	/**
	 * 修改事项
	 */
	public void resetApply(){
		String item_name = this.getPara("item_name");//事项名称（ 必填）
		String time_limit = this.getPara("time_limit");//办理时限 （必填）
		String object = this.getPara("object");//办理对象
		String condition = this.getPara("condition");//办理条件
		String material = this.getPara("material");//所需材料
		String material_annex = this.getPara("material_annex");//材料附件URL
		String item_flow = this.getPara("item_flow");//办理流程
		String flow_annex = this.getPara("flow_annex");//流程附件URL
		String transaction_window = this.getPara("transaction_window");//办事窗口
		String item_charge = this.getPara("item_charge");//收费标准
		String item_questions = this.getPara("item_questions");//常见问题
		String gist_law = this.getPara("gist_law");//办理依据
		String departId = this.getPara("departId");//所属部门 （必填）
		String itemId = this.getPara("itemId");//事项id
		
		ItemService itemService = new ItemService();
		
		EbDepart deptInfo = GovNetUtil.getDeptInfo(departId);
		if(deptInfo != null) {
			boolean result = itemService.resetItem(itemId,departId, item_name, time_limit, object,
					condition, material, material_annex, item_flow, flow_annex, transaction_window, 
					item_charge, item_questions, gist_law, deptInfo.getDepartName());
			
			if(result){
				applyManage();
			}else{
				this.setAttr("message", "事项添加本地保存失败！");
				resetApplyShow();
			}
		}else {
			this.setAttr("message", "未查询到该部门，添加事项失败！");
			resetApplyShow();
		}
	}
	
	/**
	 * 删除事项
	 */
	public void deletApply(){
		String itemId = getPara("itemId");
		
		ItemService tService = new ItemService();
		boolean deleResult = tService.deletItemById(itemId);
		
		if(deleResult){
			//删除成功，直接跳转到事项配置显示页面
			setAttr("dmessage", "1");
		}else{
			//删除失败
			setAttr("dmessage","0");
		}
		renderJson();
	}
	
	/**
	 * 验证事项是否重复
	 */
	public void checkApplyName(){
		String itemName = getPara("itemName");
		String deptId = getPara("deptId");
		
		ItemService itemService = new ItemService();
		EbItem item = itemService.getItemByDIdItemName(deptId, itemName);
		
		if(item!=null){
			this.setAttr("message", "0");//事项重复
		}else{
			this.setAttr("message", "1");//事项未重复
		}
		renderJson();
	}
	
	/**
	 * 筛选部门进行企业事项配置
	 */
	public void filterDeptForConfig() {
		int dataSrc = this.getParaToInt("dataSource", 1);
		List<EbDepart> deptList = null;
		
		if(dataSrc == 1) {
			//审批接口数据
			deptList = GovNetUtil.getDeparts(1, "9999", new HashMap<String, Integer>(), "");
		}else if (dataSrc == 2) {
			//本地库数据
//			deptList = GovNetUtil.getDeparts(1, "9999", new HashMap<String, Integer>(), "");
			//获取所有的市级部门（只能添加市级的事项）
			DepartService dservice = new DepartService();
		    deptList = dservice.getCityDept();
		}
		
		this.setAttr("deptList", deptList);
		renderJson();
	}
	
	/**
	 * 筛选事项进行企业事项配置
	 */
	public void filterItemsForConfig() {
		String departId = this.getPara("departId");
		int dataSrc = this.getParaToInt("dataSource", 1);
		
		List<EbItem> items = null;
		if(dataSrc == 1) {
			//审批接口数据
			items = GovNetUtil.getEbItems(departId, 1, "9999", new HashMap<String, Integer>());
		} else if(dataSrc == 2) {
			//本地库数据
			ItemService itemService = new ItemService();
			items = itemService.getItemsByDeptId(departId);
		}
		
		this.setAttr("itemlist", items);
		renderJson();
	}
	
	/**
	 * 查看办事事项办事指南
	 */
	public void checkServiceGuid() {
		String itemId = this.getPara("itemId");
		DepartService deptService = new DepartService();
		ItemService itemService = new ItemService();
		
		EbItem item = itemService.getItemById(itemId);
		if(item!=null){
			EbDepart dept = deptService.getDeptById(item.getDepartId()+"");
			if(dept!=null){
				item.setDept(dept);
			}
		}
		this.setAttr("item", item);
		
		render(PathUtil.getIndexPath("lookApply.html"));
	}
	
	/**
	 * 查看从审批接口传来的办事指南
	 */
	public void applyGuidByGov(){
		String itemId = this.getPara("itemId");
		
		EbItem item = GovNetUtil.getItemInfo(itemId);
		this.setAttr("item", item);
		
		render(PathUtil.getIndexPath("lookApply.html"));
	}
	
	/**
	 * 留言管理
	 */
	public void messageManage(){
		MessageService mService = new MessageService();
		int page = this.getParaToInt("page", 1);

		Page<EbMessage> mlistPage = mService.getAllMessage(page);
		List<EbMessage> mlist = mlistPage.getList();
		
		//Page前
//		List<EbMessage> mlist = mService.getAllMessage();
		
		List<EbMessage> mlistNew = new ArrayList<EbMessage>();
		for(int i=0;i<mlist.size();i++){
			EbMessage ms = mlist.get(i);
			
			//判断该留言是否有回复
			List<EbMessage> replylist = new ArrayList<EbMessage>();
			replylist = mService.getAllReplyList(ms.getMessageId()+"");
			if(replylist.size()>0){
				ms.setIs_reply("1");//"1"有回复;"0"无回复
			}else{
				ms.setIs_reply("0");
			}
			
			if(ms.getMessageTime() != null){
			    String time = ms.getMessageTime();
			   
			    long oldTime = Long.parseLong(time);
			    Date newTime = new Date(oldTime);
			   
			    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			    time = df.format(newTime);
			   
			    ms.setMessageTime(time);
			   }
			
			mlistNew.add(ms);
		}
		this.setAttr("mlistPage", mlistPage);
		this.setAttr("mlist", mlistNew);
		render(PathUtil.getIndexPath("messageManage.html"));
	}
	
	/**
	 * 留言管理筛选，根据企业名称和发起人模糊查询
	 */
	public void filterMessage(){
		String businessName = getPara("businessName");
		String userName = getPara("userName");
		
		MessageService mService = new MessageService();
		int page = this.getParaToInt("page", 1);

		Page<EbMessage> mlistPage = mService.getMesByUserBus(userName, businessName, page);
		List<EbMessage> mlist = mlistPage.getList();
		
		//Page前
//		List<EbMessage> mlist = mService.getMesByUserBus(userName, businessName);
		
		List<EbMessage> mlistNew = new ArrayList<EbMessage>();
		for(int i=0;i<mlist.size();i++){
			EbMessage ms = mlist.get(i);
			
			//判断该留言是否有回复
			List<EbMessage> replylist = new ArrayList<EbMessage>();
			replylist = mService.getAllReplyList(ms.getMessageId()+"");
			if(replylist.size()>0){
				ms.setIs_reply("1");//"1"有回复;"0"无回复
			}else{
				ms.setIs_reply("0");
			}
			if(ms.getMessageTime() != null){
			    String time = ms.getMessageTime();
			   
			    long oldTime = Long.parseLong(time);
			    Date newTime = new Date(oldTime);
			   
			    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			    time = df.format(newTime);
			   
			    ms.setMessageTime(time);
			   }
			mlistNew.add(ms);
		}
		this.setAttr("businessName", businessName);
		this.setAttr("userName", userName);
		
		this.setAttr("mlistPage", mlistPage);
		this.setAttr("mlist", mlistNew);
		render(PathUtil.getIndexPath("messageManage.html"));
	}
	
	/**
	 * 审核留言
	 */
	public void examMessage(){
		String message_id = getPara("message_id");
		String exam_status = getPara("exam_status");//（0 未审 1 已审  2审核不通过）
		String message = "";//0审核失败，1审核成功，3缺少参数
		boolean result = false;
		
		MsgInterService mService = new MsgInterService();
		EbMessage mss = mService.getMsgByMsgId(message_id);
		
		if(mss != null){
			if(exam_status.equals("1")){
				mss.setExamStatus(new BigDecimal("1"));
			    result = mss.update();
			    String newsContent = "";
			    //判断是留言还是回复，进行消息推送
			    if (mss.getParamId() != null && !(mss.getParamId() + "").equals("") 
			    		&& !(mss.getParamId() + "").equalsIgnoreCase("null")) {
			    	newsContent = mss.getUserName() + "回复了您的留言，回复内容：" + mss.getMessageContent();
				}else {
					newsContent = mss.getUserName() + "给您留了言，留言内容：" + mss.getMessageContent();
				}
			    
			    EbDepart deptInfo = new DepartService().getDeptById(mss.getDepartId() + "");
			    if (deptInfo == null) {
					deptInfo = GovNetUtil.getDeptInfo(mss.getDepartId() + "");
				}
			    
			    List<String> strAlias = new ArrayList<String>();
				if(deptInfo.getDepartName().contains("政务办")) {
					strAlias.add(Config.ZWB);
				}else if (deptInfo.getDepartName().contains("招商局")) {
					strAlias.add(Config.ZSJ);
				}else {
					strAlias.add(deptInfo.getDepartId() + "");
				}
			    
			    Hashtable<String, String> map = new Hashtable<String, String>();
				map.put("newsType", "8");
				map.put("content", newsContent);
				map.put("status", "0");
				map.put("businessId", getCheckedString(mss.getBusinessId() + ""));
				map.put("businessName", getCheckedString(mss.getBusinessName()));
				map.put("userName", mss.getUserName());
				PushUtil.pushNotifyToTagert(strAlias, StringUtil.getJson(map));
			    
			}else if(exam_status.equals("2")){
				mss.setExamStatus(new BigDecimal("2"));
				result = mss.update();
			}else{
				message = "3";
			}
		}else{
			message ="3";
		}
		if(result){
			message = "1";
		}else{
			message = "0";
		}
		this.setAttr("message", message);
		renderJson();
	}

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
	 * 留言回复
	 */
	public void replyMassage(){
		String messageId = getPara("messageId");
		
		MessageService mService = new MessageService();
		List<EbMessage> replylistNew = new ArrayList<EbMessage>();
		
		int page = this.getParaToInt("page", 1);

		Page<EbMessage> replylistPage = mService.getAllReply(messageId, page);
		List<EbMessage> replylist = replylistPage.getList();
		
		//Page前
//		replylist = mService.getAllReply(messageId);
		if(replylist.size()>0){
			for(int i=0;i<replylist.size();i++){
				EbMessage rs = replylist.get(i);
				UserService uService = new UserService(this);
				//获取用户信息
				System.out.print("测试用户名获取"+rs.getUserId());
				EbUserInfo userInfo = uService.findById(rs.getUserId()+"");
				rs.setUserInfo(userInfo);
				
				if(rs.getMessageTime() != null){
				    String time = rs.getMessageTime();
				   
				    long oldTime = Long.parseLong(time);
				    Date newTime = new Date(oldTime);
				   
				    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				    time = df.format(newTime);
				   
				    rs.setMessageTime(time);
				}
				
				replylistNew.add(rs);
			}
		}
		this.setAttr("replylistPage", replylistPage);
		this.setAttr("replylist", replylistNew);
		render(PathUtil.getIndexPath("replyMassage.html"));
	}
	
	/**
	 * 部门管理
	 */
	public void departManage(){
//		DepartService dService = new DepartService();
//		List<EbDepart> dListOld = dService.getAllDeptList();
		
		//获取所有的部门信息列表（一定是从审批接口中获取的）
		List<EbDepart> dListOld = GovNetUtil.getDeparts(1, "9999", new HashMap<String, Integer>(), null);
		//获取部门所有信息并设置到前端
		this.setAttr("dTypeList", dListOld);
		
		//Page分页
		int page = this.getParaToInt("page", 1);
		Map<String, Integer> map = new HashMap<String, Integer>();
		List<EbDepart> dInfoList = GovNetUtil.getDeparts(page, "10", map, null);//并且设置了部门签收人（如果存在）
		
		//进行分页
		if(!map.isEmpty()){
			Page<EbDepart> dInfoListPage = new Page<EbDepart>(dInfoList, page, 
					PathUtil.MIN_PAGE_SIZE, map.get("pages"), map.get("total"));
					this.setAttr("dInfoListPage", dInfoListPage);
			if(dInfoList != null) {
				for(int i = 0; i < dInfoList.size(); i++) {
					EbDepart dept = dInfoList.get(i);
					EbDepart dpt = new DeptInterService().getDeptById(dept.getDepartId() + "");
					if(dpt != null) {
						dept.setRefreshTime(TimeUtil.changeTimeStyle(dpt.getRefreshTime()));
						dept.setWorkTel(dpt.getWorkTel());
						dept.setUserCode(dpt.getUserCode()+"");
						String userCode = dept.getUserCode();
						EbUserInfo userInfo = GovNetUtil.getUserByCode(userCode);
						if(userInfo != null){
							dept.setUserInfo(userInfo);
						}
/************2015-11-08 by feini********************/
					//查询相同部门下的多个签收人
//					List<EbUserInfo> signUserInfoList = new ArrayList<EbUserInfo>();//所有签收的人的信息
//					DepartService dService = new DepartService();
//					List<EbDepart>	deptList = dService.getDeptListById(dept.getDepartId()+"");//获取部门表里部门相同的
//					if(deptList.size() > 0){
//						for(int j = 0; j < deptList.size();j++){
//							EbDepart d = deptList.get(j);
//							String userCode = d.getUserCode();
//							EbUserInfo userInfo = GovNetUtil.getUserByCode(userCode);
//							if(userInfo != null){
//								signUserInfoList.add(userInfo);
//							}
//						}
//					}
//					if(signUserInfoList.size()>0){
//						dept.setUserInfoList(signUserInfoList);//传递所有签收人用户信息列表到页面
//					}
/********************************/
					}else {
						continue;
					}
				}
			}
		}
		
		//获取所有部门信息已经该部门下的所有队员签收人
		
		this.setAttr("dInfoList", dInfoList);
		render(PathUtil.getIndexPath("departManage.html"));
	}
	
	public void loadDepts() {
		String departLevel = this.getPara("departLevel");
		DeptInterService deptService = new DeptInterService();
		
		List<EbDepart> dListOld = null;
		if (departLevel != null && departLevel.trim().equals("1")) {
			//市级单位
			dListOld = deptService.getDeptsByType(departLevel, "");
		}else {
			//区级单位
			dListOld = GovNetUtil.getDeparts(1, "9999", new HashMap<String, Integer>(), null);
		}
		
		this.setAttr("success", true);
		this.setAttr("dListOld", dListOld);
		renderJson();
		
	}
	
	/**
	 * 筛选部门管理
	 */
	public void filterDepart(){
		String departId = getPara("departId");
		String departLevel = this.getPara("departLevel");
		DeptInterService deptService = new DeptInterService();
		
		List<EbDepart> dListOld = null;
		if (departLevel != null && departLevel.trim().equals("1")) {
			//市级单位
			dListOld = deptService.getDeptsByType(departLevel, departId);
		}else {
			//区级单位
			dListOld = GovNetUtil.getDeparts(1, "9999", new HashMap<String, Integer>(), null);
		}
		
		if(dListOld == null) {
			dListOld = new ArrayList<EbDepart>();
		}
		
		//获取部门所有信息并设置到前端
		this.setAttr("dTypeList", dListOld);
		this.setAttr("departId", departId);
		this.setAttr("departLevel", departLevel);
		
		//Page分页
		int page = this.getParaToInt("page", 1);
		List<EbDepart> dInfoList = new ArrayList<EbDepart>();
		UserInterService userService = new UserInterService(this);
		
		if (departLevel != null && departLevel.trim().equals("1")) {
			//市级分页
			Page<EbDepart> dInfoListPage = deptService.pageDeptsByType(departLevel, 
					departId, page+"");
			if (dInfoListPage != null) {
				this.setAttr("dInfoListPage", dInfoListPage);
				dInfoList = dInfoListPage.getList();
				for (int i = 0; i < dInfoList.size(); i++) {
					EbDepart dept = dInfoList.get(i);//市级部门首席
					dept.setRefreshTime(TimeUtil.changeTimeStyle(dept
							.getRefreshTime()));
					String userCode = dept.getUserCode();
					if (!NullUtil.isEmpty(userCode)) {//市级单位首席办事员
						EbUserInfo userInfo = userService.findByCode(userCode, 4);
						if(userInfo != null){
							dept.setUserInfo(userInfo);
						}
						// 所有签收的人的信息
//						List<EbUserInfo> signUserInfoList = new ArrayList<EbUserInfo>();
//						signUserInfoList.add(userInfo);//目前只有一个单位首席了
//						if(signUserInfoList.size()>0){
//							//传递所有单位首席用户信息列表到页面
//							dept.setUserInfoList(signUserInfoList);
//						}
					}
				}
			}
		}else {
			//区级分页
			Map<String, Integer> map = new HashMap<String, Integer>();
			//并且设置了部门签收人（如果存在）
			dInfoList = GovNetUtil.getDeparts(page, PathUtil.MIN_PAGE_SIZE+"", 
					map, departId);
			
			// 进行分页
			if (!map.isEmpty()) {
				Page<EbDepart> dInfoListPage = new Page<EbDepart>(dInfoList, page,
						PathUtil.MIN_PAGE_SIZE, map.get("pages"), map.get("total"));
				this.setAttr("dInfoListPage", dInfoListPage);
				if (dInfoList != null) {
					for (int i = 0; i < dInfoList.size(); i++) {
						EbDepart dept = dInfoList.get(i);
						//本地部门列表中是否有该部门信息
						EbDepart dpt = deptService.getDeptById(dept
								.getDepartId() + "");
						if (dpt != null) {
							dept.setRefreshTime(TimeUtil.changeTimeStyle(dpt
									.getRefreshTime()));
							dept.setWorkTel(dpt.getWorkTel());
							
							//是否有区级部门首席
							String userCode = dpt.getUserCode();
							if (!NullUtil.isEmpty(userCode)) {
								//区级单位首席办事员
								EbUserInfo userInfo = userService.findByCode(userCode, 1);
								if(userInfo != null){
									dept.setUserInfo(userInfo);
								}
								// 所有签收的人的信息
								List<EbUserInfo> signUserInfoList = new ArrayList<EbUserInfo>();
								signUserInfoList.add(userInfo);//目前一个单位只有一个单位首席了
								if(signUserInfoList.size()>0){
									//传递所有单位首席用户信息列表到页面
									dept.setUserInfoList(signUserInfoList);
								}
							}
						}
					}
				}
			}
		}
		
//		//获取所有部门信息以及该部门下的所有队员签收人
		this.setAttr("dInfoList", dInfoList);
		render(PathUtil.getIndexPath("departManage.html"));
	}
	
	/**
	 * 设置部门首席
	 */
	public void toChiefSetView() {
		String departId = this.getPara("departId");
		String departType = this.getPara("departType");
		String workTel = this.getPara("workTel");
		String departName = this.getPara("departName");
		String workAddress = this.getPara("workAddress");
		String userCode = this.getPara("userCode");
		List<EbUserInfo> userList = new ArrayList<EbUserInfo>();
		if(departType.equals("1")) {
			//市级部门首席
			UserInterService userService = new UserInterService(this);
			userList = userService.filterUsersList("4", departId);
		}else {
			//区级部门首席
			userList = GovNetUtil.getEbUserInfos(departId, 1, "9999", new HashMap<String, Integer>());
		}
		
		this.setAttr("departId", departId);
		this.setAttr("departName", departName);
		this.setAttr("userCode", userCode);
		this.setAttr("departType", departType);
		this.setAttr("workTel", workTel);
		this.setAttr("workAddress", workAddress);
		this.setAttr("userList", userList);
		render("/WEB-INF/jsp/setSignMan.html");
	}
	
	/**
	 * 设置单位首席
	 */
	public void setChiefMan() {
		String departId = this.getPara("departId");
		String departType = this.getPara("departType");
		String workTel = this.getPara("workTel");
		String departName = this.getPara("departName");
		String workAddress = this.getPara("workAddress");
		String userAccount = this.getPara("userAccount");
		boolean success = false;
		if (!NullUtil.isEmpty(departId)) {
			DeptInterService deptService = new DeptInterService();
			if (departType.equals("2")) {
				//区级
				//本地部门表中是否有该部门信息
				EbDepart deptInfo = deptService.getLocDeptById(departId);
				if(deptInfo == null) {
					//还未设置过单位首席
					success = deptService.setDeptChief(departId, departName, departType, 
							userAccount, workTel, workAddress);
				}else {
					//如果设置过部门首席，则把前一个首席的权限在用户表改掉
					String userCodeOld = deptInfo.getUserCode();
					if(userCodeOld != null){
						UserInterService userSer = new UserInterService(this);
						EbUserInfo user = userSer.getUserByCD(userCodeOld, departId);
						if(user != null){
							//（1VIP服务专员管理员、2VIP服务专员权限 、3单位首席权限、4部门跟办人权限、5市级服务专员权限、
							//6 VIP服务专员管理员+单位首席权限、7 VIP服务专员+单位首席权限）
							int auth = user.getAuthority().intValue();
							if(auth == 6){
								user.setAuthority(new BigDecimal(1));
							}else if(auth == 7){
								user.setAuthority(new BigDecimal(2));
							}else{
								user.setAuthority(new BigDecimal(4));
							}
							user.update();
						}
					}
					deptInfo.setUserCode(userAccount);
					success = deptService.changeDeptChief(deptInfo);
				}
			}else {
				//市级
				EbDepart deptInfo = deptService.getLocDeptById(departId);
				if (deptInfo != null) {
					//如果设置过部门首席，则把前一个首席的权限在用户表改掉
					String userCodeOld = deptInfo.getUserCode();
					if(userCodeOld != null){
						UserInterService userSer = new UserInterService(this);
						EbUserInfo user = userSer.getUserByCD(userCodeOld, departId);
						if(user != null){
							//（1VIP服务专员管理员、2VIP服务专员权限 、3单位首席权限、4部门跟办人权限、5市级服务专员权限、
							//6 VIP服务专员管理员+单位首席权限、7 VIP服务专员+单位首席权限）
							int auth = user.getAuthority().intValue();
							if(auth == 6){
								user.setAuthority(new BigDecimal(1));
							}else if(auth == 7){
								user.setAuthority(new BigDecimal(2));
							}else{
								user.setAuthority(new BigDecimal(4));
							}
							user.update();
						}
					}
					deptInfo.setUserCode(userAccount);
					success = deptService.changeDeptChief(deptInfo);
				}
			}
			this.setAttr("success", success);
			if (!success) {
				this.setAttr("message", "数据更新错误，设置失败！");
			}
		}else {
			this.setAttr("success", success);
			this.setAttr("message", "部门id为空，设置失败！");
		}
		this.renderJson();
	}
	
	public void toAddDepartView() {
		render("/WEB-INF/jsp/addDepart.html");
	}
	
	public void addDepart() {
		String departName = this.getPara("departName");
		String workTel = this.getPara("workTel");
		String workAddress = this.getPara("workAddress");
		
		DeptInterService deptService = new DeptInterService();
		EbDepart deptInfo = deptService.getDeptByName(departName);
		if (deptInfo != null) {
			this.setAttr("success", false);
			this.setAttr("message", "该单位名称已存在无法添加！");
			renderJson();
		}else {
			if (deptService.addDepartInfo(departName, workTel, workAddress)) {
				this.setAttr("success", true);
				this.setAttr("message", "添加成功！");
				this.renderJson();
			}else {
				this.setAttr("success", false);
				this.setAttr("message", "添加失败！");
				this.renderJson();
			}
		}
	}
	
	/**
	 * 编辑联系电话
	 */
	public void setCall(){
		String departId = this.getPara("departId");
		String call = this.getPara("call");
		String message = "";
		
		DepartService departSer = new DepartService();
		EbDepart dept = departSer.getDeptById(departId);
		if(dept != null && call != null){
			dept.setWorkTel(call);
			if(dept.update()){
				message = "1";//编辑成功
			}else{
				message = "0";//编辑失败
			}
		}else{
			message = "2";//缺少参数
		}
		this.setAttr("message", message);
		renderJson();
	}
	
	/**
	 * 特殊任务
	 */
	public void procedure(){
		TaskService tService = new TaskService();
		
		int page = this.getParaToInt("page", 1);

		Page<EbSpcProgram> espListPage = tService.getAllPrograms(page);
		List<EbSpcProgram> espList = espListPage.getList();
		//page前
//		List<EbSpcProgram> espList = tService.getAllPrograms();
		UserService uService = new UserService(null);

		for (int i = 0; i < espList.size(); i++) {
			EbSpcProgram esp = espList.get(i);
			
			if(esp.getBuildTime() != null){
				String time = esp.getBuildTime();
				
				long oldTime = Long.parseLong(time);
				Date newTime = new Date(oldTime);
				
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				time = df.format(newTime);
				
				esp.setBuildTime(time);
			}
			
			if(esp.getExaminTime() != null){
				String time = esp.getExaminTime();
				
				long oldTime = Long.parseLong(time);
				Date newTime = new Date(oldTime);
				
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				time = df.format(newTime);
				
				esp.setExaminTime(time);
			}

			if (esp.getBuildUserId() != null) { // 签收用户
				esp.setBuildUser(uService.findById(esp.getBuildUserId()
						.toString()));
			}

			if (esp.getUserId() != null) { // 审批用户
				esp.setUserInfo(uService.findById(esp.getUserId().toString()));
			}
		}
		
		this.setAttr("espListPage", espListPage);
		this.setAttr("programList", espList);
		render(PathUtil.getIndexPath("procedure.html"));
	}
	
	/**
	 * 筛选特殊任务
	 */
	public void filterProgram() {
		String bussName = this.getPara("bussName");
		TaskService tService = new TaskService();
		int page = this.getParaToInt("page", 1);

		Page<EbSpcProgram> espListPage = tService.getByBussName(bussName,page);
		List<EbSpcProgram> espList = espListPage.getList();
		//page前
//		List<EbSpcProgram> espList = tService.getByBussName(bussName);
		UserService uService = new UserService(null);

		for (int i = 0; i < espList.size(); i++) {
			EbSpcProgram esp = espList.get(i);

			if (esp.getBuildUserId() != null) { // 签收用户
				esp.setBuildUser(uService.findById(esp.getBuildUserId()
						.toString()));
			}

			if (esp.getUserId() != null) { // 审批用户
				esp.setUserInfo(uService.findById(esp.getUserId().toString()));
			}
			
			if(esp.getBuildTime() != null){
				String time = esp.getBuildTime();
				
				long oldTime = Long.parseLong(time);
				Date newTime = new Date(oldTime);
				
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				time = df.format(newTime);
				
				esp.setBuildTime(time);
			}
			
			if(esp.getExaminTime() != null){
				String time = esp.getExaminTime();
				
				long oldTime = Long.parseLong(time);
				Date newTime = new Date(oldTime);
				
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				time = df.format(newTime);
				
				esp.setExaminTime(time);
			}
		}
		
		this.setAttr("espListPage", espListPage);
		this.setAttr("programList", espList);
		this.setAttr("bussName", bussName);
		render(PathUtil.getIndexPath("procedure.html"));
	}
	
	/**
	 * 查看特别程序
	 */
	public void lookProcedure(){
		String programId = this.getPara("programId");
		TaskService tService = new TaskService();
		
		EbSpcProgram esp = tService.getById(programId);
		
		//新建日期
		if(esp.getBuildTime() != null){
			String time = esp.getBuildTime();
			
			long oldTime = Long.parseLong(time);
			Date newTime = new Date(oldTime);
			
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			time = df.format(newTime);
			
			esp.setBuildTime(time);
		}
		
		//审批日期
		if(esp.getExaminTime() != null){
			String time = esp.getExaminTime();
			
			long oldTime = Long.parseLong(time);
			Date newTime = new Date(oldTime);
			
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			time = df.format(newTime);
			
			esp.setExaminTime(time);
		}
		
		//审批人姓名显示
		UserService uService = new UserService(null);
		if (esp.getUserId() != null) { // 审批用户
			esp.setUserInfo(uService.findById(esp.getUserId().toString()));
		}
		
		this.setAttr("esp", esp);
		
		render(PathUtil.getIndexPath("lookProcedure.html"));
	}
	
	
	/**
	 * 任务管理
	 */
	public void taskManage(){
		TaskService tService = new TaskService();
		int page = this.getParaToInt("page", 1);

		Page<EbTask> taskListPage = tService.getAllTasks(page);
		List<EbTask> taskList = taskListPage.getList();
		//page前
//		List<EbTask> taskList = tService.getAllTasks();
		UserService uService = new UserService(null);
		for(int i = 0; i < taskList.size(); i++ ) {
			EbTask task = taskList.get(i);
			LogUtil.log("签收状态：" + task.getSignStatus());
			if(task.getUserId() != null) {	//创建人用户
				task.setBuildUser(uService.findById(task.getUserId() + ""));
			}
			if(task.getSignUserId() != null) {	//签收人用户
				task.setSignUser(uService.findById(task.getSignUserId() + ""));
			}
			//任务分发状态
			if(task.getTaskId() != null){
				String taskId = task.getTaskId()+"";
				List<EbTaskDistribute> taskDli = tService.getDistribListLi(taskId);
				if(taskDli.size()>0){
					task.setDistributedStatus("1");//是否分发任务 0 未分发、1 已分发
				}else{
					task.setDistributedStatus("0");
				}
			}
			if(task.getCompTime() != null){
			    String time = task.getCompTime();
			    
			    long oldTime = Long.parseLong(time);
			    Date newTime = new Date(oldTime);
			    
			    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			    time = df.format(newTime);
			    
			    task.setCompTime(time);
			   }
			
			if(task.getSignTime() != null){
			    String time = task.getSignTime();
			    
			    long oldTime = Long.parseLong(time);
			    Date newTime = new Date(oldTime);
			    
			    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			    time = df.format(newTime);
			    
			    task.setSignTime(time);
			   }
			if(task.getAddTime() != null){
			    String time = task.getAddTime();
			    
			    long oldTime = Long.parseLong(time);
			    Date newTime = new Date(oldTime);
			    
			    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			    time = df.format(newTime);
			    
			    task.setAddTime(time);
			   }
		}
		this.setAttr("taskListPage", taskListPage);
		this.setAttr("taskList", taskList);
		render(PathUtil.getIndexPath("taskManage.html"));
	}
	
	/**
	 * 根据企业名称筛选任务管理
	 */
	public void filterTask() {
		String bussName = this.getPara("bussName");
		TaskService tService = new TaskService();
		int page = this.getParaToInt("page", 1);

		Page<EbTask> taskListPage = tService.getTasksByBussName(bussName,page);
		List<EbTask> taskList = taskListPage.getList();
		//page前
//		List<EbTask> taskList = tService.getTasksByBussName(bussName);
		UserService uService = new UserService(null);
		for(int i = 0; i < taskList.size(); i++ ) {
			EbTask task = taskList.get(i);
			if(task.getUserId() != null) {	//创建人用户
				task.setBuildUser(uService.findById(task.getUserId() + ""));
			}
			if(task.getSignUserId() != null) {	//签收人用户
				task.setSignUser(uService.findById(task.getSignUserId() + ""));
			}
			//任务分发状态
			if(task.getTaskId() != null){
				String taskId = task.getTaskId()+"";
				List<EbTaskDistribute> taskDli = tService.getDistribListLi(taskId);
				if(taskDli.size()>0){
					task.setDistributedStatus("1");//是否分发任务 0 未分发、1 已分发
				}else{
					task.setDistributedStatus("0");
				}
			}
			if(task.getCompTime() != null){
			    String time = task.getCompTime();
			    
			    long oldTime = Long.parseLong(time);
			    Date newTime = new Date(oldTime);
			    
			    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			    time = df.format(newTime);
			    
			    task.setCompTime(time);
			   }
			
			if(task.getSignTime() != null){
			    String time = task.getSignTime();
			    
			    long oldTime = Long.parseLong(time);
			    Date newTime = new Date(oldTime);
			    
			    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			    time = df.format(newTime);
			    
			    task.setSignTime(time);
			   }
			if(task.getAddTime() != null){
			    String time = task.getAddTime();
			    
			    long oldTime = Long.parseLong(time);
			    Date newTime = new Date(oldTime);
			    
			    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			    time = df.format(newTime);
			    
			    task.setAddTime(time);
			   }
		}
		
		this.setAttr("taskListPage", taskListPage);
		this.setAttr("taskList", taskList);
		this.setAttr("bussName", bussName);
		render(PathUtil.getIndexPath("taskManage.html"));
	}
	
	/**
	 * 任务分发、任务清单表
	 * @throws JSONException 
	 */
	public void taskDistri() throws JSONException{
		String taskId = this.getPara("taskId");
		String bussName = this.getPara("bussName");
		this.setAttr("taskId", taskId);
		this.setAttr("bussName", bussName);
		
		TaskService tService = new TaskService();
		int page = this.getParaToInt("page", 1);

		Page<EbTaskDistribute> distrListPage = tService.getDistribList(taskId,page);
		List<EbTaskDistribute> distrList = distrListPage.getList();
		
		UserService uService = new UserService(null);
		DepartService dService = new DepartService();
		ItemService iService = new ItemService();
		
		for (int i = 0; i < distrList.size(); i++) {
			EbTaskDistribute tDistr = distrList.get(i);
			EbItem item = iService.getItemById(tDistr.getItemId() + "");
			tDistr.setItem(item);	//事项
			
			if(tDistr.getSignUserId() != null) {
				EbUserInfo signUser = uService.findById(tDistr.getSignUserId() + "");
				if(signUser == null) {
					signUser = GovNetUtil.getUserByCode(tDistr.getSignUserCode());
				}
				tDistr.setSignUser(signUser);	//签收用户
			}
			
			EbDepart dept = dService.getDeptById(tDistr.getDepartId() + "");
			tDistr.setDept(dept);	//事项所属部门
			
			if(tDistr.getUserId() != null) {
				EbUserInfo distrUser = uService.findById(tDistr.getUserId() + "");
				if(distrUser == null) {
					distrUser = GovNetUtil.getUserByCode(tDistr.getDistrUserCode());
				}
				tDistr.setDistrUser(distrUser);	//分发人、创建人
			}
			
			if(tDistr.getTransactorId() != null) {
				EbUserInfo transUser = uService.findById(tDistr.getTransactorId() + "");
				if(transUser == null) {
					transUser = GovNetUtil.getUserByCode(tDistr.getTransUserCode());
				}
				tDistr.setTransUser(transUser);	//办理人
			}
			
			//TODO 判断事项是否办理超时
			if(tDistr.getTransactionStatus().intValue() == 2) {
				int time_limit = 15;//事项办理期限（多少个工作日）
				ItemInterService itemSer = new ItemInterService();
				EbItem itemInfo = itemSer.getItemById(tDistr.getItemId()+"");
				if(itemInfo == null) {
					itemInfo = GovNetUtil.getItemInfo(tDistr.getItemId() + "");
				}
				if(itemInfo != null){
					if(itemInfo.getTimeLimit() != null && MD5Util.isNumeric(itemInfo.getTimeLimit() + "")) {
						if(itemInfo.getTimeLimit().intValue() <= 0) {
							time_limit = 15;
						}else {
							time_limit = itemInfo.getTimeLimit().intValue();
						}
					}
				}
				long sign_time = Long.parseLong(tDistr.getSignTime());//事项签收时间
				long nowTime = new Date().getTime();
				long days = TimeUtil.getWorkDaysBylong(sign_time, nowTime);//从签收到现在的天数
				long deday = days/24/3600/1000;
				int overTime = (int)deday - time_limit;//超时时间（工作日）
				
				tDistr.setOvTime(overTime);//设置超时时间
			}
			
			if(tDistr.getDistribTime() != null){
			    String time = tDistr.getDistribTime();
			    
			    long oldTime = Long.parseLong(time);
			    Date newTime = new Date(oldTime);
			    
			    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			    time = df.format(newTime);
			    
			    tDistr.setDistribTime(time);
			   }
			
			if(tDistr.getSignTime() != null){
			    String time = tDistr.getSignTime();
			    
			    long oldTime = Long.parseLong(time);
			    Date newTime = new Date(oldTime);
			    
			    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			    time = df.format(newTime);
			    
			    tDistr.setSignTime(time);
			   }
			if(tDistr.getTransactionTime() != null){
			    String time = tDistr.getTransactionTime();
			    
			    long oldTime = Long.parseLong(time);
			    Date newTime = new Date(oldTime);
			    
			    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			    time = df.format(newTime);
			    
			    tDistr.setTransactionTime(time);
			   }
			//判断事项是否有上传的图片,有则传递images对象列表
			ImgService imgSer = new ImgService();
			List<EbFileImg> images = imgSer.findImgByDistr(tDistr.getDistrId()+"");
			if(images.size()>0){
				tDistr.setIs_haveImg(1);//(0没有，1有)
				tDistr.setImgList(images);
			}else{
				tDistr.setIs_haveImg(0);//(0没有，1有)
			}
			
		}
		//page前
//		List<EbTaskDistribute> distrList = tService.getDistribList(taskId);
		this.setAttr("distrListPage", distrListPage);
		this.setAttr("distrList", distrList);
		
		List<DepartModel> deptList = tService.getDeptIds(taskId);
		this.setAttr("deptList", deptList);
		
		render(PathUtil.getIndexPath("taskDistri.html"));
	}
	
	/**
	 * 根据任务分发ID获取图片Json数据
	 */
	public void getImagBydistrId(){
		String distrId = this.getPara("distrId");
		
		String message = "获取图片失败";//获取图片失败
		boolean success = false;
		
		List<Hashtable<String, String>> result = new ArrayList<Hashtable<String, String>>();
		
		ImgService imgSer = new ImgService();
		List<EbFileImg> images = imgSer.findImgByDistr(distrId);
		
		if(images.size()>0){
			for(int j=0; j<images.size();j++){
				//遍历图片
				success = true;
				message = "获取图片成功";
				Hashtable<String, String> resultHash = new Hashtable<String, String>();
				EbFileImg img = images.get(j);//把图片List转换成json格式
				resultHash.put("imageName", getCheckedString(img.getImageName()));
				resultHash.put("imgId",getCheckedString(img.getImageId()+""));
				resultHash.put("imgUrl",getCheckedString(img.getImageUrl()));
				result.add(resultHash);
			}
			this.setAttr("result", result);
		}
		
		setAttr("success", success);
		setAttr("message", message);
		render(new JsonRender().forIE());
	}
	
	public void changeSignTime() {
		String distrId = this.getPara("distrId");
		String newSignTime = this.getPara("newTime");
		
		long time = TimeUtil.dateToLong(newSignTime, "yyyy-MM-dd");
		
		TaskInterService tService = new TaskInterService();
		EbTaskDistribute distrInfo = tService.getDistrByDId(distrId);
		
		if(distrInfo == null) {
			this.setAttr("message", "0");
			this.setAttr("errorMessage", "服务器未查询到该事项分发数据");
		}else {
			EbTask taskInfo = tService.getTaskById(distrInfo.getTaskId() + "");
			EbBusiness bussInfo = null;
			if(taskInfo != null) {
				bussInfo = new BussInterService().findById(taskInfo.getBusinessId() + "");
			}
			
			if (distrInfo.getSignTime() == null || distrInfo.getSignTime().isEmpty()) {
				this.setAttr("message", "0");
				this.setAttr("errorMessage", "该分发事项的签收时间为空");
			}else {
				if(bussInfo == null) {
					this.setAttr("message", "0");
					this.setAttr("errorMessage", "服务器未查询到该事项相关的企业信息");
				}else if (bussInfo.getSignTime() == null || time <= Long.parseLong(bussInfo.getSignTime() + "")) {
					this.setAttr("message", "0");
					this.setAttr("errorMessage", "修改的签收日期不能早于企业签收日期");
				}else {
					EbItem itemInfo = null;
					if(distrInfo.getItemType().intValue() == 0) {
						itemInfo = new ItemInterService().getItemById(distrInfo.getItemId() + "");
					}else {
						itemInfo = GovNetUtil.getItemInfo(distrInfo.getItemId() + "");
					}
					
					if(itemInfo != null) {
						long curTime = System.currentTimeMillis();
						int timeLimit = 15;
						if(itemInfo.getTimeLimit() != null) {
							timeLimit = itemInfo.getTimeLimit().intValue();
						}
						long days = TimeUtil.getWorkDaysBylong(time, curTime);
						long deday = days/24/3600/1000;
						if(deday - timeLimit > 0) {//已超时
							try {
								distrInfo.setTransactionStatus(new BigDecimal("2"));
								distrInfo.setSignTime(time + "");
								if(distrInfo.update()) {
									this.setAttr("message", "1");
								}else {
									this.setAttr("message", "0");
									this.setAttr("errorMessage", "数据更新失败");
								}
							}catch (Exception e) {
								e.printStackTrace();
							}
						}else {//未超时
							try {
								distrInfo.setTransactionStatus(new BigDecimal("1"));
								distrInfo.setSignTime(time + "");
								if(distrInfo.update()) {
									this.setAttr("message", "1");
								}else {
									this.setAttr("message", "0");
									this.setAttr("errorMessage", "数据更新失败");
								}
							}catch (Exception e) {
								e.printStackTrace();
							}
						}
					}else {
						this.setAttr("message", "0");
						this.setAttr("errorMessage", "未查询到相关事项信息");
					}
				}
			}
		}
		renderJson();
	}
	
	/**
	 * 根据任务id和事项所属部门筛选任务清单
	 */
	public void filterDistr() {
		String taskId = this.getPara("taskId");
		String bussName = this.getPara("bussName");
		String deptId = this.getPara("departId");
		this.setAttr("taskId", taskId);
		this.setAttr("bussName", bussName);
		this.setAttr("deptId", deptId);
		
		TaskService tService = new TaskService();
		int page = this.getParaToInt("page", 1);

		Page<EbTaskDistribute> distrListPage = tService.getDistrsByDBID(taskId,deptId,page);
		List<EbTaskDistribute> distrList = distrListPage.getList();
		
		UserService uService = new UserService(null);
		DepartService dService = new DepartService();
		ItemService iService = new ItemService();
		
		for (int i = 0; i < distrList.size(); i++) {
			EbTaskDistribute tDistr = distrList.get(i);
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
			
			if(tDistr.getDistribTime() != null){
			    String time = tDistr.getDistribTime();
			    
			    long oldTime = Long.parseLong(time);
			    Date newTime = new Date(oldTime);
			    
			    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			    time = df.format(newTime);
			    
			    tDistr.setDistribTime(time);
			   }
			
			if(tDistr.getSignTime() != null){
			    String time = tDistr.getSignTime();
			    
			    long oldTime = Long.parseLong(time);
			    Date newTime = new Date(oldTime);
			    
			    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			    time = df.format(newTime);
			    
			    tDistr.setSignTime(time);
			   }
			if(tDistr.getTransactionTime() != null){
			    String time = tDistr.getTransactionTime();
			    
			    long oldTime = Long.parseLong(time);
			    Date newTime = new Date(oldTime);
			    
			    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			    time = df.format(newTime);
			    
			    tDistr.setTransactionTime(time);
			   }
			//判断事项是否有上传的图片,有则传递images对象列表
			ImgService imgSer = new ImgService();
			List<EbFileImg> images = imgSer.findImgByDistr(tDistr.getDistrId()+"");
			if(images.size()>0){
				tDistr.setIs_haveImg(1);//(0没有，1有)
				tDistr.setImgList(images);
			}else{
				tDistr.setIs_haveImg(0);//(0没有，1有)
			}
		}
		//page前
//		List<EbTaskDistribute> distrList = tService.getDistrsByDBID(taskId, deptId);
		this.setAttr("distrListPage", distrListPage);
		this.setAttr("distrList", distrList);
		
		List<DepartModel> deptList = tService.getDeptIds(taskId);
		this.setAttr("deptList", deptList);
		
		render(PathUtil.getIndexPath("taskDistri.html"));
	}
	
	/**
	 * 消息管理
	 */
	public void newsManage(){
		MessageService mService = new MessageService();
		
		int page = this.getParaToInt("page", 1);

		Page<EbNews> newsListPage = mService.getAllNews(page);
		List<EbNews> newsList = newsListPage.getList();
		//page前
//		List<EbNews> newsList = mService.getAllNews();
		UserService uService = new UserService(null);
		ItemService itemService = new ItemService();
		for(int i = 0; i < newsList.size(); i++) {
			EbNews news = newsList.get(i);
			news.setItemInfo(itemService.getItemById(news.getItemId() + ""));
			
			news.setUserInfo(uService.findById(news.getUserId() + ""));
			
			if(news.getNewsTime() != null){
			    String time = news.getNewsTime();
			    
			    long oldTime = Long.parseLong(time);
			    Date newTime = new Date(oldTime);
			    
			    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			    time = df.format(newTime);
			    
			    news.setNewsTime(time);
			   }
		}
		
		this.setAttr("newsListPage", newsListPage);
		this.setAttr("newsList", newsList);
		render(PathUtil.getIndexPath("newsManage.html"));
	}
	
	/**
	 * 根据企业名称筛选反馈消息
	 * @throws ParseException 
	 */
	public void filterNews() throws ParseException {
		String instrTime = getPara("filterTime");// 筛选时间
		// 判断筛选时间
		long start_time = 0;
		// 当前时间毫秒数
		long end_time = new Date().getTime();
		if (instrTime != null) {
			if (instrTime.equals("week")) {
				// 本周周一时间毫秒数
				start_time = TimeUtil.getCurrentMondayTime();
			} else if (instrTime.equals("month")) {
				start_time = TimeUtil.getMinMonthDateTime();
			} else if (instrTime.equals("year")) {
				Date nowDate = new Date();
				SimpleDateFormat df = new SimpleDateFormat("yyyy");
				String date = df.format(nowDate) + "-01-01";
				start_time = TimeUtil.getMinYearDateTime(date);
			} else {
				start_time = 0;
			}
		}
		System.out.print("初始毫秒数：" + start_time + "; 当前日期毫秒数:" + end_time);

		String bussName = this.getPara("businessName");
		this.setAttr("bussName1", bussName);
		MessageService mService = new MessageService();

		int page = this.getParaToInt("page", 1);

		Page<EbNews> newsListPage;
		if (start_time == 0) {
			newsListPage = mService.getNewsByBussName(bussName, page);// 不根据时间筛选
		} else {
			newsListPage = mService.getNewsByBussNameTime(bussName, page,
					start_time, end_time);// 根据时间筛选
		}

		// Page<EbNews> newsListPage =
		// mService.getNewsByBussName(bussName,page);
		List<EbNews> newsList = newsListPage.getList();
		// page前
		// List<EbNews> newsList = mService.getNewsByBussName(bussName);
		UserService uService = new UserService(null);
		ItemService itemService = new ItemService();
		for (int i = 0; i < newsList.size(); i++) {
			EbNews news = newsList.get(i);
			news.setItemInfo(itemService.getItemById(news.getItemId() + ""));

			news.setUserInfo(uService.findById(news.getUserId() + ""));

			if (news.getNewsTime() != null) {
				String time = news.getNewsTime();

				long oldTime = Long.parseLong(time);
				Date newTime = new Date(oldTime);

				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				time = df.format(newTime);

				news.setNewsTime(time);
			}
		}
		this.setAttr("newsListPage", newsListPage);
		this.setAttr("newsList", newsList);
		if (bussName != null && !"".equals(bussName)
				&& !"null".equalsIgnoreCase(bussName)) {
			this.setAttr("bussName", bussName);
		}
		if (instrTime != null) {
			this.setAttr("time", instrTime);
		}

		render(PathUtil.getIndexPath("newsManage.html"));
	}
	
	/**
	 * 评价管理
	 */
	public void evaluateManage(){
		EvaluateService eService = new EvaluateService();
		int page = this.getParaToInt("page", 1);

		Page<EbEvaluate> evListPage = eService.getAllEV(page);
		List<EbEvaluate> evList = evListPage.getList();
		
		for(int i = 0; i < evList.size(); i++) {
			EbEvaluate eva = evList.get(i);
			
			if(eva.getEvaluateTime() != null){
			    String time = eva.getEvaluateTime();
			    
			    long oldTime = Long.parseLong(time);
			    Date newTime = new Date(oldTime);
			    
			    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			    time = df.format(newTime);
			    
			    eva.setEvaluateTime(time);
			}
			
		}
		//page前
//		List<EbEvaluate> evList = eService.getAllEV();
		this.setAttr("evListPage", evListPage);
		this.setAttr("evList", evList);
		render(PathUtil.getIndexPath("evaluateManage.html"));
	}
	
	/**
	 * 根据企业名称筛选评价
	 */
	public void filterEvInfo(){
		String bussName = this.getPara("bussName");
		EvaluateService eService = new EvaluateService();
		int page = this.getParaToInt("page", 1);

		Page<EbEvaluate> evListPage = eService.getEVsByBussName(bussName,page);
		List<EbEvaluate> evList = evListPage.getList();
		for(int i = 0; i < evList.size(); i++) {
			EbEvaluate eva = evList.get(i);
			
			if(eva.getEvaluateTime() != null){
			    String time = eva.getEvaluateTime();
			    
			    long oldTime = Long.parseLong(time);
			    Date newTime = new Date(oldTime);
			    
			    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			    time = df.format(newTime);
			    
			    eva.setEvaluateTime(time);
			   }
			
		}
		//page前
//		List<EbEvaluate> evList = eService.getEVsByBussName(bussName);
		this.setAttr("bussName", bussName);
		this.setAttr("evList", evList);
		this.setAttr("evListPage", evListPage);
		render(PathUtil.getIndexPath("evaluateManage.html"));
	}
	
	/**
	 * 审核部门评价view
	 */
	public void examEvaView() {
		String paramId = this.getPara("paramId");
		String bussName = this.getPara("bussName");
		
		EvaluateService evService = new EvaluateService();
		EbDeptEvainfo deptEvainfo = evService.getDtEVByPID(paramId);
		
		this.setAttr("bussName", bussName);
		this.setAttr("deptEvainfo", deptEvainfo);
		
		render(PathUtil.getIndexPath("examDepartEva.html"));
	}
	
	/**
	 * 审核部门评价
	 */
	public void examEva() {
		String bussName = this.getPara("bussName");
		String paramId = this.getPara("paramId");
		String grade = this.getPara("grade");
		String content = this.getPara("content");
		
		if(!MD5Util.isNumeric(grade) && Integer.parseInt(grade) > 0 && Integer.parseInt(grade) <= 5) {
			this.renderText("部门评分必须为1~5的数字");
		}else {
			EvaluateService evaService = new EvaluateService();
			EbDeptEvainfo evainfo = evaService.getDtEVByPID(paramId);
			if (evainfo != null) {
				boolean flag = false;
				String evaTime = System.currentTimeMillis() + "";
				//如果审核了则新插入一天部门评价数据
				evainfo.setGrade(new BigDecimal(grade));
				evainfo.setContent(content);
				evainfo.setExamStatus(new BigDecimal("1"));
				evainfo.setTime(evaTime);
				evainfo.setExamPId(evainfo.getParamId());
				evainfo.set("param_id", "dept_evaluate_seq.nextval");
				
				try {
					flag = evainfo.save();
				} catch (Exception e) {
					e.printStackTrace();
					flag = evainfo.save();
				}
				
				if(flag) {
					//获取所有已审核的部门评价
					List<EbDeptEvainfo> deptEvas = evaService.getDeptEvExmedList(evainfo.getEvaluateId() + "");
					if(deptEvas != null) {
						int compEvaGrade = 0;
						//重新计算一遍综合评分
						for (int i = 0; i < deptEvas.size(); i++) {
							compEvaGrade += deptEvas.get(i).getGrade().intValue();
						}
						compEvaGrade = compEvaGrade / deptEvas.size();
						double ceg = (double) compEvaGrade / deptEvas.size();
						if (ceg > compEvaGrade) {
							compEvaGrade += 1;
						}
						EbEvaluate eva = evaService.getEvaById(evainfo.getEvaluateId() + "");
						if (eva != null) {
							eva.setCompGrade(new BigDecimal(compEvaGrade));
							eva.setEvaluateTime(evaTime);
							try {
								eva.update();
							} catch (Exception e) {
								e.printStackTrace();
								LogUtil.log("修改综合评分失败");
							}
						}
					}
					
					this.redirect("/lookDepartEval?evaluateId=" + evainfo.getEvaluateId() + "&bussName=" + bussName);
				}else {
					this.renderText("审核失败！");
				}
			}else {
				this.renderText("未找到该部门评价！");
			}
		}
	}
	
	/**
	 * 查看部门 评价
	 */
	public void lookDepartEval(){
		String evId = this.getPara("evaluateId");
		String bussName = this.getPara("bussName");
		this.setAttr("bussName", bussName);
		
		EvaluateService eService = new EvaluateService();
		int page = this.getParaToInt("page", 1);
		
		Page<EbDeptEvainfo> deptEvsPage = eService.getDeptEvainfos(evId, page);
		List<EbDeptEvainfo> deptEvs = new ArrayList<EbDeptEvainfo>();
		if(deptEvsPage != null) {
			deptEvs = deptEvsPage.getList();
			for(int i = 0; i < deptEvs.size(); i++) {
				EbDeptEvainfo eva = deptEvs.get(i);
				
				if(eva.getTime() != null){//变换时间显示格式
				    String time = eva.getTime();
				    long oldTime = Long.parseLong(time);
				    Date newTime = new Date(oldTime);
				    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				    time = df.format(newTime);
				    eva.setTime(time);
				}
				
				EbDeptEvainfo deptEva = eService.getDtEVByEPID(eva.getParamId() + "");
				if(deptEva != null) {
					eva.setGrade(deptEva.getGrade());
					eva.setContent(deptEva.getContent());
					eva.setExamStatus(deptEva.getExamStatus());
				}
			}
		}
		
		this.setAttr("examStatus", "2");
		this.setAttr("deptEvs", deptEvs);
		this.setAttr("deptEvsPage", deptEvsPage);
		this.setAttr("evaluateId", evId);
		render(PathUtil.getIndexPath("lookDepartEval.html"));
	}
	
	/**
	 * 根据审核状态筛选部门评价
	 */
	public void filterDeptEv() {
		String evId = this.getPara("evaluateId");
		String examStatus = this.getPara("examStatus", "2");
		String bussName = this.getPara("bussName");
		int page = this.getParaToInt("page", 1);
		this.setAttr("evaluateId", evId);
		this.setAttr("bussName", bussName);
		this.setAttr("examStatus", examStatus);//审核状态 0 原始数据、1 已审核、-1 未审核、 空字符串 
		
		EvaluateService eService = new EvaluateService();
		Page<EbDeptEvainfo> deptEvsPage = eService.getDeptEvainfos(evId, page, examStatus);
		List<EbDeptEvainfo> deptEvs = new ArrayList<EbDeptEvainfo>();
		if(deptEvsPage != null) {
			deptEvs = deptEvsPage.getList();
			for(int i = 0; i < deptEvs.size(); i++) {
				EbDeptEvainfo eva = deptEvs.get(i);
				
				if(eva.getTime() != null){//变换时间显示格式
				    String time = eva.getTime();
				    long oldTime = Long.parseLong(time);
				    Date newTime = new Date(oldTime);
				    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				    time = df.format(newTime);
				    eva.setTime(time);
				}

				if (examStatus.equals("2")) {//所有数据
					EbDeptEvainfo deptEva = eService.getDtEVByEPID(eva.getParamId() + "");
					if(deptEva != null) {
						eva.setGrade(deptEva.getGrade());
						eva.setContent(deptEva.getContent());
						eva.setExamStatus(deptEva.getExamStatus());
					}
				}else if(examStatus.equals("0")) {
					EbDeptEvainfo deptEva = eService.getDtEVByEPID(eva.getParamId() + "");
					if(deptEva != null) {
						eva.setExamStatus(deptEva.getExamStatus());
					}
				}
				
			}
		}
		
		this.setAttr("deptEvs", deptEvs);
		this.setAttr("deptEvsPage", deptEvsPage);
		render(PathUtil.getIndexPath("lookDepartEval.html"));
	}
	
	/**
	 * 业务类型配置
	 */
	public void typeConfig(){
		BusinessService bService = new BusinessService();
		//获取所有企业类型表数据
		int page = this.getParaToInt("page", 1);

		Page<EbBusinessType> bTypeListPage = bService.getAllBsType(page);
		List<EbBusinessType> bTypeList = bTypeListPage.getList();
		//page前
		for(int i = 0; i < bTypeList.size(); i++) {
			EbBusinessType tye = bTypeList.get(i);
			
			if(tye.getRefreshTime() != null){
			    String time = tye.getRefreshTime();
			    
			    long oldTime = Long.parseLong(time);
			    Date newTime = new Date(oldTime);
			    
			    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			    time = df.format(newTime);
			    
			    tye.setRefreshTime(time);
			}
			
		}
		
		this.setAttr("bTypeListPage", bTypeListPage);
		this.setAttr("bTypeList", bTypeList);
		render(PathUtil.getIndexPath("typeConfig.html"));
	}
	
	/**
	 * 添加企业类型
	 */
	public void addType(){
		String typeName = getPara("typeName");
		
		BusinessService bService = new BusinessService();
		
		EbBusinessType bType = bService.getBsTypeByTypeName(typeName);
		
		if(bType==null){
			boolean addResult = bService.addBsType(typeName);
			
			if(addResult){
				//添加成功，直接跳转到业务类型配置显示页面
				setAttr("message", "1");
			}else{
				//添加失败，跳转会添加页面
				setAttr("message","0");
			}
		}else{
			//重复类型名称
			setAttr("message","4");
		}
		renderJson();
	}
	
	/**
	 * 修改企业类型
	 */
	public void resetType(){
		String typeName = getPara("typeName");
		String typeId = getPara("typeId");
		
		BusinessService bService = new BusinessService();
		boolean resetResult = bService.resetBsType(typeId, typeName);
		
		if(resetResult){
			//添加成功，直接跳转到业务类型配置显示页面
			setAttr("message", "3");
		}else{
			//添加失败，跳转会添加页面
			setAttr("message","2");
		}
		renderJson();
	}
	
	/**
	 * 删除企业类型
	 */
	public void deletBsType(){
		String typeId = getPara("typeId");
		
		BusinessService bService = new BusinessService();
		boolean deleResult = bService.deletBsType(typeId);
		
		if(deleResult){
			//删除成功，直接跳转到业务类型配置显示页面
			setAttr("dmessage", "1");
		}else{
			//删除失败
			setAttr("dmessage","0");
		}
		renderJson();
		
	}
	
	/**
	 * 事项配置管理
	 */
	public void applyConfig(){
		String typeId = getPara("typeId");
		String typeName = getPara("typeName");
		int page = this.getParaToInt("page", 1);
//		int dataSrc = this.getParaToInt("dataSource", 1);
		
		//获取该企业类型id下的所有事项id
		ItemService iService = new ItemService();

		Page<EbTaskList> iTemIdListPage = iService.findByTypeID(typeId,page);
		List<EbTaskList> iTemIdList = iTemIdListPage.getList();
//		List<EbTaskList> iTemIdList = iService.findByTypeID(typeId);
		
		//将获取到的事项ID分别读取其事项信息
		List<EbItem> itemInfoList = new ArrayList<EbItem>();
		
		if (iTemIdList.size() > 0) {
			for (int i = 0; i < iTemIdList.size(); i++) {
				EbTaskList itemTask = iTemIdList.get(i);
				
				int itemType = 0;
				if(itemTask.getItemType() != null) {
					itemType = itemTask.getItemType().intValue();//事项数据来源
				}
				
				EbItem item = null;
				if(itemType == 1) {
					//审批接口事项数据
					item = GovNetUtil.getItemInfo(itemTask.getItemId().toString());
				}else {
					//本地库事项数据
					item = iService.getItemById(itemTask.getItemId() + "");
				}

				if (item != null) {
					String departId = item.getDepartId() + "";

					EbDepart dInfo = new EbDepart();
					DepartService dService2 = new DepartService();

					// 将部门Id得到的部门信息存入到事项列表中
					dInfo = dService2.getDeptById(departId);
					item.setDept(dInfo);

					if (item.getRefreshTime() != null) {
						String time = item.getRefreshTime();

						long oldTime = Long.parseLong(time);
						Date newTime = new Date(oldTime);

						SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
						time = df.format(newTime);

						item.setRefreshTime(time);
					}
				
					//事项配置列表的t_id
					item.setT_id(itemTask.getT_id());

					itemInfoList.add(item);
				}
			}
		}
		
		//获取所有来自审批接口的部门信息，用于筛选事项
		List<EbDepart> dList = GovNetUtil.getDeparts(1, "9999", new HashMap<String, Integer>(), "");
		this.setAttr("dList", dList);
		
		//获取所有来自审批接口的事项，用于筛选事项
		List<EbItem> items = GovNetUtil.getEbItems("", 1, "99999", new HashMap<String, Integer>());
		this.setAttr("items", items);
		
		//列表参数传递
		this.setAttr("typeId", typeId);
		this.setAttr("typeName", typeName);
		this.setAttr("itemInfoList", itemInfoList);
		this.setAttr("iTemIdListPage", iTemIdListPage);
		render(PathUtil.getIndexPath("applyConfig.html"));
	}
	
	/**
	 * 为企业类型业务添加事项
	 */
	public void addApplyForBsType(){
		//传递上个页面的企业类型信息
		String typeId = getPara("typeId");
//		String typeName = getPara("typeName");
		
		int dataSrc = this.getParaToInt("dataSource", 1);
		
		//添加事项
		String itemId = getPara("itemId");
		TaskService tService = new TaskService();
		
		boolean addResult = tService.addItemIdForType(typeId, itemId, dataSrc);
		
		if(addResult){
			//跳转会对应的事项配置列表
//			redirect("/applyConfig?typeId="+typeId+"&typeName="+typeName);
			setAttr("message", "1");//添加成功
		}else{
			setAttr("message", "0");//添加失败
		}
		renderJson();
	}
	
	/**
	 * 删除企业类型配置的事项
	 */
	public void deletApplyForBsType(){
		String itemId = getPara("itemId");
		String typeId = getPara("typeId");
		String t_id = getPara("tId");
		
		TaskService tService = new TaskService();
		boolean deleResult = tService.deletBsType(itemId,typeId,t_id);
		
		if(deleResult){
			//删除成功，直接跳转到事项配置显示页面
			setAttr("dmessage", "1");
		}else{
			//删除失败
			setAttr("dmessage","0");
		}
		renderJson();
	}
	
	/**
	 * 测试
	 */
	@Clear
	public void test(){
		LogUtil.log("aaaaaaaaaaaaaaa");
		render(PathUtil.getIndexPath("index.html"));
	}
	
	/**
	 * 添加用户已写
	 */
	public void showAdd(){
		render(PathUtil.getIndexPath("addUser.html"));
	}
	
	/**
	 *设置签收人页面展示（此功能不需要2015-11-08 by 菲尼）
	 */
	@Clear
	public void setSignManShow(){
		String departId = getPara("departId"); //单位流水号
		String departName = getPara("departName");//单位名称
		String setUserId = getPara("setUserId");//默认签收人id
		
//		List<EbUserInfo> users = new ArrayList<EbUserInfo>();
//		
//		//根据部门名称获取该部门下的所有用户
//		users = new UserService(this).filterUsersList("",departId);
		
		Map<String, Integer> map = new HashMap<String, Integer>();
		//从审批接口中获取所有该单位下的用户
		List<EbUserInfo> users = GovNetUtil.getEbUserInfos(departId, 1, "9999", map);
		
		this.setAttr("setUserId",setUserId);
		this.setAttr("userList",users);
		this.setAttr("departName",departName);
		this.setAttr("departId",departId);
		render(PathUtil.getIndexPath("setSignMan.html"));
	}
	/**
	 * 设置签收人
	 */
	public void setSignMan(){
		String departId = getPara("departId");
		String userCode = getPara("userId");//收到的签收人userCode
		
		if (userCode == null || userCode.equals("") || userCode.equalsIgnoreCase("null")) {
			setAttr("message", "2");
		}else {
		
			//取的对应的depatId的表eb_depart的一行数据，把UserId字段对应设置进去
			EbDepart dpartInfo = new EbDepart();
			DepartService dService = new DepartService();
			dpartInfo = dService.getDeptById(departId);
			
			//判断本地部门是否有这个部门信息了
			if(dpartInfo!=null){
				//如果本地有该部门
				boolean result = false;
				
/************2015-11-08 by feini**************/
				Record r = Db.findFirst("select * from eb_depart t where t.depart_id=? and t.user_code =?", departId, userCode);
				if(r == null){
					//新建一个模型，重新保存部门签收人，双主键
					EbDepart dpartInfoNew = new EbDepart();
					
					dpartInfoNew.setDepartType(dpartInfo.getDepartType());//部门类型（ 1 招商局 、 2 政务办 、 3 其他部门）
					dpartInfoNew.setDepartId(dpartInfo.getDepartId());
					dpartInfoNew.setDepartName(dpartInfo.getDepartName());
					dpartInfoNew.setUserCode(userCode);//设置好部门签收人code
					dpartInfoNew.setRefreshTime(System.currentTimeMillis() + ""); //设置更新时间
	
					if(dpartInfo.getWorkTel()!=null){
						dpartInfoNew.setWorkTel(dpartInfo.getWorkTel());
					}
					if(dpartInfo.getWorkAddress()!=null){
						dpartInfoNew.setWorkTel(dpartInfo.getWorkAddress());
					}
					result = dpartInfoNew.save();
				}
/**********************************************/
//				result = dpartInfo.update();
	//			result = dService.setSignUser(dpartInfo.getDepartId()+"", userId);
				if(result){
					setAttr("message", "1");
				}else{
					setAttr("message", "2");
				}
			}else{
				//如果本地部门表中没有该部门信息则从审批接口中获取该部门信息
				boolean result = false;
				dpartInfo = GovNetUtil.getDeptInfo(departId);//从审批接口中查出来
				if(dpartInfo!=null){
					dpartInfo.setUserCode(userCode);//设置好部门签收人code
					dpartInfo.setRefreshTime(System.currentTimeMillis() + ""); //设置更新时间
					result = dpartInfo.save();
					if(result){
						setAttr("message", "1");
					}else{
						setAttr("message", "2");
					}
				}
			}
		}
		renderJson();
	}
	
	/**
	 * 取消用户部门签收人权限
	 */
	public void cancleSignMan(){
		String userCode = getPara("userCode");//原签收人code
		String departId = getPara("departId");//对应部门
		
		Record r = Db.findFirst("select * from eb_depart t where t.depart_id=? and t.user_code =?", departId, userCode);
		
		if(r == null){
			//该用户不为签收人，或者该用户部门不存在
			setAttr("message", "2");
		}else{
			Record d = Db.findFirst("select * from eb_task_distribute t where t.status=1 and t.transaction_status=1 and t.depart_id=? and t.Sign_user_code =?", 
					departId, userCode);
			if(d == null){
//				boolean result = Db.delete("eb_depart", r);
				boolean result = Db.deleteById("eb_depart", "depart_id, user_code", departId, userCode);
				if(result){
					//该用户取消成功
					setAttr("message", "1");
				}else{
					//该用户取消失败
					setAttr("message", "4");
				}
			}else{
				//该用户还有事项未办理完，不能取消
				setAttr("message", "3");
			}
		}
		renderJson();
	}
	
	/**
	 * 设置多个部门签收人
	 */
	public void setSignManMDept(){
		String userCode = getPara("userId");//收到的签收人userCode
		
		if (userCode == null || userCode.equals("") || userCode.equalsIgnoreCase("null")) {
			setAttr("message", "2");
		}else {
			EbUserInfo user = GovNetUtil.getUserByCode(userCode);
			if(user != null){
				String isMDept = user.getIsMDept()+"";//IS_MDEPT 是否多角色 1 是、0 否
				if(isMDept.equals("1")){
					int total = 0;
					
					String MDeptId = user.getMyDepartId();
					String[] mdptId =  MDeptId.split(",");
					for(int i = 0; i < mdptId.length; i++){
						String dptid = mdptId[i];
						
						Record r = Db.findFirst("select * from eb_depart t where t.depart_id=? and t.user_code =?", dptid, userCode);
						if(r == null){
							EbDepart dept = GovNetUtil.getDeptInfo(dptid);//从审批接口中查出来
							dept.setUserCode(userCode);
							dept.setRefreshTime(System.currentTimeMillis() + ""); //设置更新时间
							
							if(dept.save()){
								total++;
							}else{
								setAttr("message", "2");//设置失败
								break;
							}
						}else{
							setAttr("message", "2");//设置失败
							break;
						}
					}
					if(total == mdptId.length){
						setAttr("message", "1");//设置成功
					}
				}
			}else{
				setAttr("message", "2");//设置失败
			}
		}
		renderJson();
	}
	
	/**
	 * 取消用户多个部门签收人权限
	 */
	public void cancleSignManMDept(){
		String userCode = getPara("userCode");//原签收人code
		
		if (userCode == null || userCode.equals("") || userCode.equalsIgnoreCase("null")) {
			//用户id不存在
			setAttr("message", "2");
		}else {
			EbUserInfo user = GovNetUtil.getUserByCode(userCode);
			if(user != null){
				String isMDept = user.getIsMDept()+"";//IS_MDEPT 是否多角色 1 是、0 否
				if(isMDept.equals("1")){
					int total = 0;
					
					String MDeptId = user.getMyDepartId();
					String[] mdptId =  MDeptId.split(",");
					for(int i = 0; i < mdptId.length; i++){
						String dptid = mdptId[i];
						boolean canCancle = false;//是否能删除
						
						Record r = Db.findFirst("select * from eb_depart t where t.depart_id=? and t.user_code =?", dptid, userCode);
						if(r != null){
							Record d = Db.findFirst("select * from eb_task_distribute t where t.status=1 and t.transaction_status=1 and t.depart_id=? and t.Sign_user_code =?", 
									dptid, userCode);
							if(d != null){
								//该用户还有事项未办理完，不能取消
								setAttr("message", "3");
								canCancle = false;
								break;
							}else{
								canCancle = true;
							}
						}else{
							//该用户不为签收人，或者该用户部门不存在
							setAttr("message", "2");
							canCancle = false;
							break;
						}
						
						//先判断，最后才执行删除语句
						if(canCancle){
							boolean result = Db.deleteById("eb_depart", "depart_id, user_code", dptid, userCode);
							if(result){
								//该用户取消成功
								total++;
								setAttr("message", "1");
							}else{
								//该用户取消失败
								setAttr("message", "4");
							}
						}
					}
					if(total == mdptId.length){
						setAttr("message", "1");//取消成功
					}
				}
			}else{
				//该用户不为签收人，或者该用户部门不存在
				setAttr("message", "2");
			}
		}
		renderJson();
	}
	
	/**
	 * 删除部门(未完成)
	 */
	public void deletDepart(){
		String departId = getPara("departId");
		
		DepartService dService = new DepartService();
		EbDepart d = dService.getDeptById(departId);
		if(d != null){
			d.setIsDelet(new BigDecimal(0));//被禁用
		
			if(d.update()){
				//删除成功，直接跳转到业务类型配置显示页面
				setAttr("message", "1");
			}else{
				//删除失败
				setAttr("message","0");
			}
		}
		renderJson();
		
	}
	
	/**
	 * 跳转到通知公告
	 */
	public void notice(){
		redirect("/view/noticeManage");
	}
	
}
