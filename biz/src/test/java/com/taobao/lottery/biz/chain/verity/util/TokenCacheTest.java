package com.taobao.lottery.biz.chain.verity.util;

import com.taobao.lottery.biz.chain.verity.cache.TokenCache;
import org.junit.Test;

/**
 * Created by qingmian.mw on 2016/8/14.
 */
public class TokenCacheTest {
	@Test
	public void test() throws Exception{
		String key = "a";
		String key1 = "b";
		String value1 = "bb";
		String result = TokenCache.getToken(key);
		System.out.println("result = " + result);
		String result1= TokenCache.getToken(key1);
		System.out.println("result1 = " + result1);
		TokenCache.setToken(key1, value1);
		String result2= TokenCache.getToken(key1);
		System.out.println("result2 = " + result2);

		/*String key2 = null;
		TokenCache.setToken(key2,"aaa");
		String resultKey2 = TokenCache.getToken(key2);*/

		String key3 = null;
		String result4 = TokenCache.getToken(key3);
		System.out.println("result4 = " + result4);
	}


	@Test
	public void cacheTest(){
		String keyStr = "key";
		String valueStr = "values";
		TokenCache.setToken(keyStr,valueStr);
		for (int i = 0; i <10;i++){
			String value = TokenCache.getToken(keyStr);
			System.out.println("value = " + value);
		}
	}
}
