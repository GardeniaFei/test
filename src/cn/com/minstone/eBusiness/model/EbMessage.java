package cn.com.minstone.eBusiness.model;

import com.jfinal.plugin.activerecord.Model;

/**
 * 数据库表名称：EB_MESSAGE
 * */
public class EbMessage extends Model<EbMessage> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3183704402325179426L;

	public static final EbMessage dao = new EbMessage();
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
	 * 数据库列名称：MESSAGE_ID
	 * */
	private java.math.BigDecimal messageId;

	public java.math.BigDecimal getMessageId() {
		this.messageId = this.get("MESSAGE_ID");
		return this.messageId;
	}

	public void setMessageId(java.math.BigDecimal messageId) {
		this.messageId = messageId;
		this.set("MESSAGE_ID", this.messageId);
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
	 * 数据库列名称：MESSAGE_CONTENT
	 * */
	private java.lang.String messageContent;

	public java.lang.String getMessageContent() {
		this.messageContent = this.get("MESSAGE_CONTENT");
		return this.messageContent;
	}

	public void setMessageContent(java.lang.String messageContent) {
		this.messageContent = messageContent;
		this.set("MESSAGE_CONTENT", this.messageContent);
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
	 * 数据库列名称：STATUS
	 * */
	private java.math.BigDecimal status;

	public java.math.BigDecimal getStatus() {
		this.status = this.get("STATUS");
		return this.status;
	}

	public void setStatus(java.math.BigDecimal status) {
		this.status = status;
		this.set("STATUS", this.status);
	}

	/**
	 * 数据库列名称：PARAM_ID
	 * */
	private java.math.BigDecimal paramId;

	public java.math.BigDecimal getParamId() {
		this.paramId = this.get("PARAM_ID");
		return this.paramId;
	}

	public void setParamId(java.math.BigDecimal paramId) {
		this.paramId = paramId;
		this.set("PARAM_ID", this.paramId);
	}


	/**
	 * 数据库列名称：MESSAGE_TIME
	 * */
	private java.lang.String messageTime;

	public java.lang.String getMessageTime() {
		this.messageTime = this.get("MESSAGE_TIME");
		
		return this.messageTime;
	}

	public void setMessageTime(java.lang.String messageTime) {
		this.messageTime = messageTime;
		this.set("MESSAGE_TIME", this.messageTime);
	}

	/**
	 * 企业信息model
	 */
	private EbBusiness businessInfo;
	
	public EbBusiness getBusinessInfo() {
		return businessInfo;
	}

	public void setBusinessInfo(EbBusiness businessInfo) {
		this.businessInfo = businessInfo;
	}
	
	/**
	 * 用户信息model
	 */
	private EbUserInfo userInfo;
	
	public EbUserInfo getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(EbUserInfo userInfo) {
		this.userInfo = userInfo;
	}
	
	/**
	 * 是否有回复
	 * @return "1"有回复;"0"无回复
	 */
	private String is_reply;
	
	public String getIs_reply() {
		return is_reply;
	}

	public void setIs_reply(String is_reply) {
		this.is_reply = is_reply;
	}
	
	/**
	 * 数据库列名称：EXAM_STATUS（0 未审 1 已审  2审核不通过）
	 * */
	private java.math.BigDecimal examStatus;

	public java.math.BigDecimal getExamStatus() {
		this.examStatus = this.get("EXAM_STATUS");
		return this.examStatus;
	}

	public void setExamStatus(java.math.BigDecimal examStatus) {
		this.examStatus = examStatus;
		this.set("EXAM_STATUS", this.examStatus);
	}
}