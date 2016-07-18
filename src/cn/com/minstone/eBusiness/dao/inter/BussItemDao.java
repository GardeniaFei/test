package cn.com.minstone.eBusiness.dao.inter;

import java.util.List;

import com.jfinal.plugin.activerecord.Page;

import cn.com.minstone.eBusiness.model.EbBussItem;
import cn.com.minstone.eBusiness.model.EbNotice;
import cn.com.minstone.eBusiness.util.PathUtil;

public class BussItemDao extends BaseInterDao {
	
	private static final String table = "eb_buss_item";
	private static final String selSQL = "select * from eb_buss_item";
	
	/**
	 * 根据企业事项id查找事项
	 * @param bItem_id
	 * @return
	 */
	public EbBussItem getBItemById(String bItem_id){
		String sql = "select * from eb_buss_item where bitem_id = ?";
		return EbBussItem.dao.findFirst(sql,bItem_id);
	}

	/**
	 * 根据企业id和账户获取事项列表
	 * @param bussId
	 * @param vUser_code
	 * @return
	 */
	public List<EbBussItem> getBItem(String bussId, String vUser_code) {
		String sql =selSQL ;
		if(bussId != null && !("".equals(bussId)) && !("null".equals(bussId))){
			sql += " where buss_id = '" +bussId + "'";
			if(vUser_code != null && !("".equals(vUser_code)) && !("null".equals(vUser_code))){
				sql += " and vUser_code = '" +vUser_code + "'";
			}
		}else{
			if(vUser_code != null && !("".equals(vUser_code))){
				sql += " where vUser_code = '" +vUser_code + "'";
			}
		}
		
		return EbBussItem.dao.find(sql);
	}

	/**
	 * 获取事项
	 * @param bussId
	 * @param page
	 * @return
	 */
	public Page<EbBussItem> getBItemPage(String bussId,int page) {
		String sql = " from eb_buss_item  where buss_id='"+bussId+"' order by creat_time desc";
		Page<EbBussItem> bitemPage = EbBussItem.dao
			.paginate(
				page, 
				PathUtil.MIN_PAGE_SIZE, 
				"select *", 
				sql);
		return bitemPage;
	}
}
