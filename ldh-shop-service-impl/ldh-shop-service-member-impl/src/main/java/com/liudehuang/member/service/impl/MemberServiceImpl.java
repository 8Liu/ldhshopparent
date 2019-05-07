package com.liudehuang.member.service.impl;

import com.liudehuang.core.base.BaseApiService;
import com.liudehuang.core.base.BaseResponse;
import com.liudehuang.core.constants.Constants;
import com.liudehuang.core.token.GenerateToken;
import com.liudehuang.core.utils.MiteBeanUtils;
import com.liudehuang.core.utils.TypeCastHelper;
import com.liudehuang.member.entity.UserDo;
import com.liudehuang.member.entity.UserEntity;
import com.liudehuang.member.feign.WeiXinAppServiceFeign;
import com.liudehuang.member.mapper.UserMapper;
import com.liudehuang.member.service.MemberService;
import com.liudehuang.output.dto.UserOutDTO;
import com.liudehuang.weixin.entity.AppEntity;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * 会员服务实现
 * @author liudehuang
 * @date 2019/4/28 9:24
 */
@RestController
public class MemberServiceImpl extends BaseApiService<UserOutDTO> implements MemberService {

    @Autowired
    private WeiXinAppServiceFeign weiXinAppServiceFeign;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private GenerateToken generateToken;

    @Override
    public BaseResponse<UserOutDTO> existMobile(@RequestParam("mobile") String mobile) {
        //1、验证手机号码是否存在
        if(StringUtils.isEmpty(mobile)){
            return setResultError("手机号码不能为空！");
        }

        //2.根据手机号码查询用户信息
        UserDo userDo = userMapper.existMobile(mobile);
        if(userDo==null){
            return setResultError(Constants.HTTP_RES_CODE_EXISTMOBILE_203,"用户信息不存在");
        }
        //3.隐藏敏感信息
        UserOutDTO userOutDTO = MiteBeanUtils.doToDto(userDo, UserOutDTO.class);
        return setResultSuccess(userOutDTO);
    }

    @Override
    public BaseResponse<UserOutDTO> getInfo(@RequestParam("token") String token) {
        // 1.参数验证
        if (StringUtils.isEmpty(token)) {
            return setResultError("token不能为空!");
        }
        // 2.使用token向redis中查询userId
        String redisValue = generateToken.getToken(token);
        if (StringUtils.isEmpty(redisValue)) {
            return setResultError("token已经失效或者不正确");
        }
        Long userId = TypeCastHelper.toLong(redisValue);
        // 3.根据userId查询用户信息
        UserDo userDo = userMapper.findByUserId(userId);
        if (userDo == null) {
            return setResultError("用户信息不存在!");
        }
        // 4.将Do转换为Dto
        UserOutDTO doToDto = MiteBeanUtils.doToDto(userDo, UserOutDTO.class);
        return setResultSuccess(doToDto);
    }
}
