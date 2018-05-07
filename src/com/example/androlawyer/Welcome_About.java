package com.example.androlawyer;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class Welcome_About extends Activity {
TextView lv,ln;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome_about);
		lv=(TextView)findViewById(R.id.ABTlove);
		ln=(TextView)findViewById(R.id.ABTName);
		//lv.setText("Made with <3 in India");
		ln.setText(Html.fromHtml("Designed by <font color='#C366FF'>Rahul Rajendra</font>"));
		lv.setText(Html.fromHtml("Made with <font color='#FF1B00'>♥</font> in India"));
	}
}
