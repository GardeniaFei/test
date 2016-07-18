package cn.com.minstone.eBusiness.ctrl;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import cn.com.minstone.eBusiness.AdminLoginInter;
import cn.com.minstone.eBusiness.dao.DepartDao;
import cn.com.minstone.eBusiness.model.EbBusiness;
import cn.com.minstone.eBusiness.model.EbDepart;
import cn.com.minstone.eBusiness.model.EbNotice;
import cn.com.minstone.eBusiness.model.EbNoticeEmail;
import cn.com.minstone.eBusiness.model.EbUserInfo;
import cn.com.minstone.eBusiness.service.BusinessService;
import cn.com.minstone.eBusiness.service.DepartService;
import cn.com.minstone.eBusiness.service.EmailService;
import cn.com.minstone.eBusiness.service.NoticeService;
import cn.com.minstone.eBusiness.service.inter.BussInterService;
import cn.com.minstone.eBusiness.service.inter.MsgInterService;
import cn.com.minstone.eBusiness.service.inter.UserInterService;
import cn.com.minstone.eBusiness.util.Config;
import cn.com.minstone.eBusiness.util.DBUtil;
import cn.com.minstone.eBusiness.util.GovNetUtil;
import cn.com.minstone.eBusiness.util.MCubeAppConfig;
import cn.com.minstone.eBusiness.util.PathUtil;
import cn.com.minstone.eBusiness.util.PushUtil;
import cn.com.minstone.eBusiness.util.StringUtil;
import cn.com.minstone.eBusiness.util.TimeUtil;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.DbPro;
import com.jfinal.plugin.activerecord.Page;

@Before(AdminLoginInter.class)
public class NoticeCtrl extends Controller {
      
	/**
	 * 通知公告管理页面显示
	 * @throws ParseException 
	 */
	public  void noticeManage() throws ParseException{
		int page = this.getParaToInt("page",1);
		String instrTime = getPara("filterTime");// 筛选时间
			// 判断筛选时间
		long start_time = 0;
			// 当前时间毫秒数
		long end_time = new Date().getTime();
		if (instrTime != null) {
				if (instrTime.equals("week")) {
					// 本周周一时间毫秒数
					start_time = TimeUtil.getCurrentMondayTime();
				} else if (instrTime.equals("month")) {
					start_time = TimeUtil.getMinMonthDateTime();
				} else if (instrTime.equals("year")) {
					Date nowDate = new Date();
					SimpleDateFormat df = new SimpleDateFormat("yyyy");
					String date = df.format(nowDate) + "-01-01";
					start_time = TimeUtil.getMinYearDateTime(date);
				} else {
					start_time = 0;
				}
		}
		System.out.print("初始毫秒数：" + start_time + "; 当前日期毫秒数:" + end_time);

		
		NoticeService noSer = new NoticeService();
		Page<EbNotice> noticePage = noSer.getAllNotice(page);
		if(start_time !=0){
			noticePage = noSer.getNoticeByTime(page,start_time,end_time);
		}
		
		List<EbNotice> notices = noticePage.getList();
		
		if(notices.size()>0){
			for(int i=0;i<notices.size();i++){
				EbNotice notice = notices.get(i);
				String newTime = TimeUtil.getTimeStyle(notice.getNotice_time(),"yyyy-MM-dd");
				notice.setNotice_time(newTime);
			}
		}
		if (instrTime != null) {
			this.setAttr("time", instrTime);
		}
		this.setAttr("notices", notices);
		this.setAttr("noticePage", noticePage);
		EmailService ser = new EmailService();
		EbNoticeEmail email = ser.getEmail();
		this.setAttr("email", email);
		render(PathUtil.getIndexPath("noticeManage.html"));
	}
	
	public void noticeAddView(){
		//获取所有的市级部门
		DepartService dservice = new DepartService();
		List<EbDepart> cityDptList = dservice.getCityDept();
		this.setAttr("cityDptList", cityDptList);
		
		//获取所有区级部门
		Map<String, Integer> map2 = new HashMap<String, Integer>();
		List<EbDepart> depts = GovNetUtil.getDeparts(1, "9999", map2, null);
		this.setAttr("deptList", depts);
		
		//获取所有企业
		BusinessService bservice = new BusinessService();
		List<EbBusiness> list = bservice.getAllBussInfoList();
		this.setAttr("bussInfos", list);
		
		render(PathUtil.getIndexPath("noticeAdd.html"));
	}
	
