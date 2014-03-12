package kn.hqup.gamexo.ai;


/**
 * Created with IntelliJ IDEA.
 * User: Andrew2212
 *<br>There is one-and-only place there we get this value after initialisation of the 'someBrain'</br>
 * <br>Method 'initCoreGame' must be called into constructor of the 'brain'</br>
 */
public class CoreGame {
    public static final Character VALUE_X = 'X';
    public static final Character VALUE_O = 'O';
    public static final Character DEFAULT_CELL_VALUE = '_';
    private static int fieldSize;
    private static int numCheckedSigns;



    public static void initCoreGame(int fieldSize, int numCheckedSigns){
        CoreGame.fieldSize = fieldSize;
        CoreGame.numCheckedSigns = numCheckedSigns;
    }

    public static int getFieldSize() {
        return fieldSize;
    }

    public static void setFieldSize(int fieldSize) {
        CoreGame.fieldSize = fieldSize;
    }

    public static int getNumCheckedSigns() {
        return numCheckedSigns;
    }

    public static void setNumCheckedSigns(int numCheckedSigns) {
        CoreGame.numCheckedSigns = numCheckedSigns;
    }

    /**
     *
     * @param x cell coordinate
     * @param y cell coordinate
     * @return   true if GameField contains cell, false if not
     */
    public static boolean isValueValid(int x, int y) {
        if ((0 <= x && x < fieldSize) && (0 <= y && y < fieldSize)) {
            return true;
        }
        return false;
    }
}
