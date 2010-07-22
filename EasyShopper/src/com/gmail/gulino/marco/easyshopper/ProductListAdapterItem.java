package com.gmail.gulino.marco.easyshopper;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gmail.gulino.marco.easyshopper.domain.CartProduct;

public class ProductListAdapterItem extends ProductModelListAdapterItem {

	private final Activity activity;
	private final CartProduct cartProduct;
	private String setPrice;
	private String removeFromCart;
	private String removeAllQuantitiesFromCart;
	private String decreaseByOne;
	private Map<String, ActionsOnProduct.Action> longClickActions;
	private final Runnable populateList;

	public ProductListAdapterItem(CartProduct cartProduct, Activity activity, Runnable populateList) {
		this.cartProduct = cartProduct;
		this.activity = activity;
		this.populateList = populateList;
		setPrice = activity.getResources().getString(R.string.Shopping_SetProductPrice);
		removeFromCart = activity.getResources().getString(R.string.Shopping_RemoveProductFromCart);
		removeAllQuantitiesFromCart = activity.getResources()
				.getString(R.string.Shopping_RemoveProductForAllQuantities);
		decreaseByOne = activity.getResources().getString(R.string.Shopping_DecreaseProductQuantity);

		longClickActions = new HashMap<String, ActionsOnProduct.Action>();
		ActionsOnProduct actionsOnProduct = new ActionsOnProduct(activity, cartProduct);
		longClickActions.put(removeFromCart, actionsOnProduct.removeAllAction());
		longClickActions.put(removeAllQuantitiesFromCart, actionsOnProduct.removeAllAction());
		longClickActions.put(decreaseByOne, actionsOnProduct.decreaseByOneAction());
		longClickActions.put(setPrice, actionsOnProduct.setPriceAction(activity));
	}

	public void executeOnClick() {
		Logger.d(this, "executeOnClick", "" + cartProduct);
		new LaunchProductActivity(activity, cartProduct.getShopping()).startProductActivity(cartProduct.getProduct().getBarcode());
	}

	@Override
	public boolean executeOnLongClick() {
		AlertDialog.Builder builder = new Builder(activity);
		String[] longClickActions = new String[] { setPrice, removeFromCart };
		if (cartProduct.getQuantity() > 1) {
			longClickActions = new String[] { setPrice, decreaseByOne, removeAllQuantitiesFromCart };
		}
		longClickDialog(builder, longClickActions);
		return true;
	}

	private void longClickDialog(AlertDialog.Builder builder, final String[] longClickActions) {
		builder.setItems(longClickActions, new OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				ProductListAdapterItem.this.longClickActions.get(longClickActions[which]).run(populateList);
			}
		});
		builder.create().show();
	}

	@Override
	public String label() {
		return cartProduct.getProduct().getName();
	}

	@Override
	public View getView(Activity activity) {
		View mainView = activity.getLayoutInflater().inflate(R.layout.products_item_layout, null);
		((TextView) mainView.findViewById(R.id.ProductName)).setText(label());
		long quantity = cartProduct.getQuantity();
		String pricePerQuantity = cartProduct.getPrice() != null ? quantity + "x " + priceLabel(1) : "";
		String totalPrice = quantity>1 ? priceLabel(quantity): "";
		((TextView) mainView.findViewById(R.id.ProductPriceQuantity)).setText(pricePerQuantity);
		((TextView) mainView.findViewById(R.id.ProductPrice)).setText(totalPrice);
		if(cartProduct.getProduct().getImage().hasImage(activity)) {
			((ImageView) mainView.findViewById(R.id.ListViewProductImage)).setImageDrawable(cartProduct.getProduct().getImage().getDrawableForProductDetails(activity));
		}

		return mainView;
	}

	private String priceLabel(long quantity) {
		String price = cartProduct.getPrice() == null ? "" : cartProduct.getPrice().getAmount().getReadableAmountLabel(quantity);
		return price;
	}
}