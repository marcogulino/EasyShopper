package com.google.code.easyshopper.activities.product.edittags;

import java.util.List;

import android.app.Activity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.code.easyshopper.ModelListAdapterItem;
import com.google.code.easyshopper.R;
import com.google.code.easyshopper.activities.ESTab;
import com.google.code.easyshopper.activities.product.editproduct.Refresher;
import com.google.code.easyshopper.db.ProductTagsDBAdapter;
import com.google.code.easyshopper.db.helpers.EasyShopperSqliteOpenHelper;
import com.google.code.easyshopper.domain.CartProduct;
import com.google.code.easyshopper.listeners.OnItemClickListenerForListAdapter;

public class EditProductTags implements ESTab {

	public static final String TAG = "edit_product_tags";
	private final Activity activity;
	private final CartProduct cartProduct;
	private View view;
	private ArrayAdapter<ModelListAdapterItem> tagsListAdapter;
	private AddNewTagListAdapterItem addNewTagListAdapterItem;

	public EditProductTags(Activity activity, CartProduct cartProduct) {
		this.activity = activity;
		this.cartProduct = cartProduct;
		addNewTagListAdapterItem = new AddNewTagListAdapterItem(activity, cartProduct, new Refresher() {
			
			public void refresh() {
				EditProductTags.this.refresh();
			}
		});
		tagsListAdapter = new ArrayAdapter<ModelListAdapterItem>(activity, android.R.layout.simple_list_item_1);
	}

	public View getView() {
		if (view == null)
			view = activity.getLayoutInflater().inflate(R.layout.edit_product_tags, null);
		return view;
	}

	public void setup() {
		ListView tagsListView = tagsListView();
		tagsListView.setAdapter(tagsListAdapter );
		tagsListView.setOnItemClickListener(new OnItemClickListenerForListAdapter());
		populateList();
	}
	
	public void refresh() {
		populateList();
	}

	
	public void updateValuesOnExit() {
	}
	
	
	private void populateList() {
		tagsListAdapter.clear();
		tagsListAdapter.add(addNewTagListAdapterItem);
		List<String> tagsFor = new ProductTagsDBAdapter(new EasyShopperSqliteOpenHelper(activity)).tagsFor(cartProduct);
		for (String tag : tagsFor) {
			tagsListAdapter.add(new TagListAdapterItem(tag));
		}
	}
	
	private ListView tagsListView() {
		return (ListView) activity.findViewById(R.id.ProductTags);
	}

}
