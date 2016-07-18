package cn.com.minstone.eBusiness.ctrl.inter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.com.minstone.eBusiness.MobileInter;
import cn.com.minstone.eBusiness.model.EbBusiness;
import cn.com.minstone.eBusiness.model.EbBussItem;
import cn.com.minstone.eBusiness.model.EbUserInfo;
import cn.com.minstone.eBusiness.service.inter.BussInterService;
import cn.com.minstone.eBusiness.service.inter.BussItemService;
import cn.com.minstone.eBusiness.service.inter.MsgInterService;
import cn.com.minstone.eBusiness.service.inter.UserInterService;
import cn.com.minstone.eBusiness.util.Config;
import cn.com.minstone.eBusiness.util.GovNetUtil;
import cn.com.minstone.eBusiness.util.MCubeAppConfig;
import cn.com.minstone.eBusiness.util.PushUtil;
import cn.com.minstone.eBusiness.util.StringUtil;
import cn.com.minstone.eBusiness.util.TimeUtil;

import com.jfinal.aop.Before;
import com.jfinal.aop.Clear;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.render.JsonRender;

@Before(MobileInter.class)
public class BussItemCtrl extends Controller {

	/**
	 * 新增企业新建事项
	 */
	public void addBItem() {
		String itemName = this.getPara("itemName"); //事项名称必填
		String bussId = this.getPara("bussId");
		String bitem_describe = this.getPara("describe");
		String userCode = Config.getSessionUserAccount(this);
		
		boolean success = false;
		String message = "新增失败";
		Hashtable<String, String> result = new Hashtable<String, String>();
		
		 if(itemName == null || itemName.equals("") || itemName.equalsIgnoreCase("null")) {
			 	message = "必须传递事项名称！";
			}else if(bussId == null || bussId.equals("") || bussId.equalsIgnoreCase("null")) {
				message = "必须传递企业Id！";
			}else{
				BussItemService bitemSer = new BussItemService();
				success = bitemSer.addBItem(itemName, userCode, bussId,bitem_describe);
				if(success){
					 message = "" ;
					 result.put("message", "新增事项成功！");
					 
					 //消息推送部分
					 UserInterService userSer = new UserInterService(this);
					 EbUserInfo user = userSer.findByCode(userCode);
						if(user == null){
							user = GovNetUtil.getUserByCode(userCode);
						}
					 BussInterService bussSer = new BussInterService();
					 EbBusiness buss = bussSer.findById(bussId);
					 String strTime = System.currentTimeMillis() + "";
					 MsgInterService msgService = new MsgInterService();
					 if(user != null && buss != null){
					 boolean flg = msgService.addNews(user, buss, "14", itemName, "企业新增事项", 
								strTime, "", itemName, "0", Config.getSessionZWBID(this) + "", 
								MCubeAppConfig.getInstance().zwb_name);
						if(flg) {
							String content ="企业："+buss.getBusinessName()+"新增事项";
							List<String> strAlias = new ArrayList<String>();
							strAlias.add(Config.ZWB);
							
							Hashtable<String, String> map = new Hashtable<String, String>();
							map.put("newsType", "14");
							map.put("content", content);
							map.put("status", "0");
							map.put("businessId", getCs(buss.getBusinessId() + ""));
							PushUtil.pushNotifyToTagert(strAlias, StringUtil.getJson(map));
						}
					 }
						
				}
			}
			
		this.setAttr("success", success);
		this.setAttr("result", result);
		this.setAttr("errorMsg", message);
		render(new JsonRender().forIE());
	}
	
