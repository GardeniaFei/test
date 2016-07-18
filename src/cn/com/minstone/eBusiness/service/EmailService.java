package cn.com.minstone.eBusiness.service;

import cn.com.minstone.eBusiness.dao.EmailDao;
import cn.com.minstone.eBusiness.model.EbNoticeEmail;

public class EmailService {
	private EmailDao dao;
	
	public EmailService() {
		this.dao = new EmailDao();
	}
	
	/**
	 * 获取当前有效的邮箱
	 * @return
	 */
	public EbNoticeEmail getEmail(){
		return dao.getEmail();
	}
	
	/**
	 * 根据邮箱id获取邮箱
	 * @param email_id
	 * @return
	 */
	public EbNoticeEmail  getEmailById(String email_id){
		return dao.getEmailById(email_id);
	}
}
