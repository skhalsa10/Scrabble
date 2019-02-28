package me.snizzle.scrabble;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

//
public class ComputerPlayerTest {

    public static void main(String args[]){
        ScrabbleWords dictionary = new ScrabbleWords("resources/twl06.txt");

        System.out.println(dictionary.verify("abductions"));
        System.out.println(dictionary.verify("abdtions"));
        System.out.println(dictionary.isPrefix("absin"));



    }
}
