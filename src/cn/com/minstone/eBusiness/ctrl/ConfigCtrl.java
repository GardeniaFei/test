package cn.com.minstone.eBusiness.ctrl;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.dom4j.DocumentException;

import cn.com.minstone.eBusiness.MainConfig;
import cn.com.minstone.eBusiness.util.MCubeAppConfig;
import cn.com.minstone.eBusiness.util.PushUtil;
import cn.com.minstone.eBusiness.valid.ConfigValidator;
import cn.com.minstone.jfinal.ext.AppVersion;
import cn.com.minstone.jfinal.ext.SystemConfig;
import cn.com.minstone.jfinal.ext.util.ConfigKit.OnValidListener;
import cn.com.minstone.jfinal.ext.util.VersionKit;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.DbKit;

/**
 * 系统初始化配置
 * */
public class ConfigCtrl extends Controller {
	

	public void index(){
		AppVersion dbVersion = null;
		
		if(DbKit.getConfig() != null){
			VersionKit kit = new VersionKit(DbKit.getConfig().getDataSource());
			dbVersion = kit.getDbVersion();
		}
		
		AppVersion xmlVersion = VersionKit.getXMLAppVersion();
		
		if(xmlVersion.getAppVersion().equals(MCubeAppConfig.getInstance().getVersion())){
			//配置文件中的版本信息与当前系统版本信息一直，表示配置文件中的配置已被配置
			renderText("当前配置已更新，无法再修改配置。");
			return;
		}
		
		List<SystemConfig> xmlConfigs = null;
		List<SystemConfig> toConfigs = MainConfig.configKit.getConfigs();
		try {
			xmlConfigs = MainConfig.configKit.loadXMLConfig();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		
		if(xmlConfigs != null){
			overrideToConfig(xmlConfigs, toConfigs);
		}
		
		setAttr("configs", toConfigs);
		setAttr("currentVersion", dbVersion);
		setAttr("newVersion", xmlVersion);
		
		render("/WEB-INF/config/index.html");
	}
	
	@Before(ConfigValidator.class)
	public void save(){
		
		AppVersion xmlVersion = VersionKit.getXMLAppVersion();
		if(xmlVersion.getAppVersion().equals(MCubeAppConfig.getInstance().getVersion())){
			//配置文件中的版本信息与当前系统版本信息一直，表示配置文件中的配置已被配置
			renderText("当前配置已更新，无法再修改配置。");
			return;
		}
		
		MainConfig.configKit.setOnValidListener(new OnValidListener() {
			
			public boolean onValid(List<SystemConfig> configs) {
				boolean success = true;
				for(SystemConfig c: configs){
					if(c.getConfigKey().equals("baseConn")){
						try {
							Context ctx = new InitialContext();
							ctx.lookup(c.getConfigValue());
							success = success && true;
						} catch (NamingException e) {
							e.printStackTrace();
							success = success && false;
						}finally{
							if(!success){
								setAttr("message", "数据源加载失败，请确认数据源名称是否正确！");
							}
						}
					}
				}
				return success;
			}
		});
		
		List<SystemConfig> configs = MainConfig.configKit.getConfigs();
		for(SystemConfig c:configs){
			c.setConfigValue(getPara(c.getConfigKey()));
		}
		boolean success = false;
		try {
			success = MainConfig.configKit.updateConfig(configs);
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			setAttr("success", success);
			if(success){
				setAttr("message", "更新配置成功！请重新启动该应用，完成系统初始化！");
			}

//			final List<String> strAlias = new ArrayList<String>();
//			strAlias.add("zhengwuban");
//			final String content = StringUtil.getJson("1", "政务办别名消息推送测试111");
//			PushUtil.pushNotifyToTagert(strAlias, content);
			
			render("/WEB-INF/config/message.html");
		}
	}
	
	private void overrideToConfig(List<SystemConfig> fromConfig, List<SystemConfig> toConfig){
		for(int i = 0 ; i < fromConfig.size(); i++){
			SystemConfig c = fromConfig.get(i);
			for(SystemConfig toC:toConfig){
				if(c.getConfigKey().equals(toC.getConfigKey())){
					toC.setOldValue(c.getConfigValue());
					if(c.getChildren() != null){
						if(toC.getChildren() == null){
							toC.setChildren(new ArrayList<SystemConfig>());
						}
						overrideToConfig(c.getChildren(), toC.getChildren());
					}
				}
			}
		}
	}
	
}
