package com.gmail.gulino.marco.easyshopper;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.gmail.gulino.marco.easyshopper.db.ProductShoppingDBAdapter;
import com.gmail.gulino.marco.easyshopper.db.helpers.EasyShopperSqliteOpenHelper;
import com.gmail.gulino.marco.easyshopper.domain.CartProduct;

public class ActionsOnProduct {

	public interface Action {
		public void run(Runnable populator);
	}

	private final Context context;
	private boolean confirm=false;
	private ProductShoppingDBAdapter productShoppingDBAdapter;
	private final CartProduct cartProduct;

	public ActionsOnProduct(Context context, CartProduct cartProduct) {
		this.context = context;
		this.cartProduct = cartProduct;
		SQLiteOpenHelper sqLiteOpenHelper = new EasyShopperSqliteOpenHelper(context);
		productShoppingDBAdapter = new ProductShoppingDBAdapter(sqLiteOpenHelper);
	}

	public Action removeAllAction() {
		return new Action() {

			public void run(Runnable populator) {
				if (askForConfirmation()) {
					productShoppingDBAdapter.deleteAll(cartProduct);
					populator.run();
				}
			}
		};
	}

	public Action decreaseByOneAction() {
		return new Action() {

			public void run(Runnable populator) {
				if (askForConfirmation()) {
					productShoppingDBAdapter.decreaseQuantity(cartProduct);
					populator.run();
				}
			}
		};
	}

	public Action setPriceAction(final Context context) {
		return new Action() {
			
			public void run(Runnable populator) {
				if(cartProduct.getShopping().getMarket()==null) {
					Toast.makeText(context, R.string.PleaseChooseMarketFirst, Toast.LENGTH_SHORT).show();
					return;
				}
				new SetPriceDialog(context, cartProduct, populator).show();
			}
		};
	}

	private boolean askForConfirmation() {
		return true;
		/*
		final HashSet<Boolean> toReturn = new HashSet<Boolean>();
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage("Are you sure you want to exit?").setCancelable(true).setPositiveButton(
				context.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						confirm=false;
					}
				}).setNegativeButton(context.getResources().getString(R.string.cancel),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						confirm=true;
						dialog.cancel();
					}
				});
		AlertDialog alert = builder.create();
		alert.show();
		return confirm;
		*/
	}

}
