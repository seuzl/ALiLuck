package com.taobao.lottery.web.home.module.screen.buc;

import com.alibaba.citrus.turbine.Context;
import com.alibaba.citrus.turbine.TurbineRunData;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.taobao.lottery.biz.buc.BucService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.util.HashMap;

/**
 * Created by qingmian.mw on 2016/8/11.
 */
public class GetNickNameCn {

	public void execute(TurbineRunData runData, Context context) throws Exception{
		HttpServletResponse response = runData.getResponse();
		response.setContentType("application/json");

		HttpServletRequest request = runData.getRequest();

		StringBuffer applicationJson = new StringBuffer();
		String line = null;
		try {
			BufferedReader reader = request.getReader();
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

		/*String fuzzyStr = runData.getRequest().getParameter("fuzzyStr");*/
		String loginName = data.get("loginName");
		String result = BucService.getNickNameCn(loginName);
		response.getWriter().println(result);

	}
}
