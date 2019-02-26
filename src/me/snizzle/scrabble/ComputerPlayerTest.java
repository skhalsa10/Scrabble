package me.snizzle.scrabble;


import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

//
public class ComputerPlayerTest {

    public static void main(String args[]){
        HashMap<ScrabbleBoardPoint, ScrabbleTile> test = new HashMap<>();
        test.put(new ScrabbleBoardPoint(1,1), new ScrabbleTile('f',3));
        test.put(new ScrabbleBoardPoint(0,1), new ScrabbleTile('t',3));
        test.put(new ScrabbleBoardPoint(0,0), new ScrabbleTile('w',3));
        Iterator points = test.keySet().iterator();
        while(points.hasNext()){
            ScrabbleBoardPoint p = (ScrabbleBoardPoint) points.next();

            System.out.println(test.keySet().size());
            points.remove();


            System.out.println(test.keySet().size());
            System.out.println(test.containsKey(p));
        }


    }
}
