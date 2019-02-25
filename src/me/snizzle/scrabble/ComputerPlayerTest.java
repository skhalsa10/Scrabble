package me.snizzle.scrabble;


import java.util.HashMap;

//
public class ComputerPlayerTest {

    public static void main(String args[]){
        ScrabbleTile test = new ScrabbleTile('s',1);
        ScrabbleTile test2 = test.clone();

        System.out.println(test == test2);
        System.out.println(test.equals(test2));


    }
}
