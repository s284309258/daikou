package com.renegade;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
@EnableAutoConfiguration(exclude = {
        org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class
})
@SpringBootApplication
@EnableCaching
@EnableScheduling
@EnableAsync
@ServletComponentScan
@EnableTransactionManagement
//@MapperScan({ "com.renegade", "com.renegade.dao.*" })
@MapperScan({ "com.*.dao", "com.*.dao.*" })
@EnableAspectJAutoProxy(proxyTargetClass=true, exposeProxy=true)//暴露代理
public class ShouBaApplication extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(ShouBaApplication.class, args);
        System.out.println("ヾ(◍°∇°◍)ﾉﾞ    springBoot启动成功      ヾ(◍°∇°◍)ﾉﾞ\n" +
                " ______                    _   ______            \n" +
                "|_   _ \\                  / |_|_   _ `.          \n" +
                "  | |_) |   .--.    .--. `| |-' | | `. \\  .--.   \n" +
                "  |  __'. / .'`\\ \\/ .'`\\ \\| |   | |  | |/ .'`\\ \\ \n" +
                " _| |__) || \\__. || \\__. || |, _| |_.' /| \\__. | \n" +
                "|_______/  '.__.'  '.__.' \\__/|______.'  '.__.'  ");
    }

	// 打成war包需要的
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		// 注意这里要指向原先用main方法执行的Application启动类
		return builder.sources(ShouBaApplication.class);
	}
}
