package cn.com.minstone.eBusiness.model;

import com.jfinal.plugin.activerecord.Model;

public class EbNotice extends Model<EbNotice> {

	/**
	 * 通知公告
	 */
	private static final long serialVersionUID = 1L;
	
	public static final EbNotice dao = new EbNotice();
	
	
	/**
	 * 数据库列名称：NOTICE_ID通知公告Id
	 * */
	private java.math.BigDecimal notice_id;
	
	public java.math.BigDecimal getNotice_id() {
		this.notice_id = this.get("notice_id");
		return notice_id;
	}

	public void setNotice_id(java.math.BigDecimal notice_id) {
		this.notice_id = notice_id;
		this.set("notice_id", this.notice_id);
	}
	
	/**
	 * 数据库列名称：notice_tatus通知公告Id/0草稿状态，1发布状态
	 * */
	private java.math.BigDecimal notice_tatus;
	
	public java.math.BigDecimal getNotice_tatus() {
		this.notice_tatus = this.get("notice_tatus");
		return notice_tatus;
	}

	public void setNotice_tatus(java.math.BigDecimal notice_tatus) {
		this.notice_tatus = notice_tatus;
		this.set("notice_tatus", this.notice_tatus);
	}
	
	/**
	 * 数据库列名称：NOTICE_depart_Id发布用户的部门id
	 * */
	private java.math.BigDecimal notice_depart_id;
	
	public java.math.BigDecimal getNotice_depart_id() {
		this.notice_depart_id = this.get("notice_depart_id");
		return notice_depart_id;
	}

	public void setNotice_depart_id(java.math.BigDecimal notice_depart_id) {
		this.notice_depart_id = notice_depart_id;
		this.set("notice_depart_id", this.notice_depart_id);
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
	 * 数据库列名称：is_delet
	 * */
	private java.math.BigDecimal is_delet;
	
	public java.math.BigDecimal getIs_delet() {
		this.is_delet = this.get("is_delet");
		return is_delet;
	}

	public void setIs_delet(java.math.BigDecimal is_delet) {
		this.is_delet = is_delet;
		this.set("is_delet", this.is_delet);
	}
	
	/**
	 * 数据库列名称：NOTICE_TYPE 通告对象，1部门，2企业，3部门和企业
	 * */
	private java.math.BigDecimal notice_type;
	
	public java.math.BigDecimal getNotice_type() {
		this.notice_type = this.get("notice_type");
		return notice_type;
	}

	public void setNotice_type(java.math.BigDecimal notice_type) {
		this.notice_type = notice_type;
		this.set("notice_type", this.notice_type);
	}
	

	/**
	 * 数据库列名称：NOTICE_TITLE 通知主题
	 * */
	private java.lang.String notice_title;
	
	public java.lang.String getNotice_title() {
		this.notice_title = this.get("notice_title");
		return notice_title;
	}

	public void setNotice_title(java.lang.String notice_title) {
		this.notice_title = notice_title;
		this.set("notice_title", this.notice_title);
	}
	

	/**
	 * 数据库列名称：NOTICE_CONTENT 通知内容
	 * */
	private String notice_content;
	
	public String getNotice_content() {
		this.notice_content = this.get("notice_content");
		return notice_content;
	}

	public void setNotice_content(String notice_content) {
		this.notice_content = notice_content;
		this.set("notice_content", this.notice_content);
	}

	/**
	 * 数据库列名称：NOTICE_TIME 通知发布时间
	 * */
	private java.lang.String notice_time;
	
	public java.lang.String getNotice_time() {
		this.notice_time = this.get("notice_time");
		return notice_time;
	}

	public void setNotice_time(java.lang.String notice_time) {
		this.notice_time = notice_time;
		this.set("notice_time", this.notice_time);
	}
	
	/**
	 * 数据库列名称：NOTICE_USER_code 发布用户账户
	 * */
	private java.lang.String notice_user_code;
	
	public java.lang.String getNotice_user_code() {
		this.notice_user_code = this.get("notice_user_code");
		return notice_user_code;
	}

	public void setNotice_user_code(java.lang.String notice_user_code) {
		this.notice_user_code = notice_user_code;
		this.set("notice_user_code", this.notice_user_code);
	}
	
	/**
	 * 数据库列名称：NOTICE_USER_name 发布用户姓名
	 * */
	private java.lang.String notice_user_name;
	
	public java.lang.String getNotice_user_name() {
		this.notice_user_name = this.get("notice_user_name");
		return notice_user_name;
	}

	public void setNotice_user_name(java.lang.String notice_user_name) {
		this.notice_user_name = notice_user_name;
		this.set("notice_user_name", this.notice_user_name);
	}

	
	/**
	 * 数据库列名称：NOTICE_Buss_Name 发布企业名字
	 * */
	private java.lang.String notice_buss_name;
	
	public java.lang.String getNotice_buss_name() {
		this.notice_buss_name = this.get("notice_buss_name");
		return notice_buss_name;
	}

	public void setNotice_buss_name(java.lang.String notice_buss_name) {
		this.notice_buss_name = notice_buss_name;
		this.set("notice_buss_name", this.notice_buss_name);
	}
	
	/**
	 * 数据库列名称：NOTICE_depart_name 发布部门名称
	 * */
	private java.lang.String notice_depart_name;

	public java.lang.String getNotice_depart_name() {
		this.notice_depart_name = this.get("notice_depart_name");
		return notice_depart_name;
	}

	public void setNotice_depart_name(java.lang.String notice_depart_name) {
		this.notice_depart_name = notice_depart_name;
		this.set("notice_depart_name", this.notice_depart_name);
	}
	
}
