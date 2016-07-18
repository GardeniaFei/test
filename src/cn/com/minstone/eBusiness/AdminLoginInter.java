package cn.com.minstone.eBusiness;

import cn.com.minstone.eBusiness.util.Config;
import cn.com.minstone.eBusiness.util.PathUtil;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;

public class AdminLoginInter implements Interceptor{
	
	public void intercept(Invocation inv) {
		String adminCode = Config.getSessionAdminID(inv.getController());
		if(adminCode==null){
			inv.getController().render(PathUtil.getIndexPath("login.html"));
		}else{
			inv.invoke();
			
		}
		
	}

	

}
