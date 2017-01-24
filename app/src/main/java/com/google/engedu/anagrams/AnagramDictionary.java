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
    int wordLength=3;
    private Random random = new Random();
    ArrayList<String> wordList=new ArrayList<String>();
    HashSet<String> wordSet=new HashSet<String>();
    HashMap<String ,ArrayList<String> > lettersToWord=new HashMap<String,ArrayList<String>>();

    HashMap<Integer ,ArrayList<String> > sizeToWord=new HashMap<Integer,ArrayList<String>>();
    public AnagramDictionary(InputStream wordListStream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(wordListStream));
        String line;
        while((line = in.readLine()) != null) {
            String word = line.trim();
            wordList.add(word);
            wordSet.add(word);
            ArrayList<String> temp_word=new ArrayList<String>();
            String sortWord;
            sortWord=alphabeticalWord(word);
            if(lettersToWord.containsKey(sortWord)){
                temp_word=lettersToWord.get(sortWord);
                temp_word.add(word);
            }else{
                temp_word.add(word);
                lettersToWord.put(sortWord,temp_word);
            }
            ArrayList<String> temp_word2=new ArrayList<String>();
            int l=word.length();
            if(sizeToWord.containsKey(l)){
                temp_word2=sizeToWord.get(l);
                temp_word2.add(word);
                sizeToWord.put(l,temp_word2);
            }else{
                temp_word2.add(word);
                sizeToWord.put(l,temp_word2);
            }
        }
    }

    public boolean isGoodWord(String word, String base) {
        if(wordSet.contains(word)){
            if(word.contains(base)){
                return false;
            }else{
                return true;
            }
        }else{
            return false;
        }
    }

    public ArrayList<String> getAnagrams(String targetWord) {
        ArrayList<String> result = new ArrayList<String>();
        return result;
    }

    public String alphabeticalWord(String str){
        char arr[];
        arr=str.toCharArray();
        Arrays.sort(arr);
        String newWord=new String(arr);
        return newWord;
    }

    public ArrayList<String> getAnagramsWithTwoMoreLetter(String word) {
        ArrayList<String> result = new ArrayList<String>();
        String newWord;
        int r;
        for(char ch='a';ch<='z';ch++){
            for(char c='a';c<='z';c++) {
                newWord = ch + word+c;
                newWord = alphabeticalWord(newWord);
                if (lettersToWord.containsKey(newWord)) {
                    result.addAll(lettersToWord.get(newWord));
                }
            }
        }
        for(r=result.size()-1;r>=0;r--){
            if(!isGoodWord(result.get(r),word)){
                result.remove(r);
            }
        }
        return result;
    }
    public ArrayList<String> getAnagramsWithOneMoreLetterTwoStrings(String word,String anotherWord) {
        ArrayList<String> result = new ArrayList<String>();
        String newWord,anotherNewWord,tempWord;
        int r;
        for(char ch='a';ch<='z';ch++){
                newWord = ch + word;
                anotherNewWord = ch + anotherWord;
                newWord = alphabeticalWord(newWord);
                anotherNewWord = alphabeticalWord(anotherNewWord);

                if (lettersToWord.containsKey(newWord)){
                    if(lettersToWord.containsKey(anotherNewWord)) {
                        for (int h = 0; h < lettersToWord.get(newWord).size(); h++) {
                            for (int z = 0; z < lettersToWord.get(anotherNewWord).size(); z++) {
                                tempWord = lettersToWord.get(newWord).get(h) + " " + lettersToWord.get(anotherNewWord).get(z);
                                result.add(tempWord);
                            }
                        }
                    }//result.addAll(lettersToWord.get(newWord));
                }
        }
        for(r=result.size()-1;r>=0;r--){
            String ans[]=result.get(r).split(" ");

            if(!isGoodWord(ans[0],word) || !isGoodWord(ans[1], anotherWord)) {
                    result.remove(r);
                }
            }
        return result;
    }
    public ArrayList<String> getAnagramsWithOneMoreLetter(String word) {
        ArrayList<String> result = new ArrayList<String>();
        String newWord;
        int r;
        for(char ch='a';ch<='z';ch++){
            newWord=ch+word;
            newWord=alphabeticalWord(newWord);
            if(lettersToWord.containsKey(newWord)){
                result.addAll(lettersToWord.get(newWord));
            }
        }
        for(r=result.size()-1;r>=0;r--){
            if(!isGoodWord(result.get(r),word)){
                result.remove(r);
            }
        }
        return result;
    }

    public ArrayList<String> getAnagram(String word,String anotherWord,char ch) {
        ArrayList<String> result = new ArrayList<String>();
        String newWord,anotherNewWord,tempWord;
        int r;
            newWord = ch + word;
            anotherNewWord = ch + anotherWord;
            newWord = alphabeticalWord(newWord);
            anotherNewWord = alphabeticalWord(anotherNewWord);

            if (lettersToWord.containsKey(newWord)){
                if(lettersToWord.containsKey(anotherNewWord)) {
                    for (int h = 0; h < lettersToWord.get(newWord).size(); h++) {
                        for (int z = 0; z < lettersToWord.get(anotherNewWord).size(); z++) {
                            tempWord = lettersToWord.get(newWord).get(h) + " " + lettersToWord.get(anotherNewWord).get(z);
                            result.add(tempWord);
                        }
                    }
                }//result.addAll(lettersToWord.get(newWord));
            }
        for(r=result.size()-1;r>=0;r--){
            String ans[]=result.get(r).split(" ");

            if(!isGoodWord(ans[0],word) || !isGoodWord(ans[1], anotherWord)) {
                result.remove(r);
            }
        }
        return result;
    }


    public String pickGoodStarterWord(){
        String word=new String();
        int j;
        ArrayList<String> lengthWords=new ArrayList<>();
        if(wordLength<=MAX_WORD_LENGTH){
            lengthWords=sizeToWord.get(wordLength);

        }
        int i=random.nextInt(lengthWords.size());
        for(j=i;j<lengthWords.size();j++){
            if(getAnagramsWithOneMoreLetter(lengthWords.get(j)).size()>=MIN_NUM_ANAGRAMS){
                Log.d("word",lengthWords.get(j));
                word=lengthWords.get(j);
                break;
            }
        }
        if(j==lengthWords.size() && word==null){
            for(j=0;j<i;j++){
                if(getAnagramsWithOneMoreLetter(lengthWords.get(j)).size()>=MIN_NUM_ANAGRAMS){
                    word=lengthWords.get(j);
                    break;
                }
            }
        }
        if(wordLength<MAX_WORD_LENGTH){
            wordLength++;

        }
        return word;
    }


    public String[] pickGoodStarterWordForTwoStrings(){
        String word[]=new String[2];
        int j;
        ArrayList<String> lengthWords=new ArrayList<>();
        if(wordLength<=MAX_WORD_LENGTH){
            lengthWords=sizeToWord.get(wordLength);

        }
        int i=random.nextInt(lengthWords.size()),p,flag=0;
        for(j=i;j<lengthWords.size();j++){
            for(char ch='a';ch<='z';ch++){
                    for (p = i; p < lengthWords.size(); p++) {
                        if (p != j && getAnagram(lengthWords.get(p),lengthWords.get(j), ch).size() >= MIN_NUM_ANAGRAMS) {
                            word[0] = lengthWords.get(j);
                            word[1] = lengthWords.get(p);
                            flag=1;
                            break;
                        }
                    }
                if(flag==1){
                    break;
                }
            }
            if(flag==1){
                break;
            }

        }
        if(j==lengthWords.size() && word==null){
            for(j=0;j<i;j++){
                for(char ch='a';ch<='z';ch++){
                    for (p = 0; p < i; p++) {
                        if (p != j && getAnagram(lengthWords.get(p),lengthWords.get(j), ch).size() >= MIN_NUM_ANAGRAMS) {
                            word[0] = lengthWords.get(j);
                            word[1] = lengthWords.get(p);
                            flag=1;
                            break;
                        }
                    }
                    if(flag==1){
                        break;
                    }
                }
                if(flag==1){
                    break;
                }
            }
        }
        if(wordLength<MAX_WORD_LENGTH){
            wordLength++;

        }
        return word;
    }
}
