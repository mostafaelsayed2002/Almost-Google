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
        Word w = (Word) obj ;
        return Objects.equals(word, w.word);
    }
}

