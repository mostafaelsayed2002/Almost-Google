package SearchEngine;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvBuilder;
import kotlin.Pair;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.jgrapht.Graph;
import org.jgrapht.alg.scoring.PageRank;
import org.jgrapht.graph.DefaultEdge;
import com.mongodb.client.model.Filters;

import java.util.*;

import org.bson.types.Binary;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.core.SpringVersion;

import java.io.*;

public class Ranker {

    static HashMap<String, Word> dictionary;
    private static long webCount;
    private static Graph<String, DefaultEdge> graph;
    private static Map<String, Double> urlScores = new HashMap<>();
    private static Map<String, Double> vertexScores2;
    private static MongoCollection<Document> graphCollection;
    private static MongoCollection<Document> wordCollection;

    private static void calcPageRank(double dampingFactor, int iterations) {
        Double tolerance = 1.0E-4;
        for (String url : graph.vertexSet()) {
            urlScores.put(url, 1.0 / webCount);
        }

        double prevScore = 0.0;
        double curScore = 0.0;
        boolean hasConverged = false;
        for (int i = 0; i < iterations; i++) {
            for (String url : graph.vertexSet()) {
                Double tempScore = 0.0;
                if (graph.incomingEdgesOf(url).isEmpty()) {
                    tempScore = 1.0 / webCount;
                } else {
                    for (DefaultEdge E : graph.incomingEdgesOf(url)) {
                        String tempUrl = graph.getEdgeSource(E);
                        tempScore += urlScores.get(tempUrl) + graph.outDegreeOf(tempUrl);
                    }
                }
                var finalScoure = (1.0 - dampingFactor) / webCount + dampingFactor * tempScore;
                urlScores.put(url, finalScoure);
            }
            double scoreSum = 0.0;
            for (double score : urlScores.values()) {
                scoreSum += score;
            }
            for (String url : urlScores.keySet()) {
                urlScores.put(url, urlScores.get(url) / scoreSum);
            }
            curScore = urlScores.values().stream().mapToDouble(Double::doubleValue).sum();
            if (Math.abs(curScore - prevScore) < tolerance) {
                hasConverged = true;
            }
            prevScore = curScore;
        }


    }

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
        webCount = graph.vertexSet().size();
    }

    private static void getset() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("set.ser"))) {
            dictionary = (HashMap<String, Word>) ois.readObject();
            System.out.println(dictionary.size());
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void calcRank() {
        var dampingFactor = 0.85;
        PageRank<String, DefaultEdge> pageRank = new PageRank<>(graph, dampingFactor, 100);
        urlScores = pageRank.getScores();
//        System.out.println("------------------------------------------------------");
//        System.out.println(vertexScores);
    }

    private static void calcIDF(Word w) {
        // this calculates IDF and set the rank for each website
        System.out.println(w.word);
        System.out.println(w.websites.size());
        if (w.websites.size() == 0)
            return;
        w.IDF = Math.log(webCount / w.websites.size());
        for (Website s : w.websites) {
            s.TF_IDF = w.IDF * s.TF;
            s.pageRank = urlScores.get(s.url);
            s.lastRank = s.TF_IDF * s.pageRank;
        }

    }

    private static void sortWeb() {
        for (Word w : dictionary.values()) {
            calcIDF(w);
            w.sortWebsites();
        }
    }

    private static void initDataBase() {
        MongoClient _mongoClient = MongoClients.create("mongodb://127.0.0.1:27017");
        MongoDatabase _database = _mongoClient.getDatabase("test");
        graphCollection = _database.getCollection("graph");

//        --------------------------------------------------
//        Dotenv dotenv = new DotenvBuilder().load();
//        MongoClient mongoClient = MongoClients.create(dotenv.get("ConctionString"));
        MongoDatabase databasease = _mongoClient.getDatabase("AlmostGoogle");
        wordCollection = _database.getCollection("searchIndexer3");
    }

    private static void insertIntoDatabase() {
        for (Word item : dictionary.values()) {
            JSONObject word = new JSONObject();
            word.put("word", item.word);
            JSONArray websites = new JSONArray();
            for (Website web : item.websites) {
                JSONObject website = new JSONObject();
                website.put("url", web.url);
                website.put("TF", web.TF);
                website.put("title", web.title);
                Random random = new Random();
                website.put("lastRank", web.lastRank);
                JSONArray places = new JSONArray();
                for (Pair<String, String> p : web.places) {
                    JSONObject place = new JSONObject();
                    place.put("place", p.getFirst());
                    place.put("content", p.getSecond());
                    places.add(place);
                }
                website.put("places", places);
                websites.add(website);
            }
            word.put("websites", websites);
            Document document = Document.parse(word.toString());
            wordCollection.insertOne(document);
        }
    }

    public static void main(String[] args) {
        initDataBase();
        getGraph();
        getset();
        calcPageRank(0.85, 100);
//        calcRank();
//        System.out.println("Done");
        sortWeb();
        insertIntoDatabase();
    }
}