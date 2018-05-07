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
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.example.androlawyer.AlertDialogManager;
import com.example.androlawyer.ConnectionDetector;
import com.example.androlawyer.R;
import com.example.androlawyer.Welcome_SessionManager;
import com.example.androlawyer.Welcome_Settings_Client;
import com.example.androlawyer.lawyer.LawyerPage;
import com.example.androlawyer.Welcome_Main;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ClientPage extends Activity {

	SharedPreferences mypref;
	Editor edit;
	String changedIP = "", cid;
	TextView txtWelcomeClient;
	LinearLayout l;
	ProgressDialog progress;
	String[] Notifier;
	String[] Notif;
	int crrntmsgs=0;
	String progresscondition = "";
	private Welcome_SessionManager session;
	static final String ACTION = "android.net.conn.CONNECTIVITY_CHANGE";
	// flag for Internet connection status
	Boolean isInternetPresent = false;
	// Connection detector class
	ConnectionDetector cd;
	boolean flag = false;
	AlertDialogManager alert = new AlertDialogManager();

	// @Override
	// protected void onResume() {
	// // TODO Auto-generated method stub
	// IntentFilter filter = new IntentFilter(ACTION);
	// this.registerReceiver(BroadCaseActivity, filter);
	// super.onResume();
	// }
	//
	// @Override
	// protected void onPause() {
	// // TODO Auto-generated method stub
	// unregisterReceiver(BroadCaseActivity);
	// super.onPause();
	// }
	@Override
	protected void onResume() {
		super.onResume();
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		mypref = getSharedPreferences("clientprofile", Context.MODE_PRIVATE);
		String dispname = mypref.getString("dispname", null);
		String flagprofile = mypref.getString("flagprofile", null);

		txtWelcomeClient = (TextView) findViewById(R.id.txtClientcpage);
		if (flagprofile.equals("TRUE")) {
			txtWelcomeClient.setText("Welcome " + dispname);
		}
		
		getNotifier();
		getMsg();

		mypref = getSharedPreferences("notif", Context.MODE_WORLD_READABLE);
		String flagNOTIF = mypref.getString("msg", null);
		
		if(flagNOTIF != null)
		{
			String prevmsgs = mypref.getString("msg", null);
			//Toast.makeText(this, "NT NULL: "+prevmsgs, Toast.LENGTH_SHORT).show();
			notif(Notifier, Notif,prevmsgs);
		}
		else
		{
			mypref = getSharedPreferences("notif", Context.MODE_APPEND);
			edit = mypref.edit();
			edit.putString("msg", ""+0);
			edit.commit();
			String prevmsgs = mypref.getString("msg", null);
			//Toast.makeText(this, "NULL: "+prevmsgs, Toast.LENGTH_SHORT).show();
			notif(Notifier, Notif,prevmsgs);
		}
		
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_client_page);
		ActionBar bar = getActionBar();
		// bar.setTitle(Html.fromHtml("<font color='#ffffff'>Hai
		// Client..</font>"));
		bar.setTitle("Hai Client..");
		bar.setIcon(android.R.color.transparent);
		flag = false;
		progress = new ProgressDialog(this);
		progress.setMessage("Setting up things..");
		progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progress.setCancelable(false);
		progress.setCanceledOnTouchOutside(false);
		progress.show();
		mypref = getSharedPreferences("preferenceclogin", Context.MODE_PRIVATE);
		String cname = mypref.getString("cname", null);
		cid = mypref.getString("clientid", null);

		mypref = getSharedPreferences("clientprofile", Context.MODE_PRIVATE);
		String dispname = mypref.getString("dispname", cname);
		String flagprofile = mypref.getString("flagprofile", null);

		mypref = getSharedPreferences("clientprofile", Context.MODE_PRIVATE);
		edit = mypref.edit();
		edit.putString("dispname", "" + dispname);
		edit.putString("flagprofile", "TRUE");
		edit.commit();

		txtWelcomeClient = (TextView) findViewById(R.id.txtClientcpage);
		txtWelcomeClient.setText("Welcome " + dispname);
		l = (LinearLayout) findViewById(R.id.ClientPagecpage);
		l.setVisibility(View.INVISIBLE);
		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {

			@Override
			public void run() {
				// Hide your View after 3 seconds
				progress.dismiss();
				l.setVisibility(View.VISIBLE);
			}
		}, 800);

		// bar.setDisplayShowCustomEnabled(true);
		// bar.setCustomView(R.layout.activity_custom_action);

		// creating connection detector class instance
		// cd = new ConnectionDetector(getApplicationContext());
		// // get Internet status
		// isInternetPresent = cd.isConnectingToInternet();
		//
		// // check for Internet status
		// if (isInternetPresent) {
		// // Internet Connection is Present
		// // make HTTP requests
		// // alert.showAlertDialog(LawyerPage.this, "Internet Connection",
		// // "You have internet connection", false);
		// } else {
		// // Internet connection is not present
		// // Ask user to connect to Internet
		// alert.showAlertDialog(LawyerPage.this, "No Internet Connection",
		// "Please turn on your wifi or mobile data.",
		// false);
		// }

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		SharedPreferences ip;
		ip = getSharedPreferences("IPaddr", Context.MODE_PRIVATE);
		changedIP = ip.getString("IP", null);
		// bar.hide();
		// session manager
		session = new Welcome_SessionManager(getApplicationContext(), "CLIENT");

		if (!session.isLoggedIn()) {
			logoutUser();
		}

		getNotifier();
		getMsg();

		mypref = getSharedPreferences("notif", Context.MODE_WORLD_READABLE);
		String flagNOTIF = mypref.getString("msg", null);
		
		if(flagNOTIF != null)
		{
			String prevmsgs = mypref.getString("msg", null);
			//Toast.makeText(this, "NT NULL: "+prevmsgs, Toast.LENGTH_SHORT).show();
			notif(Notifier, Notif,prevmsgs);
		}
		else
		{
			mypref = getSharedPreferences("notif", Context.MODE_APPEND);
			edit = mypref.edit();
			edit.putString("msg", ""+0);
			edit.commit();
			String prevmsgs = mypref.getString("msg", null);
			//Toast.makeText(this, "NULL: "+prevmsgs, Toast.LENGTH_SHORT).show();
			notif(Notifier, Notif,prevmsgs);
		}
		
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.LLChatListLL:
			Intent i = new Intent(this, Welcome_Settings_Client.class);
			startActivity(i);
			overridePendingTransition(R.anim.animation_enter_up1, R.anim.animation_leave_down1);
			return true;
		case R.id.logout:
			logoutfunc();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void CP1Case(View v) {
		Animation a = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale_up);
		v.startAnimation(a);
		Vibrator vibr = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		vibr.vibrate(100);
		http();
		if (flag) {
			Intent i = new Intent(this, ClientC1Case.class);
			startActivity(i);
			overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
			// overridePendingTransition(R.anim.abc_slide_in_bottom,
			// R.anim.blink);
		}
		// overridePendingTransition(R.anim.abc_slide_in_bottom,
		// R.anim.abc_slide_in_top);
	}

	public void CP2Lawyer(View v) {
		Animation a = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale_up);
		v.startAnimation(a);
		Vibrator vibr = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		vibr.vibrate(100);
		http();
		if (flag) {
			Intent i = new Intent(this, ClientC2Lawyer.class);
			startActivity(i);
			overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
			// overridePendingTransition(R.anim.abc_slide_in_bottom,
			// R.anim.blink);
		}
	}

	public void CP3Chat(View v) {
		Animation a = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale_up);
		v.startAnimation(a);
		Vibrator vibr = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		vibr.vibrate(100);
		http();
		if (flag) {
			Intent i = new Intent(this, C31ChooseContact.class);
			startActivity(i);
			overridePendingTransition(R.anim.abc_slide_in_bottom, R.anim.blink);
		}
	}

	public void CP4Search(View v) {
		Animation a = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale_up);
		v.startAnimation(a);
		Vibrator vibr = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		vibr.vibrate(100);
		http();
		if (flag) {
			Intent i = new Intent(this, C41ChooseCaseType.class);
			// Intent i = new Intent(this, ClientC4MapSearch.class);
			startActivity(i);
			overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
		}
	}

	public void CP5Notification(View v) {
		Animation a = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale_up);
		v.startAnimation(a);
		Vibrator vibr = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		vibr.vibrate(100);
		http();
		if (flag) {
			Intent i = new Intent(this, C51ChooseNotification.class);
			startActivity(i);
			overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
		}

	}

	public void CP6Cmplnt(View v) {
		Animation a = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale_up);
		v.startAnimation(a);
		Vibrator vibr = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		vibr.vibrate(100);
		http();
		if (flag) {
			Intent i = new Intent(this, ClientC6Complaint.class);
			startActivity(i);
			overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
		}
	}

	public void Logout(View v) {
		Animation a = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale_up);
		v.startAnimation(a);
		logoutfunc();
	}

	public void logoutfunc() {
		Vibrator vibr = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		vibr.vibrate(100);
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setIcon(R.drawable.logalert);
		builder.setTitle("Log Out");
		builder.setMessage("Are you sure ?").setPositiveButton("Yes", dialogClickListener);
		builder.setNegativeButton("No", dialogClickListener).show();
	}

	DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int which) {
			switch (which) {
			case DialogInterface.BUTTON_POSITIVE:
				logoutUser();
				break;

			case DialogInterface.BUTTON_NEGATIVE:
				dialog.dismiss();
				break;
			}
		}
	};

	/**
	 * Logging out the user. Will set isLoggedIn flag to false in shared
	 * preferences Clears the user data from sqlite users table
	 */

	private void logoutUser() {
		session.setLogin(false);
		// Launching the login activity
		Intent intent = new Intent(ClientPage.this, Welcome_Main.class);
		startActivity(intent);
		finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.client_page, menu);
		return true;
	}

	public void http() {
		try {
			HttpClient client = new DefaultHttpClient();
			HttpPost request = new HttpPost("http://" + changedIP + ":8080/AndroLawyer/Connection.jsp");

			List<NameValuePair> PostParameters = new ArrayList<NameValuePair>();
			PostParameters.add(new BasicNameValuePair("connection", "COOL"));
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
				flag = true;
			} else {
				flag = false;
				Toast.makeText(this, "Please Check Ur Server Connection", Toast.LENGTH_SHORT).show();
			}
		} catch (HttpHostConnectException e) {
			e.printStackTrace();
			Toast.makeText(this, "Please Check Ur Server Connection", Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			Toast.makeText(this, "Please Check Ur Server Connection", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
	}

	public void getNotifier() {
		int i = 0;

		try {
			HttpClient client = new DefaultHttpClient();
			HttpPost request = new HttpPost("http://" + changedIP + ":8080/AndroLawyer/actGetNotifier.jsp");
			List<NameValuePair> PostParameters = new ArrayList<NameValuePair>();

			PostParameters.add(new BasicNameValuePair("fcid", cid));
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
			Notifier = result.split("\\*");
			System.out.println("KKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKK" + Notifier.length);

		} catch (Exception e) {
			e.printStackTrace();
			alert.showAlertDialog(this, null, "Please Turn Ur Internet Connection ON", false);
		}
	}

	public void getMsg() {
		int i = 0;

		try {
			HttpClient client = new DefaultHttpClient();
			HttpPost request = new HttpPost("http://" + changedIP + ":8080/AndroLawyer/actGetNotification.jsp");
			List<NameValuePair> PostParameters = new ArrayList<NameValuePair>();

			PostParameters.add(new BasicNameValuePair("fcid", cid));
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
			Notif = result.split("\\*");

		} catch (Exception e) {
			e.printStackTrace();
			alert.showAlertDialog(this, null, "Please Turn Ur Internet Connection ON", false);
		}
	}

	public void notif(String[] Nname, String[] Nmsg, String prevmsgs) {

		int pm=Integer.parseInt(prevmsgs);
		crrntmsgs=Nname.length;
		//Toast.makeText(this,"crrntmsgs: "+crrntmsgs, Toast.LENGTH_SHORT).show();
		
		if((crrntmsgs>pm)&&(crrntmsgs!=0))
		{
			for(int i=0;i<Nname.length;i++)
			{
			NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
			mBuilder.setSmallIcon(R.drawable.notific2);
			mBuilder.setContentTitle("Message from : "+Nname[i]);
			mBuilder.setContentText(""+Nmsg[i]);
			mBuilder.setOnlyAlertOnce(true);
			mBuilder.setAutoCancel(true);
			Intent resultIntent = new Intent(this, ClientPage.class);
			TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
			stackBuilder.addParentStack(ClientPage.class);
			// Adds the Intent that starts the Activity to the top of the stack
			stackBuilder.addNextIntent(resultIntent);
			PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
			mBuilder.setContentIntent(resultPendingIntent);
			NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
			// notificationID allows you to update the notification later on.
			mNotificationManager.notify(9999, mBuilder.build());
			}
			Toast.makeText(this, "Go to 'Notification Centre' below & Check 'Receive Notifications'", Toast.LENGTH_SHORT).show();
		}
		else
		{
		}
	}

}
