package com.tools.QRCodeReader;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.tools.QRCodeReader.adapter.MusicAdater;
import com.tools.QRCodeReader.bean.DataModel;
import com.tools.QRCodeReader.db.DatabaseDao;
import com.tools.QRCodeReader.uitl.Advertisement;
import com.tools.QRCodeReader.uitl.StatusBarUtil;
import java.util.ArrayList;
import java.util.regex.Pattern;


public class ListActivity extends BaseActivity {

    private ListView musicListView = null;

    private MusicAdater musicAdater;

    private DatabaseDao database;
    private ArrayList<DataModel> listdata;
    private Dialog dialog;
    private AdView mAdView;
    private static final String TAG = TestScanActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_layout);

        StatusBarUtil.setColor(this, getResources().getColor(R.color.app));
        init();
    }

    private void init() {

        database = new DatabaseDao(ListActivity.this);
        musicListView = (ListView)findViewById(R.id.musicListView);
        listdata =database.queryAll();
        //listview里加载数据
        musicAdater = new MusicAdater(ListActivity.this,listdata);
        musicListView.setAdapter(musicAdater);
        musicListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Pattern pattern = Pattern
                        .compile("^([hH][tT]{2}[pP]://|[hH][tT]{2}[pP][sS]://)(([A-Za-z0-9-~]+).)+([A-Za-z0-9-~\\/])+$");
                Boolean boole=pattern.matcher(listdata.get(position).getUrl()).matches();
                Log.i("tag", "onItemClick: "+boole);

                show_Dialog(listdata.get(position).getUrl());

            }
        });
        mAdView = (AdView)findViewById(R.id.adView);
        AdRequest adRe = new AdRequest.Builder().build();
        mAdView.loadAd(adRe);

    }

    private void show_Dialog(final String url){
        dialog= new Dialog(this,R.style.dialog);
        View view = View.inflate(this, R.layout.delete_dialog_layout, null);
        TextView content_url=(TextView)view.findViewById(R.id.content_url);
        content_url.setText(url);
        TextView open_url=(TextView)view.findViewById(R.id.open_url);
        TextView copy_url=(TextView)view.findViewById(R.id.copy_url);
        TextView delete_url=(TextView)view.findViewById(R.id.delete_tv);
        Pattern pattern = Pattern
                .compile("^([hH][tT]{2}[pP]://|[hH][tT]{2}[pP][sS]://)(([A-Za-z0-9-~]+).)+([A-Za-z0-9-~\\/])+$");
        Boolean boole=pattern.matcher(url).matches();

        if (boole==true){
            copy_url.setText(getString(R.string.copy));
            open_url.setVisibility(View.VISIBLE);
        }else{
            copy_url.setText(getString(R.string.copynourl));
            open_url.setVisibility(View.GONE);
        }

        delete_url.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //删除
                database.deleteByUrl(url);
                listdata.clear();
                listdata =database.queryAll();
                //listview里加载数据
                musicAdater = new MusicAdater(ListActivity.this,listdata);
                musicListView.setAdapter(musicAdater);
                dialog.dismiss();
            }
        });
        open_url.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(ListActivity.this,WebViewActivity.class);
                intent.putExtra("url",url);
                startActivity(intent);
                dialog.dismiss();
            }
        });
        view.findViewById(R.id.copy_url).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager cmb = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
                cmb.setText(url.trim());
                Toast.makeText(ListActivity.this,"复制成功",Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        });
        view.findViewById(R.id.txt_cancel).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(view);
        Window window = dialog.getWindow();
        android.view.WindowManager.LayoutParams lp = window.getAttributes();
        window.setGravity(Gravity.BOTTOM);
        lp.width = LinearLayout.LayoutParams.FILL_PARENT;
        lp.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.show();

    }


    public Handler mHandler = new Handler() {

        @SuppressLint("NewApi")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    int position = msg.arg1;
                    try {
                        //先拿到单例模式里面的 实例  因为封装的是MAP  传入KEY去拿值 在显现广告
                        Advertisement.getInstance().show(getString(R.string.ad_unit_id));
                    } catch (Exception e) {
                        Log.d(TAG, "setContentView: 显示广告失败");
                    }
                    show_Dialog(listdata.get(position).getUrl());
                    break;

            }
            super.handleMessage(msg);
        }
    };





    public void clickback(View v){
        try {
            //先拿到单例模式里面的 实例  因为封装的是MAP  传入KEY去拿值 在显现广告
            Advertisement.getInstance().show(getString(R.string.ad_unit_id));
        } catch (Exception e) {
            Log.d(TAG, "setContentView: 显示广告失败");
        }
        finish();
    }


    //按两次返回键退出
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            try {
                //先拿到单例模式里面的 实例  因为封装的是MAP  传入KEY去拿值 在显现广告
                Advertisement.getInstance().show(getString(R.string.ad_unit_id));
            } catch (Exception e) {
                Log.d(TAG, "setContentView: 显示广告失败");
            }
            finish();
        }
        return false;
    }

}
