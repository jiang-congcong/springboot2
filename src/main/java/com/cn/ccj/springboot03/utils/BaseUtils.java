package com.cn.ccj.springboot03.utils;

import com.cn.ccj.springboot03.redis.RedisOperate;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * @author jiangcongcong
 * @date 2021/3/31 18:04
 */
public class BaseUtils {

    @Autowired
    private  RedisOperate redisOperate;

    //根据table数据库表名生成主键id
    public  String getSequence(String tableName) throws Exception {

        String redis_tab = "REDIS_TAB_"+tableName;
        String dateString = String.valueOf(System.currentTimeMillis());
        Object objectNum = redisOperate.getMessageFromRedis(redis_tab);
        String id = dateString;
        if(null!=objectNum){
            Long increNum = Long.parseLong(objectNum.toString());
            increNum++;
            redisOperate.updateMessageToRedis(redis_tab,increNum);//更新递增数
            id = dateString+increNum;
        }
        return id;
    }

}
