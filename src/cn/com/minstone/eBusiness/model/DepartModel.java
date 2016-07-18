package cn.com.minstone.eBusiness.model;

import com.jfinal.plugin.activerecord.Model;

public class DepartModel extends Model<DepartModel> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7061370153022161980L;
	
	private String departId;
	public String getDepartId() {
		return departId;
	}
	public void setDepartId(String departId) {
		this.departId = departId;
	}
	public String getDepartName() {
		return departName;
	}
	public void setDepartName(String departName) {
		this.departName = departName;
	}
	private String departName;

}
