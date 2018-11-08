package com.github.davidji80.maven.elasticsearch;

import com.github.davidji80.maven.elasticsearch.util.QueryUtil;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

public class QueryDemo {
    public static void main(String[] args) {
        QueryUtil util=new QueryUtil("index3",10);
        //match构造查询对象
        QueryBuilder qb=QueryBuilders.matchQuery("title","解读").operator(Operator.AND);
        util.query(qb).print();

        //match查询多个字段
        qb=QueryBuilders.multiMatchQuery("解读","title","content");
        util.query(qb).print();

        //term查询
        qb=QueryBuilders.termQuery("title","java");
        util.query(qb).print();

        //range查询
        qb=QueryBuilders.rangeQuery("postdate").from("2018-01-01").to("2018-12-31").format("yyyy-MM-dd");
        util.query(qb).print();

        //prefix查询
        qb=QueryBuilders.prefixQuery("title","单");
        util.query(qb).print();

        //wildcard查询
        qb=QueryBuilders.wildcardQuery("title","*模式*");
        util.query(qb).print();

        //regexp查询
        qb=QueryBuilders.regexpQuery("title","gc.*");
        util.query(qb).print();

        //fuzzy查询
        qb=QueryBuilders.fuzzyQuery("title","vmwere");
        util.query(qb).print();

        //type查询
        qb=QueryBuilders.typeQuery("blog");
        util.query(qb).print();

        //ids查询
        qb=QueryBuilders.idsQuery().addIds("1","3");
        util.query(qb).print();
    }
}
