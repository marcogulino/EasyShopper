package com.google.code.easyshopper.activities.product;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
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
import com.google.code.easyshopper.activities.SetPriceDialog;
import com.google.code.easyshopper.db.PriceDBAdapter;
import com.google.code.easyshopper.db.ProductDBAdapter;
import com.google.code.easyshopper.db.ProductShoppingDBAdapter;
import com.google.code.easyshopper.db.ShoppingDBAdapter;
import com.google.code.easyshopper.db.helpers.EasyShopperSqliteOpenHelper;
import com.google.code.easyshopper.domain.CartProduct;
import com.google.code.easyshopper.domain.Market;
import com.google.code.easyshopper.domain.Product;
import com.google.code.easyshopper.domain.Shopping;
import com.google.code.easyshopper.utility.CameraUtils;

public class ProductActivity extends Activity {

	public static final String PARAM_BARCODE = "_BARCODE_";
	public static final int PRODUCT_SAVED = 100;
	public static final String PRODUCT_SAVED_ACTION = "PRODUCT_SAVED";
	public static final String PARAM_SHOPPING = "_shopping_id_";
	private ImageView productPictureView;
	private Product product;
	private SQLiteOpenHelper sqLiteOpenHelper;
	
	public class AddToCartListener implements OnClickListener {

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
					Runnable onOk = new Runnable() {
						public void run() {
							addProductToCart();
						}
					};
					CartProduct cartProduct = new CartProduct(product, shopping, 1, null);
					new SetPriceDialog(ProductActivity.this, cartProduct, onOk).show();
				} else {
					addProductToCart();
				}
			}
		}

		private void addProductToCart() {
			ProductShoppingDBAdapter productShoppingDBAdapter = new ProductShoppingDBAdapter(sqLiteOpenHelper);
			productShoppingDBAdapter.insertNewAssociation(barcode, shopping);
			int howMany = productShoppingDBAdapter.countProductForShopping(shopping, product);
			String text = getResources().getString(R.string.ProductActivity_HowMany).replaceAll("%\\{howmany\\}",
					String.valueOf(howMany)).replace("%{shoppingList}",
					shopping.formattedDate(ProductActivity.this));
			Toast.makeText(ProductActivity.this, text, Toast.LENGTH_SHORT).show();
		}
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
			Intent intent = getIntent();
			intent.setAction(PRODUCT_SAVED_ACTION);
			setResult(PRODUCT_SAVED, intent);
			finish();
		}
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		logMethod("ProductActivity.onCreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_new_product);
		final String barcode = getIntent().getExtras().get(PARAM_BARCODE).toString();
		sqLiteOpenHelper = new EasyShopperSqliteOpenHelper(this);
		Shopping shopping = new ShoppingDBAdapter(sqLiteOpenHelper).lookUp(getIntent().getExtras().getLong(
				PARAM_SHOPPING));
		TextView barcodeTextview = (TextView) findViewById(R.id.ProductBarcode);
		barcodeTextview.setText(barcode);

		productPictureView = (ImageView) findViewById(R.id.ProductPicture);
		final EditText productName = (EditText) findViewById(R.id.ProductName);
		Button saveButton = (Button) findViewById(R.id.ProductCRUD_DoneButton);
		Button addToCart = (Button) findViewById(R.id.AddToCartButton);
		product = new ProductDBAdapter(sqLiteOpenHelper).lookup(barcode);
		if(product==null) product= new Product(barcode);

		saveButton.setOnClickListener(new SaveProductListener(productName, barcode));
		addToCart.setOnClickListener(new AddToCartListener(productName, barcode, shopping));
		addToCart.setEnabled(shopping.getMarket() != null);
		productName.setText(product.getName());
		productPictureView.setImageDrawable(product.getImage().getDrawableForProductDetails(this));
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
				startActivityForResult(intent, 1);
			}
		});
	}

	private void logMethod(String methodName) {
		Logger.d(this, methodName, "**** ");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		logMethod("onDestroy");
		cleanProductImage();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Logger.d(this, "onActivityResult", ": {" + "requestCode=" + requestCode + ", resultCode=" + resultCode
				+ ", intent: " + data + "}");
		cleanProductImage();
		productPictureView.setImageDrawable(product.getImage().getDrawableForProductDetails(this));
	}

	private void cleanProductImage() {
		if (!product.getImage().hasImage(this))
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
	

}
