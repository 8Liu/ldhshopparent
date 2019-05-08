package com.liudehuang.core.base;

import com.liudehuang.core.constants.Constants;
import lombok.Data;
import org.springframework.stereotype.Component;

/**
 *
 *公共返回类
 * @author liudehuang
 * @date 2019/4/29
 */
@Data
@Component
public class BaseApiService<T> {

	public BaseResponse<T> setResultError(Integer code, String msg) {
		return setResult(code, msg, null);
	}

	/**
	 * 返回错误，可以传msg
	 *
	 * @param msg
	 * @return
	 */
	public BaseResponse<T> setResultError(String msg) {
		return setResult(Constants.HTTP_RES_CODE_500, msg, null);
	}


	/**
	 * 返回成功，可以传data值
	 *
	 * @param data
	 * @return
	 */
	public BaseResponse<T> setResultSuccess(T data) {
		return setResult(Constants.HTTP_RES_CODE_200, Constants.HTTP_RES_CODE_200_VALUE, data);
	}

	/**
	 * 返回成功，沒有data值
	 *
	 * @return
	 */
	public BaseResponse<T> setResultSuccess() {
		return setResult(Constants.HTTP_RES_CODE_200, Constants.HTTP_RES_CODE_200_VALUE, null);
	}

	/** 返回成功，沒有data值
	 *
	 * @param msg
	 * @return
	 */
	public BaseResponse<T> setResultSuccess(String msg) {
		return setResult(Constants.HTTP_RES_CODE_200, msg, null);
	}

	/**
	 * 通用封装
	 *
	 * @param code
	 * @param msg
	 * @param data
	 * @return
	 */
	public BaseResponse<T> setResult(Integer code, String msg, T data) {
		return new BaseResponse(code, msg, data);
	}

	/**
	 * 调用数据库层判断
 	 */
	public Boolean toDaoResult(int result) {
		return result > 0 ? true : false;
	}
}