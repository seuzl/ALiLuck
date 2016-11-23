package com.taobao.lottery.biz.chain.verity.cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.util.concurrent.TimeUnit;

/**
 *  原来的token是存在每个客户端对应的session
 *
 * Created by qingmian.mw on 2016/8/14.
 */
public class TokenCache {

	//user_id 和token对应的缓存
	//模拟tomcat的session机制，设置1个月token活动不失效
	//设置cache大小为100000
	private static LoadingCache<String,String> cacheBuilder= CacheBuilder
			.newBuilder()
			.maximumSize(100000)
			.expireAfterAccess(24*30, TimeUnit.HOURS)
			.build(new CacheLoader<String, String>(){
				@Override
				public String load(String key) throws Exception {
					return null;
				}
			});

	/**
	 * 获取值,没有的话直接返回null
	 * 默认google不让返回null的value，会抛出异常，这里捕获掉，返回null
	 * @param userId
	 * @return
	 */
	public static String getToken(String userId) {
		try {
			return cacheBuilder.get(userId);
		}catch (Exception e){
			//默认google不让返回null的value，会抛出异常，这里捕获掉，返回null
			return null;
		}
	}

	/**
	 * Guava的cache不允许设置key为null的值
	 * @param userId
	 * @param accessToken
	 */
	public static void setToken(String userId, String accessToken){
		cacheBuilder.put(userId,accessToken);
	}
}
