package cn.com.minstone.eBusiness.model;

import java.util.List;

import org.json.JSONArray;

import com.jfinal.plugin.activerecord.Model;

/**
 * 数据库表名称：EB_TASK_DISTRIBUTE
 * */
public class EbTaskDistribute extends Model<EbTaskDistribute> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1470455038140857623L;

	public static final EbTaskDistribute dao = new EbTaskDistribute();
	
	private EbItem item;
	private EbUserInfo signUser;	//签收人
	private EbDepart dept;	//事项所属部门
	private EbUserInfo distrUser;	//事项分发人
	private EbUserInfo transUser;	//办理人
	
	private long ovTime;//超时天数

	public long getOvTime() {
		return ovTime;
	}

	public void setOvTime(long ovTime) {
		this.ovTime = ovTime;
	}

	public EbItem getItem() {
		return item;
	}

	public void setItem(EbItem item) {
		this.item = item;
	}

	public EbUserInfo getSignUser() {
		return signUser;
	}

	public void setSignUser(EbUserInfo signUser) {
		this.signUser = signUser;
	}

	public EbDepart getDept() {
		return dept;
	}

	public void setDept(EbDepart dept) {
		this.dept = dept;
	}

	public EbUserInfo getDistrUser() {
		return distrUser;
	}

	public void setDistrUser(EbUserInfo distrUser) {
		this.distrUser = distrUser;
	}

	public EbUserInfo getTransUser() {
		return transUser;
	}

	public void setTransUser(EbUserInfo transUser) {
		this.transUser = transUser;
	}

	/**
	 * 数据库列名称：DISTR_ID
	 * */
	private java.math.BigDecimal distrId;

	public java.math.BigDecimal getDistrId() {
		this.distrId = this.get("DISTR_ID");
		return this.distrId;
	}

	public void setDistrId(java.math.BigDecimal distrId) {
		this.distrId = distrId;
		this.set("DISTR_ID", this.distrId);
	}

	/**
	 * 数据库列名称：DEPART_ID
	 * */
	private java.math.BigDecimal departId;

	public java.math.BigDecimal getDepartId() {
		this.departId = this.get("DEPART_ID");
		return this.departId;
	}

	public void setDepartId(java.math.BigDecimal departId) {
		this.departId = departId;
		this.set("DEPART_ID", this.departId);
	}
	
	/**
	 * 数据库列表名称：CONTROL_SEQ
	 */
	private String controlSeq;
	
	public String getControlSeq() {
		this.controlSeq = this.get("CONTROL_SEQ");
		
		return controlSeq;
	}

	public void setControlSeq(String controlSeq) {
		this.controlSeq = controlSeq;
		this.set("CONTROL_SEQ", this.controlSeq);
	}
	
	/**
	 * 数据库列表名称：DEPART_NAME
	 */
	private String departName;
	
	public String getDepartName() {
		this.departName = this.get("DEPART_NAME");
		
		return departName;
	}

	public void setDepartName(String departName) {
		this.departName = departName;
		this.set("DEPART_NAME", this.departName);
	}

	/**
	 * 数据库列名称：TRANSACTION_STATUS
	 * */
	private java.math.BigDecimal transactionStatus;

	public java.math.BigDecimal getTransactionStatus() {
		this.transactionStatus = this.get("TRANSACTION_STATUS");
		return this.transactionStatus;
	}

	public void setTransactionStatus(java.math.BigDecimal transactionStatus) {
		this.transactionStatus = transactionStatus;
		this.set("TRANSACTION_STATUS", this.transactionStatus);
	}

	/**
	 * 数据库列名称：STATUS
	 * */
	private java.math.BigDecimal status;

	public java.math.BigDecimal getStatus() {
		this.status = this.get("STATUS");
		return this.status;
	}

	public void setStatus(java.math.BigDecimal status) {
		this.status = status;
		this.set("STATUS", this.status);
	}

	/**
	 * 数据库列名称：TRANSACTOR_ID
	 * */
	private java.math.BigDecimal transactorId;

	public java.math.BigDecimal getTransactorId() {
		this.transactorId = this.get("TRANSACTOR_ID");
		return this.transactorId;
	}

	public void setTransactorId(java.math.BigDecimal transactorId) {
		this.transactorId = transactorId;
		this.set("TRANSACTOR_ID", this.transactorId);
	}

	/**
	 * 数据库列名称：SIGN_STATUS
	 * */
	private java.math.BigDecimal signStatus;

	public java.math.BigDecimal getSignStatus() {
		this.signStatus = this.get("SIGN_STATUS");
		return this.signStatus;
	}

	public void setSignStatus(java.math.BigDecimal signStatus) {
		this.signStatus = signStatus;
		this.set("SIGN_STATUS", this.signStatus);
	}
	
	/**
	 * 数据库列名称：ITEM_TYPE
	 * */
	private java.math.BigDecimal itemType;

	public java.math.BigDecimal getItemType() {
		this.itemType = this.get("ITEM_TYPE");
		return this.itemType;
	}

	public void setItemType(java.math.BigDecimal itemType) {
		this.itemType = itemType;
		this.set("ITEM_TYPE", this.itemType);
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
	 * 数据库列名称：DISTRIB_TIME
	 * */
	private java.lang.String distribTime;

	public java.lang.String getDistribTime() {
		this.distribTime = this.get("DISTRIB_TIME");
		return this.distribTime;
	}

	public void setDistribTime(java.lang.String distribTime) {
		this.distribTime = distribTime;
		this.set("DISTRIB_TIME", this.distribTime);
	}

	/**
	 * 数据库列名称：TRANS_USER_CODE
	 * */
	private java.lang.String transUserCode;

	public java.lang.String getTransUserCode() {
		this.transUserCode = this.get("TRANS_USER_CODE");
		return this.transUserCode;
	}

	public void setTransUserCode(java.lang.String transUserCode) {
		this.transUserCode = transUserCode;
		this.set("TRANS_USER_CODE", this.transUserCode);
	}

	/**
	 * 数据库列名称：DISTR_USER_CODE
	 * */
	private java.lang.String distrUserCode;

	public java.lang.String getDistrUserCode() {
		this.distrUserCode = this.get("DISTR_USER_CODE");
		return this.distrUserCode;
	}

	public void setDistrUserCode(java.lang.String distrUserCode) {
		this.distrUserCode = distrUserCode;
		this.set("DISTR_USER_CODE", this.distrUserCode);
	}

	/**
	 * 数据库列名称：SIGN_USER_CODE
	 * */
	private java.lang.String signUserCode;

	public java.lang.String getSignUserCode() {
		this.signUserCode = this.get("SIGN_USER_CODE");
		return this.signUserCode;
	}

	public void setSignUserCode(java.lang.String signUserCode) {
		this.signUserCode = signUserCode;
		this.set("SIGN_USER_CODE", this.signUserCode);
	}

	/**
	 * 数据库列名称：TRANSACTION_TIME
	 * */
	private java.lang.String transactionTime;

	public java.lang.String getTransactionTime() {
		this.transactionTime = this.get("TRANSACTION_TIME");
		return this.transactionTime;
	}

	public void setTransactionTime(java.lang.String transactionTime) {
		this.transactionTime = transactionTime;
		this.set("TRANSACTION_TIME", this.transactionTime);
	}

	/**
	 * 数据库列名称：RETURN_REASON
	 * */
	private java.lang.String returnReason;

	public java.lang.String getReturnReason() {
		this.returnReason = this.get("RETURN_REASON");
		return this.returnReason;
	}

	public void setReturnReason(java.lang.String returnReason) {
		this.returnReason = returnReason;
		this.set("RETURN_REASON", this.returnReason);
	}

	/**
	 * 数据库列名称：SIGN_USER_ID
	 * */
	private java.math.BigDecimal signUserId;

	public java.math.BigDecimal getSignUserId() {
		this.signUserId = this.get("SIGN_USER_ID");
		return this.signUserId;
	}

	public void setSignUserId(java.math.BigDecimal signUserId) {
		this.signUserId = signUserId;
		this.set("SIGN_USER_ID", this.signUserId);
	}

	/**
	 * 数据库列名称：ITEM_ID
	 * */
	private java.math.BigDecimal itemId;

	public java.math.BigDecimal getItemId() {
		this.itemId = this.get("ITEM_ID");
		return this.itemId;
	}

	public void setItemId(java.math.BigDecimal itemId) {
		this.itemId = itemId;
		this.set("ITEM_ID", this.itemId);
	}
	
	/**
	 * 数据库列表名称：ITEM_NAME
	 */
	private String itemName;
	
	public String getItemName() {
		this.itemName = this.get("ITEM_NAME");
		
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
		this.set("ITEM_NAME", this.itemName);
	}

	/**
	 * 数据库列名称：TASK_ID
	 * */
	private java.math.BigDecimal taskId;

	public java.math.BigDecimal getTaskId() {
		this.taskId = this.get("TASK_ID");
		return this.taskId;
	}

	public void setTaskId(java.math.BigDecimal taskId) {
		this.taskId = taskId;
		this.set("TASK_ID", this.taskId);
	}

	/**
	 * 数据库列名称：SIGN_TIME
	 * */
	private java.lang.String signTime;

	public java.lang.String getSignTime() {
		this.signTime = this.get("SIGN_TIME");
		return this.signTime;
	}

	public void setSignTime(java.lang.String signTime) {
		this.signTime = signTime;
		this.set("SIGN_TIME", this.signTime);
	}
	
	/**
	 * 数据库列名称：IMAGE_Name
	 * */
	private java.lang.String image_name;
	
	public java.lang.String getImage_name() {
		return image_name;
	}

	public void setImage_name(java.lang.String image_name) {
		this.image_name = image_name;
	}

	/**
	 * 数据库列名称：IMAGE_CONTEN
	 * */
	private oracle.jdbc.OracleBlob imageConten;
	
	public oracle.jdbc.OracleBlob getImageConten() {
		this.imageConten = this.get("IMAGE_CONTEN");
		return imageConten;
	}

	public void setImageConten(oracle.jdbc.OracleBlob imageConten) {
		this.imageConten = imageConten;
		this.set("IMAGE_CONTEN", this.imageConten);
	}
	

	/**
	 * 是否有材料(0没有，1有)
	 */
	private int is_haveImg;
	
	public int getIs_haveImg() {
		return is_haveImg;
	}

	public void setIs_haveImg(int is_haveImg) {
		this.is_haveImg = is_haveImg;
	}
	
	/**
	 * 材料图片列表
	 */
	private List<EbFileImg> imgList;

	public List<EbFileImg> getImgList() {
		return imgList;
	}

	public void setImgList(List<EbFileImg> imgList) {
		this.imgList = imgList;
	}


	/**
	 * 材料图片名称列表
	 */
	private List<String> imgNames;
	
	public List<String> getImgNames() {
		return imgNames;
	}

	public void setImgNames(List<String> imgNames) {
		this.imgNames = imgNames;
	}
	

	/**
	 * 材料json封装
	 */
	private JSONArray imgNameJsons;
	
	public JSONArray getImgNameJsons() {
		return imgNameJsons;
	}

	public void setImgNameJsons(JSONArray imgNameJsons) {
		this.imgNameJsons = imgNameJsons;
	}

}