package com.taobao.lottery.dal.dao.ibatis;

import com.taobao.lottery.dal.dao.ActivityInfoDao;
import com.taobao.lottery.dal.dataObject.ActivityInfo;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import java.util.List;

/**
 * Created by LTX on 2016/8/9.
 */
public class IbatisActivityInfoDao extends SqlMapClientDaoSupport implements ActivityInfoDao{
    public boolean addActivityInfo(ActivityInfo activityInfo) {
        if(activityInfo != null){
            getSqlMapClientTemplate().insert("insertActivity",activityInfo);
            return true;
        }
        return false;
    }

    public List<ActivityInfo> getAllActivities() {
        return getSqlMapClientTemplate().queryForList("selectAllActivities");
    }



    public String getParticipantsByActivityId(int activityId) {
        return (String)getSqlMapClientTemplate().queryForObject("selectGroupById",activityId);

    }

    public List<ActivityInfo> getActivitiesByCreatorId(String creatorId) {
        if(creatorId != null){
            List<ActivityInfo> activityInfos = getSqlMapClientTemplate().queryForList("selectActivitiesByCreatorId",creatorId);
            return activityInfos;
        }
        return null;
    }

    public boolean updateActivityInfo(ActivityInfo activityInfo) {
        int re = getSqlMapClientTemplate().update("updateActivity",activityInfo);

        if(re != 0){
            return true;
        }

        return false;
    }


    public ActivityInfo getActivityById(int id) {
        return (ActivityInfo)getSqlMapClientTemplate().queryForObject("selectActivityById",id);
    }
}
