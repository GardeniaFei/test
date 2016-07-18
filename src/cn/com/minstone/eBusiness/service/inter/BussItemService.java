package cn.com.minstone.eBusiness.service.inter;

import java.math.BigDecimal;
import java.util.List;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;

import cn.com.minstone.eBusiness.dao.inter.BussItemDao;
import cn.com.minstone.eBusiness.model.EbBusiness;
import cn.com.minstone.eBusiness.model.EbBussItem;
import cn.com.minstone.eBusiness.service.BaseService;

public class BussItemService extends BaseService {
	private BussItemDao dao;
	private Controller ctrl;
	
	public BussItemService() {
		dao = new BussItemDao();
	}
	
	/**
	 * 企业新增事项
	 * @param bItem_name
	 * @param time_limit
	 * @param bUser_code 企业用户账户
	 * @return
	 */
	public boolean addBItem(String bItem_name,String bUser_code,String buss_id,String bitem_describe){
		boolean result = false;
		
		EbBussItem bitem = new EbBussItem();
		bitem.set("bItem_id", "EB_BUSS_ITEM_seq.nextval");
		bitem.setBItem_name(bItem_name);
		bitem.setBitem_describe(bitem_describe);
		bitem.setBuser_code(bUser_code);
		bitem.setBuss_id(new BigDecimal(buss_id));//企业id
		BussInterService bussSer = new BussInterService();
		EbBusiness buss = bussSer.findById(buss_id);
		if(buss != null){
			String bussName = buss.getBusinessName();
			String service_admin = buss.getService_admin();
			bitem.setBuss_name(bussName);
			bitem.setVuser_code(service_admin);//对应的vip用户
		}
		bitem.setBtran_status(new BigDecimal(0));
		bitem.setCreat_time(System.currentTimeMillis() + "");//创建时间
		bitem.set("bitem_type", new BigDecimal(2));//1为省办事项，2为企业事项
		result = bitem.save();
		
		return result;
	}
	
	/**
	 * 改变企业新增事项的状态
	 * @param btran_status 0未签收，1已签收，2退回
	 * @param bitem
	 * @return
	 */
	public boolean changeBItem(int btran_status,EbBussItem bitem){
		boolean result = false;
		
		bitem.setBtran_status(new BigDecimal(btran_status));
		bitem.setSign_time(System.currentTimeMillis() + "");//事项办理时间
		result = bitem.update();
		
		return result;
	}
	
	/**
	 * 根据企业新增事项id找事项
	 * @param bItem_id
	 * @return
	 */
	public EbBussItem findBItemById(String bItem_id){
		return dao.getBItemById(bItem_id);
	}
	
	/**
	 * 根据企业id找事项
	 * @param bussId 企业id
	 * @param bUser_code 对应vip服务专业的账户
	 * @return
	 */
	public List<EbBussItem> findBItem(String bussId,String vUser_code){
		return dao.getBItem(bussId,vUser_code);
	}
	
	/**
	 * 获取所有的企业的事项
	 * @param bussId
	 * @param page
	 * @return
	 */
	public Page<EbBussItem> findBItemPage(String bussId,int page){
		return dao.getBItemPage(bussId,page);
	}
}
