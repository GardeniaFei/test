package cn.com.minstone.eBusiness.service;

import java.math.BigDecimal;

import cn.com.minstone.eBusiness.dao.BussShowDao;
import cn.com.minstone.eBusiness.model.EbBussShow;

public class BussShowService {
	
	private BussShowDao dao;
	
	public BussShowService() {
		dao = new BussShowDao();
	}
	
	/**
	 * 根据企业Id获取展示信息
	 * @param bussId
	 * @return
	 */
	public EbBussShow getShowByBussId(String bussId){
		return dao.getShowByBussId(new BigDecimal(bussId));
	}
	
	/**
	 * 增加企业展示信息
	 * @param bussId
	 * @param order_seq 企业排序
	 * @param map_code 地图代号
	 * @param show_intro 简介
	 * @return
	 */
	public boolean addShow(String bussId, int order_seq, String map_code, String show_intro,boolean is_first,EbBussShow show){
		boolean result = false;
		show.setBuss_id(new BigDecimal(bussId));
		show.setMap_code(map_code);
		show.setOrder_seq(new BigDecimal(order_seq));
		show.setShow_intro(show_intro);
		show.setRefresh_time(System.currentTimeMillis()+"");
		if(is_first){
			show.set("show_id", "EB_SHOW_seq.nextval");
			result = show.save();
		}else{
			result = show.update();
		}
		return result;
	}
}
