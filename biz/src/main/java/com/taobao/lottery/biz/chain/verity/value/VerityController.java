package com.taobao.lottery.biz.chain.verity.value;

/**
 * Created by qingmian.mw on 2016/8/9.
 */

import com.alibaba.citrus.service.pipeline.PipelineContext;
import com.alibaba.citrus.service.pipeline.support.AbstractValve;
import com.taobao.lottery.biz.chain.verity.ClientContext;
import com.taobao.lottery.biz.chain.verity.VerityChain;
import com.taobao.lottery.biz.chain.verity.util.Request2ClientContext;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by qingmian.mw on 2016/8/9.
 * 一次请求的验证,这个是写在web模块的
 */

public class VerityController extends AbstractValve{

	@Resource
	HttpServletRequest request;
	@Resource
	HttpServletResponse response;
	@Resource
	private VerityChain verityChain;


	public void invoke(PipelineContext pipelineContext) throws Exception {
		//http请求转为能处理的结构体
		ClientContext clientContext = Request2ClientContext.getClientContext(request);

		verityChain.execute(clientContext);

		// 验证链返回true,表明验证不通过
		if(!clientContext.isSuccess()){
			String mes = clientContext.getChainExceptionCode().getMessage();
			Integer code = clientContext.getChainExceptionCode().getCode();
			String result = mes+code;
			response.getWriter().println(result);
		}else {
			//接下来的操作
			pipelineContext.invokeNext();
		}
	}

}

