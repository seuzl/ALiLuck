package com.taobao.lottery.web.home.module.screen.activity;

import com.alibaba.citrus.turbine.TurbineRunData;
import com.alibaba.citrus.util.StringUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.taobao.lottery.biz.manager.KeyValueManager;
import com.taobao.lottery.dal.dataObject.ActivityInfo;
import com.taobao.lottery.dal.jsonModel.APIResult;
import org.omg.PortableInterceptor.USER_EXCEPTION;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Created by jianghan.jh on 2016/8/11.
 */
public class Myjoin {

    @Autowired
    private KeyValueManager keyValueManager;

    APIResult apiResult;

    String userId;

    public void execute(TurbineRunData runData){

        runData.getResponse().setContentType("application/json");

        apiResult = new APIResult();

        String test = runData.getParameters().getString("test");

        if(!StringUtil.isBlank(test)){

            HashMap<String,String> data  = JSONObject.parseObject(test,new TypeReference<HashMap<String,String> >() {
            });

            userId = data.get("user_id");

        }else{

            try {

                StringBuffer dataStr = new StringBuffer();
                String line = null;
                BufferedReader reader = runData.getRequest().getReader();
                while ((line = reader.readLine()) != null)
                    dataStr.append(line);

                HashMap<String,String> data  = JSONObject.parseObject(dataStr.toString(),new TypeReference<HashMap<String,String> >() {
                });

                userId = data.get("user_id");

            } catch (IOException e) {

                apiResult.setCode(500);
                apiResult.setMsg("出错啦");
                e.printStackTrace();
            }
        }

//        try {

            if(StringUtil.isBlank(userId)){

                apiResult.setCode(400);
                apiResult.setMsg("必须有 用户ID");

            }else {
                String re =  keyValueManager.getActivitiesByUserId(userId);

                List<ActivityInfo> activityInfos = JSON.parseArray(re, ActivityInfo.class );

                for(ActivityInfo ac: activityInfos){
                    ac.setMyParticipants(null);
                    ac.setMyPriceCategory(null);
                }

                Collections.sort(activityInfos);

                apiResult.setCode(200);
                apiResult.setMsg("success");
                apiResult.setData(activityInfos);
            }

//        } catch (IOException e) {
//            apiResult.setCode(500);
//            apiResult.setMsg("出错啦");
//            e.printStackTrace();
//        }finally {
            try {
                runData.getResponse().getWriter().write(apiResult.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
//        }
    }
}
