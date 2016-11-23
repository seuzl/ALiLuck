package com.taobao.lottery.biz.chain.verity;

import com.taobao.lottery.biz.chain.verity.constants.ChainExceptionCode;
import com.taobao.lottery.biz.chain.verity.constants.JoinMethod;
import org.apache.commons.chain.impl.ContextBase;

import java.util.Date;

/**
 * Created by qingmian.mw on 2016/8/9.
 */
public class ClientContext extends ContextBase {
		//验证参数
		private String activityId;
		private String userId;
		private String department;
		private JoinMethod joinMethod;
		private Double positionX;
		private Double positionY;
		private Date userBeginTime;
		//扫码传入的类型
		private String type;

		//执行过程的结果
		private boolean success;
		private ChainExceptionCode chainExceptionCode;

		public String getActivityId() {
			return activityId;
		}

		public void setActivityId(String activityId) {
			this.activityId = activityId;
		}

		public String getUserId() {
			return userId;
		}

		public void setUserId(String userId) {
			this.userId = userId;
		}

		public String getDepartment() {
			return department;
		}

		public void setDepartment(String department) {
			this.department = department;
		}

		public JoinMethod getJoinMethod() {
			return joinMethod;
		}

		public void setJoinMethod(JoinMethod joinMethod) {
			this.joinMethod = joinMethod;
		}

		public Double getPositionX() {
			return positionX;
		}

		public void setPositionX(Double positionX) {
			this.positionX = positionX;
		}

		public Double getPositionY() {
			return positionY;
		}

		public void setPositionY(Double positionY) {
			this.positionY = positionY;
		}

		public Date getUserBeginTime() {
			return (Date)userBeginTime.clone();
		}

		public void setUserBeginTime(Date userBeginTime) {
			this.userBeginTime = (Date)userBeginTime.clone();
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public boolean isSuccess() {
			return success;
		}

		public void setSuccess(boolean success) {
			this.success = success;
		}

		public ChainExceptionCode getChainExceptionCode() {
			return chainExceptionCode;
		}

		public void setChainExceptionCode(ChainExceptionCode chainExceptionCode) {
			this.chainExceptionCode = chainExceptionCode;
		}
}
