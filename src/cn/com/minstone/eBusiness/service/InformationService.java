package cn.com.minstone.eBusiness.service;

import java.math.BigDecimal;

import com.jfinal.plugin.activerecord.Page;

import cn.com.minstone.eBusiness.dao.InfomationDao;
import cn.com.minstone.eBusiness.model.EbInformation;
import cn.com.minstone.eBusiness.model.EbNotice;

public class InformationService {
	
	private InfomationDao dao;
	
	public InformationService() {
		dao = new InfomationDao();
	}
	
	/**
	 * 根据资讯id获取信息详情
	 * @param inforId
	 * @return
	 */
	public EbInformation getInformationById(String inforId){
		return dao.findInformationById(inforId);
	}
	
	/**
	 * 根据企业id获取资讯，10行一页
	 * @param bussId
	 * @param page
	 * @return
	 */
	public Page<EbInformation> getInformationByBussId(String bussId, int page){
		return dao.findAllInfoByBussId(page, bussId);
	}
	
	/**
	 * 根据企业id获取资讯
	 * @param bussId
	 * @param page
	 * @param pageNumb 每页显示多少行
	 * @return
	 */
	public Page<EbInformation> getInformationByBussId(String bussId, int page,int pageNumb){
		return dao.findAllInfoByBussId(page, bussId, pageNumb);
	}
	
	/**
	 * 根据类型获取资讯，10行一页
	 * @param type 1是中心资讯，2是企业资讯
	 * @param page
	 * @return
	 */
	public Page<EbInformation> getInformationByType(String type, int page){
		return dao.findAllInfoByType(page, type);
	}
	
	/**
	 * 根据企业id获取资讯
	 * @param type 1是中心资讯，2是企业资讯
	 * @param page
	 * @param pageNumb 每页显示多少行
	 * @return
	 */
	public Page<EbInformation> getInformationByType(String type, int page, int pageNumb){
		return dao.findAllInfoByType(page, type, pageNumb);
	}
	
	/**
	 * 通过时间筛选对应类型的资讯
	 * @param page
	 * @param start_time
	 * @param end_time
	 *  @param type 1是中心资讯，2是企业资讯
	 * @return
	 */
	public Page<EbInformation> getInforByTime(int page,long start_time, long end_time, String type) {
		return dao.findInforByTime(page,start_time,end_time,type);
	}
	
	/**
	 * 保存中心资讯内容
	 * @param content
	 * @param inforTitle
	 * @return
	 */
	public boolean saveCenterInfor(String content, String inforTitle){
		EbInformation infor = new EbInformation();
		infor.set("INFORMATION_ID", "EB_INFORMATION_seq.nextval");
		infor.setInformationTitle(inforTitle);
		infor.setInformationContent(content);
		infor.setRefreshTime(System.currentTimeMillis()+"");
		infor.setInformationType(new BigDecimal(1));//1是中心资讯，2是企业资讯
		
		return infor.save();
	}
	
	/**
	 * 保存中心资讯内容
	 * @param content
	 * @param inforTitle
	 * @return
	 */
	public boolean saveBussInfor(String content, String inforTitle, String bussId){
		EbInformation infor = new EbInformation();
		infor.set("INFORMATION_ID", "EB_INFORMATION_seq.nextval");
		infor.setInformationTitle(inforTitle);
		infor.setInformationContent(content);
		infor.setInformationBussId(new BigDecimal(bussId));
		infor.setRefreshTime(System.currentTimeMillis()+"");
		infor.setInformationType(new BigDecimal(2));//1是中心资讯，2是企业资讯
		
		return infor.save();
	}
}
