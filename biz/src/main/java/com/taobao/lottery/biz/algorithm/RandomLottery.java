package com.taobao.lottery.biz.algorithm;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.taobao.lottery.biz.manager.ActivityInfoManager;
import com.taobao.lottery.biz.manager.ActivityResultManager;
import com.taobao.lottery.biz.manager.KeyValueManager;
import com.taobao.lottery.dal.dataObject.*;
import com.taobao.lottery.dal.jsonModel.Participant;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by hanqing.ghq on 2016/8/11.
 */
public class RandomLottery {

    @Autowired
    private ActivityResultManager activityResultManager;

    @Autowired
    private ActivityInfoManager activityInfoManager;

    @Autowired
    private KeyValueManager keyValueManager;



    public void lottery(int activity_id)
    {
        ActivityInfo activityInfo = activityInfoManager.getActivityById(activity_id);
        /*if(activityInfo == null)
        {

        }*/

        String peopleMerge = activityInfo.getParticipants();
        String prizeMerge = activityInfo.getPrize();


        List<Participant> allParticipants = JSON.parseObject(peopleMerge, new TypeReference<ArrayList<Participant>>() {
        });
        List<AwardInfo> allPrize = JSON.parseObject(prizeMerge, new TypeReference<ArrayList<AwardInfo>>() {});

        //计算奖品总数
        int num = 0;
        for(int i = 0; i < allPrize.size(); i++)
            num += allPrize.get(i).number;

        List<AwardResultSingle> result = new ArrayList<AwardResultSingle>();

        List<Integer> temp_result = new ArrayList<Integer>();

        Date present = new Date();
        int seed = present.getSeconds();
        Random r = new Random(seed);

        for(int i = 0; i < num; i++)
        {
            int win_site = r.nextInt(allParticipants.size());
            if(temp_result.indexOf(win_site) == -1)
            {
                //temp.setName(String.valueOf(num));
                Participant participant = allParticipants.get(win_site);
                String win_id = participant.getUserId();
                String userName = participant.getUserName();
                temp_result.add(win_site);
                if(allPrize.get(0).number != 0)
                {
                    if(result.size() == 0)
                    {
                        AwardResultSingle temp = new AwardResultSingle();
                        temp.setName(allPrize.get(0).getName());
                        temp.setNumber(allPrize.get(0).getNumber());
                        UserInfo user = new UserInfo();
                        user.setUser_name(userName);
                        user.setUser_id(win_id);

                        List<UserInfo> list = new ArrayList<UserInfo>();
                        list.add(user);

                        temp.setLucky(list);

                        result.add(temp);
                        allPrize.get(0).number--;
                    }
                    else
                    {
                        AwardResultSingle temp = result.get(result.size()-1);
                        List<UserInfo> list = temp.getLucky();

                        UserInfo user = new UserInfo();
                        user.setUser_name(userName);
                        user.setUser_id(win_id);

                        list.add(user);
                        allPrize.get(0).number--;
                    }
                }
                else
                {
                    allPrize.remove(0);
                    AwardResultSingle temp = new AwardResultSingle();
                    temp.setName(allPrize.get(0).getName());
                    temp.setNumber(allPrize.get(0).getNumber());
                    UserInfo user = new UserInfo();
                    user.setUser_name(userName);
                    user.setUser_id(win_id);

                    List<UserInfo> list = new ArrayList<UserInfo>();
                    list.add(user);

                    temp.setLucky(list);

                    result.add(temp);
                    allPrize.get(0).number--;
                }

            }
            else i--;
        }

        Date now = new Date();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = formatter.format(now);

        ActivityResult activityResult = new ActivityResult();
        activityResult.setActivityId(activity_id);
        activityResult.setResult(JSON.toJSONString(result));
        activityResult.setCreateTime(formattedDateTime);
        activityResult.setModifyTime(formattedDateTime);
        activityResult.setStatus(1);

        activityResultManager.addActivityResult(activityResult);

        //return result;
    }

    /*
     * @return : 如果获奖则返回奖项名称,否则返回null
     */
    public String getSingleWinResult(int activity_id, String user_id)
    {
        String totleResult = activityResultManager.getResultByActivityId(activity_id);
        List<AwardResultSingle> allPrize = JSON.parseObject(totleResult, new TypeReference<ArrayList<AwardResultSingle>>() {});
        if(allPrize == null){
            return null;
        }
        for(int i = 0; i < allPrize.size(); i++)
        {
            List<UserInfo> list = allPrize.get(i).getLucky();
            for(int j = 0; j < list.size(); j++) {
                if (user_id.equals(list.get(j).getUser_id())) {
                    keyValueManager.userJoinActivity(user_id, activity_id);
                    return allPrize.get(i).getName();   //返回所中奖项名称
                }
            }

        }
        keyValueManager.userJoinActivity(user_id,activity_id);
        return null;
    }

}