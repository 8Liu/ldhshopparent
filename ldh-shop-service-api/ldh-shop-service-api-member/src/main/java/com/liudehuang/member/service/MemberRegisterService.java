package com.liudehuang.member.service;

import com.alibaba.fastjson.JSONObject;
import com.liudehuang.core.base.BaseResponse;
import com.liudehuang.input.dto.UserInpDTO;
import com.liudehuang.member.entity.UserEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author liudehuang
 * @date 2019/4/30 10:36
 */
@Api(tags = "会员注册接口")
@RequestMapping("/member")
public interface MemberRegisterService {
    /**
     * 用户注册接口
     * @param userInpDTO
     * @param registerCode
     * @return
     */
    @PostMapping("/register")
    @ApiOperation(value="会员用户注册信息接口")
    @ApiImplicitParams({@ApiImplicitParam(name = "userEntity",dataType = "UserEntity",paramType = "query"),@ApiImplicitParam(name = "registerCode",dataType = "String",paramType = "query")})
    BaseResponse<JSONObject> register(@RequestBody UserInpDTO userInpDTO, @RequestParam("registerCode") String registerCode);
}
