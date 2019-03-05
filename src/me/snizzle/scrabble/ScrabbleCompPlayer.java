package me.snizzle.scrabble;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class ScrabbleCompPlayer extends ScrabblePlayer {

    public HashMap<ScrabbleBoardPoint, ScrabbleTile> bestMove = new HashMap<>();
    public int bestMoveScore = 0;

    public ScrabbleCompPlayer(ScrabbleBoard board, ScrabbleTileBag tileBag, ScrabbleRules rules) {
        super(board, tileBag, rules);
    }

    public ScrabbleCompPlayer(ScrabbleBoard board, ScrabbleTileBag tileBag, String trayAsString, ScrabbleRules rules){
        super(board, tileBag, trayAsString, rules);
    }

    @Override
    public boolean takeTurn() {
        //TODO may need to erase this later
        this.blankPoints = new ArrayList<>();
        /*for(ScrabbleTile t:tileTray.toArray()){
            System.out.println(t.readTile());
        }*/

        //find the best move computers version of getting a move instead of using gui
        //bestMove = findBestMoveSlow();
        bestMove = new HashMap<>();
        bestMoveScore = 0;
        findBestMove();
        //printMove(bestMove);
        System.out.println("SCORE: " + bestMoveScore);

        //cache the move like we do for the human player i know it works already
        cacheMove(bestMove);
        System.out.println("computer in da house");
        System.out.println(bestMove.size());
        //return false if it is a bad move. it is pretty much guarunteed to be valid as valid checks are conducted
        //in find best move
        if(!checkCachedMoveValid()){

            return false;
        }
        System.out.println("computer move passed the chace check");
        //commit the move to the board
        if(!approveMove()){
            return false;
        }

        System.out.println("computer move passed");
        tileTray.fillTray();

        return true;
    }

    /**
     * my other algorithm was horrible I am trying a new one from scratch I wasted so much time on the other one.
     * @return
     */
    private void findBestMove() {
        //i will get a list of points representing tiles on the board
        ArrayList<ScrabbleBoardPoint> playedPoints = board.getListPlayedTiles();


        //HORIZONTAL CALCULATIONNS
        //I will take this list and filter them to only include the points I should use to check for horizontal moves
        HashSet<ScrabbleBoardPoint> hPointsToCheck = filterHPoints(playedPoints);

        for (ScrabbleBoardPoint p:hPointsToCheck ) {
            //getMaxHMove(p,"",new HashMap<>(),tileTray.getCopy());
        }

        //VERTICAL CALCULATIONS
        //HashSet<ScrabbleBoardPoint> vPointsToCheck = filterVPoints(playedPoints);
        HashSet<ScrabbleBoardPoint> vPointsToCheck = new HashSet<>();
        vPointsToCheck.add(new ScrabbleBoardPoint(6,10));
        //printPoints(vPointsToCheck);
        for (ScrabbleBoardPoint p:vPointsToCheck ) {
            getMaxVMove(p,"",new HashMap<>(),tileTray.getCopy());
        }

    }

    private void printPoints(HashSet<ScrabbleBoardPoint> vPointsToCheck) {
        for (ScrabbleBoardPoint p: vPointsToCheck ) {
            System.out.println("r-" + p.getRow() + " c-"+p.getCol());
        }
    }

    private boolean getMaxVMove(ScrabbleBoardPoint p, String word,
                             HashMap<ScrabbleBoardPoint, ScrabbleTile> move,
                             ArrayList<ScrabbleTile> tiles){

        //this is the closing case for the recurssion
        if(tiles.size() == 0){
            //System.out.println(move.size());
           // if(rules.dictContains(word)){
                if(rules.isMoveValid(move,board)){
                    int moveScore = rules.calcScore(move,board);
                    if(moveScore> bestMoveScore){
                        bestMoveScore = moveScore;
                        bestMove = move;
                    }
                    return true;
                }
           // }
            //if the dictionary does not contain the word... or if
            // it does but the move is not valid return false
            return false;
        }
        else if(tiles.size()>0){

            //find the open points
            ScrabbleBoardPoint bottomOpen = null;
            ScrabbleBoardPoint topOpen;
            //we do not want to was time adding tiles on the rightside if the current word is not a prefix of something
            //if(rules.dictIsPrefix(word)){
                bottomOpen = getBottomOpenP(p,move);
            //}
            //we dont have an isSuffix so we have to brute force the top side
            topOpen = getTopOpenP(p,move);

            boolean top = false;
            boolean bottom = false;

            //NOW THAT WE HAVE OPENS we can loop through and reduce the recursion accordingly
            for (ScrabbleTile t:tiles ) {
                //if both are null we cant add this tile anywhere so we reduce by the tile and
                // continue when it comes back to try with the next tile
                if(topOpen == null && bottomOpen == null){
                    return getMaxVMove(p,word,copyMove(move),copyTilesWithNoT(tiles,t));
                }

                //at least one of the open points is valid
                //if it is the right side
                if(topOpen == null){
                    bottom = getMaxVMove(bottomOpen,(word+t.readTile()),copyMoveAndAdd(move,bottomOpen,t),copyTilesWithNoT(tiles,t));
                    continue;
                }

                //if it is the left side that is valid
                if(bottomOpen == null){
                    top = getMaxVMove(topOpen,(t.readTile()+word),copyMoveAndAdd(move,topOpen,t),copyTilesWithNoT(tiles,t));
                    continue;
                }

                //System.out.println(leftOpen);
                top =  getMaxVMove(topOpen,(t.readTile()+word),copyMoveAndAdd(move,topOpen,t),copyTilesWithNoT(tiles,t)) || top;
                bottom = getMaxVMove(bottomOpen,(word+t.readTile()),copyMoveAndAdd(move,bottomOpen,t),copyTilesWithNoT(tiles,t)) || bottom;
                //if we get this far both sides are valid.

            }

            while(!tiles.isEmpty()){

                getMaxVMove(p,word,move,copyTilesWithNoT(tiles,tiles.get(0)));
                tiles.remove(0);
            }

        }
        else{
            System.out.println("we should never print this!!!!");
            return false;
        }
        return true;

    }

    /**
     * filters the played points to include V points to make moves againts
     * @param playedPoints
     * @return
     */
    private HashSet<ScrabbleBoardPoint> filterVPoints(ArrayList<ScrabbleBoardPoint> playedPoints) {
        HashSet<ScrabbleBoardPoint> set = new HashSet<>();

        for (ScrabbleBoardPoint p: playedPoints ) {
            set.add(getTopPoint(p));
            if(p.getCol() != 0 && board.readTileAt(p.getRow(), p.getCol()-1) == null) {
                //if (isSpecialH(set, new ScrabbleBoardPoint(p.getRow()-1, p.getCol()))){
                set.add(new ScrabbleBoardPoint(p.getRow(), p.getCol()-1));
                //}
            }
            if(p.getCol() != board.getBoardSize()-1 && board.readTileAt(p.getRow(), p.getCol()+1) == null) {
                //if (isSpecialH(set, new ScrabbleBoardPoint(p.getRow()-1, p.getCol()))){
                set.add(new ScrabbleBoardPoint(p.getRow(), p.getCol()+1));
                //}
            }
        }

        return set;
    }

    /**
     * return the first point in a verticle set of tiles in arow. the top point is returned
     * @param p
     * @return
     */
    private ScrabbleBoardPoint getTopPoint(ScrabbleBoardPoint p) {
        if(board.readTileAt(p.getRow(),p.getCol()) == null){
            System.out.println("error this should not happen");
            return null;
        }
        //recursion endpoint where we reach the end of the board
        if(p.getRow() == 0){
            return p;
        }
        //if p is the end
        if(board.readTileAt(p.getRow()-1, p.getCol()) == null ){
            return p;
        }
        return getTopPoint(new ScrabbleBoardPoint(p.getRow()-1,p.getCol()));
    }

    /**
     *
     * I am rewriting this method for the millionth time with assumptions to make it easier.
     *
     * lets assume if a p is given that represents a blank than it is an open :)
     *
     * @param p
     * @param move
     * @return
     */
    private ScrabbleBoardPoint getBottomOpenP(ScrabbleBoardPoint p, HashMap<ScrabbleBoardPoint, ScrabbleTile> move) {

        //if we get a p off of the board to the bottom
        if(p.getRow() >= board.getBoardSize()){
            return null;
        }

        //if p represents a blank
        if(!move.containsKey(p) && board.readTileAt(p.getRow(),p.getCol())== null){
            return p;
        }


        //check the p below this one
        return getBottomOpenP(new ScrabbleBoardPoint(p.getRow()+1,p.getCol()),move);
    }


    /**
     * I am writing this method again because it is harder than it should be haha. I am now writing it
     * making some assumptions that  if i am given a p representing a blank it is an open
     * @param p
     * @param move
     * @return
     */
    private ScrabbleBoardPoint getTopOpenP(ScrabbleBoardPoint p,
                                           HashMap<ScrabbleBoardPoint, ScrabbleTile> move) {
        //if p is off the board to the top return null
        if(p.getRow() <0){
            return null;
        }

        //if p represents a blank
        if(!move.containsKey(p)&& board.readTileAt(p.getRow(),p.getCol())==null){
            return p;
        }

        //check tile above
        return getTopOpenP(new ScrabbleBoardPoint(p.getRow()-1,p.getCol()),move);
    }

    /**
     * will this be any faster than I already have!? with the slow version that barely works.
     * instead of returning null when I reach the bottom case I update the a global best move accourdingling.
     * i now dont have to reach the end of the reccursion and come back and do something.
     * @param p
     * @param word
     * @param move
     * @param tiles
     * @return
     */
    private boolean getMaxHMove(ScrabbleBoardPoint p, String word,
                                HashMap<ScrabbleBoardPoint, ScrabbleTile> move,
                                ArrayList<ScrabbleTile> tiles) {

        //okay hopefull this is faster I think checking is my move is valid costs alot so I will build up a string instead
        //if the dictionary contains the word. it is worth checking to see if the move is valid. the chances are higher
        if(tiles.size() ==0){
            //if(rules.dictContains(word)){
                if(rules.isMoveValid(move,board)){
                    System.out.println("do I get here?");
                    int moveScore = rules.calcScore(move,board);
                    if(moveScore> bestMoveScore){
                        bestMoveScore = moveScore;
                        bestMove = move;
                    }
                    return true;
             //   }
            }
            //if the dictionary does not contain the word... or if
            // it does but the move is not valid return false
            return false;
        }
        else if(tiles.size()>0){

            //find the open points
            ScrabbleBoardPoint rightOpen = null;
            ScrabbleBoardPoint leftOpen;
            //we do not want to was time adding tiles on the rightside if the current word is not a prefix of something
            if(rules.dictIsPrefix(word)){
                rightOpen = getRightOpenP(p,move);
            }
            //we dont have an isSuffix so we have to brute force the left side
            leftOpen = getLeftOpenP(p,move);

            boolean left = false;
            boolean right = false;

            //NOW THAT WE HAVE OPENS we can loop through and reduce the recursion accordingly
            for (ScrabbleTile t:tiles ) {
                //if both are null we cant add this tile anywhere so we reduce by the tile and
                // continue when it comes back to try with the next tile
                if(leftOpen == null && rightOpen == null){
                    return getMaxHMove(p,word,copyMove(move),copyTilesWithNoT(tiles,t));
                    //this may cause the move to be checked multiple times but oh well
                    //TODO i might be able to break this loop at this point actually COME BACK AND TRY

                }

                //at least one of the open points is valid
                //if it is the right side
                if(leftOpen == null){
                    right = getMaxHMove(rightOpen,(word+t.readTile()),copyMoveAndAdd(move,rightOpen,t),copyTilesWithNoT(tiles,t));
                    continue;
                }

                //if it is the left side that is valid
                if(rightOpen == null){
                    left = getMaxHMove(leftOpen,(t.readTile()+word),copyMoveAndAdd(move,leftOpen,t),copyTilesWithNoT(tiles,t));
                    continue;
                }

                //System.out.println(leftOpen);
                left =  getMaxHMove(leftOpen,(t.readTile()+word),copyMoveAndAdd(move,leftOpen,t),copyTilesWithNoT(tiles,t)) || left;
                right = getMaxHMove(rightOpen,(word+t.readTile()),copyMoveAndAdd(move,rightOpen,t),copyTilesWithNoT(tiles,t)) || right;
                //if we get this far both sides are valid.

            }

            return left ||right;
        }
        else{
            System.out.println("we should never print this!!!!");
            return false;
        }

    }

    /**
     * creats a set of H point to build horizontal words against
     * @param playedPoints
     * @return
     */
    private HashSet<ScrabbleBoardPoint> filterHPoints(ArrayList<ScrabbleBoardPoint> playedPoints) {

        HashSet<ScrabbleBoardPoint> set = new HashSet<>();

        for (ScrabbleBoardPoint p: playedPoints ) {
            set.add(getLeftPoint(p));
            if(p.getRow() != 0 && board.readTileAt(p.getRow()-1, p.getCol()) == null) {
                //if (isSpecialH(set, new ScrabbleBoardPoint(p.getRow()-1, p.getCol()))){
                set.add(new ScrabbleBoardPoint(p.getRow()-1, p.getCol()));
                //}
            }
            if(p.getRow() != board.getBoardSize()-1 && board.readTileAt(p.getRow()+1, p.getCol()) == null) {
                //if (isSpecialH(set, new ScrabbleBoardPoint(p.getRow()-1, p.getCol()))){
                set.add(new ScrabbleBoardPoint(p.getRow()+1, p.getCol()));
                //}
            }
        }
        return set;
    }



    /**
     * give p return the left edge point containing p
     * @param p p cannot point to a null tile space
     * @return
     */
    private ScrabbleBoardPoint getLeftPoint(ScrabbleBoardPoint p) {
        if(board.readTileAt(p.getRow(),p.getCol()) == null){
            System.out.println("error this should not happen");
            return null;
        }
        //recursion endpoint where we reach the end of the board
        if(p.getCol() == 0){
            return p;
        }
        //if p is not the end
        if(board.readTileAt(p.getRow(), p.getCol()-1) == null ){
            return p;
        }
        return getLeftPoint(new ScrabbleBoardPoint(p.getRow(),p.getCol()-1));

    }

    /**
     *
     * this damn method is driving me fucking insane. lets make some assumptions that
     * the p is an open if it is blank this is working better!
     *
     *
     * @param p
     * @param move
     * @return the first open point on the board that doesnt have a tile to the right of p and null if there is no open tile to the right
     */
    private ScrabbleBoardPoint getRightOpenP(ScrabbleBoardPoint p, HashMap<ScrabbleBoardPoint, ScrabbleTile> move) {

        //if we get a p off the right edge of the board
        if(p.getCol() >= board.getBoardSize()){
            return null;
        }

        //if p represents an empty spot return p
        if(!move.containsKey(p) && board.readTileAt(p.getRow(),p.getCol())== null){
            return p;
        }

        return getRightOpenP(new ScrabbleBoardPoint(p.getRow(),p.getCol()+1),move);
    }

    /**
     * why is this so hard for me. lets make some assumptions that if p is a blank it is an end
     * @param p
     * @param move
     * @return
     */
    private ScrabbleBoardPoint getLeftOpenP(ScrabbleBoardPoint p,
                                            HashMap<ScrabbleBoardPoint, ScrabbleTile> move) {

        //if p is off the left edge return null
        if(p.getCol() < 0){
            return null;
        }

        //if p represents an empty spot
        if(!move.containsKey(p)&& board.readTileAt(p.getRow(),p.getCol()) == null){
            return p;
        }

        return getLeftOpenP(new ScrabbleBoardPoint(p.getRow(),p.getCol()-1), move);


    }

    private void printMove(HashMap<ScrabbleBoardPoint, ScrabbleTile> bestMove) {
        for (ScrabbleBoardPoint p: bestMove.keySet() ) {
            System.out.println("row: " + p.getRow() + " col: " + p.getCol() + " letter: " + bestMove.get(p).readTile());
        }
    }

    /**
     *
     * @return this will return the best possible move on the board
     */
    private HashMap<ScrabbleBoardPoint, ScrabbleTile> findBestMoveSlow() {

        HashMap<ScrabbleBoardPoint, ScrabbleTile> maxMove = new HashMap<>();
        int scoreMaxMove = 0;

        //first get a list of played tiles via their points
        ArrayList<ScrabbleBoardPoint> playedPoints = board.getListPlayedTiles();

        //init horizontal
        HashMap<ScrabbleBoardPoint, ScrabbleTile> bestHorizontalMove = new HashMap<>();
        int bestHorizontalScore = 0;

        //init vertical
        HashMap<ScrabbleBoardPoint, ScrabbleTile> bestVerticalMove = new HashMap<>();
        int bestVerticalScore = 0;

        /*ArrayList<ScrabbleTile> test = new ArrayList<>();
        test.add(new ScrabbleTile('T',8));
        test.add(new ScrabbleTile('o',10));
        test.add(new ScrabbleTile('l',10));
        test.add(new ScrabbleTile('o',8));
        test.add(new ScrabbleTile('e',10));
        test.add(new ScrabbleTile('r',10));*/

        //iterate over every point
        //TODO filter p down to only the left edge and top edge
        for (ScrabbleBoardPoint p: playedPoints ) {
            bestHorizontalMove = bestHorizontalMoveAt(p, tileTray.getCopy(),new HashMap<>());
            //bestHorizontalScore= rules.calcScore(bestHorizontalMove,board);

            bestVerticalMove = bestVerticalMoveAt(p, tileTray.getCopy(),new HashMap<>());
            //bestVerticalScore = rules.calcScore(bestVerticalMove,board);

           /* if(bestHorizontalScore>=bestVerticalScore && bestHorizontalScore>scoreMaxMove){
                maxMove = bestHorizontalMove;
                scoreMaxMove = bestHorizontalScore;
            }
            if(bestVerticalScore>bestHorizontalScore && bestVerticalScore>scoreMaxMove){
                maxMove = bestVerticalMove;
                scoreMaxMove = bestVerticalScore;
            }*/


        }

        if(bestVerticalMove == null){
            System.out.println("uhoh!");
        }


        return bestVerticalMove;
    }

    private void printPlayedPoints(ArrayList<ScrabbleBoardPoint> playedPoints) {
        for (ScrabbleBoardPoint p: playedPoints ) {
            System.out.println("r: " + p.getRow() + " c: " + p.getCol() + " -> " + board.readTileAt(p.getRow(),p.getCol()));
        }
    }


    /**
     * this will return the vertical move with the highest score at the point p
     * @param p point to find best move at
     * @param tiles tiles in hand to work with
     * @param move current move already played recursively needed
     * @return
     */
    private HashMap<ScrabbleBoardPoint, ScrabbleTile> bestVerticalMoveAt(ScrabbleBoardPoint p,
                                                                         ArrayList<ScrabbleTile> tiles,
                                                                         HashMap<ScrabbleBoardPoint, ScrabbleTile> move) {
        if(tiles == null) {
            System.out.println(tiles);
        }
        if(tiles.isEmpty()){
            //System.out.println("I got to the tray being empty");
            //if this is empty we just check if the move is valid
            if(rules.isMoveValid(move, board)){
                //System.out.println("this should run");
                printMove(move);
                return move;
            }else {

                return null;
            }
        }
        else if(tiles.size() == 1){
            //System.out.println("entering tile size 1 case");
            HashMap<ScrabbleBoardPoint, ScrabbleTile> moveT;
            HashMap<ScrabbleBoardPoint, ScrabbleTile> moveB;

            ScrabbleBoardPoint topOpen = getTopOpenP(p,move);
            ScrabbleBoardPoint bottomOpen = getBottomOpenP(p,move);

            //if there is no tiles to the and and bottom available check to see if the move is valid and return accordingly
            if(topOpen == null && bottomOpen == null){
                // we ignore this tile
                //System.out.println(p.getRow()+ " c: " + p.getCol());
                //System.out.println("1 tile top and bottom null");
                return bestVerticalMoveAt(p,new ArrayList<>(),copyMove(move));
            }
            //now do the other null cases
            if(bottomOpen == null ){
                return bestVerticalMoveAt(topOpen, new ArrayList<>(), copyMoveAndAdd(move,topOpen, tiles.get(0)));
            }
            if(topOpen == null){
                //System.out.println("col " + rightOpen.getCol() + "row " + rightOpen.getRow());
                return bestVerticalMoveAt(bottomOpen, new ArrayList<>(), copyMoveAndAdd(move,bottomOpen, tiles.get(0)));
            }

            if(topOpen== null || bottomOpen == null){
                System.out.println("error tile size1 case left and right open should not be null!");
            }

            //if we get this far we check to see which move returned has a the highest score
            moveT  = bestVerticalMoveAt(topOpen, new ArrayList<>(), copyMoveAndAdd(move,topOpen, tiles.get(0)));
            moveB = bestVerticalMoveAt(bottomOpen, new ArrayList<>(), copyMoveAndAdd(move,bottomOpen, tiles.get(0)));

            //if both sides result in null moves then we return null
            if(moveT == null && moveB == null){
                return null;
            }

            //if one case is null return the non null this will have a better score by being valid :)
            if(moveT == null){
                return moveB;
            }
            if(moveB == null){
                return moveT;
            }

            //if get this far both sides are valid get the scores and return the max
            int scoreT = rules.calcScore(moveT, board);
            int scoreB = rules.calcScore(moveB, board);

            if(scoreT >= scoreB){
                return moveT;
            } else{
                return moveB;
            }

        }
        else if(tiles.size()>1) {

            //System.out.println("got here! tile size greater than 1");
            //System.out.println(tiles.size());
            //if we get this far the tray of tiles is more than one and we loop through it keeping the max score
            //first lets get the first open left and right points
            HashMap<ScrabbleBoardPoint, ScrabbleTile> moveT = null;
            HashMap<ScrabbleBoardPoint, ScrabbleTile> moveB = null;
            HashMap<ScrabbleBoardPoint, ScrabbleTile> maxMove = move;

            ScrabbleBoardPoint topOpen = getTopOpenP(p, maxMove);
            ScrabbleBoardPoint bottomOpen = getBottomOpenP(p, maxMove);


            for (ScrabbleTile t : tiles) {
                //if both of these are null then it doesnt matter how many tiles we have we cant even play them
                if (topOpen == null && bottomOpen == null) {
                    //reduce
                    //HashMap<ScrabbleBoardPoint, ScrabbleTile> temp = bestVerticalMoveAt(p, copyTilesWithNoT(tiles, t), maxMove);
                    //System.out.println(temp);
                    return bestVerticalMoveAt(p, copyTilesWithNoT(tiles, t), copyMove(maxMove));
                }


                //in this case we can only build right
                if (topOpen == null) {
                    moveB = bestVerticalMoveAt(bottomOpen, copyTilesWithNoT(tiles, t), copyMoveAndAdd(maxMove, bottomOpen, t));
                    //System.out.println("t is null: " + moveB);
                    maxMove = getMaxMove(moveB, maxMove);
                    //continue to the next tile
                    continue;

                }
                //now do the case if we can only build left
                if (bottomOpen == null) {
                    moveT = bestVerticalMoveAt(topOpen, copyTilesWithNoT(tiles, t), copyMoveAndAdd(maxMove, topOpen, t));
                    //System.out.println("b is null?: " + moveB + " max " + maxMove);
                    //printMove(maxMove);
                    maxMove = getMaxMove(moveT, maxMove);
                    continue;

                }


                //if we get this far we need to check the max of all possibilites
                //System.out.println("after " +rightOpen);
                //get the best move if we place the tile t on the right open space
                moveB = bestVerticalMoveAt(bottomOpen, copyTilesWithNoT(tiles, t), copyMoveAndAdd(maxMove, bottomOpen, t));
                //get the best move if we decide to put the tile on the left open space
                moveT = bestVerticalMoveAt(topOpen, copyTilesWithNoT(tiles, t), copyMoveAndAdd(maxMove, topOpen, t));

                //if both best moves result in nulls continue to try the next tile.
                if (moveB == null && moveT == null) {
                    continue;
                }
                //if only the moveL returns something valid
                if (moveB == null) {
                    maxMove = getMaxMove(moveT, maxMove);
                    continue;
                }
                //if only the moveR returns somthing valid
                if (moveT == null) {
                    maxMove = getMaxMove(moveB, maxMove);
                    continue;
                }
                //if we get this far then we get the Maxmove of all 3
                maxMove = getMaxMove(getMaxMove(moveT, moveB), maxMove);

            }
            if(maxMove != null && !rules.isMoveValid(maxMove,board)){
                //System.out.println("the maxMOVE is not valid why are you sending it back?");
                maxMove = null;
            }

            return maxMove;
        }
        else {
            //System.out.println(tiles.size());
            System.out.println("this should never run!");
            return null;
        }

    }



    /**
     * I can ony think of salving this with recursion hopefully this has some recursion magic in it
     * @param p the point to find the best horizontal move against
     * @param tiles this will be a list of tiles to use to find the best move
     * @return the best horizontal move at p if there is no move it will return null
     */
    private HashMap<ScrabbleBoardPoint, ScrabbleTile> bestHorizontalMoveAt(ScrabbleBoardPoint p,
                                                                           ArrayList<ScrabbleTile> tiles,
                                                                           HashMap<ScrabbleBoardPoint, ScrabbleTile> move) {

        if(tiles == null) {
            System.out.println(tiles);
        }

        //the first edge case is if our list of tiles is empty there is no move return the move as best move
        if(tiles.isEmpty()){
            //System.out.println("I got to the tray being empty");
            //if this is empty we just check if the move is valid
            if(rules.isMoveValid(move, board)){
                return move;
            }else {

                return null;
            }
        }
        //now the edge case of 1 tile will return move with the max score or null if there isnt any
        else if(tiles.size() == 1){
            //System.out.println("entering tile size 1 case");
            HashMap<ScrabbleBoardPoint, ScrabbleTile> moveL;
            HashMap<ScrabbleBoardPoint, ScrabbleTile> moveR;

            ScrabbleBoardPoint leftOpen = getLeftOpenP(p,move);
            ScrabbleBoardPoint rightOpen = getRightOpenP(p,move);

            //if there is no tiles to the left or right available check to see if the move is valid and return accordingly
            if(leftOpen == null && rightOpen == null){
                // we ignore this tile
                return bestHorizontalMoveAt(p,new ArrayList<>(),move);
            }
            //now do the other null cases
            if(rightOpen == null ){
                return bestHorizontalMoveAt(leftOpen, new ArrayList<>(), copyMoveAndAdd(move,leftOpen, tiles.get(0)));
            }
            if(leftOpen == null){
                //System.out.println("col " + rightOpen.getCol() + "row " + rightOpen.getRow());
                return bestHorizontalMoveAt(rightOpen, new ArrayList<>(), copyMoveAndAdd(move,rightOpen, tiles.get(0)));
            }

            if(leftOpen== null || rightOpen == null){
                System.out.println("error tile size1 case left and right open should not be null!");
            }

            //if we get this far we check to see which move returned has a the highest score
            moveL  = bestHorizontalMoveAt(leftOpen, new ArrayList<>(), copyMoveAndAdd(move,leftOpen, tiles.get(0)));
            moveR = bestHorizontalMoveAt(rightOpen, new ArrayList<>(), copyMoveAndAdd(move,rightOpen, tiles.get(0)));

            //if both sides result in null moves then we return null
            if(moveL == null && moveR == null){
                return null;
            }
            //if one case is null return the non null this will have a better score by being valid :)
            if(moveL == null){
                return moveR;
            }
            if(moveR == null){
                return moveL;
            }
            //if get this far both sides are valid get the scores and return the max
            int scoreL = rules.calcScore(moveL, board);
            int scoreR = rules.calcScore(moveR, board);

            if(scoreL >= scoreR){
                return moveL;
            } else{
                return moveR;
            }

        }

        else if(tiles.size()>1) {

            //System.out.println("got here! tile size greater than 1");
            //System.out.println(tiles.size());
            //if we get this far the tray of tiles is more than one and we loop through it keeping the max score
            //first lets get the first open left and right points
            HashMap<ScrabbleBoardPoint, ScrabbleTile> moveL = null;
            HashMap<ScrabbleBoardPoint, ScrabbleTile> moveR = null;
            HashMap<ScrabbleBoardPoint, ScrabbleTile> maxMove = move;

            ScrabbleBoardPoint leftOpen = getLeftOpenP(p, maxMove);
            ScrabbleBoardPoint rightOpen = getRightOpenP(p, maxMove);


            for (ScrabbleTile t : tiles) {
                //if both of these are null then it doesnt matter how many tiles we have we cant even play them
                if (leftOpen == null && rightOpen == null) {
                    return bestHorizontalMoveAt(p, copyTilesWithNoT(tiles, t), maxMove);
                }


                //in this case we can only build right
                if (leftOpen == null) {
                    moveR = bestHorizontalMoveAt(rightOpen, copyTilesWithNoT(tiles, t), copyMoveAndAdd(maxMove, rightOpen, t));

                    maxMove = getMaxMove(moveR, maxMove);
                    //continue to the next tile
                    continue;

                }
                //now do the case if we can only build left
                if (rightOpen == null) {
                    moveL = bestHorizontalMoveAt(leftOpen, copyTilesWithNoT(tiles, t), copyMoveAndAdd(maxMove, leftOpen, t));

                    maxMove = getMaxMove(moveL, maxMove);
                    continue;

                }


                //if we get this far we need to check the max of all possibilites
                //System.out.println("after " +rightOpen);
                //get the best move if we place the tile t on the right open space
                moveR = bestHorizontalMoveAt(rightOpen, copyTilesWithNoT(tiles, t), copyMoveAndAdd(maxMove, rightOpen, t));
                //get the best move if we decide to put the tile on the left open space
                moveL = bestHorizontalMoveAt(leftOpen, copyTilesWithNoT(tiles, t), copyMoveAndAdd(maxMove, leftOpen, t));

                //if both best moves result in nulls continue to try the next tile.
                if (moveR == null && moveL == null) {
                    continue;
                }
                //if only the moveL returns something valid
                if (moveR == null) {
                    maxMove = getMaxMove(moveL, maxMove);
                    continue;
                }
                //if only the moveR returns somthing valid
                if (moveL == null) {
                    maxMove = getMaxMove(moveR, maxMove);
                    continue;
                }
                //if we get this far then we get the Maxmove of all 3
                maxMove = getMaxMove(getMaxMove(moveL, moveR), maxMove);


            }

            return maxMove;
        }
        else {
            //System.out.println(tiles.size());
            System.out.println("this should never run!");
            return null;
        }
    }

    /**
     * returns the move with the better score
     * @param move
     * @param maxMove
     * @return
     */
    private HashMap<ScrabbleBoardPoint, ScrabbleTile> getMaxMove(HashMap<ScrabbleBoardPoint, ScrabbleTile> move,
                                                                 HashMap<ScrabbleBoardPoint, ScrabbleTile> maxMove) {
        if(maxMove == null){
            maxMove = move;
        }
        if(move != null){
            int score = rules.calcScore(move, board);
            int maxScore = rules.calcScore(maxMove, board);
            if(score > maxScore){
                maxMove = move;
            }
        }
        return maxMove;
    }

    /**
     * this returns a copy of tiles with out Tile t
     * @param tiles makes a deep copy of these tiles
     * @param t removes this t from the copy returned
     * @return copy of tiles without T
     */
    private ArrayList<ScrabbleTile> copyTilesWithNoT(ArrayList<ScrabbleTile> tiles, ScrabbleTile t) {
        ArrayList<ScrabbleTile> temp = new ArrayList<>();
        for (ScrabbleTile tile: tiles) {
            temp.add(new ScrabbleTile(tile.readTile(),tile.getPoints()));
        }
        temp.remove(t);
        return temp;
    }

    /**
     * makes a Deep Copy of the move and then adds the new point to it
     * @param move
     * @param p
     * @param t
     * @return
     */
    private HashMap<ScrabbleBoardPoint, ScrabbleTile> copyMoveAndAdd(HashMap<ScrabbleBoardPoint, ScrabbleTile> move, ScrabbleBoardPoint p, ScrabbleTile t) {
        if(p == null){
            System.out.println("p is null?");
        }
        HashMap<ScrabbleBoardPoint, ScrabbleTile> tempMove = copyMove(move);
        tempMove.put(new ScrabbleBoardPoint(p.getRow(),p.getCol()), new ScrabbleTile(t.readTile(),t.getPoints()));
        return tempMove;
    }

    /**
     * returns a deep copy of the input move
     * @param move
     * @return
     */
    private HashMap<ScrabbleBoardPoint, ScrabbleTile> copyMove(HashMap<ScrabbleBoardPoint, ScrabbleTile> move) {
        HashMap<ScrabbleBoardPoint, ScrabbleTile> temp = new HashMap<>();
        for (ScrabbleBoardPoint p: move.keySet() ) {
            temp.put(new ScrabbleBoardPoint(p.getRow(),p.getCol()),
                    new ScrabbleTile(move.get(p).readTile(), move.get(p).getPoints()));
        }

        return temp;
    }


    /**
     * just in case the last tile was placed in netween other tiles lets add any missing tiles to the move
     * @param p
     * @param move
     * @return TODO I dont think I need this the rules will check if the move is valid with out having to consider the tiles on the baord
     */
    private HashMap<ScrabbleBoardPoint, ScrabbleTile> fullHorizontalMoveAt(ScrabbleBoardPoint p,
                                                                           HashMap<ScrabbleBoardPoint, ScrabbleTile> move) {
        ScrabbleBoardPoint firstPoint = getLeftOpenP(p,move);
        //if this is null the first point is on a edge
        if(firstPoint == null){
            firstPoint  = new ScrabbleBoardPoint(p.getRow(),0);
        }
        //else the firstpoint is to the right of the left open
        else{
            firstPoint = new ScrabbleBoardPoint(firstPoint.getRow(), firstPoint.getCol()+1);
        }

        //we cant itrate past the max size of the board
        for (int c = firstPoint.getCol(); c < board.getBoardSize()-firstPoint.getCol(); c++) {

            //if the point deosnt already exist in the move and the location on the board is null we found the end break out
            if(!move.containsKey(new ScrabbleBoardPoint(firstPoint.getRow(),c))
                    && board.readTileAt(firstPoint.getRow(), c)== null){
                break;
            }
            //if the move does not contain this point but there is a tile on the board we need to add it to the move
            if(!move.containsKey(new ScrabbleBoardPoint(firstPoint.getRow(),c))
                    && board.readTileAt(firstPoint.getRow(), c) != null){

                move.put(new ScrabbleBoardPoint(firstPoint.getRow(),c),board.readTileAt(firstPoint.getRow(), c));

            }
        }

        return move;

    }


}
