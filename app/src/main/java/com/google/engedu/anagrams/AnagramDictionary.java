package com.google.engedu.anagrams;

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
    private static final char[] ALPHABET = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 7;
    private int wordLength = DEFAULT_WORD_LENGTH;
    private Random random = new Random();
    private ArrayList<String> wordList = new ArrayList<>();
    private HashSet<String> wordSet = new HashSet<>();
    private HashMap<String, ArrayList<String>> lettersToWord = new HashMap();
    private HashMap<Integer, ArrayList<String>> sizeToWords = new HashMap<>();

    public AnagramDictionary(InputStream wordListStream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(wordListStream));
        String line;
        while ((line = in.readLine()) != null) {

            String word = line.trim();

            if (word.length() >= DEFAULT_WORD_LENGTH && word.length() <= MAX_WORD_LENGTH) {
                wordList.add(word);
                wordSet.add(word);


                if (!sizeToWords.containsKey(word.length())) {
                    ArrayList<String> sameLength = new ArrayList<>();
                    sameLength.add(word);
                    sizeToWords.put(word.length(), sameLength);
                } else {
                    sizeToWords.get(word.length()).add(word);
                }

                if (!lettersToWord.containsKey(sortLetters(word))) {
                    ArrayList<String> anagrams = new ArrayList<>();
                    anagrams.add(word);
                    lettersToWord.put(sortLetters(word), anagrams);
                } else {
                    lettersToWord.get(sortLetters(word)).add(word);
                }
            }
        }
    }

    public boolean isGoodWord(String word, String base) {
        return wordSet.contains(word) && !word.contains(base);
    }

    public String sortLetters(String word) {
        char[] arr = word.toCharArray();
        Arrays.sort(arr);
        return String.valueOf(arr);
    }

    public ArrayList<String> getAnagrams(String targetWord) {
        ArrayList<String> result = new ArrayList<>();
        String sortedWord = sortLetters(targetWord);

        for (String word : wordList) {
            if (sortedWord.equals(sortLetters(word))) {
                result.add(word);
            }
        }
        return result;
    }

    public ArrayList<String> getAnagramsWithOneMoreLetter(String word) {
        ArrayList<String> result = new ArrayList<>();
        for (char c : ALPHABET) {
            String longerString = sortLetters(word + c);
            if (lettersToWord.containsKey(longerString)) {
                for(String entry:lettersToWord.get(longerString)){
                    if(isGoodWord(entry, word)){
                        result.add(entry);
                    }
                }
            }
        }
        return result;
    }

    public ArrayList<String> getAnagramsWithTwoMoreLetter(String word){
        ArrayList<String> result = new ArrayList<>();
        for (char c1 : ALPHABET){
            for(char c2: ALPHABET) {
                String longerString = sortLetters(word + c1+c2);
                if (lettersToWord.containsKey(longerString)) {
                    for (String entry : lettersToWord.get(longerString)) {
                        if (isGoodWord(entry, word)) {
                            result.add(entry);
                        }
                    }
                }
            }
        }
        return result;
    }

    public String pickGoodStarterWord() {
        ArrayList<String> firstWords = sizeToWords.get(wordLength);
        String startWord;
        int index;
        while(true){
            index = random.nextInt(firstWords.size());
            if(getAnagramsWithOneMoreLetter(firstWords.get(index)).size()>MIN_NUM_ANAGRAMS){
                startWord = firstWords.get(index);
                firstWords.remove(index);
                break;
            } else {
                firstWords.remove(index);
            }
        }
        return startWord;
    }

    public void increaseDifficulty(){
        wordLength++;
    }
}
