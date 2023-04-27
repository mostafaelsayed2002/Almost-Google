package SearchEngine;

import java.util.Objects;
import java.util.Vector;

public class Word {

    public String word;
    public Vector<Website> websites;

   Word()
   {
       word ="";
       websites=new Vector<Website>();
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
        Word w = (Word) obj ;
        return Objects.equals(word, w.word);
    }
}

