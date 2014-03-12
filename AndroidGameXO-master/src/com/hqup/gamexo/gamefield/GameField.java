package kn.hqup.gamexo.gamefield;

import kn.hqup.gamexo.utils.Logger;

/**
 * @author Andrew2212
 * 
 */
public class GameField {

	public static final char VALUE_X = 'X';
	public static final char VALUE_O = 'O';
	public static final char VALUE_DEFAULT = '_';

	private static int fieldSize;
	private static Character[][] fieldMatrix;

	public static void initNewFieldMatrix(int fieldSize) {

		Logger.v("GameField::initNewFieldMatrix()::fieldSize = " + fieldSize);

		GameField.fieldSize = fieldSize;
		fieldMatrix = null;
		fieldMatrix = new Character[fieldSize][fieldSize];
		fillDefaultGameMatrix();

	}

	// -------Getters and Setters---------

	/**
	 * @param cellNumeroX
	 *            cell number on the 'X'
	 * @param cellNumeroY
	 *            cell number on the 'Y'
	 * @return true if sign is set into cell, false if it's not.
	 */
	public static boolean setSignToCell(int cellNumeroX, int cellNumeroY,
			char playerSign) {

		if (!isCellValid(cellNumeroX, cellNumeroY)) {
			Logger.i("Cell is invalid!");
			return false;
		}

		Character sign = playerSign;
		fieldMatrix[cellNumeroX][cellNumeroY] = sign;
		// Check out current move
		Game.getGameFieldController().checkGameOver(cellNumeroX, cellNumeroY);

		return true;
	}

	public static Character[][] getFieldMatrix() {
		int size = fieldMatrix.length;
		Character[][] fieldMatrixCopy = new Character[size][size];
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				fieldMatrixCopy[i][j] = fieldMatrix[i][j];
			}
		}
		return fieldMatrixCopy;
	}

	public static void killFieldMatrix() {
		fieldMatrix = null;
	}

	public static Character getCellValue(int cellNumeroX, int cellNumeroY) {
		return fieldMatrix[cellNumeroX][cellNumeroY];
	}

	// ---------Private Methods---------------------

	private static void fillDefaultGameMatrix() {
		for (int i = 0; i < fieldSize; i++) {
			for (int j = 0; j < fieldSize; j++) {
				fillDefaultCurrentCell(i, j);
			}
		}
	}

	private static void fillDefaultCurrentCell(int i, int j) {
		fieldMatrix[i][j] = VALUE_DEFAULT;
	}

	private static boolean isCellValid(int x, int y) {

		if (!isValueValid(x, y)) {
			return false;
		}

		if (fieldMatrix[x][y].equals(VALUE_DEFAULT)) {
			return true;
		}
		return false;
	}

	private static boolean isValueValid(int x, int y) {

		if ((0 <= x && x < fieldSize) && (0 <= y && y < fieldSize)) {
			return true;
		}
		return false;
	}
}
