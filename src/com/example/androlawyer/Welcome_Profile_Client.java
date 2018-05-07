package com.example.androlawyer;

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

import com.example.androlawyer.lawyer.L13ViewAll;
import com.example.androlawyer.lawyer.LawyerRegMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.os.Vibrator;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class Welcome_Profile_Client extends Activity {

	EditText dispname, phone, email, user, pass, home, place, city, state, country;
	TextView txtPD, txtLAT, txtLNG;
	String lat,lng,cid;
	public String profile_details[];
	SharedPreferences mypref, ip;
	Editor edit;
	String Sdispname;
	String changedIP = "";
	ProgressDialog progress;
	String progresscondition = "";
	ScrollView s;
	String flag;
	AlertDialogManager alert = new AlertDialogManager();
	boolean flagpref = false;

	@Override
	protected void onResume() {
		super.onResume();
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		mypref = getSharedPreferences("locationlreg", Context.MODE_PRIVATE);
		lat = mypref.getString("lat", null);
		lng = mypref.getString("lng", null);
		flag = mypref.getString("flagmap", null);
		
//		System.out.println("GHHHHHHHHHHHHHHHHHH"+flag);
		
		txtLAT = (TextView) findViewById(R.id.CProfile_LATLreg);
		txtLNG = (TextView) findViewById(R.id.CProfile_LNGLreg);
		if (flag.equals("MAP"))
		{
		txtLAT.setText(lat);
		txtLNG.setText(lng);
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome_profile_client);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		ip = getSharedPreferences("IPaddr", Context.MODE_PRIVATE);
		changedIP = ip.getString("IP", null);
		mypref = getSharedPreferences("locationlreg", Context.MODE_PRIVATE);
		edit = mypref.edit();
		edit.putString("lat", "Click Below..");
		edit.putString("lng", "Click Below..");
		edit.putString("flagmap", "NOMAP");
		edit.putBoolean("flag", true);
		edit.commit();

		mypref=getSharedPreferences("preferenceclogin", Context.MODE_PRIVATE);
		cid= mypref.getString("clientid", null);

//		txtLAT.setText("Click Above..");
//		txtLNG.setText("Click Above..");
		
		progress = new ProgressDialog(this);
		progress.setMessage("Loading Clients Profile..");
		progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progress.setCancelable(false);
		progress.setCanceledOnTouchOutside(false);
		progress.show();
		s = (ScrollView) findViewById(R.id.CProfile_scrollView1Lreg);
		s.setVisibility(View.INVISIBLE);

		loaddetails();

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
		
		dispname = (EditText) findViewById(R.id.CProfile_LL_Dispname_text);
		phone = (EditText) findViewById(R.id.CProfile_LL_Number_text);
		email = (EditText) findViewById(R.id.CProfile_LL_Email_text);
		user = (EditText) findViewById(R.id.CProfile_LL_Username_text);
		pass = (EditText) findViewById(R.id.CProfile_LL_Password_text);
		home = (EditText) findViewById(R.id.CProfile_LL_Address_text);
		place = (EditText) findViewById(R.id.CProfile_LL_Place_text);
		city = (EditText) findViewById(R.id.CProfile_LL_City_text);
		state = (EditText) findViewById(R.id.CProfile_LL_State_text);
		country = (EditText) findViewById(R.id.CProfile_LL_Country_text);
		txtLAT = (TextView) findViewById(R.id.CProfile_LATLreg);
		txtLNG = (TextView) findViewById(R.id.CProfile_LNGLreg);

		flag="CREATE";
		
		dispname.setText(profile_details[0]);
		user.setText(profile_details[1]);
		pass.setText(profile_details[2]);
		home.setText(profile_details[3]);
		place.setText(profile_details[4]);
		city.setText(profile_details[5]);
		state.setText(profile_details[6]);
		country.setText(profile_details[7]);
		txtLAT.setText(profile_details[8]);
		txtLNG.setText(profile_details[9]);
		email.setText(profile_details[10]);
		phone.setText(profile_details[11]);

	}

	public void MAP(View v) {
		Animation a = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale_up);
		v.startAnimation(a);
		Vibrator vibr = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		vibr.vibrate(100);
		String splace = place.getText().toString().trim();
		String scity = city.getText().toString().trim();
		String sstate = state.getText().toString().trim();
		String scountry = country.getText().toString().trim();
		if ((splace.equals("")) || (scity.equals("")) || (sstate.equals("")) || (scountry.equals(""))) {
			alert.showAlertDialog(this, null, "Type your address", false);
		} else {
			String addr = splace + ", " + scity + ", " + sstate + ", " + scountry;
			Intent i = new Intent(this, LawyerRegMap.class);
			i.putExtra("iaddress", addr);
			startActivity(i);
			overridePendingTransition(R.anim.abc_slide_in_bottom, R.anim.abc_slide_in_top);
		}
	}

	public void refreshclk(View v) {
		dispname.setText("");
		phone.setText("");
		email.setText("");
		user.setText("");
		pass.setText("");
		home.setText("");
		place.setText("");
		city.setText("");
		state.setText("");
		country.setText("");
		txtLAT.setText("Click Below..");
		txtLNG.setText("Click Below..");
	}

	public void saveclk(View v) {
		Animation a = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale_up);
		v.startAnimation(a);
		Vibrator vibr = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		vibr.vibrate(100);
		progress = new ProgressDialog(this);
		progress.setMessage("Saving Profile Details..");
		progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progress.setCancelable(false);
		progress.setCanceledOnTouchOutside(false);
		progress.show();

		Sdispname = dispname.getText().toString();
		String Sphone = phone.getText().toString();
		String Semail = email.getText().toString();
		String Suser = user.getText().toString();
		String Spass = pass.getText().toString();
		String Shome = home.getText().toString();
		String Splace = place.getText().toString();
		String Scity = city.getText().toString();
		String Sstate = state.getText().toString();
		String Scountry = country.getText().toString();
		String StxtLAT = txtLAT.getText().toString();
		String StxtLNG = txtLNG.getText().toString();

		if (!Sdispname.equals("") && !Sphone.equals("") && !Semail.equals("") && !Suser.equals("")
				&& !Spass.equals("") && !Shome.equals("") && !Splace.equals("")
				&& !Scity.equals("") && !Sstate.equals("") && !Scountry.equals("") && !StxtLAT.equals("Click Below..")
				&& !StxtLNG.equals("Click Below..")) {
			try {
				HttpClient client = new DefaultHttpClient();
				HttpPost request = new HttpPost("http://" + changedIP + ":8080/AndroLawyer/actClientDetailsSetForProfile.jsp");
				List<NameValuePair> PostParameters = new ArrayList<NameValuePair>();
				PostParameters.add(new BasicNameValuePair("cid", cid));
				PostParameters.add(new BasicNameValuePair("Ldispname", Sdispname));
				PostParameters.add(new BasicNameValuePair("Lphone", Sphone));
				PostParameters.add(new BasicNameValuePair("Lemail", Semail));
				PostParameters.add(new BasicNameValuePair("Luser", Suser));
				PostParameters.add(new BasicNameValuePair("Lpass", Spass));
				PostParameters.add(new BasicNameValuePair("Lhome", Shome));
				PostParameters.add(new BasicNameValuePair("Lplace", Splace));
				PostParameters.add(new BasicNameValuePair("Lcity", Scity));
				PostParameters.add(new BasicNameValuePair("Lstate", Sstate));
				PostParameters.add(new BasicNameValuePair("Lcountry", Scountry));
				PostParameters.add(new BasicNameValuePair("Llat", StxtLAT));
				PostParameters.add(new BasicNameValuePair("Llng", StxtLNG));

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
		alertDialog.setMessage("Profile Modified");
		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				//Welcome_Profile.this.finish();
				mypref = getSharedPreferences("clientprofile", Context.MODE_PRIVATE);
				edit = mypref.edit();
				edit.putString("dispname",""+Sdispname);
				edit.putString("flagprofile","TRUE");
				edit.commit();
				finish();
			}
		});
		alertDialog.show();
	}

	public void loaddetails() {
		try {
			HttpClient client = new DefaultHttpClient();
			HttpPost request = new HttpPost("http://" + changedIP + ":8080/AndroLawyer/actClientDetailsGetForProfile.jsp");
			List<NameValuePair> PostParameters = new ArrayList<NameValuePair>();
			PostParameters.add(new BasicNameValuePair("cid", cid));
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
			profile_details = result.split("\\*");
		} catch (Exception e) {
			e.printStackTrace();
			progresscondition = "A";
			alert.showAlertDialog(this, null, "Please Turn Ur Internet Connection ON", false);
		}
	}

}
