package com.app.xstore.space;

import org.simple.eventbus.Subscriber;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;

import com.app.xstore.BaseActivity;
import com.app.xstore.R;
import com.base.model.KeyValue;

/**
 * 店员空间
 * @author pythoner
 *
 */
public class RefreshListActivity extends BaseActivity implements OnClickListener{

	private Context context;
    private KeyValue curKeyValue=null;  
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_refresh_list);
		context=this;
		curKeyValue = getIntent().getParcelableExtra("KeyValue");
		initActionBar(curKeyValue.getValue(), "发布", null);
		initViews();
		RefreshListFragment f=RefreshListFragment.newInstance(curKeyValue);
		addFragment(f,"RefreshListFragment");
	}

	private void addFragment(Fragment fragment, String tag) {  
	    FragmentManager manager = getSupportFragmentManager();  
	    FragmentTransaction transaction = manager.beginTransaction();  
	    transaction.add(R.id.fragment_container, fragment, tag);  
	    transaction.commitAllowingStateLoss();  
	}  
	
	@Override
	public void initViews() {
	}
	
	@Override
	public void doRightButtonClick(View v) {
		// TODO Auto-generated method stub
		if(curKeyValue==null){
			return;
		}
		Intent intent=new Intent(context,SpacePublishActivity.class);
		intent.putExtra("LabelID",Integer.parseInt(curKeyValue.getKey()));
		startActivity(intent);
		finish();
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void updateViews(Object obj){
		
	}

	@Subscriber
	void updateByEventBus(String event) {
		
	}
	
	@Override
	public void updateTheme(int color) {
		super.updateTheme(color);
	}
	
}
