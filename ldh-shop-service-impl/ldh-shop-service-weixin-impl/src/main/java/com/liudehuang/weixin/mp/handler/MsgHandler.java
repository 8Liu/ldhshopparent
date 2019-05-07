package com.liudehuang.weixin.mp.handler;

import com.liudehuang.core.base.BaseResponse;
import com.liudehuang.core.constants.Constants;
import com.liudehuang.core.utils.RedisUtil;
import com.liudehuang.core.utils.RegexUtils;
import com.liudehuang.member.entity.UserEntity;
import com.liudehuang.output.dto.UserOutDTO;
import com.liudehuang.weixin.feign.MemberServiceFeign;
import com.liudehuang.weixin.mp.builder.TextBuilder;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

import static me.chanjar.weixin.common.api.WxConsts.XmlMsgType;

/**
 * @author Binary Wang(https://github.com/binarywang)
 */
@Component
@Slf4j
public class MsgHandler extends AbstractHandler {
    /**
     * 发送验证码数据
     */
    @Value("${ldh.weixin.registration.code.message}")
    private String registrationCodeMessage;

    /**
     * 默认回复消息
     */
    @Value("${ldh.weixin.default.registration.code.message}")
    private String defaultRegistrationCodeMessage;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private MemberServiceFeign memberServiceFeign;


	@Override
	public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context, WxMpService weixinService,
			WxSessionManager sessionManager) {

		if (!wxMessage.getMsgType().equals(XmlMsgType.EVENT)) {
			// TODO 可以选择将消息保存到本地
		}

		if(wxMessage.getMsgType().equals(XmlMsgType.IMAGE)){
		    return new TextBuilder().build(defaultRegistrationCodeMessage, wxMessage, weixinService);
        }

		// 当用户输入关键词如“你好”，“客服”等，并且有客服在线时，把消息转发给在线客服
		try {
			if (StringUtils.startsWithAny(wxMessage.getContent(), "你好", "客服")
					&& weixinService.getKefuService().kfOnlineList().getKfOnlineList().size() > 0) {
				return WxMpXmlOutMessage.TRANSFER_CUSTOMER_SERVICE().fromUser(wxMessage.getToUser())
						.toUser(wxMessage.getFromUser()).build();
			}
		} catch (WxErrorException e) {
			e.printStackTrace();
		}
		//1、获取微信客户端发送的消息
		String fromContent = wxMessage.getContent();
		log.info("fromContent:" + fromContent);
        //2、使用正则表达式验证消息是否为手机号码格式
        String content = null;
        if(RegexUtils.checkMobile(fromContent)){
            //1.根据手机号码调用会员服务接口查询用户信息是否存在
            BaseResponse<UserOutDTO> response = memberServiceFeign.existMobile(fromContent);
            if(response.getRtnCode().equals(Constants.HTTP_RES_CODE_200)){
                content = "该手机号码"+fromContent+"已经注册过了";
                return new TextBuilder().build(content, wxMessage, weixinService);
            }
            //调用接口报错
            if(!response.getRtnCode().equals(Constants.HTTP_RES_CODE_EXISTMOBILE_203)){
                return new TextBuilder().build(response.getMsg(), wxMessage, weixinService);
            }
            //3.如果是手机号格式的话，则发送注册码
			//5、将注册码和手机号存入redis中
            int code = registCode();
            content = String.format(registrationCodeMessage,code);
            redisUtil.setString(Constants.WEIXINCODE_KEY+fromContent,code+"",Constants.WEIXINCODE_TIMEOUT);
        }else{
        	//4.如果不是的话，则发送默认消息
            content = defaultRegistrationCodeMessage;
        }
		return new TextBuilder().build(content, wxMessage, weixinService);

	}

    /**
     *  获取注册码
      */
    private int registCode() {
        int registCode = (int) (Math.random() * 9000 + 1000);
        return registCode;
    }

}
