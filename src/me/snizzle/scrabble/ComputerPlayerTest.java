package me.snizzle.scrabble;


import me.snizzle.datastructure.Trie;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

//
public class ComputerPlayerTest {

    public static void main(String args[]){

        /*String test = "hello";
        StringBuilder test2 = new StringBuilder(test);
        long start = System.nanoTime();
        changeS(test2);
        //System.out.println(test + " --- "+ test2.toString());
        // ending time
        long end = System.nanoTime();
        System.out.println("Find Best move takes " +  (end - start) + "ns");


        System.out.println(test + " ---- " + test2.toString());*/

        BufferedReader fileReader = null;
        try {
            fileReader = new BufferedReader(new FileReader("resources/test1"));


            ScrabblePlayer comp;
            ScrabbleTileBag tileBag = new ScrabbleTileBag();
            ScrabbleRules rules = new ScrabbleRules();
            ScrabbleBoard board = new ScrabbleBoard(fileReader, rules);
            String trayRep = fileReader.readLine();
            board.printBoard();
            //System.out.println(trayRep);
            comp = new ScrabbleCompPlayer20(board,tileBag,trayRep,rules);
            comp.takeTurn();
            board.printBoard();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }


    }

    static public void changeS(StringBuilder s){
        s.insert(0,"W");
        //System.out.println(s);
    }
}
