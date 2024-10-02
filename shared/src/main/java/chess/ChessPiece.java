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


    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {

        System.out.println("pieceMoves() called");

        ArrayList<ChessMove> validMoves = new ArrayList<>(); //where a list of valid moves is stored

        int row = myPosition.getRow(); //quick access to my cords
        int col = myPosition.getColumn(); //quick access to my cords

        ChessPosition beingScanned; //the position being scanned in the script
        ChessMove newMove; //a new move made from the position beingScanned

        ChessPiece encounter = null; //the piece located at the position beingScanned, if any.

        //uppercase is white, so the bottom 2 rows (1, 2) are white

        System.out.println(board.toString());

        if(this.type == PieceType.ROOK){
            //System.out.println("I'm a Rook");
            for(int i = row+1; i <= 8; i++){
                beingScanned = new ChessPosition(i, col);
                encounter = board.getPiece(beingScanned);
                //System.out.println("testing at " + beingScanned.toString());
                if(encounter == null){
                    //System.out.println("clear at " + beingScanned.toString());
                    newMove = new ChessMove(myPosition, beingScanned, null);
                    validMoves.add(newMove);
                }
                else{
                    //System.out.println("found a piece");
                    if(encounter.getTeamColor() == this.color){
                        //System.out.println("friendly");
                    }
                    else{
                        //System.out.println("enemy");
                        newMove = new ChessMove(myPosition, beingScanned, null);
                        validMoves.add(newMove);
                    }
                    break;
                }
            }

            for(int i = row-1; i >= 1; i--){
                beingScanned = new ChessPosition(i, col);
                encounter = board.getPiece(beingScanned);
                //System.out.println("testing at " + beingScanned.toString());
                if(encounter == null){
                    //System.out.println("clear at " + beingScanned.toString());
                    newMove = new ChessMove(myPosition, beingScanned, null);
                    validMoves.add(newMove);
                }
                else{
                    //System.out.println("found a piece");
                    if(encounter.getTeamColor() == this.color){
                        //System.out.println("friendly");
                    }
                    else{
                        //System.out.println("enemy");
                        newMove = new ChessMove(myPosition, beingScanned, null);
                        validMoves.add(newMove);
                    }
                    break;
                }
            }

            for(int i = col+1; i <= 8; i++){
                beingScanned = new ChessPosition(row, i);
                encounter = board.getPiece(beingScanned);
                //System.out.println("testing at " + beingScanned.toString());
                if(encounter == null){
                    //System.out.println("clear at " + beingScanned.toString());
                    newMove = new ChessMove(myPosition, beingScanned, null);
                    validMoves.add(newMove);
                }
                else{
                    //System.out.println("found a piece");
                    if(encounter.getTeamColor() == this.color){
                        //System.out.println("friendly");
                    }
                    else{
                        //System.out.println("enemy");
                        newMove = new ChessMove(myPosition, beingScanned, null);
                        validMoves.add(newMove);
                    }
                    break;
                }
            }

            for(int i = col-1; i >= 1; i--){
                beingScanned = new ChessPosition(row, i);
                encounter = board.getPiece(beingScanned);
                //System.out.println("testing at " + beingScanned.toString());
                if(encounter == null){
                    //System.out.println("clear at " + beingScanned.toString());
                    newMove = new ChessMove(myPosition, beingScanned, null);
                    validMoves.add(newMove);
                }
                else{
                    //System.out.println("found a piece");
                    if(encounter.getTeamColor() == this.color){
                        //System.out.println("friendly");
                    }
                    else{
                        //System.out.println("enemy");
                        newMove = new ChessMove(myPosition, beingScanned, null);
                        validMoves.add(newMove);
                    }
                    break;
                }
            }

        }

        if(this.type == PieceType.BISHOP){
            //System.out.println("I'm a Bishop");
            for(int i = 1; (row+i <= 8) && (col+i <= 8); i++){
                beingScanned = new ChessPosition(row+i, col+i);
                encounter = board.getPiece(beingScanned);
                //System.out.println("testing at " + beingScanned.toString());
                if(encounter == null){
                    //System.out.println("clear at " + beingScanned.toString());
                    newMove = new ChessMove(myPosition, beingScanned, null);
                    validMoves.add(newMove);
                }
                else{
                    //System.out.println("found a piece");
                    if(encounter.getTeamColor() == this.color){
                        //System.out.println("friendly");
                    }
                    else{
                        //System.out.println("enemy");
                        newMove = new ChessMove(myPosition, beingScanned, null);
                        validMoves.add(newMove);
                    }
                    break;
                }
            }

            for(int i = 1; (row+i <= 8) && (col-i >= 1); i++){
                beingScanned = new ChessPosition(row+i, col-i);
                encounter = board.getPiece(beingScanned);
                //System.out.println("testing at " + beingScanned.toString());
                if(encounter == null){
                    //System.out.println("clear at " + beingScanned.toString());
                    newMove = new ChessMove(myPosition, beingScanned, null);
                    validMoves.add(newMove);
                }
                else{
                    //System.out.println("found a piece");
                    if(encounter.getTeamColor() == this.color){
                        //System.out.println("friendly");
                    }
                    else{
                        //System.out.println("enemy");
                        newMove = new ChessMove(myPosition, beingScanned, null);
                        validMoves.add(newMove);
                    }
                    break;
                }
            }

            for(int i = 1; (row-i >= 1) && (col+i <= 8); i++){
                beingScanned = new ChessPosition(row-i, col+i);
                encounter = board.getPiece(beingScanned);
                //System.out.println("testing at " + beingScanned.toString());
                if(encounter == null){
                    //System.out.println("clear at " + beingScanned.toString());
                    newMove = new ChessMove(myPosition, beingScanned, null);
                    validMoves.add(newMove);
                }
                else{
                    //System.out.println("found a piece");
                    if(encounter.getTeamColor() == this.color){
                        //System.out.println("friendly");
                    }
                    else{
                        //System.out.println("enemy");
                        newMove = new ChessMove(myPosition, beingScanned, null);
                        validMoves.add(newMove);
                    }
                    break;
                }
            }

            for(int i = 1; (row-i >= 1) && (col-i >= 1); i++){
                beingScanned = new ChessPosition(row-i, col-i);
                encounter = board.getPiece(beingScanned);
                //System.out.println("testing at " + beingScanned.toString());
                if(encounter == null){
                    //System.out.println("clear at " + beingScanned.toString());
                    newMove = new ChessMove(myPosition, beingScanned, null);
                    validMoves.add(newMove);
                }
                else{
                    //System.out.println("found a piece");
                    if(encounter.getTeamColor() == this.color){
                        //System.out.println("friendly");
                    }
                    else{
                        //System.out.println("enemy");
                        newMove = new ChessMove(myPosition, beingScanned, null);
                        validMoves.add(newMove);
                    }
                    break;
                }
            }

        }

        if(this.type == PieceType.QUEEN){
            //System.out.println("I'm a Queen");
            for(int i = 1; (row+i <= 8) && (col+i <= 8); i++){
                beingScanned = new ChessPosition(row+i, col+i);
                encounter = board.getPiece(beingScanned);
                //System.out.println("testing at " + beingScanned.toString());
                if(encounter == null){
                    //System.out.println("clear at " + beingScanned.toString());
                    newMove = new ChessMove(myPosition, beingScanned, null);
                    validMoves.add(newMove);
                }
                else{
                    //System.out.println("found a piece");
                    if(encounter.getTeamColor() == this.color){
                        //System.out.println("friendly");
                    }
                    else{
                        //System.out.println("enemy");
                        newMove = new ChessMove(myPosition, beingScanned, null);
                        validMoves.add(newMove);
                    }
                    break;
                }
            }

            for(int i = 1; (row+i <= 8) && (col-i >= 1); i++){
                beingScanned = new ChessPosition(row+i, col-i);
                encounter = board.getPiece(beingScanned);
                //System.out.println("testing at " + beingScanned.toString());
                if(encounter == null){
                    //System.out.println("clear at " + beingScanned.toString());
                    newMove = new ChessMove(myPosition, beingScanned, null);
                    validMoves.add(newMove);
                }
                else{
                    //System.out.println("found a piece");
                    if(encounter.getTeamColor() == this.color){
                        //System.out.println("friendly");
                    }
                    else{
                        //System.out.println("enemy");
                        newMove = new ChessMove(myPosition, beingScanned, null);
                        validMoves.add(newMove);
                    }
                    break;
                }
            }

            for(int i = 1; (row-i >= 1) && (col+i <= 8); i++){
                beingScanned = new ChessPosition(row-i, col+i);
                encounter = board.getPiece(beingScanned);
                //System.out.println("testing at " + beingScanned.toString());
                if(encounter == null){
                    //System.out.println("clear at " + beingScanned.toString());
                    newMove = new ChessMove(myPosition, beingScanned, null);
                    validMoves.add(newMove);
                }
                else{
                    //System.out.println("found a piece");
                    if(encounter.getTeamColor() == this.color){
                        //System.out.println("friendly");
                    }
                    else{
                        //System.out.println("enemy");
                        newMove = new ChessMove(myPosition, beingScanned, null);
                        validMoves.add(newMove);
                    }
                    break;
                }
            }

            for(int i = 1; (row-i >= 1) && (col-i >= 1); i++){
                beingScanned = new ChessPosition(row-i, col-i);
                encounter = board.getPiece(beingScanned);
                //System.out.println("testing at " + beingScanned.toString());
                if(encounter == null){
                    //System.out.println("clear at " + beingScanned.toString());
                    newMove = new ChessMove(myPosition, beingScanned, null);
                    validMoves.add(newMove);
                }
                else{
                    //System.out.println("found a piece");
                    if(encounter.getTeamColor() == this.color){
                        //System.out.println("friendly");
                    }
                    else{
                        //System.out.println("enemy");
                        newMove = new ChessMove(myPosition, beingScanned, null);
                        validMoves.add(newMove);
                    }
                    break;
                }
            }

            for(int i = row+1; i <= 8; i++){
                beingScanned = new ChessPosition(i, col);
                encounter = board.getPiece(beingScanned);
                //System.out.println("testing at " + beingScanned.toString());
                if(encounter == null){
                    //System.out.println("clear at " + beingScanned.toString());
                    newMove = new ChessMove(myPosition, beingScanned, null);
                    validMoves.add(newMove);
                }
                else{
                    //System.out.println("found a piece");
                    if(encounter.getTeamColor() == this.color){
                        //System.out.println("friendly");
                    }
                    else{
                        //System.out.println("enemy");
                        newMove = new ChessMove(myPosition, beingScanned, null);
                        validMoves.add(newMove);
                    }
                    break;
                }
            }

            for(int i = row-1; i >= 1; i--){
                beingScanned = new ChessPosition(i, col);
                encounter = board.getPiece(beingScanned);
                //System.out.println("testing at " + beingScanned.toString());
                if(encounter == null){
                    //System.out.println("clear at " + beingScanned.toString());
                    newMove = new ChessMove(myPosition, beingScanned, null);
                    validMoves.add(newMove);
                }
                else{
                    //System.out.println("found a piece");
                    if(encounter.getTeamColor() == this.color){
                        //System.out.println("friendly");
                    }
                    else{
                        //System.out.println("enemy");
                        newMove = new ChessMove(myPosition, beingScanned, null);
                        validMoves.add(newMove);
                    }
                    break;
                }
            }

            for(int i = col+1; i <= 8; i++){
                beingScanned = new ChessPosition(row, i);
                encounter = board.getPiece(beingScanned);
                //System.out.println("testing at " + beingScanned.toString());
                if(encounter == null){
                    //System.out.println("clear at " + beingScanned.toString());
                    newMove = new ChessMove(myPosition, beingScanned, null);
                    validMoves.add(newMove);
                }
                else{
                    //System.out.println("found a piece");
                    if(encounter.getTeamColor() == this.color){
                        //System.out.println("friendly");
                    }
                    else{
                        //System.out.println("enemy");
                        newMove = new ChessMove(myPosition, beingScanned, null);
                        validMoves.add(newMove);
                    }
                    break;
                }
            }

            for(int i = col-1; i >= 1; i--){
                beingScanned = new ChessPosition(row, i);
                encounter = board.getPiece(beingScanned);
                //System.out.println("testing at " + beingScanned.toString());
                if(encounter == null){
                    //System.out.println("clear at " + beingScanned.toString());
                    newMove = new ChessMove(myPosition, beingScanned, null);
                    validMoves.add(newMove);
                }
                else{
                    //System.out.println("found a piece");
                    if(encounter.getTeamColor() == this.color){
                        //System.out.println("friendly");
                    }
                    else{
                        //System.out.println("enemy");
                        newMove = new ChessMove(myPosition, beingScanned, null);
                        validMoves.add(newMove);
                    }
                    break;
                }
            }

        }

        if(this.type == PieceType.KING){
            //System.out.println("I'm a King");
            for(int i = 1; (row+i <= 8) && (col+i <= 8); i++){
                beingScanned = new ChessPosition(row+i, col+i);
                encounter = board.getPiece(beingScanned);
                //System.out.println("testing at " + beingScanned.toString());
                if(encounter == null){
                    //System.out.println("clear at " + beingScanned.toString());
                    newMove = new ChessMove(myPosition, beingScanned, null);
                    validMoves.add(newMove);
                    break;
                }
                else{
                    //System.out.println("found a piece");
                    if(encounter.getTeamColor() == this.color){
                        //System.out.println("friendly");
                    }
                    else{
                        //System.out.println("enemy");
                        newMove = new ChessMove(myPosition, beingScanned, null);
                        validMoves.add(newMove);
                    }
                    break;
                }
            }

            for(int i = 1; (row+i <= 8) && (col-i >= 1); i++){
                beingScanned = new ChessPosition(row+i, col-i);
                encounter = board.getPiece(beingScanned);
                //System.out.println("testing at " + beingScanned.toString());
                if(encounter == null){
                    //System.out.println("clear at " + beingScanned.toString());
                    newMove = new ChessMove(myPosition, beingScanned, null);
                    validMoves.add(newMove);
                    break;
                }
                else{
                    //System.out.println("found a piece");
                    if(encounter.getTeamColor() == this.color){
                        //System.out.println("friendly");
                    }
                    else{
                        //System.out.println("enemy");
                        newMove = new ChessMove(myPosition, beingScanned, null);
                        validMoves.add(newMove);
                    }
                    break;
                }
            }

            for(int i = 1; (row-i >= 1) && (col+i <= 8); i++){
                beingScanned = new ChessPosition(row-i, col+i);
                encounter = board.getPiece(beingScanned);
                //System.out.println("testing at " + beingScanned.toString());
                if(encounter == null){
                    //System.out.println("clear at " + beingScanned.toString());
                    newMove = new ChessMove(myPosition, beingScanned, null);
                    validMoves.add(newMove);
                    break;
                }
                else{
                    //System.out.println("found a piece");
                    if(encounter.getTeamColor() == this.color){
                        //System.out.println("friendly");
                    }
                    else{
                        //System.out.println("enemy");
                        newMove = new ChessMove(myPosition, beingScanned, null);
                        validMoves.add(newMove);
                    }
                    break;
                }
            }

            for(int i = 1; (row-i >= 1) && (col-i >= 1); i++){
                beingScanned = new ChessPosition(row-i, col-i);
                encounter = board.getPiece(beingScanned);
                //System.out.println("testing at " + beingScanned.toString());
                if(encounter == null){
                    System.out.println("clear at " + beingScanned.toString());
                    newMove = new ChessMove(myPosition, beingScanned, null);
                    validMoves.add(newMove);
                    break;
                }
                else{
                    System.out.println("found a piece");
                    if(encounter.getTeamColor() == this.color){
                        System.out.println("friendly");
                    }
                    else{
                        System.out.println("enemy");
                        newMove = new ChessMove(myPosition, beingScanned, null);
                        validMoves.add(newMove);
                    }
                    break;
                }
            }

            for(int i = row+1; i <= 8; i++){
                beingScanned = new ChessPosition(i, col);
                encounter = board.getPiece(beingScanned);
                System.out.println("testing at " + beingScanned.toString());
                if(encounter == null){
                    System.out.println("clear at " + beingScanned.toString());
                    newMove = new ChessMove(myPosition, beingScanned, null);
                    validMoves.add(newMove);
                    break;
                }
                else{
                    System.out.println("found a piece");
                    if(encounter.getTeamColor() == this.color){
                        System.out.println("friendly");
                    }
                    else{
                        System.out.println("enemy");
                        newMove = new ChessMove(myPosition, beingScanned, null);
                        validMoves.add(newMove);
                    }
                    break;
                }
            }

            for(int i = row-1; i >= 1; i--){
                beingScanned = new ChessPosition(i, col);
                encounter = board.getPiece(beingScanned);
                System.out.println("testing at " + beingScanned.toString());
                if(encounter == null){
                    System.out.println("clear at " + beingScanned.toString());
                    newMove = new ChessMove(myPosition, beingScanned, null);
                    validMoves.add(newMove);
                    break;
                }
                else{
                    System.out.println("found a piece");
                    if(encounter.getTeamColor() == this.color){
                        System.out.println("friendly");
                    }
                    else{
                        System.out.println("enemy");
                        newMove = new ChessMove(myPosition, beingScanned, null);
                        validMoves.add(newMove);
                    }
                    break;
                }
            }

            for(int i = col+1; i <= 8; i++){
                beingScanned = new ChessPosition(row, i);
                encounter = board.getPiece(beingScanned);
                System.out.println("testing at " + beingScanned.toString());
                if(encounter == null){
                    System.out.println("clear at " + beingScanned.toString());
                    newMove = new ChessMove(myPosition, beingScanned, null);
                    validMoves.add(newMove);
                    break;
                }
                else{
                    System.out.println("found a piece");
                    if(encounter.getTeamColor() == this.color){
                        System.out.println("friendly");
                    }
                    else{
                        System.out.println("enemy");
                        newMove = new ChessMove(myPosition, beingScanned, null);
                        validMoves.add(newMove);
                    }
                    break;
                }
            }

            for(int i = col-1; i >= 1; i--){
                beingScanned = new ChessPosition(row, i);
                encounter = board.getPiece(beingScanned);
                System.out.println("testing at " + beingScanned.toString());
                if(encounter == null){
                    System.out.println("clear at " + beingScanned.toString());
                    newMove = new ChessMove(myPosition, beingScanned, null);
                    validMoves.add(newMove);
                    break;
                }
                else{
                    System.out.println("found a piece");
                    if(encounter.getTeamColor() == this.color){
                        System.out.println("friendly");
                    }
                    else{
                        System.out.println("enemy");
                        newMove = new ChessMove(myPosition, beingScanned, null);
                        validMoves.add(newMove);
                    }
                    break;
                }
            }

        }

        if(this.type == PieceType.PAWN){
            System.out.println("I'm a Pawn");

            if(color == ChessGame.TeamColor.WHITE){
                if(row+1 <= 8){
                    beingScanned = new ChessPosition(row+1, col);
                    encounter = board.getPiece(beingScanned);
                    System.out.println("testing at " + beingScanned.toString());
                    if(encounter == null){
                        if(row == 2){
                            System.out.println("clear at " + beingScanned.toString());
                            newMove = new ChessMove(myPosition, beingScanned, null);
                            validMoves.add(newMove);
                            beingScanned = new ChessPosition(row+2, col);
                            encounter = board.getPiece(beingScanned);
                            System.out.println("testing double forward " + beingScanned.toString());
                            if(encounter == null) {
                                System.out.println("clear at double forward" + beingScanned.toString());
                                newMove = new ChessMove(myPosition, beingScanned, null);
                                validMoves.add(newMove);
                            }

                        }
                        else if(row == 7){
                            System.out.println("clear to promote at " + beingScanned.toString());
                            newMove = new ChessMove(myPosition, beingScanned, PieceType.QUEEN);
                            validMoves.add(newMove);
                            newMove = new ChessMove(myPosition, beingScanned, PieceType.ROOK);
                            validMoves.add(newMove);
                            newMove = new ChessMove(myPosition, beingScanned, PieceType.BISHOP);
                            validMoves.add(newMove);
                            newMove = new ChessMove(myPosition, beingScanned, PieceType.KNIGHT);
                            validMoves.add(newMove);

                        } else {
                            System.out.println("clear at " + beingScanned.toString());
                            newMove = new ChessMove(myPosition, beingScanned, null);
                            validMoves.add(newMove);
                        }
                    }
                    else{
                        System.out.println("found a piece");
                        if(encounter.getTeamColor() == this.color){
                            System.out.println("friendly");
                        }
                        else{
                            System.out.println("enemy");
                        }
                    }
                }
                if(row+1 <= 8 && col+1 <= 8){
                    beingScanned = new ChessPosition(row+1, col+1);
                    encounter = board.getPiece(beingScanned);
                    System.out.println("testing at " + beingScanned.toString());
                    if(encounter == null){
                        System.out.println("clear at " + beingScanned.toString());
                    }
                    else{
                        System.out.println("found a piece");
                        if(encounter.getTeamColor() == this.color){
                            System.out.println("friendly");
                        }
                        else{
                            if(row == 7) {
                                System.out.println("clear to promote at " + beingScanned.toString());
                                newMove = new ChessMove(myPosition, beingScanned, PieceType.QUEEN);
                                validMoves.add(newMove);
                                newMove = new ChessMove(myPosition, beingScanned, PieceType.ROOK);
                                validMoves.add(newMove);
                                newMove = new ChessMove(myPosition, beingScanned, PieceType.BISHOP);
                                validMoves.add(newMove);
                                newMove = new ChessMove(myPosition, beingScanned, PieceType.KNIGHT);
                                validMoves.add(newMove);
                            } else {
                                System.out.println("enemy");
                                newMove = new ChessMove(myPosition, beingScanned, null);
                                validMoves.add(newMove);
                            }
                        }
                    }
                }
                if(row+1 <= 8 && col-1 >= 1){
                    beingScanned = new ChessPosition(row+1, col-1);
                    encounter = board.getPiece(beingScanned);
                    System.out.println("testing at " + beingScanned.toString());
                    if(encounter == null){
                        System.out.println("clear at " + beingScanned.toString());
                    }
                    else{
                        System.out.println("found a piece");
                        if(encounter.getTeamColor() == this.color){
                            System.out.println("friendly");
                        }
                        else{
                            if(row == 7) {
                                System.out.println("clear to promote at " + beingScanned.toString());
                                newMove = new ChessMove(myPosition, beingScanned, PieceType.QUEEN);
                                validMoves.add(newMove);
                                newMove = new ChessMove(myPosition, beingScanned, PieceType.ROOK);
                                validMoves.add(newMove);
                                newMove = new ChessMove(myPosition, beingScanned, PieceType.BISHOP);
                                validMoves.add(newMove);
                                newMove = new ChessMove(myPosition, beingScanned, PieceType.KNIGHT);
                                validMoves.add(newMove);
                            } else {
                                System.out.println("enemy");
                                newMove = new ChessMove(myPosition, beingScanned, null);
                                validMoves.add(newMove);
                            }
                        }
                    }
                }
            }
            else{
                if(row-1 >= 1){
                    beingScanned = new ChessPosition(row-1, col);
                    encounter = board.getPiece(beingScanned);
                    System.out.println("testing at " + beingScanned.toString());
                    if(encounter == null){
                        if(row == 7){
                            System.out.println("clear at " + beingScanned.toString());
                            newMove = new ChessMove(myPosition, beingScanned, null);
                            validMoves.add(newMove);
                            beingScanned = new ChessPosition(row-2, col);
                            encounter = board.getPiece(beingScanned);
                            System.out.println("testing double forward " + beingScanned.toString());
                            if(encounter == null) {
                                System.out.println("clear at double forward" + beingScanned.toString());
                                newMove = new ChessMove(myPosition, beingScanned, null);
                                validMoves.add(newMove);
                            }

                        }
                        else if(row == 2){
                            System.out.println("clear to promote at " + beingScanned.toString());
                            newMove = new ChessMove(myPosition, beingScanned, PieceType.QUEEN);
                            validMoves.add(newMove);
                            newMove = new ChessMove(myPosition, beingScanned, PieceType.ROOK);
                            validMoves.add(newMove);
                            newMove = new ChessMove(myPosition, beingScanned, PieceType.BISHOP);
                            validMoves.add(newMove);
                            newMove = new ChessMove(myPosition, beingScanned, PieceType.KNIGHT);
                            validMoves.add(newMove);

                        } else {
                            System.out.println("clear at " + beingScanned.toString());
                            newMove = new ChessMove(myPosition, beingScanned, null);
                            validMoves.add(newMove);
                        }
                    }
                    else{
                        System.out.println("found a piece");
                        if(encounter.getTeamColor() == this.color){
                            System.out.println("friendly");
                        }
                        else{
                            System.out.println("enemy");
                        }
                    }
                }
                if(row-1 >= 1 && col+1 <= 8){
                    beingScanned = new ChessPosition(row-1, col+1);
                    encounter = board.getPiece(beingScanned);
                    System.out.println("testing at " + beingScanned.toString());
                    if(encounter == null){
                        System.out.println("clear at " + beingScanned.toString());
                    }
                    else{
                        System.out.println("found a piece");
                        if(encounter.getTeamColor() == this.color){
                            System.out.println("friendly");
                        }
                        else{
                            if(row == 2) {
                                System.out.println("clear to promote at " + beingScanned.toString());
                                newMove = new ChessMove(myPosition, beingScanned, PieceType.QUEEN);
                                validMoves.add(newMove);
                                newMove = new ChessMove(myPosition, beingScanned, PieceType.ROOK);
                                validMoves.add(newMove);
                                newMove = new ChessMove(myPosition, beingScanned, PieceType.BISHOP);
                                validMoves.add(newMove);
                                newMove = new ChessMove(myPosition, beingScanned, PieceType.KNIGHT);
                                validMoves.add(newMove);
                            } else {
                                System.out.println("enemy");
                                newMove = new ChessMove(myPosition, beingScanned, null);
                                validMoves.add(newMove);
                            }
                        }
                    }
                }
                if(row-1 >= 1 && col-1 >= 1){
                    beingScanned = new ChessPosition(row-1, col-1);
                    encounter = board.getPiece(beingScanned);
                    System.out.println("testing at " + beingScanned.toString());
                    if(encounter == null){
                        System.out.println("clear at " + beingScanned.toString());
                    }
                    else{
                        System.out.println("found a piece");
                        if(encounter.getTeamColor() == this.color){
                            System.out.println("friendly");
                        }
                        else{
                            if(row == 2) {
                                System.out.println("clear to promote at " + beingScanned.toString());
                                newMove = new ChessMove(myPosition, beingScanned, PieceType.QUEEN);
                                validMoves.add(newMove);
                                newMove = new ChessMove(myPosition, beingScanned, PieceType.ROOK);
                                validMoves.add(newMove);
                                newMove = new ChessMove(myPosition, beingScanned, PieceType.BISHOP);
                                validMoves.add(newMove);
                                newMove = new ChessMove(myPosition, beingScanned, PieceType.KNIGHT);
                                validMoves.add(newMove);
                            } else {
                                System.out.println("enemy");
                                newMove = new ChessMove(myPosition, beingScanned, null);
                                validMoves.add(newMove);
                            }
                        }
                    }
                }
            }

        }

        if(this.type == PieceType.KNIGHT){
            System.out.println("I'm a Knight");
            if(row+1 <= 8 && col+2 <= 8){
                beingScanned = new ChessPosition(row+1, col+2);
                encounter = board.getPiece(beingScanned);
                System.out.println("testing at " + beingScanned.toString());
                if(encounter == null){
                    System.out.println("clear at " + beingScanned.toString());
                    newMove = new ChessMove(myPosition, beingScanned, null);
                    validMoves.add(newMove);
                }
                else{
                    System.out.println("found a piece");
                    if(encounter.getTeamColor() == this.color){
                        System.out.println("friendly");
                    }
                    else{
                        System.out.println("enemy");
                        newMove = new ChessMove(myPosition, beingScanned, null);
                        validMoves.add(newMove);
                    }
                }
            }
            if(row+2 <= 8 && col+1 <= 8){
                beingScanned = new ChessPosition(row+2, col+1);
                encounter = board.getPiece(beingScanned);
                System.out.println("testing at " + beingScanned.toString());
                if(encounter == null){
                    System.out.println("clear at " + beingScanned.toString());
                    newMove = new ChessMove(myPosition, beingScanned, null);
                    validMoves.add(newMove);
                }
                else{
                    System.out.println("found a piece");
                    if(encounter.getTeamColor() == this.color){
                        System.out.println("friendly");
                    }
                    else{
                        System.out.println("enemy");
                        newMove = new ChessMove(myPosition, beingScanned, null);
                        validMoves.add(newMove);
                    }
                }
            }
            if(row-1 >= 1 && col+2 <= 8){
                beingScanned = new ChessPosition(row-1, col+2);
                encounter = board.getPiece(beingScanned);
                System.out.println("testing at " + beingScanned.toString());
                if(encounter == null){
                    System.out.println("clear at " + beingScanned.toString());
                    newMove = new ChessMove(myPosition, beingScanned, null);
                    validMoves.add(newMove);
                }
                else{
                    System.out.println("found a piece");
                    if(encounter.getTeamColor() == this.color){
                        System.out.println("friendly");
                    }
                    else{
                        System.out.println("enemy");
                        newMove = new ChessMove(myPosition, beingScanned, null);
                        validMoves.add(newMove);
                    }
                }
            }
            if(row-2 >= 1 && col+1 <= 8){
                beingScanned = new ChessPosition(row-2, col+1);
                encounter = board.getPiece(beingScanned);
                System.out.println("testing at " + beingScanned.toString());
                if(encounter == null){
                    System.out.println("clear at " + beingScanned.toString());
                    newMove = new ChessMove(myPosition, beingScanned, null);
                    validMoves.add(newMove);
                }
                else{
                    System.out.println("found a piece");
                    if(encounter.getTeamColor() == this.color){
                        System.out.println("friendly");
                    }
                    else{
                        System.out.println("enemy");
                        newMove = new ChessMove(myPosition, beingScanned, null);
                        validMoves.add(newMove);
                    }
                }
            }
            if(row+1 <= 8 && col-2 >= 1){
                beingScanned = new ChessPosition(row+1, col-2);
                encounter = board.getPiece(beingScanned);
                System.out.println("testing at " + beingScanned.toString());
                if(encounter == null){
                    System.out.println("clear at " + beingScanned.toString());
                    newMove = new ChessMove(myPosition, beingScanned, null);
                    validMoves.add(newMove);
                }
                else{
                    System.out.println("found a piece");
                    if(encounter.getTeamColor() == this.color){
                        System.out.println("friendly");
                    }
                    else{
                        System.out.println("enemy");
                        newMove = new ChessMove(myPosition, beingScanned, null);
                        validMoves.add(newMove);
                    }
                }
            }
            if(row+2 <= 8 && col-1 >= 1){
                beingScanned = new ChessPosition(row+2, col-1);
                encounter = board.getPiece(beingScanned);
                System.out.println("testing at " + beingScanned.toString());
                if(encounter == null){
                    System.out.println("clear at " + beingScanned.toString());
                    newMove = new ChessMove(myPosition, beingScanned, null);
                    validMoves.add(newMove);
                }
                else{
                    System.out.println("found a piece");
                    if(encounter.getTeamColor() == this.color){
                        System.out.println("friendly");
                    }
                    else{
                        System.out.println("enemy");
                        newMove = new ChessMove(myPosition, beingScanned, null);
                        validMoves.add(newMove);
                    }
                }
            }
            if(row-1 >= 1 && col-2 >= 1){
                beingScanned = new ChessPosition(row-1, col-2);
                encounter = board.getPiece(beingScanned);
                System.out.println("testing at " + beingScanned.toString());
                if(encounter == null){
                    System.out.println("clear at " + beingScanned.toString());
                    newMove = new ChessMove(myPosition, beingScanned, null);
                    validMoves.add(newMove);
                }
                else{
                    System.out.println("found a piece");
                    if(encounter.getTeamColor() == this.color){
                        System.out.println("friendly");
                    }
                    else{
                        System.out.println("enemy");
                        newMove = new ChessMove(myPosition, beingScanned, null);
                        validMoves.add(newMove);
                    }
                }
            }
            if(row-2 >= 1 && col-1 >= 1){
                beingScanned = new ChessPosition(row-2, col-1);
                encounter = board.getPiece(beingScanned);
                System.out.println("testing at " + beingScanned.toString());
                if(encounter == null){
                    System.out.println("clear at " + beingScanned.toString());
                    newMove = new ChessMove(myPosition, beingScanned, null);
                    validMoves.add(newMove);
                }
                else{
                    System.out.println("found a piece");
                    if(encounter.getTeamColor() == this.color){
                        System.out.println("friendly");
                    }
                    else{
                        System.out.println("enemy");
                        newMove = new ChessMove(myPosition, beingScanned, null);
                        validMoves.add(newMove);
                    }
                }
            }

        }



        return validMoves;



    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

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
        return "ChessPiece{" +
                "pieceColor=" + color +
                ", type=" + type +
                '}';
    }

}
