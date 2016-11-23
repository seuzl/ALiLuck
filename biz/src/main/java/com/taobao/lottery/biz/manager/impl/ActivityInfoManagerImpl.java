package com.taobao.lottery.biz.manager.impl;

import com.alibaba.fastjson.JSON;
import com.taobao.lottery.biz.buc.BucService;
import com.taobao.lottery.biz.manager.ActivityInfoManager;
import com.taobao.lottery.biz.manager.KeyValueManager;
import com.taobao.lottery.dal.dao.ActivityInfoDao;
import com.taobao.lottery.dal.dataObject.ActivityInfo;
import com.taobao.lottery.dal.jsonModel.Participant;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by LTX on 2016/8/10.
 */
public class ActivityInfoManagerImpl implements ActivityInfoManager {

    @Autowired
    private ActivityInfoDao activityInfoDao;

    @Autowired
    private KeyValueManager keyValueManager;


    public boolean addActivityInfo(ActivityInfo activityInfo) {
        if(activityInfo != null){
            return activityInfoDao.addActivityInfo(activityInfo);
        }
        return false;
    }

    public String getGroupByActivityId(int activityId) {
        return activityInfoDao.getParticipantsByActivityId(activityId);
    }

    public List<ActivityInfo> getActivitiesByCreatorId(String creatorId) {
        if(creatorId != null){
            return activityInfoDao.getActivitiesByCreatorId(creatorId);
        }
        return null;
    }

    public ActivityInfo getActivityById(int activityId) {
        return activityInfoDao.getActivityById(activityId);
    }

    public boolean updateActivity(ActivityInfo activityInfo){

        return activityInfoDao.updateActivityInfo(activityInfo);
    }


    public boolean addParticipant(String userId, int activityId){
        ActivityInfo activityInfo = activityInfoDao.getActivityById(activityId);

        if(activityInfo ==null){
            return false;
        }

        List<Participant> participants = activityInfo.getMyParticipants();
        String name = BucService.getName(userId);
        if(name == null){
            name = userId;
        }
        Participant participant = new Participant();
        participant.setUserId(userId);
        participant.setUserName(name);
        participants.add(participant);

        activityInfo.setParticipants(JSON.toJSONString(activityInfo));

        boolean result = activityInfoDao.updateActivityInfo(activityInfo);

        if(!result){
            return false;
        }
        keyValueManager.newActivityUpdate(activityInfo);

        return true;
    }
}
