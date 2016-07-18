package cn.com.minstone.eBusiness.dao;

import java.util.List;

import com.jfinal.plugin.activerecord.Page;

import cn.com.minstone.eBusiness.model.EbDeptEvainfo;
import cn.com.minstone.eBusiness.model.EbEvaluate;
import cn.com.minstone.eBusiness.util.PathUtil;

public class EvaluateDao extends BaseDao {

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
		String sql = " from eb_evaluate order By evaluate_time desc";
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
				+ "and exam_status = 0 order by eva_time desc", evId);
	}
	
	/**
	 * 根据评价id获取所有已审核的部门评价
	 * @param evId
	 * @return list
	 */
	public List<EbDeptEvainfo> findDeptEvExmedList(String evId) {
		return EbDeptEvainfo.dao.find("select * from eb_dept_evainfo where evaluate_id = ? "
				+ "and exam_param_id is not null order by eva_time desc", evId);
	}
	
	/**
	 * 根据部门评价paramId获取相关的部门评价
	 * @param evId
	 * @return list
	 */
	public EbDeptEvainfo findDtEVByPID(String paramId) {
		return EbDeptEvainfo.dao.findFirst("select * from eb_dept_evainfo where param_id = ?", paramId);
	}
	
	/**
	 * 根据审核评价paramId获取相关的部门评价
	 * @param examParamId
	 * @return
	 */
	public EbDeptEvainfo findDtEVByEPID(String examParamId) {
		return EbDeptEvainfo.dao.findFirst("select * from eb_dept_evainfo where exam_param_id = ?", examParamId);
	}
	
	/**
	 * 根据综合评价id获取评价信息
	 * @param evaluateId
	 * @return EbEvaluate
	 */
	public EbEvaluate findEvaById(String evaluateId) {
		return EbEvaluate.dao.findFirst("select * from EB_EVALUATE where evaluate_id = ?", evaluateId);
	}
	
	/**
	 * 根据评价id获取所有的部门评价
	 * @param evId
	 * @param page
	 * @return Page
	 */
	public Page<EbDeptEvainfo> findAllDeptEVs(String evId, int page) {
		String sql = " from eb_dept_evainfo where evaluate_id = " + evId + " and exam_status = 0 order by eva_time desc";
		Page<EbDeptEvainfo> devPage = EbDeptEvainfo.dao
			.paginate(
				page, 
				PathUtil.MIN_PAGE_SIZE, 
				"select *", 
				sql);
		return devPage;
	}
	
	/**
	 * 根据评价id获取所有的部门评价
	 * @param evId
	 * @param page
	 * @return Page
	 */
	public Page<EbDeptEvainfo> findAllDeptEVs(String evId, int page, String examStatus) {
		String sql = " from eb_dept_evainfo where exam_status = 0 and evaluate_id = " + evId + " order by eva_time desc";
		
		if(examStatus != null && !examStatus.equals("")) {
			if (examStatus.equals("0")||examStatus.equals("2")) {//原始数据
				sql = " from eb_dept_evainfo where exam_status = 0 and evaluate_id = " + evId + " order by eva_time desc";
			}else if (examStatus.equals("-1")) {//未审核
				sql = " from EB_DEPT_EVAINFO t where not exists " + 
						"(select * from EB_DEPT_EVAINFO where exam_status = 1 and exam_param_id = t.param_id) " + 
						"and exam_status = 0 and evaluate_id = " + evId + " order by eva_time desc";
			}else {//已审核
				sql = " from eb_dept_evainfo where exam_status = 1 and evaluate_id = " + evId + " order by eva_time desc";
			}
		}
		
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
}
