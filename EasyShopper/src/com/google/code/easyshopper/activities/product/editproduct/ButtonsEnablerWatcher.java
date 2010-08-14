package com.google.code.easyshopper.activities.product.editproduct;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

import com.google.code.easyshopper.utility.StringUtils;

public class ButtonsEnablerWatcher implements TextWatcher {
	private final Button saveButton;
	private final Button addToCart;
	private final EditText otherTextBoxToCheck;

	public ButtonsEnablerWatcher(Button saveButton, Button addToCart, EditText otherTextBoxToCheck) {
		this.saveButton = saveButton;
		this.addToCart = addToCart;
		this.otherTextBoxToCheck = otherTextBoxToCheck;
	}

	public void onTextChanged(CharSequence s, int start, int before, int count) {
		boolean otherIsOk = StringUtils.editTextToString(otherTextBoxToCheck).length() > 0;
		int myLength = s.length();
		saveButton.setEnabled(myLength>0 && otherIsOk);
		addToCart.setEnabled(myLength>0 && otherIsOk);
	}

	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
	}

	public void afterTextChanged(Editable s) {
	}

}
