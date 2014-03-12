package kn.hqup.gamexo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import kn.hqup.gamexo.ai.WayEnum;
import kn.hqup.gamexo.gamefield.EnumEnemy;
import kn.hqup.gamexo.gamefield.Game;
import kn.hqup.gamexo.gamefield.GameField;
import kn.hqup.gamexo.gamefield.GameView;
import kn.hqup.gamexo.gamefield.players.HandlerNotHumanLocalMove;
import kn.hqup.gamexo.gamefield.players.IPlayer;
import kn.hqup.gamexo.gamefield.players.PlayerBot;
import kn.hqup.gamexo.gamefield.players.PlayerHumanLocal;
import kn.hqup.gamexo.utils.Logger;
import kn.hqup.gamexo.utils.Sounder;

/**
 * @author Andrew2212
 * 
 */
public class GameActivity extends FragmentActivity implements OnClickListener {

	private static LinearLayout ltRadioGroup;
	private static LinearLayout ltGameScore;
	private static LinearLayout ltLevelDifficulty;
	private static LinearLayout ltConnectoinMode;
	// Info BEFORE game start
	private static RelativeLayout ltInfoGame;
	private static TextView tvInfo_WinCount;
	private static TextView tvInfo_FieldSize;
	private static TextView tvInfo_NumChecked;
	// Info AFTER competition is over
	private static RelativeLayout ltGameResult;
	private static TextView tvGameResult_winnerName;

	private static RelativeLayout ltFieldView;

	private static RadioGroup radioGroup;
	private static TextView tvUserName;
	private static TextView tvEnemyName;
	private static TextView tvUserCount;// Count of the Win
	private static TextView tvEnemyCount;// Count of the Win
	private static int scoreUser = 0;
	private static int scoreEnemy = 0;

	private static TextView tvDifficult;
	private TextView tvMode;

	private Button btnReset;
	private Button btnStart;

	private static int numCompetitionWin; // 'winCount' from Preferences
	private static boolean isCompetitioinOver = false;

	private static IPlayer playerUser;
	private static IPlayer playerEnemy;
	private static IPlayer currentPlayer;
	// Set value into 'setPrefsValue()'
	private static char signPlayerUser;

	private static Game game;
	private static GameView gameView;
	public static HandlerNotHumanLocalMove handler;

	private static int countSteps = 0;
	private static String strDifficult;
	private static EnumEnemy enemy;
	private static WayEnum wayEnum;

	private static Context context;

