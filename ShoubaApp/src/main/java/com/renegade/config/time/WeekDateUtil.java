package com.renegade.config.time;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.LocalDateTime;

import com.renegade.util.common.CollectionUtil;

/**
 * 获取指定几个星期的周一、周日的工具类（可用于按周查询数据）
 * @author fcy
 *
 */
public class WeekDateUtil {
 
    public static List<Map<String, LocalDateTime>> getDate(Integer num, String dateTimeString){
         
        List<Map<String, LocalDateTime>> dateList = CollectionUtil.newListInstance();
         
        LocalDateTime mon = null;//本周一日期
        LocalDateTime sun = null;//本周日日期
        
        String pattern = "(\\d{4})(\\d{2})(\\d{2})";
		String timeType="$1-$2-$3";
        //当前时间
        //LocalDateTime dateTime = new LocalDateTime();  
        LocalDateTime dateTime = LocalDateTime.parse(dateTimeString.replaceFirst(pattern, timeType));
        
        int week = dateTime.getDayOfWeek(); //获取今天是星期几  
        //判断今天是周几，计算出周一和周日的日期
        switch (week) {
        case 1:
            mon = dateTime;
            sun = dateTime.plusDays(6);         
            break;
        case 2:
            mon = dateTime.minusDays(1);
            sun = dateTime.plusDays(5);
            break;
        case 3:
            mon = dateTime.minusDays(2);
            sun = dateTime.plusDays(4);
            break;
        case 4:
            mon = dateTime.minusDays(3);
            sun = dateTime.plusDays(3);
            break;
        case 5:
            mon = dateTime.minusDays(4);
            sun = dateTime.plusDays(2);
            break;
        case 6:
            mon = dateTime.minusDays(5);
            sun = dateTime.plusDays(1);
            break;
        case 7:
            mon = dateTime.minusDays(6);
            sun = dateTime;
            break;
        default:
            break;
        }
         
        int date = 0;
        //根据获取指定的前几周的周一和周日的日期（包括本周）
        for (int i = 0; i < num; i++) {  
            Map<String, LocalDateTime> map = CollectionUtil.newMapInstance();
            map.put("Mon",mon.minusDays(date));
            map.put("Sun",sun.minusDays(date));
            dateList.add(map);
            date += 7;
        }
       /* for (int i = 0; i < dateList.size(); i++) {
            System.out.println("周一：" + dateList.get(i).get("Mon").toString("yyyyMMdd"));
            System.out.println("周日：" + dateList.get(i).get("Sun").toString("yyyyMMdd"));
        }*/
        return dateList;
    }
    
    
    /**
     * 从某一个时间点往前推算若干周的时间范围
     * @param num
     * @param endTimeString
     * @param map
     * @return
     */
    public static Map<String, Object> getWeekAreaMap1(Integer num, String endTimeString,Map<String, Object> map){
    	List<Map<String, LocalDateTime>> dateList=getDate(num, "20181025");
    	List<Map<String, Object>> weekList=new ArrayList<>();
    	 for (int i = 0; i < dateList.size(); i++) {
    		 Map<String, Object> oneWeekMap=new HashMap<>();
    		 oneWeekMap.put("Monday", dateList.get(i).get("Mon").toString("yyyyMMdd"));//周一
    		 oneWeekMap.put("Sunday", dateList.get(i).get("Sun").toString("yyyyMMdd"));//周日
    		 weekList.add(oneWeekMap);
         }
    	 map.put("endTime", endTimeString);//最后一天
    	 map.put("beginTime", dateList.get(num-1).get("Mon").toString("yyyyMMdd"));//最开始的一天：往前推的最后一周的周一
    	 map.put("weekList", weekList);//每一周的集合
    	 return map;
    }
    
    
    /**
     * 从某一个时间点往前推算若干周的时间范围，并拼接处理相关数据
     * @param num
     * @param endTimeString
     * @param map
     * @return
     */
    public static Map<String, Object> getWeekAreaMap(Integer num, String endTimeString,Map<String, Object> map){
    	 List<Map<String, LocalDateTime>> dateList=getDate(num, endTimeString);
    	 List<Map<String, Object>> weekList=new ArrayList<>();
    	 //分组的字符串拼接
    	 String groupParam="";
    	 for (int i = 0; i < dateList.size(); i++) {
    		 groupParam = groupParam + "WHEN cre_date BETWEEN '"+dateList.get(i).get("Mon").toString("yyyyMMdd")+"' AND '"+dateList.get(i).get("Sun").toString("yyyyMMdd")+"' THEN '"+dateList.get(i).get("Mon").toString("yyyyMMdd")+"-"+dateList.get(i).get("Sun").toString("yyyyMMdd")+"' ";
    		 Map<String, Object> oneWeekMap=new HashMap<>();
    		 oneWeekMap.put("dateArea", dateList.get(i).get("Mon").toString("yyyyMMdd")+"-"+dateList.get(i).get("Sun").toString("yyyyMMdd"));//时间范围
    		 weekList.add(oneWeekMap);
    	 }
    	 map.put("endTime", endTimeString);//最后一天
    	 map.put("beginTime", dateList.get(num-1).get("Mon").toString("yyyyMMdd"));//最开始的一天：往前推的最后一周的周一
    	 map.put("groupParam", groupParam);//最后一天
    	 map.put("weekList", weekList);//每一周的时间范围
    	 return map;
    }
    
     
    public static void main(String[] args) {
    	Map<String, Object> map=new HashMap<>();
    	List<Map<String, Object>> resultMap=(List<Map<String, Object>>) getWeekAreaMap(2,"20181025",map).get("weekList");
    	System.out.println(getWeekAreaMap(2,"20181025",map).get("groupParam").toString());
    	System.out.println(resultMap.toString());
    }
}