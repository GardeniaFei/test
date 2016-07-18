package cn.com.minstone.eBusiness.model;

import com.jfinal.plugin.activerecord.Model;

/**
 * 数据库表名称：EB_EVALUATE
 * */
public class EbEvaluate extends Model<EbEvaluate> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5258708024342827264L;

	public static final EbEvaluate dao = new EbEvaluate();
	
	private EbBusiness bussInfo;
	
	public EbBusiness getBussInfo() {
		return bussInfo;
	}

	public void setBussInfo(EbBusiness bussInfo) {
		this.bussInfo = bussInfo;
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
	 * 数据库列名称：EVALUATE_TIME
	 * */
	private java.lang.String evaluateTime;

	public java.lang.String getEvaluateTime() {
		this.evaluateTime = this.get("EVALUATE_TIME");
		return this.evaluateTime;
	}

	public void setEvaluateTime(java.lang.String evaluateTime) {
		this.evaluateTime = evaluateTime;
		this.set("EVALUATE_TIME", this.evaluateTime);
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
	 * 数据库列名称：EVALUATE_ID
	 * */
	private java.math.BigDecimal evaluateId;

	public java.math.BigDecimal getEvaluateId() {
		this.evaluateId = this.get("EVALUATE_ID");
		return this.evaluateId;
	}

	public void setEvaluateId(java.math.BigDecimal evaluateId) {
		this.evaluateId = evaluateId;
		this.set("EVALUATE_ID", this.evaluateId);
	}

	/**
	 * 数据库列名称：COMP_GRADE
	 * */
	private java.math.BigDecimal compGrade;

	public java.math.BigDecimal getCompGrade() {
		this.compGrade = this.get("COMP_GRADE");
		return this.compGrade;
	}

	public void setCompGrade(java.math.BigDecimal compGrade) {
		this.compGrade = compGrade;
		this.set("COMP_GRADE", this.compGrade);
	}

}