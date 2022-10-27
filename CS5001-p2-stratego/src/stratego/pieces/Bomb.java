package stratego.pieces;

import stratego.Player;
import stratego.Square;


public class Bomb extends ImmobilePiece {
    public Bomb(Player owner, Square square) {
        super(owner, square, 0);
    }
}
