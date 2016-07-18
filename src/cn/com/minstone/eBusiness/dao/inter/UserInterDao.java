package cn.com.minstone.eBusiness.dao.inter;

import java.util.List;

import com.jfinal.plugin.activerecord.Page;

import cn.com.minstone.eBusiness.model.EbUserInfo;
import cn.com.minstone.eBusiness.util.PathUtil;

public class UserInterDao extends BaseInterDao {
	
	private static final String table = "EB_USER_INFO";
	
	/**
	 * 获取本系统所有的userPage
	 * @param page 页数
	 * @return Page<EbUserInfo>
	 */
	public Page<EbUserInfo> findAllUsers(int page) {
		String sql="from " + table + " where is_delet = 1 order by user_id";
		Page<EbUserInfo> userInfoPage = EbUserInfo.dao
				.paginate(
						page, 
						PathUtil.MIN_PAGE_SIZE, 
						"select *", 
						sql);
		return userInfoPage;
	}
	
	/**
	 * 获取本系统所有的userPage
	 * @return List<EbUserInfo>
	 */
	public List<EbUserInfo> findAllUsersList() {
		return EbUserInfo.dao.find("select * from " + table + " where is_delet = 1 order by user_id");
	}
	
	/**
	 * 根据用户权限获取用户列表
	 * @param auth 用户权限
	 * @return List<EbUserInfo>
	 */
	public List<EbUserInfo> findAllUserByAuth(String auth) {
		return EbUserInfo.dao.find("select * from " + table + " where is_delet = 1 and authority = ? order by user_id",auth);
	}
	
	/**
	 * 根据用户类型和部门筛选用户
	 * @param roleType
	 * @param departId
	 * @param Page
	 * @return page
	 */
	public Page<EbUserInfo> findByRD(String roleType, String departId, int page) {
		String sql = "select * from " + table + " u where u.role_type = ? and u.depart_id = ? and u.is_delet = ? order by user_id";
		if((departId==null || "".equals(departId)) && (roleType==null || "".equals(roleType))) {
			return findAllUsers(page);
		}else if((departId!=null && !"".equals(departId)) && (roleType!=null && !"".equals(roleType))) {
			sql = "from " + table + " u where u.role_type = "+ roleType +" and u.depart_id = "+ departId +" and u.is_delet = 1 order by user_id";
			Page<EbUserInfo> userInfoPage = EbUserInfo.dao
			.paginate(
					page, 
					PathUtil.MIN_PAGE_SIZE, 
					"select *", 
					sql);
			return userInfoPage;
		}else if(roleType!=null && !"".equals(roleType)) {
			sql = "from " + table + " u where u.role_type = "+ roleType +" and u.is_delet = 1 order by user_id";
			Page<EbUserInfo> userInfoPage = EbUserInfo.dao
			.paginate(
					page, 
					PathUtil.MIN_PAGE_SIZE, 
					"select *", 
					sql);
			return userInfoPage;
		}else if(departId!=null && !"".equals(departId)) {
			sql = "from " + table + " u where u.depart_id = "+ departId +" and u.is_delet = 1 order by user_id";
			Page<EbUserInfo> userInfoPage = EbUserInfo.dao
			.paginate(
					page, 
					PathUtil.MIN_PAGE_SIZE, 
					"select *", 
					sql);
			return userInfoPage;
		}
		return null;
	}
	
	/**
	 * 根据用户类型和部门筛选用户
	 * @param roleType
	 * @param departId
	 * @return list
	 */
	public List<EbUserInfo> findByRDList(String roleType, String departId) {
		String sql = "select * from " + table + " u where u.role_type = ? and u.depart_id = ? and u.is_delet = ? order by user_id";
		if((departId==null || "".equals(departId)) && (roleType==null || "".equals(roleType))) {
			return EbUserInfo.dao.find("select * from " + table + " where u.is_delet = ? order by user_id","1");
		}else if((departId!=null && !"".equals(departId)) && (roleType!=null && !"".equals(roleType))) {
			sql = "select * from " + table + " u where u.role_type = ? and u.depart_id = ? and u.is_delet = ? order by user_id";
			return EbUserInfo.dao.find(sql, roleType, departId,"1");
		}else if(roleType!=null && !"".equals(roleType)) {
			sql = "select * from " + table + " u where u.role_type = ? and u.is_delet = ? order by user_id";
			return EbUserInfo.dao.find(sql, roleType,"1");
		}else if(departId!=null && !"".equals(departId)) {
			sql = "select * from " + table + " u where u.depart_id = ? and u.is_delet = ? order by user_id";
			return EbUserInfo.dao.find(sql, departId,"1");
		}
		return null;
	}
	
