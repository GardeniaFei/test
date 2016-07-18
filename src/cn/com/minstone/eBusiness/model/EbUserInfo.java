package cn.com.minstone.eBusiness.model;

import com.jfinal.plugin.activerecord.Model;

/**
 * 数据库表名称：EB_USER_INFO
 * */
public class EbUserInfo extends Model<EbUserInfo> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2885237812682282709L;

	public static final EbUserInfo dao = new EbUserInfo();
	
	private EbBusiness bussInfo = null;
	
	public EbBusiness getBussInfo() {
		return bussInfo;
	}

	public void setBussInfo(EbBusiness bussInfo) {
		this.bussInfo = bussInfo;
	}

	private EbDepart deptInfo = null;
	
	public EbDepart getDeptInfo() {
		return deptInfo;
	}

	public void setDeptInfo(EbDepart deptInfo) {
		this.deptInfo = deptInfo;
	}

	/**
	 * 数据库列名称：USER_EMAIL
	 * */
	private java.lang.String userEmail;

	public java.lang.String getUserEmail() {
		this.userEmail = this.get("USER_EMAIL");
		return this.userEmail;
	}

	public void setUserEmail(java.lang.String userEmail) {
		this.userEmail = userEmail;
		this.set("USER_EMAIL", this.userEmail);
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
	 * 数据库列名称：USER_TEL
	 * */
	private java.lang.String userTel;

	public java.lang.String getUserTel() {
		this.userTel = this.get("USER_TEL");
		return this.userTel;
	}

	public void setUserTel(java.lang.String userTel) {
		this.userTel = userTel;
		this.set("USER_TEL", this.userTel);
	}
	
	private java.lang.String strSex;

	public java.lang.String getSex() {
		return this.strSex;
	}

	public void setSex(java.lang.String strSex) {
		this.strSex = strSex;
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
	 * mDepartId
	 * */
	private java.lang.String myDepartId;

	public java.lang.String getMyDepartId() {
		return this.myDepartId;
	}

	public void setMyDepartId(java.lang.String myDepartId) {
		this.myDepartId = myDepartId;
	}

	/**
	 * mDepartName
	 * */
	private java.lang.String myDepartName;

	public java.lang.String getMyDepartName() {
		return this.myDepartName;
	}

	public void setMyDepartName(java.lang.String mDepartName) {
		this.myDepartName = mDepartName;
	}

	/**
	 * 数据库列名称：IS_MDEPT 是否多角色 1 是、0 否
	 * */
	private java.math.BigDecimal isMDept;

	public java.math.BigDecimal getIsMDept() {
		this.isMDept = this.get("IS_MDEPT");
		return this.isMDept;
	}

	public void setIsMDept(java.math.BigDecimal isMDept) {
		this.isMDept = isMDept;
		this.set("IS_MDEPT", this.isMDept);
	}

	/**
	 * 数据库列名称：SID
	 * */
	private java.lang.String sid;

	public java.lang.String getSid() {
		this.sid = this.get("SID");
		return this.sid;
	}

	public void setSid(java.lang.String sid) {
		this.sid = sid;
		this.set("SID", this.sid);
	}

	/**
	 * 数据库列名称：IS_DELET
	 * */
	private java.math.BigDecimal isDelet;

	public java.math.BigDecimal getIsDelet() {
		this.isDelet = this.get("IS_DELET");
		return this.isDelet;
	}

	public void setIsDelet(java.math.BigDecimal isDelet) {
		this.isDelet = isDelet;
		this.set("IS_DELET", this.isDelet);
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
	 * 是否为部门签收人1为是，0为不是
	 */
	private String is_sign_Man;
	

	public String getIs_sign_Man() {
		return is_sign_Man;
	}

	public void setIs_sign_Man(String is_sign_Man) {
		this.is_sign_Man = is_sign_Man;
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
	 * 数据库列名称：ROLE_TYPE
	 * */
	private java.math.BigDecimal roleType;

	public java.math.BigDecimal getRoleType() {
		this.roleType = this.get("ROLE_TYPE");
		return this.roleType;
	}

	public void setRoleType(java.math.BigDecimal roleType) {
		this.roleType = roleType;
		this.set("ROLE_TYPE", this.roleType);
	}

	/**
	 * 数据库列名称：AUTHORITY
	 * */
	private java.math.BigDecimal authority;

	public java.math.BigDecimal getAuthority() {
		this.authority = this.get("AUTHORITY");
		return this.authority;
	}

	public void setAuthority(java.math.BigDecimal authority) {
		this.authority = authority;
		this.set("AUTHORITY", this.authority);
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
	 * 数据库列名称：USER_PHOTO
	 * */
	private java.lang.String userPhoto;

	public java.lang.String getUserPhoto() {
		this.userPhoto = this.get("USER_PHOTO");
		return this.userPhoto;
	}

	public void setUserPhoto(java.lang.String userPhoto) {
		this.userPhoto = userPhoto;
		this.set("USER_PHOTO", this.userPhoto);
	}

	/**
	 * 数据库列名称：PASSWORD
	 * */
	private java.lang.String password;

	public java.lang.String getPassword() {
		this.password = this.get("PASSWORD");
		return this.password;
	}

	public void setPassword(java.lang.String password) {
		this.password = password;
		this.set("PASSWORD", this.password);
	}

	/**
	 * 数据库列名称：USER_ACCOUNT
	 * */
	private java.lang.String userAccount;

	public java.lang.String getUserAccount() {
		this.userAccount = this.get("USER_ACCOUNT");
		return this.userAccount;
	}

	public void setUserAccount(java.lang.String userAccount) {
		this.userAccount = userAccount;
		this.set("USER_ACCOUNT", this.userAccount);
	}

	@Override
	public String toString() {
		return "EbUserInfo [bussInfo=" + bussInfo + ", deptInfo=" + deptInfo
				+ ", userEmail=" + userEmail + ", businessName=" + businessName
				+ ", userTel=" + userTel + ", strSex=" + strSex
				+ ", departName=" + departName + ", mDepartId=" + myDepartId
				+ ", mDepartName=" + myDepartName + ", isMDept=" + isMDept
				+ ", sid=" + sid + ", isDelet=" + isDelet + ", departId="
				+ departId + ", is_sign_Man=" + is_sign_Man + ", businessId="
				+ businessId + ", roleType=" + roleType + ", authority="
				+ authority + ", userId=" + userId + ", userName=" + userName
				+ ", userPhoto=" + userPhoto + ", password=" + password
				+ ", userAccount=" + userAccount + "]";
	}

	
}