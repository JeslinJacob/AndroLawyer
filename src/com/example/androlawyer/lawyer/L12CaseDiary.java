package com.example.androlawyer.lawyer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.example.androlawyer.AlertDialogManager;
import com.example.androlawyer.R;
import com.example.androlawyer.R.anim;
import com.example.androlawyer.R.id;
import com.example.androlawyer.R.layout;

import org.apache.http.NameValuePair;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.os.Vibrator;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class L12CaseDiary extends Activity {
	
	public String C_ids [];
	public String C_names[];
	public String Case_ids[];
	public String curr_client_id;
	public String curr_case_id;
	EditText date,descz;
	SharedPreferences mypref;
	ProgressDialog progress;
	String progresscondition="";
	String changedIP="";
	ScrollView s;
	AlertDialogManager alert = new AlertDialogManager();
	
	private Calendar calendar;
	private int year, month, day;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_l12_case_diary);

		progress = new ProgressDialog(this);
		progress.setMessage("Loading Details..");
		progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progress.setCancelable(false);
		progress.setCanceledOnTouchOutside(false);
		progress.show();
		s=(ScrollView)findViewById(R.id.scrollView1Diary_lawyer);
		s.setVisibility(View.INVISIBLE);
		
		ActionBar bar = getActionBar();
		bar.hide();
		
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		SharedPreferences ip;
		ip = getSharedPreferences("IPaddr", Context.MODE_PRIVATE);
		changedIP=ip.getString("IP", null);
		
		getCid();
		getCname();
		
		if((progresscondition.equals("AB"))||(progresscondition.equals("BA")))
		{
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
		
		date=(EditText)findViewById(R.id.date_casediary_lawyer);
		descz=(EditText)findViewById(R.id.desc_casediary_lawyer);
		
		calendar = Calendar.getInstance();
		year = calendar.get(Calendar.YEAR);
		month = calendar.get(Calendar.MONTH);
		day = calendar.get(Calendar.DAY_OF_MONTH);
		
		ArrayAdapter<String> dataAdapter= new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,C_names);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		Spinner clnts = (Spinner)findViewById(R.id.spinner_client_name_casediary_lawyer);
		clnts.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
				curr_client_id=C_ids[position];
				getCaseid(curr_client_id);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
		clnts.setAdapter(dataAdapter);
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
	
	public void  okclk(View v)
	{
		Animation a = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale_up);
		v.startAnimation(a);
		Vibrator vibr = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		vibr.vibrate(100);
		confirm();
	}
	
	// to get all clientID from table
	
	public void getCid()
	{
		
		int i=0;
		
		try {
			
			mypref=getSharedPreferences("preferencellogin", Context.MODE_PRIVATE);
			String lid= mypref.getString("lawyerid", null);
			
			HttpClient client = new DefaultHttpClient();
			HttpPost request = new HttpPost("http://"+changedIP+":8080/AndroLawyer/actGetClientID.jsp");

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
			progresscondition="A";
			C_ids=result.split("\\*");

		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(this, "Please Turn Ur Internet Connection ON", Toast.LENGTH_SHORT).show();
		}
		
	}
	
	// to get all client names from table
	
	public void getCname()
	{
		
		int i=0;
		
		try {
			
			mypref=getSharedPreferences("preferencellogin", Context.MODE_PRIVATE);
			String lid= mypref.getString("lawyerid", null);
			
			HttpClient client = new DefaultHttpClient();
			HttpPost request = new HttpPost("http://"+changedIP+":8080/AndroLawyer/actGetClientName.jsp");

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
			progresscondition=progresscondition+"B";
			C_names=result.split("\\*");
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(this, "Please Turn Ur Internet Connection ON", Toast.LENGTH_SHORT).show();
		}
		
	}
	
	public void senddata()
	{
		progress = new ProgressDialog(this);
		progress.setMessage("Updating Case Diary..");
		progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progress.setCancelable(false);
		progress.setCanceledOnTouchOutside(false);
		progress.show();
		String caseid = curr_case_id;
		String ddate = date.getText().toString().trim();
		String desc = descz.getText().toString();
		mypref = getSharedPreferences("preferencellogin", Context.MODE_PRIVATE);
		String lid = mypref.getString("lawyerid", null);
		if (!caseid.equals("")&&!ddate.equals("")&&!desc.equals("")) {
			
			try {
				HttpClient client = new DefaultHttpClient();
				HttpPost request = new HttpPost("http://"+changedIP+":8080/AndroLawyer/actCaseDetailsDiary.jsp");
				List<NameValuePair> PostParameters = new ArrayList<NameValuePair>();
				
				PostParameters.add(new BasicNameValuePair("caseid", caseid));
				PostParameters.add(new BasicNameValuePair("ddate", ddate));
				PostParameters.add(new BasicNameValuePair("desc", desc));
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
				//Toast.makeText(this,result, Toast.LENGTH_LONG).show();
				if (result.equals("Success")) {
					Handler handler = new Handler();
					handler.postDelayed(new Runnable() {

						@Override
						public void run() {
							// Hide your View after 3 seconds
							progress.dismiss();
							Toast.makeText(getApplicationContext(), "Case Added", Toast.LENGTH_LONG).show();
							date.setText("");
							descz.setText("");
						}
					}, 1000);
				} else {
					progress.dismiss();
					Toast.makeText(this, "Data Could Not Be Updated", Toast.LENGTH_LONG).show();
				}
			} catch (Exception e) {
				progress.dismiss();
				e.printStackTrace();
				Toast.makeText(this, "Please Turn Ur Internet Connection ON", Toast.LENGTH_SHORT).show();
			}
		} else
		{
			progress.dismiss();
			Toast.makeText(this, "Complete all the fields...", Toast.LENGTH_SHORT).show();
		}
	}
	
	public void getCaseid(String currClnt)
	{
		
		int i=0;
		
		try {
			
			HttpClient client = new DefaultHttpClient();
			HttpPost request = new HttpPost("http://"+changedIP+":8080/AndroLawyer/actCaseDetailsUsingClientID.jsp");
			List<NameValuePair> PostParameters = new ArrayList<NameValuePair>();
			
			mypref=getSharedPreferences("preferencellogin", Context.MODE_PRIVATE);
			String lid= mypref.getString("lawyerid", null);
			
			PostParameters.add(new BasicNameValuePair("flid", lid));
			PostParameters.add(new BasicNameValuePair("curr_client_id", currClnt));
			
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
			Case_ids=result.split("\\*");

			ArrayAdapter<String> dataAdapter1= new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,Case_ids);
			
			dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			Spinner caseSpin = (Spinner)findViewById(R.id.spinner_case_id_casediary_lawyer);
			caseSpin.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
					curr_case_id = Case_ids[position];
				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
				}
			});
			caseSpin.setAdapter(dataAdapter1);
			
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(this, "Please Turn Ur Internet Connection ON", Toast.LENGTH_SHORT).show();
		}
		
	}
	
	public void openDiary(View v)
	{
		Animation a = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale_up);
		v.startAnimation(a);
		Vibrator vibr = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		vibr.vibrate(100);
		Intent i = new Intent(this, L12CaseDiary1List.class);
		startActivity(i);
		overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
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
			DatePickerDialog dtd = new DatePickerDialog(this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT, myDateListener, year, month, day);
