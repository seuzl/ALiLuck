package com.taobao.lottery.biz.buc;

import com.taobao.lottery.biz.buc.BucService;
import org.junit.Test;

/**
 * Created by qingmian.mw on 2016/8/11.
 */
public class BucServiceTest {
	@Test
	public void testEmp() throws Exception{
		//String result = BucService.getEmp();
		//System.out.println(result);
	}

	@Test
	public void testAuth() throws Exception{
		String account = "1";
		String password = "1";
		String result = BucService.getAuth(account, password);
		System.out.println(result);
	}


	@Test
	public void testAuth2() throws Exception{
		String account = "1";
		String password = null;
		String result = BucService.getAuth(account, password);
		System.out.println(result);
	}

	/*@Test
	public void testFuzzy()throws Exception{
		String result = BucService.getFuzzy("");
		System.out.println("result:"+result);
	}*/

	@Test
	public void test() throws Exception{
//		String result1 = BucService.getNickNameCn("qingmian.mw");
//		System.out.println("result:"+result1);


		String result1 = BucService.getName("qingmian.mw");
		System.out.println("result:"+result1);

	}

//    @Test
//    public void testNickName(){
//        String result = BucService.getNickName("hanqing.ghq");
//        System.out.println(result);
//    }
}
