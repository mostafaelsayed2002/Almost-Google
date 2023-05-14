package SearchEngine;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvBuilder;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.jgrapht.Graph;
import org.jgrapht.alg.scoring.PageRank;
import org.jgrapht.graph.DefaultEdge;
import com.mongodb.client.model.Filters;

import java.util.Arrays;

import org.bson.types.Binary;
import org.springframework.core.SpringVersion;

import java.io.*;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Ranker {

    static Set<Word> dictionary;
    private static long webCount;
    private static Graph<String, DefaultEdge> graph;
    private static Map<String, Double> vertexScores;
    private static MongoCollection<Document> graphCollection;


    private static void getGraph() {
//        Bson filter = Filters.eq("name", "Graph");
        Document doc = graphCollection.find().first();
        byte[] graphBytes = doc.get("graph", Binary.class).getData();
        ByteArrayInputStream bis = new ByteArrayInputStream(graphBytes);
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(bis);
            graph = (Graph<String, DefaultEdge>) ois.readObject();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static void getset() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("set.ser"))) {
            dictionary = (Set<Word>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

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
        MongoDatabase database = mongoClient.getDatabase("test");
        graphCollection = database.getCollection("graph");
    }


    public static void main(String[] args) {
        initDataBase();
        getGraph();
        webCount = graph.vertexSet().size();
        getset();
        calcRank();
        sortWeb();
    }


}
