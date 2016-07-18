package cn.com.minstone.eBusiness.dao;

import java.util.List;

import com.jfinal.plugin.activerecord.Page;

import cn.com.minstone.eBusiness.model.EbBusiness;
import cn.com.minstone.eBusiness.model.EbBusinessType;
import cn.com.minstone.eBusiness.util.PathUtil;

public class BusinessDao extends BaseDao {
	
	@SuppressWarnings("unused")
	private static final String table = "eb_business";
	
	private static final String selSQL = "select * from eb_business";

	/**
	 * 获取所有的企业
	 * @return list
	 */
	public List<EbBusiness> findAllList() {
		return EbBusiness.dao.find(selSQL + " order by business_id");
	}
	
	/**
	 * 获取所有的企业
	 * @param page
	 * @return Page
	 */
	public Page<EbBusiness> findAll(int page) {
		String sql = " from eb_business  order by business_id";
		Page<EbBusiness> bussPage = EbBusiness.dao
			.paginate(
				page, 
				PathUtil.MIN_PAGE_SIZE, 
				"select *", 
				sql);
		return bussPage;
	}
	
	/**
	 * 获取所有的企业类型
	 * @return list
	 */
	public List<EbBusinessType> findAllBsTypeList() {
		return EbBusinessType.dao.find("select * from eb_business_type t where t.is_delet = ? order by refresh_time DESC","1");
	}
	
	/**
	 * 获取所有的企业类型
	 * @param page 当前页面
	 * @return Page
	 */
	public Page<EbBusinessType> findAllBsType(int page) {
		String sql = " from eb_business_type t where t.is_delet = 1 order by refresh_time DESC";
		Page<EbBusinessType> bTypePage = EbBusinessType.dao
			.paginate(
				page, 
				PathUtil.MIN_PAGE_SIZE, 
				"select *", 
				sql);
		return bTypePage;
	}
	
	/**
	 * 根据企业类型id获取企业类型
	 * @param typeId 企业类型id
	 * @return Object<EbBusinessType>
	 */
	public EbBusinessType findBsTypeById(String typeId) {
		return EbBusinessType.dao.findFirst("select * from eb_business_type t where t.type_id = ? and t.is_delet = 1", typeId);
	}
	
	/**
	 * 根据企业类型名称获取企业类型
	 * @param typeName 企业类型名称
	 * @return Object<EbBusinessType>
	 */
	public EbBusinessType findBsTypeByName(String typeName) {
		return EbBusinessType.dao.findFirst("select * from eb_business_type t where t.type_name = ? and t.is_delet = 1", typeName);
	}
	
	/**
	 * 根据企业名称获取企业
	 * @param bussName
	 * @return
	 */
	public List<EbBusiness> findByBussName(String bussName) {
		return EbBusiness.dao.find(selSQL + " b where b.business_name like '%" + bussName + "%' order by business_id");
	}
	
	public EbBusiness findById(String businessId) {
		return EbBusiness.dao.findFirst(selSQL + " b where b.business_id = ? order by business_id", businessId);
	}
	
	/**
	 * 添加企业
	 * @param buss
	 * @return
	 */
	public boolean addBussInfo(EbBusiness buss) {
		boolean flag = false;
		try {
			flag = buss.save();
		} catch (Exception e) {
			e.printStackTrace();
			flag = buss.save();
		}
		return flag;
	}
	
	/**
	 * 筛选企业(根据企业类型和企业名称选择)
	 * @param typeId	企业类型
	 * @param bussName	企业名称
	 * @return list
	 */
	public List<EbBusiness> findByTpOrNameList(int typeId, String bussName) {
		if(typeId == -1 && (bussName == null || bussName.equals(""))) {
			return EbBusiness.dao.find(selSQL + " order by business_id");
		} else if(typeId == -1 && (bussName!=null && !"".equals(bussName))) {
			return EbBusiness.dao.find(selSQL + " b where b.business_name like '%" + bussName + "%' order by business_id");
		} else if(typeId != -1 && (bussName==null || "".equals(bussName))) {
			return EbBusiness.dao.find(selSQL + " b where b.type_id = ? order by business_id", typeId);
		} else {
			return EbBusiness.dao.find(selSQL + " b where b.type_id = ? and b.business_name like '%" + bussName + "%' order by business_id", typeId);
		}
	}
	
