package stratego;

import java.util.List;

import stratego.*;
import stratego.pieces.*;;

public class Main {

    public static void main(String[] args) {
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
        // TODO
        Player p0 = new Player("Michael", 0);
        Player p1 = new Player("Ozgur", 1);
        return new Game(p0, p1);
    }
}
