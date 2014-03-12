package kn.hqup.gamexo.ai.brutforceway;

import java.util.ArrayList;
import java.util.List;

import kn.hqup.gamexo.ai.CoreGame;

/**
 * Created with IntelliJ IDEA.
 * User: Andrew2212
 * Date: 28.09.13
 * Time: 18:49
 * <br>This class is container for "Game Options' that are obtained from rest part of application or calculated here</br>
 */
public class GameOptions {

    private static Character signBot;
    private static Character signEnemy;

    private static String stringWinnerX = ""; // i.e. string XXXX
    private static String stringWinnerO = ""; // i.e. string OOOO
    private static String stringNearWinner_XXX_ = "";// i.e. string _XXX_ for numChecked = 4
    private static String stringNearWinner_OOO_ = "";// i.e. string _OOO_ for numChecked = 4

    private static List<String> listStringNearWinnerX_1;// i.e. string XXX_ without 1 sign
    private static List<String> listStringNearWinnerO_1; // i.e. string OOO_ without 1 sign
    private static List<String> listStringNearWinnerX_2; // i.e. string XX__ without 2 sign
    private static List<String> listStringNearWinnerO_2;  //i.e. string OO__ without 2 sign

    private static List<String> listStringNearWinBot_1;
    private static List<String> listStringNearWinBot_2;
    private static List<String> listStringNearWinEnemy_1;
    private static List<String> listStringNearWinEnemy_2;

    private static String stringWinnerBot;
    private static String stringWinnerEnemy;
    private static String stringNearWinner_SSS_Bot; // S - either 'X' or 'Y'
    private static String stringNearWinner_SSS_Enemy; // S - either 'X' or 'Y'

    // ---------------------Public Methods-----------------------------

    public static void initGameOptions() {
//        LoggerAI.p("GameOptions::initGameOptions");

        stringWinnerX = createStringWinner(CoreGame.VALUE_X);
        stringWinnerO = createStringWinner(CoreGame.VALUE_O);
        stringNearWinner_XXX_ = createStringNearWinner_SSS_(CoreGame.VALUE_X);
        stringNearWinner_OOO_ = createStringNearWinner_SSS_(CoreGame.VALUE_O);
        listStringNearWinnerX_1 = createListStringWin_1(CoreGame.VALUE_X);
        listStringNearWinnerO_1 = createListStringWin_1(CoreGame.VALUE_O);
        listStringNearWinnerX_2 = createListStringWin_2(CoreGame.VALUE_X);
        listStringNearWinnerO_2 = createListStringWin_2(CoreGame.VALUE_O);
    }

    public static void setSignBotAndSignEnemy(char signBot) {
//        LoggerAI.p("GameOptions::setSignBotAndSignEnemy::signBot = " + signBot);
//        if (String.valueOf(signBot).equalsIgnoreCase(VALUE_X)) {
        if (signBot == CoreGame.VALUE_X) {
            //Bot strings
            GameOptions.signBot = CoreGame.VALUE_X;
            GameOptions.setStringWinnerBot(stringWinnerX);
            GameOptions.setStringNearWinner_SSS_Bot(stringNearWinner_XXX_);
            GameOptions.setListStringNearWinBot_1(listStringNearWinnerX_1);
            GameOptions.setListStringNearWinBot_2(listStringNearWinnerX_2);

            //Enemy strings
            GameOptions.signEnemy = CoreGame.VALUE_O;
            GameOptions.setStringWinnerEnemy(stringWinnerO);
            GameOptions.setStringNearWinner_SSS_Enemy(stringNearWinner_OOO_);
            GameOptions.setListStringNearWinEnemy_1(listStringNearWinnerO_1);
            GameOptions.setListStringNearWinEnemy_2(listStringNearWinnerO_2);

        } else {
            //Bot strings
            GameOptions.signBot = CoreGame.VALUE_O;
            GameOptions.setStringWinnerBot(stringWinnerO);
            GameOptions.setStringNearWinner_SSS_Bot(stringNearWinner_OOO_);
            GameOptions.setListStringNearWinBot_1(listStringNearWinnerO_1);
            GameOptions.setListStringNearWinBot_2(listStringNearWinnerO_2);

            //Enemy strings
            GameOptions.signEnemy = CoreGame.VALUE_X;
            GameOptions.setStringWinnerEnemy(stringWinnerX);
            GameOptions.setStringNearWinner_SSS_Enemy(stringNearWinner_XXX_);
            GameOptions.setListStringNearWinEnemy_1(listStringNearWinnerX_1);
            GameOptions.setListStringNearWinEnemy_2(listStringNearWinnerX_2);
        }
    }

//    ----------------------Private Methods------------------------------------

