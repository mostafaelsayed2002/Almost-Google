package SearchEngine;


import kotlin.Pair;

import java.util.Vector;

public class Website  {

    public String url;
   // public String[] places;
    public Vector<Pair<String,String>>places;
    public int TF;
    public String title;


    Website() {
        places = new Vector<>();
        TF = 0;
    }
}

