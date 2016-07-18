package cn.com.minstone.eBusiness.service;

import java.util.List;

import com.jfinal.plugin.activerecord.Page;

import cn.com.minstone.eBusiness.dao.MessageDao;
import cn.com.minstone.eBusiness.model.EbMessage;
import cn.com.minstone.eBusiness.model.EbNews;
public class MessageService extends BaseService {
	
	private MessageDao dao;
	
	public MessageService() {
		this.dao = new MessageDao();
	}
	
	/**
	 * 获取所有的留言
	 * @return list
	 */
	public List<EbMessage> getAllMessageList() {
		return dao.findAllMessageList();
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
	 * 获取所有的对应留言id的回复
	 * @return list
	 */
	public List<EbMessage> getAllReplyList(String messageId) {
		return dao.findAllReplyList(messageId);
	}
	
	/**
	 * 获取所有的已审核对应留言id的回复
	 * @return list
	 */
	public List<EbMessage> getAllReplyExamList(String messageId) {
		return dao.findAllReplyExamList(messageId);
	}
	
	/**
	 * 获取所有的对应留言id的回复(企业用户可以看到)
	 * @return list
	 */
	public List<EbMessage> getAllReplyExamListByBuss(String messageId) {
		return dao.findAllReplyExamListByBuss(messageId);
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
	 * 获得所有的反馈消息
	 * @return list
	 */
	public List<EbNews> getAllNewsList() {
		return dao.findAllNewsList();
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
	 * 根据企业名称和筛选时间段筛选消息
	 * @param bussName
	 * @param page
	 * @param start_time起始时间
	 * @param end_time当前时间
	 * @return
	 */
	public Page<EbNews> getNewsByBussNameTime(String bussName, int page,
			long start_time, long end_time) {
		return dao.findNewsByBussNameTime(bussName,page,start_time,end_time);
	}
}
