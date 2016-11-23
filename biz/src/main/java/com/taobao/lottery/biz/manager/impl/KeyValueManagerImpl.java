package com.taobao.lottery.biz.manager.impl;

import com.alibaba.citrus.util.StringUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import com.google.common.cache.*;
import com.taobao.lottery.biz.buc.BucService;
import com.taobao.lottery.biz.manager.KeyValueManager;
import com.taobao.lottery.dal.dao.KeyValueDao;
import com.taobao.lottery.dal.dataObject.ActivityInfo;
import com.taobao.lottery.dal.dataObject.KeyValue;
import com.taobao.lottery.dal.jsonModel.Participant;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Created by jianghan.jh on 2016/8/11.
 *
 * Operate the database in KeyValue way
 */
public class KeyValueManagerImpl implements KeyValueManager {

    @Autowired
    private KeyValueDao keyValueDao;


    /****************************************************
     * 活动列表
     ****************************************************
     */

    public void newActivityUpdate(ActivityInfo activityInfo){

        SimplePropertyPreFilter filter = new SimplePropertyPreFilter(ActivityInfo.class,
                "creator_id","creator_name","activity_id",
                "title","start_time","end_time","location","create_time");

        String participants = activityInfo.getParticipants();

        List<Participant> allParticipants = JSON.parseArray(participants, Participant.class );

        for (Participant participant:allParticipants) {

            KeyValue keyValue = keyValueDao.getByKey("list_"+participant.getUserId());

            //if user has no data, create it.
            if(keyValue == null){

                KeyValue ua = new KeyValue();

                List<ActivityInfo> list = new ArrayList<ActivityInfo>();
                list.add(activityInfo);

                ua.setValue(JSON.toJSONString(list,filter));

                ua.setKey("list_"+participant.getUserId());

                Date now = new Date();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                String formattedDateTime = formatter.format(now);


                ua.setCreateTime(formattedDateTime);
                ua.setModifyTime(formattedDateTime);

                boolean success = keyValueDao.insertItem(ua);


            }else{

                //user has data, get it and update
                String acInfo = keyValue.getValue();

                ArrayList<ActivityInfo> items = JSON.parseObject(acInfo, new TypeReference<ArrayList<ActivityInfo>>() {
                });

                if(!items.contains(activityInfo)){
                    items.add(activityInfo);
                    keyValue.setValue(JSON.toJSONString(items,filter));
                    boolean success = keyValueDao.updateByKey(keyValue);
                }
            }

        }
    }


    /**
     * whether user is able to join this activity
     */
    public boolean isUserAbleJoined(String userId, int activityId){

        KeyValue ua = keyValueDao.getByKey("list_"+userId);

        if(ua == null){
            return false;
        }

        String re = ua.getValue();

        ArrayList<ActivityInfo> items = JSON.parseObject(re, new TypeReference<ArrayList<ActivityInfo>>() {
        });

        for(ActivityInfo ai : items){
            if(ai.getActivityId() == activityId){
                return true;
            }
        }
        return false;
    }

    public String getActivitiesByUserId(String userId){


        KeyValue ua = keyValueDao.getByKey("list_"+userId);

        if(ua==null){
            return "[]";
        }
        String re = ua.getValue();

        ArrayList<ActivityInfo> items = JSON.parseObject(re, new TypeReference<ArrayList<ActivityInfo>>() {
        });

        HashMap<String,String> nameMap = new HashMap<String, String>();
        nameMap.put("jianghan.jh","江焰");
        nameMap.put("hanqing.ghq","翰卿");
        nameMap.put("xichi.zl","夕迟");
        nameMap.put("tingxun.ltx","玄麟");
        nameMap.put("qingmian.mw","晴眠");
        nameMap.put("guangyu.tgy","青乌");

        for(ActivityInfo ai : items){

            String name = nameMap.get(ai.getCreatorId());
            if(name!=null){
                ai.setCreatorName(name);
            }
        }
        return JSON.toJSONString(items);
    }

    /****************************************************
     * 是否参加了活动
     ****************************************************
     */



    public boolean isUserAlreadyJoined(String userId, int activityId){
        KeyValue ua = keyValueDao.getByKey("join_"+userId);

        if(ua == null){
            return false;
        }

        String re = ua.getValue();

        if(re.contains(activityId+",")){
            return true;
        }

        return false;
    }

    public boolean userJoinActivity(String userId, int activityId){
        KeyValue ua = keyValueDao.getByKey("join_"+userId);

        if(ua == null){
            ua = new KeyValue();

            ua.setKey("join_"+userId);
            ua.setValue(activityId+",");

            Date now = new Date();

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String formattedDateTime = formatter.format(now);

            ua.setCreateTime(formattedDateTime);
            ua.setModifyTime(formattedDateTime);

            keyValueDao.insertItem(ua);

            return false;
        }


        String re = ua.getValue();

        if(re.contains(activityId+",")){
            return true;
        }
        re = re + activityId+",";

        ua.setValue(re);

        keyValueDao.updateByKey(ua);

        return true;
    }



    /****************************************************
     * 花名
     ****************************************************
     */
    public String getUsernameByDbOrBuc(String userId){
        KeyValue ua = keyValueDao.getByKey("buc_"+userId);

        if(ua == null){
            ua = new KeyValue();

            ua.setKey("buc_"+userId);

            String name = BucService.getName(userId);
            ua.setValue(name);

            Date now = new Date();

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String formattedDateTime = formatter.format(now);
            ua.setCreateTime(formattedDateTime);
            ua.setModifyTime(formattedDateTime);

            keyValueDao.insertItem(ua);
            return name;
        }

        String re = ua.getValue();
        if(StringUtil.isBlank(re)){
            return null;
        }
        return re;
    }


    public void addUsername(String userId,String username){

        KeyValue ua = keyValueDao.getByKey("buc_"+userId);

        if(ua == null){
            ua = new KeyValue();

            ua.setKey("buc_"+userId);
            ua.setValue(username);

            Date now = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String formattedDateTime = formatter.format(now);
            ua.setCreateTime(formattedDateTime);
            ua.setModifyTime(formattedDateTime);

            keyValueDao.insertItem(ua);
        }else{
            if(!StringUtil.isBlank(username)){
                ua.setValue(username);
                keyValueDao.updateByKey(ua);
            }
        }
    }



    public static void main(String[] args) throws ExecutionException, InterruptedException{
        //缓存接口这里是LoadingCache，LoadingCache在缓存项不存在时可以自动加载缓存
//        LoadingCache<String,String> studentCache


//        for (int i=0;i<20;i++) {
//            //从缓存中得到数据，由于我们没有设置过缓存，所以需要通过CacheLoader加载缓存数据
//            String student = studentCache.get(i+"");
//            System.out.println(student);
//            //休眠1秒
//            TimeUnit.SECONDS.sleep(1);
//        }
//
//        System.out.println("cache stats:");
//        //最后打印缓存的命中率等 情况
//        System.out.println(studentCache.stats().toString());
    }





}
