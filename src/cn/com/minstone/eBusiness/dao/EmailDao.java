package cn.com.minstone.eBusiness.dao;

import cn.com.minstone.eBusiness.model.EbNoticeEmail;

public class EmailDao extends BaseDao {
	
	private static final String selSQL = "select * from eb_notice_email t where t.is_delet = 1";
	
	/**
	 * 获取当前有效的邮箱
	 * @return
	 */
	public EbNoticeEmail getEmail(){
		return EbNoticeEmail.dao.findFirst(selSQL);
	}
	
	/**
	 * 根据邮箱id获取邮箱
	 * @param email_id
	 * @return
	 */
	public EbNoticeEmail   getEmailById(String email_id){
		String sql = "select * from eb_notice_email t where t.is_delet = 1 and email_id = ?";
		return EbNoticeEmail.dao.findFirst(sql,email_id);
	}
	
}
