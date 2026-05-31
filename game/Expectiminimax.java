import java.util.*;

public class Expectiminimax {

    private static int maxNodesVisited = 0;
    private static int minNodesVisited = 0;
    private static int chanceNodesVisited = 0;
    private static int leafNodesEvaluated = 0;

    public static boolean enableTracing = false;

    
    public static double evaluate(Board board, PlayerColor maximizer) {
        double score = 0.0;
        for (int i = 1; i <= Board.SIZE; i++) {
            Square s = board.getSquare(i);
            if (!s.hasPiece()) continue;

            double pieceValue = (double) i; 

            if (s.getPiece().getColor() == maximizer) {
                score += pieceValue;
            } else {
                score -= pieceValue;
            }
        }
        return score;
    }
    private static double solve(Board board, PlayerColor current, PlayerColor maxPlayer, int depth) {
        if (current == maxPlayer) maxNodesVisited++;
        else minNodesVisited++;

        if (depth == 0 || board.isFinished(maxPlayer) || board.isFinished(maxPlayer.opposite())) {
            leafNodesEvaluated++;
            return evaluate(board, maxPlayer);
        }

        chanceNodesVisited++; 
        double expectedValue = 0.0;

        for (Map.Entry<Integer, Double> entry : StickThrow.getProbabilities().entrySet()) {
            int roll = entry.getKey();
            double prob = entry.getValue();

            List<Move> legalMoves = board.getLegalMoves(current, roll);
            double nodeValue;

            if (legalMoves.isEmpty()) {
                nodeValue = solve(board, current.opposite(), maxPlayer, depth - 1);
            } else {
                if (current == maxPlayer) {
                    nodeValue = Double.NEGATIVE_INFINITY;
                    for (Move move : legalMoves) {
                        nodeValue = Math.max(nodeValue, solve(board.applyMove(move), current.opposite(), maxPlayer, depth - 1));
                    }
                } else {
                    nodeValue = Double.POSITIVE_INFINITY;
                    for (Move move : legalMoves) {
                        nodeValue = Math.min(nodeValue, solve(board.applyMove(move), current.opposite(), maxPlayer, depth - 1));
                    }
                }
            }
            
            expectedValue += prob * nodeValue;
        }
        return expectedValue;
    }

    public static Move chooseBestMove(Board board, PlayerColor player, int roll, PlayerColor maximizer, int depth) {
        List<Move> legalMoves = board.getLegalMoves(player, roll);
        if (legalMoves.isEmpty()) return null;

        maxNodesVisited = 0;
        minNodesVisited = 0;
        chanceNodesVisited = 0;
        leafNodesEvaluated = 0;

        double bestValue = (player == maximizer) ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY;
        Move bestMove = legalMoves.get(0);

        for (Move move : legalMoves) {
            double value = solve(board.applyMove(move), player.opposite(), maximizer, depth - 1);

            if (player == maximizer) {
                if (value > bestValue) {
                    bestValue = value;
                    bestMove = move;
                }
            } else {
                if (value < bestValue) {
                    bestValue = value;
                    bestMove = move;
                }
            }
        }

        if (enableTracing) {
            printSearchStatistics();
        }

        return bestMove;
    }

    private static void printSearchStatistics() {
        System.out.println("\n--- Expectiminimax Search Tree Statistics ---");
        System.out.println("  Max nodes (AI player) : " + maxNodesVisited);
        System.out.println("  Min nodes (Opponent) : " + minNodesVisited);
        System.out.println("  Chance nodes (Dice probabilities) : " + chanceNodesVisited);
        System.out.println("  Leaf nodes evaluated : " + leafNodesEvaluated);
        System.out.println("  Total nodes explored : " + 
                         (maxNodesVisited + minNodesVisited + chanceNodesVisited + leafNodesEvaluated));
        System.out.println();
    }
}