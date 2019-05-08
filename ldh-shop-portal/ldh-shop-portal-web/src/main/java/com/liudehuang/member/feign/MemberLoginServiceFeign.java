package com.liudehuang.member.feign;

import com.liudehuang.member.service.MemberLoginService;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;

/**
 * @author liudehuang
 * @date 2019/5/8 9:40
 */
@Component
@FeignClient("shop-service-member")
public interface MemberLoginServiceFeign extends MemberLoginService {
}
