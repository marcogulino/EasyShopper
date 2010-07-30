package com.google.code.easyshopper.activities.market;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.code.easyshopper.R;

public class MarketsSpinnerAdapter extends ArrayAdapter<MarketSpinnerItem> {

	private final Activity activity;

	public MarketsSpinnerAdapter(Activity activity) {
		super(activity, R.layout.markets_spinner_layout);
		this.activity = activity;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = activity.getLayoutInflater().inflate(R.layout.markets_spinner_layout, null);
		((TextView) view.findViewById(R.id.Market_Spinner_Title)).setText(getItem(position).toString());
		((TextView) view.findViewById(R.id.Market_Spinner_Distance)).setText(getItem(position).distance());
		return view;
	}
	
	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		return getView(position, convertView, parent);
	}

}
