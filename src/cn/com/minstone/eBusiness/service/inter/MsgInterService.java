package cn.com.minstone.eBusiness.service.inter;

import java.math.BigDecimal;
import java.util.List;

import com.jfinal.plugin.activerecord.Page;

import cn.com.minstone.eBusiness.dao.inter.MessageInterDao;
import cn.com.minstone.eBusiness.model.EbBusiness;
import cn.com.minstone.eBusiness.model.EbMessage;
import cn.com.minstone.eBusiness.model.EbNews;
import cn.com.minstone.eBusiness.model.EbUserInfo;
import cn.com.minstone.eBusiness.service.BaseService;
import cn.com.minstone.eBusiness.util.MD5Util;

public class MsgInterService extends BaseService {
	
	private MessageInterDao dao;
	
	public MsgInterService() {
		this.dao = new MessageInterDao();
	}
	
	/**
	 * 获取所有的留言
	 * @return list
	 */
	public List<EbMessage> getAllMessageList() {
		return dao.findAllMessageList();
	}
	
	/**
	 * 根据企业id,获取所有的用户userCode的List
	 * @param bussId
	 * @return list<String>
	 */
	public List<String> getUCBybussId(String bussId){
		return dao.findUCBybussId(bussId);
	}
	
	/**
	 * 根据企业id,获取所有的用户userID的List
	 * @param bussId
	 * @return list<String>
	 */
	public List<String> getUserIdBybussId(String bussId){
		return dao.findUserIdBybussId(bussId);
	}
	/**
	 * 获取所有的留言
	 * @param page 当前页
	 * @return Page
	 */
	public Page<EbMessage> getAllMessage(int page) {
		return dao.findAllMessage(page);
	}
	
	/**
	 * 获取相同企业id的留言
	 * @param businessId 企业Id
	 * @return Page
	 */
	public List<EbMessage> getMessageByBId(String businessId) {
		return dao.findMessageByBId(businessId);
	}
	
	/**
	 * 获取userId相同所有的留言
	 * @param page 当前页
	 * @param userId 用户id
	 * @return Page
	 */
	public Page<EbMessage> getMessageByUId(int page,String userId) {
		return dao.findMessageByUId(page,userId);
	}
	
	/**
	 * 获取userId相同所有的留言,领导用，去重企业id
	 * @param userId 用户id
	 * @return list
	 */
	public List<EbMessage> getMessageByUIdLead(String userId) {
		return dao.findMessageByUIdLead(userId);
	}
	
	/**
	 * 获取userId相同所有的留言
	 * @param page 当前页
	 * @param userId 用户id
	 * @param businessId 企业Id
	 * @return Page
	 */
	public Page<EbMessage> getMessageByUIdBId(int page,String userId,String businessId,long start_time,long end_time) {
		return dao.findMessageByUIdBId(page,userId,businessId,start_time,end_time);
	}
	
	/**
	 * 获取userId和bussId相同所有的留言列表
	 * @param page
	 * @param userId
	 * @param businessId
	 * @param start_time
	 * @param end_time
	 * @return
	 */
	public List<EbMessage> getMessageByUIdBIdList(String userId,String businessId,long start_time,long end_time) {
		return dao.findMessageByUIdBIdList(userId,businessId,start_time,end_time);
	}
	/**
	 * 获取所有的对应留言id的回复
	 * @return list
	 */
	public List<EbMessage> getAllReplyList(String messageId) {
		return dao.findAllReplyList(messageId);
	}
	
	/**
	 * 获取所有的对应留言id的回复
	 * @param page
	 * @param messageId
	 * @return Page
	 */
	public Page<EbMessage> getAllReply(String messageId,int page) {
		return dao.findAllReply(messageId,page);
	}
	
	/**
	 * 获取所有的对应留言id的回复
	 * @param page
	 * @param messageId
	 * @param bussId 企业Id
	 * @return Page
	 */
	public Page<EbMessage> getReplyByMIdBId(String messageId,String bussId,int page) {
		return dao.findReplyByMIdBId(messageId,bussId,page);
	}
	
	/**
	 * 获取所有的对应留言id的回复
	 * @param messageId
	 * @param bussId 企业Id
	 * @return List
	 */
	public List<EbMessage> getReplyByMIdBIdList(String messageId,String bussId) {
		return dao.findReplyByMIdBIdList(messageId,bussId);
	}
	
