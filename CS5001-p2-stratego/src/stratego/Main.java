package stratego;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

import stratego.*;
import stratego.pieces.*;
import stratego.terminalgame.GameMove;
import stratego.terminalgame.Move;

public class Main {

    public static void main(String[] args) {
        String nameP1, nameP2;

        try {
            if (args[0] != null && args[1] != null) {
                System.out.printf(
                        "Player 1: %s\nPlayer 2: %s\n",
                        args[0], args[1]);
                nameP1 = args[0];
                nameP2 = args[1];
            } else {
                nameP1 = "Unknown Name";
                nameP2 = "Unknown Name";
            }

        } catch (ArrayIndexOutOfBoundsException e) {
            nameP1 = "Unknown Name";
            nameP2 = "Unknown Name";
        }

        Player p1 = new Player(nameP1, 0);
        Player p2 = new Player(nameP2, 1);

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
            // System.out.printf("Round %d - Player %d's turn: \n", round, round % 2 + 1);
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

        // game play function
        // while getWinner is null
        // let players input stuff to the terminal
        // list moves and attacks for a piece on a given square
        // move/attack by giving a target square

        Square land, land2, land3, land4, water, water2;
        Piece sergeant, general, general2;
        Player michael, oz;

        land = new Square(null, 1, 2, false); // null game, but should still work
        land2 = new Square(null, 1, 3, false);
        land3 = new Square(null, 4, 3, false);
        land4 = new Square(null, 4, 9, false);
        water = new Square(null, 3, 1, true);
        water2 = new Square(null, 4, 1, true);

        michael = new Player("Michael", 0);
        oz = new Player("Ozgur", 1);

        sergeant = new StepMover(michael, land, 4); // weaker
        general = new StepMover(oz, land3, 9); // stronger
        general2 = new StepMover(michael, land4, 9);

        // general attacks sergeant (legality not checked)
        general.attack(land);

        // the general defeats the sergeant
        // the general has moved to the sergeant's square

        // Player p0 = new Player("Michael", 0);
        // Player p1 = new Player("Ozgur", 1);
        // Game game = new Game(p0, p1);
        // Piece marshal = new StepMover(p0, game.getSquare(1, 4), 10);
        // Piece sergeant = new StepMover(p0, game.getSquare(0, 2), 4);
        // Piece flag = new Flag(p0, game.getSquare(2, 5));
        // Piece scout = new Scout(p1, game.getSquare(5, 4));
        // List<Square> moves = scout.getLegalMoves();
        // List<Square> attacks = scout.getLegalAttacks();

        // Player p0 = new Player("Michael", 0);
        // Player p1 = new Player("Ozgur", 1);
        // Game game = new Game(p0, p1);
        // Piece marshal = new StepMover(p0, game.getSquare(0, 3), 10);
        // Piece sergeant = new StepMover(p1, game.getSquare(0, 2), 4);
        // Piece captain = new StepMover(p1, game.getSquare(1, 2), 6);

        // List<Square> moves = marshal.getLegalMoves();
        // List<Square> attacks = marshal.getLegalAttacks();
    }

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

        } else if (move.getMove().equals("getattacks")) {
            List<Square> attackList = currentPiece.getLegalAttacks();
            System.out.printf(
                    "Possible attacks for a piece on (%d,%d) square: ",
                    move.getCurrentRow(), move.getCurrentCol());
            for (Square s : attackList) {
                System.out.printf(" %d,%d ", s.getRow(), s.getCol());
            }
            System.out.println("done");

        } else if (move.getMove().equals("getmoves")) {
            List<Square> moveList = currentPiece.getLegalMoves();
            System.out.printf(
                    "Possible moves for a piece on (%d,%d) square: ",
                    move.getCurrentRow(), move.getCurrentCol());
            for (Square s : moveList) {
                System.out.printf(" %d,%d ", s.getRow(), s.getCol());
            }
            System.out.println("done");
        }
        GameMove.printPieceLocations(game);
    }
}
