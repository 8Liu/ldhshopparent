package com.liudehuang.member.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.liudehuang.core.base.BaseApiService;
import com.liudehuang.core.base.BaseResponse;
import com.liudehuang.core.constants.Constants;
import com.liudehuang.core.token.GenerateToken;
import com.liudehuang.core.utils.MD5Util;
import com.liudehuang.input.dto.UserLoginInpDTO;
import com.liudehuang.member.entity.UserDo;
import com.liudehuang.member.mapper.UserMapper;
import com.liudehuang.member.mapper.UserTokenMapper;
import com.liudehuang.member.service.MemberLoginService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author liudehuang
 * @date 2019/5/6 17:00
 */
@RestController
@RequestMapping("/member")
public class MemberLoginServiceImpl extends BaseApiService<JSONObject> implements MemberLoginService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserTokenMapper userTokenMapper;

    @Autowired
    private GenerateToken generateToken;

    @Override
    public BaseResponse<JSONObject> login(@RequestBody UserLoginInpDTO userLoginInpDTO) {
        //1.验证参数
        String mobile = userLoginInpDTO.getMobile();
        if (StringUtils.isEmpty(mobile)) {
            return setResultError("手机号码不能为空!");
        }
        String password = userLoginInpDTO.getPassword();
        if (StringUtils.isEmpty(password)) {
            return setResultError("密码不能为空!");
        }
        String loginType = userLoginInpDTO.getLoginType();
        if (StringUtils.isEmpty(loginType)) {
            return setResultError("登陆类型不能为空!");
        }
        //目的是限制范围
        if (!(loginType.equals(Constants.MEMBER_LOGIN_TYPE_ANDROID) || loginType.equals(Constants.MEMBER_LOGIN_TYPE_IOS)
                || loginType.equals(Constants.MEMBER_LOGIN_TYPE_PC))) {
            return setResultError("登陆类型出现错误!");
        }

        // 设备信息
        String deviceInfor = userLoginInpDTO.getDeviceInfor();
        if (StringUtils.isEmpty(deviceInfor)) {
            return setResultError("设备信息不能为空!");
        }
        //2.对登录密码进行加密
        String newPassWord = MD5Util.MD5(password);
        //3.用户名称与密码登陆
        UserDo userDo = userMapper.login(mobile, newPassWord);
        if (userDo == null) {
            return setResultError("用户名称与密码错误!");
        }
        //4、用户每一个端登录成功会生成一token令牌（临时且唯一）存放在redis中，key为token,value为userId
        //5、获取userId
        Long userId = userDo.getUserId();
        //6、生成对应的token，存放在redis中
        String prefix = Constants.MEMBER_TOKEN_KEYPREFIX + "-" + loginType+"-";
        String token = generateToken.createToken(prefix, userId + "");
        JSONObject data = new JSONObject();
        data.put("token",token);
        return setResultSuccess(data);
    }
}
