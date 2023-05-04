package com.elasticdemo.service;

import com.elasticdemo.config.ElasticSearchConfig;
import com.elasticdemo.model.FileContent;
import com.elasticdemo.model.FileModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static org.elasticsearch.index.query.QueryBuilders.matchQuery;

@Service
public class ElasticSearchService {

    @Autowired
    private ElasticSearchConfig elasticSearchConfig;

    @Autowired
    private RestHighLevelClient client;

    public List<FileModel> searchDocuments(String indexName, com.elasticdemo.model.SearchRequest input) throws IOException {


        SearchRequest searchRequest = new SearchRequest(indexName);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        String query = input.getKeyword();
        if(StringUtils.hasText(query)){
            String result = findResumeKeyWords(query);
            if(StringUtils.hasText(result)){
                query=result;
            }
        }
        if(StringUtils.hasText(query) && StringUtils.hasText(input.getBusp())){
            query="("+query + ") AND " + input.getBusp();
        }else if(StringUtils.hasText(input.getBusp())){
            query = input.getBusp();
        }

        System.out.println("Search query:"+ query);


        // build the search query
        searchSourceBuilder.query(QueryBuilders.queryStringQuery(query));
        //searchSourceBuilder.query(matchQuery(searchQuery));

        searchSourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));

       /* // set highlighting options
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field("*");
        highlightBuilder.requireFieldMatch(false);
        highlightBuilder.preTags("<span style=\"background-color: #FFFF00\">");
        highlightBuilder.postTags("</span>");
        searchSourceBuilder.highlighter(highlightBuilder);*/

        // set the source builder and timeout
        searchRequest.source(searchSourceBuilder);

        // execute the search request
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

        // iterate through the search hits
       /* for (SearchHit hit : searchResponse.getHits()) {
            // get the document source as a map
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();

            // print the document fields
            System.out.println("Document ID: " + hit.getId());
            System.out.println("Document Fields: " + sourceAsMap);

            // get the highlight fields and print them
            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
        }*/
        SearchHit[] searchHits = searchResponse.getHits().getHits();
        Gson gson = new Gson();
        List<FileModel> results = Arrays.stream(searchHits).map(hit -> gson.fromJson(hit.getSourceAsString(), FileModel.class)).collect(Collectors.toList());
        return results;
    }

    public void createIndex(FileContent fileContent, String indexName) throws IOException {
        IndexRequest request = new IndexRequest(indexName);
        request.id(fileContent.getId());

        // convert FileContent object to JSON string
        String json = new ObjectMapper().writeValueAsString(fileContent);

        request.source(json, XContentType.JSON);

        ActionListener<IndexResponse> listener = new ActionListener<IndexResponse>() {
            @Override
            public void onResponse(IndexResponse indexResponse) {
                // handle success response
            }

            @Override
            public void onFailure(Exception e) {
                // handle failure response
            }
        };

        client.indexAsync(request, RequestOptions.DEFAULT, listener);
    }

    private String findResumeKeyWords(String value) {

        List<String> resumeKeyWords = elasticSearchConfig.resumeKeyDictionary();
        String matchResult = resumeKeyWords.stream().filter(keyword -> keyword.toUpperCase().contains(value.toUpperCase())).findAny().get();
        return matchResult.toUpperCase();

    }
}


