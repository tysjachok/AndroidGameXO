package kn.hqup.gamexo;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;

import com.hqup.gamexo.R;

public class AboutActivity extends Activity implements OnClickListener {

	private Button btn_Dislike;
	private Button btn_Sad;
	private Button btn_Like;

	private Uri uriDislike;
	private Uri uriSad;
	private Uri uriLike;

	private String strDislike = "http://natribu.org/";
	private String strSad = "http://meditation-portal.com/muzyka-dlya-relaksacii-zvuki-okeana/";
	private String strLike = "https://play.google.com/store/apps/details?id=com.hqup.gamexo";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Set FullScreen mode
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		// Hide status bar
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_about);

		init();
		initUri();
		btn_Dislike.setOnClickListener(this);
		btn_Sad.setOnClickListener(this);
		btn_Like.setOnClickListener(this);
	}

	private void init() {

		btn_Dislike = (Button) findViewById(R.id.btn_AboutDislike);
		btn_Sad = (Button) findViewById(R.id.btn_AboutSad);
		btn_Like = (Button) findViewById(R.id.btn_AboutLike);
	}

	private void initUri() {
		uriDislike = Uri.parse(strDislike);
		uriSad = Uri.parse(strSad);
		uriLike = Uri.parse(strLike);
	}

	@Override
	public void onClick(View v) {
		Intent intent;

		switch (v.getId()) {

		case R.id.btn_AboutDislike:
			intent = new Intent(Intent.ACTION_VIEW, uriDislike);
			startActivity(intent);
			break;

		case R.id.btn_AboutSad:
			intent = new Intent(Intent.ACTION_VIEW, uriSad);
			startActivity(intent);
			break;

		case R.id.btn_AboutLike:
			intent = new Intent(Intent.ACTION_VIEW, uriLike);
			startActivity(intent);

			break;

		default:
			break;
		}

	}

}
