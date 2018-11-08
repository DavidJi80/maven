package com.github.davidji80.maven.elasticsearch.util;

import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.client.IndicesAdminClient;
import org.elasticsearch.client.Requests;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class ESUtil {
    //集群名,默认值elasticsearch
    private static final String CLUSTER_NAME = "docker-cluster";
    //ES集群中某个节点
    private static final String HOSTNAME = "192.168.1.8";
    //连接端口号
    private static final int PORT = 9300;
    //构建Settings对象
    private static Settings settings = Settings.builder().put("cluster.name", CLUSTER_NAME).build();
    /**
     *  TransportClient对象，用于连接ES集群
     *  volatile变量确保将变量的更新操作通知到其他线程
    */
    private static volatile TransportClient client;

    /**
     * 同步synchronized(*.class)代码块的作用和synchronized static方法作用一样,
     * 对当前对应的*.class进行持锁,static方法和.class一样都是锁的该类本身,同一个监听器
     * @return
     * @throws UnknownHostException
     */
    public static TransportClient getClient(){
        if(client==null){
            synchronized (TransportClient.class){
                try {
                    client=new PreBuiltTransportClient(settings)
                            .addTransportAddress(new TransportAddress(InetAddress.getByName(HOSTNAME), PORT));
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
            }
        }
        return client;
    }

    /**
     * 获取索引管理的IndicesAdminClient
     */
    private static IndicesAdminClient getAdminClient() {
        return getClient().admin().indices();
    }

    /**
     * 判定索引是否存在
     * @param indexName
     * @return
     */
    public static boolean isExists(String indexName){
        IndicesExistsResponse response=getAdminClient().prepareExists(indexName).get();
        return response.isExists()?true:false;
    }
    /**
     * 创建索引
     * @param indexName
     * @return
     */
    public static boolean createIndex(String indexName){
        CreateIndexResponse createIndexResponse = getAdminClient()
                .prepareCreate(indexName.toLowerCase())
                .get();
        return createIndexResponse.isAcknowledged()?true:false;
    }

    /**
     * 创建索引
     * @param indexName 索引名
     * @param shards   分片数
     * @param replicas  副本数
     * @return
     */
    public static boolean createIndex(String indexName, int shards, int replicas) {
        Settings settings = Settings.builder()
                .put("index.number_of_shards", shards)
                .put("index.number_of_replicas", replicas)
                .build();
        CreateIndexResponse createIndexResponse = getAdminClient()
                .prepareCreate(indexName.toLowerCase())
                .setSettings(settings)
                .execute().actionGet();
        return createIndexResponse.isAcknowledged()?true:false;
    }

    /**
     * 位索引indexName设置mapping
     * @param indexName
     * @param typeName
     * @param builder
     */
    public static void setMapping(String indexName, String typeName, XContentBuilder builder) {
        PutMappingRequest mapping = Requests.putMappingRequest(indexName).type(typeName).source(builder);
        getAdminClient().putMapping(mapping).actionGet();
    }

    /**
     * 删除索引
     * @param indexName
     * @return
     */
    public static boolean deleteIndex(String indexName) {
        DeleteIndexResponse deleteResponse = getAdminClient()
                .prepareDelete(indexName.toLowerCase())
                .execute()
                .actionGet();
        return deleteResponse.isAcknowledged()?true:false;
    }


}
