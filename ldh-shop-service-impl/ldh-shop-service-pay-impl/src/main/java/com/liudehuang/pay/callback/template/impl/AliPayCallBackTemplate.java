package com.liudehuang.pay.callback.template.impl;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.liudehuang.alipay.config.AlipayConfig;
import com.liudehuang.pay.callback.template.AbstractPayCallBackTemplate;
import com.liudehuang.pay.constant.PayConstant;
import com.liudehuang.pay.dao.PaymentTransactionLogMapper;
import com.liudehuang.pay.dao.PaymentTransactionMapper;
import com.liudehuang.pay.entity.PaymentTransactionEntity;
import com.liudehuang.pay.mq.producer.IntegralProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author liudehuang
 * @date 2019/5/17 14:05
 */
@Slf4j
@Component
public class AliPayCallBackTemplate extends AbstractPayCallBackTemplate {

    @Autowired
    private PaymentTransactionMapper paymentTransactionMapper;
    @Autowired
    private IntegralProducer integralProducer;

    @Override
    public Map<String, String> verifySignature(HttpServletRequest request, HttpServletResponse response) {
        log.info(">>>>>>>>>>支付宝异步回调开始<<<<<<<<<<<<");
        //1.获取所有的请求参数
        Map<String,String> params = getAllRequestParam(request);
        log.info("支付宝异步回调参数params:{}",params);
        boolean signVerified = false;
        try {
            signVerified = AlipaySignature.rsaCheckV1(params, AlipayConfig.alipay_public_key, AlipayConfig.charset, AlipayConfig.sign_type);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        log.info("#####支付宝异步通知signVerified：{}",signVerified);
        if(!signVerified){
            log.info(">>>>>>>>>>>>支付宝异步回调验签失败<<<<<<<<<<<<<<<<");
            //2.如果验签失败，则加入验签失败code码201
            params.put(PayConstant.SIGN_VERIFIED_RESULT_NAME, PayConstant.SIGN_VERIFIED_RESULT_PAYCODE_201);
        }else{
            //验签成功，加入验签成功状态码200
            params.put(PayConstant.SIGN_VERIFIED_RESULT_NAME,PayConstant.SIGN_VERIFIED_RESULT_PAYCODE_200);
            String paymentId = params.get("out_trade_no");
            params.put("paymentId",paymentId);
        }
        return params;
    }

    public Map<String,String> getAllRequestParam(HttpServletRequest request){
        Map<String,String> params = new HashMap<String,String>();
        Map<String,String[]> requestParams = request.getParameterMap();
        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用
            try {
                valueStr = new String(valueStr.getBytes("UTF-8"), "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            params.put(name, valueStr);
        }
        return params;
    }

    /**
     * 异步回调错误结果
     * @return
     */
    @Override
    public String failResult() {
        return "fail";
    }

    /**
     * 异步回调正确结果
     * @return
     */
    @Override
    public String successResult() {
        return "success";
    }

    @Override
    public String asyncService(Map<String, String> verifySignature) {
        //1.获取外部商户号
        String outTradeNo = verifySignature.get("out_trade_no");
        //2.获取第三方支付交易号
        String tradeNo = verifySignature.get("trade_no");
        //3.获取交易状态
        String tradeStatus = verifySignature.get("trade_status");
        log.info("outTradeNo:{},tradeNo:{},tradeStatus:{}",outTradeNo,tradeNo,tradeStatus);
        // 4.根据日志 手动补偿 使用支付id调用第三方支付接口查询
        PaymentTransactionEntity paymentTransaction = paymentTransactionMapper.selectByPaymentId(outTradeNo);
        // 5.根据全局id判断是否已经支付,若已经支付，则返回成功（防止幂等性问题）
        if (paymentTransaction.getPaymentStatus().equals(PayConstant.PAY_STATUS_SUCCESS)) {
            // 网络重试中，之前已经支付过
            return successResult();
        }
        // 6.将状态改为已经支付成功
        paymentTransactionMapper.updatePaymentStatus(PayConstant.PAY_STATUS_SUCCESS + "", outTradeNo);
        // 7.添加加积分操作
        // 3.调用积分服务接口增加积分(处理幂等性问题) MQ
        addMQIntegral(paymentTransaction);
        // 8.改变订单服务的订单状态
        return successResult();
    }

    /**
     * 基于MQ增加积分
     */
    @Async
    public void addMQIntegral(PaymentTransactionEntity paymentTransaction) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("paymentId", paymentTransaction.getPaymentId());
        jsonObject.put("userId", paymentTransaction.getUserId());
        jsonObject.put("integral", 100);
        integralProducer.send(jsonObject);
    }
}
