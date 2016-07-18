package cn.com.minstone.eBusiness.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.jfinal.plugin.activerecord.Page;

import cn.com.minstone.eBusiness.dao.BusinessDao;
import cn.com.minstone.eBusiness.model.EbBusiness;
import cn.com.minstone.eBusiness.model.EbBusinessType;

public class BusinessService extends BaseService {

//	private Controller ctrl;
	private BusinessDao dao;
//	private Logger log;
	
	public BusinessService() {
		dao = new BusinessDao();
//		log = new Logger(BusinessService.class);
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
	 * @return
	 */
	public List<EbBusiness> findByBussName(String bussName) {
		return dao.findByBussName(bussName);
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
		
//		bType.set("TYPE_ID","buss_type_seq.nextval");
		bType.setTypeId(new BigDecimal(109));
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
	public Page<EbBusiness> filterBuss(String typeId, String bussName, String contactName, String contactPhone, int page) {
		int bussType = -1;
		if(typeId != null && !"".equals(typeId) && !"null".equalsIgnoreCase(typeId)) {
			bussType = Integer.parseInt(typeId);
		}
		return dao.findByTpOrNameLinker(bussType, bussName, contactName, contactPhone, page);
	}
	
	/**
	 * 添加企业
	 * @param bussName 企业名称
	 * @param projectName	项目名称
	 * @param linkerName 	联系人姓名
	 * @param telephone 	电话号码
	 * @param typeId 	企业类型
	 * @return	0 失败  1 成功  -1 失败缺少参数
	 */
	public int addBussInfo(String bussName, String projectName, String linkerName, 
			String telephone, String typeId) {
		
		EbBusiness bussInfo = new EbBusiness();
		
		bussInfo.set("business_id", "business_seq.nextval");
		if(bussName != null && !"".equals(bussName)) {
			bussInfo.setBusinessName(bussName);		//企业名称
		} else {
			return -1;
		}
		if(projectName != null && !"".equals(projectName)) {
			bussInfo.setProjectName(projectName);		//项目名称
		} else {
			return -1;
		}
		if(linkerName != null && !"".equals(linkerName) && linkerName.length() <= 20) {
			bussInfo.setContactName(bussName);		//联系人姓名
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
			bussInfo.set("type_id", type);		//企业类型
		} else {
			return -1;
		}
		
		bussInfo.set("SIGN_STATUS", 0);	//签收状态
		
		bussInfo.set("SETTLE_STATUS", 0);	//落户状态
		bussInfo.set("DISTRIBUTED_STATUS", 0);	//是否分发任务
		
		if(dao.addBussInfo(bussInfo)) {
			return 1;
		}
		return 0;
	}
}
