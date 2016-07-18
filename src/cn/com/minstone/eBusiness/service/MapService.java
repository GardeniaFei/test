package cn.com.minstone.eBusiness.service;

import cn.com.minstone.eBusiness.dao.MapDao;
import cn.com.minstone.eBusiness.model.EbMapFile;

public class MapService {
	
	private MapDao dao;
	
	public MapService() {
		dao = new MapDao();
	}
	
	/**
	 * 根据业务主键查地图图片
	 * @param mapCode
	 * @return
	 */
	public EbMapFile findMapFile(String mapCode){
		return dao.findMapByCode(mapCode);
	}
}
