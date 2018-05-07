package com.example.androlawyer;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomListAdapter extends ArrayAdapter<String> {

	private final Activity context;
	private final String[] itemname;
	private final Integer[] imgid;
	private final String[] itemdesc;
	
	public CustomListAdapter(Activity context, String[] itemname, Integer[] imgid, String[] itemdesc) {
		super(context, R.layout.custom_listview, itemname);
		this.context=context;
		this.itemname=itemname;
		this.imgid=imgid;
		this.itemdesc=itemdesc;
	}
	
	public View getView(int position,View view,ViewGroup parent) {
		LayoutInflater inflater=context.getLayoutInflater();
		View rowView=inflater.inflate(R.layout.custom_listview, null,true);
		
		TextView txtTitle = (TextView) rowView.findViewById(R.id.customlv_item);
		ImageView imageView = (ImageView) rowView.findViewById(R.id.customlv_icon);
		TextView extratxt = (TextView) rowView.findViewById(R.id.customlv_sub);
		
		txtTitle.setText(itemname[position]);
		imageView.setImageResource(imgid[position]);
		//extratxt.setText("Description "+itemname[position]);
		extratxt.setText(itemdesc[position]);
		return rowView;
		
	};
}
