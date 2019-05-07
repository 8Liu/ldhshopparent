package com.liudehuang.weixin.service.impl;

import com.liudehuang.core.base.BaseApiService;
import com.liudehuang.core.base.BaseResponse;
import com.liudehuang.weixin.entity.AppEntity;
import com.liudehuang.weixin.service.WeiXinAppService;
import org.springframework.web.bind.annotation.RestController;

/**
 * 微信服务接口实现
 * @author liudehuang
 * @date 2019/4/28 9:01
 */
@RestController
public class WeiXinAppServiceImpl extends BaseApiService implements WeiXinAppService {
    @Override
    public BaseResponse<AppEntity> getApp() {
        return setResultSuccess(new AppEntity("644064","刘德煌"));
    }
}
