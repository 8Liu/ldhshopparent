package com.liudehuang.weixin.service.impl;

import com.liudehuang.weixin.entity.AppEntity;
import com.liudehuang.weixin.service.WeiXinAppService;
import org.springframework.web.bind.annotation.RestController;

/**
 * 微信服务接口实现
 * @author liudehuang
 * @date 2019/4/28 9:01
 */
@RestController
public class WeiXinAppServiceImpl implements WeiXinAppService {
    @Override
    public AppEntity getApp() {
        return new AppEntity("644064","刘德煌");
    }
}
