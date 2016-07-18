package cn.com.minstone.eBusiness.model;

import com.jfinal.plugin.activerecord.Model;

/**
 * 数据库表名称：EB_SPC_PROGRAM
 * */
public class EbSpcProgram extends Model<EbSpcProgram> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1532926018115703820L;

	public static final EbSpcProgram dao = new EbSpcProgram();
	
	/**
	 * 特别程序信息模型
	 */
	private EbItem itemInfo;
	
	public EbItem getItemInfo() {
		return itemInfo;
	}

	public void setItemInfo(EbItem itemInfo) {
		this.itemInfo = itemInfo;
	}

	/**
	 * 新增用户信息模型
	 */
	private EbUserInfo buildUser;
	
	public EbUserInfo getBuildUser() {
		return buildUser;
	}

	public void setBuildUser(EbUserInfo buildUser) {
		this.buildUser = buildUser;
	}

	/**
	 * 签收用户信息模型
	 */
	private EbUserInfo userInfo;
	
	public EbUserInfo getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(EbUserInfo userInfo) {
		this.userInfo = userInfo;
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
	 * 数据库列名称：TRANSACTION_WAY
	 * */
	private java.lang.String transactionWay;

	public java.lang.String getTransactionWay() {
		this.transactionWay = this.get("TRANSACTION_WAY");
		return this.transactionWay;
	}

	public void setTransactionWay(java.lang.String transactionWay) {
		this.transactionWay = transactionWay;
		this.set("TRANSACTION_WAY", this.transactionWay);
	}

	/**
	 * 数据库列名称：BUILD_TIME
	 * */
	private java.lang.String buildTime;

	public java.lang.String getBuildTime() {
		this.buildTime = this.get("BUILD_TIME");
		return this.buildTime;
	}

	public void setBuildTime(java.lang.String buildTime) {
		this.buildTime = buildTime;
		this.set("BUILD_TIME", this.buildTime);
	}

	/**
	 * 数据库列名称：EXAMIN_TIME
	 * */
	private java.lang.String examinTime;

	public java.lang.String getExaminTime() {
		this.examinTime = this.get("EXAMIN_TIME");
		return this.examinTime;
	}

	public void setExaminTime(java.lang.String examinTime) {
		this.examinTime = examinTime;
		this.set("EXAMIN_TIME", this.examinTime);
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
	 * 数据库列名称：REASON
	 * */
	private java.lang.String reason;

	public java.lang.String getReason() {
		this.reason = this.get("REASON");
		return this.reason;
	}

	public void setReason(java.lang.String reason) {
		this.reason = reason;
		this.set("REASON", this.reason);
	}

	/**
	 * 数据库列名称：PROGRAM_ID
	 * */
	private java.math.BigDecimal programId;

	public java.math.BigDecimal getProgramId() {
		this.programId = this.get("PROGRAM_ID");
		return this.programId;
	}

	public void setProgramId(java.math.BigDecimal programId) {
		this.programId = programId;
		this.set("PROGRAM_ID", this.programId);
	}
	
	/**
	 * 数据库列名称：DISTR_ID
	 * */
	private java.math.BigDecimal distrId;

	public java.math.BigDecimal getDistrId() {
		this.distrId = this.get("DISTR_ID");
		return this.distrId;
	}

	public void setDistrId(java.math.BigDecimal distrId) {
		this.distrId = distrId;
		this.set("DISTR_ID", this.distrId);
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
	 * 数据库列名称：ADDRESS
	 * */
	private java.lang.String address;

	public java.lang.String getAddress() {
		this.address = this.get("ADDRESS");
		return this.address;
	}

	public void setAddress(java.lang.String address) {
		this.address = address;
		this.set("ADDRESS", this.address);
	}

	/**
	 * 数据库列名称：OPINION
	 * */
	private java.lang.String opinion;

	public java.lang.String getOpinion() {
		this.opinion = this.get("OPINION");
		return this.opinion;
	}

	public void setOpinion(java.lang.String opinion) {
		this.opinion = opinion;
		this.set("OPINION", this.opinion);
	}

	/**
	 * 数据库列名称：ITEM_ID
	 * */
	private java.math.BigDecimal itemId;

	public java.math.BigDecimal getItemId() {
		this.itemId = this.get("ITEM_ID");
		return this.itemId;
	}

	public void setItemId(java.math.BigDecimal itemId) {
		this.itemId = itemId;
		this.set("ITEM_ID", this.itemId);
	}
	
	/**
	 * 数据库列名称：DEPART_NAME
	 * */
	private java.lang.String departName;

	public java.lang.String getDepartName() {
		this.departName = this.get("DEPART_NAME");
		return this.departName;
	}

	public void setDepartName(java.lang.String departName) {
		this.departName = departName;
		this.set("DEPART_NAME", this.departName);
	}

	/**
	 * 数据库列名称：DEPART_ID
	 * */
	private java.math.BigDecimal departId;

	public java.math.BigDecimal getDepartId() {
		this.departId = this.get("DEPART_ID");
		return this.departId;
	}

	public void setDepartId(java.math.BigDecimal departId) {
		this.departId = departId;
		this.set("DEPART_ID", this.departId);
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
	 * 数据库列名称：ITEM_NAME
	 * */
	private java.lang.String itemName;

	public java.lang.String getItemName() {
		this.itemName = this.get("ITEM_NAME");
		return this.itemName;
	}

	public void setItemName(java.lang.String itemName) {
		this.itemName = itemName;
		this.set("ITEM_NAME", this.itemName);
	}

	/**
	 * 数据库列名称：CONMENT
	 * */
	private java.lang.String conment;

	public java.lang.String getConment() {
		this.conment = this.get("CONMENT");
		return this.conment;
	}

	public void setConment(java.lang.String conment) {
		this.conment = conment;
		this.set("CONMENT", this.conment);
	}

	/**
	 * 数据库列名称：EXAMINE_STATUS
	 * */
	private java.math.BigDecimal examineStatus;

	public java.math.BigDecimal getExamineStatus() {
		this.examineStatus = this.get("EXAMINE_STATUS");
		return this.examineStatus;
	}

	public void setExamineStatus(java.math.BigDecimal examineStatus) {
		this.examineStatus = examineStatus;
		this.set("EXAMINE_STATUS", this.examineStatus);
	}

	/**
	 * 数据库列名称：TIME_LIMIT
	 * */
	private java.math.BigDecimal timeLimit;

	public java.math.BigDecimal getTimeLimit() {
		this.timeLimit = this.get("TIME_LIMIT");
		return this.timeLimit;
	}

	public void setTimeLimit(java.math.BigDecimal timeLimit) {
		this.timeLimit = timeLimit;
		this.set("TIME_LIMIT", this.timeLimit);
	}

}