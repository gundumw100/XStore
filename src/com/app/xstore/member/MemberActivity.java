package com.app.xstore.member;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.app.fragment.BaseFragment;
import com.app.xstore.BaseActivity;
import com.app.xstore.R;
import com.widget.view.SimpleViewPagerIndicator;

/**
 * 会员信息
 * 
 * @author pythoner
 * 
 */
public class MemberActivity extends BaseActivity{

	private Context context;
	private String[] titles = new String[] { "积分" };
	private SimpleViewPagerIndicator indicator;
	private ViewPager viewpager;
	private FragmentPagerAdapter pagerAdapter;
	private Fragment[] fragments = new Fragment[titles.length];
	private int curPosition=0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tabs);
		context = this;
		initHandler();
		initActionBar("会员", "新注册", null);
		initViews();
	}

	@Override
	public void initHandler(){
		resultHandler = new Handler(){
			public void handleMessage(android.os.Message msg) {
				String data = (String) msg.obj;
				if(data.equalsIgnoreCase("time out")||data.equalsIgnoreCase("user canceled")){
					showToast(R.string.alert_no_barcode_found);
					return;
				}
				((MemberPointsFragment)fragments[0]).update(data);
			}
		};
	}
		
	@Override
	public void doRightButtonClick(View v) {
		// TODO Auto-generated method stub
		Intent intent=new Intent(context,MemberRegisterActivity.class);
		startActivity(intent);
	}
	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		fragments[0]=MemberPointsFragment.getInstance();
		
		pagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
			@Override
			public int getCount() {
				return titles.length;
			}

			@Override
			public Fragment getItem(int position) {
				return fragments[position];
			}

		};

		viewpager = (ViewPager) findViewById(R.id.viewpager);
//		viewpager.setPageTransformer(false, new RotatePageTransformer());
		viewpager.setAdapter(pagerAdapter);
		viewpager.setOffscreenPageLimit(titles.length-1);
		indicator = (SimpleViewPagerIndicator) findViewById(R.id.indicator);
		indicator.setTitles(titles);
		indicator.setViewPager(viewpager);
		indicator.setCurrentItem(curPosition);
		indicator.setOnPageChangeListener(new SimpleViewPagerIndicator.OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				// TODO Auto-generated method stub
				curPosition=position;
			}
		});
		//只有1个干脆不显示
		indicator.setVisibility(View.GONE);
	}


	@Override
	public void updateViews(Object obj) {
		// TODO Auto-generated method stub
	}

	@Override
	public void updateTheme(int color) {
		super.updateTheme(color);
		for(Fragment fragment:fragments){
			BaseFragment baseFragment=(BaseFragment)fragment;
			if(baseFragment!=null){
				baseFragment.updateTheme(color);
			}
		}
		
	}

}
