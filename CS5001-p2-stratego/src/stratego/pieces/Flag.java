package stratego.pieces;

import stratego.Player;
import stratego.Square;

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
