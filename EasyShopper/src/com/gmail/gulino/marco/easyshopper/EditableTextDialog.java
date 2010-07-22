package com.gmail.gulino.marco.easyshopper;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public abstract class EditableTextDialog extends Dialog {

	private final String title;

	public EditableTextDialog(Context context, String title) {
		super(context);
		this.title = title;
	}

	protected String getEditText() {
		return ((EditText) findViewById(R.id.Editable_Text_Dialog_Text)).getText().toString();
	}

	protected void onCreate(Bundle savedInstanceState, View.OnClickListener okButtonListener) {
		onCreate(savedInstanceState, okButtonListener, new View.OnClickListener() {

			public void onClick(View paramView) {
				cancel();
			}
		});
	}

	protected void onCreate(Bundle savedInstanceState, View.OnClickListener okButtonListener,
			View.OnClickListener cancelButtonListener) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.editable_text_dialog);
		setTitle(title);
		Button okButton = (Button) findViewById(R.id.Editable_Text_Dialog_OK);
		Button cancelButton = (Button) findViewById(R.id.Editable_Text_Dialog_Cancel);
		okButton.setOnClickListener(okButtonListener);
		cancelButton.setOnClickListener(cancelButtonListener);
	}

	@Override
	protected abstract void onCreate(Bundle savedInstanceState);
}
