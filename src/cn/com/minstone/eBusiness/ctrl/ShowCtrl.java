package cn.com.minstone.eBusiness.ctrl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cn.com.minstone.eBusiness.model.EbBussShow;
import cn.com.minstone.eBusiness.model.EbCenter;
import cn.com.minstone.eBusiness.model.EbInformation;
import cn.com.minstone.eBusiness.service.BussShowService;
import cn.com.minstone.eBusiness.service.CenterService;
import cn.com.minstone.eBusiness.service.InformationService;
import cn.com.minstone.eBusiness.util.NullUtil;
import cn.com.minstone.eBusiness.util.PathUtil;
import cn.com.minstone.eBusiness.util.TimeUtil;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.render.JsonRender;

public class ShowCtrl  extends Controller {

	/**
	 * 展示编辑
	 * @author feini 2016-05-18
	 * @param 首页动画展示使用
	 */
	public void viewShowCenter(){
		CenterService censer = new CenterService();
		
		EbCenter center = censer.getCenter();
		this.setAttr("center", center);
		render(PathUtil.getShowPath("showCenter.html"));
	}
	
	/**
	 * 添加中心展示信息
	 */
	public void addShowCenter(){
		String slogan = this.getPara("slogan");//琶洲中心标语
		String intro = this.getPara("intro");//中心简介
		String mapCode = this.getPara("mapCode");//初始化图片id
		
		boolean result = false;
		String message = "添加失败！";
		if(!NullUtil.isEmpty(intro) || !NullUtil.isEmpty(slogan) || !NullUtil.isEmpty(mapCode)){
			CenterService censer = new CenterService();
			
			EbCenter center = censer.getCenter();
			if(center != null){
				result = censer.addCenter(intro, mapCode, slogan, false, center);
			}else{
				center = new EbCenter();
				result = censer.addCenter(intro, mapCode, slogan, true, center);
			}
		}else{
			message = "数据传输不完整！";
		}
		
		this.setAttr("message", message);
		this.setAttr("result", result);
		render(new JsonRender().forIE());
	}
	
	/**
	 * 跳转到中心资讯管理列表
	 * @throws ParseException 
	 */
	public void viewCenterInfor() throws ParseException{
		int page = this.getParaToInt("page",1);
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
		InformationService inforSer = new InformationService();
		Page<EbInformation> inforPage = inforSer.getInformationByType("1", page);
		if(start_time != 0){
			inforPage = inforSer.getInforByTime(page, start_time, end_time, "1");
		}
		List<EbInformation> infors = inforPage.getList();
		if(infors.size()>0){
			for(int i=0;i<infors.size();i++){
				EbInformation infor = infors.get(i);
				String newTime = TimeUtil.getTimeStyle(infor.getRefreshTime(),"yyyy-MM-dd");
				infor.setRefreshTime(newTime);
			}
		}
		if (instrTime != null) {
			this.setAttr("time", instrTime);
		}
		this.setAttr("infors", infors);
		this.setAttr("inforPage", inforPage);
		render(PathUtil.getShowPath("centerInforList.html"));
	}
	
	/**
	 * 中心资讯添加页面
	 */
	public void inforCenterAddView(){
		render(PathUtil.getShowPath("inforCenterAddView.html"));
	}
	
	/**
	 * 添加中心资讯
	 */
	public void addCenterInfor(){
		String content = this.getPara("intro");//资讯内容
		String inforTitle = this.getPara("inforTitle");//资讯主题
		
		boolean result = false;
		String message = "添加失败！";
		if(!NullUtil.isEmpty(inforTitle) || !NullUtil.isEmpty(content)){
			InformationService inforSer = new InformationService();
			result = inforSer.saveCenterInfor(content, inforTitle);
			if(result){
				message = "添加成功";
			}
		}else{
			message = "数据传输不完整！";
		}
		
		this.setAttr("message", message);
		this.setAttr("result", result);
		render(new JsonRender().forIE());
	}
	
	/**
	 * 查看资讯中心详情
	 */
	public void lookCenterInfor(){
		String inforId = this.getPara("inforId");
		
		InformationService inforSer = new InformationService();
		EbInformation infor = inforSer.getInformationById(inforId);
		
		if(infor != null){
			String newTime = TimeUtil.getTimeStyle(infor.getRefreshTime(),"yyyy-MM-dd");
			infor.setRefreshTime(newTime);
		}
		this.setAttr("infor", infor);
		render(PathUtil.getShowPath("inforCenter.html"));
	}
	
