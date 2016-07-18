package cn.com.minstone.eBusiness.service.inter;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.DbPro;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import cn.com.minstone.eBusiness.dao.DepartDao;
import cn.com.minstone.eBusiness.model.EbDepart;
import cn.com.minstone.eBusiness.model.EbUserInfo;
import cn.com.minstone.eBusiness.service.BaseService;
import cn.com.minstone.eBusiness.util.GovNetUtil;
import cn.com.minstone.eBusiness.util.NullUtil;
import cn.com.minstone.eBusiness.util.StringUtil;
import cn.com.minstone.eBusiness.util.TimeUtil;

public class DeptInterService extends BaseService {

	private DepartDao dao;
	private Controller ctrl;
	
	public DeptInterService() {
		this.dao = new DepartDao();
	}
	
	/**
	 * 获取所有的部门
	 * @return list
	 */
	public List<EbDepart> getAllDeptList() {
		return dao.findAllDeptList();
	}
	
	/**
	 * 获取所有的部门
	 * @param page
	 * @return Page
	 */
	public Page<EbDepart> getAllDept(int page) {
		return dao.findAllDept(page);
	}
	
	/**
	 * 根据部门id获取部门信息
	 * @param departId
	 * @return
	 */
	public EbDepart getDeptById(String departId) {
		if(departId == null || departId.equals("") || "null".equalsIgnoreCase(departId)) {
			return null;
		}
		EbDepart dept = dao.findById(departId);
		if(dept == null) {
			dept = GovNetUtil.getDeptInfo(departId);
		}
		
		return dept;
	}
	
	/**
	 * 根据部门id获取本地部门信息
	 * @param departId
	 * @return
	 */
	public EbDepart getLocDeptById(String departId) {
		if(departId == null || departId.equals("") || "null".equalsIgnoreCase(departId)) {
			return null;
		}
		EbDepart dept = dao.findById(departId);
		
		return dept;
	}
	
	/**
	 * 设置部门的签收人
	 */
	public boolean setSignUser(String departId,String userId){
		
		return dao.setUserById(departId, userId);
	}
	
	/**
	 * 根据部门名称获取市级单位
	 * @param departName
	 * @return
	 */
	public EbDepart getDeptByName(String departName) {
		return dao.findByDeptName(NullUtil.ifNull(departName, ""));
	}
	
