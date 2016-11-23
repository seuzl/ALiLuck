package com.taobao.lottery.dal.dao;

import com.taobao.lottery.dal.dataObject.KeyValue;

import java.util.List;

/**
 * Created by jianghan.jh on 2016/8/9.
 */
public interface KeyValueDao {

    List<KeyValue> getAllUserActivity();

    public boolean insertItem(KeyValue keyValue);

    public boolean updateById(KeyValue keyValue);

    public KeyValue findById(int id);

    public boolean deleteById(int id);

    public KeyValue findByKey(String key);

    public KeyValue getByKey(String key);

    public boolean updateByKey(KeyValue keyValue);

}
