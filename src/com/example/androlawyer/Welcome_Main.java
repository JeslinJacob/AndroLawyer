package com.example.androlawyer;

import com.example.androlawyer.client.ClientLogin;
import com.example.androlawyer.client.ClientPage;
import com.example.androlawyer.lawyer.LawyerLogin;
import com.example.androlawyer.lawyer.LawyerPage;
import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Point;
import android.opengl.Visibility;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.os.Vibrator;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Html;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class Welcome_Main extends FragmentActivity {
	ViewPager viewpager;
	int width;
	ImageView nxtprev, law, cli;
	SharedPreferences st;
	Editor edit;
	TextView t1,t3;
	int state = 0;
	private Welcome_SessionManager sessionlawyer, sessionclient;

	@Override
	protected void onResume() {
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		st = getSharedPreferences("state", Context.MODE_PRIVATE);
		state = st.getInt("state", 0);
		viewpager.setCurrentItem(state);
		super.onResume();
	}

	@Override
	protected void onRestart() {

		TextView MAR = (TextView) findViewById(R.id.WelcomeMAR);
		MAR.setX(width);
		MAR.startAnimation(AnimationUtils.loadAnimation(this, R.anim.move));

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		st = getSharedPreferences("state", Context.MODE_PRIVATE);
		state = st.getInt("state", 0);
		viewpager.setCurrentItem(state);

		super.onRestart();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome_main);
		// System.out.println("State: "+state);

		disp();

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);

		TextView MAR = (TextView) findViewById(R.id.WelcomeMAR);
		MAR.setX(width);
		MAR.startAnimation(AnimationUtils.loadAnimation(this, R.anim.move));

		//ActionBar bar = getActionBar();
		// bar.setBackgroundDrawable(new
		// ColorDrawable(Color.parseColor("#FFFFFF")));
		//bar.setIcon(android.R.color.transparent);
		//bar.setTitle(Html.fromHtml("<font color='#ffffff'>Welcome To Andro-Lawyer</font>"));
		//bar.setSubtitle(Html.fromHtml("<font color='#ffffff'>Lawyer at your finger-tips</font>"));
		//bar.hide();

		nxtprev = (ImageView) findViewById(R.id.Welcomenxtprev);
		law = (ImageView) findViewById(R.id.Welcomelawyer);
		cli = (ImageView) findViewById(R.id.Welcomeclient);
		viewpager = (ViewPager) findViewById(R.id.Welcomepager);
		Welcome_PagerAdapter padapter = new Welcome_PagerAdapter(getSupportFragmentManager());
		viewpager.setAdapter(padapter);
		// Toast.makeText(this,""+viewpager.getCurrentItem(),Toast.LENGTH_SHORT).show();
		viewpager.addOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				if (arg0 == 0) {
					nxtprev.setImageResource(R.drawable.op_l);

				} else {
					nxtprev.setImageResource(R.drawable.op_c);

				}
				//jump();
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
		sessionclient = new Welcome_SessionManager(getApplicationContext(), "CLIENT");
		sessionlawyer = new Welcome_SessionManager(getApplicationContext(), "LAWYER");

		// Check if lawyer is already logged in or not
		if (sessionlawyer.isLoggedIn()) {
			// User is already logged in. Take him to main activity
			Intent intent = new Intent(Welcome_Main.this, LawyerPage.class);
			startActivity(intent);
			finish();
		}

		// Check if client is already logged in or not
		if (sessionclient.isLoggedIn()) {
			// User is already logged in. Take him to main activity
			Intent intent = new Intent(Welcome_Main.this, ClientPage.class);
			startActivity(intent);
			finish();
		}

	}

	public void jump() {
		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {

			@Override
			public void run() {
				Animation a = AnimationUtils.loadAnimation(getApplication(), R.anim.scale_up);
				viewpager.startAnimation(a);
			}
		}, 500);
	}

	public void disp() {
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		width = size.x;
	}

	public void lawyer(View v) {
		Animation a = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale_up);
		v.startAnimation(a);
		Vibrator vibr = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		vibr.vibrate(100);
		state = viewpager.getCurrentItem();
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		st = getSharedPreferences("state", Context.MODE_PRIVATE);
		edit = st.edit();
		edit.putInt("state", 0);
		edit.commit();
		finish();
		Intent i1 = new Intent(getApplicationContext(), LawyerLogin.class);
		startActivity(i1);
		overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
	}

	public void client(View v) {
		Animation a = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale_up);
		v.startAnimation(a);
		Vibrator vibr = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		vibr.vibrate(100);
		state = viewpager.getCurrentItem();
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		st = getSharedPreferences("state", Context.MODE_PRIVATE);
		edit = st.edit();
		edit.putInt("state", 1);
		edit.commit();
		finish();
		Intent i1 = new Intent(getApplicationContext(), ClientLogin.class);
		startActivity(i1);
		overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
	}

}
