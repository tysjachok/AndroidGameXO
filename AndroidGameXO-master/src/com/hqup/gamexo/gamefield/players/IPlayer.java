package kn.hqup.gamexo.gamefield.players;

import kn.hqup.gamexo.ai.IBrainAI;

public interface IPlayer {
	/**
	 * 
	 * @return calculated move coordinates
	 */
	int[] doMove();

	/**
	 * 
	 * @param cellX
	 *            move coordinate 'X'
	 * @param cellY
	 *            move coordinate 'Y' <br>
	 *            Set move into the 'fieldMatrix'
	 * @return GameField.setSignToCell(cellX, cellY, signPlayer)
	 */
	boolean setMove(int cellX, int cellY, char signPlayer);

	/**
	 * 
	 * @return signPlayer 'X' or 'O'
	 */
	char getSignPlayer();

	/**
	 * Crutch for this architecture <br>
	 * Kills the brain of current playerBot</br><br>
	 * Called into Game::killGame()</br>
	 */
	void killBrain();

	/**
	 * Method for testing - it's unneeded
	 * 
	 * @return 'brain' of the PlayerBot
	 */
	<T> IBrainAI<T> getIBrain();
}
