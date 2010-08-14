package com.google.code.easyshopper.activities.product.edittags;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.google.code.easyshopper.EditableTextDialog;
import com.google.code.easyshopper.Logger;
import com.google.code.easyshopper.ModelListAdapterItem;
import com.google.code.easyshopper.R;
import com.google.code.easyshopper.activities.product.editproduct.Refresher;
import com.google.code.easyshopper.db.ProductTagsDBAdapter;
import com.google.code.easyshopper.db.helpers.EasyShopperSqliteOpenHelper;
import com.google.code.easyshopper.domain.CartProduct;
import com.google.code.easyshopper.utility.StringUtils;

public class AddNewTagListAdapterItem extends ModelListAdapterItem {

	private final class AddNewTagDialog extends EditableTextDialog {
		private AddNewTagDialog(Context context, String title) {
			super(context, title);
		}

		@Override
		protected void onCreate(Bundle savedInstanceState) {
			onCreate(savedInstanceState, new View.OnClickListener() {
				
				public void onClick(View v) {
					new ProductTagsDBAdapter(new EasyShopperSqliteOpenHelper(activity)).addTag(StringUtils.editTextToString(getEditText()), product);
					refresher.refresh();
					dismiss();
				}
			});
		}
	}

	private final Activity activity;
	private final CartProduct product;
	private final Refresher refresher;

	public AddNewTagListAdapterItem(Activity activity, CartProduct product, Refresher refresher) {
		this.activity = activity;
		this.product = product;
		this.refresher = refresher;
	}

	@Override
	public void executeOnClick() {
		Logger.d(this, "executeOnClick", "showing dialog for product " + product);
		EditableTextDialog editableTextDialog = new AddNewTagDialog(activity, label());
		editableTextDialog.show();
	}

	@Override
	protected String label() {
		return activity.getResources().getString(R.string.Add_New_Tag);
	}

}
