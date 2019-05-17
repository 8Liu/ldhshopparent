package com.liudehuang.pay.service;

import com.liudehuang.core.base.BaseResponse;
import com.liudehuang.pay.output.dto.PayMentTransacDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author liudehuang
 * @date 2019/5/16 14:13
 */
@RequestMapping("/payMent")
public interface PayMentTransacInfoService {

    @GetMapping("/tokenByPayMentTransac")
    public BaseResponse<PayMentTransacDTO> tokenByPayMentTransac(@RequestParam("token") String token);

}
