package stratego.pieces;

import java.util.ArrayList;
import java.util.List;

import stratego.CombatResult;
import stratego.Player;
import stratego.Square;


public abstract class Piece {
    private Player owner;
    private Square square;
    private int rank;

    public Piece(Player owner, Square square, int rank) {
        this.owner = owner;
        this.rank = rank;
        square.placePiece(this);
        this.square = square;
    }

    
    /** 
     * Method to move the Piece into another square on the board
     * @param toSquare square to move into
     */
    public void move(Square toSquare) {
        this.square.removePiece();
        this.square = toSquare;
        this.square.placePiece(this);
    }

    
    /** 
     * Method to attack another Piece
     * @param targetSquare square to attack
     */
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

    
    /** 
     * Method evaluating attack result
     * @param targetPiece
     * @return CombatResult
     */
    public CombatResult resultWhenAttacking(Piece targetPiece) {
        if ((targetPiece.getRank() == this.rank) || (targetPiece.getRank() == 0)) {
            return CombatResult.DRAW;
        }
        if (targetPiece.getRank() > this.rank) {
            return CombatResult.LOSE;
        }
        return CombatResult.WIN;
    }

    
    /** 
     * Method triggering capture on another Piece
     * (though a Square it's located on)
     * @param targetSquare
     */
    public void capture(Square targetSquare) {
        targetSquare.getPiece().beCaptured();
    }

    
    /** 
     * Method triggering capture on another Piece
     * @param targetPiece
     */
    public void capture(Piece targetPiece) {
        targetPiece.beCaptured();
    }

    /** 
     * Method triggering capture on this Piece
     */
    public void beCaptured() {
        this.square.removePiece();
        this.square = null;
    }

    
    /** 
     * Getter method for the Square this Piece is placed on
     * @return Square
     */
    public Square getSquare() {
        return this.square;
    }

    
    /** 
     * Getter method for the Player this Piece belongs to
     * @return Player
     */
    public Player getOwner() {
        return this.owner;
    }

    
    /** 
     * Getter method for the Rank of this Piece
     * @return int
     */
    public int getRank() {
        return this.rank;
    }

    
    /** 
     * Method returning move options for this Piece
     * @return List<Square>
     */
    public List<Square> getLegalMoves() {
        // can move 1 space forward, backward, and sideways into empty non-water squares
        List<Square> legalMoves = new ArrayList<>();

        for (Square move : this.square.getNeighbours().values()) {
            if (move.canBeEntered()) {
                legalMoves.add(move);
            }
        }
        return legalMoves;
    };

    
    /** Method returning attack options for this Piece
     * @return List<Square>
     */
    public List<Square> getLegalAttacks() {
        List<Square> legalAttacks = new ArrayList<>();

        for (Square move : square.getNeighbours().values()) {
            Piece potentialTarget = move.getPiece();
            if (potentialTarget != null) {
                Player pieceOwner = potentialTarget.getOwner();
                if (pieceOwner != this.owner) {
                    legalAttacks.add(move);
                }
            }
        }
        return legalAttacks;
    }
}
