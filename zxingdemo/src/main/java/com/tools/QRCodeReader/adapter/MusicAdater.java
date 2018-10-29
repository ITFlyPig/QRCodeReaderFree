package com.tools.QRCodeReader.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.tools.QRCodeReader.R;
import com.tools.QRCodeReader.bean.DataModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;



/**
 *
 * @author 张志珍
 * @ClassName:Case_Adapter
 * @Description:发现里案例的item
 * @date 2015-7-7
 *
 */
public class MusicAdater extends BaseAdapter {
	private Context context;
	private LayoutInflater mInflater = null;
	private ArrayList<DataModel> listdata;
	private Handler mHandler;
	String date;

	public MusicAdater(Context context, ArrayList<DataModel> musicList){
		this.mInflater = LayoutInflater.from(context);
		this.listdata = musicList;
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM");
		date=sdf.format(new java.util.Date());
	}

	@Override
	public int getCount() {
		return listdata.size();
	}


	@Override
	public Object getItem(int arg0) {
		return listdata.get(arg0);
	}


	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	static class ViewHolder
	{
		private TextView url_text,time_tv;
	}

	@Override
	public View getView(final  int arg0, View convertView, ViewGroup arg2) {
		final ViewHolder holder;
		if(convertView == null)
		{
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.list_item, null);
			holder.url_text = (TextView) convertView.findViewById(R.id.url_text);
			holder.time_tv = (TextView)convertView.findViewById(R.id.time_tv);
			convertView.setTag(holder);
		}else
		{
			holder = (ViewHolder)convertView.getTag();
		}

		holder.url_text.setText(listdata.get(arg0).getUrl());
		holder.time_tv.setText(date);
		return convertView;
	}
}
