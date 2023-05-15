package SearchEngine;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.json.JSONObject;
import org.json.JSONArray;

import javax.swing.plaf.synth.SynthDesktopIconUI;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
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
    }

    private static void initDatabase() {
        MongoClient mongoClient = MongoClients.create("mongodb://127.0.0.1:27017");
        var database = mongoClient.getDatabase("test");
        forbidden = database.getCollection("forbidden");
    }

    public static boolean isAllowed(String urlString) throws MalformedURLException {

        URL url = new URL(urlString);
        var host = url.getHost();
        Document document = (Document) forbidden.find(new Document().append("url", host)).first();
        JSONArray routes = new JSONArray((ArrayList<String>) document.get("routes"));
        String checkPart = url.toString().substring(host.length() + urlString.indexOf(host));
//        System.out.println(checkPart);
        for (var route : routes) {
            if (checkPart.matches((String) route))
                return true;
        }
        return false;
    }

    public static void checkForbidden(String urlString) throws MalformedURLException, IOException {
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
                routes.put(route);

            } else {
                flag = false;
            }
        }
        forbidden.insertOne((new Document()).append("url", host).append("routes", routes));
    }
}
