package com.app.xstore.space;

import org.simple.eventbus.EventBus;

import android.os.Bundle;
import android.view.View;

import com.app.xstore.App;
import com.app.xstore.BaseActivity;
import com.app.xstore.R;
import com.squareup.picasso.Picasso;
import com.widget.photoView.PhotoView;


/**
 * 查看图片
 * 
 * @author pythoner
 * 
 */
public class ImageActivity extends BaseActivity {
	private String url;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image);
		context = this;
		url = getIntent().getStringExtra("URL");
//		initActionBar("查看图片", null,null);
		initViews();
	}

	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		View btn_replace=findViewById(R.id.btn_replace);
		btn_replace.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				EventBus.getDefault().post(App.EVENT_SELECT_PICTURE);
				finish();
			}
		});
		boolean showTool=getIntent().getBooleanExtra("ShowTool", false);
		btn_replace.setVisibility(showTool?View.VISIBLE:View.GONE);
		PhotoView photoView=$(R.id.photoView);
		photoView.enable();
//		Log.i("tag", "url="+url);
		Picasso.with(context).load(url).error(R.drawable.picture_not_found).into(photoView);
	}
	@Override
	public void updateViews(Object obj) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		overridePendingTransition(0, R.anim.back_by_position);
//		ActivityCompat.finishAfterTransition(ImageActivity.this);
	}
}
