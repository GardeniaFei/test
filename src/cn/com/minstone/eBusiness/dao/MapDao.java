package cn.com.minstone.eBusiness.dao;

import cn.com.minstone.eBusiness.model.EbMapFile;
import cn.com.minstone.eBusiness.util.StringUtil;

public class MapDao {

	/**
	 * 根据业务主键查地图图片
	 * @param mapCode
	 * @return
	 */
	public EbMapFile findMapByCode(String mapCode) {
		if (!StringUtil.isBlank(mapCode)) {
			return EbMapFile.dao.findFirst("select * from EB_MAP_FILE f where f.MAP_CODE = ?", mapCode);
		}
		
		return null;
	}
}
