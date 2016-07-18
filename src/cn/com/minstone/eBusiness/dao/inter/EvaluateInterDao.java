package cn.com.minstone.eBusiness.dao.inter;

import java.util.List;

import com.jfinal.plugin.activerecord.Page;

import cn.com.minstone.eBusiness.model.EbDeptEvainfo;
import cn.com.minstone.eBusiness.model.EbEvaluate;
import cn.com.minstone.eBusiness.util.PathUtil;

public class EvaluateInterDao extends BaseInterDao {

	/**
	 * 获取所有综合评价
	 * @return list
	 */
	public List<EbEvaluate> findAllEVList() {
		return EbEvaluate.dao.find("select * from eb_evaluate order By evaluate_id desc");
	}
	
	/**
	 * 获取所有综合评价
	 * @param page
	 * @return Page
	 */
	public Page<EbEvaluate> findAllEV(int page) {
		String sql = " from eb_evaluate where comp_grade > 3 and comp_grade != 0 order By evaluate_time desc";
		Page<EbEvaluate> evPage = EbEvaluate.dao
			.paginate(
				page, 
				PathUtil.MIN_PAGE_SIZE, 
				"select *", 
				sql);
		return evPage;
	}
	
	/**
	 * 根据企业名称筛选综合评价信息
	 * @param bussName
	 * @return list
	 */
	public List<EbEvaluate> findEVsByBussNameList(String bussName) {
		return EbEvaluate.dao.find("select * from eb_evaluate where business_name like '%" + 
				bussName + "%' order by evaluate_id desc");
	}
	
	/**
	 * 根据企业id获取到综合评价信息
	 * @param bussId
	 * @return EbEvaluate
	 */
	public EbEvaluate findEVsByBussId(String bussId) {
		return EbEvaluate.dao.findFirst("select * from eb_evaluate where business_id =" + 
				bussId);
	}
	/**
	 * 根据企业名称筛选综合评价信息
	 * @param bussName
	 * @param page
	 * @return Page
	 */
	public Page<EbEvaluate> findEVsByBussName(String bussName,int page) {
		String sql = " from eb_evaluate where business_name like '%" + bussName + "%' order By evaluate_id desc";
		Page<EbEvaluate> evPage = EbEvaluate.dao
			.paginate(
				page, 
				PathUtil.MIN_PAGE_SIZE, 
				"select *", 
				sql);
		return evPage;
	}
	
	/**
	 * 根据评价id获取所有的部门评价
	 * @param evId
	 * @return list
	 */
	public List<EbDeptEvainfo> findAllDeptEVsList(String evId) {
		return EbDeptEvainfo.dao.find("select * from eb_dept_evainfo where evaluate_id = ? order by param_id desc", evId);
	}
	
	/**
	 * 根据评价id获取所有未审核的部门评价
	 * @param evId
	 * @return list
	 */
	public List<EbDeptEvainfo> findDeptEvSourceList(String evId) {
		return EbDeptEvainfo.dao.find("select * from eb_dept_evainfo where evaluate_id = ? "
				+ "and exam_param_id is null order by eva_time desc", evId);
	}
	
	/**
	 * 根据评价id获取所有已审核的部门评价
	 * @param evId
	 * @return list
	 */
	public List<EbDeptEvainfo> findDeptEvExmedList(String evId) {
//		return EbDeptEvainfo.dao.find("select * from eb_dept_evainfo where evaluate_id = ? "
//				+ "and exam_param_id is not null order by eva_time desc", evId);
		return EbDeptEvainfo.dao.find("select * from eb_dept_evainfo where evaluate_id = ? "
				+ "and exam_status = 1 order by eva_time desc", evId);
	}
	
	/**
	 * 根据企业id和部门id获取部门评价
	 * @param businessId
	 * @param deptId
	 * @return EbDeptEvainfo
	 */
	public EbDeptEvainfo findDeptEVByBDid(String businessId, String deptId) {
		return EbDeptEvainfo.dao.findFirst("select * from eb_dept_evainfo where business_id = ? "
				+ "and depart_id = ?", businessId, deptId);
	}
	
	/**
	 * 根据企业id获取部门评价
	 * @param businessId
	 * @return EbEvaluate
	 */
	public EbEvaluate findEvaByBId(String businessId) {
		return EbEvaluate.dao.findFirst("select * from EB_EVALUATE where business_id = ?", businessId);
	}
	
	/**
	 * 根据评价id获取所有的部门评价
	 * @param evId
	 * @param page
	 * @return Page
	 */
	public Page<EbDeptEvainfo> findAllDeptEVs(String evId,int page) {
		String sql = " from eb_dept_evainfo where evaluate_id = "+ evId +" order by param_id desc";
		Page<EbDeptEvainfo> devPage = EbDeptEvainfo.dao
			.paginate(
				page, 
				PathUtil.MIN_PAGE_SIZE, 
				"select *", 
				sql);
		return devPage;
	}
	
	/**
	 * 根据评价id和部门id获取部门评价
	 * @param evId
	 * @param deptName
	 * @return list
	 */
	public EbDeptEvainfo findDeptEvsByDtName(String evId, String deptId) {
		return EbDeptEvainfo.dao.findFirst("select * from eb_dept_evainfo where evaluate_id = ? and depart_id = ?", evId, deptId);
	}
	
	/**
	 * 进行综合评价
	 * @param evaInfo
	 * @return
	 */
	public EbEvaluate evaComp(EbEvaluate evaInfo) {
		boolean flag = false;
		try {
			flag = evaInfo.save();
		} catch (Exception e) {
			e.printStackTrace();
			flag = evaInfo.save();
		}
		
		
		if(flag) {
			return evaInfo;
		}else {
			return null;
		}
	}
	
	/**
	 * 进行部门评价
	 * @param evaInfo
	 * @return
	 */
	public EbDeptEvainfo evaDepart(EbDeptEvainfo evaDept) {
		boolean flag = false;
		
		try {
			flag = evaDept.save();
		} catch (Exception e) {
			e.printStackTrace();
			flag = evaDept.save();
		}
		
		if(flag) {
			return evaDept;
		}else {
			return null;
		}
	}
}
