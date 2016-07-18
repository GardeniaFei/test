package cn.com.minstone.eBusiness.dao.inter;

import java.util.ArrayList;
import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import cn.com.minstone.eBusiness.model.EbMessage;
import cn.com.minstone.eBusiness.model.EbNews;
import cn.com.minstone.eBusiness.util.PathUtil;

public class MessageInterDao extends BaseInterDao {

	@SuppressWarnings("unused")
	private static final String table = "eb_message";
	private static final String selSQL = "select * from eb_message";
	
	/**
	 * 获取所有的留言列表
	 * @return list
	 */
	public List<EbMessage> findAllMessageList() {
		return EbMessage.dao.find("select * from eb_message m where m.status = ?","1");
	}
	
	/**
	 * 获取对应的企业Id留言列表
	 * @param bussId
	 * @return list
	 */
	public List<EbMessage> findMessageByBId(String bussId){
		return EbMessage.dao.find("select * from eb_message m where m.status = 1 and m.business_id = ?",bussId);
	}
	
	/**
	 * 根据企业id,获取所有的用户List
	 * @param bussId
	 * @return list<String>
	 */
	public List<String> findUserIdBybussId(String bussId){
		String str = "select distinct user_id from eb_message m where m.business_id = " +bussId;
		List<Record> rs = Db.find(str);
		List<String> uList = new ArrayList<String>();
		for(Record record:rs){
			uList.add(record.getBigDecimal("user_id").toString());
		}
		return uList;
	}
	
	/**
	 * 根据企业id,获取所有的用户userCode的List
	 * @param bussId
	 * @return list<String>
	 */
	public List<String> findUCBybussId(String bussId){
		String str = "select distinct user_code from eb_message m where m.business_id = " + bussId;
		List<Record> rs = Db.find(str);
		List<String> uList = new ArrayList<String>();
		for(Record record:rs){
			uList.add(record.getBigDecimal("user_code") + "");
		}
		return uList;
	}
	
	/**
	 * 获取所有的留言列表
	 * @return Page
	 */
	public Page<EbMessage> findAllMessage(int page) {
		String sql = " from eb_message m where m.status =1";
		Page<EbMessage> messagePage = EbMessage.dao
			.paginate(
				page, 
				PathUtil.MIN_PAGE_SIZE, 
				"select *", 
				sql);
		return messagePage;
	}
	
	/**
	 * 获取用户留言列表
	 * @param userId
	 * @param page
	 * @return Page
	 */
	public Page<EbMessage> findMessageByUId(int page ,String userId){
		String sql = " from eb_message m where m.status =1 and m.user_id = "+ userId;
		Page<EbMessage> messagePage = EbMessage.dao
			.paginate(
				page, 
				PathUtil.MIN_PAGE_SIZE, 
				"select *", 
				sql);
		return messagePage;
	}
	/**
	 * 获取用户留言列表领导用,去掉重复的企业
	 * @param userId
	 * @return list
	 */
	public List<EbMessage> findMessageByUIdLead(String userId){
		String sql = "select distinct business_id from eb_message m where m.status =1 and m.user_id = "+ userId;
		List<EbMessage> messagelist = EbMessage.dao.find(sql);
		return messagelist;
	}
	
	/**
	 * 获取用户留言列表
	 * @param userId
	 * @param page
	 * @param businessId
	 * @return Page
	 */
	public Page<EbMessage> findMessageByUIdBId(int page ,String userId ,String businessId,long start_time,long end_time){
		String sql ="";
		if(businessId==null||businessId.equals("")){
			sql = " from eb_message m where m.status =1 and m.user_id = "+ userId;
		}else{
			sql = " from eb_message m where m.status =1 and m.user_id = "+ userId + "and m.business_id ="+businessId;
		}
		sql += " and m.message_time >= "+ start_time + " and m.message_time <= "+ end_time +"order by m.message_time DESC";
		Page<EbMessage> messagePage = EbMessage.dao
			.paginate(
				page, 
				PathUtil.MIN_PAGE_SIZE, 
				"select *", 
				sql);
		return messagePage;
	}
	
	/**
	 * 根据企业Id和领导用户Id，获取一定时间段内的留言列表
	 * @param userId
	 * @param businessId
	 * @param start_time
	 * @param end_time
	 * @return
	 */
	public List<EbMessage> findMessageByUIdBIdList(String userId ,String businessId,long start_time,long end_time){
		String sql = "select * from eb_message m where m.status =1 and m.exam_status = 1 and m.user_id = "+ userId + " and m.business_id ="+businessId;
		if(userId == null){
			 sql = "select * from eb_message m where m.status =1 and m.exam_status = 1 and m.business_id ="+businessId;
		}
		sql += " and m.message_time >= "+ start_time + " and m.message_time <= "+ end_time +" order by message_time DESC";
		return EbMessage.dao.find(sql);
	}
	/**
	 * 获取留言对应的回复内容
	 * @param messageId
	 * @return list
	 */
	public List<EbMessage> findAllReplyList(String messageId) {
		return EbMessage.dao.find("select * from eb_message m where m.status = 0 and m.exam_status = 1 and m.param_id=?", messageId);
	}
	
