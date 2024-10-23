package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private final ChessGame.TeamColor color;
    private final PieceType type;
    ArrayList<ChessMove> validMoves = new ArrayList<>();

    public ChessPiece(ChessGame.TeamColor pieceColor, PieceType type) {
        this.color = pieceColor;
        this.type = type;
    }

    public ChessPiece(ChessPiece piece) {
        this.color = piece.getTeamColor();
        this.type = piece.getPieceType();
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return color;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }


    private boolean generateMovesInDirection(ChessBoard board, int startRow, int startCol, int rowChange, int colChange, boolean repeat, boolean pawnMode) {
        int row = startRow + rowChange;
        int col = startCol + colChange;
        ChessPosition beingScanned;
        ChessPiece encounter;
        if (row >= 1 && row <= 8 && col >= 1 && col <= 8) {
            beingScanned = new ChessPosition(row, col);
            encounter = board.getPiece(beingScanned);
            if (encounter == null && (!pawnMode || colChange == 0)) {
                //System.out.println("added a move for being empty");
                if(pawnMode && (row == 1 || row == 8)){
                    validMoves.add(new ChessMove(new ChessPosition(startRow, startCol), beingScanned, PieceType.QUEEN));
                    validMoves.add(new ChessMove(new ChessPosition(startRow, startCol), beingScanned, PieceType.ROOK));
                    validMoves.add(new ChessMove(new ChessPosition(startRow, startCol), beingScanned, PieceType.BISHOP));
                    validMoves.add(new ChessMove(new ChessPosition(startRow, startCol), beingScanned, PieceType.KNIGHT));
                }
                else{
                    validMoves.add(new ChessMove(new ChessPosition(startRow, startCol), beingScanned, null));
                }
                if (repeat){
                    //System.out.println(rowChange + " == " + colChange);
                    if(rowChange >= 1){ rowChange++;}
                    else if(rowChange <= -1){ rowChange--;}
                    if(colChange >= 1){ colChange++;}
                    else if(colChange <= -1){ colChange--;}
                    //System.out.println(rowChange + " == " + colChange);
                    this.generateMovesInDirection(board, startRow, startCol, rowChange, colChange, true, pawnMode);
                }
                return true;
            } else {
                if (encounter != null && encounter.getTeamColor() != this.color && (!pawnMode || colChange != 0)) {
                    //System.out.println("added a move for being an enemy");
                    if(pawnMode && (row == 1 || row == 8)){
                        validMoves.add(new ChessMove(new ChessPosition(startRow, startCol), beingScanned, PieceType.QUEEN));
                        validMoves.add(new ChessMove(new ChessPosition(startRow, startCol), beingScanned, PieceType.ROOK));
                        validMoves.add(new ChessMove(new ChessPosition(startRow, startCol), beingScanned, PieceType.BISHOP));
                        validMoves.add(new ChessMove(new ChessPosition(startRow, startCol), beingScanned, PieceType.KNIGHT));
                    }
                    else{
                        validMoves.add(new ChessMove(new ChessPosition(startRow, startCol), beingScanned, null));
                    }
                    return true;
                }
            }
        }
        return false;
    }


    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {

        System.out.println("pieceMoves() called");

        validMoves = new ArrayList<>();

        int row = myPosition.getRow(); //quick access to my cords
        int col = myPosition.getColumn(); //quick access to my cords

        ChessPosition beingScanned; //the position being scanned in the script
        ChessMove newMove; //a new move made from the position beingScanned

        ChessPiece encounter; //the piece located at the position beingScanned, if any.



        if(this.type == PieceType.ROOK){
            //System.out.println("I'm a Rook");
            this.generateMovesInDirection(board, row, col, 1, 0, true, false);
            this.generateMovesInDirection(board, row, col, -1, 0, true, false);
            this.generateMovesInDirection(board, row, col, 0, -1, true, false);
            this.generateMovesInDirection(board, row, col, 0, 1, true, false);

        }

        if(this.type == PieceType.BISHOP){
            //System.out.println("I'm a Bishop");
            this.generateMovesInDirection(board, row, col, 1, 1, true, false);
            this.generateMovesInDirection(board, row, col, -1, 1, true, false);
            this.generateMovesInDirection(board, row, col, 1, -1, true, false);
            this.generateMovesInDirection(board, row, col, -1, -1, true, false);

        }

        if(this.type == PieceType.QUEEN){
            this.generateMovesInDirection(board, row, col, 1, 1, true, false);
            this.generateMovesInDirection(board, row, col, -1, 1, true, false);
            this.generateMovesInDirection(board, row, col, 1, -1, true, false);
            this.generateMovesInDirection(board, row, col, -1, -1, true, false);
            this.generateMovesInDirection(board, row, col, 1, 0, true, false);
            this.generateMovesInDirection(board, row, col, -1, 0, true, false);
            this.generateMovesInDirection(board, row, col, 0, -1, true, false);
            this.generateMovesInDirection(board, row, col, 0, 1, true, false);

        }

        if(this.type == PieceType.KING){
            this.generateMovesInDirection(board, row, col, 1, 1, false, false);
            this.generateMovesInDirection(board, row, col, -1, 1, false, false);
            this.generateMovesInDirection(board, row, col, 1, -1, false, false);
            this.generateMovesInDirection(board, row, col, -1, -1, false, false);
            this.generateMovesInDirection(board, row, col, 1, 0, false, false);
            this.generateMovesInDirection(board, row, col, -1, 0, false, false);
            this.generateMovesInDirection(board, row, col, 0, -1, false, false);
            this.generateMovesInDirection(board, row, col, 0, 1, false, false);
        }

        if(this.type == PieceType.PAWN){
            //System.out.println("I'm a Pawn");

            if(color == ChessGame.TeamColor.WHITE){
                boolean notBlocked = this.generateMovesInDirection(board, row, col, 1, 0, false, true);
                if(row == 2 && notBlocked) {
                    this.generateMovesInDirection(board, row, col, 2, 0, false, true);
                }
                this.generateMovesInDirection(board, row, col, 1, 1, false, true);
                this.generateMovesInDirection(board, row, col, 1, -1, false, true);
            }
            else{
                boolean notBlocked = this.generateMovesInDirection(board, row, col, -1, 0, false, true);
                if(row == 7 && notBlocked) {
                    this.generateMovesInDirection(board, row, col, -2, 0, false, true);
                }
                this.generateMovesInDirection(board, row, col, -1, 1, false, true);
                this.generateMovesInDirection(board, row, col, -1, -1, false, true);
            }

        }

        if(this.type == PieceType.KNIGHT){
            this.generateMovesInDirection(board, row, col, 1, 2, false, false);
            this.generateMovesInDirection(board, row, col, 2, 1, false, false);
            this.generateMovesInDirection(board, row, col, -1, 2, false, false);
            this.generateMovesInDirection(board, row, col, -2, 1, false, false);
            this.generateMovesInDirection(board, row, col, 1, -2, false, false);
            this.generateMovesInDirection(board, row, col, 2, -1, false, false);
            this.generateMovesInDirection(board, row, col, -1, -2, false, false);
            this.generateMovesInDirection(board, row, col, -2, -1, false, false);
        }



        return validMoves;



    }


    @Override
    public boolean equals(Object o) {
        if (this == o){
            return true;
        }
        if (o == null || getClass() != o.getClass()){
            return false;
        }

        ChessPiece that = (ChessPiece) o;
        return color == that.color && type == that.type;
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(color);
        result = 31 * result + Objects.hashCode(type);
        return result;
    }

    @Override
    public String toString() {
        return "[" + color + " " + type + "]";
    }

}
