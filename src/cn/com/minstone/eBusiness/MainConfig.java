package cn.com.minstone.eBusiness;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import cn.com.minstone.eBusiness.MainConfig;
import cn.com.minstone.eBusiness.controller.BaseInfoController;
import cn.com.minstone.eBusiness.controller.timeChangeCtrl;
import cn.com.minstone.eBusiness.ctrl.AdminCtrl;
import cn.com.minstone.eBusiness.ctrl.ConfigCtrl;
import cn.com.minstone.eBusiness.ctrl.FileCtrl;
import cn.com.minstone.eBusiness.ctrl.MainCtrl;
import cn.com.minstone.eBusiness.ctrl.NoticeCtrl;
import cn.com.minstone.eBusiness.ctrl.ShowCtrl;
import cn.com.minstone.eBusiness.ctrl.inter.ApplyCtrl;
import cn.com.minstone.eBusiness.ctrl.inter.BussItemCtrl;
import cn.com.minstone.eBusiness.ctrl.inter.BussListCtrl;
import cn.com.minstone.eBusiness.ctrl.inter.CommonCtrl;
import cn.com.minstone.eBusiness.ctrl.inter.LeadCtrl;
import cn.com.minstone.eBusiness.ctrl.inter.MapInterCtrl;
import cn.com.minstone.eBusiness.ctrl.inter.ProvinCtrl;
import cn.com.minstone.eBusiness.ctrl.inter.StatCtrl;
import cn.com.minstone.eBusiness.ctrl.inter.WorkCtrl;
import cn.com.minstone.eBusiness.model.BaseAdmin;
import cn.com.minstone.eBusiness.model.BaseUser;
import cn.com.minstone.eBusiness.model.EbAttention;
import cn.com.minstone.eBusiness.model.EbBusiness;
import cn.com.minstone.eBusiness.model.EbBusinessType;
import cn.com.minstone.eBusiness.model.EbBussItem;
import cn.com.minstone.eBusiness.model.EbBussShow;
import cn.com.minstone.eBusiness.model.EbCenter;
import cn.com.minstone.eBusiness.model.EbDepart;
import cn.com.minstone.eBusiness.model.EbDeptEvainfo;
import cn.com.minstone.eBusiness.model.EbEvaluate;
import cn.com.minstone.eBusiness.model.EbFileImg;
import cn.com.minstone.eBusiness.model.EbInformation;
import cn.com.minstone.eBusiness.model.EbItem;
import cn.com.minstone.eBusiness.model.EbMapFile;
import cn.com.minstone.eBusiness.model.EbMessage;
import cn.com.minstone.eBusiness.model.EbNews;
import cn.com.minstone.eBusiness.model.EbNotice;
import cn.com.minstone.eBusiness.model.EbNoticeEmail;
import cn.com.minstone.eBusiness.model.EbSpcProgram;
import cn.com.minstone.eBusiness.model.EbTask;
import cn.com.minstone.eBusiness.model.EbTaskDistribute;
import cn.com.minstone.eBusiness.model.EbTaskList;
import cn.com.minstone.eBusiness.model.EbUserInfo;
import cn.com.minstone.eBusiness.model.SmsTbl;
import cn.com.minstone.eBusiness.util.Config;
import cn.com.minstone.eBusiness.util.MCubeAppConfig;
import cn.com.minstone.eBusiness.util.PushUtil;
import cn.com.minstone.jfinal.ext.AppVersion;
import cn.com.minstone.jfinal.ext.SystemConfig;
import cn.com.minstone.jfinal.ext.plugin.TableCheckPlugin;
import cn.com.minstone.jfinal.ext.util.ConfigKit;
import cn.com.minstone.jfinal.ext.util.Logger;
import cn.com.minstone.jfinal.ext.util.VersionKit;

import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.ext.handler.ContextPathHandler;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.CaseInsensitiveContainerFactory;
import com.jfinal.plugin.activerecord.dialect.OracleDialect;
import com.jfinal.plugin.c3p0.C3p0Plugin;
import com.jfinal.render.JspRender;
import com.jfinal.render.ViewType;

public class MainConfig extends JFinalConfig {
	private Logger logger = new Logger(MainConfig.class);
	
	public static ConfigKit configKit;	//通过配置获取某些信息的工具
	
	@SuppressWarnings("deprecation")
	@Override
	public void configConstant(Constants me) {
		me.setDevMode(true);
		me.setEncoding("utf-8");
		me.setViewType(ViewType.FREE_MARKER);
		JspRender.setSupportActiveRecord(true);
		
	}