	public boolean addDepartInfo(String departName, String workTel, String workAddress) {
		EbDepart deptInfo = new EbDepart();
		Record rc = null;
		try {
			rc = Db.findFirst("select dept_seq.nextval depart_id from dual");
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (rc == null) {
			return false;
		}
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHH");
		String data = df.format(new Date());
		String locDeptId = data + rc.get("DEPART_ID");
		//BigDecimal seq = Db.queryBigDecimal("select dept_seq.nextval from dual")
		deptInfo.setDepartId(new BigDecimal(locDeptId));
		
//		deptInfo.set("depart_id", "dept_seq.nextval");
		if (!NullUtil.isEmpty(departName)) {
			deptInfo.setDepartName(departName);
		}
		
		if (!NullUtil.isEmpty(workTel)) {
			deptInfo.setWorkTel(workTel);
		}
		
		if (!NullUtil.isEmpty(workAddress)) {
			deptInfo.setWorkAddress(workAddress);
		}
		
		deptInfo.setDepartType(new BigDecimal(1));
		deptInfo.setRefreshTime(System.currentTimeMillis() + "");
		deptInfo.setUserCode("");
		deptInfo.setIsDelet(new BigDecimal(1));//是否被禁用1否
		
		boolean flag = false;
		try {
			flag = deptInfo.save();
		} catch (Exception e) {
			e.printStackTrace();
			flag = deptInfo.save();
		}
		return flag;
	}
	
	/**
	 * 获取所有的市级部门单位
	 * @param departLevel
	 * @param departName
	 * @return
	 */
	public List<EbDepart> getDeptsByType(String departLevel, String departName) {
		List<EbDepart> ds = dao.findDeptsByType(departLevel, departName);
		if(ds.size()>0){
			for(int i=0;i < ds.size();i++){
				EbDepart dept = ds.get(i);
				if (dept != null) {
					dept.setRefreshTime(TimeUtil.changeTimeStyle(dept.getRefreshTime()));
				}
			}
		}
		return ds;
	}
	
	/**
	 * 对市级单位进行分页操作
	 * @param departLevel
	 * @param departId
	 * @param pageIndex
	 * @return
	 */
	public Page<EbDepart> pageDeptsByType(String departLevel, String departId, String pageIndex) {
		return dao.findDeptsPageByType(departLevel, 
				departId, StringUtil.parseIndex(pageIndex));
	}
	
	/**
	 * 设置单位首席
	 * @param departId
	 * @param departName
	 * @param departType
	 * @param userCode
	 * @param workTel
	 * @param workAddress
	 * @return
	 */
	public boolean setDeptChief(String departId, String departName, String departType, String userCode, String workTel, String workAddress) {
		EbDepart deptInfo = new EbDepart();
//		EbDepart dpt = new EbDepart();
//		dpt = dpt.findFirst("select dept_seq.nextval depart_id from dual");
		deptInfo.setDepartId(new BigDecimal(departId));
		if (!NullUtil.isEmpty(departName)) {
			deptInfo.setDepartName(departName);
		}
		boolean userFlag = false;
		if (!NullUtil.isEmpty(userCode)) {
			deptInfo.setUserCode(userCode);
			UserInterService userSer = new UserInterService(ctrl);
			EbUserInfo user = userSer.getUserByCD(userCode, departId);
			if(user != null){
				//（1VIP服务专员管理员、2VIP服务专员权限 、3单位首席权限、4部门跟办人权限、5市级服务专员权限、
				//6 VIP服务专员管理员+单位首席权限、7 VIP服务专员+单位首席权限）
				int auth = user.getAuthority().intValue();
				if(auth == 1){
					user.setAuthority(new BigDecimal(6));
				}else if(auth == 2){
					user.setAuthority(new BigDecimal(7));
				}else{
					user.setAuthority(new BigDecimal(3));
				}
				if(user.update()){
					userFlag = true;
				}
			}else{
				//判断是否用户是审批中的用户
				EbUserInfo user2 = userSer.getUserByCDQ(userCode, departId);
				if(user2 != null){
					int auth = user2.getAuthority().intValue();
					if(auth == 1){
						user2.setAuthority(new BigDecimal(6));
					}else if(auth == 2){
						user2.setAuthority(new BigDecimal(7));
					}else{
						user2.setAuthority(new BigDecimal(3));
					}
					if(user2.update()){
						userFlag = true;
					}
				}
			}
			
		}
		
		if (!NullUtil.isEmpty(workTel)) {
			deptInfo.setWorkTel(workTel);
		}
		
		if (!NullUtil.isEmpty(workAddress)) {
			deptInfo.setWorkAddress(workAddress);
		}
		
		deptInfo.setDepartType(new BigDecimal(departType));
		deptInfo.setRefreshTime(System.currentTimeMillis() + "");
		
		boolean flag = false;
		if(userFlag){//用户信息保存成功
			try {
				flag = deptInfo.save();
			} catch (Exception e) {
				e.printStackTrace();
				flag = deptInfo.save();
			}
		}
		return flag;
	}
	
	/**
	 * 修改单位首席
	 * @param deptInfo
	 * @return
	 */
	public boolean changeDeptChief(EbDepart deptInfo) {

		boolean flag = false;
		boolean userFlag = false;
		try {
			if (deptInfo != null) {
				deptInfo.setRefreshTime(System.currentTimeMillis() + "");
				String userCode = deptInfo.getUserCode();
				String departId = deptInfo.getDepartId()+"";
				
				UserInterService userSer = new UserInterService(ctrl);
				EbUserInfo user = userSer.getUserByCD(userCode, departId);
				if(user != null){
					//（1VIP服务专员管理员、2VIP服务专员权限 、3单位首席权限、4部门跟办人权限、5市级服务专员权限、
					//6 VIP服务专员管理员+单位首席权限、7 VIP服务专员+单位首席权限）
					int auth = user.getAuthority().intValue();
					if(auth == 1){
						user.setAuthority(new BigDecimal(6));
					}else if(auth == 2){
						user.setAuthority(new BigDecimal(7));
					}else{
						user.setAuthority(new BigDecimal(3));
					}
					if(user.update()){
						userFlag = true;
					}
				}
				//设置权限
				flag = deptInfo.update()&& userFlag;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}
}
