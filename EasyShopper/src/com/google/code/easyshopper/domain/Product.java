package com.google.code.easyshopper.domain;




public class Product {
	private String name;
	private final String barcode;
	private ProductImage productImage;
	private int numberOfPriceCharacters;
	
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
	
	public void setName(String name) {
		this.name = name;
	}
	
	public int getNumberOfPriceCharacters() {
		return numberOfPriceCharacters;
	}
	
	public void setNumberOfPriceCharacters(int numberOfPriceCharacters) {
		this.numberOfPriceCharacters = numberOfPriceCharacters;
	}
	
	public boolean isPriceDefinedInBarcode() {
		return numberOfPriceCharacters>0;
	}
	
	@Override
	public String toString() {
		return "Product {barcode='" + barcode +"', name='" + name + "', numberOfPriceCharacters=" + numberOfPriceCharacters + "}";
	}

}
