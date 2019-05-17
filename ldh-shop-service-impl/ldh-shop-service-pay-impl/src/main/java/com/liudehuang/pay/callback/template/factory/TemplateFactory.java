package com.liudehuang.pay.callback.template.factory;

import com.liudehuang.core.utils.SpringContextUtil;
import com.liudehuang.pay.callback.template.AbstractPayCallBackTemplate;

/**
 * @author liudehuang
 * @date 2019/5/17 14:03
 */
public class TemplateFactory {
    public static AbstractPayCallBackTemplate getPayCallBackTemplate(String beanId) {
        return (AbstractPayCallBackTemplate) SpringContextUtil.getBean(beanId);
    }

}
