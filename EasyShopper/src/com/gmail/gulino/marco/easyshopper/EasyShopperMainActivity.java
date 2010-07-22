package com.gmail.gulino.marco.easyshopper;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.gmail.gulino.marco.easyshopper.db.ShoppingDBAdapter;
import com.gmail.gulino.marco.easyshopper.db.helpers.EasyShopperSqliteOpenHelper;
import com.gmail.gulino.marco.easyshopper.domain.Shopping;
import com.gmail.gulino.marco.easyshopper.listeners.OnItemClickListenerForListAdapter;

public class EasyShopperMainActivity extends Activity {
	
	private ArrayAdapter<ModelListAdapterItem> mainMenuListAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		logMethod("onCreate");
		setContentView(R.layout.main);
		ListView listView = (ListView) findViewById(R.id.MainMenuList);
		mainMenuListAdapter = new ArrayAdapter<ModelListAdapterItem>(this, android.R.layout.simple_list_item_1);
		listView.setAdapter(mainMenuListAdapter);
		
		populateMainList();
		listView.setOnItemClickListener(new OnItemClickListenerForListAdapter());
		listView.setOnItemLongClickListener(new OnItemLongClickListenerForListAdapter());
	}
	

	private void populateMainList() {
		mainMenuListAdapter.clear();
		Runnable listPopulator = new Runnable() {
			public void run() {
				populateMainList();
			}
		};
		mainMenuListAdapter.add(new AddNewShoppingListAdapter(this));
		List<Shopping> allShoppings = new ShoppingDBAdapter(new EasyShopperSqliteOpenHelper(this)).allShoppings();
		for (Shopping shopping : allShoppings) {
			mainMenuListAdapter.add(new ShoppingListAdapter(shopping.formattedDate(this), shopping, this, listPopulator));
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		populateMainList();
		logMethod("onActivityResult: " + requestCode + ", " + resultCode + ", " + data);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		logMethod("onDestroy");
	}
	
	private void logMethod(String methodName) {
		Logger.d(this, methodName, "********** " + getClass().getName() + "::" + methodName);
	}
}