package com.liudehuang.member.controller;

import com.alibaba.fastjson.JSONObject;
import com.liudehuang.core.base.BaseResponse;
import com.liudehuang.core.utils.MeiteBeanUtils;
import com.liudehuang.input.dto.UserInpDTO;
import com.liudehuang.member.entity.vo.RegisterVo;
import com.liudehuang.member.feign.MemberRegisterServiceFeign;
import com.liudehuang.member.service.MemberRegisterService;
import com.liudehuang.web.base.BaseWebController;
import com.liudehuang.web.utils.RandomValidateCodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;

@Controller
public class RegisterController extends BaseWebController {
	private static final String MEMBER_REGISTER_FTL = "member/register";

	private static final String MEMBER_LOGIN_FTL = "member/login";

	@Autowired
	private MemberRegisterServiceFeign memberRegisterServiceFeign;

	/**
	 * 跳转到注册页面
	 * 
	 * @return
	 */
	@GetMapping("/register")
	public String getRegister() {
		return MEMBER_REGISTER_FTL;
	}

	/**
	 * 跳转到注册页面
	 * 
	 * @return
	 */
	@PostMapping("/register")
	public String postRegister(@ModelAttribute("registerVo") @Validated RegisterVo registerVo, BindingResult bindingResult, Model model, HttpSession session) {
		//1、接收表单参数，创建接受对象
		if(bindingResult.hasErrors()){
			//如果参数验证错误
			String errorMsg = bindingResult.getFieldError().getDefaultMessage();
			setErrorMsg(model,errorMsg);
			return MEMBER_REGISTER_FTL;
		}
		//2.判断图形验证是否正确
		String code = registerVo.getGraphicCode();
		Boolean flag = RandomValidateCodeUtil.checkVerify(code, session);
		if(!flag){
			setErrorMsg(model,"图形验证码不正确");
		}
		//3、调用会员服务接口实现注册,将VO转换成DTO
		UserInpDTO userInpDTO = MeiteBeanUtils.voToDto(registerVo,UserInpDTO.class);
		BaseResponse<JSONObject> response = memberRegisterServiceFeign.register(userInpDTO, registerVo.getRegistCode());
		if(!isSuccess(response)){
			setErrorMsg(model,response.getMsg());
			return MEMBER_REGISTER_FTL;
		}

		return MEMBER_LOGIN_FTL;
	}

}