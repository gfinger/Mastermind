package de.makkiato.android.mastermind;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;

public class ResultView extends TileView {
	private int[][] results = new int[noLines][2];
	Paint blackColor = new Paint();
	Paint whiteColor = new Paint();

	public ResultView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialize();
	}

	public ResultView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initialize();
	}

	private void initialize() {
		resetResults();
		blackColor.setColor(Color.BLACK);
		blackColor.setStyle(Style.FILL);
		whiteColor.setColor(Color.WHITE);
		whiteColor.setStyle(Style.FILL);
	}

	private void resetResults() {
		for (int i = 0; i < noLines; i++) {
			for (int j = 0; j < 2; j++) {
				results[i][j] = 0;
			}
		}
	}

	@Override
	public void onDraw(Canvas canvas) {
		for (int l = 0; l < noLines; l++) {
			int blacks = 0;
			int whites = 0;
			Paint color = emptyColor;
			for (int p = 0; p < noPositions; p++) {
				if (l == getActiveLine()) {
					canvas.drawBitmap(tileBmpActive, xOffset + p * tileSize,
							yOffset + l * tileSize, paint);
				}
				else {
					canvas.drawBitmap(tileBmp, xOffset + p * tileSize, yOffset
							+ l * tileSize, paint);

					if (blacks < results[l][0]) {
						color = blackColor;
						blacks++;
					} else if (whites < results[l][1]) {
						color = whiteColor;
						whites++;
					} else {
						color = emptyColor;
					}
					canvas.drawCircle(
							xOffset + 1 + p * tileSize + tileSize / 4, yOffset
									+ 1 + l * tileSize + tileSize / 4,
							(tileSize / 4) - 4, color);
					if (blacks < results[l][0]) {
						color = blackColor;
						blacks++;
					} else if (whites < results[l][1]) {
						color = whiteColor;
						whites++;
					} else {
						color = emptyColor;
					}
					canvas.drawCircle(xOffset - 1 + p * tileSize + tileSize / 4
							* 3, yOffset + 1 + l * tileSize + tileSize / 4,
							(tileSize / 4) - 4, color);
					if (blacks < results[l][0]) {
						color = blackColor;
						blacks++;
					} else if (whites < results[l][1]) {
						color = whiteColor;
						whites++;
					} else {
						color = emptyColor;
					}
					canvas.drawCircle(
							xOffset + 1 + p * tileSize + tileSize / 4, yOffset
									- 1 + l * tileSize + tileSize / 4 * 3,
							(tileSize / 4) - 4, color);
					if (blacks < results[l][0]) {
						color = blackColor;
						blacks++;
					} else if (whites < results[l][1]) {
						color = whiteColor;
						whites++;
					} else {
						color = emptyColor;
					}
					canvas.drawCircle(xOffset - 1 + p * tileSize + tileSize / 4
							* 3, yOffset - 1 + l * tileSize + tileSize / 4 * 3,
							(tileSize / 4) - 4, color);
				}
			}
		}
	}

	@Override
	protected void setupConstants(Context context) {
		super.setupConstants(context);
		try {
			noPositions = Integer.parseInt(context
					.getString(R.string.resultNoPositions));
		} catch (NumberFormatException ex) {
			noPositions = 0;
		}
	}

	public void setResult(int activeLine, int[] result) {
		if (activeLine >= 0 && activeLine < noLines) {
			results[activeLine] = result;
			invalidate();
		}
	}

	public int reset() {
		resetResults();
		return setInitialActiveLine();
	}
}
