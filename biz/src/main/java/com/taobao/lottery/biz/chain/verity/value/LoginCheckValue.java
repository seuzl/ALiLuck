package com.taobao.lottery.biz.chain.verity.value;

import com.alibaba.citrus.service.pipeline.PipelineContext;
import com.alibaba.citrus.service.pipeline.support.AbstractValve;
import com.alibaba.citrus.turbine.TurbineRunData;
import com.alibaba.citrus.turbine.util.TurbineUtil;
import com.alibaba.fastjson.JSON;
import com.taobao.lottery.biz.chain.verity.ClientContext;
import com.taobao.lottery.biz.chain.verity.VerityChain;
import com.taobao.lottery.biz.chain.verity.cache.TokenCache;
import com.taobao.lottery.biz.chain.verity.util.Request2ClientContext;
import com.taobao.lottery.biz.chain.verity.util.TokenProcessor;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.annotation.Resource;
import javax.lang.model.SourceVersion;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by qingmian.mw on 2016/8/11.
 */
public class LoginCheckValue extends AbstractValve {

	@Resource
	HttpServletRequest request;
	@Resource
	HttpServletResponse response;

	/**
	 * login.do  scanCode.do
	 *
	 *
	 * 其他的接口请求情况一定是下面两种
	 *
	 * user_id 不为空 reverse_id 为空
	 * access_token不为空 reverse_token为空
	 *
	 * user_id 不为空 reverse_id 不为空
	 * access_token不为空 reverse_token不为空
	 *
	 * @param pipelineContext
	 * @throws Exception
	 */
	public void invoke(PipelineContext pipelineContext) throws Exception {
		response.setContentType("application/json");
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Headers", "user_id,access_token,reverse_id,reverse_token");

		//因为头部加入信息，解决跨域问题，浏览器
		if(request.getMethod().equals("OPTIONS")){
			response.setStatus(204);
			response.getWriter().println();
			System.out.println("LoginCheckValue OPTIONS");
			return;
		}

		String uriStr = request.getRequestURI();
		System.out.println("uriStr:"+uriStr);

		//首页,helloWorld.vm,login.do登录,scanCode.do扫码登录,helloWorld.do测试连通    不拦截
		if(uriStr.endsWith("/") || uriStr.endsWith("/helloWorld.vm") ||
				uriStr.endsWith("/login.do") || uriStr.endsWith("/scanCode.do") ||uriStr.endsWith("/helloWorld.do")){
			pipelineContext.invokeNext();
			return;
		}

		//其他页面需要token验证

		//为了获取本地token的缓存
		String userId = request.getHeader("user_id");
		String reverseId = request.getHeader("reverse_id");

		//取得请求者头中的access_token
		String accessToken = (String)request.getHeader("access_token");
		//多点传输reverse_token验证，reverse_token不为空表明是转发
		String reverseToken = (String)request.getHeader("reverse_token");
		System.out.println("accessToken = " + accessToken);
		System.out.println("reverseToken = " + reverseToken);

		//请求头没有accessToken,表示没有登录
		if(StringUtils.isBlank(accessToken)){
			//直接告知客户端,没有登录
			Map<String,String> resultMap = new HashMap<String,String>();
			resultMap.put("code","500");
			resultMap.put("msg","请先登录");
			String resultJson = JSON.toJSONString(resultMap);
			response.getWriter().println(resultJson);
			return ;
		} else {
			/*
			检测token的是否有效
			HttpSession session = request.getSession();
			String accessTokenOfSession = (String)session.getAttribute("access_token");
			*/

			//原来存在服务器为每个用户的session，现在存在公共的TokenCache中
			String accessTokenOfCache = null;
			String accessTokenReverseOfCache = null;


			if(!StringUtils.isBlank(userId))
				accessTokenOfCache = TokenCache.getToken(userId);
			if(!StringUtils.isBlank(reverseId))
				accessTokenReverseOfCache = TokenCache.getToken(reverseId);
			System.out.println("userId:"+userId);
			System.out.println("reverseId = " + reverseId);
			System.out.println("accessTokenOfCache = " + accessTokenOfCache);
			System.out.println("accessTokenReverseOfCache = " + accessTokenReverseOfCache);

			//验证accessToken
			if(accessToken.equals(accessTokenOfCache)){

				/*//查看是否有转换者信息，如果有验证转发者
				if(!StringUtils.isBlank(reverseToken)){
					//验证转换者token是否合法
					if(!reverseToken.equals(accessTokenReverseOfCache)){
						Map<String,String> resultMap = new HashMap<String,String>();
						resultMap.put("code","500");
						resultMap.put("msg","转发者token失效");
						String resultJson = JSON.toJSONString(resultMap);
						response.getWriter().println(resultJson);
						return;
					}
				}

				//其他情况正常通过
				// accessToken正确，没有转发者reverseToken，表示自身请求
				// accessToken正确，转发者reverseToken不为空，reverseToken合法，表示转发请求
				pipelineContext.invokeNext();*/

				//表明自身请求
				if(StringUtils.isBlank(reverseToken)){
					pipelineContext.invokeNext();
				}//表明本次是转发请求
				else{
					//判断转发的token是否匹配
					if(reverseToken.equals(accessTokenReverseOfCache)){
						pipelineContext.invokeNext();
					}//转发者token不匹配，表明没有登录
					else{
						Map<String,String> resultMap = new HashMap<String,String>();
						resultMap.put("code","500");
						resultMap.put("msg","转发者token失效");
						String resultJson = JSON.toJSONString(resultMap);
						response.getWriter().println(resultJson);
						return;
					}
				}

			}//自身token不合法
			else{
				Map<String,String> resultMap = new HashMap<String,String>();
				resultMap.put("code","500");
				resultMap.put("msg","请求者token失效");
				String resultJson = JSON.toJSONString(resultMap);
				response.getWriter().println(resultJson);
			}


		}

	}

}

