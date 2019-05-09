package com.liudehuang.member.service;

import com.liudehuang.core.base.BaseResponse;
import com.liudehuang.input.dto.UserLoginInpDTO;
import com.liudehuang.member.entity.UserEntity;
import com.liudehuang.output.dto.UserOutDTO;
import com.liudehuang.weixin.entity.AppEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

/**
 * 会员服务接口
 * @author liudehuang
 * @date 2019/4/28 9:14
 */
@Api(tags = "会员服务接口")
@RequestMapping("/member")
public interface MemberService {


    /**
     * 根据手机号码查询是否已经存在,如果存在返回当前用户信息
     *
     * @param mobile
     * @return
     */
    @ApiOperation(value = "根据手机号码查询是否已经存在")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "mobile", dataType = "String", required = true, value = "用户手机号码"), })
    @PostMapping("/existMobile")
    BaseResponse<UserOutDTO> existMobile(@RequestParam("mobile") String mobile);

    /**
     * 根据token查询用户信息
     *
     * @param token
     * @return
     */
    @GetMapping("/getUserInfo")
    @ApiOperation(value = "根据token获取用户信息")
    BaseResponse<UserOutDTO> getInfo(@RequestParam("token") String token);

    /**
     * SSO认证系统登陆接口
     *
     * @param userLoginInpDTO
     * @return
     */
    @PostMapping("/ssoLogin")
    public BaseResponse<UserOutDTO> ssoLogin(@RequestBody UserLoginInpDTO userLoginInpDTO);
}
