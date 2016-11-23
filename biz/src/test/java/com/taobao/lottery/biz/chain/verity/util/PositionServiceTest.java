package com.taobao.lottery.biz.chain.verity.util;

import com.taobao.lottery.biz.chain.verity.service.PositionService;
import org.junit.Test;

/**
 * Created by qingmian.mw on 2016/8/10.
 */
public class PositionServiceTest {
	@Test
	public void test(){

		double lon1 = 39.9042570000;
		double lat1 = 116.4330790000;
		double lon2 = 30.2541070000;
		double lat2 = 120.1378340000;
		/*double lon1 = 30.28035141;
		double lat1 = 120.02855301;
		double lon2 = 30.27924423;
		double lat2 = 120.02593517;*/

		double position = PositionService.getShortestDistanceBetweenTowCandidates(lon1,lat1,lon2,lat2);
		System.out.println(position);

	}
}
