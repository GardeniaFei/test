package cn.com.minstone.eBusiness.ctrl.inter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import cn.com.minstone.eBusiness.MobileInter;
import cn.com.minstone.eBusiness.model.EbTaskDistribute;
import cn.com.minstone.eBusiness.service.inter.TaskInterService;
import cn.com.minstone.eBusiness.util.TimeUtil;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.render.JsonRender;

public class StatCtrl extends Controller {

	/**
	 * 统计办件
	 */
	public void statItem(){
		int type = this.getParaToInt("type");// 办件来源：1区级，0市级
		//根据事项来源查询所有分发任务表(0总数,1正在办理，2办理超时，3已办结)、时间月份
		//获取当前年字符串
		SimpleDateFormat fort = new SimpleDateFormat("yyyy");
		String yearStr = fort.format(new Date());
		//全部数量
		Hashtable<String, Object> result = new Hashtable<String, Object>();
		long start_time = TimeUtil.dateToLong(yearStr+"-01-01", "yyyy-MM-dd");
		long end_time = TimeUtil.dateToLong(yearStr+"-12-31", "yyyy-MM-dd");
		int inprocessData = numbs(type,1,start_time,end_time);//正在办理总数
		result.put("inprocessData", inprocessData);
		
		int processedData = numbs(type,3,start_time,end_time);//已办结总数
		result.put("processedData", processedData);
		
		int overTimeData = numbs(type,2,start_time,end_time);//超时总数
		result.put("overTimeData", overTimeData);
		
		//1月
		long start_time_1 = TimeUtil.dateToLong(yearStr+"-01-01", "yyyy-MM-dd");
		long end_time_1 = TimeUtil.dateToLong(yearStr+"-01-31", "yyyy-MM-dd");
		Hashtable<String, String> januaryData = new Hashtable<String, String>();
		int janTotal = numbs(type,0,start_time_1,end_time_1);//当月总数
		januaryData.put("total", janTotal+"");
//		int inprocessData_1 = numbs(type,1,start_time_1,end_time_1);//当月正在办理
//		januaryData.put("inprocessData", inprocessData_1+"");
//		int overTimeData_1 = numbs(type,3,start_time_1,end_time_1);//当月办理超时
//		januaryData.put("overTimeData", overTimeData_1+"");
//		int processedData_1 = numbs(type,2,start_time_1,end_time_1);//当月办理完结
//		januaryData.put("processedData", processedData_1+"");
		result.put("januaryData", januaryData);
		
		//2月
		long start_time_2 = TimeUtil.dateToLong(yearStr+"-02-01", "yyyy-MM-dd");
		long end_time_2 = TimeUtil.dateToLong(yearStr+"-02-31", "yyyy-MM-dd");
		Hashtable<String, String> februaryData = new Hashtable<String, String>();
		int febTotal = numbs(type,0,start_time_2,end_time_2);//当月总数
		februaryData.put("total", febTotal+"");
//		int inprocessData_2 = numbs(type,1,start_time_2,end_time_2);//当月正在办理
//		januaryData.put("inprocessData", inprocessData_2+"");
//		int overTimeData_2 = numbs(type,3,start_time_2,end_time_2);//当月办理超时
//		januaryData.put("overTimeData", overTimeData_2+"");
//		int processedData_2 = numbs(type,2,start_time_2,end_time_2);//当月办理完结
//		januaryData.put("processedData", processedData_2+"");
		result.put("februaryData", februaryData);
		
		//3月
		long start_time_3 = TimeUtil.dateToLong(yearStr+"-03-01", "yyyy-MM-dd");
		long end_time_3 = TimeUtil.dateToLong(yearStr+"-03-31", "yyyy-MM-dd");
		Hashtable<String, String> marchData = new Hashtable<String, String>();
		int marTotal = numbs(type,0,start_time_3,end_time_3);//当月总数
		marchData.put("total", marTotal+"");
//		int inprocessData_3 = numbs(type,1,start_time_3,end_time_3);//当月正在办理
//		januaryData.put("inprocessData", inprocessData_3+"");
//		int overTimeData_3 = numbs(type,3,start_time_3,end_time_3);//当月办理超时
//		januaryData.put("overTimeData", overTimeData_3+"");
//		int processedData_3 = numbs(type,2,start_time_3,end_time_3);//当月办理完结
//		januaryData.put("processedData", processedData_3+"");
		result.put("marchData", marchData);
		
		//4月
		long start_time_4 = TimeUtil.dateToLong(yearStr+"-04-01", "yyyy-MM-dd");
		long end_time_4 = TimeUtil.dateToLong(yearStr+"-04-31", "yyyy-MM-dd");
		Hashtable<String, String> aprilData = new Hashtable<String, String>();
		int aprTotal = numbs(type,0,start_time_4,end_time_4);//当月总数
		aprilData.put("total", aprTotal+"");
//		int inprocessData_4 = numbs(type,1,start_time_4,end_time_4);//当月正在办理
//		januaryData.put("inprocessData", inprocessData_4+"");
//		int overTimeData_4 = numbs(type,3,start_time_4,end_time_4);//当月办理超时
//		januaryData.put("overTimeData", overTimeData_4+"");
//		int processedData_4 = numbs(type,2,start_time_4,end_time_4);//当月办理完结
//		januaryData.put("processedData", processedData_4+"");
		result.put("aprilData", aprilData);
		
		//5月
		long start_time_5 = TimeUtil.dateToLong(yearStr+"-05-01", "yyyy-MM-dd");
		long end_time_5 = TimeUtil.dateToLong(yearStr+"-05-31", "yyyy-MM-dd");
		Hashtable<String, String> monthData = new Hashtable<String, String>();
		int monTotal = numbs(type,0,start_time_5,end_time_5);//当月总数
		monthData.put("total", monTotal+"");
//		int inprocessData_5 = numbs(type,1,start_time_5,end_time_5);//当月正在办理
//		januaryData.put("inprocessData", inprocessData_5+"");
//		int overTimeData_5 = numbs(type,3,start_time_5,end_time_5);//当月办理超时
//		januaryData.put("overTimeData", overTimeData_5+"");
//		int processedData_5 = numbs(type,2,start_time_5,end_time_5);//当月办理完结
//		januaryData.put("processedData", processedData_5+"");
		result.put("monthData", monthData);
		
		//6月
		long start_time_6 = TimeUtil.dateToLong(yearStr+"-06-01", "yyyy-MM-dd");
		long end_time_6 = TimeUtil.dateToLong(yearStr+"-06-31", "yyyy-MM-dd");
		Hashtable<String, String> juneData = new Hashtable<String, String>();
		int juneTotal = numbs(type,0,start_time_6,end_time_6);//当月总数
		juneData.put("total", juneTotal+"");
		result.put("juneData", juneData);
		
		//7月
		long start_time_7 = TimeUtil.dateToLong(yearStr+"-07-01", "yyyy-MM-dd");
		long end_time_7 = TimeUtil.dateToLong(yearStr+"-07-31", "yyyy-MM-dd");
		Hashtable<String, String> julyData = new Hashtable<String, String>();
		int julyTotal = numbs(type,0,start_time_7,end_time_7);//当月总数
		julyData.put("total", julyTotal+"");
		result.put("julyData", julyData);
		
		//8月
		long start_time_8 = TimeUtil.dateToLong(yearStr+"-08-01", "yyyy-MM-dd");
		long end_time_8 = TimeUtil.dateToLong(yearStr+"-08-31", "yyyy-MM-dd");
		Hashtable<String, String> augustData = new Hashtable<String, String>();
		int augTotal = numbs(type,0,start_time_8,end_time_8);//当月总数
		augustData.put("total", augTotal+"");
		result.put("augustData", augustData);
		
		//9月
		long start_time_9 = TimeUtil.dateToLong(yearStr+"-09-01", "yyyy-MM-dd");
		long end_time_9 = TimeUtil.dateToLong(yearStr+"-09-31", "yyyy-MM-dd");
		Hashtable<String, String> septemberData = new Hashtable<String, String>();
		int sepTotal = numbs(type,0,start_time_9,end_time_9);//当月总数
		septemberData.put("total", sepTotal+"");
		result.put("septemberData", septemberData);
		
		//10月
		long start_time_10 = TimeUtil.dateToLong(yearStr+"-10-01", "yyyy-MM-dd");
		long end_time_10 = TimeUtil.dateToLong(yearStr+"-10-31", "yyyy-MM-dd");
		Hashtable<String, String> octoberData = new Hashtable<String, String>();
		int octoTotal = numbs(type,0,start_time_10,end_time_10);//当月总数
		octoberData.put("total", octoTotal+"");
		result.put("octoberData", octoberData);
		
		//11月
		long start_time_11 = TimeUtil.dateToLong(yearStr+"-11-01", "yyyy-MM-dd");
		long end_time_11 = TimeUtil.dateToLong(yearStr+"-11-31", "yyyy-MM-dd");
		Hashtable<String, String> novermberData = new Hashtable<String, String>();
		int noveTotal = numbs(type,0,start_time_11,end_time_11);//当月总数
		novermberData.put("total", noveTotal+"");
		result.put("novermberData", novermberData);
		
		//12月
		long start_time_12 = TimeUtil.dateToLong(yearStr+"-12-01", "yyyy-MM-dd");
		long end_time_12 = TimeUtil.dateToLong(yearStr+"-12-31", "yyyy-MM-dd");
		Hashtable<String, String> decemberData = new Hashtable<String, String>();
		int decTotal = numbs(type,0,start_time_12,end_time_12);//当月总数
		decemberData.put("total", decTotal+"");
		result.put("decemberData", decemberData);
		
		setAttr("result", result);
		setAttr("success", true);
		setAttr("errorMsg", "");
		render(new JsonRender().forIE());
	}
	
	/**
	 * 计算不同类型的数量
	 * @param type 办件来源：1区级，0市级
	 * @param statu  0总数，1正在办理，2办理超时，3已办结，
	 * @param start_time 时间月份为一整年2016-1-1
	 * @param end_time时间月份为一整年2016-12-31
	 * @return
	 */
	public int numbs(int type,int statu,long start_time,long end_time){
		int numbs = 0;
		TaskInterService taskSer = new TaskInterService();
		List<EbTaskDistribute> distrs = taskSer.getDistrsByTST(type, statu, start_time, end_time) ;
		if(distrs.size()>0){
			numbs = distrs.size();
		}
		return numbs;
	}
}
