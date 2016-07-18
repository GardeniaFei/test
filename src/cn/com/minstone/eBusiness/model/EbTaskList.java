package cn.com.minstone.eBusiness.model;

import com.jfinal.plugin.activerecord.Model;

/**
 * 数据库表名称：EB_TASK_LIST
 * */
public class EbTaskList extends Model<EbTaskList> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7169356306535368501L;

	public static final EbTaskList dao = new EbTaskList();
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
	 * 数据库列名称：T_ID自增主键
	 * */
	private java.math.BigDecimal t_id;

	public java.math.BigDecimal getT_id() {
		this.t_id = this.get("T_ID");
		return this.t_id;
	}

	public void setT_id(java.math.BigDecimal t_id) {
		this.t_id = t_id;
		this.set("T_ID", this.t_id);
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
	 * 数据库列表名称：REFRESH_TIME
	 */
	private String refreshTime;
	
	public String getRefreshTime() {
		this.refreshTime = this.get("REFRESH_TIME");
		
		return refreshTime;
	}

	public void setRefreshTime(String refreshTime) {
		this.refreshTime = refreshTime;
		this.set("REFRESH_TIME", this.refreshTime);
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
	 * 数据库列名称：IS_DELET是否被删除
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