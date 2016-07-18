package cn.com.minstone.eBusiness.ctrl.inter;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import cn.com.minstone.eBusiness.model.EbBusiness;
import cn.com.minstone.eBusiness.model.EbCenter;
import cn.com.minstone.eBusiness.model.EbCompTask;
import cn.com.minstone.eBusiness.model.EbInformation;
import cn.com.minstone.eBusiness.model.EbMapFile;
import cn.com.minstone.eBusiness.service.CenterService;
import cn.com.minstone.eBusiness.service.InformationService;
import cn.com.minstone.eBusiness.service.MapService;
import cn.com.minstone.eBusiness.service.TaskService;
import cn.com.minstone.eBusiness.service.inter.BussInterService;
import cn.com.minstone.eBusiness.util.NullUtil;
import cn.com.minstone.eBusiness.util.TimeUtil;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.render.JsonRender;

public class MapInterCtrl extends Controller {
	
	private String getCheckedString(String str){
		if((str != null) && (!str.equals("null"))){
			return str;
		}
		return "无";
	}
	
	/**
	 * 64.琶洲中心展示信息
	 */
	public void getCenter(){
		CenterService censer = new CenterService();
		EbCenter center = censer.getCenter();
		
		boolean success = false;
		String message = "数据为空";
		if(center!=null){
			success = true;
			message = "成功!";
			Hashtable<String, String> result = new Hashtable<String, String>();
			//中心存储Id
			result.put("center_id", getCheckedString(center.getCenterId()+""));
			//介绍
			result.put("center_intro", getCheckedString(center.getCenterIntro()));
			//标语
			result.put("slogan", getCheckedString(center.getSlogan()));
			//地图图片主键
			result.put("map_code", getCheckedString(center.getMapCode()));
			
			result.put("refresh_time", TimeUtil.changeTimeStyle(center.getRefreshTime()));
			//地图url
			MapService imgSer = new MapService();
			EbMapFile map = imgSer.findMapFile(center.getMapCode());
			if(map!=null){
				String mapUrl = map.getMapUrl();
				result.put("map_url", getCheckedString(mapUrl));
			}else{
				result.put("map_url", "");
			}
			this.setAttr("result", result);
		}
		setAttr("success", success);
		setAttr("message", message);
		render(new JsonRender().forIE());
	}
	
	/**
	 * 错误传递
	 * @param error
	 */
	private void faultResult(String error) {
		this.setAttr("success", false);
		this.setAttr("result", null);
		this.setAttr("message", error);
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
		return "无";
	}
	
	/**
	 * 65。资讯列表
	 */
	public void getInforList(){
		int inforType = this.getParaToInt("type",1);//1中心资讯，2企业资讯
		String bussId = this.getPara("bussId");//企业Id
		int page = this.getParaToInt("page",1);
		int pageNumb = this.getParaToInt("pageNumb",5);//每页展示行数
		
		if(inforType == 2 && !NullUtil.isEmpty(bussId)){
			faultResult("请传递企业Id");
		}else if(inforType != 1 && inforType != 2){
			faultResult( "请传递正确的资讯类型参数！");
		}else{
			if(inforType == 1){//中心资讯列表
				setInforCenterList(page, pageNumb,1);
			}else if(inforType == 2){
				//企业资讯列表
				getInforbussList(page, pageNumb, bussId);
			}
		}
		render(new JsonRender().forIE());
	}
	
	/**
	 * 中心资讯列表
	 */
	public void setInforCenterList(int page, int pageNumb, int inforType){
		boolean success = false;
		String message = "数据为空";
		
		InformationService inforSer = new InformationService();
		Page<EbInformation> inforsPage = inforSer.getInformationByType(inforType+"",page, pageNumb);
		List<EbInformation> infors = inforsPage.getList();
		
		List<Hashtable<String, String>> result = new ArrayList<Hashtable<String,String>>();
		if(infors.size()>0){
			success = true;
			message = "";
			for(int i = 0; i < infors.size(); i++){
				EbInformation infor = infors.get(i);
				Hashtable<String, String> resultHash = new Hashtable<String, String>();
				resultHash.put("inforId", getCs(infor.getInformationId()+""));//资讯Id
				resultHash.put("inforTitle", getCs(infor.getInformationTile()));//主题
				resultHash.put("inforType", getCs(infor.getInformationType()+""));//类型 1中心资讯，2企业资讯
				
				String newTime = TimeUtil.getTimeStyle(infor.getRefreshTime(),"yyyy-MM-dd");
				resultHash.put("refreshTime", getCs(newTime));//时间
				
				resultHash.put("bussId", "无");//企业id
				resultHash.put("bussName", "无");//企业名称
				
				result.add(resultHash);
			}
			setAttr("result", result);
		}
		setAttr("success", success);
		setAttr("message", message);
		setAttr("current_page", inforsPage.getPageNumber());
		setAttr("total_numb", inforsPage.getTotalRow());
		setAttr("total_page", inforsPage.getTotalPage());
	}
	
