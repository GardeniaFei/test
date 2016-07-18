package cn.com.minstone.eBusiness.dao.inter;

import java.util.ArrayList;
import java.util.List;

import com.jfinal.plugin.activerecord.Page;

import cn.com.minstone.eBusiness.model.EbAttention;
import cn.com.minstone.eBusiness.model.EbBusiness;
import cn.com.minstone.eBusiness.model.EbBusinessType;
import cn.com.minstone.eBusiness.model.EbDepart;
import cn.com.minstone.eBusiness.model.EbMessage;
import cn.com.minstone.eBusiness.model.EbTask;
import cn.com.minstone.eBusiness.util.GovNetUtil;
import cn.com.minstone.eBusiness.util.PathUtil;

public class BussInterDao extends BaseInterDao {
	
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
	 * 根据用户id获取所有的企业
	 * @param userId
	 * @param page
	 * @return
	 */
	public Page<EbBusiness> findAllByUId(String userId, int page) {
		String sql = " from eb_business b where b.user_id = " + userId + 
				" or b.build_user_id = " + userId + " order by b.sign_time desc, b.add_time desc";
		Page<EbBusiness> bussPage = EbBusiness.dao
			.paginate(
				page, 
				PathUtil.MIN_PAGE_SIZE, 
				"select *", 
				sql);
		return bussPage;
	}
	
	/**
	 * 获取所有已签收的企业
	 * @param page
	 * @return Page
	 */
	public Page<EbBusiness> findAllSign(int page) {
		String sql = " from eb_business where sign_status = 1 order by business_id";
		Page<EbBusiness> bussPage = EbBusiness.dao
			.paginate(
				page, 
				PathUtil.MIN_PAGE_SIZE, 
				"select *", 
				sql);
		return bussPage;
	}
	
