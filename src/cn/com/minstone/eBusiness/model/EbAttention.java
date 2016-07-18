package cn.com.minstone.eBusiness.model;

import com.jfinal.plugin.activerecord.Model;

/**
 * 数据库表名称：EB_ATTENTION
 * */
public class EbAttention extends Model<EbAttention> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1648256955186090100L;
	
	public static final EbAttention dao = new EbAttention();

	/**
	 * 数据库列名称：ATTENT_ID
	 * */
	private java.math.BigDecimal attentId;

	public java.math.BigDecimal getAttentId() {
		this.attentId = this.get("ATTENT_ID");
		return this.attentId;
	}

	public void setAttentId(java.math.BigDecimal attentId) {
		this.attentId = attentId;
		this.set("ATTENT_ID", this.attentId);
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