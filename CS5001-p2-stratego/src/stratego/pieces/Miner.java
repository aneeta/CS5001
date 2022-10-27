package stratego.pieces;

import stratego.CombatResult;
import stratego.Player;
import stratego.Square;


public class Miner extends StepMover {
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
