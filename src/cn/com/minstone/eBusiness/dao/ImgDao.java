package cn.com.minstone.eBusiness.dao;

import java.util.List;

import cn.com.minstone.eBusiness.model.EbFileImg;
import cn.com.minstone.eBusiness.util.StringUtil;

public class ImgDao {
	public EbFileImg findImgByName(String imgName) {
		if (!StringUtil.isBlank(imgName)) {
			return EbFileImg.dao.findFirst("select * from EB_FILE_IMG f where f.IMAGE_NAME = ?", imgName);
		}
		
		return null;
	}
	
	/**
	 * 根据图片对应的任务Id得到图片列表
	 * @param distrId
	 * @return
	 */
	public List<EbFileImg> findImgByDistr(String distrId){
		if (!StringUtil.isBlank(distrId)) {
			return EbFileImg.dao.find("select * from EB_FILE_IMG f where f.DISTR_ID = ?", distrId);
		}
		
		return null;
	}
}
