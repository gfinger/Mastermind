package de.makkiato.android.mastermind;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

public class PatternView extends TileView {

	public PatternView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public PatternView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public void onDraw(Canvas canvas) {
		for (int p = 0; p < noPositions; p++) {
			for (int l = 0; l < noLines; l++) {
				if (tileBmp != null) {
					canvas.drawBitmap(tileBmp, xOffset + p * tileSize, yOffset
							+ l * tileSize, paint);
				}
				if (isColorSet[p][l]) {
					canvas.drawCircle(xOffset + p * tileSize + tileSize / 2,
							yOffset + l * tileSize + tileSize / 2,
							(tileSize / 2) - 8, colors.get(tileColors[p][l]));
				}
			}
		}
	}

	@Override
	protected void setupConstants(Context context) {
		super.setupConstants(context);
		try {
			noLines = Integer.parseInt(context
					.getString(R.string.patternNoLines));
		} catch (NumberFormatException ex) {
			noLines = 0;
		}
	}

	public int getNumberOfPositions() {
		return noPositions;
	}

	public int getNumberOfColors() {
		return colors.size();
	}

	public void setPositionColors(int[] positionColors) {
		for (int p = 0; p < noPositions; p++) {
			tileColors[p][0] = positionColors[p];
			isColorSet[p][0] = true;
		}
		invalidate();
	}

	@Override
	public int reset() {
		// TODO Auto-generated method stub
		return 0;
	}
}
