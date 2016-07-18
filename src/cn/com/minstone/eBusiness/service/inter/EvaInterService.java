package cn.com.minstone.eBusiness.service.inter;

import java.math.BigDecimal;
import java.util.List;

import com.jfinal.plugin.activerecord.Page;

import cn.com.minstone.eBusiness.dao.inter.EvaluateInterDao;
import cn.com.minstone.eBusiness.model.EbDeptEvainfo;
import cn.com.minstone.eBusiness.model.EbEvaluate;
import cn.com.minstone.eBusiness.service.BaseService;

public class EvaInterService extends BaseService {
	
	private EvaluateInterDao dao;
	
	public EvaInterService() {
		this.dao = new EvaluateInterDao();
	}
	
	/**
	 * 获取所有的综合评价
	 * @return list
	 */
	public List<EbEvaluate> getAllEVList() {
		List<EbEvaluate> list = dao.findAllEVList();
		return list;
	}
	
	/**
	 * 获取所有的综合评价
	 * @param page
	 * @return Page
	 */
	public Page<EbEvaluate> getAllEV(int page) {
		Page<EbEvaluate> list = dao.findAllEV(page);
		return list;
	}
	
	/**
	 * 根据企业名称筛选企业综合评价信息
	 * @param bussName
	 * @return Page
	 */
	public List<EbEvaluate> getEVsByBussNameList(String bussName) {
		if(bussName == null || bussName.equals("") || "null".equalsIgnoreCase(bussName)) {
			return null;
		}
		return dao.findEVsByBussNameList(bussName);
	}
	
	/**
	 * 根据企业id获取综合评价信息
	 * @param bussName
	 * @return Page
	 */
	public EbEvaluate getEVsByBussId(String bussId) {
		if(bussId == null || bussId.equals("") || "null".equalsIgnoreCase(bussId)) {
			return null;
		}
		return dao.findEVsByBussId(bussId);
	}
	
	/**
	 * 根据企业名称筛选企业综合评价信息
	 * @param bussName
	 * @param page
	 * @return Page
	 */
	public Page<EbEvaluate> getEVsByBussName(String bussName,int page) {
		if(bussName == null || bussName.equals("") || "null".equalsIgnoreCase(bussName)) {
			return getAllEV(page);
		}
		return dao.findEVsByBussName(bussName,page);
	}
	
	/**
	 * 根据综合评价id获取部门评价列表
	 * @param evId
	 * @return list
	 */
	public List<EbDeptEvainfo> getDeptEvainfosList(String evId) {
		if(evId == null || evId.equals("") || "null".equalsIgnoreCase(evId)) {
			return null;
		}
		return dao.findAllDeptEVsList(evId);
	}
	
	/**
	 * 根据综合评价id获取部门评价列表
	 * @param evId
	 * @param page
	 * @return Page
	 */
	public Page<EbDeptEvainfo> getDeptEvainfos(String evId,int page) {
		if(evId == null || evId.equals("") || "null".equalsIgnoreCase(evId)) {
			return null;
		}
		return dao.findAllDeptEVs(evId,page);
	}
	
	/**
	 * 根据综合评价id获取未审核的部门评价列表
	 * @param evId
	 * @return list
	 */
	public List<EbDeptEvainfo> getDeptEvSourceList(String evId) {
		if(evId == null || evId.equals("") || "null".equalsIgnoreCase(evId)) {
			return null;
		}
		return dao.findDeptEvSourceList(evId);
	}
	
	/**
	 * 根据综合评价id获取已审核的部门评价列表
	 * @param evId
	 * @return list
	 */
	public List<EbDeptEvainfo> getDeptEvExmedList(String evId) {
		if(evId == null || evId.equals("") || "null".equalsIgnoreCase(evId)) {
			return null;
		}
		return dao.findDeptEvExmedList(evId);
	}
	
	/**
	 * 根据综合评价id和部门id筛选部门评价列表
	 * @param evId
	 * @param deptId
	 * @return list
	 */
	public List<EbDeptEvainfo> getDtEVsByEDid(String evId, String deptId) {
		if(evId == null || evId.equals("") || "null".equalsIgnoreCase(evId)) {
			return null;
		}else if(deptId == null || deptId.equals("") || "null".equalsIgnoreCase(deptId)) {
			return getDeptEvainfosList(evId);
		}
		return dao.findAllDeptEVsList(evId);
	}
	
	/**
	 * 根据综合评价id和部门id筛选部门评价
	 * @param businessId
	 * @param deptId
	 * @return EbDeptEvainfo
	 */
	public EbDeptEvainfo getDtEVByBDid(String businessId, String deptId) {
		if(deptId == null || deptId.equals("") || "null".equalsIgnoreCase(deptId)) {
			return null;
		}
		return dao.findDeptEVByBDid(businessId, deptId);
	}
	
	/**
	 * 根据企业id筛选评价
	 * @param businessId
	 * @return EbEvaluate
	 */
	public EbEvaluate getEvaByBid(String businessId) {
		if(businessId == null || businessId.equals("") || "null".equalsIgnoreCase(businessId)) {
			return null;
		}
		return dao.findEvaByBId(businessId);
	}
	
	/**
	 * 进行综合评价
	 * @param businessId
	 * @param compGrade
	 * @param evaTime
	 * @return
	 */
	public EbEvaluate evalComp(String businessId, String compGrade, String evaTime,String bussName) {
		EbEvaluate evaInfo = new EbEvaluate();
		
		evaInfo.set("evaluate_id", "evaluat_seq.nextval");
		evaInfo.set("business_id", Integer.parseInt(businessId));
		evaInfo.set("comp_grade", Integer.parseInt(compGrade));
		evaInfo.set("evaluate_time", evaTime);
		evaInfo.set("business_name", bussName);
		return dao.evaComp(evaInfo);
	}
	
	/**
	 * 进行部门评价
	 * @param evaluateId
	 * @param grade
	 * @param content
	 * @param departId
	 * @param evaTime
	 * @return
	 */
	public EbDeptEvainfo evaDepart(String evaluateId, String grade, String content, String departId, 
			String evaTime,String deptName, String businessId, String businessName) {
		EbDeptEvainfo evaDept = new EbDeptEvainfo();
		
		evaDept.set("param_id", "dept_evaluate_seq.nextval");
		evaDept.set("evaluate_id", Integer.parseInt(evaluateId)); //评价id
		evaDept.set("grade", Integer.parseInt(grade));//部门评分
		evaDept.set("content", content);//评价内容
		evaDept.set("depart_id", departId);//评价的部门名称
		evaDept.set("eva_time", evaTime);//评价时间
		evaDept.set("depart_name", deptName);//评价的部门名称
		evaDept.set("business_id", businessId);//评价企业id
		evaDept.set("business_name", businessName);//评价企业名称
		evaDept.setExamStatus(new BigDecimal("0"));
		return dao.evaDepart(evaDept);
	}

}
