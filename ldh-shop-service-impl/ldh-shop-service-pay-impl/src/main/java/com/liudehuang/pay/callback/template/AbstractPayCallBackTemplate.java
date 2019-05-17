package com.liudehuang.pay.callback.template;

import com.liudehuang.pay.constant.PayConstant;
import com.liudehuang.pay.dao.PaymentTransactionLogMapper;
import com.liudehuang.pay.entity.PaymentTransactionLogEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author liudehuang
 * @date 2019/5/17 13:13
 * 异步回调抽象类
 */
@Slf4j
public abstract class AbstractPayCallBackTemplate {

    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Autowired
    private PaymentTransactionLogMapper paymentTransactionLogMapper;

    /**
     * 获取异步回调请求所有参数，封装成map集合，并且验证参数是否被篡改
     * @param request
     * @param response
     * @return
     */
    public abstract Map<String,String> verifySignature(HttpServletRequest request, HttpServletResponse response);

    /**
     * 返回错误的结果
     * @return
     */
    public abstract String failResult();

    /**
     * 返回正确的结果
     * @return
     */
    public abstract String successResult();

    /**
     * 异步回调执行业务逻辑
     * @param verifySignature
     * @return
     */
    @Transactional
    public abstract String asyncService(Map<String,String> verifySignature);

    @Transactional
    public String asyncCallBack(HttpServletRequest request,HttpServletResponse response){
        //1.验证报文参数，相同点获取所有的请求参数封装成map集合，并且进行参数验证
        Map<String,String> map = verifySignature(request,response);
        //2.将日志根据支付id存放在数据库中
        String paymentId = map.get("paymentId");
        if(StringUtils.isEmpty(paymentId)){
            return failResult();
        }
        log.info(">>>>>asyncCallBack service 01");
        // 3.采用异步形式写入日志到数据库中
        threadPoolTaskExecutor.execute(new PayLogThread(paymentId, map));
        String result = map.get(PayConstant.SIGN_VERIFIED_RESULT_NAME);
        // 4.201报文验证签名失败
        if (result.equals(PayConstant.SIGN_VERIFIED_RESULT_PAYCODE_201)) {
            return failResult();
        }
        // 5.执行的异步回调业务逻辑
        return asyncService(map);
    }

    /**
     * 使用多线程写入日志的目的，加快响应，提高程序效率，使用线程池维护线程
     */
    class PayLogThread implements Runnable {

        private String paymentId;

        private Map<String, String> map;

        public PayLogThread(String paymentId,Map<String,String> map){
            this.paymentId = paymentId;
            this.map = map;
        }

        @Override
        public void run() {
            payLog(paymentId, map);
        }
    }

    /**
     * 采用多线程技术或者mq技术将数据存放在数据库中
     * @param paymentId
     * @param map
     */
    private void payLog(String paymentId,Map<String,String> map){
        log.info(">>>>paymentId:{},map:{}",paymentId,map);
        PaymentTransactionLogEntity paymentTransactionLogEntity = new PaymentTransactionLogEntity();
        paymentTransactionLogEntity.setAsyncLog(map.toString());
        paymentTransactionLogEntity.setTransactionId(paymentId);
        paymentTransactionLogMapper.insertTransactionLog(paymentTransactionLogEntity);
    }
}
