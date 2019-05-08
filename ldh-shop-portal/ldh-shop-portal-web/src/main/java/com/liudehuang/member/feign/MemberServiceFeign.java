package com.liudehuang.member.feign;

import com.liudehuang.member.service.MemberService;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;

/**
 * @author liudehuang
 * @date 2019/5/8 10:30
 */
@Component
@FeignClient("shop-service-member")
public interface MemberServiceFeign extends MemberService {
}
