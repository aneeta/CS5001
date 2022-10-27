package stratego.pieces;

import stratego.Player;
import stratego.Square;

/**
 * Class representing the Bomb Stratego Game Piece.
 */
public class Bomb extends ImmobilePiece {

    /**
     * Game constructor.
     * @param owner Player who the piece belongs to
     * @param square Square to place the piece on
     */
    public Bomb(Player owner, Square square) {
        super(owner, square, 0);
    }
}
