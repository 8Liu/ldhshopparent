package com.liudehuang.member.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.liudehuang.core.base.BaseApiService;
import com.liudehuang.core.base.BaseResponse;
import com.liudehuang.core.constants.Constants;
import com.liudehuang.core.token.GenerateToken;
import com.liudehuang.core.transactiion.RedisDataSoureceTransaction;
import com.liudehuang.member.entity.UserDo;
import com.liudehuang.member.entity.UserTokenDo;
import com.liudehuang.member.mapper.UserMapper;
import com.liudehuang.member.mapper.UserTokenMapper;
import com.liudehuang.member.service.QQAuthoriService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.TransactionStatus;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class QQAuthoriServiceImpl extends BaseApiService<JSONObject> implements QQAuthoriService {
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private GenerateToken generateToken;
	@Autowired
	private UserTokenMapper userTokenMapper;
	@Autowired
	private RedisDataSoureceTransaction redisDataSoureceTransaction;

	@Override
	public BaseResponse<JSONObject> findByOpenId(@RequestParam("qqOpenId") String qqOpenId,@RequestParam("loginType") String loginType) {
		if (StringUtils.isEmpty(qqOpenId)) {
			return setResultError("qqOpenId不能为空!");
		}
		// 1.根据openid查询用户信息
		UserDo userDo = userMapper.findByOpenId(qqOpenId);
		if (userDo == null) {
			return setResultError(Constants.HTTP_RES_CODE_NOTUSER_203, "根据qqOpenId没有查询到用户信息");
		}
		TransactionStatus status = null;
		try{
			Long userId = userDo.getUserId();

			//根据userId+loginType查询当前登录类型账号之前是否登录过，如果登录过，清楚之前redis的token
			UserTokenDo userTokenDo = userTokenMapper.selectByUserIdAndLoginType(userId, loginType);
			if(userTokenDo!=null){
				//说明之前登录过
				Boolean flag = generateToken.removeToken(userTokenDo.getToken());
				//把该token的状态改为1
				int updateTokenAvailability = userTokenMapper.updateTokenAvailability(userTokenDo.getToken());
				if(!toDaoResult(updateTokenAvailability)){
					return setResultError("系统错误");
				}
			}
			UserTokenDo userToken = new UserTokenDo();
			userToken.setUserId(userId);
			userToken.setLoginType(loginType);
			// 2.如果能够查询到用户信息,则直接生成对应的用户令牌
			String keyPrefix = Constants.MEMBER_TOKEN_KEYPREFIX + Constants.HTTP_RES_CODE_QQ_LOGINTYPE;
			String token = generateToken.createToken(keyPrefix, userId + "");
			userToken.setToken(token);
			int row = userTokenMapper.insertUserToken(userToken);
			if(!toDaoResult(row)){
				redisDataSoureceTransaction.rollback(status);
				return setResultError("系统错误");
			}
			JSONObject data = new JSONObject();
			data.put("token",token);
			redisDataSoureceTransaction.commit(status);
			return setResultSuccess(data);
		}catch (Exception e){
			try {
				redisDataSoureceTransaction.rollback(status);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			return setResultError("系统错误");
		}


	}

}