	/**
	 * 签收企业新增的事项
	 */
	public void signBItem(){
		String bitemId  = this.getPara("bitemId");
		String userCode = Config.getSessionUserAccount(this);//签收的vip
		
		String Vuser_code = "";
		BussItemService bitemSer = new BussItemService();
		EbBussItem bItem = bitemSer.findBItemById(bitemId);
		if(bItem != null){
			Vuser_code = bItem.getVuser_code();
		}
		
		boolean success = false;
		String message = "签收失败";
		Hashtable<String, String> result = new Hashtable<String, String>();
		
		if(bitemId == null || bitemId.equals("") || bitemId.equalsIgnoreCase("null")) {
		 	message = "必须传递事项id！";
		}else if(!Vuser_code.equals(userCode) ) {
			message = "该用户没有权限！";
		}else{
			if(bItem != null){
				success = bitemSer.changeBItem(1, bItem);
			}
			if(success){
				 message = "" ;
				 result.put("message", "签收事项成功！");
				 
				//消息推送部分
				 UserInterService userSer = new UserInterService(this);
				 EbUserInfo user = userSer.findByCode(userCode);
					if(user == null){
						user = GovNetUtil.getUserByCode(userCode);
					}
				EbBusiness buss = null;
				if(bItem != null){
					 BussInterService bussSer = new BussInterService();
					 buss = bussSer.findById(bItem.getBuss_id()+"");
				}
				 String strTime = System.currentTimeMillis() + "";
				 MsgInterService msgService = new MsgInterService();
				 if(user != null && buss != null && bItem!=null){
				 boolean flg = msgService.addNews(user, buss, "15", bItem.getBItem_name(), "企业新增事项被签收", 
							strTime, "", bItem.getBItem_name(), "0", Config.getSessionZWBID(this) + "", 
							MCubeAppConfig.getInstance().zwb_name);
					if(flg) {
						String content ="企业："+buss.getBusinessName()+"新增事项被"+user.getUserName()+"签收";
						List<String> strAlias = new ArrayList<String>();
						strAlias.add(buss.getBusinessId() + "");
						
						Hashtable<String, String> map = new Hashtable<String, String>();
						map.put("newsType", "15");
						map.put("content", content);
						map.put("status", "0");
						map.put("businessId", getCs(buss.getBusinessId() + ""));
						PushUtil.pushNotifyToTagert(strAlias, StringUtil.getJson(map));
					}
				 }
			}
		}
		
		this.setAttr("success", success);
		this.setAttr("result", result);
		this.setAttr("errorMsg", message);
		render(new JsonRender().forIE());
	}
	
	/**
	 * 退回企业新增的事项
	 */
	public void backBItem(){
		String bitemId  = this.getPara("bitemId");
		String userCode = Config.getSessionUserAccount(this);//签收的vip
		
		String Vuser_code = "";
		BussItemService bitemSer = new BussItemService();
		EbBussItem bItem = bitemSer.findBItemById(bitemId);
		if(bItem != null){
			Vuser_code = bItem.getVuser_code();
		}
		
		boolean success = false;
		String message = "退回失败";
		Hashtable<String, String> result = new Hashtable<String, String>();
		
		if(bitemId == null || bitemId.equals("") || bitemId.equalsIgnoreCase("null")) {
		 	message = "必须传递事项id！";
		}else if(!Vuser_code.equals(userCode) ) {
			message = "该用户没有权限！";
		}else{
			if(bItem != null){
				success = bitemSer.changeBItem(2, bItem);
			}
			if(success){
				 message = "" ;
				 result.put("message", "退回事项成功！");
				 
				//消息推送部分
				 UserInterService userSer = new UserInterService(this);
				 EbUserInfo user = userSer.findByCode(userCode);
					if(user == null){
						user = GovNetUtil.getUserByCode(userCode);
					}
				EbBusiness buss = null;
				if(bItem != null){
					 BussInterService bussSer = new BussInterService();
					 buss = bussSer.findById(bItem.getBuss_id()+"");
				}
				 String strTime = System.currentTimeMillis() + "";
				 MsgInterService msgService = new MsgInterService();
				 if(user != null && buss != null && bItem!=null){
				 boolean flg = msgService.addNews(user, buss, "16", bItem.getBItem_name(), "企业新增事项被退回", 
							strTime, "", bItem.getBItem_name(), "0", Config.getSessionZWBID(this) + "", 
							MCubeAppConfig.getInstance().zwb_name);
					if(flg) {
						String content ="企业："+buss.getBusinessName()+"新增事项被"+user.getUserName()+"退回";
						List<String> strAlias = new ArrayList<String>();
						strAlias.add(buss.getBusinessId() + "");
						
						Hashtable<String, String> map = new Hashtable<String, String>();
						map.put("newsType", "16");
						map.put("content", content);
						map.put("status", "0");
						map.put("businessId", getCs(buss.getBusinessId() + ""));
						PushUtil.pushNotifyToTagert(strAlias, StringUtil.getJson(map));
					}
				 }
			}
		}
		
		this.setAttr("success", success);
		this.setAttr("result", result);
		this.setAttr("errorMsg", message);
		render(new JsonRender().forIE());
	}
	
