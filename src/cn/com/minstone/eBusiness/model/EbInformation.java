package cn.com.minstone.eBusiness.model;

import com.jfinal.plugin.activerecord.Model;

public class EbInformation extends Model<EbInformation>{

	/**
	 * 资讯表
	 */
	private static final long serialVersionUID = -216902161159897642L;
	
	public static final EbInformation dao = new EbInformation();
	
	/**
	 * 数据库列名称：INFORMATION_ID
	 * */
	private java.math.BigDecimal informationId;
	
	public java.math.BigDecimal getInformationId() {
		this.informationId = this.get("INFORMATION_ID");
		return informationId;
	}

	public void setInformationId(java.math.BigDecimal informationId) {
		this.informationId = informationId;
		this.set("INFORMATION_ID", this.informationId);
	}
	
	/**
	 * 数据库列名称：INFORMATION_TYPE
	 * */
	private java.math.BigDecimal informationType;
	
	public java.math.BigDecimal getInformationType() {
		this.informationType = this.get("INFORMATION_TYPE");
		return informationType;
	}

	public void setInformationType(java.math.BigDecimal informationType) {
		this.informationType = informationType;
		this.set("INFORMATION_TYPE", this.informationType);
	}
	
	/**
	 * 数据库列名称：INFORMATION_BUSS_ID
	 * */
	private java.math.BigDecimal informationBussId;
	
	public java.math.BigDecimal getInformationBussId() {
		this.informationBussId = this.get("INFORMATION_BUSS_ID");
		return informationBussId;
	}

	public void setInformationBussId(java.math.BigDecimal informationBussId) {
		this.informationBussId = informationBussId;
		this.set("INFORMATION_BUSS_ID", this.informationBussId);
	}

	/**
	 * 数据库列名称：REFRESH_TIME 更新时间
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
	 * 数据库列名称：INFORMATION_CONTENT
	 * */
	private java.lang.String informationContent;

	public java.lang.String getInformationContent() {
		this.informationContent = this.get("INFORMATION_CONTENT");
		return this.informationContent;
	}

	public void setInformationContent(java.lang.String informationContent) {
		this.informationContent = informationContent;
		this.set("INFORMATION_CONTENT", this.informationContent);
	}
	
	/**
	 * 数据库列名称：INFORMATION_TITLE
	 * */
	private java.lang.String informationTitle;

	public java.lang.String getInformationTile() {
		this.informationTitle = this.get("INFORMATION_TITLE");
		return this.informationTitle;
	}

	public void setInformationTitle(java.lang.String informationTitle) {
		this.informationTitle= informationTitle;
		this.set("INFORMATION_TITLE", this.informationTitle);
	}
	
}
