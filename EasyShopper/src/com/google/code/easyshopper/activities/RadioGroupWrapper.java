package com.google.code.easyshopper.activities;

import android.widget.RadioGroup;


public class RadioGroupWrapper implements IRadioGroup {

	private final RadioGroup radioGroup;

	public RadioGroupWrapper(RadioGroup radioGroup) {
		this.radioGroup = radioGroup;
	}

	public int getCheckedRadioButtonId() {
		return radioGroup.getCheckedRadioButtonId();
	}

}
