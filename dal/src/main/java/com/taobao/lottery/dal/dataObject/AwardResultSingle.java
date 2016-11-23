package com.taobao.lottery.dal.dataObject;

import java.util.List;

/**
 * Created by hanqing.ghq on 2016/8/11.
 */
public class AwardResultSingle {

    private String name;

    private int number;

    private List<UserInfo> lucky;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public List<UserInfo> getLucky() {
        return lucky;
    }

    public void setLucky(List<UserInfo> lucky) {
        this.lucky = lucky;
    }
}
