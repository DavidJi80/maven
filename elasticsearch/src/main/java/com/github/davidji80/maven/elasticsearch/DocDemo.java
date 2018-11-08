package com.github.davidji80.maven.elasticsearch;

import com.github.davidji80.maven.elasticsearch.util.ESDocUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;

import java.io.IOException;

public class DocDemo {
    private static Logger LOGGER = LogManager.getLogger(DocDemo.class);

    private static void addDocByJson() {
        String json = "{" +
                "\"id\":\"1\"," +
                "\"title\":\"Java设计模式之装饰模式\"," +
                "\"content\":\"在不必改变原类文件和使用继承的情况下，动态地扩展一个对象的功能。\"," +
                "\"postdate\":\"2018-02-03 14:38:00\"," +
                "\"url\":\"csdn.net/79239072\"" +
                "}";
        LOGGER.info(ESDocUtil.addDocByJson("index3", "blog", "2", json));
    }

    private static void addDocByXCB() {
        try {
            XContentBuilder doc = XContentFactory.jsonBuilder()
                    .startObject()
                    .field("id", "2")
                    .field("title", "Java设计模式之单例模式")
                    .field("content", "枚举单例模式可以防反射攻击。")
                    .field("postdate", "2018-02-03 19:27:00")
                    .field("url", "csdn.net/79247746")
                    .endObject();
            LOGGER.info(ESDocUtil.addDocByXCB("index3", "blog", "4", doc));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void updateDoc() {
        UpdateRequest request = new UpdateRequest();
        try {
            request.index("index3")
                    .type("blog")
                    .id("2")
                    .doc(
                            XContentFactory.jsonBuilder().startObject()
                                    .field("title", "单例模式解读")
                                    .endObject()
                    );
        } catch (IOException e) {
            e.printStackTrace();
        }
        ESDocUtil.updateDoc("index3", "blog", "3", request);
    }

    /**
     * 文档upsert操作：如果文档存在则执行更新操作，否则执行添加操作
     */
    private static void updateOrAddDoc() {
        IndexRequest request1 = null;
        UpdateRequest request2= null;
        try {
            request1 = new IndexRequest("index3","blog","3")
                    .source(
                            XContentFactory.jsonBuilder().startObject()
                                    .field("id","1")
                                    .field("title","装饰模式")
                                    .field("content","动态地扩展一个对象的功能")
                                    .field("postdate","2018-02-03 14:38:10")
                                    .field("url","csdn.net/79239072")
                                    .endObject()
                    );
            request2 = new UpdateRequest("index3","blog","3")
                    .doc(
                            XContentFactory.jsonBuilder().startObject()
                                    .field("title","装饰模式解读")
                                    .endObject()
                    ).upsert(request1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ESDocUtil.updateDoc("index3", "blog", "3", request2);
    }

    public static void main(String[] args) {
        //addDocByXCB();
        //ESDocUtil.getDoc("index3","blog","4");
        //ESDocUtil.deleteDoc("index3", "blog", "4");
        updateOrAddDoc();
    }
}
