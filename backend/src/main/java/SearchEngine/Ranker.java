package SearchEngine;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvBuilder;
import org.bson.Document;
import org.jgrapht.Graph;
import org.jgrapht.alg.scoring.PageRank;
import org.jgrapht.graph.DefaultEdge;

import java.util.Map;
import java.util.Set;

public class Ranker {

    static Set<Word> dictionary;
    private static long webCount;
    private static Graph<String, DefaultEdge> graph;
    private static Map<String, Double> vertexScores;
    private static MongoCollection<Document> collection;


    private static void calcRank() {
        var dampingFactor = 0.85;
        PageRank<String, DefaultEdge> pageRank = new PageRank<>(graph, dampingFactor, 100, 1 / graph.vertexSet().size());
        vertexScores = pageRank.getScores();
    }

    private static void calcIDF(Word w) {            // this calculates IDF and set the rank for each website
        w.IDF = Math.log(webCount / w.websites.size());
        for (Website s : w.websites) {
            s.TF_IDF = w.IDF * s.TF;
            s.pageRank = vertexScores.get(s.url);
            s.lastRank = s.TF_IDF * s.pageRank;
        }

    }


    private static void sortWeb() {
        for (Word w : dictionary) {
            calcIDF(w);
            w.sortWebsites();
        }
    }

    private static void initDataBase() {
        Dotenv dotenv = new DotenvBuilder().load();
        MongoClient mongoClient = MongoClients.create(dotenv.get("ConctionString"));
        MongoDatabase database = mongoClient.getDatabase("AlmostGoogle");
        collection = database.getCollection("visited");
    }

    public static void main(String[] args) {
        initDataBase();
        graph = CrawlerStore.graph;
        webCount = graph.vertexSet().size();
        dictionary = Indexer.dictionary;
        calcRank();
        sortWeb();
    }


}
