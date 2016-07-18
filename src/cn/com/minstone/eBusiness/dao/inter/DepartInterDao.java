package cn.com.minstone.eBusiness.dao.inter;

import java.util.Date;
import java.util.List;

import com.jfinal.plugin.activerecord.Page;

import cn.com.minstone.eBusiness.model.EbDepart;
import cn.com.minstone.eBusiness.util.PathUtil;

public class DepartInterDao extends BaseInterDao {
	
	private static final String table = "eb_depart";
	private static final String selSQL = "select * from eb_depart order by refresh_time DESC";
	
	/**
	 * 获取所有的部门
	 * @return list
	 */
	public List<EbDepart> findAllDeptList() {
		return EbDepart.dao.find(selSQL);
	}
	
	/**
	 * 获取所有的部门
	 * @return Page
	 */
	public Page<EbDepart> findAllDept(int page) {
		String sql = " from eb_depart order by refresh_time DESC";
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
	 * 设置部门签收人
	 */
	public boolean setUserById(String departId,String userId){
		Date date= new Date();
		String str = date.getTime()+"";
		
		boolean result = EbDepart.dao.findById(departId).set("refresh_time", str).update();
		boolean result2 = EbDepart.dao.findById(departId).set("user_id", userId).update();
		
		return result&&result2;
	}
}