	/**
	 * 获取留言对应的回复内容
	 * @param messageId
	 * @param page
	 * @return Page
	 */
	public Page<EbMessage> findAllReply(String messageId,int page) {
		String sql = " from eb_message m where m.status =0 and m.param_id= " + messageId + " order by message_time DESC";
		Page<EbMessage> replyPage = EbMessage.dao
			.paginate(
				page, 
				PathUtil.MIN_PAGE_SIZE, 
				"select *", 
				sql);
		return replyPage;
	}
	
	/**
	 * 获取留言对应的回复内容
	 * @param messageId
	 * @param page
	 * @return Page
	 */
	public Page<EbMessage> findReplyByMIdBId(String messageId,String bussId ,int page) {
		String sql = " from eb_message m where m.status =0 and m.param_id= " + messageId +"and m.business_id ="+bussId;
		Page<EbMessage> replyPage = EbMessage.dao
			.paginate(
				page, 
				PathUtil.MIN_PAGE_SIZE, 
				"select *", 
				sql);
		return replyPage;
	}
	
	public List<EbMessage> findReplyByMIdBIdList(String messageId,String bussId){
		return EbMessage.dao.find("select * from eb_message m where m.status = 0 and m.exam_status = 1 and m.param_id=? and m.business_id = ? order by message_time DESC", messageId,bussId);
	} 
	
	/**
	 * 筛选留言(根据发起人名称和企业名称选择)
	 * @param userName	发起人名称
	 * @param busName	企业名称
	 * @return list
	 */
	public List<EbMessage> findMesByUserBusList(String userName ,String busName){
		if((userName != null) && (busName == null || busName.equals(""))){
			return EbMessage.dao.find(selSQL + " m where m.user_name like '%" + userName + "%' and m.status = ? order by message_id","1");
		}else if((busName != null) && (userName == null || userName.equals(""))){
			return EbMessage.dao.find(selSQL + " m where m.business_name like '%" + busName + "%' and m.status = ? order by message_id","1");
		}else if((busName == null || busName.equals("")) && (userName == null || userName.equals(""))){
			return EbMessage.dao.find("select * from eb_message m where m.status = ?","1");
		}else if((busName != null) && (userName != null)){
			return EbMessage.dao.find(selSQL + " m where m.business_name like '%" + busName + "%' and m.user_name like '%" + userName + "%' and m.status = ? order by message_id","1");
		}else{
			return null;
		}
	}
	
	/**
	 * 筛选留言(根据发起人名称和企业名称选择)
	 * @param userName	发起人名称
	 * @param busName	企业名称
	 * @return Page
	 */
	public Page<EbMessage> findMesByUserBus(String userName ,String busName,int page){
		if((userName != null) && (busName == null || busName.equals(""))){
			String sql = " from eb_message m where m.user_name like '%" + userName + "%' and m.status =1";
			Page<EbMessage> messagePage = EbMessage.dao
				.paginate(
					page, 
					PathUtil.MIN_PAGE_SIZE, 
					"select *", 
					sql);
			return messagePage;
		}else if((busName != null) && (userName == null || userName.equals(""))){
			String sql = " from eb_message m where m.business_name like '%" + busName + "%' and m.status =1";
			Page<EbMessage> messagePage = EbMessage.dao
				.paginate(
					page, 
					PathUtil.MIN_PAGE_SIZE, 
					"select *", 
					sql);
			return messagePage;
		}else if((busName == null || busName.equals("")) && (userName == null || userName.equals(""))){
			String sql = " from eb_message m where m.status =1";
			Page<EbMessage> messagePage = EbMessage.dao
				.paginate(
					page, 
					PathUtil.MIN_PAGE_SIZE, 
					"select *", 
					sql);
			return messagePage;
		}else if((busName != null) && (userName != null)){
			String sql = " from eb_message m where m.business_name like '%" + busName + "%' and m.user_name like '%" + userName + "%' and m.status =1 order by message_id";
			Page<EbMessage> messagePage = EbMessage.dao
				.paginate(
					page, 
					PathUtil.MIN_PAGE_SIZE, 
					"select *", 
					sql);
			return messagePage;
		}else{
			return null;
		}
	}
	
	/**
	 * 获取所有的反馈消息
	 * @return list
	 */
	public List<EbNews> findAllNewsList() {
		return EbNews.dao.find("select * from eb_news order by news_id");
	}
	
