package cn.com.minstone.eBusiness.util;

import java.io.File;

import org.dom4j.DocumentException;

import cn.com.minstone.jfinal.ext.AppVersion;
import cn.com.minstone.jfinal.ext.util.ConfigKit;
import cn.com.minstone.jfinal.ext.util.Logger;
import cn.com.minstone.jfinal.ext.util.VersionKit;
import cn.com.minstone.eBusiness.util.Config;

public class MCubeAppConfig {
	
	private static Logger logger = new Logger(MCubeAppConfig.class);
	
	private static MCubeAppConfig instance;
	private static final String FILE_NAME = "MCubeAppConfig.xml";
	private long lastConfigModify;
	
	public static MCubeAppConfig getInstance(){
		File configFile = new File(FILE_NAME);

		if(!configFile.exists()){
			logger.info("配置文件路径：" + configFile.getAbsolutePath());
			logger.error("配置文件不存在，请认真检查配置文件。");
		}
		
		if(instance == null){
			synchronized (FILE_NAME) {
				if(instance == null){
					instance = new MCubeAppConfig();
					instance.lastConfigModify = configFile.lastModified();
				}
			}
			
			logger.debug("加载配置文件成功！");
			logger.info("配置文件路径：" + configFile.getAbsolutePath());
		}else if(instance.lastConfigModify < configFile.lastModified()){//配置文件已更新
			instance.init();
			instance.lastConfigModify = configFile.lastModified();
			
			logger.debug("检测到配置文件已更新，重新加载配置文件成功！");
		}
		
		return instance;
	}

	
	private MCubeAppConfig(){
		this.init();
	}
	
	/**
	 * 初始化参数
	 * */
	private void init(){
		synchronized (FILE_NAME) {
			AppVersion xmlVersion = VersionKit.getXMLAppVersion();
			ConfigKit kit = new ConfigKit(xmlVersion.getAppName());
			
			try {
				
				this.baseConn = kit.getNodeValue(Config.BASE_CONN);
				this.version = kit.getNodeValue(Config.VERSION);
				this.govUrl = kit.getNodeValue(Config.GOV_URL);
				this.govUser = kit.getNodeValue(Config.GOV_USER);
				this.govPwd = kit.getNodeValue(Config.GOV_PWD);
				this.appLoadUrl = kit.getNodeValue(Config.APP_LOAD_URL);
				this.defaultPwd = kit.getNodeValue(Config.DEFAULT_PWD);
				
				this.appId = kit.getNodeValue(Config.APP_ID);
				this.appKey = kit.getNodeValue(Config.APP_KEY);
				this.master = kit.getNodeValue(Config.MASTER);
				
				this.publishConn = kit.getNodeValue(Config.PUBLISH_CONN);
				this.Debug = kit.getNodeValue(Config.DEBUG, false);
				
				this.zwb_name = kit.getNodeValue(Config.ZWB_NAME);
				this.zsj_name = kit.getNodeValue(Config.ZSJ_NAME);
				
				this.imgUrl = kit.getNodeValue(Config.IMG_URL);
				this.baseUrl = kit.getNodeValue(Config.BASE_URL);
				this.zwb_name_city = kit.getNodeValue(Config.ZWB_NAME_CITY);
				
				this.prin_pwd = kit.getNodeValue(Config.PRIN_PWD);
				this.prin_user = kit.getNodeValue(Config.PRIN_USER);
				this.jdbcPassword = kit.getNodeValue(Config.JDBCPASSWORD);
				this.initialPoolSize = kit.getNodeValue(Config.INITALPOOLSIZE, 5);
				this.maxPoolSize = kit.getNodeValue(Config.MAXPOOLSIZE, 10);
				this.minPoolSize = kit.getNodeValue(Config.MIXPOOLSIZE, 5);
				this.jdbcUrl = kit.getNodeValue(Config.JDBCURL);
			} catch (DocumentException e) {
				e.printStackTrace();
			}
			
		}
	}
	
	/**
	 * 分中心功能
	 */
	public String prin_user;
	public String getPrin_user() {
		return prin_user;
	}

	public void setPrin_user(String prin_user) {
		this.prin_user = prin_user;
	}

	public String getPrin_pwd() {
		return prin_pwd;
	}

	public void setPrin_pwd(String prin_pwd) {
		this.prin_pwd = prin_pwd;
	}

	public String prin_pwd;
	
	/**
	 * 政务办名称
	 */
	public String zwb_name;
	
	public String getZwb_name() {
		return zwb_name;
	}
	
	/**
	 * 市级政务办名称
	 */
	public String zwb_name_city;
	
	
	public String getZwb_name_city() {
		return zwb_name_city;
	}


	/**
	 * 内网地址
	 */
	public String baseUrl;
	
	public String getBaseUrl() {
		return baseUrl;
	}

	/**
	 * 招商局名称
	 */
	public String zsj_name;
	
	public String getZsj_name() {
		return zsj_name;
	}

	/**
	 * 是否为调试模式
	 * */
	private boolean Debug;
	public boolean getDebug() {
		return Debug;
	}
	/**
	 * 公共连接池名称
	 * */
	private String publishConn;
	public String getPublishConn() {
		return publishConn;
	}

	/**
	 * 连接池名称
	 * */
	private String baseConn;
	/**
	 * 当前配置文件版本
	 * */
	private String version;
	
	/**
	 * 审批接口地址
	 */
	public String govUrl;
	
	/**
	 * 审批接口用户
	 */
	public String govUser;
	
	/**
	 * 审批接口用户密码
	 */
	public String govPwd;
	
	/**
	 * 易企办应用下载地址
	 */
	public String appLoadUrl;

	/**
	 * 获取app应用下载地址
	 * @return
	 */
	public String getAppLoadUrl() {
		return appLoadUrl;
	}
	
	/**
	 * 用户账号默认密码
	 */
	public String defaultPwd;
	
	/**
	 * 获取用户账号默认密码
	 * @return
	 */
	public String getDefaultPwd() {
		return this.defaultPwd;
	}
	
	/**
	 * 消息推送客户端唯一Id
	 */
	public String appId;
	
	/**
	 * 鉴定身份密钥
	 */
	public String appKey;
	
	/**
	 * 鉴权码
	 */
	public String master;
	
	/**
	 * 基础配置数据库驱动
	 */
	private String jdbcUrl;
	private String jdbcPassword;
	private int initialPoolSize;
	private int maxPoolSize;
	private int minPoolSize;
	
	public String getAppId() {
		return appId;
	}


	public String getAppKey() {
		return appKey;
	}


	public String getMaster() {
		return master;
	}


	public String getGovUser() {
		return govUser;
	}


	public String getGovPwd() {
		return govPwd;
	}


	public String getGovUrl() {
		return govUrl;
	}

	public String getBaseConn() {
		return baseConn;
	}

	public String getVersion(){
		return version;
	}
	public String getJdbcUrl() {
		return jdbcUrl;
	}


	public String getJdbcPassword() {
		return jdbcPassword;
	}


	public int getInitialPoolSize() {
		return initialPoolSize;
	}


	public int getMaxPoolSize() {
		return maxPoolSize;
	}


	public int getMinPoolSize() {
		return minPoolSize;
	}

	
	/**
	 * 图片保存路径
	 */
	private String imgUrl;
	public String getImagUrl() {
		return imgUrl;
	}
}
