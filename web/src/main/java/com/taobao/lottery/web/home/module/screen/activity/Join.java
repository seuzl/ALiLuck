package com.taobao.lottery.web.home.module.screen.activity;

import com.alibaba.citrus.turbine.Context;
import com.alibaba.citrus.turbine.TurbineRunData;
import com.alibaba.fastjson.JSON;
import com.taobao.lottery.biz.algorithm.RandomLottery;
import com.taobao.lottery.biz.chain.verity.ClientContext;
import com.taobao.lottery.biz.chain.verity.ao.ChainAO;
import com.taobao.lottery.biz.manager.KeyValueManager;
import org.apache.commons.lang.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by qingmian.mw on 2016/8/11.
 */
public class Join {

	@Resource ChainAO chainAO;

	@Resource RandomLottery randomLottery;

	@Resource
    KeyValueManager keyValueManager;

	public void execute(TurbineRunData runData, Context context) throws Exception{
		HttpServletResponse response = runData.getResponse();
		HttpServletRequest request = runData.getRequest();
		response.setContentType("application/json");
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Headers", "user_id,access_token,reverse_id,reverse_token");

		/*//因为头部加入信息，解决跨域问题
		if(request.getMethod().equals("OPTIONS")){
			response.setStatus(204);
			response.getWriter().println();
			return;
		}*/

		ClientContext clientContext = chainAO.get(runData);
		// 验证链返回true,表明验证不通过,返回结果
		if(!clientContext.isSuccess()){
			String mes = clientContext.getChainExceptionCode().getMessage();
			Integer code = clientContext.getChainExceptionCode().getCode();
			Map<String,String> resultMap = new HashMap<String,String>();
			resultMap.put("code", String.valueOf(code));
			resultMap.put("msg",mes);
			String result = JSON.toJSONString(resultMap);
			response.getWriter().println(result);
			return;
		}

		String userId = clientContext.getUserId();
		String activityId = clientContext.getActivityId();
		//int activity_id = Integer.valueOf(activityId).intValue();
		int activity_id = Integer.parseInt(activityId);

		/*验证通过*/

		//1. 检测用户是否参与过对应活动
		boolean isJoinActivity = keyValueManager.isUserAlreadyJoined(userId, activity_id);
		//2. 参与过,直接返回获奖结果
		if(isJoinActivity){
			//保证参与过对应活动后，一定能查到用户的获奖结果
			HashMap<String,String> joinActivityResult = new HashMap<String,String>();
			joinActivityResult.put("code","304");
			joinActivityResult.put("msg","用户已经参加了活动");
			String lotteryResult = null;
			try {
				//获取用户的获奖结果
				lotteryResult = randomLottery.getSingleWinResult(activity_id, userId);
			}catch (Exception e){
				lotteryResult = null;
			}
			//为了代码的健壮性，如果这里检测lotteryResult的结果是null，
			//重新设置msg，code信息

			/*if(StringUtils.isBlank(lotteryResult)){
				joinActivityResult.put("code", "200");
				joinActivityResult.put("msg","success");
				lotteryResult = "沒有中奖";
			}*/
			joinActivityResult.put("data",lotteryResult);
			System.out.println(lotteryResult);
			String resultJson = JSON.toJSONString(joinActivityResult);
			response.getWriter().println(resultJson);
			return;
		}
		//3. 接下来抽奖操作
		String lotteryResult = randomLottery.getSingleWinResult(activity_id,userId);
		//4. 用户参与过写入数据库,以便下次查询
		keyValueManager.userJoinActivity(userId,activity_id);
		//5. 用户抽奖结果写入数据库,其实早就写入数据库了，省略

		//向客户端返回抽奖结果
		Map<String,String> result = new HashMap<String,String>();
		result.put("code", "200");
		result.put("msg","success");
		/*if(StringUtils.isBlank(lotteryResult)){
			lotteryResult = "沒有中奖";
		}*/
		result.put("data", lotteryResult);
		String jsonResult = JSON.toJSONString(result);

		//返回结果
		response.getWriter().println(jsonResult);
	}
}
