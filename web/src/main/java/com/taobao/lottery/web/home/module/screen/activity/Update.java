package com.taobao.lottery.web.home.module.screen.activity;

import com.alibaba.citrus.turbine.TurbineRunData;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.taobao.lottery.biz.manager.ActivityInfoManager;
import com.taobao.lottery.dal.dataObject.ActivityInfo;
import com.taobao.lottery.dal.jsonModel.APIResult;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by LTX on 2016/8/11.
 */
public class Update {

    @Autowired
    private ActivityInfoManager activityInfoManager;


    TurbineRunData runData;
    APIResult apiResult;

    public void execute(TurbineRunData runData){
        this.runData = runData;
        apiResult = new APIResult();

        runData.getResponse().setContentType("application/json");

        System.out.println("call update");
        StringBuffer updateStr = new StringBuffer();
        String line = null;
        try {
            BufferedReader reader = runData.getRequest().getReader();
            while ((line = reader.readLine()) != null)
                updateStr.append(line);
        } catch (Exception e) {
            /*report an error*/
            e.printStackTrace();
        }

        System.out.println("jb = " + updateStr);

        try {
            ActivityInfo ai = JSON.parseObject(updateStr.toString(), ActivityInfo.class);

            if(ai.getActivityId()==null){
                apiResult.setCode(400);
                apiResult.setMsg("必须有活动ID");
            }else{

                activityInfoManager.updateActivity(ai);

                System.out.print(JSON.toJSONString(ai));

                Map<String, Integer> result = new HashMap<String, Integer>();
                result.put("activity_id",ai.getActivityId());
                apiResult.setCode(200);
                apiResult.setMsg("success");
                apiResult.setData(result);
            }

        }catch (JSONException e){

            e.printStackTrace();
            apiResult.setCode(400);
            apiResult.setMsg("JSON 解析失败");

        }catch (Exception e){
            e.printStackTrace();
            apiResult.setCode(400);
            apiResult.setMsg("其他错误");
        }


        try {
            runData.getResponse().getWriter().write(apiResult.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }


//        JSONObject updateInfoObj = JSONObject.parseObject(updateStr.toString());
//
//        ActivityInfo activityInfo = new ActivityInfo();
//
//        activityInfo.setTitle(updateInfoObj.getString("title"));
//        activityInfo.setStartTime(updateInfoObj.getString("start_time"));
//        activityInfo.setEndTime(updateInfoObj.getString("end_time"));
//
//        List<String> participants = (List<String>)updateInfoObj.get("participants");
//        String participantsStr = JSON.toJSONString(participants);
//        activityInfo.setParticipants(participantsStr);
//
//        activityInfo.setLongitude(updateInfoObj.getDouble("longitude"));
//        activityInfo.setLatitude(updateInfoObj.getDouble("latitude"));
//        activityInfo.setLimit(updateInfoObj.getDouble("limit"));
//
//        List<AwardInfo> awardInfoList = (List<AwardInfo>)updateInfoObj.get("price_category");
//        String awardInfos = JSON.toJSONString(awardInfoList);
//        activityInfo.setPrize(awardInfos);
//
//        activityInfo.setCreatorId(updateInfoObj.getString("user_id"));
//
//        Date now = new Date();
//
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        String formattedDateTime = formatter.format(now);
//        activityInfo.setModifyTime(formattedDateTime);
//        activityInfo.setLogicStatus(Status.SUCCESS.getIndex());





    }

}
