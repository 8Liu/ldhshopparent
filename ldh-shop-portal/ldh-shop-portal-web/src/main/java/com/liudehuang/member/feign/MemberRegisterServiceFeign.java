package com.liudehuang.member.feign;

import com.liudehuang.member.service.MemberRegisterService;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;

/**
 * @author liudehuang
 * @date 2019/5/7 17:06
 */
@Component
@FeignClient("shop-service-member")
public interface MemberRegisterServiceFeign extends MemberRegisterService {
}
