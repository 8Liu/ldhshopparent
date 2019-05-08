package com.liudehuang.member.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.liudehuang.core.base.BaseApiService;
import com.liudehuang.core.base.BaseResponse;
import com.liudehuang.core.constants.Constants;
import com.liudehuang.core.token.GenerateToken;
import com.liudehuang.core.transactiion.RedisDataSoureceTransaction;
import com.liudehuang.core.utils.MD5Util;
import com.liudehuang.input.dto.UserLoginInpDTO;
import com.liudehuang.member.entity.UserDo;
import com.liudehuang.member.entity.UserTokenDo;
import com.liudehuang.member.mapper.UserMapper;
import com.liudehuang.member.mapper.UserTokenMapper;
import com.liudehuang.member.service.MemberLoginService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.TransactionStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * redis 与数据库的事务怎么保持一致性
 * redis中也存在事务
 * 数据库也存在事务
 * 自定义方法使用编程式事务:既要控制redis的事务和数据库的事务
 *
 * 如果redis的值和数据库的值不一致，怎么处理？
 * 删除redis的值，再查数据库的值，缓存一遍
 * @author liudehuang
 * @date 2019/5/6 17:00
 */
@RestController
public class MemberLoginServiceImpl extends BaseApiService<JSONObject> implements MemberLoginService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserTokenMapper userTokenMapper;

    @Autowired
    private GenerateToken generateToken;

    @Autowired
    private RedisDataSoureceTransaction redisDataSoureceTransaction;

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
        TransactionStatus status = null;
        try{
            //4、用户每一个端登录成功会生成一token令牌（临时且唯一）存放在redis中，key为token,value为userId
            //5、获取userId
            Long userId = userDo.getUserId();
            //6、根据userId+loginType查询当前登录类型账号之前是否登录过，如果登录过，清楚之前redis的token
            UserTokenDo userTokenDo = userTokenMapper.selectByUserIdAndLoginType(userId, loginType);
            status = redisDataSoureceTransaction.begin();
            if(userTokenDo!=null){
                //说明之前已经登录过
                //6.1 清楚之前的token,如果开启了redis事务，删除方法会返回false
                Boolean flag = generateToken.removeToken(userTokenDo.getToken());
                //把该token的状态改为1
                int updateTokenAvailability = userTokenMapper.updateTokenAvailability(userTokenDo.getToken());
                if(!toDaoResult(updateTokenAvailability)){
                    return setResultError("系统错误");
                }
            }

            //如果有传openId，将openId更新到数据库中
            String qqOpenId = userLoginInpDTO.getQqOpenId();
            if (!StringUtils.isEmpty(qqOpenId)) {
                userMapper.updateUserOpenId(qqOpenId, userId);
            }
            // 8.存入在数据库中，插入新的token
            UserTokenDo userToken = new UserTokenDo();
            userToken.setUserId(userId);
            userToken.setLoginType(userLoginInpDTO.getLoginType());
            //7、生成对应的token，存放在redis中
            String prefix = Constants.MEMBER_TOKEN_KEYPREFIX + "-" + loginType+"-";
            String token = generateToken.createToken(prefix, userId + "");
            userToken.setToken(token);
            userToken.setDeviceInfor(deviceInfor);
            int row = userTokenMapper.insertUserToken(userToken);
            if(!toDaoResult(row)){
                redisDataSoureceTransaction.rollback(status);
                return setResultError("系统错误");
            }
            JSONObject data = new JSONObject();
            data.put("token",token);
            redisDataSoureceTransaction.commit(status);
            return setResultSuccess(data);
            //token 存放在PC端，放在Cookie中
            //token 在安卓和ios端，放在本地文件中
            //当前token存在哪些问题？用户退出或者修改密码时需要把token清楚

        }catch (Exception e){
            try {
                redisDataSoureceTransaction.rollback(status);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            return setResultError("系统错误");
        }



    }

    @Override
    public BaseResponse<JSONObject> exit(@RequestParam("token") String token) {
        Boolean flag = generateToken.removeToken(token);
        int row = userTokenMapper.updateTokenAvailability(token);
        if(!toDaoResult(row)){
            return setResultError("更新失败");
        }
        if(!flag){
            return setResultError("退出失败");
        }
        return setResultSuccess();
    }
}
