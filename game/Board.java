import java.util.*;

public class Board {

    public static final int SIZE = 30;
    private final Square[] squares = new Square[SIZE + 1];

    private Set<Integer> newlyArrivedSpecial = new HashSet<>();

    public Board() {
        for (int i = 1; i <= SIZE; i++) {
            squares[i] = new Square(i);
        }
        squares[15].setType(SquareType.REBIRTH);
        squares[26].setType(SquareType.HAPPINESS);
        squares[27].setType(SquareType.WATER);
        squares[28].setType(SquareType.THREE_TRUTHS);
        squares[29].setType(SquareType.RE_ATOUM);
        squares[30].setType(SquareType.HORUS);
    }

    public Square getSquare(int i) {
        return squares[i];
    }

    public boolean isFinished(PlayerColor player) {
        for (int i = 1; i <= SIZE; i++) {
            if (squares[i].hasPiece(player)) return false;
        }
        return true;
    }

    public List<Move> getLegalMoves(PlayerColor player, int roll) {
        List<Move> moves = new ArrayList<>();

        for (int i = 1; i <= SIZE; i++) {
            Square from = squares[i];
            if (!from.hasPiece(player)) continue;

            int target = i + roll;
            if (i < 26 && target > 26) {
                continue;
            }

            if (from.getIndex() == 28) {
                if (roll == 3) {
                    moves.add(new Move(from));
                }
                continue;
            }

            if (from.getIndex() == 29) {
                if (roll == 2) {
                    moves.add(new Move(from));
                }
                continue;
            }

            if (from.getIndex() == 30) {
                moves.add(new Move(from));
                continue;
            }

            if (target > SIZE) {
                moves.add(new Move(from));
                continue;
            }

            Square to = squares[target];

            if (to.hasPiece(player)) continue;

            moves.add(new Move(from, to));
        }
        return moves;
    }

    public Board applyMove(Move move) {
        Board copy = this.copy();

        Square from = copy.getSquare(move.from.getIndex());
        Piece movingPiece = from.removePiece();

        if (move.isExit()) {
            return copy;
        }

        Square to = copy.getSquare(move.to.getIndex());

        if (to.hasPiece()) {
            Piece opponent = to.removePiece();
            from.setPiece(opponent);
        }

        to.setPiece(movingPiece);

        if (to.getType() == SquareType.WATER) {
            Piece fallen = to.removePiece();
            copy.sendToRebirth(fallen);
        }

        int toPos = to.getIndex();
        if (toPos == 28 || toPos == 29 || toPos == 30) {
            copy.newlyArrivedSpecial.add(toPos);
        }

        return copy;
    }

    private void sendToRebirth(Piece p) {
        if (!squares[15].hasPiece()) {
            squares[15].setPiece(p);
            return;
        }

        for (int i = 14; i >= 1; i--) {
            if (!squares[i].hasPiece()) {
                squares[i].setPiece(p);
                return;
            }
        }
    }

    
     
    public void enforcePenalty(PlayerColor player) {
        int[] special = {28, 29, 30};

        for (int pos : special) {
            Square sq = getSquare(pos);
            if (sq.hasPiece(player)) {
                if (newlyArrivedSpecial.contains(pos)) {
                    continue;
                }

                Piece piece = sq.removePiece();
                sendToRebirth(piece);
                System.out.println("Penalty: " + player + "'s piece at square " + pos +
                                   " was not moved out in time → sent back to Rebirth (15)!");
            }
        }

        newlyArrivedSpecial.clear();
    }

    public Board copy() {
        Board b = new Board();
        for (int i = 1; i <= SIZE; i++) {
            if (squares[i].hasPiece()) {
                b.squares[i].setPiece(new Piece(squares[i].getPiece().getColor()));
            }
        }
        b.newlyArrivedSpecial.addAll(this.newlyArrivedSpecial);
        return b;
    }

    public void print() {
        System.out.println("");
        System.out.println("                 SENET BOARD                 ");
        System.out.println("");

        // Row 1: 1 → 10
        System.out.print(" ");
        for (int i = 1; i <= 10; i++) {
            System.out.print(getSquareSymbol(i) + " ");
        }
        System.out.println(" ");

        // Row 2: 20 ← 11
        System.out.print(" ");
        for (int i = 20; i >= 11; i--) {
            System.out.print(getSquareSymbol(i) + " ");
        }
        System.out.println(" ");

        // Row 3: 21 → 30
        System.out.print(" ");
        for (int i = 21; i <= 30; i++) {
            System.out.print(getSquareSymbol(i) + " ");
        }
        System.out.println(" ");

        System.out.println("");

        System.out.println("  W = White (You)    B = Black (AI)    - = Empty");
        System.out.println("  Special squares:");
        System.out.println("  R = Rebirth (15)    H = Happiness (26)");
        System.out.println("  Wa = Water (27)     3 = Three Truths (28)");
        System.out.println("  A = Re-Atoum (29)   Ho = Horus (30)");
        System.out.println();
    }

    private String getSquareSymbol(int pos) {
        Square s = squares[pos];
        if (s.hasPiece()) {
            return s.getPiece().getColor() == PlayerColor.WHITE ? "W" : "B";
        }

        SquareType t = s.getType();
        return switch (t) {
            case REBIRTH       -> "R";
            case HAPPINESS     -> "H";
            case WATER         -> "Wa";   
            case THREE_TRUTHS  -> "3";    
            case RE_ATOUM      -> "A";    
            case HORUS         -> "Ho";   
            default            -> "-";
        };
    }
}