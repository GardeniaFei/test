package cn.com.minstone.eBusiness.model;

import java.math.BigDecimal;

import com.jfinal.plugin.activerecord.Model;

public class EbBussShow extends Model<EbBussShow> {

	/**
	 * 企业展示信息表
	 */
	private static final long serialVersionUID = -4597332711574685597L;
	
	public static final EbBussShow dao = new EbBussShow();

	private BigDecimal show_id;
	
	public BigDecimal getShow_id() {
		this.show_id = this.get("SHOW_ID");
		return show_id;
	}

	public void setShow_id(BigDecimal show_id) {
		this.show_id = show_id;
		this.set("show_id", this.show_id);
	}
	
	private BigDecimal buss_id;//企业Id
	
	public BigDecimal getBuss_id() {
		this.buss_id = this.get("BUSS_ID");
		return buss_id;
	}

	public void setBuss_id(BigDecimal buss_id) {
		this.buss_id = buss_id;
		this.set("BUSS_ID", this.buss_id);
	}
	
	private String map_code;//图块id

	public String getMap_code() {
		this.map_code = this.get("MAP_CODE");
		return map_code;
	}

	public void setMap_code(String map_code) {
		this.map_code = map_code;
		this.set("MAP_CODE", this.map_code);
	}

	
	private String show_intro;//介绍
	
	public String getShow_intro() {
		this.show_intro = this.get("SHOW_INTRO");
		return show_intro;
	}

	public void setShow_intro(String show_intro) {
		this.show_intro = show_intro;
		this.set("SHOW_INTRO", this.show_intro);
	}
	
	private String refresh_time;
	
	public String getRefresh_time() {
		this.refresh_time = this.get("REFRESH_TIME");
		return refresh_time;
	}

	public void setRefresh_time(String refresh_time) {
		this.refresh_time = refresh_time;
		this.set("REFRESH_TIME", this.refresh_time);
	}
	
	private BigDecimal order_seq;//排列顺序

	public BigDecimal getOrder_seq() {
		this.order_seq = this.get("ORDER_SEQ");
		return order_seq;
	}

	public void setOrder_seq(BigDecimal order_seq) {
		this.order_seq = order_seq;
		this.set("ORDER_SEQ", this.order_seq);
	}
	
}
