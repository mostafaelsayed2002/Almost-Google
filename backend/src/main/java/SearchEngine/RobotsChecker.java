package SearchEngine;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.json.JSONObject;
import org.json.simple.JSONArray;

import javax.swing.plaf.synth.SynthDesktopIconUI;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.regex.*;

public class RobotsChecker {
    static MongoCollection forbidden;
    HashMap<String, HashSet<String>> PageDisallows;

    public RobotsChecker() {
        initDatabase();
    }

    public static void main(String args[]) throws IOException {
        initDatabase();

        String originalPattern = "/books?*q=*";
        String regex = originalPattern.replaceAll("\\*", ".*").replaceAll("\\?", "\\\\?");
        regex = Pattern.quote(regex);
        System.out.println(regex);
        checkForbidden("https://www.google.com/");
        isAllowed("https://www.google.com/books?q=something");

    }

    private static void initDatabase() {
        MongoClient mongoClient = MongoClients.create("mongodb://127.0.0.1:27017");
        var database = mongoClient.getDatabase("test");
        forbidden = database.getCollection("forbidden");
    }

    public static boolean isAllowed(String urlString) throws MalformedURLException {

        URL url = new URL(urlString);
        var host = url.getHost();
//        var document = forbidden.find(new Document("url", url)).first();
//        System.out.println(document);

        return true;
    }

    public static void checkForbidden(String urlString) throws java.net.MalformedURLException, IOException {
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
                String[] regexKeyCharacters = {".", "+", "?", "^", "$", "[", "]", "|", "(", ")"};
                for (var keyChar : regexKeyCharacters)
                    route = route.replace(keyChar, "\\" + keyChar);
                route = route.replaceAll("\\*", ".*");
                routes.add(route);

            } else {
                flag = false;
            }
        }
        forbidden.insertOne((new Document()).append("url", host).append("routes", routes));
    }
}
