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
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.Toast;

public class C53Not2LawyerRequests extends Activity {
	public String Lawyer_Names[],Lawyer_IDs[];
	ListView lv;
	ProgressDialog progress;
	String progresscondition="";
	String changedIP="",userid;
	SharedPreferences mypref;
	LinearLayout l,l2;
	TextView nodatatv;
	String Lawyerid;
	AlertDialogManager alert = new AlertDialogManager();
	
	@Override
	protected void onRestart() {
		super.onRestart();
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		SharedPreferences ip;
		ip = getSharedPreferences("IPaddr", Context.MODE_PRIVATE);
		changedIP=ip.getString("IP", null);
	
		
		mypref=getSharedPreferences("preferencellogin", Context.MODE_PRIVATE);
		userid= mypref.getString("lawyerid", null);
		
		getid();
		getRequests();
		
		progress = new ProgressDialog(this);
		progress.setMessage("Generating All Requests..");
		progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progress.setCancelable(false);
		progress.setCanceledOnTouchOutside(false);
		progress.show();
		l=(LinearLayout)findViewById(R.id.Lrequest);
		l.setVisibility(View.INVISIBLE);
		
		l2 = (LinearLayout) findViewById(R.id.C53NODATALL);
		nodatatv= (TextView) findViewById(R.id.C53NODATA);
		nodatatv.setVisibility(View.INVISIBLE);
		//l2.setVisibility(View.INVISIBLE);
		
		if((progresscondition.equals("A")))
		{
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
		setContentView(R.layout.activity_c53_not2_lawyer_requests);
		System.out.println("RRRRRRRRRRRRRRRRRRRRRR11111111111111111");
		ActionBar bar = getActionBar();
		ColorDrawable c = new ColorDrawable(Color.parseColor("#32b4ff"));
		bar.setBackgroundDrawable(c);
		//bar.setTitle(Html.fromHtml("<font color='#ffffff'>Notifications</font>"));
		bar.setTitle("Notifications");
		bar.setIcon(android.R.color.transparent);
		bar.setDisplayHomeAsUpEnabled(true);
		
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		SharedPreferences ip;
		ip = getSharedPreferences("IPaddr", Context.MODE_PRIVATE);
		changedIP=ip.getString("IP", null);
	
		mypref = getSharedPreferences("preferenceclogin", Context.MODE_PRIVATE);
		userid = mypref.getString("clientid", null);
		
		progress = new ProgressDialog(this);
		progress.setMessage("Generating All Requests..");
		progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progress.setCancelable(false);
		progress.setCanceledOnTouchOutside(false);
		progress.show();
		l=(LinearLayout)findViewById(R.id.Lrequest);
		l.setVisibility(View.INVISIBLE);
		
		l2 = (LinearLayout) findViewById(R.id.C53NODATALL);
		nodatatv= (TextView) findViewById(R.id.C53NODATA);
		nodatatv.setVisibility(View.INVISIBLE);
		//l2.setVisibility(View.INVISIBLE);
		
		getid();
		getRequests();
		
		
		if((progresscondition.equals("A")))
		{
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
			HttpPost request = new HttpPost("http://"+changedIP+":8080/AndroLawyer/RecieveLawyerRequest.jsp");
			List<NameValuePair> PostParameters = new ArrayList<NameValuePair>();
			PostParameters.add(new BasicNameValuePair("fcid", userid));
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
			Lawyer_Names = result.split("\\*");
			System.out.println("RRRRRRRRRRRRRRRRRRRRRR"+result);
			ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
					Lawyer_Names);
			ListView lv = (ListView) findViewById(R.id.requestList_Lrequest);
			lv.setAdapter(dataAdapter1);
			if (lv.getAdapter().getItem(0).toString().equals("")) {
				// l.setVisibility(View.INVISIBLE);
				l2.setVisibility(View.INVISIBLE);
				nodatatv.setVisibility(View.VISIBLE);
			}
			lv.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					Lawyerid=Lawyer_IDs[position];
					Vibrator vibr = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
					vibr.vibrate(100);
					ApproveFunction();
					
					
//		            Intent i = new Intent(getApplicationContext(), C13ViewAll.class);   
//		            i.putExtra("caseid", caseid);
//		            startActivity(i);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
			progress.dismiss();
			alert.showAlertDialog(this,null, "Please Turn Ur Internet Connection ON", false);
		}
		
		
	}
	
	public void getid()
	{
		try {

			HttpClient client = new DefaultHttpClient();
			HttpPost request = new HttpPost("http://"+changedIP+":8080/AndroLawyer/RecieveRequestedLawyerID.jsp");
			List<NameValuePair> PostParameters = new ArrayList<NameValuePair>();
			PostParameters.add(new BasicNameValuePair("fcid", userid));
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
			Lawyer_IDs = result.split("\\*");
			System.out.println("RRRRRRRRRRRRRRRRRRRRRR"+result);
			
		} catch (Exception e) {
			e.printStackTrace();
			progress.dismiss();
			alert.showAlertDialog(this,null, "Please Turn Ur Internet Connection ON", false);
		}
		
		
	}
	public void ApproveFunction()
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		// builder.setIcon(R.drawable.logalert);
		builder.setTitle("Choose Action");
		builder.setMessage("Approve/Reject Lawyer").setPositiveButton("Reject",
				dialogClickListener);
		builder.setNegativeButton("Approve", dialogClickListener).show();
	}
	DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int which) {
			switch (which) {
			case DialogInterface.BUTTON_POSITIVE:
				Toast.makeText(getApplicationContext(), "Rejected", Toast.LENGTH_LONG).show();
//				overridePendingTransition(R.anim.animation_enter, R.anim.animation_leave);
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

	public void onapprove()
	{
		try {

			HttpClient client = new DefaultHttpClient();
			HttpPost request = new HttpPost("http://"+changedIP+":8080/AndroLawyer/actOnApprove.jsp");
			List<NameValuePair> PostParameters = new ArrayList<NameValuePair>();
			PostParameters.add(new BasicNameValuePair("ffrom", Lawyerid));
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
			progresscondition="A";
			
			
			
			if (result.equals("Success")) {
				Toast.makeText(getApplicationContext(), "Approved", Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(getApplicationContext(), "Could not approve lawyer", Toast.LENGTH_LONG).show();
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
			progress.dismiss();
			alert.showAlertDialog(this,null, "Please Turn Ur Internet Connection ON", false);
		}
	}
	
	public void updateMylawyer()
	{
		try {
			
			 System.out.println("YYYYYYYYYYYYYYYYYYYYYYYYYYYYYYY"+Lawyerid);

			HttpClient client = new DefaultHttpClient();
			HttpPost request = new HttpPost("http://"+changedIP+":8080/AndroLawyer/updateMylawyer&Myclient.jsp");
			List<NameValuePair> PostParameters = new ArrayList<NameValuePair>();
			PostParameters.add(new BasicNameValuePair("flawyer", Lawyerid));
			PostParameters.add(new BasicNameValuePair("fclient", userid));
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
			
			
		} catch (Exception e) {
			e.printStackTrace();
			progress.dismiss();
			alert.showAlertDialog(this,null, "Please Turn Ur Internet Connection ON", false);
		}
	}
	
}