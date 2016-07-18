package cn.com.minstone.eBusiness.dao;

import cn.com.minstone.eBusiness.model.EbNotice;
import cn.com.minstone.eBusiness.util.PathUtil;

import com.jfinal.plugin.activerecord.Page;

public class NoticeDao extends BaseDao {
	
	private static final String selSQL = "select * from eb_notice";
	
	/**
	 * 获取所有的通知公告
	 * @param page
	 * @return Page
	 */
	public Page<EbNotice> findAll(int page) {
		String sql = " from eb_notice  where is_delet=1 order by notice_time desc";
		Page<EbNotice> noticePage = EbNotice.dao
			.paginate(
				page, 
				PathUtil.MIN_PAGE_SIZE, 
				"select *", 
				sql);
		return noticePage;
	}
	
	/**
	 * 获取所有的通知公告
	 * @param page
	 * @return Page
	 */
	public Page<EbNotice> findAllByApp(int page) {
		String sql = " from eb_notice  where notice_tatus =1 and is_delet=1 order by notice_time desc";
		Page<EbNotice> noticePage = EbNotice.dao
			.paginate(
				page, 
				PathUtil.MIN_PAGE_SIZE, 
				"select *", 
				sql);
		return noticePage;
	}

	/**
	 * 根据通告对象筛选通告
	 * @param page
	 * @param role 1 部门，2 企业
	 * @return
	 */
	public Page<EbNotice> findAllByType(int page, int role) {
		String sql="";
		if(role == 1){
			sql = " from eb_notice  where notice_type in(1,3) and notice_tatus =1 and is_delet=1 order by notice_time desc";
		}else{
			sql = " from eb_notice  where notice_type in(2,3) and notice_tatus =1 and is_delet=1 order by notice_time desc";
		}
		Page<EbNotice> noticePage = EbNotice.dao
			.paginate(
				page, 
				PathUtil.MIN_PAGE_SIZE, 
				"select *", 
				sql);
		return noticePage;
	}

	/**
	 * 根据时间筛选通告
	 * @param page
	 * @param start_time
	 * @param end_time
	 * @return
	 */
	public Page<EbNotice> findNoticeByTime(int page, long start_time,
			long end_time) {
		String sql  = " from eb_notice where notice_time >= "+ start_time + " and notice_time <= "+ end_time +" and is_delet=1 order by notice_time DESC";
		
		Page<EbNotice> notices = EbNotice.dao
			.paginate(
				page, 
				PathUtil.MIN_PAGE_SIZE, 
				"select *", 
				sql);
		return notices;
	}

	/**
	 * 根据id获取通知公告
	 * @param noticeId
	 * @return
	 */
	public EbNotice findById(String noticeId) {
		String sql = selSQL + " where notice_id = ?";
		return EbNotice.dao.findFirst(sql, noticeId);
	}
}
