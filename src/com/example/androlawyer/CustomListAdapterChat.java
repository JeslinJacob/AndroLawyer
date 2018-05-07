package com.example.androlawyer;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CustomListAdapterChat extends ArrayAdapter<String> {

	private final Activity context;
	public String unique[], userid;
	public ArrayList<String> names = new ArrayList<String>();
	public ArrayList<String> messages = new ArrayList<String>();
	public CustomListAdapterChat(Activity context, ArrayList<String> Unames, ArrayList<String> Umsg, String unique[],
			String userid) {
		super(context, R.layout.custom_listchat, Unames);
		this.context = context;
		this.unique = unique;
		this.names = Unames;
		this.userid = userid;
		this.messages = Umsg;
		// System.out.println("CUSTOMLISTADAPTER" + userid);
		// System.out.println("CUSTOMLISTADAPTER" + unique[0]);
		// System.out.println("CUSTOMLISTADAPTER" + unique[1]);
	}

	public void SetAdapterData(ArrayList<String> Unames, ArrayList<String> Umsg) {
		this.names = Unames;
		this.messages = Umsg;
	}

	public View getView(int position, View view, ViewGroup parent) {
		LayoutInflater inflater = context.getLayoutInflater();
		View rowView = inflater.inflate(R.layout.custom_listchat, null, true);
		TextView txtTitle = (TextView) rowView.findViewById(R.id.clistdatetxt);
		TextView extratxt = (TextView) rowView.findViewById(R.id.clistdesctext);
		LinearLayout ll2 = (LinearLayout) rowView.findViewById(R.id.LLChatListLL);
		LinearLayout ll = (LinearLayout) rowView.findViewById(R.id.LLChatListMARGIN);
		
//		 System.out.println("CUSTOMLISTADAPTERVIEW" + position +
//		 names.get(position));
//		 System.out.println("CUSTOMLISTADAPTERVIEWs" + position +
//		 names.get(position).substring(names.get(position).length() - 6));
		
		if (names.get(position).substring(names.get(position).length() - 3).equals("der")) {
			LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			int l=70,t=0,r=10,b=5;
			params1.setMargins(l,t,r,b);
			ll.setLayoutParams(params1);
			txtTitle.setTextColor(Color.parseColor("#32b4ff"));
			txtTitle.setText(names.get(position).replaceAll("Sender", ""));
			extratxt.setText(messages.get(position).replaceAll("<n>", "\n"));
			return rowView;
		}
		else
		{
			
		}

		txtTitle.setTextColor(Color.parseColor("#2F972F"));
		txtTitle.setText(names.get(position));
		txtTitle.setText(names.get(position).replaceAll("Sender", ""));
		
		ViewGroup.LayoutParams layoutParams1 = ll2.getLayoutParams(); // Step															// 1.
		LinearLayout.LayoutParams castLayoutParams1 = (LinearLayout.LayoutParams) layoutParams1; // Step																		// 2.
		castLayoutParams1.gravity = Gravity.LEFT;
		
		LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		int l=10,t=0,r=70,b=5;
		params2.setMargins(l,t,r,b);
		ll.setLayoutParams(params2);
		
		ViewGroup.LayoutParams layoutParams2 = txtTitle.getLayoutParams(); // Step																	// 1.
		LinearLayout.LayoutParams castLayoutParams2 = (LinearLayout.LayoutParams) layoutParams2; // Step																							// 2.
		castLayoutParams2.gravity = Gravity.LEFT;

		extratxt.setText(messages.get(position).replaceAll("<n>", "\n"));

		ViewGroup.LayoutParams layoutParams3 = extratxt.getLayoutParams(); // Step																	// 1.
		LinearLayout.LayoutParams castLayoutParams3 = (LinearLayout.LayoutParams) layoutParams3; // Step																							// 2.
		castLayoutParams3.gravity = Gravity.LEFT;

		return rowView;
	};
}
