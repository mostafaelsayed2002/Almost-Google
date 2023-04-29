package SearchEngine;


public class Website {

    public String url;
    public String[] places;
    public int TF;
    public double TF_IDF;
    public double pageRank;
    public double lastRank;     // =pageRank*TF_IDF


    Website() {
        places = new String[8];
        url = "";
        TF = 0;
        TF_IDF = 0;
    }
}

