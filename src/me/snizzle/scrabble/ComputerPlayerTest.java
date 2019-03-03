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
            comp = new ScrabbleCompPlayer(board,tileBag,trayRep,rules);
            comp.takeTurn();
            board.printBoard();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }


    }
}
