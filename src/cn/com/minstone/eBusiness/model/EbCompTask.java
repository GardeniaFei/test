package cn.com.minstone.eBusiness.model;

import com.jfinal.plugin.activerecord.Model;

public class EbCompTask extends Model<EbCompTask>{

	/**
	 * 展示已完成事项模型
	 */
	private static final long serialVersionUID = 597291198532843207L;

	private String bussId;
	
	private String bussName;//企业名称
	
	private String comp_time;//完成事项时间
	
	private String item_name;//事项名称
	
	private String item_id;
	
	private String distr_id;//分发id
	
	private String task_id;//任务id
	
	private String trans_status;//事项状态，3完成

	public String getBussId() {
		return bussId;
	}

	public void setBussId(String bussId) {
		this.bussId = bussId;
	}

	public String getBussName() {
		return bussName;
	}

	public void setBussName(String bussName) {
		this.bussName = bussName;
	}

	public String getComp_time() {
		return comp_time;
	}

	public void setComp_time(String comp_time) {
		this.comp_time = comp_time;
	}

	public String getItem_name() {
		return item_name;
	}

	public void setItem_name(String item_name) {
		this.item_name = item_name;
	}

	public String getItem_id() {
		return item_id;
	}

	public void setItem_id(String item_id) {
		this.item_id = item_id;
	}

	public String getDistr_id() {
		return distr_id;
	}

	public void setDistr_id(String distr_id) {
		this.distr_id = distr_id;
	}

	public String getTask_id() {
		return task_id;
	}

	public void setTask_id(String task_id) {
		this.task_id = task_id;
	}

	public String getTrans_status() {
		return trans_status;
	}

	public void setTrans_status(String trans_status) {
		this.trans_status = trans_status;
	}
	
}
