package com.google.code.easyshopper.listeners;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;

import com.google.code.easyshopper.Logger;
import com.google.code.easyshopper.ModelListAdapterItem;

public class OnItemSelectedForModelAdapter implements OnItemSelectedListener {


	public void onItemSelected(AdapterView<?> paramAdapterView, View view, int position, long id) {
		Logger.d(this, "onItemSelected", "" + paramAdapterView + ", " + view + ", " + position + ", " + id);
		ModelListAdapterItem onItemClickListAdapter = (ModelListAdapterItem) paramAdapterView.getItemAtPosition(position);
		onItemClickListAdapter.executeOnClick();		
	}

	public void onNothingSelected(AdapterView<?> arg0) {
		Logger.d(this, "onNothingSelected", "view: " + arg0);
	}

}
