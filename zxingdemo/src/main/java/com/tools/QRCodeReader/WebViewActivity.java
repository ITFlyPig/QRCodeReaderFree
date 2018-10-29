package com.tools.QRCodeReader;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.tools.QRCodeReader.uitl.Advertisement;
import com.tools.QRCodeReader.uitl.StatusBarUtil;


public class WebViewActivity extends BaseActivity {

	private static final String TAG = WebViewActivity.class.getSimpleName();
	private WebView webview;
	private String url;
	private int index;
	private Button back_tv;
	private ProgressBar web_progressbar;
	ProgressBar pb;
	private ImageView goback,goForward;
	private LinearLayout linear_close;
	AdView mAdView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webview_view);
		StatusBarUtil.setColor(this, getResources().getColor(R.color.app));
		url = getIntent().getStringExtra("url");
		findViews();
	}

	private void findViews(){
		try {
			//先拿到单例模式里面的 实例  因为封装的是MAP  传入KEY去拿值 在显现广告
			Advertisement.getInstance().show(getString(R.string.ad_unit_id));
		} catch (Exception e) {
			Log.d(TAG, "setContentView: 显示广告失败");
		}
		pb = (ProgressBar) findViewById(R.id.pb);
		pb.setMax(100);
		mAdView = (AdView)findViewById(R.id.main_adView);
		//横幅
		AdRequest adRequest = new AdRequest.Builder().build();
		// 最后，请求广告。
		mAdView.loadAd(adRequest);
		linear_close = (LinearLayout)findViewById(R.id.linear_close);
		webview = (WebView) findViewById(R.id.webView);

		WebSettings webSettings = webview.getSettings();
		webSettings.setSavePassword(false);
		webSettings.setSaveFormData(false);
		webSettings.setSupportZoom(false);
		webview.setWebChromeClient(new MyWebChromeClient());
		//设置WebView属性，能够执行Javascript脚本
		webview.getSettings().setJavaScriptEnabled(true);
		webview.setWebChromeClient(new MyWebChromeClient());

		//加载需要显示的网页
		webview.loadUrl(url);
		goback = (ImageView) findViewById(R.id.left);
		goForward = (ImageView) findViewById(R.id.right);
		webview.setWebViewClient(new WebViewClient() {
			@Override
			public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
				goback.setEnabled(webview.canGoBack());
				goForward.setEnabled(webview.canGoForward());
				if (goback.isEnabled()){
					goback.setBackgroundResource(R.drawable.pressleft);
				}else{
					goback.setBackgroundResource(R.drawable.defaultleft);
				}
				if (goForward.isEnabled()){
					goForward.setBackgroundResource(R.drawable.pressright);
				}else{
					goForward.setBackgroundResource(R.drawable.defaultright);
				}
			}
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				goback.setEnabled(webview.canGoBack());
				goForward.setEnabled(webview.canGoForward());
				if (goback.isEnabled()){
					goback.setBackgroundResource(R.drawable.pressleft);
				}else{
					goback.setBackgroundResource(R.drawable.defaultleft);
				}
				if (goForward.isEnabled()){
					goForward.setBackgroundResource(R.drawable.pressright);
				}else{
					goForward.setBackgroundResource(R.drawable.defaultright);
				}
			}
		});
	}




	final class MyWebChromeClient extends WebChromeClient {
		@Override
		public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
			result.confirm();
			return true;
		}
		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			super.onProgressChanged(view, newProgress);
			if(newProgress==100){
			}
			super.onProgressChanged(view, newProgress);
		}
	}



	public void onLinearClose(View v){

		finish();
	}

	public void onButtonClickClose(View v){
		finish();
	}


	public  void onButtonClickback(View v){
		//来判断是否能回退网页 后退
		if (webview.canGoBack()){
			webview.goBack();
		}else{
		}
	}

	public void onLinearClickback(View v){
		//		后退
		if (webview.canGoBack()){
			webview.goBack();
		}
	}

	public  void onButtonClickForward(View v){
		//前进
		if (webview.canGoForward()){
			webview.goForward();
		}else{
		}
	}

	public void onLinearClickForward(View v){
		//前进
		if (webview.canGoForward()){
			webview.goForward();
		}else{
		}
	}


	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN && event.getRepeatCount() == 0){
			webview.goBack();
		}
		return super.dispatchKeyEvent(event);
	}
	//这个方法就可以监听按钮返回键或者简直返回键操作了 return false就禁止返回

}
