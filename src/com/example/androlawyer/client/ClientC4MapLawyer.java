package com.example.androlawyer.client;

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

import com.example.androlawyer.AlertDialogManager;
import com.example.androlawyer.R;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.os.Vibrator;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class ClientC4MapLawyer extends Activity {
	public String Case_ids[];
	public String L_details[];
	String lawyerid;
	ListView lv;
	TextView LName, LawyerID;
	EditText LEMail, LPhn, LHaddr, LPlace, LCity, LState, LCountry, LQual, LCT;
	ProgressDialog progress;
	String progresscondition = "";
	String changedIP = "";
	String callnum, callname;
	ScrollView s;
	AlertDialogManager alert = new AlertDialogManager();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_client_c4_map_lawyer);
		ActionBar bar = getActionBar();
		ColorDrawable c = new ColorDrawable(Color.parseColor("#32b4ff"));
		bar.setBackgroundDrawable(c);
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			lawyerid = (String) bundle.get("lawyerid");
		}
		//bar.setTitle(Html.fromHtml("<font color='#ffffff'>Go back to map</font>"));
		bar.setTitle("Go back to map");
		bar.setIcon(android.R.color.transparent);
		bar.setDisplayHomeAsUpEnabled(true);

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		SharedPreferences ip;
		ip = getSharedPreferences("IPaddr", Context.MODE_PRIVATE);
		changedIP = ip.getString("IP", null);
		SharedPreferences mypref;

		progress = new ProgressDialog(this);
		progress.setMessage("Loading Lawyer Details..");
		progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progress.setCancelable(false);
		progress.setCanceledOnTouchOutside(false);
		progress.show();
		s = (ScrollView) findViewById(R.id.scrollView1C4map);
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

		LawyerID = (TextView) findViewById(R.id.txtLawyerView_C4map);
		LName = (TextView) findViewById(R.id.LawyerName_C4map);
		LEMail = (EditText) findViewById(R.id.LawyerEmail_C4map);
		LPhn = (EditText) findViewById(R.id.LawyerPhn_C4map);
		LHaddr = (EditText) findViewById(R.id.LawyerHaddr_C4map);
		LPlace = (EditText) findViewById(R.id.LawyerPlace_C4map);
		LCity = (EditText) findViewById(R.id.LawyerCity_C4map);
		LState = (EditText) findViewById(R.id.LawyerState_C4map);
		LCountry = (EditText) findViewById(R.id.LawyerCountry_C4map);
		LQual = (EditText) findViewById(R.id.LawyerQualification_C4map);
		LCT = (EditText) findViewById(R.id.LawyerCT_C4map);
		
		LawyerID.setText("LawyerID: " + lawyerid);
		LName.setText(L_details[1]);
		LEMail.setText(L_details[2]);
		LPhn.setText(L_details[3]);
		LHaddr.setText(L_details[4]);
		LPlace.setText(L_details[5]);
		LCity.setText(L_details[6]);
		LState.setText(L_details[7]);
		LCountry.setText(L_details[8]);
		LQual.setText(L_details[9]);
		
		LCT.setText(L_details[10]);
		
		Handler Shandler = new Handler();
		Shandler.postDelayed(new Runnable() {

			@Override
			public void run() {
				s.fullScroll(View.FOCUS_DOWN);
			}
		}, 2000);
		
	}
	
	public void getLawyerDetails() {
		try {
			HttpClient client = new DefaultHttpClient();
			HttpPost request = new HttpPost("http://" + changedIP + ":8080/AndroLawyer/actMapLawyerDetailsGet.jsp");
			List<NameValuePair> PostParameters = new ArrayList<NameValuePair>();
			PostParameters.add(new BasicNameValuePair("lid", lawyerid));
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
		} catch (Exception e) {
			e.printStackTrace();
			progresscondition = "A";
			alert.showAlertDialog(this, null, "Please Turn Ur Internet Connection ON", false);
		}
	}

	public void textclk(View v) {
		Animation a = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale_up);
		v.startAnimation(a);
		Vibrator vibr = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		vibr.vibrate(100);
		Intent i = new Intent(getApplicationContext(), ClientC3Chat.class);
		i.putExtra("toid", lawyerid);
		startActivity(i);
	}

	public void callclk(View v) {
		Animation a = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale_up);
		v.startAnimation(a);
		Vibrator vibr = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		vibr.vibrate(100);
		String CallNum = LPhn.getText().toString().trim();
		String CallName = LName.getText().toString().trim();
		callclient(CallNum, CallName);
	}

	public void sendclk(View v) {
		Animation a = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale_up);
		v.startAnimation(a);
		Vibrator vibr = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		vibr.vibrate(100);

		SharedPreferences mypref = getSharedPreferences("preferenceclogin", Context.MODE_PRIVATE);
		String cid = mypref.getString("clientid", null);

		try {
			HttpClient client = new DefaultHttpClient();
			HttpPost request = new HttpPost("http://" + changedIP + ":8080/AndroLawyer/SendRequest.jsp");
			List<NameValuePair> PostParameters = new ArrayList<NameValuePair>();
			PostParameters.add(new BasicNameValuePair("toid", lawyerid));
			PostParameters.add(new BasicNameValuePair("fromid", cid));
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

			if (result.equals("success")) {
				Toast.makeText(getApplicationContext(), "Request sent Successfully", Toast.LENGTH_LONG).show();
			} else if (result.equals("exist")) {

				alert.showAlertDialog(this, null, "Request Already Sent or Already Received", false);
			} else {
				alert.showAlertDialog(this, null, "Error In Server", false);
			}

		} catch (Exception e) {
			e.printStackTrace();
			progresscondition = "A";
			alert.showAlertDialog(this, null, "Please Turn Ur Internet Connection ON", false);
		}
	}

	public void callclient(String num, String name) {
		callnum = num;
		callname = name;
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		// builder.setIcon(R.drawable.logalert);
		builder.setTitle(callname);
		builder.setMessage("Call Lawyer ?").setPositiveButton("Yes", dialogClickListener);
		builder.setNegativeButton("No", dialogClickListener).show();
	}

	DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int which) {
			switch (which) {
			case DialogInterface.BUTTON_POSITIVE:
				call();
				break;

			case DialogInterface.BUTTON_NEGATIVE:
				dialog.dismiss();
				break;
			}
		}
	};

	public void call() {
		Intent i = null;
		i = new Intent(Intent.ACTION_CALL, Uri.parse("tel:+91" + callnum));
		startActivity(i);
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