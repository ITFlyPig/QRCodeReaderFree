package com.tools.QRCodeReader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.tools.QRCodeReader.uitl.StatusBarUtil;


public class StartActivity extends BaseActivity {
    private static final String TAG = StartActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        ImageView img_start  = (ImageView) findViewById(R.id.img_start);
        img_start.startAnimation(AnimationUtils.loadAnimation(this, R.anim.start_alpha));
        StatusBarUtil.setColor(this, getResources().getColor(R.color.app));

        new Handler().postDelayed(new Runnable() {
            public void run() {
                Intent intent = new Intent(StartActivity.this,TestScanActivity.class);//新建一个意图，也就是跳转的界面
                startActivity(intent);//开始跳转
                finish();
            }
        }, 2000);

    }

}
