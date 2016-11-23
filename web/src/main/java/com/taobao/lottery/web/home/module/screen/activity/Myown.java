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
import java.util.Collections;
import java.util.List;

/**
 * Created by LTX on 2016/8/11.
 */
public class Myown {

    @Autowired
    private ActivityInfoManager activityInfoManager;

    APIResult apiResult;

    public void execute(TurbineRunData runData){

        runData.getResponse().setContentType("application/json");

        apiResult = new APIResult();

        StringBuffer dataStr = new StringBuffer();
        String line = null;
        try {
            BufferedReader reader = runData.getRequest().getReader();
            while ((line = reader.readLine()) != null)
                dataStr.append(line);
        } catch (Exception e) {
            /*report an error*/
            e.printStackTrace();
        }

        System.out.println("jb = " + dataStr);

        JSONObject obj = JSONObject.parseObject(dataStr.toString());
        String userId = obj.getString("user_id");

        if(StringUtil.isBlank(userId)){

            apiResult.setCode(400);
            apiResult.setMsg("必须有 userId");

        }else{

            List<ActivityInfo> activityInfos = activityInfoManager.getActivitiesByCreatorId(userId);

            for(ActivityInfo ac: activityInfos){
                ac.setMyParticipants(null);
                ac.setMyPriceCategory(null);
                ac.setLongitude(null);
                ac.setLatitude(null);
                ac.setLimit(null);
            }
            Collections.sort(activityInfos);

            apiResult.setCode(200);
            apiResult.setMsg("success");
            apiResult.setData(activityInfos);
        }


        try {
            runData.getResponse().getWriter().write(apiResult.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
