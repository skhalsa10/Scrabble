package me.snizzle.scrabble;



//
public class ComputerPlayerTest {

    public static void main(String args[]){
        ScrabbleTile test1 = new ScrabbleTile('f',10);
        ScrabbleTile test1clone = test1.clone();

        System.out.println(test1 + " " + test1.readTile() + " " + test1.getPoints());
        System.out.println(test1clone + " " + test1clone.readTile() + " " + test1clone.getPoints());
        System.out.println(test1 + " and " + test1clone + " are equal? " + test1.equals(test1clone));

        System.out.println("are they the same object? " + (test1 ==test1clone));

    }
}
