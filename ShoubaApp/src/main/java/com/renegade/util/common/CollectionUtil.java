package com.renegade.util.common;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 集合工厂类
 * @author fcy
 *
 */
public class CollectionUtil {
     
    public static <K,V> HashMap<K, V> newMapInstance(){
        return new HashMap<K, V>();
    }
     
    public static <T> ArrayList<T> newListInstance(){
        return new ArrayList<T>();
    }
}