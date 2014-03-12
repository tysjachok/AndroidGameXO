package kn.hqup.gamexo.gamefield.players;

import kn.hqup.gamexo.ai.IBrainAI;
import kn.hqup.gamexo.gamefield.GameField;
import kn.hqup.gamexo.gamefield.GameView;
import kn.hqup.gamexo.utils.Logger;

public class PlayerHumanLocal implements IPlayer {

	private char signPlayer;

	public PlayerHumanLocal(char signPlayer) {
		this.signPlayer = signPlayer;
		Logger.v("CONSTRUCTOR");
	}

	@Override
	public int[] doMove() {
		Logger.v();
		return GameView.getResultOnTouch();
	}

	@Override
	public boolean setMove(int cellX, int cellY, char signPlayer) {
		Logger.v();
		return GameField.setSignToCell(cellX, cellY, signPlayer);
	}

	public char getSignPlayer() {
		return signPlayer;
	}

	@Override
	public void killBrain() {
		// nothing happens
	}

	@Override
	public <T> IBrainAI<T> getIBrain() {
		// nothing happens
		return null;
	}

}
