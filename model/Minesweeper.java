package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class Minesweeper implements MinesweeperObserver {
    public static final char MINE = 'M';
    public static final char COVERED = '-';
    private int rows;
    private int cols;
    private int mineCount;
    private char[][] board;
    private boolean[][] isCovered;
    private int moves;
    private GameState gameState;
    private Set<Location> possibleSelections;
    private static final int[] ROW_MOVES = {-1, -1, -1, 0, 0, 1, 1, 1};
    private static final int[] COL_MOVES = {-1, 0, 1, -1, 1, -1, 0, 1};
    private MinesweeperObserver observer;

    public MinesweeperObserver  getObserver (){
        return observer;
    }
    public void register(MinesweeperObserver observer){
        this.observer = observer;
    }



    private void notifyObserver(Location location){
        if(observer != null) {
            //add mindsweeperobserver
            //pass in whole board

            System.out.println(getGameState());
            if (getGameState() == GameState.LOST || getGameState() == GameState.WON) {
                revealBoard();
                for (int col = 0; col < cols; col++) {
                    for (int row = 0; row < rows; row++) {
                        observer.cellUpdated(new Location(row, col));
                    }
                }
            }
            else{
                observer.cellUpdated(location);
            }
        }


    }


    public Minesweeper(int rows, int cols, int mineCount){
        this.rows = rows;
        this.cols = cols;
        this.mineCount = mineCount;
        this.board = new char[rows][cols];
        this.isCovered = new boolean[rows][cols];
        this.moves = 0;
        this.gameState = GameState.NOT_STARTED;
        for(int r = 0; r < rows; r++){
            for(int c = 0; c < cols; c++){
                board[r][c] = '0';
                isCovered[r][c] = true;
            }
        }

        Random random = new Random();
        while(mineCount > 0){
            int rand_row = random.nextInt(rows);
            int rand_col = random.nextInt(cols);
            if(board[rand_row][rand_col] == '0'){
                board[rand_row][rand_col] = MINE;
                mineCount -= 1;
            }
        }

        for(int row = 0; row < rows; row++){
            for(int col = 0; col < cols; col++){
                int mineCounter = 0;
                if(board[row][col] != MINE){
                    for(int i = 0; i < 8; i++){
                        int tempX = row+ROW_MOVES[i];
                        int tempY = col+COL_MOVES[i];
                        if(isNotOutOfBounds(tempX, tempY)){
                            if(board[tempX][tempY] == MINE){
                                mineCounter += 1;
                            }
                        }
                    }
                    String cellInfo = Integer.toString(mineCounter);
                    board[row][col] = cellInfo.charAt(0);
                }
            }
        }
        this.possibleSelections = new HashSet<Location>(getPossibleSelections());
    }

    public int getCols() {
        return cols;
    }

    public int getRows() {
        return rows;
    }

    //deep copy
    public Minesweeper(Minesweeper other){
        this.board = new char[other.rows][other.cols];
        this.isCovered = new boolean[other.rows][other.cols];
        this.rows = other.rows;
        this.cols = other.cols;
        this.mineCount = other.mineCount;
        this.gameState = other.gameState;
        this.possibleSelections = new HashSet<>(other.getPossibleSelections());
        
        for(int r = 0; r < rows; r++){
            this.board[r] = Arrays.copyOf(other.board[r], rows);
            this.isCovered[r] = Arrays.copyOf(other.isCovered[r], rows);
        }

    }

    public GameState setGameState(){
        if(possibleSelections.isEmpty()){
            return GameState.WON;
        }
        else{
            return GameState.IN_PROGRESS;
        }
    }
    public void makeSelection(Location location) throws MinesweeperException  {
        //implement row
        int row = location.getRow();
        //implement col
        int col = location.getCol();
        
        //see if covered is true change boolean board and displayed board
        if (isCovered[row][col] == true) {
            isCovered[row][col] = false;
            if(board[row][col] != MINE){
                possibleSelections = new HashSet<>(getPossibleSelections());
               // System.out.println(possibleSelections);
                moves += 1;
                gameState = setGameState();
            }
            else{
                gameState = GameState.LOST;
            }
            //notify observer
            notifyObserver( location);
        }else{
            gameState = GameState.IN_PROGRESS;
            throw new MinesweeperException("location unavailable");
        }



    }
    public Collection<Location> getPossibleSelections(){
        List<Location> locations = new ArrayList<>();
        for(int r = 0; r < rows; r++){
            for(int c = 0; c < cols; c++){
                if(board[r][c] != MINE && isCovered[r][c] == true){
                    Location l = new Location(r, c);
                    locations.add(l);
                }
            }
        }
        return locations;
    }

    public int getMoves() {
        return moves;
    }

    public GameState getGameState() {
        return gameState;
    }

    public boolean isNotOutOfBounds(int x, int y){
        return (x >= 0 && x < rows) && (y >= 0 && y < cols);
    }
    
    @Override
    public String toString() {

        // TODO Auto-generated method stub
        String s = "";
        char[][] cells = new char[rows][cols];
        if(gameState != GameState.WON && gameState != GameState.LOST){
            for(int r = 0; r < rows; r++){
                for(int c = 0; c < cols; c++){
                    if(isCovered[r][c] == true){
                        cells[r][c] = COVERED;
                    }
                    else{
                        cells[r][c] = board[r][c];
                    }
                    s += cells[r][c];
                }
                s += "\n";
            }
        }
        else{
            if(gameState == GameState.LOST){
                revealBoard();
            }
            for(int r = 0; r < rows; r++){
                for(int c = 0; c < cols; c++){
                    cells[r][c] = board[r][c];
                    s += cells[r][c];
                }
                s += "\n";
            }
        }
        return s;
    }

    public void revealBoard(){
        Collection<Location> remainingLocations = possibleSelections;
        for(Location l: remainingLocations){
            int mineCounter = 0;
            isCovered[l.getRow()][l.getCol()] = false;
            if(board[l.getRow()][l.getCol()] != MINE){
                for(int i = 0; i < 8; i++){
                    int tempX = l.getRow()+ROW_MOVES[i];
                    int tempY = l.getCol()+COL_MOVES[i];
                    if(isNotOutOfBounds(tempX, tempY)){
                        if(board[tempX][tempY] == MINE){
                            mineCounter += 1;
                        }
                    }
                }
                String cellInfo = Integer.toString(mineCounter);
                board[l.getRow()][l.getCol()] = cellInfo.charAt(0);
            }
        }
        
    }

    public char getSymbol(Location location){
        return board[location.getRow()][location.getCol()];
    }

    public boolean isCovered(Location location){
        return isCovered[location.getRow()][location.getCol()];
    }

    @Override
    public void cellUpdated(Location location) {
        // TODO Auto-generated method stub
        
    }    
}