	/**
	 * 获得所有的反馈消息
	 * @return list
	 */
	public List<EbNews> getAllNewsList() {
		return dao.findAllNewsList();
	}
	
	/**
	 * 根据企业Id筛选出不同部门不同事项的消息列表
	 * @param businessid
	 * @param itemId
	 * @param deptId
	 */
	public List<EbNews> getNewsByBIdId(String businessId ,String itemId ,String deptId){
		return dao.findNewsByBIDId(businessId, deptId, itemId);
	}
	
	/**
	 * 根据企业Id筛选出反馈消息
	 * @param businessid 企业id
	 * @param deptId 部门id
	 * @param itemId 事项id
	 */
	public List<EbNews> getNewsToFB(String businessId,String deptId,String itemId){
		return dao.findNewsToFB(businessId,deptId,itemId);
	}
	
	/**
	 * 获得所有的反馈消息
	 * @param page
	 * @return Page
	 */
	public Page<EbNews> getAllNews(int page) {
		return dao.findAllNews(page);
	}
	
	/**
	 * 根据企业名称获取反馈消息
	 * @param bussName
	 * @return list
	 */
	public List<EbNews> getNewsByBussNameList(String bussName) {
		if(bussName == null || "".equals(bussName) || "null".equalsIgnoreCase(bussName)) {
			return getAllNewsList();
		}
		return dao.findNewsByBussNameList(bussName);
	}
	
	/**
	 * 根据企业名称获取反馈消息
	 * @param bussName
	 * @param page
	 * @return Page
	 */
	public Page<EbNews> getNewsByBussName(String bussName, int page) {
		if(bussName == null || "".equals(bussName) || "null".equalsIgnoreCase(bussName)) {
			return getAllNews(page);
		}
		return dao.findNewsByBussName(bussName,page);
	}
	
	/**
	 * 根据用户名或者企业名筛选留言信息
	 * @param userName 用户名称
	 * @param busName 企业名称
	 * @return Page
	 */
	public Page<EbMessage> getMesByUserBus(String userName ,String busName,int page){
	    return dao.findMesByUserBus(userName,busName,page);
	}
	
	/**
	 * 根据用户名或者企业名筛选留言信息
	 * @param userName 用户名称
	 * @param busName 企业名称
	 * @return list
	 */
	public List<EbMessage> getMesByUserBusList(String userName ,String busName){
	    return dao.findMesByUserBusList(userName,busName);
	}
	
	/**
	 * 根据留言id查找留言
	 * @param messageId
	 * @return
	 */
	public EbMessage getMsgByMsgId(String messageId) {
		if(messageId == null || "".equals(messageId) || "null".equalsIgnoreCase(messageId)) {
			return null;
		}
		return dao.findMsgByMsgId(messageId);
	}
	
	/**
	 * 进行留言回复
	 * @param messageId
	 * @param businessId
	 * @param content
	 * @param userId
	 * @return
	 */
	public boolean replyMsg(String messageId, String businessId, String content, String userId, String departId) {
		EbMessage msg = new EbMessage();
		
		msg.set("message_id", "message_seq.nextval");
		msg.set("param_id", messageId);
		msg.setMessageContent(content);
		msg.setMessageTime(System.currentTimeMillis() + "");
		msg.set("status", 0);	//（ 0 回复、 1 留言）
		msg.set("user_id", userId);	//留言回复人
		msg.set("business_id", businessId);
		EbUserInfo userInfo = new UserInterService(null).findById(userId);
		
		if(userInfo == null) {
			msg.set("depart_id", null);
			msg.setUserName(null);
		}else {
//			msg.setDepartId(userInfo.getDepartId());
			msg.setUserName(userInfo.getUserName());
		}

/**********************2015-11-30 feini********************************/
		EbUserInfo uInfo = new UserInterService(null).findById(userId);
		if(uInfo != null){
			String role_type = userInfo.getRoleType()+"";//用户角色(1 办事人员、2 领导、 3 企业)
			if(role_type.equals("3")){
				msg.setExamStatus(new BigDecimal(0));//（0 未审 1 已审  2审核不通过）
			}else{
				msg.setExamStatus(new BigDecimal(1));//（0 未审 1 已审  2审核不通过）
			}
		}
/****************************用户角色为企业则要审核 end********************/

		if(departId != null && !"".equals(departId) && !"null".equalsIgnoreCase(departId)) {
			msg.set("depart_id", Integer.parseInt(departId));
		}
		
		msg.set("item_id", null);
		
		EbBusiness bussInfo = new BussInterService().getBussById(businessId);
		if(bussInfo == null) {
			msg.setBusinessName(null);
		}else {
			msg.setBusinessName(bussInfo.getBusinessName());
		}
		
		boolean flag = dao.addMessageInfo(msg);
		return flag;
	}
	
