package cn.com.minstone.eBusiness.model;

import com.jfinal.plugin.activerecord.Model;

/**
 * 数据库表名称：EB_NEWS
 * */
public class EbNews extends Model<EbNews> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -120665383801509555L;

	public static final EbNews dao = new EbNews();
	
	private EbUserInfo userInfo;
	private EbItem itemInfo;
	
	public EbUserInfo getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(EbUserInfo userInfo) {
		this.userInfo = userInfo;
	}

	public EbItem getItemInfo() {
		return itemInfo;
	}

	public void setItemInfo(EbItem itemInfo) {
		this.itemInfo = itemInfo;
	}

	/**
	 * 数据库列名称：NEWS_TYPE
	 * */
	private java.math.BigDecimal newsType;

	public java.math.BigDecimal getNewsType() {
		this.newsType = this.get("NEWS_TYPE");
		return this.newsType;
	}

	public void setNewsType(java.math.BigDecimal newsType) {
		this.newsType = newsType;
		this.set("NEWS_TYPE", this.newsType);
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
	 * 数据库列名称：COUPLE_BACK
	 * */
	private java.lang.String coupleBack;

	public java.lang.String getCoupleBack() {
		this.coupleBack = this.get("COUPLE_BACK");
		return this.coupleBack;
	}

	public void setCoupleBack(java.lang.String coupleBack) {
		this.coupleBack = coupleBack;
		this.set("COUPLE_BACK", this.coupleBack);
	}

	/**
	 * 数据库列名称：NEWS_TIME
	 * */
	private java.lang.String newsTime;

	public java.lang.String getNewsTime() {
		this.newsTime = this.get("NEWS_TIME");
		return this.newsTime;
	}

	public void setNewsTime(java.lang.String newsTime) {
		this.newsTime = newsTime;
		this.set("NEWS_TIME", this.newsTime);
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
	 * 数据库列名称：NEWS_CONTENT
	 * */
	private java.lang.String newsContent;

	public java.lang.String getNewsContent() {
		this.newsContent = this.get("NEWS_CONTENT");
		return this.newsContent;
	}

	public void setNewsContent(java.lang.String newsContent) {
		this.newsContent = newsContent;
		this.set("NEWS_CONTENT", this.newsContent);
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
	 * 数据库列名称：NEWS_ID
	 * */
	private java.math.BigDecimal newsId;

	public java.math.BigDecimal getNewsId() {
		this.newsId = this.get("NEWS_ID");
		return this.newsId;
	}

	public void setNewsId(java.math.BigDecimal newsId) {
		this.newsId = newsId;
		this.set("NEWS_ID", this.newsId);
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

}