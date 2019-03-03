package me.snizzle.scrabble;

import java.util.ArrayList;
import java.util.HashMap;

public class ScrabbleCompPlayer extends ScrabblePlayer {
    public HashMap<ScrabbleBoardPoint, ScrabbleTile> bestMove = new HashMap<>();
    public int bestMoveScore = 0;

    public ScrabbleCompPlayer(ScrabbleBoard board, ScrabbleTileBag tileBag, ScrabbleRules rules) {
        super(board, tileBag, rules);
    }

    public ScrabbleCompPlayer(ScrabbleBoard board, ScrabbleTileBag tileBag, String trayFileName, ScrabbleRules rules){
        super(board, tileBag, trayFileName, rules);
    }

    @Override
    public boolean takeTurn() {
        //TODO need to right algorithm for taking a turn.
        //first get a list of played tiles via their points
        ArrayList<ScrabbleBoardPoint> playedPoints = board.getListPlayedTiles();
        HashMap<ScrabbleBoardPoint, ScrabbleTile> bestHorizontalMove = new HashMap<>();
        int bestHorizontalScore = 0;
        HashMap<ScrabbleBoardPoint, ScrabbleTile> bestVerticalMove = new HashMap<>();
        int bestVerticalScore = 0;
        //iterate over every point
        for (ScrabbleBoardPoint p: playedPoints ) {
            bestHorizontalMove = bestHorizontalMoveAt(p, tileTray.getCopy(),new HashMap<>());
            if(bestHorizontalMove == null) {
                bestHorizontalMove = new HashMap<>();

                // bestHorizontalScore = rules.calcScore(bestHorizontalMove, board);
            }

            //bestVerticalMove = bestVerticalMoveAt(p, tileTray.getCopy());
            //bestVerticalScore = rules.calcScore(bestVerticalMove,board);

            /*if(bestHorizontalScore>=bestVerticalScore && bestHorizontalScore>bestMoveScore){
                bestMove = bestHorizontalMove;
                bestMoveScore = bestHorizontalScore;
            }
            if(bestVerticalScore>bestHorizontalScore && bestVerticalScore>bestMoveScore){
                bestMove = bestVerticalMove;
                bestMoveScore = bestVerticalScore;
            }*/

        }
        cacheMove(bestHorizontalMove);
        approveMove();
        /*
        if(bestMove.isEmpty()){
            System.out.println("error couldnt find any moves at all");
            //TODO write something here
        }*/



        return false;
    }


