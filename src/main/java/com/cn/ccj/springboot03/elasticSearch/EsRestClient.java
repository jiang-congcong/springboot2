package com.cn.ccj.springboot03.elasticSearch;

import com.cn.ccj.springboot03.generalException.GeneralException;
import com.cn.ccj.springboot03.jsonUtil.JsonUtil;
import com.cn.ccj.springboot03.redis.RedisOperate;
import com.cn.ccj.springboot03.utils.CommonCode;
import io.netty.util.internal.StringUtil;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.*;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.Scroll;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author jiangcongcong
 * @date 2021/3/12 11:14
 * ES操作工具类
 * 官方文档链接：https://www.elastic.co/guide/en/elasticsearch/client/java-rest/current/java-rest-high.html
 */
public class EsRestClient {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    public static Logger logger = LoggerFactory.getLogger(RedisOperate.class);

    @Autowired
    private CommonCode commonCode;

    public EsRestClient() {
        final ClientConfiguration clientConfiguration = ClientConfiguration.builder()
                .connectedTo("localhost:9200")
                .build();
        this.restHighLevelClient = RestClients.create(clientConfiguration).rest();
    }

    public Map<String,Object> searchEsMessageAll() throws IOException {
        Map<String,Object> resultMap = new HashMap<String,Object>();

        SearchRequest searchRequest = new SearchRequest("test");
        //searchRequest.types("product");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        Long total = searchResponse.getHits().getTotalHits().value;
        SearchHit[] hits = searchResponse.getHits().getHits();
        List<Map> getHits = new ArrayList<Map>();
        if(null!=hits&&hits.length>0) {
            for (SearchHit searchHit : hits) {
                getHits.add(searchHit.getSourceAsMap());
            }
        }
        resultMap.put("total",total);
        resultMap.put("meaasge",getHits);
        return resultMap;
    }

    //单条数据插入ES
    public void putMessageToES(String index,String type,Object putMessage,String id) throws Exception {
        if(StringUtil.isNullOrEmpty(index)||StringUtil.isNullOrEmpty(type)){
            throw new GeneralException(commonCode.DEFAUT_ERROR_CODE,"ES索引和类型必传不为空");
        }
        if(StringUtil.isNullOrEmpty(id)){
            throw new GeneralException(commonCode.DEFAUT_ERROR_CODE,"ES插入ID必传不为空");
        }
        if(null==putMessage){
            throw new GeneralException(commonCode.DEFAUT_ERROR_CODE,"插入ES数据不能为空");
        }
        //IndexRequest indexRequest = new IndexRequest(index, type);
        String source = JsonUtil.objectToString(putMessage);//转成json字符串
        IndexRequest indexRequest = new IndexRequest(index, type, id);//指定id插入
        indexRequest.source(source, XContentType.JSON);
        try {
            restHighLevelClient.index(indexRequest,RequestOptions.DEFAULT);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            logger.error(putMessage+"插入ES失败："+e.getMessage());
            throw new GeneralException(commonCode.DEFAUT_ERROR_CODE,"插入ES失败");
        }

    }

    //批量数据插入ES
    public void batchPutMessageToES(String index,String type,List<Map<String,Object>> batchPutMessage) throws Exception {
        if(StringUtil.isNullOrEmpty(index)||StringUtil.isNullOrEmpty(type)){
            throw new GeneralException(commonCode.DEFAUT_ERROR_CODE,"ES索引和类型必传不为空");
        }
        if(null==batchPutMessage){
            throw new GeneralException(commonCode.DEFAUT_ERROR_CODE,"批量插入ES数据不能为空");
        }
        BulkRequest bulkRequest = new BulkRequest();
        for(Map<String,Object> eachbatchPutMessage:batchPutMessage){
            String id = (String)eachbatchPutMessage.get("id");//指定id插入
            Object object = eachbatchPutMessage.get("putMessage");//插入ES数据
            String source = JsonUtil.objectToString(object);//转json
            IndexRequest indexRequest = new IndexRequest(index,type,id);
            indexRequest.source(source,XContentType.JSON);
            bulkRequest.add(indexRequest);
        }
        try {
            BulkResponse bulk = restHighLevelClient.bulk(bulkRequest,RequestOptions.DEFAULT);
            int code = bulk.status().getStatus();
            System.out.println(code);
        }catch (Exception e){
            logger.error(batchPutMessage+"批量插入ES失败："+e.getMessage());
            throw new GeneralException(commonCode.DEFAUT_ERROR_CODE,"批量插入ES失败");
        }

    }