	@Override
	public void afterJFinalStart() {
		// TODO Auto-generated method stub
		super.afterJFinalStart();
		
		PushUtil.init(MCubeAppConfig.getInstance().appId, MCubeAppConfig.getInstance().appKey, MCubeAppConfig.getInstance().master);
		
		logger.error("=======================================");
		logger.error("           易企办平台启动成功！！！               ");
		logger.error("=======================================");
		
		new CommonCtrl().caculateItemTime();//计算超时事项
		new CommonCtrl().changeDistrStatus();//同步审批办件状态
	}

	@Override
	public void configRoute(Routes me) {
		me.add("/config", ConfigCtrl.class);
		me.add("/", MainCtrl.class);
		me.add("/admin", AdminCtrl.class);
		me.add("/common", CommonCtrl.class);
		me.add("/interface/common", CommonCtrl.class);
		me.add("/interface/leader", LeadCtrl.class);
//		me.add("/interface/business", BussController.class);
		me.add("/user", BaseInfoController.class);
		me.add("/interface/worker", WorkCtrl.class);
		me.add("/appylist",ApplyCtrl.class);
		me.add("/stat",StatCtrl.class);//统计类
		me.add("/change",timeChangeCtrl.class);
		me.add("/buss",BussListCtrl.class);
		me.add("/img",FileCtrl.class);
		me.add("/view",NoticeCtrl.class);
		me.add("/interface/bitem",BussItemCtrl.class);
		me.add("/province",ProvinCtrl.class);
		me.add("/show",ShowCtrl.class);
		me.add("/map",MapInterCtrl.class);
	}

	@Override
	public void configPlugin(Plugins me) {
		//该方法主要配置连接oracle数据库的相关配置、配置JFinal数据库操作插件
		initConfig();
		
		ActiveRecordPlugin publishArp = null;
		ActiveRecordPlugin arp = null;
		DataSource ds = null;
		boolean isDataSourceLoaded = false;
		try {
			Context ctx = new InitialContext();
			String baseConn = MCubeAppConfig.getInstance().getBaseConn();
			String publicConn = MCubeAppConfig.getInstance().getPublishConn();
			
			if(baseConn == null){
				logger.error("无基础数据源。请先进入配置页面进行配置。http://ip:port/EBusiness/config");
				return;
			}
			
			if(publicConn == null){
				logger.error("无公共数据源。请先进入配置页面进行配置。http://ip:port/EBusiness/config");
				return;
			}
			//启动打包的驱动
			C3p0Plugin c3p0Plugin = new C3p0Plugin(MCubeAppConfig.getInstance().getJdbcUrl(), MCubeAppConfig.getInstance().getBaseConn(), 
					MCubeAppConfig.getInstance().getJdbcPassword(),"oracle.jdbc.OracleDriver");
			c3p0Plugin.setInitialPoolSize(MCubeAppConfig.getInstance().getInitialPoolSize());
			c3p0Plugin.setMaxPoolSize(MCubeAppConfig.getInstance().getMaxPoolSize());
			c3p0Plugin.setMinPoolSize(MCubeAppConfig.getInstance().getMinPoolSize());
			c3p0Plugin.start();
			ds = c3p0Plugin.getDataSource();
			
//			ds = (DataSource) ctx.lookup(baseConn);
			arp = new ActiveRecordPlugin(ds);
			
			//地区库连接池
			DataSource baseDataSource = (DataSource) ctx.lookup(MCubeAppConfig.getInstance().getBaseConn());
			arp = new ActiveRecordPlugin("base", baseDataSource);
			
			DataSource publishDataSource = (DataSource) ctx.lookup(MCubeAppConfig.getInstance().getPublishConn());
			publishArp = new ActiveRecordPlugin("publish", publishDataSource); 
			
			isDataSourceLoaded = true;
		} catch (NamingException e) {
			e.printStackTrace();
			isDataSourceLoaded = false;
		}finally{
			if(!isDataSourceLoaded){
				logger.error("=====无法加载数据源！请检查数据源配置是否正确！=====");
				return;
			}
		}

		//自动执行建表语句
//		InitDatabasePlugin idp = new InitDatabasePlugin(ds);
//		me.add(idp);
		
		//自动检测数据源和表
		TableCheckPlugin tcp = new TableCheckPlugin(ds, "EB_ATTENTION", "EB_BUSINESS", "EB_BUSINESS_TYPE", "EB_DEPT_EVAINFO", 
				"EB_DEPART", "EB_EVALUATE", "EB_ITEM", "EB_MESSAGE", "EB_NEWS", "EB_SPC_PROGRAM", "EB_OVERTIME_NEWS", 
				"EB_TASK_DISTRIBUTE", "EB_TASK_LIST", "EB_TASK", "EB_USER_INFO","baserole_admin","base_user", "SMS_TBL",
				"EB_FILE_IMG","EB_BUSS_ITEM","EB_NOTICE","EB_NOTICE_EMAIL","EB_MAP_FILE","EB_CENTER","EB_INFORMATION","EB_BUSS_SHOW");
		me.add(tcp);

		// 配置Oracle方言
		arp.setDialect(new OracleDialect());
		arp.setShowSql(true);
		arp.setContainerFactory(new CaseInsensitiveContainerFactory());

		publishArp.setDialect(new OracleDialect());
		publishArp.setShowSql(MCubeAppConfig.getInstance().getDebug());
		publishArp.setContainerFactory(new CaseInsensitiveContainerFactory());
		
		arp.addMapping("EB_ATTENTION", "ATTENT_ID", EbAttention.class);
		arp.addMapping("EB_BUSINESS", "BUSINESS_ID", EbBusiness.class);
		arp.addMapping("EB_BUSINESS_TYPE", "TYPE_ID", EbBusinessType.class);
		arp.addMapping("EB_DEPT_EVAINFO", "PARAM_ID", EbDeptEvainfo.class);
		arp.addMapping("EB_DEPART", "DEPART_ID", EbDepart.class);
		arp.addMapping("EB_EVALUATE", "EVALUATE_ID", EbEvaluate.class);
		arp.addMapping("EB_ITEM", "ITEM_ID", EbItem.class);
		arp.addMapping("EB_MESSAGE", "MESSAGE_ID", EbMessage.class);
		arp.addMapping("EB_NEWS", "NEWS_ID", EbNews.class);
		arp.addMapping("EB_SPC_PROGRAM", "PROGRAM_ID", EbSpcProgram.class);
		arp.addMapping("EB_TASK_DISTRIBUTE", "DISTR_ID", EbTaskDistribute.class);
		arp.addMapping("EB_TASK_LIST", "T_ID", EbTaskList.class);
		arp.addMapping("EB_TASK", "TASK_ID", EbTask.class);
		arp.addMapping("EB_USER_INFO", "USER_ID", EbUserInfo.class);
		arp.addMapping("base_user", "user_code", BaseUser.class);
		arp.addMapping("baserole_admin", "C_USER_CODE", BaseAdmin.class);
		arp.addMapping("EB_FILE_IMG", "image_id", EbFileImg.class);
		arp.addMapping("EB_BUSS_ITEM", "bItem_id", EbBussItem.class);
		arp.addMapping("EB_NOTICE", "notice_id", EbNotice.class);
		arp.addMapping("EB_NOTICE_EMAIL", "email_id", EbNoticeEmail.class);
		arp.addMapping("EB_MAP_FILE", "map_id", EbMapFile.class);
		arp.addMapping("EB_CENTER", "CENTER_ID", EbCenter.class);
		arp.addMapping("EB_INFORMATION", "INFORMATION_ID", EbInformation.class);
		arp.addMapping("EB_BUSS_SHOW", "show_id",EbBussShow.class);
		//发送短信
		publishArp.addMapping("SMS_TBL", "SMS_SEQ", SmsTbl.class);
		
		//此句放到最后
		me.add(arp);
		me.add(publishArp);
	}

