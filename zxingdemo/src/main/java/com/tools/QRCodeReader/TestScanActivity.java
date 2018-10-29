package com.tools.QRCodeReader;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.google.android.gms.ads.InterstitialAd;
import com.tools.QRCodeReader.bean.DataModel;
import com.tools.QRCodeReader.db.DatabaseDao;
import com.tools.QRCodeReader.uitl.Advertisement;
import com.tools.QRCodeReader.uitl.PreferenceUtil;
import com.tools.QRCodeReader.uitl.StatusBarUtil;
import com.umeng.analytics.MobclickAgent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;
import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zxing.QRCodeDecoder;
import cn.bingoogolapple.qrcode.zxing.ZXingView;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import static com.tools.QRCodeReader.uitl.DateUtils.getStringToDate;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;



public class TestScanActivity extends AppCompatActivity implements QRCodeView.Delegate,EasyPermissions.PermissionCallbacks {
    private static final String TAG = TestScanActivity.class.getSimpleName();
    private static final int REQUEST_CODE_CHOOSE_QRCODE_FROM_GALLERY = 666;
    private static final int REQUEST_CODE_QRCODE_PERMISSIONS = 1;

    private QRCodeView mQRCodeView;

    private Dialog dialog;
    private DatabaseDao database;
    private ArrayList<DataModel> listdata;
    private ImageView light_iv,scan_iv;

    //设置前后摄像头
    private Boolean isCamera=true;

    private String picturePath;
    private AdView mAdView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_scan);
//        FacebookSdk.sdkInitialize(getApplicationContext());
        StatusBarUtil.setColor(this, getResources().getColor(R.color.app));
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        database = new DatabaseDao(TestScanActivity.this);
        light_iv = (ImageView)findViewById(R.id.light_iv);
        scan_iv = (ImageView)findViewById(R.id.scan_iv);
        scan_iv.setImageResource(R.drawable.scan_sel);
        mQRCodeView = (ZXingView) findViewById(R.id.zxingview);
        mQRCodeView.setDelegate(this);
        mQRCodeView.startSpot(isCamera);
        mAdView = (AdView)findViewById(R.id.adView);
        AdRequest adRe = new AdRequest.Builder().build();
        mAdView.loadAd(adRe);


    }


    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        scan_iv.setImageResource(R.drawable.scan_sel);
