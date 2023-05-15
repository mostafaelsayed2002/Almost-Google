package SearchEngine;


import kotlin.Pair;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Objects;
import java.util.Vector;

public class Website implements Serializable {

    public String url;
    // public String[] places;
    public Vector<Pair<String, String>> places;
    public int TF;
    public double TF_IDF;
    public double pageRank;
    public double lastRank;     // =pageRank*TF_IDF

    public String title;


    public Website() {
        places = new Vector<>();
        TF = 0;
        TF_IDF = 0;
    }


    @Override
    public int hashCode() {
        return Objects.hash(url);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Website)) {
            return false;
        }
        Website web = (Website) obj;
        return Objects.equals(url, web.url);
    }

    public double getMyVariable() {
        return lastRank;
    }

    public void setMyVariable(int myVariable) {
        this.lastRank = myVariable;
    }


}

