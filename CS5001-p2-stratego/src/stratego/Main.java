package stratego;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import stratego.*;
import stratego.pieces.*;

public class Main {

    public static void main(String[] args) {
        // implement game instantiation
        // Randomly assign pieces to a board for each player
        // player one range [0,0]->[4,9]
        // player two range [6,0]->[9,9]
        // generate arrays represnting the locations
        // generate random number to represent the index
        // assign a piece to the drawn coordinates
        // remove form the array 

        // game play function
        // while getWinner is null
        // let players input stuff to the terminal
        // list moves and attacks for a piece on a given square
        // move/attack by giving a target square


        Square land, land2, land3, land4, water, water2;
        Piece sergeant, general, general2;
        Player michael, oz;

        land = new Square(null, 1, 2, false);  // null game, but should still work
        land2 = new Square(null, 1, 3, false);
        land3 = new Square(null, 4, 3, false);
        land4 = new Square(null, 4, 9, false);
        water = new Square(null, 3, 1, true);
        water2 = new Square(null, 4, 1, true);

        michael = new Player("Michael", 0);
        oz = new Player("Ozgur", 1);

        sergeant = new StepMover(michael, land, 4);  // weaker
        general = new StepMover(oz, land3, 9);  // stronger
        general2 = new StepMover(michael, land4, 9);

        
        // general attacks sergeant (legality not checked)
        general.attack(land);  

        // the general defeats the sergeant

        // the general has moved to the sergeant's square

        // Player p0 = new Player("Michael", 0);
        // Player p1 = new Player("Ozgur", 1);
        // Game game = new Game(p0, p1);
        // Piece marshal = new StepMover(p0, game.getSquare(1, 4), 10);
        // Piece sergeant = new StepMover(p0, game.getSquare(0, 2), 4);
        // Piece flag = new Flag(p0, game.getSquare(2, 5));
        // Piece scout = new Scout(p1, game.getSquare(5, 4));
        // List<Square> moves = scout.getLegalMoves();
        // List<Square> attacks = scout.getLegalAttacks();

        // Player p0 = new Player("Michael", 0);
        // Player p1 = new Player("Ozgur", 1);
        // Game game = new Game(p0, p1);
        // Piece marshal = new StepMover(p0, game.getSquare(0, 3), 10);
        // Piece sergeant = new StepMover(p1, game.getSquare(0, 2), 4);
        // Piece captain = new StepMover(p1, game.getSquare(1, 2), 6);

        // List<Square> moves = marshal.getLegalMoves();
        // List<Square> attacks = marshal.getLegalAttacks();

    }

    public static Game initalizeRandomGame(int seed) {
        Player p1 = new Player("Alice", 0);
        Player p2 = new Player("Bob", 1);

        Game game = new Game(p1, p2);

        Map<Player,List<Piece>> piecesMap = new HashMap<>();
        piecesMap.put(p1, new ArrayList<>());
        piecesMap.put(p2, new ArrayList<>());
        // Populate the Squares
        // Player 1
        for (Player key : piecesMap.keySet()) {


        }
        
        
        return game;
    }

    public static int getRandomInt(int lower, int upper) {
        // fixing seed for deterministic results
        Random random = new Random(0);
        return random.ints(lower, upper).findFirst().getAsInt();
    }
}
