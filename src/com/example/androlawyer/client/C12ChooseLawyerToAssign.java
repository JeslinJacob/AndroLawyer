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
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class C12ChooseLawyerToAssign extends Activity {
	public String Case_ids[];
	public String C_details[];
	public String L_ids[];
	public String L_names[];
	String caseid,curr_lawyer_id;
	TextView CName, CaseID;
	TextView CTitle, CDesc, CDate, CStatus;

	
	ProgressDialog progress;
	String progresscondition = "";
	String changedIP = "";
	ScrollView s;
	AlertDialogManager alert = new AlertDialogManager();
	SharedPreferences mypref;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_c12_choose_lawyer_to_assign);

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
		s = (ScrollView) findViewById(R.id.ALscroll);
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
		
		
		getLid();
		getLNames();
		
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
				L_names);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		Spinner lawyerspinn = (Spinner) findViewById(R.id.ALSpinner);
		lawyerspinn.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
				curr_lawyer_id = L_ids[position];
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
		lawyerspinn.setAdapter(dataAdapter);

		CaseID = (TextView) findViewById(R.id.ALCaseId);
		
		CTitle = (TextView) findViewById(R.id.ALCaseTitle);
		CDate = (TextView) findViewById(R.id.ALdatetxt2);
		CDesc = (TextView) findViewById(R.id.ALDesc2);
		CStatus = (TextView) findViewById(R.id.ALStatus);
		
		
		
		CaseID.setText("CaseID: " + caseid);
		CTitle.setText(C_details[1]);
		CDesc.setText(C_details[2]);
		CDate.setText(C_details[3]);
		CStatus.setText(C_details[4]);
		
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

	public void getLid() {

		int i = 0;

		try {

			mypref = getSharedPreferences("preferenceclogin", Context.MODE_PRIVATE);
			String Clientid = mypref.getString("clientid", null);

			HttpClient client = new DefaultHttpClient();
			HttpPost request = new HttpPost("http://"+changedIP+":8080/AndroLawyer/GetLawyerIDfromMylawyerwithClientID.jsp");

			List<NameValuePair> PostParameters = new ArrayList<NameValuePair>();

			PostParameters.add(new BasicNameValuePair("fClientID", Clientid));

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
			progresscondition="A";
			L_ids = result.split("\\*");

		} catch (Exception e) {
			e.printStackTrace();
			progresscondition="A";
			Toast.makeText(this, "Please Turn Ur Internet Connection ON", Toast.LENGTH_SHORT).show();
		}

	}

	
	public void getLNames() {

		int i = 0;

		try {

			mypref = getSharedPreferences("preferenceclogin", Context.MODE_PRIVATE);
			String Clientid = mypref.getString("clientid", null);

			HttpClient client = new DefaultHttpClient();
			HttpPost request = new HttpPost("http://"+changedIP+":8080/AndroLawyer/GetLawyerNamefromMylawyerwithClientID.jsp");

			List<NameValuePair> PostParameters = new ArrayList<NameValuePair>();

			PostParameters.add(new BasicNameValuePair("fClientID", Clientid));

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
			progresscondition="A";
			L_names = result.split("\\*");

		} catch (Exception e) {
			e.printStackTrace();
			progresscondition="A";
			Toast.makeText(this, "Please Turn Ur Internet Connection ON", Toast.LENGTH_SHORT).show();
		}

	}


	public void saveclk(View v) {
		Animation a = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale_up);
		v.startAnimation(a);
		Vibrator vibr = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		vibr.vibrate(100);
		progress = new ProgressDialog(this);
		progress.setMessage("Assigning Case..");
		progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progress.setCancelable(false);
		progress.setCanceledOnTouchOutside(false);
		progress.show();

		
		int i=0;

		if (!curr_lawyer_id.equals("")) {
			try {
				HttpClient client = new DefaultHttpClient();
				HttpPost request = new HttpPost("http://" + changedIP + ":8080/AndroLawyer/AssignCaseToLawyer.jsp");
				List<NameValuePair> PostParameters = new ArrayList<NameValuePair>();
			
				PostParameters.add(new BasicNameValuePair("fCid", caseid));
				PostParameters.add(new BasicNameValuePair("fCur_lid", curr_lawyer_id));
				
				

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
			Toast.makeText(this, "Complete all the fields...", Toast.LENGTH_SHORT).show();
			//alert.showAlertDialog(this, null, "Complete all the fields....", false);
		}

	}

	public void alert() {
		AlertDialog alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setTitle(null);
		alertDialog.setMessage("Case Assigned Successfully");
		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				C12ChooseLawyerToAssign.this.finish();
			}
		});
		alertDialog.show();
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
}