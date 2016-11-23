package com.taobao.lottery.biz.ratelimit;

import com.google.common.util.concurrent.RateLimiter;

/**
 * Created by qingmian.mw on 2016/8/15.
 */
public class MockUsage {

	RateLimiter rateLimiter = RateLimiter.create(10); //TPS为10

	public void doRequest(String threadName) {
		boolean isAcquired = rateLimiter.tryAcquire();
		if (isAcquired) {
			System.out.println(threadName + ":下单成功");
		} else {
			System.out.println(threadName + ":当前下单人数过多，请稍后再试");
		}
	}
}
