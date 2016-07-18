package cn.com.minstone.eBusiness.model;

import com.jfinal.plugin.activerecord.Model;

/**
 * 数据库表名称：EB_ITEM
 * */
public class EbItem extends Model<EbItem> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1909520364566548580L;

	public static final EbItem dao = new EbItem();
	
	/**
	 * EbDepart模型
	 * */
	private EbDepart dept;
	
	public EbDepart getDept() {
		return dept;
	}

	public void setDept(EbDepart dept) {
		this.dept = dept;
	}

	/**
	 * 数据库列名称：FLOW_ANNEX
	 * */
	private java.lang.String flowAnnex;

	public java.lang.String getFlowAnnex() {
		this.flowAnnex = this.get("FLOW_ANNEX");
		return this.flowAnnex;
	}

	public void setFlowAnnex(java.lang.String flowAnnex) {
		this.flowAnnex = flowAnnex;
		this.set("FLOW_ANNEX", this.flowAnnex);
	}

	/**
	 * 数据库列名称：DEPART_NAME
	 * */
	private java.lang.String departName;

	public java.lang.String getDepartName() {
		this.departName = this.get("DEPART_NAME");
		return this.departName;
	}

	public void setDepartName(java.lang.String departName) {
		this.departName = departName;
		this.set("DEPART_NAME", this.departName);
	}

	/**
	 * 数据库列名称：MATERIAL_ANNEX
	 * */
	private java.lang.String materialAnnex;

	public java.lang.String getMaterialAnnex() {
		this.materialAnnex = this.get("MATERIAL_ANNEX");
		return this.materialAnnex;
	}

	public void setMaterialAnnex(java.lang.String materialAnnex) {
		this.materialAnnex = materialAnnex;
		this.set("MATERIAL_ANNEX", this.materialAnnex);
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
	 * 数据库列名称：T_ID自增主键对应事项配置列表，暂时字段
	 * */
	private java.math.BigDecimal t_id;

	public java.math.BigDecimal getT_id() {
		return t_id;
	}

	public void setT_id(java.math.BigDecimal t_id) {
		this.t_id = t_id;
	}

	/**
	 * 数据库列名称：GIST_LAW
	 * */
	private java.lang.String gistLaw;

	public java.lang.String getGistLaw() {
		this.gistLaw = this.get("GIST_LAW");
		return this.gistLaw;
	}

	public void setGistLaw(java.lang.String gistLaw) {
		this.gistLaw = gistLaw;
		this.set("GIST_LAW", this.gistLaw);
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
	 * 数据库列名称：TRANSACTION_WINDOW
	 * */
	private java.lang.String transactionWindow;

	public java.lang.String getTransactionWindow() {
		this.transactionWindow = this.get("TRANSACTION_WINDOW");
		return this.transactionWindow;
	}

	public void setTransactionWindow(java.lang.String transactionWindow) {
		this.transactionWindow = transactionWindow;
		this.set("TRANSACTION_WINDOW", this.transactionWindow);
	}

	/**
	 * 数据库列名称：ITEM_QUESTIONS
	 * */
	private java.lang.String itemQuestions;

	public java.lang.String getItemQuestions() {
		this.itemQuestions = this.get("ITEM_QUESTIONS");
		return this.itemQuestions;
	}

	public void setItemQuestions(java.lang.String itemQuestions) {
		this.itemQuestions = itemQuestions;
		this.set("ITEM_QUESTIONS", this.itemQuestions);
	}

	/**
	 * 数据库列名称：ITEM_NAME
	 * */
	private java.lang.String itemName;

	public java.lang.String getItemName() {
		this.itemName = this.get("ITEM_NAME");
		return this.itemName;
	}

	public void setItemName(java.lang.String itemName) {
		this.itemName = itemName;
		this.set("ITEM_NAME", this.itemName);
	}

	/**
	 * 数据库列名称：NEED_CONDITION
	 * */
	private java.lang.String needCondition;

	public java.lang.String getNeedCondition() {
		this.needCondition = this.get("NEED_CONDITION");
		return this.needCondition;
	}

	public void setNeedCondition(java.lang.String needCondition) {
		this.needCondition = needCondition;
		this.set("NEED_CONDITION", this.needCondition);
	}

	/**
	 * 数据库列名称：TIME_LIMIT
	 * */
	private java.math.BigDecimal timeLimit;

	public java.math.BigDecimal getTimeLimit() {
		this.timeLimit = this.get("TIME_LIMIT");
		return this.timeLimit;
	}

	public void setTimeLimit(java.math.BigDecimal timeLimit) {
		this.timeLimit = timeLimit;
		this.set("TIME_LIMIT", this.timeLimit);
	}

	/**
	 * 数据库列名称：TRANSACTION_OBJECT
	 * */
	private java.lang.String transactionObject;

	public java.lang.String getTransactionObject() {
		this.transactionObject = this.get("TRANSACTION_OBJECT");
		return this.transactionObject;
	}

	public void setTransactionObject(java.lang.String transactionObject) {
		this.transactionObject = transactionObject;
		this.set("TRANSACTION_OBJECT", this.transactionObject);
	}

	/**
	 * 数据库列名称：ITEM_CHARGE
	 * */
	private java.lang.String itemCharge;

	public java.lang.String getItemCharge() {
		this.itemCharge = this.get("ITEM_CHARGE");
		return this.itemCharge;
	}

	public void setItemCharge(java.lang.String itemCharge) {
		this.itemCharge = itemCharge;
		this.set("ITEM_CHARGE", this.itemCharge);
	}

	/**
	 * 数据库列名称：ITEM_FLOW
	 * */
	private java.lang.String itemFlow;

	public java.lang.String getItemFlow() {
		this.itemFlow = this.get("ITEM_FLOW");
		return this.itemFlow;
	}

	public void setItemFlow(java.lang.String itemFlow) {
		this.itemFlow = itemFlow;
		this.set("ITEM_FLOW", this.itemFlow);
	}

	/**
	 * 数据库列名称：NEED_MATERIAL
	 * */
	private java.lang.String needMaterial;

	public java.lang.String getNeedMaterial() {
		this.needMaterial = this.get("NEED_MATERIAL");
		return this.needMaterial;
	}

	public void setNeedMaterial(java.lang.String needMaterial) {
		this.needMaterial = needMaterial;
		this.set("NEED_MATERIAL", this.needMaterial);
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
}