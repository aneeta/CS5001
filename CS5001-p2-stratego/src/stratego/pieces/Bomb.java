package stratego.pieces;

import stratego.Player;
import stratego.Square;

/**
 * Class representing the Bomb Stratego Game Piece.
 */
public class Bomb extends ImmobilePiece {

    public Bomb(Player owner, Square square) {
        super(owner, square, 0);
    }
}
