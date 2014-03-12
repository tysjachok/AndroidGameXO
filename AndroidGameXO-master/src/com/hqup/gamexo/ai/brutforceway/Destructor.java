package kn.hqup.gamexo.ai.brutforceway;

import kn.hqup.gamexo.ai.CoreGame;
import kn.hqup.gamexo.ai.utils.LoggerAI;

import java.util.*;

/**
 * Created with IntelliJ IDEA. User: Andrew2212 <br>
 * Sets weight into certain cells in accordance its own check methods</br> <br>
 * By method 'getDestructiveMove' returns some 'destructiveMove'</br>
 */
public class Destructor {

	private final Integer NEAR_MOVE = 1; // i.e. cell close to enemy move
	private final Integer NEAR_WIN_ENEMY_1; // i.e. string XXXX_ without 1 sign
	private final Integer NEAR_WIN_ENEMY_2; // i.e. string XXX__ without 2 sign
	// for cell that alters string '_XXX_X_' to string enemyWIN 'XXXXX'
	private final Integer NEAR_WIN_ENEMY_1_DANGER;
	// for cell that alters string '_XX_X_' to string nearWIN_1 '_XXXX_'
	private final Integer NEAR_WIN_ENEMY_2_DANGER;

	private Map<KeyCell, Integer> weightMap;
	private String stringResultOfCheck;
	/**
	 * Temporary list of cell coordinate for checked line
	 */
	private ArrayList<int[]> listCheckedCell = new ArrayList<int[]>();
	/**
	 * Temporary list of cell coordinate for checked line returned as 'new
	 * ArrayList<int[]>(listCheckedCell)' <br>
	 * by method 'setWeightToNearWin_1()' in order to reduce weight its cell
	 * into 'weightMap' after destructive move;</br>
	 */
	private ArrayList<int[]> listCheckedCellOldWin_1;
	/**
	 * Temporary list of cell coordinate for checked line returned as 'new
	 * ArrayList<int[]>(listCheckedCell)' <br>
	 * by method 'setWeightToNearWin_2()' in order to reduce weight its cell
	 * into 'weightMap' after destructive move;</br>
	 */
	private ArrayList<int[]> listCheckedCellOldWin_2;
	private ArrayList<int[]> listCellsNearLastEnemyMove = new ArrayList<int[]>();

	public Destructor() {
		LoggerAI.p("Destructor::CONSTRUCTOR");
		NEAR_WIN_ENEMY_1 = CoreGame.getNumCheckedSigns() * 10;
		NEAR_WIN_ENEMY_1_DANGER = CoreGame.getNumCheckedSigns() * 20;
		NEAR_WIN_ENEMY_2 = CoreGame.getNumCheckedSigns() * 2;
		NEAR_WIN_ENEMY_2_DANGER = CoreGame.getNumCheckedSigns() * 6;
		weightMap = new HashMap<KeyCell, Integer>();
	}

	// ------------Public Methods-------------------------------

	/**
	 * @param lastEnemyMoveX
	 * @param lastEnemyMoveY
	 * <br>
	 *            Return destructive move - i.e. move that destroys enemy WIN
	 *            draft
	 */
	public int[] getDestructiveMove(int lastEnemyMoveX, int lastEnemyMoveY) {

		LoggerAI.p("Destructor::getDestructiveMove()::lastEnemyMoveX = "
				+ lastEnemyMoveX + " lastEnemyMoveY = " + lastEnemyMoveY);

		findCellsNearLastEnemyMove(lastEnemyMoveX, lastEnemyMoveY);
		setWeightToCellNearLastEnemyMove();

		setWeightInRow(lastEnemyMoveX, lastEnemyMoveY);
		setWeightInColumn(lastEnemyMoveX, lastEnemyMoveY);
		setWeightInDiagonalCW(lastEnemyMoveX, lastEnemyMoveY);
		setWeightInDiagonalCCW(lastEnemyMoveX, lastEnemyMoveY);

		int[] destructiveMove;// It's 'move' to return

		// Print value of the 'weightMap'
		// controlWeightMap();

		// Coordinate of the cell with max 'weight'
		KeyCell keyMaxWeight = getMaxWeight(weightMap);
		destructiveMove = new int[] { keyMaxWeight.getX(), keyMaxWeight.getY() };

		if (keyMaxWeight != null) {
			weightMap.remove(keyMaxWeight);
		}

		// reduceWeightOldWin_1Cells(); //Check out whether it is necessary(???)
		// reduceWeightOldWin_2Cells(); //Check out whether it is necessary(???)
		setWeightToCellNearLastEnemyMove();
		return destructiveMove;

	}

