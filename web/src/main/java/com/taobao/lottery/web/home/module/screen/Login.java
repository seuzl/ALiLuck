package com.taobao.lottery.web.home.module.screen;

import com.alibaba.citrus.turbine.Context;
import com.alibaba.citrus.turbine.TurbineRunData;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.taobao.lottery.biz.algorithm.RSAsecurity;
import com.taobao.lottery.biz.buc.BucService;
import com.taobao.lottery.biz.chain.verity.cache.TokenCache;
import com.taobao.lottery.biz.chain.verity.util.TokenProcessor;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by qingmian.mw on 2016/8/11.
 */
public class Login {

	public void execute(TurbineRunData runData, Context context) throws Exception{
		HttpServletResponse response = runData.getResponse();
		response.setContentType("application/json");


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

		/*String user_id = runData.getParameters().getString("user_id");
		String password = runData.getParameters().getString("password");*/
		String userId = data.get("user_id");
		String encryptPassword = data.get("password");
		String password = RSAsecurity.decryptCipher(encryptPassword);
		/*String password = data.get("password");*/

		String authResult = BucService.getAuth(userId,password);

		//解析result结果
		JSONObject jsonObject = JSON.parseObject(authResult);

		boolean content = jsonObject.getBooleanValue("content");

		//content验证通过
		if(content){
			//1.生成token
			String accessToken = TokenProcessor.generateTokeCode(userId);
			System.out.println("accessToken:"+accessToken);
			//2.存入session中,进行下次服务端的匹配
			//request.getSession().setAttribute("access_token",accessToken);
			//转发需求存入本地的session的缓存中
			TokenCache.setToken(userId, accessToken);

			//3.向客户端返回access_token
			Map<String,Object> result = new HashMap<String, Object>();
			result.put("code","200");
			result.put("msg","success");

			Map<String,String> tokenMap = new HashMap<String,String>();
			tokenMap.put("access_token",accessToken);

			result.put("data",tokenMap);
			String jsonResult = JSON.toJSONString(result);
			response.getWriter().println(jsonResult);
			return;
		}else{
			//直接返回失败
			Map<String,String> result = new HashMap<String, String>();
			result.put("code","500");
			result.put("msg","登录验证失败");
			String resultStr = JSON.toJSONString(result);
			response.getWriter().println(resultStr);
			return;
		}

	}
}
