package com.example.androlawyer.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.example.androlawyer.AlertDialogManager;
import com.example.androlawyer.CustomListAdapterChat;
import com.example.androlawyer.R;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.Toast;

public class ClientC3Chat extends Activity {

	TableLayout tb;
	Button bt_send;
	EditText ed_chat;
	HttpClient httpClient = null;
	HttpPost request = null;
	HttpResponse response = null;
	String result = "";
	BufferedReader br;
	SharedPreferences pref, mypref;
	Editor editor;
	ScrollView sv;
	String to;
	String lawyerid;
	String userid = "";
	String username = "";
	ProgressDialog progress;
	String progresscondition = "";
	String changedIP = "";
	RelativeLayout r;
	AlertDialogManager alert = new AlertDialogManager();
	ListView list;
	public String unique[];
	ArrayList<String> names = new ArrayList<String>();
	ArrayList<String> messages = new ArrayList<String>();

	// CustomListAdapterChat adapter;
	CustomListAdapterChat adapter;

	@Override
	public void onBackPressed() {
		this.finish();
		overridePendingTransition(R.anim.animation_enter, R.anim.animation_leave);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_client_c3_chat);

		r = (RelativeLayout) findViewById(R.id.RLChat_client);
		// sv = (ScrollView) findViewById(R.id.Chatscroll_lawyer);
		//
		//
		//
		// sv.post(new Runnable() {
		//
		// @Override
		// public void run() {
		// // TODO Auto-generated method stub
		// sv.fullScroll(ScrollView.FOCUS_DOWN);
		// }
		// });

