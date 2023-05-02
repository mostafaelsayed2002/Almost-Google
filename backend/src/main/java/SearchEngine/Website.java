package SearchEngine;


import kotlin.Pair;

import java.util.Vector;

public class Website  {

    public String url;
   // public String[] places;
    public Vector<Pair<String,String>>places;
    public int TF;
    public double TF_IDF;
    public double pageRank;
    public double lastRank;     // =pageRank*TF_IDF


    Website() {
        places = new Vector<>();
        TF = 0;
        TF_IDF = 0;
    }
}

