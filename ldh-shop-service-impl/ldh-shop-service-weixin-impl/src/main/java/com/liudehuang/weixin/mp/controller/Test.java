package com.liudehuang.weixin.mp.controller;

import com.google.gson.Gson;
import me.chanjar.weixin.common.bean.menu.WxMenu;
import me.chanjar.weixin.common.bean.menu.WxMenuButton;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liudehuang
 * @date 2019/4/28 15:56
 */
public class Test {
    public static void main(String[] args) {
        WxMenu menu = new WxMenu();
        WxMenuButton button1 = new WxMenuButton();
        button1.setName("精选干货");
        WxMenuButton button11 = new WxMenuButton();
        button11.setType("view");
        button11.setName("历史文章");
        button11.setUrl("https://www.jianshu.com/u/706a919ce53a");
        WxMenuButton button12 = new WxMenuButton();
        button12.setType("view");
        button12.setName("面试宝典");
        button12.setUrl("https://www.jianshu.com/users/706a919ce53a/liked_notes");
        button1.getSubButtons().add(button11);
        button1.getSubButtons().add(button12);
        WxMenuButton button2 = new WxMenuButton();
        button2.setName("联系我们");
        button2.setType("click");
        button2.setKey("lianxiwomen");
        WxMenuButton button3 = new WxMenuButton();
        button3.setName("加入我们");
        button3.setUrl("http://www.baidu.com/");
        button3.setType("view");
        menu.getButtons().add(button11);
        menu.getButtons().add(button2);
        menu.getButtons().add(button3);
        /*WxMenuRule rule = new WxMenuRule();
        rule.setCity("中国");
        rule.setClientPlatformType("Android");
        rule.setCity("温州");
        rule.setSex("男");
        rule.setLanguage("zh_CN");
        rule.setCountry("中国");
        rule.setProvince("浙江");
        rule.setTagId("11");
        menu.setMatchRule(rule);*/
        Gson gson = new Gson();
        System.out.println(gson.toJson(menu));
    }
}
