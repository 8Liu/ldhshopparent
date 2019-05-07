package com.liudehuang.weixin.feign;

import com.liudehuang.member.service.MemberService;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;

/**
 * @author liudehuang
 * @date 2019/5/6 10:10
 */
@Component
@FeignClient("shop-service-member")
public interface MemberServiceFeign extends MemberService {
}
