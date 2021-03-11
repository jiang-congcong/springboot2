package com.cn.ccj.springboot03;

import com.cn.ccj.springboot03.entity.User;
import com.cn.ccj.springboot03.redis.RedisOperate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.text.SimpleDateFormat;
import java.util.*;

@SpringBootTest
class Springboot03ApplicationTests {

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    RedisOperate redisOperate;

    @Test
    void contextLoads() {
    }

    @Test
    public void testTime(){
//        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");//设置日期格式
//        String s = df.format(new Date());
//        System.out.println(s);// new Date()为获取当前系统时间

//        String version = "202103031435050002";
//        int num = Integer.valueOf(version.substring(version.length()-4));
//        System.out.println(num);
        String s= "0000";
        String s1= "2";
        StringBuilder sb = new StringBuilder(s);
        sb.replace(4-s1.length(),5,s1);
        String s2 = "20210303143505"+sb;
        System.out.println(s2);
    }

    @Test
    public void setUserNameToRedis(){
        redisTemplate.setKeySerializer(new StringRedisSerializer());//key序列化
//        redisTemplate.opsForValue().set("id","00001");
        User user = new User();
        user.setAge(21);
        user.setGender("男");
        user.setName("聪聪");
        user.setId("0002");
        redisTemplate.opsForValue().set("user",user);
        System.out.println(redisTemplate.opsForValue().get("user"));

    }

    @Test
    public void setListToRedis() throws Exception {
        redisTemplate.setKeySerializer(new StringRedisSerializer());//key序列化
//        redisTemplate.opsForValue().set("id","00001");
//        List list = new ArrayList();
//        User user = new User();
//        user.setAge(21);
//        user.setGender("男");
//        user.setName("聪聪");
//        user.setId("0002");
//        User user1 = new User();
//        user1.setAge(24);
//        list.add(user);
//        list.add(user1);
//        redisTemplate.opsForValue().set("list",list);
//        System.out.println(redisTemplate.opsForValue().get("list"));
//        List<User> list1 = (List<User>)redisTemplate.opsForValue().get("list");
        List<User> list1 = (List<User>)redisOperate.getMessageFromRedis("list");

        for(User user2: list1){
            System.out.println(user2.getAge());
        }

    }

    @Test
    public void test02(){
        Map map = new HashMap();
        map.put("id","0001");
        String string = (String)map.get("name");
        System.out.println(string);
    }

}
