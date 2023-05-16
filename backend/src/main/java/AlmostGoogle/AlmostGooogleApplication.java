package AlmostGoogle;

import SearchEngine.Result;
import SearchEngine.Website;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvBuilder;
import kotlin.Pair;
import org.bson.Document;
import org.bson.json.JsonObject;
import org.codehaus.jackson.JsonNode;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.web.bind.annotation.*;
import org.tartarus.snowball.ext.englishStemmer;

import javax.print.Doc;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Pattern;

@SpringBootApplication
@RestController
public class AlmostGooogleApplication {
    static Set<String> stopWords = new HashSet<>(Arrays.asList("0o", "0s", "3a", "3b", "3d", "6b", "6o", "a", "a1", "a2", "a3", "a4", "ab", "able", "about", "above", "abst", "ac", "accordance", "according", "accordingly", "across", "act", "actually", "ad", "added", "adj", "ae", "af", "affected", "affecting", "affects", "after", "afterwards", "ag", "again", "against", "ah", "ain", "ain't", "aj", "al", "all", "allow", "allows", "almost", "alone", "along", "already", "also", "although", "always", "am", "among", "amongst", "amoungst", "amount", "an", "and", "announce", "another", "any", "anybody", "anyhow", "anymore", "anyone", "anything", "anyway", "anyways", "anywhere", "ao", "ap", "apart", "apparently", "appear", "appreciate", "appropriate", "approximately", "ar", "are", "aren", "arent", "aren't", "arise", "around", "as", "a's", "aside", "ask", "asking", "associated", "at", "au", "auth", "av", "available", "aw", "away", "awfully", "ax", "ay", "az", "b", "b1", "b2", "b3", "ba", "back", "bc", "bd", "be", "became", "because", "become", "becomes", "becoming", "been", "before", "beforehand", "begin", "beginning", "beginnings", "begins", "behind", "being", "believe", "below", "beside", "besides", "best", "better", "between", "beyond", "bi", "bill", "biol", "bj", "bk", "bl", "bn", "both", "bottom", "bp", "br", "brief", "briefly", "bs", "bt", "bu", "but", "bx", "by", "c", "c1", "c2", "c3", "ca", "call", "came", "can", "cannot", "cant", "can't", "cause", "causes", "cc", "cd", "ce", "certain", "certainly", "cf", "cg", "ch", "changes", "ci", "cit", "cj", "cl", "clearly", "cm", "c'mon", "cn", "co", "com", "come", "comes", "con", "concerning", "consequently", "consider", "considering", "contain", "containing", "contains", "corresponding", "could", "couldn", "couldnt", "couldn't", "course", "cp", "cq", "cr", "cry", "cs", "c's", "ct", "cu", "currently", "cv", "cx", "cy", "cz", "d", "d2", "da", "date", "dc", "dd", "de", "definitely", "describe", "described", "despite", "detail", "df", "di", "did", "didn", "didn't", "different", "dj", "dk", "dl", "do", "does", "doesn", "doesn't", "doing", "don", "done", "don't", "down", "downwards", "dp", "dr", "ds", "dt", "du", "due", "during", "dx", "dy", "e", "e2", "e3", "ea", "each", "ec", "ed", "edu", "ee", "ef", "effect", "eg", "ei", "eight", "eighty", "either", "ej", "el", "eleven", "else", "elsewhere", "em", "empty", "en", "end", "ending", "enough", "entirely", "eo", "ep", "eq", "er", "es", "especially", "est", "et", "et-al", "etc", "eu", "ev", "even", "ever", "every", "everybody", "everyone", "everything", "everywhere", "ex", "exactly", "example", "except", "ey", "f", "f2", "fa", "far", "fc", "few", "ff", "fi", "fifteen", "fifth", "fify", "fill", "find", "fire", "first", "five", "fix", "fj", "fl", "fn", "fo", "followed", "following", "follows", "for", "former", "formerly", "forth", "forty", "found", "four", "fr", "from", "front", "fs", "ft", "fu", "full", "further", "furthermore", "fy", "g", "ga", "gave", "ge", "get", "gets", "getting", "gi", "give", "given", "gives", "giving", "gj", "gl", "go", "goes", "going", "gone", "got", "gotten", "gr", "greetings", "gs", "gy", "h", "h2", "h3", "had", "hadn", "hadn't", "happens", "hardly", "has", "hasn", "hasnt", "hasn't", "have", "haven", "haven't", "having", "he", "hed", "he'd", "he'll", "hello", "help", "hence", "her", "here", "hereafter", "hereby", "herein", "heres", "here's", "hereupon", "hers", "herself", "hes", "he's", "hh", "hi", "hid", "him", "himself", "his", "hither", "hj", "ho", "home", "hopefully", "how", "howbeit", "however", "how's", "hr", "hs", "http", "hu", "hundred", "hy", "i", "i2", "i3", "i4", "i6", "i7", "i8", "ia", "ib", "ibid", "ic", "id", "i'd", "ie", "if", "ig", "ignored", "ih", "ii", "ij", "il", "i'll", "im", "i'm", "immediate", "immediately", "importance", "important", "in", "inasmuch", "inc", "indeed", "index", "indicate", "indicated", "indicates", "information", "inner", "insofar", "instead", "interest", "into", "invention", "inward", "io", "ip", "iq", "ir", "is", "isn", "isn't", "it", "itd", "it'd", "it'll", "its", "it's", "itself", "iv", "i've", "ix", "iy", "iz", "j", "jj", "jr", "js", "jt", "ju", "just", "k", "ke", "keep", "keeps", "kept", "kg", "kj", "km", "know", "known", "knows", "ko", "l", "l2", "la", "largely", "last", "lately", "later", "latter", "latterly", "lb", "lc", "le", "least", "les", "less", "lest", "let", "lets", "let's", "lf", "like", "liked", "likely", "line", "little", "lj", "ll", "ll", "ln", "lo", "look", "looking", "looks", "los", "lr", "ls", "lt", "ltd", "m", "m2", "ma", "made", "mainly", "make", "makes", "many", "may", "maybe", "me", "mean", "means", "meantime", "meanwhile", "merely", "mg", "might", "mightn", "mightn't", "mill", "million", "mine", "miss", "ml", "mn", "mo", "more", "moreover", "most", "mostly", "move", "mr", "mrs", "ms", "mt", "mu", "much", "mug", "must", "mustn", "mustn't", "my", "myself", "n", "n2", "na", "name", "namely", "nay", "nc", "nd", "ne", "near", "nearly", "necessarily", "necessary", "need", "needn", "needn't", "needs", "neither", "never", "nevertheless", "new", "next", "ng", "ni", "nine", "ninety", "nj", "nl", "nn", "no", "nobody", "non", "none", "nonetheless", "noone", "nor", "normally", "nos", "not", "noted", "nothing", "novel", "now", "nowhere", "nr", "ns", "nt", "ny", "o", "oa", "ob", "obtain", "obtained", "obviously", "oc", "od", "of", "off", "often", "og", "oh", "oi", "oj", "ok", "okay", "ol", "old", "om", "omitted", "on", "once", "one", "ones", "only", "onto", "oo", "op", "oq", "or", "ord", "os", "ot", "other", "others", "otherwise", "ou", "ought", "our", "ours", "ourselves", "out", "outside", "over", "overall", "ow", "owing", "own", "ox", "oz", "p", "p1", "p2", "p3", "page", "pagecount", "pages", "par", "part", "particular", "particularly", "pas", "past", "pc", "pd", "pe", "per", "perhaps", "pf", "ph", "pi", "pj", "pk", "pl", "placed", "please", "plus", "pm", "pn", "po", "poorly", "possible", "possibly", "potentially", "pp", "pq", "pr", "predominantly", "present", "presumably", "previously", "primarily", "probably", "promptly", "proud", "provides", "ps", "pt", "pu", "put", "py", "q", "qj", "qu", "que", "quickly", "quite", "qv", "r", "r2", "ra", "ran", "rather", "rc", "rd", "re", "readily", "really", "reasonably", "recent", "recently", "ref", "refs", "regarding", "regardless", "regards", "related", "relatively", "research", "research-articl", "respectively", "resulted", "resulting", "results", "rf", "rh", "ri", "right", "rj", "rl", "rm", "rn", "ro", "rq", "rr", "rs", "rt", "ru", "run", "rv", "ry", "s", "s2", "sa", "said", "same", "saw", "say", "saying", "says", "sc", "sd", "se", "sec", "second", "secondly", "section", "see", "seeing", "seem", "seemed", "seeming", "seems", "seen", "self", "selves", "sensible", "sent", "serious", "seriously", "seven", "several", "sf", "shall", "shan", "shan't", "she", "shed", "she'd", "she'll", "shes", "she's", "should", "shouldn", "shouldn't", "should've", "show", "showed", "shown", "showns", "shows", "si", "side", "significant", "significantly", "similar", "similarly", "since", "sincere", "six", "sixty", "sj", "sl", "slightly", "sm", "sn", "so", "some", "somebody", "somehow", "someone", "somethan", "something", "sometime", "sometimes", "somewhat", "somewhere", "soon", "sorry", "sp", "specifically", "specified", "specify", "specifying", "sq", "sr", "ss", "st", "still", "stop", "strongly", "sub", "substantially", "successfully", "such", "sufficiently", "suggest", "sup", "sure", "sy", "system", "sz", "t", "t1", "t2", "t3", "take", "taken", "taking", "tb", "tc", "td", "te", "tell", "ten", "tends", "tf", "th", "than", "thank", "thanks", "thanx", "that", "that'll", "thats", "that's", "that've", "the", "their", "theirs", "them", "themselves", "then", "thence", "there", "thereafter", "thereby", "thered", "therefore", "therein", "there'll", "thereof", "therere", "theres", "there's", "thereto", "thereupon", "there've", "these", "they", "theyd", "they'd", "they'll", "theyre", "they're", "they've", "thickv", "thin", "think", "third", "this", "thorough", "thoroughly", "those", "thou", "though", "thoughh", "thousand", "three", "throug", "through", "throughout", "thru", "thus", "ti", "til", "tip", "tj", "tl", "tm", "tn", "to", "together", "too", "took", "top", "toward", "towards", "tp", "tq", "tr", "tried", "tries", "truly", "try", "trying", "ts", "t's", "tt", "tv", "twelve", "twenty", "twice", "two", "tx", "u", "u201d", "ue", "ui", "uj", "uk", "um", "un", "under", "unfortunately", "unless", "unlike", "unlikely", "until", "unto", "uo", "up", "upon", "ups", "ur", "us", "use", "used", "useful", "usefully", "usefulness", "uses", "using", "usually", "ut", "v", "va", "value", "various", "vd", "ve", "ve", "very", "via", "viz", "vj", "vo", "vol", "vols", "volumtype", "vq", "vs", "vt", "vu", "w", "wa", "want", "wants", "was", "wasn", "wasnt", "wasn't", "way", "we", "wed", "we'd", "welcome", "well", "we'll", "well-b", "went", "were", "we're", "weren", "werent", "weren't", "we've", "what", "whatever", "what'll", "whats", "what's", "when", "whence", "whenever", "when's", "where", "whereafter", "whereas", "whereby", "wherein", "wheres", "where's", "whereupon", "wherever", "whether", "which", "while", "whim", "whither", "who", "whod", "whoever", "whole", "who'll", "whom", "whomever", "whos", "who's", "whose", "why", "why's", "wi", "widely", "will", "willing", "wish", "with", "within", "without", "wo", "won", "wonder", "wont", "won't", "words", "world", "would", "wouldn", "wouldnt", "wouldn't", "www", "x", "x1", "x2", "x3", "xf", "xi", "xj", "xk", "xl", "xn", "xo", "xs", "xt", "xv", "xx", "y", "y2", "yes", "yet", "yj", "yl", "you", "youd", "you'd", "you'll", "your", "youre", "you're", "yours", "yourself", "yourselves", "you've", "yr", "ys", "yt", "z", "zero", "zi", "zz"));

