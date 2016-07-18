package cn.com.minstone.eBusiness;

import cn.com.minstone.eBusiness.service.UserService;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;

public class MobileInter implements Interceptor {

	public void intercept(Invocation inv) {
		String sid = inv.getController().getPara("sid");
		if(sid == null || sid.equals("") || sid.equalsIgnoreCase("null") || 
				"undefined".equalsIgnoreCase(sid.trim())) {
			inv.getController().renderText("{'success':false, 'errorMsg':'请传入登录sid！', 'result':null}");
		}else if(new UserService(inv.getController()).hasLoginWithSid(sid)){
			inv.invoke();
		}else{
			inv.getController().renderText("{'success':false, 'errorMsg':'未登录！', 'result':null}");
		}
	}

}
