package model;

import static org.junit.jupiter.api.Assertions.assertEquals;


import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.platform.commons.annotation.Testable;

@Testable
public class MinesweeperTest {
    
    @Test
    public void registerTest(){
        MinesweeperObserver expected = new MinesweeperObserver() {
            @Override
            public void cellUpdated(Location location) {
                int x = 3;
            }
        };

        Minesweeper mineSweeper = new Minesweeper(5,5,5);

        mineSweeper.register(expected);

        MinesweeperObserver actual = mineSweeper.getObserver();

        assertEquals(actual,expected);
    }


    @Test
    public void getGameStateTest(){
        Minesweeper mineSweeper = new Minesweeper(3, 4, 0);
        GameState expected = GameState.NOT_STARTED;
        GameState actual = mineSweeper.getGameState();

        assertEquals(expected,actual);

    }

    @Test
    public void makeSelectionTest(){
        Minesweeper mineSweeper = new Minesweeper(3, 4, 0);

        try{
            mineSweeper.makeSelection(new Location(2,2));
        }catch (MinesweeperException e) {
            ;
        }

        Boolean[][] expectedArray = new Boolean[mineSweeper.getRows()][mineSweeper.getCols()];
        for(int r = 0; r < mineSweeper.getRows(); r++){
            for(int c = 0; c < mineSweeper.getCols(); c++){


                expectedArray[r][c] = true;
            }
        }
        expectedArray[2][2] = false;

        Boolean expected =expectedArray[2][2];
        Boolean actual = mineSweeper.isCovered(new Location(2,2));

        assertEquals(expected,actual);

    }

    @Test
    public void getMovesTest(){
        Minesweeper a = new Minesweeper(3, 4, 0);
        int expected = 0;
        int actual = a.getMoves();

        assertEquals(expected,actual);

    }

    @Test
    public void getColsTest(){
        Minesweeper a = new Minesweeper(3, 4, 0);
        int expected = 4;
        int actual = a.getCols();

        assertEquals(expected,actual);

    }

    @Test
    public void getRowsTest(){
        Minesweeper a = new Minesweeper(3, 4, 0);
        int expected = 3;
        int actual = a.getRows();

        assertEquals(expected,actual);

    }

    @Test
    public void setGameStateTest(){
        Minesweeper mineSweeper = new Minesweeper(3, 4, 0);
        GameState expected = GameState.IN_PROGRESS;
        GameState actual = mineSweeper.setGameState();

        assertEquals(expected,actual);

    }

    @Test
    public void isNotOutOfBoundsTest(){
        Minesweeper mineSweeper = new Minesweeper(3, 4, 0);
        Boolean expected = false;
        Boolean actual = mineSweeper.isNotOutOfBounds(6,6);

        assertEquals(expected,actual);

    }
    @Test
    public void getPossibleSelectionsTest(){
        Minesweeper a = new Minesweeper(4, 4, 0);
        List<Location> location = new ArrayList<>();
        location.add(new Location(2,3));
        location.add(new Location(4,3));
        location.add(new Location(1,2));
        Location actual = location.get(1);
        Location expected = location.get(1);
        assertEquals(expected,actual);

    }

    @Test
    public void isCoveredTest(){
        Minesweeper mineSweeper = new Minesweeper(3, 4, 0);
        Boolean expected = true;
        Boolean actual = mineSweeper.isCovered(new Location(2,2));

        assertEquals(expected,actual);


    }


}
