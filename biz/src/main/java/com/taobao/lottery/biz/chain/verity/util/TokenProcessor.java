package com.taobao.lottery.biz.chain.verity.util;

/**
 * Created by qingmian.mw on 2016/8/10.
 */

import sun.misc.BASE64Encoder;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

//令牌生产器
public class TokenProcessor {

	//通过系统时间和user_id生成
	public static String generateTokeCode(String user_id){
		StringBuffer buffer = new StringBuffer();
		buffer.append(System.currentTimeMillis());
		buffer.append(user_id);
		String value = buffer.toString();
		//获取数据指纹,指纹是唯一的
		try {
			MessageDigest md = MessageDigest.getInstance("md5");
			//产生数据的指纹
			byte[] b = new byte[0];
			try {
				b = md.digest(value.getBytes("UTF-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			//Base64编码
			BASE64Encoder be = new BASE64Encoder();
			be.encode(b);
			return be.encode(b);//制定一个编码
		} catch (NoSuchAlgorithmException e) {
			//发生异常直接返回一个设定的access_token
			return "qingmian";
		}

	}

	/*//解码传递过来的token
	public static String getWorkId(String token){

	}*/
}