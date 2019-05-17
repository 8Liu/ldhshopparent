package com.liudehuang.pay.service;

import com.alibaba.fastjson.JSONObject;
import com.liudehuang.core.base.BaseResponse;
import com.liudehuang.pay.input.dto.PayCratePayTokenDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author liudehuang
 * @date 2019/5/16 9:48
 */
@RequestMapping("/payMent")
public interface PayMentTransacTokenService {
    /**
     * 创建支付token
     * @return
     */
    @PostMapping("/createPayToken")
    BaseResponse<JSONObject> createPayToken(@RequestBody PayCratePayTokenDto payCratePayTokenDto);
}
