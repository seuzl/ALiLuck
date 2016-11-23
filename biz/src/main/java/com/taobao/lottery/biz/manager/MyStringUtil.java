package com.taobao.lottery.biz.manager;

/**
 * Created by LTX on 2016/8/11.
 */
public class MyStringUtil {
    public static String trimSqlResultToJson(String sqlResult){
        String result = sqlResult.replace("\\\"","\"");
        result = result.replace("\"[","[");
        result = result.replace("]\"","]");
        return result;
    }
}
