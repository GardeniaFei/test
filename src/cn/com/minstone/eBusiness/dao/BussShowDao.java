package cn.com.minstone.eBusiness.dao;

import java.math.BigDecimal;

import cn.com.minstone.eBusiness.model.EbBussShow;

public class BussShowDao {

	/**
	 * 根据企业Id查询对应的展示信息
	 * @param bussId
	 * @return
	 */
	public EbBussShow getShowByBussId(BigDecimal bussId){
		String sql = "select * from EB_BUSS_SHOW where buss_id =?";
		return  EbBussShow.dao.findFirst(sql,bussId);
	}
}
