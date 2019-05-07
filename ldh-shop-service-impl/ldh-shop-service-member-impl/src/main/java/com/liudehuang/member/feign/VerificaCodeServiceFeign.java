package com.liudehuang.member.feign;

import com.liudehuang.weixin.service.VerificaCodeService;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;

/**
 * @author liudehuang
 * @date 2019/5/6 10:36
 */
@Component
@FeignClient("shop-service-weixin")
public interface VerificaCodeServiceFeign extends VerificaCodeService {
}