	/**
	 * 展示中心资讯编辑
	 */
	public void viewCenterEditInfor(){
		String inforId = this.getPara("inforId");
		
		InformationService inforSer = new InformationService();
		EbInformation infor = inforSer.getInformationById(inforId);
		
		if(infor != null){
			String newTime = TimeUtil.getTimeStyle(infor.getRefreshTime(),"yyyy-MM-dd");
			infor.setRefreshTime(newTime);
		}
		this.setAttr("infor", infor);
		render(PathUtil.getShowPath("editCenterInfor.html"));
	}
	
	/**
	 * 编辑保存中心资讯
	 */
	public void editCenterInfor(){
		String inforId = this.getPara("inforId");
		String content = this.getPara("intro");//资讯内容
		String inforTitle = this.getPara("inforTitle");//资讯主题
		
		boolean result = false;
		String message = "添加失败！";
		if(!NullUtil.isEmpty(inforTitle) || !NullUtil.isEmpty(content)){
		
			InformationService inforSer = new InformationService();
			EbInformation infor = inforSer.getInformationById(inforId);
			
			if(infor != null){
				infor.setRefreshTime(System.currentTimeMillis()+"");
				infor.setInformationTitle(inforTitle);
				infor.setInformationContent(content);
				result=infor.update();
				if(result){
					message = "资讯修改成功";
				}
			}else{
				message = "此资讯不存在,请重试！";
			}
		}else{
			message="数据不完整！";
		}
		this.setAttr("message", message);
		this.setAttr("result", result);
		render(new JsonRender().forIE());
	}
	
	/**
	 * 删除资讯
	 */
	public void deletCenterInfor(){
		String inforId = this.getPara("inforId"); 
		boolean result = false;
		String message = "删除失败！";
		
		InformationService inforSer = new InformationService();
		EbInformation infor = inforSer.getInformationById(inforId);
		
		if(infor != null){
			result = infor.delete();
			if(result){
				message = "资讯删除成功";
			}
		}
		this.setAttr("message", message);
		this.setAttr("result", result);
		render(new JsonRender().forIE());
	}
	
	/**
	 * 展示企业编辑页面
	 */
	public void viewBussPublic(){
		String bussId = this.getPara("businessId");
		String bussName = this.getPara("businessName");
		
		BussShowService showSer = new BussShowService();
		EbBussShow bussShow = showSer.getShowByBussId(bussId);
		if(bussShow != null){
			this.setAttr("bussShow", bussShow);
		}
		this.setAttr("bussName", bussName);
		this.setAttr("bussId", bussId);
		render(PathUtil.getShowPath("viewBussPublic.html"));
	}
	
	/**
	 * 添加企业展示信息
	 */
	public void addBussShow(){
		int order_seq = this.getParaToInt("order_seq",0);
		String show_intro = this.getPara("show_intro");
		String map_code = this.getPara("map_code");
		String bussId = this.getPara("bussId");
		
		boolean result = false;
		String message = "添加失败！";
		
		if(!NullUtil.isEmpty(order_seq+"") || !NullUtil.isEmpty(show_intro) || !NullUtil.isEmpty(map_code) || !NullUtil.isEmpty(bussId)){
			BussShowService bussShowSer = new BussShowService();
			EbBussShow bussShow = bussShowSer.getShowByBussId(bussId);
			if(bussShow == null){//判断是否存在唯一企业展示信息
				bussShow = new EbBussShow();
				result = bussShowSer.addShow(bussId, order_seq, map_code, show_intro, true, bussShow);
			}else{
				result =  bussShowSer.addShow(bussId, order_seq, map_code, show_intro, false, bussShow);
			}
			if(result){
				message = "添加成功！";
			}
		}else{
			message="数据不完整！";
		}
		this.setAttr("message", message);
		this.setAttr("result", result);
		render(new JsonRender().forIE());
	}
	
	/**
	 * 删除企业展示
	 */
	public void deleteBussShow(){
		String show_id = this.getPara("show_id");
		boolean result = false;
		String message = "删除失败！";
		
		EbBussShow bussShow = EbBussShow.dao.findById(show_id);
		if(bussShow != null){
			result = bussShow.delete();
			if(result){
				message = "删除成功";
			}
		}
		this.setAttr("message", message);
		this.setAttr("result", result);
		render(new JsonRender().forIE());
	}
	
