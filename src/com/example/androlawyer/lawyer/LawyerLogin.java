package com.example.androlawyer.lawyer;

import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.os.Vibrator;

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
import com.example.androlawyer.Welcome_Main;
import com.example.androlawyer.R;
import com.example.androlawyer.Welcome_IPConfig;
import com.example.androlawyer.Welcome_SessionManager;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Paint;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LawyerLogin extends Activity implements OnClickListener {
	TextView Lreg;
	SharedPreferences mypref, ip;
	Editor edit;
	ProgressDialog progress;
	String changedIP = null;
	public String login[];
	private Welcome_SessionManager session;
	AlertDialogManager alert = new AlertDialogManager();

	@Override
	public void onBackPressed() {
		finish();
		Intent i1 = new Intent(getApplicationContext(), Welcome_Main.class);
		startActivity(i1);
		overridePendingTransition(R.anim.animation_enter, R.anim.animation_leave);
	}

	@Override
	protected void onResume() {
		super.onResume();
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		ip = getSharedPreferences("IPaddr", Context.MODE_PRIVATE);
		changedIP = ip.getString("IP", null);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		ActionBar ab = getActionBar();
//		ab.hide();
//	    getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
//	    getActionBar().hide();
		setContentView(R.layout.activity_llogin);
		
//		ActionBar bar = getActionBar();
//		bar.hide();
		
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);

		mypref = getSharedPreferences("IPaddr", Context.MODE_PRIVATE);
		edit = mypref.edit();
		edit.putString("IP", "192.168.3.10");
		edit.commit();

		ip = getSharedPreferences("IPaddr", Context.MODE_PRIVATE);
		changedIP = ip.getString("IP", null);

		session = new Welcome_SessionManager(getApplicationContext(), "LAWYER");

		// Check if user is already logged in or not
		if (session.isLoggedIn()) {
			// User is already logged in. Take him to main activity
			Intent intent = new Intent(LawyerLogin.this, LawyerPage.class);
			startActivity(intent);
			finish();
		}

		progress = new ProgressDialog(this);
		Lreg = (TextView) findViewById(R.id.Lregisterllogin);
		Lreg.setPaintFlags(Lreg.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
		Lreg.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Animation a = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale_up);
		v.startAnimation(a);
		Vibrator vibr1 = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		vibr1.vibrate(100);
		Intent i = new Intent(this, LawyerReg.class);
		startActivity(i);
		overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
		// overridePendingTransition(R.anim.animation_enter,
		// R.anim.animation_leave);
	}

	public void IPAddress(View v) {
		Animation a = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale_up);
		v.startAnimation(a);
		Vibrator vibr = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		vibr.vibrate(100);
		Intent i = new Intent(this, Welcome_IPConfig.class);
		startActivity(i);
		overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
	}

	public void signin(View v) {
		progress.setMessage("Connecting to server..");
		progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progress.setCancelable(false);
		progress.setCanceledOnTouchOutside(false);
		progress.show();
		Vibrator vibr1 = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		vibr1.vibrate(100);
		final String user;
		final String pass;
		EditText Luser, Lpass;
		Luser = (EditText) findViewById(R.id.Luserllogin);
		Lpass = (EditText) findViewById(R.id.Lpassllogin);

		user = Luser.getText().toString().trim();
		pass = Lpass.getText().toString().trim();

		if (!user.equals("") && !pass.equals("")) {

			try {
				HttpClient client = new DefaultHttpClient();
				HttpPost request = new HttpPost("http://" + changedIP + ":8080/AndroLawyer/LawyerLogin.jsp");
				List<NameValuePair> PostParameters = new ArrayList<NameValuePair>();

				PostParameters.add(new BasicNameValuePair("Luser", user));
				PostParameters.add(new BasicNameValuePair("Lpass", pass));

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
							storedata(user, pass);
							session.setLogin(true);
							Intent i = new Intent(getApplicationContext(), LawyerPage.class);
							startActivity(i);
							finish();
						}
					}, 500);
				} else {
					progress.dismiss();
					alert.showAlertDialog(LawyerLogin.this, "Login Failed!!", "Incorrect Username or password", false);
					// Toast.makeText(this, "Incorrect Username or password",
					// Toast.LENGTH_LONG).show();
				}
			} catch (Exception e) {
				e.printStackTrace();
				progress.dismiss();
				alert.showAlertDialog(LawyerLogin.this, "Login Failed!!", "Please Turn Ur Internet Connection ON",
						false);
				// Toast.makeText(this, "Please Turn Ur Internet Connection ON",
				// Toast.LENGTH_SHORT).show();
			}
		} else {
			progress.dismiss();
			alert.showAlertDialog(LawyerLogin.this, "Login Failed!!", "Enter Both Username & Password", false);
			// Toast.makeText(this, "Enter Both Username & Password",
			// Toast.LENGTH_SHORT).show();
		}
	}

	public void storedata(String user, String pass) {

		try {
			HttpClient client = new DefaultHttpClient();
			HttpPost request = new HttpPost("http://" + changedIP + ":8080/AndroLawyer/LawyerLogin_getLid.jsp");

			List<NameValuePair> PostParameters = new ArrayList<NameValuePair>();

			PostParameters.add(new BasicNameValuePair("Luser", user));
			PostParameters.add(new BasicNameValuePair("Lpass", pass));

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
			login = result.split("\\*");
			mypref = getSharedPreferences("preferencellogin", Context.MODE_PRIVATE);
			edit = mypref.edit();
			edit.putString("lawyerid", login[0]);
			edit.putString("lname", login[1]);
			edit.commit();

		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(this, "Please Turn Ur Internet Connection ON", Toast.LENGTH_SHORT).show();
		}

	}

}