//        mQRCodeView.startSpot(isCamera);
    }

    @Override
    protected void onPause() {
        MobclickAgent.onPause(this);
//        mQRCodeView.stopCamera();
        super.onPause();
    }

    public void onclicklight(View v){
        String tag = (String) PreferenceUtil.getInstance().get("flash", "close");
        if (TextUtils.equals(tag, "open")) {
            Log.i(TAG, "onclickBack: open");
            mQRCodeView.closeFlashlight();
            PreferenceUtil.getInstance().put("flash", "close");
            light_iv.setImageResource(0);
            light_iv.setBackgroundResource(R.drawable.lightning);
        } else {
            Log.i(TAG, "onclickBack: close");
            mQRCodeView.openFlashlight();
            PreferenceUtil.getInstance().put("flash", "open");
            light_iv.setImageResource(0);
            light_iv.setBackgroundResource(R.drawable.lightclose);
        }
    }

    int i=0;
    /**
     * 切换前后摄像头
     * @param v
     */
    public void onClickCamera(View v){
        mQRCodeView.stopCamera();

        if (i==1){
            Log.i("QRCodeView", "startCamera:------------- 后置");
            i=2;
            isCamera=true;
            mQRCodeView.startSpot(isCamera);
            mQRCodeView.showScanRect();
        }else{
            i=1;
            Log.i("QRCodeView", "startCamera:------------- 前置");
            isCamera=false;
            mQRCodeView.startSpot(isCamera);
            mQRCodeView.showScanRect();
        }
//        Log.i(TAG, "onClickCamera: ");
//        Toast.makeText(TestScanActivity.this,"前",Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        picturePath=savedInstanceState.getString("");
    }

    @Override
    protected void onStart() {
        super.onStart();
        mQRCodeView.startSpot(isCamera);
        mQRCodeView.startCamera();
        requestCodeQRCodePermissions();
        mQRCodeView.showScanRect();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
    }

    @AfterPermissionGranted(REQUEST_CODE_QRCODE_PERMISSIONS)
    private void requestCodeQRCodePermissions() {
        String[] perms = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (!EasyPermissions.hasPermissions(this, perms)) {
            EasyPermissions.requestPermissions(this, "扫描二维码需要打开相机和散光灯的权限", REQUEST_CODE_QRCODE_PERMISSIONS, perms);
        }
    }

    @Override
    protected void onStop() {
        mQRCodeView.stopCamera();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mQRCodeView.onDestroy();
        super.onDestroy();
    }

    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }

    @Override
    public void onScanQRCodeSuccess(String result) {
        Log.i(TAG, "result:" + result);
//        Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
        vibrate();
        mQRCodeView.startSpot(isCamera);
        if (!TextUtils.isEmpty(result)){
            try {
                //先拿到单例模式里面的 实例  因为封装的是MAP  传入KEY去拿值 在显现广告
                Advertisement.getInstance().show(getString(R.string.ad_unit_id));
            } catch (Exception e) {
                Log.d(TAG, "setContentView: 显示广告失败");
            }
            show_Dialog(result);
            listdata =database.queryByUrl(result);
            if (listdata.size()>0){
            }else{
                DataModel plantsModel=new DataModel();
                plantsModel.setUrl(result);
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
                String date=format.format(new Date());
                Long timelong=getStringToDate(date);
                Log.i(TAG, "时间戳"+timelong);
                plantsModel.setTop_time(timelong);
                database.insert(plantsModel);
            }
        }
    }

    private void show_Dialog(final String url){
        dialog= new Dialog(this,R.style.dialog);
        View view = View.inflate(this, R.layout.dialog_layout, null);
        TextView content_url=(TextView)view.findViewById(R.id.content_url);
        content_url.setText(url);
        TextView open_url=(TextView)view.findViewById(R.id.open_url);
        TextView copy_url=(TextView)view.findViewById(R.id.copy_url);
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
        if (boole==true){
            open_url.setVisibility(View.VISIBLE);
        }else{
            open_url.setVisibility(View.GONE);
        }
        open_url.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(TestScanActivity.this,WebViewActivity.class);
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
                Toast.makeText(TestScanActivity.this,"复制成功",Toast.LENGTH_LONG).show();
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

    public void onclickMore(View v){
        startActivity(new Intent(this, ListActivity.class));
    }

    public void onclickHistory(View v){
        scan_iv.setImageResource(R.drawable.scan);
        startActivity(new Intent(this, ListActivity.class));
    }

    public void  onclickScan(View v){
        mQRCodeView.startSpot(isCamera);
    }

    /**
     * 扫描图片
     * @param v
     */
    public void onClickPhoto(View v){
        Log.i(TAG, "onClickPhoto: ");
          Intent intent=new Intent(TestScanActivity.this,AlbumsActivity.class);
          startActivity(intent);
//         startActivityForResult(BGAPhotoPickerActivity.newIntent(this, null, 1, null, false), REQUEST_CODE_CHOOSE_QRCODE_FROM_GALLERY);
    }



    public void onClickMore(View v){
        scan_iv.setImageResource(R.drawable.scan);
        Intent intent=new Intent(TestScanActivity.this,MoreActivity.class);
        startActivity(intent);
    }

    @Override
    public void onScanQRCodeOpenCameraError() {
        Log.e(TAG, "打开相机出错");
    }

    /*public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start_spot:
                mQRCodeView.startSpot();
                break;
            case R.id.stop_spot:
                mQRCodeView.stopSpot();
                break;
            case R.id.start_spot_showrect:
                mQRCodeView.startSpotAndShowRect();
                break;
            case R.id.stop_spot_hiddenrect:
                mQRCodeView.stopSpotAndHiddenRect();
                break;
            case R.id.show_rect:
                mQRCodeView.showScanRect();
                break;
            case R.id.hidden_rect:
                mQRCodeView.hiddenScanRect();
                break;
            case R.id.start_preview:
                mQRCodeView.startCamera();
                break;
            case R.id.stop_preview:
                mQRCodeView.stopCamera();
                break;
            case R.id.open_flashlight:
                mQRCodeView.openFlashlight();
                break;
            case R.id.close_flashlight:
                mQRCodeView.closeFlashlight();
                break;
            case R.id.scan_barcode:
                mQRCodeView.changeToScanBarcodeStyle();
                break;
            case R.id.scan_qrcode:
                mQRCodeView.changeToScanQRCodeStyle();
                break;
            case R.id.choose_qrcde_from_gallery:
                *//*
                从相册选取二维码图片，这里为了方便演示，使用的是
                https://github.com/bingoogolapple/BGAPhotoPicker-Android
                这个库来从图库中选择二维码图片，这个库不是必须的，你也可以通过自己的方式从图库中选择图片
                 *//*
                startActivityForResult(BGAPhotoPickerActivity.newIntent(this, null, 1, null, false), REQUEST_CODE_CHOOSE_QRCODE_FROM_GALLERY);
                break;
        }
    }*/

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if((Intent.FLAG_ACTIVITY_CLEAR_TOP & intent.getFlags())!=0){
            picturePath = intent.getStringExtra("selectIma");
            Log.i(TAG, "onActivityResult: "+picturePath);
            new AsyncTask<Void, Void, String>() {
                @Override
                protected String doInBackground(Void... params) {
                    return QRCodeDecoder.syncDecodeQRCode(picturePath);
                }

                @Override
                protected void onPostExecute(String result) {
                    if (TextUtils.isEmpty(result)) {
                        Toast.makeText(TestScanActivity.this, TestScanActivity.this.getString(R.string.nofindRQ), Toast.LENGTH_SHORT).show();
                    } else {
                        if (!TextUtils.isEmpty(result)){
                            try {
                                //先拿到单例模式里面的 实例  因为封装的是MAP  传入KEY去拿值 在显现广告
                                Advertisement.getInstance().show(getString(R.string.ad_unit_id));
                            } catch (Exception e) {
                                Log.d(TAG, "setContentView: 显示广告失败");
                            }
                            show_Dialog(result);
                            listdata =database.queryByUrl(result);
                            if (listdata.size()>0){
                            }else{
                                DataModel plantsModel=new DataModel();
                                plantsModel.setUrl(result);
                                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
                                String date=format.format(new Date());
                                Long timelong=getStringToDate(date);
                                Log.i(TAG, "时间戳"+timelong);
                                plantsModel.setTop_time(timelong);
                                database.insert(plantsModel);
                            }
                        }
//                       Toast.makeText(TestScanActivity.this, result, Toast.LENGTH_SHORT).show();
                    }
                }
            }.execute();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        mQRCodeView.showScanRect();

        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_CHOOSE_QRCODE_FROM_GALLERY) {

//            final String picturePath = BGAPhotoPickerActivity.getSelectedImages(data).get(0);
            final String picturePath = data.getStringExtra("selectIma");
            Log.i(TAG, "onActivityResult: "+picturePath);
            /*
            这里为了偷懒，就没有处理匿名 AsyncTask 内部类导致 Activity 泄漏的问题
            请开发在使用时自行处理匿名内部类导致Activity内存泄漏的问题，处理方式可参考 https://github.com/GeniusVJR/LearningNotes/blob/master/Part1/Android/Android%E5%86%85%E5%AD%98%E6%B3%84%E6%BC%8F%E6%80%BB%E7%BB%93.md
             */
            new AsyncTask<Void, Void, String>() {
                @Override
                protected String doInBackground(Void... params) {
                    return QRCodeDecoder.syncDecodeQRCode(picturePath);
                }

                @Override
                protected void onPostExecute(String result) {
                    if (TextUtils.isEmpty(result)) {
                        Toast.makeText(TestScanActivity.this,TestScanActivity.this.getString(R.string.nofindRQ), Toast.LENGTH_SHORT).show();
                    } else {
                        if (!TextUtils.isEmpty(result)){
                            try {
                                //先拿到单例模式里面的 实例  因为封装的是MAP  传入KEY去拿值 在显现广告
                                Advertisement.getInstance().show(getString(R.string.ad_unit_id));
                            } catch (Exception e) {
                                Log.d(TAG, "setContentView: 显示广告失败");
                            }
                            show_Dialog(result);
                            listdata =database.queryByUrl(result);
                            if (listdata.size()>0){
                            }else{
                                DataModel plantsModel=new DataModel();
                                plantsModel.setUrl(result);
                                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
                                String date=format.format(new Date());
                                Long timelong=getStringToDate(date);
                                Log.i(TAG, "时间戳"+timelong);
                                plantsModel.setTop_time(timelong);
                                database.insert(plantsModel);
                            }
                        }
//                        Toast.makeText(TestScanActivity.this, result, Toast.LENGTH_SHORT).show();
                    }
                }
            }.execute();
        }
    }


}