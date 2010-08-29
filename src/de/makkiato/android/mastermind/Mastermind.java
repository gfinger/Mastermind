package de.makkiato.android.mastermind;

import java.util.Random;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ScrollView;

/**
 * @author gregor
 */
public class Mastermind extends Activity {

	private PatternView patternView;
	private GridView gridView;
	private ResultView resultView;
	private ScrollView scrollView;
	private GameView gameView;
	private final int DIALOG = 0;

	private int[] patternColors;

	private int gameState;
	private static final int INITIAL = 0;
	private static final int STARTED = 1;
	private static final int WIN = 2;
	private static final int LOSE = 3;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		gameState = INITIAL;
		gameView = new GameView(this, R.layout.main);
		setContentView(gameView);
		patternView = (PatternView) gameView.findViewById(R.id.patternView);
		gridView = (GridView) gameView.findViewById(R.id.gridView);
		resultView = (ResultView) gameView.findViewById(R.id.resultView);
		resultView.setOnTouchListener(new ResultTouchListener());
		scrollView = (ScrollView) gameView.findViewById(R.id.scrollView);

		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		newgame();
	}

	@Override
	public Dialog onCreateDialog(int id) {
		AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
		alertBuilder.setTitle("Placeholder Title");
		alertBuilder.setItems(R.array.dialog_text_list,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int item) {
						if (item == 0) {
							newgame();
							dialog.dismiss();
						} else {
							endgame();
							dialog.dismiss();
						}
					}
				});
		return alertBuilder.create();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		switch (item.getItemId()) {
		case (R.id.capitulate): {
			gameState = LOSE;
			capitulate();
			return true;
		}
		case (R.id.endgame): {
			gameState = LOSE;
			endgame();
			return true;
		}
		case (R.id.newgame): {
			gameState = INITIAL;
			newgame();
			return true;
		}
		}
		return false;
	}
	
	@Override
	protected void onPrepareDialog(int id, Dialog dialog) {
		switch (gameState) {
		case WIN:
			dialog.setTitle(R.string.win_dialog_title);
			break;
		case LOSE:
			dialog.setTitle(R.string.lose_dialog_title);
			break;
		default:
			break;
		}
	}

	private void endgame() {
		finish();
	}

	private void capitulate() {
		patternView.setVisibility(View.VISIBLE);
		resultView.setEnabled(false);
		gridView.setEnabled(false);
	}

	private void newgame() {
		if (gridView.reset() != -1) {
			gridView.setEnabled(true);
			patternColors = getRandomPositionColors(patternView
					.getNumberOfPositions(), patternView.getNumberOfColors());
			patternView.setPositionColors(patternColors);
			patternView.setVisibility(View.GONE);
			resultView.reset();
			resultView.setEnabled(true);
			scrollView.post(new Runnable() {
				public void run() {
					scrollView.fullScroll(ScrollView.FOCUS_DOWN);
				}

			});
			gameState = STARTED;
		}
	}

	private int[] getRandomPositionColors(int noPositions, int noColors) {
		int[] colors = new int[noPositions];
		Random rand = new Random();
		for (int i = 0; i < noPositions; i++) {
			colors[i] = rand.nextInt(noColors);
		}
		return colors;
	}

	private class ResultTouchListener implements OnTouchListener {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (event.getAction() == MotionEvent.ACTION_UP) {
				int[] lineColors = gridView.getLineColors();
				int[] result = evaluateGameState(lineColors);
				resultView.setResult(gridView.getActiveLine(), result);
				if (result[0] == 4) {
					gameState = WIN;
					showDialog(DIALOG);
				} else {
					int activeLine = gridView.setNextLineActive();
					resultView.setNextLineActive();
					if (activeLine == -1) {
						gameState = LOSE;
						showDialog(DIALOG);
					}
				}
			}
			return true;
		}

	}

	private int[] evaluateGameState(int[] lineColors) {
		int noPositions = patternView.getNumberOfPositions();
		int guessColors[] = new int[noPositions];
		int gameColors[] = new int[noPositions];
		int blacks = 0;
		int whites = 0;
		for (int i = 0; i < noPositions; i++) {
			guessColors[i] = lineColors[i];
			gameColors[i] = patternColors[i];
		}
		for (int i = 0; i < noPositions; i++) {
			if (gameColors[i] == guessColors[i]) {
				blacks++;
				guessColors[i] = -11;
				gameColors[i] = -12;
			}
		}
		for (int i = 0; i < noPositions; i++) {
			for (int j = 0; j < noPositions; j++) {
				if (gameColors[i] == guessColors[j]) {
					whites++;
					guessColors[j] = -11;
					gameColors[i] = -12;
					break;
				}
			}
		}
		return new int[] { blacks, whites };
	}
}