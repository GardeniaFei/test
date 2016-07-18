package cn.com.minstone.eBusiness;

import cn.com.minstone.eBusiness.service.UserService;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;

public class UserInterceptor implements Interceptor {

	public void intercept(Invocation inv) {
		if(new UserService(inv.getController()).hasLogin()){
			inv.invoke();
		}else{
			inv.getController().render("login.html");
		}
	}

}
