package com.example.androlawyer.client;

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
import android.text.Html;
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

public class ClientC4MapSearch extends Activity {
	public String L_details[], L_lat[], L_lng[];
	EditText searchview;
	TextView txtadress, txtLT, txtLG, txtfaddr, txtNLawyers;
	Button btnconfirm, btnSearch;
	String searchtxt;
	String MARKER_CLICK_CID, MARKER_CLICK_CNAME;
	double home_long, home_lat;
	double LT, LG;
	int j = 0;
	String NEXTPREV[];
	String FADDR, tag;
	GoogleMap map;
	String addressText;
	LatLng latLng, LatLngfrommarker;
	MarkerOptions markerOptions;
	SharedPreferences mypref;
	Editor edit;
	String NXTPRVLNAME="";
	RelativeLayout r;
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
		setContentView(R.layout.activity_client_c4_map_search);
		// disp();
		ActionBar bar = getActionBar();
		ColorDrawable c = new ColorDrawable(Color.parseColor("#03A9F4"));
		bar.setBackgroundDrawable(c);
		// bar.setTitle(Html.fromHtml("<font color='#ffffff'>Find
		// Lawyer</font>"));
		bar.setTitle("Find Lawyer");
		bar.setIcon(android.R.color.transparent);
		bar.setDisplayHomeAsUpEnabled(true);

		Bundle bundleCT = getIntent().getExtras();
		if (bundleCT != null) {
			tag = (String) bundleCT.get("CaseType");
		}

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		SharedPreferences ip;
		ip = getSharedPreferences("IPaddr", Context.MODE_PRIVATE);
		changedIP = ip.getString("IP", null);

		// imgDetails = (ImageButton) findViewById(R.id.imgDETAILSclient);
		// imgDetails.setVisibility(View.INVISIBLE);

		progress = new ProgressDialog(this);
		progressmarker = new ProgressDialog(this);
		progress.setMessage("Loading Map..");
		progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progress.setCancelable(false);
		progress.setCanceledOnTouchOutside(false);
		progress.show();
		r = (RelativeLayout) findViewById(R.id.RLMapSearchclient);
		r.setVisibility(View.INVISIBLE);

		getLawyerDetailsMAPlat(tag);
		getLawyerDetailsMAPlng(tag);
		
		if ((progresscondition.equals("AB")) || (progresscondition.equals("BA"))) {
			Handler handler = new Handler();
			handler.postDelayed(new Runnable() {

				@Override
				public void run() {
					// Hide your View after 3 seconds
					progress.dismiss();
					r.setVisibility(View.VISIBLE);
					if (L_lat[0].equals("NULL")) {
						txtNLawyers = (TextView) findViewById(R.id.TXTNOLAWYERS);
						txtNLawyers.setVisibility(View.VISIBLE);
						markerplotNOPLOT("10.1209924460876", "76.3471336662769");
						Toast.makeText(getApplicationContext(), "Sorry \nNo lawyers currently available",
								Toast.LENGTH_SHORT).show();
					} else {
						// Toast.makeText(getApplicationContext(), "Click
						// Search..", Toast.LENGTH_SHORT).show();
						for (int i = 0; i < L_lat.length; i++) {
							// System.out.println("latlatlatlatlatlat"+C_lat[i]);
							// System.out.println("lnglnglnglnglnglng"+C_lng[i]);
							j = i;
							markerplot(L_lat[i], L_lng[i]);
						}
					}
				}
			}, 1000);
		}

