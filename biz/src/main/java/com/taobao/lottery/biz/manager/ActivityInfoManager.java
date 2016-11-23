package com.taobao.lottery.biz.manager;

import com.taobao.lottery.dal.dataObject.ActivityInfo;

import java.util.List;

/**
 * Created by LTX on 2016/8/10.
 * Modify by jianghan.jh on 2016/8/13.
 */
public interface ActivityInfoManager {

    ActivityInfo getActivityById(int activityId);

    List<ActivityInfo> getActivitiesByCreatorId(String creatorId);

    String getGroupByActivityId(int activityId);

    boolean addActivityInfo(ActivityInfo activityInfo);

    public boolean updateActivity(ActivityInfo activityInfo);

    public boolean addParticipant(String userId, int activityId);
}
