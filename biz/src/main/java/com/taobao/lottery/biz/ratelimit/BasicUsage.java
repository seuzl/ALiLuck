package com.taobao.lottery.biz.ratelimit;

import com.google.common.util.concurrent.RateLimiter;

/**
 * Created by qingmian.mw on 2016/8/15.
 */
public class BasicUsage {

	//之前的限流方式都不能很好地应对突发请求，即瞬间请求可能都被允许从而导致一些问题；因此在一些场景中需要对突发请求进行整形，整形为平均速率请求处理（比如5r/s，则每隔200毫秒处理一个请求，平滑了速率）。
	//这个时候有两种算法满足我们的场景：令牌桶和漏桶算法。Guava框架提供了令牌桶算法实现，可直接拿来使用。

	public static void main(String[] args) {
		RateLimiter rateLimiter = RateLimiter.create(5); //令牌桶容量为5，即每200毫秒产生1个令牌
		System.out.println(rateLimiter.acquire()); //阻塞获取一个令牌
		System.out.println(rateLimiter.acquire());
		System.out.println(rateLimiter.acquire());
		System.out.println(rateLimiter.acquire());
		System.out.println(rateLimiter.acquire());
		System.out.println(rateLimiter.acquire());
	}

	public static void main1(String[] args) {
		RateLimiter rateLimiter = RateLimiter.create(5); //令牌桶容量为5，即每200毫秒产生1个令牌
		System.out.println(rateLimiter.acquire(10)); //透支令牌
		System.out.println(rateLimiter.acquire());
		System.out.println(rateLimiter.acquire());
		System.out.println(rateLimiter.acquire());
		System.out.println(rateLimiter.acquire());
		System.out.println(rateLimiter.acquire());
	}

	public static void main2(String[] args) throws InterruptedException {
		RateLimiter rateLimiter = RateLimiter.create(1000); //每秒投放1000个令牌
		for (int i = 0; i < 10; i++) {
			if (rateLimiter.tryAcquire()) { //tryAcquire检测有没有可用的令牌，结果马上返回
				System.out.println("处理请求");
			} else {
				System.out.println("拒绝请求");
			}
		}
	}
}
