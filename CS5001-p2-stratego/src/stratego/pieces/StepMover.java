package stratego.pieces;

import stratego.Player;
import stratego.Square;

/**
 * Class representing the moving Stratego Game Piece.
 */
public class StepMover extends Piece {
    public StepMover(Player owner, Square square, int rank) {
        super(owner, square, rank);
    }
}
