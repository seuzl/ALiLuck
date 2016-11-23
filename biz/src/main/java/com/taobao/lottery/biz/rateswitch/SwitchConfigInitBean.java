package com.taobao.lottery.biz.rateswitch;

import com.taobao.csp.switchcenter.core.SwitchManager;
import com.taobao.csp.switchcenter.exception.SwitchCenterException;
import org.springframework.core.PriorityOrdered;

/**
 *
 *
 * Created by qingmian.mw on 2016/8/15.
 */
public class SwitchConfigInitBean implements PriorityOrdered {
	private static final String APP_NAME = "lottery";

	public void init(){
		try {
			SwitchManager.init(APP_NAME,RateTypeSwitch.class);
		}catch (SwitchCenterException e){
			e.printStackTrace();

		}
	}

	@Override
	public int getOrder() {
		return HIGHEST_PRECEDENCE;
	}

}
