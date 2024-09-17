package chess;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private final ChessGame.TeamColor pieceColor;
    private final PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
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
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }


    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        System.out.println("pieceMoves() called");

        ArrayList<ChessMove> moves = new ArrayList<>();
        ChessPosition end = null;

        if (this.type == PieceType.BISHOP){
            System.out.println("this is a bishop");
            end = new ChessPosition(1, 1);
            moves.add(new ChessMove(myPosition, end));
        }
        else if (this.type == PieceType.KING){
            System.out.println("this is a king");
        }
        else if (this.type == PieceType.ROOK){
            System.out.println("this is a rook");
        }
        else if (this.type == PieceType.PAWN){
            System.out.println("this is a pawn");
        }
        else if (this.type == PieceType.KNIGHT){
            System.out.println("this is a knight");
        }
        else if (this.type == PieceType.QUEEN){
            System.out.println("this is a queen");
        }
        return moves;
    }

    @Override
    public String toString() {
        return "ChessPiece{" +
                "pieceColor=" + pieceColor +
                ", type=" + type +
                '}';
    }

}
