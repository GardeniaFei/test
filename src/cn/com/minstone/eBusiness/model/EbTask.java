package cn.com.minstone.eBusiness.model;

import com.jfinal.plugin.activerecord.Model;

/**
 * 数据库表名称：EB_TASK
 * */
public class EbTask extends Model<EbTask> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4715147111393950903L;

	public static final EbTask dao = new EbTask();
	
	/**
	 * 创建人用户
	 */
	private EbUserInfo buildUser;
	
	public EbUserInfo getBuildUser() {
		return buildUser;
	}

	public void setBuildUser(EbUserInfo buildUser) {
		this.buildUser = buildUser;
	}

	/**
	 * 签收人用户
	 */
	private EbUserInfo signUser;

	public EbUserInfo getSignUser() {
		return signUser;
	}

	public void setSignUser(EbUserInfo signUser) {
		this.signUser = signUser;
	}
	
	/**
	 * 数据库列名称：COMP_STATUS
	 * */
	private java.math.BigDecimal compStatus;

	public java.math.BigDecimal getCompStatus() {
		this.compStatus = this.get("COMP_STATUS");
		return this.compStatus;
	}

	public void setCompStatus(java.math.BigDecimal compStatus) {
		this.compStatus = compStatus;
		this.set("COMP_STATUS", this.compStatus);
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
	 * 数据库列名称：COMP_TIME
	 * */
	private java.lang.String compTime;

	public java.lang.String getCompTime() {
		this.compTime = this.get("COMP_TIME");
		return this.compTime;
	}

	public void setCompTime(java.lang.String compTime) {
		this.compTime = compTime;
		this.set("COMP_TIME", this.compTime);
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
	 * 数据库列名称：SIGN_USER_ID部门签收人Id
	 * */
	private java.math.BigDecimal signUserId;

	public java.math.BigDecimal getSignUserId() {
		this.signUserId = this.get("SIGN_USER_ID");
		return this.signUserId;
	}

	public void setSignUserId(java.math.BigDecimal signUserId) {
		this.signUserId = signUserId;
		this.set("SIGN_USER_ID", this.signUserId);
	}

	/**
	 * 数据库列名称：SIGN_TIME签收时间
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
	 * 数据库列名称：SIGN_STATUS签收状态非空
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
	 * 数据库列名称：TASK_ID
	 * */
	private java.math.BigDecimal taskId;

	public java.math.BigDecimal getTaskId() {
		this.taskId = this.get("TASK_ID");
		return this.taskId;
	}

	public void setTaskId(java.math.BigDecimal taskId) {
		this.taskId = taskId;
		this.set("TASK_ID", this.taskId);
	}
	
	/**
	 *任务分发状态：DISTRIBUTED_STATUS
	 * */
	private String distributedStatus;

	public String getDistributedStatus() {
		return distributedStatus;
	}

	public void setDistributedStatus(String distributedStatus) {
		this.distributedStatus = distributedStatus;
	}

}