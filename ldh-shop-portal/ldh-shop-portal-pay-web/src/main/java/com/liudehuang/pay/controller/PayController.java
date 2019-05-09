package com.liudehuang.pay.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author liudehuang
 * @date 2019/5/9 16:40
 */
@RestController
public class PayController {
    @GetMapping("/pay")
    public String pay(){
        return "蚂蚁课堂聚合支付平台";
    }
}
