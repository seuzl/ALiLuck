package com.taobao.lottery.dal.jsonModel;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by jianghan.jh on 8/13/16.
 */
public class Participant {

    @JSONField(name="user_id")
    String userId;

    @JSONField(name="user_name")
    String userName;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
