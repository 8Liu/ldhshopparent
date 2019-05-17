package com.liudehuang.pay.strategy.impl;

import com.liudehuang.pay.entity.PaymentChannelEntity;
import com.liudehuang.pay.output.dto.PayMentTransacDTO;
import com.liudehuang.pay.strategy.PayStrategy;

/**
 * @author liudehuang
 * @date 2019/5/17 10:09
 */
public class XiaoMiPayStrategy implements PayStrategy {
    @Override
    public String toPayHtml(PaymentChannelEntity pymentChannel, PayMentTransacDTO payMentTransacDTO) {
        return "小米支付form表单提交";
    }
}
