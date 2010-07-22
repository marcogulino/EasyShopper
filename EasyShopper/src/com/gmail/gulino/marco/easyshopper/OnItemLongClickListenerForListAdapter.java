package com.gmail.gulino.marco.easyshopper;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;

public class OnItemLongClickListenerForListAdapter implements OnItemLongClickListener {

	public boolean onItemLongClick(AdapterView<?> viewAdapter, View view, int position, long id) {
		ModelListAdapterItem onItemClickListAdapter = (ModelListAdapterItem) viewAdapter.getItemAtPosition(position);
		return onItemClickListAdapter.executeOnLongClick();
	}

}
