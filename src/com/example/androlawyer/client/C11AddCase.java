package com.example.androlawyer.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.StringTokenizer;
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
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.os.Vibrator;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

public class C11AddCase extends Activity {

	public int mYear, mMonth, mDay;
	public String curr_client_id;
	SharedPreferences mypref;
	public String Status[]={"OPEN","CLOSED"};;
	private Calendar calendar;
	private int year, month, day;
	public String curr_status;
	EditText cid, ctitle, cdesc, cdate, cstatus;
	ProgressDialog progress;
	String progresscondition = "";
	String changedIP = "";
	ScrollView s;
	AlertDialogManager alert = new AlertDialogManager();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_c11_add_case);

		progress = new ProgressDialog(this);
		progress.setMessage("Loading Details..");
		progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progress.setCancelable(false);
		progress.setCanceledOnTouchOutside(false);
		progress.show();
		progress.dismiss();

		s=(ScrollView)findViewById(R.id.scrollView1AddCase);
		
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		SharedPreferences ip;
		ip = getSharedPreferences("IPaddr", Context.MODE_PRIVATE);
		changedIP = ip.getString("IP", null);

		if ((progresscondition.equals("AB")) || (progresscondition.equals("BA"))) {
			Handler handler = new Handler();
			handler.postDelayed(new Runnable() {

				@Override
				public void run() {
					// Hide your View after 3 seconds
					progress.dismiss();
					s.setVisibility(View.VISIBLE);
				}
			}, 1000);
		}

		ActionBar bar = getActionBar();
		bar.hide();

		mypref = getSharedPreferences("preferenceclogin", Context.MODE_PRIVATE);
		String Clientid = mypref.getString("clientid", null);
		curr_client_id = Clientid;

		cid = (EditText) findViewById(R.id.c11Caseid);
		ctitle = (EditText) findViewById(R.id.c11CTitle);
		cdesc = (EditText) findViewById(R.id.c11CDesc);
		cdate = (EditText) findViewById(R.id.c11CDate);

		calendar = Calendar.getInstance();
		year = calendar.get(Calendar.YEAR);
		month = calendar.get(Calendar.MONTH);
		day = calendar.get(Calendar.DAY_OF_MONTH);
		ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
				Status);
		dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		Spinner StatSpin = (Spinner) findViewById(R.id.c11CStatusSpinner);
		StatSpin.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
				curr_status = Status[position];
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
		StatSpin.setAdapter(dataAdapter1);
		
		Handler Shandler = new Handler();
		Shandler.postDelayed(new Runnable() {

			@Override
			public void run() {
				s.fullScroll(View.FOCUS_DOWN);
			}
		}, 2000);
		
	}

	public void clearfields() {
		cid.setText("");
		ctitle.setText("");
		cdesc.setText("");
		cdate.setText("");
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
			Calendar minDate = Calendar.getInstance();
			minDate.set(calendar.get(Calendar.YEAR) - 0, calendar.get(Calendar.MONTH),
					calendar.get(Calendar.DAY_OF_MONTH) - 1);
			// dt.DatePicker.MaxDate = maxDate.TimeInMillis;
			dtd.getDatePicker().setMinDate(minDate.getTimeInMillis());
			Calendar maxDate = Calendar.getInstance();
			maxDate.set(calendar.get(Calendar.YEAR) + 3, calendar.get(Calendar.MONTH),
					calendar.get(Calendar.DAY_OF_MONTH));
			// dt.DatePicker.MaxDate = maxDate.TimeInMillis;
			dtd.getDatePicker().setMaxDate(maxDate.getTimeInMillis());
			dtd.setTitle("Date Picker");
			dtd.setMessage("Enter/Select Case Date");
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
		cdate.setText(new StringBuilder().append(day).append("/").append(month).append("/").append(year));
	}

	// DATE PICKER ENDS

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

	public void saveclk(View v) {
		progress = new ProgressDialog(this);
		progress.setMessage("Adding Case..");
		progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progress.setCancelable(false);
		progress.setCanceledOnTouchOutside(false);
		progress.show();
		Animation a = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale_up);
		v.startAnimation(a);

		Vibrator vibr = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		vibr.vibrate(100);

		String Scaseid = cid.getText().toString().trim();
		String Stitle = ctitle.getText().toString().trim();
		String Sdesc = cdesc.getText().toString().trim();
		String Sdate = cdate.getText().toString().trim();
		String Sstatus = curr_status;

		if (!Scaseid.equals("") && !Stitle.equals("") && !Sdesc.equals("") && !Sdate.equals("") && !Sstatus.equals("")) {

			try {
				HttpClient client = new DefaultHttpClient();
				HttpPost request = new HttpPost(
						"http://" + changedIP + ":8080/AndroLawyer/actClientCaseDetailsAdd.jsp");
				List<NameValuePair> PostParameters = new ArrayList<NameValuePair>();

				PostParameters.add(new BasicNameValuePair("fclientid", curr_client_id));
				PostParameters.add(new BasicNameValuePair("fcaseid", Scaseid));
				PostParameters.add(new BasicNameValuePair("ftitle", Stitle));
				PostParameters.add(new BasicNameValuePair("fdesc", Sdesc));
				PostParameters.add(new BasicNameValuePair("fdate", Sdate));
				PostParameters.add(new BasicNameValuePair("fstatus", Sstatus));
				PostParameters.add(new BasicNameValuePair("flid", null));

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
							Toast.makeText(getApplicationContext(), "Added Case Successfully", Toast.LENGTH_LONG)
									.show();
							clearfields();
						}
					}, 1000);
				} else if (result.equals("data exist")) {
					progress.dismiss();
					Toast.makeText(this, "This case already exists", Toast.LENGTH_LONG).show();
				} else {
					progress.dismiss();
					Toast.makeText(this, "Invalid Data Entered", Toast.LENGTH_LONG).show();
				}
			} catch (Exception e) {
				e.printStackTrace();
				progress.dismiss();
				Toast.makeText(this, "Please Turn Ur Internet Connection ON", Toast.LENGTH_SHORT).show();
			}
		} else {
			progress.dismiss();
			Toast.makeText(this, "Complete all the fields...", Toast.LENGTH_SHORT).show();
		}
	}

	public void refreshclk(View v) {
		Animation a = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale_up);
		v.startAnimation(a);
		Vibrator vibr = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		vibr.vibrate(100);
		clearfields();
		Toast.makeText(this, "Fields cleared", Toast.LENGTH_SHORT).show();
	}
}