    private HashMap<ScrabbleBoardPoint, ScrabbleTile> bestVerticalMoveAt(ScrabbleBoardPoint p,
                                                                         ArrayList<ScrabbleTile> tiles) {
        return null;
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

        /*if(tiles == null) {
            System.out.println(tiles);
        }

        //the first edge case is if our list of tiles is empty there is no move return the move as best move
        if(tiles.isEmpty()){
            //we should be able to modify this last move without deep copying it into the method TODO just incase I am wrong we may need to copy move
            //move = fullHorizontalMoveAt(p, move); TODO i dont think I need this

            //if this is empty we just check if the move is valid
            if(rules.isMoveValid(move, board)){
                return move;
            }

            return null;
        }
        //now the edge case of 1 tile will return move with the max score or null if there isnt any
        else if(tiles.size() == 1){
            HashMap<ScrabbleBoardPoint, ScrabbleTile> moveL = null;
            HashMap<ScrabbleBoardPoint, ScrabbleTile> moveR = null;

            ScrabbleBoardPoint leftOpen = getLeftOpenP(p,move);
            ScrabbleBoardPoint rightOpen = getRightOpenP(p,move);

            //if there is no tiles to the left or right available check to see if the move is valid and return accordingly
            if(leftOpen == null && rightOpen == null){
                // we ignore this tile
                return bestHorizontalMoveAt(p,new ArrayList<>(),move);
            }
            //now do the other null cases
            if(rightOpen == null){
                return bestHorizontalMoveAt(leftOpen, new ArrayList<>(), copyMoveAndAdd(move,leftOpen, tiles.get(0)));
            }
            if(leftOpen == null){
                return bestHorizontalMoveAt(rightOpen, new ArrayList<>(), copyMoveAndAdd(move,rightOpen, tiles.get(0)));
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

            //System.out.println("got here!");

            //if we get this far the tray of tiles is more than one and we loop through it keeping the max score
            //first lets get the first open left and right points
            HashMap<ScrabbleBoardPoint, ScrabbleTile> moveL = null;
            HashMap<ScrabbleBoardPoint, ScrabbleTile> moveR = null;
            HashMap<ScrabbleBoardPoint, ScrabbleTile> maxMove = null;

            ScrabbleBoardPoint leftOpen = getLeftOpenP(p, move);
            ScrabbleBoardPoint rightOpen = getRightOpenP(p, move);
            //if both of these are null then it doesnt matter how many tiles we have we cant even play them
            if (leftOpen == null && rightOpen == null) {
                return null;
            }

            for (ScrabbleTile t : tiles) {
                //System.out.println("before " +rightOpen);
                //TODO i must erase this to deal with blanks
                if (t.getPoints() == 0) {
                    continue;
                }

                //in this case we can only build right
                if (leftOpen == null) {
                    moveR = bestHorizontalMoveAt(rightOpen, copyTilesWithNoT(tiles, t), copyMoveAndAdd(move, rightOpen, t));

                    maxMove = getMaxMove(moveR, maxMove);
                    //if we get this far just leave Max move alone
                }
                //now do the case if we can only build left
                if (rightOpen == null) {
                    moveL = bestHorizontalMoveAt(leftOpen, copyTilesWithNoT(tiles, t), copyMoveAndAdd(move, leftOpen, t));

                    maxMove = getMaxMove(moveL, maxMove);
                    // if we get this far leave the max move alone it could be nul or not
                }

                //if we get this far we need to check the max of all possibilites
                //System.out.println("after " +rightOpen);
                //get the best move if we place the tile t on the right open space
                moveR = bestHorizontalMoveAt(rightOpen, copyTilesWithNoT(tiles, t), copyMoveAndAdd(move, rightOpen, t));
                //get the best move if we decide to put the tile on the left open space
                moveL = bestHorizontalMoveAt(leftOpen, copyTilesWithNoT(tiles, t), copyMoveAndAdd(move, leftOpen, t));

                //if both best moves result in nulls continue to try the next tile.
                if (moveR == null && moveL == null) {
                    continue;
                }
                //if only the moveL returns something valid
                if (moveR == null) {
                    maxMove = getMaxMove(moveL, maxMove);
                }
                //if only the moveR returns somthing valid
                if (moveL == null) {
                    maxMove = getMaxMove(moveR, maxMove);
                }
                //if we get this far then we get the Maxmove of all 3
                maxMove = getMaxMove(getMaxMove(moveL, moveR), maxMove);
                System.out.println("what?");
                return maxMove;
            }
        }

        //System.out.println(tiles.size());
        //System.out.println("this should never run!");*/
        return null;
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
     *
     * @param p
     * @param move
     * @return the first open point on the board that doesnt have a tile to the right of p and null if there is no open tile to the right
     */
    private ScrabbleBoardPoint getRightOpenP(ScrabbleBoardPoint p, HashMap<ScrabbleBoardPoint, ScrabbleTile> move) {
        //there cant be a point to left of column 0
        if(p.getCol() == board.getBoardSize()-1){
            return null;
        }
        //lets build the potential empty p by moving right
        ScrabbleBoardPoint p2 = new ScrabbleBoardPoint(p.getRow(),p.getCol()+1);
        //if the p2 is not in the move or has a tile on the board then this is a blank point
        if(!move.containsKey(p2) && board.readTileAt(p2.getRow(),p2.getCol())== null){
            return p2;
        }
        //else return the point right of p2 that is empty
        else{
            return getLeftOpenP(p2,move);
        }
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

    /**
     * returns the first open space on the left of the point p and returns null if there is none
     * @param p
     * @param move
     * @return
     */
    private ScrabbleBoardPoint getLeftOpenP(ScrabbleBoardPoint p,
                                            HashMap<ScrabbleBoardPoint, ScrabbleTile> move) {
        //there cant be a point to left of column 0
        if(p.getCol() == 0){
            return null;
        }
        //lets build the potential empty p
        ScrabbleBoardPoint p2 = new ScrabbleBoardPoint(p.getRow(),p.getCol()-1);
        //if the p2 is not in the move or on the board then this is a blank point
        if(!move.containsKey(p2) && board.readTileAt(p2.getRow(),p2.getCol())== null){
            return p2;
        }
        //else return the point left of p2 that is empty
        else{
            return getLeftOpenP(p2,move);
        }

    }
}
