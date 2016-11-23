package com.taobao.lottery.biz.chain.verity;

import com.taobao.lottery.biz.chain.verity.constants.ChainExceptionCode;
import com.taobao.lottery.biz.chain.verity.constants.JoinMethod;
import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;
import org.apache.commons.lang.StringUtils;

/**
 * Created by qingmian.mw on 2016/8/13.
 */
public class JoinMethodCheck implements Command {
	/**
	 * 参与方法验证
	 * @param context
	 * @return
	 * @throws Exception
	 */
	public boolean execute(Context context) throws Exception {

		ClientContext clientContext = (ClientContext)context;

		//扫码进入验证type
		JoinMethod joinMethod = clientContext.getJoinMethod();
		//不是扫码进入，直接退出
		if(joinMethod == JoinMethod.URLJOIN){
			clientContext.setSuccess(false);
			clientContext.setChainExceptionCode(ChainExceptionCode.TYPE_CHECK);
			return true;
		}

		return false;
	}
}
