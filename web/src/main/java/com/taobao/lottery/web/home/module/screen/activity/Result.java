package com.taobao.lottery.web.home.module.screen.activity;

/**
 * Created by qingmian.mw on 2016/8/11.
 */

import com.alibaba.citrus.turbine.Context;
import com.alibaba.citrus.turbine.TurbineRunData;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.taobao.lottery.biz.algorithm.RandomLottery;
import com.taobao.lottery.biz.manager.ActivityResultManager;
import com.taobao.lottery.biz.manager.MyStringUtil;
import org.apache.commons.lang.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.util.HashMap;
import java.util.Map;

public class Result {

	@Resource ActivityResultManager activityResultManager;

	@Resource RandomLottery randomLottery;

	public void execute(TurbineRunData runData, Context context) throws Exception {
		HttpServletResponse response = runData.getResponse();
		response.setContentType("application/json");
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Headers", "user_id,access_token,reverse_id,reverse_token");

		StringBuffer applicationJson = new StringBuffer();
		String line = null;
		try {
			BufferedReader reader = runData.getRequest().getReader();
			while ((line = reader.readLine()) != null)
				applicationJson.append(line);
		} catch (Exception e) {
			/*report an error*/
			e.printStackTrace();
		}
		System.out.println("json is  = " + applicationJson);
		HashMap<String,String> data  = JSONObject
				.parseObject(applicationJson.toString(),new TypeReference<HashMap<String,String> >() {
				});

		//int activity_id = runData.getParameters().getInt("activity_id");
		//String userId = runData.getRequest().getParameter("user_id");
		String activityId = data.get("activity_id");
		//int activity_id = Integer.valueOf(activityId).intValue();
		int activity_id = Integer.parseInt(activityId);
		String userId = data.get("user_id");

		System.out.println("activity_id:"+activityId);
		System.out.println("user_id:"+userId);

		String lotteryResult = null;

		//如果只有一个activity_id ,最终获奖情况
		if(StringUtils.isBlank(userId)){
			lotteryResult = activityResultManager.getResultByActivityId(activity_id);
			//处理结果的转义字符
			if(!StringUtils.isBlank(lotteryResult)) {
				lotteryResult = MyStringUtil.trimSqlResultToJson(lotteryResult);
				System.out.println("lotteryResult:"+lotteryResult);
				/*
				  原来的版本里面数据是这样
				  [{"name":"一等奖","user_id":"jianghan.jh","user_name":"江焰"},{"name":"二等奖","user_id":"xichi.zl","user_name":"夕迟"}]
				  现在数据库存储的是这样
				  [{"lucky":[{"user_id":"jianghan.jh","user_name":"江焰"},{"user_id":"qingmian.mw","user_name":"晴眠"}],"name":"一等奖","number":2},{"lucky":[{"user_id":"xichi.zl","user_name":"夕迟"},{"user_id":"hanqing.ghq","user_name":"翰卿"}],"name":"二等奖","number":2}]
				 */
				//不需要转换
				//List<SingleResult>lotteryResultList = ResultRevertService.getAllResult(lotteryResult);

				Map<String,String> resultMap = new HashMap<String, String>();
				resultMap.put("code","200");
				resultMap.put("msg","success");
				//resultMap.put("data",lotteryResultList);
				resultMap.put("data", lotteryResult);
				String jsonResult = JSON.toJSONString(resultMap);
				jsonResult = MyStringUtil.trimSqlResultToJson(jsonResult);
				response.getWriter().println(jsonResult);
				return;
			}
			else{
				Map<String,String> result = new HashMap<String,String>();
				result.put("code", "404");
				result.put("msg","没有活动的获奖信息");
				String jsonResult = JSON.toJSONString(result);
				response.getWriter().println(jsonResult);
				return;
			}
		}//否则个人中奖结果
		else{

			try {
				//randomLottery 调用的是 activityResultManager中的中奖结果。查询后返回单人结果
				lotteryResult = randomLottery.getSingleWinResult(activity_id, userId);
				//查询结果为空
				if(StringUtils.isBlank(lotteryResult)) {
					Map<String,String> result = new HashMap<String,String>();
					result.put("code", "405");
					result.put("msg","没有用户的获奖信息");
					String jsonResult = JSON.toJSONString(result);
					response.getWriter().println(jsonResult);
					return;
				}else{
					//有正常的结果
					Map<String,String> result = new HashMap<String,String>();
					result.put("code", "200");
					result.put("msg","success");
					result.put("data",lotteryResult);
					String jsonResult = JSON.toJSONString(result);
					response.getWriter().println(jsonResult);
					return;
				}
			}catch (Exception e){
					Map<String,String> result = new HashMap<String,String>();
					result.put("code", "405");
					result.put("msg","没有用户的获奖信息");
					String jsonResult = JSON.toJSONString(result);
					response.getWriter().println(jsonResult);
					return;
			}
		}

	}

}

