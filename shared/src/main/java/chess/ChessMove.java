package chess;

import java.util.Objects;

/**
 * Represents moving a chess piece on a chessboard
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessMove {

    private final ChessPosition startPosition;
    private final ChessPosition endPosition;
    private final ChessPiece.PieceType promotionPiece;

    public ChessMove(ChessPosition startPosition, ChessPosition endPosition,
                     ChessPiece.PieceType promotionPiece) {
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.promotionPiece = promotionPiece;
    }

    /**
     * @return ChessPosition of starting location
     */
    public ChessPosition getStartPosition() {
        //System.out.println("getStartPosition() called");
        return startPosition;
    }

    /**
     * @return ChessPosition of ending location
     */
    public ChessPosition getEndPosition() {
        //System.out.println("getEndPosition() called");
        return endPosition;
    }

    /**
     * Gets the type of piece to promote a pawn to if pawn promotion is part of this
     * chess move
     *
     * @return Type of piece to promote a pawn to, or null if no promotion
     */
    public ChessPiece.PieceType getPromotionPiece() {
        return promotionPiece;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(startPosition.getRow());
        switch(startPosition.getColumn()){
            case 1 -> s.append("A");
            case 2 -> s.append("B");
            case 3 -> s.append("C");
            case 4 -> s.append("D");
            case 5 -> s.append("E");
            case 6 -> s.append("F");
            case 7 -> s.append("G");
            case 8 -> s.append("H");
            default -> s.append(startPosition.getColumn());
        }
        s.append(" to ");
        s.append(endPosition.getRow());
        switch(endPosition.getColumn()){
            case 1 -> s.append("A");
            case 2 -> s.append("B");
            case 3 -> s.append("C");
            case 4 -> s.append("D");
            case 5 -> s.append("E");
            case 6 -> s.append("F");
            case 7 -> s.append("G");
            case 8 -> s.append("H");
            default -> s.append(endPosition.getColumn());
        }
        if (promotionPiece != null){
            s.append(" with promotion to ").append(getPromotionPiece());
        }
        return s.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        };
        if (o == null || getClass() != o.getClass()){
            return false;
        }

        ChessMove chessMove = (ChessMove) o;
        return Objects.equals(startPosition, chessMove.startPosition)
                && Objects.equals(endPosition, chessMove.endPosition)
                && promotionPiece == chessMove.promotionPiece;
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(startPosition);
        result = 31 * result + Objects.hashCode(endPosition);
        result = 31 * result + Objects.hashCode(promotionPiece);
        return result;
    }
}
