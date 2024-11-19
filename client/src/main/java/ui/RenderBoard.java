package ui;

import chess.ChessBoard;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.Arrays;

import static chess.ChessGame.TeamColor.BLACK;
import static chess.ChessGame.TeamColor.WHITE;
import static ui.EscapeSequences.*;

public class RenderBoard {

    public String getBoardRender(Boolean isBLACK){
        StringBuilder s = new StringBuilder();
        StringBuilder temp = new StringBuilder();
        Integer[][] gridColor = new Integer[10][10];
        ChessBoard board = new ChessBoard();
        board.resetBoard();

        //set background colors
        for(int row = 0; row <= 9; row++){
            for(int col = 0; col <= 9; col++){
                if (row == 0 || row == 9 || col == 0 || col == 9){
                    gridColor[row][col] = 2; //BLUE border
                }
                else if (row % 2 == 1 - (col % 2)){
                    gridColor[row][col] = 0; //BLACK spaces
                }
                else{
                    gridColor[row][col] = 1; //WHITE spaces
                }
            }
        }
        if(isBLACK) {
            for (int row = 0; row <= 9; row++) {
                for (int col = 9; col >= 0; col--) {
                    if (gridColor[row][col] == 2) {
                        temp.append(SET_BG_COLOR_BLUE + getBoardCharacters(board, row, col) + RESET_BG_COLOR);
                    } else if (gridColor[row][col] == 0) {
                        temp.append(SET_BG_COLOR_LIGHT_GREY + getBoardCharacters(board, row, col) + RESET_BG_COLOR);
                    } else if (gridColor[row][col] == 1) {
                        temp.append(SET_BG_COLOR_DARK_GREY + getBoardCharacters(board, row, col) + RESET_BG_COLOR);
                    }
                }
                temp.append("\n");
            }
        }
        else{
            for (int row = 9; row >= 0; row--) {
                for (int col = 0; col <= 9; col++) {
                    if (gridColor[row][col] == 2) {
                        temp.append(SET_BG_COLOR_BLUE + getBoardCharacters(board, row, col) + RESET_BG_COLOR);
                    } else if (gridColor[row][col] == 0) {
                        temp.append(SET_BG_COLOR_LIGHT_GREY + getBoardCharacters(board, row, col) + RESET_BG_COLOR);
                    } else if (gridColor[row][col] == 1) {
                        temp.append(SET_BG_COLOR_DARK_GREY + getBoardCharacters(board, row, col) + RESET_BG_COLOR);
                    }
                }
                temp.append("\n");
            }
        }


        /*if (isBLACK){
            s.append(SET_TEXT_BOLD);
            s.append(SET_BG_COLOR_BLUE + EMPTY + " a " + "  b " + " c " + "  d " + "  e " + "  f " + " g " + "  h " +
                    EMPTY + RESET_BG_COLOR + "\n");
            s.append(SET_BG_COLOR_BLUE + " 8 " + SET_TEXT_COLOR_WHITE + SET_BG_COLOR_LIGHT_GREY + WHITE_ROOK +
                    SET_BG_COLOR_DARK_GREY + WHITE_KNIGHT + SET_BG_COLOR_LIGHT_GREY + WHITE_BISHOP + SET_BG_COLOR_DARK_GREY
                    + WHITE_KING + SET_BG_COLOR_LIGHT_GREY + WHITE_QUEEN + SET_BG_COLOR_DARK_GREY + WHITE_BISHOP+
                    SET_BG_COLOR_LIGHT_GREY + WHITE_KNIGHT + SET_BG_COLOR_DARK_GREY + WHITE_ROOK+ SET_BG_COLOR_BLUE +
                    RESET_TEXT_COLOR+" 8 "+  RESET_BG_COLOR + "\n");
            s.append(SET_BG_COLOR_BLUE + " 7 " + SET_TEXT_COLOR_WHITE + SET_BG_COLOR_DARK_GREY + WHITE_PAWN +
                    SET_BG_COLOR_LIGHT_GREY + WHITE_PAWN + SET_BG_COLOR_DARK_GREY + WHITE_PAWN+ SET_BG_COLOR_LIGHT_GREY
                    + WHITE_PAWN + SET_BG_COLOR_DARK_GREY + WHITE_PAWN+ SET_BG_COLOR_LIGHT_GREY + WHITE_PAWN +
                    SET_BG_COLOR_DARK_GREY + WHITE_PAWN + SET_BG_COLOR_LIGHT_GREY + WHITE_PAWN + SET_BG_COLOR_BLUE  +
                    RESET_TEXT_COLOR + " 7 "+ RESET_BG_COLOR + "\n");
            s.append(SET_BG_COLOR_BLUE + " 6 " + SET_BG_COLOR_LIGHT_GREY + EMPTY + SET_BG_COLOR_DARK_GREY + EMPTY+
                    SET_BG_COLOR_LIGHT_GREY + EMPTY + SET_BG_COLOR_DARK_GREY + EMPTY+ SET_BG_COLOR_LIGHT_GREY + EMPTY
                    + SET_BG_COLOR_DARK_GREY + EMPTY+ SET_BG_COLOR_LIGHT_GREY + EMPTY + SET_BG_COLOR_DARK_GREY + EMPTY+
                    SET_BG_COLOR_BLUE + " 6 "+ RESET_BG_COLOR + "\n");
            s.append(SET_BG_COLOR_BLUE + " 5 " + SET_BG_COLOR_DARK_GREY + EMPTY + SET_BG_COLOR_LIGHT_GREY + EMPTY +
                    SET_BG_COLOR_DARK_GREY + EMPTY+ SET_BG_COLOR_LIGHT_GREY + EMPTY + SET_BG_COLOR_DARK_GREY + EMPTY+
                    SET_BG_COLOR_LIGHT_GREY + EMPTY + SET_BG_COLOR_DARK_GREY + EMPTY+ SET_BG_COLOR_LIGHT_GREY + EMPTY +
                    SET_BG_COLOR_BLUE + " 5 " + RESET_BG_COLOR+ "\n");
            s.append(SET_BG_COLOR_BLUE + " 4 " + SET_BG_COLOR_LIGHT_GREY + EMPTY + SET_BG_COLOR_DARK_GREY + EMPTY+
                    SET_BG_COLOR_LIGHT_GREY + EMPTY + SET_BG_COLOR_DARK_GREY + EMPTY+ SET_BG_COLOR_LIGHT_GREY + EMPTY +
                    SET_BG_COLOR_DARK_GREY + EMPTY+ SET_BG_COLOR_LIGHT_GREY + EMPTY + SET_BG_COLOR_DARK_GREY + EMPTY+
                    SET_BG_COLOR_BLUE + " 4 "+ RESET_BG_COLOR + "\n");
            s.append(SET_BG_COLOR_BLUE + " 3 " + SET_BG_COLOR_DARK_GREY + EMPTY + SET_BG_COLOR_LIGHT_GREY + EMPTY +
                    SET_BG_COLOR_DARK_GREY + EMPTY+ SET_BG_COLOR_LIGHT_GREY + EMPTY + SET_BG_COLOR_DARK_GREY + EMPTY+
                    SET_BG_COLOR_LIGHT_GREY + EMPTY + SET_BG_COLOR_DARK_GREY + EMPTY+ SET_BG_COLOR_LIGHT_GREY + EMPTY +
                    SET_BG_COLOR_BLUE + " 3 " + RESET_BG_COLOR+ "\n");
            s.append(SET_BG_COLOR_BLUE + " 2 " + SET_TEXT_COLOR_BLACK +  SET_BG_COLOR_LIGHT_GREY + BLACK_PAWN +
                    SET_BG_COLOR_DARK_GREY + BLACK_PAWN+ SET_BG_COLOR_LIGHT_GREY + BLACK_PAWN + SET_BG_COLOR_DARK_GREY +
                    BLACK_PAWN+ SET_BG_COLOR_LIGHT_GREY + BLACK_PAWN + SET_BG_COLOR_DARK_GREY + BLACK_PAWN+
                    SET_BG_COLOR_LIGHT_GREY + BLACK_PAWN + SET_BG_COLOR_DARK_GREY + BLACK_PAWN+ SET_BG_COLOR_BLUE  +
                    RESET_TEXT_COLOR +" 2 "+ RESET_BG_COLOR + "\n");
            s.append(SET_BG_COLOR_BLUE + " 1 " + SET_TEXT_COLOR_BLACK + SET_BG_COLOR_DARK_GREY + BLACK_ROOK +
                    SET_BG_COLOR_LIGHT_GREY + BLACK_KNIGHT + SET_BG_COLOR_DARK_GREY + BLACK_BISHOP+ SET_BG_COLOR_LIGHT_GREY
                    + BLACK_KING + SET_BG_COLOR_DARK_GREY + BLACK_QUEEN + SET_BG_COLOR_LIGHT_GREY + BLACK_BISHOP +
                    SET_BG_COLOR_DARK_GREY + BLACK_KNIGHT + SET_BG_COLOR_LIGHT_GREY + BLACK_ROOK + SET_BG_COLOR_BLUE
                    + RESET_TEXT_COLOR + " 1 "+ RESET_BG_COLOR + "\n");
            s.append(SET_BG_COLOR_BLUE + EMPTY + " a " + "  b " + " c " + "  d " + "  e " + "  f " + " g " + "  h " +
                    EMPTY + RESET_BG_COLOR);
            s.append(RESET_TEXT_BOLD_FAINT + RESET_BG_COLOR);
        }
        else{
            s.append(SET_TEXT_BOLD);
            s.append(SET_BG_COLOR_BLUE + EMPTY + " h " + "  b " + " f " + "  e " + "  d " + "  c " + " b " + "  a " +
                    EMPTY + RESET_BG_COLOR + "\n");
            s.append(SET_BG_COLOR_BLUE + " 1 " + SET_TEXT_COLOR_BLACK + SET_BG_COLOR_LIGHT_GREY + WHITE_ROOK +
                    SET_BG_COLOR_DARK_GREY + WHITE_KNIGHT + SET_BG_COLOR_LIGHT_GREY + WHITE_BISHOP + SET_BG_COLOR_DARK_GREY
                    + WHITE_QUEEN + SET_BG_COLOR_LIGHT_GREY + WHITE_KING + SET_BG_COLOR_DARK_GREY + WHITE_BISHOP+
                    SET_BG_COLOR_LIGHT_GREY + WHITE_KNIGHT + SET_BG_COLOR_DARK_GREY + WHITE_ROOK+ SET_BG_COLOR_BLUE  +
                    RESET_TEXT_COLOR+" 1 "+ RESET_BG_COLOR +  "\n");
            s.append(SET_BG_COLOR_BLUE + " 2 " + SET_TEXT_COLOR_BLACK + SET_BG_COLOR_DARK_GREY + WHITE_PAWN +
                    SET_BG_COLOR_LIGHT_GREY + WHITE_PAWN + SET_BG_COLOR_DARK_GREY + WHITE_PAWN+ SET_BG_COLOR_LIGHT_GREY
                    + WHITE_PAWN + SET_BG_COLOR_DARK_GREY + WHITE_PAWN+ SET_BG_COLOR_LIGHT_GREY + WHITE_PAWN +
                    SET_BG_COLOR_DARK_GREY + WHITE_PAWN + SET_BG_COLOR_LIGHT_GREY + WHITE_PAWN + SET_BG_COLOR_BLUE +
                    RESET_TEXT_COLOR + " 2 "+ RESET_BG_COLOR + "\n");
            s.append(SET_BG_COLOR_BLUE + " 3 " + SET_BG_COLOR_LIGHT_GREY + EMPTY + SET_BG_COLOR_DARK_GREY +
                    EMPTY+ SET_BG_COLOR_LIGHT_GREY + EMPTY + SET_BG_COLOR_DARK_GREY + EMPTY+ SET_BG_COLOR_LIGHT_GREY +
                    EMPTY + SET_BG_COLOR_DARK_GREY + EMPTY+ SET_BG_COLOR_LIGHT_GREY + EMPTY + SET_BG_COLOR_DARK_GREY +
                    EMPTY+ SET_BG_COLOR_BLUE + " 3 "+ RESET_BG_COLOR + "\n");
            s.append(SET_BG_COLOR_BLUE + " 4 " + SET_BG_COLOR_DARK_GREY + EMPTY + SET_BG_COLOR_LIGHT_GREY + EMPTY +
                    SET_BG_COLOR_DARK_GREY + EMPTY+ SET_BG_COLOR_LIGHT_GREY + EMPTY + SET_BG_COLOR_DARK_GREY + EMPTY+
                    SET_BG_COLOR_LIGHT_GREY + EMPTY + SET_BG_COLOR_DARK_GREY + EMPTY+ SET_BG_COLOR_LIGHT_GREY + EMPTY +
                    SET_BG_COLOR_BLUE + " 4 " + RESET_BG_COLOR+ "\n");
            s.append(SET_BG_COLOR_BLUE + " 5 " + SET_BG_COLOR_LIGHT_GREY + EMPTY + SET_BG_COLOR_DARK_GREY + EMPTY+
                    SET_BG_COLOR_LIGHT_GREY + EMPTY + SET_BG_COLOR_DARK_GREY + EMPTY+ SET_BG_COLOR_LIGHT_GREY + EMPTY +
                    SET_BG_COLOR_DARK_GREY + EMPTY+ SET_BG_COLOR_LIGHT_GREY + EMPTY + SET_BG_COLOR_DARK_GREY + EMPTY+
                    SET_BG_COLOR_BLUE + " 5 "+ RESET_BG_COLOR + "\n");
            s.append(SET_BG_COLOR_BLUE + " 6 " + SET_BG_COLOR_DARK_GREY + EMPTY + SET_BG_COLOR_LIGHT_GREY + EMPTY +
                    SET_BG_COLOR_DARK_GREY + EMPTY+ SET_BG_COLOR_LIGHT_GREY + EMPTY + SET_BG_COLOR_DARK_GREY + EMPTY+
                    SET_BG_COLOR_LIGHT_GREY + EMPTY + SET_BG_COLOR_DARK_GREY + EMPTY+ SET_BG_COLOR_LIGHT_GREY + EMPTY +
                    SET_BG_COLOR_BLUE + " 6 " + RESET_BG_COLOR+ "\n");
            s.append(SET_BG_COLOR_BLUE + " 7 " + SET_TEXT_COLOR_WHITE +  SET_BG_COLOR_LIGHT_GREY + BLACK_PAWN +
                    SET_BG_COLOR_DARK_GREY + BLACK_PAWN+ SET_BG_COLOR_LIGHT_GREY + BLACK_PAWN + SET_BG_COLOR_DARK_GREY +
                    BLACK_PAWN+ SET_BG_COLOR_LIGHT_GREY + BLACK_PAWN + SET_BG_COLOR_DARK_GREY + BLACK_PAWN+ SET_BG_COLOR_LIGHT_GREY
                    + BLACK_PAWN + SET_BG_COLOR_DARK_GREY + BLACK_PAWN+ SET_BG_COLOR_BLUE + RESET_TEXT_COLOR +" 7 "+
                    RESET_BG_COLOR + "\n");
            s.append(SET_BG_COLOR_BLUE + " 8 " + SET_TEXT_COLOR_WHITE + SET_BG_COLOR_DARK_GREY + BLACK_ROOK +
                    SET_BG_COLOR_LIGHT_GREY + BLACK_KNIGHT + SET_BG_COLOR_DARK_GREY + BLACK_BISHOP+
                    SET_BG_COLOR_LIGHT_GREY + BLACK_QUEEN + SET_BG_COLOR_DARK_GREY + BLACK_KING + SET_BG_COLOR_LIGHT_GREY
                    + BLACK_BISHOP + SET_BG_COLOR_DARK_GREY + BLACK_KNIGHT + SET_BG_COLOR_LIGHT_GREY + BLACK_ROOK +
                    SET_BG_COLOR_BLUE  + RESET_TEXT_COLOR + " 8 "+RESET_BG_COLOR + "\n");
            s.append(SET_BG_COLOR_BLUE + EMPTY + " h " + "  g " + " f " + "  e " + "  d " + "  c " + " b " + "  a " +
                    EMPTY + RESET_BG_COLOR);
            s.append(RESET_TEXT_BOLD_FAINT + RESET_BG_COLOR);
        }*/
        return temp.toString();
    }

