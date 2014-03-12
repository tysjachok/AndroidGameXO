package kn.hqup.gamexo.ai.brutforceway;

import kn.hqup.gamexo.ai.CoreGame;
import kn.hqup.gamexo.ai.IBrainAI;
import kn.hqup.gamexo.ai.utils.FieldMatrixConverter;
import kn.hqup.gamexo.ai.utils.GetterLastEnemyMove;
import kn.hqup.gamexo.ai.utils.LoggerAI;

/**
 * Created with IntelliJ IDEA. User: Andrew2212 <br>
 * Just simply AI </br>
 */
public class BrutforceAI implements IBrainAI<Object> {

	private int[] MOVE;
	private static final int X = 0;
	private static final int Y = 1;
	private static Character[][] fieldMatrix;

	private GetterLastEnemyMove getterLastEnemyMove;
	private Constructor constructor;
	private Destructor destructor;
	private boolean isFirstMoveDone = false;// For giving signBot

	public BrutforceAI(int fieldSize, int numChecked) {
		// Set 'CoreGame options' where we'll get them from
		CoreGame.initCoreGame(fieldSize, numChecked);
		GameOptions.initGameOptions();
		getterLastEnemyMove = new GetterLastEnemyMove();
		constructor = new Constructor();
		destructor = new Destructor();
	}

	/**
	 * @param fieldMatrixObject
	 *            Object[][] matrix from 'Game core'
	 * @param figure
	 *            player's sign
	 * @return MOVE i.e. int[2] - coordinates of cell
	 */
	public int[] findMove(Object[][] fieldMatrixObject, Object figure) {

		FieldMatrixConverter<Object> converter = new FieldMatrixConverter<Object>();
		Character[][] fieldMatrixCharacter = converter
				.convertFieldMatrixToCharacter(fieldMatrixObject);
		BrutforceAI.fieldMatrix = fieldMatrixCharacter;

		LoggerAI.p("Brutforce::findMove()::fieldMatrix.length = "
				+ BrutforceAI.fieldMatrix.length);

		// Executes only one time
		LoggerAI.p("Brutforce::isFirstMoveDone = " + isFirstMoveDone);
		if (!isFirstMoveDone) {
			GameOptions.setSignBotAndSignEnemy(converter
					.convertSignToCharacter(figure));
			LoggerAI.p("isFirstMoveDone*****************signBot = "
					+ GameOptions.getSignBot());
			isFirstMoveDone = true;
		}
		// Get lastEnemyMove (if it exists)
		int[] lastEnemyMove = getterLastEnemyMove.getLastEnemyMove(fieldMatrix);

		if (null != lastEnemyMove) {

			LoggerAI.p("Brutforce::findMove()::lastEnemyMove[X] = "
					+ lastEnemyMove[X] + " lastEnemyMove[Y] = "
					+ lastEnemyMove[Y]);

			// Get ConstructiveWIN MOVE
			int[] moveConstructiveWin = constructor.getConstructiveWinMove();
			if (moveConstructiveWin != null) {
				LoggerAI.p("BrutforceAI::findMove moveConstructiveWin[X] = "
						+ moveConstructiveWin[X] + " moveConstructiveWin[Y] = "
						+ moveConstructiveWin[Y] + " signBot = "
						+ GameOptions.getSignBot());
				// Here should be GAME OVER =)
				return moveConstructiveWin;
			}
			// Get Destructive MOVE
			int[] moveDestructive = destructor.getDestructiveMove(
					lastEnemyMove[X], lastEnemyMove[Y]);
			if (moveDestructive != null) {
				LoggerAI.p("BrutforceAI::findMove moveDestructive[X] = "
						+ moveDestructive[X] + " moveDestructive[Y] = "
						+ moveDestructive[Y] + " signBot = "
						+ GameOptions.getSignBot());
				constructor.setLastMyOwnMove(moveDestructive);
				getterLastEnemyMove.setMyOwnMove(moveDestructive[X],
						moveDestructive[Y], GameOptions.getSignBot());
				return moveDestructive;
			}
		}

		// Get Constructive move
		MOVE = getConstructiveMove();
		LoggerAI.p("BrutforceAI::getConstructiveMove constructiveMove[X] = "
				+ MOVE[X] + " constructiveMove[Y] = " + MOVE[Y]);
		constructor.setLastMyOwnMove(MOVE);
		getterLastEnemyMove.setMyOwnMove(MOVE[X], MOVE[Y],
				GameOptions.getSignBot());
		return MOVE;
	}

	public static Character[][] getCopyFieldMatrix() {
		int size = fieldMatrix.length;
		Character[][] fieldMatrixCopy = new Character[size][size];
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				fieldMatrixCopy[i][j] = fieldMatrix[i][j];
			}
		}
		return fieldMatrixCopy;

	}

	// ---------Private Methods---------------------

	private boolean isCellValid(int x, int y) {

		if (!CoreGame.isValueValid(x, y)) {
			return false;
		}

		if (fieldMatrix[x][y] == CoreGame.DEFAULT_CELL_VALUE) {
			return true;
		}

		return false;
	}

	private int[] getConstructiveMove() {
		// Get CONSTRUCTIVE move
		int[] constructiveMove = new int[2];
		// This is what you calculate
		do {
			constructiveMove[X] = (int) Math.floor(Math.random()
					* fieldMatrix.length);
			constructiveMove[Y] = (int) Math.floor(Math.random()
					* fieldMatrix.length);
		} while (!isCellValid(constructiveMove[X], constructiveMove[Y]));

		return constructiveMove;
	}

}