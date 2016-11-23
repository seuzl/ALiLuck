package com.taobao.lottery.biz.buc;

import com.alibaba.buc.keycenter.client.HmacClient;
import com.alibaba.buc.keycenter.common.enums.Version;
import com.alibaba.buc.keycenter.common.util.ClassCastUtils;
import com.alibaba.buc.keycenter.common.util.HttpClientUtils;
import com.alibaba.citrus.util.StringUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by qingmian.mw on 2016/8/11.
 */
public class BucService {

	//流程申请分配的apiKey
	//private static String mockApiKey = "  lottery-seal-t(@2C@2p(2$rIZJb2";
	// 流程申请分配的secretKey,请注意保密,不要泄露
	//private static String mockSecretKey = "@MWIf@$n_n8b8I$@M6r$@&@_pKtC680&(___ShrQ";
	// 默认的url网址
	//private static String url = "http://u-api.alibaba.net";

	/*public static String getEmp() throws Exception{
		// 流程申请分配的apiKey
		String mockApiKey = "BUC-Mock-h8d(t_4CE&6&0@ZveUry";
		// 流程申请分配的secretKey,请注意保密,不要泄露
		String mockSecretKey = "1234n0Ejx_Q$2MYU6f$_&Q406l$$Q2zC&d4r6h$B";
		// 新建一个hmac client
		HmacClient client = new HmacClient(mockApiKey, mockSecretKey, Version.V1);

		// api url
		// 线上api url要求必须是https,日常用http://u-api.alibaba.net,线上用https://u-api.alibaba-inc.com
		String url = "http://u-api.alibaba.net";
		// rest path
		String restPath = "/rpc/enhancedUserQuery/getUserByEmpId.json";

		Map<String, String> formData = new HashMap<String, String>();
		formData.put("empId", "12345");
		// post请求提交
		String result = client.post(url+restPath, ClassCastUtils.castMapStringToMapList(formData));

		System.out.println(result);

		// 如果参数中含有中文需要encoding
		String params = HttpClientUtils.encodeParams(formData);
		// get请求提交
		result = client.get(url+restPath+"?"+params);

		System.out.println(result);

		return result;
	}*/

	public static String getAuth(String account, String password)throws Exception{
		// 流程申请分配的apiKey
		String mockApiKey = "  lottery-seal-t(@2C@2p(2$rIZJb2";
		// 流程申请分配的secretKey,请注意保密,不要泄露
		String mockSecretKey = "@MWIf@$n_n8b8I$@M6r$@&@_pKtC680&(___ShrQ";

		// 新建一个hmac client
		HmacClient client = new HmacClient(mockApiKey, mockSecretKey, Version.V1);
		// api url
		// 线上api url要求必须是https,日常用http://u-api.alibaba.net,线上用https://u-api.alibaba-inc.com
		String url = "http://u-api.alibaba.net";
		// rest path
		String restPath = "/rpc/auth/auth.json";

		Map<String, String> formData = new HashMap<String, String>();
		//{"content":true,"hasError":false}
		//{"content":false,"errors":[{"code":"1006","field":"BucException","msg":"authenticate failed"}],"hasError":true}
		/*formData.put("account","qingmian.mw");
		formData.put("password","Mw11234567890");*/
		formData.put("account",account);
		formData.put("password",password);

		String result = null;
		try {
			// post请求提交
			result = client.post(url + restPath, ClassCastUtils.castMapStringToMapList(formData));

			System.out.println(result);

			// 如果参数中含有中文需要encoding
			String params = HttpClientUtils.encodeParams(formData);

			// get请求提交
			result = client.get(url + restPath + "?" + params);

			System.out.println(result);
		}catch(Exception e){
			//发生异常,直接构造不能登录信息
			result="{\"content\":false, \"msg\":\"authenticate failed\"}";
		}

		/*String restPath1 = "/rpc/enhancedUserQuery/findUsers/byFuzzyQuery.json";
		Map<String, String> formData1 = new HashMap<String, String>();
		formData.put("fuzzyStr", "晴眠");
		// post请求提交
		result = client.post(url + restPath1, ClassCastUtils.castMapStringToMapList(formData1));*/

		/*String restPath2 = "/rpc/enhancedUserQuery/getUserByNickNameCn.json";
		Map<String, String> formData2 = new HashMap<String, String>();
		formData.put("nickNameCn", "晴眠");
		// post请求提交
		result = client.post(url + restPath2, ClassCastUtils.castMapStringToMapList(formData));

		String restPath3 = "/rpc/enhancedUserQuery/getUserByNickNameCn.json";
		Map<String, String> formData3 = new HashMap<String, String>();
		//formData.put("nickNameCn", "晴眠");
		formData.put("account",account);
		formData.put("password",password);
		formData.put("loginName", "qingmian.mw");
		// post请求提交
		result = client.post(url+ restPath3, ClassCastUtils.castMapStringToMapList(formData));*/

		return result;
	}

