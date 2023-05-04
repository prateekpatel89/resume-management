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

import java.util.ArrayList;
import java.util.List;

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


    public List<String> resumeKeyDictionary(){
        List<String> resume = new ArrayList<>();
        resume.add("Java OR springboot OR J2EE OR JSP OR Servlet OR spring or API or OOPS");
        resume.add("OOPS OR C++");
        resume.add("C OR C++");
        resume.add("aws OR lamda or ecs or aws architect or ec2 or s3 or dynamo or ebs");
        resume.add("database or mongo or oracle or sql or mysql or postgres");
        resume.add("\"big data\" or hadoop or spark");
        resume.add("UI or angular or javascript or react or html or \"User Interface\"");
        resume.add("android or IOS");
        resume.add(".Net or Dotnet or C#");

        resume.add("ETL OR Abnitio");
        return resume;
    }

//    @Bean
//    public ElasticsearchOperations elasticsearchTemplate() {
//        return new ElasticsearchRestTemplate(client());
//    }
}

