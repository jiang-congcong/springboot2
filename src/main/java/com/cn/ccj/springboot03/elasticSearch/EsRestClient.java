package com.cn.ccj.springboot03.elasticSearch;

import com.cn.ccj.springboot03.generalException.GeneralException;
import com.cn.ccj.springboot03.jsonUtil.JsonUtil;
import com.cn.ccj.springboot03.redis.RedisOperate;
import com.cn.ccj.springboot03.util.CommonCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.netty.util.internal.StringUtil;
import org.apache.lucene.search.TotalHits;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.eql.EqlSearchResponse;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
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
 */
public class EsRestClient {

    private RestHighLevelClient restHighLevelClient;

    public static Logger logger = LoggerFactory.getLogger(RedisOperate.class);

    private CommonCode commonCode;

    public EsRestClient() {
        final ClientConfiguration clientConfiguration = ClientConfiguration.builder()
                .connectedTo("localhost:9200")
                .build();
        this.restHighLevelClient = RestClients.create(clientConfiguration).rest();
    }

    public Map<String,Object> searchEsMessageAll() throws IOException {
        Map<String,Object> resultMap = new HashMap<String,Object>();

        SearchRequest searchRequest = new SearchRequest("ecommerce");
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
    public void putMessageToES(String index,String type,Object putMessage) throws Exception {
        if(null==index||null==type||index.length()<1||type.length()<1){
            throw new GeneralException(commonCode.DEFAUT_ERROR_CODE,"ES索引和类型必传不为空");
        }
        if(null==putMessage){
            throw new GeneralException(commonCode.DEFAUT_ERROR_CODE,"插入ES数据不能为空");
        }
        IndexRequest indexRequest = new IndexRequest(index, type);
        String source = JsonUtil.objectToString(putMessage);//转成json字符串
        indexRequest.source(source, XContentType.JSON);
        try {
            restHighLevelClient.index(indexRequest,RequestOptions.DEFAULT);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            logger.error(putMessage+"插入ES失败："+e.getMessage());
            throw new GeneralException(commonCode.DEFAUT_ERROR_CODE,"插入ES失败");
        }

    }

    //批量数据插入ES
    public void batchPutMessageToES(String index,String type,List batchPutMessage) throws Exception {
        if(null==index||null==type||index.length()<1||type.length()<1){
            throw new GeneralException(commonCode.DEFAUT_ERROR_CODE,"ES索引和类型必传不为空");
        }
        if(null==batchPutMessage){
            throw new GeneralException(commonCode.DEFAUT_ERROR_CODE,"批量插入ES数据不能为空");
        }
        BulkRequest bulkRequest = new BulkRequest();
        for(Object eachbatchPutMessage:batchPutMessage){
            String source = JsonUtil.objectToString(eachbatchPutMessage);//转json
            IndexRequest indexRequest = new IndexRequest(index,type);
            indexRequest.source(source,XContentType.JSON);
            bulkRequest.add(indexRequest);
        }
        try {
            restHighLevelClient.bulk(bulkRequest,RequestOptions.DEFAULT);
        }catch (Exception e){
            logger.error(batchPutMessage+"批量插入ES失败："+e.getMessage());
            throw new GeneralException(commonCode.DEFAUT_ERROR_CODE,"批量插入ES失败");
        }

    }

    //查询ES数据
    public Map<String,Object> queryMessageFromES(String index,SearchSourceBuilder searchSourceBuilder) throws Exception {
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



}
