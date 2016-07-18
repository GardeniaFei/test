package cn.com.minstone.eBusiness;

import cn.com.minstone.eBusiness.util.MCubeAppConfig;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
public class ProvinceInter implements Interceptor {

	public void intercept(Invocation inv) {
		String userId = inv.getController().getPara("eb_user");
		String pwd = inv.getController().getPara("eb_pwd");
		if(userId == null || userId.equals("") || userId.equalsIgnoreCase("null") || 
				"undefined".equalsIgnoreCase(userId.trim())) {
			inv.getController().renderText("{'success':false, 'errorMsg':'请传入登录用户！', 'result':null}");
		}else if(pwd == null || pwd.equals("") || pwd.equalsIgnoreCase("null") || 
				"undefined".equalsIgnoreCase(userId.trim())){
			inv.getController().renderText("{'success':false, 'errorMsg':'请传入登录密码！', 'result':null}");
		}else if(hasLogin(userId,pwd)){
			inv.invoke();
		}else{
			inv.getController().renderText("{'success':false, 'errorMsg':'用户名或密码错误！', 'result':null}");
		}
	}
	/**
	 * 判断网分中心登录权限
	 * @param userId
	 * @param pwd
	 * @return
	 */
	public boolean hasLogin(String userId,String pwd){
		boolean result = false;
		if(userId.equals(MCubeAppConfig.getInstance().getPrin_user())){
			if(pwd.equals(MCubeAppConfig.getInstance().getPrin_pwd())){
				result = true;
			}          
		}
		return result;
	}

}
