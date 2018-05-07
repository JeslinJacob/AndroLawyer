package com.example.androlawyer.client;

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

public class MarkerWindowClient extends Activity {
	String lawyername = "", lawyerphn = "";
	String lid = "";float r;
	TextView MWName, MWPhn;
	RatingBar MWRating;
	String rating, changedIP = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_marker_window_client);

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		SharedPreferences ip;
		ip = getSharedPreferences("IPaddr", Context.MODE_PRIVATE);
		changedIP = ip.getString("IP", null);

		MWName = (TextView) findViewById(R.id.MWName);
		MWPhn = (TextView) findViewById(R.id.MWPhn);
		MWRating = (RatingBar) findViewById(R.id.MWRating);
		LayerDrawable stars = (LayerDrawable) MWRating.getProgressDrawable();
		stars.getDrawable(2).setColorFilter(Color.parseColor("#f9c425"),PorterDuff.Mode.SRC_ATOP);
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			lawyername = (String) bundle.get("lawyername");
			lawyerphn = (String) bundle.get("lawyerphn");
			lid = (String) bundle.get("lawyerid");
		}
		rating();
		MWRating.setRating(r);
		MWName.setText(lawyername);
		MWPhn.setText(lawyerphn);
	}

	public void MORE(View v) {
		Intent to = new Intent(getApplicationContext(), ClientC4MapLawyer.class);
		to.putExtra("lawyerid", lid);
		startActivity(to);
		finish();
	}
	
	public void rating() {
		try {
			HttpClient client = new DefaultHttpClient();
			HttpPost request = new HttpPost("http://" + changedIP + ":8080/AndroLawyer/GetRating.jsp");
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
			rating = result;
			DecimalFormat decimalFormat = new DecimalFormat("#.#");
			Float rat=Float.parseFloat(rating)*5;
			r = Float.valueOf(decimalFormat.format(rat));
		} catch (Exception e) {
			e.printStackTrace();

		}

	}

}
