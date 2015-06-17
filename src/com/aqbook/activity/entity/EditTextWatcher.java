package com.aqbook.activity.entity;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;

public class EditTextWatcher implements TextWatcher {

	private Button button;
	
	public EditTextWatcher(Button button){
		this.button = button;
	}
	@Override
	public void afterTextChanged(Editable text) {
		// TODO Auto-generated method stub
		if(!text.toString().equals("")){
			button.setEnabled(true);
		}else{
			button.setEnabled(false);
		}
	}

	@Override
	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
			int arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		
	}

}
