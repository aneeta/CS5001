package stratego.pieces;

import stratego.Player;
import stratego.Square;

import stratego.exceptions.IllegalArgumentException;

public class StepMover extends Piece {
    public StepMover(Player owner, Square square, int rank) {
        super(owner, square, rank);
    }
}
