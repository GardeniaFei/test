package cn.com.minstone.eBusiness.model;

import com.jfinal.plugin.activerecord.Model;

public class EbCenter extends Model<EbCenter>{

	/**
	 * 中心表   展示信息
	 */
	private static final long serialVersionUID = 1L;

	public static final EbCenter dao = new EbCenter();
	
	/**
	 * 数据库列名称：MAP_CODE 对应地图的主键
	 * */
	private java.lang.String mapCode;

	public java.lang.String getMapCode() {
		this.mapCode = this.get("MAP_CODE");
		return this.mapCode;
	}

	public void setMapCode(java.lang.String mapCode) {
		this.mapCode = mapCode;
		this.set("MAP_CODE", this.mapCode);
	}
	
	/**
	 * 数据库列名称：CENTER_ID
	 * */
	private java.math.BigDecimal centerId;
	
	public java.math.BigDecimal getCenterId() {
		this.centerId = this.get("CENTER_ID");
		return centerId;
	}

	public void setCenterId(java.math.BigDecimal centerId) {
		this.centerId = centerId;
		this.set("CENTER_ID", this.centerId);
	}
	
	/**
	 * 数据库列名称：SLOGAN 标语
	 * */
	private java.lang.String slogan;

	public java.lang.String getSlogan() {
		this.slogan = this.get("slogan");
		return slogan;
	}

	public void setSlogan(java.lang.String slogan) {
		this.slogan = slogan;
		this.set("slogan", this.slogan);
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
	 * 数据库列名称：CENTER_INTRO 简介
	 * */
	private java.lang.String centerIntro;

	public java.lang.String getCenterIntro() {
		this.centerIntro = this.get("CENTER_INTRO");
		return this.centerIntro;
	}

	public void setCenterIntro(java.lang.String centerIntro) {
		this.centerIntro = centerIntro;
		this.set("CENTER_INTRO", this.centerIntro);
	}
}