	/**
	 * 添加留言，用于企业留言
	 * @param businessId
	 * @param content
	 * @param userId
	 * @param departId
	 * @return
	 */
	public boolean addMessage(String businessId, String content, String userId, String departId) {
		EbMessage msg = new EbMessage();
		
		msg.set("message_id", "message_seq.nextval");
		msg.setMessageContent(content);
		msg.setMessageTime(System.currentTimeMillis() + "");
		msg.set("status", 1);	//（ 0 回复、 1 留言）
		msg.set("user_id", userId);	//留言人
		msg.set("business_id", businessId);
		EbUserInfo userInfo = new UserInterService(null).findById(userId);
		
		if(userInfo == null) {
			msg.set("depart_id", null);
			msg.setUserName(null);
		}else {
			msg.setDepartId(userInfo.getDepartId());
			msg.setUserName(userInfo.getUserName());
		}
		if(departId != null && !"".equals(departId) && !"null".equalsIgnoreCase(departId) && MD5Util.isNumeric(departId)) {
			msg.set("depart_id", new BigDecimal(departId));
		}
/**********************2015-11-30 feini********************************/
		EbUserInfo uInfo = new UserInterService(null).findById(userId);
		if(uInfo != null){
			String role_type = userInfo.getRoleType()+"";//用户角色(1 办事人员、2 领导、 3 企业)
			if(role_type.equals("3")){
				msg.setExamStatus(new BigDecimal(0));//（0 未审 1 已审  2审核不通过）
			}else{
				msg.setExamStatus(new BigDecimal(1));//（0 未审 1 已审  2审核不通过）
			}
		}
/****************************用户角色为企业则要审核 end*********************/
		msg.set("item_id", null);
		
		EbBusiness bussInfo = new BussInterService().getBussById(businessId);
		if(bussInfo == null) {
			msg.setBusinessName(null);
		}else {
			msg.setBusinessName(bussInfo.getBusinessName());
		}
		
		boolean flag = dao.addMessageInfo(msg);
		return flag;
	}
	
	/**
	 * 领导督办添加留言项目
	 * @param businessId 企业id
	 * @param content留言内容
	 * @param userId用户Id
	 * @param userDeptId 部门Id
	 * @param itemId 事项id
	 * @param businessName
	 * @param userName
	 * @return
	 */
	public boolean addMessageToItem(String businessId, String content, String userId, String itemDeptId, 
			String itemId,String businessName,String userName){
		EbMessage msg = new EbMessage();
		
		msg.set("message_id", "message_seq.nextval");
		msg.setMessageContent(content);
		msg.setMessageTime(System.currentTimeMillis() + "");
		msg.set("status", 1);	//0代表回复 1代表留言
		//发起留言人
		msg.setUserId(new BigDecimal(userId));
		msg.setBusinessId(new BigDecimal(businessId));
		msg.setItemId(new BigDecimal(itemId));
		msg.setDepartId(new BigDecimal(itemDeptId));
		msg.setBusinessName(businessName);
		msg.setUserName(userName);
		msg.setExamStatus(new BigDecimal(1));//（0 未审 1 已审  2审核不通过）
		
		boolean result = false;
		if(msg.save()){
			result = true;
		}
		return result;
	}
	
	/**
	 * 领导批示留言回复
	 * @param messageId 留言Id
	 * @param content
	 * @param userId 回复的用户Id
	 * @param departId
	 * @param userName 回复的用户姓名
	 * @param businessId 企业Id
	 * @param businessName
	 * @param itemId
	 * @return
	 */
	public boolean addReplyByLeader(String messageId,String content,String userId,String departId,String userName,
			String businessId,String businessName,String itemId){
		EbMessage reply = new EbMessage();
		
		reply.set("message_id", "message_seq.nextval");
		reply.setStatus(new BigDecimal(0));//（ 0 回复、 1 留言）
		reply.setParamId(new BigDecimal(messageId));//留言id
		reply.setUserId(new BigDecimal(userId));
		reply.setBusinessId(new BigDecimal(businessId));
		reply.setBusinessName(businessName);
		reply.setUserName(userName);
		if(departId==null || departId.equals("null") || departId.equals("")){
			System.out.print("正确判读null！");
		}else{
			reply.setDepartId(new BigDecimal(departId));
		}
		
		if(itemId != null && !(itemId.equals("null")) && !("".equals(itemId))){
			reply.setItemId(new BigDecimal(itemId));
		}
		reply.setMessageContent(content);//保存留言内容
		reply.setMessageTime(System.currentTimeMillis() + "");
		reply.setExamStatus(new BigDecimal(1));//（0 未审 1 已审  2审核不通过）
		
		boolean result = false;
		if(reply.save()){
			result = true;
		}
		return result;
	}
	
