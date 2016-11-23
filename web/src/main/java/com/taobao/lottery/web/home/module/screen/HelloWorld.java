package com.taobao.lottery.web.home.module.screen;

import com.alibaba.citrus.turbine.Context;
import com.alibaba.citrus.turbine.Navigator;
import com.alibaba.citrus.turbine.TurbineRunData;
import com.alibaba.fastjson.JSON;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by LTX on 2016/8/10.
 */
public class HelloWorld {

    @Resource
    HttpServletRequest request;
    @Resource
    HttpServletResponse response;

    public void execute(TurbineRunData rundata, final Context context) throws Exception{
        Map<String,String> map = new HashMap<String,String>();
        map.put("code","200");
        map.put("msg","测试连通成功");
        String jsonResult = JSON.toJSONString(map);
        response.getWriter().println(jsonResult);
    }
}
