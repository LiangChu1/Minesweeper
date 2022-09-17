package model;
import backtracker.Backtracker;
import backtracker.Configuration;


import java.util.*;

public class MinesweeperConfiguration implements Configuration {
    public  Minesweeper minesweepercurr; //first configuration
     public List<Location> minesweeperList;


    public MinesweeperConfiguration(Minesweeper firstBoard) {


        this. minesweepercurr = firstBoard;
        this.minesweeperList = new ArrayList<>();

    }

    public MinesweeperConfiguration(List<Location> list, Minesweeper minesweeper) {
        //get the number of configurations
            this.minesweepercurr = minesweeper;
            this.minesweeperList = list;
        }


    public Minesweeper getMinesweepercurr() {
        return minesweepercurr;
    }

    public List<Location> getMinesweeperList() {
        return minesweeperList;
    }


        @Override
    public Collection<Configuration> getSuccessors(){
        //get list of mindsweepers
        List<Configuration> successors = new ArrayList<>();
        //shallow copy
        for (Location location : minesweepercurr.getPossibleSelections()) {
            Minesweeper gameCopy = new Minesweeper(minesweepercurr);
            List<Location> locCopy = new ArrayList<>(minesweeperList);

            try{
                gameCopy.makeSelection(location);
            }
            catch(MinesweeperException e){

            }
            locCopy.add(location);
            Configuration successor = new MinesweeperConfiguration(locCopy,gameCopy);
            successors.add(successor);
        }


        return successors;
    }

    @Override
    public boolean isValid() {
        if(minesweepercurr.getGameState() == GameState.LOST){
            return false;
        }
        return true;
    }


    @Override
    public boolean isGoal() {
        return this.isValid() && minesweepercurr.getGameState() == GameState.WON;
    }

    @Override
    public String toString(){
        return minesweepercurr.toString();
    }

    public static void main(String[] args) {
        Minesweeper board = new Minesweeper(4,4,3);
        Backtracker backtracker = new Backtracker(true);
        Configuration initconfig = new  MinesweeperConfiguration(board);
        Configuration goal = backtracker.solve(initconfig);
        if (goal == null) {
            System.out.println("no sol");
        } else {
            System.out.println(goal);
        }
    }
}
