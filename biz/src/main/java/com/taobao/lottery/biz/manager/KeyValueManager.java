package com.taobao.lottery.biz.manager;

import com.taobao.lottery.dal.dataObject.ActivityCate;
import com.taobao.lottery.dal.dataObject.ActivityInfo;

import java.util.List;

/**
 * Created by jianghan.jh on 2016/8/11.
 */
public interface KeyValueManager {

    public void newActivityUpdate(ActivityInfo activityInfo);

    public String getActivitiesByUserId(String userId);

    public boolean isUserAbleJoined(String userId, int activityId);

    public boolean isUserAlreadyJoined(String userId, int activityId);

    public boolean userJoinActivity(String userId, int activityId);

    public String getUsernameByDbOrBuc(String userId);

    public void addUsername(String userId,String username);

}
