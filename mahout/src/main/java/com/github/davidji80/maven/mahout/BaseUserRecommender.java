package com.github.davidji80.maven.mahout;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.common.LongPrimitiveIterator;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.EuclideanDistanceSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;

/**
 * 基于用户的协同过滤算法
 */
public class BaseUserRecommender {

    final static int NEIGHBORHOOD_NUM = 2;   //用户邻居数量
    final static int RECOMMENDER_NUM = 3;    //推荐结果个数

    public static void main(String[] args) throws IOException, TasteException {
        /*
        准备数据 这里是电影评分数据
        数据集，其中第一列表示用户id；第二列表示商品id；第三列表示评分，评分是5分制
        */
        URL url=BaseUserRecommender.class.getClassLoader().getResource("ratings.data");
        /*
        将数据加载到内存中
        基于文件的model，通过文件形式来读入,且此类型所需要读入的数据的格式要求很低，只需要满足每一行是用户id，物品id，用户偏好，且之间用tab或者是逗号隔开即可
        */
        DataModel dataModel  = new FileDataModel(new File(url.getFile()));
        /*
        计算相似度，相似度算法有很多种，欧几里得、皮尔逊等等。
        基于用户的协同过滤算法，基于物品的协同过滤算法，这里使用了EuclideanDistanceSimilarity
        计算欧式距离，欧式距离来定义相似性，用s=1/(1+d)来表示，范围在[0,1]之间，值越大，表明d越小，距离越近，则表示相似性越大
        */
        UserSimilarity similarity  = new EuclideanDistanceSimilarity(dataModel );
        /*
        计算最近邻域，邻居有两种算法，基于固定数量的邻居和基于相似度的邻居，这里使用基于固定数量的邻居。
        NEIGHBORHOOD_NUM指定用户邻居数量
        */
        NearestNUserNeighborhood  neighbor = new NearestNUserNeighborhood(NEIGHBORHOOD_NUM, similarity, dataModel );
        /*
        构建推荐器，协同过滤推荐有两种，分别是基于用户的和基于物品的，这里使用基于用户的协同过滤推荐
        构建基于用户的推荐系统
        */
        Recommender r = new GenericUserBasedRecommender(dataModel , neighbor, similarity);


        //得到所有用户的id集合
        LongPrimitiveIterator iter = dataModel .getUserIDs();
        while(iter.hasNext()) {
            long uid = iter.nextLong();
            //获取推荐结果，获取指定用户指定数量的推荐结果
            List<RecommendedItem> list = r.recommend(uid,RECOMMENDER_NUM);
            System.out.printf("用户:%s",uid);
            //遍历推荐结果
            System.out.print("--》 推荐电影：");
            for(RecommendedItem ritem : list) {
                //获取推荐结果和推荐度
                System.out.print(ritem.getItemID()+"["+ritem.getValue()+"] ");
            }
            System.out.println();
        }
    }
}
