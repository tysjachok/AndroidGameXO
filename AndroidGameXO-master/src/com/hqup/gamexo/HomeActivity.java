package kn.hqup.gamexo;

import kn.hqup.gamexo.gamefield.EnumEnemy;
import kn.hqup.gamexo.utils.Logger;
import kn.hqup.gamexo.utils.Sounder;
import kn.hqup.gamexo.utils.Toaster;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class HomeActivity extends FragmentActivity implements OnClickListener {

	private TextView tvHiUser;
	private Button btnGameVsHuman;
	private Button btnGameVsBot;
	private Button btnGameRemote;
	private Button btnStatistic;
	private Button btnPrefs;

	private SharedPreferences prefs;
	private ImageView ivHomeAnim;

	private class AnimatorTween implements Runnable {
		public void run() {
			Animation animationTween = AnimationUtils.loadAnimation(
					getApplicationContext(), R.anim.home_anim);
			ivHomeAnim.startAnimation(animationTween);
			animationTween.setFillAfter(true);
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Set FullScreen mode
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		// Hide status bar
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_home);

		init();
		Sounder.doSound(this, R.raw.beep_notify);

		// Animation is been executed
		if (isSplashScreenAllowed()) {
			ivHomeAnim.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.splashscreen));
			new Thread(new AnimatorTween()).start();
		}

	}

	@Override
	protected void onResume() {
		super.onResume();

		setUserName();
	}

	@Override
	public void onClick(View v) {

		Logger.v();
		Intent intent;
		String enemy = getResources().getString(R.string.put_extra_enemy);

		switch (v.getId()) {

		case R.id.btn_HomeGameVsHuman:
			Sounder.doSound(this, R.raw.beep_notify);
			intent = new Intent(this, GameActivity.class);
			intent.putExtra(enemy, EnumEnemy.HUMAN);
			startActivity(intent);
			break;

		case R.id.btn_HomeGameVsBot:
			Sounder.doSound(this, R.raw.beep_notify);
			intent = new Intent(this, GameActivity.class);
			intent.putExtra(enemy, EnumEnemy.BOT);
			startActivity(intent);
			break;

		case R.id.btn_HomeGameRemote:
			Sounder.doSound(this, R.raw.beep);
			intent = new Intent(this, RemoteActivity.class);
			startActivity(intent);
			break;

		case R.id.btn_HomePrefs:
			Sounder.doSound(this, R.raw.beep);
			intent = new Intent(this, PrefsActivity.class);
			startActivity(intent);
			break;

		case R.id.btn_HomeStatistic:
			Sounder.doSound(this, R.raw.wilhelm_scream);
			Toaster.doToastShort(this, R.string.toast_nothing_happens);
			break;

		default:
			Toaster.doToastShort(this, R.string.toast_nothing_happens);
			break;
		}

	}

	// --------Private Methods---------------------

	private void init() {

		tvHiUser = (TextView) findViewById(R.id.tv_HomeHiUser);
		btnGameVsHuman = (Button) findViewById(R.id.btn_HomeGameVsHuman);
		btnGameVsBot = (Button) findViewById(R.id.btn_HomeGameVsBot);
		btnGameRemote = (Button) findViewById(R.id.btn_HomeGameRemote);
		btnStatistic = (Button) findViewById(R.id.btn_HomeStatistic);
		btnPrefs = (Button) findViewById(R.id.btn_HomePrefs);

		ivHomeAnim = (ImageView) findViewById(R.id.iv_HomeAnim);

		btnGameVsHuman.setOnClickListener(this);
		btnGameVsBot.setOnClickListener(this);
		btnGameRemote.setOnClickListener(this);
		btnStatistic.setOnClickListener(this);
		btnPrefs.setOnClickListener(this);

		prefs = PreferenceManager.getDefaultSharedPreferences(this);
	}

	private boolean isSplashScreenAllowed() {

		boolean result = prefs
				.getBoolean(
						getResources().getString(
								R.string.pref_splash_screen_key), true);
		return result;
	}

	private void setUserName() {
		String userName = prefs.getString(
				getResources().getString(R.string.pref_user_name_key),
				getResources().getString(R.string.pref_user_name_value));
		tvHiUser.setText(getResources().getString(R.string.screen_hi) + " "
				+ userName + "!");
	}
}
