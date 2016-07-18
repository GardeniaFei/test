package cn.com.minstone.eBusiness.model;

import com.jfinal.plugin.activerecord.Model;

public class BaseAdmin  extends Model<BaseAdmin>{
	/**
	 * 镇街服务中心配置信息管理员认证
	 */
	private static final long serialVersionUID = -9197483061417964378L;
	public final  static BaseAdmin dao = new BaseAdmin();
	
	private java.lang.String C_USER_CODE; //用户名
	
	public java.lang.String getC_USER_CODE() {
		this.C_USER_CODE = this.get("C_USER_CODE");
		return this.C_USER_CODE;
	}

	public void setC_USER_CODE(java.lang.String C_USER_CODE) {
		this.C_USER_CODE = C_USER_CODE;
		this.set("C_USER_CODE", this.C_USER_CODE);
	}


	private java.math.BigDecimal I_USER_ADM;//是否是管理员，1为是

	public java.math.BigDecimal getI_USER_ADM() {
		this.I_USER_ADM = this.get("I_USER_ADM");
		return this.I_USER_ADM;
	}

	public void setI_USER_ADM(java.math.BigDecimal i_USER_ADM) {
		this.I_USER_ADM = i_USER_ADM;
		this.set("I_USER_ADM", this.I_USER_ADM);
	}
	
}
