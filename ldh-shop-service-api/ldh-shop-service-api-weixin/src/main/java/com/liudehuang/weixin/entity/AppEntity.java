package com.liudehuang.weixin.entity;

import lombok.Data;

/**
 * @author liudehuang
 * @date 2019/4/28 8:53
 */
@Data
public class AppEntity {
    /**
     * appId
     */
    private String appId;
    /**
     * 应用名称
     */
    private String appName;

    public AppEntity(String appId, String appName) {
        this.appId = appId;
        this.appName = appName;
    }

    public AppEntity() {
    }
}
