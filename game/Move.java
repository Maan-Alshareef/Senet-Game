public class Move {
    public final Square from;
    public final Square to;  

    public Move(Square from, Square to) {
        this.from = from;
        this.to = to;
    }

    public Move(Square from) {  
        this.from = from;
        this.to = null;
    }

    public boolean isExit() {
        return to == null;
    }
}