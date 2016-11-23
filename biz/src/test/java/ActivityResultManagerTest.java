import com.taobao.lottery.biz.manager.ActivityResultManager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import static junit.framework.TestCase.assertNotNull;

/**
 * Created by LTX on 2016/8/11.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:lottery/dal/dao.xml",
        "classpath:lottery/dal/data-source.xml",
        "classpath:lottery/biz/manager.xml"})
@Transactional
@TransactionConfiguration(transactionManager = "txManager")
public class ActivityResultManagerTest {

    @Autowired
    private ActivityResultManager activityResultManager;

    @Test
    public void addResult(){
        assertNotNull(activityResultManager);
    }
}
