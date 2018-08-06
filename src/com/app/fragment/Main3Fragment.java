package com.app.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.xstore.R;


/**
 * 试衣
 * @author pythoner
 *
 */
public class Main3Fragment extends BaseFragment{

	
	public static Main3Fragment newInstance(String param1) {
		Main3Fragment fragment = new Main3Fragment();
        Bundle args = new Bundle();
        args.putString("agrs1", param1);
        fragment.setArguments(args);
        return fragment;
    }
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		return inflater.inflate(R.layout.fragment_main_3, container,false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		initViews(view);
		updateTheme(view);
	}
	
	@Override
	public void initViews(View view){
		
		
	}
	
	
	@Override
	public void updateViews(Object obj) {
	}
	
//	@Subscriber
//	void updateByEventBus(String event) {
//		if(event.equals(App.EVENT_SAVE_FITTING)){
//		}
//	}
	
	@Override
	public void updateTheme(int color) {
		super.updateTheme(color);
	}
	
	private void updateTheme(View view) {
//		if(context!=null&&view!=null){
//			context.setThemeDrawable(context,R.id.btn_ok);
//		}
	}
	
}