	/**
	 * 根据用户类型获取用户信息
	 */
	public void getUserByType(){
		int userType = this.getParaToInt("roleType");//用户角色，1区级，2市级，3企业
		String dpt_sj = this.getPara("deptId_sj");//市级用户所属部门
		String dpt_qj = this.getPara("deptId_qj");//区级用户所属部门
		String bussId = this.getPara("buss_id");//企业用户所属企业Id
		List<EbUserInfo> userInfos = new ArrayList<EbUserInfo>();//获取到的用户列表
		UserInterService userSer = new UserInterService(this);
		
		if(userType ==3 ){
			//用户为企业用户
			userInfos = userSer.findByBussid(bussId);
		}
		if(userType ==2 ){
			//用户为市级用户
			userInfos = userSer.getUserByD(dpt_sj);
		}
		if(userType ==1){
			//用户为区级用户
			Map<String, Integer> map = new HashMap<String, Integer>();
			userInfos = GovNetUtil.getEbUserInfos(dpt_qj, 1, "9999", map);	//根据单位流水号筛选用户
		}
		if(userInfos.size()>0){
			this.setAttr("flag",1);
			this.setAttr("userInfos", userInfos);
		}else{
			this.setAttr("flag",0);
			this.setAttr("userInfos", "");
		}
		renderJson();
	}
	
	/**
	 * 保存通知公告
	 * @throws ParseException 
	 */
	public void addNotice() throws ParseException{
		int userType = this.getParaToInt("roleType");//用户角色，1区级，2市级，3企业
		String userCode = this.getPara("userAccount");//用户账户
		String notice_type = this.getPara("notice_type");//1企业，2部门，3企业+部门
		String notice_title = this.getPara("notice_title");//通知公告主题
		String noticeEditor = getPara("veVenue.venue_brief");//取出编辑器中的html的内容
		
		//取出用户信息
		UserInterService userSer = new UserInterService(this);
		EbUserInfo user = userSer.findByCode(userCode);
		if(user == null){
			user = GovNetUtil.getUserByCode(userCode);
		}
		EbBusiness bussNew = null;//判断用户企业信息是否存在
		BussInterService bussSer = new BussInterService();
		EbNotice notice = new EbNotice();
		notice.set("NOTICE_ID","EB_NOTICE_seq.nextval");
		notice.set("NOTICE_CONTENT", noticeEditor);//编辑器取出内容
		notice.setIs_delet(new BigDecimal(1));
		notice.setNotice_user_code(userCode);
		if(user != null){
			notice.setNotice_user_name(user.getUserName());
			if(userType == 1 || userType ==2){
				notice.setNotice_depart_id(user.getDepartId());
				notice.setNotice_depart_name(user.getDepartName());
			}else{
				notice.setNotice_buss_name(user.getBusinessName());
				notice.setBusinessId(user.getBusinessId());
				
				bussNew = bussSer.getBussById(user.getBusinessId()+"");
			}
		}
		notice.setNotice_title(notice_title);//通知公告主题
		notice.setNotice_type(new BigDecimal(notice_type));//通告类型1企业，2部门，3企业+部门
		notice.setNotice_time(System.currentTimeMillis() + "");
		notice.setNotice_tatus(new BigDecimal(0));//0草稿状态，1发布状态
		
		notice.save();
		
		//消息推送部分
		 String strTime = System.currentTimeMillis() + "";
		 MsgInterService msgService = new MsgInterService();
		 if(user != null){
		 boolean flg = false;
		 if(notice_type.equals(1)){//消息状态 0部门不可以看到、1部门可以看到
			 flg = msgService.addNews(user, bussNew, "13", "新发布一则公告", notice_title, 
						strTime, "", "", "0", "" + "", "");
		 }else{
			 flg = msgService.addNews(user, bussNew, "13", "新发布一则公告", notice_title, 
					strTime, "", "", "1", "" + "", "");
		 }
			if(flg) {
				String content ="新发布一则公告";
				List<String> strAlias = new ArrayList<String>();
				
				if(notice_type.equals("1")){//只通知企业
					BusinessService bservice = new BusinessService();
					List<EbBusiness> list = bservice.getAllBussInfoList();
					if(list.size()>0){
						for(int i=0;i<list.size();i++){
							EbBusiness buss = list.get(i);
							String bussId = buss.getBusinessId()+"";
							strAlias.add(bussId);
						}
					}
				}else if(notice_type.equals("2")){//只通知部门
					Map<String, Integer> map2 = new HashMap<String, Integer>();
					List<EbDepart> depts = GovNetUtil.getDeparts(1, "9999", map2, null);
					if(depts.size()>0){
						for(int i=0;i<depts.size();i++){
							EbDepart dept = depts.get(i);
							String deptId = dept.getDepartId()+"";
							strAlias.add(deptId);
						}
					}
					//获取所有的市级部门
					DepartService dservice = new DepartService();
					List<EbDepart> cityDptList = dservice.getCityDept();
					if(cityDptList.size()>0){
						for(int i=0;i<cityDptList.size();i++){
							EbDepart dept = cityDptList.get(i);
							String deptId = dept.getDepartId()+"";
							strAlias.add(deptId);
						}
					}
				}else{
					//获取所有企业
					BusinessService bservice = new BusinessService();
					List<EbBusiness> list = bservice.getAllBussInfoList();
					if(list.size()>0){
						for(int i=0;i<list.size();i++){
							EbBusiness buss = list.get(i);
							String bussId = buss.getBusinessId()+"";
							strAlias.add(bussId);
						}
					}
					//获取所有区级部门
					Map<String, Integer> map2 = new HashMap<String, Integer>();
					List<EbDepart> depts = GovNetUtil.getDeparts(1, "9999", map2, null);
					if(depts.size()>0){
						for(int i=0;i<depts.size();i++){
							EbDepart dept = depts.get(i);
							String deptId = dept.getDepartId()+"";
							strAlias.add(deptId);
						}
					}
					//获取所有的市级部门
					DepartService dservice = new DepartService();
					List<EbDepart> cityDptList = dservice.getCityDept();
					if(cityDptList.size()>0){
						for(int i=0;i<cityDptList.size();i++){
							EbDepart dept = cityDptList.get(i);
							String deptId = dept.getDepartId()+"";
							strAlias.add(deptId);
						}
					}
				}
				Hashtable<String, String> map = new Hashtable<String, String>();
				map.put("newsType", "13");
				map.put("content", content);
				map.put("status", "0");
				map.put("businessId", getCs(user.getBusinessId() + ""));
				PushUtil.pushNotifyToTagert(strAlias, StringUtil.getJson(map));
				}
			}
		redirect("/view/noticeManage");
	}
	
