package com.taobao.lottery.dal.dao;

import com.taobao.lottery.dal.dataObject.ActivityResult;

import java.util.List;

/**
 * Created by LTX on 2016/8/9.
 */
public interface ActivityResultDao {
    boolean addActivityResult(ActivityResult activityResult);

    List<ActivityResult> getAllActivityResults();

    String getResultByActivityId(int activityId);
}
