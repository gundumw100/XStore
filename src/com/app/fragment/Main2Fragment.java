package com.app.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.xstore.R;
import com.widget.refreshlayout.Constants;
import com.widget.refreshlayout.IIndicator;
import com.widget.refreshlayout.MovingStatus;
import com.widget.refreshlayout.RefreshingListenerAdapter;
import com.widget.refreshlayout.SmoothRefreshLayout;


/**
 * 试衣
 * @author pythoner
 *
 */
public class Main2Fragment extends BaseFragment{

	private SmoothRefreshLayout mRefreshLayout;
    private TextView mTextView;
    private Handler mHandler = new Handler();
    private int mCount = 0;
	public static Main2Fragment newInstance(String param1) {
		Main2Fragment fragment = new Main2Fragment();
        Bundle args = new Bundle();
        args.putString("agrs1", param1);
        fragment.setArguments(args);
        return fragment;
    }
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		return inflater.inflate(R.layout.fragment_main_2, container,false);
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
		mTextView = (TextView)view.findViewById(R.id.textView_with_textView_desc);
		mRefreshLayout = (SmoothRefreshLayout)view.findViewById(R.id.refreshLayout);
//		mRefreshLayout.setHeaderView(new ClassicHeader(context));
//		mRefreshLayout.setFooterView(new ClassicFooter(context));
//		refreshLayout.setDisableLoadMore(false);
//        refreshLayout.setDisablePerformRefresh(true);
//        refreshLayout.setDisablePerformLoadMore(true);
//        refreshLayout.getHeaderView().getView().setVisibility(View.GONE);
//        refreshLayout.getFooterView().getView().setVisibility(View.GONE);
//		refreshLayout.setOnRefreshListener(new RefreshingListenerAdapter() {
//			@Override
//			public void onRefreshBegin(boolean isRefresh) {
//				mHandler.postDelayed(new Runnable() {
//					@Override
//					public void run() {
//						refreshLayout.refreshComplete();
//					}
//				}, 2000);
//			}
//		});
		
		mRefreshLayout.setEnableKeepRefreshView(true);
        mRefreshLayout.setDisableLoadMore(false);
        mRefreshLayout.setDisablePerformLoadMore(true);
//        mRefreshLayout.getFooterView().getView().setVisibility(View.GONE);
        mRefreshLayout.setOnRefreshListener(new RefreshingListenerAdapter() {
            @Override
            public void onRefreshBegin(boolean isRefresh) {
                mCount++;
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mRefreshLayout.refreshComplete();
                        String times = "" + mCount;
                        mTextView.setText(times);
                    }
                }, 2000);
            }
        });
        mRefreshLayout.autoRefresh(true);
        mRefreshLayout.setIndicatorOffsetCalculator(new IIndicator.IOffsetCalculator() {
            @Override
            public float calculate(@MovingStatus int status, int currentPos, float
                    offset) {
                if (status == Constants.MOVING_HEADER) {
                    if (offset < 0) {
                        //如果希望拖动缩回时类似QQ一样没有阻尼效果，阻尼效果只存在于下拉则可以在此返回offset
                        //如果希望拖动缩回时类似QQ一样有阻尼效果，那么请注释掉这个判断语句
                        return offset;
                    }
                    return (float) Math.pow(Math.pow(currentPos / 2, 1.28d) + offset, 1 / 1.28d) *
                            2 - currentPos;
                } else if (status == Constants.MOVING_FOOTER) {
                    if (offset > 0) {
                        //如果希望拖动缩回时类似QQ一样没有阻尼效果，阻尼效果只存在于上拉则可以在此返回offset
                        //如果希望拖动缩回时类似QQ一样有阻尼效果，那么请注释掉这个判断语句
                        return offset;
                    }
                    return -((float) Math.pow(Math.pow(currentPos / 2, 1.28d) - offset, 1 / 1.28d) *
                            2 - currentPos);
                } else {
                    if (offset > 0) {
                        return (float) Math.pow(offset, 1 / 1.28d) * 2;
                    } else if (offset < 0) {
                        return -(float) Math.pow(-offset, 1 / 1.28d) * 2;
                    } else {
                        return offset;
                    }
                }
            }
        });
		
		
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
