package com.app.xstore.fitting;

import org.simple.eventbus.EventBus;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.app.fragment.FittingFragment;
import com.app.fragment.FittingSearchFragment;
import com.app.xstore.App;
import com.app.xstore.BaseActivity;
import com.app.xstore.R;
import com.widget.view.SimpleViewPagerIndicator;

/**
 * 试衣
 * @author pythoner
 * 
 */
public class FittingActivity extends BaseActivity{

	private Context context;
	private String[] titles = new String[] { "试衣", "查询" };
	private SimpleViewPagerIndicator indicator;
	private ViewPager viewpager;
	private FragmentPagerAdapter pagerAdapter;
	private Fragment[] fragments = new Fragment[titles.length];
	private int curPosition=0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fitting);
		context = this;
		initActionBar("试衣", "保存", null);
		initViews();
		createFloatView(0);
	}

	@Override
	public void doRightButtonClick(View v) {
		// TODO Auto-generated method stub
		if(viewpager.getCurrentItem()==0){
			EventBus.getDefault().post(App.EVENT_SAVE_FITTING);
		}else if(viewpager.getCurrentItem()==1){
			EventBus.getDefault().post(App.EVENT_EXPORT_FITTING);
		}
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		removeFloatView();
	}
	
	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		fragments[0]=new FittingFragment();
		fragments[1]=new FittingSearchFragment();
		
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
				switch (position) {
				case 0:
					showFloatView();
					getRightButton().setText("保存");
					break;
				case 1:
					hideFloatView();
					getRightButton().setText("导出");
					break;
				}
			}
		});
		
	}

	@Override
	public void updateViews(Object obj) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onScanProductHandleMessage(String prodID){
		((FittingFragment)fragments[0]).addItem(prodID);
	}

	@Override
	public void updateTheme(int color) {
		super.updateTheme(color);
		if(fragments[0]!=null){
			((FittingFragment)fragments[0]).updateTheme(color);
		}
		if(fragments[1]!=null){
			((FittingSearchFragment)fragments[1]).updateTheme(color);
		}
	}

}
