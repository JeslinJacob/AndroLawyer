package com.example.androlawyer;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.os.Vibrator;
import android.text.Html;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Welcome_IPConfig extends Activity implements OnItemSelectedListener {
	TextView txtIP;
	Button btnIP;
	SharedPreferences mypref;
	Editor edit;
	int width, height;
	String changedIP = null, item, stringIP;
	ProgressDialog progress;

	@Override
	public void onBackPressed() {
		finish();
		overridePendingTransition(R.anim.animation_enter, R.anim.animation_leave);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
		super.onCreate(savedInstanceState);
		// disp();
		ActionBar bar = getActionBar();
		ColorDrawable c = new ColorDrawable(Color.parseColor("#03A9F4"));
		bar.setBackgroundDrawable(c);
		//bar.setTitle(Html.fromHtml("<font color='#ffffff'>IP Configuration</font>"));
		bar.setTitle("IP Configuration");
		bar.setIcon(android.R.color.transparent);
		bar.setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.activity_ipconfig);

		progress = new ProgressDialog(this);

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		SharedPreferences ip;
		ip = getSharedPreferences("IPaddr", Context.MODE_PRIVATE);
		changedIP = ip.getString("IP", null);

		txtIP = (TextView) findViewById(R.id.txtIPprev);
		txtIP.setText("Current IP Address: " + changedIP);
		btnIP = (Button) findViewById(R.id.btn_IP);
		Spinner IPSpinner = (Spinner) findViewById(R.id.spinner_IP);
		IPSpinner.setOnItemSelectedListener(this);

		// Spinner Drop down elements
		List<String> categories = new ArrayList<String>();
		categories.add("192.168.43.47");
		categories.add("192.168.43.77");
		categories.add("192.168.0.3");
		categories.add("192.168.0.4");
		categories.add("192.168.0.5");
		categories.add("192.168.0.6");
		categories.add("192.168.43.130");

		// Creating adapter for spinner
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
				categories);

		// Drop down layout style - list view with radio button
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		// attaching data adapter to spinner
		IPSpinner.setAdapter(dataAdapter);
		IPSpinner.setSelected(false);
	}

	public void Btn_IP(View v) {
		progress.setMessage("Changing IP address..");
		progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progress.setCancelable(false);
		progress.setCanceledOnTouchOutside(false);
		progress.show();
		stringIP = txtIP.getText().toString().trim();
		if (stringIP.equals("")) {
			Toast.makeText(getApplicationContext(), "Select an IP address", Toast.LENGTH_SHORT).show();
		} else {
			Handler handler = new Handler();
			handler.postDelayed(new Runnable() {

				@Override
				public void run() {
					// Hide your View after 3 seconds
					progress.dismiss();
					StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
					StrictMode.setThreadPolicy(policy);
					mypref = getSharedPreferences("IPaddr", Context.MODE_PRIVATE);
					edit = mypref.edit();
					edit.putString("IP", "" + item);
					edit.commit();
					txtIP.setText("New IP Address: " + item);
					confirmIP(item);
				}
			}, 1000);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			overridePendingTransition(R.anim.animation_enter, R.anim.animation_leave);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		item = parent.getItemAtPosition(position).toString();
		// txtIP.setText("Current IP Address: " + item);
	}

	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub
		// txtIP.setText("Current IP Address: " + changedIP);
	}

	public void disp() {
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		width = size.x;
		height = size.y;
		int dialogWidth = width - 100;
		int dialogHeight = height - 1300;
		getWindow().setLayout((int) dialogWidth, (int) dialogHeight);
	}

	// public boolean onKeyDown(int keycode, KeyEvent event) {
	// // if clicked button code is equal to menu button code
	// // then the if condition will turn true
	// // To perform action on another button just write keyEvent
	// // followed by dot(.) and press CTRL+Space and you will get all
	// // the code options. Select anyone and perform your action.
	// if ((keycode == KeyEvent.KEYCODE_BACK)) {
	// Toast.makeText(this,"Click Confirm.", Toast.LENGTH_SHORT).show();
	// return true;
	// }
	// return super.onKeyDown(keycode, event);
	// }

	public boolean onTouchEvent(MotionEvent event) {

		if (event.getAction() == MotionEvent.ACTION_CANCEL) {
			Toast.makeText(this, "Click Confirm.", Toast.LENGTH_SHORT).show();
		}
		return false;
	}

	public void confirmIP(String nIP) {
		AlertDialog alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setTitle("IP Address Successfully Changed");
		alertDialog.setMessage("New IP Address: " + nIP);
		alertDialog.setCancelable(false);
		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				finish();
				overridePendingTransition(R.anim.animation_enter, R.anim.animation_leave);
			}
		});
		//alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		WindowManager.LayoutParams wmlp = alertDialog.getWindow().getAttributes();
		//wmlp.gravity = Gravity.TOP | Gravity.CENTER;
		wmlp.x = width; // x position
		wmlp.y = height+180; // y position
		alertDialog.show();
	}
}
