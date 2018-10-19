package com.app.fragment;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.app.model.MenuItem;
import com.app.xstore.R;
import com.app.xstore.caigoukanhuo.CaiGouKanHuoActivity;
import com.app.xstore.cashier.ShoppingCartActivity;
import com.app.xstore.fitting.FittingActivity;
import com.app.xstore.inventory.InventoryActivity;
import com.app.xstore.member.MemberActivity;
import com.app.xstore.mendiancaigouruku.CaiGouRuKuCreateActivity;
import com.app.xstore.mendiancaigouruku.CaiGouRuKuActivity;
import com.app.xstore.mendiandiaochu.MenDianDiaoChuActivity;
import com.app.xstore.mendiandiaoru.MenDianDiaoRuActivity;
import com.app.xstore.mendiankucun.KuCunQueryBySkuActivity;
import com.app.xstore.shangpindangan.ShangPinDangAnActivity;
import com.widget.common.recycler.BaseRecyclerAdapter;
import com.widget.common.recycler.SpacesItemDecoration;


/**
 * 首页
 * @author pythoner
 *
 */
public class Main0Fragment extends BaseFragment{

	public static Main0Fragment newInstance(String param1) {
		Main0Fragment fragment = new Main0Fragment();
        Bundle args = new Bundle();
        args.putString("agrs1", param1);
        fragment.setArguments(args);
        return fragment;
    }
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		return inflater.inflate(R.layout.fragment_main_0, container,false);
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
		initRecyclerView0(view);
		initRecyclerView1(view);
		initRecyclerView2(view);
	}
	
	private void initRecyclerView0(View view){
		RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView0);
		// recyclerView.addItemDecoration(new DividerGridItemDecoration(this));
		recyclerView.addItemDecoration(new SpacesItemDecoration(10));
		recyclerView.setItemAnimator(new DefaultItemAnimator());
		GridLayoutManager manager = new GridLayoutManager(context, 4);
		recyclerView.setLayoutManager(manager);

		final ArrayList<MenuItem> list = new ArrayList<MenuItem>();
		list.add(new MenuItem(7,R.drawable.ic_product48, "商品档案",ShangPinDangAnActivity.class));
		list.add(new MenuItem(8,R.drawable.ic_mendian_caigou64, "采购入库",CaiGouRuKuActivity.class));
		list.add(new MenuItem(9,R.drawable.ic_mendian_diaoru64, "门店调入",MenDianDiaoRuActivity.class));
		list.add(new MenuItem(10,R.drawable.ic_mendian_diaochu64, "门店调出",MenDianDiaoChuActivity.class));
		list.add(new MenuItem(0,R.drawable.icon_main_pandian, "盘点",InventoryActivity.class));
		list.add(new MenuItem(11,R.drawable.ic_mendian_chakucun64, "查库存",KuCunQueryBySkuActivity.class));
		
		BaseRecyclerAdapter<MenuItem> adapter;
		
		recyclerView.setAdapter(adapter=new BaseRecyclerAdapter<MenuItem>(context, list) {

			@Override
			public int onCreateViewLayoutID(int viewType) {
				// TODO Auto-generated method stub
				return R.layout.item_for_recyclerview;
			}

			@Override
			public void onBindViewHolder(
					com.widget.common.recycler.ViewHolder holder,
					MenuItem item, int position) {
				// TODO Auto-generated method stub
				holder.setImageResource(R.id.item_0,item.resID);
				holder.setText(R.id.item_1,item.text);
			}
		});

		adapter.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				Class clazz=list.get(position).clazz;
				if(clazz!=null){
					Intent intent=new Intent(context,list.get(position).clazz);
					startActivity(intent);
				}else{
					showToast(list.get(position).text);
				}
			}
		});
	}
	
	
	private void initRecyclerView1(View view){
		RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView1);
		// recyclerView.addItemDecoration(new DividerGridItemDecoration(this));
		recyclerView.addItemDecoration(new SpacesItemDecoration(10));
		recyclerView.setItemAnimator(new DefaultItemAnimator());
		GridLayoutManager manager = new GridLayoutManager(context, 4);
		recyclerView.setLayoutManager(manager);

		final ArrayList<MenuItem> list = new ArrayList<MenuItem>();
        list.add(new MenuItem(2,R.drawable.icon_main_shoppingcart, "收银台",ShoppingCartActivity.class));
        list.add(new MenuItem(4,R.drawable.icon_main_huiyuan64, "会员",MemberActivity.class));
        list.add(new MenuItem(12,R.drawable.icon_main_product, "调价",null));
        list.add(new MenuItem(13,R.drawable.icon_main_product, "充值",null));
        list.add(new MenuItem(14,R.drawable.icon_main_match, "试穿登记",FittingActivity.class));
        list.add(new MenuItem(15,R.drawable.icon_main_product, "分析报表",null));
        list.add(new MenuItem(16,R.drawable.icon_main_product, "销售指标",null));
        if(context.isCompanyUser()){
	        list.add(new MenuItem(16,R.drawable.icon_main_caigoukanhuo64, "采购看货",CaiGouKanHuoActivity.class));
        }

		BaseRecyclerAdapter<MenuItem> adapter;
		
		recyclerView.setAdapter(adapter=new BaseRecyclerAdapter<MenuItem>(context, list) {

			@Override
			public int onCreateViewLayoutID(int viewType) {
				// TODO Auto-generated method stub
				return R.layout.item_for_recyclerview;
			}

			@Override
			public void onBindViewHolder(
					com.widget.common.recycler.ViewHolder holder,
					MenuItem item, int position) {
				// TODO Auto-generated method stub
				holder.setImageResource(R.id.item_0,item.resID);
				holder.setText(R.id.item_1,item.text);
			}
		});

		adapter.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				Class clazz=list.get(position).clazz;
				if(clazz!=null){
					Intent intent=new Intent(context,clazz);
					startActivity(intent);
				}else{
					showToast(list.get(position).text);
				}
			}
		});
	}
	
	private void initRecyclerView2(View view){
		RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView2);
		// recyclerView.addItemDecoration(new DividerGridItemDecoration(this));
		recyclerView.addItemDecoration(new SpacesItemDecoration(10));
		recyclerView.setItemAnimator(new DefaultItemAnimator());
		GridLayoutManager manager = new GridLayoutManager(context, 4);
		recyclerView.setLayoutManager(manager);

		final ArrayList<MenuItem> list = new ArrayList<MenuItem>();
		list.add(new MenuItem(17,R.drawable.icon_main_product, "标签打印",null));
		list.add(new MenuItem(18,R.drawable.icon_main_product, "用户帮助",null));
		list.add(new MenuItem(19,R.drawable.icon_main_product, "门店日志",null));
		list.add(new MenuItem(20,R.drawable.icon_main_product, "时尚资讯",null));
		list.add(new MenuItem(21,R.drawable.icon_main_product, "商城",null));
        

		BaseRecyclerAdapter<MenuItem> adapter;
		
		recyclerView.setAdapter(adapter=new BaseRecyclerAdapter<MenuItem>(context, list) {

			@Override
			public int onCreateViewLayoutID(int viewType) {
				// TODO Auto-generated method stub
				return R.layout.item_for_recyclerview;
			}

			@Override
			public void onBindViewHolder(
					com.widget.common.recycler.ViewHolder holder,
					MenuItem item, int position) {
				// TODO Auto-generated method stub
				holder.setImageResource(R.id.item_0,item.resID);
				holder.setText(R.id.item_1,item.text);
			}
		});

		adapter.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				Class clazz=list.get(position).clazz;
				if(clazz!=null){
					Intent intent=new Intent(context,list.get(position).clazz);
					startActivity(intent);
				}else{
					showToast(list.get(position).text);
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
//		if (adapter != null) {
//			adapter.notifyDataSetChanged();
//		}
	}
	
	private void updateTheme(View view) {
//		if(context!=null&&view!=null){
//			context.setThemeDrawable(context,R.id.btn_ok);
//		}
	}
	
}
