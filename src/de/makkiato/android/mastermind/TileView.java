package de.makkiato.android.mastermind;

import java.util.List;
import java.util.Vector;

import android.content.Context;
import android.content.res.XmlResourceParser;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

public abstract class TileView extends View {
	List<Paint> colors = new Vector<Paint>();
	Paint emptyColor = new Paint();
	int[][] tileColors;
	boolean[][] isColorSet;
	int noLines = 0;
	int noPositions = 0;
	int xOffset = 0;
	int yOffset = 0;
	int tileSize = 0;
	final Paint paint = new Paint();
	Bitmap tileBmp;
	Bitmap tileBmpActive;
	private int activeLine = 0;

	public TileView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialize(context);
	}

	public TileView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initialize(context);
	}

	@Override
	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		for (int p = 0; p < noPositions; p++) {
			for (int l = 0; l < noLines; l++) {
				if (tileBmp != null) {
					canvas.drawBitmap(tileBmp, xOffset + p * tileSize, yOffset
							+ l * tileSize, paint);
				}
			}
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int measuredWidth = measureSize(widthMeasureSpec, noPositions
				* tileSize);
		int measuredHeight = measureSize(heightMeasureSpec, noLines * tileSize);
		setMeasuredDimension(measuredWidth, measuredHeight);
	}

	private void initialize(Context context) {
		setupConstants(context);
		setupTileBitmaps(context);
		setupColors(context);
	}

	protected void setupConstants(Context context) {
		Rect outRect = new Rect();
		getWindowVisibleDisplayFrame(outRect);
		try {
			noPositions = Integer.parseInt(context
					.getString(R.string.noPositions));
			noLines = Integer.parseInt(context.getString(R.string.noLines));
			tileSize = (Math.min(outRect.height(), outRect.width()) - 15)
					/ (noPositions + 1);
		} catch (NumberFormatException ex) {
			tileSize = noPositions = noLines = 0;
		}
		xOffset = 0;
		yOffset = 0;
	}

	private void setupTileBitmaps(Context context) {
		Drawable drawable = context.getResources().getDrawable(R.drawable.tile);
		Bitmap bitmap = Bitmap.createBitmap(tileSize, tileSize,
				Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, tileSize, tileSize);
		drawable.draw(canvas);
		tileBmp = bitmap;

		drawable = context.getResources().getDrawable(R.drawable.tile_active);
		bitmap = Bitmap.createBitmap(tileSize, tileSize,
				Bitmap.Config.ARGB_8888);
		canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, tileSize, tileSize);
		drawable.draw(canvas);
		tileBmpActive = bitmap;
	}

	private void setupColors(Context context) {
		tileColors = new int[noPositions][noLines];
		isColorSet = new boolean[noPositions][noLines];
		for (int p = 0; p < noPositions; p++) {
			for (int l = 0; l < noLines; l++) {
				tileColors[p][l] = -1;
				isColorSet[p][l] = false;
			}
		}

		XmlResourceParser parser = context.getResources().getXml(R.xml.colors);
		int eventType;
		try {
			eventType = parser.getEventType();
			while (eventType != XmlResourceParser.END_DOCUMENT) {
				if (eventType == XmlResourceParser.START_TAG) {
					if (parser.getName().equals("color")) {
						eventType = parser.next();
						if (eventType == XmlResourceParser.TEXT) {
							Paint p = new Paint();
							p.setColor(Color.parseColor(parser.getText()));
							p.setStyle(Style.FILL_AND_STROKE);
							colors.add(p);
						}
					} else if (parser.getName().equals("empty")) {
						eventType = parser.next();
						if (eventType == XmlResourceParser.TEXT) {
							emptyColor.setColor(Color.parseColor(parser
									.getText()));
							emptyColor.setStyle(Style.FILL);
						}
					}
				}
				eventType = parser.next();
			}
		} catch (Exception e) {
		}
	}

	private int measureSize(int measureSpec, int defaultSize) {
		int result = 0;
		int specMode = MeasureSpec.getMode(measureSpec);
		// int specSize = MeasureSpec.getSize(measureSpec);

		if (specMode == MeasureSpec.UNSPECIFIED) {
			result = defaultSize;
		} else {
			result = defaultSize;
		}
		return result;
	}

	public int[] getPosition(int x, int y) {
		return new int[] { (int) Math.floor(x / tileSize),
				(int) Math.floor(y / tileSize) };
	}

	public int getActiveLine() {
		return activeLine;
	}

	public int setInitialActiveLine() {
		int result;
		if (noLines > 0) {
			activeLine = noLines - 1;
			result = activeLine;
		} else {
			activeLine = 0;
			result = -1;
		}
		invalidate();
		return result;
	}

	public int setNextLineActive() {
		int result;
		if (activeLine > 0) {
			activeLine--;
			result = activeLine;
		} else {
			activeLine = 0;
			result = -1;
		}
		invalidate();
		return result;
	}
	
	public abstract int reset();
}