    /**
     * @param value 'X' or 'O'
     * @return all strings such as '_XX, X_X, XX_, XXX_' (so if numCheckedSigns = 4) and all that
     */
    private static List<String> createListStringWin_1(char value) {
        List<String> listStringNearWinner_1 = new ArrayList<String>();

        for (int i = 0; i < CoreGame.getNumCheckedSigns(); i++) { // number of the strings
            String strWin = "";
            for (int j = 0; j < CoreGame.getNumCheckedSigns(); j++) { // number of the signs
                if (i == j) {
                    strWin += String.valueOf(CoreGame.DEFAULT_CELL_VALUE);
                } else {
                    strWin += value;
                }
            }
            listStringNearWinner_1.add(strWin);
        }

        return listStringNearWinner_1;
    }

    /**
     * @param value 'X' or 'O'
     * @return all strings such as 'XX__, X__X, __XX' (so if numCheckedSigns = 4) and all that
     */
    private static List<String> createListStringWin_2(char value) {
        List<String> listStringNearWinner_2 = new ArrayList<String>();

        for (int i = 0; i < CoreGame.getNumCheckedSigns() - 1; i++) { // number of the strings
            String strWin = "";
            for (int j = 0; j < CoreGame.getNumCheckedSigns(); j++) { // number of the signs
                if (i == j) {
                    strWin += String.valueOf(CoreGame.DEFAULT_CELL_VALUE + "" + CoreGame.DEFAULT_CELL_VALUE);
                    j += 1;
                } else {
                    strWin += value;
                }
            }
            listStringNearWinner_2.add(strWin);
        }
        if (3 < CoreGame.getNumCheckedSigns()) {
//        Add  all strings such as '_X_X'
            listStringNearWinner_2.addAll(createListStringWin_2add(value));
            //        Add  all strings such as '_XX_'
            listStringNearWinner_2.addAll(createListStringWin_2add_(value));
        }
        return listStringNearWinner_2;
    }

    /**
     * @param value 'X' or 'O'
     * @return all strings such as '_X_X, X_X_' (so if numCheckedSigns = 4) and all that
     *         <br>It is called ONLY for fieldSize > 3 </br>
     */
    private static List<String> createListStringWin_2add(char value) {
        List<String> listStringNearWinner_2w = new ArrayList<String>();

        for (int i = 0; i < CoreGame.getNumCheckedSigns() - 2; i++) { // number of the strings
            String strWin = "";
            for (int j = 0; j < CoreGame.getNumCheckedSigns() - 1; j++) { // number of the signs
                if (i == j) {
                    strWin += String.valueOf("" + CoreGame.DEFAULT_CELL_VALUE + value + CoreGame.DEFAULT_CELL_VALUE);
                    j += 1;
                } else {
                    strWin += value;
                }
            }
            listStringNearWinner_2w.add(strWin);
        }
        return listStringNearWinner_2w;
    }

