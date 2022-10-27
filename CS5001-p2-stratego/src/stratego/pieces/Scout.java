package stratego.pieces;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import stratego.Direction;
import stratego.Player;
import stratego.Square;

/**
 * Class representing Scout Stratego Game Piece.
 */
public class Scout extends Piece {

    /**
     * Scout constructor.
     * @param owner Player who the piece belongs to
     * @param square Square to place the piece on
     */
    public Scout(Player owner, Square square) {
        super(owner, square, 2);
    }

    @Override
    public List<Square> getLegalMoves() {
        List<Square> legalMoves = new ArrayList<>();
        Map<Direction, Square> movesMap = getSquare().getNeighbours();

        for (Map.Entry<Direction, Square> move : movesMap.entrySet()) {

            Direction dir = move.getKey();
            Square next = move.getValue();
            while (next.canBeEntered()) {
                try {
                    legalMoves.add(next);
                    next = next.moveByOne(dir);
                } catch (IndexOutOfBoundsException e) {
                    // Reached board edge
                    break;
                }
            }
        }
        return legalMoves;
    }
}
