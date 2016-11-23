package com.taobao.lottery.biz.result.service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qingmian.mw on 2016/8/13.
 */
public class SingleResult{
	String name;
	int number;
	//命名为lucky是为了在转为jsonstr的时候，默认指定key为lucky
	//最后key的关系在于set和get函数的名字，自动生成的函数名字依据变量名
	List<Lucky> lucky = new ArrayList<Lucky>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public List<Lucky> getLucky() {
		return lucky;
	}

	public void setLucky(List<Lucky> lucky) {
		this.lucky = lucky;
	}
}

