package com.google.engedu.ghost;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

public class SimpleDictionary implements GhostDictionary {
    private ArrayList<String> words;

    public SimpleDictionary(InputStream wordListStream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(wordListStream));
        words = new ArrayList<>();
        String line = null;
        while((line = in.readLine()) != null) {
            String word = line.trim();
            if (word.length() >= MIN_WORD_LENGTH)
              words.add(line.trim());
        }
    }

    @Override
    public boolean isWord(String word) {
        return words.contains(word);
    }

    @Override
    public String getAnyWordStartingWith(String prefix) {
        Log.d("buggy", "geting an word");
        if(null == prefix || prefix.length() == 0){
            Log.d("buggy", "empty");
            Random rnd = new Random();
            Log.d("buggy", Integer.toString(words.size()));
            int index = rnd.nextInt(words.size());
            Log.d("buggy", Integer.toString(index));
            return words.get(index);
        }
        else{
            Log.d("buggy", "not empty");
            return binaryWordSearch(prefix);
        }
    }

    public String binaryWordSearch(String prefix){
        Log.d("buggy", "starting search");
        int end = words.size()-1;
        int str = 0;
        int mid;
        while(str < end) {
            mid = (str + end) / 2;
            //mid starts wit or is equal to prefix
            if (words.get(mid).startsWith(prefix)) {
                return words.get(mid);
            }
            //mid is less than prefix
            if (words.get(mid).compareTo(prefix) < 0) {
                str = mid + 1;
            }
            //id is greater than prefix
            else if (words.get(mid).compareTo(prefix) > 0) {
                end = mid - 1;
            }
        }
        Log.d("buggy","end search");
        return null;
    }

    @Override
    public String getGoodWordStartingWith(String prefix) {
        return null;
    }
}
