package com.taobao.lottery.biz.chain.verity;

import com.taobao.lottery.biz.chain.verity.constants.ChainExceptionCode;
import com.taobao.lottery.biz.chain.verity.service.PositionService;
import com.taobao.lottery.biz.manager.ActivityInfoManager;
import com.taobao.lottery.dal.dataObject.ActivityInfo;
import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;

import javax.annotation.Resource;


/**
 * Created by qingmian.mw on 2016/8/9.
 * 地理位置检查
 */

public class PositionCheck implements Command {

	@Resource
	ActivityInfoManager activityInfoManager;

	/**
	 * 地理位置验证
	 *
	 * @param context
	 * @return
	 * @throws Exception
	 */
	public boolean execute(Context context) throws Exception {
		//获取地理位置
		ClientContext clientContext = (ClientContext)context;
		Double positionX = clientContext.getPositionX();
		Double positionY = clientContext.getPositionY();
		//int activityId = Integer.valueOf(clientContext.getActivityId()).intValue();
		int activityId = Integer.parseInt(clientContext.getActivityId());
		ActivityInfo activityInfo = activityInfoManager.getActivityById(activityId);

		//当前活动的地理位置坐标

		//纬度
		double lat1 = activityInfo.getLatitude();
		//经度
		double lot1 = activityInfo.getLongitude();

		//客户端在设置不进行地理位置限制的时候，lot1=0.0 lat1=0.0
		boolean isCheckPositon = true;
		if(lot1 == 0.0 && lat1 == 0.0){
			isCheckPositon = false;
		}

		//地理位置校验
		if(isCheckPositon) {
			//活动地理范围限制
			double distanceByActivity = activityInfo.getLimit();

			//计算两点的位置的距离
			double distance = PositionService.getShortestDistanceBetweenTowCandidates(lat1, lot1, positionX, positionY);
			double difference = distance - distanceByActivity;

			//1米为单位,设置地理位置限制为500m的地理位置误差
			if (Math.abs(difference) >= 500.0) {
				clientContext.setSuccess(false);
				clientContext.setChainExceptionCode(ChainExceptionCode.POSITION_CHECK);
				return true;
			}
		}

		return false;

	}
}
