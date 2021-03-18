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
        String type = "_doc";
        Map map = new HashMap();
        map.put("id","0002");
        map.put("name","憨憨");
        map.put("age",24);
        map.put("sex","男");
        map.put("hobby","学习，玩手机，做饭");
        map.put("work","programmer");
        try {
            esRestClient.putMessageToES(index,type,map,(String)map.get("id"));
        }catch (Exception e){
            logger.error("插入ES失败"+e.getMessage());
            throw new GeneralException("-9999","插入ES失败");
        }
        System.out.println(map);
    }

    @Test
    public void testEsRestClient02() throws Exception { //测试es批量插入
        String index = "test";
        String type = "_doc";
        Map map1 = new HashMap();
        map1.put("id","0003");
        map1.put("name","珊珊");
        map1.put("age",22);
        map1.put("sex","女");
        map1.put("hobby","看书，玩手机，学习，逛街");
        Map map2 = new HashMap();
        map2.put("id","0004");
        map2.put("name","婷婷");
        map2.put("age",23);
        map2.put("sex","女");
        map2.put("hobby","玩手机，打游戏，干饭");
        User user = new User();
        user.setGender("男");
        user.setName("小何");
        user.setId("0005");
        user.setAge(24);

        List list = new ArrayList();
        Map mapToList01 = new HashMap();
        mapToList01.put("id",map1.get("id"));
        mapToList01.put("putMessage",map1);
        Map mapToList02 = new HashMap();
        mapToList02.put("id",map2.get("id"));
        mapToList02.put("putMessage",map2);
        Map mapToList03 = new HashMap();
        mapToList03.put("id",user.getId());
        mapToList03.put("putMessage",user);

        list.add(mapToList01);
        list.add(mapToList02);
        list.add(mapToList03);
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
        searchSourceBuilder.size(100);
        searchSourceBuilder.sort("age");
        searchSourceBuilder.fetchSource(new String[] {"name","age"},new String[] {});//第一个参数指查询返回字段列表，第二个指不返回字段
        //key.keyword key+.keyword来匹配，不然字符串匹配不上，IK分词器的问题（elasticsearch 里默认的IK分词器是会将每一个中文都进行了分词的切割，所以你直接想查一整个词，或者一整句话是无返回结果的）
        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("name.keyword","小红");
        TermQueryBuilder termQueryBuilder02 = QueryBuilders.termQuery("sex.keyword","女");
        MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("hobby","打游戏");
        RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("age");
        rangeQueryBuilder.gte(12);
        rangeQueryBuilder.lte(30);
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(termQueryBuilder);
        boolQueryBuilder.must(termQueryBuilder02);
        boolQueryBuilder.must(matchQueryBuilder);
        boolQueryBuilder.must(rangeQueryBuilder);
        searchSourceBuilder.query(boolQueryBuilder);
        Map<String,Object> resultMap = esRestClient.queryMessageFromES("test",searchSourceBuilder);
        System.out.println(resultMap);
    }

    @Test
    public void testEsRestClient04() throws Exception { //测试ES删除功能
        esRestClient.deleteMessageFromES("test","_doc","s9YONXgBgMBi6CeU2ysm");
    }

    @Test
    public void testEsRestClient05() throws Exception {//测试ES更新功能
        Map updateMap = new HashMap();
        updateMap.put("name","小甜甜");
        updateMap.put("age",22);
        updateMap.put("hobby","学习、画画、逛街");
        esRestClient.updateMessageToES("test","_doc","v30LOngBf9b6dnEdQdas",updateMap);
    }

    @Test
    public void testEsRestClient06() throws Exception { //测试ES滚动查询
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.size(1000);
        searchSourceBuilder.sort("age");
        //searchSourceBuilder.fetchSource(new String[] {"name","age"},new String[] {});//第一个参数指查询返回字段列表，第二个指不返回字段
        //key.keyword key+.keyword来匹配，不然字符串匹配不上，IK分词器的问题（elasticsearch 里默认的IK分词器是会将每一个中文都进行了分词的切割，所以你直接想查一整个词，或者一整句话是无返回结果的）
        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("name.keyword","小红");
        TermQueryBuilder termQueryBuilder02 = QueryBuilders.termQuery("sex.keyword","女");
        MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("hobby","打游戏");
        RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("age");
        rangeQueryBuilder.gte(12);
        rangeQueryBuilder.lte(30);
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
//        boolQueryBuilder.must(termQueryBuilder);
//        boolQueryBuilder.must(termQueryBuilder02);
//        boolQueryBuilder.must(matchQueryBuilder);
        boolQueryBuilder.must(rangeQueryBuilder);
        searchSourceBuilder.query(boolQueryBuilder);
        Map<String,Object> resultMap = esRestClient.scrollSearchFromES("test",searchSourceBuilder);
        System.out.println(resultMap);
    }

}
