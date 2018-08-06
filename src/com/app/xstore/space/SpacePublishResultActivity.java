package com.app.xstore.space;

import org.simple.eventbus.EventBus;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.app.xstore.App;
import com.app.xstore.BaseActivity;
import com.app.xstore.R;

/**
 * 
 * @author pythoner
 *
 */
public class SpacePublishResultActivity extends BaseActivity implements View.OnClickListener{

	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_space_publish_result);
		context=this;
		initActionBar("发布结果", null, null);
		initViews();
	}

	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		findViewById(R.id.btn_continue).setOnClickListener(this);
		findViewById(R.id.btn_back).setOnClickListener(this);
	}

	@Override
	public void updateViews(Object obj) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_continue:
			int labelID=getIntent().getIntExtra("LabelID", 0);
			Intent intent=new Intent(context,SpacePublishActivity.class);
			intent.putExtra("LabelID",labelID);
			startActivity(intent);
			finish();
			break;
		case R.id.btn_back:
			EventBus.getDefault().post(App.EVENT_UPDATE_SPACE);
			finish();
			break;

		default:
			break;
		}
	}
	
}
