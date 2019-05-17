package com.liudehuang.pay.service;

import com.alibaba.fastjson.JSONObject;
import com.liudehuang.core.base.BaseResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author liudehuang
 * @date 2019/5/17 10:21
 */
public interface PayContextService {

    @GetMapping("/toPayHtml")
    public BaseResponse<JSONObject> toPayHtml(@RequestParam("channelId") String channelId,
                                              @RequestParam("payToken") String payToken);
}
