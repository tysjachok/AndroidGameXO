package kn.hqup.gamexo.ai.gardnerway;

import java.util.ArrayList;

import kn.hqup.gamexo.ai.IBrainAI;
import kn.hqup.gamexo.ai.utils.FieldMatrixConverter;
import kn.hqup.gamexo.ai.utils.LoggerAI;

public class Gardner implements IBrainAI<Object> {

	private WriterComparator writerComparator;
	private GameStatusChecker checker;
	private char[][] oldFieldMatrix;
	private ArrayList<Integer> history;
	private char enemyChip, aiChip;
	private boolean firstMove = true;
	private int[] move = new int[2];
	private final char[][] GAME_BOARD;
	private final int BOARD_SIZE;
	private final int NUM_IN_THE_ROW;
	private final String FILE_NAME;
	private final String BASE_DIR;
	private static final int X = 0;
	private static final int Y = 1;
	private static final char EMPTY = '_';
	/**
	 * testing counter
	 */
	private int counter = 0;

	public Gardner(int fieldSize, int numInTheRow) {
		GAME_BOARD = new char[fieldSize][fieldSize];
		firstMove = true;
		for (int y = 0; y < fieldSize; y++) {
			for (int x = 0; x < fieldSize; x++) {
				GAME_BOARD[x][y] = EMPTY;
			}
		}
		NUM_IN_THE_ROW = numInTheRow;
		history = new ArrayList<Integer>();
		BOARD_SIZE = fieldSize;
		FILE_NAME = "(" + BOARD_SIZE + "x" + BOARD_SIZE + ")[" + NUM_IN_THE_ROW
				+ "].xog";
		BASE_DIR = BOARD_SIZE + " x " + BOARD_SIZE + " [" + NUM_IN_THE_ROW
				+ "]/";
		checker = new GameStatusChecker(EMPTY, NUM_IN_THE_ROW, BOARD_SIZE);
		writerComparator = new WriterComparator(BASE_DIR, FILE_NAME, BOARD_SIZE);

		LoggerAI.p("Gardner::CONSTRUCTOR::firstMove = " + firstMove);
	}

	/*
	 * Constructor for tests
	 */
	// public Gardner (char[][] gB, char myChip, int numInTheRow,
	// ArrayList<Integer> h) {
	// GAME_BOARD = gB;
	// BOARD_SIZE = gB.length;
	// NUM_IN_THE_ROW = numInTheRow;
	// FILE_NAME = "(" + BOARD_SIZE + "x" + BOARD_SIZE + ")[" + NUM_IN_THE_ROW +
	// "].xog";
	// BASE_DIR = BOARD_SIZE + " x " + BOARD_SIZE + " [" + NUM_IN_THE_ROW +
	// "]/";
	// firstMove = false;
	// history = h;
	// this.aiChip = myChip;
	// enemyChip = (myChip == 'O') ? 'X' : 'O';
	// checker = new GameStatusChecker(EMPTY, NUM_IN_THE_ROW, BOARD_SIZE);
	// }

	/**
	 * ���� ��� ����� ���������� ��������� ���, � �������� ������, ���
	 * ���������� ���������� ����. ���������� ������� ���������� �� ������� ���
	 * ����� ������ ������������ ���, � ����� ����� ������ ��������� ���
	 * ������������.
	 * 
	 * @param fieldMatrixObject
	 *            ������� ����, ���������� �����.
	 * @return ������ ��������� ������, ���� ����������� ���.
	 */
	public int[] findMove(Object[][] fieldMatrixObject, Object figure) {
		counter++;
		LoggerAI.p("findMove::counter = " + counter);
		LoggerAI.p("Gardner::findMove()::1::firstMove = " + firstMove);
		
		FieldMatrixConverter<Object> converter = new FieldMatrixConverter<Object>();
		Character[][] fieldMatrixCharacter = converter
				.convertFieldMatrixToCharacter(fieldMatrixObject);
		char[][] fieldMatrix = CoordinateConverter
				.characterToChar(fieldMatrixCharacter);
		int[] enemyMove = getLastMove(fieldMatrix); // ���������� ����������
													// ���� ����������
		return findMove(enemyMove[X], enemyMove[Y]);
	}

