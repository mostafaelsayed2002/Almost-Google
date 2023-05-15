package SearchEngine;

public class Fetching {
    public static void main(String[] args) {
        Crawler.main(args);
        System.out.println("Crawler Done");
        Indexer.main(args);
        System.out.println("Indexer Done");
        Ranker.main(args);
        System.out.println("Ranker Done");
    }
}
