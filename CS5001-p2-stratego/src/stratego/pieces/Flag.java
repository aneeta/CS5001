package stratego.pieces;

import stratego.Player;
import stratego.Square;

/**
 * Class representing Flag Stratego Game Pieces.
 */
public class Flag extends ImmobilePiece {

    public Flag(Player owner, Square square) {
        super(owner, square, -1);

    }

    @Override
    public void beCaptured() {
        super.beCaptured();
        getOwner().loseGame();
    }
}
