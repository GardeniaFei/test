package cn.com.minstone.eBusiness.service;

import com.jfinal.plugin.activerecord.Page;

import cn.com.minstone.eBusiness.dao.NoticeDao;
import cn.com.minstone.eBusiness.model.EbNews;
import cn.com.minstone.eBusiness.model.EbNotice;

public class NoticeService {
	private NoticeDao dao;
	
	public NoticeService() {
		dao = new NoticeDao();
	}
	
	/**
	 * 获取所有的通知公告
	 * @return Page
	 */
	public Page<EbNotice> getAllNotice(int page) {
		Page<EbNotice> listPage = dao.findAll(page);
		return listPage;
	}
	
	/**
	 * 获取所有的通知公告app
	 * @return Page
	 */
	public Page<EbNotice> getAllNoticeByApp(int page) {
		Page<EbNotice> listPage = dao.findAllByApp(page);
		return listPage;
	}
	
	/**
	 * 通过时间筛选通告
	 * @param page
	 * @param start_time
	 * @param end_time
	 * @return
	 */
	public Page<EbNotice> getNoticeByTime(int page,long start_time, long end_time) {
		return dao.findNoticeByTime(page,start_time,end_time);
	}
	
	/**
	 * 获取所有的通知公告
	 * @return Page
	 * @param role 2企业，1部门用户
	 */
	public Page<EbNotice> getAllNoticeByType(int page,int role) {
		Page<EbNotice> listPage = dao.findAllByType(page,role);
		return listPage;
	}
	
	/**
	 * 根据公告id查询公告
	 * @param noticeId
	 * @return
	 */
	public EbNotice getNoticeById(String noticeId){
		return dao.findById(noticeId);
	}
}