		searchview = (EditText) findViewById(R.id.edtSearchclient);
		txtadress = (TextView) findViewById(R.id.txtAddressclient);
		txtLT = (TextView) findViewById(R.id.txtLTclient);
		txtLG = (TextView) findViewById(R.id.txtLGclient);
		txtfaddr = (TextView) findViewById(R.id.txtFAddrclient);
		btnconfirm = (Button) findViewById(R.id.bttnConfirmclient);
		btnSearch = (Button) findViewById(R.id.bttnSearchclient);

		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			searchtxt = (String) bundle.get("iaddress");
			searchview.setText(searchtxt);
		}

		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.clientmapclient)).getMap();
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
				getLawyerDetailsMAP_nointent(markerlat, markerlng);
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
		// progressmarker.setMessage("Loading Lawyer Details..");
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
		// getLawyerDetailsMAP(markerlat, markerlng);
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

	public void NEXT(View v) {
		if (j == L_lat.length - 1) {
			j = 0;
		} else {
			j++;
		}
		getLNAMEthroughNXTPRV(L_lat[j], L_lng[j]);
		markerplotNXTPRV(L_lat[j], L_lng[j], j);
	}

	public void PREV(View v) {
		if (j == 0) {
			j = L_lat.length - 1;
		} else {
			j--;
		}
		getLNAMEthroughNXTPRV(L_lat[j], L_lng[j]);
		markerplotNXTPRV(L_lat[j], L_lng[j], j);
	}

	public void showInfoWindow() {
		markerlat = "" + LatLngfrommarker.latitude;
		markerlng = "" + LatLngfrommarker.longitude;
		getLawyerDetailsMAPMarkerWindow(markerlat, markerlng);
	}

	public void getLawyerDetailsMAPlat(String tag) {

		int i = 0;

		try {
			HttpClient client = new DefaultHttpClient();
			HttpPost request = new HttpPost("http://" + changedIP + ":8080/AndroLawyer/actLawyerDetailsGetAllLAT.jsp");
			List<NameValuePair> PostParameters = new ArrayList<NameValuePair>();
			PostParameters.add(new BasicNameValuePair("tag", tag));
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
			L_lat = result.split("\\*");
		} catch (Exception e) {
			e.printStackTrace();
			progresscondition = "A";
			alert.showAlertDialog(this, null, "Please Turn Ur Internet Connection ON", false);
		}
	}

	public void getLawyerDetailsMAPlng(String tag) {

		int i = 0;

		try {
			HttpClient client = new DefaultHttpClient();
			HttpPost request = new HttpPost("http://" + changedIP + ":8080/AndroLawyer/actLawyerDetailsGetAllLNG.jsp");
			List<NameValuePair> PostParameters = new ArrayList<NameValuePair>();
			PostParameters.add(new BasicNameValuePair("tag", tag));
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
			L_lng = result.split("\\*");
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
		marker.setTitle(j+1+"");
		getLNAMEthroughNXTPRV(plotLAT,plotLNG);
		marker.setSnippet(NXTPRVLNAME);
		marker.showInfoWindow();
		map.moveCamera(CameraUpdateFactory.newLatLng(dplotmarker));
		map.animateCamera(CameraUpdateFactory.zoomTo(10));
	}

	public void markerplotNXTPRV(String plotLAT, String plotLNG, int j) {
		Double dplotLAT = Double.parseDouble(plotLAT);
		Double dplotLNG = Double.parseDouble(plotLNG);
		LatLng dplotmarker = new LatLng(dplotLAT, dplotLNG);
		Marker marker = map.addMarker(new MarkerOptions().position(dplotmarker));

		marker.setTitle(j+1+"");
		marker.setSnippet(L_details[1]);
		marker.showInfoWindow();
		
		map.moveCamera(CameraUpdateFactory.newLatLng(dplotmarker));
		map.animateCamera(CameraUpdateFactory.zoomTo(11));
	}

	public void markerplotNOPLOT(String plotLAT, String plotLNG) {
		Double dplotLAT2 = Double.parseDouble(plotLAT);
		Double dplotLNG2 = Double.parseDouble(plotLNG);
		LatLng dplotmarker2 = new LatLng(dplotLAT2, dplotLNG2);
		map.moveCamera(CameraUpdateFactory.newLatLng(dplotmarker2));
		map.animateCamera(CameraUpdateFactory.zoomTo(0));
	}

	public void getLawyerDetailsMAP_nointent(String sendingLAT, String sendingLNG) {

		int i = 0;

		try {
			HttpClient client = new DefaultHttpClient();
			HttpPost request = new HttpPost("http://" + changedIP + ":8080/AndroLawyer/actLawyerDetailsGetMap.jsp");
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
			L_details = result.split("\\*");
			MARKER_CLICK_CID = L_details[0];
			MARKER_CLICK_CNAME = L_details[1];
			System.out.println("MAPMAPMAPMAP" + L_details[0]);
			progressmarker.dismiss();
			// Toast.makeText(this, result, Toast.LENGTH_LONG).show();
		} catch (Exception e) {
			e.printStackTrace();
			progresscondition2 = "C";
			alert.showAlertDialog(this, null, "Please Turn Ur Internet Connection ON", false);
		}
	}

	public void getLawyerDetailsMAPMarkerWindow(String sendingLAT, String sendingLNG) {

		int i = 0;

		try {
			HttpClient client = new DefaultHttpClient();
			HttpPost request = new HttpPost("http://" + changedIP + ":8080/AndroLawyer/actLawyerDetailsGetMap.jsp");
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
			String L_detailsMW[] = result.split("\\*");
			String MARKER_CLICK_LNAME_MW = L_detailsMW[1];
			String MARKER_CLICK_LPHN_MW = L_detailsMW[5];
			String MARKER_CLICK_LID_MW = L_detailsMW[0];
			progressmarker.dismiss();
			Intent to = new Intent(getApplicationContext(), MarkerWindowClient.class);
			to.putExtra("lawyername", MARKER_CLICK_LNAME_MW);
			to.putExtra("lawyerphn", MARKER_CLICK_LPHN_MW);
			to.putExtra("lawyerid", MARKER_CLICK_LID_MW);
			startActivity(to);
			// Toast.makeText(this, result, Toast.LENGTH_LONG).show();
		} catch (Exception e) {
			e.printStackTrace();
			progresscondition2 = "C";
			alert.showAlertDialog(this, null, "Please Turn Ur Internet Connection ON", false);
		}
	}

	public void getLNAMEthroughNXTPRV(String sendingLAT,String sendingLNG)
	{
		try {
			HttpClient client = new DefaultHttpClient();
			HttpPost request = new HttpPost("http://" + changedIP + ":8080/AndroLawyer/actLawyerDetailsGetMap.jsp");
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
			L_details = result.split("\\*");
			NXTPRVLNAME = L_details[1];
			// Toast.makeText(this, result, Toast.LENGTH_LONG).show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void getLawyerDetailsMAP(String sendingLAT, String sendingLNG) {

		int i = 0;

		try {
			HttpClient client = new DefaultHttpClient();
			HttpPost request = new HttpPost("http://" + changedIP + ":8080/AndroLawyer/actLawyerDetailsGetMap.jsp");
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
			L_details = result.split("\\*");
			MARKER_CLICK_CID = L_details[0];
			MARKER_CLICK_CNAME = L_details[1];
			System.out.println("MAPMAPMAPMAP" + L_details[0]);
			progressmarker.dismiss();
			Intent to = new Intent(getApplicationContext(), ClientC4MapLawyer.class);
			to.putExtra("lawyerid", L_details[0]);
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
