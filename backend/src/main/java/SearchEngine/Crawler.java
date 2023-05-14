package SearchEngine;

/*
    Crawler is the consumer
    so there must be a producer
 */

import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.UpdateResult;
import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvBuilder;
import org.apache.commons.lang.ObjectUtils;
import org.bson.conversions.Bson;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.*;
import java.nio.file.Paths;
import java.util.*;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import static com.mongodb.client.MongoClients.create;
//

class CrawlerStore  {
  public static int MAX_SIZE = 200;
  public static Graph<String, DefaultEdge> graph = new DefaultDirectedGraph<>(DefaultEdge.class);
  private final Queue<String> queue = new LinkedList<>();
  private final Hashtable<Integer, Boolean> rec = new Hashtable<>();
  private final MongoCollection<org.bson.Document> queueCollection;
  private final MongoCollection<org.bson.Document> visitedCollection;
  private final MongoCollection<org.bson.Document> graphCollection;

  private boolean readSeed;

  CrawlerStore(MongoCollection<org.bson.Document> qc, MongoCollection<org.bson.Document> vc,
      MongoCollection<org.bson.Document> gc) {
    queueCollection = qc;
    visitedCollection = vc;
    graphCollection = gc;
    readSeed = false;
  }

  private static ArrayList<String> readSeeds() {
    ArrayList<String> seeds = new ArrayList<>();
    try {
      File seedFile = new File(
          "/home/walid/Downloads/vsCode/Almost-Google/backend/src/main/java/SearchEngine/seed.txt");
      Scanner seedFileScanner = new Scanner(seedFile);
      while (seedFileScanner.hasNextLine()) {
        String link = seedFileScanner.nextLine();
        seeds.add(link);
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    return seeds;
  }

  public void fillQueue() {
    var seeds = readSeeds();
    if (!readSeed && visitedCollection.countDocuments(new org.bson.Document().append("url", seeds.get(0))) == 0)
      synchronized (queue) {
        queue.addAll(seeds);
        try
        {
          initialGraph(seeds);
        }
        catch (IOException e )
        {
          e.printStackTrace();
        }
        System.out.println("======================================= Initial Fill ===========================");
      }
    else {
      readSeed = true;
      FindIterable<org.bson.Document> documentCursor = queueCollection.find()
          .sort(new org.bson.Document().append("_id", 1)).limit(MAX_SIZE);
      var urlQueue = new ArrayList<String>();
      for (org.bson.Document document : documentCursor) {
        urlQueue.add((String) document.get("url"));
        queueCollection.deleteOne(new org.bson.Document().append("url", document.get("url")));
      }
      synchronized (queue) {
        queue.addAll(urlQueue);
        queue.notifyAll();
      }
    }
  }

  public void addToQueueCollection(ArrayList<org.bson.Document> urls) {
    synchronized (queue) {
      queueCollection.insertMany(urls);
    }
  }

  public String dequeueUrl() {
    synchronized (queue) {
      if (queue.isEmpty()) {
        try {
          queue.notify();
          queue.wait();
        } catch (InterruptedException e) {
          throw new RuntimeException(e);
        }
      }
      String result = queue.poll();
      while (rec.containsKey(result.hashCode())
          && visitedCollection.countDocuments(new org.bson.Document().append("url", result.hashCode())) != 0)
        result = queue.poll();
      return result;
    }
  }

  public void addToVisited(org.bson.Document url) {
    synchronized (queue) {
      visitedCollection.insertOne(url);
      rec.put(url.get("url").hashCode(), true);
    }
  }

  public boolean isQueueEmpty() {
    return queue.isEmpty();
  }

  public boolean doesDocumentExist(String url, String compactString) {
    return rec.containsKey(url.hashCode())
        || visitedCollection.countDocuments(new org.bson.Document().append("url", url.hashCode())) > 0
        || visitedCollection.countDocuments(new org.bson.Document().append("compactString", compactString)) > 0;
  }

  public int queueSize() {
    return queue.size();
  }

  public void addToGraph(String url, ArrayList<org.bson.Document> urls) throws IOException {
    synchronized (this) {
      System.out.println("------------------------------------------------------------------------");
      System.out.println("--------------------" + url);
      System.out.println("------------------------------------------------------------------------");
      for (DefaultEdge e : graph.edgeSet()) {
        // check if the edge contains the specific vertex
        if (graph.getEdgeSource(e).equals(url) || graph.getEdgeTarget(e).equals(url)) {
          System.out.println(e.toString()); // print the edge
        }
      }
      System.out.println("------------------------------------------------------------------------");
      for (org.bson.Document u : urls) {
        if (rec.containsKey(u.get("url").hashCode())
            && visitedCollection.countDocuments(new org.bson.Document().append("url", u.get("url").hashCode())) != 0) {
          continue;
        } else {
          graph.addVertex(u.get("url").toString());
          graph.addEdge(url, u.get("url").toString());
        }
      }
      ByteArrayOutputStream bos = new ByteArrayOutputStream();
      ObjectOutputStream oos = new ObjectOutputStream(bos);
      oos.writeObject(graph);
      byte[] graphBytes = bos.toByteArray();
      Bson filter = Filters.eq("name", "Graph");
      Bson update = Updates.set("graph", graphBytes);
      graphCollection.updateOne(filter, update);
    }
  }

  public void initialGraph(ArrayList<String> urls) throws IOException {
    graph.addVertex("Head");
    for (String u : urls) {
      graph.addVertex(u);
      graph.addEdge("Head", u);
    }
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    ObjectOutputStream oos = new ObjectOutputStream(bos);
    oos.writeObject(graph);
    byte[] graphBytes = bos.toByteArray();
    graphCollection.insertOne(new org.bson.Document("name", "Graph").append("graph", graphBytes));
    System.out.println("------------------------------------------------------------------------");
    System.out.println(graph.toString());

  }

}

class Consumer implements Runnable {
  private static String cwd;
  private static CrawlerStore store;

  Consumer(CrawlerStore cs) {
    cwd = Paths.get("").toAbsolutePath().toString();
    store = cs;
  }

  private static void storeHTMLOnDisk(String pageLink, String JsonDocument) {
    try {
      String name = pageLink;
      name = name.replace("*", "`{}");
      name = name.replace("://", "}");
      name = name.replace("/", "{");
      name = name.replace("?", "`");
      System.out.println(cwd + "/Documents");
      File file = new File(cwd + "/Documents/" + name + ".json");
      System.out.println(file.getName());
      System.out.println(cwd + "/Documents/" + name + ".json");
      file.createNewFile();
      FileWriter writer = new FileWriter(file);
      writer.write(JsonDocument);
      writer.close();
      System.out.println("Link " + pageLink + " done");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static Document requestPage(String page) {
    try {
      Connection connection = Jsoup.connect(page);
      if (connection == null)
        return null;
      Document doc = connection.get();
      if (connection.response().statusMessage() != null && connection.response().statusMessage().equals("OK"))
        return doc;
      else
        return null;
    } catch (IOException e) {
      return null;
    }
  }

  private static String createCompactString(Document doc) {
    String[] htmlParagraphTags = { "p", "h1", "h2", "h3", "a", "div" };
    StringBuilder CompactString = new StringBuilder();
    for (var tag : htmlParagraphTags) {
      for (var element : doc.select(tag)) {
        String text = element.ownText();
        int i = text.length() / 2;
        while (text.length() > 0 && i < text.length() && text.charAt(i) == ' ') {
          i += 1;
        }
        if (i < text.length())
          CompactString.append(text.charAt(i));
      }
    }
    return CompactString.substring(CompactString.length() / 2);
  }

  @Override
  public void run() {
    synchronized (store) {
      try {
        store.wait();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    crawl();
  }

  private void crawl() {
    while (true) {
      if (store.isQueueEmpty()) {
        try {
          synchronized (store) {
            store.notifyAll();
            store.wait();
          }
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }

      String pageLink = store.dequeueUrl();
      Document doc;
      if ((doc = requestPage(pageLink)) == null)
        continue;
      String compactString = createCompactString(doc);
      if (!store.doesDocumentExist(pageLink, compactString)) {

        store.addToVisited(new org.bson.Document().append("compactString", compactString).append("url", pageLink));

        var linksCrawled = new ArrayList<org.bson.Document>();

        for (var link : doc.select("a[href]")) {
          String newLinkStr = link.absUrl("href");
          linksCrawled.add(new org.bson.Document().append("url", newLinkStr));
        }
        if (!linksCrawled.isEmpty()) {
          store.addToQueueCollection(linksCrawled);
          try {
            store.addToGraph(pageLink, linksCrawled);
          } catch (IOException e) {
            throw new RuntimeException(e);
          }
        }
        String JsonDocument = (new org.bson.Document().append("url", pageLink).append("document", doc.toString()))
            .toJson();
        storeHTMLOnDisk(pageLink, JsonDocument);
      }
    }
  }

}

class Producer implements Runnable {
  CrawlerStore store;

  Producer(CrawlerStore cs) {
    store = cs;
  }

  @Override
  public void run() {
    store.fillQueue();
    synchronized (store) {
      store.notifyAll();
    }
    produce();
  }

  private void produce() {
    while (true) {
      synchronized (store) {
        if (store.queueSize() >= store.MAX_SIZE) {
          try {
            store.notifyAll();
            store.wait();
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }
        store.fillQueue();
      }
    }
  }
}

public class Crawler {
  private static MongoDatabase database;
  private static MongoCollection<org.bson.Document> visitedCollection;
  private static MongoCollection<org.bson.Document> queueCollection;
  private static MongoCollection<org.bson.Document> graphCollection;
  private static CrawlerStore store;

  public static void main(String[] args) {
    initDatabase();
    store = new CrawlerStore(queueCollection, visitedCollection, graphCollection);
    ArrayList<Thread> threads = new ArrayList<>();
    for (Integer i = 0; i < 20; i++) {
      threads.add((new Thread(new Consumer(store), "c" + i.toString())));
      threads.get(i).start();
    }
    (new Thread(new Producer(store),"p1")).start();

  // for (Integer i = 0; i < 20; i++) {
  // try {
  // threads.get(i).join();
  // } catch (InterruptedException e) {
  // throw new RuntimeException(e);
  // }
  // }
  // TODO: this should be removed and replaced with joins on the thread
  while(true) {
    }
  }

  private static void initDatabase() {
    MongoClient mongoClient = MongoClients.create("mongodb://127.0.0.1:27017");
    database = mongoClient.getDatabase("test");
    visitedCollection = database.getCollection("visited");
    queueCollection = database.getCollection("queue");
    graphCollection = database.getCollection("graph");
  }
}