/**
 * 
 */
package com.gmail.gulino.marco.easyshopper.listeners;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.gmail.gulino.marco.easyshopper.ModelListAdapterItem;

public class OnItemClickListenerForListAdapter implements OnItemClickListener {

		public void onItemClick(AdapterView<?> paramAdapterView,
				View paramView, int position, long id) {
			ModelListAdapterItem onItemClickListAdapter = (ModelListAdapterItem) paramAdapterView.getItemAtPosition(position);
			onItemClickListAdapter.executeOnClick();
		}
	}