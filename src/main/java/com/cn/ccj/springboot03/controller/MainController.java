package com.cn.ccj.springboot03.controller;

import com.cn.ccj.springboot03.entity.RequestInputObject;
import com.cn.ccj.springboot03.entity.ResultOutputObject;
import com.cn.ccj.springboot03.entity.User;
import com.cn.ccj.springboot03.iservice.IMainSV;
import com.cn.ccj.springboot03.redis.RedisOperate;
import io.netty.util.internal.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.logging.log4j.core.pattern.AbstractStyleNameConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("controller")
/**
 * @RestController就是@ResponseBody和@Controller的合体
 *
 */
@RestController

@Api(value = "mainController",description = "主操作类")
public class MainController {

    @Autowired
    private IMainSV iMainSV;

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    RedisOperate redisOperate;

    public static Logger logger = LoggerFactory.getLogger(RedisOperate.class);

    @ApiOperation(value = "获取用户信息")
    @RequestMapping(method = RequestMethod.POST,value = "/getUserMessage")
    public ResultOutputObject getUserMeaager(@RequestBody RequestInputObject requestInputObject) throws Exception {
        ResultOutputObject resultOutputObject = new ResultOutputObject();

        Map params = requestInputObject.getParams();
        String key = (String) params.get("userId");
        User user = new User();
        try {
            if (redisOperate.isExitKey(key)) { //先查缓存
                user = (User) redisOperate.getMessageFromRedis(key);
                if (null == user) {
                    Map map = iMainSV.getUserMsssage(key);
                    if(null!=map&&map.size()>0){
                        user.setAge((int)map.get("age"));
                        user.setId((String)map.get("userId"));
                        user.setName((String)map.get("userName"));
                        user.setGender((String) map.get("gender"));
                    }
                    redisOperate.saveMessageToRedis(key,user);//将信息保存进缓存

                }
            }
            else {
                Map map = iMainSV.getUserMsssage(key); //查数据库
                if(null!=map&&map.size()>0){
                    user.setAge((int)map.get("age"));
                    user.setId((String)map.get("userId"));
                    user.setName((String)map.get("userName"));
                    user.setGender((String) map.get("gender"));
                }
                redisOperate.saveMessageToRedis(key,user);//将信息保存进缓存
            }
        }
        catch (Exception e){
            logger.error("查询用户信息失败");
            throw new Exception("保存用户信息失败");
        }
        Map resultMap = new HashMap();
        resultMap.put("userMeaage",user);
        List resultList = new ArrayList();
        resultList.add(resultMap);
        resultOutputObject.setBeans(resultList);
        resultOutputObject.setRtnMsg("查询用户信息成功");
        resultOutputObject.setRtnCode("0");

        return resultOutputObject;
    }


    @ApiOperation(value = "保存用户信息")
    @RequestMapping(method = RequestMethod.POST,value = "/setUserMessage")
    public ResultOutputObject setUserNameToRedis(@RequestBody RequestInputObject requestInputObject) throws Exception {
        ResultOutputObject resultOutputObject = new ResultOutputObject();
        Map params = requestInputObject.getParams();
        int result = 0 ;
        try {
            result = iMainSV.insertUserMessage(params);
        }catch (Exception e){
            logger.error("保存用户信息失败："+e.getMessage());
            throw new Exception("保存用户信息失败");
        }
        if(result>0){
            resultOutputObject.setRtnCode("0");
            resultOutputObject.setRtnMsg("用户信息保存成功");
        }else {
            resultOutputObject.setRtnCode("-9999");
            resultOutputObject.setRtnMsg("用户信息保存失败");
        }
        return resultOutputObject;

    }



}
