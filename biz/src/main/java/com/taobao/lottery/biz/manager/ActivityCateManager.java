package com.taobao.lottery.biz.manager;

import com.taobao.lottery.dal.dataObject.ActivityCate;

import java.util.List;

/**
 * Created by LTX on 2016/8/10.
 */
public interface ActivityCateManager {

    boolean addActivityCate(ActivityCate activityCate);

    List<ActivityCate> getAllActivityCates();
}
