package com.gmail.gulino.marco.easyshopper.domain;




public class Product {
	private final String name;
	private final String barcode;
	private ProductImage productImage;
	private final int id;
	
	public Product(String barcode) {
		this(-1, barcode, "");
	}

	public Product(int id, String barcode, String name) {
		this.id = id;
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
	
	public int getId() {
		return id;
	}
	
	@Override
	public String toString() {
		return "Product {id=" + id + ", barcode='" + barcode +"', name='" + name + "'}";
	}

}
