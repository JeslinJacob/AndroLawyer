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
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class C12AssignCase extends Activity {

	public String Case_ids[];
	ListView lv;
	ProgressDialog progress;
	String progresscondition = "";
	String changedIP = "", userid;
	SharedPreferences mypref;
	LinearLayout l,nodatall;
	TextView nodatatv;
	AlertDialogManager alert = new AlertDialogManager();

	@Override
	protected void onRestart() {
		super.onRestart();
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		SharedPreferences ip;
		ip = getSharedPreferences("IPaddr", Context.MODE_PRIVATE);
		changedIP = ip.getString("IP", null);

		mypref = getSharedPreferences("preferenceclogin", Context.MODE_PRIVATE);
		String Clientid = mypref.getString("clientid", null);
		userid = Clientid;

		progress = new ProgressDialog(this);
		progress.setMessage("Generating All Cases..");
		progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progress.setCancelable(false);
		progress.setCanceledOnTouchOutside(false);
		progress.show();
		l = (LinearLayout) findViewById(R.id.AssignLLCaseView);
		nodatatv= (TextView) findViewById(R.id.C12NODATA);
		nodatatv.setVisibility(View.INVISIBLE);
		nodatall= (LinearLayout) findViewById(R.id.C12NODATALL);
		l.setVisibility(View.INVISIBLE);
		getCaseid();
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
		setContentView(R.layout.activity_c12_assign_case);

		ActionBar bar = getActionBar();
		ColorDrawable c = new ColorDrawable(Color.parseColor("#32b4ff"));
		bar.setBackgroundDrawable(c);
		//bar.setTitle(Html.fromHtml("<font color='#ffffff'>Manage Cases</font>"));
		bar.setTitle("Manage Cases");
		bar.setIcon(android.R.color.transparent);
		bar.setDisplayHomeAsUpEnabled(true);

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		SharedPreferences ip;
		ip = getSharedPreferences("IPaddr", Context.MODE_PRIVATE);
		changedIP = ip.getString("IP", null);

		mypref = getSharedPreferences("preferenceclogin", Context.MODE_PRIVATE);
		String Clientid = mypref.getString("clientid", null);
		userid = Clientid;

		progress = new ProgressDialog(this);
		progress.setMessage("Generating All Cases..");
		progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progress.setCancelable(false);
		progress.setCanceledOnTouchOutside(false);
		progress.show();
		l = (LinearLayout) findViewById(R.id.AssignLLCaseView);
		
		nodatatv= (TextView) findViewById(R.id.C12NODATA);
		nodatatv.setVisibility(View.INVISIBLE);
		nodatall= (LinearLayout) findViewById(R.id.C12NODATALL);

		l.setVisibility(View.INVISIBLE);

		getCaseid();
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

	public void getCaseid() {
		try {
			HttpClient client = new DefaultHttpClient();
			HttpPost request = new HttpPost("http://" + changedIP + ":8080/AndroLawyer/GetCaseIdWithClientID.jsp");
			List<NameValuePair> PostParameters = new ArrayList<NameValuePair>();
			PostParameters.add(new BasicNameValuePair("fClientid", userid));

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
			Case_ids = result.split("\\*");

			ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
					Case_ids);
			ListView lv = (ListView) findViewById(R.id.AssignlstLviewall);
			lv.setAdapter(dataAdapter1);
			System.out.println("DATADATADATADATADATADATA" + lv.getAdapter().getItem(0).toString());
			if (lv.getAdapter().getItem(0).toString().equals("")) {
				// l.setVisibility(View.INVISIBLE);
				nodata();
			}
			lv.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					String caseid = (String) parent.getItemAtPosition(position);
					Vibrator vibr = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
					vibr.vibrate(100);
					Intent i = new Intent(getApplicationContext(), C12ChooseLawyerToAssign.class);
					i.putExtra("caseid", caseid);
					startActivity(i);
				}
			});
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
//		AlertDialog.Builder builder = new AlertDialog.Builder(this);
//		// builder.setIcon(R.drawable.logalert);
//		//builder.setTitle("NOTE:");
//		builder.setCancelable(false).setMessage("No cases to assign!").setPositiveButton("OK",
//				dialogClickListener).show();
	}

	DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int which) {
			switch (which) {
			case DialogInterface.BUTTON_POSITIVE:
				finish();
				overridePendingTransition(R.anim.animation_enter, R.anim.animation_leave);
				break;
			}
		}
	};
}