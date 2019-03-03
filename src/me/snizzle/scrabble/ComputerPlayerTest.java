package me.snizzle.scrabble;


import me.snizzle.datastructure.Trie;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

//
public class ComputerPlayerTest {

    public static void main(String args[]){

        HashMap<ScrabbleBoardPoint, ScrabbleTile> test = new HashMap<>();
        HashMap<ScrabbleBoardPoint, ScrabbleTile> test2 = new HashMap<>();
        test2.put(new ScrabbleBoardPoint(0,0), new ScrabbleTile('c',0));
        test2.put(new ScrabbleBoardPoint(0,1), new ScrabbleTile('k',1));

        test.putAll(test2);

        System.out.println(test.containsKey(new ScrabbleBoardPoint(0,0)));
        System.out.println(test.containsValue(new ScrabbleTile('k',1)));


    }
}