	/**
	 * 获取所有的反馈消息
	 * @param page 当前页数
	 * @return Page
	 */
	public Page<EbNews> findAllNews(int page) {
		String sql = " from eb_news order by news_id";
		Page<EbNews> news = EbNews.dao
			.paginate(
				page, 
				PathUtil.MIN_PAGE_SIZE, 
				"select *", 
				sql);
		return news;
	}
	
	/**
	 * 根据企业名称筛选反馈消息
	 * @param bussName
	 * @return list
	 */
	public List<EbNews> findNewsByBussNameList(String bussName) {
		return EbNews.dao.find("select * from eb_news where business_name like '%" + bussName + "%' order by news_id");
	}
	
	/**
	 * 根据企业Id筛选反馈消息
	 * @param businessId
	 * @param deptId
	 * @param itemId
	 * @return list
	 */
	public List<EbNews> findNewsByBIDId(String businessId ,String deptId ,String itemId) {
		if((deptId == null) && (itemId == null)){
			return EbNews.dao.find("select * from eb_news where business_id = " + businessId + " order by news_id");
		}else if((deptId == null) && (itemId != null)){
			return EbNews.dao.find("select * from eb_news where business_id = " + businessId + " and item_id = "+ itemId +" order by news_id");
		}else if((deptId != null) && (itemId == null)){
			return EbNews.dao.find("select * from eb_news where business_id = " + businessId + " and depart_id = "+ deptId +" order by news_id");
		}else{
			return EbNews.dao.find("select * from eb_news where business_id = " + businessId + " and item_id = "+ itemId +" and depart_id = " + deptId + " order by news_id");
		}
	}
	
	/**
	 * 根据企业Id筛选反馈消息
	 * @param businessId
	 * @param deptId
	 * @param itemId
	 * @return list
	 */
	public List<EbNews> findNewsToFB(String businessId,String deptId,String itemId) {
		if((deptId == null) && (itemId == null)){
			return EbNews.dao.find("select * from eb_news where business_id = ? and news_type = 1 order by news_time desc", businessId);
		}else if((deptId == null) && (itemId != null)){
			return EbNews.dao.find("select * from eb_news where business_id = ?  and item_id = ? and news_type = 1 order by news_time desc",businessId,itemId);
		}else if((deptId != null) && (itemId == null)){
			return EbNews.dao.find("select * from eb_news where business_id = ? and depart_id = ? and news_type = 1 order by news_time desc",businessId,deptId);
		}else{
			return EbNews.dao.find("select * from eb_news where business_id = ? and item_id = ? and depart_id = ? and news_type = 1order by news_time desc",businessId,itemId,deptId);
		}
	}
	
	/**
	 * 根据企业名称筛选反馈消息
	 * @param page
	 * @param bussName
	 * @return Page
	 */
	public Page<EbNews> findNewsByBussName(String bussName,int page) {
		String sql = " from eb_news where business_name like '%" + bussName + "%' order by news_id";
		Page<EbNews> news = EbNews.dao
			.paginate(
				page, 
				PathUtil.MIN_PAGE_SIZE, 
				"select *", 
				sql);
		return news;
	}
	
	/**
	 * 根据留言id获取留言
	 * @param messageId
	 * @return
	 */
	public EbMessage findMsgByMsgId(String messageId) {
		return EbMessage.dao.findFirst("select * from eb_message where message_id = ?", messageId);
	}
	
	/**
	 * 添加留言回复或留言
	 * @param msgInfo
	 * @return
	 */
	public boolean addMessageInfo(EbMessage msgInfo) {
		boolean flag = false;
		try {
			flag = msgInfo.save();
		} catch (Exception e) {
			e.printStackTrace();
			flag = msgInfo.save();
		}
		
		return flag;
	}
	
	/**
	 * 根据部门id获取消息列表
	 * @param departId
	 * @return
	 */
	public List<EbNews> findNewsByDeptId(String departId) {
		if(departId == null || departId.equals("") || departId.equalsIgnoreCase("null")) {
			return null;
		}else {
			long time = System.currentTimeMillis() - 7 * 24 * 3600000;
			
			return EbNews.dao.find("select * from eb_news where depart_id = ? and news_time > ? order by news_id", departId, time);
		}
	}
	
	/**
	 * 根据部门ID筛选消息列表
	 * @param departId
	 * @param pageIndex
	 * @param pageColunm
	 * @return
	 */
	public Page<EbNews> findNewsByDeptId(String departId, int pageIndex, int pageColunm) {
		String sql = " from eb_news where depart_id = " + departId + " or news_type = 13 order by news_time desc";
		Page<EbNews> news = EbNews.dao
			.paginate(
				pageIndex, 
				pageColunm, 
				"select *", 
				sql);
		return news;
	}
}
