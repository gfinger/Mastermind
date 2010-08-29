package de.makkiato.android.mastermind;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

public class GameView extends LinearLayout {

	public GameView(Context context, int id) {
		super(context);
		String infService = Context.LAYOUT_INFLATER_SERVICE;
		LayoutInflater li = (LayoutInflater) getContext().getSystemService(
				infService);
		li.inflate(id, this, true);
	}
	
	public GameView(Context context, AttributeSet ats) {
		super(context, ats);
	}
}