	/*public static String getFuzzy(String fuzzyStr){
		// 流程申请分配的apiKey
		String mockApiKey = "  lottery-seal-t(@2C@2p(2$rIZJb2";
		// 流程申请分配的secretKey,请注意保密,不要泄露
		String mockSecretKey = "@MWIf@$n_n8b8I$@M6r$@&@_pKtC680&(___ShrQ";
		// 线上api url要求必须是https,日常用http://u-api.alibaba.net,线上用https://u-api.alibaba-inc.com
		String url = "http://u-api.alibaba.net";

		// 新建一个hmac client
		HmacClient client = new HmacClient(mockApiKey, mockSecretKey, Version.V1);
		String restPath = "/rpc/enhancedUserQuery/findUsers/byFuzzyQuery.json";
		Map<String, String> formData = new HashMap<String, String>();
		formData.put("fuzzyStr", "晴眠");

		String result = null;
		try {
			// post请求提交
			result = client.post(url + restPath, ClassCastUtils.castMapStringToMapList(formData));

			System.out.println(result);

			// 如果参数中含有中文需要encoding
			String params = HttpClientUtils.encodeParams(formData);

			// get请求提交
			result = client.get(url + restPath + "?" + params);

			System.out.println(result);
		}catch(Exception e){
			//发生异常,直接构造不能登录信息
			result="{\"content\":false, \"msg\":\"authenticate failed\"}";
		}

		return result;

	}*/

	public static String  getNickNameCn(String loginName){
			// 流程申请分配的apiKey
			String mockApiKey = "  lottery-seal-t(@2C@2p(2$rIZJb2";
			// 流程申请分配的secretKey,请注意保密,不要泄露
			String mockSecretKey = "@MWIf@$n_n8b8I$@M6r$@&@_pKtC680&(___ShrQ";
			// 新建一个hmac client
			HmacClient client = new HmacClient(mockApiKey, mockSecretKey, Version.V1);

			// api url

			// 线上api url要求必须是https,日常用http://u-api.alibaba.net,线上用https://u-api.alibaba-inc.com
			String url = "http://u-api.alibaba.net";

			// rest path
			//        String restPath = "/rpc/auth/auth.json";

			String restPath = "/rpc/enhancedUserQuery/getUserByLoginName.json";

			Map<String, String> formData = new HashMap<String, String>();
			formData.put("loginName", loginName);
			//        formData.put("account", "xichi.zl");
			//        formData.put("password","Zhaolei371");

			String result = null;
			try {

				// post请求提交
				result = client.post(url+restPath, ClassCastUtils.castMapStringToMapList(formData));

				System.out.println(result);

				// 如果参数中含有中文需要encoding
				String params = HttpClientUtils.encodeParams(formData);

				// get请求提交
				result = client.get(url+restPath+"?"+params);
			} catch (UnsupportedEncodingException e) {

				e.printStackTrace();

			} catch (Exception e) {
				e.printStackTrace();
			}

			System.out.println(result);

		    JSONObject jsonObject = JSON.parseObject(result);

			if(null==jsonObject){
                Map<String, String> resultMap = new HashMap<String, String>();
                resultMap.put("code", "500");
                resultMap.put("msg","没有查询到用户信息" );
                String jsonResult = JSON.toJSONString(resultMap);
                return jsonResult;
            }else {
				JSONObject jsonObject1 = jsonObject.getJSONObject("content");
				if(null==jsonObject1){
					Map<String, String> resultMap = new HashMap<String, String>();
					resultMap.put("code", "500");
					resultMap.put("msg","没有查询到用户信息" );
					String jsonResult = JSON.toJSONString(resultMap);
					return jsonResult;
				}
                String nickNameCn = jsonObject1.getString("nickNameCn");
                String empId = jsonObject1.getString("empId");
                String lastName = jsonObject1.getString("lastName");
                String emailPrefix = jsonObject1.getString("emailPrefix");
                Map<String, String> resultMap = new HashMap<String, String>();
                resultMap.put("nickNameCn", nickNameCn);
                resultMap.put("empId", empId);
                resultMap.put("lastName", lastName);
                resultMap.put("emailPrefix", emailPrefix);
                String jsonResult = JSON.toJSONString(resultMap);
                return jsonResult;
            }

		}