    static org.jsoup.nodes.Document ParseDoc(String fileName) {
        String content = null;
        try {
            File file = new File("/home/walid/Downloads/vsCode/Almost-Google/backend/Documents/" + fileName + ".json" );
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readValue(file, JsonNode.class);
            content = jsonNode.get("document").getTextValue();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return Jsoup.parse(content);
    }

    public static void main(String[] args) {
        SpringApplication.run(AlmostGooogleApplication.class, args);
    }

    static MongoCollection<Document> InitDataBase() {
        Dotenv dotenv = new DotenvBuilder().load();
        MongoClient mongoClient = MongoClients.create("mongodb://127.0.0.1:27017");
        MongoDatabase database = mongoClient.getDatabase("test");
        var collection = database.getCollection("searchIndexer3");
        return collection;
    }

    static int FindCount(String str, String sub) {
        if (str.contains(sub)) {
            return 1 + FindCount(str.replaceFirst(sub, ""), sub);
        }
        return 0;
    }

    @GetMapping("/")
    @CrossOrigin(origins = "http://localhost:3000")
    public @ResponseBody Document getResult(@RequestParam String Input , @RequestParam String page) {
        var pageNumber = Integer.parseInt(page);
        var numberOfWebsites = 0;

        boolean type = true;
        if (Input.charAt(0) == '\"' && Input.charAt(Input.length() - 1) == '\"') type = false;
        englishStemmer stemmer = new englishStemmer();

        String PhraseSearch = "";
        Vector<String> words = new Vector<>();
        String[] input = Input.replaceAll("\"", "").split("\\s");
        for (String word : input) {
            word = word.replaceAll("[^a-zA-Z\\\\s]", "");
            if (word.equals("")) continue;
            if (!stopWords.contains(word.toLowerCase())) {
                if (PhraseSearch.length() == 0) {
                    PhraseSearch = word;
                } else {
                    PhraseSearch += " " + word;
                }
                stemmer.setCurrent(word); /* use the stemmer to stem the word */
                stemmer.stem();
                word = stemmer.getCurrent();
                words.add(word);
            }
        }

        Vector<HashSet<Website>> Graph = new Vector<>();
        for (int i = 0; i < words.size(); i++) {
            HashSet<Website> temp = new HashSet<>();
            Graph.add(temp);
        }


        var collection = InitDataBase();
        for (int i = 0; i < words.size(); i++) {
            Document query = new Document("word", words.get(i).toLowerCase());
            Document result = collection.find(query).first();

            List<Document> websites = ((List<Document>) result.get("websites"));


            for (Document website : websites) {
                Website w = new Website();
                w.url = website.getString("url");
                w.title = website.getString("title");
                w.TF = website.getInteger("TF");
                //w.lastRank= website.getDouble("lastRank");
                w.lastRank = website.getDouble("lastRank");
                List<Document> places = (List<Document>) website.get("places");
                for (Document place : places) {
                    String tag = place.getString("place");
                    String text = place.getString("content");
                    w.places.add(new Pair<String, String>(tag, text));
                }
                Graph.get(i).add(w);
            }
        }

        ArrayList<Website> Intersection = new ArrayList<>();
        if (Graph.size() == 0)
            return new Document();
        for (Website web : Graph.get(0)) {

            int rank = 0;
            boolean Intersected = true;
            for (HashSet<Website> H : Graph) {
                if (!H.contains(web)) {
                    Intersected = false;
                    break;
                } else {
                    for (Website w : H)
                        if (w.url.equals(web.url))
                            rank += w.lastRank;
                }
            }
            if (Intersected) {
                web.lastRank = rank;
                Intersection.add(web);
            }
        }


        if (type == false) {
            String[] tags = {"title", "p", "h1", "h2", "h3", "h4", "h5", "h6", "li"};
            Vector<Website> ToRemove = new Vector<>();

            for (Website web : Intersection) {
                String name = web.url;
                name = name.replace("*", "`{}");
                name = name.replace("://", "}");
                name = name.replace("/", "{");
                name = name.replace("?", "`");
                name = name.replace(":", "&");
                org.jsoup.nodes.Document doc = ParseDoc(name);

                String text = doc.text();
                if (!Pattern.compile(Pattern.quote(PhraseSearch), Pattern.CASE_INSENSITIVE).matcher(text).find())
                    ToRemove.add(web);
                else {
                    boolean flag = false;
                    web.lastRank = FindCount(text, PhraseSearch);
                    for (String tag : tags) {
                        Elements T = doc.select(tag);
                        for (Element t : T) {
                            if (t.text().contains(PhraseSearch)) {
                                web.places.clear();
                                web.places.add(new Pair<String, String>("breif", t.text()));
                                flag = true;
                                break;
                            }
                            if (flag) break;
                        }
                    }
                }
            }
            for (Website web : ToRemove)
                Intersection.remove(web);
        }

        Comparator<Website> myComparator = new Comparator<Website>() {
            @Override
            public int compare(Website o1, Website o2) {
                return (int) (o2.getMyVariable() - o1.getMyVariable());
            }
        };
        Collections.sort(Intersection, myComparator);


//        for (Website web : Intersection)
//            System.out.println(" url: " + web.url + " lastRank: " + web.lastRank);
        // for single words only
        ArrayList<Result> results = new ArrayList<>();
        for (Website web : Intersection) {
            var x = new Result(web.url, web.places.get(0).getSecond(), web.title);
            results.add(x);
        }

        int startIndex = (pageNumber - 1) * 10;
        int endIndex = Math.min(startIndex + 10, results.size());
        numberOfWebsites = results.size();
        var x  = results.subList(startIndex, endIndex);
        System.out.println(x);

        Document result = new Document();
        result.append("numberOfWebsites", numberOfWebsites).append("websites", x);
        return result;
    }
}