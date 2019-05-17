package com.liudehuang.pay.input.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author liudehuang
 * @date 2019/5/16 9:52
 */
@Data
public class PayCratePayTokenDto {
    /**
     * 支付金额
     */
    @NotNull(message = "支付金额不能为空")
    private Long payAmount;
    /**
     * 订单号
     */
    @NotNull(message = "订单号码不能为空")
    private String orderId;

    /**
     * userId
     */
    @NotNull(message = "userId不能空")
    private Long userId;
   /* *//**
     * 商品描述
     *//*
    @NotNull(message = "商品描述不能为空")
    private String subject;*/
}
