package com.taobao.lottery.biz.manager.impl;

import com.taobao.lottery.biz.manager.ActivityResultManager;
import com.taobao.lottery.dal.dao.ActivityResultDao;
import com.taobao.lottery.dal.dataObject.ActivityResult;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by LTX on 2016/8/10.
 */
public class ActivityResultManagerImpl implements ActivityResultManager {

    @Autowired
    private ActivityResultDao activityResultDao;

    public boolean addActivityResult(ActivityResult activityResult) {
        if(activityResult != null){
            return activityResultDao.addActivityResult(activityResult);
        }
        return false;
    }

    public String getResultByActivityId(int activityId) {
        return activityResultDao.getResultByActivityId(activityId);
    }
}
