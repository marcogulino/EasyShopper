package com.google.code.easyshopper.domain;

import java.util.Currency;

public class Price {

	private final Amount amount;
	private Market market;
	private Product product;
	private long id;
	
	public Price(long id) {
		this.id = id;
		this.amount=new Amount();
	}

	public Amount getAmount() {
		return amount;
	}
	
	public long getLongAmount() {
		return getAmount().getAmount();
	}

	public void setId(long id) {
		this.id = id;
	}

	public Currency getCurrency() {
		return getAmount().getCurrency();
	}

	public long getId() {
		return id;
	}

	public Market getMarket() {
		return market;
	}

	public Product getProduct() {
		return product;
	}

	public void setMarket(Market market) {
		this.market = market;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	@Override
	public String toString() {
		return this.getClass().getName() + " { id=" + id + ", amount=" + amount + ", market=" + market + ", product=" + product + "}";
	}

}
