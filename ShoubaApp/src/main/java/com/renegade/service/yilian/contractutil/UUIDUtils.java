package com.renegade.service.yilian.contractutil;

import java.util.UUID;

public class UUIDUtils {
	
	public static String generateUUID() {
		String uuid = UUID.randomUUID().toString();
        //去掉“-”符号 
        return uuid.replaceAll("-", "");
	}

}
