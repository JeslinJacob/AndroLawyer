package com.example.androlawyer.lawyer;

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

public class L21AddClient extends Activity {

	EditText fullname, phone, email, home, place, city, state, country, age;
	RadioButton Cgenderm, Cgenderf;
	String Sgender = "", Sage;
	SharedPreferences mypref;
	ProgressDialog progress;
	private Calendar calendar;
	private int year, month, day;
	String changedIP = "";
	AlertDialogManager alert = new AlertDialogManager();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_l21_add_client);
		ActionBar bar = getActionBar();
		bar.hide();

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		SharedPreferences ip;
		ip = getSharedPreferences("IPaddr", Context.MODE_PRIVATE);
		changedIP = ip.getString("IP", null);

		progress = new ProgressDialog(this);
		fullname = (EditText) findViewById(R.id.Cname_addclient_lawyer);

		phone = (EditText) findViewById(R.id.Cphone_addclient_lawyer);
		age = (EditText) findViewById(R.id.Cage_addclient_lawyer);
		email = (EditText) findViewById(R.id.Cemail_addclient_lawyer);

		Cgenderm = (RadioButton) findViewById(R.id.Cmale_addclient_lawyer);
		Cgenderf = (RadioButton) findViewById(R.id.Cfemale_addclient_lawyer);

		home = (EditText) findViewById(R.id.Chome_addclient_lawyer);
		place = (EditText) findViewById(R.id.Cplace_addclient_lawyer);
		city = (EditText) findViewById(R.id.Ccity_addclient_lawyer);
		state = (EditText) findViewById(R.id.Cstate_addclient_lawyer);
		country = (EditText) findViewById(R.id.Ccountry_addclient_lawyer);

		calendar = Calendar.getInstance();
		year = calendar.get(Calendar.YEAR);
		month = calendar.get(Calendar.MONTH);
		day = calendar.get(Calendar.DAY_OF_MONTH);
	}

	public void GenderCheck(View v) {
		boolean checked = ((RadioButton) v).isChecked();
		switch (v.getId()) {
		case R.id.Cmale_addclient_lawyer:
			if (checked)
				Cgenderf.setChecked(false);
			Sgender = "Male";
			break;
		case R.id.Cfemale_addclient_lawyer:
			if (checked)
				Cgenderm.setChecked(false);
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
		age.setText(new StringBuilder().append(day).append("/").append(month).append("/").append(year));
	}

	// DATE PICKER ENDS

	// candidate Registration

	public void reg(View v) {
		progress = new ProgressDialog(this);
		progress.setMessage("Adding Client..");
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
				&& !splace.equals("") && !scity.equals("") && !sstate.equals("") && !scountry.equals("")
				&& !Sgender.equals("")) {
			try {

				mypref = getSharedPreferences("preferencellogin", Context.MODE_PRIVATE);
				String lid = mypref.getString("lawyerid", null);

				HttpClient client = new DefaultHttpClient();
				HttpPost request = new HttpPost(
						"http://" + changedIP + ":8080/AndroLawyer/actClientDetailsRegistration.jsp");
				List<NameValuePair> PostParameters = new ArrayList<NameValuePair>();
				PostParameters.add(new BasicNameValuePair("Cfullname", Sfullname));
				PostParameters.add(new BasicNameValuePair("Cage", Sage));

				PostParameters.add(new BasicNameValuePair("Cgender", Sgender));
				PostParameters.add(new BasicNameValuePair("Cphone", Sphone));
				PostParameters.add(new BasicNameValuePair("Cemail", Semail));
				PostParameters.add(new BasicNameValuePair("Chome", shome));
				PostParameters.add(new BasicNameValuePair("Cplace", splace));
				PostParameters.add(new BasicNameValuePair("Ccity", scity));
				PostParameters.add(new BasicNameValuePair("Cstate", sstate));
				PostParameters.add(new BasicNameValuePair("Ccountry", scountry));
				PostParameters.add(new BasicNameValuePair("flid", lid));

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
			//alert.showAlertDialog(this, null, "Complete all the fields....", false);
			Toast.makeText(this, "Complete all the fields...",Toast.LENGTH_SHORT).show();
		}

	}

	public void alert() {
		AlertDialog alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setTitle("Registration");
		alertDialog.setMessage("Client Added");
		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				refresh();
				Toast.makeText(getApplicationContext(), "Now you can add another client", Toast.LENGTH_LONG).show();
			}
		});
		alertDialog.show();
	}

	public void refresh() {
		fullname.setText("");

		phone.setText("");
		age.setText("");
		email.setText("");

		Cgenderm.setChecked(false);
		Cgenderf.setChecked(false);

		home.setText("");
		place.setText("");
		city.setText("");
		state.setText("");
		country.setText("");
	}

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
}
