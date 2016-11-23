package com.taobao.lottery.biz.chain.verity;

import com.taobao.lottery.biz.chain.verity.constants.ChainExceptionCode;
import com.taobao.lottery.biz.manager.KeyValueManager;
import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;

import javax.annotation.Resource;

/**
 * Created by qingmian.mw on 2016/8/9.
 * 身份检查
 */
public class IdentityCheck implements Command {

	@Resource
    KeyValueManager keyValueManager;


	public boolean execute(Context context) throws Exception {
		// 验证身份
		return isExist(context);
	}

	private boolean isExist(Context context){

		ClientContext clientContext = (ClientContext)context;
		String activityId = clientContext.getActivityId();
		String userId = clientContext.getUserId();

		boolean result = false;

		//result = keyValueManager.isUserAbleJoined(userId, Integer.valueOf(activityId).intValue());
		result = keyValueManager.isUserAbleJoined(userId, Integer.parseInt(activityId));
		//不在里面
		if(!result){
			clientContext.setSuccess(false);
			clientContext.setChainExceptionCode(ChainExceptionCode.IDENTITY_CHECK);
			return true;
		}

		return false;
	}
}