	/**
	 * 展示企业咨询
	 * @throws ParseException 
	 */
	public void viewBussInfor() throws ParseException{
		String bussId = this.getPara("bussId");
		String bussName = this.getPara("bussName");
		this.setAttr("bussName", bussName);
		this.setAttr("bussId", bussId);
		
		int page = this.getParaToInt("page",1);
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
		InformationService inforSer = new InformationService();
		Page<EbInformation> inforPage = inforSer.getInformationByBussId(bussId, page);
		if(start_time != 0){
			inforPage = inforSer.getInforByTime(page, start_time, end_time, "1");
		}
		List<EbInformation> infors = inforPage.getList();
		if(infors.size()>0){
			for(int i=0;i<infors.size();i++){
				EbInformation infor = infors.get(i);
				String newTime = TimeUtil.getTimeStyle(infor.getRefreshTime(),"yyyy-MM-dd");
				infor.setRefreshTime(newTime);
			}
		}
		if (instrTime != null) {
			this.setAttr("time", instrTime);
		}
		this.setAttr("infors", infors);
		this.setAttr("inforPage", inforPage);
		render(PathUtil.getShowPath("bussInforList.html"));
	}
	
	/**
	 * 添加企业资讯页面展示
	 */
	public void inforBussAddView(){
		String bussId = this.getPara("bussId");
		String bussName = this.getPara("bussName");
		
		this.setAttr("bussName", bussName);
		this.setAttr("bussId", bussId);
		
		render(PathUtil.getShowPath("inforBussAddView.html"));
	}
	
	/**
	 * 添加企业资讯
	 */
	public void addBussInfor(){
		String content = this.getPara("intro");//资讯内容
		String inforTitle = this.getPara("inforTitle");//资讯主题
		String bussId = this.getPara("bussId");
		
		boolean result = false;
		String message = "添加失败！";
		if(!NullUtil.isEmpty(inforTitle) || !NullUtil.isEmpty(content) || NullUtil.isEmpty(bussId)){
			InformationService inforSer = new InformationService();
			result = inforSer.saveBussInfor(content, inforTitle, bussId);
			if(result){
				message = "添加成功";
			}
		}else{
			message = "数据传输不完整！";
		}
		
		this.setAttr("message", message);
		this.setAttr("result", result);
		render(new JsonRender().forIE());
	}
	
	/**
	 * 查看企业资讯信息
	 */
	public void lookBussInfor(){
		String inforId = this.getPara("inforId");
		
		String bussName = this.getPara("bussName");
		this.setAttr("bussName", bussName);
		
		InformationService inforSer = new InformationService();
		EbInformation infor = inforSer.getInformationById(inforId);
		
		if(infor != null){
			String newTime = TimeUtil.getTimeStyle(infor.getRefreshTime(),"yyyy-MM-dd");
			infor.setRefreshTime(newTime);
		}
		this.setAttr("infor", infor);
		render(PathUtil.getShowPath("inforBuss.html"));
	}
	
	/**
	 * 编辑企业资讯信息
	 */
	public void viewBussEditInfor(){
		String inforId = this.getPara("inforId");
		String bussId = this.getPara("bussId");//企业ID
		String bussName = this.getPara("bussName");//企业ID
		this.setAttr("bussId", bussId);
		this.setAttr("bussName", bussName);
		
		InformationService inforSer = new InformationService();
		EbInformation infor = inforSer.getInformationById(inforId);
		
		if(infor != null){
			String newTime = TimeUtil.getTimeStyle(infor.getRefreshTime(),"yyyy-MM-dd");
			infor.setRefreshTime(newTime);
		}
		this.setAttr("infor", infor);
		render(PathUtil.getShowPath("editBussInfor.html"));
	}
	
	/**
	 * 编辑保存中心资讯
	 */
	public void editBussInfor(){
		String inforId = this.getPara("inforId");
		String content = this.getPara("intro");//资讯内容
		String inforTitle = this.getPara("inforTitle");//资讯主题
		String bussId = this.getPara("bussId");//企业ID
		
		boolean result = false;
		String message = "添加失败！";
		if(!NullUtil.isEmpty(inforTitle) || !NullUtil.isEmpty(content)){
		
			InformationService inforSer = new InformationService();
			EbInformation infor = inforSer.getInformationById(inforId);
			
			if(infor != null){
				infor.setRefreshTime(System.currentTimeMillis()+"");
				infor.setInformationTitle(inforTitle);
				infor.setInformationContent(content);
				result=infor.update();
				if(result){
					message = "资讯修改成功";
				}
			}else{
				message = "此资讯不存在,请重试！";
			}
		}else{
			message="数据不完整！";
		}
		this.setAttr("message", message);
		this.setAttr("result", result);
		render(new JsonRender().forIE());
	}
	
	/**
	 * 删除企业资讯
	 */
	public void deletBussInfor(){
		String inforId = this.getPara("inforId"); 
		boolean result = false;
		String message = "删除失败！";
		
		InformationService inforSer = new InformationService();
		EbInformation infor = inforSer.getInformationById(inforId);
		
		if(infor != null){
			result = infor.delete();
			if(result){
				message = "资讯删除成功";
			}
		}
		this.setAttr("message", message);
		this.setAttr("result", result);
		render(new JsonRender().forIE());
	}
}
