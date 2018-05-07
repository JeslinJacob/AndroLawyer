package com.example.androlawyer.client;

import com.example.androlawyer.CustomListAdapter;
import com.example.androlawyer.R;
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
import android.text.Html;
import android.view.Display;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class ClientC1Case extends Activity {
	ListView list;
	int width, height;

	String[] itemname = { "Add Case", "Assign Case", "View All Assigned Cases", "Case History", "Case Calender" };

	String[] itemdesc = { "Add new cases", "Assign different lawyers to different cases", "View/Edit Cases",
			"View case history", "Add cases on calenders with alarm" };

	Integer[] imgid = { R.drawable.ic_adcas1, R.drawable.ic_assign, R.drawable.ic_viewassign, R.drawable.ic_chistory,
			R.drawable.ic_calendar };

	@Override
	public void onBackPressed() {
		finish();
		overridePendingTransition(R.anim.blink, R.anim.abc_slide_out_bottom);
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
		// bar.setTitle(Html.fromHtml("<font color='#ffffff'>Manage
		// Cases</font>"));
		bar.setTitle("Manage Cases");
		bar.setIcon(android.R.color.transparent);
		bar.setDisplayHomeAsUpEnabled(true);

		setContentView(R.layout.activity_client_c1_case);
		CustomListAdapter adapter = new CustomListAdapter(this, itemname, imgid, itemdesc);
		list = (ListView) findViewById(R.id.listclientcase1);
		list.setAdapter(adapter);
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				switch (position) {
				case 0:
					AddNew();
					break;
				case 1:
					AssignLawyer();
					break;
				case 2:
					View();
					break;
				case 3:
					CaseHistory();
					break;
				case 4:
					CaseCal();
					break;
				default:
					break;
				}
			}
		});
	}

	public void disp() {
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		width = size.x;
		height = size.y;
		int dialogWidth = width - 100;
		int dialogHeight = height - 350;
		getWindow().setLayout((int) dialogWidth, (int) dialogHeight);
	}

	public void AddNew() {
		Vibrator vibr = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		vibr.vibrate(100);
		Intent i = new Intent(this, C11AddCase.class);
		startActivity(i);
	}

	public void AssignLawyer() {
		Vibrator vibr = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		vibr.vibrate(100);
		Intent i = new Intent(this, C12AssignCase.class);
		startActivity(i);
	}

	public void View() {
		Vibrator vibr = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		vibr.vibrate(100);
		Intent i = new Intent(this, C13ViewAllList.class);
		startActivity(i);
	}

	public void CaseHistory() {
		Vibrator vibr = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		vibr.vibrate(100);
		Intent i = new Intent(this, C14CaseDiaryList.class);
		startActivity(i);
	}

	public void CaseCal() {
		Vibrator vibr = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		vibr.vibrate(100);
		Intent i = new Intent(this, C15CaseCalendarList.class);
		startActivity(i);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			overridePendingTransition(R.anim.blink, R.anim.abc_slide_out_bottom);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