//			DatePickerDialog dtd = new DatePickerDialog(this, myDateListener, year, month, day);
			Calendar minDate = Calendar.getInstance();
			minDate.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
					calendar.get(Calendar.DAY_OF_MONTH) - 3);
			// dt.DatePicker.MaxDate = maxDate.TimeInMillis;
			dtd.getDatePicker().setMinDate(minDate.getTimeInMillis());
			Calendar maxDate = Calendar.getInstance();
			maxDate.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
			// dt.DatePicker.MaxDate = maxDate.TimeInMillis;
			dtd.getDatePicker().setMaxDate(maxDate.getTimeInMillis());
			dtd.setTitle("Date Picker");
			dtd.setMessage("Enter/Select day before yesterday, yesterday or today's date");
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
		date.setText(new StringBuilder().append(day).append("/").append(month).append("/").append(year));
	}

	// DATE PICKER ENDS
	
	public void confirm() {
		Vibrator vibr = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		vibr.vibrate(100);
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		// builder.setIcon(R.drawable.logalert);
		builder.setTitle("Enter Case Details");
		builder.setMessage("Entered Details will be all saved and cannot be edited later!").setPositiveButton("Save",
				dialogClickListener);
		builder.setNegativeButton("Let me check", dialogClickListener).show();
	}

	DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int which) {
			switch (which) {
			case DialogInterface.BUTTON_POSITIVE:
				senddata();
				overridePendingTransition(R.anim.animation_enter, R.anim.animation_leave);
				break;

			case DialogInterface.BUTTON_NEGATIVE:
				dialog.dismiss();
				break;
			}
		}
	};
}
