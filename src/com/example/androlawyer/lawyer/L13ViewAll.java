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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class L13ViewAll extends Activity {
	public String Case_ids[];
	public String C_details[];
	String caseid;
	ListView lv;
	TextView CName, CaseID;
	EditText CTitle, CDesc, CDate, CStatus;
	public int mYear, mMonth, mDay;
	String EdtCTitle, EdtCDesc, EdtCDate, EdtCStatus;
	ProgressDialog progress;
	String progresscondition = "";
	String changedIP = "";
	ScrollView s;
	AlertDialogManager alert = new AlertDialogManager();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_l13_view_all);

		ActionBar bar = getActionBar();
		ColorDrawable c = new ColorDrawable(Color.parseColor("#32b4ff"));
		bar.setBackgroundDrawable(c);
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			caseid = (String) bundle.get("caseid");
		}
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
		s = (ScrollView) findViewById(R.id.scrollView1Case_lawyer);
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

		CaseID = (TextView) findViewById(R.id.txt113_lawyer);
		CName = (TextView) findViewById(R.id.CName_113lawyer);
		CTitle = (EditText) findViewById(R.id.CTitle_113lawyer);
		CDate = (EditText) findViewById(R.id.CDate_113lawyer);
		CDesc = (EditText) findViewById(R.id.CDesc_113lawyer);
		CStatus = (EditText) findViewById(R.id.CStatus_113lawyer);
		CaseID.setText("CaseID: " + caseid);
		CName.setText(C_details[6]);
		CTitle.setText(C_details[1]);
		CDesc.setText(C_details[2]);
		CDate.setText(C_details[3]);
		CStatus.setText(C_details[4]);
	}

	public void getCaseDetails() {
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
				L13ViewAll.this.finish();
			}
		});
		alertDialog.show();
	}

	public void mydate(View v) {
		showDialog(0);

		final Calendar c = Calendar.getInstance();
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);

		updateDisplay();
	}

	private void updateDisplay() {
		CDate.setText(new StringBuilder()
				// Month is 0 based so add 1
				.append(mDay).append("-").append(mMonth + 1).append("-").append(mYear).append(" "));
	}

	@Override
	protected Dialog onCreateDialog(int id) {

		return new DatePickerDialog(this, mDateSetListener, mYear, mMonth, mDay);
	}

	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

			mYear = year;
			mMonth = monthOfYear;
			mDay = dayOfMonth;

			updateDisplay();
		}
	};

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