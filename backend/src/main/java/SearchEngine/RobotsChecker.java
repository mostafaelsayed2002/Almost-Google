package SearchEngine;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.json.JSONObject;
import org.json.simple.JSONArray;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;

public class RobotsChecker {
  HashMap<String, HashSet<String>> PageDisallows;
  static MongoCollection forbidden;

  public RobotsChecker() {
    initDatabase();
  }

  public boolean isAllowed(String url) {
    return true;
  }

  public static void main(String args[]) throws java.net.MalformedURLException, IOException {
    initDatabase();
    String urlString = "https://www.google.com";
    URL url = new URL(urlString);
    var protocol = url.getProtocol();
    var host = url.getHost();

    if (forbidden.countDocuments((new Document()).append("url", host)) != 0)
      return;

    URL robotsUrl = new URL(protocol + "://" + host + "/robots.txt");
    DataInputStream dataInputStream = new DataInputStream(robotsUrl.openStream());
    StringBuilder builder = new StringBuilder();
    {
      String line;
      while ((line = dataInputStream.readLine()) != null)
        builder.append(line).append("\n");
    }
    var instructions = builder.toString().split("\n");
    var flag = false;


    JSONArray routes = new JSONArray();
    for (var instruction : instructions) {
      if (instruction.startsWith("User-agent: *"))
        flag = true;
      else if (instruction.startsWith("Allow") && flag)
        continue;
      else if ((instruction.startsWith("Disallow") && flag)) {
        String route = instruction.split(" ")[1];

    String[] regexKeyCharacters = {".", "*", "+", "?", "^", "$", "[", "]", "|", "(", ")", "\\"};
        for (var keyChar : regexKeyCharacters)
          route.replaceAll(keyChar, "\\" + keyChar);

        routes.add(route);

      } else {
        flag = false;
      }
    }
    forbidden.insertOne((new Document()).append("url", host).append("routes", routes));
  }

  private static void initDatabase() {
    MongoClient mongoClient = MongoClients.create("mongodb://127.0.0.1:27017");
    var database = mongoClient.getDatabase("test");
    forbidden = database.getCollection("forbidden");
  }
}