	/**
	 * 根据sid查找用户
	 * @param sid
	 * @return
	 */
	public EbUserInfo findBySid(String sid){
		return EbUserInfo.dao.findFirst("select * from " + table + " u where u.sid = ?", sid);
	}
	
	/**
	 * 根据企业Id获取企业用户
	 * @param bussId
	 * @return
	 */
	public List<EbUserInfo> findByBussid(String bussId){
		return EbUserInfo.dao.find("select * from " + table + " u where business_id = ?", bussId);
	}
	
	/**
	 * 根据Code查找用户
	 * @param userCode 用户账号
	 */
	public EbUserInfo findByCode(String userCode){
		return EbUserInfo.dao.findFirst("select * from " + table + " u where u.user_account = ? and is_delet = 1", userCode);
	}
	
	/**
	 * 根据Code和区市级角色筛选用户
	 * @param userCode 用户账号
	 * @param roleType 区市级办事人员角色
	 */
	public EbUserInfo findByCode(String userCode, int roleType){
		return EbUserInfo.dao.findFirst("select * from " + table + 
				" u where u.user_account = ? and is_delet = 1 and role_type = ?", 
				userCode, roleType);
	}
	
	/**
	 * 根据Code查找用户
	 * @param userCode 用户账号
	 * @param departId 部门id
	 */
	public EbUserInfo findByCD(String userCode, String departId){
		return EbUserInfo.dao.findFirst("select * from " + table + " u where u.user_account = ? and u.depart_id = ? and is_delet = 1", 
				userCode, departId);
	}
	
	/**
	 * 根据部门筛选查找用户(去重)
	 * @param departId 部门id
	 */
	public List<EbUserInfo> findByD(String departId){
		return EbUserInfo.dao.find("select * from " + table + " u where u.depart_id = ? and is_delet = 1", 
				 departId);
	}
	/**
	 * 根据userCode和企业id查找企业用户
	 * @param userCode 用户账号
	 * @param businessId 企业id
	 */
	public EbUserInfo findByCoBID(String userCode, String businessId){
		return EbUserInfo.dao.findFirst("select * from " + table + " u where u.user_account = ? and u.business_id = ?", userCode, businessId);
	}
	
	/**
	 * 根据Code查找本地非办事员用户
	 * @param userCode 用户账号
	 */
	public EbUserInfo findByCR(String userCode){
		return EbUserInfo.dao.findFirst("select * from " + table + " u where u.user_account = ? and u.role_type != 1 and is_delet = 1", userCode);
	}
	
	/**
	 * 根据userCode进行登录验证
	 * @param userCode 用户账号
	 */
	public EbUserInfo findLoginByCR(String userCode){
		return EbUserInfo.dao.findFirst("select * from " + table + " u where u.user_account = ? and is_delet = 1", userCode);
	}
	
	/**
	 * 根据用户账号查找用户
	 * @param userCode
	 * @return
	 */
	public EbUserInfo findByUserCode(String userCode){
		return EbUserInfo.dao.findFirst("select * from " + table + " u where u.user_account = ? and is_delet = 1", userCode);
	}
	
	public EbUserInfo findByUserId(String userId) {
		return EbUserInfo.dao.findFirst("select * from " + table + " u where u.user_id = ? and is_delet = 1", userId);
	}
	
	/**
	 * 更新用户数据
	 * @param userInfo
	 * @return
	 */
	public boolean updateUserInfo(EbUserInfo userInfo){
		return userInfo.update();
	}
	
	/**
	 * 添加用户、一般为企业或领导用户
	 * @param userInfo
	 * @return
	 */
	public boolean addUserInfo(EbUserInfo userInfo) {
		userInfo.set("is_delet", "1");
		boolean flag = false;
		try {
			flag = userInfo.save();
		} catch (Exception e) {
			e.printStackTrace();
			flag = userInfo.save();
		}
		
		return flag;
	}
	
	/**
	 * 删除用户(已不用真的删除)
	 * @param userInfo
	 * @return
	 */
	public boolean deleteUserInfo(EbUserInfo userInfo) {
		return userInfo.delete();
	}
	
}
