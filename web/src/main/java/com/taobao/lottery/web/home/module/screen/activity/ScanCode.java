package com.taobao.lottery.web.home.module.screen.activity;

import com.alibaba.citrus.turbine.Context;
import com.alibaba.citrus.turbine.TurbineRunData;
import com.alibaba.fastjson.JSON;
import com.taobao.lottery.biz.algorithm.RandomLottery;
import com.taobao.lottery.biz.chain.verity.ClientContext;
import com.taobao.lottery.biz.chain.verity.ao.ScanCodeAO;
import com.taobao.lottery.biz.chain.verity.cache.TokenCache;
import com.taobao.lottery.biz.chain.verity.util.CalendarUtil;
import com.taobao.lottery.biz.chain.verity.util.TokenProcessor;
import com.taobao.lottery.biz.manager.ActivityInfoManager;
import com.taobao.lottery.biz.manager.KeyValueManager;
import com.taobao.lottery.dal.dataObject.ActivityInfo;
import org.apache.commons.lang.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by qingmian.mw on 2016/8/13.
 */
public class ScanCode {

	@Resource
	ScanCodeAO scanCodeAO;

	@Resource
	RandomLottery randomLottery;

	@Resource
    KeyValueManager keyValueManager;

	@Resource
	ActivityInfoManager activityInfoManager;

	public void execute(TurbineRunData runData, Context context) throws Exception{
		HttpServletResponse response = runData.getResponse();
		response.setContentType("application/json");
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Headers", "user_id,access_token,reverse_id,reverse_token");

		/*//因为头部加入信息，解决跨域问题
		if(request.getMethod().equals("OPTIONS")){
			response.setStatus(204);
			response.getWriter().println();
			return;
		}*/

		/* 验证参数 */
		ClientContext clientContext = scanCodeAO.get(runData);
		//参数必须传递user_id  activity_id type=1 三个参数
		// 扫码参与的人员必须在名单内，否者不让其参与活动
		// 验证链返回true,表明验证不通过,直接返回结果
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
		//验证通过后，在response的头中，设置access_token，返回给js的调用方

		//1.生成access_token
		//String accessToken = TokenProcessor.generateTokeCode(userId);
		//2.存入session中,进行下次服务端的匹配
		/*request.getSession().setAttribute("access_token",accessToken);*/
		//多点传输的需求，现在token存入本地cache
		//TokenCache.setToken(userId, accessToken);
		//3.向返回头中access_token,让接下来的调用方有权利调用其他的接口
		//response.setHeader("access_token",accessToken);

		String activityId = clientContext.getActivityId();
		//int activity_id = Integer.valueOf(activityId).intValue();
		int activity_id = Integer.parseInt(activityId);

		//检测用户是否已经参与过刮奖活动
		boolean isJoinActivity = keyValueManager.isUserAlreadyJoined(userId,activity_id);
		//参与过,直接返回获奖结果
		if(isJoinActivity){
			HashMap<String,String> joinActivityResult = new HashMap<String,String>();
			joinActivityResult.put("code","304");
			joinActivityResult.put("msg","用户已经参加了活动");
			String lotteryResult = null;
			try {
				lotteryResult = randomLottery.getSingleWinResult(activity_id, userId);
			}catch (Exception e){
				lotteryResult = null;
			}
			joinActivityResult.put("data",lotteryResult);
			System.out.println(lotteryResult);

			String resultJson = JSON.toJSONString(joinActivityResult);
			response.getWriter().println(resultJson);
			return;
		}

		//3. 第一次刮奖，返回中奖结果
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

		/*//获取本次活动的时间
		ActivityInfo activityInfo = activityInfoManager.getActivityById(activity_id);
		//比较游戏时间和服务器时间，返回结果
		String startTime = activityInfo.getStartTime();
		Date date = CalendarUtil.toDate(startTime,CalendarUtil.TIME_PATTERN);
		long startTimeOfActivity = date.getTime();

		//当前服务器的时间
		long time = System.currentTimeMillis();
		//当前时间加上15秒
		long limitTime = time+15*1000;


		//进来的时间如果在当前活动开始时间十五秒之前，返回活动没开始
		//否者返回不能扫码
		if(startTimeOfActivity>=limitTime){
			//加入participation名单 重新设置抽奖结果
			activityInfoManager.addParticipant(userId,activity_id);
			//重新抽奖
			randomLottery.lottery(activity_id);
			Map<String,String> result = new HashMap<String,String>();
			result.put("code", "200");
			result.put("msg","活动还没开始");
			String jsonResult = JSON.toJSONString(result);
			//返回结果
			response.getWriter().println(jsonResult);
		}//其他返回不能扫码加入活动
		else {
			Map<String,String> result = new HashMap<String,String>();
			result.put("code", "200");
			result.put("msg","已经过了扫码时间，不允许扫码加入");
			String jsonResult = JSON.toJSONString(result);
			//返回结果
			response.getWriter().println(jsonResult);
		}*/

	}
}
