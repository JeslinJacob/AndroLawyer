package com.example.androlawyer;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CustomListAdapterDiary extends ArrayAdapter<String> {

	private final Activity context;
	private final String[] itemname;
	
	private final String[] itemdesc;
	
	public CustomListAdapterDiary(Activity context, String[] itemname, String[] itemdesc) {
		super(context, R.layout.custom_listdiary, itemname);
		this.context=context;
		this.itemname=itemname;
		
		this.itemdesc=itemdesc;
	}
	
	public View getView(int position,View view,ViewGroup parent) {
		LayoutInflater inflater=context.getLayoutInflater();
		View rowView=inflater.inflate(R.layout.custom_listdiary, null,true);
		
		TextView txtTitle = (TextView) rowView.findViewById(R.id.clistdatetxt);
		
		TextView extratxt = (TextView) rowView.findViewById(R.id.clistdesctext);
		
		txtTitle.setText(itemname[position]);
		
		//extratxt.setText("Description "+itemname[position]);
		extratxt.setText(itemdesc[position]);
		return rowView;
		
	};
}
