package com.taobao.lottery.web.home.module.screen.reward;

import com.alibaba.citrus.turbine.Context;
import com.alibaba.citrus.turbine.TurbineRunData;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.taobao.lottery.biz.manager.ActivityInfoManager;
import com.taobao.lottery.biz.manager.MyStringUtil;
import com.taobao.lottery.dal.dataObject.ActivityInfo;
import org.apache.commons.lang.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by qingmian.mw on 2016/8/12.
 */
public class RewardOfActivity {

	@Resource
	private ActivityInfoManager activityInfoManager;

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

		//传入参数为空,返回
		if(StringUtils.isBlank(activityId)){
			Map<String,String> resultMap = new HashMap<String,String>();
			resultMap.put("code","500");
			resultMap.put("msg","参数不能为空");
			String rewardOfActivityStr = JSON.toJSONString(resultMap);
			response.getWriter().println(rewardOfActivityStr);
			return;
		}

		//int activity_id = Integer.valueOf(activityId).intValue();
		int activity_id = Integer.parseInt(activityId);
		ActivityInfo activityInfo = activityInfoManager.getActivityById(activity_id);

		//没有这个活动,返回
		if(null==activityInfo) {
			Map<String,String> resultMap = new HashMap<String,String>();
			resultMap.put("code","406");
			resultMap.put("msg","没有这个活动");
			String rewardOfActivityStr = JSON.toJSONString(resultMap);
			response.getWriter().println(rewardOfActivityStr);
			return;
		}else{//正常返回
			String priceCategory = activityInfo.getPrize();
			priceCategory = MyStringUtil.trimSqlResultToJson(priceCategory);
			System.out.println("priceCategory:"+priceCategory);
			Map<String,String> resultMap = new HashMap<String,String>();
			resultMap.put("code","200");
			resultMap.put("msg","success");
			resultMap.put("data",priceCategory);
			String rewardOfActivityStr = JSON.toJSONString(resultMap);
			rewardOfActivityStr = MyStringUtil.trimSqlResultToJson(rewardOfActivityStr);
			System.out.println("rewardOfActivityStr:"+rewardOfActivityStr);
			response.getWriter().println(rewardOfActivityStr);
			return;
		}

	}


}
