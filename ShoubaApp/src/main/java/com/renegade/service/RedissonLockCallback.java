package com.renegade.service;
/** redisson接口回调类
*  @Copyright    2018 
*        @author Renegade丶四叶草 
*                    All right reserved
*      @Created   2019年5月3日 
*   @version  1.0
* @email 291312408@qq.com
*/
public interface RedissonLockCallback<T> {
	 /** 
     * 调用者必须在此方法中实现需要加分布式锁的业务逻辑 
     * 
     * @return 
     */  
    public T process();  
  
    /** 
     * 得到分布式锁名称 
     * 
     * @return 
     */  
    public String getLockName(); 
}

