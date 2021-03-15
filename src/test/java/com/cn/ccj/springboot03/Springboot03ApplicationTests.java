package com.cn.ccj.springboot03;

import com.cn.ccj.springboot03.elasticSearch.EsRestClient;
import com.cn.ccj.springboot03.entity.User;
import com.cn.ccj.springboot03.generalException.GeneralException;
import com.cn.ccj.springboot03.jsonUtil.JsonUtil;
import com.cn.ccj.springboot03.redis.RedisOperate;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@SpringBootTest
class Springboot03ApplicationTests {

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    RedisOperate redisOperate;

    @Autowired
    EsRestClient esRestClient;

    public static Logger logger = LoggerFactory.getLogger(RedisOperate.class);

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
    public void testEsRestClient() throws IOException {
        Map map = esRestClient.searchEsMessageAll();
        System.out.println(map);
    }

    @Test
    public void testEsRestClient01() throws Exception { //测试es单条插入
        String index = "test";
        String type = "";
        Map map = new HashMap();
        map.put("name","小花");
        map.put("age",18);
        map.put("sex","女");
        try {
            esRestClient.putMessageToES(index,type,map);
        }catch (Exception e){
            logger.error("插入ES失败");
            throw new GeneralException("-9999","插入ES失败");
        }
        System.out.println(map);
    }

    @Test
    public void testEsRestClient02() throws Exception { //测试es批量插入
        String index = "test";
        String type = "_doc";
        Map map1 = new HashMap();
        map1.put("name","小红");
        map1.put("age",18);
        map1.put("sex","女");
        map1.put("hobby","看书，下棋，玩手机，打游戏");
        Map map2 = new HashMap();
        map2.put("name","小张");
        map2.put("age",20);
        map2.put("sex","男");
        map2.put("hobby","玩手机，打游戏，刷视频");
        List list = new ArrayList();
        list.add(map1);
        list.add(map2);
        try {
            esRestClient.batchPutMessageToES(index,type,list);
        }catch (Exception e){
            logger.error("插入ES失败");
            throw new GeneralException("-9999","插入ES失败");
        }

    }

    @Test
    public void testEsRestClient03() throws Exception { //测试es查询功能
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.from(0);
        searchSourceBuilder.size(10);
        searchSourceBuilder.sort("age");
        //key.keyword key+.keyword来匹配，不然字符串匹配不上，IK分词器的问题（elasticsearch 里默认的IK分词器是会将每一个中文都进行了分词的切割，所以你直接想查一整个词，或者一整句话是无返回结果的）
        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("name.keyword","小红");
        MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("hobby","打游戏");
        RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("age");
        rangeQueryBuilder.gte(12);
        rangeQueryBuilder.lte(30);
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(termQueryBuilder);
        boolQueryBuilder.must(matchQueryBuilder);
        boolQueryBuilder.must(rangeQueryBuilder);
        searchSourceBuilder.query(boolQueryBuilder);
        Map<String,Object> resultMap = esRestClient.queryMessageFromES("test",searchSourceBuilder);
        System.out.println(resultMap);
    }

}
