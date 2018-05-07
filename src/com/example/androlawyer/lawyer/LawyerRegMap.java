package com.example.androlawyer.lawyer;

import java.util.List;
import java.util.Locale;
import java.util.Random;

import com.example.androlawyer.R;
import com.example.androlawyer.R.id;
import com.example.androlawyer.R.layout;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LawyerRegMap extends Activity {
	EditText searchview;
	TextView txtadress, txtLT, txtLG, txtfaddr;
	Button btnconfirm, btnSearch;
	String searchtxt;
	Double home_long, home_lat;
	double LT, LG;
	String FADDR;
	GoogleMap map;
	String addressText;
	LatLng latLng;
	MarkerOptions markerOptions;
	SharedPreferences mypref;
	Editor edit;
	int width, height;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lawyer_reg_map);
		disp();
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		ActionBar bar = getActionBar();
		ColorDrawable c = new ColorDrawable(Color.parseColor("#03A9F4"));
		bar.setBackgroundDrawable(c);
		bar.setTitle("Address Picker");
		bar.setIcon(android.R.color.transparent);
		bar.setDisplayHomeAsUpEnabled(true);
		searchview = (EditText) findViewById(R.id.edtSearchlawyerreg);
		txtadress = (TextView) findViewById(R.id.txtAddresslawyerreg);
		txtLT = (TextView) findViewById(R.id.txtLTlawyerreg);
		txtLG = (TextView) findViewById(R.id.txtLGlawyerreg);
		txtfaddr = (TextView) findViewById(R.id.txtFAddrlawyerreg);
		btnconfirm = (Button) findViewById(R.id.bttnConfirmlawyerreg);
		btnSearch = (Button) findViewById(R.id.bttnSearchlawyerreg);

		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			searchtxt = (String) bundle.get("iaddress");
			searchview.setText(searchtxt);
			Toast.makeText(getApplicationContext(), "Click Search..", Toast.LENGTH_SHORT).show();
		}

		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.maplawyerreg)).getMap();
		map.setMyLocationEnabled(true);
		map.getUiSettings().setMyLocationButtonEnabled(true);
		map.getUiSettings().setZoomControlsEnabled(true);
		map.getUiSettings().setCompassEnabled(true);
		map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		map.getUiSettings().setRotateGesturesEnabled(true);
		map.getUiSettings().setTiltGesturesEnabled(true);
		map.setOnMapClickListener(new OnMapClickListener() {

			@Override
			public void onMapClick(LatLng point) {
				// TODO Auto-generated method stub
				map.clear();
				home_lat = point.latitude;
				home_long = point.longitude;
				LatLng Place = new LatLng(home_lat, home_long);
				markerOptions = new MarkerOptions();
				markerOptions.position(Place);
				markerOptions.title("Flag");
				markerOptions.snippet("" + Place);
				Float f = colors();
				markerOptions.icon(BitmapDescriptorFactory.defaultMarker(f));
				map.addMarker(markerOptions);
				txtadress.setText("Latitude:" + home_lat + ", Longitude:" + home_long);
				txtLT.setText("" + home_lat);
				txtLG.setText("" + home_long);
				GetAddress(home_lat, home_long);
			}
		});
		btnconfirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// Toast.makeText(getApplicationContext(), "Confirmed",
				// Toast.LENGTH_SHORT).show();
				try {
					LT = Double.parseDouble(txtLT.getText().toString());
					LG = Double.parseDouble(txtLG.getText().toString());
					FADDR = txtfaddr.getText().toString();
					mypref = getSharedPreferences("locationlreg", Context.MODE_PRIVATE);
					edit = mypref.edit();
					edit.putString("lat", "" + LT);
					edit.putString("lng", "" + LG);
					edit.putString("flagmap","MAP");
					edit.putBoolean("flag", true);
					edit.commit();
					Toast.makeText(getApplicationContext(), "Latitude & Longitude Picked", Toast.LENGTH_SHORT).show();
					LawyerRegMap.this.finish();
					// GetAddress(LT, LG);
					Log.d("LT", "" + LT);
					Log.d("LG", "" + LG);
					Log.d("FADDR", FADDR);
				} catch (Exception e) {
					Toast.makeText(getApplicationContext(), "Pick an address and click confirm address..",
							Toast.LENGTH_SHORT).show();
				}
			}
		});
		btnSearch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// Toast.makeText(getApplicationContext(),"WOW",
				// Toast.LENGTH_SHORT).show();
				// Log.d("RandomNo:", ""+randomgenerator(10));
				// Intent i=new Intent(getApplicationContext(), Find.class);
				// startActivity(i);
				String g = searchview.getText().toString().trim();
				if((g.equals(""))||(g.equals(", , ,")))
				{
					Toast.makeText(getApplicationContext(), "Type an address!!",
							Toast.LENGTH_SHORT).show();
				}
				else
				{
				Geocoder geocoder = new Geocoder(getBaseContext());
				List<Address> addresses = null;
				try 
				{
					// Getting a maximum of 3 Address that matches the input
					// text
					addresses = geocoder.getFromLocationName(g, 3);
					if (addresses != null && !addresses.equals(""))
						search(addresses);
				} 
				catch (Exception e) 
				{
				}
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

	protected void search(List<Address> addresses) {

		Address address = (Address) addresses.get(0);
		home_long = address.getLongitude();
		home_lat = address.getLatitude();
		latLng = new LatLng(address.getLatitude(), address.getLongitude());
		String city = address.getLocality();
		String state = address.getAdminArea();
		// String country = address.getCountryName();
		String postalCode = address.getPostalCode();
		addressText = String.format("%s, %s, %s, %s",
				address.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0) : "", city, state, postalCode);
		Toast.makeText(getApplicationContext(), addressText, Toast.LENGTH_LONG).show();
		markerOptions = new MarkerOptions();
		// MarkerOptions markerOptions=new
		// MarkerOptions().position(latLng).title("You are
		// here!").snippet(""+latLng).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
		// //change color as your need
		markerOptions.position(latLng);
		markerOptions.title(city);
		// markerOptions.draggable(true);
		markerOptions.snippet(addressText);
		markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));

		map.clear();
		map.addMarker(markerOptions);
		map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
		map.animateCamera(CameraUpdateFactory.zoomTo(12));

		txtadress.setText("Latitude:" + address.getLatitude() + ", Longitude:" + address.getLongitude());
		txtLT.setText("" + address.getLatitude());
		txtLG.setText("" + address.getLongitude());
		Log.d("LTSearch:", "" + address.getLatitude());
		Log.d("LGSearch:", "" + address.getLongitude());
		Log.d("LAddr:", "" + addressText);
		txtfaddr.setText(addressText);
		// GetAddress(address.getLatitude(), address.getLongitude());
	}

	public void GetAddress(double a, double b) {
		try {
			Geocoder geocoder;
			List<Address> addresses;
			geocoder = new Geocoder(this, Locale.getDefault());

			addresses = geocoder.getFromLocation(a, b, 5); // Here
															// 1
															// represent
															// max
															// location
															// result
															// to
															// returned,
															// by
															// documents
															// it
															// recommended
															// 1
															// to
															// 5

			String address = addresses.get(0).getAddressLine(0); // If any
																	// additional
																	// address
																	// line
																	// present
																	// than
																	// only,
																	// check
																	// with max
																	// available
																	// address
																	// lines by
																	// getMaxAddressLineIndex()
			String city = addresses.get(0).getLocality();
			String state = addresses.get(0).getAdminArea();
			String country = addresses.get(0).getCountryName();
			String postalCode = addresses.get(0).getPostalCode();
			// String knownName = addresses.get(0).getFeatureName(); // Only if
			// available else return NULL
			txtfaddr.setText(address + "," + city + ", " + state + ", " + country + ", " + postalCode);
			Log.d("Laddr", "" + txtfaddr.getText().toString());
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(this, "Cannot get address", Toast.LENGTH_SHORT).show();
		}
	}

	public int randomgenerator(int a) {
		// note a single Random object is reused here
		Random randomGenerator = new Random();
		int randomInt = randomGenerator.nextInt(a);
		return randomInt;

	}

	public float colors() {
		float[] color = { 210, 240, 180, 120, 300, 30, 0, 330, 270, 60 };
		return color[randomgenerator(10)];
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			Toast.makeText(this, "Pick an address and click confirm address..", Toast.LENGTH_SHORT).show();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public boolean onKeyDown(int keycode, KeyEvent event) {
		// if clicked button code is equal to menu button code
		// then the if condition will turn true
		// To perform action on another button just write keyEvent
		// followed by dot(.) and press CTRL+Space and you will get all
		// the code options. Select anyone and perform your action.
		if (keycode == KeyEvent.KEYCODE_BACK) {
			Toast.makeText(this, "Pick an address and click confirm address..", Toast.LENGTH_SHORT).show();
			return true;
		}
		return super.onKeyDown(keycode, event);
	}

	public boolean onTouchEvent(MotionEvent event) {

		if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
			Toast.makeText(this, "Pick an address and click confirm address..", Toast.LENGTH_SHORT).show();
		}
		return false;
	}
}
