package com.taobao.lottery.web.home.module.screen.activity;

import com.alibaba.citrus.turbine.TurbineRunData;
import com.alibaba.citrus.util.StringUtil;
import com.alibaba.fastjson.JSONObject;
import com.taobao.lottery.biz.manager.ActivityInfoManager;
import com.taobao.lottery.dal.dataObject.ActivityInfo;
import com.taobao.lottery.dal.jsonModel.APIResult;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * Created by LTX on 2016/8/11.
 */
public class Get {

    @Autowired
    private ActivityInfoManager activityInfoManager;

    APIResult apiResult;

    public void execute(TurbineRunData runData){

        runData.getResponse().setContentType("application/json");

        apiResult = new APIResult();

        StringBuffer data = new StringBuffer();
        String line = null;
        try {
            BufferedReader reader = runData.getRequest().getReader();
            while ((line = reader.readLine()) != null)
                data.append(line);

        } catch (Exception e) {
            apiResult.setCode(400);
            apiResult.setMsg("参数有误");
        }

        System.out.println("jb = " + data);

        JSONObject obj = JSONObject.parseObject(data.toString());
        String id = obj.getString("activity_id");


        if(StringUtil.isBlank(id)){

            apiResult.setCode(400);
            apiResult.setMsg("activityID 必须提供");

        }else {
            int activityId = Integer.parseInt(id);
            ActivityInfo activityInfo = activityInfoManager.getActivityById(activityId);


            apiResult.setCode(200);
            apiResult.setMsg("success");
            apiResult.setData(activityInfo);

//            String activity = JSON.toJSONString(activityInfo);

        }




        try {
            runData.getResponse().getWriter().write(apiResult.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
