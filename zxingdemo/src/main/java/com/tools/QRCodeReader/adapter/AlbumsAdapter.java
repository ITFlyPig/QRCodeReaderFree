package com.tools.QRCodeReader.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.tools.QRCodeReader.R;
import com.tools.QRCodeReader.bean.PhotoUpImageBucket;
import java.util.ArrayList;
import java.util.List;


public class AlbumsAdapter extends BaseAdapter {

	private List<PhotoUpImageBucket> arrayList;
	private LayoutInflater layoutInflater;
    private Context mContext;
	private String TAG = AlbumsAdapter.class.getSimpleName();
	public AlbumsAdapter(Context context){
		mContext=context;
		layoutInflater = LayoutInflater.from(context);
		arrayList = new ArrayList<PhotoUpImageBucket>();//初始化集合

		
	};
	@Override
	public int getCount() {
		return arrayList.size();
	}

	@Override
	public Object getItem(int position) {
		return arrayList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder;
		if (convertView == null) {
			holder = new Holder();
			convertView = layoutInflater.inflate(R.layout.ablums_adapter_item, parent, false);
			holder.image = (ImageView) convertView.findViewById(R.id.image);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.count = (TextView) convertView.findViewById(R.id.count);
			convertView.setTag(holder);
		}else {
			holder = (Holder) convertView.getTag();
		}
		holder.count.setText(""+arrayList.get(position).getCount());
		holder.name.setText(arrayList.get(position).getBucketName());
		Glide.with(mContext).load("file://"+arrayList.get(position).getImageList().get(0).getImagePath()).into(holder.image);
		return convertView;
	}

	class Holder{
		ImageView image;
		TextView name;
		TextView count;
	}

	public void setArrayList(List<PhotoUpImageBucket> arrayList) {
		this.arrayList = arrayList;
	}
}
