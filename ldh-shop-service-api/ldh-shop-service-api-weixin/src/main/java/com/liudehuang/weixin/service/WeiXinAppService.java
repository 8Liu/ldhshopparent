package com.liudehuang.weixin.service;

import com.liudehuang.weixin.entity.AppEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author liudehuang
 * @date 2019/4/28 8:55
 */
@Api(tags = "微信服务接口调用")
@RequestMapping("/weiXin")
public interface WeiXinAppService {
    /**
     * 应用服务接口
     * @return
     */
    @ApiOperation("获取应用详情")
    @GetMapping("getApp")
    AppEntity getApp();
}
