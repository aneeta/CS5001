package stratego.pieces;

import stratego.Player;
import stratego.Square;

/**
 * Class representing the moving Stratego Game Piece.
 */
public class StepMover extends Piece {

    /**
     * StepMover constructor.
     * @param owner Player who the piece belongs to
     * @param square Square to place the piece on
     * @param rank Piece rank (determines attack outcome)
     */
    public StepMover(Player owner, Square square, int rank) {
        super(owner, square, rank);
    }
}
