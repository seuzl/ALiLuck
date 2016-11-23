package com.taobao.lottery.dal.dao.ibatis;

import com.taobao.lottery.dal.dao.ActivityResultDao;
import com.taobao.lottery.dal.dataObject.ActivityResult;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import java.util.List;

/**
 * Created by LTX on 2016/8/9.
 */
public class IbatisActivityResultDao extends SqlMapClientDaoSupport implements ActivityResultDao {
    public boolean addActivityResult(ActivityResult activityResult) {
        if(activityResult != null){
            int result = getSqlMapClientTemplate().update("insertActivityResult",activityResult);
            return (result == 1);
        }
        return false;
    }

    public List<ActivityResult> getAllActivityResults() {
        return getSqlMapClientTemplate().queryForList("selectAllActivityResult");
    }

    public String getResultByActivityId(int activityId) {
        return (String)getSqlMapClientTemplate().queryForObject("selectResultByActivityId",activityId);
    }
}
