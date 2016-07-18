package cn.com.minstone.eBusiness.model;

import com.jfinal.plugin.activerecord.Model;

/**
 * 数据库表名称：SMS_TBL
 * */
public class SmsTbl extends Model<SmsTbl> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8748870536528735764L;

	public static final SmsTbl dao = new SmsTbl();

	/**
	 * 数据库列名称：SMS_SEQ
	 * */
	private java.math.BigDecimal smsSeq;

	public java.math.BigDecimal getSmsTbl() {
		this.smsSeq = this.get("SMS_SEQ");
		return this.smsSeq;
	}

	public void setSmsTbl(java.math.BigDecimal smsSeq) {
		this.smsSeq = smsSeq;
		this.set("SMS_SEQ", this.smsSeq);
	}

	/**
	 * 数据库列名称：SEND_USER
	 * */
	private java.lang.String sendUser;

	public java.lang.String getSendUser() {
		this.sendUser = this.get("SEND_USER");
		return this.sendUser;
	}

	public void setSendUser(java.lang.String sendUser) {
		this.sendUser = sendUser;
		this.set("SEND_USER", this.sendUser);
	}

	/**
	 * 数据库列名称：MOBL_NMBR
	 * */
	private java.lang.String moblNmbr;

	public java.lang.String getMoblNmbr() {
		this.moblNmbr = this.get("MOBL_NMBR");
		return this.moblNmbr;
	}

	public void setMoblNmbr(java.lang.String moblNmbr) {
		this.moblNmbr = moblNmbr;
		this.set("MOBL_NMBR", this.moblNmbr);
	}
	
	/**
	 * 数据库列名称：RECV_USER
	 * */
	private java.lang.String recvUser;

	public java.lang.String getRecvUser() {
		this.recvUser = this.get("RECV_USER");
		return this.recvUser;
	}

	public void setRecvUser(java.lang.String recvUser) {
		this.recvUser = recvUser;
		this.set("RECV_USER", this.recvUser);
	}

	/**
	 * 数据库列名称：SMS_MSG
	 * */
	private java.lang.String smsMsg;

	public java.lang.String getSmsMsg() {
		this.smsMsg = this.get("SMS_MSG");
		return this.smsMsg;
	}

	public void setSmsMsg(java.lang.String smsMsg) {
		this.smsMsg = smsMsg;
		this.set("SMS_MSG", this.smsMsg);
	}

	/**
	 * 数据库列名称：SAVE_TIME
	 * */
	private java.sql.Date saveTime;

	public java.sql.Date getSaveTime() {
		this.saveTime = this.get("SAVE_TIME");
		return this.saveTime;
	}

	public void setSaveTime(java.sql.Date saveTime) {
		this.saveTime = saveTime;
		this.set("SAVE_TIME", this.saveTime);
	}

	/**
	 * 数据库列名称：SEND_TIME
	 * */
	private java.sql.Date sendTime;

	public java.sql.Date getSendTime() {
		this.sendTime = this.get("SEND_TIME");
		return this.sendTime;
	}

	public void setSendTime(java.sql.Date sendTime) {
		this.sendTime = sendTime;
		this.set("SEND_TIME", this.sendTime);
	}

	/**
	 * 数据库列名称：SEND_STAT
	 * */
	private java.math.BigDecimal sendStat;

	public java.math.BigDecimal getSendStat() {
		this.sendStat = this.get("SEND_STAT");
		return this.sendStat;
	}

	public void setSendStat(java.math.BigDecimal sendStat) {
		this.sendStat = sendStat;
		this.set("SEND_STAT", this.sendStat);
	}

	/**
	 * 数据库列名称：SEND_NMBR
	 * */
	private java.math.BigDecimal sendNmbr;

	public java.math.BigDecimal getSendNmbr() {
		this.sendNmbr = this.get("SEND_NMBR");
		return this.sendNmbr;
	}

	public void setSendNmbr(java.math.BigDecimal sendNmbr) {
		this.sendNmbr = sendNmbr;
		this.set("SEND_NMBR", this.sendNmbr);
	}

	/**
	 * 数据库列名称：FAIL_RESN
	 * */
	private java.lang.String failResn;

	public java.lang.String getFailResn() {
		this.failResn = this.get("FAIL_RESN");
		return this.failResn;
	}

	public void setFailResn(java.lang.String failResn) {
		this.failResn = failResn;
		this.set("FAIL_RESN", this.failResn);
	}

	/**
	 * 数据库列名称：SMS_TYPE
	 * */
	private java.math.BigDecimal smsType;

	public java.math.BigDecimal getSmsType() {
		this.smsType = this.get("SMS_TYPE");
		return this.smsType;
	}

	public void setSmsType(java.math.BigDecimal smsType) {
		this.smsType = smsType;
		this.set("SMS_TYPE", this.smsType);
	}

	/**
	 * 数据库列名称：ATTA_INFO1
	 * */
	private java.lang.String attaInfo1;

	public java.lang.String getAttaInfo1() {
		this.attaInfo1 = this.get("ATTA_INFO1");
		return this.attaInfo1;
	}

	public void setAttaInfo1(java.lang.String attaInfo1) {
		this.attaInfo1 = attaInfo1;
		this.set("ATTA_INFO1", this.attaInfo1);
	}

	/**
	 * 数据库列名称：ATTA_INFO2
	 * */
	private java.lang.String attaInfo2;

	public java.lang.String getAttaInfo2() {
		this.attaInfo2 = this.get("ATTA_INFO2");
		return this.attaInfo2;
	}

	public void setAttaInfo2(java.lang.String attaInfo2) {
		this.attaInfo2 = attaInfo2;
		this.set("ATTA_INFO2", this.attaInfo2);
	}

	/**
	 * 数据库列名称：WANT_SEND_TIME
	 * */
	private java.sql.Date wantSendTime;

	public java.sql.Date getWantSendTime() {
		this.wantSendTime = this.get("WANT_SEND_TIME");
		return this.wantSendTime;
	}

	public void setWantSendTime(java.sql.Date wantSendTime) {
		this.wantSendTime = wantSendTime;
		this.set("WANT_SEND_TIME", this.wantSendTime);
	}

	/**
	 * 数据库列名称：SERVICE_ID
	 * */
	private java.lang.String serviceId;

	public java.lang.String getServiceId() {
		this.serviceId = this.get("SERVICE_ID");
		return this.serviceId;
	}

	public void setServiceId(java.lang.String serviceId) {
		this.serviceId = serviceId;
		this.set("SERVICE_ID", this.serviceId);
	}

}