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

import com.example.androlawyer.R;
import com.example.androlawyer.R.anim;
import com.example.androlawyer.R.id;
import com.example.androlawyer.R.layout;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RatingBar;
import android.widget.Toast;

public class C24Rating extends Activity {
	String lawyerid;
	SharedPreferences mypref;
	String cid;
	RatingBar rb;
	ProgressDialog progress;
	String rate="2.5";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rating);

		rb = (RatingBar) findViewById(R.id.rateBar);

		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			lawyerid = (String) bundle.get("Slawyerid");
		}
		mypref = getSharedPreferences("preferenceclogin", Context.MODE_PRIVATE);
		cid = mypref.getString("clientid", null);
		LayerDrawable stars = (LayerDrawable) rb.getProgressDrawable();
		stars.getDrawable(2).setColorFilter(Color.parseColor("#f9c425"),PorterDuff.Mode.SRC_ATOP);
	}

	public void saveclk(View v) {
		Animation a = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale_up);
		v.startAnimation(a);
		Vibrator vibr = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		vibr.vibrate(100);

		SharedPreferences ip;
		ip = getSharedPreferences("IPaddr", Context.MODE_PRIVATE);
		String changedIP = ip.getString("IP", null);

		progress = new ProgressDialog(this);
		progress.setMessage("Uploading your rating..");
		progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progress.setCancelable(false);
		progress.setCanceledOnTouchOutside(false);
		progress.show();

		try {
			HttpClient client = new DefaultHttpClient();
			HttpPost request = new HttpPost("http://" + changedIP + ":8080/AndroLawyer/AddRating.jsp");
			List<NameValuePair> PostParameters = new ArrayList<NameValuePair>();
			rate = String.valueOf(rb.getRating());
			PostParameters.add(new BasicNameValuePair("flawyerid", lawyerid));
			PostParameters.add(new BasicNameValuePair("fcid", cid));
			PostParameters.add(new BasicNameValuePair("frate", rate));

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
						alert();
					}
				}, 1500);
			} else {
				progress.dismiss();
				Toast.makeText(this, "Unknown error", Toast.LENGTH_LONG).show();
			}
		} catch (Exception e) {
			e.printStackTrace();
			progress.dismiss();
			Toast.makeText(this, "Unknown error", Toast.LENGTH_LONG).show();
		}

	}

	public void alert() {
		Toast.makeText(this, "Lawyer Succefully Rated: "+rate, Toast.LENGTH_LONG).show();
		finish();
	}

}