	/**
	 * 筛选企业(根据企业类型和企业名称选择)
	 * @param typeId	企业类型
	 * @param bussName	企业名称
	 * @param page
	 * @return Page
	 */
	public Page<EbBusiness> findByTpOrName(int typeId, String bussName, String contactName, String contactPhone, int page) {
		if(typeId == -1 && (bussName == null || bussName.equals(""))) {
			String sql = " from eb_business order by business_id";
			Page<EbBusiness> bussPage = EbBusiness.dao
					.paginate(
						page, 
						PathUtil.MIN_PAGE_SIZE, 
						"select *", 
						sql);
			return bussPage;
		} else if(typeId == -1 && (bussName!=null && !"".equals(bussName))) {
			String sql = " from eb_business b where b.business_name like '%" + bussName + "%' order by business_id";
			Page<EbBusiness> bussPage = EbBusiness.dao
					.paginate(
						page, 
						PathUtil.MIN_PAGE_SIZE, 
						"select *", 
						sql);
			return bussPage;
		} else if(typeId != -1 && (bussName==null || "".equals(bussName))) {
			String sql = " from eb_business b where b.type_id = "+ typeId +" order by business_id";
			Page<EbBusiness> bussPage = EbBusiness.dao
					.paginate(
						page, 
						PathUtil.MIN_PAGE_SIZE, 
						"select *", 
						sql);
			return bussPage;
		} else {
			String sql = " from eb_business b where b.type_id = "+ typeId +" and b.business_name like '%" + bussName + "%' order by business_id";
			Page<EbBusiness> bussPage = EbBusiness.dao
					.paginate(
						page, 
						PathUtil.MIN_PAGE_SIZE, 
						"select *", 
						sql);
			return bussPage;
		}
	}
	
	/**
	 * 筛选企业(根据企业类型和企业名称选择)
	 * @param typeId	企业类型
	 * @param bussName	企业名称
	 * @param contactName	联系人
	 * @param contactPhone	联系电话
	 * @param page
	 * @return Page
	 */
	public Page<EbBusiness> findByTpOrNameLinker(int typeId, String bussName, String contactName, String contactPhone, int page) {
		String sql = " from eb_business ";
		
		if(typeId != -1) {
			sql += "where type_id = " + typeId;
		}
		
		if(bussName != null) {
			if(sql.contains("where")) {
				sql += " and business_name like '%" + bussName + "%'";
			}else {
				sql += " where business_name like '%" + bussName + "%'";
			}
		}
		
		if(contactName != null) {
			if(sql.contains("where")) {
				sql += " and contact_name like '%" + contactName + "%'";
			}else {
				sql += " where contact_name like '%" + contactName + "%'";
			}
		}
		
		if(contactPhone != null) {
			if(sql.contains("where")) {
				sql += " and contact_phone like '%" + contactPhone + "%'";
			}else {
				sql += " where contact_phone like '%" + contactPhone + "%'";
			}
		}
		
		sql += " order by add_time";
		
		Page<EbBusiness> bussPage = EbBusiness.dao
				.paginate(
					page, 
					PathUtil.MIN_PAGE_SIZE, 
					"select *", 
					sql);
		return bussPage;
	}

	/**
	 * 根据企业类型Id删除企业信息
	 * @param  typeIdInfo 企业类型model对象
	 * @return boolean
	 */
	public boolean deleteUserInfo(EbBusinessType typeIdInfo) {
		return typeIdInfo.delete();
	}
}
