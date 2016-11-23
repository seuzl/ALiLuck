package com.taobao.lottery.dal.dao.ibatis;

import com.taobao.lottery.dal.dao.ActivityCateDao;
import com.taobao.lottery.dal.dataObject.ActivityCate;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import java.util.List;

/**
 * Created by LTX on 2016/8/9.
 */

public class IbatisActivityCateDao extends SqlMapClientDaoSupport implements ActivityCateDao {
    public boolean addActivityCate(ActivityCate activityCate) {
        if(activityCate != null){
            int result = getSqlMapClientTemplate().update("insertActivityCate",activityCate);
            return (result==1);
        }
        return false;
    }

    public List<ActivityCate> getAllActivityCates() {
        List<ActivityCate> result = getSqlMapClientTemplate().queryForList("selectAllActivityCates");
        return result;

    }
}
