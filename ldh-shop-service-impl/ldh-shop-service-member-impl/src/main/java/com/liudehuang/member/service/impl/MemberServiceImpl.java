package com.liudehuang.member.service.impl;

import com.liudehuang.member.feign.WeiXinAppServiceFeign;
import com.liudehuang.member.service.MemberService;
import com.liudehuang.weixin.entity.AppEntity;
import com.liudehuang.weixin.service.WeiXinAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;


/**
 * 会员服务实现
 * @author liudehuang
 * @date 2019/4/28 9:24
 */
@RestController
public class MemberServiceImpl implements MemberService {

    @Autowired
    private WeiXinAppServiceFeign weiXinAppServiceFeign;


    @Override
    public AppEntity memberInvokeWeiXin() {
        return weiXinAppServiceFeign.getApp();
    }
}
