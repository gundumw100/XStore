package com.app.xstore;

import com.app.net.Commands;
import com.app.xstore.R;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * 帮助
 * @author pythoner
 * 
 */
public class HelpActivity extends BaseActivity{

	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_inventory_help);
		context = this;
		initActionBar("帮助", null, null);
		initViews();
	}

	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		String file=getIntent().getStringExtra("FILE");
		if(isEmpty(file)){
			showToast("未找到网页");
			return;
		}
		String url=Commands.IP+"/"+file;
		Log.i("tag", url);
		WebView webView=(WebView)findViewById(R.id.webView);
		webView.loadUrl(url);
		// 覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// TODO Auto-generated method stub
				// 返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
				view.loadUrl(url);
				return true;
			}
		});
	       
	}
	@Override
	public void updateViews(Object obj) {
		// TODO Auto-generated method stub

	}
}
