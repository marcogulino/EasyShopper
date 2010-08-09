package com.google.code.easyshopper.domain;

public class CartProduct {

	private Product product;
	private final Shopping shopping;
	private final long quantity;
	private final Price price;

	public CartProduct(Product product, Shopping shopping, long quantity, Price price) {
		this.product = product;
		this.shopping = shopping;
		this.quantity = quantity;
		this.price = price;
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
		return getClass().getName() + " { product=" + product + ", shopping=" + shopping + ", quantity=" + quantity + ", price=" + price + " }";
	}

	public Price getPrice() {
		return price;
	}

	public void setProduct(Product product) {
		this.product=product;
	}
	
}
