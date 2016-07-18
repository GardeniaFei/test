package cn.com.minstone.eBusiness.service;

import java.util.List;

import com.jfinal.plugin.activerecord.Page;

import cn.com.minstone.eBusiness.dao.EvaluateDao;
import cn.com.minstone.eBusiness.model.EbDeptEvainfo;
import cn.com.minstone.eBusiness.model.EbEvaluate;

public class EvaluateService extends BaseService {
	
	private EvaluateDao dao;
	
	public EvaluateService() {
		this.dao = new EvaluateDao();
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
	 * 根据综合评价id和审核状态获取部门评价列表
	 * @param evId
	 * @param page
	 * @return Page
	 */
	public Page<EbDeptEvainfo> getDeptEvainfos(String evId, int page, String examStatus) {
		if(evId == null || evId.equals("") || "null".equalsIgnoreCase(evId)) {
			return null;
		}
		return dao.findAllDeptEVs(evId, page, examStatus);
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
	 * 根据部门评价id部门评价
	 * @param paraId
	 * @return
	 */
	public EbDeptEvainfo getDtEVByPID(String paramId) {
		if(paramId == null || paramId.equals("") || "null".equalsIgnoreCase(paramId)) {
			return null;
		}
		return dao.findDtEVByPID(paramId);
	}
	
	/**
	 * 根据审核评价id部门评价
	 * @param examParamId
	 * @return 
	 */
	public EbDeptEvainfo getDtEVByEPID(String examParamId) {
		if(examParamId == null || examParamId.equals("") || "null".equalsIgnoreCase(examParamId)) {
			return null;
		}
		return dao.findDtEVByEPID(examParamId);
	}
	
	/**
	 * 根据综合评价id筛选评价
	 * @param evaluateId
	 * @return EbEvaluate
	 */
	public EbEvaluate getEvaById(String evaluateId) {
		if(evaluateId == null || evaluateId.equals("") || "null".equalsIgnoreCase(evaluateId)) {
			return null;
		}
		return dao.findEvaById(evaluateId);
	}

}
