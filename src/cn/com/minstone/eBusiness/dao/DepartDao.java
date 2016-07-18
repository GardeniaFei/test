package cn.com.minstone.eBusiness.dao;

import java.util.Date;
import java.util.List;

import com.jfinal.plugin.activerecord.Page;

import cn.com.minstone.eBusiness.model.EbDepart;
import cn.com.minstone.eBusiness.util.NullUtil;
import cn.com.minstone.eBusiness.util.PathUtil;

public class DepartDao extends BaseDao {
	
	private static final String table = "eb_depart";
	private static final String selSQL = "select * from eb_depart t where t.is_delet = 1 order by t.refresh_time DESC";
	
	/**
	 * 获取所有的部门
	 * @return list
	 */
	public List<EbDepart> findAllDeptList() {
		return EbDepart.dao.find(selSQL);
	}
	
	/**
	 * 获取所有的市级部门
	 * @return list
	 */
	public List<EbDepart> findAllCityDept() {
		return EbDepart.dao.find("select * from eb_depart d where d.depart_type = ? and d.is_delet = 1 order by d.refresh_time DESC",1);
	}
	
	/**
	 * 获取所有的部门
	 * @return Page
	 */
	public Page<EbDepart> findAllDept(int page) {
		String sql = " from eb_depart t where t.is_delet = 1 order by t.refresh_time DESC";
		Page<EbDepart> departPage = EbDepart.dao
			.paginate(
				page, 
				PathUtil.MIN_PAGE_SIZE, 
				"select *", 
				sql);
		return departPage;
	}

	/**
	 * 根据部门id获取部门
	 * @param departId 部门id
	 * @return Object<EbDepart>
	 */
	public EbDepart findById(String departId) {
		return EbDepart.dao.findFirst("select * from " + table + " d where d.depart_id = ?", departId);
	}
	
	/**
	 * 根据部门id获取部门
	 * @param departId 部门id
	 * @return List<EbDepart>
	 */
	public List<EbDepart> findByIdList(String departId) {
		return EbDepart.dao.find("select * from " + table + " d where d.depart_id = ?", departId);
	}
	
	/**
	 * 获取市级部门列表
	 * @param departLevel
	 * @param departId
	 * @return
	 */
	public List<EbDepart> findDeptsByType(String departLevel, String departName) {
		String sql = "select * from " + table + " t where t.is_delet = 1 and t.depart_type = " + departLevel;
		if (!NullUtil.isEmpty(departName)) {
			sql += " and t.depart_name = ?";
			sql += " order by t.refresh_time desc";
			
			return EbDepart.dao.find(sql, departName);
		}
		
		sql += " order by t.refresh_time desc";
		
		return EbDepart.dao.find(sql);
	}
	
	/**
	 * 设置部门签收人
	 */
	public boolean setUserById(String departId,String userId){
		Date date= new Date();
		String str = date.getTime()+"";
		
		boolean result = EbDepart.dao.findById(departId).set("refresh_time", str).update();
		boolean result2 = EbDepart.dao.findById(departId).set("user_id", userId).update();
		
		return result&&result2;
	}
	
	/**
	 * 根据部门名称获取部门对象
	 * @param departName
	 * @return
	 */
	public EbDepart findByDeptName(String departName) {
		return EbDepart.dao.findFirst("select * from " + table + 
				" d where d.depart_name = ? and d.depart_type = 1 and d.is_delet = 1", departName);
	}
	
	/**
	 * 对市级单位进行分页筛选
	 * @param departLevel
	 * @param departId
	 * @param pageIndex
	 * @return
	 */
	public Page<EbDepart> findDeptsPageByType(String departLevel, String departId, int pageIndex) {
		String sql = " from eb_depart d where d.depart_type = 1 and d.is_delet = 1";
		if (!NullUtil.isEmpty(departId)) {
			sql += " and d.depart_name = '" + departId + "'";
		}
		sql += " order by d.refresh_time desc";
		
		return EbDepart.dao.paginate(pageIndex, 
				PathUtil.MIN_PAGE_SIZE, "select *", sql);
	}
}
