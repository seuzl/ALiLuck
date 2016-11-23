package com.taobao.lottery.dal;

import com.ibatis.sqlmap.client.extensions.ParameterSetter;
import com.ibatis.sqlmap.client.extensions.ResultGetter;
import com.ibatis.sqlmap.client.extensions.TypeHandlerCallback;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CustomDateHandler implements TypeHandlerCallback {

    @Override
    public Object getResult(ResultGetter getter) throws SQLException {

        final Object obj = getter.getTimestamp();
        Date date = (Date) obj;

        // Convert input string into a date
//        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.S");
//        Date date = inputFormat.parse(inputString);

// Format date into output format
        DateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String outputString = outputFormat.format(date);

        return outputString;
    }

    @Override
    public void setParameter(ParameterSetter setter, Object value) throws SQLException {


        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.S");
        Date date = null;
        try {
            date = inputFormat.parse((String)value);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        setter.setTimestamp(value != null ? new Timestamp((date).getTime()) : null);
//        setter.setTimestamp();

//        value != null
//        ? new Timestamp(((Date)value).getTime()) :
//        null;
    }

    @Override
    public Object valueOf(String datetime) {
        return Timestamp.valueOf(datetime);
    }
}