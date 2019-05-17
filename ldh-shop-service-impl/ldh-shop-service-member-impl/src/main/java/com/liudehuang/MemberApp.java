package com.liudehuang;

import com.spring4all.swagger.EnableSwagger2Doc;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author liudehuang
 * @date 2019/4/28 9:57
 */
@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
@EnableSwagger2Doc
@MapperScan(basePackages = "com.liudehuang.member.dao")
public class MemberApp {

    public static void main(String[] args) {
        SpringApplication.run(MemberApp.class,args);
    }
}