	/**
	 * 根据部门id获取消息列表
	 * @param departId
	 * @return
	 */
	public List<EbNews> getNewsByDeptId(String departId) {
		if(departId == null || departId.equals("") || departId.equalsIgnoreCase("null")) {
			return null;
		}else {
			return dao.findNewsByDeptId(departId);
		}
	}
	
	/**
	 * 根据部门id获取消息列表
	 * @param departId
	 * @param pageIndex
	 * @param pageColunm
	 * @return
	 */
	public Page<EbNews> getNewsByDeptId(String departId, int pageIndex, int pageColunm) {
		if(pageIndex <= 0) {
			pageIndex = 1;
		}
		if(pageColunm <= 0) {
			pageColunm = 10;
		}
		if(departId == null || departId.equals("") || departId.equalsIgnoreCase("null")) {
			return null;
		}else {
			return dao.findNewsByDeptId(departId, pageIndex, pageColunm);
		}
	}
	
	/**
	 * 新增消息
	 * @param userInfo 当前用户信息、不能为空
	 * @param bussInfo 企业信息
	 * @param newsType 消息类型
	 * @param newsContent 消息内容
	 * @param coupleBack 反馈内容
	 * @param newsTime 消息产生时间 
	 * @param itemId 相关事项ID
	 * @param itemName 相关事项名称
	 * @param status 事项状态
	 * @param departId 相关部门ID
	 * @param departName 相关部门名称
	 * @return
	 */
	public boolean addNews(EbUserInfo userInfo,EbBusiness bussInfo,  String newsType, String newsContent, 
			String coupleBack, String newsTime, String itemId, String itemName, 
			String status, String departId, String departName) {
		EbNews newsInfo = new EbNews();
		
		newsInfo.set("NEWS_ID", "news_seq.nextval");//消息ID
		if(userInfo != null) {
			newsInfo.setUserId(userInfo.getUserId());//消息发起人ID
			newsInfo.setUserCode(userInfo.getUserAccount());//消息发起人code
			newsInfo.setUserName(userInfo.getUserName());//消息发起人名称
		}
		
		newsInfo.setNewsType(new BigDecimal(newsType));//消息类型
		newsInfo.setNewsContent(newsContent);//消息内容
		newsInfo.setCoupleBack(coupleBack);//反馈
		newsInfo.setNewsTime(newsTime);//消息时间
		if(bussInfo != null) {
			newsInfo.setBusinessId(bussInfo.getBusinessId());//相关企业ID
			newsInfo.setBusinessName(bussInfo.getBusinessName());//相关企业名称
		}else{
			newsInfo.setBusinessId(new BigDecimal(0000));//相关企业ID
			newsInfo.setBusinessName("所有企业");//相关企业名称
		}
		if(itemId != null && !itemId.equals("") && !itemId.equalsIgnoreCase("null")){
			newsInfo.setItemId(new BigDecimal(itemId));//相关事项ID
		}
//		newsInfo.setItemId(new BigDecimal(itemId));//相关事项ID
		if(itemName != null){
			newsInfo.setItemName(itemName);//相关事项名称
		}
		
		newsInfo.setStatus(new BigDecimal(status));//相关事项状态
			
		if(departId != null && !departId.equals("") && !departId.equalsIgnoreCase("null")){
			newsInfo.setDepartId(new BigDecimal(departId));//相关部门ID
		}
		if(departName != null){
			newsInfo.setDepartName(departName);//相关部门名称
		}
		
		boolean flag = false;
		try {
			flag = newsInfo.save();
		} catch (Exception e) {
			e.printStackTrace();
			flag = newsInfo.save();
		}
		
		return flag;
	}
	
}
