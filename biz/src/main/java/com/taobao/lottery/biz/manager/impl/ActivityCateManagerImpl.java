package com.taobao.lottery.biz.manager.impl;

import com.taobao.lottery.biz.manager.ActivityCateManager;
import com.taobao.lottery.dal.dao.ActivityCateDao;
import com.taobao.lottery.dal.dataObject.ActivityCate;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by LTX on 2016/8/10.
 */
public class ActivityCateManagerImpl implements ActivityCateManager {

    @Autowired
    private ActivityCateDao activityCateDao;

    public boolean addActivityCate(ActivityCate activityCate) {
        if(activityCate != null){
            return activityCateDao.addActivityCate(activityCate);
        }
        return false;
    }

    public List<ActivityCate> getAllActivityCates() {
        return activityCateDao.getAllActivityCates();
    }
}
