package com.liudehuang.weixin.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.liudehuang.core.base.BaseApiService;
import com.liudehuang.core.base.BaseResponse;
import com.liudehuang.core.constants.Constants;
import com.liudehuang.core.utils.RedisUtil;
import com.liudehuang.weixin.service.VerificaCodeService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author liudehuang
 * @date 2019/4/29 13:53
 */
@RestController
public class VerificaCodeServiceImpl extends BaseApiService implements VerificaCodeService {
    @Autowired
    private RedisUtil redisUtil;

    @Override
    public BaseResponse<JSONObject> verificaWeixinCode(@RequestParam("phone") String phone, @RequestParam("weixinCode") String weixinCode) {
        //1、验证参数是否为空
        if(StringUtils.isEmpty(phone)){
            return setResultError("手机号码不能为空!");
        }
        if(StringUtils.isEmpty(weixinCode)){
            return setResultError("注册码不能为空!");
        }
        //2、根据手机号码查询redis返回对应的注册码
        String code = redisUtil.getString(Constants.WEIXINCODE_KEY+phone);
        if(StringUtils.isEmpty(code)){
            return setResultError("注册码已过期，请重新获取注册码!");
        }
        //3、redis的注册码和传参的注册码比对
        if(!code.equals(weixinCode)){
            return setResultError("注册码比对错误");
        }
        //6、移除注册码
        redisUtil.delKey(Constants.WEIXINCODE_KEY+phone);
        return setResultSuccess("注册码比对正确");
    }
}
