package cn.com.minstone.eBusiness.dao;

import java.util.List;

import com.jfinal.plugin.activerecord.Page;

import cn.com.minstone.eBusiness.model.EbUserInfo;
import cn.com.minstone.eBusiness.util.NullUtil;
import cn.com.minstone.eBusiness.util.PathUtil;

public class UserDao extends BaseDao {
	
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
	 * 获取本系统所有的本地用户userPage
	 * @param page 页数
	 * @return Page<EbUserInfo>
	 */
	public Page<EbUserInfo> findAllUsers2(int page) {
		String sql="from " + table + " where is_delet = 1 and role_type in (2,3,4) order by user_id";
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
	 * 根据用户类型和部门筛选用户
	 * @param roleType
	 * @param departId
	 * @param Page
	 * @param phoneNum
	 * @return page
	 */
	public Page<EbUserInfo> findByRD(String roleType, String departId, int page, String phoneNum) {
		String sql = "select * from " + table + " u where u.role_type = ? and u.depart_id = ? "
				+ "and u.user_account like '?%' and u.is_delet = ? order by user_id";
		if((departId==null || "".equals(departId)) && (roleType==null || "".equals(roleType))) {
			return findAllUsers(page);
		}else if((departId!=null && !"".equals(departId)) && (roleType!=null && !"".equals(roleType))) {
			sql = "from " + table + " u where u.role_type = "+ roleType +" and u.depart_id = "+ departId + 
					" and u.is_delet = 1 and u.user_account like '" + phoneNum + "%' order by user_id";
			Page<EbUserInfo> userInfoPage = EbUserInfo.dao
			.paginate(
					page, 
					PathUtil.MIN_PAGE_SIZE, 
					"select *", 
					sql);
			return userInfoPage;
		}else if(roleType!=null && !"".equals(roleType)) {
			sql = "from " + table + " u where u.role_type = "+ roleType + 
					" and u.is_delet = 1 and u.user_account like '" + phoneNum + "%' order by user_id";
			Page<EbUserInfo> userInfoPage = EbUserInfo.dao
			.paginate(
					page, 
					PathUtil.MIN_PAGE_SIZE, 
					"select *", 
					sql);
			return userInfoPage;
		}else if(departId!=null && !"".equals(departId)) {
			sql = "from " + table + " u where u.depart_id = "+ departId + 
					" and u.is_delet = 1 and u.user_account like '" + phoneNum + "%' order by user_id";
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
	 * 根据用户类型和姓名筛选用户,企业和领导用户
	 * @param roleType
	 * @param userName
	 * @param Page
	 * @param phoneNum
	 * @param bussName
	 * @return page
	 */
	public Page<EbUserInfo> findOtherByRD(String roleType, String userName, int page, String phoneNum,String bussName) {
		String sql = "select * from " + table + " u where u.role_type = ? and u.depart_id = ? "
				+ "and u.user_account like '?%' and u.is_delet = ? order by user_id";
		if((userName==null || "".equals(userName)) && (roleType==null || "".equals(roleType))) {
			return findAllUsers2(page);
		}else if((userName!=null && !"".equals(userName)) && (roleType!=null && !"".equals(roleType))) {
			sql = " from " + table + " u where u.role_type = "+ roleType +" and u.user_name like '"+ userName + 
					"%' and u.is_delet = 1 and u.user_account like '" + phoneNum + "%' order by user_id";
			Page<EbUserInfo> userInfoPage = EbUserInfo.dao
			.paginate(
					page, 
					PathUtil.MIN_PAGE_SIZE, 
					"select *", 
					sql);
			return userInfoPage;
		}else if(roleType != null && !"".equals(roleType)) {
			sql = " from " + table + " u where u.role_type = "+ roleType;
			if(phoneNum != null && !phoneNum.equals("") && !phoneNum.equalsIgnoreCase("null")) {
				sql += " and u.user_account like '" + phoneNum + "%'";
			}
			
			if(bussName != null && !bussName.equals("") && !bussName.equalsIgnoreCase("null")) {
				sql += " and u.business_name like '" + bussName + "%'";
			}
			
			sql += " order by user_id";
			
			Page<EbUserInfo> userInfoPage = EbUserInfo.dao
			.paginate(
					page, 
					PathUtil.MIN_PAGE_SIZE, 
					"select *", 
					sql);
			return userInfoPage;
		}else if(userName!=null && !"".equals(userName)) {
			sql = " from " + table + " u where u.user_name like '"+ userName + 
					"%' and u.is_delet = 1 and u.user_account like '" + phoneNum + "%' order by user_id";
			Page<EbUserInfo> userInfoPage = EbUserInfo.dao
			.paginate(
					page, 
					PathUtil.MIN_PAGE_SIZE, 
					"select *", 
					sql);
			return userInfoPage;
		}else if(bussName!=null && !"".equals(bussName)) {
			sql = " from " + table + " u where u.business_name like '"+ bussName + 
					"%' and u.is_delet = 1 and u.user_account like '" + phoneNum + "%' order by user_id";
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
	 * 筛选用户类型不为1（办事员）的用户
	 * @param roleType
	 * @param Page
	 * @param phoneNum
	 * @return page
	 */
	public Page<EbUserInfo> findByType(int page, String phoneNum, String userName, String bussName) {
		String sqlString = "from " + table + " where role_type != 1";
		
		if(phoneNum != null && !phoneNum.equals("") && !phoneNum.equalsIgnoreCase("null")) {
			sqlString += " and user_account like '" + phoneNum + "%'";
		}
		if(userName != null && !userName.equals("") && !userName.equalsIgnoreCase("null")) {
			sqlString += " and user_name like '" + userName + "%'";
		}
		if(bussName != null && !bussName.equals("") && !bussName.equalsIgnoreCase("null")) {
			sqlString += " and business_name like '" + bussName + "%'";
		}
		
		sqlString += " and is_delet = 1 order by user_id";
		
//		String sql = "from " + table + " u where u.role_type != 1 and u.user_account like '" + phoneNum + 
//				"%' and u.user_name like '%" + userName + "%' and u.business_name like '%" + bussName + 
//				"%' and u.is_delet = 1 order by user_id";
		
		Page<EbUserInfo> userInfoPage = EbUserInfo.dao
		.paginate(
				page, 
				PathUtil.MIN_PAGE_SIZE, 
				"select *", 
				sqlString);
		return userInfoPage;
	}
	
	/**
	 * 筛选用户类型,部门Id,用户名，电话的用户
	 * @param roleType
	 * @param Page
	 * @param userName
	 * @param phoneNum
	 * @param deptCityId 市级部门Id
	 * @return page
	 */
	public Page<EbUserInfo> findByCityType(int page,String roleType, String phoneNum, String userName, String deptCityId) {
		String sqlString = "from " + table + " where role_type != 0";
		
		if(phoneNum != null && !phoneNum.equals("") && !phoneNum.equalsIgnoreCase("null")) {
			sqlString += " and user_account like '" + phoneNum + "%'";
		}
		if(userName != null && !userName.equals("") && !userName.equalsIgnoreCase("null")) {
			sqlString += " and user_name like '" + userName + "%'";
		}
		if(deptCityId != null && !deptCityId.equals("") && !deptCityId.equalsIgnoreCase("null")) {
			sqlString += " and depart_id = '" + deptCityId + "'";
		}
		
		if(roleType != null && !roleType.equals("") && !roleType.equalsIgnoreCase("null")) {
			sqlString += " and role_type = '" + roleType + "'";
		}
		
		sqlString += " and is_delet = 1 order by user_id";
		
		Page<EbUserInfo> userInfoPage = EbUserInfo.dao
		.paginate(
				page, 
				PathUtil.MIN_PAGE_SIZE, 
				"select *", 
				sqlString);
		return userInfoPage;
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
	 * 根据用户code筛选区级用户
	 * @param userAccount
	 */
	public EbUserInfo findAreaUsersByCode(String userAccount) {
		EbUserInfo user = null;
		String sql = "select * from " + table + " u where u.user_account = ? and u.role_type != ? order by u.user_id desc";
		
		if (!NullUtil.isEmpty(userAccount)) {
			user = EbUserInfo.dao.findFirst(sql, userAccount, 4);
		}
		
		return user;
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
	 * 根据Code查找用户
	 * @param userCode 用户账号
	 */
	public EbUserInfo findByCode(String userCode){
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
	/**
	 * 根据用户账号查找用户
	 * @param userId
	 * @return
	 */
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
