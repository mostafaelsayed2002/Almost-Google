package SearchEngine;

import java.io.Serializable;
import java.util.Objects;
import java.util.Vector;
import java.util.Comparator;


class rankComparator implements Comparator<Website> {
    public int compare(Website w1, Website w2) {
        return Double.compare(w2.lastRank, w1.lastRank);
    }
}

public class Word implements Serializable {

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
        return Objects.hash(word);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Word)) {
            return false;
        }
        Word w = (Word) obj;
        return Objects.equals(word, w.word);
    }

    public void sortWebsites() {
        rankComparator rankcomparator = new rankComparator();
        websites.sort(rankcomparator);
        System.out.println("-------------------------------------------------------");
        for (Website w : websites
        ) {
            System.out.println(w.lastRank);
        }
        System.out.println("-------------------------------------------------------");
    }

}



