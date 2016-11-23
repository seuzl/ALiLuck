package com.taobao.lottery.web.home.module.screen.activity;

import com.alibaba.citrus.turbine.TurbineRunData;
import com.alibaba.citrus.util.StringUtil;
import com.alibaba.fastjson.JSON;
import com.taobao.lottery.biz.algorithm.RandomLottery;
import com.taobao.lottery.biz.manager.ActivityInfoManager;
import com.taobao.lottery.biz.manager.KeyValueManager;
import com.taobao.lottery.dal.dataObject.ActivityInfo;
import com.taobao.lottery.dal.dataObject.Status;
import com.taobao.lottery.dal.jsonModel.APIResult;
import com.taobao.lottery.dal.jsonModel.Participant;
import com.taobao.lottery.dal.jsonModel.PriceCategory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.BufferedReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by LTX on 2016/8/10.
 * Modified by jianghan.jh on 2016/8/13.
 */
public class Create {

    @Autowired
    private ActivityInfoManager activityInfoManager;

    @Autowired
    private KeyValueManager keyValueManager;

    @Autowired
    private RandomLottery randomLottery;

    APIResult apiResult;

    TurbineRunData runData;

    public void execute(TurbineRunData runData) {

        this.runData = runData;
        apiResult = new APIResult();

        runData.getResponse().setContentType("application/json");

        System.out.println("call create activity");
        //ActivityInfo activityInfo = new ActivityInfo();
        ActivityInfo activityInfo = null;

        String test = runData.getParameters().getString("test");

        if(!StringUtil.isBlank(test)){

            activityInfo = JSON.parseObject(test.toString(), ActivityInfo.class);

        }else{

            StringBuffer activityStr = new StringBuffer();
            String line = null;
            try {
                BufferedReader reader = runData.getRequest().getReader();
                while ((line = reader.readLine()) != null)
                    activityStr.append(line);
            } catch (Exception e) {
            /*report an error*/
                e.printStackTrace();
            }

            System.out.println("jb = " + activityStr);
            activityInfo = JSON.parseObject(activityStr.toString(), ActivityInfo.class);

        }

        if(activityInfo == null){
            write(500,"Json 解析失败");
            return ;
        }
        if(StringUtil.isBlank(activityInfo.getCreatorId())){

            write(400,"必须有创建者 ID");
            return;

        }else{


            List<Participant> participants = activityInfo.getMyParticipants();

            List<PriceCategory> priceCategories = activityInfo.getMyPriceCategory();

            if(participants==null||participants.size()==0){
                write(400,"参与者不可为空");
                return;
            }

            if(priceCategories==null||priceCategories.size()==0){
                write(400,"奖品不允许为空");
                return;
            }

            int totalPrizeNumber = 0;

            for(PriceCategory priceCategory: priceCategories){
                totalPrizeNumber = totalPrizeNumber + priceCategory.getNumber();
            }

            if(participants.size() < totalPrizeNumber){
                write(400,"参与人员数必须大于奖品数");
                return;
            }


            Date now = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String formattedDateTime = formatter.format(now);

            System.out.println("Time is " + formattedDateTime);

            activityInfo.setCreateTime(formattedDateTime);
            activityInfo.setModifyTime(formattedDateTime);
            activityInfo.setLogicStatus(Status.SUCCESS.getIndex());

            System.out.println("activityInfo is"+JSON.toJSONString(activityInfo));

            activityInfoManager.addActivityInfo(activityInfo);
            keyValueManager.newActivityUpdate(activityInfo);

            randomLottery.lottery(activityInfo.getActivityId());

            Map<String, Integer> result = new HashMap<String, Integer>();
            result.put("activity_id",activityInfo.getActivityId());


            apiResult.setData(result);
            write(200,"success");
            return;
        }


    }


    public void write(int code, String msg){
        try {
            apiResult.setCode(code);
            apiResult.setMsg(msg);
            runData.getResponse().getWriter().write(apiResult.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
