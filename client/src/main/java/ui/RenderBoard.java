package ui;

import static ui.EscapeSequences.*;

public class RenderBoard {

    public String getBoardRender(Boolean isBLACK){
        StringBuilder s = new StringBuilder();
        StringBuilder temp = new StringBuilder();
        Integer[][] gridColor = new Integer[10][10];

        //set background colors
        for(int row = 0; row < 9; row++){
            for(int col = 0; col < 9; col++){
                if (row == 0 || row == 9 || col == 0 || col == 9){
                    gridColor[row][col] = 2; //blue border
                    temp.append(SET_BG_COLOR_BLUE + EMPTY + RESET_BG_COLOR);
                }
                else if (row % 2 == 1 - (col % 2)){
                    gridColor[row][col] = 0; //BLACK spaces
                    temp.append(SET_BG_COLOR_LIGHT_GREY + EMPTY + RESET_BG_COLOR);
                }
                else{
                    gridColor[row][col] = 1; //WHITE spaces
                    temp.append(SET_BG_COLOR_DARK_GREY + EMPTY + RESET_BG_COLOR);
                }
            }
            temp.append("\n");
        }
        //System.out.println(temp.toString());


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
}
