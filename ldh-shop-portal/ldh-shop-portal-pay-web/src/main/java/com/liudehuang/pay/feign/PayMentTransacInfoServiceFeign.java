package com.liudehuang.pay.feign;

import com.liudehuang.pay.service.PayMentTransacInfoService;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;

/**
 * @author liudehuang
 * @date 2019/5/16 14:10
 */

@Component
@FeignClient("shop-pay")
public interface PayMentTransacInfoServiceFeign extends PayMentTransacInfoService {
}