		ActionBar bar = getActionBar();
		ColorDrawable c = new ColorDrawable(Color.parseColor("#03A9F4"));// FF6600
		bar.setBackgroundDrawable(c);
		//bar.setTitle(Html.fromHtml("<font color='#ffffff'>Chat Zone</font>"));
		bar.setTitle("Chat Zone");
		bar.setIcon(android.R.color.transparent);
		bar.setDisplayHomeAsUpEnabled(true);

		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			to = (String) bundle.get("toid");
		}

		// System.out.println("ASDASDASDASDASD"+to);

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		SharedPreferences ip;
		ip = getSharedPreferences("IPaddr", Context.MODE_PRIVATE);
		changedIP = ip.getString("IP", null);

		bt_send = (Button) findViewById(R.id.bttn_send_msg_client);
		ed_chat = (EditText) findViewById(R.id.edt_chat_msg_client);

		mypref = getSharedPreferences("preferenceclogin", Context.MODE_PRIVATE);
		userid = mypref.getString("clientid", null);
		username = mypref.getString("cname", null);

		chat();
		adapter = new CustomListAdapterChat(this, names, messages, unique, userid);
		list = (ListView) findViewById(R.id.ChatListView_client);

		list.setAdapter(adapter);
		list.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
		list.setStackFromBottom(true);
	}

	public void refresh(View v) {
		list.setAdapter(null);
		chat();
		adapter = new CustomListAdapterChat(this, names, messages, unique, userid);
		list.setAdapter(adapter);
		list.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
		list.setStackFromBottom(true);
		adapter.notifyDataSetChanged();
	}

	public void refresh_send() {
		chat();
		adapter = new CustomListAdapterChat(this, names, messages, unique, userid);
		list.setAdapter(adapter);
		list.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
		list.setStackFromBottom(true);
		adapter.notifyDataSetChanged();
	}

	public void chat() {
		progress = new ProgressDialog(this);
		progress.setMessage("Chat Starting..");
		progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progress.setCancelable(false);
		progress.setCanceledOnTouchOutside(false);

		mypref = getSharedPreferences("preferenceclogin", Context.MODE_PRIVATE);
		userid = mypref.getString("clientid", null);
		username = mypref.getString("cname", null);

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);

		try {

			httpClient = new DefaultHttpClient();
			request = new HttpPost("http://" + changedIP + ":8080/AndroLawyer/actChatGet.jsp");
			List<NameValuePair> postParameters = new ArrayList<NameValuePair>();
			postParameters.add(new BasicNameValuePair("feedid", userid));
			postParameters.add(new BasicNameValuePair("tooid", to));
			System.out.println("send feed to>>>>>>>>>>>" + to);
			System.out.println("send feed from>>>>>>>>>>>" + userid);
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(postParameters);
			request.setEntity(entity);
			response = httpClient.execute(request);
			if (response != null) {
				br = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
				String Line = "";
				StringBuilder sb = new StringBuilder("");
				while ((Line = br.readLine()) != null) {
					sb.append(Line);
				}
				result = sb.toString();

				if (result.trim().length() > 0) {
					Handler handler = new Handler();
					handler.postDelayed(new Runnable() {

						@Override
						public void run() {
							// Hide your View after 3 seconds
							// progress.dismiss();
							// l.setVisibility(View.VISIBLE);
						}
					}, 1000);
					String val[] = result.split("\\#");
					for (int i = 0; i < val.length; i++) {

						String va[] = val[i].split("\\*");
						chatunique();
						if (unique[i].equals(userid)) {
							names.add(va[1] + " Sender");
						} else {
							names.add(va[1]);
						}
						messages.add(va[0]);
						// username = va[1];

					}

				}

			}

		} catch (Exception e) {
			System.out.println(e);
			Toast.makeText(getApplicationContext(), "Exception in chat " + e, Toast.LENGTH_SHORT).show();
		}

		bt_send.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (ed_chat.getText().toString().trim().equals("")) {
					Animation a = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale_up);
					v.startAnimation(a);
					Toast.makeText(getApplicationContext(), "type something..", Toast.LENGTH_SHORT).show();
				} else {
					Animation a = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale_up);
					v.startAnimation(a);
					mypref = getSharedPreferences("preferenceclogin", Context.MODE_PRIVATE);
					userid = mypref.getString("clientid", null);
					username = mypref.getString("cname", null);

					names.add(username + " Sender");
					messages.add((ed_chat.getText().toString()));
					// messages.add((ed_chat.getText().toString().replaceAll("\\n",
					// "<br />")));

					// adapter.notifyDataSetChanged();

					// sv.post(new Runnable() {
					//
					// @Override
					// public void run() {
					// // TODO Auto-generated method stub
					// sv.fullScroll(ScrollView.FOCUS_DOWN);
					// }
					// });

					try {
						if (adapter != null) {
							adapter.notifyDataSetChanged();
						}
						httpClient = new DefaultHttpClient();
						request = new HttpPost("http://" + changedIP + ":8080/AndroLawyer/actChatAdd.jsp");
						List<NameValuePair> postParameters = new ArrayList<NameValuePair>();
						postParameters.add(
								new BasicNameValuePair("cmnt", ed_chat.getText().toString().replaceAll("\\n", "<n>")));
						// postParameters.add(new BasicNameValuePair("cmnt",
						// ed_chat.getText().toString()));
						postParameters.add(new BasicNameValuePair("id", userid));
						postParameters.add(new BasicNameValuePair("user", username));
						postParameters.add(new BasicNameValuePair("toid", to));

						UrlEncodedFormEntity entity = new UrlEncodedFormEntity(postParameters);
						request.setEntity(entity);

						response = httpClient.execute(request);
						if (response != null) {

							br = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
							String Line = "";
							StringBuilder sb = new StringBuilder("");
							while ((Line = br.readLine()) != null) {
								sb.append(Line);
							}
							result = sb.toString();
							ed_chat.setText("");
							System.out.println("chat ok result>>>>>>" + result);
							// sv.post(new Runnable() {
							//
							// @Override
							// public void run() {
							// // TODO Auto-generated method stub
							// sv.fullScroll(ScrollView.FOCUS_DOWN);
							// }
							// });
						}

					} catch (Exception e) {
						Toast.makeText(getApplicationContext(), "Exception in chat " + e, Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
	}

	public void chatunique() {

		mypref = getSharedPreferences("preferenceclogin", Context.MODE_PRIVATE);
		userid = mypref.getString("clientid", null);
		username = mypref.getString("cname", null);

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);

		try {

			httpClient = new DefaultHttpClient();
			request = new HttpPost("http://" + changedIP + ":8080/AndroLawyer/actChatGetUnique.jsp");
			List<NameValuePair> postParameters = new ArrayList<NameValuePair>();
			postParameters.add(new BasicNameValuePair("feedid", userid));
			postParameters.add(new BasicNameValuePair("tooid", to));
			System.out.println("send feed to uu >>>>>>>>>>>" + to);
			System.out.println("send feed from uu >>>>>>>>>>>" + userid);
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(postParameters);
			request.setEntity(entity);
			response = httpClient.execute(request);
			if (response != null) {
				br = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
				String Line = "";
				StringBuilder sb = new StringBuilder("");
				while ((Line = br.readLine()) != null) {
					sb.append(Line);
				}
				result = sb.toString();

				if (result.trim().length() > 0) {
					Handler handler = new Handler();
					handler.postDelayed(new Runnable() {

						@Override
						public void run() {
							// Hide your View after 3 seconds
							// progress.dismiss();
							// l.setVisibility(View.VISIBLE);
						}
					}, 1000);
					unique = result.split("\\*");

				}
			}

		} catch (Exception e) {
			System.out.println(e);
			Toast.makeText(getApplicationContext(), "Exception in chat " + e, Toast.LENGTH_SHORT).show();
		}
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

	// private void setMargins(View view, int left, int top, int right, int
	// bottom) {
	// if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
	// ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams)
	// view.getLayoutParams();
	// p.setMargins(left, top, right, bottom);
	// view.requestLayout();
	// }
	// }
}
