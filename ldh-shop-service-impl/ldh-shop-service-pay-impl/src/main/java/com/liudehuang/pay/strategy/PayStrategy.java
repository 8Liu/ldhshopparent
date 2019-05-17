package com.liudehuang.pay.strategy;

import com.liudehuang.pay.entity.PaymentChannelEntity;
import com.liudehuang.pay.output.dto.PayMentTransacDTO;

/**
 * @author liudehuang
 * @date 2019/5/17 10:04
 */
public interface PayStrategy {

    /**
     *
     * @param pymentChannel
     *            渠道参数
     * @param payMentTransacDTO
     *            支付参数
     * @return
     */
    public String toPayHtml(PaymentChannelEntity pymentChannel, PayMentTransacDTO payMentTransacDTO);

}
