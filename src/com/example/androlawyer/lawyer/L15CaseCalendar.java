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
import com.example.androlawyer.R.anim;
import com.example.androlawyer.R.id;
import com.example.androlawyer.R.layout;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.os.Vibrator;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class L15CaseCalendar extends Activity implements OnCheckedChangeListener, OnItemSelectedListener {
	public String Case_ids[];
	public String C_details[];
	String caseid;
	int calmin1, calhour1, calmin2, calhour2;
	int caly, calm, cald;
	int caly2, calm2, cald2;
	int item;
	String format = "";
	private Calendar calendar;
	ListView lv;
	LinearLayout l;
	Boolean flag = false;
	EditText CNdate, CNtitle, CNdesc, CNbtime;
	Spinner CNhours;
	CheckBox CNfullday;
	TextView CaseID, CTitle, CDesc, CDate, CStatus, CNetime;
	private int year, month, day;
	String EdtCTitle, EdtCDesc, EdtCDate, EdtCStatus;
	ProgressDialog progress;
	String progresscondition = "";
	String changedIP = "", SendEdtCTitle, SendEdtCDesc;
	ScrollView s;
	AlertDialogManager alert = new AlertDialogManager();
	Calendar CalTime = Calendar.getInstance();
	TimePickerDialog.OnTimeSetListener t = new TimePickerDialog.OnTimeSetListener() {
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			CalTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
			CalTime.set(Calendar.MINUTE, minute);
			updateLabel();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_l15_case_calendar);

		ActionBar bar = getActionBar();
		ColorDrawable c = new ColorDrawable(Color.parseColor("#32b4ff"));
		bar.setBackgroundDrawable(c);
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			caseid = (String) bundle.get("caseid");
		}
		bar.setTitle("View All Cases");
		bar.setIcon(android.R.color.transparent);
		bar.setDisplayHomeAsUpEnabled(true);

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		SharedPreferences ip;
		ip = getSharedPreferences("IPaddr", Context.MODE_PRIVATE);
		changedIP = ip.getString("IP", null);

		progress = new ProgressDialog(this);
		progress.setMessage("Loading Case Details..");
		progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progress.setCancelable(false);
		progress.setCanceledOnTouchOutside(false);
		progress.show();
		s = (ScrollView) findViewById(R.id.scrollView1Case_Lawyer_cal);
		s.setVisibility(View.INVISIBLE);

		getCaseDetails();

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

		CaseID = (TextView) findViewById(R.id.txtc13_lawyer_cal);

		CTitle = (TextView) findViewById(R.id.CTitle_c13_lawyer_cal);
		CDate = (TextView) findViewById(R.id.CDate_c13_lawyer_cal);
		CDesc = (TextView) findViewById(R.id.CDesc_c13_lawyer_cal);
		CStatus = (TextView) findViewById(R.id.CStatus_c13_lawyer_cal);
		CaseID.setText("CaseID: " + caseid);

		CTitle.setText(C_details[1]);
		CDesc.setText(C_details[2]);
		CDate.setText(C_details[3]);
		CStatus.setText(C_details[4]);

		CNdate = (EditText) findViewById(R.id.setcal_date_txtlawyer);
		CNtitle = (EditText) findViewById(R.id.setcal_title_txtlawyer);
		CNdesc = (EditText) findViewById(R.id.setcal_desc_txtlawyer);
		CNbtime = (EditText) findViewById(R.id.setcal_btime_txtlawyer);
		CNetime = (TextView) findViewById(R.id.setcal_eventetime_txtlawyer);
		CNfullday = (CheckBox) findViewById(R.id.setcal_ftimelawyer);
		CNhours = (Spinner) findViewById(R.id.setcal_spinnerlawyer);
		CNhours.setOnItemSelectedListener(this);
		List<String> categories = new ArrayList<String>();
		for (int i = 0; i < 100; i++)
			categories.add("" + i);
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
				categories);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		CNhours.setAdapter(dataAdapter);
		CNhours.setSelected(false);

		CNfullday.setOnCheckedChangeListener(this);

		calendar = Calendar.getInstance();
		year = calendar.get(Calendar.YEAR);
		month = calendar.get(Calendar.MONTH);
		day = calendar.get(Calendar.DAY_OF_MONTH);
		l = (LinearLayout) findViewById(R.id.setcalchecklawyer);
		// l.setVisibility(View.GONE);

		Handler Shandler = new Handler();
		Shandler.postDelayed(new Runnable() {

			@Override
			public void run() {
				s.fullScroll(View.FOCUS_DOWN);
			}
		}, 2000);

	}

	public void getCaseDetails() {

		int i = 0;

		try {
			HttpClient client = new DefaultHttpClient();
			HttpPost request = new HttpPost("http://" + changedIP + ":8080/AndroLawyer/actCaseDetailsGet.jsp");
			List<NameValuePair> PostParameters = new ArrayList<NameValuePair>();
			PostParameters.add(new BasicNameValuePair("cid", caseid));
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
			C_details = result.split("\\*");
		} catch (Exception e) {
			e.printStackTrace();
			progresscondition = "A";
			alert.showAlertDialog(this, null, "Please Turn Ur Internet Connection ON", false);
		}
	}

	public void CAL(View v) {
		Animation a = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale_up);
		v.startAnimation(a);
		Vibrator vibr = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		vibr.vibrate(100);

		EdtCDate = CNdate.getText().toString();
		EdtCTitle = CNtitle.getText().toString();
		EdtCDesc = CNdesc.getText().toString();

		if (EdtCDate.equals("")) {
			String casedate = CDate.getText().toString();
			String val[] = casedate.split("\\/");
			caly = Integer.parseInt(val[2]);
			calm = Integer.parseInt(val[1]);
			cald = Integer.parseInt(val[0]);
		} else {

		}
		if (EdtCTitle.equals("")) {
			SendEdtCTitle = CTitle.getText().toString();
		} else {
			SendEdtCTitle = EdtCTitle;
		}
		if (EdtCDesc.equals("")) {
			SendEdtCDesc = CDesc.getText().toString();
		} else {
			SendEdtCDesc = EdtCDesc;
		}
		if (CNfullday.isChecked() == true) {
			flag = true;
		} else {
			flag = false;
		}

		Intent intent = new Intent(Intent.ACTION_EDIT);
		intent.setType("vnd.android.cursor.item/event");
		intent.putExtra(Events.TITLE, SendEdtCTitle);
		intent.putExtra(Events.DESCRIPTION, SendEdtCDesc);
		intent.putExtra(CalendarContract.Events.HAS_ALARM, 1);
		if (flag == true) {
			intent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, true);
			startActivity(intent);
			Toast.makeText(this, "Click Save/Cancel.\nOr you can modify any particular fields\n&then click 'Save'",
					Toast.LENGTH_LONG).show();
		} else {

			Calendar cal = Calendar.getInstance();
			// Toast.makeText(this, "1CC" + calmin1 + " " + calhour1,
			// Toast.LENGTH_LONG).show();
			// System.out.println("1CC" + caly + " " + calm + " " + cald);
			cal.set(Calendar.MONTH, calm - 1);
			cal.set(Calendar.YEAR, caly);
			cal.set(Calendar.DAY_OF_MONTH, cald);
			cal.set(Calendar.HOUR_OF_DAY, calhour1);
			cal.set(Calendar.MINUTE, calmin1);
			cal.set(Calendar.SECOND, 0);
			intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, cal.getTimeInMillis());

			cal.add(Calendar.HOUR_OF_DAY, item);
			intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, cal.getTimeInMillis());

			startActivity(intent);
			Toast.makeText(this, "Click Save/Cancel.\nOr you can modify any particular fields\n&then click 'Save'",
					Toast.LENGTH_LONG).show();
		}

	}

	public void mytime(View v) {
		new TimePickerDialog(L15CaseCalendar.this, t, CalTime.get(Calendar.HOUR_OF_DAY), CalTime.get(Calendar.MINUTE),
				true).show();
	}

	public void updateLabel() {
		int min = CalTime.get(Calendar.MINUTE);
		int hour = CalTime.get(Calendar.HOUR_OF_DAY);
		calmin1 = min;
		calhour1 = hour;
		if (hour == 0) {
			hour += 12;
			format = "AM";
		} else if (hour == 12) {
			format = "PM";
		} else if (hour > 12) {
			hour -= 12;
			format = "PM";
		} else {
			format = "AM";
		}
		CNbtime.setText(hour + ":" + min + "" + format);
		CNetime.setText(hour + ":" + min + "" + format);
		CNhours.setSelection(0);
	}

	public void updateLabel2() {
		CalTime.clear();
		CalTime.set(Calendar.MINUTE, calmin1);
		CalTime.set(Calendar.HOUR_OF_DAY, calhour1);
		CalTime.add(Calendar.HOUR_OF_DAY, item);
		int min = CalTime.get(Calendar.MINUTE);
		int hour = CalTime.get(Calendar.HOUR_OF_DAY);
		caly2 = CalTime.get(Calendar.YEAR);
		calm2 = CalTime.get(Calendar.MONTH);
		cald2 = CalTime.get(Calendar.DAY_OF_MONTH);
		if (hour == 0) {
			hour += 12;
			format = "AM";
		} else if (hour == 12) {
			format = "PM";
		} else if (hour > 12) {
			hour -= 12;
			format = "PM";
		} else {
			format = "AM";
		}
		CNetime.setText(hour + ":" + min + "" + format);
		calmin2 = min;
		calhour2 = hour;
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
			Calendar minDate = Calendar.getInstance();
			minDate.set(calendar.get(Calendar.YEAR) - 0, calendar.get(Calendar.MONTH),
					calendar.get(Calendar.DAY_OF_MONTH) - 1);
			// dt.DatePicker.MaxDate = maxDate.TimeInMillis;
			dtd.getDatePicker().setMinDate(minDate.getTimeInMillis());
			Calendar maxDate = Calendar.getInstance();
			maxDate.set(calendar.get(Calendar.YEAR) + 3, calendar.get(Calendar.MONTH),
					calendar.get(Calendar.DAY_OF_MONTH));
			// dt.DatePicker.MaxDate = maxDate.TimeInMillis;
			dtd.getDatePicker().setMaxDate(maxDate.getTimeInMillis());
			dtd.setTitle("Date Picker");
			dtd.setMessage("Enter/Select Case Date");
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
		CNdate.setText(new StringBuilder().append(day).append("/").append(month).append("/").append(year));
		cald = day;
		calm = month;
		caly = year;
	}

	// DATE PICKER ENDS

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

	@Override
	public void onCheckedChanged(CompoundButton v, boolean isChecked) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.setcal_ftimelawyer:

			if (isChecked == true) {
				l.setVisibility(View.GONE);
			} else {
				l.setVisibility(View.VISIBLE);
				scroll();
			}
			break;
		}
	}

	public void scroll() {
		Handler Shandler = new Handler();
		Shandler.postDelayed(new Runnable() {

			@Override
			public void run() {
				s.fullScroll(View.FOCUS_DOWN);
			}
		}, 200);
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		item = Integer.parseInt(parent.getItemAtPosition(position).toString());
		updateLabel2();
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub

	}

}