	/**
	 * If 'infoGameInit()' for info inscription data is called - it's true;
	 * false otherwise
	 */
	public static boolean isInfoGameInit = false;
	/**
	 * If button 'Start Game' is pressed - it's true; false otherwise
	 */
	public static boolean isGameStart = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Set FullScreen mode
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		// Hide status bar
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_game);

		context = this;
		init();
		identificatioinModeGame();
		setPrefsValue();

		btnReset.setOnClickListener(this);
		btnStart.setOnClickListener(this);

	}

	@Override
	protected void onResume() {
		super.onResume();

		isInfoGameInit = false;
		isGameStart = false;
		countSteps = 0;
		scoreUser = 0;
		scoreEnemy = 0;
	}

	@Override
	public void onClick(View v) {
		Logger.v();
		Sounder.doSound(this, R.raw.beep);

		switch (v.getId()) {

		case R.id.btn_GameReset:
			// if set of games is over
			if (isCompetitioinOver) {
				isCompetitioinOver = false;
				// Restart Activity
				Intent intent = getIntent();
				finish();
				startActivity(intent);
			} else {
				Game.killGame();
				// Start 'new Game()'
				initNewGame();
			}
			Logger.v("currentPlayer = " + currentPlayer);
			// Try to get enemy move
			getNotHumanLocalEnemyMove();// if 'bot' has first step

			break;

		case R.id.btn_GameStart:
			// Count of steps in current 'game' set
			countSteps = 0;
			// Start 'new Game()'
			initNewGame();
			// Exchange visible of the buttons
			btnStart.setVisibility(View.GONE);
			btnReset.setVisibility(View.VISIBLE);
			// Hide RadoiGroup and show Game Score
			hideRadioGroup();
			// Hide layout 'Info'
			ltInfoGame.setVisibility(View.GONE);
			// Flag 'isGameStart'
			isGameStart = true;
			// Choose currentUser
			currentPlayer = playerUser;
			Logger.v("currentPlayer = " + currentPlayer);

			// Try to get enemy move
			getNotHumanLocalEnemyMove();// for if 'bot' has first step
			break;

		default:
			break;
		}

	}

	/**
	 * Initialization 'newGame'
	 */
	private static void initNewGame() {

		// Difficult (kind) of playerBot brain
		getDifficulty();
		// Start 'new Game()'
		game = new Game(enemy, wayEnum);
		playerUser = game.getPlayerUser();
		playerEnemy = game.getPlayerEnemy();
		// Set controller into GameView
		gameView.setGameFieldController(Game.getGameFieldController());
		// Refresh GameView
		gameView.invalidate();
		// Request focus on GameView
		gameView.requestFocusFromTouch();
		// Try to use Handler
		initHandler();
	}

	/**
	 * Toggles tvPlayerName color in accordance with queue of steps <br>
	 * It is called into GameView::onTouchEvent() after executing
	 * 'GameField.setSignToCell(cellX, cellY)'
	 */
	public static void switchPlayer() {
		Logger.v();
		int red = context.getResources().getColor(R.color.red);
		int green = context.getResources().getColor(R.color.green);

		if (countSteps % 2 == 0) {
			tvUserName.setTextColor(red);
			tvEnemyName.setTextColor(green);
			currentPlayer = playerEnemy;

			if (!(Game.getIsGameOver())) {
				Logger.v("*******GameOver**********");
				getNotHumanLocalEnemyMove();
			}

		} else {
			tvUserName.setTextColor(green);
			tvEnemyName.setTextColor(red);
			currentPlayer = playerUser;

		}

		Sounder.doSound(context, R.raw.beep);
		countSteps++;
	}

	/**
	 * It's called into GameView within its method 'onDraw' <br>
	 * it's executed AFTER definition 'viewSize' and corresponding
	 * 'fieldSizeCalculated'
	 */
	public static void initInfoGame() {
		Logger.v();
		// This condition exist in order to call this method one time only
		if (isGameStart || isInfoGameInit)
			return;

		// Start 'new Game()'
		initNewGame();

		// Set INFO FieldSize
		String fieldSizeString = String.valueOf(Game.getFieldSize());
		tvInfo_FieldSize.setText(context.getResources().getString(
				R.string.screen_info_field_size)
				+ " " + fieldSizeString + "x" + fieldSizeString);
		// Set INFO numCheckedSigns
		String numCheckedString = String.valueOf(Game.getNumCheckedSigns());
		tvInfo_NumChecked.setText(context.getResources().getString(
				R.string.screen_info_checked_signs)
				+ " "
				+ numCheckedString
				+ " "
				+ context.getResources().getString(
						R.string.screen_info_checked_signs_signs));

		isInfoGameInit = true;
	}

	/**
	 * Increases score previous 'currentPlayer' after 'gameOver' occurrence
	 * within GameView
	 */
	public static void increaseScoreWin() {

		if (currentPlayer.equals(playerEnemy)) {
			scoreUser += 1;
			tvUserCount.setText(String.valueOf(scoreUser));
		}

		if (currentPlayer.equals(playerUser)) {
			scoreEnemy += 1;
			tvEnemyCount.setText(String.valueOf(scoreEnemy));
		}

		if (scoreUser == numCompetitionWin) {
			isCompetitioinOver = true;
			showResultOfCompetition(tvUserName.getText().toString());
		}

		if (scoreEnemy == numCompetitionWin) {
			isCompetitioinOver = true;
			showResultOfCompetition(tvEnemyName.getText().toString());
		}

	}

	// -----------Getters and Setters--------------------

	public static char getSignPlayerUser() {
		return signPlayerUser;
	}

	/**
	 * 
	 * @return context for 'AI Gardner' getting filePath
	 */
	public static Context getContext() {
		return context;
	}

	public static IPlayer getCurrentPlayer() {
		return currentPlayer;
	}

	// -------Private Methods----------------------

	// ===========Try to use Handler==========
	private static void initHandler() {
		handler = new HandlerNotHumanLocalMove(context, gameView, playerEnemy);
	}

	private static void getNotHumanLocalEnemyMove() {

		Logger.v("currentPlayer = " + currentPlayer);
		if (currentPlayer instanceof PlayerHumanLocal)
			return;
		if (Game.getIsGameOver() || currentPlayer == null)
			return;

		Logger.v("getNotHumanLocalEnemyMove():: "
				+ playerEnemy.getClass().getSimpleName());
		Logger.v("getNotHumanLocalEnemyMove():: "
				+ playerEnemy.getIBrain().getClass().getSimpleName());

		@SuppressWarnings("rawtypes")
		Message msg = ((PlayerBot) currentPlayer).obtainMessage();
		int delay = context.getResources().getInteger(
				R.integer.delay_bot_message);
		if (!(currentPlayer instanceof PlayerHumanLocal))
			handler.sendMessageDelayed(msg, delay);
	}

	// ===========Try to use Handler==========

	private static void showResultOfCompetition(String winnerName) {
		tvGameResult_winnerName.setText(winnerName);
		ltGameResult.setVisibility(View.VISIBLE);
		ltFieldView.setVisibility(View.GONE);
	}

	private void identificatioinModeGame() {

		String namePutMode = getResources().getString(R.string.put_extra_enemy);
		enemy = (EnumEnemy) getIntent().getSerializableExtra(namePutMode);

		switch (enemy) {
		case HUMAN:
			setModeGameHuman();
			break;

		case BOT:
			seModeGameBot();
			break;

		case REMOTE:

			break;

		case REMOTE_BLUETOOTH:
			setModeGameBluetooth();
			break;

		case REMOTE_INET:
			setModeGameInet();
			break;

		default:
			break;
		}
	}

	private void hideRadioGroup() {
		getDifficulty();
		tvDifficult.setText(strDifficult);
		ltRadioGroup.setVisibility(View.GONE);
		ltGameScore.setVisibility(View.VISIBLE);
		ltLevelDifficulty.setVisibility(View.VISIBLE);
		ltConnectoinMode.setVisibility(View.GONE);
	}

	// =============Set Mode View Value===================

	private void seModeGameBot() {
		tvEnemyName.setText(R.string.screen_vs_bot);
	}

	private void setModeGameHuman() {
		setModeExceptBot();
		tvEnemyName.setText(R.string.screen_vs_human);
	}

	private void setModeGameBluetooth() {
		setModeExceptBot();
		tvEnemyName.setText(R.string.screen_vs_remote);
		tvMode.setText(R.string.screen_bluetooth);
		ltConnectoinMode.setVisibility(View.VISIBLE);
	}

	private void setModeGameInet() {
		setModeExceptBot();
		tvEnemyName.setText(R.string.screen_vs_remote);
		tvMode.setText(R.string.screen_inet);
		ltConnectoinMode.setVisibility(View.VISIBLE);
	}

	private void setModeExceptBot() {
		ltRadioGroup.setVisibility(View.GONE);
		ltGameScore.setVisibility(View.VISIBLE);
		ltLevelDifficulty.setVisibility(View.GONE);
	}

	// ============================================================

	/**
	 * Sets 'difficulty' from RadioGroup into 'strDifficult'
	 */
	private static void getDifficulty() {

		switch (radioGroup.getCheckedRadioButtonId()) {
		case R.id.radio_Easy:
			wayEnum = WayEnum.BRUTFORCE;
			strDifficult = context.getResources().getString(
					R.string.screen_easy);
			break;

		case R.id.radio_Middle:
			// wayEnum = WayEnum.BRUTFORCE;
			wayEnum = WayEnum.MINIMAX;
			strDifficult = context.getResources().getString(
					R.string.screen_middle);
			break;

		case R.id.radio_Hard:
			// wayEnum = WayEnum.SPARE;
			wayEnum = WayEnum.GARDNER;
			strDifficult = context.getResources().getString(
					R.string.screen_hard);
			break;

		default:
			wayEnum = WayEnum.NONE;
			break;
		}

	}

	/**
	 * Sets value from 'Preferences' such as 'UserName', 'FieldSize' and etc. to
	 * SOMEWHERE
	 */
	private void setPrefsValue() {

		// set value by hardCode yet. Get from Preferences soon
		signPlayerUser = GameField.VALUE_X;

		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		// Set User Name
		String userName = prefs
				.getString(
						context.getResources().getString(
								R.string.pref_user_name_key),
						context.getResources().getString(
								R.string.pref_user_name_value));
		tvUserName.setText(userName);
		// Set INFO WinCount
		String winCount = prefs
				.getString(
						context.getResources().getString(
								R.string.pref_win_count_key),
						context.getResources().getString(
								R.string.pref_win_count_value));
		tvInfo_WinCount.setText(context.getResources().getString(
				R.string.screen_info_game_to)
				+ " "
				+ winCount
				+ " "
				+ context.getResources().getString(
						R.string.screen_info_game_to_wins));

		numCompetitionWin = Integer.valueOf(winCount);
	}

	private void init() {

		ltRadioGroup = (LinearLayout) findViewById(R.id.layout_GameRadioGroup);
		ltGameScore = (LinearLayout) findViewById(R.id.layout_GameCount);
		ltLevelDifficulty = (LinearLayout) findViewById(R.id.layout_GameLevelDifficulty);
		ltConnectoinMode = (LinearLayout) findViewById(R.id.layout_GameConnectionMode);

		radioGroup = (RadioGroup) findViewById(R.id.radioGroup);

		tvUserName = (TextView) findViewById(R.id.tv_GameUserName);
		tvEnemyName = (TextView) findViewById(R.id.tv_GameEnemyName);
		tvUserCount = (TextView) findViewById(R.id.tv_GameUserCount);
		tvEnemyCount = (TextView) findViewById(R.id.tv_GameEnemyCount);
		tvDifficult = (TextView) findViewById(R.id.tv_GameDifficult);
		tvMode = (TextView) findViewById(R.id.tv_GameMode);

		ltInfoGame = (RelativeLayout) findViewById(R.id.layout_Info);
		tvInfo_WinCount = (TextView) findViewById(R.id.tv_info_WinCount);
		tvInfo_FieldSize = (TextView) findViewById(R.id.tv_info_FieldSize);
		tvInfo_NumChecked = (TextView) findViewById(R.id.tv_info_numChecked);

		ltGameResult = (RelativeLayout) findViewById(R.id.layout_GameResult);
		tvGameResult_winnerName = (TextView) findViewById(R.id.tv_GameResult_winnerName);

		ltFieldView = (RelativeLayout) findViewById(R.id.layout_Field);

		btnStart = (Button) findViewById(R.id.btn_GameStart);
		btnReset = (Button) findViewById(R.id.btn_GameReset);

		gameView = (GameView) findViewById(R.id.game_view);

	}

}
