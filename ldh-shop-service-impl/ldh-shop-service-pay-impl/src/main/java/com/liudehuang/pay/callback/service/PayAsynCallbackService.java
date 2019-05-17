package com.liudehuang.pay.callback.service;

import com.liudehuang.pay.callback.template.AbstractPayCallBackTemplate;
import com.liudehuang.pay.callback.template.factory.TemplateFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author liudehuang
 * @date 2019/5/17 15:14
 */
@RestController
@RequestMapping("/callBack")
public class PayAsynCallbackService {

    private static final String ALIPAYCALLBACK_TEMPLATE = "aliPayCallBackTemplate";


    /**
     * 银联异步回调接口执行代码
     *
     * @param req
     * @param resp
     * @return
     */
    @RequestMapping("/aliPayCallBack")
    public String unionPayAsynCallback(HttpServletRequest req, HttpServletResponse resp) {
        AbstractPayCallBackTemplate abstractPayCallbackTemplate = TemplateFactory
                .getPayCallBackTemplate(ALIPAYCALLBACK_TEMPLATE);
        return abstractPayCallbackTemplate.asyncCallBack(req, resp);
    }
}
