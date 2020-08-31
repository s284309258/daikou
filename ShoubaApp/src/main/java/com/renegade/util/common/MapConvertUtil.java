package com.renegade.util.common;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapConvertUtil<T> {

    /***
     * Map转换成Object对象，List存储
     * @param maps
     * @param clss
     * @return
     */
    public List<T> MapListConvertClass(List<Map<String,Object>> maps, Class<T> clss){
        List<T> list = new ArrayList<>();
        for(Map<String,Object> map : maps){
            T tt = JSON.parseObject(JSON.toJSONString(map),clss);
            list.add(tt);
        }
        return list;
    }

    /***
     * Map转换成Object对象，对象存储
     * @param map
     * @param clss
     * @return
     */
    public T MapConvertClass(Map<String,Object> map, Class<T> clss){
        T tt = JSON.parseObject(JSON.toJSONString(map),clss);
        return tt;
    }

    public static Map<String,Object> StringArrayToMap(String[] args){
        Map<String,Object> map = new HashMap<>();
        for(String ss : args){
            String arr0 = ss.split(":")[0];
            String arr1 = ss.split(":")[1];
            if(arr0==null)arr0="";
            if(arr1==null)arr1="";
            map.put(arr0,arr1);
        }
        return map;
    }
}
