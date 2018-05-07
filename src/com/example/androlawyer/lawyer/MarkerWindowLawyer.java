package com.example.androlawyer.lawyer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.example.androlawyer.R;
import com.example.androlawyer.R.id;
import com.example.androlawyer.R.layout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

public class MarkerWindowLawyer extends Activity {
	String clientname = "", clientphn = "";
	String cid = "";
	float r;
	TextView MWName, MWPhn;
	String changedIP = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_marker_window_lawyer);

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		SharedPreferences ip;
		ip = getSharedPreferences("IPaddr", Context.MODE_PRIVATE);
		changedIP = ip.getString("IP", null);

		MWName = (TextView) findViewById(R.id.MWNamelawyer);
		MWPhn = (TextView) findViewById(R.id.MWPhnlawyer);
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			clientname = (String) bundle.get("clientname");
			clientphn = (String) bundle.get("clientphn");
			cid = (String) bundle.get("clientid");
		}
		MWName.setText(clientname);
		MWPhn.setText(clientphn);
	}

	public void MORE(View v) {
		Intent to = new Intent(getApplicationContext(), LawyerL4MapClient.class);
		to.putExtra("clientid", cid);
		startActivity(to);
		finish();
	}

}
