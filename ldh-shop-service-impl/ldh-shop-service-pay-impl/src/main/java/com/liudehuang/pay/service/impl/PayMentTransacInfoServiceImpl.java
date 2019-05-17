package com.liudehuang.pay.service.impl;

import com.liudehuang.core.base.BaseApiService;
import com.liudehuang.core.base.BaseResponse;
import com.liudehuang.core.token.GenerateToken;
import com.liudehuang.core.utils.MeiteBeanUtils;
import com.liudehuang.core.utils.MiteBeanUtils;
import com.liudehuang.pay.dao.PaymentTransactionMapper;
import com.liudehuang.pay.entity.PaymentTransactionEntity;
import com.liudehuang.pay.output.dto.PayMentTransacDTO;
import com.liudehuang.pay.service.PayMentTransacInfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author liudehuang
 * @date 2019/5/16 14:26
 */
@RestController
public class PayMentTransacInfoServiceImpl extends BaseApiService implements PayMentTransacInfoService {
    @Autowired
    private GenerateToken generateToken;
    @Autowired
    private PaymentTransactionMapper paymentTransactionMapper;

    @Override
    public BaseResponse<PayMentTransacDTO> tokenByPayMentTransac(@RequestParam("token") String token) {
        // 1.验证token是否为空
        if (StringUtils.isEmpty(token)) {
            return setResultError("token参数不能空!");
        }
        // 2.使用token查询redisPayMentTransacID
        String value = generateToken.getToken(token);
        if (StringUtils.isEmpty(value)) {
            return setResultError("该Token可能已经失效或者已经过期");
        }
        // 3.转换为整数类型
        Long transactionId = Long.parseLong(value);
        // 4.使用transactionId查询支付信息
        PaymentTransactionEntity paymentTransaction = paymentTransactionMapper.selectById(transactionId);
        if (paymentTransaction == null) {
            return setResultError("未查询到该支付信息");
        }
        return setResultSuccess(MiteBeanUtils.doToDto(paymentTransaction, PayMentTransacDTO.class));

    }
}
