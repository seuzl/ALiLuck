package com.taobao.lottery.dal.dao;

import com.alibaba.fastjson.JSON;
import com.taobao.lottery.dal.dataObject.ActivityInfo;
import com.taobao.lottery.dal.dataObject.Status;
import com.taobao.lottery.dal.jsonModel.PriceCategory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.taobao.tddl.common.utils.Assert.assertTrue;

/**
 * Created by LTX on 2016/8/9.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:lottery/dal/data-source.xml",
        "classpath:lottery/dal/dao.xml"})
//@Transactional
//@TransactionConfiguration(transactionManager = "txManager")
public class ActivityInfoDaoTest {

    @Autowired
    private ActivityInfoDao activityInfoDao;

    @Test
   // @Rollback(true)
    public void addActivity(){
        ActivityInfo activityInfo = new ActivityInfo();
        
        Date now = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = formatter.format(now);

        activityInfo.setActivityType(1);
        activityInfo.setTitle("么么哒么么哒");
        activityInfo.setStartTime(formattedDateTime);
        activityInfo.setEndTime(formattedDateTime);

//        ArrayList<PriceCategory> priceCategories = new ArrayList<PriceCategory>();
//        PriceCategory priceCategory = new PriceCategory();
//        priceCategory.setName("江瀚");
//        priceCategory.setNumber(20);
//        priceCategories.add(priceCategory);
//        activityInfo.setPrize(JSON.toJSONString(priceCategories));
//        activityInfo.setMyPriceCategory(priceCategories);

        activityInfo.setCreateTime(formattedDateTime);
        activityInfo.setModifyTime(formattedDateTime);

        activityInfo.setLogicStatus(Status.SUCCESS.getIndex());
        activityInfo.setLongitude(1.3);
        activityInfo.setLatitude(0.3);
        activityInfo.setLimit(0.4);

        activityInfo.setCreatorId("tingxun.ltx");

        activityInfo.setCreatorName("玄麟");
        activityInfo.setLocation("西溪园区");
//        activityInfo.setPrize("ee");




        System.out.print(JSON.toJSONString(activityInfo));

        assertTrue(activityInfoDao.addActivityInfo(activityInfo));


        System.out.println("$$$$  activityInfoId " + activityInfo.getActivityId());
//
//        List<ActivityInfo> activityInfos = activityInfoDao.getAllActivities();
//        for (ActivityInfo activityInfo1 : activityInfos){
//            System.out.println("activityInfo1 = " + activityInfo1);
//        }

        ActivityInfo my = activityInfoDao.getActivityById(activityInfo.getActivityId());
        System.out.println("activityInfo = " + JSON.toJSONString(my));


    }

    /*@Test
    public void getAllActivities(){
        List<ActivityInfo> activityInfos = activityInfoDao.getAllActivities();
        for (ActivityInfo activityInfo1 : activityInfos){
            System.out.println("activityInfo1 = " + activityInfo1);
        }
    }

    @Test
    public void getGroupById(){
        int id = 5;
        String group = activityInfoDao.getParticipantsByActivityId(id);
        System.out.println("group = " + group);
    }

    @Test
    public void getActivitiesByCreatorId(){
        String creatorId = "xichi.zl";
        List<ActivityInfo> activityInfos = activityInfoDao.getActivitiesByCreatorId(creatorId);
        for (ActivityInfo activityInfo : activityInfos){
            System.out.println(activityInfo);
        }
    }

    @Test
    public void getActivityById(){
        int activityId = 1;
        ActivityInfo activityInfo = activityInfoDao.getActivityById(activityId);
        assertNotNull(activityInfo);


        for(int i = 1;i<10;i++) {
            activityInfo.setActivityId(i);
            activityInfo.setTitle("阿里幸运"+i);
            activityInfoDao.updateActivityInfo(activityInfo);
        }
        for(int i = 20;i<24;i++) {
            activityInfo.setActivityId(i);
            activityInfo.setTitle("阿里幸运"+i);
            activityInfoDao.updateActivityInfo(activityInfo);
        }
        System.out.println("activityInfo = " + activityInfo);
    }

    @Test
    public void getPriceCategoryById(){
        int activityId = 25;
        ActivityInfo activityInfo = activityInfoDao.getActivityById(activityId);
        String priceCategory = activityInfo.getPrize();
        System.out.println("priceCategory = " + priceCategory);
    }





    @Test
    public void upate(){

        for(int i = 1;i<10;i++) {
            ActivityInfo activityInfo = new ActivityInfo();
            activityInfo.setActivityId(i);

            ArrayList<Participant> participants = new ArrayList<Participant>();
            Participant participant = new Participant();
            participant.setKey("jianghan.jh");
            participant.setUserName("江焰");
            participants.add(participant);

            activityInfo.setParticipants(participants.toString());

            Date now = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String formattedDateTime = formatter.format(now);


            activityInfo.setCreateTime(formattedDateTime);
            activityInfo.setModifyTime(formattedDateTime);

            System.out.print(participants.toString());
            activityInfoDao.updateActivityInfo(activityInfo);
        }

    }*/






}
