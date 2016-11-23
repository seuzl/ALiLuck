import com.alibaba.fastjson.JSON;
import com.taobao.lottery.dal.dataObject.AwardResultSingle;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hanqing.ghq on 2016/8/13.
 */
public class AwardResultSingleTest {

    @Test
    public void TestJson()
    {
        AwardResultSingle awardResultSingle = new AwardResultSingle();

        awardResultSingle.setName("一等奖");
        //awardResultSingle.setUser_id("hanqing.ghq");
        //awardResultSingle.setUser_name("翰卿");

        String awardJson = JSON.toJSONString(awardResultSingle);

        System.out.println(awardJson);
    }
}