    /**
     * @param value 'X' or 'O'
     * @return all strings such as '_XX_X, X_XX_' (so if numCheckedSigns = 4) and all that
     *         <br>It is called ONLY for fieldSize > 3 </br>
     */
    private static List<String> createListStringWin_2add_(char value) {
        List<String> listStringNearWinner_2w = new ArrayList<String>();

        for (int i = 0; i < CoreGame.getNumCheckedSigns() - 3; i++) { // number of the strings
            String strWin = "";
            for (int j = 0; j < CoreGame.getNumCheckedSigns() - 2; j++) { // number of the signs
                if (i == j) {
                    strWin += String.valueOf("" + CoreGame.DEFAULT_CELL_VALUE + value + value + CoreGame.DEFAULT_CELL_VALUE);
                    j += 1;
                } else {
                    strWin += value;
                }
            }
            listStringNearWinner_2w.add(strWin);
        }
        return listStringNearWinner_2w;
    }

    private static String createStringWinner(char value) {
        String result = "";
        for (int i = 0; i <= CoreGame.getNumCheckedSigns() - 1; i++) {
            result += value;
        }
        return result;
    }

    /**
     *
     * @param value  'X' or 'Y'
     * @return  string that could be WIN line by either of the two next enemy moves
     */
    private static String createStringNearWinner_SSS_(char value) {
        String result = "";
        for (int i = 0; i <= CoreGame.getNumCheckedSigns(); i++) {
            if (i == 0) {
                result += String.valueOf("" + CoreGame.DEFAULT_CELL_VALUE);
            } else if (i == CoreGame.getNumCheckedSigns()) {
                result += String.valueOf("" + CoreGame.DEFAULT_CELL_VALUE);
            } else {
                result += value;
            }
        }
        return result;
    }

    // ---------------------Getters and Setters-----------------------------

    public static Character getSignBot() {
        return signBot;
    }

    public static Character getSignEnemy() {
        return signEnemy;
    }

    public static List<String> getListStringNearWinBot_1() {
        return listStringNearWinBot_1;
    }

    public static void setListStringNearWinBot_1(List<String> listStringNearWinBot_1) {
        GameOptions.listStringNearWinBot_1 = listStringNearWinBot_1;
    }

    public static List<String> getListStringNearWinBot_2() {
        return listStringNearWinBot_2;
    }

    public static void setListStringNearWinBot_2(List<String> listStringNearWinBot_2) {
        GameOptions.listStringNearWinBot_2 = listStringNearWinBot_2;
    }

    public static List<String> getListStringNearWinEnemy_1() {
        return listStringNearWinEnemy_1;
    }

    public static void setListStringNearWinEnemy_1(List<String> listStringNearWinEnemy_1) {
        GameOptions.listStringNearWinEnemy_1 = listStringNearWinEnemy_1;
    }

    public static List<String> getListStringNearWinEnemy_2() {
        return listStringNearWinEnemy_2;
    }

    public static void setListStringNearWinEnemy_2(List<String> listStringNearWinEnemy_2) {
        GameOptions.listStringNearWinEnemy_2 = listStringNearWinEnemy_2;
    }

    public static String getStringWinnerBot() {
        return stringWinnerBot;
    }

    public static void setStringWinnerBot(String stringWinnerBot) {
        GameOptions.stringWinnerBot = stringWinnerBot;
    }

    public static String getStringWinnerEnemy() {
        return stringWinnerEnemy;
    }

    public static void setStringWinnerEnemy(String stringWinnerEnemy) {
        GameOptions.stringWinnerEnemy = stringWinnerEnemy;
    }

    public static String getStringNearWinner_SSS_Bot() {
        return stringNearWinner_SSS_Bot;
    }

    public static void setStringNearWinner_SSS_Bot(String stringNearWinner_SSS_Bot) {
        GameOptions.stringNearWinner_SSS_Bot = stringNearWinner_SSS_Bot;
    }

    public static String getStringNearWinner_SSS_Enemy() {
        return stringNearWinner_SSS_Enemy;
    }

    public static void setStringNearWinner_SSS_Enemy(String stringNearWinner_SSS_Enemy) {
        GameOptions.stringNearWinner_SSS_Enemy = stringNearWinner_SSS_Enemy;
    }
}
