package stratego;

import java.util.List;

import stratego.*;
import stratego.pieces.*;;

public class Main {

    public static void main(String[] args) {
        Player p0 = new Player("Michael", 0);
        Player p1 = new Player("Ozgur", 1);
        Game game = new Game(p0, p1);
        Piece marshal = new StepMover(p0, game.getSquare(1, 4), 10);
        Piece sergeant = new StepMover(p0, game.getSquare(0, 2), 4);
        Piece flag = new Flag(p0, game.getSquare(2, 5));
        Piece scout = new Scout(p1, game.getSquare(5, 4));
        List<Square> moves = scout.getLegalMoves();
        List<Square> attacks = scout.getLegalAttacks();

        // Player p0 = new Player("Michael", 0);
        // Player p1 = new Player("Ozgur", 1);
        // Game game = new Game(p0, p1);
        // Piece marshal = new StepMover(p0, game.getSquare(0, 3), 10);
        // Piece sergeant = new StepMover(p1, game.getSquare(0, 2), 4);
        // Piece captain = new StepMover(p1, game.getSquare(1, 2), 6);

        // List<Square> moves = marshal.getLegalMoves();
        // List<Square> attacks = marshal.getLegalAttacks();

    }
}
