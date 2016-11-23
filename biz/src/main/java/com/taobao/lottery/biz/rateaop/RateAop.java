package com.taobao.lottery.biz.rateaop;

import com.taobao.lottery.biz.rateswitch.RateTypeSwitch;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * 用于拦截
 *
 *
 * Created by qingmian.mw on 2016/8/15.
 */
public class RateAop implements MethodInterceptor{

	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable{
			boolean isSimple = RateTypeSwitch.SimpleLimit;
			try {
				if (isSimple) {
					//简单的限流操作
					//在限制的数目的前面
					return "rate 数目达到阈值，抛弃本次请求";
				} else {
					return invocation.proceed();
				}
			} catch (Exception e){
				//异常处理
				return "rate拦截发生异常，忽略本次请求";
			}
	}
}
