package com.cn.ccj.springboot03.serviceImpl;

import com.cn.ccj.springboot03.dao.MainDao;
import com.cn.ccj.springboot03.entity.User;
import com.cn.ccj.springboot03.iservice.IMainSV;
import com.cn.ccj.springboot03.redis.RedisOperate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class MainSVImpl implements IMainSV {

    @Autowired
    private MainDao mainDao;

    @Autowired
    private RedisOperate redisOperate;

    public static Logger logger = LoggerFactory.getLogger(RedisOperate.class);

    @Override
    public Map getUserMsssage(String userId) {
        Map map = mainDao.getUserName(userId);
        return map;
    }

    @Override
    public int insertUserMessage(Map reqMap) throws Exception {
        int result = -1;
        if(null!=reqMap&&reqMap.size()>0){
            Map map = new HashMap();
            map.put("userId",reqMap.get("userId"));
            map.put("userName",reqMap.get("userName"));
            map.put("gender",reqMap.get("gender"));
            map.put("age",reqMap.get("age"));
            try {
                result = mainDao.insertUserMessage(map);
            }catch (Exception e){
                logger.error("保存用户信息失败："+e.getMessage());
                throw new Exception("保存用户信息失败");
            }
            if(result>0){
                User user = new User();
                user.setAge((int)reqMap.get("age"));
                user.setId((String)reqMap.get("userId"));
                user.setName((String)reqMap.get("userName"));
                user.setGender((String) reqMap.get("gender"));
                try {
                    redisOperate.saveMessageToRedis((String) reqMap.get("userId"), user);
                }catch (Exception e){
                    logger.error("保存用户信息至redis失败："+e.getMessage());
                    throw new Exception("保存用户信息至redis失败");
                }
            }
        }
        return result;
    }
}
