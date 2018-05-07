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
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.os.Vibrator;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class L53Not2ClientRequests extends Activity {
	public String Client_Names[], Client_IDs[];
	ListView lv;
	ProgressDialog progress;
	String progresscondition = "";
	String changedIP = "", userid;
	SharedPreferences mypref;
	LinearLayout l, nodatall;
	TextView nodatatv;
	String Clientid;
	AlertDialogManager alert = new AlertDialogManager();

	@Override
	protected void onRestart() {
		super.onRestart();
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		SharedPreferences ip;
		ip = getSharedPreferences("IPaddr", Context.MODE_PRIVATE);
		changedIP = ip.getString("IP", null);

		mypref = getSharedPreferences("preferencellogin", Context.MODE_PRIVATE);
		userid = mypref.getString("lawyerid", null);

		getid();
		getRequests();

		progress = new ProgressDialog(this);
		progress.setMessage("Generating All Requests..");
		progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progress.setCancelable(false);
		progress.setCanceledOnTouchOutside(false);
		progress.show();
		l = (LinearLayout) findViewById(R.id.Crequest);
		l.setVisibility(View.INVISIBLE);

		nodatatv = (TextView) findViewById(R.id.L53NODATA);
		nodatatv.setVisibility(View.INVISIBLE);
		nodatall = (LinearLayout) findViewById(R.id.L53NODATALL);

		if ((progresscondition.equals("A"))) {
			Handler handler = new Handler();
			handler.postDelayed(new Runnable() {

				@Override
				public void run() {
					// Hide your View after 3 seconds
					progress.dismiss();
					l.setVisibility(View.VISIBLE);
				}
			}, 1000);
		}

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		System.out.println("RRRRRRRRRRRRRRRRRRRRRR11111111111111111");
		setContentView(R.layout.activity_l53_not2_client_requests);
		System.out.println("RRRRRRRRRRRRRRRRRRRRRR11111111111111111");
		ActionBar bar = getActionBar();
		ColorDrawable c = new ColorDrawable(Color.parseColor("#32b4ff"));
		bar.setBackgroundDrawable(c);
		bar.setTitle("Notifications");
		bar.setIcon(android.R.color.transparent);
		bar.setDisplayHomeAsUpEnabled(true);

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		SharedPreferences ip;
		ip = getSharedPreferences("IPaddr", Context.MODE_PRIVATE);
		changedIP = ip.getString("IP", null);

		mypref = getSharedPreferences("preferencellogin", Context.MODE_PRIVATE);
		userid = mypref.getString("lawyerid", null);

		progress = new ProgressDialog(this);
		progress.setMessage("Generating All Requests..");
		progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progress.setCancelable(false);
		progress.setCanceledOnTouchOutside(false);
		progress.show();
		l = (LinearLayout) findViewById(R.id.Crequest);
		l.setVisibility(View.INVISIBLE);

		nodatatv = (TextView) findViewById(R.id.L53NODATA);
		nodatatv.setVisibility(View.INVISIBLE);
		nodatall = (LinearLayout) findViewById(R.id.L53NODATALL);

		System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
		getid();
		getRequests();
		System.out.println("BBBBBBBBBBBBBBBBBBB");

		if ((progresscondition.equals("A"))) {
			Handler handler = new Handler();
			handler.postDelayed(new Runnable() {

				@Override
				public void run() {
					// Hide your View after 3 seconds
					progress.dismiss();
					l.setVisibility(View.VISIBLE);
				}
			}, 1000);
		}
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

	public void getRequests() {

		int i = 0;

		try {

			HttpClient client = new DefaultHttpClient();
			HttpPost request = new HttpPost(
					"http://" + changedIP + ":8080/AndroLawyer/RecieveRequestedClientNames.jsp");
			List<NameValuePair> PostParameters = new ArrayList<NameValuePair>();
			PostParameters.add(new BasicNameValuePair("flid", userid));
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
			Client_Names = result.split("\\*");
			System.out.println("RRRRRRRRRRRRRRRRRRRRRR" + result);
			ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
					Client_Names);
			ListView lv = (ListView) findViewById(R.id.requestList_Crequest);
			lv.setAdapter(dataAdapter1);
			if (lv.getAdapter().getItem(0).toString().equals("")) {
				// l.setVisibility(View.INVISIBLE);
				nodata();
			}
			lv.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					Clientid = Client_IDs[position];
					Vibrator vibr = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
					vibr.vibrate(100);
					ApproveFunction();

					// Intent i = new Intent(getApplicationContext(),
					// C13ViewAll.class);
					// i.putExtra("caseid", caseid);
					// startActivity(i);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
			progress.dismiss();
			alert.showAlertDialog(this, null, "Please Turn Ur Internet Connection ON", false);
		}

	}

	public void getid() {
		try {

			HttpClient client = new DefaultHttpClient();
			HttpPost request = new HttpPost("http://" + changedIP + ":8080/AndroLawyer/RecieveRequestedClientIDs.jsp");
			List<NameValuePair> PostParameters = new ArrayList<NameValuePair>();
			PostParameters.add(new BasicNameValuePair("flid", userid));
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
			Client_IDs = result.split("\\*");
			System.out.println("RRRRRRRRRRRRRRRRRRRRRR" + result);

		} catch (Exception e) {
			e.printStackTrace();
			progress.dismiss();
			alert.showAlertDialog(this, null, "Please Turn Ur Internet Connection ON", false);
		}

	}

	public void ApproveFunction() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		// builder.setIcon(R.drawable.logalert);
		builder.setTitle("Choose Action");
		builder.setMessage("Approve/Reject Client").setPositiveButton("Reject", dialogClickListener);
		builder.setNegativeButton("Approve", dialogClickListener).show();
	}

	DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int which) {
			switch (which) {
			case DialogInterface.BUTTON_POSITIVE:
				Toast.makeText(getApplicationContext(), "Rejected", Toast.LENGTH_LONG).show();
				// overridePendingTransition(R.anim.animation_enter,
				// R.anim.animation_leave);
				dialog.dismiss();
				break;

			case DialogInterface.BUTTON_NEGATIVE:
				onapprove();
				updateMylawyer();
				dialog.dismiss();
				finish();
				startActivity(getIntent());
				break;
			}
		}
	};

	public void onapprove() {
		try {

			HttpClient client = new DefaultHttpClient();
			HttpPost request = new HttpPost("http://" + changedIP + ":8080/AndroLawyer/actOnApprove.jsp");
			List<NameValuePair> PostParameters = new ArrayList<NameValuePair>();
			PostParameters.add(new BasicNameValuePair("ffrom", Clientid));
			PostParameters.add(new BasicNameValuePair("ftoid", userid));
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

			if (result.equals("Success")) {
				Toast.makeText(getApplicationContext(), "Approved", Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(getApplicationContext(), "Could not approve lawyer", Toast.LENGTH_LONG).show();
			}

		} catch (Exception e) {
			e.printStackTrace();
			progress.dismiss();
			alert.showAlertDialog(this, null, "Please Turn Ur Internet Connection ON", false);
		}
	}

	public void updateMylawyer() {
		try {

			System.out.println("YYYYYYYYYYYYYYYYYYYYYYYYYYYYYYY" + Clientid);

			HttpClient client = new DefaultHttpClient();
			HttpPost request = new HttpPost("http://" + changedIP + ":8080/AndroLawyer/updateMylawyer&Myclient.jsp");
			List<NameValuePair> PostParameters = new ArrayList<NameValuePair>();
			PostParameters.add(new BasicNameValuePair("flawyer", userid));
			PostParameters.add(new BasicNameValuePair("fclient", Clientid));
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

		} catch (Exception e) {
			e.printStackTrace();
			progress.dismiss();
			alert.showAlertDialog(this, null, "Please Turn Ur Internet Connection ON", false);
		}
	}

	public void nodata() {
		Vibrator vibr = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		vibr.vibrate(100);
		nodatall.setVisibility(View.INVISIBLE);
		nodatatv.setVisibility(View.VISIBLE);
	}
}