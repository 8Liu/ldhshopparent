package com.liudehuang.portal.controller;

import com.liudehuang.core.base.BaseResponse;
import com.liudehuang.member.feign.MemberServiceFeign;
import com.liudehuang.output.dto.UserOutDTO;
import com.liudehuang.web.base.BaseWebController;
import com.liudehuang.web.constants.WebConstants;
import com.liudehuang.web.utils.CookieUtils;
import io.swagger.annotations.Api;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.jws.WebParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author liudehuang
 * @date 2019/5/7 15:49
 */
@Controller
public class IndexController extends BaseWebController {
    /**
     * 跳转到index页面
     */
    public static final String INDEX_FTL = "index";

    @Autowired
    private MemberServiceFeign memberServiceFeign;


    @RequestMapping("/")
    public String index(HttpServletRequest request, HttpServletResponse response, Model model){

        //1.从cookie中获取token
        String token = CookieUtils.getCookieValue(request, WebConstants.LOGIN_TOKEN_COOKIENAME,true);
        if(!StringUtils.isEmpty(token)){
            //2.根据token获取用户信息
            BaseResponse<UserOutDTO> result = memberServiceFeign.getInfo(token);
            if (isSuccess(result)) {
                UserOutDTO data = result.getData();
                if (data != null) {
                    String mobile = data.getMobile();
                    // 对手机号码实现脱敏
                    String desensMobile = mobile.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
                    model.addAttribute("desensMobile", desensMobile);
                }

            }
        }

        return INDEX_FTL;
    }
}
