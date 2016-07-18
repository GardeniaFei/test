package cn.com.minstone.eBusiness.service;

import java.util.List;

import com.jfinal.plugin.activerecord.Page;

import cn.com.minstone.eBusiness.dao.DepartDao;
import cn.com.minstone.eBusiness.model.EbDepart;

public class DepartService extends BaseService {

	private DepartDao dao;
	
	public DepartService() {
		this.dao = new DepartDao();
	}
	
	/**
	 * 获取所有的部门
	 * @return list
	 */
	public List<EbDepart> getAllDeptList() {
		return dao.findAllDeptList();
	}
	
	/**
	 * 获取所有的部门
	 * @param page
	 * @return Page
	 */
	public Page<EbDepart> getAllDept(int page) {
		return dao.findAllDept(page);
	}
	
	/**
	 * 获取所有的市级部门
	 * @return list
	 */
	public List<EbDepart> getCityDept(){
		return dao.findAllCityDept();
	}
	
	/**
	 * 根据部门id获取部门信息
	 * @param departId
	 * @return
	 */
	public EbDepart getDeptById(String departId) {
		if(departId == null || departId.equals("") || "null".equalsIgnoreCase(departId)) {
			return null;
		}
		return dao.findById(departId);
	}
	/**
	 * 根据部门id获取部门信息
	 * @param departId
	 * @return
	 */
	public List<EbDepart> getDeptListById(String departId) {
		if(departId == null || departId.equals("") || "null".equalsIgnoreCase(departId)) {
			return null;
		}
		return dao.findByIdList(departId);
	}
	
	/**
	 * 设置部门的签收人
	 */
	public boolean setSignUser(String departId,String userId){
		
		return dao.setUserById(departId, userId);
	}
}
