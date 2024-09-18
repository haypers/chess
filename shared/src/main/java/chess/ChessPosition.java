package chess;

/**
 * Represents a single square position on a chess board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPosition {

    private final int row;
    private final int col;

    public ChessPosition(int row, int col) {
        this.row = row;
        this.col = col;
    }

    /**
     * @return which row this position is in
     * 1 codes for the bottom row
     */
    public int getRow() {
        return row;
    }

    /**
     * @return which column this position is in
     * 1 codes for the left row
     */
    public int getColumn() {
        return col;
    }

    @Override
    public String toString() {
        return "piece: [" + (row) + "][" + (col) + ']';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true; //check if the memory address is the same.
        if (o == null || getClass() != o.getClass()) return false; //check if the object is null or an incorrect class.


        ChessPosition that = (ChessPosition) o; //this is a typecast command! Because we allow comparisons to any object, ,not just other chess positions, we need to ensure that we can actually access the object as a chess peice object, so we are first checking if it is a chess position object, then we typecast it back into one, so we can pull values out and compare them.
        return row == that.row && col == that.col;
    }

    @Override
    public int hashCode() {
        int result = row;
        result = 31 * result + col; //should be a prime for more randomness
        return result;
    }
}