    public static String getNickName(String user_id)
    {
        String result = getNickNameCn(user_id);
        Map<String, String> temp = JSON.parseObject(result, new TypeReference<HashMap<String, String>>() {});
        if(temp.get("code") != null)
            return null;
        return temp.get("nickNameCn");
    }

	/*public static String  getFuzzy(String fuzzyStr){
		// 流程申请分配的apiKey
		String mockApiKey = "  lottery-seal-t(@2C@2p(2$rIZJb2";
		// 流程申请分配的secretKey,请注意保密,不要泄露
		String mockSecretKey = "@MWIf@$n_n8b8I$@M6r$@&@_pKtC680&(___ShrQ";
		// 新建一个hmac client
		HmacClient client = new HmacClient(mockApiKey, mockSecretKey, Version.V1);

		// api url

		// 线上api url要求必须是https,日常用http://u-api.alibaba.net,线上用https://u-api.alibaba-inc.com
		String url = "http://u-api.alibaba.net";

		// rest path
		//        String restPath = "/rpc/auth/auth.json";

		String restPath = "/rpc/enhancedUserQuery/findUsers/byFuzzyQuery.json";

		Map<String, String> formData = new HashMap<String, String>();
		formData.put("fuzzyStr", "晴眠");
		//        formData.put("account", "xichi.zl");
		//        formData.put("password","Zhaolei371");

		String result = null;
		try {

			// post请求提交
			result = client.post(url+restPath, ClassCastUtils.castMapStringToMapList(formData));

			System.out.println(result);

			// 如果参数中含有中文需要encoding
			String params = HttpClientUtils.encodeParams(formData);

			// get请求提交
			result = client.get(url+restPath+"?"+params);
		} catch (UnsupportedEncodingException e) {

			e.printStackTrace();

		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println(result);


		return result;
	}*/



	public static String getName(String userId){
		// 流程申请分配的apiKey
		String mockApiKey = "  lottery-seal-t(@2C@2p(2$rIZJb2";
		// 流程申请分配的secretKey,请注意保密,不要泄露
		String mockSecretKey = "@MWIf@$n_n8b8I$@M6r$@&@_pKtC680&(___ShrQ";
		// 新建一个hmac client
		HmacClient client = new HmacClient(mockApiKey, mockSecretKey, Version.V1);

		// 线上api url要求必须是https,日常用http://u-api.alibaba.net,线上用https://u-api.alibaba-inc.com
		String url = "http://u-api.alibaba.net";

		String restPath = "/rpc/enhancedUserQuery/getUserByLoginName.json";

		Map<String, String> formData = new HashMap<String, String>();
		formData.put("loginName", userId);

		String result = null;
		try {
			// post请求提交
			result = client.post(url+restPath, ClassCastUtils.castMapStringToMapList(formData));

			System.out.println(result);

			// 如果参数中含有中文需要encoding
			String params = HttpClientUtils.encodeParams(formData);

			// get请求提交
			result = client.get(url+restPath+"?"+params);
		} catch (UnsupportedEncodingException e) {

			e.printStackTrace();
		} catch (Exception e) {

			e.printStackTrace();
		}

		System.out.println(result);

		JSONObject jsonObject = JSON.parseObject(result);

		if(null==jsonObject){
			System.err.println("json 解析出错");
		}else {
			JSONObject jsonObject1 = jsonObject.getJSONObject("content");
			if(null==jsonObject1){
				System.err.println("json 解析出错2");
				return null;
			}
			String nickNameCn = jsonObject1.getString("nickNameCn");

			if(!StringUtil.isBlank(nickNameCn)){
				return nickNameCn;
			}else {
				System.err.println("中文名为空");
				return null;
			}

		}
		return null;
	}

}
