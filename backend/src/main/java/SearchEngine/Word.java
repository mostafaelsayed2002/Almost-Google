package SearchEngine;

import java.util.Objects;
import java.util.Vector;
import java.util.Comparator;


class rankComparator implements Comparator<Website> {
    public int compare(Website w1, Website w2) {
        return (int) (w1.lastRank - w2.lastRank);
    }
}

public class Word {

    public String word;
    public Vector<Website> websites;

    public double IDF;


    Word() {
        word = "";
        websites = new Vector<Website>();
        IDF = 0;
    }

    @Override
    public int hashCode() {

//        int x = 0;
//        for (int i = 0; i < word.length(); i++) {
//            char c = word.charAt(i);
//            x = x * 256 + (int) c;
//        }
        return Objects.hash(word);
    }

    @Override
    public boolean equals(Object obj) {
//        Word w = (Word) obj;
//        if (w.word == this.word)
//            return true;
//        return false;

        if (!(obj instanceof Word)) {
            return false;
        }
        Word w = (Word) obj;
        return Objects.equals(word, w.word);
    }

    public void sortWebsites() {
        rankComparator rankcomparator = new rankComparator();
        websites.sort(rankcomparator);
    }

}



