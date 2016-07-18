package cn.com.minstone.eBusiness.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * 时间日期处理工具类
 * @author gongb
 *
 */
public class TimeUtil {
	
	public static void main(String[] args) {
//		System.out.println(dateToLong("2015-09-17", "yyyy-MM-dd"));
		String str = "aaaa,bbbbb,";
		
		if (str.substring(str.lastIndexOf(",") + 1).equals("")) {
			System.out.println("fffffffffffffffffff");
		}
		
		System.out.println(str.substring(0, str.lastIndexOf(",") - 1));
		
	}
	
	/**
	 * 日期格式转毫秒格式
	 * @param dateStr  日期
	 * @param dateFormat "yy-MM-dd"
	 * @return 返回毫秒数
	 */
	public static long dateToLong(String dateStr, String dateFormat) {
		SimpleDateFormat format = new SimpleDateFormat(dateFormat);
		long dateLong = 0;
		try {
			Date date = format.parse(dateStr);
			
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			dateLong = cal.getTimeInMillis();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return dateLong;
	}
	
	/**
	 * 获取日期
	 * @param time
	 * @param dateFormat
	 * @return
	 */
	public static java.sql.Date getSqlDate(Date time, String dateFormat) {
		SimpleDateFormat formate = new SimpleDateFormat(dateFormat);
		String dateStr = formate.format(time);
		java.sql.Date date = null;
		try {
			Date formDate = formate.parse(dateStr);
			
			date = new java.sql.Date(formDate.getTime());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return date;
	}
	
	/**
	 * 随机数
	 * @return
	 * @throws Exception
	 */
	public static String getDateRandomSeq() throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
		Date sysDate = new Date(System.currentTimeMillis());
		java.util.Random ran = new java.util.Random();
		String smsSeq = (sdf.format(sysDate) + java.lang.Math.abs(ran
				.nextLong())).substring(0, 16);
		return smsSeq;
	}
	
	/**
	 * 获得当前时间的yyyyMMddHHmmss格式的日期字符串
	 * @return
	 */
	public static String getCurrentTime() {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		Date date = new Date(System.currentTimeMillis());
		String strDate = format.format(date);
		LogUtil.log("TimeUtil:getCurrentTime(); singDate=：" + strDate);
		return strDate;
	}
	
	/**
	 * 根据时间字符串获取格式化后的日期字符串
	 * style: <yyyy-MM-dd HH:mm>
	 * @param strTime 
	 * @return String
	 */
	public static String getTimeStyle(String strTime, String strFormat) {
		if(strTime == null || "".equals(strTime) || "null".equalsIgnoreCase(strTime)) {
			return "";
		}
		
		Date date = new Date(Long.parseLong(strTime));
		SimpleDateFormat sdf = new SimpleDateFormat(strFormat);
		String strDate = sdf.format(date);
		
		return strDate;
	}
	
	/**
	 * 根据时间字符串获取格式化后的日期字符串
	 * style: <yyyy-MM-dd HH:mm>
	 * @param strTime
	 * @return String
	 */
	public static String changeTimeStyle(String strTime) {
		if(strTime == null || "".equals(strTime) || "null".equalsIgnoreCase(strTime)) {
			return "";
		}
//		return strTime;
		
		Date date = new Date(Long.parseLong(strTime));
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String strDate = sdf.format(date);
		
		return strDate;
	}

	/**
	 * 根据日期字符串形式的日期获取两日期相差的工作日天数
	 * @param start
	 * @param end
	 * @param format
	 * @return
	 */
	public static long calcWorkDaysByString(String start, String end, String format) {
//		SimpleDateFormat sdf = new SimpleDateFormat(format);
		Date sdate;
		Date edate;
		try {
			sdate = new Date(Long.parseLong(start));
			edate = new Date(Long.parseLong(end));
			return calcWorkDaysByDate(sdate, edate);
		} catch (Exception e) {
			e.printStackTrace();
			return new Long(0);
		}
	}
	
	/**
	 * 获取相差工作日天数
	 * @param start
	 * @param end
	 * @return
	 */
	public static long calcWorkDaysByDate(Date start, Date end) {
		return calcWorkDaysByLong(start.getTime(), end.getTime());
	}
	
	/**
	 * 获取相差工作日天数
	 * @param start
	 * @param end
	 * @return
	 */
	public static long calcWorkDaysByLong(Long start, Long end) {
		return getWorkDaysBylong(start.longValue(), end.longValue());
	}
	
	/**
	 * 获取相差工作日天数
	 * @param start
	 * @param end
	 * @return
	 */
	public static long getWorkDaysBylong(long start, long end) {
		return cacularWorkDays(start, end);
	}
	
	/**
	 * 根据日期的long数据计算两个日期相差的工作日天数
	 * @param start
	 * @param end
	 * @return
	 */
	public static long cacularWorkDays(long start, long end) {
		if (start > end) {
			long temp = start;
			start = end;
			end = temp;
		}

		Calendar startDate = Calendar.getInstance();
		Calendar endDate = Calendar.getInstance();
		startDate.setTimeInMillis(start);
		endDate.setTimeInMillis(end);

//		System.out.println("开始时间：" + startDate.get(Calendar.DAY_OF_WEEK)
//				+ ", 结束时间：" + endDate.get(Calendar.DAY_OF_WEEK));

		// 如果两个时间在同一周并且都不是周末日期，则直接返回时间差，增加执行效率
		if (startDate.get(Calendar.YEAR) == endDate.get(Calendar.YEAR)
				&& startDate.get(Calendar.WEEK_OF_YEAR) == endDate
						.get(Calendar.WEEK_OF_YEAR)
				&& startDate.get(Calendar.DAY_OF_WEEK) != 1
				&& startDate.get(Calendar.DAY_OF_WEEK) != 7
				&& endDate.get(Calendar.DAY_OF_WEEK) != 1
				&& endDate.get(Calendar.DAY_OF_WEEK) != 7) {

			return new Long(end - start);
		}

		//以下两句代码放在此处是防止调用getNextMonday函数后startDate和endDate的值发生变化
		int dayStart = startDate.get(Calendar.DAY_OF_WEEK);
		int dayEnd = endDate.get(Calendar.DAY_OF_WEEK);

		// 首先取得起始日期与结束日期的下个周一的日期
		Calendar snextM = getNextMonday(startDate);
		Calendar enextM = getNextMonday(endDate);

		// 获取这两个周一之间的实际天数
		int days = getDaysBetween(snextM, enextM);

		// 获取这两个周一之间的工作日数
		int workDays = days / 7 * 5;

		int offDays = 0;
		//开始时间的偏移量为当前日期到本周六的时间差
		if ((dayStart != 1) && (dayStart != 7)) {
			dayStart = 6 - dayStart;
		}else {
			dayStart = 0;
		} 

		//结束时间的偏移量为当前日期到本周六的时间差
		if ((dayEnd != 1) && (dayEnd != 7)) {
			dayEnd = 6 - dayEnd;
		}else {
			dayEnd = 0;
		}
		
		offDays = dayStart - dayEnd;
		
		int deday = (workDays + offDays);
		long delay = deday * 24 * Long.parseLong(3600000 + "");
		
//		System.out.println("偏移量毫秒值：" + 48 * 24 * Long.parseLong(3600000 + ""));

		// 计算最终结果，具体为：workdays加上开始时间的时间偏移量，减去结束时间的时间偏移量
		return delay;
	}

	/**
	 * 获得日期的下一个周一的日期
	 * 
	 * @param cal
	 * @return
	 */
	private static Calendar getNextMonday(Calendar cal) {
		int dayNum = cal.get(Calendar.DAY_OF_WEEK);
		int addnum = 9 - dayNum;

		if (addnum == 8) {
			addnum = 1; // 周日的情况
		}

		cal.add(Calendar.DATE, addnum);

		return cal;
	}

	/**
	 * 获取两个日期之间的实际天数，支持跨年
	 * 
	 * @param start
	 *            <Calendar>
	 * @param end
	 *            <Calendar>
	 * @return int
	 */
	public static int getDaysBetween(Calendar start, Calendar end) {
		if (start.after(end)) {
			Calendar swap = start;
			start = end;
			end = swap;
		}

		int days = end.get(Calendar.DAY_OF_YEAR)
				- start.get(Calendar.DAY_OF_YEAR);
		int y2 = end.get(Calendar.YEAR);
		if (start.get(Calendar.YEAR) != y2) {
			start = (Calendar) start.clone();
			do {
				days += start.getActualMaximum(Calendar.DAY_OF_YEAR);
				start.add(Calendar.YEAR, 1);
			} while (start.get(Calendar.YEAR) != y2);
		}

		return days;
	}
	
	/**
	 * 获得当前日期与本周一相差的天数
	 * @return int
	 */
	private static int getMondayPlus() {
        Calendar cd = Calendar.getInstance();
        // 获得今天是一周的第几天，星期日是第一天，星期二是第二天......
        int dayOfWeek = cd.get(Calendar.DAY_OF_WEEK);
        if (dayOfWeek == 1) {
            return -6;
        } else {
            return 2 - dayOfWeek;
        }
    } 
    
    /**
     * 获得当前周- 周一的日期
     * @return String
     */
    public static String getCurrentMonday() {
        int mondayPlus = getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus);
        Date monday = currentDate.getTime();
        DateFormat df = DateFormat.getDateInstance();
        String preMonday = df.format(monday);
        return preMonday;
    }
    
