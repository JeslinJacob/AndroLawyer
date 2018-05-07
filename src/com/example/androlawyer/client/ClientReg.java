package com.example.androlawyer.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.example.androlawyer.AlertDialogManager;
import com.example.androlawyer.R;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.os.Vibrator;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class ClientReg extends Activity {
	EditText fullname, dob, phone, email, user, pass, home, place, city, state, country;
	TextView txtPD, txtLAT, txtLNG;
	RadioButton lgenderm, lgenderf;
	String Sgender="", Sdob, Sage, lat, lng;
	SharedPreferences mypref, ip;
	Editor edit;
	String changedIP = "";
	ProgressDialog progress;
	AlertDialogManager alert = new AlertDialogManager();
	boolean flagpref = false;

	private Calendar calendar;
	private int year, month, day;

	@Override
	public void onBackPressed() {
		regoutfunc();
	}

	public void regoutfunc() {
		Vibrator vibr = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		vibr.vibrate(100);
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		// builder.setIcon(R.drawable.logalert);
		builder.setTitle("Registration Incomplete");
		builder.setMessage("Entered Details will be all cleared !").setPositiveButton("Let me out",
				dialogClickListener);
		builder.setNegativeButton("Let me complete", dialogClickListener).show();
	}

	DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int which) {
			switch (which) {
			case DialogInterface.BUTTON_POSITIVE:
				finish();
				overridePendingTransition(R.anim.animation_enter, R.anim.animation_leave);
				break;

			case DialogInterface.BUTTON_NEGATIVE:
				dialog.dismiss();
				break;
			}
		}
	};

	@Override
	protected void onResume() {
		super.onResume();
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		mypref = getSharedPreferences("locationcreg", Context.MODE_PRIVATE);
		flagpref = mypref.getBoolean("flag", true);
		mypref = getSharedPreferences("locationcreg", Context.MODE_PRIVATE);
		lat = mypref.getString("lat", null);
		lng = mypref.getString("lng", null);
		txtLAT = (TextView) findViewById(R.id.txtLATCreg);
		txtLNG = (TextView) findViewById(R.id.txtLNGCreg);
		txtLAT.setText(lat);
		txtLNG.setText(lng);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_creg);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		ip = getSharedPreferences("IPaddr", Context.MODE_PRIVATE);
		changedIP = ip.getString("IP", null);
		mypref = getSharedPreferences("locationcreg", Context.MODE_PRIVATE);
		edit = mypref.edit();
		edit.putString("lat", "Click Above..");
		edit.putString("lng", "Click Above..");
		edit.putBoolean("flag", true);
		edit.commit();

		progress = new ProgressDialog(this);

		txtPD = (TextView) findViewById(R.id.txtPDCreg);
		txtPD.setPaintFlags(txtPD.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

		fullname = (EditText) findViewById(R.id.CnameCreg);
		dob = (EditText) findViewById(R.id.CDOBCreg);
		phone = (EditText) findViewById(R.id.CPhnCreg);
		email = (EditText) findViewById(R.id.CEmailCreg);
		user = (EditText) findViewById(R.id.CUserCreg);
		pass = (EditText) findViewById(R.id.CPassCreg);
		lgenderm = (RadioButton) findViewById(R.id.CGenderMCreg);
		lgenderf = (RadioButton) findViewById(R.id.CGenderFCreg);
		home = (EditText) findViewById(R.id.DdateCreg);
		place = (EditText) findViewById(R.id.placeCreg);
		city = (EditText) findViewById(R.id.cityCreg);
		state = (EditText) findViewById(R.id.stateCreg);
		country = (EditText) findViewById(R.id.countryCreg);
		txtLAT = (TextView) findViewById(R.id.txtLATCreg);
		txtLNG = (TextView) findViewById(R.id.txtLNGCreg);
		txtLAT.setText("Click Above..");
		txtLNG.setText("Click Above..");
		calendar = Calendar.getInstance();
		year = calendar.get(Calendar.YEAR);
		month = calendar.get(Calendar.MONTH);
		day = calendar.get(Calendar.DAY_OF_MONTH);
	}

	public void GenderCheck(View v) {
		boolean checked = ((RadioButton) v).isChecked();
		switch (v.getId()) {
		case R.id.CGenderMCreg:
			if (checked)
				lgenderf.setChecked(false);
			Sgender = "Male";
			break;
		case R.id.CGenderFCreg:
			if (checked)
				lgenderm.setChecked(false);
			Sgender = "Female";
			break;
		default:
			break;
		}
	}

	public String agecalc() {
		String Sdob = dob.getText().toString().trim();
		int length = Sdob.length();
		String SdobYear = Sdob.substring(Sdob.length() - 4);
		int DobYear = Integer.parseInt(SdobYear);
		int CurrentYear = Calendar.getInstance().get(Calendar.YEAR);
		int intSage = CurrentYear - DobYear;
		String Sage = "" + intSage;
		return Sage;
	}

	public void MAP(View v) {
		Animation a = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale_up);
		v.startAnimation(a);
		Vibrator vibr = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		vibr.vibrate(100);
		String splace = place.getText().toString().trim();
		String scity = city.getText().toString().trim();
		String sstate = state.getText().toString().trim();
		String scountry = country.getText().toString().trim();
		if ((splace.equals("")) || (scity.equals("")) || (sstate.equals("")) || (scountry.equals(""))) {
			alert.showAlertDialog(this, null, "Type your address", false);
		} else {
			String addr = splace + ", " + scity + ", " + sstate + ", " + scountry;
			Intent i = new Intent(this, ClientRegMap.class);
			i.putExtra("iaddress", addr);
			startActivity(i);
			overridePendingTransition(R.anim.abc_slide_in_bottom, R.anim.abc_slide_in_top);
		}
	}

	// DATE PICKER FUNCTIONS

	@SuppressWarnings("deprecation")
	public void mydate(View view) {
		showDialog(999);
		// Toast.makeText(getApplicationContext(), "Date Selected",
		// Toast.LENGTH_SHORT)
		// .show();
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		// TODO Auto-generated method stub
		if (id == 999) {
			DatePickerDialog dtd = new DatePickerDialog(this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT, myDateListener,
					year, month, day);
			// DatePickerDialog dtd = new DatePickerDialog(this, myDateListener,
			// year, month, day);
			Calendar minDate = Calendar.getInstance();
			minDate.set(calendar.get(Calendar.YEAR) - 70, calendar.get(Calendar.MONTH),
					calendar.get(Calendar.DAY_OF_MONTH));
			// dt.DatePicker.MaxDate = maxDate.TimeInMillis;
			dtd.getDatePicker().setMinDate(minDate.getTimeInMillis());
			Calendar maxDate = Calendar.getInstance();
			maxDate.set(calendar.get(Calendar.YEAR) - 10, calendar.get(Calendar.MONTH),
					calendar.get(Calendar.DAY_OF_MONTH));
			// dt.DatePicker.MaxDate = maxDate.TimeInMillis;
			dtd.getDatePicker().setMaxDate(maxDate.getTimeInMillis());
			dtd.setTitle("Date Picker");
			dtd.setMessage("Enter/Select Date Of Birth: ");
			return dtd;
		}
		return null;
	}

	private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker dt, int year, int month, int day) {
			showDate(year, month + 1, day);
			// Toast.makeText(getApplicationContext(), "Date Selected",
			// Toast.LENGTH_SHORT).show();
		}
	};

	private void showDate(int year, int month, int day) {
		dob.setText(new StringBuilder().append(day).append("/").append(month).append("/").append(year));
	}

	// DATE PICKER ENDS

	public void reg(View v) {
		progress.setMessage("Uploading to the server..");
		progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progress.setCancelable(false);
		progress.setCanceledOnTouchOutside(false);
		progress.show();
		String Sfullname = fullname.getText().toString().trim();
		String Sphone = phone.getText().toString().trim();
		String Sdob = dob.getText().toString().trim();
		String Semail = email.getText().toString().trim();
		String Suser = user.getText().toString().trim();
		String Spass = pass.getText().toString().trim();

		String Shome = home.getText().toString().trim();
		String Splace = place.getText().toString().trim();
		String Scity = city.getText().toString().trim();
		String Sstate = state.getText().toString().trim();
		String Scountry = country.getText().toString().trim();

		String Slat = txtLAT.getText().toString().trim();
		String Slng = txtLNG.getText().toString().trim();

		if (!Sfullname.equals("") && !Sphone.equals("") && !Semail.equals("") && !Suser.equals("") && !Spass.equals("")
				&& !Shome.equals("") && !Splace.equals("") && !Scity.equals("") && !Sstate.equals("")
				&& !Scountry.equals("") && !Slat.equals("Click Above..")
				&& !Slng.equals("Click Above..") && !Sdob.equals("") && !Sgender.equals("")) {
			try {
				Sage = agecalc();
				HttpClient client = new DefaultHttpClient();
				HttpPost request = new HttpPost("http://" + changedIP + ":8080/AndroLawyer/ClientRegistration.jsp");
				List<NameValuePair> PostParameters = new ArrayList<NameValuePair>();
				PostParameters.add(new BasicNameValuePair("Cfullname", Sfullname));
				PostParameters.add(new BasicNameValuePair("Cage", Sage));
				PostParameters.add(new BasicNameValuePair("Cgender", Sgender));
				PostParameters.add(new BasicNameValuePair("Cphone", Sphone));
				PostParameters.add(new BasicNameValuePair("Cemail", Semail));
				PostParameters.add(new BasicNameValuePair("Cuser", Suser));
				PostParameters.add(new BasicNameValuePair("Cpass", Spass));

				PostParameters.add(new BasicNameValuePair("Chome", Shome));
				PostParameters.add(new BasicNameValuePair("Cplace", Splace));
				PostParameters.add(new BasicNameValuePair("Ccity", Scity));
				PostParameters.add(new BasicNameValuePair("Cstate", Sstate));
				PostParameters.add(new BasicNameValuePair("Ccountry", Scountry));
				PostParameters.add(new BasicNameValuePair("Clat", Slat));
				PostParameters.add(new BasicNameValuePair("Clng", Slng));

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
							// Hide your View after 1.5 seconds
							progress.dismiss();
							alert();
						}
					}, 1500);

				} else {
					progress.dismiss();
					alert.showAlertDialog(this, null, "Username already exist", false);
				}
			} catch (Exception e) {
				e.printStackTrace();
				progress.dismiss();
				alert.showAlertDialog(this, "Registration Failed!!", "Please Turn Ur Internet Connection ON", false);
			}
		} else {
			progress.dismiss();
			Toast.makeText(this, "Complete all the fields...",Toast.LENGTH_SHORT).show();
			//alert.showAlertDialog(this, null, "Complete all the fields....", false);
		}

	}

	public void alert() {
		AlertDialog alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setTitle("Registration");
		alertDialog.setMessage("Client registered");
		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				finish();
				Intent i = new Intent(getApplicationContext(), ClientLogin.class);
				startActivity(i);
				Toast.makeText(getApplicationContext(), "Login using the username and password", Toast.LENGTH_LONG)
						.show();
			}
		});
		alertDialog.show();
	}
}
