package com.com.taobao.lottery.biz.result.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.taobao.lottery.biz.result.service.ResultRevertService;
import com.taobao.lottery.biz.result.service.SingleResult;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by qingmian.mw on 2016/8/13.
 */
public class ResultRevertServiceTest {

	@Test
	public void test(){
		String str = "[{\"name\":\"一等奖\",\"user_id\":\"jianghan.jh\",\"user_name\":\"江焰\"},{\"name\":\"二等奖\",\"user_id\":\"xichi.zl\",\"user_name\":\"夕迟\"}]";
		List<SingleResult> lotteryResultList = ResultRevertService.getAllResult(str);
		Map<String,Object> resultMap = new HashMap<String,Object>();
		resultMap.put("code","200");
		resultMap.put("msg","success");
		resultMap.put("data",lotteryResultList);
		String jsonResult = JSON.toJSONString(resultMap);
		System.out.println(jsonResult);
	}

	@Test
	public void test1(){
		String str = "[{\"name\":\"一等奖\",\"user_id\":\"jianghan.jh\",\"user_name\":\"江焰\"},{\"name\":\"二等奖\",\"user_id\":\"xichi.zl\",\"user_name\":\"夕迟\"}]";
		Map<String,Object> resultMap = new HashMap<String,Object>();
		resultMap.put("data",str);
		String jsonResult = JSON.toJSONString(resultMap);
		System.out.println(jsonResult);

	}

	/*@Test
	public void test2(){
		String result = null;
		JSONObject jsonObject = JSON.parseObject(result);
		JSONObject jsonObject1 = jsonObject.getJSONObject("1");
		System.out.println(jsonObject);
	}*/

}