	/**
	 * ��������� � ������������ � ��������� ������������ ����������� ��������
	 * �����������, � ������ ������� ��������� ���. ���� ��������� �����
	 * �������� ��������� �����, �� �� �������� �������� ���� ����� ������.
	 * 
	 * @param enemyX
	 *            ���������� �� X
	 * @param enemyY
	 *            ���������� �� Y
	 * @return ������ ��������� �� ���������� X � Y
	 */
	public int[] findMove(int enemyX, int enemyY) {

		LoggerAI.p("Gardner::findMove()::firstMove = " + firstMove);

		move[X] = 25; // �������� ������������ ���������� ������
		move[Y] = 25;

		if (firstMove) {
			move[X] = BOARD_SIZE / 2;
			move[Y] = BOARD_SIZE / 2;

			LoggerAI.p("Gardner::findMove():: move[X] = " + move[X] + "move[Y]"
					+ move[Y]);

			firstMove = false;
		} else {
			setChipOnBoard(enemyX, enemyY, enemyChip);

			if (history.size() > NUM_IN_THE_ROW) {
				/*
				 * Checks last AI move. Maybe it lead to win.
				 */
				int[] myMove = CoordinateConverter.getCoordinateFromIndex(
						history.get(history.size() - 2), BOARD_SIZE);
				/*
				 * If AI gets win, there is enemy last position will be written.
				 */

				if (checker.isWin(GAME_BOARD, myMove[X], myMove[Y], aiChip)) {
					writerComparator.writePosition(history);
					System.out.println("I win");
				}
				System.arraycopy(checker.getMove(), 0, move, 0, move.length);
			}

			if (move[X] == 25 || move[Y] == 25) {
				if (checker.isWin(GAME_BOARD, enemyX, enemyY, enemyChip)) {
					/*
					 * write last AI position before enemy had to move.
					 */
					ArrayList<Integer> temp = HistoryMaster.rewindHistoryBack(
							history, 1);
					writerComparator.writePosition(temp);
				}
				System.arraycopy(checker.getMove(), 0, move, 0, move.length);

				if (move[X] != 25 || move[Y] != 25) {
					setChipOnBoard(move[X], move[Y], aiChip);
					if (writerComparator.comparePos(history)) {
						/*
						 * write prior AI position
						 */
						ArrayList<Integer> temp = HistoryMaster
								.rewindHistoryBack(history, 2);
						writerComparator.writePosition(temp);
					}
					return move;
				}
			}
		}

		while (true) {
			ArrayList<Integer> tempHistory = new ArrayList<Integer>(
					history.size());

			tempHistory.addAll(history);

			ArrayList<Integer> deniedCells = new ArrayList<Integer>();

			start: while (true) {
				/*
				 * ���� ������� - ���� �� �����, ��� ��������� ����� ���������
				 * ��������, �� ������ � ��� ����� ���� �����.
				 */
				if (move[X] == 25 || move[Y] == 25) {

					ArrayList<Integer> moveList = MoveAdviser
							.preferableMoves(GAME_BOARD);

					int index = (int) Math.floor(Math.random()
							* moveList.size());
					move = CoordinateConverter.getCoordinateFromIndex(
							moveList.get(index), BOARD_SIZE);
					// move[Y] = (int) Math.floor(Math.random() * BOARD_SIZE);
				}

				for (Integer i : deniedCells) {
					if (move == CoordinateConverter.getCoordinateFromIndex(i,
							BOARD_SIZE)) {
						continue start;
					}
				}
				if (isCellEmpty(move[X], move[Y])) { // we have to play in empty
														// cell
					break;
				}
				move[X] = 25;
				move[Y] = 25;
			}

			tempHistory.add(CoordinateConverter.getIndexOfCell(move[X],
					move[Y], BOARD_SIZE));
			/*
			 * If there is all cells lead to defeat, AI marks his previous
			 * position as illegal.
			 */
			if (!writerComparator.comparePos(tempHistory))
				break;

			deniedCells.add(CoordinateConverter.getIndexOfCell(move[X],
					move[Y], BOARD_SIZE));

			if (deniedCells.size() == BOARD_SIZE * BOARD_SIZE - history.size()) {

				ArrayList<Integer> temporaryHistory = HistoryMaster
						.rewindHistoryBack(tempHistory, 2);
				writerComparator.writePosition(temporaryHistory);

				break;
			}
			move[X] = 25;
			move[Y] = 25;
		}

		/*
		 * adds AI move
		 */
		setChipOnBoard(move[X], move[Y], aiChip);

		return move;
	}

