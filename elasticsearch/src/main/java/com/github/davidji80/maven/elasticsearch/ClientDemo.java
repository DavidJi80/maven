package com.github.davidji80.maven.elasticsearch;

import com.github.davidji80.maven.elasticsearch.util.ESUtil;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import java.io.IOException;

public class ClientDemo {

    public static void main(String[] args) {
        //1.判定索引是否存在
        boolean flag=ESUtil.isExists("index2");
        System.out.println("isExists:"+flag);
        //2.创建索引
        flag=ESUtil.createIndex("index2", 3, 0);
        System.out.println("createIndex:"+flag);
        //3.设置Mapping
        try {
            XContentBuilder builder = XContentFactory.jsonBuilder()
                    .startObject()
                        .startObject("properties")
                            .startObject("name")
                            .field("type", "text")
                            .endObject()
                        .endObject()
                    .endObject();
            ESUtil.setMapping("index2","user",builder);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //4.删索引
        ESUtil.deleteIndex("index1");
    }
}
