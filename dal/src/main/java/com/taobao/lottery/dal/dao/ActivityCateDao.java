package com.taobao.lottery.dal.dao;

import com.taobao.lottery.dal.dataObject.ActivityCate;

import java.util.List;

/**
 * Created by LTX on 2016/8/9.
 */
public interface ActivityCateDao {
    boolean addActivityCate(ActivityCate activityCate);

    List<ActivityCate> getAllActivityCates();
}
