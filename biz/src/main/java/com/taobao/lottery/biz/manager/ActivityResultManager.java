package com.taobao.lottery.biz.manager;

import com.taobao.lottery.dal.dataObject.ActivityResult;

/**
 * Created by LTX on 2016/8/10.
 */
public interface ActivityResultManager {
    boolean addActivityResult(ActivityResult activityResult);

    String getResultByActivityId(int activityId);
}
