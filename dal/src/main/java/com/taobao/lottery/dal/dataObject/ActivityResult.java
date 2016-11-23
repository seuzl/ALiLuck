package com.taobao.lottery.dal.dataObject;


/**
 * Created by LTX on 2016/8/9.
 */
public class ActivityResult {
    private int resultId;
    private int activityId;
    private String result;

    private String createTime;
    private String modifyTime;
    private int status;

    public int getResultId() {
        return resultId;
    }

    public void setResultId(int resultId) {
        this.resultId = resultId;
    }

    public int getActivityId() {
        return activityId;
    }

    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(String modifyTime) {
        this.modifyTime = modifyTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "ActivityResult{" +
                "resultId=" + resultId +
                ", activityId=" + activityId +
                ", result='" + result + '\'' +
                ", createTime='" + createTime + '\'' +
                ", modifyTime='" + modifyTime + '\'' +
                ", status=" + status +
                '}';
    }
}
