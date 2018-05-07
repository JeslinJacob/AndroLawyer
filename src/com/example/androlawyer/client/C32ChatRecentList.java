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
import android.app.ProgressDialog;
import android.content.Context;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class C32ChatRecentList extends Activity {
	public String recent_names[], recent_ids[];
	ListView lv;
	ProgressDialog progress;
	String casename;
	String POS;
	String progresscondition = "";
	String changedIP = "", fromid;
	SharedPreferences mypref;
	LinearLayout l, nodatall;
	TextView nodatatv;
	AlertDialogManager alert = new AlertDialogManager();

	@Override
	public void onBackPressed() {
		this.finish();
		overridePendingTransition(R.anim.animation_enter, R.anim.animation_leave);
	}

	@Override
	protected void onRestart() {

		super.onRestart();
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		SharedPreferences ip;
		ip = getSharedPreferences("IPaddr", Context.MODE_PRIVATE);
		changedIP = ip.getString("IP", null);

		mypref = getSharedPreferences("preferenceclogin", Context.MODE_PRIVATE);
		fromid = mypref.getString("clientid", null);

		progress = new ProgressDialog(this);
		progress.setMessage("Generating All Lawyers..");
		progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progress.setCancelable(false);
		progress.setCanceledOnTouchOutside(false);
		progress.show();
		l = (LinearLayout) findViewById(R.id.CC32CHATLIST);
		nodatatv = (TextView) findViewById(R.id.C32NODATA);
		nodatatv.setVisibility(View.INVISIBLE);
		nodatall = (LinearLayout) findViewById(R.id.C32NODATALL);
		l.setVisibility(View.INVISIBLE);
		getRecentid();
		getRecentName();
		if ((progresscondition.equals("AB")) || (progresscondition.equals("BA"))) {
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
		setContentView(R.layout.activity_c32_chat_recent_list);

		ActionBar bar = getActionBar();
		ColorDrawable c = new ColorDrawable(Color.parseColor("#32b4ff"));
		bar.setBackgroundDrawable(c);
		//bar.setTitle(Html.fromHtml("<font color='#ffffff'>Main Menu</font>"));
		bar.setTitle("Main Menu");
		bar.setIcon(android.R.color.transparent);
		bar.setDisplayHomeAsUpEnabled(true);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		SharedPreferences ip;
		ip = getSharedPreferences("IPaddr", Context.MODE_PRIVATE);
		changedIP = ip.getString("IP", null);

		mypref = getSharedPreferences("preferenceclogin", Context.MODE_PRIVATE);
		fromid = mypref.getString("clientid", null);

		progress = new ProgressDialog(this);
		progress.setMessage("Generating All Lawyers..");
		progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progress.setCancelable(false);
		progress.setCanceledOnTouchOutside(false);
		progress.show();
		l = (LinearLayout) findViewById(R.id.CC32CHATLIST);
		nodatatv = (TextView) findViewById(R.id.C32NODATA);
		nodatatv.setVisibility(View.INVISIBLE);
		nodatall = (LinearLayout) findViewById(R.id.C32NODATALL);
		l.setVisibility(View.INVISIBLE);
		getRecentid();
		getRecentName();
		if ((progresscondition.equals("AB")) || (progresscondition.equals("BA"))) {
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

	public void getRecentid() {

		int i = 0;

		try {

			HttpClient client = new DefaultHttpClient();
			HttpPost request = new HttpPost("http://" + changedIP + ":8080/AndroLawyer/GetRecentChatIdsForClient.jsp");
			List<NameValuePair> PostParameters = new ArrayList<NameValuePair>();
			PostParameters.add(new BasicNameValuePair("fLid", fromid));
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
			recent_ids = result.split("\\*");
		} catch (Exception e) {
			e.printStackTrace();
			progresscondition = "A";
			progress.dismiss();
			alert.showAlertDialog(this, null, "Please Turn Ur Internet Connection ON", false);
		}
	}

	public void getRecentName() {

		int i = 0;

		try {

			HttpClient client = new DefaultHttpClient();
			HttpPost request = new HttpPost(
					"http://" + changedIP + ":8080/AndroLawyer/GetRecentChatNamesForClient.jsp");
			List<NameValuePair> PostParameters = new ArrayList<NameValuePair>();
			PostParameters.add(new BasicNameValuePair("fLid", fromid));
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
			recent_names = result.split("\\*");

			System.out.println("CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC  : " + result);
			ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
					recent_names);
			ListView lv = (ListView) findViewById(R.id.CList32CHATLIST);
			lv.setAdapter(dataAdapter1);
			if (lv.getAdapter().getItem(0).toString().equals("")) {
				// l.setVisibility(View.INVISIBLE);
				nodata();
			}
			lv.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					casename = (String) parent.getItemAtPosition(position);
					POS = recent_ids[position];
					//POS = 1+"";
					System.out.println("PPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPP  : " + recent_ids[position]);
					Intent i = new Intent(getApplicationContext(), ClientC3Chat.class);
					i.putExtra("toid", POS);
					startActivity(i);
				}
			});

			// L24ViewClient
		} catch (Exception e) {
			e.printStackTrace();
			progresscondition = progresscondition + "B";
			progress.dismiss();
			alert.showAlertDialog(this, null, "Please Turn Ur Internet Connection ON", false);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			overridePendingTransition(R.anim.animation_enter, R.anim.animation_leave);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void nodata() {
		Vibrator vibr = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		vibr.vibrate(100);
		nodatall.setVisibility(View.INVISIBLE);
		nodatatv.setVisibility(View.VISIBLE);
	}
}
