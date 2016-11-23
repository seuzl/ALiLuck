package com.taobao.lottery.biz.ratelimit;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by qingmian.mw on 2016/8/15.
 */
public class TimeLimit {
		private long limit = 5; //限流数

		static volatile boolean exit;

		private LoadingCache<Long, AtomicLong> counter =
				CacheBuilder.newBuilder()
						.expireAfterWrite(2, TimeUnit.SECONDS)
						.build(new CacheLoader<Long, AtomicLong>() {
							@Override
							public AtomicLong load(Long aLong) throws Exception {
								return new AtomicLong(0);
							}
						});


		public void doRequest(String threadName) throws ExecutionException {
			long currentSecond = System.currentTimeMillis() / 1000;
			if (counter.get(currentSecond).incrementAndGet() > limit) {
				System.out.println(threadName + ":请求过多，请稍后再尝试");
			} else {
				System.out.println(threadName + ":您的请求已受理");
			}
		}

	public static void main(String[] args) throws InterruptedException {
		//或等当前秒数

		final TimeLimit timeLimit = new TimeLimit();
		final CountDownLatch latch = new CountDownLatch(1);
		for (int i = 0; i < 10; i++) {
			final int finalI = i;
			Thread t = new Thread(new Runnable() {
				public void run() {
					try {
						latch.await();
						while (!exit) {
							timeLimit.doRequest("t-" + finalI);
							Thread.sleep(1000);
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					} catch (ExecutionException e) {
						e.printStackTrace();
					}
				}
			});
			t.start();
		}
		latch.countDown();
		Thread.sleep(3000);
		exit = true;
	}

}

