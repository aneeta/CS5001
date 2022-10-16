package stratego;

import java.lang.ArrayIndexOutOfBoundsException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.print.attribute.HashAttributeSet;

import stratego.pieces.Piece;

public class Square {
    private Game game;
    private int row;
    private int col;
    private boolean isWater;
    private Piece gamePiece;

    public Square(Game game, int row, int col, boolean isWater) {
        this.game = game;
        this.row = row;
        this.col = col;
        this.isWater = isWater;
    }

    public void placePiece(Piece piece) {
        if (gamePiece != null) {
            throw new IllegalArgumentException("Square already occupied!");
        }
        gamePiece = piece;
    }

    public void removePiece() {
        gamePiece = null;
    }

    public Piece getPiece() {
        return gamePiece;
    }

    public Game getGame() {
        return game;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public boolean canBeEntered() {
        return ((!isWater) && (gamePiece == null));
    }

    public Map<String, Square> getNeighbours() {
        Map<String, Square> neighbours = new HashMap<String, Square>();
        Map<String,Coords> possibleNeighbours = new HashMap<String,Coords>();
        possibleNeighbours.put("RIGHT", new Coords(row, col + 1));
        possibleNeighbours.put("LEFT", new Coords(row, col - 1));
        possibleNeighbours.put("DOWN", new Coords(row + 1, col));
        possibleNeighbours.put("UP", new Coords(row - 1, col));

        for (Map.Entry<String, Coords> entry : possibleNeighbours.entrySet()) {
            // why not catching exception??
            // try {
            //     Square nextSquare = getGame().getSquare(entry.getValue().x, entry.getValue().y);
            //     neighbours.put(entry.getKey(), nextSquare);
            // } catch (ArrayIndexOutOfBoundsException e) {}
            int i = entry.getValue().x;
            int j = entry.getValue().y;
            if ((i >= 0 && i < 10 ) && (j >= 0 && j < 10)) {
                Square nextSquare = getGame().getSquare(i, j);
                neighbours.put(entry.getKey(), nextSquare);
            }
        }
        return neighbours;
    }

}

class Coords {
    int x;
    int y;

    Coords(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
