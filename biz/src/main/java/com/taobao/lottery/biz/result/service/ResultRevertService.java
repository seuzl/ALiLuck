package com.taobao.lottery.biz.result.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by qingmian.mw on 2016/8/13.
 */
public class ResultRevertService {

	/**
	 * lotteryResult:
	 * [{"awardName":"一等奖","id":"jianghan.jh"},{"awardName":"二等奖","id":"guangyu.tgy"},{"awardName":"二等奖","id":"xichi.zl"}]
	 * [{"name":"一等奖","user_id":"jianghan.jh","user_name":"江焰"},{"name":"二等奖","user_id":"xichi.zl","user_name":"夕迟"}]
	 *
	 * @param lotteryResult
	 * @return
	 */
	public static List<SingleResult> getAllResult(String lotteryResult){

		Map<String,SingleResult> allResult = new HashMap<String,SingleResult>();

		JSONArray jsonArray = JSON.parseArray(lotteryResult);

		for(int i=0; i<jsonArray.size(); i++){
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			String name =jsonObject.getString("name");
			String user_id = jsonObject.getString("user_id");
			String user_name = jsonObject.getString("user_name");
			Lucky lucky = new Lucky(user_name,user_id);
			SingleResult singleResult = null;
			//1.判断map有没有awardName
			if(allResult.containsKey(name)){
				//2.获取对应奖项,获奖者的列表
				singleResult = allResult.get(name);
				List<Lucky> luckyList = singleResult.getLucky();
				//3.添加获奖者信息到对应award的列表
				luckyList.add(lucky);
			}else{
				//4.没有award，建立新的award
				singleResult = new SingleResult();
				singleResult.setName(name);
				List<Lucky> luckyList = singleResult.getLucky();
				//5.添加获奖者信息到对应award的列表
				luckyList.add(lucky);
				//6.加入到map中
				allResult.put(name,singleResult);
			}
		}

		//遍历map所有的value，设置size大小，添加到最后结果的List
		List<SingleResult> singleResultList = new ArrayList<SingleResult>();
		for (SingleResult value : allResult.values()) {
			List<Lucky> luckies = value.getLucky();
			int number = luckies.size();
			//1.设置number
			value.setNumber(number);
			//2.加入返回结果
			singleResultList.add(value);
		}

		//返回List
		return singleResultList;

	}
}

