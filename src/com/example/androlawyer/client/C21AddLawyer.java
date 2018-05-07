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
import com.example.androlawyer.R.anim;
import com.example.androlawyer.R.id;
import com.example.androlawyer.R.layout;

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
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.os.Vibrator;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

public class C21AddLawyer extends Activity {

	EditText fullname, phone, email, home, place, city, state, country, age;
	RadioButton Lgenderm, Lgenderf;
	String Sgender, Sage;
	SharedPreferences mypref;
	ProgressDialog progress;
	private Calendar calendar;
	private int year, month, day;
	String changedIP = "";
	AlertDialogManager alert = new AlertDialogManager();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_c21_add_lawyer);
		ActionBar bar = getActionBar();
		bar.hide();

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		SharedPreferences ip;
		ip = getSharedPreferences("IPaddr", Context.MODE_PRIVATE);
		changedIP = ip.getString("IP", null);

		progress = new ProgressDialog(this);
		fullname = (EditText) findViewById(R.id.Cname_addlawyer);

		phone = (EditText) findViewById(R.id.Cphone_addlawyer);
		age = (EditText) findViewById(R.id.Cage_addlawyer);
		email = (EditText) findViewById(R.id.Cemail_addlawyer);

		Lgenderm = (RadioButton) findViewById(R.id.Cmale_addlawyer);
		Lgenderf = (RadioButton) findViewById(R.id.Cfemale_addlawyer);

		home = (EditText) findViewById(R.id.Chome_addlawyer);
		place = (EditText) findViewById(R.id.Cplace_addlawyer);
		city = (EditText) findViewById(R.id.Ccity_addlawyer);
		state = (EditText) findViewById(R.id.Cstate_addlawyer);
		country = (EditText) findViewById(R.id.Ccountry_addlawyer);
		calendar = Calendar.getInstance();
		year = calendar.get(Calendar.YEAR);
		month = calendar.get(Calendar.MONTH);
		day = calendar.get(Calendar.DAY_OF_MONTH);
	}

	public void GenderCheck(View v) {
		boolean checked = ((RadioButton) v).isChecked();
		switch (v.getId()) {
		case R.id.Cmale_addlawyer:
			if (checked)
				Lgenderf.setChecked(false);
			Sgender = "Male";
			break;
		case R.id.Cfemale_addlawyer:
			if (checked)
				Lgenderm.setChecked(false);
			Sgender = "Female";
			break;
		default:
			break;
		}
	}

	public String agecalc() {
		String Sdob = age.getText().toString().trim();
		if (!Sdob.equals("")) {
			int length = Sdob.length();
			String SdobYear = Sdob.substring(Sdob.length() - 4);
			int DobYear = Integer.parseInt(SdobYear);
			int CurrentYear = Calendar.getInstance().get(Calendar.YEAR);
			int intSage = CurrentYear - DobYear;
			String Sage = "" + intSage;
			return Sage;
		} else {
			return "";
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			this.finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
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
			minDate.set(calendar.get(Calendar.YEAR) - 60, calendar.get(Calendar.MONTH),
					calendar.get(Calendar.DAY_OF_MONTH));
			// dt.DatePicker.MaxDate = maxDate.TimeInMillis;
			dtd.getDatePicker().setMinDate(minDate.getTimeInMillis());
			Calendar maxDate = Calendar.getInstance();
			maxDate.set(calendar.get(Calendar.YEAR) - 27, calendar.get(Calendar.MONTH),
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
		age.setText(new StringBuilder().append(day).append("/").append(month).append("/").append(year));
	}

	// DATE PICKER ENDS

	// candidate Registration

	public void reg(View v) {
		progress = new ProgressDialog(this);
		progress.setMessage("Adding Lawyer..");
		progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progress.setCancelable(false);
		progress.setCanceledOnTouchOutside(false);
		progress.show();
		Animation a = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale_up);
		v.startAnimation(a);

		Vibrator vibr = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		vibr.vibrate(100);

		String Sfullname = fullname.getText().toString().trim();
		String Sphone = phone.getText().toString().trim();
		String Semail = email.getText().toString().trim();
		String Sage = agecalc();

		String shome = home.getText().toString().trim();
		String splace = place.getText().toString().trim();
		String scity = city.getText().toString().trim();
		String sstate = state.getText().toString().trim();
		String scountry = country.getText().toString().trim();

		if (!Sfullname.equals("") && !Sphone.equals("") && !Semail.equals("") && !Sage.equals("") && !shome.equals("")
				&& !splace.equals("") && !scity.equals("") && !scountry.equals("")) {
			try {

				mypref = getSharedPreferences("preferenceclogin", Context.MODE_PRIVATE);
				String Clientid = mypref.getString("clientid", null);

				HttpClient client = new DefaultHttpClient();
				HttpPost request = new HttpPost(
						"http://" + changedIP + ":8080/AndroLawyer/actLawyerDetailsRegistration.jsp");
				List<NameValuePair> PostParameters = new ArrayList<NameValuePair>();
				PostParameters.add(new BasicNameValuePair("Lfullname", Sfullname));
				PostParameters.add(new BasicNameValuePair("Lage", Sage));

				PostParameters.add(new BasicNameValuePair("Lgender", Sgender));
				PostParameters.add(new BasicNameValuePair("Lphone", Sphone));
				PostParameters.add(new BasicNameValuePair("Lemail", Semail));
				PostParameters.add(new BasicNameValuePair("Lhome", shome));
				PostParameters.add(new BasicNameValuePair("Lplace", splace));
				PostParameters.add(new BasicNameValuePair("Lcity", scity));
				PostParameters.add(new BasicNameValuePair("Lstate", sstate));
				PostParameters.add(new BasicNameValuePair("Lcountry", scountry));
				PostParameters.add(new BasicNameValuePair("fcid", Clientid));

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
							// Toast.makeText(getApplicationContext(),
							// "Registration Success",
							// Toast.LENGTH_LONG).show();
						}
					}, 1000);

				} else {
					progress.dismiss();
					alert.showAlertDialog(this, null, "Registration UnSuccessfull", false);
					// Toast.makeText(this, "Registration UnSuccessfull",
					// Toast.LENGTH_LONG).show();
				}
			} catch (Exception e) {
				e.printStackTrace();
				progress.dismiss();
				alert.showAlertDialog(this, null, "Please Turn Ur Internet Connection ON", false);
			}
		} else {
			progress.dismiss();
			alert.showAlertDialog(this, null, "Complete all the fields....", false);
			// Toast.makeText(this, "Complete all the fields...",
			// Toast.LENGTH_SHORT).show();
		}

	}

	public void alert() {
		AlertDialog alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setTitle("Registration");
		alertDialog.setMessage("Lawyer Added");
		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				refresh();
				Toast.makeText(getApplicationContext(), "Now you can add another Lawyer", Toast.LENGTH_LONG).show();
			}
		});
		alertDialog.show();
	}

	public void refresh() {
		fullname.setText("");

		phone.setText("");
		age.setText("");
		email.setText("");

		Lgenderm.setChecked(false);
		Lgenderf.setChecked(false);

		home.setText("");
		place.setText("");
		city.setText("");
		state.setText("");
		country.setText("");
	}

}
