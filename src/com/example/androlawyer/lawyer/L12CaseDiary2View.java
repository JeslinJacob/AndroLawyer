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
import com.example.androlawyer.CustomListAdapterDiary;
import com.example.androlawyer.R;
import com.example.androlawyer.R.anim;
import com.example.androlawyer.R.id;
import com.example.androlawyer.R.layout;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class L12CaseDiary2View extends Activity {
	ListView list;
	String caseid;
	int width, height;
	String progresscondition = "";
	AlertDialogManager alert = new AlertDialogManager();
	String changedIP = "";
	String[] CaseDate;
	LinearLayout l;
	TextView nodatatv;
	String[] CaseDesc;

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

		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			caseid = (String) bundle.get("caseid");
		}

		SharedPreferences ip;
		ip = getSharedPreferences("IPaddr", Context.MODE_PRIVATE);
		changedIP = ip.getString("IP", null);

		GetDairyDate();
		GetDairyDesc();

		ActionBar bar = getActionBar();
		ColorDrawable c = new ColorDrawable(Color.parseColor("#32b4ff"));
		bar.setBackgroundDrawable(c);
		bar.setTitle("Select Cases");
		bar.setIcon(android.R.color.transparent);
		bar.setDisplayHomeAsUpEnabled(true);

		setContentView(R.layout.activity_l12_case_diary2_view);
		CustomListAdapterDiary adapter = new CustomListAdapterDiary(this, CaseDate, CaseDesc);
		l = (LinearLayout) findViewById(R.id.LLCaseViewDiaryView_L12);
		nodatatv = (TextView) findViewById(R.id.L12NoHistory);
		nodatatv.setVisibility(View.INVISIBLE);
		
		list = (ListView) findViewById(R.id.L12diary2view);
		list.setAdapter(adapter);
		if (list.getAdapter().getItem(0).toString().equals("")) {
			l.setVisibility(View.INVISIBLE);
			nodatatv.setVisibility(View.VISIBLE);
		}
	}

	public void GetDairyDate() {
		int i = 0;

		try {
			HttpClient client = new DefaultHttpClient();
			HttpPost request = new HttpPost("http://" + changedIP + ":8080/AndroLawyer/GetDairyDateWithCaseID.jsp");
			List<NameValuePair> PostParameters = new ArrayList<NameValuePair>();

			PostParameters.add(new BasicNameValuePair("fCaseid", caseid));
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
			CaseDate = result.split("\\*");

		} catch (Exception e) {
			e.printStackTrace();
			alert.showAlertDialog(this, null, "Please Turn Ur Internet Connection ON", false);
		}
	}

	public void GetDairyDesc() {
		int i = 0;

		try {
			HttpClient client = new DefaultHttpClient();
			HttpPost request = new HttpPost("http://" + changedIP + ":8080/AndroLawyer/GetDairyDescWithCaseID.jsp");
			List<NameValuePair> PostParameters = new ArrayList<NameValuePair>();

			PostParameters.add(new BasicNameValuePair("fCaseid", caseid));
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
			CaseDesc = result.split("\\*");

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
			overridePendingTransition(R.anim.blink, R.anim.abc_slide_out_bottom);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}

	}
}
