package ui;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.Collection;

import static chess.ChessGame.TeamColor.WHITE;
import static ui.EscapeSequences.*;

public class RenderBoard {

    public String getBoardRender(Boolean isBLACK, ChessBoard board){
        StringBuilder temp = new StringBuilder();
        Integer[][] gridColor = new Integer[10][10];

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
        //add pieces
        if(isBLACK) {
            for (int row = 0; row <= 9; row++) {
                for (int col = 9; col >= 0; col--) {
                    if (gridColor[row][col] == 2) {
                        temp.append(SET_BG_COLOR_BLUE).append(getBoardCharacters(board, row, col)).append(RESET_BG_COLOR);
                    } else if (gridColor[row][col] == 0) {
                        temp.append(SET_BG_COLOR_LIGHT_GREY).append(getBoardCharacters(board, row, col)).append(RESET_BG_COLOR);
                    } else if (gridColor[row][col] == 1) {
                        temp.append(SET_BG_COLOR_DARK_GREY).append(getBoardCharacters(board, row, col)).append(RESET_BG_COLOR);
                    }
                }
                temp.append("\n");
            }
        }
        else{
            for (int row = 9; row >= 0; row--) {

                for (int col = 0; col <= 9; col++) {

                    if (gridColor[row][col] == 2)
                    {
                        temp.append(SET_BG_COLOR_BLUE).append(getBoardCharacters(board, row, col)).append(RESET_BG_COLOR);
                    } else if (gridColor[row][col] == 0)
                    {
                        temp.append(SET_BG_COLOR_LIGHT_GREY).append(getBoardCharacters(board, row, col)).append(RESET_BG_COLOR);
                    } else if (gridColor[row][col] == 1)
                    {
                        temp.append(SET_BG_COLOR_DARK_GREY).append(getBoardCharacters(board, row, col)).append(RESET_BG_COLOR);
                    }
                }
                temp.append("\n");
            }
        }
        return temp.toString();
    }

    public String getBoardRender(Boolean isBLACK, ChessBoard board, Collection<ChessMove> moves){
        StringBuilder temp = new StringBuilder();
        Integer[][] gridColor = new Integer[10][10];

        //set background colors
        for(int row = 0; row <= 9; row++){
            for(int col = 0; col <= 9; col++){
                if (row == 0 || row == 9 || col == 0 || col == 9){
                    gridColor[row][col] = 2; //BLUE border
                }
                else if (row % 2 == 1 - (col % 2)){
                    gridColor[row][col] = checkForValidMoves(moves, row, col, true); //white spaces
                }
                else{
                    gridColor[row][col] = checkForValidMoves(moves, row, col, false); //black spaces
                }
            }
        }
        //add pieces
        if(isBLACK) {
            for (int row = 0; row <= 9; row++) {
                for (int col = 9; col >= 0; col--) {
                    if (gridColor[row][col] == 2) {
                        temp.append(SET_BG_COLOR_BLUE).append(getBoardCharacters(board, row, col)).append(RESET_BG_COLOR);
                    } else if (gridColor[row][col] == 0) {
                        temp.append(SET_BG_COLOR_LIGHT_GREY).append(getBoardCharacters(board, row, col)).append(RESET_BG_COLOR);
                    } else if (gridColor[row][col] == 1) {
                        temp.append(SET_BG_COLOR_DARK_GREY).append(getBoardCharacters(board, row, col)).append(RESET_BG_COLOR);
                    } else if (gridColor[row][col] == 3) {
                        temp.append(SET_BG_COLOR_RED).append(getBoardCharacters(board, row, col)).append(RESET_BG_COLOR);
                    } else if (gridColor[row][col] == 5) {
                        temp.append(SET_BG_COLOR_DARK_GREEN).append(getBoardCharacters(board, row, col)).append(RESET_BG_COLOR);
                    } else if (gridColor[row][col] == 4) {
                        temp.append(SET_BG_COLOR_GREEN).append(getBoardCharacters(board, row, col)).append(RESET_BG_COLOR);
                    }
                }
                temp.append("\n");
            }
        }
        else{
            for (int row = 9; row >= 0; row--) {
                for (int col = 0; col <= 9; col++) {
                    if (gridColor[row][col] == 2) {
                        temp.append(SET_BG_COLOR_BLUE).append(getBoardCharacters(board, row, col)).append(RESET_BG_COLOR);
                    }
                    else if (gridColor[row][col] == 0) {
                        temp.append(SET_BG_COLOR_LIGHT_GREY).append(getBoardCharacters(board, row, col)).append(RESET_BG_COLOR);
                    }
                    else if (gridColor[row][col] == 1) {
                        temp.append(SET_BG_COLOR_DARK_GREY).append(getBoardCharacters(board, row, col)).append(RESET_BG_COLOR);
                    }
                    else if (gridColor[row][col] == 3) {
                        temp.append(SET_BG_COLOR_RED).append(getBoardCharacters(board, row, col)).append(RESET_BG_COLOR);
                    }
                    else if (gridColor[row][col] == 5) {
                        temp.append(SET_BG_COLOR_DARK_GREEN).append(getBoardCharacters(board, row, col)).append(RESET_BG_COLOR);
                    }
                    else if (gridColor[row][col] == 4) {
                        temp.append(SET_BG_COLOR_GREEN).append(getBoardCharacters(board, row, col)).append(RESET_BG_COLOR);
                    }
                }
                temp.append("\n");
            }
        }
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
        else if (row == 0 || row == 9){
            return switch (col){
                case 1 -> " A ";
                case 2 -> "  B ";
                case 3 -> " C ";
                case 4 -> "  D ";
                case 5 -> "  E ";
                case 6 -> "  F ";
                case 7 -> " G ";
                case 8 -> " H ";
                default -> EMPTY;
            };
        }
        else if (col == 0 || col == 9){
            if (row >= 1 && row <= 8){
                return " " + Integer.toString(row) + " ";
            }
        }
        return EMPTY;
    }
    public Integer checkForValidMoves(Collection<ChessMove> moves, int row, int col, boolean white){
        for(ChessMove move : moves){
            if (move.getStartPosition().getRow() == row && move.getStartPosition().getColumn() == col){
                return 3; //red spaces
            }
            else if (move.getEndPosition().getRow() == row && move.getEndPosition().getColumn() == col){
                if (white){
                    return 4;
                }
                return 5;
            }
        }
        if (white) {
            return 0;
        }
        else{
            return 1;
        }
    }
}
