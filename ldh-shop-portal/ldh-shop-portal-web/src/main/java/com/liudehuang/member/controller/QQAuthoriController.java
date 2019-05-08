package com.liudehuang.member.controller;

import com.alibaba.fastjson.JSONObject;
import com.liudehuang.core.base.BaseResponse;
import com.liudehuang.core.constants.Constants;
import com.liudehuang.core.utils.MeiteBeanUtils;
import com.liudehuang.input.dto.UserLoginInpDTO;
import com.liudehuang.member.entity.vo.LoginVo;
import com.liudehuang.member.feign.MemberLoginServiceFeign;
import com.liudehuang.member.feign.QQAuthoriServiceFeign;
import com.liudehuang.web.base.BaseWebController;
import com.liudehuang.web.constants.WebConstants;
import com.liudehuang.web.utils.CookieUtils;
import com.qq.connect.QQConnectException;
import com.qq.connect.api.OpenID;
import com.qq.connect.api.qzone.UserInfo;
import com.qq.connect.javabeans.AccessToken;
import com.qq.connect.javabeans.qzone.UserInfoBean;
import com.qq.connect.oauth.Oauth;
import lombok.extern.slf4j.Slf4j;
import nl.bitwalker.useragentutils.Browser;
import nl.bitwalker.useragentutils.UserAgent;
import nl.bitwalker.useragentutils.Version;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author liudehuang
 * @date 2019/5/8 13:45
 */
@Controller
@Slf4j
public class QQAuthoriController extends BaseWebController {

    private static final String MEMBER_QQ_LOGIN = "member/qqlogin";

    private static final String REDIRECT_INDEX = "redirect:/";

    @Autowired
    private MemberLoginServiceFeign memberLoginServiceFeign;

    @Autowired
    private QQAuthoriServiceFeign qqAuthoriServiceFeign;

    @RequestMapping("/createQQAuth")
    public String createQQAuth(HttpServletRequest request){
        try{
            String authorizeURL = new Oauth().getAuthorizeURL(request);
            log.info("授权链接：{}",authorizeURL);
            return "redirect:"+authorizeURL;
        }catch (Exception e){
            return ERROR_500_FTL;
        }
    }


    @RequestMapping("/qqLoginBack")
    public String qqLoginBack(String code, HttpServletRequest request, HttpServletResponse response){
        //1.获取授权码
        try {
            //2.使用授权码获取accessToken
            AccessToken accessTokenObj = (new Oauth()).getAccessTokenByRequest(request);
            if(accessTokenObj == null){
                return ERROR_500_FTL;
            }
            String accessToken = accessTokenObj.getAccessToken();
            if(accessToken == null){
                return ERROR_500_FTL;
            }
            //使用accessToken获取openId
            OpenID openIdObj = new OpenID(accessToken);
            String openId = openIdObj.getUserOpenID();
            if(StringUtils.isEmpty(openId)){
                return ERROR_500_FTL;
            }
            //使用openId获取用户信息
            BaseResponse<JSONObject> result = qqAuthoriServiceFeign.findByOpenId(openId, "PC");
            if(!isSuccess(result)){
                return ERROR_500_FTL;
            }
            if(result.getRtnCode().equals(Constants.HTTP_RES_CODE_EXISTMOBILE_203)){
                //页面需要展示qq头像
                UserInfo userInfoObj = new UserInfo(accessToken,openId);
                UserInfoBean userInfo = userInfoObj.getUserInfo();
                if(userInfo == null){
                    return ERROR_500_FTL;
                }
                //获取qq头像
                String avatarURL100 = userInfo.getAvatar().getAvatarURL100();
                request.setAttribute("avatarURL100",avatarURL100);
                request.getSession().setAttribute(WebConstants.LOGIN_QQ_OPENID,openId);
                return MEMBER_QQ_LOGIN;
            }
            //如果能购查询到用户信息，直接自动登陆
            JSONObject data = result.getData();
            String token = data.getString("token");
            CookieUtils.setCookie(request, response, WebConstants.LOGIN_TOKEN_COOKIENAME, token);
            return REDIRECT_INDEX;
        } catch (QQConnectException e) {
            e.printStackTrace();
            return ERROR_500_FTL;
        }

    }


    @RequestMapping("/qqJointLogin")
    public String qqJointLogin(@ModelAttribute("loginVo") LoginVo loginVo, Model model, HttpServletRequest request,
                               HttpServletResponse response, HttpSession httpSession) {

        // 1.获取用户openid
        String qqOpenId = (String) httpSession.getAttribute(WebConstants.LOGIN_QQ_OPENID);
        if (StringUtils.isEmpty(qqOpenId)) {
            return ERROR_500_FTL;
        }

        // 2.将vo转换dto调用会员登陆接口
        UserLoginInpDTO userLoginInpDTO = MeiteBeanUtils.voToDto(loginVo, UserLoginInpDTO.class);
        userLoginInpDTO.setQqOpenId(qqOpenId);
        userLoginInpDTO.setLoginType(Constants.MEMBER_LOGIN_TYPE_PC);
        String info = webBrowserInfo(request);
        userLoginInpDTO.setDeviceInfor(info);
        BaseResponse<JSONObject> login = memberLoginServiceFeign.login(userLoginInpDTO);
        if (!isSuccess(login)) {
            setErrorMsg(model, login.getMsg());
            return MEMBER_QQ_LOGIN;
        }
        // 3.登陆成功之后如何处理 保持会话信息 将token存入到cookie 里面 首页读取cookietoken 查询用户信息返回到页面展示
        JSONObject data = login.getData();
        String token = data.getString("token");
        CookieUtils.setCookie(request, response, WebConstants.LOGIN_TOKEN_COOKIENAME, token);
        return REDIRECT_INDEX;
    }


    public String webBrowserInfo(HttpServletRequest request) {
        // 获取浏览器信息
        Browser browser = UserAgent.parseUserAgentString(request.getHeader("User-Agent")).getBrowser();
        // 获取浏览器版本号
        Version version = browser.getVersion(request.getHeader("User-Agent"));
        String info = browser.getName() + "/" + version.getVersion();
        return info;
    }
}