	/**
	 * 获取所有已签收的企业
	 * @return list
	 */
	public List<EbBusiness> findAllSignli() {
		return EbBusiness.dao.find("select * from eb_business where sign_status = 1 order by business_id");
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
	
	/**
	 * 根据企业名称获取企业
	 * @param bussName
	 * @return
	 */
	public EbBusiness findByBussNameOne(String bussName) {
		return EbBusiness.dao.findFirst(selSQL + " b where b.business_name = '" + bussName +"'");
	}
	/**
	 * 根据企业id获得企业对象
	 * @param businessId
	 * @return
	 */
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
	public Page<EbBusiness> findByTpOrName(int typeId, String bussName,int page) {
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
	 * 根据企业类型Id删除企业信息
	 * @param  typeIdInfo 企业类型model对象
	 * @return boolean
	 */
	public boolean deleteUserInfo(EbBusinessType typeIdInfo) {
		return typeIdInfo.delete();
	}
	
	/**
	 * 根据企业id获取企业审批办理单位列表
	 * @param businessId
	 * @return
	 */
	public List<EbDepart> findDeptsByBussId(String businessId) {
		if (businessId == null || "".equals(businessId) || "null".equalsIgnoreCase(businessId)) {
			return null;
		}
		EbTask task = EbTask.dao.findFirst("select * from eb_task where business_id = ? order by task_id desc", businessId);
		if(task == null) {
			return null;
		}else {
			String taskId = task.getTaskId() + "";
			List<String> deptIds = new TaskInterDao().findDeptIdsByTaskId(taskId);
			if(deptIds == null) {
				return null;
			} else {
				List<EbDepart> depts = new ArrayList<EbDepart>();
				for (int i = 0; i < deptIds.size(); i++) {
					//由于有设置部门签收人环节，一般而言，本地库中的部门表中能查到部门。
					EbDepart dept = new DepartInterDao().findById(deptIds.get(i));
					if(dept == null) {
						dept = GovNetUtil.getDeptInfo(deptIds.get(i));
					}
					if(dept != null) {
						depts.add(new DepartInterDao().findById(deptIds.get(i)));
					}
				}
				return depts;
			}
		}
	}
	
	/**
	 * 根据企业id和部门id获取留言+回复列表
	 * @param businessId
	 * @param departId
	 * @return
	 */
	public List<EbMessage> findMsgByBDId(String businessId, String departId) {
		if (departId == null || "".equals(departId) || "null".equalsIgnoreCase(departId)) {
			return null;
		}else {
			return EbMessage.dao.find("select * from eb_message where business_id = ? "
					+ "and depart_id = ? order by message_id desc", businessId, departId);
		}
	}
	
	/**
	 * 根据企业id和部门id获取留言列表
	 * @param businessId
	 * @param departId
	 * @return
	 */
	public List<EbMessage> findMsgByBDId_Status(String businessId, String departId) {
		if (businessId == null || "".equals(businessId) || "null".equalsIgnoreCase(businessId)) {
			return null;
		}else {
			if (departId == null || "".equals(departId) || "null".equalsIgnoreCase(departId)) {
				return EbMessage.dao.find("select * from eb_message where business_id = ? "
					+ "and status = 1 and exam_status = 1 order by message_id desc", businessId);
			}else{
				return EbMessage.dao.find("select * from eb_message where business_id = ? "
					+ "and depart_id = ? and status = 1 and exam_status = 1 order by message_id desc", businessId,departId);
			}
		}
	}
	
	/**
	 * 根据企业id和部门id获取留言列表(企业可看到未审核的)
	 * @param businessId
	 * @param departId
	 * @return
	 */
	public List<EbMessage> findMsgByBDId_StatusByBuss(String businessId, String departId) {
		if (businessId == null || "".equals(businessId) || "null".equalsIgnoreCase(businessId)) {
			return null;
		}else {
			if (departId == null || "".equals(departId) || "null".equalsIgnoreCase(departId)) {
				return EbMessage.dao.find("select * from eb_message where business_id = ? "
					+ "and status = 1 order by message_id desc", businessId);
			}else{
				return EbMessage.dao.find("select * from eb_message where business_id = ? "
					+ "and depart_id = ? and status = 1 order by message_id desc", businessId,departId);
			}
		}
	}
	/**
	 * 根据领导id获取领导关注的所有企业列表
	 * @param userId
	 * @return list
	 */
	public List<EbAttention> findAllAttentByUserIdList(String userId){
		String sql = "select * from eb_attention where is_delet = 1 and user_id = ? order by attent_id desc";
		return EbAttention.dao.find(sql,userId);
	}
	
	/**
	 * 根据领导id获取领导关注的所有企业列表
	 * @param userId
	 * @param page 页数
	 * @return page
	 */
	public Page<EbAttention> findAllAttentByUserId (String userId,int page){
		String sql = " from eb_attention where is_delet = 1 and user_id = "+userId+" order by attent_id desc";
		Page<EbAttention> attPage = EbAttention.dao
				.paginate(
					page, 
					PathUtil.MIN_PAGE_SIZE, 
					"select *", 
					sql);
		return attPage;
	}
	
	/**
	 * 根据用户id和企业id查询是否被关注
	 * @param userId
	 * @param businessId
	 * @return
	 */
	public EbAttention findAttenByUBid(String userId,String businessId){
		String sql = "select * from eb_attention where is_delet = 1 and user_id = ? and business_id = ?";
		return EbAttention.dao.findFirst(sql,userId,businessId);
	}
	
	/**
	 * 根据用户id和企业id查询企业关注信息
	 * @param userId
	 * @param businessId
	 * @return
	 */
	public EbAttention findAttenByUBID(String userId,String businessId){
		String sql = "select * from eb_attention where user_id = ? and business_id = ?";
		return EbAttention.dao.findFirst(sql, userId, businessId);
	}
	
	/**
	 * 根据企业签收人VIP服务专员code筛选未分发任务的企业
	 * @param userCode
	 * @param page
	 * @return
	 */
	public Page<EbBusiness> findBsInfosByVipCode(String userCode, int page) {
		String sql = " from eb_business where service_admin = '" + userCode + "' and DISTRIBUTED_STATUS = 0 order by business_id desc";
		Page<EbBusiness> bussPage = EbBusiness.dao
				.paginate(
					page, 
					PathUtil.MIN_PAGE_SIZE, 
					"select *", 
					sql);
		return bussPage;
	}
	
	/**
	 * VIP服务专员管理员可以看到所有正在办理的企业
	 * @param page
	 * @return
	 */
	public Page<EbBusiness> findBsInfosByVIP(int page) {
		String sql = " from eb_business where settle_status = 0 order by business_id desc";
		Page<EbBusiness> bussPage = EbBusiness.dao
				.paginate(
					page, 
					PathUtil.MIN_PAGE_SIZE, 
					"select *", 
					sql);
		return bussPage;
	}
}
