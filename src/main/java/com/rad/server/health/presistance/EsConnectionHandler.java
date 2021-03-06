package com.rad.server.health.presistance;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rad.server.health.components.EsProperties;
import com.rad.server.health.entities.CoronaVirusData;
import org.apache.http.HttpHost;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class EsConnectionHandler {

    //The config parameters for the connection

    @Autowired
    private EsProperties esProperties;


    private  RestHighLevelClient restHighLevelClient;
    private  ObjectMapper objectMapper = new ObjectMapper();

    private  final String INDEX = "corona_data";



    public  synchronized RestHighLevelClient makeConnection() {


        if(restHighLevelClient == null) {
            restHighLevelClient = new RestHighLevelClient(
                    RestClient.builder(
                            new HttpHost(esProperties.getUrl(),
                                    esProperties.getPort(),
                                    esProperties.getScheme())));
        }

        return restHighLevelClient;
    }

    public  synchronized void closeConnection() throws IOException {
        restHighLevelClient.close();
        restHighLevelClient = null;
    }

    public  CoronaVirusData insertData(CoronaVirusData data){
        data.setId(UUID.randomUUID().toString());
        String dataMap = null;
        try {
            dataMap = objectMapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        IndexRequest indexRequest = new IndexRequest(INDEX);
        indexRequest.source(dataMap, XContentType.JSON);
        try {
            IndexResponse response = restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
            System.out.println(response.toString());
        } catch(ElasticsearchException e) {
            e.getDetailedMessage();
        } catch (java.io.IOException ex){
            ex.getLocalizedMessage();
        }

        return data;
    }

    public  List<CoronaVirusData> getByContinent(String continent){
        SearchSourceBuilder builder = new SearchSourceBuilder().size(1000)
                .query(QueryBuilders.boolQuery()
                        .must(QueryBuilders
                                .matchQuery("continent",continent)));

        SearchRequest searchRequest = new SearchRequest(INDEX);
        searchRequest.searchType(SearchType.DFS_QUERY_THEN_FETCH);
        searchRequest.source(builder);
        SearchResponse response = null;
        try {
            response = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {

        }
        catch (ElasticsearchException e){
            e.getDetailedMessage();
            return new ArrayList<>();
        }
        SearchHit[] searchHits = response.getHits().getHits();
        List<CoronaVirusData> results =
                Arrays.stream(searchHits)
                        .map(hit -> {
                            try {
                                return objectMapper.readValue(hit.getSourceAsString(),CoronaVirusData.class);
                            } catch (IOException e) {
                                e.printStackTrace();
                                return null;
                            }
                        })
                        .collect(Collectors.toList());
        return results;
    }
}
