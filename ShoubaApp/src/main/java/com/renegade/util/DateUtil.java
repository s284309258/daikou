package com.renegade.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

	/**
	 * 日期格式：yyyyMMddHHmmss
	 */
	private static final SimpleDateFormat format1 = new SimpleDateFormat("yyyyMMddHHmmss");
	
	/**
	 * 日期格式：yyyy-MM-dd HH:mm:ss
	 */
	private static final SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	/**
	 * 日期格式：yyyyMMdd
	 */
	private static final SimpleDateFormat format3 = new SimpleDateFormat("yyyyMMdd");
	
	/**
	 * 日期格式：yyyy-MM-dd
	 */
	private static final SimpleDateFormat format4 = new SimpleDateFormat("yyyy-MM-dd");
	
	/**
	 * 日期格式：HHmmss
	 */
	private static final SimpleDateFormat format5 = new SimpleDateFormat("HHmmss");
	
	/**
	 * 日期格式：HH:mm:ss
	 */
	private static final SimpleDateFormat format6 = new SimpleDateFormat("HH:mm:ss");
	
	/**
	 * 获取当前日期
	 * 格式为：yyyyMMddHHmmss
	 * @return
	 */
	public static String get_format1_today(){
		return format1.format(new Date());
	}
	
	/**
	 * 获取前一天日期 yyyyMMdd230000
	 * @return
	 */
	public static String get_format1_yesterday(){
		Calendar cal0 = Calendar.getInstance();
    	cal0.setTime(new Date());
    	cal0.add(Calendar.DAY_OF_YEAR, -1);
    	cal0.set(Calendar.HOUR_OF_DAY, 23);
    	cal0.set(Calendar.MINUTE, 0);
    	cal0.set(Calendar.SECOND, 0);
    	return format1.format(cal0.getTime());
	}
	
	/**
	 * 获取前两天日期 yyyyMMdd230000
	 * @return
	 */
	public static String get_format1_yesTwoDay(){
		Calendar cal0 = Calendar.getInstance();
		cal0.setTime(new Date());
    	cal0.add(Calendar.DAY_OF_YEAR, -2);
    	cal0.set(Calendar.HOUR_OF_DAY, 23);
    	cal0.set(Calendar.MINUTE, 0);
    	cal0.set(Calendar.SECOND, 0);
    	return format1.format(cal0.getTime());
	}
	
	/**
	 * 获取前一天日期 yyyyMMdd000000
	 * @return
	 */
	public static String get_format1_yesterday00(){
		Calendar cal0 = Calendar.getInstance();
    	cal0.setTime(new Date());
    	cal0.add(Calendar.DAY_OF_YEAR, -1);
    	cal0.set(Calendar.HOUR_OF_DAY, 0);
    	cal0.set(Calendar.MINUTE, 0);
    	cal0.set(Calendar.SECOND, 0);
    	return format1.format(cal0.getTime());
	}
	
	/**
	 * 获取前一天日期 yyyyMMdd235959
	 * @return
	 */
	public static String get_format1_yesterday24(){
		Calendar cal0 = Calendar.getInstance();
    	cal0.setTime(new Date());
    	cal0.add(Calendar.DAY_OF_YEAR, -1);
    	cal0.set(Calendar.HOUR_OF_DAY, 23);
    	cal0.set(Calendar.MINUTE, 59);
    	cal0.set(Calendar.SECOND, 59);
    	return format1.format(cal0.getTime());
	}
	
	/**
	 * 获取当前日期
	 * 格式为：yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static String get_format2_today(){
		return format2.format(new Date());
	}
	
	/**
	 * 获取当前日期
	 * 格式为：yyyyMMdd
	 * @return
	 */
	public static String get_format3_today(){
		return format3.format(new Date());
	}
	
	/**
	 * 获取当前日期
	 * 格式为：yyyy-MM-dd
	 * @return
	 */
	public static String get_format4_today(){
		return format4.format(new Date());
	}
	
	/**
	 * 获取当前日期
	 * 格式为：HHmmss
	 * @return
	 */
	public static String get_format5_today(){
		return format5.format(new Date());
	}
	
	/**
	 * 获取当前日期
	 * 格式为：HH:mm:ss
	 * @return
	 */
	public static String get_format6_today(){
		return format6.format(new Date());
	}
	
	public static String cutDateStr(String time){
		Calendar cal0 = Calendar.getInstance();
    	try {
			cal0.setTime(format1.parse(time));
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
    	cal0.set(Calendar.HOUR_OF_DAY, 23);
    	cal0.set(Calendar.MINUTE, 0);
    	cal0.set(Calendar.SECOND, 0);
    	return format1.format(cal0.getTime());
	}
	
	/**
	 * 将时间前推10分钟
	 * @param startTime
	 * @return
	 */
	public static String beforeTenMin(String startTime){
		Calendar cal0 = Calendar.getInstance();
		try {
			cal0.setTime(format1.parse(startTime));
			cal0.add(Calendar.MINUTE, -10);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
		return format1.format(cal0.getTime());
	}
	
	/**
	 * 获取前一个月的第一个天
	 * @return
	 */
	public static String getBeforeMonthFirstDay(){
		Calendar cal_1=Calendar.getInstance();//获取当前日期 
		cal_1.add(Calendar.MONTH, -1);
		cal_1.set(Calendar.DAY_OF_MONTH,1);//设置为1号,当前日期既为本月第一天 
		return format3.format(cal_1.getTime());
	}
	
	/**
	 * 获取前一个月的最后一天
	 * @return
	 */
	public static String getBeforeMonthLastDay(){
		Calendar cal_1=Calendar.getInstance();//获取当前日期 
		cal_1.set(Calendar.DAY_OF_MONTH,0);//设置为1号,当前日期既为本月第一天 
		return format3.format(cal_1.getTime());
	}
	
	/**
	 * 获取当月的第一天
	 * @return
	 */
	public static String getMonthFirstDay(){
		Calendar cal_1=Calendar.getInstance();//获取当前日期 
		cal_1.set(Calendar.DAY_OF_MONTH,1);//设置为1号,当前日期既为本月第一天 
		return format3.format(cal_1.getTime());
	}
	
	/**
	 * 获取当月的最后一天
	 * @return
	 */
	public static String getMonthLastDay(){
		Calendar cal_1=Calendar.getInstance();//获取当前日期 
		cal_1.set(Calendar.DAY_OF_MONTH, cal_1.getActualMaximum(Calendar.DAY_OF_MONTH));//设置为1号,当前日期既为本月第一天 
		return format3.format(cal_1.getTime());
	}
	
	public static void main(String args[]){
		System.out.println(get_format1_yesterday00());
		System.out.println(get_format1_yesterday24());
	}
	
}
