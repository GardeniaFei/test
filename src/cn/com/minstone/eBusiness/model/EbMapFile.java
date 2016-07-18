package cn.com.minstone.eBusiness.model;

import com.jfinal.plugin.activerecord.Model;

public class EbMapFile  extends Model<EbMapFile>{

	/**
	 * 展示地图文件图片表
	 */
	private static final long serialVersionUID = 1L;
	
	public static final EbMapFile dao = new EbMapFile();

	/**
	 * 数据库列名称：MAP_CODE
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
	 * 数据库列名称：MAP_URL
	 * */
	private java.lang.String mapUrl;
	
	public java.lang.String getMapUrl() {
		this.mapUrl = this.get("MAP_URL");
		return mapUrl;
	}

	public void setMapUrl(java.lang.String mapUrl) {
		this.mapUrl = mapUrl;
		this.set("MAP_URL", this.mapUrl);
	}
	
	/**
	 * 数据库列名称：MAP_ID
	 * */
	private java.math.BigDecimal mapId;
	
	public java.math.BigDecimal getMapId() {
		this.mapId = this.get("MAP_ID");
		return mapId;
	}

	public void setMapId(java.math.BigDecimal mapId) {
		this.mapId = mapId;
		this.set("MAP_ID", this.mapId);
	}

	/**
	 * 数据库列名称：MAP_CONTEN
	 * */
	private oracle.jdbc.OracleBlob mapConten;
	
	public oracle.jdbc.OracleBlob getMapConten() {
		this.mapConten = this.get("MAP_CONTEN");
		return mapConten;
	}

	public void setMapConten(oracle.jdbc.OracleBlob mapConten) {
		this.mapConten = mapConten;
		this.set("MAP_CONTEN", this.mapConten);
	}
	
	/**
	 * 数据库列名称：MAP_TYPE
	 * */
	private java.math.BigDecimal mapType;

	public java.math.BigDecimal getMapType() {
		this.mapType = this.get("MAP_TYPE");
		return this.mapType;
	}

	public void setMapType(java.math.BigDecimal mapType) {
		this.mapType = mapType;
		this.set("MAP_TYPE", this.mapType);
	}
}
