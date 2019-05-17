package com.liudehuang.pay.feign;

import com.liudehuang.pay.service.PaymentChannelService;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;

@Component
@FeignClient("shop-pay")
public interface PaymentChannelServiceFeign extends PaymentChannelService {

}
