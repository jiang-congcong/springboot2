package com.cn.ccj.springboot03.elasticSearch;

import org.apache.lucene.search.TotalHits;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
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



}
