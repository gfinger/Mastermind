package de.makkiato.android.mastermind;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class GridView extends TileView {
	public GridView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public GridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public void onDraw(Canvas canvas) {
		for (int p = 0; p < noPositions; p++) {
			for (int l = 0; l < noLines; l++) {
				if (l == getActiveLine() && tileBmpActive != null) {
					canvas.drawBitmap(tileBmpActive, xOffset + p * tileSize,
							yOffset + l * tileSize, paint);
				}
				if (l != getActiveLine() && tileBmp != null) {
					canvas.drawBitmap(tileBmp, xOffset + p * tileSize, yOffset
							+ l * tileSize, paint);
				}
				if (isColorSet[p][l]) {
					canvas.drawCircle(xOffset + p * tileSize + tileSize / 2,
							yOffset + l * tileSize + tileSize / 2,
							(tileSize / 2) - 8, colors.get(tileColors[p][l]));
				} else {
					canvas.drawCircle(xOffset + p * tileSize + tileSize / 2,
							yOffset + l * tileSize + tileSize / 2,
							(tileSize / 2) - 8, emptyColor);
				}
			}
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (isEnabled() && event.getAction() == MotionEvent.ACTION_UP) {
			int x = (int) event.getX();
			int y = (int) event.getY();
			int[] position = getPosition(x, y);
			int pos = position[0];
			int line = position[1];
			if (line == getActiveLine()) {
				nextButtonColor(pos, line);
				isColorSet[pos][line] = true;
				invalidate();
			}
		}
		return true;
	}

	protected void setupConstants(Context context) {
		super.setupConstants(context);
		try {
			noLines = Integer.parseInt(context.getString(R.string.gridNoLines));
		} catch (NumberFormatException ex) {
			noLines = 0;
		}
	}

	private void nextButtonColor(int pos, int line) {
		tileColors[pos][line] = (tileColors[pos][line] + 1) % colors.size();
	}

	private void resetColors() {
		for (int p = 0; p < noPositions; p++) {
			for (int l = 0; l < noLines; l++) {
				tileColors[p][l] = colors.size();
				isColorSet[p][l] = false;
			}
		}
	}

	public int reset() {
		resetColors();
		return setInitialActiveLine();
	}

	public int[] getLineColors() {
		int[] lineColors = new int[noPositions];
		int activeLine = getActiveLine();
		if (activeLine >= 0 && activeLine < noLines) {
			for (int p = 0; p < noPositions; p++) {
				lineColors[p] = tileColors[p][activeLine];
			}
		}
		return lineColors;
	}
}