    /**
     * 获得当前周- 周一的日期毫秒数
     * @return long
     * @throws ParseException 
     */
    public static long getCurrentMondayTime() throws ParseException {
        int mondayPlus = getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus);
        Date monday = currentDate.getTime();
//        long mondayTime = monday.getTime();
        
        //先去当前周一0点0分0秒
        long t2 = monday.getTime();
		Date d1 = new Date(t2);
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
		String oldtime = df.format(d1);
		
		//z再转成毫秒数
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date date = format.parse(oldtime);

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        long mondayTime = cal.getTimeInMillis();
		System.out.print("测试周一起始时间"+mondayTime+"  "+date);
        return mondayTime;
    }

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    /**
     * 获得当前月--开始日期
     * @return String
     */
    public static String getMinMonthDate(String date) {   
             Calendar calendar = Calendar.getInstance();   
              try {
                 calendar.setTime(dateFormat.parse(date));
                 calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH)); 
                 return dateFormat.format(calendar.getTime());
               } catch (java.text.ParseException e) {
               e.printStackTrace();
              }
            return null;
    }
    
    /**
     * 获得当前月--开始日期
     * @return long
     */
    public static long getMinMonthDateTime() {   
             Calendar calendar = Calendar.getInstance();   
              calendar.setTime(new Date());
			 calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH)); 
			 return calendar.getTime().getTime();
    }
    
    /**
     * 获得当前月--结束日期
     * @return String
     */
    public static String getMaxMonthDate(String date){   
         Calendar calendar = Calendar.getInstance();   
         try {
                calendar.setTime(dateFormat.parse(date));
                calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
                return dateFormat.format(calendar.getTime());
         }  catch (java.text.ParseException e) {
                e.printStackTrace();
          }
        return null;
    }
    
    /**
     * 获得当前年--开始日期
     * @return long
     */
    public static long getMinYearDateTime(String date) {   
    	Calendar calendar = Calendar.getInstance();   
        try {
           calendar.setTime(dateFormat.parse(date));
           calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH)); 
           return calendar.getTime().getTime();
         } catch (java.text.ParseException e) {
         e.printStackTrace();
        }
      return 0;
    }
}
