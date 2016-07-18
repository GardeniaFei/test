package cn.com.minstone.eBusiness.model;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.jfinal.plugin.activerecord.Model;

/**
 * 数据库表名称：EB_BUSINESS_TYPE
 * */
public class EbBusinessType extends Model<EbBusinessType> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3385206856796113495L;
	
	public static final EbBusinessType dao = new EbBusinessType();
	/**
	 * 数据库列名称：TYPE_NAME
	 * */
	private java.lang.String typeName;

	public java.lang.String getTypeName() {
		this.typeName = this.get("TYPE_NAME");
		return this.typeName;
	}

	public void setTypeName(java.lang.String typeName) {
		this.typeName = typeName;
		this.set("TYPE_NAME", this.typeName);
	}

	/**
	 * 数据库列名称：TYPE_ID
	 * */
	private java.math.BigDecimal typeId;

	public java.math.BigDecimal getTypeId() {
		this.typeId = this.get("TYPE_ID");
		return this.typeId;
	}

	public void setTypeId(java.math.BigDecimal typeId) {
		this.typeId = typeId;
		this.set("TYPE_ID", this.typeId);
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