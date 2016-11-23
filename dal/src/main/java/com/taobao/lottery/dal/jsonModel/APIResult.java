package com.taobao.lottery.dal.jsonModel;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by jianghan.jh on 8/13/16.
 */
public class APIResult {

    Integer code;
    String msg;
    Object data;


    public APIResult(){
        this.code = 500;
        this.msg="请求失败";
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }


}
