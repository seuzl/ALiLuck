package com.taobao.lottery.dal.dao;

import com.taobao.lottery.dal.dataObject.ActivityCate;
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
import java.util.List;

import static com.taobao.tddl.common.utils.Assert.assertTrue;

/**
 * Created by LTX on 2016/8/9.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:lottery/dal/data-source.xml",
        "classpath:lottery/dal/dao.xml"})
@Transactional
@TransactionConfiguration(transactionManager = "txManager")
public class ActivityCateDaoTest {

    @Autowired
    private ActivityCateDao activityCateDao;

    @Test
    @Rollback(true)
    public void addActivityCate(){
        ActivityCate activityCate = new ActivityCate();
        Date now = new Date();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = formatter.format(now);

        activityCate.setActivityCateName("activityCateTest");
        activityCate.setStaticUrl("staticUrlTest");
        activityCate.setPcResultUrl("pcResultUrl");
        activityCate.setMobileResultUrl("mobileResultUrl");
        activityCate.setCreateTime(formattedDateTime);
        activityCate.setModifyTime(formattedDateTime);

        assertTrue(activityCateDao.addActivityCate(activityCate));

        List<ActivityCate> activityCates = activityCateDao.getAllActivityCates();
        for(ActivityCate activityCate1 : activityCates){
            System.out.println("activityCate1 = " + activityCate1);
        }
    }
}
