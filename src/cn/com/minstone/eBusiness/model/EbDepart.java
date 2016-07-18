package cn.com.minstone.eBusiness.model;

import java.util.List;

import com.jfinal.plugin.activerecord.Model;

/**
 * 数据库表名称：EB_DEPART
 * */
public class EbDepart extends Model<EbDepart> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1661749872643282669L;
	
	public static final EbDepart dao = new EbDepart();
	
	/**
	 * 获取用户信息modelList
	 */
	private List<EbUserInfo> userInfoList;
	
	public List<EbUserInfo> getUserInfoList() {
		return userInfoList;
	}

	public void setUserInfoList(List<EbUserInfo> userList) {
		this.userInfoList = userList;
	}
	
	/**
	 * 获取用户信息model
	 */
	private EbUserInfo userInfo;
	
	public EbUserInfo getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(EbUserInfo userInfo) {
		this.userInfo = userInfo;
	}
	
	/**
	 * 部门事项配置mode列表
	 */
	private List<EbItem> itemList;
	
	public List<EbItem> getItemList() {
		return itemList;
	}

	public void setItemList(List<EbItem> itemList) {
		this.itemList = itemList;
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
	 * 数据库列名称：WORK_TEL
	 * */
	private java.lang.String workTel;

	public java.lang.String getWorkTel() {
		this.workTel = this.get("WORK_TEL");
		return this.workTel;
	}

	public void setWorkTel(java.lang.String workTel) {
		this.workTel = workTel;
		this.set("WORK_TEL", this.workTel);
	}

	/**
	 * 数据库列名称：WORK_ADDRESS
	 * */
	private java.lang.String workAddress;

	public java.lang.String getWorkAddress() {
		this.workAddress = this.get("WORK_ADDRESS");
		return this.workAddress;
	}

	public void setWorkAddress(java.lang.String departName) {
		this.workAddress = departName;
		this.set("WORK_ADDRESS", this.workAddress);
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
	 * 数据库列名称：DEPART_TYPE
	 * */
	private java.math.BigDecimal departType;

	public java.math.BigDecimal getDepartType() {
		this.departType = this.get("DEPART_TYPE");
		return this.departType;
	}

	public void setDepartType(java.math.BigDecimal departType) {
		this.departType = departType;
		this.set("DEPART_TYPE", this.departType);
	}

	/**
	 * 数据库列名称：REFRESH_TIME
	 * */
	private java.lang.String refreshTime;

	public java.lang.String getRefreshTime() {
		this.refreshTime = this.get("REFRESH_TIME");
		return this.refreshTime;
	}

	public void setRefreshTime(java.lang.String refreshTime) {
		this.refreshTime = refreshTime;
		this.set("REFRESH_TIME", this.refreshTime);
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

}