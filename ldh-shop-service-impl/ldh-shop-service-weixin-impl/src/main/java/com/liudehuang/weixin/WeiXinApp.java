package com.liudehuang.weixin;

import com.spring4all.swagger.EnableSwagger2Doc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @author liudehuang
 * @date 2019/4/28 9:03
 */
@SpringBootApplication
@EnableEurekaClient
@EnableSwagger2Doc
public class WeiXinApp {

    public static void main(String[] args) {
        SpringApplication.run(WeiXinApp.class,args);
    }
}
