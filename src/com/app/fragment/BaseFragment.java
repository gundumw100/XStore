package com.app.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.xstore.BaseActivity;
import com.base.fragment.BaseAppFragment;

/**
 * 基类，所有fragment必须继承此类，提供一些工具方法
 * 
 * @author pythoner
 * 
 */
public abstract class BaseFragment extends BaseAppFragment{

	public BaseActivity context;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		context = (BaseActivity) getActivity();
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
	}
	
	/**
	 * 初始化view组件，通常写在实现类的onCreateView()中
	 */
	public abstract void initViews(View view) ;
	/**
	 * 刷新view中内容，通常写在网络请求成功后的回调函数中
	 */
	public abstract void updateViews(Object obj) ;
	
	/**
	 * 不管是什么类型的View，直接一个$方法搞定，类似JQuery
	 * @param viewID
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T extends View> T $(View view,int viewID) {
	    return (T) view.findViewById(viewID);
	}
	
	public void showToast(int text){
		context.showToast(text);
	}
	public void showToast(String text){
		context.showToast(text);
	}
	
	public void updateTheme(int color) {
	}
	
}
