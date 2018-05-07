package com.example.androlawyer;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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

public class Welcome_Settings_Lawyer extends Activity {
	ListView list;
	int width, height;
	String[] itemname = { "Profile Management", "IP Configuration", "Sample 3", "About", };

	String[] itemdesc = { "Edit your profle", "Configure your IP address", "Nothing to show..",
			"View the maker of the app", };

	Integer[] imgid = { R.drawable.ic_profile, R.drawable.ic_ip, R.drawable.ic_adcas, R.drawable.ic_about, };

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
					Sample3();
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
		Intent i = new Intent(this, Welcome_Profile_Lawyer.class);
		startActivity(i);
	}

	public void IP() {
		Vibrator vibr = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		vibr.vibrate(100);
		Intent i = new Intent(this, Welcome_IPConfig.class);
		startActivity(i);
	}

	public void Sample3() {
		Vibrator vibr = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		vibr.vibrate(100);
//		Intent i = new Intent(this, Welcome_IPConfig.class);
//		startActivity(i);
		Toast.makeText(this, "Nothing to show..", Toast.LENGTH_SHORT).show();
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
