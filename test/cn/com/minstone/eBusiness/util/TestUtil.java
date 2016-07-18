package cn.com.minstone.eBusiness.util;

import cn.com.minstone.eBusiness.model.EbAttention;
import cn.com.minstone.eBusiness.model.EbBusiness;
import cn.com.minstone.eBusiness.model.EbBusinessType;
import cn.com.minstone.eBusiness.model.EbDepart;
import cn.com.minstone.eBusiness.model.EbDeptEvainfo;
import cn.com.minstone.eBusiness.model.EbEvaluate;
import cn.com.minstone.eBusiness.model.EbItem;
import cn.com.minstone.eBusiness.model.EbMessage;
import cn.com.minstone.eBusiness.model.EbNews;
import cn.com.minstone.eBusiness.model.EbSpcProgram;
import cn.com.minstone.eBusiness.model.EbTask;
import cn.com.minstone.eBusiness.model.EbTaskDistribute;
import cn.com.minstone.eBusiness.model.EbTaskList;
import cn.com.minstone.eBusiness.model.EbUserInfo;

import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.CaseInsensitiveContainerFactory;
import com.jfinal.plugin.activerecord.dialect.OracleDialect;
import com.jfinal.plugin.c3p0.C3p0Plugin;

public class TestUtil {
	
	public static void init(){
		C3p0Plugin c3p0Plugin = new C3p0Plugin("jdbc:oracle:thin:@localhost:1521/oanet", "test_base", "11", "oracle.jdbc.OracleDriver");
		c3p0Plugin.setInitialPoolSize(50);
		c3p0Plugin.setMaxPoolSize(100);
		c3p0Plugin.setMinPoolSize(5);
		c3p0Plugin.start();
		
		ActiveRecordPlugin arp = new ActiveRecordPlugin(c3p0Plugin.getDataSource());
		
		arp.setDialect(new OracleDialect());
		arp.setShowSql(true);
		arp.setContainerFactory(new CaseInsensitiveContainerFactory());

		arp.addMapping("EB_ATTENTION", "USER_ID", EbAttention.class);
		arp.addMapping("EB_BUSINESS", "BUSINESS_ID", EbBusiness.class);
		arp.addMapping("EB_BUSINESS_TYPE", "TYPE_ID", EbBusinessType.class);
		arp.addMapping("EB_DEPT_EVAINFO", "PARAM_ID", EbDeptEvainfo.class);
		arp.addMapping("EB_DEPART", "DEPART_ID", EbDepart.class);
		arp.addMapping("EB_EVALUATE", "EVALUATE_ID", EbEvaluate.class);
		arp.addMapping("EB_ITEM", "ITEM_ID", EbItem.class);
		arp.addMapping("EB_MESSAGE", "MESSAGE_ID", EbMessage.class);
		arp.addMapping("EB_NEWS", "NEWS_ID", EbNews.class);
		arp.addMapping("EB_SPC_PROGRAM", "PROGRAM_ID", EbSpcProgram.class);
		arp.addMapping("EB_TASK_DISTRIBUTE", "TASK_ID", EbTaskDistribute.class);
		arp.addMapping("EB_TASK_LIST", "TYPE_ID", EbTaskList.class);
		arp.addMapping("EB_TASK", "TASK_ID", EbTask.class);
		arp.addMapping("EB_USER_INFO", "USER_ID", EbUserInfo.class);
		
		arp.start();
	}

}
