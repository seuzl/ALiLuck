package com.taobao.lottery.dal.dao.ibatis;

import com.google.common.cache.*;
import com.taobao.lottery.dal.dao.KeyValueDao;
import com.taobao.lottery.dal.dataObject.KeyValue;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Created by jianghan.jh on 2016/8/9.
 */
public class IbatisKeyValueDao extends SqlMapClientDaoSupport implements KeyValueDao {

    LoadingCache<String,String> kvCache //CacheBuilder的构造函数是私有的，只能通过其静态方法newBuilder()来获得CacheBuilder的实例
            = CacheBuilder.newBuilder()
            //设置并发级别为8，并发级别是指可以同时写缓存的线程数
            .concurrencyLevel(8)

            //设置写缓存后8秒钟过期
            .expireAfterWrite(3600, TimeUnit.SECONDS)

            //设置缓存容器的初始容量为10
            .initialCapacity(1000)

            //设置缓存最大容量为100，超过100之后就会按照LRU最近虽少使用算法来移除缓存项
            .maximumSize(10000)
            //设置要统计缓存的命中率
            .recordStats()
            //设置缓存的移除通知
            .removalListener(new RemovalListener<Object, Object>() {
                @Override
                public void onRemoval(RemovalNotification<Object, Object> notification) {
                    System.out.println(notification.getKey() + " was removed, cause is " + notification.getCause());
                }
            })
            //build方法中可以指定CacheLoader，在缓存不存在时通过CacheLoader的实现自动加载缓存
            .build(
                    new CacheLoader<String, String>() {
                        @Override
                        public String load(String key) throws Exception {
                            System.out.println("load key " + key);

                            String value = null;

                            KeyValue kv = findByKey(key);

                            if(kv == null){
                                return "null";
                            }
                            value = kv.getValue();

                            if(value == null){
                                return "null";
                            }else{
                                return value;
                            }

                        }
                    }
            );



    public List<KeyValue> getAllUserActivity() {
        return getSqlMapClientTemplate().queryForList("selectAllUserActivity");
    }

    public boolean insertItem(KeyValue keyValue) {
        kvCache.put(keyValue.getKey(),keyValue.getValue());

            int result = getSqlMapClientTemplate().update("insert", keyValue);

            if (result == 1) {
                return true;
            } else {
                return false;
            }
    }

    public boolean updateById(KeyValue keyValue){

        kvCache.put(keyValue.getKey(),keyValue.getValue());

        int result = getSqlMapClientTemplate().update("updateById", keyValue);

        if(result == 1){
            return true;
        }else {
            return false;
        }
    }


    public boolean updateByKey(KeyValue keyValue){

        kvCache.put(keyValue.getKey(),keyValue.getValue());

        int result = getSqlMapClientTemplate().update("updateByKey", keyValue);

        if(result == 1){
            return true;
        }else {
            return false;
        }
    }

    public KeyValue findById(int id){

        KeyValue re = (KeyValue) getSqlMapClientTemplate().queryForObject("findById",id);

        if(re != null){
            return re;
        }else {
            return null;
        }
    }


    public KeyValue getByKey(String key){

        try {
            String value = kvCache.get(key);

            if(value.equals("null")){
                return null;
            }

            KeyValue kv = new KeyValue();
            kv.setKey(key);
            kv.setValue(value);

            return kv;
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }


    public KeyValue findByKey(String key){

        List<KeyValue> res = (List<KeyValue>) getSqlMapClientTemplate().queryForList("findByKey",key);

        KeyValue re = null;
        if(res!=null&&res.size()!=0){
            re = res.get(0);
        }

        if(re != null){
            return re;
        }else {
            return null;
        }
    }

    public boolean deleteById(int id){
        /*int result = getSqlMapClientTemplate().delete("deleteById", id);
        if(result == 1){
            return true;
        }else {
            return false;
        }*/
        return true;
    }

}
