package com.tools.QRCodeReader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.tools.QRCodeReader.adapter.AlbumItemAdapter;
import com.tools.QRCodeReader.bean.PhotoUpImageBucket;
import com.tools.QRCodeReader.bean.PhotoUpImageItem;
import com.tools.QRCodeReader.uitl.StatusBarUtil;

import java.util.ArrayList;

public class AlbumItemActivity extends Activity  {

	private GridView gridView;

	private PhotoUpImageBucket photoUpImageBucket;
	private ArrayList<PhotoUpImageItem> selectImages;
	private AlbumItemAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.album_item_images);
		StatusBarUtil.setColor(this, getResources().getColor(R.color.app));
		init();
		setListener();
	}
	private void init(){
		gridView = (GridView) findViewById(R.id.album_item_gridv);

		selectImages = new ArrayList<PhotoUpImageItem>();
		
		Intent intent = getIntent();
		photoUpImageBucket = (PhotoUpImageBucket) intent.getSerializableExtra("imagelist");
		adapter = new AlbumItemAdapter(photoUpImageBucket.getImageList(), AlbumItemActivity.this);
		gridView.setAdapter(adapter);
	}
	private void setListener(){

		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {

				Intent intent = new Intent(AlbumItemActivity.this,TestScanActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.putExtra("selectIma", photoUpImageBucket.getImageList().get(position).getImagePath());
				startActivity(intent);
                finish();
			}
		});
	}

	public void onClickBack(View v){
		finish();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}


}
