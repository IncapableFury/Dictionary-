/*
Author : Dongming Ma
A useless dictionary just like the hotdog detector.
 */
package hw3;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class ultimateUeslessDictionary {

    public static File file = new File("dict.txt");
//    public static ArrayList<String> line = new ArrayList<>();
    public static Scanner s = new Scanner(System.in);
    public static String word = "";
    public static HashMap dict = new HashMap();

    public static void main(String[] args) throws IOException {
        readFileIntoDict(file,dict);
        System.out.println("What are you looking for?");
        word = s.next();
//
        if (dict.containsKey(word)) {
            System.out.println("Okay we have it, but we don't what does it mean either");
        } else {
            System.out.println("Mis-spelled,why don't you google it?");
        }
    }

    public static void readFileIntoDict(File file,HashMap dict) throws FileNotFoundException {
        Scanner reader = new Scanner(file);
        for (int i = 0; reader.hasNextLine(); i++) {
            dict.put(reader.nextLine(),1);
        }
        reader.close();
    }
}
