package com.taobao.lottery.biz.chain.verity.util;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.taobao.lottery.biz.chain.verity.ClientContext;
import com.taobao.lottery.biz.chain.verity.constants.JoinMethod;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by qingmian.mw on 2016/8/9.
 */
public class Request2ClientContext {

	/**
	 * 查找request请求的参数,转化到
	 * 1. type
	 * 2. position(x,y)
	 * 3. workId
	 * 4. begintime
	 * 5  department
	 */
	public static ClientContext getClientContext(HttpServletRequest request){
		//pap测试只有get请求，为了兼容
		String test = request.getParameter("test");

		if(StringUtils.isBlank(test)){
			StringBuffer applicationJson = new StringBuffer();
			String line = null;
			try {
				BufferedReader reader = request.getReader();
				while ((line = reader.readLine()) != null)
					applicationJson.append(line);
			} catch (Exception e) {
			/*report an error*/
				e.printStackTrace();
			}
			System.out.println("json is  = " + applicationJson);
			test = applicationJson.toString();
		}

		HashMap<String, String> data = JSONObject
				.parseObject(test, new TypeReference<HashMap<String, String>>() {
				});


		ClientContext clientContext = new ClientContext();
		//初始为
		clientContext.setSuccess(true);

		/*String activityId = request.getParameter("activity_Id");
		String workId = request.getParameter("user_id");
		String department = request.getParameter("department");
		String type = request.getParameter("type");
		String userBeginTime = request.getParameter("userBeginTime");
		String positionX = request.getParameter("longitude");
		String positionY = request.getParameter("latitude");*/

		String activityId = data.get("activity_id");
		String userId = data.get("user_id");
		String department = data.get("department");
		String type = data.get("type");
		String userBeginTime = data.get("userBeginTime");
		//纬度
		String positionX = data.get("latitude");
		//经度
		String positionY = data.get("longitude");


		//设置所需要检测的参数
		if(!StringUtils.isBlank(activityId)){
			//activityId一定不为null
			activityId = StringUtils.trim(activityId);
			clientContext.setActivityId(activityId);
		}

		if(!StringUtils.isBlank(userId)) {
			//workId一定不为null
			userId = StringUtils.trim(userId);
			clientContext.setUserId(userId);
		}

		//department可以为空
		if(!StringUtils.isBlank(department)){
			department = StringUtils.trim(department);
			clientContext.setDepartment(department);
		}

		//type可以为空
		if(StringUtils.isBlank(type)){
			//传递的type为空,默认为URLJOIN
			clientContext.setJoinMethod(JoinMethod.URLJOIN);
		}else{
			type = StringUtils.trim(type);
			//1代表扫码进入,其他URL进入
			if(type.equals("1")){
				clientContext.setJoinMethod(JoinMethod.SCANCODE);
			}else {
				clientContext.setJoinMethod(JoinMethod.URLJOIN);
			}
		}

		//userBeginTime可以为空
		if(StringUtils.isBlank(userBeginTime)){
			//没有传递开始时间,设置用户开始时间为进入系统的时间
			Date date = new Date();
			clientContext.setUserBeginTime(date);
		}else{
			//时间格式定义为"yyyy-MM-dd HH:mm:ss"  2016-08-10 13:43:52
			userBeginTime = StringUtils.trim(userBeginTime);
			//如果非法非法格式,处理方式按照系统时间
			Date date = CalendarUtil.toDate(userBeginTime,CalendarUtil.TIME_PATTERN);
			if(null == date){
				date = new Date();
			}
			clientContext.setUserBeginTime(date);
		}

		//positionX,positionY可以为空
		//根据活动设置地理位置限制,进行地理位置验证
		if(StringUtils.isBlank(positionX)){
			clientContext.setPositionX(Double.valueOf(0.0));
		} else {
			positionX = StringUtils.trim(positionX);
			try {
				clientContext.setPositionX(Double.valueOf(positionX));
			}catch(Exception e){
				//转化异常,设置默认地址坐标
				clientContext.setPositionX(Double.valueOf(0.0));
			}
		}

		if(StringUtils.isBlank(positionY)){
			clientContext.setPositionY(Double.valueOf(0.0));
		}else{
			positionY = StringUtils.trim(positionY);
			try {
				clientContext.setPositionY(Double.valueOf(positionY));
			}catch(Exception e){
				//转化异常,设置默认地址坐标
				clientContext.setPositionY(Double.valueOf(0.0));
			}
		}

		return clientContext;
	}
}