	/**
	 * ������� ����� ������� ����� � ���������� �������� � ���������� ����������
	 * ���������� ����. ��� �� �� ��������� ������� ����� ����� � ���������
	 * ����� ���������� �������� ����� � ����������� �� ����������� ����������.
	 * 
	 * @param fieldMatrix
	 *            ����, ������� ������ �� ����
	 * @return move does by enemy Player.
	 */

	public int[] getLastMove(char[][] fieldMatrix) {
		LoggerAI.p("Gardner::getLastMove()::1::firstMove = " + firstMove);
		int[] enemyMove = new int[2];
		/*
		 * Very first move done by AI It recognise hwo is doing first move.
		 * First player gets sign X and second gets O.
		 */
		if (firstMove) {
			oldFieldMatrix = CoordinateConverter.copyBoard(fieldMatrix);
			for (int y = 0; y < BOARD_SIZE; y++) {
				for (int x = 0; x < BOARD_SIZE; x++) {
					if ((x == 0) && (y == 0))
						continue;
					/*
					 * In this case AI moves second;
					 */
					if (fieldMatrix[0][0] != fieldMatrix[x][y]) {
						/*
						 * Move might be done in 0-0 sell.
						 */
						if (fieldMatrix[0][0] != fieldMatrix[BOARD_SIZE - 1][BOARD_SIZE - 1]
								&& fieldMatrix[0][0] != fieldMatrix[0][1]) {

							enemyMove[X] = 0;
							enemyMove[Y] = 0;
						} else {
							enemyMove[X] = x;
							enemyMove[Y] = y;
						}
						LoggerAI.p("Gardner::getLastMove()::2::firstMove = "
								+ firstMove);
						aiChip = 'O';
						enemyChip = 'X';
						firstMove = false;
						LoggerAI.p("Gardner::getLastMove()::3::firstMove = "
								+ firstMove);
						LoggerAI.p("Gardner::getLastMove()::3::enemyMove[X] = "
								+ enemyMove[X] + " enemyMove[Y] = " + enemyMove[Y]);
						return enemyMove;
					}
				}
			}

			/*
			 * If all cells was empty AI moves first
			 */
			aiChip = 'X';
			enemyChip = 'O';
			enemyMove[X] = 100;
			enemyMove[Y] = 100;
			return enemyMove;
		}
		/*
		 * Gets last move coordinates from incoming game field.
		 */
		for (int y = 0; y < BOARD_SIZE; y++) {
			for (int x = 0; x < BOARD_SIZE; x++) {
				if (fieldMatrix[x][y] != oldFieldMatrix[x][y]) {
					int num = CoordinateConverter.getIndexOfCell(x, y,
							BOARD_SIZE);
					/*
					 * Checks if current cell is equal to previous move done by
					 * AI
					 */
					int historyNum = history.get(history.size() - 1);
					if (num != historyNum) {
						enemyMove[X] = x;
						enemyMove[Y] = y;
						oldFieldMatrix = CoordinateConverter
								.copyBoard(fieldMatrix);
						return enemyMove;
					}
				}
			}
		}
		// Has no new moves. Or move was done in occupied cell.
		return enemyMove;
	}


	public boolean isCellEmpty(int x, int y) {
		return GAME_BOARD[x][y] == EMPTY;
	}

	private void setChipOnBoard(int x, int y, char chip) {
		GAME_BOARD[x][y] = chip;
		history.add(CoordinateConverter.getIndexOfCell(x, y, BOARD_SIZE));
	}

}
