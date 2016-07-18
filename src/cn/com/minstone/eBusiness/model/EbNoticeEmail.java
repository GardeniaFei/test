package cn.com.minstone.eBusiness.model;

import com.jfinal.plugin.activerecord.Model;

public class EbNoticeEmail extends Model<EbNoticeEmail> {

	/**
	 * 用户投稿邮箱
	 */
	private static final long serialVersionUID = 1L;
	public static final EbNoticeEmail dao = new EbNoticeEmail();
	
	/**
	 * 数据库列名称：EMAIL_ID通知公告Id
	 * */
	private java.math.BigDecimal email_id;
	
	public java.math.BigDecimal getEmail_id() {
		this.email_id = this.get("email_id");
		return email_id;
	}

	public void setEmail_id(java.math.BigDecimal email_id) {
		this.email_id = email_id;
		this.set("email_id", this.email_id);
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
	 * 数据库列名称：EMAIL_NAME 通知主题
	 * */
	private java.lang.String email_name;
	
	public java.lang.String getEmail_name() {
		this.email_name = this.get("email_name");
		return email_name;
	}

	public void setEmail_name(java.lang.String email_name) {
		this.email_name = email_name;
		this.set("email_name", this.email_name);
	}
	
	/**
	 * 数据库列名称：creat_TIME 创建时间
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

}
