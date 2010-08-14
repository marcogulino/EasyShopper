package com.google.code.easyshopper.domain;

import java.util.Currency;
import java.util.Locale;

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

	public static Price newDefault() {
		Price price = new Price(-1);
		price.getAmount().setCurrency(defaultCurrency());
		return price;
	}

	public static Currency defaultCurrency() {
		Currency usd = Currency.getInstance("USD");
		Currency defaultCurrency;
		try {
			defaultCurrency = Currency.getInstance(Locale.getDefault());
			if( !defaultCurrency.getCurrencyCode().equals("EUR") && ! defaultCurrency.getCurrencyCode().equals("USD")) return usd;
		} catch (Exception e) {
			defaultCurrency = usd;
		}
		return defaultCurrency;
	}

}
