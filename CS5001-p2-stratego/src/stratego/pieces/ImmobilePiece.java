package stratego.pieces;

import java.util.ArrayList;
import java.util.List;

import stratego.Player;
import stratego.Square;

/**
 * Class representing static Stratego Game Pieces.
 */
public abstract class ImmobilePiece extends Piece {
    public ImmobilePiece(Player owner, Square square, int rank) {
        super(owner, square, rank);
    }

    @Override
    public void move(Square toSquare) {
        System.out.println("Immobile piece cannot move!");
        // throw new IllegalArgumentException();
    }

    @Override
    public void attack(Square targetSquare) {
        System.out.println("Immobile piece cannot attack!");
        // throw new IllegalArgumentException("Immobile piece cannot attack!");
    }

    @Override
    public List<Square> getLegalAttacks() {
        return new ArrayList<Square>();
    }

    @Override
    public List<Square> getLegalMoves() {
        return new ArrayList<Square>();
    }
}
