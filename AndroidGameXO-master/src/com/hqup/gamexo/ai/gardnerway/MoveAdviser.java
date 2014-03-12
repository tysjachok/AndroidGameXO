package kn.hqup.gamexo.ai.gardnerway;


import java.util.HashSet;
import java.util.ArrayList;

/**
 * User: Oleg
 * Date: 21.10.13
 * Time: 18:33
 */
public class MoveAdviser {

    private static final char EMPTY = '_';

        public static ArrayList<Integer> preferableMoves(char[][] gB) {

        ArrayList<Integer> preferableMoves = new ArrayList<Integer>();

        for (int y = 0; y < gB.length; y++) {
            for (int x = 0; x < gB.length; x++) {
                if (gB[x][y] !=EMPTY) {
                    preferableMoves.addAll(putPreferableMoves(gB, x, y));
                }
            }
        }

                return preferableMoves;
        }

    private static HashSet<Integer> putPreferableMoves(char[][] gB, int x, int y) {

        HashSet<Integer> moves = new HashSet<Integer>();

        for (int iY = y - 1; iY <= y + 1 ; iY++) {
            for (int iX = x - 1; iX <= x + 1 ; iX++) {
                    if (isLegalCell(gB, iX, iY)) {
                            moves.add(CoordinateConverter.getIndexOfCell(iX, iY, gB.length));
                    }
            }
        }

        return moves;
    }

        private static boolean isLegalCell(char[][]gB, int x, int y) {

                if (x >= 0 && x < gB.length) {
                        if (y >= 0 && y < gB.length) {
                                if (gB[x][y] == EMPTY) {
                                        return true;
                                }
                        }
                }

                return false;
        }
}
