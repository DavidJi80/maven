package com.github.davidji80.maven.elasticsearch.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;

import java.util.Map;

public class QueryUtil {
    private static Logger LOGGER = LogManager.getLogger(QueryUtil.class);

    private String index="index3";
    private int size=10;
    private SearchHits hits;
    private TransportClient client = ESUtil.getClient();

    public QueryUtil(String index,int size){
        this.index=index;
        this.size=size;
    }

    public QueryUtil query(QueryBuilder query){
        //搜索结果存入SearchResponse
        SearchResponse response=client.prepareSearch(index)
                .setQuery(query) //设置查询器
                .setSize(size)   //一次查询文档数
                .get();
        this.hits=response.getHits();
        return this;
    }

    public void print(){
        if(hits==null){
            return ;
        }
        for(SearchHit hit:hits){
            LOGGER.info("-----------------------");
            LOGGER.info("source:"+hit.getSourceAsString());
            LOGGER.info("index:"+hit.getIndex());
            LOGGER.info("type:"+hit.getType());
            LOGGER.info("id:"+hit.getId());
            //遍历文档的每个字段
            Map<String,Object> map=hit.getSourceAsMap();
            for(String key:map.keySet()){
                LOGGER.info(key+"="+map.get(key));
            }
        }
    }
}
