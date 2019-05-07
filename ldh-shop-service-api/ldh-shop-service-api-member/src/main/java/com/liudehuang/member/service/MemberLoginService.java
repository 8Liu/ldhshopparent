package com.liudehuang.member.service;

import com.alibaba.fastjson.JSONObject;
import com.liudehuang.core.base.BaseResponse;
import com.liudehuang.input.dto.UserLoginInpDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 用户登录接口服务类
 * @author liudehuang
 * @date 2019/5/6 16:53
 */
@Api(tags = "会员登录接口")
public interface MemberLoginService {
    /**
     * 用户登陆接口
     *
     * @param
     * @return
     */
    @PostMapping("/login")
    @ApiOperation(value = "会员用户登陆信息接口")
    BaseResponse<JSONObject> login(@RequestBody UserLoginInpDTO userLoginInpDTO);
}
