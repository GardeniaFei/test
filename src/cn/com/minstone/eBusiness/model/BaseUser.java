package cn.com.minstone.eBusiness.model;



import com.jfinal.plugin.activerecord.Model;

public class BaseUser extends Model<BaseUser> {

	/**
	 * 管理员登录信息对应表BASE_USER
	 */
	private static final long serialVersionUID = 1L;
	public final  static BaseUser dao = new BaseUser();
	
	private java.lang.String USER_CODE; //用户名
	
	public java.lang.String getUSER_CODE() {
		this.USER_CODE = this.get("USER_CODE");
		return this.USER_CODE;
	}

	public void setUSER_CODE(java.lang.String USER_CODE) {
		this.USER_CODE = USER_CODE;
		this.set("USER_CODE", this.USER_CODE);
	}

	private java.lang.String USER_PWD; //密码
	
	public java.lang.String getUSER_PWD() {
		this.USER_PWD = this.get("USER_PWD");
		return this.USER_PWD;
	}

	public void setUSER_PWD(java.lang.String USER_PWD) {
		this.USER_PWD = USER_PWD;
		this.set("USER_PWD", this.USER_PWD);
	}
}
