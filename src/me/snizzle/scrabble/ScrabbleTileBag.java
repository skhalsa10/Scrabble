package me.snizzle.scrabble;

import java.util.Collections;
import java.util.LinkedList;

/**
 * this will generate the 100 tiles and place them in the tile bag  this will be a wrapper for a collection.
 * most likely just a LinkedList. The letters will be scrambled  upon creation and the draw will just pick the
 * first tile in the list.
 *
 * I will build it off of this info I found on Wikipedia.
 *     2 blank tiles (scoring 0 points)
 *     1 point: E ×12, A ×9, I ×9, O ×8, N ×6, R ×6, T ×6, L ×4, S ×4, U ×4
 *     2 points: D ×4, G ×3
 *     3 points: B ×2, C ×2, M ×2, P ×2
 *     4 points: F ×2, H ×2, V ×2, W ×2, Y ×2
 *     5 points: K ×1
 *     8 points: J ×1, X ×1
 *     10 points: Q ×1, Z ×1
 */
public class ScrabbleTileBag {
    private LinkedList<ScrabbleTile> tileBag;

    public ScrabbleTileBag(){
        tileBag = initBag();
    }

    /**
     * this is horrible and messy due to the different inpu. AHHH SO MANY LINES OF CODE EWWW.
     * @return a shuffled Linked list of tiles
     */
    private LinkedList<ScrabbleTile> initBag() {
        LinkedList<ScrabbleTile> tempBag = new LinkedList<>();

        //add blanks
        tempBag.add(new ScrabbleTile(' ', 0));
        tempBag.add(new ScrabbleTile(' ', 0));

        //add the Es
        for(int i = 0; i<12;i++){
            tempBag.add(new ScrabbleTile('E', 1));
        }
        //add the As
        for(int i = 0; i<9;i++){
            tempBag.add(new ScrabbleTile('A', 1));
        }
        //add the Is
        for(int i = 0; i<9;i++){
            tempBag.add(new ScrabbleTile('I', 1));
        }
        //add the Os
        for(int i = 0; i<8;i++){
            tempBag.add(new ScrabbleTile('O', 1));
        }
        //add the Ns
        for(int i = 0; i<6;i++){
            tempBag.add(new ScrabbleTile('N', 1));
        }
        //add the Rs
        for(int i = 0; i<6;i++){
            tempBag.add(new ScrabbleTile('R', 1));
        }
        //add the Ts
        for(int i = 0; i<6;i++){
            tempBag.add(new ScrabbleTile('T', 1));
        }
        //add the Ls
        for(int i = 0; i<4;i++){
            tempBag.add(new ScrabbleTile('L', 1));
        }
        //add the Ss
        for(int i = 0; i<4;i++){
            tempBag.add(new ScrabbleTile('S', 1));
        }
        //add the Us
        for(int i = 0; i<4;i++){
            tempBag.add(new ScrabbleTile('U', 1));
        }
        //add the 2 point letters
        //add the Ds
        for(int i = 0; i<4;i++){
            tempBag.add(new ScrabbleTile('D', 2));
        }
        //add the Gs
        for(int i = 0; i<3;i++){
            tempBag.add(new ScrabbleTile('G', 2));
        }
        //add the 3 points
        tempBag.add(new ScrabbleTile('B', 3));
        tempBag.add(new ScrabbleTile('B', 3));
        tempBag.add(new ScrabbleTile('C', 3));
        tempBag.add(new ScrabbleTile('C', 3));
        tempBag.add(new ScrabbleTile('M', 3));
        tempBag.add(new ScrabbleTile('M', 3));
        tempBag.add(new ScrabbleTile('P', 3));
        tempBag.add(new ScrabbleTile('P', 3));

        //add the 4 pointers
        tempBag.add(new ScrabbleTile('F', 4));
        tempBag.add(new ScrabbleTile('F', 4));
        tempBag.add(new ScrabbleTile('H', 4));
        tempBag.add(new ScrabbleTile('H', 4));
        tempBag.add(new ScrabbleTile('V', 4));
        tempBag.add(new ScrabbleTile('V', 4));
        tempBag.add(new ScrabbleTile('W', 4));
        tempBag.add(new ScrabbleTile('W', 4));
        tempBag.add(new ScrabbleTile('Y', 4));
        tempBag.add(new ScrabbleTile('Y', 4));

        //add the only 5 pointer
        tempBag.add(new ScrabbleTile('K', 5));

        //8 pointer
        tempBag.add(new ScrabbleTile('J', 8));
        tempBag.add(new ScrabbleTile('X', 8));

        //10 pointer
        tempBag.add(new ScrabbleTile('Q', 10));
        tempBag.add(new ScrabbleTile('Z', 10));

        Collections.shuffle(tempBag);

        return tempBag;
    }

    /**
     *
     * @return the next tile in a scrambled bag of tiles. returns NULL is the bag is empty
     */
    public ScrabbleTile draw(){
        return tileBag.pollFirst();
    }

    public boolean isEmpty(){
        return tileBag.isEmpty();
    }

    /**
     * returns the amount of tiles left in the bag
     * @return the amount of tiles left in the bag as an int
     */
    public int size(){
        return tileBag.size();
    }
}
