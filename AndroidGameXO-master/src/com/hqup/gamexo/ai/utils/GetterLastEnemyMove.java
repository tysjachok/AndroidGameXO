package kn.hqup.gamexo.ai.utils;

import kn.hqup.gamexo.ai.CoreGame;

/**
 * Created with IntelliJ IDEA. User: Andrew2212 Date: 25.09.13 Time: 10:37 <br>
 * * Gets last enemy move from fieldMatrix[][] by comparing with
 * previousFieldMatrix[][] and set our own move into previousFieldMatrix[][]
 */
public class GetterLastEnemyMove {

	private Character[][] previousFieldMatrix;
	private int fieldSize;

	private final int[] MOVE = new int[2];
	private static final int X = 0;
	private static final int Y = 1;

	public GetterLastEnemyMove() {

		fieldSize = CoreGame.getFieldSize();
		previousFieldMatrix = new Character[fieldSize][fieldSize];
		fillDefaultGameMatrix(previousFieldMatrix);

	}

	public int[] getLastEnemyMove(Character[][] fieldMatrix) {
		LoggerAI.p("GetterLastEnemyMove::getLastEnemyMove()::");

		for (int i = 0; i < fieldSize; i++) {
			for (int j = 0; j < fieldSize; j++) {

				if (!fieldMatrix[i][j].equals(CoreGame.DEFAULT_CELL_VALUE)
						&& (!previousFieldMatrix[i][j]
								.equals(fieldMatrix[i][j]))) {

					MOVE[X] = i;
					MOVE[Y] = j;

					LoggerAI.p("previousFieldMatrix[" + i + "][" + j + "] = "
							+ previousFieldMatrix[i][j]);
					LoggerAI.p("fieldMatrix[" + i + "][" + j + "] = "
							+ fieldMatrix[i][j]);
					LoggerAI.p("GetterLastEnemyMove::getLastEnemyMove():: MOVE[X] = "
							+ MOVE[X] + ", MOVE[Y] = " + MOVE[Y]);

					previousFieldMatrix[i][j] = fieldMatrix[i][j];
					return MOVE;

				}
			}
		}

		return null;
	}

	public void setMyOwnMove(int moveX, int moveY, char signBot) {
		// checkout for random - it isn't needed for real AI
		LoggerAI.p("GetterLastEnemyMove::1::setMyOwnMove::move = " + moveX
				+ ", " + moveY);
		if (isCellValid(moveX, moveY)) {
			LoggerAI.p("GetterLastEnemyMove::setMyOwnMove::move = " + moveX
					+ ", " + moveY);
			previousFieldMatrix[moveX][moveY] = signBot;
		}

	}

	// -----------------Private Methods---------------------------

	private void fillDefaultGameMatrix(Character[][] fieldMatrix) {
		for (int i = 0; i < fieldMatrix.length; i++) {
			for (int j = 0; j < fieldMatrix.length; j++) {
				fillDefaultCurrentCell(i, j);
			}
		}
	}

	private void fillDefaultCurrentCell(int i, int j) {
		previousFieldMatrix[i][j] = CoreGame.DEFAULT_CELL_VALUE;
	}

	private boolean isCellValid(int x, int y) {

		if (!CoreGame.isValueValid(x, y)) {

			return false;
		}

		if (previousFieldMatrix[x][y].equals(CoreGame.DEFAULT_CELL_VALUE)) {
			return true;
		}
		LoggerAI.p("GetterLastEnemyMove::::isCellValid()::previousFieldMatrix[x][y] =  "
				+ previousFieldMatrix[x][y]);
		return false;
	}

}
