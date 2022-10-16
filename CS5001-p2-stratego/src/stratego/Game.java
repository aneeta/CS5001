package stratego;

import java.util.Arrays;

public class Game {

    public static int HEIGHT = 10;
    public static int WIDTH = 10;
    public static int[] WATER_ROWS = {4, 5};
    public static int[] WATER_COLS = {2, 3, 6, 7};

    private Player[] playerArray;
    private Player winner;
    private Square[][] board = new Square[HEIGHT][WIDTH];

    public Game(Player p0, Player p1) {
        playerArray = new Player[]{p0, p1};
        setUpBoard();
    }

    void setUpBoard() {
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                // boolean waterCol = ((Arrays.stream(WATER_COLS).anyMatch(x -> x == j))? true: false);
                boolean isWater = ((contains(WATER_ROWS, i) && contains(WATER_COLS, j)) ? true : false);
                board[i][j] = new Square(this, i, j, isWater);
            }
        }
    }

    public Player getPlayer(int playerNumber) {
        if (playerNumber < 0 || playerNumber > 1) {
            throw new IllegalArgumentException("No such player. Choose from player 0 or 1.");
        }
        return playerArray[playerNumber];
    }

    public Player getWinner() {
        // if (playerArray[0].hasLost() && playerArray[0].hasLost()) {
        //     throw new Exception("Both players lost?");
        // }
        if (playerArray[0].hasLost()) {
            winner = playerArray[1];
        }
        if (playerArray[1].hasLost()) {
            winner = playerArray[0];
        }
        return winner;
    }

    public Square getSquare(int row, int col) throws IndexOutOfBoundsException {
        if ((row < 0 || row > HEIGHT) || (col < 0 || col > WIDTH)) {
            throw new IndexOutOfBoundsException("Square beyond the board!");
        }
        return board[row][col];
    }

    public static boolean contains(final int[] list, final int idx) {
        return Arrays.stream(list).anyMatch(x -> x == idx);
    }
}
