package com.taobao.lottery.biz.ratelimit;

import com.taobao.lottery.biz.rateswitch.RateTypeSwitch;
import com.taobao.lottery.biz.rateswitch.SwitchConfigInitBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * Created by qingmian.mw on 2016/8/15.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:switch-config.xml"})
public class RateTimeTest {

	@Resource
	SwitchConfigInitBean switchInitBean;

	@Test
	public void test(){
		System.out.println("switchInitBean:"+switchInitBean);

		System.out.println("SimpleLimit:"+RateTypeSwitch.SimpleLimit);
		if(RateTypeSwitch.SimpleLimit){
			System.out.println("simpleLimit is running");
		}

		System.out.println("TimeLimit:"+RateTypeSwitch.TimeLimit);
		if(RateTypeSwitch.TimeLimit) {
			System.out.println("timeLimit is runnning");
		}
	}
}
