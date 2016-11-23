package com.taobao.lottery.dal.jsonModel;

import com.alibaba.fastjson.JSON;

import java.util.HashMap;

/**
 * Created by jianghan.jh on 8/13/16.
 */
public class PriceCategory {

    String name;
    Integer number;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
