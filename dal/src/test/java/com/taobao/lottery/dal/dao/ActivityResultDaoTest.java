package com.taobao.lottery.dal.dao;

import com.taobao.lottery.dal.dataObject.ActivityResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.taobao.tddl.common.utils.Assert.assertTrue;

/**
 * Created by LTX on 2016/8/9.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:lottery/dal/data-source.xml",
        "classpath:lottery/dal/dao.xml"})
@Transactional
@TransactionConfiguration(transactionManager = "txManager")
public class ActivityResultDaoTest {

    @Autowired
    private ActivityResultDao activityResultDao;

    @Test
    @Rollback(true)
    public void addActivityResultTest(){
        ActivityResult activityResult = new ActivityResult();
        Date now = new Date();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = formatter.format(now);

        activityResult.setActivityId(1);
        activityResult.setResult("result");
        activityResult.setCreateTime(formattedDateTime);
        activityResult.setModifyTime(formattedDateTime);

        assertTrue(activityResultDao.addActivityResult(activityResult));
    }

    @Test
    public void getResultById(){
        int activityId = 111;
        String result = activityResultDao.getResultByActivityId(activityId);
        System.out.println("result = " + result);

        //Map<String,String> resultMap = (Map<String, String>) JSON.parse(result);
    }


}
