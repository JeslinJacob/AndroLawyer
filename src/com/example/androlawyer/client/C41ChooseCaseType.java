package com.example.androlawyer.client;

import com.example.androlawyer.R;
import com.example.androlawyer.R.layout;
import com.example.androlawyer.lawyer.LawyerL4MapSearch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class C41ChooseCaseType extends Activity {
	private RadioGroup radioCaseTypeGroup;
	private RadioButton radioCaseTypeButton;
	String txt;

	@Override
	public void onBackPressed() {
		this.finish();
		overridePendingTransition(R.anim.animation_enter, R.anim.animation_leave);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_c41_choose_case_type);
		radioCaseTypeGroup = (RadioGroup) findViewById(R.id.radioGroup);
	}

	public void MAP(View v) {
		int selectedId = radioCaseTypeGroup.getCheckedRadioButtonId();
		radioCaseTypeButton = (RadioButton) findViewById(selectedId);
		txt = radioCaseTypeButton.getTag().toString().trim();
		//Toast.makeText(this, "Tag " + txt, Toast.LENGTH_SHORT).show();
	}

	public void GOTOMAP(View v) {
		txt = radioCaseTypeButton.getTag().toString().trim();
		Intent i = new Intent(this, ClientC4MapSearch.class);
		i.putExtra("CaseType", txt);
		startActivity(i);
		overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
	}

}
