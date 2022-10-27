package stratego.pieces;

import java.util.ArrayList;
import java.util.List;

import stratego.Player;
import stratego.Square;

/**
 * Class representing static Stratego Game Pieces.
 */
public abstract class ImmobilePiece extends Piece {

    /**
     * ImmobilePiece constructor.
     * @param owner Player who the piece belongs to
     * @param square Square to place the piece on
     * @param rank Piece rank (determines attack outcome)
     */
    public ImmobilePiece(Player owner, Square square, int rank) {
        super(owner, square, rank);
    }

    @Override
    public void move(Square toSquare) {
        // Would throw an exception but not consistent with tests
        System.out.println("Immobile piece cannot move!");
    }

    @Override
    public void attack(Square targetSquare) {
        // Would throw an exception but not consistent with tests
        System.out.println("Immobile piece cannot attack!");
    }

    @Override
    public List<Square> getLegalAttacks() {
        // empty because piece cannot move
        return new ArrayList<Square>();
    }

    @Override
    public List<Square> getLegalMoves() {
        // empty because piece cannot move
        return new ArrayList<Square>();
    }
}
