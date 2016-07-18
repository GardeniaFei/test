package cn.com.minstone.eBusiness.service.inter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;

import cn.com.minstone.eBusiness.dao.inter.BussInterDao;
import cn.com.minstone.eBusiness.model.EbAttention;
import cn.com.minstone.eBusiness.model.EbBusiness;
import cn.com.minstone.eBusiness.model.EbBusinessType;
import cn.com.minstone.eBusiness.model.EbDepart;
import cn.com.minstone.eBusiness.model.EbMessage;
import cn.com.minstone.eBusiness.model.EbUserInfo;
import cn.com.minstone.eBusiness.service.BaseService;

public class BussInterService extends BaseService {

	private BussInterDao dao;
	private Controller ctrl;
	
	public BussInterService() {
		dao = new BussInterDao();
	}
	
	/**
	 * 修改企业的类型id
	 * @param businessId
	 * @param typeId
	 */
	public void updateType(String businessId, String typeId) {
		try {
			if (typeId != null && !typeId.equals("") && !typeId.equalsIgnoreCase("null")) {
				BussInterService bService = new BussInterService();
				EbBusiness bussInfo = bService.findById(businessId);
				if(bussInfo != null) {
					bussInfo.setTypeId(new BigDecimal(typeId));
					bussInfo.update();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取所有的企业
	 * @return list
	 */
	public List<EbBusiness> getAllBussInfoList() {
		List<EbBusiness> list = new ArrayList<EbBusiness>();
		list = dao.findAllList();
//		log.error("打印获取到的数据" + list.toString());
		return list;
	}
	
	/**
	 * 获取所有的企业
	 * @return Page
	 */
	public Page<EbBusiness> getAllBussInfo(int page) {
		Page<EbBusiness> listPage = dao.findAll(page);
		return listPage;
	}
	
	/**
	 * 根据用户id获取所有的企业
	 * @param userId
	 * @param page
	 * @return
	 */
	public Page<EbBusiness> getBussByUId(String userId, int page) {
		Page<EbBusiness> listPage = dao.findAllByUId(userId, page);
		return listPage;
	}
	
	/**
	 * 获取所有已签收企业
	 * @return Page
	 */
	public Page<EbBusiness> getAllBussSign(int page) {
		Page<EbBusiness> listPage = dao.findAllSign(page);
		return listPage;
	}
	
	/**
	 * 获取所有已签收企业
	 * @return list
	 */
	public List<EbBusiness> getAllBussSignli() {
		List<EbBusiness> list = dao.findAllSignli();
		return list;
	}
	
	/**
	 * 获取所有的企业类型
	 * @return list
	 */
	public List<EbBusinessType> getAllBsTypeList() {
		
		return dao.findAllBsTypeList();
	}
	
	/**
	 * 获取所有的企业类型
	 * @param page
	 * @return Page
	 */
	public Page<EbBusinessType> getAllBsType(int page) {
		
		return dao.findAllBsType(page);
	}
	
	/**
	 * 根据企业名称获取企业
	 * @param bussName
	 * @return list
	 */
	public List<EbBusiness> findByBussName(String bussName) {
		return dao.findByBussName(bussName);
	}
	
	/**
	 * 根据企业名称获取企业
	 * @param bussName
	 * @return EbBusiness
	 */
	public EbBusiness findByBussNameOne(String bussName) {
		return dao.findByBussNameOne(bussName);
	}
	
	/**
	 * 获取企业类型信息
	 * @param typeId
	 * @return
	 */
	public EbBusinessType getBsTypeById(String typeId) {
		if(typeId == null || "".equals(typeId) || "null".equalsIgnoreCase(typeId)) {
			return null;
		}else {
			return dao.findBsTypeById(typeId);
		}
	}
	
	/**
	 * 获取企业类型信息
	 * @param typeName
	 * @return
	 */
	public EbBusinessType getBsTypeByTypeName(String typeName) {
		return dao.findBsTypeByName(typeName);
	}
	
	/**
	 * 添加企业类型信息
	 * @param typeName
	 * @return
	 */
	public boolean addBsType(String typeName){
		EbBusinessType bType = new EbBusinessType();
		
		bType.set("TYPE_ID","buss_type_seq.nextval");
		bType.setTypeName(typeName);
		bType.set("IS_DELET", "1");
		
		//获取当前系统时间作为更新时间
		bType.setRefreshTime(new Date().getTime()+"");
		
		boolean result = false;
		if(bType.save()){
			result = true;
		}else{
			result = false;
		}
		return result;
	}
	
	/**
	 * 删除企业类型信息
	 * @param typeId
	 * 
	 */
	public boolean deletBsType(String typeId){
		EbBusinessType bType = dao.findBsTypeById(typeId);
		if(bType!=null){
			//假删除，数据存在
			return EbBusinessType.dao.findById(bType.getTypeId()).set("is_delet", "0").update();
			//真删除，数据不存在
//			return dao.deleteUserInfo(bType);
		}else{
			return false;
		}
		
	}
	
	/**
	 * 修改企业类型信息
	 * @param typeName
	 * @param typeId
	 * @return
	 */
	public boolean resetBsType(String typeId,String typeName){
		EbBusinessType bType = new EbBusinessType();
		bType = dao.findBsTypeById(typeId);
		
		boolean result = false;
		if(bType!=null){
			//修改名字和更新时间
			bType.setTypeName(typeName);
			
			//获取当前系统时间作为更新时间
			bType.setRefreshTime(new Date().getTime()+"");
			
			if(bType.update()){
				result = true;
			}else{
				result = false;
			}
		}else{
			result = false;
		}
		
		return result;
	}
		
		
	/**
	 * 根据企业id获取企业信息
	 * @param bussId
	 * @return
	 */
	public EbBusiness getBussById(String bussId) {
		if(bussId == null || "".equals(bussId) || "null".equalsIgnoreCase(bussId)) {
			return null;
		}else {
			return dao.findById(bussId);
		}
	}
	
	/**
	 * 根据企业id获取企业
	 * @param businessId
	 * @return Object<EbBusiness>
	 */
	public EbBusiness findById(String businessId) {
		if(businessId == null || "".equals(businessId) || "null".equalsIgnoreCase(businessId)) {
			return null;
		}
		return dao.findById(businessId);
	}
	
	/**
	 * 根据企业类型和企业名称筛选所有企业
	 * @param typeId 企业类型id
	 * @param bussName 企业名称
	 * @return list
	 */
	public List<EbBusiness> filterBussList(String typeId, String bussName) {
		int bussType = -1;
		if(typeId != null && !"".equals(typeId) && !"null".equalsIgnoreCase(typeId)) {
			bussType = Integer.parseInt(typeId);
		}
		return dao.findByTpOrNameList(bussType, bussName);
	}
	
	/**
	 * 根据企业类型和企业名称筛选所有企业
	 * @param typeId 企业类型id
	 * @param bussName 企业名称
	 * @param page
	 * @return Page
	 */
	public Page<EbBusiness> filterBuss(String typeId, String bussName, int page) {
		int bussType = -1;
		if(typeId != null && !"".equals(typeId) && !"null".equalsIgnoreCase(typeId)) {
			bussType = Integer.parseInt(typeId);
		}
		return dao.findByTpOrName(bussType, bussName, page);
	}
	
	/**
	 * 
	 * 添加企业
	 * @param bussName 企业名称
	 * @param projectName	项目名称
	 * @param linkerName 	联系人姓名
	 * @param telephone 	电话号码
	 * @param typeId 	企业类型
	 * @param userId	添加用户id
	 * @param projectIntro 项目简介
	 * @param bussInto 企业简介
	 * @param registCapital 注册资金
	 * @param operateScope 许可经营范围
	 * @param email 
	 * @param webAddress 企业网址
	 * @param bussAddress 企业地址
	 * @return	0 失败  1 成功  -1 失败缺少参数
	 */
	public int addBussInfo(String bussName, String projectName, String linkerName, 
			String telephone, String typeId,String userId,String projectIntro, 
			String bussInto, String registCapital, String operateScope, String bussEmail, 
			String webAddress, String bussAddress,String service_admin) {
		
		EbBusiness bussInfo = new EbBusiness();
		EbUserInfo userInfo = new UserInterService(null).findById(userId);
		
		bussInfo.set("BUILD_USER_ID", userId);
		if(userInfo != null) {
			bussInfo.setBuildUserCode(userInfo.getUserAccount());
			bussInfo.setBuildUserName(userInfo.getUserName());
		}
		
		bussInfo.setAddTime(System.currentTimeMillis() + "");
		bussInfo.set("BUSINESS_ID", "business_seq.nextval");
		if(bussName != null && !"".equals(bussName)) {
			bussInfo.setBusinessName(bussName);		//企业名称
		} else {
			return -1;
		}
		bussInfo.setProjectName(projectName);		//项目名称
		if(linkerName != null && !"".equals(linkerName) && linkerName.length() <= 20) {
			bussInfo.setContactName(linkerName);		//联系人姓名
		} else {
			return -1;
		}
		if(telephone != null && !"".equals(telephone)) {
			bussInfo.setContactPhone(telephone);		//联系人电话
		} else {
			return -1;
		}
		if(typeId != null && !"".equals(typeId)) {
			int type = Integer.parseInt(typeId);
			bussInfo.setTypeId(new BigDecimal(type));
		} else {
			return -1;
		}
		
		if(registCapital != null && !registCapital.equalsIgnoreCase("null") && !registCapital.equals("")) {
			bussInfo.set("REGIST_CAPITAL", registCapital);//注册资金
		}else {
			return -1;
		}
		
		if(operateScope != null && !operateScope.equalsIgnoreCase("null") && !operateScope.equals("")) {
			bussInfo.set("OPERATE_SCOPE", operateScope);//经营许可范围
		}else {
			return -1;
		}
		bussInfo.setBusinessIntro(bussInto);//企业简介
		
		if(bussEmail != null && !bussEmail.equalsIgnoreCase("null") && !bussEmail.equals("")){
//			if(!StringUtil.valiateEmail(bussEmail)) {//验证邮箱
//				return -1;
//			}
			bussInfo.setBussEmail(bussEmail);//企业邮箱
		}
		
		bussInfo.setSignStatus(new BigDecimal(0));//签收状态
		
		bussInfo.setSettleStatus(new BigDecimal(0));	//落户状态
		bussInfo.setDistributedStatus(new BigDecimal(0));	//是否分发任务
		bussInfo.setProject_intro(projectIntro);//增加项目简介字段
		bussInfo.setWebAddress(webAddress);//企业网址
		bussInfo.setBussAddress(bussAddress);//企业地址
		
		String user_authority = "4";
		String service_admin2 = "";
		UserInterService uSer = new UserInterService(ctrl);
		EbUserInfo u = uSer.getUserByUId(userId);//获取登录用户信息
		if(u != null){
			user_authority = u.getAuthority()+"";//判断用户什么权限
			service_admin2 = u.getUserAccount();//取出用户账号
		}
		if(user_authority.equals("2")) {//用户为VIP服务专员，则用户自己为企业服务
			bussInfo.setService_admin(service_admin2);
		}else{
			bussInfo.setService_admin(service_admin);
		}
		
		if(dao.addBussInfo(bussInfo)) {
			return 1;
		}
		return 0;
	}
	
	/**
	 * 根据企业id获取企业审批单位列表
	 * @param businessId
	 * @return
	 */
	public List<EbDepart> getDeptsByBussId(String businessId) {
		return dao.findDeptsByBussId(businessId);
	}
	
	/**
	 * 根据企业id和部门id获取留言或回复列表
	 * @param businessId
	 * @param departId
	 * @return
	 */
	public List<EbMessage> getMsgByBDId(String businessId, String departId) {
		return dao.findMsgByBDId(businessId, departId);
	}
	
	/**
	 * 根据企业id和部门id获取留言列表
	 * @param businessId
	 * @param departId
	 * @return
	 */
	public List<EbMessage> getMsgByBDId_Status(String businessId, String departId) {
		List<EbMessage> list = dao.findMsgByBDId_Status(businessId, departId);
		return list;
	}
	/**
	 * 根据企业id和部门id获取留言列表(企业用户可看到自己)
	 * @param businessId
	 * @param departId
	 * @return
	 */
	public List<EbMessage> getMsgByBDId_StatusByBuss(String businessId, String departId) {
		List<EbMessage> list = dao.findMsgByBDId_StatusByBuss(businessId, departId);
		return list;
	}
	
	/**
	 * 根据领导用户id获取关注的所有企业
	 * @param userId
	 * @return
	 */
	public List<EbAttention> getAllAttentByUserIdList(String userId){
		return dao.findAllAttentByUserIdList(userId);
	}
	
	/**
	 * 根据领导用户id获取关注的所有企业
	 * @param userId
	 * @param page
	 * @return
	 */
	public Page<EbAttention> getAllAttentByUserId(String userId,int page){
		return dao.findAllAttentByUserId(userId,page);
	}
	
	/**
	 * 根据用户id和企业id查询是否被关注
	 * @param userId
	 * @param businessId
	 * @return
	 */
	public EbAttention getAttenByUBid(String userId,String businessId){
		return dao.findAttenByUBid(userId,businessId);
	}
	
	/**
	 * 根据用户id和企业id查询企业关注信息
	 * @param userId
	 * @param businessId
	 * @return
	 */
	public EbAttention getAttenByUBID(String userId, String businessId){
		return dao.findAttenByUBID(userId, businessId);
	}
	
	/**
	 * 添加企业关注，领导用户
	 * @param userId
	 * @param businessId
	 * @return
	 */
	public boolean addAtten(String userId,String businessId){
		boolean result = false;
		EbAttention att = new EbAttention();
		att.set("ATTENT_ID", "lead_atten_seq.nextval");
		att.setBusinessId(new BigDecimal(businessId));
		att.setUserId(new BigDecimal(userId));
		att.set("is_delet", new BigDecimal("1"));
		try {
			result = att.save();
		} catch (Exception e) {
			e.printStackTrace();
			result = att.save();
		}
		return result;
	}
	
	/**
	 * 根据VIP服务专员code筛选未分发任务的企业
	 * @param userCode
	 * @param page
	 * @return
	 */
	public Page<EbBusiness> filterBsInfosByVipCode(String userCode, int page) {
		
		return dao.findBsInfosByVipCode(userCode, page);
	}
	
	/**
	 * vip服务专员管理员
	 * @param page
	 * @return
	 */
	public Page<EbBusiness> findBsInfosByVIP(int page){
		return dao.findBsInfosByVIP(page);
	}
}