	// ---------------Private Methods--------------------------------------

	/**
	 * @param weightMap
	 * @return KeyCell 'keyMaxWeight' that corresponds to max value from HashMap
	 */
	private KeyCell getMaxWeight(Map<KeyCell, Integer> weightMap) {

		Integer maxWeight = null;
		KeyCell keyMaxWeight = null;

		Iterator<KeyCell> iterator = weightMap.keySet().iterator();
		while (iterator.hasNext()) {
			KeyCell key = (KeyCell) iterator.next();
			Integer value = weightMap.get(key);
			if (maxWeight == null || maxWeight < value) {
				maxWeight = value;
				keyMaxWeight = key;
			}
		}
		return keyMaxWeight;
	}

	// ------Reduce cell weight after destructive move---------

	/**
	 * Reduces weight of cells that had been into 'lineWin_1' before our last
	 * move
	 */
	@SuppressWarnings("unused")
	private void reduceWeightOldWin_1Cells() {
		if (listCheckedCellOldWin_1 != null) {
			for (int i = 0; i < listCheckedCellOldWin_1.size(); i++) {
				KeyCell keyMaxWeight = new KeyCell(
						listCheckedCellOldWin_1.get(i));
				weightMap.put(keyMaxWeight, NEAR_MOVE * 4);
			}
		}
	}

	/**
	 * Reduces weight of cells that had been into 'lineWin_2' before our last
	 * move
	 */
	@SuppressWarnings("unused")
	private void reduceWeightOldWin_2Cells() {
		if (listCheckedCellOldWin_2 != null) {
			for (int i = 0; i < listCheckedCellOldWin_2.size(); i++) {
				KeyCell keyMaxWeight = new KeyCell(
						listCheckedCellOldWin_2.get(i));
				weightMap.put(keyMaxWeight, NEAR_MOVE * 2);
			}
		}
	}

	// -----Check lines and set weight of the cells--------------

	/**
	 * @param cellX
	 *            lastEnemyMove X
	 * @param cellY
	 *            lastEnemyMove 'Y' <br>
	 *            Checks particular line and set 'weight' of cells into
	 *            'weightMap' by methods 'setWeightTo...' </br>
	 */
	private void setWeightInRow(int cellX, int cellY) {
		stringResultOfCheck = "" + GameOptions.getSignEnemy();
		listCheckedCell.add(new int[] { cellX, cellY });

		for (int i = 1; i < CoreGame.getNumCheckedSigns(); i++) {
			writeCheckedValue(cellX + i, cellY);
			writeCheckedValueInverse(cellX - i, cellY);
		}
		setWeightToNearWin_1();
		setWeightToNearWin_2();
		listCheckedCell.clear();
		listCheckedCell.trimToSize();
	}

	/**
	 * @param cellX
	 *            lastEnemyMove X
	 * @param cellY
	 *            lastEnemyMove 'Y' <br>
	 *            Checks particular line and set 'weight' of cells into
	 *            'weightMap' by methods 'setWeightTo...' </br>
	 */
	private void setWeightInColumn(int cellX, int cellY) {
		stringResultOfCheck = "" + GameOptions.getSignEnemy();
		listCheckedCell.add(new int[] { cellX, cellY });

		for (int i = 1; i < CoreGame.getNumCheckedSigns(); i++) {
			writeCheckedValue(cellX, cellY + i);
			writeCheckedValueInverse(cellX, cellY - i);
		}
		setWeightToNearWin_1();
		setWeightToNearWin_2();
		listCheckedCell.clear();
		listCheckedCell.trimToSize();
	}

