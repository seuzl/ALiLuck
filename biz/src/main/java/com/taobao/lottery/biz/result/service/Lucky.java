package com.taobao.lottery.biz.result.service;

/**
 * Created by qingmian.mw on 2016/8/13.
 */
public class Lucky{

	String user_name;
	String user_id;

	public Lucky(){}

	public Lucky(String user_name, String user_id) {
		this.user_name = user_name;
		this.user_id = user_id;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
}

