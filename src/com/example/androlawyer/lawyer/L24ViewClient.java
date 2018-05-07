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
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class L24ViewClient extends Activity {
	public String Case_ids[];
	public String C_details[];
	String clientid;
	ListView lv;
	TextView CName, ClientID;
	EditText CEMail, CPhn, CHaddr, CPlace, CCity, CState, CCountry;
	String SCName, SCEMail, SCPhn, SCHaddr, SCPlace, SCCity, SCState, SCCountry;
	ProgressDialog progress;
	String progresscondition = "";
	String changedIP = "";
	ScrollView s;
	String lid;
	SharedPreferences mypref;
	AlertDialogManager alert = new AlertDialogManager();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_l24_view_client);
		ActionBar bar = getActionBar();
		ColorDrawable c = new ColorDrawable(Color.parseColor("#32b4ff"));
		bar.setBackgroundDrawable(c);
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			clientid = (String) bundle.get("clientid");
		}
		bar.setTitle("View All Clients");
		bar.setIcon(android.R.color.transparent);
		bar.setDisplayHomeAsUpEnabled(true);

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		SharedPreferences ip;
		ip = getSharedPreferences("IPaddr", Context.MODE_PRIVATE);
		changedIP = ip.getString("IP", null);

		mypref = getSharedPreferences("preferencellogin", Context.MODE_PRIVATE);
		lid = mypref.getString("lawyerid", null);

		progress = new ProgressDialog(this);
		progress.setMessage("Loading Client Details..");
		progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progress.setCancelable(false);
		progress.setCanceledOnTouchOutside(false);
		progress.show();
		s = (ScrollView) findViewById(R.id.scrollView1View_Client_lawyer);
		s.setVisibility(View.INVISIBLE);

		getClientDetails();

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

		ClientID = (TextView) findViewById(R.id.txtClientView_lawyer);
		CName = (TextView) findViewById(R.id.ClientName_lawyer);
		CEMail = (EditText) findViewById(R.id.ClientEmail_lawyer);
		CPhn = (EditText) findViewById(R.id.ClientPhn_lawyer);
		CHaddr = (EditText) findViewById(R.id.ClientHaddr_lawyer);
		CPlace = (EditText) findViewById(R.id.ClientPlace_lawyer);
		CCity = (EditText) findViewById(R.id.ClientCity_lawyer);
		CState = (EditText) findViewById(R.id.ClientState_lawyer);
		CCountry = (EditText) findViewById(R.id.ClientCountry_lawyer);

		ClientID.setText("ClientID: " + clientid);
		CName.setText(C_details[1]);
		CEMail.setText(C_details[2]);
		CPhn.setText(C_details[3]);
		CHaddr.setText(C_details[4]);
		CPlace.setText(C_details[5]);
		CCity.setText(C_details[6]);
		CState.setText(C_details[7]);
		CCountry.setText(C_details[8]);
	}

	public void getClientDetails() {

		int i = 0;

		try {
			HttpClient client = new DefaultHttpClient();
			HttpPost request = new HttpPost("http://" + changedIP + ":8080/AndroLawyer/actClientDetailsGet.jsp");
			List<NameValuePair> PostParameters = new ArrayList<NameValuePair>();
			PostParameters.add(new BasicNameValuePair("cid", clientid));
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
		CEMail.setText("");
		CPhn.setText("");
		CHaddr.setText("");
		CPlace.setText("");
		CCity.setText("");
		CState.setText("");
		CCountry.setText("");
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

		SCEMail = CEMail.getText().toString();
		SCPhn = CPhn.getText().toString();
		SCHaddr = CHaddr.getText().toString();
		SCPlace = CPlace.getText().toString();
		SCCity = CCity.getText().toString();
		SCState = CState.getText().toString();
		SCCountry = CCountry.getText().toString();

		if (!SCEMail.equals("") && !SCPhn.equals("") && !SCHaddr.equals("") && !SCPlace.equals("") && !SCCity.equals("")
				&& !SCState.equals("") && !SCCountry.equals("")) {
			try {
				HttpClient client = new DefaultHttpClient();
				HttpPost request = new HttpPost(
						"http://" + changedIP + ":8080/AndroLawyer/actClientDetailsViewAll.jsp");
				List<NameValuePair> PostParameters = new ArrayList<NameValuePair>();

				PostParameters.add(new BasicNameValuePair("clientid", clientid));
				PostParameters.add(new BasicNameValuePair("CEMail", SCEMail));
				PostParameters.add(new BasicNameValuePair("CPhn", SCPhn));
				PostParameters.add(new BasicNameValuePair("CHaddr", SCHaddr));
				PostParameters.add(new BasicNameValuePair("CPlace", SCPlace));
				PostParameters.add(new BasicNameValuePair("CCity", SCCity));
				PostParameters.add(new BasicNameValuePair("CState", SCState));
				PostParameters.add(new BasicNameValuePair("CCountry", SCCountry));

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

	public void alertedit() {
		alert.showAlertDialog(this, null, "Edition Success", false);
	}

	public void removeclientfunc() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		// builder.setIcon(R.drawable.logalert);
		builder.setTitle("Remove Current Client");
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

	public void removeclient() {
		progress = new ProgressDialog(this);
		progress.setMessage("Deleting Client Details..");
		progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progress.setCancelable(false);
		progress.setCanceledOnTouchOutside(false);
		progress.show();
		try {
			HttpClient client = new DefaultHttpClient();
			HttpPost request = new HttpPost("http://" + changedIP + ":8080/AndroLawyer/actClientDetailsRemove.jsp");
			List<NameValuePair> PostParameters = new ArrayList<NameValuePair>();

			PostParameters.add(new BasicNameValuePair("clientid", clientid));
			PostParameters.add(new BasicNameValuePair("lawyerid", lid));

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
				L24ViewClient.this.finish();
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

	public void nodata() {
		Vibrator vibr = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		vibr.vibrate(100);
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		// builder.setIcon(R.drawable.logalert);
		// builder.setTitle("NOTE:");
		builder.setMessage("No Clients !!").setPositiveButton("OK", dialogClickListener2).show();
	}

	DialogInterface.OnClickListener dialogClickListener2 = new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int which) {
			switch (which) {
			case DialogInterface.BUTTON_POSITIVE:
				finish();
				overridePendingTransition(R.anim.animation_enter, R.anim.animation_leave);
				break;
			}
		}
	};
}