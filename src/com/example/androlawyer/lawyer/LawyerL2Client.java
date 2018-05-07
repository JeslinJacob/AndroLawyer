package com.example.androlawyer.lawyer;

import com.example.androlawyer.CustomListAdapter;
import com.example.androlawyer.R;
import com.example.androlawyer.R.anim;
import com.example.androlawyer.R.drawable;
import com.example.androlawyer.R.id;
import com.example.androlawyer.R.layout;

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
import android.view.Display;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class LawyerL2Client extends Activity {
	ListView list;
	int width, height;
	String[] itemname = { "Add Client (from outside) ", "Add Client (having app)", "Contact Any Client",
			"View All Clients", };

	String[] itemdesc = { "Add new client from outside app", "Add clients having app", "Save contact / Call a client",
			"View/Edit/Remove Clients", };

	Integer[] imgid = { R.drawable.add1, R.drawable.add1, R.drawable.ic_edit, R.drawable.ic_find, };

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
		bar.setTitle("Manage Clients");
		bar.setIcon(android.R.color.transparent);
		bar.setDisplayHomeAsUpEnabled(true);

		setContentView(R.layout.activity_lawyer_l2_client);
		CustomListAdapter adapter = new CustomListAdapter(this, itemname, imgid, itemdesc);
		list = (ListView) findViewById(R.id.listlawyerclient2);
		list.setAdapter(adapter);

		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				switch (position) {
				case 0:
					AddNew();
					break;
				case 1:
					AddNewMapClient();
					break;
				case 2:
					Call();
					break;
				case 3:
					View();
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
		Intent i = new Intent(this, L21AddClient.class);
		startActivity(i);
	}

	public void AddNewMapClient() {
		Vibrator vibr = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		vibr.vibrate(100);
		AlertDialog alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setTitle("Add New Client");
		alertDialog.setMessage("Go to Lawyer Page...and select 'Find Clients'");
		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				finish();
				Toast.makeText(getApplicationContext(), "Now scroll down and click 'Find Clients' listed below..",
						Toast.LENGTH_LONG).show();
			}
		});
		alertDialog.show();
	}

	public void Call() {
		Vibrator vibr = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		vibr.vibrate(100);
		Intent i = new Intent(this, L22ContactClientList.class);
		startActivity(i);
	}

	public void View() {
		Vibrator vibr = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		vibr.vibrate(100);
		Intent i = new Intent(this, L24ViewClientList.class);
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
