package com.taobao.lottery.dal.dataObject;


/**
 * Created by LTX on 2016/8/9.
 */

public class ActivityCate {
    private int activityCateId;
    private String activityCateName;
    private String staticUrl;
    private String pcResultUrl;
    private String mobileResultUrl;

    private String createTime;
    private String modifyTime;

    private int status;

    public int getActivityCateId() {
        return activityCateId;
    }

    public void setActivityCateId(int activityCateId) {
        this.activityCateId = activityCateId;
    }

    public String getActivityCateName() {
        return activityCateName;
    }

    public void setActivityCateName(String activityCateName) {
        this.activityCateName = activityCateName;
    }

    public String getStaticUrl() {
        return staticUrl;
    }

    public void setStaticUrl(String staticUrl) {
        this.staticUrl = staticUrl;
    }

    public String getPcResultUrl() {
        return pcResultUrl;
    }

    public void setPcResultUrl(String pcResultUrl) {
        this.pcResultUrl = pcResultUrl;
    }

    public String getMobileResultUrl() {
        return mobileResultUrl;
    }

    public void setMobileResultUrl(String mobileResultUrl) {
        this.mobileResultUrl = mobileResultUrl;
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
}
