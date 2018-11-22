package com.github.davidji80.maven.mahout.recommender;

import com.mysql.cj.jdbc.MysqlDataSource;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.model.jdbc.MySQLJDBCDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.model.JDBCDataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

import java.util.List;

public class JdbcDataModelDemo {
    public static  void main(String[] args) throws TasteException {
        long t1=System.currentTimeMillis();
        MysqlDataSource dataSource=new MysqlDataSource();
        dataSource.setServerName("192.168.1.7");
        dataSource.setUser("xhsit");
        dataSource.setPassword("sit1818");
        dataSource.setDatabaseName("mahout");
        JDBCDataModel dataModel=new MySQLJDBCDataModel(dataSource,"rating","userid","movieid","rating", "ratetime");
        //	JDBCDataModel dataModel=new MySQLJDBCDataModel(dataSource,"mytable01","uid","iid","val",null);
        DataModel model=dataModel;
        UserSimilarity similarity=new PearsonCorrelationSimilarity(model);
        UserNeighborhood neighborhood=new NearestNUserNeighborhood(2,similarity,model);
        Recommender recommender=new GenericUserBasedRecommender(model,neighborhood,similarity);
        // the Recommender.recommend() method's arguments: first one is the user id;
        //     the second one is the number recommended
        List<RecommendedItem> recommendations=recommender.recommend(1, 2);
        for(RecommendedItem recommendation:recommendations){
            System.out.println(recommendation);
        }
        System.out.println("done and time spend:"+(System.currentTimeMillis()-t1));
    }
}
