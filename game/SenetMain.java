import java.util.*;

public class SenetMain {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("");
        System.out.println("            Welcome to SENET          ");
        System.out.println("");

        System.out.println("Select game mode:");
        System.out.println("1. Human vs AI");
        System.out.println("2. AI vs AI (Hard mode)");
        System.out.print("Enter your choice (1 or 2): ");

        int mode = scanner.nextInt();
        boolean humanVsAI = (mode == 1);
        boolean aiVsAI = (mode == 2);

        System.out.print("Enable AI Algorithm Tracing (Show node types and values)? (y/n): ");
        String traceChoice = scanner.next();
        Expectiminimax.enableTracing = traceChoice.equalsIgnoreCase("y");

        if (!humanVsAI && !aiVsAI) {
            System.out.println("Invalid input. Starting Human vs AI by default.");
            humanVsAI = true;
        }

        Board board = new Board();

        for (int i = 1; i <= 14; i += 2) {
            board.getSquare(i).setPiece(new Piece(PlayerColor.WHITE));
            board.getSquare(i + 1).setPiece(new Piece(PlayerColor.BLACK));
        }

        PlayerColor currentPlayer = PlayerColor.WHITE;
        int aiDepth = Expectiminimax.enableTracing ? 3 : (aiVsAI ? 5 : 4);

        System.out.println(humanVsAI ?
                "\nYou play WHITE (W)  AI plays BLACK (B)" :
                "\nAI WHITE vs AI BLACK (Hard mode)");

        while (!board.isFinished(PlayerColor.WHITE) && !board.isFinished(PlayerColor.BLACK)) {
            board.print();

            int roll = StickThrow.throwSticks();
            boolean isHumanTurn = humanVsAI && currentPlayer == PlayerColor.WHITE;

            System.out.println("Current turn: " + (currentPlayer == PlayerColor.WHITE ? "WHITE" : "BLACK"));
            System.out.println("Sticks result: " + roll);

            List<Move> legalMoves = board.getLegalMoves(currentPlayer, roll);

            if (legalMoves.isEmpty()) {
                System.out.println("No legal moves available -> turn skipped.");
                board.enforcePenalty(currentPlayer);
                currentPlayer = currentPlayer.opposite();
                continue;
            }

            Move chosenMove = null;

            if (isHumanTurn) {
                Set<Integer> possibleFrom = new HashSet<>();
                for (Move m : legalMoves) {
                    possibleFrom.add(m.from.getIndex());
                }
                System.out.println("Possible squares to move from: " + possibleFrom);

                while (chosenMove == null) {
                    System.out.print("Enter square number: ");
                    int selected = scanner.nextInt();

                    for (Move m : legalMoves) {
                        if (m.from.getIndex() == selected) {
                            chosenMove = m;
                            break;
                        }
                    }

                    if (chosenMove == null) {
                        System.out.println("Invalid choice. Try again.");
                    }
                }
            } else {
                System.out.println("AI (" + currentPlayer + ") is thinking...");
                
                try {
                    Thread.sleep(aiVsAI ? 100 : 1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

                chosenMove = Expectiminimax.chooseBestMove(board, currentPlayer, roll, currentPlayer, aiDepth);
            }

            if (chosenMove.isExit()) {
                System.out.println("Piece exited from square " + chosenMove.from.getIndex());
            } else {
                System.out.println("Moved from " + chosenMove.from.getIndex() +
                                   " to " + chosenMove.to.getIndex());
            }

            board = board.applyMove(chosenMove);
            board.enforcePenalty(currentPlayer);

            currentPlayer = currentPlayer.opposite();
            System.out.println();
        }

        board.print();

        System.out.println("");
        if (board.isFinished(PlayerColor.BLACK)) {
            System.out.println("BLACK WINS!");
        } else {
            System.out.println("WHITE WINS!");
        }

        System.out.println("Game finished.");
        scanner.close();
    }
}