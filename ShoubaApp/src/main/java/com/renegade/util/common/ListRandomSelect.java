package com.renegade.util.common;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;  
  
public class ListRandomSelect {  
	

    /**
     * 从list中随机抽取若干不重复元素
     * @param paramList：被抽取list
     * @param count：抽取元素的个数
     * @param newList：组成的新的list
     */
    public static void getRandomList(List paramList,List newList,int count){
        if(paramList.size()<count){
            return;
        }
        Random random=new Random();
        List<Integer> tempList=new ArrayList<Integer>();
        int temp=0;
        for(int i=0;i<count;i++){
            temp=random.nextInt(paramList.size());//将产生的随机数作为被抽list的索引
            if(!tempList.contains(temp)){
                tempList.add(temp);
                newList.add(paramList.get(temp));
                paramList.remove(temp);
            }
            else{
                i--;
            }   
        }
        return;
    }

}  