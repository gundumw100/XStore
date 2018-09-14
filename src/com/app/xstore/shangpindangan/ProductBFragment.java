package com.app.xstore.shangpindangan;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.fragment.BaseFragment;
import com.app.xstore.R;
import com.widget.flowlayout.FlowLayout;
import com.widget.flowlayout.TagAdapter;
import com.widget.flowlayout.TagFlowLayout;

public class ProductBFragment extends BaseFragment{

	public static ProductBFragment newInstance(String param1) {
		ProductBFragment fragment = new ProductBFragment();
        Bundle args = new Bundle();
        args.putString("agrs1", param1);
        fragment.setArguments(args);
        return fragment;
    }
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		return inflater.inflate(R.layout.fragment_product_b, container,false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		initViews(view);
	}
	
	@Override
	public void initViews(View view){
		initFengeFlowLayout(view);
		initBanxingFlowLayout(view);
		initCaizhiFlowLayout(view);
		initHouduFlowLayout(view);
		initRenqunFlowLayout(view);
	}
	
	private TagFlowLayout flowLayout_fenge;
	private TagAdapter<ProdLabel> fengeAdapter;
	private List<ProdLabel> fengeList=new ArrayList<ProdLabel>();
	private void initFengeFlowLayout(View view){
		flowLayout_fenge=(TagFlowLayout)view.findViewById(R.id.flowLayout_fenge);
		flowLayout_fenge.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
			
			@Override
			public boolean onTagClick(FlowLayout parent, View view, int position) {
				// TODO Auto-generated method stub
				return true;
			}
		});
		
		flowLayout_fenge.setmOnTagLongClickListener(new TagFlowLayout.OnTagLongClickListener() {
			
			@Override
			public boolean onTagLongClick(FlowLayout parent, View view, final int position) {
				// TODO Auto-generated method stub
				return true;
			}
		});
		
		fengeList.add(new ProdLabel("0001", "休闲"));
		fengeList.add(new ProdLabel("0002", "清新"));
		fengeList.add(new ProdLabel("0003", "嘻哈风"));
		fengeList.add(new ProdLabel("0004", "中国风"));
		fengeList.add(new ProdLabel("0005", "商务"));
		fengeList.add(new ProdLabel("0006", "潮流"));
		fengeList.add(new ProdLabel("0007", "复古"));
		fengeList.add(new ProdLabel("0008", "乡村"));
		fengeList.add(new ProdLabel("0009", "可爱"));
		
		notifyDataChangedFenge();
	}
	
	private void notifyDataChangedFenge(){
		if(fengeAdapter==null){
			flowLayout_fenge.setAdapter(fengeAdapter=new TagAdapter<ProdLabel>(fengeList){
				@Override
				public View getView(FlowLayout parent, int position, ProdLabel item){
					TextView tv = (TextView) LayoutInflater.from(context).inflate(R.layout.item_text,flowLayout_fenge, false);
					tv.setText(item.getDescription());
					tv.setTag(item);
					return tv;
				}
			});
		}else{
			flowLayout_fenge.notifyDataChanged();
		}
	}
	
	private TagFlowLayout flowLayout_banxing;
	private TagAdapter<ProdLabel> banxingAdapter;
	private List<ProdLabel> banxingList=new ArrayList<ProdLabel>();
	private void initBanxingFlowLayout(View view){
		flowLayout_banxing=(TagFlowLayout)view.findViewById(R.id.flowLayout_banxing);
		banxingList.add(new ProdLabel("0001", "标准"));
		banxingList.add(new ProdLabel("0002", "修身"));
		banxingList.add(new ProdLabel("0003", "宽松"));
		banxingList.add(new ProdLabel("0004", "加宽"));
		banxingList.add(new ProdLabel("0005", "直筒"));
		banxingList.add(new ProdLabel("0006", "斗篷"));
		
		notifyDataChangedBanxing();
	}
	
	private void notifyDataChangedBanxing(){
		if(banxingAdapter==null){
			flowLayout_banxing.setAdapter(banxingAdapter=new TagAdapter<ProdLabel>(banxingList){
				@Override
				public View getView(FlowLayout parent, int position, ProdLabel item){
					TextView tv = (TextView) LayoutInflater.from(context).inflate(R.layout.item_text,flowLayout_banxing, false);
					tv.setText(item.getDescription());
					tv.setTag(item);
					return tv;
				}
			});
		}else{
			flowLayout_banxing.notifyDataChanged();
		}
	}
	
	
	private TagFlowLayout flowLayout_caizhi;
	private TagAdapter<ProdLabel> caizhiAdapter;
	private List<ProdLabel> caizhiList=new ArrayList<ProdLabel>();
	private void initCaizhiFlowLayout(View view){
		flowLayout_caizhi=(TagFlowLayout)view.findViewById(R.id.flowLayout_caizhi);
		caizhiList.add(new ProdLabel("0001", "棉"));
		caizhiList.add(new ProdLabel("0002", "涤纶"));
		caizhiList.add(new ProdLabel("0003", "亚麻"));
		caizhiList.add(new ProdLabel("0004", "桑蚕丝"));
		caizhiList.add(new ProdLabel("0005", "莫代尔"));
		caizhiList.add(new ProdLabel("0006", "莱卡"));
		caizhiList.add(new ProdLabel("0006", "金丝绒"));
		caizhiList.add(new ProdLabel("0006", "人造革"));
		caizhiList.add(new ProdLabel("0006", "牛仔布"));
		caizhiList.add(new ProdLabel("0006", "天鹅绒"));
		caizhiList.add(new ProdLabel("0006", "棉纶"));
		caizhiList.add(new ProdLabel("0006", "氨纶"));
		caizhiList.add(new ProdLabel("0006", "聚酯纤维"));
		caizhiList.add(new ProdLabel("0006", "羊毛"));
		caizhiList.add(new ProdLabel("0006", "腈纶"));
		caizhiList.add(new ProdLabel("0006", "兔毛"));
		
		notifyDataChangedCaizhi();
	}
	
	private void notifyDataChangedCaizhi(){
		if(caizhiAdapter==null){
			flowLayout_caizhi.setAdapter(caizhiAdapter=new TagAdapter<ProdLabel>(caizhiList){
				@Override
				public View getView(FlowLayout parent, int position, ProdLabel item){
					TextView tv = (TextView) LayoutInflater.from(context).inflate(R.layout.item_text,flowLayout_caizhi, false);
					tv.setText(item.getDescription());
					tv.setTag(item);
					return tv;
				}
			});
		}else{
			flowLayout_banxing.notifyDataChanged();
		}
	}
	
	private TagFlowLayout flowLayout_houdu;
	private TagAdapter<ProdLabel> houduAdapter;
	private List<ProdLabel> houduList=new ArrayList<ProdLabel>();
	private void initHouduFlowLayout(View view){
		flowLayout_houdu=(TagFlowLayout)view.findViewById(R.id.flowLayout_houdu);
		houduList.add(new ProdLabel("0001", "轻薄"));
		houduList.add(new ProdLabel("0001", "超薄"));
		houduList.add(new ProdLabel("0002", "适中"));
		houduList.add(new ProdLabel("0002", "中厚"));
		houduList.add(new ProdLabel("0003", "加厚"));
		houduList.add(new ProdLabel("0004", "加绒加厚"));
		
		notifyDataChangedHoudu();
	}
	
	private void notifyDataChangedHoudu(){
		if(houduAdapter==null){
			flowLayout_houdu.setAdapter(houduAdapter=new TagAdapter<ProdLabel>(houduList){
				@Override
				public View getView(FlowLayout parent, int position, ProdLabel item){
					TextView tv = (TextView) LayoutInflater.from(context).inflate(R.layout.item_text,flowLayout_houdu, false);
					tv.setText(item.getDescription());
					tv.setTag(item);
					return tv;
				}
			});
		}else{
			flowLayout_houdu.notifyDataChanged();
		}
	}
	
	private TagFlowLayout flowLayout_renqun;
	private TagAdapter<ProdLabel> renqunAdapter;
	private List<ProdLabel> renqunList=new ArrayList<ProdLabel>();
	private void initRenqunFlowLayout(View view){
		flowLayout_renqun=(TagFlowLayout)view.findViewById(R.id.flowLayout_renqun);
		renqunList.add(new ProdLabel("0001", "青少年"));
		renqunList.add(new ProdLabel("0001", "青年"));
		renqunList.add(new ProdLabel("0002", "男士"));
		renqunList.add(new ProdLabel("0002", "女士"));
		renqunList.add(new ProdLabel("0003", "中老年"));
		renqunList.add(new ProdLabel("0004", "少女"));
		renqunList.add(new ProdLabel("0004", "儿童"));
		renqunList.add(new ProdLabel("0004", "学生"));
		renqunList.add(new ProdLabel("0004", "情侣"));
		renqunList.add(new ProdLabel("0004", "大码人群"));
		renqunList.add(new ProdLabel("0004", "轻熟女"));
		renqunList.add(new ProdLabel("0004", "亲子装"));
		renqunList.add(new ProdLabel("0004", "成熟"));
		renqunList.add(new ProdLabel("0004", "胖mm"));
		renqunList.add(new ProdLabel("0004", "孕妇"));
		renqunList.add(new ProdLabel("0004", "中性"));
		
		notifyDataChangedRenqun();
	}
	
	private void notifyDataChangedRenqun(){
		if(renqunAdapter==null){
			flowLayout_renqun.setAdapter(renqunAdapter=new TagAdapter<ProdLabel>(renqunList){
				@Override
				public View getView(FlowLayout parent, int position, ProdLabel item){
					TextView tv = (TextView) LayoutInflater.from(context).inflate(R.layout.item_text,flowLayout_renqun, false);
					tv.setText(item.getDescription());
					tv.setTag(item);
					return tv;
				}
			});
		}else{
			flowLayout_renqun.notifyDataChanged();
		}
	}
	
	@Override
	public void updateViews(Object obj) {
	}
	
//	@Subscriber
//	void updateByEventBus(String event) {
//		if(event.equals(App.EVENT_SAVE_FITTING)){
//		}
//	}
	
}