	/**
	 * 获取事项列表
	 */
	public void getBItemList(){
		String bussId = this.getPara("bussId");
		String userCode = Config.getSessionUserAccount(this);//登录的vip
		
		UserInterService userSer = new UserInterService(this);
		EbUserInfo user = userSer.findByCode(userCode);
		if(user == null){
			user = GovNetUtil.getUserByCode(userCode);
		}
		
		boolean success = false;
		String message = "数据为空";
		List<Hashtable<String, String>> result = new ArrayList<Hashtable<String,String>>();
		
		BussItemService bitemSer = new BussItemService();
		List<EbBussItem> bItems = bitemSer.findBItem(bussId, "");
		
		if(user != null){
			String authority = user.getAuthority()+"";
			if(authority.equals(2) || authority.equals(7)){
				bItems = bitemSer.findBItem(bussId, user.getUserAccount());
			}
		}
		
		if(bItems.size()>0){
			success = true;
			message = "";
			for(int i = 0; i < bItems.size(); i++){
				EbBussItem bitem = bItems.get(i);
				Hashtable<String, String> resultHash = new Hashtable<String, String>();
				resultHash.put("bitemId", getCs(bitem.getBItem_id()+""));//事项id
				resultHash.put("bitemName", getCs(bitem.getBItem_name()));//事项名称
				resultHash.put("btranStatus", getCs(bitem.getBtran_status()+""));//事项状态0未签收，1已签收，2被退回
				resultHash.put("bussId", getCs(bitem.getBuss_id()+""));//相关企业id
				resultHash.put("bussName", getCs(bitem.getBuss_name()));
				resultHash.put("bitemType", getCs(bitem.get("bitem_type")+""));//获取事项类型1为省办事项，2为企业事项
				result.add(resultHash);
			}
		}else{
			success = true;
		}
		
		this.setAttr("success", success);
		this.setAttr("result", result);
		this.setAttr("errorMsg", message);
		render(new JsonRender().forIE());
	}
	
	/**
	 * 检查字符串是否为空，为空则传递“”
	 * @param str
	 * @return
	 */
	private String getCs(String str){
		if(str != null){
			return str;
		}
		return "";
	}
	
