package customtests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import stratego.terminalgame.GameMove;
import stratego.Game;
import stratego.Player;

public class GameMoveTest {

    private Game testGame;
    private Player testP1, testP2;

    @Before
    public void setUp() {
        testP1 = new Player("Aaa", 0);
        testP2 = new Player("Bbb", 1);
        testGame = new Game(testP1, testP2);
    }

    @Test
    public void testPopulateBoard() {
        Game populatedGame = GameMove.populateBoard(testGame);
        assertEquals(testGame, populatedGame);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testParseInput() {
        String testStr1 = "move 2,3";
        String testStr2 = "MOVE 2,3 3,3";

        GameMove generatedMove = GameMove.parseInput(testStr2);
        assertEquals(generatedMove.getMove(), "move");
        assertEquals(generatedMove.getTargetCol(), 3);
        assertEquals(generatedMove.getCurrentRow(), 2);

        GameMove generatedMove2 = GameMove.parseInput(testStr1);

    }

    @Test
    public void testGenerateCoordsList() {
        List<Integer> testList = GameMove.generateCoordsList(new int[] { 2, 3, 4 }, 2);
        assertEquals(
                new ArrayList<Integer>(Arrays.asList(2, 2, 3, 3, 4, 4)),
                testList);
    }
}