package com.cn.ccj.springboot03.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.concurrent.TimeUnit;

public class RedisOperate {

    @Autowired
    private RedisTemplate redisTemplate;


    public static Logger logger = LoggerFactory.getLogger(RedisOperate.class);

    public void saveMessageToRedis(String key,Object value) throws Exception {
        redisTemplate.setKeySerializer(new StringRedisSerializer());//key序列化
        try {
            redisTemplate.opsForValue().set(key, value);
        }catch (Exception e){
            logger.error("保存redis失败，原因："+e.getMessage());
            throw new Exception("保存redis失败");
        }
    }

    public Object getMessageFromRedis(String key) throws Exception {
        redisTemplate.setKeySerializer(new StringRedisSerializer());//key序列化
        Object object = new Object();
        try{
            object = redisTemplate.opsForValue().get(key);
        }catch (Exception e){
            logger.error("获取redis值失败，原因："+e.getMessage());
            throw new Exception("获取redis值失败");
        }

        return object;
    }

    public void deleteMessageInRedis(String key) throws Exception {
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        try {
            redisTemplate.delete(key);
        }catch (Exception e){
            logger.error("删除redis失败，原因："+e.getMessage());
            throw new Exception("删除redis失败");
        }

    }

    public Object updateMessageToRedis(String key,Object updateMessage) throws Exception {
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        Object oldValue = new Object();
        try {
            oldValue = redisTemplate.opsForValue().getAndSet(key, updateMessage);
        }catch (Exception e){
            logger.error("更新redis失败，原因："+e.getMessage());
            throw new Exception("更新redis失败");
        }
        return oldValue;
    }

    public Boolean isExitKey(String key) throws Exception{
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        Boolean isExit = false;
        try {
            isExit = redisTemplate.hasKey(key);
        }catch (Exception e){
            logger.error("查询redis失败");
            throw new Exception("查询redis失败");
        }

        return isExit;
    }

    public void saveMessageToRedis02(String key, String value, int time, TimeUnit timeUnit) throws Exception {
        redisTemplate.setKeySerializer(new StringRedisSerializer());//key序列化
        try {
            redisTemplate.opsForValue().set(key, value,time,timeUnit);
        }catch (Exception e){
            logger.error("保存redis失败，原因："+e.getMessage());
            throw new Exception("保存redis失败");
        }
    }

}
