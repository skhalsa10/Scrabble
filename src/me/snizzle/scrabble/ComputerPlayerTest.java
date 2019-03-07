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

        if(args.length==1){
            System.out.println(args[0]);
        }

        BufferedReader fileReader = null;
        try {
            fileReader = new BufferedReader(new FileReader("resources/test.txt"));
            String s = fileReader.readLine();
            while(s != null) {
                ScrabblePlayer comp;
                ScrabbleTileBag tileBag = new ScrabbleTileBag();
                ScrabbleRules rules = new ScrabbleRules();
                ScrabbleBoard board = new ScrabbleBoard(fileReader, rules);
                String trayRep = fileReader.readLine();
                comp = new ScrabbleCompPlayer20(board, tileBag, trayRep, rules);
                comp.takeTurn();
                System.out.println("SCORE: " + comp.getScore());
                board.printBoard();
                s = fileReader.readLine();
            }
            fileReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }

    }

}
