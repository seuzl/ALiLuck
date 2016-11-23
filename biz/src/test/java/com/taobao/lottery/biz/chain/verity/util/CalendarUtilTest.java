package com.taobao.lottery.biz.chain.verity.util;

import org.junit.Test;

import java.util.Date;

/**
 * Created by qingmian.mw on 2016/8/10.
 */
public class CalendarUtilTest {
	@Test
	public void testDate(){
		Date date = new Date();
		System.out.println(date.toString());
	}
	@Test
	public void testDateFormat(){
		Date date = new Date();
		String pattern = CalendarUtil.toString(date, CalendarUtil.TIME_PATTERN);
		System.out.println("revert pattern:"+pattern);
		date = CalendarUtil.toDate(pattern, CalendarUtil.TIME_PATTERN);
		System.out.println("revert date:"+date);
	}
}