    public String getBoardCharacters(ChessBoard board, Integer row, Integer col){
        if(row >= 1 && row <= 8 && col >= 1 && col <= 8){
            ChessPiece piece = board.getPiece(new ChessPosition(row, col));
            if (piece != null){
                if (piece.getPieceType() == ChessPiece.PieceType.ROOK){
                    if(piece.getTeamColor() == WHITE){
                        return SET_TEXT_COLOR_WHITE + WHITE_ROOK + RESET_TEXT_COLOR;
                    } else{
                        return SET_TEXT_COLOR_BLACK + BLACK_ROOK + RESET_TEXT_COLOR;
                    }
                }
                else if (piece.getPieceType() == ChessPiece.PieceType.BISHOP){
                    if(piece.getTeamColor() == WHITE){
                        return SET_TEXT_COLOR_WHITE + WHITE_BISHOP + RESET_TEXT_COLOR;
                    } else{
                        return SET_TEXT_COLOR_BLACK + BLACK_BISHOP + RESET_TEXT_COLOR;
                    }
                }
                else if (piece.getPieceType() == ChessPiece.PieceType.KNIGHT){
                    if(piece.getTeamColor() == WHITE){
                        return SET_TEXT_COLOR_WHITE + WHITE_KNIGHT + RESET_TEXT_COLOR;
                    } else{
                        return SET_TEXT_COLOR_BLACK + BLACK_KNIGHT + RESET_TEXT_COLOR;
                    }
                }
                else if (piece.getPieceType() == ChessPiece.PieceType.QUEEN){
                    if(piece.getTeamColor() == WHITE){
                        return SET_TEXT_COLOR_WHITE + WHITE_QUEEN + RESET_TEXT_COLOR;
                    } else{
                        return SET_TEXT_COLOR_BLACK + BLACK_QUEEN + RESET_TEXT_COLOR;
                    }
                }
                else if (piece.getPieceType() == ChessPiece.PieceType.KING){
                    if(piece.getTeamColor() == WHITE){
                        return SET_TEXT_COLOR_WHITE + WHITE_KING + RESET_TEXT_COLOR;
                    } else{
                        return SET_TEXT_COLOR_BLACK + BLACK_KING + RESET_TEXT_COLOR;
                    }
                }
                else if (piece.getPieceType() == ChessPiece.PieceType.PAWN){
                    if(piece.getTeamColor() == WHITE){
                        return SET_TEXT_COLOR_WHITE + WHITE_PAWN + RESET_TEXT_COLOR;
                    } else{
                        return SET_TEXT_COLOR_BLACK + BLACK_PAWN + RESET_TEXT_COLOR;
                    }
                }
                else{
                    return "ERR";
                }
            }
            else{
                return EMPTY;
            }
        }
        else {
            return EMPTY;
        }
    }
}