	/**
	 * 获取企业对应的资讯列表
	 */
	public void getInforbussList(int page, int pageNumb, String bussId){
		boolean success = false;
		String message = "数据为空";
		
		InformationService inforSer = new InformationService();
		Page<EbInformation> inforsPage = inforSer.getInformationByBussId(bussId, page, pageNumb);
		List<EbInformation> infors = inforsPage.getList();
		
		List<Hashtable<String, String>> result = new ArrayList<Hashtable<String,String>>();
		if(infors.size()>0){
			success = true;
			message = "";
			for(int i = 0; i < infors.size(); i++){
				EbInformation infor = infors.get(i);
				Hashtable<String, String> resultHash = new Hashtable<String, String>();
				resultHash.put("inforId", getCs(infor.getInformationId()+""));//资讯Id
				resultHash.put("inforTitle", getCs(infor.getInformationTile()));//主题
				resultHash.put("inforType", getCs(infor.getInformationType()+""));//类型 1中心资讯，2企业资讯
				
				String newTime = TimeUtil.getTimeStyle(infor.getRefreshTime(),"yyyy-MM-dd");
				resultHash.put("refreshTime", getCs(newTime));//时间
				
				resultHash.put("bussId", bussId);//企业id
				//获取企业信息
				String bussName = "";
				BussInterService bussSer = new BussInterService();
				EbBusiness buss = bussSer.getBussById(bussId);
				if(buss != null){
					bussName = buss.getBusinessName();
				}
				resultHash.put("bussName", bussName);//企业名称
				
				result.add(resultHash);
			}
			setAttr("result", result);
		}
		setAttr("success", success);
		setAttr("message", message);
		setAttr("current_page", inforsPage.getPageNumber());
		setAttr("total_numb", inforsPage.getTotalRow());
		setAttr("total_page", inforsPage.getTotalPage());
	}
	
	/**
	 * 66.资讯详情
	 */
	public void getInfor(){
		String inforId = this.getPara("inforId");
		
		InformationService inforSer = new InformationService();
		EbInformation infor = inforSer.getInformationById(inforId);
		
		boolean success = false;
		String message = "数据为空";
		if(infor!=null){
			success = true;
			message = "成功!";
			Hashtable<String, String> result = new Hashtable<String, String>();
			result.put("inforId", getCs(infor.getInformationId()+""));//资讯Id
			result.put("inforTitle", getCs(infor.getInformationTile()));//主题
			result.put("inforContent", getCs(infor.getInformationContent()));//内容
			result.put("inforType", getCs(infor.getInformationType()+""));//类型 1中心资讯，2企业资讯
			
			String newTime = TimeUtil.getTimeStyle(infor.getRefreshTime(),"yyyy-MM-dd");
			result.put("refreshTime", getCs(newTime));//时间
			
			result.put("bussId", getCs(infor.getInformationBussId()+""));//企业id
			//获取企业信息
			String bussId = infor.getInformationBussId()+"";
			String bussName = "";
			BussInterService bussSer = new BussInterService();
			EbBusiness buss = bussSer.getBussById(bussId);
			if(buss != null){
				bussName = buss.getBusinessName();
			}
			result.put("bussName",bussName);//企业名称
			
			this.setAttr("result", result);
		}
		setAttr("success", success);
		setAttr("message", message);
		render(new JsonRender().forIE());
	}
	
	/**
	 * 已进驻企业办事列表
	 */
	public void getBussListShow(){
		int page = this.getParaToInt("page",1);
		int pageNumb = this.getParaToInt("pageNumb",5);//每页展示行数
		
		boolean success = false;
		String message = "数据为空";
		TaskService taskSer = new TaskService();
		Page<EbCompTask> taskPage = taskSer.getCompTaskPage(page, pageNumb);
		List<EbCompTask> infors = taskPage.getList();
		
		List<Hashtable<String, String>> result = new ArrayList<Hashtable<String,String>>();
		if(infors.size()>0){
			success = true;
			message = "";
			for(int i = 0; i < infors.size(); i++){
				EbCompTask infor = infors.get(i);
				Hashtable<String, String> resultHash = new Hashtable<String, String>();
				resultHash.put("bussId", getCs(infor.getBussId()));//企业Id
				resultHash.put("bussName",  getCs(infor.getBussName()));
				resultHash.put("comp_time",getCs(infor.getComp_time()));
				resultHash.put("item_name",getCs(infor.getItem_name()));
				resultHash.put("item_id",getCs(infor.getItem_id()));
				resultHash.put("distr_id",getCs(infor.getDistr_id()));
				result.add(resultHash);
			}
			setAttr("result", result);
		}
		setAttr("success", success);
		setAttr("message", message);
		setAttr("current_page", taskPage.getPageNumber());
		setAttr("total_numb", taskPage.getTotalRow());
		setAttr("total_page", taskPage.getTotalPage());
		render(new JsonRender().forIE());
	}
}
