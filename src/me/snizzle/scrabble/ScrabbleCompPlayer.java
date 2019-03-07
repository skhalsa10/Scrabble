package me.snizzle.scrabble;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public abstract class ScrabbleCompPlayer extends ScrabblePlayer {

    protected HashMap<ScrabbleBoardPoint, ScrabbleTile> bestMove = new HashMap<>();
    protected int bestMoveScore = 0;
    //private Trie testedWords;

    public ScrabbleCompPlayer(ScrabbleBoard board, ScrabbleTileBag tileBag, ScrabbleRules rules) {
        super(board, tileBag, rules);
    }

    public ScrabbleCompPlayer(ScrabbleBoard board, ScrabbleTileBag tileBag, String trayAsString, ScrabbleRules rules){
        super(board, tileBag, trayAsString, rules);
    }

    @Override
    public boolean takeTurn() {

        //find the best move computers version of getting a move instead of using gui
        bestMove = new HashMap<>();
        bestMoveScore = 0;


        long start = System.currentTimeMillis();
        findBestMove();
        long end = System.currentTimeMillis();
        System.out.println("Find Best move takes " +  (end - start) + "ms");


        //cache the move like we do for the human player i know it works already
        cacheMove(bestMove);

        //return false if it is a bad move. it is pretty much guarunteed to be valid as valid checks are conducted
        //in find best move
        if(!checkCachedMoveValid()){

            return false;
        }

        //commit the move to the board
        if(!approveMove()){
            return false;
        }
        score.addToScore(bestMoveScore);

        tileTray.fillTray();

        return true;
    }

    /**
     * my other algorithm was horrible I am trying a new one from scratch I wasted so much time on the other one.
     * @return
     */
    abstract void findBestMove();

    protected String getHWord(ScrabbleBoardPoint p, HashMap<ScrabbleBoardPoint, ScrabbleTile> move) {
        ScrabbleBoardPoint leftP = getLeftPoint(p);
        if(leftP==null){
            return "";
        }
        return getHWordHelper(leftP,move);
    }

    private String getHWordHelper(ScrabbleBoardPoint p, HashMap<ScrabbleBoardPoint, ScrabbleTile> move){
        //if off the boar to the left
        if(p.getCol()>= board.getBoardSize()){
            return "";
        }

        //if we have an empty
        if(board.readTileAt(p.getRow(), p.getCol()) == null && !move.containsKey(p)){
            return "";
        }
        //if tile on board or in the current move
        String s = "";
        if(board.readTileAt(p.getRow(), p.getCol()) != null){
            s = s+board.readTileAt(p.getRow(), p.getCol()).readTile();
        }
        if(move.containsKey(p)){
            s = s+move.get(p).readTile();
        }

        return s + getHWordHelper(new ScrabbleBoardPoint(p.getRow(),p.getCol()+1),move);
    }

    private String getVWordHelper(ScrabbleBoardPoint p, HashMap<ScrabbleBoardPoint, ScrabbleTile> move) {
        //if the given p is below the board return an empty string
        if(p.getRow()>= board.getBoardSize()){
            return "";
        }
        //if we have an empty
        if(board.readTileAt(p.getRow(), p.getCol()) == null && !move.containsKey(p)){
            return "";
        }
        //if tile on board or in the current move
        String s = "";
        if(board.readTileAt(p.getRow(), p.getCol()) != null){
            s = s+board.readTileAt(p.getRow(), p.getCol()).readTile();
        }
        if(move.containsKey(p)){
            s = s+move.get(p).readTile();
        }

        return s + getVWordHelper(new ScrabbleBoardPoint(p.getRow()+1,p.getCol()),move);
    }

    protected String getVWord(ScrabbleBoardPoint p, HashMap<ScrabbleBoardPoint, ScrabbleTile> move){
        ScrabbleBoardPoint topP = getTopPoint(p);
        if(topP == null){
            return "";
        }

        return getVWordHelper(topP, move);
    }


    private void printPoints(HashSet<ScrabbleBoardPoint> vPointsToCheck) {
        for (ScrabbleBoardPoint p: vPointsToCheck ) {
            System.out.println("r-" + p.getRow() + " c-"+p.getCol());
        }
    }

    //TODO I need to actually build up a word when I get the next open  what if the next open skips a tile
    protected boolean getMaxVMove(ScrabbleBoardPoint p, String word,
                             HashMap<ScrabbleBoardPoint, ScrabbleTile> move,
                             ArrayList<ScrabbleTile> tiles){

        //this is the closing case for the recurssion
        if(tiles.size() == 0){
            if(rules.dictContains(word)){
                if(rules.isMoveValid(move,board)){
                    int moveScore = rules.calcScore(move,board);
                    if(moveScore> bestMoveScore){
                        bestMoveScore = moveScore;
                        bestMove = move;
                    }
                    return true;
                }
            }

            //if the dictionary does not contain the word... or if
            // it does but the move is not valid return false
            return false;
        }
        else if(tiles.size()>0){

            //word = getVWord(p, move);

            //find the open points
            ScrabbleBoardPoint bottomOpen;
            ScrabbleBoardPoint topOpen;
            //add to the string as we find the bottom open
            StringBuilder bWordBuilder = new StringBuilder(word);
            bottomOpen = getBottomOpenP(p,move,bWordBuilder);
            String bWord = bWordBuilder.toString();

            //we do not want to was time adding tiles on the rightside if the current word is not a prefix of something
            if(!rules.dictIsPrefix(bWord)){
                bottomOpen = null;
            }

            //we dont have an isSuffix so we have to brute force the top side
            //but lets also update the word as we find the open
            StringBuilder tWordBuilder =  new StringBuilder(word);
            topOpen = getTopOpenP(p,move,tWordBuilder);
            String tWord = tWordBuilder.toString();

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
                    bottom = getMaxVMove(bottomOpen,(bWord+t.readTile()),copyMoveAndAdd(move,bottomOpen,t),copyTilesWithNoT(tiles,t));
                    continue;
                }

                //if it is the left side that is valid
                if(bottomOpen == null){
                    top = getMaxVMove(topOpen,(t.readTile()+tWord),copyMoveAndAdd(move,topOpen,t),copyTilesWithNoT(tiles,t));
                    continue;
                }

                top =  getMaxVMove(topOpen,(t.readTile()+tWord),copyMoveAndAdd(move,topOpen,t),copyTilesWithNoT(tiles,t)) || top;
                bottom = getMaxVMove(bottomOpen,(bWord+t.readTile()),copyMoveAndAdd(move,bottomOpen,t),copyTilesWithNoT(tiles,t)) || bottom;
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
    protected HashSet<ScrabbleBoardPoint> filterVPoints(ArrayList<ScrabbleBoardPoint> playedPoints) {
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
    private ScrabbleBoardPoint getBottomOpenP(ScrabbleBoardPoint p, HashMap<ScrabbleBoardPoint, ScrabbleTile> move, StringBuilder bWord) {

        //if we get a p off of the board to the bottom
        if(p.getRow() >= board.getBoardSize()){
            return null;
        }

        //if p represents a blank
        if(!move.containsKey(p) && board.readTileAt(p.getRow(),p.getCol())== null){
            return p;
        }

        //add the next letter to the word if it exists
        if(p.getRow() < board.getBoardSize()-1 && board.readTileAt(p.getRow()+1,p.getCol()) != null){
            bWord.append(board.readTileAt(p.getRow()+1,p.getCol()).readTile());
        }


        //check the p below this one
        return getBottomOpenP(new ScrabbleBoardPoint(p.getRow()+1,p.getCol()),move,bWord);
    }


    private ScrabbleBoardPoint getTopOpenP(ScrabbleBoardPoint p,
                                           HashMap<ScrabbleBoardPoint, ScrabbleTile> move, StringBuilder tWord) {
        //if p is off the board to the top return null
        if(p.getRow() <0){
            return null;
        }

        //if p represents a blank
        if(!move.containsKey(p)&& board.readTileAt(p.getRow(),p.getCol())==null){
            return p;
        }

        if(p.getRow()>0 && board.readTileAt(p.getRow()-1,p.getCol())!=null){
            //tWord.reverse();
            //tWord.append(board.readTileAt(p.getRow()-1,p.getCol()).readTile());
            //tWord.reverse();
            tWord.insert(0,board.readTileAt(p.getRow()-1,p.getCol()).readTile());
        }

        //check tile above
        return getTopOpenP(new ScrabbleBoardPoint(p.getRow()-1,p.getCol()),move, tWord);
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
    protected boolean getMaxHMove(ScrabbleBoardPoint p, String word,
                                  HashMap<ScrabbleBoardPoint, ScrabbleTile> move,
                                  ArrayList<ScrabbleTile> tiles) {

        //okay hopefull this is faster I think checking is my move is valid costs alot so I will build up a string instead
        //if the dictionary contains the word. it is worth checking to see if the move is valid. the chances are higher
        if(tiles.size() ==0){
            if(rules.dictContains(word)){
                if(rules.isMoveValid(move,board)){
                    int moveScore = rules.calcScore(move,board);
                    if(moveScore> bestMoveScore){
                        bestMoveScore = moveScore;
                        bestMove = move;
                    }
                    return true;
                }
            }
            //if the dictionary does not contain the word... or if
            // it does but the move is not valid return false
            return false;
        }
        else if(tiles.size()>0){

            //find the open points
            ScrabbleBoardPoint rightOpen = null;
            ScrabbleBoardPoint leftOpen;

            StringBuilder rWordBuilder = new StringBuilder(word);
            rightOpen = getRightOpenP(p,move,rWordBuilder);
            String rWord = rWordBuilder.toString();
            //we do not want to was time adding tiles on the rightside if the current word is not a prefix of something
            if(!rules.dictIsPrefix(rWord)){
                rightOpen = null;
            }

            //we dont have an isSuffix so we have to brute force the left side
            StringBuilder lWordBuilder = new StringBuilder(word);
            leftOpen = getLeftOpenP(p,move,lWordBuilder);
            String lWord = rWordBuilder.toString();

            boolean left = false;
            boolean right = false;


            //NOW THAT WE HAVE OPENS we can loop through and reduce the recursion accordingly
            for (ScrabbleTile t:tiles ) {
                //if both are null we cant add this tile anywhere so we reduce by the tile and
                // continue when it comes back to try with the next tile
                if(leftOpen == null && rightOpen == null){
                    return getMaxHMove(p,word,copyMove(move),copyTilesWithNoT(tiles,t));
                }

                //at least one of the open points is valid
                //if it is the right side
                if(leftOpen == null){
                    right = getMaxHMove(rightOpen,(rWord+t.readTile()),copyMoveAndAdd(move,rightOpen,t),copyTilesWithNoT(tiles,t));
                    continue;
                }

                //if it is the left side that is valid
                if(rightOpen == null){
                    left = getMaxHMove(leftOpen,(t.readTile()+lWord),copyMoveAndAdd(move,leftOpen,t),copyTilesWithNoT(tiles,t));
                    continue;
                }

                //System.out.println(leftOpen);
                left =  getMaxHMove(leftOpen,(t.readTile()+lWord),copyMoveAndAdd(move,leftOpen,t),copyTilesWithNoT(tiles,t)) || left;
                right = getMaxHMove(rightOpen,(rWord+t.readTile()),copyMoveAndAdd(move,rightOpen,t),copyTilesWithNoT(tiles,t)) || right;
                //if we get this far both sides are valid.

            }

            while(!tiles.isEmpty()){

                getMaxHMove(p,word,move,copyTilesWithNoT(tiles,tiles.get(0)));
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
     * creats a set of H point to build horizontal words against
     * @param playedPoints
     * @return
     */
    protected HashSet<ScrabbleBoardPoint> filterHPoints(ArrayList<ScrabbleBoardPoint> playedPoints) {

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
    private ScrabbleBoardPoint getRightOpenP(ScrabbleBoardPoint p, HashMap<ScrabbleBoardPoint, ScrabbleTile> move, StringBuilder rWord) {

        //if we get a p off the right edge of the board
        if(p.getCol() >= board.getBoardSize()){
            return null;
        }

        //if p represents an empty spot return p
        if(!move.containsKey(p) && board.readTileAt(p.getRow(),p.getCol())== null){
            return p;
        }
        if(p.getCol()<board.getBoardSize()-1&& board.readTileAt(p.getRow(),p.getCol()+1)!= null){
            rWord.append(board.readTileAt(p.getRow(),p.getCol()+1).readTile());
        }

        return getRightOpenP(new ScrabbleBoardPoint(p.getRow(),p.getCol()+1),move, rWord);
    }

    /**
     * why is this so hard for me. lets make some assumptions that if p is a blank it is an end
     * @param p
     * @param move
     * @return
     */
    private ScrabbleBoardPoint getLeftOpenP(ScrabbleBoardPoint p,
                                            HashMap<ScrabbleBoardPoint, ScrabbleTile> move, StringBuilder lWord) {

        //if p is off the left edge return null
        if(p.getCol() < 0){
            return null;
        }

        //if p represents an empty spot
        if(!move.containsKey(p)&& board.readTileAt(p.getRow(),p.getCol()) == null){
            return p;
        }

        if(p.getCol()>0 && board.readTileAt(p.getRow(),p.getCol()-1) != null){
            lWord.insert(0,board.readTileAt(p.getRow(),p.getCol()-1).readTile());
        }

        return getLeftOpenP(new ScrabbleBoardPoint(p.getRow(),p.getCol()-1), move, lWord);


    }

    private void printMove(HashMap<ScrabbleBoardPoint, ScrabbleTile> bestMove) {
        for (ScrabbleBoardPoint p: bestMove.keySet() ) {
            System.out.println("row: " + p.getRow() + " col: " + p.getCol() + " letter: " + bestMove.get(p).readTile());
        }
    }



    private void printPlayedPoints(ArrayList<ScrabbleBoardPoint> playedPoints) {
        for (ScrabbleBoardPoint p: playedPoints ) {
            System.out.println("r: " + p.getRow() + " c: " + p.getCol() + " -> " + board.readTileAt(p.getRow(),p.getCol()));
        }
    }


    /**
     * this returns a copy of tiles with out Tile t
     * @param tiles makes a deep copy of these tiles
     * @param t removes this t from the copy returned
     * @return copy of tiles without T
     */
    protected ArrayList<ScrabbleTile> copyTilesWithNoT(ArrayList<ScrabbleTile> tiles, ScrabbleTile t) {
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


}
