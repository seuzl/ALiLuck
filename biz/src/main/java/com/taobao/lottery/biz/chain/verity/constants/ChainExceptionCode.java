package com.taobao.lottery.biz.chain.verity.constants;

/**
 * Created by qingmian.mw on 2016/8/9.
 * 验证链中的状态码
 */
public enum ChainExceptionCode {

	DEFALUT_CHECK(Integer.valueOf(0),"验证异常失败"),
	ACTIVITYID_CHECK(Integer.valueOf(1), "活动不能为空"),
	WORKID_CHECK(Integer.valueOf(2), "用户不能为空"),
	DEPARTMENGT_CHECK(Integer.valueOf(3), "部门不能为空"),
	IDENTITY_CHECK(Integer.valueOf(4), "不在活动的名单中"),
	POSITION_CHECK(Integer.valueOf(5),"不在地理位置范围内"),
	TYPE_CHECK(Integer.valueOf(6),"扫码type不能为空");

	private final Integer code;
	private final String message;

	private ChainExceptionCode(Integer code, String message){
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
