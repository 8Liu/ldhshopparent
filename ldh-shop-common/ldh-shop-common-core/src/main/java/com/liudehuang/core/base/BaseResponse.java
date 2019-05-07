package com.liudehuang.core.base;

import lombok.Data;

@Data
public class BaseResponse<T> {
    /**
     * 返回code码
     */
	private Integer rtnCode;
    /**
     * 消息
     */
	private String msg;
    /**
     *
     */
	private T data;

	public BaseResponse() {

	}

	public BaseResponse(Integer rtnCode, String msg, T data) {
		super();
		this.rtnCode = rtnCode;
		this.msg = msg;
		this.data = data;
	}

}