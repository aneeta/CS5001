package stratego;

import java.util.Arrays;

/**
 * Class representing a Game of Stratego.
 */
public class Game {

    /** Board Height. */
    public static int HEIGHT = 10;
    /** Board Width. */
    public static int WIDTH = 10;
    /** Row (X-) Coordinates of Squares containing water. */
    public static int[] WATER_ROWS = {4, 5};
    /** Column (Y-) Coordinates of Squares containing water. */
    public static int[] WATER_COLS = {2, 3, 6, 7};

    private Player[] playerArray;
    private Player winner;
    private Square[][] board = new Square[HEIGHT][WIDTH];


    /**
     * Game constructor.
     * @param p0 Player 1
     * @param p1 Player 2
     */
    public Game(Player p0, Player p1) {
        playerArray = new Player[]{p0, p1};
        setUpBoard();
    }

    void setUpBoard() {
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                boolean isWater = (
                    (contains(WATER_ROWS, i) && contains(WATER_COLS, j)) ? true : false);
                board[i][j] = new Square(this, i, j, isWater);
            }
        }
    }


    /** 
     * Getter method for a Player assosiated with the Game.
     * @param playerNumber int indicating player number (0 or 1)
     * @return Player
     */
    public Player getPlayer(int playerNumber) {
        if (playerNumber < 0 || playerNumber > 1) {
            throw new IllegalArgumentException(
                "No such player. Choose from player 0 or 1.");
        }
        return playerArray[playerNumber];
    }


    /** 
     * Method checking if either of the Players has lost.
     * Then returning the non-losing (winning) Player
     * @return Player
     */
    public Player getWinner() {
        if (playerArray[0].hasLost() && playerArray[1].hasLost()) {
            // Would maybe throw error or consult to ask if a situation
            // like this would arise/make sense
            System.out.println("Both players lost!");
            return null;
        }
        else if (playerArray[0].hasLost()) {
            winner = playerArray[1];
        }
        else if (playerArray[1].hasLost()) {
            winner = playerArray[0];
        }
        return winner;
    }


    /** 
     * Getter method for a Square on the board of this Game.
     * @param row Square row
     * @param col Square column
     * @return Square
     * @throws IndexOutOfBoundsException accessing a location outside the board
     */
    public Square getSquare(int row, int col) throws IndexOutOfBoundsException {
        if ((row < 0 || row > HEIGHT) || (col < 0 || col > WIDTH)) {
            throw new IndexOutOfBoundsException("Square beyond the board!");
        }
        return board[row][col];
    }


    /** 
     * Helper method checking if an given list contains a given number.
     * @param list int array to check
     * @param target int checked
     * @return boolean
     */
    public static boolean contains(final int[] list, final int target) {
        return Arrays.stream(list).anyMatch(x -> x == target);
    }
}
