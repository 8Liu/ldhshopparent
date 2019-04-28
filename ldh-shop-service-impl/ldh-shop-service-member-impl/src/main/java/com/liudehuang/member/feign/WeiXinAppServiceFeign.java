package com.liudehuang.member.feign;

import com.liudehuang.weixin.service.WeiXinAppService;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;

/**
 * @author liudehuang
 * @date 2019/4/28 9:33
 */
@Component
@FeignClient("shop-service-weixin")
public interface WeiXinAppServiceFeign extends WeiXinAppService {

}
