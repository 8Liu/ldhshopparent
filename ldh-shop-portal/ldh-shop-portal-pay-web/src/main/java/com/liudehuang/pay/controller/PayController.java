package com.liudehuang.pay.controller;

import com.alibaba.fastjson.JSONObject;
import com.liudehuang.core.base.BaseResponse;
import com.liudehuang.pay.feign.PayContextServiceFeign;
import com.liudehuang.pay.feign.PayMentTransacInfoServiceFeign;
import com.liudehuang.pay.feign.PaymentChannelServiceFeign;
import com.liudehuang.pay.output.dto.PayMentTransacDTO;
import com.liudehuang.pay.output.dto.PaymentChannelDTO;
import com.liudehuang.web.base.BaseWebController;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author liudehuang
 * @date 2019/5/9 16:40
 */
@Controller
public class PayController extends BaseWebController {
    @Autowired
    private PayContextServiceFeign payContextServiceFeign;
    @Autowired
    private PayMentTransacInfoServiceFeign payMentTransacInfoServiceFeign;
    @Autowired
    private PaymentChannelServiceFeign paymentChannelServiceFeign;

    @RequestMapping("/pay")
    public String pay(String payToken, Model model){
        //1.验证payToken参数
        if (StringUtils.isEmpty(payToken)) {
            setErrorMsg(model, "支付令牌不能为空!");
            return ERROR_500_FTL;
        }
        //2.使用token查询支付信息
        BaseResponse<PayMentTransacDTO> tokenByPayMentTransac = payMentTransacInfoServiceFeign.tokenByPayMentTransac(payToken);
        if (!isSuccess(tokenByPayMentTransac)) {
            setErrorMsg(model, tokenByPayMentTransac.getMsg());
            return ERROR_500_FTL;
        }
        //3.查询渠道信息
        PayMentTransacDTO data = tokenByPayMentTransac.getData();
        model.addAttribute("data", data);
        // 4.查询渠道信息
        List<PaymentChannelDTO> paymentChanneList = paymentChannelServiceFeign.selectAll();
        model.addAttribute("paymentChanneList", paymentChanneList);
        model.addAttribute("payToken", payToken);
        return "index";
    }

    /**
     *
     * @param payToken
     * @return
     * @throws IOException
     */
    @RequestMapping("/payHtml")
    public void payHtml(String channelId, String payToken, HttpServletResponse response) throws IOException {
        response.setContentType("text/html; charset=utf-8");
        BaseResponse<JSONObject> payHtmlData = payContextServiceFeign.toPayHtml(channelId, payToken);
        if (isSuccess(payHtmlData)) {
            JSONObject data = payHtmlData.getData();
            String payHtml = data.getString("payHtml");
            response.getWriter().print(payHtml);
        }

    }
}
