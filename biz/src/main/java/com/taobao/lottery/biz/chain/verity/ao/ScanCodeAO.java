package com.taobao.lottery.biz.chain.verity.ao;

import com.alibaba.citrus.turbine.TurbineRunData;
import com.taobao.lottery.biz.chain.verity.ClientContext;
import com.taobao.lottery.biz.chain.verity.ScanCodeChain;
import com.taobao.lottery.biz.chain.verity.constants.ChainExceptionCode;
import com.taobao.lottery.biz.chain.verity.util.Request2ClientContext;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by qingmian.mw on 2016/8/13.
 */
public class ScanCodeAO {
	@Resource ScanCodeChain scanCodeChain;

	public ClientContext get(TurbineRunData runData){
		HttpServletRequest request = runData.getRequest();
		ClientContext clientContext = Request2ClientContext.getClientContext(request);

		try {
			scanCodeChain.execute(clientContext);
		}catch (Exception e){
			//发生异常,设置一个默认因素
			clientContext.setSuccess(false);
			clientContext.setChainExceptionCode(ChainExceptionCode.DEFALUT_CHECK);
		}

		return clientContext;

	}
}
