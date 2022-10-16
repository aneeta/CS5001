package stratego.pieces;

import java.util.ArrayList;
import java.util.List;

import stratego.CombatResult;
import stratego.Player;
import stratego.Square;

import stratego.exceptions.IllegalArgumentException;


public abstract class Piece {
    private Player owner;
    private Square square;
    private int rank;

    public Piece(Player owner, Square square, int rank) {
        this.owner = owner;
        this.square = square;
        this.rank = rank;

        square.placePiece(this);
    }

    public void move(Square toSquare) {
        square.removePiece();
        square = toSquare;
        square.placePiece(this);
    }

    public void attack(Square targetSquare) {
        CombatResult attackRes = resultWhenAttacking(targetSquare.getPiece());
        // if win, move into targetSquare
        if (attackRes == CombatResult.WIN) {
            capture(targetSquare);
            targetSquare.placePiece(this);
            this.square.removePiece();
            this.square = targetSquare;
        }
        else {
            // if lost, destroyed
            beCaptured();
            // both destroyed
            if (attackRes == CombatResult.DRAW) {
                capture(targetSquare);
            }
        }
    }

    public CombatResult resultWhenAttacking(Piece targetPiece) {
        if ((targetPiece.getRank() == this.rank) || (targetPiece.getRank() == 0)) {return CombatResult.DRAW;}
        if (targetPiece.getRank() > this.rank) {return CombatResult.LOSE;}
        return CombatResult.WIN;
    }

    public void capture(Square targetSquare) {
        targetSquare.getPiece().beCaptured();
    }

    public void capture(Piece targetPiece) {
        targetPiece.beCaptured();
    }

    public void beCaptured() {
        this.square.removePiece();
        this.square = null;
    }

    public Square getSquare() {
        return square;
    }

    public Player getOwner() {
        return owner;
    }

    public int getRank() {
        return rank;
    }

    public List<Square> getLegalMoves() {
        // can move 1 space forward, backward, and sideways into empty non-water squares
        List<Square> legalMoves = new ArrayList<>();

        for (Square move : square.getNeighbours().values()) {
            if (move.canBeEntered()) { legalMoves.add(move); }
        }

        return legalMoves;

    };

    public List<Square> getLegalAttacks() {
        List<Square> legalAttacks = new ArrayList<>();

        for (Square move : square.getNeighbours().values()) {
            Piece potentialTarget = move.getPiece();
            if (potentialTarget != null) {
                Player pieceOwner = potentialTarget.getOwner();
                if (pieceOwner != owner) { legalAttacks.add(move); }
            }
        }
        return legalAttacks;
    }
}