	/**
	 * 检查字符串是否为空，为空则传递“”
	 * @param str
	 * @return
	 */
	private String getCs(String str){
		if(str != null){
			return str;
		}
		return "";
	}
	/**
	 * 跳转到通告详情页面
	 * @throws IOException 
	 */
	public void noticeInfo() throws IOException{
		String noticeId = this.getPara("noticeId");
		
		NoticeService noSer = new NoticeService();
		EbNotice notice = noSer.getNoticeById(noticeId);
		
		if(notice != null){
				this.setAttr("notice", notice);
		}
		render(PathUtil.getIndexPath("noticeInfo.html"));
	}
	
	/**
	 * 通知公告编辑页面显示
	 */
	public void noticeEdit(){
		String noticeId = this.getPara("noticeId");
		
		NoticeService noSer = new NoticeService();
		EbNotice notice = noSer.getNoticeById(noticeId);
		
		this.setAttr("notice", notice);
		render(PathUtil.getIndexPath("noticeEdit.html"));
	}
	
	/**
	 * 保存编辑的通告
	 * @throws ParseException 
	 */
	public void noticeSave() throws ParseException{
		String noticeId = this.getPara("noticeId");
		String notice_type = this.getPara("notice_type");//1企业，2部门，3企业+部门
		String notice_title = this.getPara("notice_title");//通知公告主题
		String noticeEditor = getPara("veVenue.venue_brief");//取出编辑器中的html的内容
		
		NoticeService noSer = new NoticeService();
		EbNotice notice = noSer.getNoticeById(noticeId);
		boolean result = false;
		if(notice != null){
			notice.setNotice_title(notice_title);//通知公告主题
			notice.setNotice_type(new BigDecimal(notice_type));//通告类型1企业，2部门，3企业+部门
			notice.setNotice_time(System.currentTimeMillis() + "");
			notice.set("NOTICE_CONTENT", noticeEditor);//编辑器取出内容
			result = notice.update();
		}
		if(result){
			noticeManage();
		}else{
			redirect("/view/noticeEdit?noticeId="+noticeId);
		}
	}
	
	/**
	 * 删除通告
	 * @throws ParseException
	 */
	public void deleteNotice() throws ParseException{
		String noticeId = this.getPara("noticeId");
		
		NoticeService noSer = new NoticeService();
		EbNotice notice = noSer.getNoticeById(noticeId);
		boolean result = false;
		if(notice != null){
			notice.setIs_delet(new BigDecimal(0));//通知公告主题1有效，0无效
			result = notice.update();
		}
		this.setAttr("result", result);
		renderJson();
	}
	
	/**
	 * 保存邮箱
	 */
	public void editEmail(){
		String email_name = this.getPara("notice_email");
		boolean result = false;
		boolean flag = true;
		EmailService ser = new EmailService();
		EbNoticeEmail email = ser.getEmail();
		if(email != null){
			email.setIs_delet(new BigDecimal(0));//使邮箱无效
			flag = email.update();
		}
		if(flag){
		 email = new EbNoticeEmail();
		 email.set("email_id", "EB_EMAIL_seq.nextval");
		 email.setEmail_name(email_name);
		 email.setIs_delet(new BigDecimal(1));
		 email.setCreat_time(System.currentTimeMillis() + "");
		 result = email.save();
		}
		this.setAttr("result", result);
		renderJson();
	}
	
	/**
	 * 设置发布状态
	 */
	public void publicNotice(){
		Long notice_id = getParaToLong("notice_id");
		int statu = this.getParaToInt("statu");
		/*NoticeService noSer = new NoticeService();
		EbNotice notice = noSer.getNoticeById(notice_id);	
		boolean result = false;
		if(notice != null){
			notice.setNotice_tatus(new BigDecimal(statu));//0未发布，1已发布
			try {
				result = notice.update();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}*/
		
		int rs=DbPro.use("base").update("update eb_notice set notice_tatus=? where notice_id=?", new Object[]{new BigDecimal(statu),notice_id});
		
		this.setAttr("result", rs>0);
		renderJson();
	}
}
