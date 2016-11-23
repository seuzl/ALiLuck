package com.taobao.lottery.dal.dao;

import com.taobao.lottery.dal.dataObject.ActivityInfo;

import java.util.List;

/**
 * Created by LTX on 2016/8/9.
 */
public interface ActivityInfoDao {
    boolean addActivityInfo(ActivityInfo activityInfo);
    List<ActivityInfo> getAllActivities();

    //String getGroupByActivityId(int activityId);

    String getParticipantsByActivityId(int activityId);

    List<ActivityInfo> getActivitiesByCreatorId(String creatorId);

    boolean updateActivityInfo(ActivityInfo activityInfo);


   ActivityInfo getActivityById(int id);

}
