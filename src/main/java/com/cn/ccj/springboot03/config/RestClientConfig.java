package com.cn.ccj.springboot03.config;

import com.cn.ccj.springboot03.elasticSearch.EsRestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;

/**
 * @author jiangcongcong
 * @date 2021/3/12 10:44
 * elasticSearch配置文件
 */
@Configuration
public class RestClientConfig{

    @Bean
    public EsRestClient esRestClient(){
        return new EsRestClient();
    }

}
