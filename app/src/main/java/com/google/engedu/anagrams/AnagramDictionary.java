package com.google.engedu.anagrams;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 7;
    private static int wordLength = DEFAULT_WORD_LENGTH;

    private Random random = new Random();
    private static ArrayList<String> wordList = new ArrayList<>();
    private static HashSet<String> wordSet = new HashSet<>();
    private static HashMap<String, ArrayList<String>> lettersToWord = new HashMap<>();
    private static HashMap<Integer, ArrayList<String>> sizeToWords = new HashMap<>();

    public AnagramDictionary(InputStream wordListStream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(wordListStream));
        String line;
        while ((line = in.readLine()) != null) {
            String word = line.trim();

            String sorted = alphabeticalOrder(word);

            wordList.add(word);
            wordSet.add(word);

            if (!lettersToWord.containsKey(sorted)) {
                lettersToWord.put(sorted, new ArrayList<String>());
                lettersToWord.get(sorted).add(word);
            } else {
                lettersToWord.get(sorted).add(word);
            }

            if (!sizeToWords.containsKey(sorted.length())) {
                sizeToWords.put(sorted.length(), new ArrayList<String>());
                sizeToWords.get(sorted.length()).add(word);
            } else {
                sizeToWords.get(sorted.length()).add(word);
            }


        }
    }

    public String alphabeticalOrder(String word) {
        char[] chars = word.toCharArray();
        Arrays.sort(chars);
        return new String(chars);
    }

    public boolean isGoodWord(String word, String base) {
        return wordSet.contains(word) && (!word.contains(base));
    }

    public ArrayList<String> getAnagramsWithOneMoreLetter(String word) {
        ArrayList<String> result = new ArrayList<String>();
        String temp;
        for (int i = 97; i < 123; i++) {
            temp = word + (char) i;
            temp = alphabeticalOrder(temp);

            if (lettersToWord.containsKey(temp)) {
                for (String string : lettersToWord.get(temp)) {
                    if (isGoodWord(string, word))
                        result.add(string);
                }
            }
        }

        return result;
    }


    public ArrayList<String> getAnagramsWithTwoMoreLetter(String word) {
        ArrayList<String> result = new ArrayList<String>();
        String temp;

        for (int i = 97; i < 123; i++)
            for (int j = 97; i < 123; i++) {
                temp = word + ((char) i) + ((char) j);
                temp = alphabeticalOrder(temp);

                if (lettersToWord.containsKey(temp)) {
                    for (String string : lettersToWord.get(temp)) {
                        if (isGoodWord(string, word))
                            result.add(string);
                    }
                }
            }

        return result;
    }

    public void removeFromPool() {
        int index = 0;
        int numberOfWords = wordSet.size();
        while (index < numberOfWords) {
            if (getAnagramsWithTwoMoreLetter(alphabeticalOrder(sizeToWords.get(wordLength).get(index))).size() < MIN_NUM_ANAGRAMS) {
                numberOfWords -= sizeToWords.get(wordLength).size();
                sizeToWords.get(wordLength).remove(index);
            }

            index++;

        }
    }

    public String pickGoodStarterWord() {

        int rand;
        while (true) {
            if (wordLength > MAX_WORD_LENGTH)
                wordLength = DEFAULT_WORD_LENGTH;

//            rand = random.nextInt(wordList.size());
            rand = random.nextInt(sizeToWords.get(wordLength).size());

//            if ((getAnagramsWithOneMoreLetter(alphabeticalOrder(sizeToWords.get(wordLength).get(rand))).size() >= MIN_NUM_ANAGRAMS) && (wordLength) <= MAX_WORD_LENGTH)
            if ((getAnagramsWithTwoMoreLetter(alphabeticalOrder(sizeToWords.get(wordLength).get(rand))).size() >= MIN_NUM_ANAGRAMS) && (wordLength) <= MAX_WORD_LENGTH)
                break;
        }

        Log.d("pickGoodStarterWord", sizeToWords.get(wordLength).get(rand));

//        return wordList.get(rand);
        return sizeToWords.get(wordLength++).get(rand);
    }
}