	/**
	 * @param cellX
	 *            lastEnemyMove X
	 * @param cellY
	 *            lastEnemyMove 'Y' <br>
	 *            Checks particular line and set 'weight' of cells into
	 *            'weightMap' by methods 'setWeightTo...' </br>
	 */
	private void setWeightInDiagonalCW(int cellX, int cellY) {
		stringResultOfCheck = "" + GameOptions.getSignEnemy();
		listCheckedCell.add(new int[] { cellX, cellY });

		for (int i = 1; i < CoreGame.getNumCheckedSigns(); i++) {
			writeCheckedValue(cellX + i, cellY - i);
			writeCheckedValueInverse(cellX - i, cellY + i);
		}
		setWeightToNearWin_1();
		setWeightToNearWin_2();
		listCheckedCell.clear();
		listCheckedCell.trimToSize();
	}

	/**
	 * @param cellX
	 *            lastEnemyMove X
	 * @param cellY
	 *            lastEnemyMove 'Y' <br>
	 *            Checks particular line and set 'weight' of cells into
	 *            'weightMap' by methods 'setWeightTo...' </br>
	 */
	private void setWeightInDiagonalCCW(int cellX, int cellY) {

		stringResultOfCheck = "" + GameOptions.getSignEnemy();
		listCheckedCell.add(new int[] { cellX, cellY });

		for (int i = 1; i < CoreGame.getNumCheckedSigns(); i++) {
			writeCheckedValue(cellX + i, cellY + i);
			writeCheckedValueInverse(cellX - i, cellY - i);
		}
		setWeightToNearWin_1();
		setWeightToNearWin_2();
		listCheckedCell.clear();
		listCheckedCell.trimToSize();
	}

	// -------------Write Checked Value---------------------------

	/**
	 * @param x
	 *            coordinate X of checked cell
	 * @param y
	 *            coordinate Y of checked cell <br>
	 *            Writes value of the checked cell into 'stringResultOfCheck' to
	 *            end (index = 'size')</br> <br>
	 *            Writes coordinate of the checked cell into 'listCheckedCell'
	 *            to end (index = 'size')</br>
	 */
	private void writeCheckedValue(int x, int y) {
		stringResultOfCheck = stringResultOfCheck + fetchCellValue(x, y);
		if (CoreGame.isValueValid(x, y)) {
			// LoggerAI.p("writeCheckedValue::listCheckedCell.add(cell_)::cell_[x] = "
			// + x + " cell_[y] = " + y);
			listCheckedCell.add(new int[] { x, y });
		}
	}

	/**
	 * @param x
	 *            coordinate X of checked cell
	 * @param y
	 *            coordinate Y of checked cell <br>
	 *            Writes value of the checked cell into 'stringResultOfCheck' to
	 *            start(index = 0)</br> <br>
	 *            Writes coordinate of the checked cell into 'listCheckedCell'
	 *            to start(index = 0)</br>
	 */
	private void writeCheckedValueInverse(int x, int y) {
		stringResultOfCheck = fetchCellValue(x, y) + stringResultOfCheck;
		if (CoreGame.isValueValid(x, y)) {
			// LoggerAI.p("-  listCheckedCell.addInverse(cell_)::cell_[x] = " +
			// x + " cell_[y] = " + y);
			listCheckedCell.add(0, new int[] { x, y });
		}
	}

	// ------------Setup weight into checked cell-------------

