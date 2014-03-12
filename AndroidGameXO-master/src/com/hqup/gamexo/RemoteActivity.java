package kn.hqup.gamexo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;

import com.hqup.gamexo.R;

import kn.hqup.gamexo.gamefield.EnumEnemy;
import kn.hqup.gamexo.utils.Sounder;

public class RemoteActivity extends Activity {

	Button btnRemoteBluetoothStart;
	Button btnRemoteInetStart;
	Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_remote);

		context = this;
		btnRemoteBluetoothStart = (Button) findViewById(R.id.btn_RemoteBluetooth);
		btnRemoteInetStart = (Button) findViewById(R.id.btn_RemoteInet);
		btnRemoteBluetoothStart.setOnClickListener(listenerBtnRemote);
		btnRemoteInetStart.setOnClickListener(listenerBtnRemote);

	}

	public OnClickListener listenerBtnRemote = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Sounder.doSound(context, R.raw.beep_notify);
			String enemy = getResources().getString(R.string.put_extra_enemy);
			Intent intent = new Intent(context, GameActivity.class);

			switch (v.getId()) {
			case R.id.btn_RemoteBluetooth:
				intent.putExtra(enemy, EnumEnemy.REMOTE_BLUETOOTH);
				startActivity(intent);
				finish();
				break;

			case R.id.btn_RemoteInet:
				intent.putExtra(enemy, EnumEnemy.REMOTE_INET);
				startActivity(intent);
				finish();
				break;

			default:
				break;
			}
		}
	};

}
