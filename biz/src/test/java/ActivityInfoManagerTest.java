import com.taobao.lottery.biz.manager.ActivityInfoManager;
import com.taobao.lottery.dal.dataObject.ActivityInfo;
import com.taobao.lottery.dal.dataObject.Status;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.taobao.tddl.common.utils.Assert.assertNotNull;
import static com.taobao.tddl.common.utils.Assert.assertTrue;

/**
 * Created by LTX on 2016/8/10.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:lottery/dal/dao.xml",
        "classpath:lottery/dal/data-source.xml",
        "classpath:lottery/biz/manager.xml"})
public class ActivityInfoManagerTest {

    @Autowired
    private ActivityInfoManager activityInfoManager;

    @Test
    public void addActivityInfo(){

        ActivityInfo activityInfo = new ActivityInfo();
        Date now = new Date();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = formatter.format(now);

        activityInfo.setActivityType(1);
        activityInfo.setTitle("aa");
        activityInfo.setStartTime(formattedDateTime);
        activityInfo.setEndTime(formattedDateTime);
        activityInfo.setParticipants("dd");
        activityInfo.setCreateTime(formattedDateTime);
        activityInfo.setModifyTime(formattedDateTime);

        activityInfo.setLogicStatus(Status.SUCCESS.getIndex());

        assertTrue(activityInfoManager.addActivityInfo(activityInfo));

    }

    @Test
    public void getGroupById(){
        String group = activityInfoManager.getGroupByActivityId(5);
        assertNotNull(group);


        List<ActivityInfo> ais = activityInfoManager.getActivitiesByCreatorId("jianghan.jh");
        for(ActivityInfo ai: ais){
            System.out.println(ai.getTitle());
        }
    }



}
