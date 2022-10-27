package stratego.pieces;

import stratego.CombatResult;
import stratego.Player;
import stratego.Square;

/**
 * Class representing the Spy Stratego Game Piece.
 */
public class Spy extends StepMover {

    /**
     * Spy constructor.
     * @param owner Player who the piece belongs to
     * @param square Square to place the piece on
     */
    public Spy(Player owner, Square square) {
        super(owner, square, 1);
    }

    @Override
    public CombatResult resultWhenAttacking(Piece targetPiece) {
        // If attacking a Marshal or a flag
        if ((targetPiece.getRank() == 10) || targetPiece.getRank() == -1) {
            return CombatResult.WIN;
        }
        return CombatResult.LOSE;
    }
}
