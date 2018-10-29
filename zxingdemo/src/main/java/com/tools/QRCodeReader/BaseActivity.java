package com.tools.QRCodeReader;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.tools.QRCodeReader.uitl.StatusBarUtil;
import com.umeng.analytics.MobclickAgent;
import com.unity3d.ads.metadata.MediationMetaData;

/**
 * Created by ${张志珍} on 2016/12/2217:00.
 * Super Mario RunDemo
 * com.superguide.supermariorunsecond.base
 */

public class BaseActivity extends AppCompatActivity {

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);

        MediationMetaData mediationMetaData = new MediationMetaData(this);
        mediationMetaData.setName("Example mediation network");
        mediationMetaData.setVersion("1.2.3,4");
        mediationMetaData.commit();
//        FacebookSdk.sdkInitialize(getApplicationContext());
        //sdk会对日志加密，防止攻击
        MobclickAgent.enableEncrypt(true);
        //该接口默认参数是true，即采集mac地址，但如果开发者需要在googleplay发布，考虑到审核风险，可以调用该接口，参数设置为false就不会采集mac地址。
        MobclickAgent.setCheckDevice(false);
        setStatusBar();
    }


    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.app));
    }

    // 使用Intent实现activity跳转
    public void goActivity(Class<?> activity) {
        Intent intent = new Intent();
        intent.setClass(this, activity);
        startActivity(intent);
    }


    public void goActivity(Class<?> activity, String paName, String param,
                           String pName, String pname, String paaName, String panaame) {
        Intent intent = new Intent(this, activity);
        intent.putExtra(paName, param);
        intent.putExtra(pName, pname);
        intent.putExtra(paaName, panaame);
        startActivity(intent);
    }

    /**
     * 弹出消息提示层。
     *
     * @param message
     */
    public void AlertToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT)
                .show();
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }


}