    //查询ES数据
    public Map<String,Object> queryMessageFromES(String index,SearchSourceBuilder searchSourceBuilder) throws Exception {
        if(StringUtil.isNullOrEmpty(index)){
            throw new GeneralException(commonCode.DEFAUT_ERROR_CODE,"ES索引必传不为空");
        }
        if(null==searchSourceBuilder){
            throw new GeneralException(commonCode.DEFAUT_ERROR_CODE,"查询报文builder必传");
        }
        Map<String,Object> resultMap = new HashMap<String,Object>();
        SearchRequest searchRequest = new SearchRequest(index);
        searchRequest.source(searchSourceBuilder);
        try{
            SearchResponse searchResponce = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            Long total = searchResponce.getHits().getTotalHits().value;
            resultMap.put("total",total);
            SearchHit[] hits = searchResponce.getHits().getHits();
            List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
            for(SearchHit hit:hits){
                Map<String,Object> eachMap = hit.getSourceAsMap();
                resultList.add(eachMap);
            }
            resultMap.put("resultList",resultList);
        }catch (Exception e){
            logger.error(searchSourceBuilder+"查询ES失败："+e.getMessage());
            throw new GeneralException(commonCode.DEFAUT_ERROR_CODE,"查询ES失败");
        }


        return resultMap;
    }

    //根据id删除ES数据
    public void deleteMessageFromES(String index,String type,String id) throws Exception {
        if(StringUtil.isNullOrEmpty(index)||StringUtil.isNullOrEmpty(type)){
            throw new GeneralException(commonCode.DEFAUT_ERROR_CODE,"ES索引和类型必传不为空");
        }
        if(StringUtil.isNullOrEmpty(id)){
            throw new GeneralException(commonCode.DEFAUT_ERROR_CODE,"删除ES数据id必传不为空");
        }
        DeleteRequest deleteRequest = new DeleteRequest(index,type,id);
        try {
            restHighLevelClient.delete(deleteRequest,RequestOptions.DEFAULT);
        }catch (Exception e){
            logger.error("ES删除失败："+id+e.getMessage());
            throw new GeneralException(commonCode.DEFAUT_ERROR_CODE,"ES删除失败");
        }

    }

    //更新ES数据
    public void updateMessageToES(String index, String type,String id,Object updateMessage) throws Exception {
        if(StringUtil.isNullOrEmpty(index)||StringUtil.isNullOrEmpty(type)){
            throw new GeneralException(commonCode.DEFAUT_ERROR_CODE,"ES索引和类型必传不为空");
        }
        if(StringUtil.isNullOrEmpty(id)){
            throw new GeneralException(commonCode.DEFAUT_ERROR_CODE,"删除ES数据id必传不为空");
        }
        if(null==updateMessage){
            throw new GeneralException(commonCode.DEFAUT_ERROR_CODE,"更新ES更新数据不能为空");
        }
        UpdateRequest updateRequest = new UpdateRequest(index, type, id);
        String jsonString = JsonUtil.objectToString(updateMessage);
        updateRequest.doc(jsonString,XContentType.JSON);
        try {
            restHighLevelClient.update(updateRequest,RequestOptions.DEFAULT);
        } catch (Exception e) {
            logger.error(id+"更新ES失败:"+e.getMessage());
            throw  new GeneralException(commonCode.DEFAUT_ERROR_CODE,id+"更新ES失败:"+e.getMessage());
        }
    }

    //ES滚动查询
    public Map<String, Object> scrollSearchFromES(String index, SearchSourceBuilder searchSourceBuilder) throws Exception {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        List resultList = new ArrayList();

        final Scroll scroll = new Scroll(TimeValue.timeValueMinutes(1L));
        SearchRequest searchRequest = new SearchRequest(index);
        searchRequest.scroll(scroll);
        searchRequest.source(searchSourceBuilder);
        try {
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            String scrollId = searchResponse.getScrollId();
            SearchHit[] searchHits = searchResponse.getHits().getHits();
            if (null != searchHits && searchHits.length > 0) {
                for (SearchHit searchHit : searchHits) {
                    resultList.add(searchHit.getSourceAsMap());
                }
            }
            Long total = searchResponse.getHits().getTotalHits().value;
            resultMap.put("total", total);
            while (searchHits != null && searchHits.length > 0) {

                SearchScrollRequest scrollRequest = new SearchScrollRequest(scrollId);
                scrollRequest.scroll(scroll);
                searchResponse = restHighLevelClient.scroll(scrollRequest, RequestOptions.DEFAULT);
                scrollId = searchResponse.getScrollId();
                searchHits = searchResponse.getHits().getHits();
                if (null != searchHits && searchHits.length > 0) {
                    for (SearchHit searchHit : searchHits) {
                        resultList.add(searchHit.getSourceAsMap());
                    }
                }
            }
            resultMap.put("resultList", resultList);

            ClearScrollRequest clearScrollRequest = new ClearScrollRequest();
            clearScrollRequest.addScrollId(scrollId);
            ClearScrollResponse clearScrollResponse = restHighLevelClient.clearScroll(clearScrollRequest, RequestOptions.DEFAULT);
        }catch (Exception e){
            logger.error("滚动查询失败" + e.getMessage());
            throw new GeneralException(commonCode.DEFAUT_ERROR_CODE, "滚动查询失败" + e.getMessage());
        }
        return resultMap;

    }



}
