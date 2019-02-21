package me.snizzle.scrabble;

import me.snizzle.datastructure.Trie;

import java.util.HashMap;

/**
 * these rules will be used as a rule utility almost. these rules will be for the standard scrabble. if different
 * versions eventually get created it may be better to turn this into an interface that can be implented. but will do that later
 * if needed.
 *
 * the rules will use the following scrabble tile info:
 *
 * * 0 2
 * e 1 12
 * a 1 9
 * i 1 9
 * o 1 8
 * n 1 6
 * r 1 6
 * t 1 6
 * l 1 4
 * s 1 4
 * u 1 4
 * d 2 4
 * g 2 3
 * b 3 2
 * c 3 2
 * m 3 2
 * p 3 2
 * f 4 2
 * h 4 2
 * v 4 2
 * w 4 2
 * y 4 2
 * k 5 1
 * j 8 1
 * x 8 1
 * q 10 1
 * z 10 1
 *
 *
 *
 * @author Siri Khalsa
 * @version 1
 */
public class ScrabbleRules {

    private HashMap<Character,Integer> charPoints;
    private HashMap<Character,Integer> charCount;
    private boolean isFirstMove;
    private ScrabbleWords dictionary;

    public ScrabbleRules(){
        this("twl06.txt");
    }

    public ScrabbleRules(String fileName){
        initCharMaps();
        dictionary = new ScrabbleWords(fileName);
        isFirstMove = true;
    }

    /**
     * if you want to know what the standard point value is for a specific char
     * @param c the char to check for point value. * is the blank
     * @return the standard tile point value for that char. will return -1 if invalid input.
     */
    public int standardCharPoints(char c){
        c = Character.toLowerCase(c);
        if((c != '*' ) && !(c >='a' && c <= 'z')){return -1;}
        return charPoints.get(c);
    }

    /**
     * if you want to know what the standard count of tiles with c character
     * @param c the char to check for counts of. * is the blank tile
     * @return the standard count of c character tiles. will return -1 if invalid input.
     */
    public int standardCharCount(char c){
        c = Character.toLowerCase(c);
        if((c != '*' ) && !(c >='a' && c <= 'z')){return -1;}
        return charCount.get(c);
    }

    private void initCharMaps() {
        charPoints = new HashMap<>();
        charPoints.put('*',0);
        charPoints.put('e',1);
        charPoints.put('a',1);
        charPoints.put('i',1);
        charPoints.put('o',1);
        charPoints.put('n',1);
        charPoints.put('r',1);
        charPoints.put('t',1);
        charPoints.put('l',1);
        charPoints.put('s',1);
        charPoints.put('u',1);
        charPoints.put('d',2);
        charPoints.put('g',2);
        charPoints.put('b',3);
        charPoints.put('c',3);
        charPoints.put('m',3);
        charPoints.put('p',3);
        charPoints.put('f',4);
        charPoints.put('h',4);
        charPoints.put('v',4);
        charPoints.put('w',4);
        charPoints.put('y',4);
        charPoints.put('k',5);
        charPoints.put('j',8);
        charPoints.put('x',8);
        charPoints.put('q',10);
        charPoints.put('z',10);

        charCount = new HashMap<>();
        charCount.put('*',2);
        charCount.put('e',12);
        charCount.put('a',9);
        charCount.put('i',9);
        charCount.put('o',8);
        charCount.put('n',6);
        charCount.put('r',6);
        charCount.put('t',6);
        charCount.put('l',4);
        charCount.put('s',4);
        charCount.put('u',4);
        charCount.put('d',4);
        charCount.put('g',3);
        charCount.put('b',2);
        charCount.put('c',2);
        charCount.put('m',2);
        charCount.put('p',2);
        charCount.put('f',2);
        charCount.put('h',2);
        charCount.put('v',2);
        charCount.put('w',2);
        charCount.put('y',2);
        charCount.put('k',1);
        charCount.put('j',1);
        charCount.put('x',1);
        charCount.put('q',1);
        charCount.put('z',1);

    }
}
