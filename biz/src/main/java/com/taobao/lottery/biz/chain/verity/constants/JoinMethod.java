package com.taobao.lottery.biz.chain.verity.constants;

/**
 * Created by qingmian.mw on 2016/8/10.
 */
public enum JoinMethod {

	URLJOIN(Integer.valueOf(0),"列表进入"),
	SCANCODE(Integer.valueOf(1),"扫码进入");


	private final Integer code;
	private final String  message;

	private JoinMethod(Integer code, String message){
		this.code = code;
		this.message = message;
	}

	public Integer getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}
}
