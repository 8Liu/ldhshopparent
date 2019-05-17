package com.liudehuang.pay.constant;

/**
 * @author liudehuang
 * @date 2019/5/17 13:35
 */
public interface PayConstant {
    /**
     * 验签结果字段
     */
    String SIGN_VERIFIED_RESULT_NAME = "SIGN_VERIFIED_RESULT";
    /**
     * 验签成功code码
     */
    String SIGN_VERIFIED_RESULT_PAYCODE_201 = "201";
    /**
     * 验签失败code码
     */
    String SIGN_VERIFIED_RESULT_PAYCODE_200 = "200";
    /**
     * 已经支付成功状态
     */
    Integer PAY_STATUS_SUCCESS = 1;
    /**
     * 返回银联通知成功
     */
    String YINLIAN_RESULT_SUCCESS = "ok";
    /**
     * 返回银联失败通知
     */
    String YINLIAN_RESULT_FAIL = "fail";
}
