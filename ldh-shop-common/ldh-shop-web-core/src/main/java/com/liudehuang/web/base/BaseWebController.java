package com.liudehuang.web.base;

import com.liudehuang.core.base.BaseResponse;
import com.liudehuang.core.constants.Constants;
import org.springframework.ui.Model;

/**
 * @author liudehuang
 * @date 2019/5/7 16:57
 */
public class BaseWebController {
    /**
     * 500页面
     */
    protected static final String ERROR_500_FTL = "500.ftl";

    public Boolean isSuccess(BaseResponse<?> baseResp) {
        if (baseResp == null) {
            return false;
        }
        if (baseResp.getRtnCode().equals(Constants.HTTP_RES_CODE_500)) {
            return false;
        }
        return true;
    }

    public void setErrorMsg(Model model, String errorMsg) {
        model.addAttribute("error", errorMsg);
    }

}