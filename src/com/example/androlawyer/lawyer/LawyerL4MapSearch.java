package com.example.androlawyer.lawyer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

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
import com.example.androlawyer.client.MarkerWindowClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class LawyerL4MapSearch extends Activity {
	public String C_details[], C_lat[], C_lng[];
	EditText searchview;
	TextView txtadress, txtLT, txtLG, txtfaddr;
	Button btnconfirm, btnSearch;
	String searchtxt;
	String MARKER_CLICK_CID, MARKER_CLICK_CNAME;
	Double home_long, home_lat;
	double LT, LG;
	String FADDR;
	String NXTPRVCNAME = "";
	GoogleMap map;
	String addressText;
	LatLng latLng, LatLngfrommarker;
	MarkerOptions markerOptions;
	SharedPreferences mypref;
	Editor edit;
	RelativeLayout r;

	int j = 0;

	ProgressDialog progress, progressmarker;
	String progresscondition = "";
	String progresscondition2 = "";
	String changedIP = "";
	String markerlat, markerlng, markertitle, markersnippet;
	AlertDialogManager alert = new AlertDialogManager();

	// int width, height;
	@Override
	public void onBackPressed() {
		this.finish();
		overridePendingTransition(R.anim.animation_enter, R.anim.animation_leave);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lawyer_l4_mapsearch);
		// disp();
		ActionBar bar = getActionBar();
		ColorDrawable c = new ColorDrawable(Color.parseColor("#03A9F4"));
		bar.setBackgroundDrawable(c);
		bar.setTitle("Find Client");
		bar.setIcon(android.R.color.transparent);
		bar.setDisplayHomeAsUpEnabled(true);

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		SharedPreferences ip;
		ip = getSharedPreferences("IPaddr", Context.MODE_PRIVATE);
		changedIP = ip.getString("IP", null);

		// imgDetails = (ImageButton) findViewById(R.id.imgDETAILSlawyer);
		// imgDetails.setVisibility(View.INVISIBLE);

		progress = new ProgressDialog(this);
		progressmarker = new ProgressDialog(this);
		progress.setMessage("Loading Map..");
		progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progress.setCancelable(false);
		progress.setCanceledOnTouchOutside(false);
		progress.show();
		r = (RelativeLayout) findViewById(R.id.RLMapSearchlawyer);
		r.setVisibility(View.INVISIBLE);

		getClientDetailsMAPlat();
		getClientDetailsMAPlng();

		if ((progresscondition.equals("AB")) || (progresscondition.equals("BA"))) {
			Handler handler = new Handler();
			handler.postDelayed(new Runnable() {

				@Override
				public void run() {
					// Hide your View after 3 seconds
					progress.dismiss();
					r.setVisibility(View.VISIBLE);
					// if(C_lat.length==1)
					// {
					// nothing();
					// }
					for (int i = 0; i < C_lat.length; i++) {
						// System.out.println("latlatlatlatlatlat"+C_lat[i]);
						// System.out.println("lnglnglnglnglnglng"+C_lng[i]);
						markerplot(C_lat[i], C_lng[i]);
						j = i;
					}
				}
			}, 1000);
		}

		searchview = (EditText) findViewById(R.id.edtSearchlawyer);
		txtadress = (TextView) findViewById(R.id.txtAddresslawyer);
		txtLT = (TextView) findViewById(R.id.txtLTlawyer);
		txtLG = (TextView) findViewById(R.id.txtLGlawyer);
		txtfaddr = (TextView) findViewById(R.id.txtFAddrlawyer);
		btnconfirm = (Button) findViewById(R.id.bttnConfirmlawyer);
		btnSearch = (Button) findViewById(R.id.bttnSearchlawyer);

		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			searchtxt = (String) bundle.get("iaddress");
			searchview.setText(searchtxt);
			Toast.makeText(getApplicationContext(), "Click Search..", Toast.LENGTH_SHORT).show();
		}

		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.lawyermaplawyer)).getMap();
		map.setMyLocationEnabled(true);
		// LatLng frstmarkr = new LatLng(10.0326519, 76.3262801);
		// Marker marker = map.addMarker(new
		// MarkerOptions().position(frstmarkr).title("Akhil Tulla")
		// .snippet("Choose me!! I hav got Button Click"));
		// Marker marker2 = map.addMarker(new MarkerOptions().position(new
		// LatLng(20.0326519, 66.3262801)).title("Jeslin")
		// .snippet("Choose me!!"));
		// Marker marker3 = map.addMarker(
		// new MarkerOptions().position(new LatLng(30.0326519,
		// 76.3262801)).title("Rahul").snippet("Choose me!!"));
		// Marker marker4 = map.addMarker(
		// new MarkerOptions().position(new LatLng(40.0326519,
		// 86.3262801)).title("Bibin").snippet("Choose me!!"));
		// map.moveCamera(CameraUpdateFactory.newLatLng(frstmarkr));
		map.animateCamera(CameraUpdateFactory.zoomTo(12));
		map.getUiSettings().setMyLocationButtonEnabled(true);
		map.getUiSettings().setZoomControlsEnabled(true);
		map.getUiSettings().setCompassEnabled(true);
		map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		map.getUiSettings().setRotateGesturesEnabled(true);
		map.getUiSettings().setTiltGesturesEnabled(true);
		map.setOnMarkerClickListener(new OnMarkerClickListener() {

			@Override
			public boolean onMarkerClick(Marker mrkrfrommap) {
				LatLngfrommarker = mrkrfrommap.getPosition();
				markerlat = "" + LatLngfrommarker.latitude;
				markerlng = "" + LatLngfrommarker.longitude;
				getClientDetailsMAP_nointent(markerlat, markerlng);
				// mrkrfrommap.setTitle(MARKER_CLICK_CID);
				mrkrfrommap.setSnippet(MARKER_CLICK_CNAME);
				// imgDetails.setVisibility(View.VISIBLE);
				String mrkronclcksnippet = mrkrfrommap.getSnippet();
				String mrkronclcktitle = mrkrfrommap.getTitle();
				// Toast.makeText(getApplicationContext(),"ha",
				// Toast.LENGTH_SHORT).show();
				// mrkrfrommap.showInfoWindow();
				showInfoWindow();
				return false;
			}
		});

		map.setOnMapClickListener(new OnMapClickListener() {
			@Override
			public void onMapClick(LatLng point) {
				// TODO Auto-generated method stub
				// imgDetails.setVisibility(View.INVISIBLE);
			}
		});

		// imgDetails.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// progressmarker.setMessage("Loading Clients Details..");
		// progressmarker.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		// progressmarker.setCancelable(false);
		// progressmarker.setCanceledOnTouchOutside(false);
		// progressmarker.show();
		// markerlat = "" + LatLngfrommarker.latitude;
		// markerlng = "" + LatLngfrommarker.longitude;
		// // markertitle = mrkrfrommap.getTitle();
		// // markersnippet = mrkrfrommap.getSnippet();
		// // txtLT.setText("" + markerlat);
		// // txtLG.setText("" + markerlng);
		// getClientDetailsMAP(markerlat, markerlng);
		// }
		// });

		// btnconfirm.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// // Toast.makeText(getApplicationContext(), "Confirmed",
		// // Toast.LENGTH_SHORT).show();
		// try {
		// LT = Double.parseDouble(txtLT.getText().toString());
		// LG = Double.parseDouble(txtLG.getText().toString());
		// FADDR = txtfaddr.getText().toString();
		// mypref = getSharedPreferences("locationlreg", Context.MODE_PRIVATE);
		// edit = mypref.edit();
		// edit.putString("lat", "" + LT);
		// edit.putString("lng", "" + LG);
		// edit.putBoolean("flag", true);
		// edit.commit();
		// // Toast.makeText(getApplicationContext(), "Latitude &
		// // Longitude Picked", Toast.LENGTH_SHORT).show();
		// Toast.makeText(getApplicationContext(), "Selected Client Picked",
		// Toast.LENGTH_SHORT).show();
		// LawyerL4MapSearch.this.finish();
		// overridePendingTransition(R.anim.animation_enter,
		// R.anim.animation_leave);
		// // GetAddress(LT, LG);
		// Log.d("LT", "" + LT);
		// Log.d("LG", "" + LG);
		// Log.d("FADDR", FADDR);
		// } catch (Exception e) {
		// Toast.makeText(getApplicationContext(), "Pick a client and click
		// confirm..", Toast.LENGTH_SHORT)
		// .show();
		// }
		// }
		// });
		btnSearch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// Toast.makeText(getApplicationContext(),"WOW",
				// Toast.LENGTH_SHORT).show();
				// Log.d("RandomNo:", ""+randomgenerator(10));
				// Intent i=new Intent(getApplicationContext(), Find.class);
				// startActivity(i);
				String g = searchview.getText().toString().trim();
				if ((g.equals("")) || (g.equals(", , ,"))) {
					Toast.makeText(getApplicationContext(), "Type an address!!", Toast.LENGTH_SHORT).show();
				} else {
					Geocoder geocoder = new Geocoder(getBaseContext());
					List<Address> addresses = null;
					try {
						// Getting a maximum of 3 Address that matches the input
						// text
						addresses = geocoder.getFromLocationName(g, 3);
						if (addresses != null && !addresses.equals(""))
							search(addresses);
					} catch (Exception e) {
					}
				}
			}
		});
	}

	public void getClientDetailsMAPMarkerWindow(String sendingLAT, String sendingLNG) {

		int i = 0;

		try {
			HttpClient client = new DefaultHttpClient();
			HttpPost request = new HttpPost("http://" + changedIP + ":8080/AndroLawyer/actClientDetailsGetMap.jsp");
			List<NameValuePair> PostParameters = new ArrayList<NameValuePair>();
			PostParameters.add(new BasicNameValuePair("lat", sendingLAT));
			PostParameters.add(new BasicNameValuePair("lng", sendingLNG));
			UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(PostParameters);
			request.setEntity(formEntity);

			// System.out.println("1111111111111111111111111111111111111");
			HttpResponse response = client.execute(request);
			// System.out.println("2222222222222222222222222222222222222");
			BufferedReader obj = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			StringBuffer sb = new StringBuffer("");
			String Line = "";
			while ((Line = obj.readLine()) != null) {
				sb.append(Line);
			}
			obj.close();
			// System.out.println("3333333333333333333333333333333333333");
			String result = sb.toString().trim();
			String C_detailsMW[] = result.split("\\*");
			String MARKER_CLICK_CNAME_MW = C_detailsMW[1];
			String MARKER_CLICK_CPHN_MW = C_detailsMW[5];
			String MARKER_CLICK_CID_MW = C_detailsMW[0];
			// System.out.println("4444444444444444444444444444444444444");
			Intent to = new Intent(getApplicationContext(), MarkerWindowLawyer.class);
			to.putExtra("clientname", MARKER_CLICK_CNAME_MW);
			to.putExtra("clientphn", MARKER_CLICK_CPHN_MW);
			to.putExtra("clientid", MARKER_CLICK_CID_MW);
			startActivity(to);
			// Toast.makeText(this, result, Toast.LENGTH_LONG).show();
		} catch (Exception e) {
			e.printStackTrace();
			progresscondition2 = "C";
			alert.showAlertDialog(this, null, "Please Turn Ur Internet Connection ON", false);
		}
	}

	public void showInfoWindow() {
		markerlat = "" + LatLngfrommarker.latitude;
		markerlng = "" + LatLngfrommarker.longitude;
		getClientDetailsMAPMarkerWindow(markerlat, markerlng);
		// System.out.println("00000000000000000000000000000000");

	}

	public void NEXT(View v) {
		if (j == C_lat.length - 1) {
			j = 0;
		} else {
			j++;
		}
		getCNAMEthroughNXTPRV(C_lat[j], C_lng[j]);
		markerplotNXTPRV(C_lat[j], C_lng[j], j);
	}

	public void PREV(View v) {
		if (j == 0) {
			j = C_lat.length - 1;
		} else {
			j--;
		}
		getCNAMEthroughNXTPRV(C_lat[j], C_lng[j]);
		markerplotNXTPRV(C_lat[j], C_lng[j], j);
	}

	public void nothing() {
		Toast.makeText(this, "No Clients registered...", Toast.LENGTH_SHORT).show();
		finish();
	}

	public void markerplotNXTPRV(String plotLAT, String plotLNG, int j) {
		Double dplotLAT = Double.parseDouble(plotLAT);
		Double dplotLNG = Double.parseDouble(plotLNG);
		LatLng dplotmarker = new LatLng(dplotLAT, dplotLNG);
		Marker marker = map.addMarker(new MarkerOptions().position(dplotmarker));

		marker.setTitle(j + 1 + "");
		marker.setSnippet(C_details[1]);
		marker.showInfoWindow();

		map.moveCamera(CameraUpdateFactory.newLatLng(dplotmarker));
		map.animateCamera(CameraUpdateFactory.zoomTo(11));
	}

	public void getCNAMEthroughNXTPRV(String sendingLAT, String sendingLNG) {
		try {
			HttpClient client = new DefaultHttpClient();
			HttpPost request = new HttpPost("http://" + changedIP + ":8080/AndroLawyer/actClientDetailsGetMap.jsp");
			List<NameValuePair> PostParameters = new ArrayList<NameValuePair>();
			PostParameters.add(new BasicNameValuePair("lat", sendingLAT));
			PostParameters.add(new BasicNameValuePair("lng", sendingLNG));
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
			C_details = result.split("\\*");
			NXTPRVCNAME = C_details[1];
			// Toast.makeText(this, result, Toast.LENGTH_LONG).show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void getClientDetailsMAPlat() {

		int i = 0;

		try {
			HttpClient client = new DefaultHttpClient();
			HttpPost request = new HttpPost("http://" + changedIP + ":8080/AndroLawyer/actClientDetailsGetAllLAT.jsp");
			List<NameValuePair> PostParameters = new ArrayList<NameValuePair>();
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
			// System.out.println("latlatlatlatlatlat"+result);
			progresscondition = "A";
			C_lat = result.split("\\*");
		} catch (Exception e) {
			e.printStackTrace();
			progresscondition = "A";
			alert.showAlertDialog(this, null, "Please Turn Ur Internet Connection ON", false);
		}
	}

	public void getClientDetailsMAPlng() {

		int i = 0;

		try {
			HttpClient client = new DefaultHttpClient();
			HttpPost request = new HttpPost("http://" + changedIP + ":8080/AndroLawyer/actClientDetailsGetAllLNG.jsp");
			List<NameValuePair> PostParameters = new ArrayList<NameValuePair>();
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
			System.out.println("lnglnglnglnglnglng" + result);
			progresscondition = progresscondition + "B";
			C_lng = result.split("\\*");
		} catch (Exception e) {
			e.printStackTrace();
			progresscondition = progresscondition + "B";
			alert.showAlertDialog(this, null, "Please Turn Ur Internet Connection ON", false);
		}
	}

	public void markerplot(String plotLAT, String plotLNG) {
		Double dplotLAT = Double.parseDouble(plotLAT);
		Double dplotLNG = Double.parseDouble(plotLNG);
		LatLng dplotmarker = new LatLng(dplotLAT, dplotLNG);
		Marker marker = map.addMarker(new MarkerOptions().position(dplotmarker));
		// marker.setTitle(j+1+"");
		getCNAMEthroughNXTPRV(plotLAT, plotLNG);
		marker.setSnippet(NXTPRVCNAME);
		marker.showInfoWindow();
		map.moveCamera(CameraUpdateFactory.newLatLng(dplotmarker));
		map.animateCamera(CameraUpdateFactory.zoomTo(10));
	}

	public void getClientDetailsMAP_nointent(String sendingLAT, String sendingLNG) {

		int i = 0;

		try {
			HttpClient client = new DefaultHttpClient();
			HttpPost request = new HttpPost("http://" + changedIP + ":8080/AndroLawyer/actClientDetailsGetMap.jsp");
			List<NameValuePair> PostParameters = new ArrayList<NameValuePair>();
			PostParameters.add(new BasicNameValuePair("lat", sendingLAT));
			PostParameters.add(new BasicNameValuePair("lng", sendingLNG));
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
			progresscondition2 = "C";
			C_details = result.split("\\*");
			MARKER_CLICK_CID = C_details[0];
			MARKER_CLICK_CNAME = C_details[1];
			System.out.println("MAPMAPMAPMAP" + C_details[0]);
			progressmarker.dismiss();
			// Toast.makeText(this, result, Toast.LENGTH_LONG).show();
		} catch (Exception e) {
			e.printStackTrace();
			progresscondition2 = "C";
			alert.showAlertDialog(this, null, "Please Turn Ur Internet Connection ON", false);
		}
	}

	public void getClientDetailsMAP(String sendingLAT, String sendingLNG) {

		int i = 0;

		try {
			HttpClient client = new DefaultHttpClient();
			HttpPost request = new HttpPost("http://" + changedIP + ":8080/AndroLawyer/actClientDetailsGetMap.jsp");
			List<NameValuePair> PostParameters = new ArrayList<NameValuePair>();
			PostParameters.add(new BasicNameValuePair("lat", sendingLAT));
			PostParameters.add(new BasicNameValuePair("lng", sendingLNG));
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
			progresscondition2 = "C";
			C_details = result.split("\\*");
			MARKER_CLICK_CID = C_details[0];
			MARKER_CLICK_CNAME = C_details[1];
			System.out.println("MAPMAPMAPMAP" + C_details[0]);
			progressmarker.dismiss();
			Intent to = new Intent(getApplicationContext(), LawyerL4MapClient.class);
			to.putExtra("clientid", C_details[0]);
			startActivity(to);
			// Toast.makeText(this, result, Toast.LENGTH_LONG).show();
		} catch (Exception e) {
			e.printStackTrace();
			progresscondition2 = "C";
			alert.showAlertDialog(this, null, "Please Turn Ur Internet Connection ON", false);
		}
	}

	// public void alert() {
	// alert.showAlertDialog(this, null, markerlat + ", " + markerlng + ", " +
	// markertitle + ", " + markersnippet,
	// false);
	// }

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
		// markerOptions = new MarkerOptions();
		// // MarkerOptions markerOptions=new
		// // MarkerOptions().position(latLng).title("You are
		// //
		// here!").snippet(""+latLng).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
		// // //change color as your need
		// markerOptions.position(latLng);
		// markerOptions.title(city);
		// // markerOptions.draggable(true);
		// markerOptions.snippet(addressText);
		// markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
		//
		// map.addMarker(markerOptions);
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
		return color[randomgenerator(11)];
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
	// @Override
	// public boolean onOptionsItemSelected(MenuItem item) {
	// switch (item.getItemId()) {
	// case android.R.id.home:
	// Toast.makeText(this, "Pick a client and click confirm..",
	// Toast.LENGTH_SHORT).show();
	// return true;
	// default:
	// return super.onOptionsItemSelected(item);
	// }
	// }
	// public boolean onKeyDown(int keycode, KeyEvent event) {
	// // if clicked button code is equal to menu button code
	// // then the if condition will turn true
	// // To perform action on another button just write keyEvent
	// // followed by dot(.) and press CTRL+Space and you will get all
	// // the code options. Select anyone and perform your action.
	// if (keycode == KeyEvent.KEYCODE_BACK) {
	// Toast.makeText(this, "Pick a client and click confirm..",
	// Toast.LENGTH_SHORT).show();
	// return true;
	// }
	// return super.onKeyDown(keycode, event);
	// }
	//
	// public boolean onTouchEvent(MotionEvent event) {
	//
	// if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
	// Toast.makeText(this, "Pick a client and click confirm..",
	// Toast.LENGTH_SHORT).show();
	// }
	// return false;
	// }
}
