package stratego.pieces;

import stratego.Player;
import stratego.Square;

/**
 * Class representing Flag Stratego Game Pieces.
 */
public class Flag extends ImmobilePiece {

    /**
     * Flag constructor.
     * @param owner Player who the piece belongs to
     * @param square Square to place the piece on
     */
    public Flag(Player owner, Square square) {
        // Fixing rank to -1 since any piece can capture the flag
        super(owner, square, -1);

    }

    @Override
    public void beCaptured() {
        super.beCaptured();
        getOwner().loseGame();
    }
}
