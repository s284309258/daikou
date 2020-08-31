package com.renegade.util.common;

import java.text.SimpleDateFormat;
import java.util.Date;


public class CornUtil {

	/**
	 * @param date
	 * @param dateFormat
	 * @return
	 */
	public static String formatDateByPattern(Date date,String dateFormat){  
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);  
        String formatTimeStr = null;  
        if (date != null) {  
            formatTimeStr = sdf.format(date);  
        }  
        return formatTimeStr;  
    } 
	
	
	/**
	 * 功能描述：日期转corn表达式
	 * @param date
	 * @return
	 */
	public static String getCron(Date  date){  
        String dateFormat="ss mm HH dd MM ? yyyy";  
        return formatDateByPattern(date, dateFormat);  
    }  
	
	
	/**
	 * 功能描述：corn表达式转为日期
	 * @param cron
	 * @return
	 */
	public static Date getCronToDate(String cron) {  
		String dateFormat="ss mm HH dd MM ? yyyy";  
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);  
		Date date = null;  
		try {  
			date = sdf.parse(cron);  
	    } catch (Exception e) {  
	    	return null;
	    }  
		return date;  
	}
	
	
	public static void main(String[] args) {
		Date cron=getCronToDate("0 0/1 * * * ?");
        System.out.println(cron);  
	}
}
