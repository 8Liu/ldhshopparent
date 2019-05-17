package com.liudehuang.pay.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.liudehuang.core.base.BaseApiService;
import com.liudehuang.core.base.BaseResponse;
import com.liudehuang.core.token.GenerateToken;
import com.liudehuang.core.twitter.SnowflakeIdUtils;
import com.liudehuang.pay.entity.PaymentTransactionEntity;
import com.liudehuang.pay.input.dto.PayCratePayTokenDto;
import com.liudehuang.pay.dao.PaymentTransactionMapper;
import com.liudehuang.pay.service.PayMentTransacTokenService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author liudehuang
 * @date 2019/5/16 10:13
 */
@RestController
public class PayMentTransacTokenServiceImpl extends BaseApiService<JSONObject> implements PayMentTransacTokenService {
    @Autowired
    private PaymentTransactionMapper paymentTransactionMapper;

    @Autowired
    private GenerateToken generateToken;

    @Override
    public BaseResponse<JSONObject> createPayToken(@RequestBody PayCratePayTokenDto payCratePayTokenDto) {
        //1.验证参数
        String orderId = payCratePayTokenDto.getOrderId();
        if(StringUtils.isEmpty(orderId)){
            return setResultError("订单号码不能为空！！！");
        }
        Long payAmount = payCratePayTokenDto.getPayAmount();
        if(payAmount==null){
            return setResultError("订单金额不能为空！！！");
        }
        Long userId = payCratePayTokenDto.getUserId();
        if (userId == null) {
            return setResultError("userId不能为空!");
        }
        // 2.将输入插入数据库中
        PaymentTransactionEntity paymentTransactionEntity = new PaymentTransactionEntity();
        paymentTransactionEntity.setOrderId(orderId);
        paymentTransactionEntity.setPayAmount(payAmount);
        paymentTransactionEntity.setUserId(userId);
        // 使用雪花算法 生成全局id
        paymentTransactionEntity.setPaymentId(SnowflakeIdUtils.nextId() + "");
        int result = paymentTransactionMapper.insertPaymentTransaction(paymentTransactionEntity);
        if (!toDaoResult(result)) {
            return setResultError("系统错误!");
        }
        Long payId = paymentTransactionEntity.getId();
        if (payId == null) {
            return setResultError("系统错误!");
        }

        // 3.生成对应支付令牌
        String keyPrefix = "pay_";
        String token = generateToken.createToken(keyPrefix, payId + "");
        JSONObject dataResult = new JSONObject();
        dataResult.put("token", token);
        return setResultSuccess(dataResult);
    }
}
