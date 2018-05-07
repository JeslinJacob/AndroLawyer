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
import com.example.androlawyer.CustomListAdapterDiary;
import com.example.androlawyer.R;

import android.app.ActionBar;
import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class C52Not1Notifications extends Activity {
	ListView list;
	String caseid;
	int width, height;
	String progresscondition = "";
	AlertDialogManager alert = new AlertDialogManager();
	String changedIP = "";
	String cid;
	String[] Notifier;
	String[] Notif;
	LinearLayout l;
	TextView nodatatv;
	
	SharedPreferences mypref;
	Editor edit;
	
	@Override
	public void onBackPressed() {
		this.finish();
		overridePendingTransition(R.anim.blink, R.anim.abc_slide_out_bottom);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		
		SharedPreferences ip;
		ip = getSharedPreferences("IPaddr", Context.MODE_PRIVATE);
		changedIP = ip.getString("IP", null);
		
		mypref = getSharedPreferences("preferenceclogin", Context.MODE_PRIVATE);
		cid = mypref.getString("clientid", null);

		getNotifier();
		getMsg();

		mypref = getSharedPreferences("notif", Context.MODE_PRIVATE);
		edit = mypref.edit();
		edit.putString("msg", "" + Notif.length);
		edit.commit();
		
		NotificationManager notifManager= (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
		notifManager.cancelAll();
		
		ActionBar bar = getActionBar();
		ColorDrawable c = new ColorDrawable(Color.parseColor("#32b4ff"));
		bar.setBackgroundDrawable(c);
		//bar.setTitle(Html.fromHtml("<font color='#ffffff'>View Notifications:</font>"));
		bar.setTitle("View Notifications:");
		bar.setIcon(android.R.color.transparent);
		bar.setDisplayHomeAsUpEnabled(true);

		setContentView(R.layout.activity_c52_not1_notifications);
		
		l = (LinearLayout) findViewById(R.id.C52NODATALL);
		nodatatv= (TextView) findViewById(R.id.C52NODATA);
		nodatatv.setVisibility(View.INVISIBLE);
		//l.setVisibility(View.INVISIBLE);
		
		CustomListAdapterDiary adapter = new CustomListAdapterDiary(this, Notifier, Notif);
		list = (ListView) findViewById(R.id.C52Not1View);
		list.setAdapter(adapter);
		if (list.getAdapter().getItem(0).toString().equals("")) {
			// l.setVisibility(View.INVISIBLE);
			l.setVisibility(View.INVISIBLE);
			nodatatv.setVisibility(View.VISIBLE);
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
			System.out.println("KKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKK"+Notifier.length);

//			if (list.getAdapter().getItem(0).toString().equals("")) 
//			{
//				Toast.makeText(getApplicationContext(), " No Notifications", Toast.LENGTH_LONG).show();
//			}

		} catch (Exception e) 
		{
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
