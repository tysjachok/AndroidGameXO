package kn.hqup.gamexo.gamefield;

import kn.hqup.gamexo.GameActivity;
import kn.hqup.gamexo.ai.WayEnum;
import kn.hqup.gamexo.gamefield.players.IPlayer;
import kn.hqup.gamexo.gamefield.players.PlayerBot;
import kn.hqup.gamexo.gamefield.players.PlayerHumanLocal;
import kn.hqup.gamexo.utils.Logger;

public class Game {

	private static EnumEnemy enumEmemy;
	private static WayEnum wayEnum;

	private static int fieldSize;
	private static int numCheckedSigns;
	private static GameFieldController gameFieldController;
	private static boolean isGameOver;

	private static IPlayer playerUser;
	private static IPlayer playerEnemy;

	private char signPlayerUser;
	private char signPlayerEnemy;
	private static GameStateEnum gameState;

	/**
	 * constructor counter (it's crutch)
	 */
	private static int counter = 0;

	public enum GameStateEnum {
		WIN, DRAW, CONTINUE;
	}

	public Game(EnumEnemy enumEmemy, WayEnum wayEnum) {
		Logger.v("Game::CONSTRUCTOR::counter = " + counter);
		counter++;
		Game.enumEmemy = enumEmemy;
		Game.wayEnum = wayEnum;
		Game.fieldSize = GameView.getFieldSizeCalculated();
		Game.numCheckedSigns = GameView.getNumCheckedSigns();

		// Logger.v("*******Game CONSTRUCTOR*******");
		// Logger.v("*******Game::wayEnum = " + wayEnum);
		// Logger.v("*******Game::enumEmemy = " + enumEmemy);
		Logger.v("*******Game::fieldSize = " + fieldSize);
		Logger.v("*******Game::numCheckedSigns = " + numCheckedSigns);

		// Create new fieldMatrix and default fill it
		GameField.initNewFieldMatrix(fieldSize);

		// Create new Controller
		gameFieldController = new GameFieldController(fieldSize,
				numCheckedSigns);

		// Create playerUser (local human)
		playerUser = null;
		signPlayerUser = GameActivity.getSignPlayerUser();
		playerUser = new PlayerHumanLocal(signPlayerUser);

		// Create playerEnemy
		playerEnemy = null;
		signPlayerEnemy = specifySignPlayerEnemy(signPlayerUser);
		playerEnemy = chooseAndInitEnemy(signPlayerEnemy);

		isGameOver = false;
	}

	/**
	 * Crutch for this architecture <br>
	 * It's called into GameActivity after event 'GameOver' or within
	 * 'onResume()'</br>
	 */
	public static void killGame() {
		if (0 < counter) {
			playerUser = null;
			playerEnemy.killBrain();
			playerEnemy = null;
			gameFieldController = null;
			GameField.killFieldMatrix();
		}

	}

	public static GameStateEnum getGameState() {
		return gameState;
	}

	public static void setGameState(GameStateEnum gameState) {
		Game.gameState = gameState;
	}

	public static GameFieldController getGameFieldController() {
		return gameFieldController;
	}

	public static void setIsGameOver(boolean isGameOver) {
		Game.isGameOver = isGameOver;
	}

	public static boolean getIsGameOver() {
		return isGameOver;
	}

	private char specifySignPlayerEnemy(char signPlayerUser) {
		if (signPlayerUser == GameField.VALUE_X) {
			return GameField.VALUE_O;
		}
		return GameField.VALUE_X;
	}

	private <T> IPlayer chooseAndInitEnemy(char signPlayerEnemy) {

		Logger.v("Game::chooseAndInitEnemy()::counter = " + counter);

		switch (enumEmemy) {
		case HUMAN:
			playerEnemy = new PlayerHumanLocal(signPlayerEnemy);
			break;

		case BOT:
			playerEnemy = new PlayerBot<T>(fieldSize, numCheckedSigns,
					signPlayerEnemy);
			break;

		case REMOTE:

			break;

		case REMOTE_BLUETOOTH:

			break;

		case REMOTE_INET:

			break;

		default:
			break;
		}

		return playerEnemy;
	}

	// ------------Getters and Setters--------------

	public IPlayer getPlayerUser() {
		return playerUser;
	}

	public void setPlayerUser(IPlayer player) {
		Game.playerUser = player;
	}

	public IPlayer getPlayerEnemy() {
		return playerEnemy;
	}

	public void setPlayerEnemy(IPlayer playerEnemy) {
		Game.playerEnemy = playerEnemy;
	}

	public static EnumEnemy getEnumEmemy() {
		return enumEmemy;
	}

	public static void setEnumEmemy(EnumEnemy enumEmemy) {
		Game.enumEmemy = enumEmemy;
	}

	public static WayEnum getWayEnum() {
		return wayEnum;
	}

	public static void setWayEnum(WayEnum wayEnum) {
		Game.wayEnum = wayEnum;
	}

	public static int getFieldSize() {
		return fieldSize;
	}

	public static void setFieldSize(int fieldSize) {
		Game.fieldSize = fieldSize;
	}

	public static int getNumCheckedSigns() {
		return numCheckedSigns;
	}

	public static void setNumCheckedSigns(int numCheckedSigns) {
		Game.numCheckedSigns = numCheckedSigns;
	}

}
