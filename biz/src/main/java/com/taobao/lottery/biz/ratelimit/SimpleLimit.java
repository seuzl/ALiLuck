package com.taobao.lottery.biz.ratelimit;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by qingmian.mw on 2016/8/15.
 */
public class SimpleLimit {

	private AtomicInteger requestCount;

	public void doRequest(String threadName) {
		try {
			if (requestCount.decrementAndGet() < 0) {
				System.out.println(threadName + ":请求过多，请稍后再尝试");
			}else {
				System.out.println(threadName + ":您的请求已受理");
			}
		} finally {
			requestCount.incrementAndGet();
		}
	}

	public static void main(String[] args) throws InterruptedException, IOException {
		final SimpleLimit simpleLimit = new SimpleLimit();
		final CountDownLatch latch = new CountDownLatch(1); //保证线程同一时刻start
		simpleLimit.requestCount = new AtomicInteger(10);
		for (int i = 0; i < 50; i++) {
			final int finalI = i;
			Thread t = new Thread(new Runnable() {
				public void run() {
					try {
						latch.await();
						simpleLimit.doRequest("t-" + finalI);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			});
			t.start();
		}
		latch.countDown();
		System.in.read();
	}
}
