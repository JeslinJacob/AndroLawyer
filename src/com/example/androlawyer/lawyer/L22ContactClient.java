package com.example.androlawyer.lawyer;

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
import com.example.androlawyer.R.anim;
import com.example.androlawyer.R.id;
import com.example.androlawyer.R.layout;

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
import android.provider.Contacts;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class L22ContactClient extends Activity {
	public String Case_ids[];
	public String C_details[];
	String clientid;
	ListView lv;
	TextView CName, ClientID;
	TextView CEMail, CPhn, CHaddr, CPlace, CCity, CState, CCountry;
	String SCName, SCEMail, SCPhn, SCHaddr, SCPlace, SCCity, SCState, SCCountry;
	ProgressDialog progress;
	String progresscondition = "";
	String changedIP = "",callnum;
	ScrollView s;
	AlertDialogManager alert = new AlertDialogManager();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_l22_contact_client);
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

		progress = new ProgressDialog(this);
		progress.setMessage("Loading Client Details..");
		progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progress.setCancelable(false);
		progress.setCanceledOnTouchOutside(false);
		progress.show();
		s = (ScrollView) findViewById(R.id.scrollView1ClientContacts);
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

		ClientID = (TextView) findViewById(R.id.txtClientViewContacts_lawyer);
		CName = (TextView) findViewById(R.id.ClientNameContacts_lawyer);
		CEMail = (TextView) findViewById(R.id.ClientEmailContacts_lawyer);
		CPhn = (TextView) findViewById(R.id.ClientPhnContacts_lawyer);
		CHaddr = (TextView) findViewById(R.id.ClientHaddrContacts_lawyer);
		CPlace = (TextView) findViewById(R.id.ClientPlaceContacts_lawyer);
		CCity = (TextView) findViewById(R.id.ClientCityContacts_lawyer);
		CState = (TextView) findViewById(R.id.ClientStateContacts_lawyer);
		CCountry = (TextView) findViewById(R.id.ClientCountryContacts_lawyer);

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

	public void callclk(View v) {
		SCPhn = CPhn.getText().toString().trim();
		String Name = CName.getText().toString().trim();
		String Email = CEMail.getText().toString().trim();
		String Haddress = CHaddr.getText().toString().trim();
		String place = CPlace.getText().toString().trim();
		String City = CCity.getText().toString().trim();
		String State = CState.getText().toString().trim();
		String Country = CCountry.getText().toString().trim();
		String addr = Haddress + ", " + place + ", " + City + ", " + State + ", " + Country;
		// Intent i = null;
		// i = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:+91" + SLPhn));
		// startActivity(i);
		Intent addContactIntent = new Intent(Contacts.Intents.Insert.ACTION, Contacts.People.CONTENT_URI);
		addContactIntent.putExtra(Contacts.Intents.Insert.NAME, Name);
		addContactIntent.putExtra(Contacts.Intents.Insert.PHONE, SCPhn);
		addContactIntent.putExtra(Contacts.Intents.Insert.EMAIL, Email);
		addContactIntent.putExtra(Contacts.Intents.Insert.POSTAL, addr);
		addContactIntent.putExtra(Contacts.Intents.Insert.NOTES, "Andro Lawyer");
		startActivity(addContactIntent);
		Toast.makeText(this, "Click Save/Cancel", Toast.LENGTH_SHORT).show();
		
		
	}
	
	public void callclient(String num, String name) {
		callnum = num;
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		// builder.setIcon(R.drawable.logalert);
		builder.setTitle(name);
		builder.setMessage("Call Client ?").setPositiveButton("Yes", dialogClickListener);
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
	
	public void calledclk(View v) {
		Animation a = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale_up);
		v.startAnimation(a);
		Vibrator vibr = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		vibr.vibrate(100);
		String CallNum = CPhn.getText().toString().trim();
		String CallName = CName.getText().toString().trim();
		callclient(CallNum, CallName);
//		SCPhn = CPhn.getText().toString().trim();
//		Intent i = null;
//		i = new Intent(Intent.ACTION_CALL, Uri.parse("tel:+91" + SCPhn));
//		startActivity(i);
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