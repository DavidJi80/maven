package com.github.davidji80.maven.elasticsearch.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentType;

import java.util.concurrent.ExecutionException;

public class ESDocUtil {
    private static Logger LOGGER = LogManager.getLogger(ESDocUtil.class);

    /**
     * 用json添加文档
     *
     * @param json
     * @return
     */
    public static String addDocByJson(String index, String type, String id, String json) {
        TransportClient client = ESUtil.getClient();
        IndexResponse response = client.prepareIndex(index, type, id)
                .setSource(json, XContentType.JSON)
                .get();
        return response.status().toString();
    }

    /**
     * 用XContentBuilder添加文档
     *
     * @param builder
     * @return
     */
    public static String addDocByXCB(String index, String type, String id, XContentBuilder builder) {
        TransportClient client = ESUtil.getClient();
        IndexResponse response = client.prepareIndex(index, type, id)
                .setSource(builder)
                .get();
        return response.status().toString();
    }

    /**
     * 获取文档
     *
     * @param index
     * @param type
     * @param id
     */
    public static void getDoc(String index, String type, String id) {
        TransportClient client = ESUtil.getClient();
        GetResponse response = client.prepareGet(index, type, id).get();
        LOGGER.info(response.isExists());
        LOGGER.info(response.getIndex());
        LOGGER.info(response.getType());
        LOGGER.info(response.getId());
        LOGGER.info(response.getVersion());
        String source = response.getSourceAsString();
        LOGGER.info(source);
    }

    /**
     * 删除文档
     *
     * @param index
     * @param type
     * @param id
     */
    public static void deleteDoc(String index, String type, String id) {
        TransportClient client = ESUtil.getClient();
        DeleteResponse response = client.prepareDelete(index, type, id).get();
        //删除成功返回OK，否则返回NOT_FOUND
        LOGGER.info(response.status());
        //返回被删除文档的类型
        LOGGER.info(response.getType());
        //返回被删除文档的ID
        LOGGER.info(response.getId());
        //返回被删除文档的版本信息
        LOGGER.info(response.getVersion());
    }

    /**
     * 更新文档
     *
     * @param request
     */
    public static void updateDoc(String index, String type, String id, UpdateRequest request) {
        TransportClient client = ESUtil.getClient();
        UpdateResponse response = null;
        try {
            response = client.update(request).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        //更新成功返回OK，否则返回NOT_FOUND
        LOGGER.info(response.status());
        //返回被更新文档的类型
        LOGGER.info(response.getType());
        //返回被更新文档的ID
        LOGGER.info(response.getId());
        //返回被更新文档的版本信息
        LOGGER.info(response.getVersion());
    }

}
