package com.taobao.lottery.biz.chain.verity;

import com.taobao.lottery.biz.chain.verity.constants.ChainExceptionCode;
import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;
import org.apache.commons.lang.StringUtils;

/**
 * Created by qingmian.mw on 2016/8/9.
 * 参数检查
 */

public class ParamCheck implements Command{

	/**
	 *参数检测合格,不做处理,否者返回参数验证结果
	 * @param context
	 * @return
	 * @throws Exception
	 */
	public boolean execute(Context context) throws Exception {

		ClientContext clientContext = (ClientContext)context;

		//活动id检测
		String activityId = clientContext.getActivityId();
		if(StringUtils.isBlank(activityId)){
			clientContext.setSuccess(false);
			clientContext.setChainExceptionCode(ChainExceptionCode.ACTIVITYID_CHECK);
			return true;
		}

		//用户id检测
		String userId =  clientContext.getUserId();
		if(StringUtils.isBlank(userId)){
			clientContext.setSuccess(false);
			clientContext.setChainExceptionCode(ChainExceptionCode.WORKID_CHECK);
			return true;
		}

		/*//部门检测
		String department = clientContext.getDepartment();
		if(StringUtils.isBlank(workId)){
			clientContext.setSuccess(false);
			clientContext.setChainExceptionCode(ChainExceptionCode.DEPARTMENGT_CHECK);
			return true;
		}*/

		return false;
	}
}
