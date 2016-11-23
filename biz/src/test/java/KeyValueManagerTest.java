import com.taobao.lottery.biz.manager.KeyValueManager;
import com.taobao.lottery.dal.dataObject.ActivityInfo;
import com.taobao.lottery.dal.dataObject.Status;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


/**
 * Created by jianghan.jh on 2016/8/11.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:lottery/dal/dao.xml",
        "classpath:lottery/dal/data-source.xml",
        "classpath:lottery/biz/manager.xml"})
public class KeyValueManagerTest {

    @Autowired
    private KeyValueManager keyValueManager;


    @Test
    public void newActivityTest() {

        ActivityInfo activityInfo = new ActivityInfo();
        Date now = new Date();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = formatter.format(now);



        activityInfo.setActivityId(0);
        activityInfo.setActivityType(1);
        activityInfo.setTitle("aa");
        activityInfo.setStartTime(formattedDateTime);
        activityInfo.setEndTime(formattedDateTime);
        activityInfo.setParticipants("[\"jianghan.jh\",\"hahhss.jh\"]");
        activityInfo.setCreateTime(formattedDateTime);
        activityInfo.setModifyTime(formattedDateTime);

        activityInfo.setLogicStatus(Status.SUCCESS.getIndex());
        activityInfo.setLongitude(1.3);
        activityInfo.setLatitude(0.3);
        activityInfo.setLimit(0.4);

        activityInfo.setCreatorId("tingxun.ltx");

        keyValueManager.newActivityUpdate(activityInfo);

        activityInfo = new ActivityInfo();

        activityInfo.setActivityId(1);
        activityInfo.setActivityType(1);
        activityInfo.setTitle("圣诞节");
        activityInfo.setStartTime(formattedDateTime);
        activityInfo.setEndTime(formattedDateTime);
//        activityInfo.setParticipants("[\"jianghan.jh\",\"tingxun.ltx\"]");
        activityInfo.setCreateTime(formattedDateTime);
        activityInfo.setModifyTime(formattedDateTime);

        activityInfo.setLogicStatus(Status.SUCCESS.getIndex());
        activityInfo.setLongitude(1.3);
        activityInfo.setLatitude(0.3);
        activityInfo.setLimit(0.4);

        activityInfo.setCreatorId("tingxun.ltx");
//        activityInfo.setPrize("[{\"name\":\"一等奖\",\"number\":13,},{\"name\":\"二等奖\",\"number\":23}]");


        keyValueManager.newActivityUpdate(activityInfo);


//        assertTrue(activityInfoDao.addActivityInfo(activityInfo));
//        List<ActivityInfo> activityInfos = activityInfoDao.getAllActivities();
//        for (ActivityInfo activityInfo1 : activityInfos){
//            System.out.println("activityInfo1 = " + activityInfo1);
//        }


    }

    @Test
    public void testGetActivitiesByUserId() {
        String re = keyValueManager.getActivitiesByUserId("jianghan.jh");
//        re = MyStringUtil.trimSqlResultToJson(re);
        System.out.println(re);
    }


    @Test
    public void testIsUserJoined() {
        assertTrue(keyValueManager.isUserAbleJoined("jianghan.jh",0));
        assertTrue(keyValueManager.isUserAbleJoined("jianghan.jh",0));

        assertTrue(!keyValueManager.isUserAbleJoined("jianghan.jh",5));
        assertTrue(!keyValueManager.isUserAbleJoined("jianghan.jh2",5));
    }



    @Test
    public void testUserJoined(){

        assertFalse(keyValueManager.isUserAlreadyJoined("jianghan.jh",456));
        assertTrue(keyValueManager.isUserAlreadyJoined("jianghan.jh",123));
        assertTrue(keyValueManager.userJoinActivity("jianghan.jh", 123));
        assertTrue(keyValueManager.userJoinActivity("jianghan.jh", 456));
        assertTrue(keyValueManager.isUserAlreadyJoined("jianghan.jh",456));

    }


    @Test
    public void cacheTest()throws ExecutionException, InterruptedException{

        HashMap<String,String> nameMap = new HashMap<String, String>();
        nameMap.put("jianghan.jh","江焰");
        nameMap.put("hanqing.ghq","翰卿");
        nameMap.put("xichi.zl","夕迟");
        nameMap.put("tingxun.ltx","玄麟");
        nameMap.put("qingmian.mw","晴眠");
        nameMap.put("guangyu.tgy","青乌");

        for (Map.Entry<String, String> entry : nameMap.entrySet()) {

            System.out.println(keyValueManager.getUsernameByDbOrBuc(  entry.getKey() ));
//            entry.getKey();
//            entry.getValue();
        }





    }


}
