package com.elasticdemo.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;

@Configuration
public class ElasticSearchConfig {

    @Value("${elasticsearch.host}")
    private String host;

    @Value("${elasticsearch.port}")
    private int port;

    @Bean
    public RestHighLevelClient client() {
        RestClientBuilder builder = RestClient.builder(new HttpHost(host, port, "http"));
        return new RestHighLevelClient(builder);
    }

//    @Bean
//    public ElasticsearchOperations elasticsearchTemplate() {
//        return new ElasticsearchRestTemplate(client());
//    }
}

