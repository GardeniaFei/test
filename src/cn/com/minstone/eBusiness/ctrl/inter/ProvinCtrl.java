package cn.com.minstone.eBusiness.ctrl.inter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.com.minstone.eBusiness.ProvinceInter;
import cn.com.minstone.eBusiness.model.EbBusiness;
import cn.com.minstone.eBusiness.model.EbBussItem;
import cn.com.minstone.eBusiness.service.inter.BussInterService;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.render.JsonRender;

@Before(ProvinceInter.class)
public class ProvinCtrl extends Controller {
	
	/**
	 * 添加省办的事项
	 * @throws JSONException 
	 */
	public void addProvince() throws JSONException{
		//单个
		String bussId = this.getPara("businessId");
		String puserName = this.getPara("userName");
		String puserPosion = this.getPara("userPosion");//用户职位
		String puserTel = this.getPara("userTel");//用户电话
		String puserAdrress = this.getPara("userAdrress");//用户地址
		String itmes = this.getPara("items");
		String message = "新增失败";
		Hashtable<String, String> result = new Hashtable<String, String>();
		boolean success = false;
		
		 if(itmes == null || itmes.equals("") || itmes.equalsIgnoreCase("null")){
			 message = "事项不能为空";
		 }else if(bussId == null || bussId.equals("") || bussId.equalsIgnoreCase("null")){
			 message = "企业Id不能为空";
		 }else if(puserName == null || puserName.equals("") || puserName.equalsIgnoreCase("null")){
			 message = "联系人名不能为空";
		 }else if(puserTel == null || puserTel.equals("") || puserTel.equalsIgnoreCase("null")){
			 message = "联系人电话不能为空";
		 }else{
			 //分解数组json
			String strCompEva = "{\"items\":" + itmes + "}";
			JSONObject evalue = new JSONObject(strCompEva);
			JSONArray taskEvas = (JSONArray) evalue.optJSONArray("items");
			int total = 0;
			for(int i = 0; i < taskEvas.length(); i++) {
				JSONObject obj = taskEvas.optJSONObject(i);
				if(obj != null) {
					String itemName = obj.optString("itemName");//省办事项名称
					String itemSerial = obj.optString("itemSerial");//省办事项流水号
					boolean flag = saveSingleItem(itemName, itemSerial, bussId, puserName, puserTel, puserAdrress, puserPosion);
					if(flag){
						total ++;
					}
				}
			}
			if(total == taskEvas.length()){
				message = "" ;
				success = true;
				result.put("message", "新增事项成功！");
			}
		 }
		this.setAttr("success", success);
		this.setAttr("result", result);
		this.setAttr("errorMsg", message);
		render(new JsonRender().forIE());
	}
	
	/**
	 * 保存省办事项
	 * @param itemName
	 * @param itemSerial
	 * @param bussId
	 * @param puserName
	 * @param puserTel
	 * @param puserAdrress
	 * @param puserPosion
	 */
	public boolean saveSingleItem(String itemName,String itemSerial,String bussId,
			String puserName,String puserTel,String puserAdrress,String puserPosion){
		
			boolean result = false;
		
			String bitem_describe = "此事项为省办事项。";
			String buserCode = "";
			String vuserCode = "";
			String bussName ="";
			
			BussInterService bussSer = new BussInterService();
			EbBusiness buss = bussSer.findById(bussId);
			if(buss != null){
				buserCode = buss.getContactName();
				vuserCode = buss.getService_admin();
				bussName = buss.getBusinessName();
			}
			
			EbBussItem bitem  = new EbBussItem();
			bitem.set("bItem_id", "EB_BUSS_ITEM_seq.nextval");
			bitem.setBItem_name(itemName);
			bitem.setBitem_describe(bitem_describe);
			bitem.setBuser_code(buserCode);
			bitem.setBuss_id(new BigDecimal(bussId));//企业id
			bitem.setBuss_name(bussName);
			bitem.setVuser_code(vuserCode);//对应的vip用户
			bitem.setBtran_status(new BigDecimal(0));
			bitem.setCreat_time(System.currentTimeMillis() + "");//创建时间
			bitem.setSign_time(System.currentTimeMillis() + "");//事项办理时间
			bitem.set("province_user_name", puserName);
			bitem.set("province_user_position", puserPosion);
			bitem.set("province_user_tel", puserTel);
			bitem.set("province_user_addresss", puserAdrress);
			bitem.set("province_item_serial", itemSerial);
			bitem.set("bitem_type", new BigDecimal(1));//1为省办事项，2为企业事项
			
		result = bitem.save();
		 
		return result;
	}
	
	/**
	 * 获取所有的企业(省办)
	 */
	public void getAllBuss(){
		BussInterService bussSer = new BussInterService();
		Page<EbBusiness> bussPage = bussSer.getAllBussInfo(1);
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
		setAttr("total_numb", bussPage.getTotalRow());
		setAttr("success", success);
		setAttr("message", message);
		render(new JsonRender().forIE());
	}
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
