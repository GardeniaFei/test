package cn.com.minstone.eBusiness.model;

import com.jfinal.plugin.activerecord.Model;

/**
 * 数据库表名称：EB_DEPT_EVAINFO
 * */
public class EbDeptEvainfo extends Model<EbDeptEvainfo> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7860652288438219279L;

	public static final EbDeptEvainfo dao = new EbDeptEvainfo();

	/**
	 * 数据库列名称：business_name
	 * */
	private java.lang.String businessName;

	public java.lang.String getBusinessName() {
		this.businessName = this.get("business_name");
		return this.businessName;
	}

	public void setBusinessName(java.lang.String businessName) {
		this.businessName = businessName;
		this.set("business_name", this.businessName);
	}

	/**
	 * 数据库列名称：exam_param_id 审核部门评价id
	 * */
	private java.math.BigDecimal examPId;

	public java.math.BigDecimal getExamPId() {
		this.examPId = this.get("exam_param_id");
		return this.examPId;
	}

	public void setExamPId(java.math.BigDecimal examPId) {
		this.examPId = examPId;
		this.set("exam_param_id", this.examPId);
	}
	
	/**
	 * 数据库列名称：business_id
	 * */
	private java.math.BigDecimal businessId;

	public java.math.BigDecimal getBusinessId() {
		this.businessId = this.get("business_id");
		return this.businessId;
	}

	public void setBusinessId(java.math.BigDecimal businessId) {
		this.businessId = businessId;
		this.set("business_id", this.businessId);
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
	 * 数据库列名称：EXAM_STATUS
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

	/**
	 * 数据库列名称：EVA_TIME
	 * */
	private java.lang.String Time;

	public java.lang.String getTime() {
		this.Time = this.get("EVA_TIME");
		return this.Time;
	}

	public void setTime(java.lang.String Time) {
		this.Time = Time;
		this.set("EVA_TIME", this.Time);
	}

	/**
	 * 数据库列名称：GRADE
	 * */
	private java.math.BigDecimal grade;

	public java.math.BigDecimal getGrade() {
		this.grade = this.get("GRADE");
		return this.grade;
	}

	public void setGrade(java.math.BigDecimal grade) {
		this.grade = grade;
		this.set("GRADE", this.grade);
	}

	/**
	 * 数据库列名称：CONTENT
	 * */
	private java.lang.String content;

	public java.lang.String getContent() {
		this.content = this.get("CONTENT");
		return this.content;
	}

	public void setContent(java.lang.String content) {
		this.content = content;
		this.set("CONTENT", this.content);
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

}