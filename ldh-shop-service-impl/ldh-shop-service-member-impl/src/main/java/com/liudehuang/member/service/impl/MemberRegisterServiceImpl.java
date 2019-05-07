package com.liudehuang.member.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.liudehuang.core.base.BaseApiService;
import com.liudehuang.core.base.BaseResponse;
import com.liudehuang.core.constants.Constants;
import com.liudehuang.core.utils.MD5Util;
import com.liudehuang.core.utils.MiteBeanUtils;
import com.liudehuang.input.dto.UserInpDTO;
import com.liudehuang.member.entity.UserDo;
import com.liudehuang.member.entity.UserEntity;
import com.liudehuang.member.feign.VerificaCodeServiceFeign;
import com.liudehuang.member.mapper.UserMapper;
import com.liudehuang.member.service.MemberRegisterService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author liudehuang
 * @date 2019/4/30 10:43
 */
@RestController
public class MemberRegisterServiceImpl extends BaseApiService implements MemberRegisterService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private VerificaCodeServiceFeign verificaCodeServiceFeign;

    @Transactional
    @Override
    public BaseResponse<JSONObject> register(@RequestBody UserInpDTO userInpDTO, @RequestParam("registerCode") String registerCode) {
        //1.参数验证
        String userName = userInpDTO.getUserName();
        if(StringUtils.isEmpty(userName)){
            return setResultError("用户名称不能为空!");
        }
        String phone = userInpDTO.getMobile();
        if(StringUtils.isEmpty(phone)){
            return setResultError("电话号码不能为空!");
        }
        String password = userInpDTO.getPassword();
        if(StringUtils.isEmpty(password)){
            return setResultError("密码不能为空!");
        }

        //2.验证注册码是否正确
        BaseResponse<JSONObject> response = verificaCodeServiceFeign.verificaWeixinCode(phone, registerCode);
        if(!response.getRtnCode().equals(Constants.HTTP_RES_CODE_200)){
            return setResultError(response.getMsg());
        }
        //3.对用户的密码进行加密
        String newPassword = MD5Util.MD5(password);
        userInpDTO.setPassword(newPassword);
        //4.调用数据库插入数据,将请求的dto装换成do
        UserDo userDo = MiteBeanUtils.dtoToDo(userInpDTO, UserDo.class);
        int registerResult = userMapper.register(userDo);
        return registerResult > 0 ? setResultSuccess("注册成功") : setResultSuccess("注册失败");
    }
}
