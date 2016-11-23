package com.taobao.lottery.biz.rateswitch;

import com.taobao.csp.switchcenter.annotation.AppSwitch;
import com.taobao.csp.switchcenter.bean.Switch;

/**
 *
 * Created by qingmian.mw on 2016/8/15.
 */
public class RateTypeSwitch {
	@AppSwitch(des = "Boolean 类型开关", level = Switch.Level.p1)
	public static Boolean SimpleLimit = false;

	@AppSwitch(des = "Boolean 类型开关",level = Switch.Level.p1)
	public static Boolean TimeLimit = false;
}
