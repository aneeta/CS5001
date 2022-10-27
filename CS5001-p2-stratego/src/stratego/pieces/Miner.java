package stratego.pieces;

import stratego.CombatResult;
import stratego.Player;
import stratego.Square;

/**
 * Class representing the Miner Stratego Game Piece.
 */
public class Miner extends StepMover {

    /**
     * Miner constructor.
     * @param owner Player who the piece belongs to
     * @param square Square to place the piece on
     */
    public Miner(Player owner, Square square) {
        super(owner, square, 3);
    }

    @Override
    public CombatResult resultWhenAttacking(Piece targetPiece) {
        if (targetPiece.getRank() == 0) {
            return CombatResult.WIN;
        }
        return super.resultWhenAttacking(targetPiece);
    }
}
