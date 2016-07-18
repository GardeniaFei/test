package cn.com.minstone.eBusiness.valid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.dom4j.DocumentException;


import cn.com.minstone.eBusiness.MainConfig;
import cn.com.minstone.eBusiness.util.MCubeAppConfig;
import cn.com.minstone.jfinal.ext.AppVersion;
import cn.com.minstone.jfinal.ext.SystemConfig;
import cn.com.minstone.jfinal.ext.util.VersionKit;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.DbKit;
import com.jfinal.validate.Validator;

public class ConfigValidator extends Validator{

	@Override
	protected void validate(Controller c) {
		AppVersion xmlVersion = VersionKit.getXMLAppVersion();
		if(xmlVersion.getAppVersion().equals(MCubeAppConfig.getInstance().getVersion())){
			//配置文件中的版本信息与当前系统版本信息一直，表示配置文件中的配置已被配置
			addError("message", "当前配置已更新，无法再修改配置。");
		}

		Map<String, SystemConfig> map = new HashMap<String, SystemConfig>();
		List<SystemConfig> configs = MainConfig.configKit.getConfigs();
		for(SystemConfig config:configs){
			config.setConfigValue(c.getPara(config.getConfigKey()));
			
			if(config.getIsMust()){
				validateRequired(config.getConfigKey(), "message", "请输入内容：[" + config.getConfigName() + "]");
			}
			if(config.getIsMust() && config.getConfigValue().trim().length()!= 0 && config.getValidRex() != null){
				validateRegex(config.getConfigKey(), config.getValidRex(), "message", "[" + config.getConfigName() + "] 的输入格式有误:[" + config.getConfigValue() + "]");
			}
			
			map.put(config.getConfigKey(), config);
			
		}
		
		try {
			Context ctx = new InitialContext();
			ctx.lookup(map.get("baseConn").getConfigValue());
			
		} catch (NamingException e) {
			e.printStackTrace();
			addError("message", "无法加载连接池，请检查连接池名称是否正确，或数据库是否可以正常连接！");
		}
		
		//TODO:继续校验其他内容

	}

	@Override
	protected void handleError(Controller c) {
		AppVersion dbVersion = null;
		
		if(DbKit.getConfig() != null){
			VersionKit kit = new VersionKit(DbKit.getConfig().getDataSource());
			dbVersion = kit.getDbVersion();
		}		
		AppVersion xmlVersion = VersionKit.getXMLAppVersion();

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
		
		c.setAttr("configs", toConfigs);
		c.setAttr("currentVersion", dbVersion);
		c.setAttr("newVersion", xmlVersion);
		c.render("/WEB-INF/config/index.html");
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
