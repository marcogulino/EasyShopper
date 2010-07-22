package com.google.code.easyshopper.domain;

import java.util.Currency;

import com.google.code.easyshopper.Logger;

public class Amount {
	private Currency currency;
	private long amount;

	public Amount() {
		this(0, null);
	}

	public Amount(long amount, Currency currency) {
		this.amount = amount;
		this.currency = currency;
	}

	public long getAmount() {
		return amount;
	}

	public Currency getCurrency() {
		return currency;
	}

	public void setAmount(long amount) {
		this.amount = amount;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	public String getReadableAmount(long quantity) {
		long currentAmount = amount * quantity;
		long scaleFactor = scaleFactor();
		long intPart = (long) (currentAmount / scaleFactor);
		long fractPart = (currentAmount - (intPart * scaleFactor));
		Logger.d(this, "getReadableAmount", "amount: " + amount + ", quantity: " + quantity + ", intPart: " + intPart
				+ ", fractPart: " + fractPart);
		String fractPartAsString = String.valueOf(fractPart);
		int defaultFractionDigits = currency.getDefaultFractionDigits();
		Logger.d(this, "getReadableAmount", "fractPartAsString: '" + fractPartAsString + "(" + fractPartAsString.length()  + ")', defaultFractionDigits: "
				+ defaultFractionDigits);
		while(fractPartAsString.length() < defaultFractionDigits) {
			fractPartAsString="0".concat(fractPartAsString);
			Logger.d(this, "getReadableAmount", "adding a 0: fractPartAsString: '" + fractPartAsString  + "(" + fractPartAsString.length()  + ")', defaultFractionDigits: "
					+ defaultFractionDigits);
		}
		return "" + intPart + getSeparator() + fractPartAsString.substring(0, defaultFractionDigits);
	}

	public void setFromReadableAmount(String amount) {
		if (amount.indexOf(getSeparator()) < 0) {
			this.amount = Long.parseLong(amount) * scaleFactor();
			return;
		}
		int separatorIndex = amount.indexOf(getSeparator());
		long intPart = Long.parseLong(amount.substring(0, separatorIndex)) * scaleFactor();
		String originalDecimalPart = amount.substring(separatorIndex + 1, amount.length());
		long decimalPart = Long.parseLong(originalDecimalPart.concat("00000").substring(0,
				currency.getDefaultFractionDigits()));
		this.amount = intPart + decimalPart;
	}

	private long scaleFactor() {
		return (long) Math.pow(10, currency.getDefaultFractionDigits());
	}

	private char getSeparator() {
		return '.';
		// DecimalFormat decimalFormat = (DecimalFormat)
		// DecimalFormat.getInstance(Locale.getDefault());
		// return decimalFormat.getDecimalFormatSymbols().getDecimalSeparator();
	}

	@Override
	public String toString() {
		return this.getClass().getName() + " { amount=" + amount + ", currency=" + currency + " }";
	}

	public String getReadableAmountLabel(long quantity) {
		return getReadableAmount(quantity) + " " + currency.getSymbol();
	}
}
