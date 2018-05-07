package com.example.androlawyer.lawyer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
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
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.DatePickerDialog.OnDateSetListener;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class LawyerReg extends Activity {
	EditText fullname, dob, phone, qualification, email, user, pass, home, place, city, state, country;
	TextView txtPD, txtSelectCT, txtLAT, txtLNG;
	RadioButton lgenderm, lgenderf;
	CheckBox c1,c2,c3,c4,c5,c6;
	String Sc1="NULL",Sc2="NULL",Sc3="NULL",Sc4="NULL",Sc5="NULL",Sc6="NULL";
	String Sgender = "", Sdob, Sage, lat, lng;
	SharedPreferences mypref, ip;
	Editor edit;
	String changedIP = "";
	ProgressDialog progress;
	AlertDialogManager alert = new AlertDialogManager();
	boolean flagpref = false;

	private Calendar calendar;
	private int year, month, day;

	@Override
	public void onBackPressed() {
		regoutfunc();
	}

	public void regoutfunc() {
		Vibrator vibr = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		vibr.vibrate(100);
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		// builder.setIcon(R.drawable.logalert);
		builder.setTitle("Registration Incomplete");
		builder.setMessage("Entered Details will be all cleared !").setPositiveButton("Let me out",
				dialogClickListener);
		builder.setNegativeButton("Let me complete", dialogClickListener).show();
	}

	DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int which) {
			switch (which) {
			case DialogInterface.BUTTON_POSITIVE:
				finish();
				overridePendingTransition(R.anim.animation_enter, R.anim.animation_leave);
				break;

			case DialogInterface.BUTTON_NEGATIVE:
				dialog.dismiss();
				break;
			}
		}
	};

	@Override
	protected void onResume() {
		super.onResume();
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		mypref = getSharedPreferences("locationlreg", Context.MODE_PRIVATE);
		flagpref = mypref.getBoolean("flag", true);
		mypref = getSharedPreferences("locationlreg", Context.MODE_PRIVATE);
		lat = mypref.getString("lat", null);
		lng = mypref.getString("lng", null);
		txtLAT = (TextView) findViewById(R.id.LATLreg);
		txtLNG = (TextView) findViewById(R.id.LNGLreg);
		txtLAT.setText(lat);
		txtLNG.setText(lng);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lreg);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		ip = getSharedPreferences("IPaddr", Context.MODE_PRIVATE);
		changedIP = ip.getString("IP", null);
		mypref = getSharedPreferences("locationlreg", Context.MODE_PRIVATE);
		edit = mypref.edit();
		edit.putString("lat", "Click Above..");
		edit.putString("lng", "Click Above..");
		edit.putBoolean("flag", true);
		edit.commit();

		progress = new ProgressDialog(this);

		txtPD = (TextView) findViewById(R.id.txtPDLreg);
		txtSelectCT = (TextView) findViewById(R.id.lregCTtxt);
		
		txtPD.setPaintFlags(txtPD.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
		txtSelectCT.setPaintFlags(txtSelectCT.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
		
		c1=(CheckBox)findViewById(R.id.lregCT1);
		c2=(CheckBox)findViewById(R.id.lregCT2);
		c3=(CheckBox)findViewById(R.id.lregCT3);
		c4=(CheckBox)findViewById(R.id.lregCT4);
		c5=(CheckBox)findViewById(R.id.lregCT5);
		c6=(CheckBox)findViewById(R.id.lregCT6);
		
		fullname = (EditText) findViewById(R.id.LNameLreg);
		dob = (EditText) findViewById(R.id.LDOBLreg);
		phone = (EditText) findViewById(R.id.LPhnLreg);
		qualification = (EditText) findViewById(R.id.LQualLreg);
		email = (EditText) findViewById(R.id.LEmailLreg);
		user = (EditText) findViewById(R.id.LUserLreg);
		pass = (EditText) findViewById(R.id.LPassLreg);
		lgenderm = (RadioButton) findViewById(R.id.LGenderMLreg);
		lgenderf = (RadioButton) findViewById(R.id.LGenderFLreg);
		home = (EditText) findViewById(R.id.addressLreg);
		place = (EditText) findViewById(R.id.placeLreg);
		city = (EditText) findViewById(R.id.cityLreg);
		state = (EditText) findViewById(R.id.stateLreg);
		country = (EditText) findViewById(R.id.countryLreg);
		txtLAT = (TextView) findViewById(R.id.LATLreg);
		txtLNG = (TextView) findViewById(R.id.LNGLreg);
		txtLAT.setText("Click Above..");
		txtLNG.setText("Click Above..");
		calendar = Calendar.getInstance();
		year = calendar.get(Calendar.YEAR);
		month = calendar.get(Calendar.MONTH);
		day = calendar.get(Calendar.DAY_OF_MONTH);
		System.out.println("1" + txtLAT.getText().toString() + "2");
		System.out.println("1" + txtLNG.getText().toString() + "2");
	}
	
	public void CTCheck(View v)
	{
		//Toast.makeText(this, Sc1+Sc2+Sc3+Sc4+Sc5+Sc6, Toast.LENGTH_SHORT).show();
	}
	 
         
	public void GenderCheck(View v) {
		boolean checked = ((RadioButton) v).isChecked();
		switch (v.getId()) {
		case R.id.LGenderMLreg:
			if (checked)
				lgenderf.setChecked(false);
			Sgender = "Male";
			break;
		case R.id.LGenderFLreg:
			if (checked)
				lgenderm.setChecked(false);
			Sgender = "Female";
			break;
		default:
			break;
		}
	}

	public String agecalc() {
		String Sdob = dob.getText().toString().trim();
		String SdobYear = Sdob.substring(Sdob.length() - 4);
		int DobYear = Integer.parseInt(SdobYear);
		int CurrentYear = Calendar.getInstance().get(Calendar.YEAR);
		int intSage = CurrentYear - DobYear;
		String Sage = "" + intSage;
		return Sage;
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

	// DATE PICKER FUNCTIONS

	@SuppressWarnings("deprecation")
	public void mydate(View view) {
		showDialog(999);
		// Toast.makeText(getApplicationContext(), "Date Selected",
		// Toast.LENGTH_SHORT)
		// .show();
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		// TODO Auto-generated method stub
		if (id == 999) {
			DatePickerDialog dtd = new DatePickerDialog(this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT, myDateListener,
					year, month, day);
			// DatePickerDialog dtd = new DatePickerDialog(this, myDateListener,
			// year, month, day);
			Calendar minDate = Calendar.getInstance();
			minDate.set(calendar.get(Calendar.YEAR) - 60, calendar.get(Calendar.MONTH),
					calendar.get(Calendar.DAY_OF_MONTH));
			// dt.DatePicker.MaxDate = maxDate.TimeInMillis;
			dtd.getDatePicker().setMinDate(minDate.getTimeInMillis());
			Calendar maxDate = Calendar.getInstance();
			maxDate.set(calendar.get(Calendar.YEAR) - 20, calendar.get(Calendar.MONTH),
					calendar.get(Calendar.DAY_OF_MONTH));
			// dt.DatePicker.MaxDate = maxDate.TimeInMillis;
			dtd.getDatePicker().setMaxDate(maxDate.getTimeInMillis());
			dtd.setTitle("Date Picker");
			dtd.setMessage("Enter/Select Date Of Birth: ");
			return dtd;
		}
		return null;
	}

	private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker dt, int year, int month, int day) {
			showDate(year, month + 1, day);
			// Toast.makeText(getApplicationContext(), "Date Selected",
			// Toast.LENGTH_SHORT).show();
		}
	};

	private void showDate(int year, int month, int day) {
		dob.setText(new StringBuilder().append(day).append("/").append(month).append("/").append(year));
	}

	// DATE PICKER ENDS

	public void reg(View v) {
		progress.setMessage("Uploading to the server..");
		progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progress.setCancelable(false);
		progress.setCanceledOnTouchOutside(false);
		progress.show();
		String Sfullname = fullname.getText().toString().trim();
		String Sphone = phone.getText().toString().trim();
		String Sdob = dob.getText().toString().trim();
		String Semail = email.getText().toString().trim();
		String Squalification = qualification.getText().toString().trim();
		String Suser = user.getText().toString().trim();
		String Spass = pass.getText().toString().trim();

		String Shome = home.getText().toString().trim();
		String Splace = place.getText().toString().trim();
		String Scity = city.getText().toString().trim();
		String Sstate = state.getText().toString().trim();
		String Scountry = country.getText().toString().trim();

		String Slat = txtLAT.getText().toString().trim();
		String Slng = txtLNG.getText().toString().trim();

		if (!Sfullname.equals("") && !Sphone.equals("") && !Squalification.equals("") && !Semail.equals("")
				&& !Suser.equals("") && !Spass.equals("") && !Shome.equals("") && !Splace.equals("")
				&& !Scity.equals("") && !Sstate.equals("") && !Scountry.equals("") && !Slat.equals("Click Above..")
				&& !Slng.equals("Click Above..") && !Sdob.equals("") && !Sgender.equals("")) {
			try {
				Sage = agecalc();
				HttpClient client = new DefaultHttpClient();
				HttpPost request = new HttpPost("http://" + changedIP + ":8080/AndroLawyer/LawyerRegistration.jsp");
				List<NameValuePair> PostParameters = new ArrayList<NameValuePair>();
				PostParameters.add(new BasicNameValuePair("Lfullname", Sfullname));
				PostParameters.add(new BasicNameValuePair("Lage", Sage));
				PostParameters.add(new BasicNameValuePair("Lgender", Sgender));
				PostParameters.add(new BasicNameValuePair("Lphone", Sphone));
				PostParameters.add(new BasicNameValuePair("Lemail", Semail));
				PostParameters.add(new BasicNameValuePair("Lqualification", Squalification));
				PostParameters.add(new BasicNameValuePair("Luser", Suser));
				PostParameters.add(new BasicNameValuePair("Lpass", Spass));

				PostParameters.add(new BasicNameValuePair("Lhome", Shome));
				PostParameters.add(new BasicNameValuePair("Lplace", Splace));
				PostParameters.add(new BasicNameValuePair("Lcity", Scity));
				PostParameters.add(new BasicNameValuePair("Lstate", Sstate));
				PostParameters.add(new BasicNameValuePair("Lcountry", Scountry));
				PostParameters.add(new BasicNameValuePair("Llat", Slat));
				PostParameters.add(new BasicNameValuePair("Llng", Slng));
				
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
							// Hide your View after 1.5 seconds
							progress.dismiss();
							alert();
						}
					}, 1500);

				} else {
					progress.dismiss();
					alert.showAlertDialog(this, null, "Username already exist", false);
				}
			} catch (Exception e) {
				e.printStackTrace();
				progress.dismiss();
				alert.showAlertDialog(this, "Registration Failed!!", "Please Turn Ur Internet Connection ON", false);
			}
		} else {
			progress.dismiss();
			Toast.makeText(this, "Complete all the fields...", Toast.LENGTH_SHORT).show();
			// alert.showAlertDialog(this, null, "Complete all the fields....",
			// false);
		}

	}

	public void alert() {
		AlertDialog alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setTitle("Registration");
		alertDialog.setMessage("Lawyer registered");
		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				Sc1 = "NULL";
				Sc2 = "NULL";
				Sc3 = "NULL";
				Sc4 = "NULL";
				Sc5 = "NULL";
				Sc6 = "NULL";
				finish();
				Intent i = new Intent(getApplicationContext(), LawyerLogin.class);
				startActivity(i);
				Toast.makeText(getApplicationContext(), "Now wait for admin to approve", Toast.LENGTH_LONG).show();
			}
		});
		alertDialog.show();
	}
}
