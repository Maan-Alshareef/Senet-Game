public class Square {
    private final int index;
    private Piece piece;
    private SquareType type = SquareType.NORMAL;

    public Square(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public boolean hasPiece() {
        return piece != null;
    }

    public boolean hasPiece(PlayerColor c) {
        return piece != null && piece.getColor() == c;
    }

    public Piece getPiece() {
        return piece;
    }

    public void setPiece(Piece p) {
        this.piece = p;
    }

    public Piece removePiece() {
        Piece p = piece;
        piece = null;
        return p;
    }

    public SquareType getType() {
        return type;
    }

    public void setType(SquareType type) {
        this.type = type;
    }
}