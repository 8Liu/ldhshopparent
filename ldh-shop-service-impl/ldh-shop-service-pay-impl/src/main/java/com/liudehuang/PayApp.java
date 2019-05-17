package com.liudehuang;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @author liudehuang
 * @date 2019/5/16 10:02
 */
@MapperScan("com.liudehuang.pay.dao")
@SpringBootApplication
@EnableEurekaClient
public class PayApp {

    public static void main(String[] args) {
        SpringApplication.run(PayApp.class,args);
    }
}
