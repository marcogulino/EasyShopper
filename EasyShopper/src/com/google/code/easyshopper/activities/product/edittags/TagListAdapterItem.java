package com.google.code.easyshopper.activities.product.edittags;

import com.google.code.easyshopper.ModelListAdapterItem;

public class TagListAdapterItem extends ModelListAdapterItem {

	private final String tag;

	public TagListAdapterItem(String tag) {
		this.tag = tag;
	}

	@Override
	public void executeOnClick() {
		// TODO Auto-generated method stub

	}

	@Override
	protected String label() {
		return tag;
	}

}
