package cn.com.minstone.eBusiness.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import cn.com.minstone.eBusiness.util.PathUtil;

import com.jfinal.core.Controller;

public class timeChangeCtrl  extends Controller {
	/**
	 * 毫秒与日期转化类
	 */
	public void show(){
		render(PathUtil.getIndexPath("timeChange.html"));
	}
	
	public void mschange() throws ParseException{
		String oldtime = getPara("oldtime");//添加的时间格式
		String newtime = getPara("newtime");//添加的毫秒数
		
		if(oldtime==null || "".equals(oldtime) || oldtime.length()==0){
			if(newtime!=null){
				long t2 = Long.parseLong(newtime);
				Date d1 = new Date(t2);
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
				oldtime = df.format(d1);
				setAttr("oldtime", oldtime);
				setAttr("newtime",newtime);
			}
		}else{
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	        Date date = format.parse(oldtime);
	
	        Calendar cal = Calendar.getInstance();
	        cal.setTime(date);
	        long s1 = cal.getTimeInMillis();
	        newtime = s1+"";
	        System.out.print("测试时间显示："+newtime);
	        setAttr("newtime",newtime);
	        setAttr("oldtime", oldtime);
		}
		render(PathUtil.getIndexPath("timeChange.html"));
	}

}
