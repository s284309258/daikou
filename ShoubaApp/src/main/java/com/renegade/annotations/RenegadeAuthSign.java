package com.renegade.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** 
*  @Copyright    2018 
*        @author Renegade丶四叶草 
*                    All right reserved
*      @Created   2019年7月5日 
*   @version  1.0
* @email 291312408@qq.com
*/
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RenegadeAuthSign {

}

