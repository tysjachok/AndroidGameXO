package kn.hqup.gamexo.ai.gardnerway;

import java.util.ArrayList;

/**
 * User: KOlegA
 * Date: 15.09.13
 * Time: 0:42
 */
class CoordinateConverter {

    private final static int X = 0;
    private final static int Y = 1;
    

    /**
     * Makes copy of current game board.
     * @param board game board we want to copy.
     * @return copy of game board.
     */
    static char[][] copyBoard(char[][] board) {
        char[][] cBoard = new char[board.length][board[X].length];
        for (int i = 0; i < board.length; i++) {
            System.arraycopy(board[i], 0, cBoard[i], 0, board[i].length);
        }
        return cBoard;
    }

        static char[][] characterToChar(Character[][] board) {
                char[][] cBoard = new char[board.length][board[X].length];
                for (int y = 0; y < board.length; y++) {
                        for (int x = 0; x < board.length; x++) {
                                 cBoard[x][y] = board[x][y];
                        }
                }
                return cBoard;
        }



    static char[][] rotate(char[][] board, int degrees) {
        char[][] rBoard = new char[board.length][board[X].length];
        for (int y = 0; y < board.length; y++) {
            for (int x = 0; x < board.length; x++) {
                if (board[x][y] != '\u0000') {
                    int[] xy = rotateXY(x, y, board.length, degrees);
                    rBoard[xy[X]][xy[Y]] = board[x][y];
                }

            }

        }
        return rBoard;
    }

        static ArrayList<Integer> rotateHistory(ArrayList<Integer> history, int size, int degree) {
                for (int i = 0; i < history.size(); i++) {
                        Integer indexOfRotatedCell = rotateHisIndex(history.get(i), size, degree);
                        history.set(i, indexOfRotatedCell);
                }
                return history;
        }

        /**
         * Rotates index of board cell.
         * @param index of cell which we want to rotate.
         * @param size of current board;
         * @param degree we need to rotate;
         * @return  rotated index of cell.
         */
        static Integer rotateHisIndex(int index, int size, int degree){
                int[] xy = getCoordinateFromIndex(index, size);
                int[] rotatedXY = rotateXY(xy[X], xy[Y], size, degree);
                return getIndexOfCell(rotatedXY[X], rotatedXY[Y], size);
        }

    // ������������ ���������� �� �������� ����
    static int[] rotateXY(int x, int y, int size, int degree) {
        int[] xy = new int[2];
        switch (degree) {
            case 90  :                    //rotate x,y at 90 degrees clockwise;
                xy[X] = (size - 1) - y;
                xy[Y] = x;
                break;
            case 180 :
                xy[X] = (size - 1) - x;
                xy[Y] = (size - 1) - y;
                break;
            case 270 :                    //rotate x,y at 90 degrees anticlockwise;
                xy[X] = y;
                xy[Y] = (size - 1) - x;
                break;
            case 11 :                    //flip around axis Y
                xy[X] = (size - 1) - x;
                xy[Y] = y;
                break;
            case 22 :                    //flip around axis X
                xy[X] = x;
                xy[Y] = (size - 1) - y;
        }
        return xy;
    }

    /**
     * ��������� ���������� � ���������� ����� ������
     * ������� ������ ����� ������.
     * @param x  ���������� �� �
     * @param y  ���������� �� �
     * @return ���������� ����� ������ �������.
     */
    static Integer getIndexOfCell(Integer x, Integer y, Integer boardSize) {
        Integer number;
        number = y * boardSize + x;
        return number;
    }

    /**
     * ��������� ���������� ����� ������ ������� �
     * ���������� ���� ������ �� � � �.
     * @param number ���������� ����� ������
     * @return ������ ���������
     */
    static int[] getCoordinateFromIndex(int number, int boardSize) {
        int[] num = new int[2];
        num[X] = number % boardSize;
        num[Y] = number / boardSize;
        return num;
    }

}
