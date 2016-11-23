package com.taobao.lottery.dal.dao;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.taobao.lottery.dal.dataObject.ActivityInfo;
import com.taobao.lottery.dal.dataObject.KeyValue;
import com.taobao.lottery.dal.dataObject.Status;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by jianghan.jh on 2016/8/9.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:lottery/dal/data-source.xml",
        "classpath:lottery/dal/dao.xml"})
//@Transactional
//@TransactionConfiguration(transactionManager = "txManager")
public class KeyValueDaoTest {

    @Autowired
    private KeyValueDao keyValueDao;

    @Autowired
    private ActivityInfoDao activityInfoDao;


    @Test
    public void testUserActvity() {

        KeyValue re = keyValueDao.findByKey("list_jianghan.jh");
        System.out.print(JSON.toJSONString(re));


//        KeyValue re = keyValueDao.findById(6);
//        System.out.print(JSON.toJSONString(re));
//


//        List<KeyValue> result = keyValueDao.getAllUserActivity();
//        System.out.print(JSON.toJSONString(result));
//        keyValueDao.insertItem(userActivity);

//        assertTrue(keyValueDao.insertItem(userActivity));

    }


    @Test
    public void testUpdate() {
        KeyValue keyValue = new KeyValue();
//        keyValue.setValue("this is a test");
        keyValue.setKey("list_xichi.zl");


        Date now = new Date();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = formatter.format(now);
        keyValue.setModifyTime(formattedDateTime);
        keyValue.setCreateTime(formattedDateTime);
        keyValue.setValue("[]");
//        keyValue.setId(2);


        keyValueDao.insertItem(keyValue);
    }


    public void testDelete() {
        keyValueDao.deleteById(6);
        keyValueDao.deleteById(7);
        keyValueDao.deleteById(8);
        keyValueDao.deleteById(12);

    }



    @Test
    public void test1(){
        String peopleMerge = "['11333','33333','333']";
        List<String> allParticipants = JSON.parseObject(peopleMerge, new TypeReference<ArrayList<String>>() {
        });

        for(int i = 0; i < allParticipants.size(); i++)
            System.out.println(allParticipants.get(i));
    }


    @Test
    public void updateTest(){




//
//        KeyValue ua = keyValueDao.findByKey("list_xichi.zl");

//
//        List<ActivityInfo> ais = activityInfoDao.getActivitiesByCreatorId("jianghan.jh");
//
//        SimplePropertyPreFilter filter = new SimplePropertyPreFilter(ActivityInfo.class, "creator_id","creator_name","activity_id",
//                "title","start_time","end_time","location");
//        ua.setValue(JSON.toJSONString(ais,filter));
//
//        System.out.print(JSON.toJSONString(ais,filter));
//        keyValueDao.updateById(ua);



//
//        for(int i = 7; i < 10; i++) {
//            KeyValue ua = keyValueDao.findById(i);
//
////            List<ActivityInfo> ais = new ArrayList<ActivityInfo>();
////            ua.setValue(JSON.toJSONString(ais));
//
//            ua.setValue("");
//
//            keyValueDao.updateById(ua);
//        }







    }

    @Test
    public void setActivityListToEmpty(){


//        KeyValue ua = keyValueDao.findById(2);
//        ua.setKey("list_jianghan.jh");
//        keyValueDao.updateById(ua);
//
        for(int i =50;i<52;i++){

            System.out.print(i);
            KeyValue ua = keyValueDao.findById(i);
//            ua.setKey(ua.getKey()+ua.getId());
            ua.setStatus(0);
            ua.setKey("no_"+i);
            keyValueDao.updateById(ua);
        }




//        ua = keyValueDao.findById(27);
//        ua.setKey(ua.getKey()+ua.getId());
//        keyValueDao.updateById(ua);
//
//        ua = keyValueDao.findById(26);
//        ua.setKey(ua.getKey()+ua.getId());
//        keyValueDao.updateById(ua);
//
//        ua = keyValueDao.findById(28);
//        ua.setKey(ua.getKey()+ua.getId());
//        keyValueDao.updateById(ua);
//
//        ua = keyValueDao.findById(29);
//        ua.setKey(ua.getKey()+ua.getId());
//        keyValueDao.updateById(ua);
//
//
//        ua = keyValueDao.findById(24);
//        ua.setKey(ua.getKey()+ua.getId());
//        keyValueDao.updateById(ua);




//        ua.setValue("[]");
//        ua.setKey(ua.getKey().substring(5));

    }


    @Test
    public void updateUaByAcI(){

//
//        //update user_activity
//        KeyValue ua = keyValueDao.findById(1);
//
////        List<ActivityInfo> ais = new ArrayList<ActivityInfo>();
////        ActivityInfo ai = activityInfoDao.getActivityById(25);
////        ais.add(ai);
//
//        List<ActivityInfo> ais  = activityInfoDao.getActivitiesByCreatorId("xichi.zl");
//
//        SimplePropertyPreFilter filter = new SimplePropertyPreFilter(ActivityInfo.class, "creator_id","creator_name","activity_id",
//                "title","start_time","end_time","location","create_time");
//        ua.setValue(JSON.toJSONString(ais,filter));
//
//        keyValueDao.updateByKey(ua);


    }


    @Test
    public void updateActivity(){

        ActivityInfo ai = activityInfoDao.getActivityById(1);
        ai.setLogicStatus(Status.DELETED.getIndex());
        activityInfoDao.updateActivityInfo(ai);

    }


}
