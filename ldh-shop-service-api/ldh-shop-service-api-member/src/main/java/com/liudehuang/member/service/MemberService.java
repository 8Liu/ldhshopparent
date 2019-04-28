package com.liudehuang.member.service;

import com.liudehuang.weixin.entity.AppEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 会员服务接口
 * @author liudehuang
 * @date 2019/4/28 9:14
 */
@Api(tags = "会员服务接口")
@RequestMapping("/member")
public interface MemberService {
    /**
     * 会员调用微信接口
     * @return
     */
    @ApiOperation("会员服务调用微信服务接口")
    @GetMapping("/memberInvokeWeiXin")
    AppEntity memberInvokeWeiXin();
}
