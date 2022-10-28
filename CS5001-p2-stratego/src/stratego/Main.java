package stratego;

import java.util.List;
import java.util.Scanner;

import stratego.pieces.Piece;
import stratego.terminalgame.GameMove;

/**
 * Main method. Contains entry point to Teminal Game of Stratego.
 */
public class Main {

    /**
     * Main function.
     * Launches terminal Stratego game.
     * 
     * @param args <Name 1> <Name 2> (optional)
     */
    public static void main(String[] args) {
        String nameP1 = "Unknown Name";
        String nameP2 = "Unknown Name";

        if (args[0].length() >= 2) {
            System.out.printf(
                    "Player 1: %s\nPlayer 2: %s\n",
                    args[0], args[1]);
            nameP1 = args[0];
            nameP2 = args[1];
        }

        // instnatiate players
        Player p1 = new Player(nameP1, 0);
        Player p2 = new Player(nameP2, 1);
        // instnatiate game
        Game game = new Game(p1, p2);
        GameMove.populateBoard(game);

        Scanner input = new Scanner(System.in);

        int round = 0;
        System.out.printf("Round %d - Player %d's turn: \n", round, round % 2 + 1);
        while (input.hasNextLine()) {
            if (game.getWinner() != null) {
                System.out.printf("%s won!\n", game.getWinner().getName());
                break;
            }
            String currentInput = input.nextLine();
            // Player One plays on even turns, Player Two on odd turns
            Player player = ((round % 2 == 0) ? p1 : p2);
            System.out.printf("Player %d's command: ", round % 2 + 1);
            System.out.printf("%s\n", currentInput);

            // parse user input
            GameMove thisMove = GameMove.parseInput(currentInput);

            // play one iteration
            playOneRound(thisMove, player, game);
            // increment round
            round++;
        }
        input.close();
    }

    /**
     * Method getting one game round for a given player and move configuration.
     * 
     * @param move   move object
     * @param player associated player
     * @param game   associated game
     */
    public static void playOneRound(GameMove move, Player player, Game game) {
        Piece currentPiece = game.getSquare(
                move.getCurrentRow(), move.getCurrentCol()).getPiece();
        if (currentPiece == null) {
            System.out.println("Chosen square empty");
            return;
        }
        if (move.getMove().equals("attack")) {
            Square toSquare = game.getSquare(move.getTargetRow(), move.getTargetCol());
            currentPiece.attack(toSquare);

        } else if (move.getMove().equals("move")) {
            printMove(game, move, currentPiece);
        } else if (move.getMove().equals("getattacks")) {
            printAttacks(move, currentPiece);
        } else if (move.getMove().equals("getmoves")) {
            printMoves(move, currentPiece);
        }
        GameMove.printPieceLocations(game);
    }

    /**
     * Helper method to print a move made by a player.
     * 
     * @param game         associated game
     * @param move         move object
     * @param currentPiece move piece
     */
    public static void printMove(Game game, GameMove move, Piece currentPiece) {
        Square toSquare = game.getSquare(move.getTargetRow(), move.getTargetCol());
        try {
            System.out.printf(
                    "Moving from (%d,%d) to (%d,%d)\n",
                    currentPiece.getSquare().getRow(), currentPiece.getSquare().getCol(),
                    toSquare.getRow(), toSquare.getCol());
            currentPiece.move(toSquare);
        } catch (IllegalArgumentException e) {
            System.out.println("Square Occupied!");
            System.out.println("Warning - Rules not fully enforced!");
            return;
        }
    }

    /**
     * Helper method printing attack list.
     * 
     * @param move         move object
     * @param currentPiece move piece
     */
    public static void printAttacks(GameMove move, Piece currentPiece) {
        List<Square> attackList = currentPiece.getLegalAttacks();
        System.out.printf(
                "Possible attacks for a piece on (%d,%d) square: ",
                move.getCurrentRow(), move.getCurrentCol());
        for (Square s : attackList) {
            System.out.printf(" %d,%d ", s.getRow(), s.getCol());
        }
        System.out.println("done");
    }

    /**
     * Helper method to print a move potential moves list.
     * 
     * @param move         move object
     * @param currentPiece moved piece
     */
    public static void printMoves(GameMove move, Piece currentPiece) {
        List<Square> moveList = currentPiece.getLegalMoves();
        System.out.printf(
                "Possible moves for a piece on (%d,%d) square: ",
                move.getCurrentRow(), move.getCurrentCol());
        for (Square s : moveList) {
            System.out.printf(" %d,%d ", s.getRow(), s.getCol());
        }
        System.out.println("done");
    }
}
