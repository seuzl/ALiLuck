package com.taobao.lottery.dal.dataObject;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.taobao.lottery.dal.jsonModel.Participant;
import com.taobao.lottery.dal.jsonModel.PriceCategory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jianghan.jh on 2016/8/13.
 */

public class ActivityInfo implements Comparable{

    @JSONField(name="activity_id")
    private Integer activityId;

    @JSONField(name="creator_id")
    private String creatorId;

    @JSONField(name="creator_name")
    private String creatorName;

    private String title;

    @JSONField(name="start_time")
    private String startTime;

    @JSONField(name="end_time")
    private String endTime;

    //存储的时候使用
    @JSONField(serialize = false)
    private String participants;

    //序列化的时候使用
    @JSONField(name="participants")
    private List<Participant> myParticipants;

    //存储的时候使用
    @JSONField(serialize = false)
    private String prize;

    //序列化的时候使用
    @JSONField(name="prize")
    private List<PriceCategory> myPriceCategory;

    private String location;
    private Double longitude;
    private Double latitude;
    private Double limit;

    @JSONField(serialize = false)
    private Integer logicStatus;

    @JSONField(serialize = false)
    private Integer activityType;

    @JSONField(name="create_time")
    private String createTime;

    @JSONField(serialize = false)
    private String modifyTime;




    public Integer getActivityId() {
        return activityId;
    }

    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getParticipants() {
        return participants;
    }

    public void setParticipants(String participants) {
        
        this.participants = participants;
        this.myParticipants = JSON.parseArray(participants, Participant.class );
    }

    public List<Participant> getMyParticipants() {
        return myParticipants;
    }

    public void setMyParticipants(ArrayList<Participant> myParticipants) {
        this.myParticipants = myParticipants;
    }

    public String getPrize() {
        return prize;
    }

    public void setPrize(String prize) {

        this.prize = prize;
        this.myPriceCategory = JSON.parseArray(prize, PriceCategory.class );
    }

    public List<PriceCategory> getMyPriceCategory() {
        return myPriceCategory;
    }

    public void setMyPriceCategory(ArrayList<PriceCategory> myPriceCategory) {
        this.myPriceCategory = myPriceCategory;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLimit() {
        return limit;
    }

    public void setLimit(Double limit) {
        this.limit = limit;
    }

    public Integer getLogicStatus() {
        return logicStatus;
    }

    public void setLogicStatus(Integer logicStatus) {
        this.logicStatus = logicStatus;
    }

    public Integer getActivityType() {
        return activityType;
    }

    public void setActivityType(Integer activityType) {
        this.activityType = activityType;
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


    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

    @Override
    public int compareTo(Object o) {

        ActivityInfo second = (ActivityInfo)o;

        int result = second.getCreateTime().compareTo(this.getCreateTime());

        return result;
    }



    /*public static void main(String[] args){
//        ActivityInfo2 ai2 = new ActivityInfo2();
//        ai2.setActivityId(112);
//        ai2.setCreatorId("jianghan.jh");
//        ai2.setCreatorName("江焰");
//        ai2.setTitle("江焰活动");
//
////        String[] str = new String[4];
////        str[1] = "happy";
//
//        ArrayList<PriceCategory> priceCategories = new ArrayList<PriceCategory>();
//        PriceCategory prize = new PriceCategory();
//        prize.setName("江瀚");
//        prize.setNumber(20);
//        priceCategories.add(prize);
//
////        System.out.print(priceCategories.toString());
//
//
//        ai2.setPrize(priceCategories);
//
////        HashMap<String,String> map = new HashMap<String, String>();
////        map.put("hello","嗨");
////        map.put("wish","嗨2");
////
////        ai2.setParticipants(map);
//        System.out.print(JSON.toJSON(ai2));


        ActivityInfo activityInfo = new ActivityInfo();

        Date now = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String formattedDateTime = formatter.format(now);

        activityInfo.setActivityType(1);
        activityInfo.setTitle("aa");
        activityInfo.setStartTime(formattedDateTime);
        activityInfo.setEndTime(formattedDateTime);

        ArrayList<PriceCategory> priceCategories = new ArrayList<PriceCategory>();
        PriceCategory priceCategory = new PriceCategory();
        priceCategory.setName("江瀚");
        priceCategory.setNumber(20);
        priceCategories.add(priceCategory);

        activityInfo.setPrize(JSON.toJSONString(priceCategories));


        //activityInfo.setCreateTime(formattedDateTime);
        //activityInfo.setModifyTime(formattedDateTime);

        activityInfo.setLogicStatus(Status.SUCCESS.getIndex());
        activityInfo.setLongitude(1.3);
        activityInfo.setLatitude(0.3);
        activityInfo.setLimit(0.4);

        activityInfo.setCreatorId("tingxun.ltx");
//        activityInfo.setPrize("ee");



        System.out.print(JSON.toJSONString(activityInfo));

    }*/
}



