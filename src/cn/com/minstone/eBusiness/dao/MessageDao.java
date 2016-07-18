package cn.com.minstone.eBusiness.dao;

import java.util.List;

import com.jfinal.plugin.activerecord.Page;

import cn.com.minstone.eBusiness.model.EbMessage;
import cn.com.minstone.eBusiness.model.EbNews;
import cn.com.minstone.eBusiness.util.PathUtil;

public class MessageDao extends BaseDao {

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
	 * 获取留言对应的回复内容
	 * @param messageId
	 * @return list
	 */
	public List<EbMessage> findAllReplyList(String messageId) {
		return EbMessage.dao.find("select * from eb_message m where m.status = 0 and m.param_id=?", messageId);
	}
	
	/**
	 * 获取留言对应已通过审核的回复内容
	 * @param messageId
	 * @return list
	 */
	public List<EbMessage> findAllReplyExamList(String messageId) {
		return EbMessage.dao.find("select * from eb_message m where m.status = 0 and m.exam_status = 1 and m.param_id=?", messageId);
	}
	
	/**
	 * 获取留言对应的回复内容(企业用户可看到未审核)
	 * @param messageId
	 * @return list
	 */
	public List<EbMessage> findAllReplyExamListByBuss(String messageId) {
		return EbMessage.dao.find("select * from eb_message m where m.status = 0 and m.param_id=?", messageId);
	}
	/**
	 * 获取留言对应的回复内容
	 * @param messageId
	 * @param page
	 * @return Page
	 */
	public Page<EbMessage> findAllReply(String messageId,int page) {
		String sql = " from eb_message m where m.status =0 and m.param_id= " + messageId;
		Page<EbMessage> replyPage = EbMessage.dao
			.paginate(
				page, 
				PathUtil.MIN_PAGE_SIZE, 
				"select *", 
				sql);
		return replyPage;
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
		String sql = " from eb_news order by news_time desc";
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
	 * 根据企业名称筛选反馈消息
	 * @param page
	 * @param bussName
	 * @return Page
	 */
	public Page<EbNews> findNewsByBussName(String bussName,int page) {
		String sql = " from eb_news where business_name like '%" + bussName + "%' order by news_time DESC";
		Page<EbNews> news = EbNews.dao
			.paginate(
				page, 
				PathUtil.MIN_PAGE_SIZE, 
				"select *", 
				sql);
		return news;
	}

	public Page<EbNews> findNewsByBussNameTime(String bussName, int page,
			long start_time, long end_time) {
		String sql = "";
		if(bussName == null || "".equals(bussName) || "null".equalsIgnoreCase(bussName)){
			sql = " from eb_news where news_time >= "+ start_time + " and news_time <= "+ end_time +"order by news_time DESC";
		}else{
			sql = " from eb_news where business_name like '%" + bussName + "%'";
			sql += " and news_time >= "+ start_time + " and news_time <= "+ end_time +"order by news_time DESC";
		}
		
		Page<EbNews> news = EbNews.dao
			.paginate(
				page, 
				PathUtil.MIN_PAGE_SIZE, 
				"select *", 
				sql);
		return news;
	}
	
}
