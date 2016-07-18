package cn.com.minstone.eBusiness.model;


import com.jfinal.plugin.activerecord.Model;

public class EbFileImg extends Model<EbFileImg>{

	/**
	 * 文件图片上传数据库表
	 */
	private static final long serialVersionUID = 1L;
	
	public static final EbFileImg dao = new EbFileImg();
	
	/**
	 * 数据库列名称：IMAGE_NAME
	 * */
	private java.lang.String imageName;

	public java.lang.String getImageName() {
		this.imageName = this.get("IMAGE_NAME");
		return this.imageName;
	}

	public void setImageName(java.lang.String imageName) {
		this.imageName = imageName;
		this.set("IMAGE_NAME", this.imageName);
	}

	/**
	 * 数据库列名称：IMAGE_URL
	 * */
	private java.lang.String imageUrl;
	
	public java.lang.String getImageUrl() {
		this.imageUrl = this.get("IMAGE_URL");
		return imageUrl;
	}

	public void setImageUrl(java.lang.String imageUrl) {
		this.imageUrl = imageUrl;
		this.set("IMAGE_URL", this.imageUrl);
	}
	
	/**
	 * 数据库列名称：IMAGE_ID
	 * */
	private java.math.BigDecimal imageId;
	
	public java.math.BigDecimal getImageId() {
		this.imageId = this.get("IMAGE_ID");
		return imageId;
	}

	public void setImageId(java.math.BigDecimal imageId) {
		this.imageId = imageId;
		this.set("IMAGE_ID", this.imageId);
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

}
