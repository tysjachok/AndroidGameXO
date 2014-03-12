package kn.hqup.gamexo.gamefield.players;

import android.content.Context;
import android.os.AsyncTask;

import kn.hqup.gamexo.GameActivity;
import kn.hqup.gamexo.R;
import kn.hqup.gamexo.utils.Toaster;

//http://stackoverflow.com/questions/2523459/handler-vs-asynctask
/**
 * 
 * @author Andrew2212 <br>
 *         It's NOT USED yet - instead we use 'Handler'
 */
public class AsyncTaskerPlayerMove extends AsyncTask<Void, Void, int[]> {

	// private static final int X = 0;
	// private static final int Y = 1;
	private IPlayer player;
	private Context context;

	public AsyncTaskerPlayerMove(IPlayer player, Context context) {
		this.player = player;
		this.context = context;
	}

	@Override
	protected int[] doInBackground(Void... params) {

		try {
			Thread.sleep(context.getResources().getInteger(
					R.integer.delay_bot_message));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		int[] move = player.doMove();
		return move;
	}

	@Override
	protected void onPostExecute(int[] move) {
		// player.setMove(move[X], move[Y], player.getSignPlayer());
		GameActivity.switchPlayer();
		Toaster.doToastShort(context, "AsynkTasker " + player.getSignPlayer());
	}

}
