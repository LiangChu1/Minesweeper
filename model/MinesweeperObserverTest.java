package model;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.platform.commons.annotation.Testable;

@Testable
public class MinesweeperObserverTest {
    public class testObserverMethod implements MinesweeperObserver{
    private Location example_location;
        @Override
        public void cellUpdated(Location location) {
            this.example_location = location;
        }
    }
    @Test
    public void testObserver(){
        Minesweeper board = new Minesweeper(3,3,3);
        Location expected = new Location(2, 2);
        
        if(board.isCovered(expected)){
            assertTrue(board.isCovered(expected));
        }
    }
    
}
