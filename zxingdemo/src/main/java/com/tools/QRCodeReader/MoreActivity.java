package com.tools.QRCodeReader;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.tools.QRCodeReader.uitl.Advertisement;
import com.tools.QRCodeReader.uitl.StatusBarUtil;
import com.umeng.analytics.MobclickAgent;

import java.util.Locale;


public class MoreActivity extends BaseActivity implements OnClickListener {
	private static final String TAG = MoreActivity.class.getSimpleName();
	private Button but_back;
	private TextView tv_feedback,tv_commnts;
	//	@Bind(main_adView)
	AdView mAdView;
	private InterstitialAd interstitial;
	private TextView tvPlolicy;
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_more_view);
		StatusBarUtil.setColor(this, getResources().getColor(R.color.app));

		findViews();
	}






	@SuppressLint("NewApi")
	private void findViews() {
		mAdView = (AdView)findViewById(R.id.main_adView);
		//横幅
		AdRequest adRequest = new AdRequest.Builder().build();
		// 最后，请求广告。
		mAdView.loadAd(adRequest);
		// 插屏广告
		interstitial = new InterstitialAd(this);
		interstitial.setAdUnitId(getString(R.string.ad_unit_id));
		AdRequest request = new AdRequest.Builder().build();
		interstitial.loadAd(request);


		tv_feedback = (TextView)findViewById(R.id.tv_feedback);
		tv_commnts = (TextView)findViewById(R.id.tv_share);
		tv_feedback.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent data=new Intent(Intent.ACTION_SENDTO);
				data.setData(Uri.parse("mailto:rekindleteam@gmail.com"));
				data.putExtra(Intent.EXTRA_SUBJECT, "这是标题");
				data.putExtra(Intent.EXTRA_TEXT, "这是内容");
				startActivity(data);

			}
		});

		tv_commnts.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {

				//这里开始执行一个应用市场跳转逻辑，默认this为Context上下文对象
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setData(Uri.parse("market://details?id=" + "com.tools.QRCodeReader")); //跳转到应用市场，非Google Play市场一般情况也实现了这个接口
				//存在手机里没安装应用市场的情况，跳转会包异常，做一个接收判断
				if (intent.resolveActivity(getPackageManager()) != null) { //可以接收
					startActivity(intent);
                    //Intent intent2 = new Intent();
                    // intent.setAction("android.intent.action.VIEW");
                   // Uri content_url = Uri.parse("https://play.google.com/store/apps/details?id=com.gameguide.minebox");
                   // intent.setData(content_url);
					// startActivity(intent);

				} else { //没有应用市场，我们通过浏览器跳转到Google Play
					intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=" + "com.tools.QRCodeReader"));
					//这里存在一个极端情况就是有些用户浏览器也没有，再判断一次
					if (intent.resolveActivity(getPackageManager()) != null) { //有浏览器
						startActivity(intent);
					} else { //天哪，这还是智能手机吗？
						Toast.makeText(MoreActivity.this, "您没安装应用市场，连浏览器也没有", Toast.LENGTH_SHORT).show();
					}
				}

			}
		});
		tvPlolicy = (TextView) findViewById(R.id.tv_policy);
		tvPlolicy.setOnClickListener(this);
	}

	private void setLang(Locale l) {
		// 获得res资源对象
		Resources resources = getResources();
		// 获得设置对象
		Configuration config = resources.getConfiguration();
		// 获得屏幕参数：主要是分辨率，像素等。
		DisplayMetrics dm = resources.getDisplayMetrics();
		// 语言
		config.locale = l;
		resources.updateConfiguration(config, dm);
	}

	public void ButtonClick(View v){

		try {
			//先拿到单例模式里面的 实例  因为封装的是MAP  传入KEY去拿值 在显现广告
			Advertisement.getInstance().show(getString(R.string.ad_unit_id));
		} catch (Exception e) {
			Log.d(TAG, "setContentView: 显示广告失败");
		}
		finish();
	}

	public void onEmailClick(View v){
		String[] email = {"3802**92@qq.com"}; // 需要注意，email必须以数组形式传入
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("message/rfc822"); // 设置邮件格式
		intent.putExtra(Intent.EXTRA_EMAIL, email); // 接收人
		intent.putExtra(Intent.EXTRA_CC, email); // 抄送人
		intent.putExtra(Intent.EXTRA_SUBJECT, "这是邮件的主题部分"); // 主题
		intent.putExtra(Intent.EXTRA_TEXT, "这是邮件的正文部分"); // 正文
		startActivity(Intent.createChooser(intent, "请选择邮件类应用"));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.tv_policy:
				Intent intent = new Intent(this, WebViewActivity.class);
				intent.putExtra("url", "https://gujia.kuaizhan.com/73/62/p432946254be75f");
				startActivity(intent);

				break;
		}
	}
}
