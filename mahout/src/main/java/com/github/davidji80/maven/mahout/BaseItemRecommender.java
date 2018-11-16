package com.github.davidji80.maven.mahout;

import java.io.File;
import java.net.URL;
import java.util.List;

import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;

/**
 * mahout基于物品的协同过滤
 */
public class BaseItemRecommender {

    public static void main(String[] args) throws Exception {
        URL url=BaseUserRecommender.class.getClassLoader().getResource("movie.data");
        DataModel dataModel = new FileDataModel(new File(url.getFile()));
        /*
        计算相似度，相似度算法有很多种，欧几里得、皮尔逊等等。
        这里使用的是皮尔逊PearsonCorrelationSimilarity
        */
        ItemSimilarity itemSimilarity = new PearsonCorrelationSimilarity(dataModel);
        //构建推荐器，协同过滤推荐有两种，分别是基于用户的和基于物品的，这里使用基于物品的协同过滤推荐
        GenericItemBasedRecommender recommender = new GenericItemBasedRecommender(dataModel, itemSimilarity);
        //给指定用户推荐若干个与指定商品相似的商品
        List<RecommendedItem> recommendedItemList = recommender.recommendedBecause(1, 5, 2);
        //打印推荐的结果
        System.out.println("使用基于物品的协同过滤算法");
        System.out.println("根据用户1当前浏览的商品5，推荐2个相似的商品");
        for (RecommendedItem recommendedItem : recommendedItemList) {
            System.out.println(recommendedItem);
        }
    }
}
