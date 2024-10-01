package chess;

import java.util.Collection;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    public ChessGame.TeamColor turnColor = TeamColor.WHITE;

    private ChessBoard board = new ChessBoard();

    public ChessGame() {
        System.out.println("ChessGame() called");
        board.resetBoard();
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        System.out.println("getTeamTurn() called");
        return turnColor;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        System.out.println("setTeamTurn() called");
        turnColor = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        System.out.println("validMoves() called");
        if (board.getPiece(startPosition) != null){
            ChessPiece piece = new ChessPiece(board.getPiece(startPosition));
            return piece.pieceMoves(board, startPosition);
        }
        else{
            return null;
        }

    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        System.out.println("makeMove() called");
        if(board.getPiece(move.getStartPosition()) != null){
            ChessPiece pieceToMove = new ChessPiece(board.getPiece(move.getStartPosition()));
            board.addPiece(move.getEndPosition(), pieceToMove);
            board.addPiece(move.getStartPosition(), null);
            System.out.println("piece moved");
        }
        else{
            System.out.println("trying to move null!!");
            throw new InvalidMoveException("trying to move null");
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        System.out.println("isInCheck() called");
        ChessPosition king = null;
        for(int row = 1; row < 8; row ++){
            for(int col = 1; col < 8; col++){
                if(board.getPiece(new ChessPosition(row, col)) != null && board.getPiece(new ChessPosition(row, col)).getPieceType() == ChessPiece.PieceType.KING && board.getPiece(new ChessPosition(row, col)).getTeamColor() == teamColor){
                    king = new ChessPosition(row, col);
                }
            }
        }
        if (king == null){
            System.out.println("can't find king to check for check!");
        }
        for(int row = 1; row < 8; row ++){
            for(int col = 1; col < 8; col++){
                ChessPiece toScan = board.getPiece(new ChessPosition(row, col));
                if(toScan != null && toScan.getTeamColor() != teamColor){
                    System.out.println("found an enemy piece");
                    Collection<ChessMove> options = toScan.pieceMoves(board, new ChessPosition(row, col));
                    for (ChessMove move : options){
                        if (move.getEndPosition() == king){
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        System.out.println("isInCheckmate() called");
        return false;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        System.out.println("isInStalemate() called");
        return false;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        System.out.println("setBoard() called");
        board.resetBoard();
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        System.out.println("getBoard() called");
        return(board);
    }
}