	/**
	 * <br>
	 * Puts into weightMap 'value' (weight = NEAR_WIN_ENEMY_1) with 'key'
	 * (KeyCell keyWeight = new KeyCell(listCheckedCell.get(i)))</br> <br>
	 * if checked line of cells is 'line WIN without 1 sign' from
	 * 'listStringNearWinEnemy_1' and cellValue into fieldMatrix == DEFAULT
	 * (cell is empty) <br>
	 * Return current 'listCheckedCell' to reduce weight of cells in it or
	 * 'null' if not any value has been set</br>
	 */
	private List<int[]> setWeightToNearWin_1() {

		for (int j = 0; j < GameOptions.getListStringNearWinEnemy_1().size(); j++) {

			// Check condition 'contains' for each string from
			// 'listStringNearWinEnemy_1'
			if (stringResultOfCheck.contains(GameOptions
					.getListStringNearWinEnemy_1().get(j))) {
				for (int i = 0; i < stringResultOfCheck.length(); i++) {

					KeyCell keyCell = new KeyCell(listCheckedCell.get(i));
					char cellValue = stringResultOfCheck.charAt(i);
					// LoggerAI.p("Destructor::setWeightToNearWin_1::cell[0] = "
					// + keyCell.getX() + " cell[1] = " + keyCell.getX() +
					// " cellValue = " + cellValue);
					if (cellValue == (CoreGame.DEFAULT_CELL_VALUE)) {

						Integer cellNewWeight = NEAR_WIN_ENEMY_1;

						/*
						 * Check whether enemyMove into cell with coordinates
						 * 'keyCell' alters the string 'stringResultOfCheck' to
						 * 'stringWinnerEnemy_' and set to it weight
						 * 'NEAR_WIN_ENEMY_1_DANGER'
						 */
						// Copy current fieldMatrix
						Character[][] testFieldMatrix = BrutforceAI
								.getCopyFieldMatrix();
						// Get coordinates imagined enemyMove and set it into
						// the testFieldMatrix
						testFieldMatrix[keyCell.getX()][keyCell.getY()] = GameOptions
								.getSignEnemy();
						// Check whether it's DANGER
						boolean isDanger = checkToWin_1_Danger(listCheckedCell,
								testFieldMatrix);
						// If it's DANGER
						if (isDanger && weightMap.containsKey(keyCell)) {
							cellNewWeight = NEAR_WIN_ENEMY_1_DANGER
									+ (Integer) weightMap.get(keyCell);
							// LoggerAI.p("Destructor*DANGER*weightMap.containsKey(keyCell)::weightMap.get(keyCell) = "
							// + weightMap.get(keyCell));
						} else if (!isDanger && weightMap.containsKey(keyCell)) {
							cellNewWeight = NEAR_WIN_ENEMY_1
									+ (Integer) weightMap.get(keyCell);
						}

						weightMap.put(keyCell, cellNewWeight);// to empty cell

						// LoggerAI.p("1************Destructor::setWeightToNearWin_1::cell = "
						// + keyCell.toString() + "cellNewWeight = " +
						// cellNewWeight);
					} else {
						// Remove from 'weightMap' if cell is not empty
						weightMap.remove(keyCell);
					}
				}

				listCheckedCellOldWin_1 = new ArrayList<int[]>(listCheckedCell);
				return listCheckedCellOldWin_1;
			}
		}
		return null;
	}

	/**
	 * <br>
	 * Puts into weightMap 'value' (weight = NEAR_WIN_ENEMY_2) with 'key'
	 * (KeyCell keyWeight = new KeyCell(listCheckedCell.get(i)))</br> <br>
	 * if checked line of cells close to 'WIN without 2 signs' from
	 * 'listStringNearWinEnemy_2' and cellValue into fieldMatrix == DEFAULT
	 */
	private List<int[]> setWeightToNearWin_2() {
		for (int j = 0; j < GameOptions.getListStringNearWinEnemy_2().size(); j++) {
			// LoggerAI.p("W2*** " + j + " strWin_2 = " +
			// GameOptions.listStringNearWinEnemy_2.get(j));
			// LoggerAI.p("W2*** " + j + " stringResultOfCheck = " +
			// stringResultOfCheck);

			// Check condition 'contains' for each string from
			// 'listStringNearWinEnemy_2'
			if (stringResultOfCheck.contains(GameOptions
					.getListStringNearWinEnemy_2().get(j))) {

				for (int i = 0; i < stringResultOfCheck.length(); i++) {
					KeyCell keyCell = new KeyCell(listCheckedCell.get(i));
					char cellValue = stringResultOfCheck.charAt(i);
					// LoggerAI.p("Destructor::setWeightToNearWin_2::cell = " +
					// keyCell.toString() + "cellValue = " + cellValue);

					if (cellValue == (CoreGame.DEFAULT_CELL_VALUE)) {

						Integer cellNewWeight = NEAR_WIN_ENEMY_2;
						/*
						 * Check whether enemyMove into cell with coordinates
						 * 'keyCell' alters the string 'stringResultOfCheck' to
						 * 'stringNearWinEnemy_SSS_' and set to it weight
						 * 'NEAR_WIN_ENEMY_2_DANGER'
						 */
						// Copy current fieldMatrix
						Character[][] testFieldMatrix = BrutforceAI
								.getCopyFieldMatrix();
						// Get coordinates imagined enemyMove and set it into
						// the testFieldMatrix
						testFieldMatrix[keyCell.getX()][keyCell.getY()] = GameOptions
								.getSignEnemy();
						// Check whether it's DANGER
						boolean isDanger = checkTo_SSS_Danger(listCheckedCell,
								testFieldMatrix);
						// If it's DANGER
						if (isDanger && weightMap.containsKey(keyCell)) {
							cellNewWeight = NEAR_WIN_ENEMY_2_DANGER
									+ (Integer) weightMap.get(keyCell);
							// LoggerAI.p("Destructor*DANGER*weightMap.containsKey(keyCell)::weightMap.get(keyCell) = "
							// + weightMap.get(keyCell));
						} else if (!isDanger && weightMap.containsKey(keyCell)) {
							// LoggerAI.p("weightMap.containsKey(keyCell)::weightMap.get(keyCell) = "
							// + (Integer) weightMap.get(keyCell));
							cellNewWeight = NEAR_WIN_ENEMY_2
									+ (Integer) weightMap.get(keyCell);
						}

						weightMap.put(keyCell, cellNewWeight);// to empty cell

						// System.out.println("2*Destructor::setWeightToNearWin_2::cell = "
						// + keyCell.toString() + "cellNewWeight = " +
						// cellNewWeight);
					} else {
						// Remove from 'weightMap' if cell is not empty
						weightMap.remove(keyCell);
					}
				}
				listCheckedCellOldWin_2 = new ArrayList<int[]>(listCheckedCell);
				return listCheckedCellOldWin_2;
			}
		}
		return null;
	}

