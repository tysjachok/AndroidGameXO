package kn.hqup.gamexo.gamefield;

import java.util.ArrayList;
import java.util.List;

import kn.hqup.gamexo.GameActivity;
import kn.hqup.gamexo.gamefield.Game.GameStateEnum;
import kn.hqup.gamexo.utils.Logger;

/**
 * @author Andrew2212
 * 
 */
public class GameFieldController {

	private String stringWinnerX = "";
	private String stringWinnerO = "";
	private Character signWIn = GameField.VALUE_DEFAULT;
	private int countSteps = 0;
	private int fieldSize;
	private int numCheckedSigns;
	private boolean isGameOver = false;
	private List<int[]> listCellWin;// List of cells for drawing lineWin

	public GameFieldController(int fieldSize, int numCheckedSigns) {
		Logger.v("CONSTRUCTOR");
		this.fieldSize = fieldSize;
		this.numCheckedSigns = numCheckedSigns;

		// Instantiation array 'listCellWin'
		listCellWin = new ArrayList<int[]>();

		// Initialization 'gameState'
		Game.setGameState(GameStateEnum.CONTINUE);
		Game.setIsGameOver(isGameOver);

		// Get String for the comparison
		createStringWinnerX();
		createStringWinnerO();
	}

	public boolean checkGameOver(int cellNumeroX, int cellNumeroY) {

		// 'playerSign'  it's sign that will be set into the cell
		String playerSign = String.valueOf(GameActivity.getCurrentPlayer()
				.getSignPlayer());

		if (isRowOrColumnCompleted(cellNumeroX, cellNumeroY, playerSign)
				|| isDiagonalCompleted(cellNumeroX, cellNumeroY, playerSign)) {

			signWIn = playerSign.charAt(0);
			countSteps = 0;
			isGameOver = true;

			// for (int[] cell : listCellWin) {
			// Logger.v("Cell coordinate = " + cell[0] + ", " + cell[1]);
			// }
			Game.setGameState(GameStateEnum.WIN);
			Game.setIsGameOver(isGameOver);

			return isGameOver;
		}

		if (isGameFieldFilled()) {
			countSteps = 0;
			isGameOver = true;

			Game.setGameState(GameStateEnum.DRAW);
			Game.setIsGameOver(isGameOver);

			return isGameOver;
		}

		Game.setGameState(GameStateEnum.CONTINUE);
		Game.setIsGameOver(isGameOver);

		return isGameOver;
	}

	// --------Getters and Setters-----------------

	public Character getSignWin() {
		return signWIn;
	}

	/**
	 * @return List of the win cells for the marking into GameView
	 */
	public List<int[]> getListCellWin() {
		return listCellWin;
	}

	// ----------------Private Methods------------------

	private boolean isGameFieldFilled() {

		if (countSteps == (fieldSize * fieldSize - 1)) {
			return true;
		}
		countSteps++;
		return false;
	}

	private boolean isRowOrColumnCompleted(int cellNumeroX, int cellNumeroY,
			String playerSign) {

		return (checkToWinColumns(cellNumeroX, cellNumeroY, playerSign) || checkToWinRows(
				cellNumeroX, cellNumeroY, playerSign));
	}

	private boolean isDiagonalCompleted(int cellNumeroX, int cellNumeroY,
			String playerSign) {

		return (checkToWinDiagonalCW(cellNumeroX, cellNumeroY, playerSign) || checkToWinDiagonalCCW(
				cellNumeroX, cellNumeroY, playerSign));
	}

	/**
	 * @param cellNumeroX
	 * @param cellNumeroY
	 * @param playerSign
	 * @return true if column contains 'stringWinner' , false if it don't
	 */
	private boolean checkToWinColumns(int cellNumeroX, int cellNumeroY,
			String playerSign) {

		String stringCol = playerSign;
		listCellWin.add(new int[] { cellNumeroX, cellNumeroY });

		for (int i = 1; i < numCheckedSigns; i++) {
			stringCol += fetchCellValueToWin(cellNumeroX, cellNumeroY + i,
					playerSign);
			stringCol = fetchCellValueToWin(cellNumeroX, cellNumeroY - i,
					playerSign) + stringCol;
		}

		if (stringCol.contains(stringWinnerX)
				|| stringCol.contains(stringWinnerO)) {
			// Logger.v("listCellWin.size() = " + listCellWin.size());
			return true;
		} else {
			listCellWin.clear();
		}

		return false;
	}

