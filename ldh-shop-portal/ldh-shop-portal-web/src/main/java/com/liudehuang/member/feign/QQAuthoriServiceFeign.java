package com.liudehuang.member.feign;

import com.liudehuang.member.service.QQAuthoriService;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;

/**
 * @author liudehuang
 * @date 2019/5/8 15:35
 */
@Component
@FeignClient("shop-service-member")
public interface QQAuthoriServiceFeign extends QQAuthoriService {
}
