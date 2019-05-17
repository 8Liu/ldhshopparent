package com.liudehuang.pay.feign;

import com.liudehuang.pay.service.PayContextService;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;

/**
 * @author liudehuang
 * @date 2019/5/17 10:49
 */
@Component
@FeignClient("shop-pay")
public interface PayContextServiceFeign extends PayContextService {
}
