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
import android.content.Context;
import android.content.SharedPreferences;
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

public class LawyerL6Complaint extends Activity {
	
	public String C_ids [];
	public String C_names[];
	public String curr_client_id;
	SharedPreferences mypref;
	EditText from,title,date,descr1;
	ProgressDialog progress;
	String progresscondition="";
	String changedIP="";
	ScrollView s;
	private Calendar calendar;
	private int year, month, day;
	AlertDialogManager alert = new AlertDialogManager();

	@Override
	public void onBackPressed() {
		finish();
		overridePendingTransition(R.anim.animation_enter, R.anim.animation_leave);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lawyer_l6_complaint);
		ActionBar bar = getActionBar();
		bar.hide();
		
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		SharedPreferences ip;
		ip = getSharedPreferences("IPaddr", Context.MODE_PRIVATE);
		changedIP=ip.getString("IP", null);
		
		from=(EditText)findViewById(R.id.frmlcomplaint);
		title= (EditText) findViewById(R.id.titlelcomplaint);
		date = (EditText) findViewById(R.id.datelcomplaint);
		descr1 = (EditText) findViewById(R.id.desclcomplaint);
		
		calendar = Calendar.getInstance();
		year = calendar.get(Calendar.YEAR);
		month = calendar.get(Calendar.MONTH);
		day = calendar.get(Calendar.DAY_OF_MONTH);
		
		mypref=getSharedPreferences("preferencellogin", Context.MODE_PRIVATE);
		String lname= mypref.getString("lname", null);
		
		from.setText("From : "+lname);
		
		progress = new ProgressDialog(this);
		progress.setMessage("Loading Details..");
		progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progress.setCancelable(false);
		progress.setCanceledOnTouchOutside(false);
		progress.show();
		
		s=(ScrollView)findViewById(R.id.scrollView1lcomplaint);
		s.setVisibility(View.INVISIBLE);
		getCid();
		getCname();
		
		if((progresscondition.equals("AB"))||(progresscondition.equals("BA"))||(progresscondition.equals("A"))||(progresscondition.equals("B")))
		{
			Handler handler = new Handler();
			handler.postDelayed(new Runnable() {

				@Override
				public void run() {
					// Hide your View after 3 seconds
					progress.dismiss();
					s.setVisibility(View.VISIBLE);
				}
			}, 2000);
		}

		ArrayAdapter<String> dataAdapter= new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,C_names);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		Spinner clnts = (Spinner)findViewById(R.id.spinnerlcomplaint);
		clnts.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
				curr_client_id=C_ids[position];
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
		clnts.setAdapter(dataAdapter);
	}

	
	public void getCid()
	{
		
		int i=0;
		
		try {
			
			mypref=getSharedPreferences("preferencellogin", Context.MODE_PRIVATE);
			String lid= mypref.getString("lawyerid", null);
			
			HttpClient client = new DefaultHttpClient();
			HttpPost request = new HttpPost("http://"+changedIP+":8080/AndroLawyer/actGetClientIDFromApp.jsp");

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
			progresscondition="A";
			alert.showAlertDialog(this,null, "Please Turn Ur Internet Connection ON", false);
		}
		
	}
	
	public void getCname()
	{
		
		int i=0;
		
		try {
			
			mypref=getSharedPreferences("preferencellogin", Context.MODE_PRIVATE);
			String lid= mypref.getString("lawyerid", null);
			
			HttpClient client = new DefaultHttpClient();
			HttpPost request = new HttpPost("http://"+changedIP+":8080/AndroLawyer/actGetClientNameFromApp.jsp");

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
			progresscondition=progresscondition+"B";
			alert.showAlertDialog(this,null, "Please Turn Ur Internet Connection ON", false);
		}
		
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
	
	public void send(View v) {
		Animation a = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale_up);
		v.startAnimation(a);
		Vibrator vibr = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		vibr.vibrate(100);
		SendCompl();
	}
	
	public void SendCompl() {
		
		progress = new ProgressDialog(this);
		progress.setMessage("Sending Complaint..");
		progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progress.setCancelable(false);
		progress.setCanceledOnTouchOutside(false);
		progress.show();
	
		mypref=getSharedPreferences("preferencellogin", Context.MODE_PRIVATE);
		String lid= mypref.getString("lawyerid", null);
		
		String Sfrom = lid;
		String Sagainst = curr_client_id;
		String Stitle = title.getText().toString().trim();
		String Sdate = date.getText().toString().trim();
		String Sdescr1 = descr1.getText().toString().trim();

		if (!Sfrom.equals("")&&!Sagainst.equals("")&&!Stitle.equals("")&&!Sdate.equals("")&&!Sdescr1.equals("")) {
			
			try {
				HttpClient client = new DefaultHttpClient();
				HttpPost request = new HttpPost("http://"+changedIP+":8080/AndroLawyer/actAddComplaint.jsp");
				
				List<NameValuePair> PostParameters = new ArrayList<NameValuePair>();
				PostParameters.add(new BasicNameValuePair("ffrom", Sfrom));
				PostParameters.add(new BasicNameValuePair("fagainst", Sagainst));
				PostParameters.add(new BasicNameValuePair("ftitle", Stitle));
				PostParameters.add(new BasicNameValuePair("fdate", Sdate));
				PostParameters.add(new BasicNameValuePair("fdescr1", Sdescr1));
				
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
					alert.showAlertDialog(this, "Complaint Failed!!", "Complaint not sent", false);
				}
			} catch (Exception e) {
				e.printStackTrace();
				progress.dismiss();
				alert.showAlertDialog(this, "Complaint Failed!!", "Please Turn Ur Internet Connection ON", false);
			}
		} else
		{
			progress.dismiss();
			Toast.makeText(this, "Complete all the fields...",Toast.LENGTH_SHORT).show();
			//alert.showAlertDialog(this,null, "Complete all the fields....", false);
		}
		
	   } 

	public void alert()
	{
		alert.showAlertDialog(this, "Complaint Registration", "Sent Successfully", false);
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
			dtd.setMessage("Enter/Select Complaint date");
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
}
