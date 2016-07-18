package cn.com.minstone.eBusiness.dao;

import java.util.List;

import com.jfinal.plugin.activerecord.Page;

import cn.com.minstone.eBusiness.model.EbInformation;
import cn.com.minstone.eBusiness.model.EbNotice;
import cn.com.minstone.eBusiness.util.PathUtil;
import cn.com.minstone.eBusiness.util.StringUtil;

public class InfomationDao {

	/**
	 * 根据id查询资讯
	 * @param mapCode
	 * @return
	 */
	public EbInformation findInformationById(String informationId) {
		if (!StringUtil.isBlank(informationId)) {
			return EbInformation.dao.findFirst("select * from EB_INFORMATION f where f.INFORMATION_ID = ?", informationId);
		}
		return null;
	}
	
	/**
	 * 根据企业id查询资讯列表
	 * @param mapCode
	 * @return
	 */
	public List<EbInformation> findInformationByBussId(String bussId) {
		if (!StringUtil.isBlank(bussId)) {
			return EbInformation.dao.find("select * from EB_INFORMATION f where f.INFORMATION_BUSS_ID = ?", bussId);
		}
		return null;
	}
	
	/**
	 * 获取所有的企业资讯
	 * @param page
	 * @param bussId
	 * @return Page
	 */
	public Page<EbInformation> findAllInfoByBussId(int page, String bussId) {
		String sql = " from EB_INFORMATION t where t.INFORMATION_BUSS_ID = '" +bussId+"'";
		Page<EbInformation> inforPage = EbInformation.dao
			.paginate(
				page, 
				PathUtil.MIN_PAGE_SIZE, 
				"select *", 
				sql);
		return inforPage;
	}
	
	/**
	 * 获取所有的企业资讯
	 * @param page
	 * @param bussId
	 * @return Page
	 */
	public Page<EbInformation> findAllInfoByBussId(int page, String bussId,int pageNumb) {
		String sql = " from EB_INFORMATION t where t.INFORMATION_BUSS_ID = '" +bussId+"'";
		Page<EbInformation> inforPage = EbInformation.dao
			.paginate(
				page, 
				pageNumb, 
				"select *", 
				sql);
		return inforPage;
	}
	
	/**
	 * 根据资讯类型查询资讯列表
	 * @param type 1是中心资讯，2是企业资讯
	 * @return
	 */
	public List<EbInformation> findInformationByType(String type) {
		if (!StringUtil.isBlank(type)) {
			return EbInformation.dao.find("select * from EB_INFORMATION f where f.INFORMATION_TYPE = ?", type);
		}
		return null;
	}
	
	/**
	 * 获取所有的企业资讯
	 * @param page
	 * @param type 1是中心资讯，2是企业资讯
	 * @return Page
	 */
	public Page<EbInformation> findAllInfoByType(int page, String type) {
		String sql = " from EB_INFORMATION t where t.INFORMATION_TYPE = '" +type+"'";
		Page<EbInformation> inforPage = EbInformation.dao
			.paginate(
				page, 
				PathUtil.MIN_PAGE_SIZE, 
				"select *", 
				sql);
		return inforPage;
	}
	
	/**
	 * 获取所有的企业资讯
	 * @param page
	 * @param type 1是中心资讯，2是企业资讯
	 * @return Page
	 */
	public Page<EbInformation> findAllInfoByType(int page, String type, int pageNumb) {
		String sql = " from EB_INFORMATION t where t.INFORMATION_TYPE = '" +type+"'";
		Page<EbInformation> inforPage = EbInformation.dao
			.paginate(
				page, 
				pageNumb, 
				"select *", 
				sql);
		return inforPage;
	}
	
	/**
	 * 根据时间类型筛选资讯
	 * @param page
	 * @param start_time
	 * @param end_time
	 * @param type 1是中心资讯，2是企业资讯
	 * @return
	 */
	public Page<EbInformation> findInforByTime(int page, long start_time, long end_time, String type) {
		String sql  = " from EB_INFORMATION where refresh_time >= "+ start_time + " and refresh_time <= "+ end_time +" and INFORMATION_TYPE= '" +type+"' order by refresh_time DESC";
		
		Page<EbInformation> infors = EbInformation.dao
			.paginate(
				page, 
				PathUtil.MIN_PAGE_SIZE, 
				"select *", 
				sql);
		return infors;
	}
}
