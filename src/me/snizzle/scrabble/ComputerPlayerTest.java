package me.snizzle.scrabble;



//
public class ComputerPlayerTest {

    public static void main(String args[]){
        ScrabbleTileBag tileBag = new ScrabbleTileBag();
        ScrabbleTileTray tileTray = new ScrabbleTileTray(tileBag);
        System.out.println((tileTray.toArray()).length);
        System.out.println(tileBag.size());
    }
}
