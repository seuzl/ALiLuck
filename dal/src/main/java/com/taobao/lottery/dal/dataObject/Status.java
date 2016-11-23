package com.taobao.lottery.dal.dataObject;

/**
 * Created by LTX on 2016/8/10.
 */
public enum Status {
    DELETED("被删除",0),SUCCESS("可用",1);

    private final String message;
    private final int index;
    Status(String message, int index) {
        this.message = message;
        this.index = index;
    }

    // 普通方法
    public static String getMessage(int index) {
        for (Status c : Status.values()) {
            if (c.getIndex() == index) {
                return c.message;
            }
        }
        return null;
    }

    public String getMessage() {
        return message;
    }

    public int getIndex() {
        return index;
    }
}
