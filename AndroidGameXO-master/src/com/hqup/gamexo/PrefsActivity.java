package kn.hqup.gamexo;

import kn.hqup.gamexo.utils.Sounder;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

/**
 * 
 * @author Andrew2212
 * 
 */
public class PrefsActivity extends PreferenceActivity implements
		OnClickListener {

	private static final int ID_BUTTON_ABOUT = 99;

	// We find out right way later
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Set FullScreen mode
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		// Hide status bar
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		addPreferencesFromResource(R.xml.preferences);
		setContentView(R.layout.activity_prefs);

		// Add button 'About Developers'
		ListView v = getListView();
		Button btn_About = new Button(this);
		btn_About.setId(ID_BUTTON_ABOUT);
		btn_About.setText(getResources().getString(R.string.btn_about));
		int color = getResources().getColor(R.color.orange);
		btn_About.setTextColor(color);
		v.addFooterView(btn_About);

		btn_About.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {

		Intent intent;
		Sounder.doSound(this, R.raw.beep_notify);
		switch (v.getId()) {

		case ID_BUTTON_ABOUT:
			intent = new Intent(this, AboutActivity.class);
			startActivity(intent);
			break;
		}

	}

}
