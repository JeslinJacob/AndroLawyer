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
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class LawyerL5Notification extends Activity {

	EditText from, sub, desc;
	public String C_ids[];
	public String C_names[];
	public String curr_client_id;
	SharedPreferences mypref;
	ProgressDialog progress;
	String progresscondition = "";
	String changedIP = "";
	ScrollView s;
	AlertDialogManager alert = new AlertDialogManager();

	@Override
	public void onBackPressed() {
		finish();
		overridePendingTransition(R.anim.animation_enter, R.anim.animation_leave);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lawyer_l5_notification);

		ActionBar bar = getActionBar();
		bar.hide();

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		SharedPreferences ip;
		ip = getSharedPreferences("IPaddr", Context.MODE_PRIVATE);
		changedIP = ip.getString("IP", null);

		from = (EditText) findViewById(R.id.frmlnoti);
		sub = (EditText) findViewById(R.id.sublnoti);
		desc = (EditText) findViewById(R.id.desclnoti);

		mypref = getSharedPreferences("preferencellogin", Context.MODE_PRIVATE);
		String lname = mypref.getString("lname", null);

		from.setText("From : " + lname);

		progress = new ProgressDialog(this);
		progress.setMessage("Loading Details..");
		progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progress.setCancelable(false);
		progress.setCanceledOnTouchOutside(false);
		progress.show();
		s = (ScrollView) findViewById(R.id.scrollView1lnotification);
		s.setVisibility(View.INVISIBLE);

		getCid();
		getCname();

		if ((progresscondition.equals("AB")) || (progresscondition.equals("BA")) || (progresscondition.equals("A"))
				|| (progresscondition.equals("B"))) {
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

		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
				C_names);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		Spinner clnts = (Spinner) findViewById(R.id.spinnerlnoti);
		clnts.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
				curr_client_id = C_ids[position];
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
		clnts.setAdapter(dataAdapter);
	}

	public void SendNotif() {
		progress = new ProgressDialog(this);
		progress.setMessage("Sending Notification..");
		progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progress.setCancelable(false);
		progress.setCanceledOnTouchOutside(false);
		progress.show();
		mypref = getSharedPreferences("preferencellogin", Context.MODE_PRIVATE);
		String lid = mypref.getString("lawyerid", null);

		String Sfrom = lid;
		String Sto = curr_client_id;
		String Ssub = sub.getText().toString().trim();
		String Sdesc = desc.getText().toString().trim();

		if (!Sfrom.equals("") && !Sto.equals("") && !Ssub.equals("") && !Sdesc.equals("")) {
			try {
				HttpClient client = new DefaultHttpClient();
				HttpPost request = new HttpPost("http://" + changedIP + ":8080/AndroLawyer/actAddNotification.jsp");

				List<NameValuePair> PostParameters = new ArrayList<NameValuePair>();
				PostParameters.add(new BasicNameValuePair("ffrom", Sfrom));
				PostParameters.add(new BasicNameValuePair("fto", Sto));
				PostParameters.add(new BasicNameValuePair("fsub", Ssub));
				PostParameters.add(new BasicNameValuePair("fdesc", Sdesc));

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
						}
					}, 1500);
					notif();
					alert.showAlertDialog(this, "Notification", "Sent Successfully", false);
				} else {
					progress.dismiss();
					alert.showAlertDialog(this, "Notification Failed!!", "Notification not sent", false);
				}
			} catch (Exception e) {
				e.printStackTrace();
				progress.dismiss();
				alert.showAlertDialog(this, "Notification Failed!!", "Please Turn Ur Internet Connection ON", false);
			}
		} else {
			progress.dismiss();
			Toast.makeText(this, "Complete all the fields...",Toast.LENGTH_SHORT).show();
			//alert.showAlertDialog(this, null, "Complete all the fields....", false);
		}
	}

	public void getCid() {
		int i = 0;
		try {

			mypref = getSharedPreferences("preferencellogin", Context.MODE_PRIVATE);
			String lid = mypref.getString("lawyerid", null);

			HttpClient client = new DefaultHttpClient();
			HttpPost request = new HttpPost("http://" + changedIP + ":8080/AndroLawyer/actGetClientIDFromApp.jsp");

			List<NameValuePair> PostParameters = new ArrayList<NameValuePair>();

			PostParameters.add(new BasicNameValuePair("flid", lid));

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
			C_ids = result.split("\\*");

		} catch (Exception e) {
			progresscondition = "A";
			e.printStackTrace();
			alert.showAlertDialog(this, null, "Please Turn Ur Internet Connection ON", false);
		}

	}

	public void getCname() {

		int i = 0;

		try {

			mypref = getSharedPreferences("preferencellogin", Context.MODE_PRIVATE);
			String lid = mypref.getString("lawyerid", null);

			HttpClient client = new DefaultHttpClient();
			HttpPost request = new HttpPost("http://" + changedIP + ":8080/AndroLawyer/actGetClientNameFromApp.jsp");

			List<NameValuePair> PostParameters = new ArrayList<NameValuePair>();
			PostParameters.add(new BasicNameValuePair("flid", lid));
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
			progresscondition = progresscondition + "B";
			C_names = result.split("\\*");

		} catch (Exception e) {
			e.printStackTrace();
			progresscondition = progresscondition + "B";
			alert.showAlertDialog(this, null, "Please Turn Ur Internet Connection ON", false);
		}

	}

	public void notif() {
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
		mBuilder.setSmallIcon(R.drawable.notific2);
		mBuilder.setContentTitle("Notification Alert");
		mBuilder.setContentText("Hi, your message has been successfully sent.");
		mBuilder.setAutoCancel(true);
		Intent resultIntent = new Intent(this, LawyerPage.class);
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
		stackBuilder.addParentStack(LawyerPage.class);
		// Adds the Intent that starts the Activity to the top of the stack
		stackBuilder.addNextIntent(resultIntent);
		PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
		mBuilder.setContentIntent(resultPendingIntent);
		NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		// notificationID allows you to update the notification later on.
		mNotificationManager.notify(9999, mBuilder.build());
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return false;
	}

	public void send(View v) {
		Animation a = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale_up);
		v.startAnimation(a);
		Vibrator vibr = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		vibr.vibrate(100);
		SendNotif();
	}

}
