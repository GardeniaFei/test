package cn.com.minstone.eBusiness.service;


import java.util.List;

import cn.com.minstone.eBusiness.dao.ImgDao;
import cn.com.minstone.eBusiness.model.EbFileImg;

public class ImgService {
	private ImgDao dao;
	
	public ImgService() {
		dao = new ImgDao();
	}
	
	/**
	 * 根据图片名称找到图片
	 * @param imgName
	 * @return
	 */
	public EbFileImg findImg(String imgName){
		EbFileImg imgInfo = new EbFileImg();
		imgInfo = dao.findImgByName(imgName);
		
		return imgInfo;
	}
	
	public List<EbFileImg> findImgByDistr(String distrId){
		return dao.findImgByDistr(distrId);
	}
	
}
