package cn.com.minstone.eBusiness.model;

import com.jfinal.plugin.activerecord.Model;

public class EbBussItem extends Model<EbBussItem> {

	/**
	 * 企业新增事项表
	 */
	private static final long serialVersionUID = 1L;
	
	public static final EbBussItem dao = new EbBussItem();

	/**
	 * 数据库列名称：BITEM_ID  企业事项Id
	 * */
	private java.math.BigDecimal bItem_id;
	
	public java.math.BigDecimal getBItem_id() {
		this.bItem_id = this.get("bItem_id");
		return bItem_id;
	}

	public void setBItem_id(java.math.BigDecimal bItem_id) {
		this.bItem_id = bItem_id;
		this.set("bItem_id", this.bItem_id);
	}
	
	/**
	 * 数据库列名称：BUSS_ID  企业Id
	 * */
	private java.math.BigDecimal buss_id;
	
	public java.math.BigDecimal getBuss_id() {
		this.buss_id = this.get("buss_id");
		return buss_id;
	}

	public void setBuss_id(java.math.BigDecimal buss_id) {
		this.buss_id = buss_id;
		this.set("buss_id", this.buss_id);
	}
	
	/**
	 * 数据库列名称：BTRAN_STATUS  办理状态0未签收，1已签收，2被退回
	 * */
	private java.math.BigDecimal btran_status;
	
	public java.math.BigDecimal getBtran_status() {
		this.btran_status = this.get("btran_status");
		return btran_status;
	}

	public void setBtran_status(java.math.BigDecimal btran_status) {
		this.btran_status = btran_status;
		this.set("btran_status", this.btran_status);
	}
	
	/**
	 * 数据库列名称：BUSER_CODE 发起企业账户
	 * */
	private java.lang.String buser_code;
	
	public java.lang.String getBuser_code() {
		this.buser_code = this.get("buser_code");
		return buser_code;
	}

	public void setBuser_code(java.lang.String buser_code) {
		this.buser_code = buser_code;
		this.set("buser_code", this.buser_code);
	}
	
	/**
	 * 数据库列名称：VUSER_CODE VIP服务专业code
	 * */
	private java.lang.String vuser_code;
	
	public java.lang.String getVuser_code() {
		this.vuser_code = this.get("vuser_code");
		return vuser_code;
	}

	public void setVuser_code(java.lang.String vuser_code) {
		this.vuser_code = vuser_code;
		this.set("vuser_code", this.vuser_code);
	}
	
	/**
	 * 数据库列名称：BITEM_NAME 企业发起事项名称
	 * */
	private java.lang.String bItem_name;
	
	public java.lang.String getBItem_name() {
		this.bItem_name = this.get("bItem_name");
		return bItem_name;
	}

	public void setBItem_name(java.lang.String bItem_name) {
		this.bItem_name = bItem_name;
		this.set("bItem_name", this.bItem_name);
	}
	
	/**
	 * 数据库列名称：buss_name
	 * */
	private java.lang.String buss_name;

	public java.lang.String getBuss_name() {
		this.buss_name = this.get("buss_name");
		return this.buss_name;
	}

	public void setBuss_name(java.lang.String buss_name) {
		this.buss_name = buss_name;
		this.set("buss_name", this.buss_name);
	}
	
	/**
	 * 数据库列名称：CTREAT_TIME 创建时间
	 * */
	private java.lang.String creat_time;
	
	public java.lang.String getCreat_time() {
		this.creat_time = this.get("creat_time");
		return creat_time;
	}

	public void setCreat_time(java.lang.String creat_time) {
		this.creat_time = creat_time;
		this.set("creat_time", this.creat_time);
	}
	
	/**
	 * 数据库列名称：SIGN_TIME 签收时间
	 * */
	private java.lang.String sign_time;
	
	public java.lang.String getSign_time() {
		this.sign_time = this.get("sign_time");
		return sign_time;
	}

	public void setSign_time(java.lang.String sign_time) {
		this.sign_time = sign_time;
		this.set("sign_time", this.sign_time);
	}
	
	/**
	 * 数据库列名称：bitem_describe 事项描述
	 * */
	private java.lang.String bitem_describe;
	
	public java.lang.String getBitem_describe() {
		this.bitem_describe = this.get("bitem_describe");
		return bitem_describe;
	}

	public void setBitem_describe(java.lang.String bitem_describe) {
		this.bitem_describe = bitem_describe;
		this.set("bitem_describe", this.bitem_describe);
	}
	
}