	@Override
	public void configInterceptor(Interceptors me) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void configHandler(Handlers me) {
		me.add(new ContextPathHandler("base"));
		
	}
	
	private void initConfig(){
		if(configKit == null){
			//获取当前应用的版本
			AppVersion appVersion = VersionKit.getXMLAppVersion();
			configKit = new ConfigKit(appVersion.getAppName());
			
			SystemConfig bcc = new SystemConfig(Config.BASE_CONN,"test_base", "数据库连接池",true);
			SystemConfig a2 = new SystemConfig(Config.PUBLISH_CONN, "test_base", "版本发布连接池", true);
			SystemConfig vc = new SystemConfig(Config.VERSION, appVersion.getAppVersion(), "当前系统版本，请勿修改该值。");
			
			SystemConfig gov = new SystemConfig(Config.GOV_URL, null, "审批接口地址",true);
			gov.setExamValue("http://ip:port/CommonService/api/apprItem/itemList/query.v");
			
			SystemConfig govUser = new SystemConfig(Config.GOV_USER, null, "审批接口用户",true);
			govUser.setExamValue("150928114832859");
			
			SystemConfig govPwd = new SystemConfig(Config.GOV_PWD, null, "审批接口用户密码",true);
			govPwd.setExamValue("8387844514b96ba09ff6e9665745e9ac");
			
			SystemConfig appLoadUrl = new SystemConfig(Config.APP_LOAD_URL,null, "App应用下载地址",true);
			SystemConfig defaultPwd = new SystemConfig(Config.DEFAULT_PWD,"123456", "用户账号默认密码",true);
			
			//消息推送配置信息
			SystemConfig appId = new SystemConfig(Config.APP_ID, null, "消息推送客户端应用唯一ID(可不填)",false);
			appId.setExamValue("oQAn9YSLSo7LFzgVWQG5a4");
			
			SystemConfig appKey = new SystemConfig(Config.APP_KEY, null, "消息推送鉴定身份密钥",true);
			appKey.setExamValue("dwjO9QABnu8yVHBadUzO11");
			
			SystemConfig master = new SystemConfig(Config.MASTER, null, "消息推送鉴定权限码",true);
			master.setExamValue("lDoOHpBezKApsP9amUBj44");
			
			SystemConfig a6 = new SystemConfig(Config.DEBUG,null,"是否为调试模式",true);
			a6.addOptions("true","false");
			
			SystemConfig a7 = new SystemConfig(Config.ZWB_NAME,"政务办","区级政务办名称",true);
//			SystemConfig a8 = new SystemConfig(Config.ZSJ_NAME,"招商局","招商局名称",true);
			SystemConfig a8 = new SystemConfig(Config.ZWB_NAME_CITY,"市级政务办","市级政务办名称",true);
			
			SystemConfig baseUrl = new SystemConfig(Config.BASE_URL, null, "外网系统访问地址", true);
			SystemConfig imgUrl = new SystemConfig(Config.IMG_URL, null, "保存地址", true);
			
			SystemConfig prinUser = new SystemConfig(Config.PRIN_USER,"minstone_user", "琶洲分中心外网用户名",true);
			SystemConfig prinPwd = new SystemConfig(Config.PRIN_PWD,"minstone12345", "琶洲分中心外网密码",true);
			
			SystemConfig a11 = new SystemConfig(Config.JDBCURL , "jdbc:oracle:thin:@127.0.0.1:1521/oanet" , "连接池驱动url",true);
			SystemConfig a12 = new SystemConfig(Config.JDBCPASSWORD , "11" , "连接池密码" , true);
			SystemConfig a13 = new SystemConfig(Config.INITALPOOLSIZE ,"5" , "连接池驱动初始值" , true);
			SystemConfig a14 = new SystemConfig(Config.MAXPOOLSIZE , "10" , "连接池驱动最大值" , true);
			SystemConfig a15 = new SystemConfig(Config.MIXPOOLSIZE , "5" , "连接池驱动最小值" , true);
			
			configKit.addConfig(bcc);
			configKit.addConfig(a2);
			configKit.addConfig(a6);
			configKit.addConfig(vc);
			configKit.addConfig(gov);
			configKit.addConfig(govPwd);
			configKit.addConfig(govUser);
			configKit.addConfig(defaultPwd);
			configKit.addConfig(appLoadUrl);
			configKit.addConfig(appId);
			configKit.addConfig(appKey);
			configKit.addConfig(master);
			configKit.addConfig(a7);
			configKit.addConfig(a8);
			configKit.addConfig(baseUrl);
			configKit.addConfig(imgUrl);
			configKit.addConfig(prinUser);
			configKit.addConfig(prinPwd);
			configKit.addConfig(a11);
			configKit.addConfig(a12);
			configKit.addConfig(a13);
			configKit.addConfig(a14);
			configKit.addConfig(a15);
		}
	}
	

//	public static void main(String[] args){//192.168.0.220
//		C3p0Plugin c3p0Plugin = new C3p0Plugin("jdbc:oracle:thin:@127.0.0.1:1521/oanet", "test_base", "11", "oracle.jdbc.OracleDriver");
//		c3p0Plugin.setInitialPoolSize(5);
//		c3p0Plugin.setMaxPoolSize(10);
//		c3p0Plugin.setMinPoolSize(5);
//		c3p0Plugin.start();
//		
//		DataSource ds = c3p0Plugin.getDataSource();
//		
//		
//		/**
//		 * 根据数据库表名称，生成JFinal的Model类
//		 * */
//		ModelGeneraPlugin plugin = new ModelGeneraPlugin(ds, "cn.com.minstone.eBusiness.model", 
//				"EB_USER_INFO", "EB_ATTENTION", "EB_BUSINESS", "EB_BUSINESS_TYPE", 
//				"EB_DEPT_EVAINFO", "EB_DEPART", "EB_EVALUATE", "EB_ITEM", 
//				"EB_MESSAGE", "EB_NEWS", "EB_SPC_PROGRAM", "EB_TASK_DISTRIBUTE", 
//				"EB_TASK_LIST", "EB_TASK");
//		plugin.start();
//	}
}


