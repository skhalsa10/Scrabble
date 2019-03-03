package me.snizzle.scrabble;



import java.util.*;

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
    private ScrabbleWords dictionary;
    //private ScrabbleBoard board;

    public ScrabbleRules(){
        this("resources/twl06.txt");
    }

    public ScrabbleRules(String fileName){
        initCharMaps();
        dictionary = new ScrabbleWords(fileName);

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

    /**
     * this will assume that the tiles being placed on the board are valid and are contained in the tray.
     *
     * the purpose of this method is to check if the given move can be played on the board.
     *
     * this will check both the validity of where the tiles are placed and all words that are created  are real words
     *
     * @param currentMove to check
     * @param board the board to check the move against.
     * @return true or false accordingly.
     */
    public boolean isMoveValid(HashMap<ScrabbleBoardPoint, ScrabbleTile> currentMove, ScrabbleBoard board) {

        //printCurrentMove(currentMove);

        //lets first confirm that none of of the points already have tiles on the board.
        for (ScrabbleBoardPoint p: currentMove.keySet()) {
            //if there is a tile on the board already then this is an illegal move return false
           if(board.readTileAt(p.getRow(),p.getCol()) != null){return false;}
        }
        //System.out.println("pass 1");
        //if first move must be in the middle and have at least two tiles
        if(isFirstMove(board)){
            if(!isFirstMoveLegal(currentMove.keySet(), board.getBoardSize())){
                return false;
            }
        }
        //System.out.println("pass 2");
        //must touch an already played tile
        if(!currentMoveTouchesTile(currentMove.keySet(),board)){
            return false;
        }
        //System.out.println("pass 3");

        //lets get the main direction of the word
        Direction currentDirection = calcDirection(currentMove.keySet());

        //diagnal moves are illegal return false
        if (currentDirection == Direction.DIAG || currentDirection == Direction.ERROR){
            return false;
        }
        //System.out.println("pass 4");

        //get set of of words represented by points
        HashSet<ArrayList<ScrabbleBoardPoint>> wordPoints = getWordPointsSet(currentMove, board, currentDirection);

        //make sure all the words are actual words in the dictionary
        if(!wordsInDictionary(wordPoints, board, currentMove)){
            return false;
        }

        //System.out.println("pass 5");
        return true;
    }

    /**
     * this will assume the currentMove is valid and calculate the total score.
     * @param currentMove valid move to calculate score
     * @param board board to reference to calculate against
     * @return int representing the score.
     */
    public int calcScore(HashMap<ScrabbleBoardPoint, ScrabbleTile> currentMove, ScrabbleBoard board){
        //TODO need to fix this method to take into account the following:
        // TODO Letter and word premiums count only on the turn in which they are played. On later
        //TODO turns, letters already played on premium squares count at face value.
        int total = 0;
        boolean isBingo = false;

        //if all 7 tiles are played we have a bingo
        if(currentMove.keySet().size() ==7){
            isBingo = true;
        }

        //convert to set of words represented by a list of points
        HashSet<ArrayList<ScrabbleBoardPoint>> words =
                getWordPointsSet(currentMove, board, calcDirection(currentMove.keySet()));

        //for each word add the score to the total
        for (ArrayList<ScrabbleBoardPoint> word: words ) {
            total += calcWordScore(currentMove, word, board);
        }

        if(isBingo){
            total += 50;
        }

        return total;
    }

    /**
     * given a word represented as points calculate the total score and return it
     * @param word word of points to calculate
     * @param board board to reference
     * @return total score of the word
     */
    private int calcWordScore(HashMap<ScrabbleBoardPoint, ScrabbleTile> currentMove,
                              ArrayList<ScrabbleBoardPoint> word, ScrabbleBoard board){
        int total = 0;
        ArrayList<ScrabbleBoardPoint> wordMultipliers = new ArrayList<>();
        //add up the letters and keeptrack of the word multipliers for later
        for (ScrabbleBoardPoint p:word) {
            //if the point references a word multiplier only add the value of the
            // tile add the point to wordMultipliers for later calculation
            if(board.valueIsWordMultAt(p.getRow(),p.getCol())){
                wordMultipliers.add(p);
                //if the point references a tile on the board add the tile value
                if(board.readTileAt(p.getRow(),p.getCol()) != null){
                    total += board.readTileAt(p.getRow(),p.getCol()).getPoints();
                }
                //the point better be in the current move if not on the board ... or we afre in trouble.
                else if(currentMove.containsKey(p)){
                    total += currentMove.get(p).getPoints();
                }
                else{
                    System.out.println("error in calcWordScore()");
                }
                //we are done with this iteration lets continue to the next
                continue;
            }
            //lets finish up this by assuming that it is not a word multiplier
            //if the tile is on the board add the points multiplied by the board value.
            if(board.readTileAt(p.getRow(),p.getCol())!= null){
                total += board.readTileAt(p.getRow(),p.getCol()).getPoints() *
                        board.getBoardValueAt(p.getRow(),p.getCol());
            }
            //if tile is not yet on the board it should be in the currentMove
            else if(currentMove.containsKey(p)){
                total += currentMove.get(p).getPoints() *
                        board.getBoardValueAt(p.getRow(),p.getCol());
            }
            //if it is not there we are screwed an error happened
            else{
                System.out.println("Error again in calcWordScore()");
            }
        }
        //now lets multiply the word  by the word multipliers :) chaching!
        for (ScrabbleBoardPoint p: wordMultipliers ) {
            total *= (board.getBoardValueAt(p.getRow(),p.getCol())%'w');
        }

        return total;
    }


    /**
     * prints the currentMove mostly for debugging
     * @param currentMove
     */
    private void printCurrentMove(HashMap<ScrabbleBoardPoint, ScrabbleTile> currentMove) {
        for (ScrabbleBoardPoint p: currentMove.keySet() ) {
            System.out.println("r: " + p.getRow() + " c: " + p.getCol() + " -> " + currentMove.get(p).readTile());
        }
    }

    /**
     * this method checks to see if the hashset of list of points representing words are real words in the dictionary
     * @param wordPoints hashset of list of points
     * @return true if all words are breal words in dictionary
     */
    private boolean wordsInDictionary(HashSet<ArrayList<ScrabbleBoardPoint>> wordPoints,
                                      ScrabbleBoard board,
                                      HashMap<ScrabbleBoardPoint, ScrabbleTile> currentMove) {

        ArrayList<String> wordsAsStrings = convertPointsToStrings(wordPoints, board, currentMove);
        for (String word: wordsAsStrings ) {
            //System.out.println(word);
            if(!dictionary.verify(word)){
                System.out.println(word + " is not a word");
                return false;
            }
            System.out.println(word + " is a word!!!");
        }

        return true;
    }

    /**
     * takes a hashset of a list of points representing words and returns a list of those words as strings
     * @param wordPoints
     * @param board
     * @return
     */
    private ArrayList<String> convertPointsToStrings(HashSet<ArrayList<ScrabbleBoardPoint>> wordPoints,
                                                     ScrabbleBoard board,
                                                     HashMap<ScrabbleBoardPoint, ScrabbleTile> currentMove) {
        ArrayList<String> temp = new ArrayList<>();
        for (ArrayList<ScrabbleBoardPoint> list: wordPoints) {
            StringBuilder string = new StringBuilder();
            for (ScrabbleBoardPoint p: list ) {
                //if the tile on the board is not null get the letter of it
                if(board.readTileAt(p.getRow(),p.getCol()) != null) {
                    //System.out.println(board.readTileAt(p.getRow(), p.getCol()).readTile());
                    string.append(board.readTileAt(p.getRow(),p.getCol()).readTile());
                }else if(currentMove.containsKey(p)){
                    //if it is null then it should be in the currentMove and point to a tile
                    string.append(currentMove.get(p).readTile());
                }
                else{
                    System.out.println("Error in convertPointsToStrings");
                }
            }
            temp.add(string.toString());

        }

        return temp;
    }

    /**
     * this method will return a hash set of an ArrayList of scrabble points representing words created from the current move
     * @param currentMove the current move used to figure out moves from
     * @param board the board to check the current move against.
     * @param direction the direction of all tiles
     * @return set of words represented by a list of points
     */
    private HashSet<ArrayList<ScrabbleBoardPoint>> 
    getWordPointsSet(HashMap<ScrabbleBoardPoint, ScrabbleTile> currentMove, ScrabbleBoard board, Direction direction) {
        HashSet<ArrayList<ScrabbleBoardPoint>> wordsSet = new HashSet<>();
        ArrayList<ScrabbleBoardPoint> word = new ArrayList<>();

        Iterator<ScrabbleBoardPoint> iterator = currentMove.keySet().iterator();
        ScrabbleBoardPoint p = iterator.next();
        //if the direction is horizontal get the horizontal word
        if(direction == Direction.HORIZ){
            word = getHorizontalWord(currentMove,p, board);
            wordsSet.add(word);

            //check to see if the first p has a vertical
            if(isVerticalWordEnd(p, board)){
                // since it is a vertical end lets calculate the word

                word = getVerticalWord(currentMove,p, board);
                wordsSet.add(word);
            }

            //after getting the horizontal word find vertical words that make new words on the rest of the points
            while(iterator.hasNext()){
                p = iterator.next();

                //check to see if the p is an end piece to a vertical word. if it is a middle piece it will not make a new word
                if(isVerticalWordEnd(p, board)){
                    // since it is a vertical end lets calculate the word

                    word = getVerticalWord(currentMove, p, board);
                    wordsSet.add(word);
                }
            }

            return wordsSet;
        }
        //lets to the same if
        if(direction == Direction.VERT){
            word = getVerticalWord(currentMove, p, board);
            wordsSet.add(word);

            //check to see if the first p makes up a horizontal word
            if(isHorizontalWordEnd(p,board)){
                word = getHorizontalWord(currentMove,p,board);
                wordsSet.add(word);
            }
            //after getting the main vertical word added check for any horizontal word ends and add those horizontal words
            while(iterator.hasNext()){
                p = iterator.next();

                //check to see if the first p makes up a horizontal word
                if(isHorizontalWordEnd(p,board)){
                    word = getHorizontalWord(currentMove,p,board);
                    wordsSet.add(word);
                }
            }
            return wordsSet;
        }

        //the last case we are dealing with is if we place a single tile and there is no direction
        //just check if it is a vertical or horizontal word end and get the words
        if(isVerticalWordEnd(p,board)){
            word = getVerticalWord(currentMove, p, board);
            wordsSet.add(word);
        }
        if(isHorizontalWordEnd(p,board)){
            word = getHorizontalWord(currentMove,p,board);
            wordsSet.add(word);
        }


        return wordsSet;
    }

    /**
     * will check to see if the given point represents a tile that  is the end of a horizontal word
     * @param p point to check against
     * @param board board to reference
     * @return true if p contains a tile that is the end piece of a horizontal word
     */
    private boolean isHorizontalWordEnd(ScrabbleBoardPoint p, ScrabbleBoard board) {
        //if on left edge
        if (p.getCol() == 0 && board.readTileAt(p.getRow(), p.getCol() + 1) != null) {
            return true;
        }
        //if left end
        if (board.readTileAt(p.getRow(), p.getCol() - 1) == null &&
                p.getCol() != board.getBoardSize()-1 &&
                board.readTileAt(p.getRow(), p.getCol() + 1) != null){

            return true;
        }
        //checking if it is a right end
        //on the edge
        if(p.getCol() == board.getBoardSize()-1 && board.readTileAt(p.getRow(),p.getCol()-1) != null){
            return true;
        }
        //not on edge
        if(board.readTileAt(p.getRow(),p.getCol() +1) ==null &&
                p.getCol() != 0 && board.readTileAt(p.getRow(),p.getCol()-1) != null){
            return true;
        }

        return false;

    }


    /**
     * this method will assume there is a vertical word containing the point p. it will return the points representing a the
     * vertical word containing the point
     * @param p point in the vertical word
     * @param board the board to check against
     * @return
     */
    private ArrayList<ScrabbleBoardPoint> getVerticalWord(HashMap<ScrabbleBoardPoint, ScrabbleTile> currentMove,
                                                          ScrabbleBoardPoint p, ScrabbleBoard board) {
        ArrayList<ScrabbleBoardPoint> word = new ArrayList<>();
        //must find the top most point
        ScrabbleBoardPoint p1 = getFirstVerticalP(currentMove, p, board);
        //okay now that p1 is the first point we can go down from there
        buildVerticalWord(word, p1, board, currentMove);
        return word;
    }

    /**
     * this takes a reference to an arraylist of points and build a list of points representing a vertical word
     * @param word builds a word at this refernce
     * @param p1 point to build down from
     * @param board board to refernce
     */
    private void buildVerticalWord(ArrayList<ScrabbleBoardPoint> word,
                                   ScrabbleBoardPoint p1, ScrabbleBoard board,
                                   HashMap<ScrabbleBoardPoint, ScrabbleTile> currentMove) {
        //lets add the point
        word.add(p1);
        //if this point happens to be the on the last row of the board return we have reached the end
        if(p1.getRow() == board.getBoardSize()-1){
            return;
        }
        //if there is no more tiles down return as we have reached the end of the word
        if(board.readTileAt(p1.getRow()+1, p1.getCol()) == null &&
                !currentMove.containsKey(new ScrabbleBoardPoint(p1.getRow()+1, p1.getCol()))){
            return;
        }
        //else finish building the word starting at the tile below
        buildVerticalWord(word, new ScrabbleBoardPoint(p1.getRow()+1, p1.getCol()),board, currentMove);
    }

    /**
     *     returns the top most point in a vertical word
     */
    private ScrabbleBoardPoint getFirstVerticalP(HashMap<ScrabbleBoardPoint, ScrabbleTile> currentMove,
                                                 ScrabbleBoardPoint p, ScrabbleBoard board) {
        //System.out.println("firstverticalp");
        if (p.getRow() == 0) {
            return p;
        }
        if (board.readTileAt(p.getRow() - 1, p.getCol()) == null &&
            !currentMove.containsKey(new ScrabbleBoardPoint(p.getRow() - 1, p.getCol()))) {
            return p;
        }
        return getFirstVerticalP(currentMove, new ScrabbleBoardPoint(p.getRow()-1, p.getCol()), board);
    }

    /**
     * returns true it the point p represent the end of a vertical word
     * @param p
     * @param board
     * @return
     */
    private boolean isVerticalWordEnd(ScrabbleBoardPoint p, ScrabbleBoard board) {
        //if at top of board
        if(p.getRow() == 0 && board.readTileAt(p.getRow()+1, p.getCol()) != null){
            return true;
        }
        // tile above is empty but tile below is not ... end piece
        if(board.readTileAt(p.getRow()-1, p.getCol()) == null &&
                p.getRow() != board.getBoardSize()-1 &&
                board.readTileAt(p.getRow()+1, p.getCol()) != null){
            return true;
        }

        //if at bottom of board
        if(p.getRow() == board.getBoardSize()-1 && board.readTileAt(p.getRow()-1, p.getCol()) != null){
            return true;
        }
        // tile below is empty but tile above is not ... end piece
        if(board.readTileAt(p.getRow()+1, p.getCol()) == null &&
                p.getRow() != 0 && board.readTileAt(p.getRow()-1, p.getCol()) != null){
            return true;
        }

        return false;

    }

    /**
     * this will return an ArrayList of points representing a horizontal word containing the point p2
     * @param p2 point contained oint he returned horizontal word
     * @param board board to reference
     * @return ArrayList of points representing a horizontal word containing the point p2
     */
    private ArrayList<ScrabbleBoardPoint> getHorizontalWord(HashMap<ScrabbleBoardPoint, ScrabbleTile> currentMove,ScrabbleBoardPoint p2, ScrabbleBoard board) {
        ArrayList<ScrabbleBoardPoint> word = new ArrayList<>();
        ScrabbleBoardPoint p1 = getFirstHorizontalP(currentMove,p2, board);
        buildHorizontalWord(word, p1, board, currentMove);
        return word;
    }

    /**
     * used with getHorizontalWord to build a word containing point p
     * @param word reference to a ArrayList of points that will be built with this method
     * @param p point in the word
     * @param board board to reference
     */
    private void buildHorizontalWord(ArrayList<ScrabbleBoardPoint> word,
                                     ScrabbleBoardPoint p, ScrabbleBoard board,
                                     HashMap<ScrabbleBoardPoint, ScrabbleTile> currentMove) {
        //add the p to the word
        word.add(p);
        //if we get to the edge of the board the word is over
        if(p.getCol() == board.getBoardSize()-1){
            return;
        }
        //if the next horizontal tile iss null the end of the word is found
        if(board.readTileAt(p.getRow(),p.getCol()+1) == null &&
                !currentMove.containsKey(new ScrabbleBoardPoint(p.getRow(),p.getCol()+1))){
            return;
        }
        //continue to build the word
        buildHorizontalWord(word, new ScrabbleBoardPoint(p.getRow(),p.getCol()+1), board,currentMove);
    }

    /**
     * Given any point p2 in a horizontal word. this method will return the leftmost first point in the horizontal word
     * @param p2 any point in the horizontal word.
     * @param board board used as reference
     * @return
     */
    private ScrabbleBoardPoint getFirstHorizontalP(HashMap<ScrabbleBoardPoint, ScrabbleTile> currentMove,
                                                   ScrabbleBoardPoint p2, ScrabbleBoard board) {

        //recursion endpoint where we reach the end of the board
        if(p2.getCol() == 0){
            return p2;
        }
       //crap I have a bug here I havent actually placed the tiles on the board so it has null I need to check if the current move contains the point
        if(board.readTileAt(p2.getRow(), p2.getCol()-1) == null &&
                !currentMove.containsKey(new ScrabbleBoardPoint(p2.getRow(), p2.getCol()-1))){
            return p2;
        }
        return getFirstHorizontalP(currentMove, new ScrabbleBoardPoint(p2.getRow(),p2.getCol()-1), board);

    }

    /**
     * the first move needs to be at least 2 letters and contain the center spot
     * @return
     * @param keySet
     */
    private boolean isFirstMoveLegal(Set<ScrabbleBoardPoint> keySet, int boardSize) {
        if(keySet.size()<2){
            return false;

        }
        for (ScrabbleBoardPoint p: keySet) {
            if(p.equals(new ScrabbleBoardPoint((int)boardSize/2, (int)boardSize/2))){
                return true;
            }
        }
        return false;
    }

    /**
     *
     * @return returns true if the center of the board is empty
     */
    private boolean isFirstMove(ScrabbleBoard board) {
        if(board.readTileAt(board.getBoardSize()/2, board.getBoardSize()/2) == null){
            return true;
        }

        return false;
    }

    /**
     * this will return true if ANY tile in the move touches a current tile already played on the board
     * @param keySet points in the move to check if there are tiles touching around it
     * @param board to check against
     * @return true if any tile touches a played tile on the board flase otherwise
     */
    private boolean currentMoveTouchesTile(Set<ScrabbleBoardPoint> keySet, ScrabbleBoard board) {
        //if this is the first move? cant touch tiles then just return true
        if(board.readTileAt(board.getBoardSize()/2,board.getBoardSize()/2)== null){
            return true;
        }

        for (ScrabbleBoardPoint p: keySet) {
            //check the left side
            if(p.getCol()-1 >=0 && board.readTileAt(p.getRow(),p.getCol()-1) != null){
                return true;
            }
            //check right side
            if(p.getCol()+1 < board.getBoardSize() && board.readTileAt(p.getRow(),p.getCol()+1) != null){
                return true;
            }
            //check top side
            if(p.getRow()-1 >= 0 && board.readTileAt(p.getRow()-1,p.getCol()) != null){
                return true;
            }
            //check bottom side
            if(p.getRow()+1 < board.getBoardSize() && board.readTileAt(p.getRow()+1,p.getCol()) != null){
                return true;
            }

        }

        return false;
    }

    /**
     * this method will return the direction of the move. NONE means it is a single tile with no direction.
     * this is a legal move. Horizontal and vertical are Legal. and ... ERROR and Diagnal are illegal
     * @param keySet sert of board points being played
     * @return Direction
     */
    private Direction calcDirection(Set<ScrabbleBoardPoint> keySet) {
        // only one of these can be true by the end
        boolean isHorizontal = true;
        boolean isVertical = true;
        //if no tiles are played this is an error
        if (keySet.size() == 0) {
            return Direction.ERROR;
        }
        //one tile is valid
        if (keySet.size() == 1) {
            return Direction.NONE;
        }
        //if more than one tile either all Rows are equal for horizontal or all Col are equal for vertical
        if(keySet.size() >= 2){
            Iterator<ScrabbleBoardPoint> iterator = keySet.iterator();
            ScrabbleBoardPoint p1 = iterator.next();
            while(iterator.hasNext()) {
                ScrabbleBoardPoint p2 = iterator.next();
                //if a row is not the same it cant be horizontal
                if (p1.getRow() != p2.getRow()) {
                    isHorizontal = false;
                }
                //if col is not the same it cannot be vertical
                if (p1.getCol() != p2.getCol()) {
                    isVertical = false;
                }
                p1 = p2;
            }
        }
        if(isHorizontal && !isVertical){return Direction.HORIZ;}
        if(!isHorizontal && isVertical){return Direction.VERT;}
        return Direction.ERROR;
    }

    /**
     * represents the direction of a word
     */
    enum Direction {
        HORIZ, VERT, DIAG, NONE, ERROR
    }
}
