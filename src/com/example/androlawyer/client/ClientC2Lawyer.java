package com.example.androlawyer.client;

import com.example.androlawyer.CustomListAdapter;
import com.example.androlawyer.R;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.Toast;

public class ClientC2Lawyer extends Activity {
	ListView list;
	int width, height;
	String[] itemname = { "Add Lawyer (from outside) ", "Add Lawyer (having app)", "Contact Any Lawyer",
			"View All Lawyers","Rate Any lawyer" };

	String[] itemdesc = { "Add new lawyer from outside app", "Add lawyers having app", "Save contact / Call a lawyer",
			"View/Edit/Remove Lawyers","Review lawyer's performance" };

	Integer[] imgid = { R.drawable.add1, R.drawable.add1, R.drawable.ic_phone, R.drawable.ic_find,R.drawable.ic_star };

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
		ColorDrawable c = new ColorDrawable(Color.parseColor("#32b4ff"));
		bar.setBackgroundDrawable(c);
		//bar.setTitle(Html.fromHtml("<font color='#ffffff'>Manage Lawyers</font>"));
		bar.setTitle("Manage Lawyers");
		bar.setIcon(android.R.color.transparent);
		bar.setDisplayHomeAsUpEnabled(true);

		setContentView(R.layout.activity_client_c2_lawyer);
		CustomListAdapter adapter = new CustomListAdapter(this, itemname, imgid, itemdesc);
		list = (ListView) findViewById(R.id.listclientlawyer2);
		list.setAdapter(adapter);

		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				switch (position) {
				case 0:
					AddNew();
					break;
				case 1:
					AddNewMapLawyer();
					break;
				case 2:
					Call();
					break;
				case 3:
					View();
					break;
				case 4:
					Rate();
					break;
				default:
					break;
				}
			}
		});
	}

	public void AddNew() {
		Vibrator vibr = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		vibr.vibrate(100);
		Intent i = new Intent(this, C21AddLawyer.class);
		startActivity(i);
	}

	public void AddNewMapLawyer() {
		Vibrator vibr = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		vibr.vibrate(100);
		AlertDialog alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setTitle("Add New Lawyer");
		alertDialog.setMessage("Go to Client Page...and select 'Find Lawyers'");
		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				finish();
				Toast.makeText(getApplicationContext(), "Now scroll down and click 'Find Lawyers' listed below..",
						Toast.LENGTH_LONG).show();
			}
		});
		alertDialog.show();
	}

	public void Call() {
		Vibrator vibr = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		vibr.vibrate(100);
		Intent i = new Intent(this, C23ContactLawyerList.class);
		startActivity(i);
	}

	public void View() {
		Vibrator vibr = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		vibr.vibrate(100);
		Intent i = new Intent(this, C22ViewLawyerList.class);
		startActivity(i);
	}

	public void Rate() {
		Vibrator vibr = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		vibr.vibrate(100);
		Intent i = new Intent(this, C24ViewLawyerList.class);
		startActivity(i);
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
