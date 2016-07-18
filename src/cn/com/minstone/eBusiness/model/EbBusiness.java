package cn.com.minstone.eBusiness.model;

import com.jfinal.plugin.activerecord.Model;

/**
 * 数据库表名称：EB_BUSINESS
 * */
public class EbBusiness extends Model<EbBusiness> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2362900353917762322L;
	
	public static final EbBusiness dao = new EbBusiness();
	
	/**
	 * 签收用户信息
	 */
	private EbUserInfo signUser;
	
	public EbUserInfo getSignUser() {
		return signUser;
	}

	public void setSignUser(EbUserInfo signUser) {
		this.signUser = signUser;
	}

	/**
	 * 企业类型
	 */
	private EbBusinessType bussType;
	
	public EbBusinessType getBussType() {
		return bussType;
	}

	public void setBussType(EbBusinessType bussType) {
		this.bussType = bussType;
	}

	/**
	 * 数据库列名称：BUSINESS_ID
	 * */
	private java.math.BigDecimal businessId;

	public java.math.BigDecimal getBusinessId() {
		this.businessId = this.get("BUSINESS_ID");
		return this.businessId;
	}

	public void setBusinessId(java.math.BigDecimal businessId) {
		this.businessId = businessId;
		this.set("BUSINESS_ID", this.businessId);
	}

	/**
	 * 数据库列名称：ADD_TIME
	 * */
	private java.lang.String addTime;

	public java.lang.String getAddTime() {
		this.addTime = this.get("ADD_TIME");
		return this.addTime;
	}

	public void setAddTime(java.lang.String addTime) {
		this.addTime = addTime;
		this.set("ADD_TIME", this.addTime);
	}

	/**
	 * 数据库列名称：REGIST_CAPITAL
	 * */
	private java.lang.String registCapital;

	public java.lang.String getRegistCapital() {
		this.registCapital = this.get("REGIST_CAPITAL");
		return this.registCapital;
	}

	public void setRegistCapital(java.lang.String registCapital) {
		this.registCapital = registCapital;
		this.set("REGIST_CAPITAL", this.registCapital);
	}

	/**
	 * 数据库列名称：OPERATE_SCOPE
	 * */
	private java.lang.String operateScope;

	public java.lang.String getOperateScope() {
		this.operateScope = this.get("OPERATE_SCOPE");
		return this.operateScope;
	}

	public void setOperateScope(java.lang.String operateScope) {
		this.operateScope = operateScope;
		this.set("OPERATE_SCOPE", this.operateScope);
	}

	/**
	 * 数据库列名称：BUSS_EMAIL
	 * */
	private java.lang.String bussEmail;

	public java.lang.String getBussEmail() {
		this.bussEmail = this.get("BUSS_EMAIL");
		return this.bussEmail;
	}

	public void setBussEmail(java.lang.String bussEmail) {
		this.bussEmail = bussEmail;
		this.set("BUSS_EMAIL", this.bussEmail);
	}

	/**
	 * 数据库列名称：BUSINESS_INTRO
	 * */
	private java.lang.String businessIntro;

	public java.lang.String getBusinessIntro() {
		this.businessIntro = this.get("BUSINESS_INTRO");
		return this.businessIntro;
	}

	public void setBusinessIntro(java.lang.String businessIntro) {
		this.businessIntro = businessIntro;
		this.set("BUSINESS_INTRO", this.businessIntro);
	}

	/**
	 * 数据库列名称：WEB_ADDRESS 企业网址
	 * */
	private java.lang.String webAddress;

	public java.lang.String getWebAddress() {
		this.webAddress = this.get("WEB_ADDRESS");
		return this.webAddress;
	}

	public void setWebAddress(java.lang.String webAddress) {
		this.webAddress = webAddress;
		this.set("WEB_ADDRESS", this.webAddress);
	}

	/**
	 * 数据库列名称：BUSS_ADDRESS 企业地址
	 * */
	private java.lang.String bussAddress;

	public java.lang.String getBussAddress() {
		this.bussAddress = this.get("BUSS_ADDRESS");
		return this.bussAddress;
	}

	public void setBussAddress(java.lang.String bussAddress) {
		this.bussAddress = bussAddress;
		this.set("BUSS_ADDRESS", this.bussAddress);
	}

	/**
	 * 数据库列名称：project_intro
	 * */
	private java.lang.String project_intro;

	public java.lang.String getProject_intro() {
		this.project_intro = this.get("project_intro");
		return this.project_intro;
	}

	public void setProject_intro(java.lang.String project_intro) {
		this.project_intro = project_intro;
		this.set("project_intro", this.project_intro);
	}

	/**
	 * 数据库列名称：USER_CODE
	 * */
	private java.lang.String userCode;

	public java.lang.String getUserCode() {
		this.userCode = this.get("USER_CODE");
		return this.userCode;
	}

	public void setUserCode(java.lang.String userCode) {
		this.userCode = userCode;
		this.set("USER_CODE", this.userCode);
	}

	/**
	 * 数据库列名称：USER_NAME
	 * */
	private java.lang.String userName;

	public java.lang.String getUserName() {
		this.userName = this.get("USER_NAME");
		return this.userName;
	}

	public void setUserName(java.lang.String userName) {
		this.userName = userName;
		this.set("USER_NAME", this.userName);
	}

	/**
	 * 数据库列名称：BUILD_USER_NAME
	 * */
	private java.lang.String buildUserName;

	public java.lang.String getBuildUserName() {
		this.buildUserName = this.get("BUILD_USER_NAME");
		return this.buildUserName;
	}

	public void setBuildUserName(java.lang.String buildUserName) {
		this.buildUserName = buildUserName;
		this.set("BUILD_USER_NAME", this.buildUserName);
	}

	/**
	 * 数据库列名称：BUILD_USER_ID
	 * */
	private java.math.BigDecimal buildUserId;

	public java.math.BigDecimal getBuildUserId() {
		this.buildUserId = this.get("BUILD_USER_ID");
		return this.buildUserId;
	}

	public void setBuildUserId(java.math.BigDecimal buildUserId) {
		this.buildUserId = buildUserId;
		this.set("BUILD_USER_ID", this.buildUserId);
	}

	/**
	 * 数据库列名称：BUILD_USER_CODE
	 * */
	private java.lang.String buildUserCode;

	public java.lang.String getBuildUserCode() {
		this.buildUserCode = this.get("BUILD_USER_CODE");
		return this.buildUserCode;
	}

	public void setBuildUserCode(java.lang.String buildUserCode) {
		this.buildUserCode = buildUserCode;
		this.set("BUILD_USER_CODE", this.buildUserCode);
	}

	/**
	 * 数据库列名称：BUSINESS_NAME
	 * */
	private java.lang.String businessName;

	public java.lang.String getBusinessName() {
		this.businessName = this.get("BUSINESS_NAME");
		return this.businessName;
	}

	public void setBusinessName(java.lang.String businessName) {
		this.businessName = businessName;
		this.set("BUSINESS_NAME", this.businessName);
	}

	/**
	 * 数据库列名称：SIGN_STATUS
	 * */
	private java.math.BigDecimal signStatus;

	public java.math.BigDecimal getSignStatus() {
		this.signStatus = this.get("SIGN_STATUS");
		return this.signStatus;
	}

	public void setSignStatus(java.math.BigDecimal signStatus) {
		this.signStatus = signStatus;
		this.set("SIGN_STATUS", this.signStatus);
	}

	/**
	 * 数据库列名称：USER_ID
	 * */
	private java.math.BigDecimal userId;

	public java.math.BigDecimal getUserId() {
		this.userId = this.get("USER_ID");
		return this.userId;
	}

	public void setUserId(java.math.BigDecimal userId) {
		this.userId = userId;
		this.set("USER_ID", this.userId);
	}

	/**
	 * 数据库列名称：CONTACT_NAME
	 * */
	private java.lang.String contactName;

	public java.lang.String getContactName() {
		this.contactName = this.get("CONTACT_NAME");
		return this.contactName;
	}

	public void setContactName(java.lang.String contactName) {
		this.contactName = contactName;
		this.set("CONTACT_NAME", this.contactName);
	}

	/**
	 * 数据库列名称：SETTLE_TIME
	 * */
	private java.lang.String settleTime;

	public java.lang.String getSettleTime() {
		this.settleTime = this.get("SETTLE_TIME");
		return this.settleTime;
	}

	public void setSettleTime(java.lang.String settleTime) {
		this.settleTime = settleTime;
		this.set("SETTLE_TIME", this.settleTime);
	}

	/**
	 * 数据库列名称：TYPE_ID
	 * */
	private java.math.BigDecimal typeId;

	public java.math.BigDecimal getTypeId() {
		this.typeId = this.get("TYPE_ID");
		return this.typeId;
	}

	public void setTypeId(java.math.BigDecimal typeId) {
		this.typeId = typeId;
		this.set("TYPE_ID", this.typeId);
	}

	/**
	 * 数据库列名称：DISTRIBUTED_STATUS
	 * */
	private java.math.BigDecimal distributedStatus;

	public java.math.BigDecimal getDistributedStatus() {
		this.distributedStatus = this.get("DISTRIBUTED_STATUS");
		return this.distributedStatus;
	}

	public void setDistributedStatus(java.math.BigDecimal distributedStatus) {
		this.distributedStatus = distributedStatus;
		this.set("DISTRIBUTED_STATUS", this.distributedStatus);
	}

	/**
	 * 数据库列名称：SETTLE_STATUS
	 * */
	private java.math.BigDecimal settleStatus;

	public java.math.BigDecimal getSettleStatus() {
		this.settleStatus = this.get("SETTLE_STATUS");
		return this.settleStatus;
	}

	public void setSettleStatus(java.math.BigDecimal settleStatus) {
		this.settleStatus = settleStatus;
		this.set("SETTLE_STATUS", this.settleStatus);
	}

	/**
	 * 数据库列名称：CONTACT_PHONE
	 * */
	private java.lang.String contactPhone;

	public java.lang.String getContactPhone() {
		this.contactPhone = this.get("CONTACT_PHONE");
		return this.contactPhone;
	}

	public void setContactPhone(java.lang.String contactPhone) {
		this.contactPhone = contactPhone;
		this.set("CONTACT_PHONE", this.contactPhone);
	}

	/**
	 * 数据库列名称：PROJECT_NAME
	 * */
	private java.lang.String projectName;

	public java.lang.String getProjectName() {
		this.projectName = this.get("PROJECT_NAME");
		return this.projectName;
	}

	public void setProjectName(java.lang.String projectName) {
		this.projectName = projectName;
		this.set("PROJECT_NAME", this.projectName);
	}

	/**
	 * 数据库列名称：SIGN_TIME
	 * */
	private java.lang.String signTime;

	public java.lang.String getSignTime() {
		this.signTime = this.get("SIGN_TIME");
		
//		long oldTime = Long.parseLong(this.signTime);
//		Date newTime = new Date(oldTime);
//		
//		SimpleDateFormat df = new SimpleDateFormat("yyyy-mm-dd");
//		this.signTime = df.format(newTime);
		
		return this.signTime;
	}

	public void setSignTime(java.lang.String signTime) {
		this.signTime = signTime;
		this.set("SIGN_TIME", this.signTime);
	}

	/**
	 * 数据库列名称：service_admin VIP服务专员
	 * */
	private java.lang.String service_admin;
	
	public java.lang.String getService_admin() {
		this.service_admin = this.get("service_admin");
		return service_admin;
	}

	public void setService_admin(java.lang.String service_admin) {
		this.service_admin = service_admin;
		this.set("service_admin", this.service_admin);
	}
	
}