	/**
	 * @param listCheckedCell
	 * @param testFieldMatrix
	 * @return true if action ::testFieldMatrix[keyCell.getX()][keyCell.getY()]
	 *         = GameOptions.getSignEnemy():: alters current checked String to
	 *         the 'WINNER' line that contains 'SSSSS'
	 */
	private boolean checkToWin_1_Danger(List<int[]> listCheckedCell,
			Character[][] testFieldMatrix) {

		int x = 0;
		int y = 1;
		String resultOfCheck = "";
		for (int i = 0; i < listCheckedCell.size(); i++) {

			resultOfCheck += testFieldMatrix[listCheckedCell.get(i)[x]][listCheckedCell
					.get(i)[y]];
		}
		// LoggerAI.p("Destructor::checkTo_SSS_Danger()::resultOfCheck = " +
		// resultOfCheck);
		if (resultOfCheck.contains(GameOptions.getStringWinnerEnemy())) {
			return true;
		}

		return false;
	}

	/**
	 * @param listCheckedCell
	 * @param testFieldMatrix
	 * @return true if action ::testFieldMatrix[keyCell.getX()][keyCell.getY()]
	 *         = GameOptions.getSignEnemy():: alters current checked String to
	 *         the 'WIN_1 line that contains '_SSSS_' i.e. WIN_1 line that has
	 *         "_" before its start and after its end
	 */
	private boolean checkTo_SSS_Danger(List<int[]> listCheckedCell,
			Character[][] testFieldMatrix) {

		// It's NOT works for field 3x3 and for numCheckedSigns = 3
		if (CoreGame.getNumCheckedSigns() < 4)
			return false;

		int x = 0;
		int y = 1;
		String resultOfCheck = "";
		for (int i = 0; i < listCheckedCell.size(); i++) {

			resultOfCheck += testFieldMatrix[listCheckedCell.get(i)[x]][listCheckedCell
					.get(i)[y]];
		}
		// LoggerAI.p("Destructor::checkTo_SSS_Danger()::resultOfCheck = " +
		// resultOfCheck);
		if (resultOfCheck.contains(GameOptions.getStringNearWinner_SSS_Enemy())) {
			return true;
		}

		return false;
	}

	// ---------- Handling cells that are close to LastEnemyMove-------------

