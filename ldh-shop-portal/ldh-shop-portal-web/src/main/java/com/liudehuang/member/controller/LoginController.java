package com.liudehuang.member.controller;

import com.alibaba.fastjson.JSONObject;
import com.liudehuang.core.base.BaseResponse;
import com.liudehuang.core.constants.Constants;
import com.liudehuang.core.utils.MeiteBeanUtils;
import com.liudehuang.input.dto.UserLoginInpDTO;
import com.liudehuang.member.entity.vo.LoginVo;
import com.liudehuang.member.feign.MemberLoginServiceFeign;
import com.liudehuang.web.base.BaseWebController;
import com.liudehuang.web.constants.WebConstants;
import com.liudehuang.web.utils.CookieUtils;
import com.liudehuang.web.utils.RandomValidateCodeUtil;
import nl.bitwalker.useragentutils.Browser;
import nl.bitwalker.useragentutils.UserAgent;
import nl.bitwalker.useragentutils.Version;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author liudehuang
 * @date 2019/5/7 15:53
 */
@Controller
public class LoginController extends BaseWebController {

    public static final String MEMBER_LOGIN_FTL = "member/login";


    private static final String REDIRECT_INDEX = "redirect:/";

    @Autowired
    private MemberLoginServiceFeign memberLoginServiceFeign;

    /**
     * 跳转页面
     * @return
     */
    @GetMapping("/login")
    public String getLogin(){
        return MEMBER_LOGIN_FTL;
    }

    /**
     * 接收请求
     * @return
     */
    @PostMapping("/login")
    public String postLogin(@ModelAttribute("loginVo") LoginVo loginVo, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session){
        // 1.图形验证码判断
        String graphicCode = loginVo.getGraphicCode();
        if (!RandomValidateCodeUtil.checkVerify(graphicCode, session)) {
            setErrorMsg(model, "图形验证码不正确!");
            return MEMBER_LOGIN_FTL;
        }
        // 2.将vo转换为dto
        UserLoginInpDTO voToDto = MeiteBeanUtils.voToDto(loginVo, UserLoginInpDTO.class);
        voToDto.setLoginType(Constants.MEMBER_LOGIN_TYPE_PC);
        String info = webBrowserInfo(request);
        voToDto.setDeviceInfor(info);
        BaseResponse<JSONObject> login = memberLoginServiceFeign.login(voToDto);
        if (!isSuccess(login)) {
            setErrorMsg(model, login.getMsg());
            return MEMBER_LOGIN_FTL;
        }
        // 2.登录成功后，将token存入到cookie中
        JSONObject data = login.getData();
        String token = data.getString("token");
        CookieUtils.setCookie(request, response, WebConstants.LOGIN_TOKEN_COOKIENAME, token);
        return REDIRECT_INDEX;
    }

    @GetMapping("/exit")
    public String exit(HttpServletRequest request,HttpServletResponse response){
        //1、获取token
        String token = CookieUtils.getCookieValue(request,WebConstants.LOGIN_TOKEN_COOKIENAME,true);
        BaseResponse<JSONObject> result = memberLoginServiceFeign.exit(token);
        if(isSuccess(result)){
            return MEMBER_LOGIN_FTL;
        }

        return ERROR_500_FTL;
    }


    /**
     * 获取浏览器信息
     *
     * @return
     */
    public String webBrowserInfo(HttpServletRequest request) {
        // 获取浏览器信息
        Browser browser = UserAgent.parseUserAgentString(request.getHeader("User-Agent")).getBrowser();
        // 获取浏览器版本号
        Version version = browser.getVersion(request.getHeader("User-Agent"));
        String info = browser.getName() + "/" + version.getVersion();
        return info;
    }

}
