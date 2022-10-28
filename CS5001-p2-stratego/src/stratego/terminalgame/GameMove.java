package stratego.terminalgame;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import stratego.Game;
import stratego.Player;
import stratego.Square;
import stratego.pieces.Bomb;
import stratego.pieces.Miner;
import stratego.pieces.Piece;
import stratego.pieces.Scout;
import stratego.pieces.StepMover;

public class GameMove {
    private String move;
    private int currentRow;
    private int currentCol;
    private int targetRow;
    private int targetCol;

    private static int[] PLAYER_1_ROWS = { 0, 1, 2, 3 };
    private static int[] PLAYER_2_ROWS = { 6, 7, 8, 9 };
    private static int[] PLAYER_COLS = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };

    public static GameMove parseInput(String input) {
        GameMove gameMove = new GameMove();
        String[] splitOne = input.split(" ");
        String move = splitOne[0].toLowerCase();
        gameMove.move = move;

        String[] currentCoords = splitOne[1].split(",");
        gameMove.currentRow = Integer.parseInt(currentCoords[0]);
        gameMove.currentCol = Integer.parseInt(currentCoords[1]);

        if (move.equals("attack") || move.equals("move")) {
            String[] targetCoords = splitOne[2].split(",");
            gameMove.targetRow = Integer.parseInt(targetCoords[0]);
            gameMove.targetCol = Integer.parseInt(targetCoords[1]);
        }
        return gameMove;
    }

    public static Game populateBoard(Game game) {
        System.out.println("Populating the board");
        // Randomly assign pieces to a board for each player
        // player one range [0,0]->[4,9]
        // player two range [6,0]->[9,9]
        List<Integer> playerOneColCoords = generateCoordsList(PLAYER_COLS, 4);
        List<Integer> playerTwoColCoords = generateCoordsList(PLAYER_COLS, 4);
        List<Integer> playerOneRowCoords = generateCoordsList(PLAYER_1_ROWS, 10);
        List<Integer> playerTwoRowCoords = generateCoordsList(PLAYER_2_ROWS, 10);

        assignPiece(game.getPlayer(0), game, playerOneRowCoords, playerOneColCoords);
        assignPiece(game.getPlayer(1), game, playerTwoRowCoords, playerTwoColCoords);

        return game;
    }

    public static List<Integer> generateCoordsList(int[] listCoords, int n) {
        List<Integer> list = Arrays.stream(listCoords).boxed().toList();
        return list.stream().flatMap(i -> Collections.nCopies(n, i).stream())
                .collect(Collectors.toList());
    }

    public static void assignPiece(Player player, Game game, List<Integer> rows, List<Integer> cols) {

        for (int i = 0; i < 8; i++) {
            int x = getRandomInt(0, rows.size() - 1);
            int y = getRandomInt(0, cols.size() - 1);
            Square square = game.getSquare(rows.get(x), cols.get(y));
            // Square square = getRandomSquare(game, rows, cols);
            // if (!square.canBeEntered()) {
            // x = getRandomInt(0, rows.size() - 1);
            // y = getRandomInt(0, cols.size() - 1);
            // square = game.getSquare(rows.get(x), cols.get(y));
            // }
            try {
                new Scout(player, square);
            } catch (IllegalArgumentException e) {
                continue;
            }

            rows.remove(x);
            cols.remove(y);
        }
        for (int i = 0; i < 6; i++) {
            int x = getRandomInt(0, rows.size() - 1);
            int y = getRandomInt(0, cols.size() - 1);

            Square square = game.getSquare(rows.get(x), cols.get(y));
            // if (!square.canBeEntered()) {
            // square = getRandomSquare(game, rows, cols);
            // }
            if (!square.canBeEntered()) {
                x = getRandomInt(0, rows.size() - 1);
                y = getRandomInt(0, cols.size() - 1);
                square = game.getSquare(rows.get(x), cols.get(y));
            }
            try {
                new Bomb(player, square);
            } catch (IllegalArgumentException e) {
                continue;
            }

            rows.remove(x);
            cols.remove(y);
        }
        for (int i = 0; i < 5; i++) {
            int x = getRandomInt(0, rows.size() - 1);
            int y = getRandomInt(0, cols.size() - 1);

            Square square = game.getSquare(rows.get(x), cols.get(y));
            // while (square.canBeEntered()) {
            // square = getRandomSquare(game, rows, cols);
            // }
            if (!square.canBeEntered()) {
                x = getRandomInt(0, rows.size() - 1);
                y = getRandomInt(0, cols.size() - 1);
                square = game.getSquare(rows.get(x), cols.get(y));
            }
            try {
                new Miner(player, square);
            } catch (IllegalArgumentException e) {
                continue;
            }

            rows.remove(x);
            cols.remove(y);
        }
        // Place Sargents
        placeStepMover(player, game, rows, cols, 4, 4);
        // for (int i = 0; i < 4; i++) {
        // int x = getRandomInt(0, rows.size());
        // int y = getRandomInt(0, cols.size());

        // Square square = game.getSquare(x, y);
        // square.placePiece(new StepMover(player, square, 4));

        // rows.remove(x);
        // cols.remove(y);
        // }
        // Place Lieutenants
        placeStepMover(player, game, rows, cols, 4, 5);
        // for (int i = 0; i < 4; i++) {
        // int x = getRandomInt(0, rows.size());
        // int y = getRandomInt(0, cols.size());

        // Square square = game.getSquare(x, y);
        // square.placePiece(new StepMover(player, square, 4));

        // rows.remove(x);
        // cols.remove(y);
        // }
        // Place Captains
        placeStepMover(player, game, rows, cols, 4, 6);
        // for (int i = 0; i < 4; i++) {
        // int x = getRandomInt(0, rows.size());
        // int y = getRandomInt(0, cols.size());

        // Square square = game.getSquare(x, y);
        // square.placePiece(new StepMover(player, square, 4));

        // rows.remove(x);
        // cols.remove(y);
        // }
        // Place Majors
        placeStepMover(player, game, rows, cols, 3, 7);
        // Place Colonel
        placeStepMover(player, game, rows, cols, 2, 8);
        // Place General
        placeStepMover(player, game, rows, cols, 1, 9);
        // Place Marshal
        placeStepMover(player, game, rows, cols, 1, 10);

    }

    public static void printPieceLocations(Game game) {
        Player p1 = game.getPlayer(0);
        String[][] printArray = new String[10][10];
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                Piece piece = game.getSquare(i, j).getPiece();
                if (game.getSquare(i, j).getPiece() == null) {
                    printArray[i][j] = "  ";
                } else if (piece.getOwner() == p1) {
                    printArray[i][j] = "P1";
                } else {
                    printArray[i][j] = "P2";
                }
            }
        }
        for (String[] row : printArray) {
            printRow(row);
        }
    }

    public static void printRow(String[] row) {
        for (String i : row) {
            System.out.print(i);
            System.out.print("\t");
        }
        System.out.println();
    }

    public static void placeStepMover(Player player, Game game, List<Integer> rows, List<Integer> cols, int n,
            int rank) {
        for (int i = 0; i < n; i++) {
            // generate random number to represent the index
            int x = getRandomInt(0, rows.size() - 1);
            int y = getRandomInt(0, cols.size() - 1);

            Square square = game.getSquare(rows.get(x), cols.get(y));
            if (!square.canBeEntered()) {
                x = getRandomInt(0, rows.size() - 1);
                y = getRandomInt(0, cols.size() - 1);
                square = game.getSquare(rows.get(x), cols.get(y));
            }
            // while (square.canBeEntered()) {
            // square = getRandomSquare(game, rows, cols);
            // }
            // assign a piece to the drawn coordinates
            try {
                new StepMover(player, square, rank);
            } catch (IllegalArgumentException e) {
                continue;
            }
            // remove form the array
            rows.remove(x);
            cols.remove(y);
        }
    }

    public static Square getRandomSquare(Game game, List<Integer> rows, List<Integer> cols) {
        int x = getRandomInt(0, rows.size() - 1);
        int y = getRandomInt(0, cols.size() - 1);
        Square square = game.getSquare(rows.get(x), cols.get(y));
        return square;
    }

    public static int getRandomInt(int lower, int upper) {
        // fixing seed for deterministic results
        Random random = new Random();
        return random.nextInt(upper - lower + 1) + lower;
    }

    public String getMove() {
        return this.move;
    }

    public int getCurrentRow() {
        return this.currentRow;
    }

    public int getCurrentCol() {
        return this.currentCol;
    }

    public int getTargetRow() {
        return this.targetRow;
    }

    public int getTargetCol() {
        return this.targetCol;
    }

    public void setMove(String move) {
        this.move = move;
    }

    public void setCurrentRow(int currentRow) {
        this.currentRow = currentRow;
    }

    public void setCurrentCol(int currentCol) {
        this.currentCol = currentCol;
    }

    public void setTargetRow(int targetRow) {
        this.targetRow = targetRow;
    }

    public void setTargetCol(int targetCol) {
        this.targetCol = targetCol;
    }
}
