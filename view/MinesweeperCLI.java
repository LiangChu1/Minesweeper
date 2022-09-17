package view;


import java.util.Collection;
import java.util.Scanner;

import backtracker.Backtracker;
import model.GameState;
import model.Location;
import model.Minesweeper;
import model.MinesweeperConfiguration;
import model.MinesweeperException;


public class MinesweeperCLI{
  


    public static String printBoard(Minesweeper board){
        return board.toString();
    }

    public static void help(){
        System.out.println("Available Commands: ");
        System.out.println("help - this help message");
        System.out.println("pick <row> <col> - uncovers cell a <row> <col>");
        System.out.println("hint - displays a safe selection");
        System.out.println("reset - resets to a new game");
        System.out.println("solve - executes all moves to solve the game");
        System.out.println("quit - quits the game");
    }

    public static boolean pick_row_col(int row, int col, Minesweeper board){
        Location location = new Location(row, col);
        if(board.isNotOutOfBounds(row, col)){
            try{
                board.makeSelection(location);
                return true;
            }
            catch(MinesweeperException e){
                System.out.println(e.getMessage());
                return false;
            }
        }
        else{
            System.out.println("location unavailable");
            return false;
        }

    }

    public static void quit(){
        System.out.println("Quitting game.... Goodbye!");
    }

    public static void hint(Minesweeper board){
        Collection<Location> locations = board.getPossibleSelections();
        Location location = (Location)locations.toArray()[0];
        System.out.println("Try : " + location);
    }

    public static void solve(Minesweeper board){
        MinesweeperConfiguration initconfig = new  MinesweeperConfiguration(board);
        Backtracker backtracker = new Backtracker(false);
        MinesweeperConfiguration goal = (MinesweeperConfiguration)backtracker.solve(initconfig);
        for(Location l : goal.getMinesweeperList()){
            try{
                System.out.println("Selection" + ": " +l +"\n");
                board.makeSelection(l);
                System.out.println(board.toString());
                System.out.println("Moves:" + board.getMoves());
            }catch(MinesweeperException e){

            }
        }
    }


    public static void main(String[] args) {
            int ROWS = 4;
            int COLS = 4;
            int MINES = 3;
            Scanner scanner = new Scanner(System.in);
            Minesweeper board = new Minesweeper(ROWS, COLS, MINES);
            boolean status = false;
            System.out.println("Mines:" + MINES);
            help();
            while(status != true){
                System.out.println();
                System.out.println(printBoard(board));
                System.out.println();
                System.out.println("Moves: " + board.getMoves());
                System.out.print("Enter you input:");
                String[] userInput = scanner.nextLine().split(" ");


                switch (userInput[0]) {
                    case "help":

                        help();
                        break;
                    case "pick":

                        int row = Integer.parseInt(userInput[1]);
                        int col = Integer.parseInt(userInput[2]);
                        pick_row_col(row,col,board);
                        if(board.getGameState() == GameState.WON){
                            System.out.println("Congratulations you won the game");
                        }else if(board.getGameState() == GameState.LOST){
                            System.out.println("Boom! Better luck next time!");
                            System.out.println("Here is what the board looks like:");
                        }
                        break;
                    case "hint":

                        hint(board);
                        break;
                    case "reset":
                        System.out.println("Resetting to new game.");
                        board = new Minesweeper(ROWS, COLS, MINES);
                        break;
                    case "quit":

                        quit();
                        status = true;
                        break;
                    case "solve":

                        solve(board);
                        if(board.getGameState() == GameState.WON){
                            System.out.println("you won");
                        }
                        break;
                    default:
                        System.out.println("Invalid input");
                        break;
                }

                System.out.println("\n");

            }
            scanner.close();
               
        

    }
















}
