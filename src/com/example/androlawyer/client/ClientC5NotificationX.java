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
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.os.Vibrator;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;

public class ClientC5NotificationX extends Activity {
	
	EditText from,sub,desc;
	public String L_ids [];
	public String L_names[];
	public String curr_lawyer_id;
	SharedPreferences mypref;
	ProgressDialog progress;
	String progresscondition="";
	String changedIP="";
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
		setContentView(R.layout.activity_client_c5_notification);

		ActionBar bar = getActionBar();
		bar.hide();
		
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		SharedPreferences ip;
		ip = getSharedPreferences("IPaddr", Context.MODE_PRIVATE);
		changedIP=ip.getString("IP", null);
		
		from = (EditText) findViewById(R.id.frmcnoti);
		sub= (EditText) findViewById(R.id.subcnoti);
		desc = (EditText) findViewById(R.id.desccnoti);
		
		mypref=getSharedPreferences("preferenceclogin", Context.MODE_PRIVATE);
		String cname= mypref.getString("cname", null);
		
		from.setText("From : "+cname);
		
		progress = new ProgressDialog(this);
		progress.setMessage("Loading Details..");
		progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progress.setCancelable(false);
		progress.setCanceledOnTouchOutside(false);
		progress.show();
		s=(ScrollView)findViewById(R.id.scrollView1cnotification);
		s.setVisibility(View.INVISIBLE);
		
		getLid();
		getLname();
		
		if((progresscondition.equals("AB"))||(progresscondition.equals("BA"))||(progresscondition.equals("A"))||(progresscondition.equals("B")))
		{
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
		
		ArrayAdapter<String> dataAdapter= new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,L_names);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		Spinner clnts = (Spinner)findViewById(R.id.spinnercnoti);
		clnts.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
				curr_lawyer_id=L_ids[position];
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {	
			}
		});
		clnts.setAdapter(dataAdapter);
		
		Handler Shandler = new Handler();
		Shandler.postDelayed(new Runnable() {

			@Override
			public void run() {
				s.fullScroll(View.FOCUS_DOWN);
			}
		}, 2000);
	}
	
	
	
	public void SendNotif() {
		progress = new ProgressDialog(this);
		progress.setMessage("Sending Notification..");
		progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progress.setCancelable(false);
		progress.setCanceledOnTouchOutside(false);
		progress.show();
		mypref=getSharedPreferences("preferenceclogin", Context.MODE_PRIVATE);
		String cid= mypref.getString("clientid", null);
		
		String Sfrom = cid;
		String Sto = curr_lawyer_id;
		String Ssub = sub.getText().toString().trim();
		String Sdesc = desc.getText().toString().trim();
		
		
		if (!Sfrom.equals("")&&!Sto.equals("")&&!Ssub.equals("")&&!Sdesc.equals("")) {
			try {
				HttpClient client = new DefaultHttpClient();
				HttpPost request = new HttpPost("http://"+changedIP+":8080/AndroLawyer/actAddNotification.jsp");
				
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
		} else
		{
			progress.dismiss();
			alert.showAlertDialog(this,null, "Complete all the fields....", false);
		}
	   } 

	public void getLid()
	{
		int i=0;
		
		try {
			
			mypref=getSharedPreferences("preferenceclogin", Context.MODE_PRIVATE);
			String cid= mypref.getString("clientid", null);

			HttpClient client = new DefaultHttpClient();
			HttpPost request = new HttpPost("http://"+changedIP+":8080/AndroLawyer/actGetClientID.jsp");

			List<NameValuePair> PostParameters = new ArrayList<NameValuePair>();
			
			PostParameters.add(new BasicNameValuePair("flid", cid));
			
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
			progresscondition="A";
			L_ids=result.split("\\*");
			
		} catch (Exception e) {
			progresscondition="A";
			e.printStackTrace();
			alert.showAlertDialog(this,null, "Please Turn Ur Internet Connection ON", false);
		}
		
	}
	
	public void getLname()
	{
		
		int i=0;
		
		try {
			
			mypref=getSharedPreferences("preferenceclogin", Context.MODE_PRIVATE);
			String cid= mypref.getString("clientid", null);
			
			HttpClient client = new DefaultHttpClient();
			HttpPost request = new HttpPost("http://"+changedIP+":8080/AndroLawyer/actGetClientName.jsp");

			List<NameValuePair> PostParameters = new ArrayList<NameValuePair>();
			PostParameters.add(new BasicNameValuePair("flid", cid));
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
			progresscondition=progresscondition+"B";
			L_names=result.split("\\*");
	
		} catch (Exception e) {
			e.printStackTrace();
			progresscondition=progresscondition+"B";
			alert.showAlertDialog(this,null, "Please Turn Ur Internet Connection ON", false);
		}
		
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
