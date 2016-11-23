import com.alibaba.citrus.util.Assert;
import com.alibaba.fastjson.JSON;
import com.taobao.lottery.biz.algorithm.RandomLottery;
import com.taobao.lottery.dal.dataObject.AwardResultSingle;
import com.taobao.lottery.dal.dataObject.UserInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.security.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * Created by jianghan.jh on 2016/8/11.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:lottery/dal/dao.xml",
        "classpath:lottery/dal/data-source.xml",
        "classpath:lottery/biz/manager.xml"})
public class AlgorithmTest {

    @Autowired
    private  RandomLottery algorithm;


    @Test
    public void testAlgorithm() {

        algorithm.lottery(1);

        /*List<AwardResultSingle> result = algorithm.Lottery(1);
        //Assert.assertNull(result);
        //String s_result = JSON.toJSONString(result);

        for(int i = 0; i < result.size(); i++) {
            System.out.println(result.get(i).getName() + "    " + result.get(i).getNumber());
            List<UserInfo> list = result.get(i).getLucky();
            for(int j = 0; j < list.size(); j++)
            {
                System.out.println("名单：  " + list.get(j).getUser_name());
            }
        }*/

    }

}
