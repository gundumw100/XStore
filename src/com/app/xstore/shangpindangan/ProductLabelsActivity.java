package com.app.xstore.shangpindangan;

import org.simple.eventbus.EventBus;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.RadioButton;

import com.app.xstore.App;
import com.app.xstore.BaseActivity;
import com.app.xstore.R;

/**
 * 商品标签管理
 * @author pythoner
 * 
 */
public class ProductLabelsActivity extends BaseActivity implements View.OnClickListener{

	private String goods_sn;
	
	private ProductAFragment fragment0;
	private ProductBFragment fragment1;
	private ProductCFragment fragment2;
	private final int COUNT=3;
	private RadioButton[] rbs;
	private int index = 0;//
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shangpindangan_product_labels);
		context = this;
		initActionBar("自定义标签", null, null);
		goods_sn=getIntent().getStringExtra("goods_sn");
		if(isEmpty(goods_sn)||goods_sn.length()<6){
			showToast("编码有误");
			finish();
			return;
		}
		initViews();
	}

	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		initIndicators();
	}
	
	private void initIndicators() {
		rbs = new RadioButton[COUNT];
		rbs[0] = $(R.id.btn_a);
		rbs[1] = $(R.id.btn_b);
		rbs[2] = $(R.id.btn_c);
		for (int i = 0; i < rbs.length; i++) {
			rbs[i].setOnClickListener(this);
		}
		rbs[index].performClick();
	}
	
	@Override
	public void updateViews(Object obj) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_a:
			index = 0;
			switchFragment(index);
			break;
		case R.id.btn_b:
			index = 1;
			switchFragment(index);
			break;
		case R.id.btn_c:
			index = 2;
			switchFragment(index);
			break;
		}
	}
	
	/**
	 * 默认false
	 * 
	 * @param index
	 */
	private void switchFragment(int index) {
		switchFragment(index, true);
	}

	/**
	 * 
	 * @param index
	 * @param allowingStateLoss
	 *            为了解决IllegalStateException: Can not perform this action after
	 *            onSaveInstanceState
	 */
	private void switchFragment(int index, boolean allowingStateLoss) {
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		hideAllFragment(transaction);
		switch (index) {
		case 0:
			if (fragment0 == null) {
				fragment0 = ProductAFragment.newInstance(goods_sn);
				transaction.add(R.id.container, fragment0);
			}else{
				transaction.show(fragment0);
			}
			break;
		case 1:
			if (fragment1 == null) {
				fragment1 = ProductBFragment.newInstance(goods_sn);
				transaction.add(R.id.container, fragment1);
			} else{
				transaction.show(fragment1);
			}
			break;
		case 2:
			if (fragment2 == null) {
				fragment2 = ProductCFragment.newInstance(goods_sn);
				transaction.add(R.id.container, fragment2);
			}else{
				transaction.show(fragment2);
			}
			break;
		}
		// transaction.addToBackStack(null);
		if (allowingStateLoss) {
			transaction.commitAllowingStateLoss();
		} else {
			transaction.commit();
		}
	}

	private void hideAllFragment(FragmentTransaction transaction){
		if (fragment0 != null) {
			transaction.hide(fragment0);
		}
		if (fragment1 != null) {
			transaction.hide(fragment1);
		}
		if (fragment2 != null) {
			transaction.hide(fragment2);
		}
	}
	
	public void gotoFragment(int position) {
//		rbs[position].performClick();
		index = position;
		rbs[index].setChecked(true);
		switchFragment(index, true);
	}
	
	@Override
	public void updateTheme(int color) {
		super.updateTheme(color);
		if(fragment0!=null){
			fragment0.updateTheme(color);
		}
		if(fragment1!=null){
			fragment1.updateTheme(color);
		}
		if(fragment2!=null){
			fragment2.updateTheme(color);
		}
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		EventBus.getDefault().post(App.EVENT_REFRESH);
		super.onDestroy();
	}
}
