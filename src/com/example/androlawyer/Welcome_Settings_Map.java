package com.example.androlawyer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.os.Vibrator;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Welcome_Settings_Map extends Activity implements OnItemSelectedListener {

	int item;
	SharedPreferences mypref;
	Editor edit;
	String changedIP = "";
	String cid;
	ProgressDialog progress;
	int width, height;
	AlertDialogManager alert = new AlertDialogManager();

	@Override
	public void onBackPressed() {
		this.finish();
		//(R.anim.animation_enter, R.anim.animation_leave);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		disp();
		int id = Resources.getSystem().getIdentifier("action_bar_title", "id", "android");
		TextView txtTitle = (TextView) findViewById(id);
		txtTitle.setGravity(Gravity.CENTER);
		txtTitle.setWidth(width);

		ActionBar bar = getActionBar();
		ColorDrawable c = new ColorDrawable(Color.parseColor("#03A9F4"));
		bar.setBackgroundDrawable(c);
		bar.setTitle("Map Configuration");
		bar.setIcon(android.R.color.transparent);
		bar.setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.activity_settings_map);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		SharedPreferences ip;
		ip = getSharedPreferences("IPaddr", Context.MODE_PRIVATE);
		changedIP = ip.getString("IP", null);

		mypref = getSharedPreferences("preferenceclogin", Context.MODE_PRIVATE);
		cid = mypref.getString("clientid", null);

		Spinner MapSpinner = (Spinner) findViewById(R.id.SettingsMapStatusSpinner);
		MapSpinner.setOnItemSelectedListener(this);

		// Spinner Drop down elements
		List<String> categories = new ArrayList<String>();
		categories.add("No");
		categories.add("Yes");

		// Creating adapter for spinner
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
				categories);

		// Drop down layout style - list view with radio button
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		// attaching data adapter to spinner
		MapSpinner.setAdapter(dataAdapter);
		MapSpinner.setSelected(false);
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		item = position;
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub

	}

	public void MAP(View v) {
		Vibrator vibr = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		vibr.vibrate(100);
		progress = new ProgressDialog(this);
		progress.setMessage("Saving Map Visibility..");
		progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progress.setCancelable(false);
		progress.setCanceledOnTouchOutside(false);
		progress.show();

		try {
			HttpClient client = new DefaultHttpClient();
			HttpPost request = new HttpPost("http://" + changedIP + ":8080/AndroLawyer/MapSettings.jsp");
			List<NameValuePair> PostParameters = new ArrayList<NameValuePair>();
			PostParameters.add(new BasicNameValuePair("fcid", cid));
			PostParameters.add(new BasicNameValuePair("fitem", "" + item));

			UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(PostParameters);

			request.setEntity(formEntity);

			HttpResponse response = client.execute(request);

			BufferedReader obj = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			StringBuffer sb = new StringBuffer("");
			String Line = "";

			while ((Line = obj.readLine()) != null) {
				sb.append(Line);
			}
			obj.close();
			String result = sb.toString().trim();
			if (result.equals("Success")) {
				Handler handler = new Handler();
				handler.postDelayed(new Runnable() {

					@Override
					public void run() {
						// Hide your View after 3 seconds
						progress.dismiss();
						alert();
					}
				}, 2000);
			} else {
				progress.dismiss();
				alert.showAlertDialog(this, null, "Unknown Error", false);
			}
		} catch (Exception e) {
			e.printStackTrace();
			progress.dismiss();
			alert.showAlertDialog(this, null, "Please Turn Ur Internet Connection ON", false);
		}
	}

	public void alert() {
		AlertDialog alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setTitle(null);
		alertDialog.setMessage("Profile Set");
		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// Welcome_Profile.this.finish();
				mypref = getSharedPreferences("mapsetting", Context.MODE_PRIVATE);
				edit = mypref.edit();
				edit.putString("status", "" + item);
				edit.commit();
				finish();
			}
		});
		alertDialog.show();
	}

	public void disp() {
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		width = size.x;
		height = size.y;
		int dialogWidth = width - 280;
		int dialogHeight = height - 950;
		getWindow().setLayout((int) dialogWidth, (int) dialogHeight);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			//overridePendingTransition(R.anim.blink, R.anim.abc_slide_out_bottom);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