	/**
	 * @param cellNumeroX
	 * @param cellNumeroY
	 * @param playerSign
	 * @return true if row contains 'stringWinner' , false if it don't
	 */
	private boolean checkToWinRows(int cellNumeroX, int cellNumeroY,
			String playerSign) {

		String stringRow = playerSign;
		listCellWin.add(new int[] { cellNumeroX, cellNumeroY });

		for (int i = 1; i < numCheckedSigns; i++) {
			stringRow += fetchCellValueToWin(cellNumeroX + i, cellNumeroY,
					playerSign);
			stringRow = fetchCellValueToWin(cellNumeroX - i, cellNumeroY,
					playerSign) + stringRow;
		}

		if (stringRow.contains(stringWinnerX)
				|| stringRow.contains(stringWinnerO)) {
			return true;
		} else {
			listCellWin.clear();
		}

		return false;
	}

	/**
	 * @param cellNumeroX
	 * @param cellNumeroY
	 * @param playerSign
	 * @return true if diagonalCW contains 'stringWinner' , false if it don't
	 */
	private boolean checkToWinDiagonalCW(int cellNumeroX, int cellNumeroY,
			String playerSign) {

		String stringDiagonalCW = playerSign;
		listCellWin.add(new int[] { cellNumeroX, cellNumeroY });

		for (int i = 1; i < numCheckedSigns; i++) {
			stringDiagonalCW += fetchCellValueToWin(cellNumeroX + i,
					cellNumeroY - i, playerSign);
			stringDiagonalCW = fetchCellValueToWin(cellNumeroX - i, cellNumeroY
					+ i, playerSign)
					+ stringDiagonalCW;
		}

		if (stringDiagonalCW.contains(stringWinnerX)
				|| stringDiagonalCW.contains(stringWinnerO)) {
			return true;
		} else {
			listCellWin.clear();
		}

		return false;
	}

	/**
	 * @param cellNumeroX
	 * @param cellNumeroY
	 * @param playerSign
	 * @return true if diagonalCCW contains 'stringWinner' , false if it don't
	 */
	private boolean checkToWinDiagonalCCW(int cellNumeroX, int cellNumeroY,
			String playerSign) {

		String stringDiagonalCCW = playerSign;
		listCellWin.add(new int[] { cellNumeroX, cellNumeroY });

		for (int i = 1; i < numCheckedSigns; i++) {
			stringDiagonalCCW += fetchCellValueToWin(cellNumeroX + i,
					cellNumeroY + i, playerSign);
			stringDiagonalCCW = fetchCellValueToWin(cellNumeroX - i,
					cellNumeroY - i, playerSign) + stringDiagonalCCW;
		}

		if (stringDiagonalCCW.contains(stringWinnerX)
				|| stringDiagonalCCW.contains(stringWinnerO)) {
			return true;
		} else {
			listCellWin.clear();
		}

		return false;
	}

	/**
	 * @param cellNumeroX
	 * @param cellNumeroY
	 * @return cellValue from GameField::gameFieldMatrix[][]</br> It gets
	 *         cellValue and adds cell coordinate to the 'listCellWin' if it
	 *         equals 'playerSign'
	 */
	private String fetchCellValueToWin(int cellNumeroX, int cellNumeroY,
			String playerSign) {

		String cellValue = fetchCellValue(cellNumeroX, cellNumeroY);
		// Logger.v("playerSign = " + playerSign + ", cellValue = " +
		// cellValue);

		if (cellValue.equalsIgnoreCase(playerSign)) {
			listCellWin.add(new int[] { cellNumeroX, cellNumeroY });
		}

		return cellValue;
	}

	/**
	 * @param cellNumeroX
	 * @param cellNumeroY
	 * @return cellValue from GameField::gameFieldMatrix[][]</br> It just gets
	 *         cell value
	 */
	private String fetchCellValue(int cellNumeroX, int cellNumeroY) {

		String cellValue = "";
		if (0 <= cellNumeroX && cellNumeroX < fieldSize) {
			if (0 <= cellNumeroY && cellNumeroY < fieldSize) {

				cellValue += GameField.getFieldMatrix()[cellNumeroX][cellNumeroY];
			}
		}
		return cellValue;
	}

	private String createStringWinnerX() {
		for (int i = 0; i < numCheckedSigns; i++) {
			stringWinnerX += GameField.VALUE_X;
		}

		// Logger.v(stringWinnerX);
		return stringWinnerX;
	}

	private String createStringWinnerO() {
		for (int i = 0; i < numCheckedSigns; i++) {
			stringWinnerO += GameField.VALUE_O;
		}

		// Logger.v(stringWinnerO);
		return stringWinnerO;
	}

}
