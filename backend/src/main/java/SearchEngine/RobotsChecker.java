package SearchEngine;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.json.JSONArray;

import javax.net.ssl.SSLHandshakeException;
import java.io.DataInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class RobotsChecker {
    static MongoCollection forbidden;
    HashMap<String, HashSet<String>> PageDisallows;

    public RobotsChecker() {
        initDatabase();
    }

    private void initDatabase() {
        MongoClient mongoClient = MongoClients.create("mongodb://127.0.0.1:27017");
        var database = mongoClient.getDatabase("test");
        forbidden = database.getCollection("forbidden");
    }

    private boolean checkForbidden(String urlString) {
        try {

            URL url = new URL(urlString);
            var host = url.getHost();
            Document document = (Document) forbidden.find(new Document().append("url", host)).first();
            JSONArray routes = new JSONArray((ArrayList<String>) document.get("routes"));
            String checkPart = url.toString().substring(host.length() + urlString.indexOf(host));
            for (var route : routes) {
                if (checkPart.matches((String) route))
                    return false;
            }
            return true;
        } catch (MalformedURLException e) {
            System.out.println(e.toString());
            return true;
        }
    }

    public boolean isAllowed(String urlString) {
        try {

            URL url = new URL(urlString);
            var protocol = url.getProtocol();
            var host = url.getHost();

            if (forbidden.countDocuments((new Document()).append("url", host)) != 0)
                return checkForbidden(urlString);

            URL robotsUrl = new URL(protocol + "://" + host + "/robots.txt");
            DataInputStream dataInputStream;
            try {
                dataInputStream = new DataInputStream(robotsUrl.openStream());
            } catch (FileNotFoundException e) {
                System.out.println(e.toString());

                return true;
            } catch (SSLHandshakeException e) {
                System.out.println(e.toString());

                return true;
            }
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
                    var instructionArray = instruction.split(" ");
                    if (instructionArray.length < 2)
                        continue;
                    String route = instructionArray[1];
                    String[] regexKeyCharacters = { ".", "+", "?", "^", "$", "[", "]", "|", "(", ")" };
                    for (var keyChar : regexKeyCharacters)
                        route = route.replace(keyChar, "\\" + keyChar);
                    route = route.replaceAll("\\*", ".*");
                    routes.put(route);

                } else if (instruction.startsWith("User-agent:")) {
                    flag = false;
                } else {
                    continue;
                }
            }
            forbidden.insertOne((new Document()).append("url", host).append("routes", routes));
            return checkForbidden(urlString);
        } catch (MalformedURLException e) {
            System.out.println(e.toString());
            return true;
        } catch (IOException e) {
            System.out.println(e.toString());
            return true;
        }
    }
}
