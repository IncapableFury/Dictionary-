package parsetext;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

public class ParseText {

    File book, wordsFile;
    static Scanner reader;
    static ArrayList<String> keywords = new ArrayList<>(200);
//    Set<String> sortedKetWords = new TreeSet<>();
    static ArrayList<String> words = new ArrayList<>();
    static HashMap<String, Integer> wordCount = new HashMap<>();

    public static void main(String[] args) throws FileNotFoundException {
//read in keywords
        File wordsFile = new File("wordsFile.dat");
        reader = new Scanner(wordsFile);

        for (int i = 0; reader.hasNextLine(); i++) {
//            keywords.add(reader.nextLine());
            String[] str = reader.nextLine().trim().split("\\s+");
            for (int j = 0; j < str.length; j++) {
                keywords.add(str[j]);
            }
        }
        reader.close();

//read in book
        File book = new File("Frankenstein.txt");
        reader = new Scanner(book);
        for (int i = 0; reader.hasNextLine(); i++) {
            String[] wordsPerLine = reader.nextLine().replaceAll(".[\\n\\r]+", " ### ").split("[^\\w'(###)]+|[\\t\\n\\r\\*\\s]+");//replace new paragraph by ###
            for (int j = 0; j < wordsPerLine.length; j++) {
                words.add(wordsPerLine[j]);
            }
        }
        System.out.println("There are " + words.size() + " words in book.");

        for (int i = 1; i < words.size() - 1; i++) {
//            String before = words.get(i - 1);
            String w = words.get(i);
//            String after = words.get(i + 1);
            String before = words.get(i - 1) + " " + words.get(i);
            String after = words.get(i) + " " + words.get(i + 1);
//if(before.contains(".")||after.contains(".")) System.out.println(before);
//if(after.contains(".")) System.out.println(after);
            if (keywords.contains(w)) {

                if (!before.contains("###")) {
                    wordCount.put(before, wordCount.getOrDefault(before, 0) + 1);
                }
                if (!before.contains("###")) {
                    wordCount.put(after, wordCount.getOrDefault(after, 0) + 1);
                }
            }
        }

//        wordCount.put("", 0);//cheating
        Iterator iterator = wordCount.keySet().iterator();
        System.out.println("Here are the word combinations which count for more than 50 times:");
        int howMany = 0;
        int total = 0;
        while (iterator.hasNext()) {
            String key = (String) iterator.next();

            if (wordCount.get(key) > 50) {
                System.out.println(key + "  ==>  " + wordCount.get(key));
                howMany += 1;
                total += wordCount.get(key);
            }
        }
//        System.out.println("her and" + "  ==>  " + wordCount.get("her and"));
        System.out.println("There are " + howMany + " in total.");
        System.out.println("They counts total " + total + " .");
//        System.out.println(wordCount);
    }

}
