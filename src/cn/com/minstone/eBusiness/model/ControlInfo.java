package cn.com.minstone.eBusiness.model;

/**
 * 文件描述：审批办件信息
 * 
 * @author gongb
 * @since 2016年2月23日 
 */
public class ControlInfo {

	private String controlSeq;//办件流水号
	private String beginDate;//开始日期
	private String approveSeq;//事项流水号
	private String approveName;//事项名称
	private String unitSeq;//单位流水号
	private String unitName;//单位名称
	private String unitUniteSeq;//合并后单位流水号
	private String unitUniteName;//合并后单位名称
	private String approveStatus;//办件状态
	private String approveResult;//办件结果
	private String custName;//办事主体名称
	private String custContactPerson;//经办人名称
	private String custType;//办事主体类型
	private String approveDealWay;//办理方式
	private String legalLimit;//法定办理时限
	private String promiseLimit;//承诺办理时限
	
	public String getCustName() {
		return custName;
	}

	public void setCustName(String custName) {
		this.custName = custName;
	}

	public String getCustContactPerson() {
		return custContactPerson;
	}

	public void setCustContactPerson(String custContactPerson) {
		this.custContactPerson = custContactPerson;
	}

	public String getCustType() {
		return custType;
	}

	public void setCustType(String custType) {
		this.custType = custType;
	}

	public String getApproveDealWay() {
		return approveDealWay;
	}

	public void setApproveDealWay(String approveDealWay) {
		this.approveDealWay = approveDealWay;
	}

	public String getLegalLimit() {
		return legalLimit;
	}

	public void setLegalLimit(String legalLimit) {
		this.legalLimit = legalLimit;
	}

	public String getPromiseLimit() {
		return promiseLimit;
	}

	public void setPromiseLimit(String promiseLimit) {
		this.promiseLimit = promiseLimit;
	}
	
	public String getControlSeq() {
		return controlSeq;
	}
	
	public void setControlSeq(String controlSeq) {
		this.controlSeq = controlSeq;
	}
	
	public String getBeginDate() {
		return beginDate;
	}
	
	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}
	
	public String getApproveSeq() {
		return approveSeq;
	}
	
	public void setApproveSeq(String approveSeq) {
		this.approveSeq = approveSeq;
	}
	
	public String getApproveName() {
		return approveName;
	}
	
	public void setApproveName(String approveName) {
		this.approveName = approveName;
	}
	
	public String getUnitSeq() {
		return unitSeq;
	}
	
	public void setUnitSeq(String unitSeq) {
		this.unitSeq = unitSeq;
	}
	
	public String getUnitName() {
		return unitName;
	}
	
	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
	
	public String getUnitUniteSeq() {
		return unitUniteSeq;
	}
	
	public void setUnitUniteSeq(String unitUniteSeq) {
		this.unitUniteSeq = unitUniteSeq;
	}
	
	public String getUnitUniteName() {
		return unitUniteName;
	}
	
	public void setUnitUniteName(String unitUniteName) {
		this.unitUniteName = unitUniteName;
	}
	
	public String getApproveStatus() {
		return approveStatus;
	}
	
	public void setApproveStatus(String approveStatus) {
		this.approveStatus = approveStatus;
	}
	
	public String getApproveResult() {
		return approveResult;
	}
	
	public void setApproveResult(String approveResult) {
		this.approveResult = approveResult;
	}
}
