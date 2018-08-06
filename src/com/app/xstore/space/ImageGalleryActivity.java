package com.app.xstore.space;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import com.app.xstore.BaseActivity;
import com.app.xstore.R;
import com.base.util.comm.DensityUtils;
import com.squareup.picasso.Picasso;
import com.widget.photoView.PhotoView;


/**
 * 查看图片
 * 
 * @author pythoner
 * 
 */
public class ImageGalleryActivity extends BaseActivity {
	
	private int curPosition;
	private ArrayList<String> list;
	private ViewPager viewPager;
	private ArrayList<View> listViews = new ArrayList<View>();
	private MyPagerAdapter myPagerAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_gallery);
		context = this;
		curPosition = getIntent().getIntExtra("Position", 0);
		list =getIntent().getStringArrayListExtra("Imgs");
//		initActionBar("查看图片", null,null);
		initViews();
	}

	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		LinearLayout container=(LinearLayout)findViewById(R.id.container);
		int size=DensityUtils.dp2px(this,80);
		LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(size,size);
		viewPager=(ViewPager)findViewById(R.id.viewPager);
		for(int i=0;i<list.size();i++){
			final int p=i;
			View view=LayoutInflater.from(context).inflate(R.layout.item_product_img, null);
			listViews.add(view);
			
			String url=list.get(i);
			ImageView iv=new ImageView(context);
			iv.setPadding(8, 8, 8, 8);
			iv.setLayoutParams(params);
			iv.setScaleType(ScaleType.CENTER_CROP);
			iv.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					viewPager.setCurrentItem(p);
				}
			});
			Picasso.with(context).load(url).resize(160, 160).centerCrop().error(R.drawable.picture_not_found).into(iv);
			container.addView(iv);
		}
//		viewPager.setOffscreenPageLimit(3);
		if(myPagerAdapter==null){
			myPagerAdapter=new MyPagerAdapter(listViews);
			viewPager.setAdapter(myPagerAdapter);
		}else{
			myPagerAdapter.setViews(listViews);
			myPagerAdapter.notifyDataSetChanged();
		}
		viewPager.setCurrentItem(curPosition);
	}
	@Override
	public void updateViews(Object obj) {
		// TODO Auto-generated method stub
		
	}
	
	public class MyPagerAdapter extends PagerAdapter {
		public List<View> views;

		public MyPagerAdapter(List<View> views) {
			this.views = views;
		}

		public List<View> getViews() {
			return views;
		}

		public void setViews(List<View> views) {
			this.views = views;
		}

		@Override
		public int getItemPosition(Object object) {
			return POSITION_NONE;
		}

		@Override
		public void destroyItem(View v, int position, Object object) {
			((ViewPager) v).removeView((View) object);
		}

		@Override
		public void finishUpdate(View arg0) {
		}

		@Override
		public int getCount() {
			return views.size();
		}

		@Override
		public Object instantiateItem(View v, int position) {
			View view = views.get(position);
			String url=list.get(position);
			PhotoView photoView=(PhotoView)view.findViewById(R.id.item_0);
			photoView.enable();
//			Log.i("tag", "url="+url);
			Picasso.with(context).load(url).error(R.drawable.picture_not_found).into(photoView);
			((ViewPager) v).addView(view, 0);
			return view;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == (arg1);
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {
		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {
		}

	}

}
