package by.bsuir.oop.paint.configuration.language;

import java.util.HashMap;

public abstract class Language {
   HashMap<Words, String> wordsMap = new HashMap<>();

   public HashMap<Words, String> getWordsMap() {
      return wordsMap;
   }
}
