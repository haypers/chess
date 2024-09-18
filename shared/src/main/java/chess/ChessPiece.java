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
        //select piece type


        //----------------------------BISHOP------------------------------
        if (this.type == PieceType.BISHOP){
            System.out.println("this is a bishop");

            //check all up and to the right positions
            for (int i = 1; i <= 7; i++) {

                //check if out of bounds
                if(myPosition.getRow()+i <= 8 && myPosition.getColumn()+i <= 8) {
                    System.out.println("Checking up right space " + i);

                    //check if colliding with a piece
                    if (board.getPiece(new ChessPosition(myPosition.getRow()+i, myPosition.getColumn()+i)) != null) {
                        System.out.println("Piece Collision");

                        //check if piece is friendly
                        if(board.getPiece(new ChessPosition(myPosition.getRow()+i, myPosition.getColumn()+i)).getTeamColor() == this.pieceColor){
                            System.out.println("Friendly Collision");
                            break;
                        }
                        //piece is not friendly
                        else {
                            System.out.println("Enemy Collision");
                            end = new ChessPosition(myPosition.getRow()+i, myPosition.getColumn()+i);
                            moves.add(new ChessMove(myPosition, end));
                            break;
                        }
                    }
                    //valid move, no collision yet
                    else{
                        end = new ChessPosition(myPosition.getRow()+i, myPosition.getColumn()+i);
                        moves.add(new ChessMove(myPosition, end));
                    }

                }

                else{
                    System.out.println("Wall Collision");
                    break;
                }
            }

            //check all down and to the right positions
            for (int i = 1; i <= 7; i++) {

                //check if out of bounds
                if(myPosition.getRow()-i >= 1 && myPosition.getColumn()+i <= 8) {
                    System.out.println("Checking down right space " + i);

                    //check if colliding with a piece
                    if (board.getPiece(new ChessPosition(myPosition.getRow()-i, myPosition.getColumn()+i)) != null) {
                        System.out.println("Piece Collision");

                        //check if piece is friendly
                        if(board.getPiece(new ChessPosition(myPosition.getRow()-i, myPosition.getColumn()+i)).getTeamColor() == this.pieceColor){
                            System.out.println("Friendly Collision");
                            break;
                        }
                        //piece is not friendly
                        else {
                            System.out.println("Enemy Collision");
                            end = new ChessPosition(myPosition.getRow()-i, myPosition.getColumn()+i);
                            moves.add(new ChessMove(myPosition, end));
                            break;
                        }
                    }
                    //valid move, no collision yet
                    else{
                        end = new ChessPosition(myPosition.getRow()-i, myPosition.getColumn()+i);
                        moves.add(new ChessMove(myPosition, end));
                    }

                }

                else{
                    System.out.println("Wall Collision");
                    break;
                }
            }

            //check all up and to the left positions
            for (int i = 1; i <= 7; i++) {

                //check if out of bounds
                if(myPosition.getRow()+i <= 8 && myPosition.getColumn()-i >= 1) {
                    System.out.println("Checking up left space " + i);

                    //check if colliding with a piece
                    if (board.getPiece(new ChessPosition(myPosition.getRow()+i, myPosition.getColumn()-i)) != null) {
                        System.out.println("Piece Collision");

                        //check if piece is friendly
                        if(board.getPiece(new ChessPosition(myPosition.getRow()+i, myPosition.getColumn()-i)).getTeamColor() == this.pieceColor){
                            System.out.println("Friendly Collision");
                            break;
                        }
                        //piece is not friendly
                        else {
                            System.out.println("Enemy Collision");
                            end = new ChessPosition(myPosition.getRow()+i, myPosition.getColumn()-i);
                            moves.add(new ChessMove(myPosition, end));
                            break;
                        }
                    }
                    //valid move, no collision yet
                    else{
                        end = new ChessPosition(myPosition.getRow()+i, myPosition.getColumn()-i);
                        moves.add(new ChessMove(myPosition, end));
                    }

                }

                else{
                    System.out.println("Wall Collision");
                    break;
                }
            }

            //check all down and to the left positions
            for (int i = 1; i <= 7; i++) {

                //check if out of bounds
                if(myPosition.getRow()-i >= 1 && myPosition.getColumn()-i >= 1) {
                    System.out.println("Checking down left space " + i);

                    //check if colliding with a piece
                    if (board.getPiece(new ChessPosition(myPosition.getRow()-i, myPosition.getColumn()-i)) != null) {
                        System.out.println("Piece Collision");

                        //check if piece is friendly
                        if(board.getPiece(new ChessPosition(myPosition.getRow()-i, myPosition.getColumn()-i)).getTeamColor() == this.pieceColor){
                            System.out.println("Friendly Collision");
                            break;
                        }
                        //piece is not friendly
                        else {
                            System.out.println("Enemy Collision");
                            end = new ChessPosition(myPosition.getRow()-i, myPosition.getColumn()-i);
                            moves.add(new ChessMove(myPosition, end));
                            break;
                        }
                    }
                    //valid move, no collision yet
                    else{
                        end = new ChessPosition(myPosition.getRow()-i, myPosition.getColumn()-i);
                        moves.add(new ChessMove(myPosition, end));
                    }

                }

                else{
                    System.out.println("Wall Collision");
                    break;
                }
            }

        }





        //----------------------------KING------------------------------
        else if (this.type == PieceType.KING){
            System.out.println("this is a king");
        }




        //----------------------------ROOK------------------------------
        else if (this.type == PieceType.ROOK){
            System.out.println("this is a rook");
        }




        //----------------------------PAWN------------------------------
        else if (this.type == PieceType.PAWN){
            System.out.println("this is a pawn");
        }



        //----------------------------KNIGHT------------------------------
        else if (this.type == PieceType.KNIGHT){
            System.out.println("this is a knight");
        }





        //----------------------------QUEEN------------------------------
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
