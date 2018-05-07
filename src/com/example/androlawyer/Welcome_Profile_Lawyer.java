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
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.example.androlawyer.lawyer.L13ViewAll;
import com.example.androlawyer.lawyer.LawyerRegMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.os.Vibrator;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class Welcome_Profile_Lawyer extends Activity {

	EditText dispname, phone, email, user, pass, home, place, city, state, country;
	TextView txtPD, txtLAT, txtLNG, txtSelectCT;
	String lat, lng, lid;
	CheckBox c1, c2, c3, c4, c5, c6;
	String Sc1 = "NULL", Sc2 = "NULL", Sc3 = "NULL", Sc4 = "NULL", Sc5 = "NULL", Sc6 = "NULL";
	public String profile_details[];
	SharedPreferences mypref, ip;
	Editor edit;
	String Sdispname;
	String changedIP = "";
	ProgressDialog progress;
	String progresscondition = "";
	ScrollView s;
	Boolean[] f=new Boolean[6];
	String flag;
	AlertDialogManager alert = new AlertDialogManager();
	boolean flagpref = false;

	@Override
	protected void onResume() {
		super.onResume();
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		mypref = getSharedPreferences("locationlreg", Context.MODE_PRIVATE);
		lat = mypref.getString("lat", null);
		lng = mypref.getString("lng", null);
		flag = mypref.getString("flagmap", null);

		// System.out.println("GHHHHHHHHHHHHHHHHHH"+flag);

		txtLAT = (TextView) findViewById(R.id.Profile_LATLreg);
		txtLNG = (TextView) findViewById(R.id.Profile_LNGLreg);
		if (flag.equals("MAP")) {
			txtLAT.setText(lat);
			txtLNG.setText(lng);
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome_profile_lawyer);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		ip = getSharedPreferences("IPaddr", Context.MODE_PRIVATE);
		changedIP = ip.getString("IP", null);
		mypref = getSharedPreferences("locationlreg", Context.MODE_PRIVATE);
		edit = mypref.edit();
		edit.putString("lat", "Click Below..");
		edit.putString("lng", "Click Below..");
		edit.putString("flagmap", "NOMAP");
		edit.putBoolean("flag", true);
		edit.commit();

		mypref = getSharedPreferences("preferencellogin", Context.MODE_PRIVATE);
		lid = mypref.getString("lawyerid", null);

		// txtLAT.setText("Click Above..");
		// txtLNG.setText("Click Above..");

		progress = new ProgressDialog(this);
		progress.setMessage("Loading Lawyers Profile..");
		progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progress.setCancelable(false);
		progress.setCanceledOnTouchOutside(false);
		progress.show();
		s = (ScrollView) findViewById(R.id.Profile_scrollView1Lreg);
		s.setVisibility(View.INVISIBLE);

		txtSelectCT = (TextView) findViewById(R.id.lprofileCTtxt);
		txtSelectCT.setPaintFlags(txtSelectCT.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

		c1 = (CheckBox) findViewById(R.id.lprofileCT1);
		c2 = (CheckBox) findViewById(R.id.lprofileCT2);
		c3 = (CheckBox) findViewById(R.id.lprofileCT3);
		c4 = (CheckBox) findViewById(R.id.lprofileCT4);
		c5 = (CheckBox) findViewById(R.id.lprofileCT5);
		c6 = (CheckBox) findViewById(R.id.lprofileCT6);

		loaddetails();

		if ((progresscondition.equals("A"))) {
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

		dispname = (EditText) findViewById(R.id.Profile_LL_Dispname_text);
		phone = (EditText) findViewById(R.id.Profile_LL_Number_text);
		email = (EditText) findViewById(R.id.Profile_LL_Email_text);
		user = (EditText) findViewById(R.id.Profile_LL_Username_text);
		pass = (EditText) findViewById(R.id.Profile_LL_Password_text);
		home = (EditText) findViewById(R.id.Profile_LL_Address_text);
		place = (EditText) findViewById(R.id.Profile_LL_Place_text);
		city = (EditText) findViewById(R.id.Profile_LL_City_text);
		state = (EditText) findViewById(R.id.Profile_LL_State_text);
		country = (EditText) findViewById(R.id.Profile_LL_Country_text);
		txtLAT = (TextView) findViewById(R.id.Profile_LATLreg);
		txtLNG = (TextView) findViewById(R.id.Profile_LNGLreg);

		flag = "CREATE";

		dispname.setText(profile_details[0]);
		user.setText(profile_details[1]);
		pass.setText(profile_details[2]);
		home.setText(profile_details[3]);
		place.setText(profile_details[4]);
		city.setText(profile_details[5]);
		state.setText(profile_details[6]);
		country.setText(profile_details[7]);
		txtLAT.setText(profile_details[8]);
		txtLNG.setText(profile_details[9]);
		email.setText(profile_details[10]);
		phone.setText(profile_details[11]);
		
		for(int i=1;i<7;i++)
		{
			String flagprofile=profile_details[11+i];
			if(flagprofile.equals("NULL"))
			{
				f[i-1]=false;
			}
			else
				f[i-1]=true;
		}
		
		c1.setChecked(f[0]);
		c2.setChecked(f[1]);
		c3.setChecked(f[2]);
		c4.setChecked(f[3]);
		c5.setChecked(f[4]);
		c6.setChecked(f[5]);
		
	}

	public void MAP(View v) {
		Animation a = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale_up);
		v.startAnimation(a);
		Vibrator vibr = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		vibr.vibrate(100);
		String splace = place.getText().toString().trim();
		String scity = city.getText().toString().trim();
		String sstate = state.getText().toString().trim();
		String scountry = country.getText().toString().trim();
		if ((splace.equals("")) || (scity.equals("")) || (sstate.equals("")) || (scountry.equals(""))) {
			alert.showAlertDialog(this, null, "Type your address", false);
		} else {
			String addr = splace + ", " + scity + ", " + sstate + ", " + scountry;
			Intent i = new Intent(this, LawyerRegMap.class);
			i.putExtra("iaddress", addr);
			startActivity(i);
			overridePendingTransition(R.anim.abc_slide_in_bottom, R.anim.abc_slide_in_top);
		}
	}

	public void CTCheck(View v)
	{
		//Toast.makeText(this, Sc1+Sc2+Sc3+Sc4+Sc5+Sc6, Toast.LENGTH_SHORT).show();
	}
	
	public void refreshclk(View v) {
		dispname.setText("");
		phone.setText("");
		email.setText("");
		user.setText("");
		pass.setText("");
		home.setText("");
		place.setText("");
		city.setText("");
		state.setText("");
		country.setText("");
		txtLAT.setText("Click Below..");
		txtLNG.setText("Click Below..");
	}

	public void saveclk(View v) {
		Animation a = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale_up);
		v.startAnimation(a);
		Vibrator vibr = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		vibr.vibrate(100);
		progress = new ProgressDialog(this);
		progress.setMessage("Saving Profile Details..");
		progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progress.setCancelable(false);
		progress.setCanceledOnTouchOutside(false);
		progress.show();

		Sdispname = dispname.getText().toString();
		String Sphone = phone.getText().toString();
		String Semail = email.getText().toString();
		String Suser = user.getText().toString();
		String Spass = pass.getText().toString();
		String Shome = home.getText().toString();
		String Splace = place.getText().toString();
		String Scity = city.getText().toString();
		String Sstate = state.getText().toString();
		String Scountry = country.getText().toString();
		String StxtLAT = txtLAT.getText().toString();
		String StxtLNG = txtLNG.getText().toString();

		if (!Sdispname.equals("") && !Sphone.equals("") && !Semail.equals("") && !Suser.equals("") && !Spass.equals("")
				&& !Shome.equals("") && !Splace.equals("") && !Scity.equals("") && !Sstate.equals("")
				&& !Scountry.equals("") && !StxtLAT.equals("Click Below..") && !StxtLNG.equals("Click Below..")) {
			try {
				HttpClient client = new DefaultHttpClient();
				HttpPost request = new HttpPost(
						"http://" + changedIP + ":8080/AndroLawyer/actLawyerDetailsSetForProfile.jsp");
				List<NameValuePair> PostParameters = new ArrayList<NameValuePair>();
				PostParameters.add(new BasicNameValuePair("lid", lid));
				PostParameters.add(new BasicNameValuePair("Ldispname", Sdispname));
				PostParameters.add(new BasicNameValuePair("Lphone", Sphone));
				PostParameters.add(new BasicNameValuePair("Lemail", Semail));
				PostParameters.add(new BasicNameValuePair("Luser", Suser));
				PostParameters.add(new BasicNameValuePair("Lpass", Spass));
				PostParameters.add(new BasicNameValuePair("Lhome", Shome));
				PostParameters.add(new BasicNameValuePair("Lplace", Splace));
				PostParameters.add(new BasicNameValuePair("Lcity", Scity));
				PostParameters.add(new BasicNameValuePair("Lstate", Sstate));
				PostParameters.add(new BasicNameValuePair("Lcountry", Scountry));
				PostParameters.add(new BasicNameValuePair("Llat", StxtLAT));
				PostParameters.add(new BasicNameValuePair("Llng", StxtLNG));

				
				if(c1.isChecked()){  
					Sc1=c1.getTag().toString().trim();
			     }  
				if(c2.isChecked()){  
					Sc2=c2.getTag().toString().trim();
			     }  
				if(c3.isChecked()){  
					Sc3=c3.getTag().toString().trim();
			     }  
				if(c4.isChecked()){  
					Sc4=c4.getTag().toString().trim();
			     }  
				if(c5.isChecked()){  
					Sc5=c5.getTag().toString().trim();
			     }  
				if(c6.isChecked()){  
					Sc6=c6.getTag().toString().trim();
			     }  
				
				PostParameters.add(new BasicNameValuePair("Sc1", Sc1));
				PostParameters.add(new BasicNameValuePair("Sc2", Sc2));
				PostParameters.add(new BasicNameValuePair("Sc3", Sc3));
				PostParameters.add(new BasicNameValuePair("Sc4", Sc4));
				PostParameters.add(new BasicNameValuePair("Sc5", Sc5));
				PostParameters.add(new BasicNameValuePair("Sc6", Sc6));
				
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
					}, 1000);
				} else {
					progress.dismiss();
					alert.showAlertDialog(this, null, "Unknown Error", false);
				}
			} catch (Exception e) {
				e.printStackTrace();
				progress.dismiss();
				alert.showAlertDialog(this, null, "Please Turn Ur Internet Connection ON", false);
			}
		} else {
			progress.dismiss();
			Toast.makeText(this, "Complete all the fields....", Toast.LENGTH_SHORT).show();
			// alert.showAlertDialog(this, null, "Complete all the fields....",
			// false);
		}

	}

	public void alert() {
		AlertDialog alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setTitle(null);
		alertDialog.setMessage("Profile Modified");
		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// Welcome_Profile.this.finish();
				mypref = getSharedPreferences("lawyerprofile", Context.MODE_PRIVATE);
				edit = mypref.edit();
				edit.putString("dispname", "" + Sdispname);
				edit.putString("flagprofile", "TRUE");
				edit.commit();
				finish();
			}
		});
		alertDialog.show();
	}

	public void loaddetails() {
		try {
			HttpClient client = new DefaultHttpClient();
			HttpPost request = new HttpPost(
					"http://" + changedIP + ":8080/AndroLawyer/actLawyerDetailsGetForProfile.jsp");
			List<NameValuePair> PostParameters = new ArrayList<NameValuePair>();
			PostParameters.add(new BasicNameValuePair("lid", lid));
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
			profile_details = result.split("\\*");
		} catch (Exception e) {
			e.printStackTrace();
			progresscondition = "A";
			alert.showAlertDialog(this, null, "Please Turn Ur Internet Connection ON", false);
		}
	}

}
