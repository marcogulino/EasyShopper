package com.google.code.easyshopper;

public class SimpleStringModelAdapter extends ModelListAdapterItem {

	private final String string;

	public SimpleStringModelAdapter(String string) {
		this.string = string;
	}

	@Override
	public void executeOnClick() {

	}

	@Override
	protected String label() {
		return string;
	}

}