	/**
	 * @param lastEnemyMoveX
	 * @param lastEnemyMoveY
	 * <br>
	 *            Sets weight of cells that are close to last enemy move</br>
	 */
	private void findCellsNearLastEnemyMove(int lastEnemyMoveX,
			int lastEnemyMoveY) {

		int x = lastEnemyMoveX + 1;
		int y = lastEnemyMoveY;
		getListCellsNearLastEnemyMove(x, y);

		x = lastEnemyMoveX - 1;
		y = lastEnemyMoveY;
		getListCellsNearLastEnemyMove(x, y);

		x = lastEnemyMoveX;
		y = lastEnemyMoveY + 1;
		getListCellsNearLastEnemyMove(x, y);

		x = lastEnemyMoveX;
		y = lastEnemyMoveY - 1;
		getListCellsNearLastEnemyMove(x, y);

		x = lastEnemyMoveX - 1;
		y = lastEnemyMoveY - 1;
		getListCellsNearLastEnemyMove(x, y);

		x = lastEnemyMoveX - 1;
		y = lastEnemyMoveY + 1;
		getListCellsNearLastEnemyMove(x, y);

		x = lastEnemyMoveX + 1;
		y = lastEnemyMoveY - 1;
		getListCellsNearLastEnemyMove(x, y);

		x = lastEnemyMoveX + 1;
		y = lastEnemyMoveY + 1;
		getListCellsNearLastEnemyMove(x, y);

	}

	private void getListCellsNearLastEnemyMove(int x, int y) {
		listCellsNearLastEnemyMove.add(new int[] { x, y });
	}

	/**
	 * Set weight to cells that is close to lastEnemyMove <br>
	 * in accordance with keyCell = new
	 * KeyCell(listCellsNearLastEnemyMove.get(i));</br> <br>
	 * It is executed AFTER our move. That is done in order to ... (explanation
	 * will take a lot of time=) </br>
	 */
	private void setWeightToCellNearLastEnemyMove() {

		for (int i = 0; i < listCellsNearLastEnemyMove.size(); i++) {

			KeyCell keyCell = new KeyCell(listCellsNearLastEnemyMove.get(i));
			// if cell is empty of signs
			if (fetchCellValue(keyCell.getX(), keyCell.getY()).equals(
					String.valueOf(CoreGame.DEFAULT_CELL_VALUE))) {
				// Increase cell weight on value 'weight'
				Integer cellPreviousValue = (Integer) weightMap.get(keyCell);
				int cellNewValue = NEAR_MOVE;
				if (cellPreviousValue != null) {
					cellNewValue += cellPreviousValue;
					weightMap.put(keyCell, cellNewValue);
				}
				weightMap.put(keyCell, cellNewValue);
			} else {
				// Remove from 'weightMap' if cell is not empty
				weightMap.remove(keyCell);
			}
		}

		listCellsNearLastEnemyMove.clear();
		listCellsNearLastEnemyMove.trimToSize();
	}

	// -------------------Util Methods-----------------------------

	/**
	 * @param cellX
	 * @param cellY
	 * @return cellValue from GameField::gameFieldMatrix[][] <br>
	 *         It just gets cell value</br>
	 */
	private String fetchCellValue(int cellX, int cellY) {

		String cellValue = "";
		if (CoreGame.isValueValid(cellX, cellY)) {
			cellValue += BrutforceAI.getCopyFieldMatrix()[cellX][cellY];
		}

		// LoggerAI.p("Destructor::fetchCellValue()::cellX = " + cellX
		// + " cellY = " + cellY + "cellValue = " + cellValue);

		return cellValue;
	}

	// ---------Methods for test print value =) ---------------
	@SuppressWarnings("unused")
	private void controlListCheckedCells() {
		LoggerAI.p("listCheckedCell.size() = " + listCheckedCell.size());

		for (int i = 0; i < listCheckedCell.size(); i++) {
			LoggerAI.p(i + " cell[x] = " + listCheckedCell.get(i)[0]
					+ " cell[y] = " + listCheckedCell.get(i)[1]);
		}
	}

	@SuppressWarnings("unused")
	private void controlWeightMap() {

		LoggerAI.p("Destructor::controlWeightMap::weightMap.size() = "
				+ weightMap.size());

		Iterator<KeyCell> iterator = weightMap.keySet().iterator();
		while (iterator.hasNext()) {
			KeyCell keyCell = (KeyCell) iterator.next();
			Integer value = (Integer) weightMap.get(keyCell);

			LoggerAI.p("Destructor::weightMap is: ");
			LoggerAI.p("keyCell = " + keyCell.toString() + "  value = " + value);
		}
	}

}
