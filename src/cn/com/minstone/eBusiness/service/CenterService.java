package cn.com.minstone.eBusiness.service;

import cn.com.minstone.eBusiness.dao.CenterDao;
import cn.com.minstone.eBusiness.model.EbCenter;

public class CenterService {

	private CenterDao dao;
	
	public CenterService() {
		dao = new CenterDao();
	}
	
	/**
	 * 找到唯一的中心展示信息
	 * @return
	 */
	public EbCenter getCenter(){
		return dao.findCenter();
	}
	
	/**
	 * 添加新的中心
	 * @param intro 简介
	 * @param mapCode 地图图片code
	 * @param slogan 标语
	 * @param is_first 是否是第一次 true：是第一次
	 * @return
	 */
	public boolean addCenter(String intro, String mapCode, String slogan, boolean is_first, EbCenter center){
		boolean result = false;
		
		center.setCenterIntro(intro);
		center.setSlogan(slogan);
		center.setMapCode(mapCode);
		center.setRefreshTime(System.currentTimeMillis()+"");
		
		if(is_first){
			center.set("center_id", "EB_CENTER_seq.nextval");
			result = center.save();
		}else{
			result = center.update();
		}
		
		return result;
	}
}
