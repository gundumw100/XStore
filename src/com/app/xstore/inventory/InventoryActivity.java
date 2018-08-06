package com.app.xstore.inventory;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.simple.eventbus.EventBus;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;

import com.android.volley.Response.Listener;
import com.app.fragment.InventoryFragment;
import com.app.model.LocQty;
import com.app.model.ProdPreChcekData;
import com.app.model.response.UploadProdPreCheckDataResponse;
import com.app.net.Commands;
import com.app.widget.InventoryDialog;
import com.app.widget.SimplePairListPopupWindow;
import com.app.widget.VerificationCodeDialog;
import com.app.xstore.App;
import com.app.xstore.BaseActivity;
import com.app.xstore.HelpActivity;
import com.app.xstore.R;

/**
 * 盘点
 * @author pythoner
 * 
 */
public class InventoryActivity extends BaseActivity{

	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_inventory);
		context = this;
		initActionBar(App.user.getShopInfo().getShop_name(), "扫描", null);
		initViews();
	}

	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		View btn_add=findViewById(R.id.btn_add);
		btn_add.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				final InventoryDialog d=new InventoryDialog(context,"预点",null);
				d.setOnClickListener(new InventoryDialog.OnClickListener() {

					@Override
					public void onClick(View v, ProdPreChcekData item) {
						// TODO Auto-generated method stub
						LocQty locQty=new LocQty();
						locQty.setShelf_code(item.getShelf_code());
						locQty.setTotal_qty(Integer.parseInt(item.getTotal_qty()));
						
						List<LocQty> list=new  ArrayList<LocQty>();
						list.add(locQty);
						doCommandUploadProdPreCheckData(list);
						
						d.dismiss();
					}
					
				});
				d.show();
			}
		});
		View btn_more=findViewById(R.id.btn_more);
		btn_more.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showSimplePopupWindow(v);
			}
		});
		setThemeDrawable(this, btn_add, R.drawable.btn_pressed);
		setThemeDrawable(this, btn_more, R.drawable.btn_pressed);
	}
	
	private void doCommandUploadProdPreCheckData(List<LocQty> list){
		String shop_code=App.user.getShopInfo().getShop_code();
		String create_user=App.user.getUserInfo().getUser_code();
		Commands.doCommandUploadProdPreCheckData(context, shop_code,create_user,list, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
//				Log.i("tag", response.toString());
				//{"ErrMessage":"","Result":true,"ErrSysTrackMessage":"","ErrSysMessage":"","Message":"OK","Precheck_Info":[{"create_time_string":"2016-02-14","id":1,"total_qty":0,"create_user":"","shelf_code":"","shop_id":"S001"},{"create_time_string":"2016-02-14","id":3,"total_qty":10,"create_user":"","shelf_code":"001","shop_id":"S001"}]}
				if(isSuccess(response)){
					UploadProdPreCheckDataResponse obj=mapperToObject(response, UploadProdPreCheckDataResponse.class);
					if(obj!=null){
						List<ProdPreChcekData> list=obj.getPrecheck_Info();
						if(list!=null){
							EventBus.getDefault().post(App.EVENT_UPDATE_INVENTORY);
						}
					}
				}
			}
		});
	}
	
	@Override
	public void updateViews(Object obj) {
		// TODO Auto-generated method stub

	}
	@Override
	public void doRightButtonClick(View v) {
		// TODO Auto-generated method stub
		Intent intent=new Intent(context,CreateDocumentActivity.class);
		startActivity(intent);
	}
	
	private void showSimplePopupWindow(View v){
		ArrayList<Pair<Integer, String>> beans = new ArrayList<Pair<Integer, String>>();
		beans.add(new Pair<Integer, String>(1, "扫描清单"));
		beans.add(new Pair<Integer, String>(5, "清空"));
		beans.add(new Pair<Integer, String>(4, "帮助"));
//		beans.add(new Pair<Integer, String>(6, "离线盘点"));
		
		View view = LayoutInflater.from(context).inflate(R.layout.popupwindow_simple, null);
		final SimplePairListPopupWindow<Integer, String> popupWindow = new SimplePairListPopupWindow<Integer, String>(context,view, (int)App.res.getDimension(R.dimen.popupwindow_width), beans);
		popupWindow.showAsDropDown(v, 0, 0);
		popupWindow.setOnItemClickListener(new SimplePairListPopupWindow.OnItemClickListener<Integer, String>() {

			@Override
			public void onItemClick(int position, Pair<Integer, String> pair) {
				// TODO Auto-generated method stub
				Intent intent=null;
				switch (pair.first) {
				case 1://扫描清单
					intent=new Intent(context,DocumentListActivity.class);
					startActivity(intent);
					break;
				case 4://帮助
					intent=new Intent(context,HelpActivity.class);
					intent.putExtra("FILE", "pandian/pandian.htm");
					startActivity(intent);
					break;
				case 5://清空
					final VerificationCodeDialog d=new VerificationCodeDialog(context);
					d.setOnClickListener(new VerificationCodeDialog.OnClickListener() {
						
						@Override
						public void onClick(View v, String text) {
							// TODO Auto-generated method stub
							EventBus.getDefault().post(App.EVENT_CLEAR_SHELF);
						}
					});
					d.show();
					break;
//				case 6://离线扫描
//					intent=new Intent(context,OfflineDocumentListActivity.class);
//					intent.putExtra("CanUpload", true);
//					startActivity(intent);
//					break;
				default:
					showToast(R.string.error_unknown_function);
					break;
				}
			}
		});
	}
	
	@Override
	public void updateTheme(int color) {
		super.updateTheme(color);
		Fragment fragment=getSupportFragmentManager().findFragmentById(R.id.fragment_inventory);
		if(fragment!=null){
			InventoryFragment f=(InventoryFragment)fragment;
			f.updateTheme(color);
		}
	}
	
}
