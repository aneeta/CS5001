package stratego.pieces;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import stratego.Game;
import stratego.Player;
import stratego.Square;

public class Scout extends Piece {

    public Scout(Player owner, Square square) {
        super(owner, square, 2);
    }

    // @Override
    // public void move(Square toSquare) {
    //     // TODO Auto-generated method stub
    //     super.move(toSquare);
    // }

    @Override
    public List<Square> getLegalMoves() {
        List<Square> legalMoves = super.getLegalMoves();
        Map<String, Square> movesMap = getSquare().getNeighbours();

        Game game = getSquare().getGame();

        for (Map.Entry<String, Square> move : movesMap.entrySet()) {
            int col = move.getValue().getCol();
            int row = move.getValue().getRow();
            Square next;
            if (move.getKey() == "LEFT" && legalMoves.contains(move.getValue())) {
                col--;
                next = game.getSquare(row, col);
                while ((col > 0) && (next.canBeEntered())) {
                    legalMoves.add(next);
                    col--;
                    next = game.getSquare(row, col);
                }
            }
            else if (move.getKey() == "RIGHT" && legalMoves.contains(move.getValue())) {
                col++;
                next = game.getSquare(row, col);
                while ((col < 9) && (next.canBeEntered())) {
                    legalMoves.add(next);
                    col++;
                    next = game.getSquare(row, col);
                    
                }
            }
            else if (move.getKey() == "UP" && legalMoves.contains(move.getValue())) {
                row--;
                next = game.getSquare(row, col);
                while ((row > 0) && (next.canBeEntered())) {
                    legalMoves.add(next);
                    row--;
                    next = game.getSquare(row, col);
                }
            }
            else if (move.getKey() == "DOWN" && legalMoves.contains(move.getValue())) {
                row++;
                next = game.getSquare(row, col);
                while ((row < 9) && (next.canBeEntered())) {
                    legalMoves.add(next);
                    row++;
                    next = game.getSquare(row, col);
                }
            }
        }
        // List<Square> uniqueMoves = new ArrayList<Square>();
        // for (Square i : legalMoves) {
        //     if (!uniqueMoves.contains(i)) {
        //         uniqueMoves.add(i);
        //     }
        // }
        return legalMoves;
    }
}
