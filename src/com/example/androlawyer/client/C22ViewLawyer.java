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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class C22ViewLawyer extends Activity {

	String lawyerid,L_details[];
	ListView lv;
	TextView LName, txtLawyerID;
	EditText LEMail, LPhn, LHaddr, LPlace, LLity, LState, Lcountry,LCity;
	String SLName, SLEMail, SLPhn, SLHaddr, SLPlace, SLCity, SLState, SLCountry;
	ProgressDialog progress;
	String progresscondition = "";
	String changedIP = "";
	String cid;
	ScrollView s;
	AlertDialogManager alert = new AlertDialogManager();
	SharedPreferences mypref;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_c22_view_lawyer);
		ActionBar bar = getActionBar();
		ColorDrawable c = new ColorDrawable(Color.parseColor("#32b4ff"));
		bar.setBackgroundDrawable(c);
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			lawyerid = (String) bundle.get("Slawyerid");
		}
		//bar.setTitle(Html.fromHtml("<font color='#ffffff'>View All Lawyers</font>"));
		bar.setTitle("View All Lawyers");
		bar.setIcon(android.R.color.transparent);
		bar.setDisplayHomeAsUpEnabled(true);

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		SharedPreferences ip;
		ip = getSharedPreferences("IPaddr", Context.MODE_PRIVATE);
		changedIP = ip.getString("IP", null);

		mypref = getSharedPreferences("preferenceclogin", Context.MODE_PRIVATE);
		cid = mypref.getString("clientid", null);
		
		progress = new ProgressDialog(this);
		progress.setMessage("Loading Lawyer Details..");
		progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progress.setCancelable(false);
		progress.setCanceledOnTouchOutside(false);
		progress.show();
		s=(ScrollView)findViewById(R.id.scrollView2ViewLawyer_c22);
		s.setVisibility(View.INVISIBLE);
		
		getLawyerDetails();

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

		txtLawyerID = (TextView) findViewById(R.id.txtLawyerView_c22);
		LName = (TextView) findViewById(R.id.LawyerName_ViewLawyer_c22);
		LEMail = (EditText) findViewById(R.id.LawyerEmail_ViewLawyer_c22);
		LPhn = (EditText) findViewById(R.id.LawyerPhn_ViewLawyer_c22);
		LHaddr = (EditText) findViewById(R.id.LawyerHaddr_ViewLawyer_c22);
		LPlace = (EditText) findViewById(R.id.LawyerPlace_ViewLawyer_c22);
		LCity = (EditText) findViewById(R.id.LawyerCity_ViewLawyer_c22);
		LState = (EditText) findViewById(R.id.LawyerState_ViewLawyer_c22);
		Lcountry = (EditText) findViewById(R.id.Lcountry_c22);

		txtLawyerID.setText("LawyerID: " + lawyerid);
		
		LName.setText(L_details[1]);
		LEMail.setText(L_details[2]);
		LPhn.setText(L_details[3]);
		LHaddr.setText(L_details[4]);
		LPlace.setText(L_details[5]);
		LCity.setText(L_details[6]);
		LState.setText(L_details[7]);
		Lcountry.setText(L_details[8]);
		
		Handler Shandler = new Handler();
		Shandler.postDelayed(new Runnable() {

			@Override
			public void run() {
				s.fullScroll(View.FOCUS_DOWN);
			}
		}, 2000);
	}

	public void getLawyerDetails() {

		int i = 0;

		try {
			HttpClient client = new DefaultHttpClient();
			HttpPost request = new HttpPost("http://" + changedIP + ":8080/AndroLawyer/actLawyerDetailsGet.jsp");
			List<NameValuePair> PostParameters = new ArrayList<NameValuePair>();
			PostParameters.add(new BasicNameValuePair("fLid", lawyerid));
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
			L_details = result.split("\\*");
			System.out.println("VVVVVVVVVVVVVVVVVVVVVVVVVVV"+L_details[8]);
		} catch (Exception e) {
			e.printStackTrace();
			progresscondition = "A";
			alert.showAlertDialog(this,null, "Please Turn Ur Internet Connection ON", false);
		}
	}

	public void refreshclk(View v) {
		Animation a = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale_up);
		v.startAnimation(a);
		Vibrator vibr = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		vibr.vibrate(100);
		LEMail.setText("");
		LPhn.setText("");
		LHaddr.setText("");
		LPlace.setText("");
		LCity.setText("");
		LState.setText("");
		Lcountry.setText("");
		Toast.makeText(this,"Now fill in the new details", Toast.LENGTH_SHORT).show();
	}

	public void deleteclk(View v) {
		Animation a = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale_up);
		v.startAnimation(a);
		Vibrator vibr = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		vibr.vibrate(100);
		removeclientfunc();
	}

	public void saveclk(View v) {
		Animation a = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale_up);
		v.startAnimation(a);
		Vibrator vibr = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		vibr.vibrate(100);
		progress = new ProgressDialog(this);
		progress.setMessage("Saving Client Details..");
		progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progress.setCancelable(false);
		progress.setCanceledOnTouchOutside(false);
		progress.show();

		SLEMail = LEMail.getText().toString();
		SLPhn = LPhn.getText().toString();
		SLHaddr = LHaddr.getText().toString();
		SLPlace = LPlace.getText().toString();
		SLCity = LCity.getText().toString();
		SLState = LState.getText().toString();
		SLCountry = Lcountry.getText().toString();

		if (!SLEMail.equals("") && !SLPhn.equals("") && !SLHaddr.equals("") && !SLPlace.equals("") && !SLCity.equals("")
				&& !SLState.equals("") && !SLCountry.equals("")) {
			try {
				HttpClient client = new DefaultHttpClient();
				HttpPost request = new HttpPost("http://" + changedIP + ":8080/AndroLawyer/actLawyerDetailsUpdate.jsp");
				List<NameValuePair> PostParameters = new ArrayList<NameValuePair>();

				PostParameters.add(new BasicNameValuePair("flawyerid", lawyerid));
				PostParameters.add(new BasicNameValuePair("fEMail", SLEMail));
				PostParameters.add(new BasicNameValuePair("fPhn", SLPhn));
				PostParameters.add(new BasicNameValuePair("fHaddr", SLHaddr));
				PostParameters.add(new BasicNameValuePair("fPlace", SLPlace));
				PostParameters.add(new BasicNameValuePair("fCity", SLCity));
				PostParameters.add(new BasicNameValuePair("fState", SLState));
				PostParameters.add(new BasicNameValuePair("fCountry", SLCountry));

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
							alertedit();
						}
					}, 1500);
				} else {
					progress.dismiss();
					alert.showAlertDialog(this,null, "Unknown Error", false);
				}
			} catch (Exception e) {
				e.printStackTrace();
				progress.dismiss();
				alert.showAlertDialog(this,null, "Please Turn Ur Internet Connection ON", false);
			}
		} else {
			progress.dismiss();
			alert.showAlertDialog(this,null, "Complete all the fields....", false);
		}

	}

	public void alertedit()
	{
		alert.showAlertDialog(this,null, "Edition Success", false);
	}
	
	public void removeclientfunc() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		//builder.setIcon(R.drawable.logalert);
		builder.setTitle("Remove Current Lawyer");
		builder.setMessage("Are you sure ?").setPositiveButton("Yes", dialogClickListener);
		builder.setNegativeButton("No", dialogClickListener).show();
	}

	DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int which) {
			switch (which) {
			case DialogInterface.BUTTON_POSITIVE:
				removeclient();
				break;

			case DialogInterface.BUTTON_NEGATIVE:
				dialog.dismiss();
				break;
			}
		}
	};
	
	public void removeclient()
	{
		
		progress = new ProgressDialog(this);
		progress.setMessage("Deleting Lawyer Details..");
		progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progress.setCancelable(false);
		progress.setCanceledOnTouchOutside(false);
		progress.show();
		try {
			HttpClient client = new DefaultHttpClient();
			HttpPost request = new HttpPost("http://" + changedIP + ":8080/AndroLawyer/actLawyerDetailsRemove.jsp");
			List<NameValuePair> PostParameters = new ArrayList<NameValuePair>();

			PostParameters.add(new BasicNameValuePair("flawyerid", lawyerid));
			PostParameters.add(new BasicNameValuePair("fclientid", cid));
			
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
			System.out.println("RRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR"+result);
			if (result.equals("Success")) {
				Handler handler = new Handler();
				handler.postDelayed(new Runnable() {

					@Override
					public void run() {
						// Hide your View after 3 seconds
						progress.dismiss();
						alertdelete();
					}
				}, 1500);
			} else {
				progress.dismiss();
			}
		} catch (Exception e) {
			e.printStackTrace();
			progress.dismiss();
		}
	}
	
	public void alertdelete() {
		AlertDialog alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setTitle(null);
		alertDialog.setMessage("Deletion Success");
		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				C22ViewLawyer.this.finish();
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