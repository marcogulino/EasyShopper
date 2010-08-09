package com.google.code.easyshopper.activities.product;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.code.easyshopper.Logger;
import com.google.code.easyshopper.R;
import com.google.code.easyshopper.activities.ESTab;
import com.google.code.easyshopper.db.PriceDBAdapter;
import com.google.code.easyshopper.db.ProductDBAdapter;
import com.google.code.easyshopper.db.ProductShoppingDBAdapter;
import com.google.code.easyshopper.domain.CartProduct;
import com.google.code.easyshopper.domain.Market;
import com.google.code.easyshopper.domain.Product;
import com.google.code.easyshopper.domain.Shopping;
import com.google.code.easyshopper.utility.CameraUtils;

public class EditProduct implements ESTab {

	private final Activity activity;
	private final SQLiteOpenHelper sqLiteOpenHelper;
	private ImageView productPictureView;
	private Product product;
	private final String barcode;
	private final Shopping shopping;
	private View view;
	private UpdateValues updateOnExit;
	static final String TAG = "edit_product";

	public EditProduct(String barcode, Product product, Shopping shopping, Activity activity, SQLiteOpenHelper sqLiteOpenHelper) {
		this.barcode = barcode;
		this.product = product;
		this.shopping = shopping;
		this.activity = activity;
		this.sqLiteOpenHelper = sqLiteOpenHelper;
	}
	
	public View getView() {
		if(view==null)
			view = activity.getLayoutInflater().inflate(R.layout.edit_product, null);
		return view;
	}

	public void setup() {

		TextView barcodeTextview = (TextView) activity.findViewById(R.id.ProductBarcode);
		barcodeTextview.setText(barcode);

		productPictureView = (ImageView) activity.findViewById(R.id.ProductSmallPicture);
		final EditText productName = (EditText) activity.findViewById(R.id.ProductName);
		Button saveButton = (Button) activity.findViewById(R.id.ProductCRUD_DoneButton);
		Button addToCart = (Button) activity.findViewById(R.id.AddToCartButton);
		saveButton.setOnClickListener(new SaveProductListener(productName, barcode));
		addToCart.setOnClickListener(new AddToCartListener(productName, barcode, shopping));
		addToCart.setEnabled(shopping.getMarket() != null);
		productName.setText(product.getName());
		this.updateOnExit=new UpdateValues() {
			public void update() {
				product.setName(productName.getText().toString().trim());
			}
			
		};
		productPictureView.setImageDrawable(product.getImage().getDrawableForProductDetails(activity));
		productPictureView.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				cleanProductImage();
				File file = new File(CameraUtils.SAVED_PATH);
				boolean mkdirs = file.mkdirs();
				Logger.d(this, "onClick", "create directories for " + file + ": " + mkdirs);
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				Uri savePath = CameraUtils.getImagePath(barcode);
				Logger.d(this, "onClick", "trying to save to file: " + savePath);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, savePath);
				intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
				activity.startActivityForResult(intent, 1);
			}
		});	
	}
	
	void cleanProductImage() {
		if (!product.getImage().hasImage(activity))
			return;
		try {
			BitmapDrawable drawable = (BitmapDrawable) productPictureView.getDrawable();
			Bitmap bitmap = drawable.getBitmap();
			productPictureView.setImageBitmap(null);
			bitmap.recycle();
		} catch (Exception e) {

		}
	}
	
	private void saveProduct(String barcode, EditText productName) {
		ProductDBAdapter productDBAdapter = new ProductDBAdapter(sqLiteOpenHelper);
		productDBAdapter.save(barcode, productName.getText().toString().trim());
		this.product = productDBAdapter.lookup(barcode);
	}
	

	private final class SaveProductListener implements OnClickListener {
		private final EditText productName;
		private final String barcode;

		private SaveProductListener(EditText productName, String barcode) {
			this.productName = productName;
			this.barcode = barcode;
		}

		public void onClick(View v) {
			saveProduct(barcode, productName);
			Intent intent = activity.getIntent();
			intent.setAction(EditProductActivity.PRODUCT_SAVED_ACTION);
			activity.setResult(EditProductActivity.PRODUCT_SAVED, intent);
			activity.finish();
		}
	}

	
	public class AddToCartListener implements OnClickListener {

		private final class AddProductToCart implements Runnable {
			public void run() {
				addProductToCart();
			}
		}

		private final EditText productName;
		private final String barcode;
		private final Shopping shopping;

		public AddToCartListener(EditText productName, String barcode, Shopping shopping) {
			this.productName = productName;
			this.barcode = barcode;
			this.shopping = shopping;
		}

		public void onClick(View v) {
			saveProduct(barcode, productName);
			Market market = shopping.getMarket();
			if (market != null) {
				if (new PriceDBAdapter(sqLiteOpenHelper).priceFor(barcode, market) == null) {
					Runnable onOk = new AddProductToCart();
					onOk.run();
					CartProduct cartProduct = new CartProduct(product, shopping, 1, null);
					// TODO show setPriceDialog tab instead
//					new SetPriceDialog(activity, cartProduct, onOk).show();
				} else {
					addProductToCart();
				}
			}
		}

		private void addProductToCart() {
			ProductShoppingDBAdapter productShoppingDBAdapter = new ProductShoppingDBAdapter(sqLiteOpenHelper);
			productShoppingDBAdapter.insertNewAssociation(barcode, shopping);
			int howMany = productShoppingDBAdapter.countProductForShopping(shopping, product);
			String text = activity.getResources().getString(R.string.ProductActivity_HowMany).replaceAll("%\\{howmany\\}",
					String.valueOf(howMany)).replace("%{shoppingList}",
					shopping.formattedDate(activity));
			Toast.makeText(activity, text, Toast.LENGTH_SHORT).show();
		}
	}


	public void refreshProductImage() {
		cleanProductImage();
		productPictureView.setImageDrawable(product.getImage().getDrawableForProductDetails(activity));
	}

	public void updateValuesOnExit() {
		if(updateOnExit != null ) updateOnExit.update();
	}
}
