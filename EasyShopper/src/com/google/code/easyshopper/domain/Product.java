package com.google.code.easyshopper.domain;




public class Product {
	private final String name;
	private final String barcode;
	private ProductImage productImage;
	
	public Product(String barcode) {
		this(barcode, "");
	}

	public Product( String barcode, String name) {
		this.barcode = barcode;
		this.name = name;
		this.productImage=new ProductImage(barcode);
	}
	
	public ProductImage getImage() {
		return productImage;
	}
	
	public String getBarcode() {
		return barcode;
	}
	
	public String getName() {
		return name;
	}
	
	@Override
	public String toString() {
		return "Product {barcode='" + barcode +"', name='" + name + "'}";
	}

}
