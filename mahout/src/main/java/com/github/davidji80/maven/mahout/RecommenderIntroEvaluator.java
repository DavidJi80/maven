package com.github.davidji80.maven.mahout;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.eval.RecommenderBuilder;
import org.apache.mahout.cf.taste.eval.RecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.eval.AverageAbsoluteDifferenceRecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.apache.mahout.common.RandomUtils;
import java.io.File;
import java.net.URL;

/**
 * 对推荐程序做差值评价
 */
public class RecommenderIntroEvaluator {
    public static void main(String[] args) throws Exception {
        //每次产生的随机数序列
        RandomUtils.useTestSeed();
        /*
        准备数据
        此处采用的是从网上下载的一个大数据集，这里是电影评分数据
        */
        URL url=BaseUserRecommender.class.getClassLoader().getResource("ratings.data");
        //将数据加载到内存中
        DataModel dataModel  = new FileDataModel(new File(url.getFile()));
        //推荐评估，使用均方根
        //RecommenderEvaluator evaluator = new RMSRecommenderEvaluator();
        //这里使用的评估方法--平均差值
        RecommenderEvaluator evaluator = new AverageAbsoluteDifferenceRecommenderEvaluator();
        /*
        我们创建了一个推荐器生成器
        因为评估的时候我们需要将源数据中的一部分作为测试数据，其他作为算法的训练数据
        需要通过新训练的DataModel构建推荐器，所以采用生成器的方式生成推荐器
        */
        RecommenderBuilder builder = new RecommenderBuilder() {

            public Recommender buildRecommender(DataModel dataModel) throws TasteException {
                UserSimilarity similarity = new PearsonCorrelationSimilarity(dataModel);
                UserNeighborhood neighborhood = new NearestNUserNeighborhood(2, similarity, dataModel);
                return new GenericUserBasedRecommender(dataModel, neighborhood, similarity);
            }
        };
        /*
        RecommenderEvaluator负责将数据分为训练集和测试集，用训练集构建一个DataModel和Recommender用来进行测试活动，得到结果之后在于真实数据进行比较。
        参数中0.7代表训练数据为70%，测试数据是30%。最后的1.0代表的是选取数据集的多少数据做整个评估。
        此处第二个null处，使用null就可以满足基本需求，但是如果我们有特殊需求，比如使用特殊的DataModel，在这里可以使用DataModelBuilder的一个实例。
        */
        double score = evaluator.evaluate(builder, null, dataModel, 0.7, 1.0);
        /*
        最后得出的评估值越小，说明推荐结果越好
        最后的评价结果是0.943877551020408，表示的是平均与真实结果的差值是0.9.
        */
        System.out.println(score);
    }
}
