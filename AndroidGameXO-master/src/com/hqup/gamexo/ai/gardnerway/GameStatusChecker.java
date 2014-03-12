package kn.hqup.gamexo.ai.gardnerway;

import java.util.TreeSet;

/**
 * User: KOlegA
 * Date: 13.10.13
 * Time: 0:36
 */
public class GameStatusChecker {

    private int[] move = new int[2];
    private final int X = 0;
    private final int Y = 1;
    private final int NUM_IN_THE_ROW;
    private final char EMPTY;
    private final int BOARD_SIZE;

    GameStatusChecker(char emptyCell, int numInTheRow, int boardSize){
        EMPTY = emptyCell;
        NUM_IN_THE_ROW = numInTheRow;
        BOARD_SIZE = boardSize;
    }

    // �������� �� ������
    public boolean isWin(char[][] gB, int x, int y, char chip) {
        move[X] = 25;
        move[Y] = 25;
        boolean result = false;
        TreeSet<Integer> win = new TreeSet<Integer>();

        for (int i = 0; i < 4; i++) {
            win.addAll(checkRow(gB, x, y, chip, i));


            if (win.size() > 1) {
                result = true;
                if (win.size() == 3) {
                    return true;
                }
                break;
            }
        }

        if (win.size() != 0) {
            move = CoordinateConverter.getCoordinateFromIndex(win.last(), BOARD_SIZE);
        }

        return result;
    }


    public TreeSet<Integer> checkRow(char[][] gB, int xx, int yy, char chip, int direction) {
        int x = 0, y = 0;
        TreeSet<Integer> emptySell = new TreeSet<Integer>();
        TreeSet<Integer> emptySum = new TreeSet<Integer>();

        start:
        for (int i = 0; i < NUM_IN_THE_ROW; i++) {
            for (int k = 0; k < NUM_IN_THE_ROW; k++) {
                x = xx - (NUM_IN_THE_ROW - 1) + i + k;
                switch (direction) {
                    case 0:
                        y = yy;
                        break;
                    case 1:
                        x = xx;
                        y = yy - (NUM_IN_THE_ROW - 1) + i + k;
                        break;
                    case 2:
                        y = x + (yy - xx);
                        break;
                    case 3:
                        y = (yy + xx) - x;
                        break;
                }
                try {


                    if (gB[x][y] != chip & gB[x][y] != EMPTY) {
                        emptySell.clear();
                        continue start;
                    }

                    if (gB[x][y] == EMPTY) {
                        Integer number = CoordinateConverter.getIndexOfCell(x, y, BOARD_SIZE);
                        emptySell.add(number);
                    }

                } catch (IndexOutOfBoundsException ex) {
                    emptySell.clear();
                    continue start;
                }
            }

            if (emptySell.size() == 0) {
                System.out.println(emptySell.size());
                for (int g = 0; g < 3; g++) {
                    emptySell.add(50 + g);
                }
                return emptySell;
            }

            if (emptySell.size() > 1) {
                emptySell.clear();
            }
            emptySum.addAll(emptySell);
            emptySell.clear();
        }
        return emptySum;
    }

    public int[] getMove() {
        return move;
    }
}