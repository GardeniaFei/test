package cn.com.minstone.eBusiness.dao;

import cn.com.minstone.eBusiness.model.EbCenter;

public class CenterDao {
     
	public EbCenter findCenter(){
		String sql = "select * from EB_CENTER";
		return EbCenter.dao.findFirst(sql);
	}
}
