package com.example.androlawyer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.example.androlawyer.client.C31ChooseContact;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.os.Vibrator;
import android.view.Display;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class Welcome_Settings_Client extends Activity {
	ListView list;
	int width, height;
	boolean flag = false;
	String changedIP = "";
	String[] itemname = { "Profile Management", "IP Configuration", "Map Settings", "About", };

	String[] itemdesc = { "Edit your profle", "Configure your IP address", "Show yourself on map.",
			"View the maker of the app", };

	Integer[] imgid = { R.drawable.ic_profile, R.drawable.ic_ip, R.drawable.mapsetings, R.drawable.ic_about, };

	@Override
	public void onBackPressed() {
		finish();
		overridePendingTransition(R.anim.animation_enter_up2, R.anim.animation_leave_down2);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		disp();

		int id = Resources.getSystem().getIdentifier("action_bar_title", "id", "android");
		TextView txtTitle = (TextView) findViewById(id);
		txtTitle.setGravity(Gravity.CENTER);
		txtTitle.setWidth(width);

		ActionBar bar = getActionBar();
		ColorDrawable c = new ColorDrawable(Color.parseColor("#03A9F4"));
		bar.setBackgroundDrawable(c);
		// bar.setTitle(Html.fromHtml("<font color='#ffffff'>Settings</font>"));
		bar.setTitle("Settings");
		bar.setIcon(android.R.color.transparent);
		bar.setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.activity_welcome_settings);

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		SharedPreferences ip;
		ip = getSharedPreferences("IPaddr", Context.MODE_PRIVATE);
		changedIP = ip.getString("IP", null);
		
		CustomListAdapter adapter = new CustomListAdapter(this, itemname, imgid, itemdesc);
		list = (ListView) findViewById(R.id.listwelcomesettings);
		list.setAdapter(adapter);
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				switch (position) {
				case 0:
					Profile();
					break;
				case 1:
					IP();
					break;
				case 2:
					Map();
					break;
				case 3:
					About();
					break;
				default:
					break;
				}
			}
		});

	}

	public void Profile() {
		
		Vibrator vibr = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		vibr.vibrate(100);
		http();
		if (flag) {
			Intent i = new Intent(this, Welcome_Profile_Client.class);
			startActivity(i);
		}
	}

	public void IP() {
		Vibrator vibr = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		vibr.vibrate(100);
		Intent i = new Intent(this, Welcome_IPConfig.class);
		startActivity(i);
	}

	public void Map() {
		Vibrator vibr = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		vibr.vibrate(100);
		Intent i = new Intent(this, Welcome_Settings_Map.class);
		startActivity(i);
		//overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
	}

	public void About() {
		Vibrator vibr = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		vibr.vibrate(100);
		Intent i = new Intent(this, Welcome_About.class);
		startActivity(i);
	}

	public void disp() {
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		width = size.x;
		height = size.y;
		int dialogWidth = width - 100;
		int dialogHeight = height - 365;
		getWindow().setLayout((int) dialogWidth, (int) dialogHeight);
	}
	public void http() {
		try {
			HttpClient client = new DefaultHttpClient();
			HttpPost request = new HttpPost("http://" + changedIP + ":8080/AndroLawyer/Connection.jsp");

			List<NameValuePair> PostParameters = new ArrayList<NameValuePair>();
			PostParameters.add(new BasicNameValuePair("connection", "COOL"));
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
				flag = true;
			} else {
				flag = false;
				Toast.makeText(this, "Please Check Ur Server Connection", Toast.LENGTH_SHORT).show();
			}
		} catch (HttpHostConnectException e) {
			e.printStackTrace();
			Toast.makeText(this, "Please Check Ur Server Connection", Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			Toast.makeText(this, "Please Check Ur Server Connection", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			overridePendingTransition(R.anim.animation_enter_up2, R.anim.animation_leave_down2);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