	/**
	 * 企业新增事项详情
	 */
	public void bItemInfo(){
		String bitemId  = this.getPara("bitemId");
		
		boolean success = false;
		String message = "获取失败";
		Hashtable<String, String> result = new Hashtable<String, String>();
		
		if(bitemId == null || bitemId.equals("") || bitemId.equalsIgnoreCase("null")) {
		 	message = "必须传递事项id！";
		}else{
			BussItemService bitemSer = new BussItemService();
			EbBussItem bitem = bitemSer.findBItemById(bitemId);
			if(bitem != null){
				success = true;
				message = "获取成功";
				result.put("bitemId", getCs(bitem.getBItem_id()+""));//事项id
				result.put("bitemName", getCs(bitem.getBItem_name()));//事项名称
				result.put("btranStatus", getCs(bitem.getBtran_status()+""));//事项状态0未签收，1已签收，2被退回
				result.put("bussId", getCs(bitem.getBuss_id()+""));//相关企业id
				result.put("bussName", getCs(bitem.getBuss_name()));
				result.put("describe", getCs(bitem.getBitem_describe()));//事项描述
				result.put("Vuser_code", getCs(bitem.getVuser_code()));//vip用户账户
				result.put("Buser_code", getCs(bitem.getBuser_code()));//企业用户账户
				result.put("tran_time", TimeUtil.changeTimeStyle(bitem.getSign_time()));//处理时间
				result.put("creat_time",  TimeUtil.changeTimeStyle(bitem.getCreat_time()));//签收时间
				result.put("province_user_name", getCs(bitem.get("province_user_name")+""));
				result.put("province_user_position", getCs(bitem.get("province_user_position")+""));
				result.put("province_user_tel", getCs(bitem.get("province_user_tel")+""));
				result.put("province_user_addresss", getCs(bitem.get("province_user_addresss")+""));
				result.put("bitem_type", getCs(bitem.get("bitem_type")+""));////1为省办事项，2为企业事项
				result.put("province_item_serial", getCs(bitem.get("province_item_serial")+""));
				
			}
		}
		
		this.setAttr("success", success);
		this.setAttr("result", result);
		this.setAttr("errorMsg", message);
		render(new JsonRender().forIE());
	}
	
//	/**
//	 * 添加省办的事项
//	 * @throws JSONException 
//	 */
//	@Clear
//	public void addProvince() throws JSONException{
//		//单个
//		String bussId = this.getPara("businessId");
//		String puserName = this.getPara("userName");
//		String puserPosion = this.getPara("userPosion");//用户职位
//		String puserTel = this.getPara("userTel");//用户电话
//		String puserAdrress = this.getPara("userAdrress");//用户地址
//		String itmes = this.getPara("items");
//		String message = "新增失败";
//		Hashtable<String, String> result = new Hashtable<String, String>();
//		boolean success = false;
//		
//		 if(itmes == null || itmes.equals("") || itmes.equalsIgnoreCase("null")){
//			 message = "事项不能为空";
//		 }else if(bussId == null || bussId.equals("") || bussId.equalsIgnoreCase("null")){
//			 message = "企业Id不能为空";
//		 }else if(puserName == null || puserName.equals("") || puserName.equalsIgnoreCase("null")){
//			 message = "联系人名不能为空";
//		 }else if(puserTel == null || puserTel.equals("") || puserTel.equalsIgnoreCase("null")){
//			 message = "联系人电话不能为空";
//		 }else{
//			 //分解数组json
//			String strCompEva = "{\"items\":" + itmes + "}";
//			JSONObject evalue = new JSONObject(strCompEva);
//			JSONArray taskEvas = (JSONArray) evalue.optJSONArray("items");
//			int total = 0;
//			for(int i = 0; i < taskEvas.length(); i++) {
//				JSONObject obj = taskEvas.optJSONObject(i);
//				if(obj != null) {
//					String itemName = obj.optString("itemName");//省办事项名称
//					String itemSerial = obj.optString("itemSerial");//省办事项流水号
//					boolean flag = saveSingleItem(itemName, itemSerial, bussId, puserName, puserTel, puserAdrress, puserPosion);
//					if(flag){
//						total ++;
//					}
//				}
//			}
//			if(total == taskEvas.length()){
//				message = "" ;
//				success = false;
//				result.put("message", "新增事项成功！");
//			}
//		 }
//		this.setAttr("success", success);
//		this.setAttr("result", result);
//		this.setAttr("errorMsg", message);
//		render(new JsonRender().forIE());
//	}
//	
//	/**
//	 * 保存省办事项
//	 * @param itemName
//	 * @param itemSerial
//	 * @param bussId
//	 * @param puserName
//	 * @param puserTel
//	 * @param puserAdrress
//	 * @param puserPosion
//	 */
//	@Clear
//	public boolean saveSingleItem(String itemName,String itemSerial,String bussId,
//			String puserName,String puserTel,String puserAdrress,String puserPosion){
//		
//			boolean result = false;
//		
//			String bitem_describe = "此事项为省办事项。";
//			String buserCode = "";
//			String vuserCode = "";
//			String bussName ="";
//			
//			BussInterService bussSer = new BussInterService();
//			EbBusiness buss = bussSer.findById(bussId);
//			if(buss != null){
//				buserCode = buss.getContactName();
//				vuserCode = buss.getService_admin();
//				bussName = buss.getBusinessName();
//			}
//			
//			EbBussItem bitem  = new EbBussItem();
//			bitem.set("bItem_id", "EB_BUSS_ITEM_seq.nextval");
//			bitem.setBItem_name(itemName);
//			bitem.setBitem_describe(bitem_describe);
//			bitem.setBuser_code(buserCode);
//			bitem.setBuss_id(new BigDecimal(bussId));//企业id
//			bitem.setBuss_name(bussName);
//			bitem.setVuser_code(vuserCode);//对应的vip用户
//			bitem.setBtran_status(new BigDecimal(0));
//			bitem.setCreat_time(System.currentTimeMillis() + "");//创建时间
//			bitem.setSign_time(System.currentTimeMillis() + "");//事项办理时间
//			bitem.set("province_user_name", puserName);
//			bitem.set("province_user_position", puserPosion);
//			bitem.set("province_user_tel", puserTel);
//			bitem.set("province_user_addresss", puserAdrress);
//			bitem.set("province_item_serial", itemSerial);
//			bitem.set("bitem_type", new BigDecimal(1));//1为省办事项，2为企业事项
//			
//		result = bitem.save();
//		 
//		return result;
//	}
//	
//	/**
//	 * 获取所有的企业(省办)
//	 */
//	@Clear
//	public void getAllBuss(){
//		BussInterService bussSer = new BussInterService();
//		Page<EbBusiness> bussPage = bussSer.getAllBussInfo(1);
//		List<EbBusiness> bussList = bussSer.getAllBussInfoList();
//				
//		boolean success = false;
//		String message = "数据为空";
//
//		if (bussList != null && !bussList.isEmpty()) {
//			success = true;
//			message = "成功!";
//			List<Hashtable<String, String>> result = new ArrayList<Hashtable<String, String>>();
//			for (int i = 0; i < bussList.size(); i++) {
//				EbBusiness buss = bussList.get(i);
//				Hashtable<String, String> resultHash = new Hashtable<String, String>();
//				// 企业Id
//				resultHash.put("businessId",
//						getCheckedString(buss.getBusinessId() + ""));
//				// //企业名称
//				resultHash.put("businessName",
//						getCheckedString(buss.getBusinessName() + ""));
//				//企业vip服务专员
//				resultHash.put("serviceAdmin",
//						getCheckedString(buss.getService_admin()));
//				resultHash.put("isAttention", "");// 办事员没有关注
//				resultHash.put("settleStatus", buss.getSettleStatus() + "");//企业落户状态 1 已落户 0 正在办理
//
//				result.add(resultHash);
//			}
//			this.setAttr("result", result);
//		}
//		setAttr("total_numb", bussPage.getTotalRow());
//		setAttr("success", success);
//		setAttr("message", message);
//		render(new JsonRender().forIE());
//	}
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
}
