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
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class C13ViewAll extends Activity {
	public String Case_ids[];
	public String C_details[];
	String caseid;
	private Calendar calendar;
	ListView lv;
	TextView CaseID;
	EditText CTitle, CDesc, CDate, CStatus;
	public int mYear, mMonth, mDay;
	private int year, month, day;
	String EdtCTitle, EdtCDesc, EdtCDate, EdtCStatus;
	ProgressDialog progress;
	String progresscondition = "";
	String changedIP = "";
	ScrollView s;
	AlertDialogManager alert = new AlertDialogManager();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_c13_view_all);

		ActionBar bar = getActionBar();
		ColorDrawable c = new ColorDrawable(Color.parseColor("#32b4ff"));
		bar.setBackgroundDrawable(c);
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			caseid = (String) bundle.get("caseid");
		}
		//bar.setTitle(Html.fromHtml("<font color='#ffffff'>View All Cases</font>"));
		bar.setTitle("View All Cases");
		bar.setIcon(android.R.color.transparent);
		bar.setDisplayHomeAsUpEnabled(true);

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		SharedPreferences ip;
		ip = getSharedPreferences("IPaddr", Context.MODE_PRIVATE);
		changedIP = ip.getString("IP", null);

		progress = new ProgressDialog(this);
		progress.setMessage("Loading Case Details..");
		progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progress.setCancelable(false);
		progress.setCanceledOnTouchOutside(false);
		progress.show();
		s = (ScrollView) findViewById(R.id.scrollView1Case_Client);
		s.setVisibility(View.INVISIBLE);

		getCaseDetails();

		if ((progresscondition.equals("A"))) {
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

		CaseID = (TextView) findViewById(R.id.txtc13_client);
	
		CTitle = (EditText) findViewById(R.id.CTitle_c13_client);
		CDate = (EditText) findViewById(R.id.CDate_c13_client);
		CDesc = (EditText) findViewById(R.id.CDesc_c13_client);
		CStatus = (EditText) findViewById(R.id.CStatus_c13_client);
		CaseID.setText("CaseID: " + caseid);
		
		CTitle.setText(C_details[1]);
		CDesc.setText(C_details[2]);
		CDate.setText(C_details[3]);
		CStatus.setText(C_details[4]);
		calendar = Calendar.getInstance();
		year = calendar.get(Calendar.YEAR);
		month = calendar.get(Calendar.MONTH);
		day = calendar.get(Calendar.DAY_OF_MONTH);
		
		Handler Shandler = new Handler();
		Shandler.postDelayed(new Runnable() {

			@Override
			public void run() {
				s.fullScroll(View.FOCUS_DOWN);
			}
		}, 2000);
	}

	public void getCaseDetails() {

		int i = 0;

		try {
			HttpClient client = new DefaultHttpClient();
			HttpPost request = new HttpPost("http://" + changedIP + ":8080/AndroLawyer/actCaseDetailsGet.jsp");
			List<NameValuePair> PostParameters = new ArrayList<NameValuePair>();
			PostParameters.add(new BasicNameValuePair("cid", caseid));
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
			progresscondition = "A";
			C_details = result.split("\\*");
		} catch (Exception e) {
			e.printStackTrace();
			progresscondition = "A";
			alert.showAlertDialog(this, null, "Please Turn Ur Internet Connection ON", false);
		}
	}

	public void refreshclk(View v) {
		Animation a = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale_up);
		v.startAnimation(a);
		Vibrator vibr = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		vibr.vibrate(100);
		CTitle.setText("");
		CDesc.setText("");
		CDate.setText("");
		CStatus.setText("");
		Toast.makeText(this,"Now fill in the new details", Toast.LENGTH_SHORT).show();
	}

	public void closeclk(View v) {
		Animation a = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale_up);
		v.startAnimation(a);
		Vibrator vibr = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		vibr.vibrate(100);
		CStatus.setText("CLOSED");
		Toast.makeText(this, "Click on 'SAVE'", Toast.LENGTH_SHORT).show();
	}

	public void saveclk(View v) {
		Animation a = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale_up);
		v.startAnimation(a);
		Vibrator vibr = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		vibr.vibrate(100);
		progress = new ProgressDialog(this);
		progress.setMessage("Saving Case Details..");
		progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progress.setCancelable(false);
		progress.setCanceledOnTouchOutside(false);
		progress.show();

		EdtCTitle = CTitle.getText().toString();
		EdtCDesc = CDesc.getText().toString();
		EdtCDate = CDate.getText().toString();
		EdtCStatus = CStatus.getText().toString();

		if (!EdtCTitle.equals("") && !EdtCDesc.equals("") && !EdtCDate.equals("") && !EdtCStatus.equals("")) {
			try {
				HttpClient client = new DefaultHttpClient();
				HttpPost request = new HttpPost("http://" + changedIP + ":8080/AndroLawyer/actCaseDetailsViewAll.jsp");
				List<NameValuePair> PostParameters = new ArrayList<NameValuePair>();
				PostParameters.add(new BasicNameValuePair("Cid", caseid));
				PostParameters.add(new BasicNameValuePair("CTitle", EdtCTitle));
				PostParameters.add(new BasicNameValuePair("CDesc", EdtCDesc));
				PostParameters.add(new BasicNameValuePair("CDate", EdtCDate));
				PostParameters.add(new BasicNameValuePair("CStatus", EdtCStatus));

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
					}, 1000);
				} else {
					progress.dismiss();
					alert.showAlertDialog(this, null, "Unknown Error", false);
				}
			} catch (Exception e) {
				e.printStackTrace();
				progress.dismiss();
				alert.showAlertDialog(this, null, "Please Turn Ur Internet Connection ON", false);
			}
		} else {
			progress.dismiss();
			Toast.makeText(this, "Complete all the fields....", Toast.LENGTH_SHORT).show();
			//alert.showAlertDialog(this, null, "Complete all the fields....", false);
		}

	}

	public void alert() {
		AlertDialog alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setTitle(null);
		alertDialog.setMessage("Case Edition Successfull");
		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				C13ViewAll.this.finish();
			}
		});
		alertDialog.show();
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
			CDate.setText(new StringBuilder().append(day).append("/").append(month).append("/").append(year));
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
}