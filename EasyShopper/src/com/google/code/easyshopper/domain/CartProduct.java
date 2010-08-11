package com.google.code.easyshopper.domain;

import java.util.Currency;

public class CartProduct {

	private Product product;
	private final Shopping shopping;
	private final long quantity;
	private final Price price;
	private final String fullBarcode;

	public CartProduct(String fullBarcode, Product product, Shopping shopping, long quantity, Price price) {
		this.fullBarcode = fullBarcode;
		this.product = product;
		this.shopping = shopping;
		this.quantity = quantity;
		this.price = price;
	}
	
	public String getFullBarcode() {
		return fullBarcode;
	}
	
	public String getBarcodeForProduct() {
		return barcodeForProduct(product.getNumberOfPriceCharacters(), getFullBarcode());
	}

	public static String barcodeForProduct(int numberOfPriceCharacters, String fullBarcode) {
		try {
			return fullBarcode.substring(0, fullBarcode.length()- numberOfPriceCharacters);
		} catch (Exception e) {
			return fullBarcode;
		}
	}
	public Product getProduct() {
		return product;
	}
	
	public long getQuantity() {
		return quantity;
	}
	
	public Shopping getShopping() {
		return shopping;
	}
	
	@Override
	public String toString() {
		return getClass().getName() + " { fullBarcode=" + fullBarcode  + ", product=" + product + ", shopping=" + shopping + ", quantity=" + quantity + ", price=" + price + " }";
	}

	public Price getPrice() {
		return price;
	}

	public void setProduct(Product product) {
		this.product=product;
	}

	public Amount calculatePriceAmount() {
		if(product.isPriceDefinedInBarcode()) {
			return calculatePriceAmount(price.getCurrency());
		}
		return price.getAmount();
	}

	public Amount calculatePriceAmount(Currency currency) {
		Amount amount;
		if( ! product.isPriceDefinedInBarcode() ) {
			amount=price.getAmount();
		} else
		{
			StringBuilder price= new StringBuilder(fullBarcode.substring(fullBarcode.length() - product.getNumberOfPriceCharacters()));
			amount = new Amount().setCurrency(currency);
			price.insert(price.length()-3, amount.getSeparator());
			amount.setFromReadableAmount(price.toString());
		}
		return amount;
	}
}
