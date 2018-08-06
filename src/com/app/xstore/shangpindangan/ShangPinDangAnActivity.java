package com.app.xstore.shangpindangan;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
//import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response.Listener;
import com.app.net.Commands;
import com.app.widget.SimpleListPopupWindow;
import com.app.widget.SimpleListPopupWindow.OnItemClickListener;
import com.app.xstore.App;
import com.app.xstore.BaseActivity;
import com.app.xstore.R;
import com.app.xstore.shangpindangan.SimpleTextListDialog.OnCheckedChangeListener;
import com.base.util.D;
import com.base.util.comm.DisplayUtils;
import com.squareup.picasso.Picasso;
import com.widget.flowlayout.FlowLayout;
import com.widget.imagepicker.ImageConfig;
import com.widget.imagepicker.ImageSelector;
import com.widget.imagepicker.ImageSelectorActivity;
import com.widget.imagepicker.PicassoLoader;

/**
 * 
 * @author pythoner
 * 
 */
public class ShangPinDangAnActivity extends BaseActivity implements View.OnClickListener{

	private EditText et_productSku;
	private AutoCompleteTextView et_productName;
	private AutoCompleteAdapter<ProductDangAn> adapter;
	private TextView tv_productSku,tv_year,tv_brand,tv_season,tv_category,tv_color,tv_size,tv_date,tv_other,tv_jldw,tv_cw;
	private TextView tv_cs;
	private TextView btn_addYear;
	private int width=100;//每个显示小图片的宽度
	private EditText et_jh_price,et_ls_price,et_zxs;
	private  final int MAX_SIZE=2;//
	private FlowLayout flowLayout;
	private String goodsSn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shangpindangan);
		context = this;
		initActionBar("商品档案", null, null);
		goodsSn=getIntent().getStringExtra("goodsSn");
		initViews();
		initScanner(new OnScannerResult() {
			
			@Override
			public void onResult(String data) {
				// TODO Auto-generated method stub
				et_productSku.setText(data);
			}
		});
	}

	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		final TextView btn_generateCode=$(R.id.btn_generateCode);
		btn_generateCode.setOnClickListener(this);
		$(R.id.btn_query).setOnClickListener(this);
		$(R.id.btn_print).setOnClickListener(this);
		$(R.id.btn_productSku_query).setOnClickListener(this);
		$(R.id.btn_save).setOnClickListener(this);
		//款号
		tv_productSku=$(R.id.tv_productSku);
		et_productSku=$(R.id.et_productSku);
		et_productSku.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				btn_generateCode.setEnabled(s.length()==0);
				tv_productSku.setText("");
				tv_productSku.setTag(null);
				if(!et_productName.isEnabled()){
					et_productName.setEnabled(true);
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		if(!isEmpty(goodsSn)){
			et_productSku.setText(goodsSn);
		}
		et_productName=$(R.id.et_productName);
		
		List<ProductDangAn> products=DataSupport.order("id desc").limit(200).find(ProductDangAn.class);
		adapter = new AutoCompleteAdapter<ProductDangAn>(context, products,10);
		adapter.setDefaultMode(AutoCompleteAdapter.MODE_CONTAINS);// 设置匹配模式
//	    adapter.setDefaultMode(AutoCompleteAdapter.MODE_STARTSWITH | AutoCompleteAdapter.MODE_SPLIT);
		adapter.setSupportPreview(true);// 支持使用特殊符号进行预览提示内容，默认为'@'
		final int simpleItemHeight = adapter.getSimpleItemHeight();
		
//		adapter.setOnFilterResultsListener(new AutoCompleteAdapter.OnFilterResultsListener() {
//	        @Override
//	        public void onFilterResultsListener(int count) {
//	        }
//	    });

		adapter.setOnSimpleItemDeletedListener(new AutoCompleteAdapter.OnSimpleItemDeletedListener<ProductDangAn>() {
	        @Override
	        public void onSimpleItemDeletedListener(ProductDangAn value) {
	        	DataSupport.delete(ProductDangAn.class, value.getId());
	        }
	    });
	
		et_productName.setAdapter(adapter);
		et_productName.setThreshold(1);
		// 设置下拉提示框中底部的提示
	    // et_productName.setCompletionHint("最多显示"+maxMatch+"条最近记录");
		// 设置下拉提示框的高度为200dp
	    // mAutoCompleteTv.setDropDownHeight();// 或XML中为android:dropDownHeight="200dp"

		
		$(R.id.btn_scan).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				doScan(resultHandler);
			}
		});
		final ImageView iv_help_productName=$(R.id.iv_help_productName);
		iv_help_productName.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				showSimpleTextPopupWindow(iv_help_productName,"商品名称不能重复，若款号相似请加‘后缀’区分，可参照示例。");
			}
		});
		//年份
		Calendar date = Calendar.getInstance();
        String year = String.valueOf(date.get(Calendar.YEAR));
        
		tv_year=$(R.id.tv_year);
		tv_year.setText(year);
		String year_code=tv_year.getText().toString().substring(2);
		tv_year.setTag(year_code);
		tv_year.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showYearPopupWindow(tv_year);
			}
		});
		btn_addYear=$(R.id.btn_addYear);
		btn_addYear.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showYearPopupWindow(tv_year);
			}
		});
		//品牌
		tv_brand=$(R.id.tv_brand);
		tv_brand.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showBrandPopupWindow(tv_brand);
			}
		});
		
		TextView btn_addBrand=$(R.id.btn_addBrand);
		btn_addBrand.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Commands.doCommandGetProdBrandList(context, null,new Listener<JSONObject>() {
					
					@Override
					public void onResponse(JSONObject response) {
						// TODO Auto-generated method stub
//						Log.i("tag", "response="+response.toString());
						if (isSuccess(response)) {
							GetProdBrandListResponse obj=mapperToObject(response, GetProdBrandListResponse.class);
							brandList=obj.getInfo();
							if(!isFinishing()){
								showBrandDialog();
							}
						}
					}
				});
				
			}
		});

		//季节
		tv_season=$(R.id.tv_season);
		tv_season.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showSeasonPopupWindow(tv_season);
			}
		});
		$(R.id.btn_addSeason).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Commands.doCommandGetProdSeasonList(context, null, new Listener<JSONObject>() {
					
					@Override
					public void onResponse(JSONObject response) {
						// TODO Auto-generated method stub
//						Log.i("tag", "response="+response.toString());
						if (isSuccess(response)) {
							GetProdSeasonListResponse obj=mapperToObject(response, GetProdSeasonListResponse.class);
							seasonList=obj.getInfo();
							if(!isFinishing()){
								showSeasonDialog();
							}
						}
					}
				});
				
			}
		});
		
		//类别
		tv_category=$(R.id.tv_category);
		tv_category.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showSortPopupWindow(tv_category);
			}
		});
		
		TextView btn_addCategory=$(R.id.btn_addCategory);
		btn_addCategory.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Commands.doCommandGetProdSortList(context, null, new Listener<JSONObject>() {
					
					@Override
					public void onResponse(JSONObject response) {
						// TODO Auto-generated method stub
//						Log.i("tag", "response="+response.toString());
						if (isSuccess(response)) {
							GetProdSortListResponse obj=mapperToObject(response, GetProdSortListResponse.class);
							sortList=obj.getInfo();
							if(!isFinishing()){
								showCategoryDialog();
							}
						}
					}
				});
				
			}
		});
		/////////////////////////////////////////////////////////////////////////////////
		//颜色
		tv_color=$(R.id.tv_color);
		tv_color.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showColorPopupWindow(tv_color);
			}
		});
		
//		colorList.add(new Pair<Integer, String>(0, "均色"));
		TextView btn_addColor=$(R.id.btn_addColor);
		btn_addColor.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Commands.doCommandGetProdColorList(context, null, new Listener<JSONObject>() {
					
					@Override
					public void onResponse(JSONObject response) {
						// TODO Auto-generated method stub
//						Log.i("tag", "response="+response.toString());
						if (isSuccess(response)) {
							GetProdColorListResponse obj=mapperToObject(response, GetProdColorListResponse.class);
							colorList=obj.getInfo();
							if(!isFinishing()){
								showColorDialog();
							}
							
						}
					}
				});
			}
		});
		//尺码
		tv_size=$(R.id.tv_size);
		tv_size.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showSizePopupWindow(tv_size);
			}
		});
		
//		sizeList.add(new Pair<Integer, String>(0, "均码"));
		TextView btn_addSize=$(R.id.btn_addSize);
		btn_addSize.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Commands.doCommandGetProdSpecList(context, null, new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						// TODO Auto-generated method stub
//						Log.i("tag", "response="+response.toString());
						if (isSuccess(response)) {
							GetProdSpecListResponse obj=mapperToObject(response, GetProdSpecListResponse.class);
							specList=obj.getInfo();
							if(!isFinishing()){
								showSpecDialog();
							}
						}
					}
				});
			}
		});
		//////////////////////////////////////////////////////////////////////////
		//上架日期
		final Calendar c = Calendar.getInstance();
        final java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd",Locale.CHINA);
        
		tv_date=$(R.id.tv_date);
		tv_date.setText(format.format(c.getTime()));
		tv_date.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				c.setTimeInMillis(System.currentTimeMillis());
//				List<String> dates=new ArrayList<String>();
//				for(int i=0;i<12;i++){
//					dates.add(format.format(c.getTime()));
//					c.add(Calendar.MONTH, 1);
//				}
//				
//				View view = LayoutInflater.from(context).inflate(R.layout.popupwindow_simple, null);
//				SimpleListPopupWindow<String> popupWindow=new SimpleListPopupWindow<String>(context, view, tv_date.getWidth(), dates);
//				popupWindow.showAsDropDown(tv_date, 0, 0);
//				popupWindow.setOnItemClickListener(new SimpleListPopupWindow.OnItemClickListener<String>() {
//					
//					@Override
//					public void onItemClick(int position, String item) {
//						// TODO Auto-generated method stub
//						tv_date.setText(item+"-01");
//						tv_date.setTag(item+"-01");
//					}
//				});
				
				Calendar c = Calendar.getInstance();
		        int mYear = c.get(Calendar.YEAR);
		        int mMonth = c.get(Calendar.MONTH);
		        int mDay = c.get(Calendar.DAY_OF_MONTH);
		        
				DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
					
					@Override
					public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
						// TODO Auto-generated method stub
						Calendar calendar=Calendar.getInstance();
						calendar.set(year, month, dayOfMonth);
						String date=formatTime(calendar.getTimeInMillis(), "yyyy-MM-dd");
						tv_date.setText(date);
						tv_date.setTag(date);
					}
				},mYear,mMonth,mDay);

                datePickerDialog.show();
                
			}
		});

		$(R.id.btn_date).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				tv_date.performClick();
			}
		});
		//其他
		tv_other=$(R.id.tv_other);
		tv_other.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showOtherPopupWindow(tv_other);
			}
		});
		
		TextView btn_other=$(R.id.btn_other);
		btn_other.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Commands.doCommandGetProdOtherList(context, null, new Listener<JSONObject>() {
					
					@Override
					public void onResponse(JSONObject response) {
						// TODO Auto-generated method stub
//						Log.i("tag", "response="+response.toString());
						if (isSuccess(response)) {
							GetProdOtherListResponse obj=mapperToObject(response, GetProdOtherListResponse.class);
							otherList=obj.getInfo();
							if(!isFinishing()){
								showOtherDialog();
							}
						}
					}
				});
			}
		});
		//厂商
		tv_cs=$(R.id.tv_cs);
		tv_cs.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showCsPopupWindow(tv_cs);
			}
		});
		
		TextView btn_cs=$(R.id.btn_cs);
		btn_cs.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Commands.doCommandGetProdCsList(context, null, new Listener<JSONObject>() {
					
					@Override
					public void onResponse(JSONObject response) {
						// TODO Auto-generated method stub
//						Log.i("tag", "response="+response.toString());
						if (isSuccess(response)) {
							GetProdCsListResponse obj=mapperToObject(response, GetProdCsListResponse.class);
							csList=obj.getInfo();
							if(!isFinishing()){
								showCsDialog();
							}
						}
					}
				});
			}
		});
		
		//计量单位
		tv_jldw=$(R.id.tv_jldw);
		tv_jldw.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showJLDWPopupWindow(tv_jldw);
			}
		});
		
		TextView btn_jldw=$(R.id.btn_jldw);
		btn_jldw.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Commands.doCommandGetProdJLDWList(context, null, new Listener<JSONObject>() {
					
					@Override
					public void onResponse(JSONObject response) {
						// TODO Auto-generated method stub
//						Log.i("tag", "response="+response.toString());
						if (isSuccess(response)) {
							GetProdJLDWListResponse obj=mapperToObject(response, GetProdJLDWListResponse.class);
							jldwList=obj.getInfo();
							if(!isFinishing()){
								showJldwDialog();
							}
						}
					}
				});
			}
		});
		
		//仓位
		tv_cw=$(R.id.tv_cw);
		tv_cw.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showCWPopupWindow(tv_cw);
			}
		});
		
		TextView btn_cw=$(R.id.btn_cw);
		btn_cw.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Commands.doCommandGetProdCwList(context, null, new Listener<JSONObject>() {
					
					@Override
					public void onResponse(JSONObject response) {
						// TODO Auto-generated method stub
//						Log.i("tag", "response="+response.toString());
						if (isSuccess(response)) {
							GetProdCwListResponse obj=mapperToObject(response, GetProdCwListResponse.class);
							cwList=obj.getInfo();
							if(!isFinishing()){
								showCwDialog();
							}
						}
					}
				});
			}
		});
				
		et_jh_price=$(R.id.et_jh_price);
		et_ls_price=$(R.id.et_ls_price);
		et_zxs=$(R.id.et_zxs);
		//////////////////////////////////////////////////////////////////////////
		
		initFlowLayout();
		
	}

	@Override
	public void updateViews(Object obj) {
		// TODO Auto-generated method stub

	}

	private void initFlowLayout(){
		width=(App.config.getScreenWidth()-2*DisplayUtils.dip2px(context, 8))/4;
		flowLayout=$(R.id.flowLayout);
		final ImageView iv_addImage=$(R.id.iv_addImage);
		iv_addImage.setLayoutParams(new LinearLayout.LayoutParams(width,width));
		iv_addImage.setPadding(4, 4, 4, 4);
		iv_addImage.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				openImageSelector(flowLayout);
			}
		});
		
		judgeAddImageViewVisibleOrNot();
	}
	
	private void openImageSelector(FlowLayout flowLayout){
		this.flowLayout=flowLayout;
		ImageConfig imageConfig
        = new ImageConfig.Builder(new PicassoLoader())
//        .steepToolBarColor(getResources().getColor(R.color.blue))// 修改状态栏颜色 
//        .titleBgColor(getResources().getColor(R.color.blue))// 标题的背景颜色 
//        .titleSubmitTextColor(getResources().getColor(R.color.white))// 提交按钮字体的颜色 
//        .titleTextColor(getResources().getColor(R.color.white))// 标题颜色
//        .crop()  // (截图默认配置：关闭    比例 1：1    输出分辨率  500*500)
//        .singleSelect()// 开启单选   （默认为多选） 
        .mutiSelect()// 开启多选   （默认为多选） 
//        .mutiSelectMaxSize(9)// 多选时的最大数量   （默认 9 张）
        .mutiSelectMaxSize(MAX_SIZE-flowLayout.getChildCount())// 多选时的最大数量   （默认 9 张）
        .showCamera()// 开启拍照功能 （默认关闭）
//        .pathList(paths)// 已选择的图片路径,需要及时清空，否则会重复
        .filePath("/xStore/picture")// 拍照后存放的图片路径（默认 /temp/picture） （会自动创建）
        .build();
		
		ImageSelector.open(this, imageConfig);   // 开启图片选择器
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == ImageSelector.IMAGE_REQUEST_CODE) {
			if (data != null && flowLayout != null) {
				List<String> list = data.getStringArrayListExtra(ImageSelectorActivity.EXTRA_RESULT);
				List<File> files=new ArrayList<File>();
				
				for (String path : list) {
					File file = new File(path);
					files.add(file);
					addImageViewToFlowLayout(flowLayout,file);
				}
				
				judgeAddImageViewVisibleOrNot();
				
				luban(files);
			}
		}
	}
	
	private void addImageViewToFlowLayout(final FlowLayout flowLayout,final File file){
		LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(width,width);
		params.gravity=Gravity.CENTER_HORIZONTAL;
		final ImageView child=new ImageView(context);
		child.setTag(file);
		child.setLayoutParams(params);
		child.setPadding(4, 4, 4, 4);
		child.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String url=null;
				url="file://"+file.getPath();
				//file:///storage/emulated/0/CloudStorePhoto/1512543851772.jpg
				changeToImageActivity(child,url);
			}
		});
		child.setOnLongClickListener(new View.OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				String message="删除图片吗？";
				D.showDialog(context, message, "确定","取消", new D.OnPositiveListener() {
					
					@Override
					public void onPositive() {
						// TODO Auto-generated method stub
						flowLayout.removeView(child);
						judgeAddImageViewVisibleOrNot();
						
					}
				});
				return true;
			}
		});
		flowLayout.addView(child,flowLayout.getChildCount()-1);
		loadImageByPicasso(child,width,file);//从本地加载图片
	}
	
	private void judgeAddImageViewVisibleOrNot(){
		if(flowLayout.getChildCount()<MAX_SIZE){//最多上传9张图片
			flowLayout.getChildAt(flowLayout.getChildCount()-1).setVisibility(View.VISIBLE);
		}else{
			flowLayout.getChildAt(flowLayout.getChildCount()-1).setVisibility(View.GONE);
		}
	}
	
	private void loadImageByPicasso(ImageView iv,int width,File file){
		Picasso.with(context).load(file)
//		.transform(new CropCircleTransformation())
		.resize(width, width).centerCrop()
		.placeholder(R.drawable.default_img)
		.error(R.drawable.default_img)
		.into(iv);
	}
	
	private void luban(List<File> files) {
		if(files==null||files.size()==0){
//			Log.i(tag, "==========No file===========");
			return;
		}
//		Luban.get(context)
//			.load(files)
//			.putGear(Luban.THIRD_GEAR)// 传人要压缩的图片
//			.setOnMultiCompressListener(// 设定压缩档次，默认三挡
//				new Luban.OnMultiCompressListener() {
//
//					@Override
//					public void onStart(){
//						context.showProgress();
//					}
//					
//					@Override
//					public void onSuccess(List<File> files,long timeSpent) {
//						Log.i("tag", "timeSpent=" + timeSpent);
//						context.closeProgress();
//						uploadImages(files);
//					}
//
//					@Override
//					public void onError(long timeSpent) {
//						context.closeProgress();
//					}
//				}).launch();
	}
	
//	private void uploadImages(final List<File> files) {
//		TimerTask task=new TimerTask() {
//			
//			@Override
//			public void run() {
//				// TODO Auto-generated method stub
//				try {
//					Looper.prepare();
//					uploadFilesByVolley(files);
//					Looper.loop();
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//		};
//		Timer t=new Timer();
//		t.schedule(task, 5);
//	}
//	
//	private void uploadFilesByVolley(List<File> files) {
//		if(isEmptyList(files)){
//			Log.i("tag", "No file found");
//			return;
//		}
//		String uploadUrl=Cmd.BASE_URL+"asn/uploadImageList";
//		Map<String, String> map=new HashMap<String, String>();
//		VolleyHelper.execPostRequest(context, App.user.getAccessToken(),uploadUrl, "files", files, map, new Listener<JSONObject>() {
//
//			@Override
//			public void onResponse(JSONObject response) {
//				// TODO Auto-generated method stub
//				Log.i("tag", "response="+response.toString());
//				context.closeProgress();
//				if(context.isSuccessful(response)){
//					UploadImageListResponse obj=context.toObject(response, UploadImageListResponse.class);
//					
//				}
//			}
//		}, true);
//		
//	}
	
	private void showYearPopupWindow(final TextView tv){
		Calendar date = Calendar.getInstance();
		int curYear=date.get(Calendar.YEAR);
		int lastYear=curYear-1;
		int nextYear=curYear+1;
		List<String> list=new ArrayList<String>();
		list.add(String.valueOf(lastYear));
		list.add(String.valueOf(curYear));
		list.add(String.valueOf(nextYear));
		View view=LayoutInflater.from(this).inflate(R.layout.popupwindow_simple, null);
		SimpleListPopupWindow<String> slpw=new SimpleListPopupWindow<String>(this, view, tv.getWidth(), list);
		slpw.showAsDropDown(tv, 0, 0);
		slpw.setOnItemClickListener(new OnItemClickListener<String>() {

			@Override
			public void onItemClick(int position, String item) {
				// TODO Auto-generated method stub
				tv.setText(item);
				String year_code=tv.getText().toString().substring(2);
				tv.setTag(year_code);
			}
		});
	}
	
	private List<ProdColor> colorList = null;
	private void showColorPopupWindow(final TextView tv){
		if(isEmptyList(colorList)||isUpdateColor){
			Commands.doCommandGetProdColorList(context, "1",new Listener<JSONObject>() {
	
				@Override
				public void onResponse(JSONObject response) {
					// TODO Auto-generated method stub
//					Log.i("tag", "response="+response.toString());
					if (isSuccess(response)) {
						GetProdColorListResponse obj=mapperToObject(response, GetProdColorListResponse.class);
						colorList=obj.getInfo();
						isUpdateColor=false;
						if(!isFinishing()){
							if(isEmptyList(colorList)){
								showToast("暂无数据");
							}else{
								View view = LayoutInflater.from(context).inflate(R.layout.popupwindow_simple, null);
								SimpleListPopupWindow<ProdColor> popupWindow=new SimpleListPopupWindow<ProdColor>(context, view, tv.getWidth(), colorList);
								popupWindow.showAsDropDown(tv, 0, 0);
								popupWindow.setOnItemClickListener(new SimpleListPopupWindow.OnItemClickListener<ProdColor>() {
		
									@Override
									public void onItemClick(int position, ProdColor item) {
										// TODO Auto-generated method stub
										tv.setText(item.getDescription());
										tv.setTag(item);
										
										ProdStyle prodStyle=(ProdStyle)tv_productSku.getTag();
										updateProductSkuTextView(prodStyle);
									}
								});
							}
						}
						
					}
				}
			});
		}else{
			View view = LayoutInflater.from(context).inflate(R.layout.popupwindow_simple, null);
			SimpleListPopupWindow<ProdColor> popupWindow=new SimpleListPopupWindow<ProdColor>(context, view, tv.getWidth(), colorList);
			popupWindow.showAsDropDown(tv, 0, 0);
			popupWindow.setOnItemClickListener(new SimpleListPopupWindow.OnItemClickListener<ProdColor>() {

				@Override
				public void onItemClick(int position, ProdColor item) {
					// TODO Auto-generated method stub
					tv.setText(item.getDescription());
					tv.setTag(item);
					ProdStyle prodStyle=(ProdStyle)tv_productSku.getTag();
					updateProductSkuTextView(prodStyle);
				}
			});
		}
	}
	
	private List<ProdSpec> specList = null;
	private void showSizePopupWindow(final TextView tv){
		if(isEmptyList(specList)||isUpdateSpec){
			Commands.doCommandGetProdSpecList(context, "1", new Listener<JSONObject>() {

				@Override
				public void onResponse(JSONObject response) {
					// TODO Auto-generated method stub
//					Log.i("tag", "response="+response.toString());
					if (isSuccess(response)) {
						GetProdSpecListResponse obj=mapperToObject(response, GetProdSpecListResponse.class);
						specList=obj.getInfo();
						isUpdateSpec=false;
						if(!isFinishing()){
							if(isEmptyList(specList)){
								showToast("暂无数据");
							}else{
								View view = LayoutInflater.from(context).inflate(R.layout.popupwindow_simple, null);
								SimpleListPopupWindow<ProdSpec> popupWindow=new SimpleListPopupWindow<ProdSpec>(context, view, tv.getWidth(), specList);
								popupWindow.showAsDropDown(tv, 0, 0);
								popupWindow.setOnItemClickListener(new SimpleListPopupWindow.OnItemClickListener<ProdSpec>() {
		
									@Override
									public void onItemClick(int position, ProdSpec item) {
										// TODO Auto-generated method stub
										tv.setText(item.getDescription());
										tv.setTag(item);
										ProdStyle prodStyle=(ProdStyle)tv_productSku.getTag();
										updateProductSkuTextView(prodStyle);
									}
								});
							}
						}
					}
				}
			});
			
		}else{
			View view = LayoutInflater.from(context).inflate(R.layout.popupwindow_simple, null);
			SimpleListPopupWindow<ProdSpec> popupWindow=new SimpleListPopupWindow<ProdSpec>(context, view, tv.getWidth(), specList);
			popupWindow.showAsDropDown(tv, 0, 0);
			popupWindow.setOnItemClickListener(new SimpleListPopupWindow.OnItemClickListener<ProdSpec>() {

				@Override
				public void onItemClick(int position, ProdSpec item) {
					// TODO Auto-generated method stub
					tv.setText(item.getDescription());
					tv.setTag(item);
					ProdStyle prodStyle=(ProdStyle)tv_productSku.getTag();
					updateProductSkuTextView(prodStyle);
				}
			});
		}
		
	}
	
	
	private List<ProdBrand> brandList = null;
	private void showBrandPopupWindow(final TextView tv){
		if(isEmptyList(brandList)||isUpdateBrand){
			Commands.doCommandGetProdBrandList(context, "1", new Listener<JSONObject>() {
				
				@Override
				public void onResponse(JSONObject response) {
					// TODO Auto-generated method stub
//					Log.i("tag", "response="+response.toString());
					if (isSuccess(response)) {
						GetProdBrandListResponse obj=mapperToObject(response, GetProdBrandListResponse.class);
						brandList=obj.getInfo();
						isUpdateBrand=false;
						if(!isFinishing()){
							if(isEmptyList(brandList)){
								showToast("暂无数据");
							}else{
								View view = LayoutInflater.from(context).inflate(R.layout.popupwindow_simple, null);
								SimpleListPopupWindow<ProdBrand> popupWindow=new SimpleListPopupWindow<ProdBrand>(context, view, tv.getWidth(), brandList);
								popupWindow.showAsDropDown(tv, 0, 0);
								popupWindow.setOnItemClickListener(new SimpleListPopupWindow.OnItemClickListener<ProdBrand>() {
									
									@Override
									public void onItemClick(int position, ProdBrand item) {
										// TODO Auto-generated method stub
										tv.setText(item.getDescription());
										tv.setTag(item);
									}
								});
							}
						}
					}
				}
			});
			
		}else{
			View view = LayoutInflater.from(context).inflate(R.layout.popupwindow_simple, null);
			SimpleListPopupWindow<ProdBrand> popupWindow=new SimpleListPopupWindow<ProdBrand>(context, view, tv.getWidth(), brandList);
			popupWindow.showAsDropDown(tv, 0, 0);
			popupWindow.setOnItemClickListener(new SimpleListPopupWindow.OnItemClickListener<ProdBrand>() {
				
				@Override
				public void onItemClick(int position, ProdBrand item) {
					// TODO Auto-generated method stub
					tv.setText(item.getDescription());
					tv.setTag(item);
				}
			});
		}
		
	}
	
	
	private List<ProdSort> sortList = null;
	private void showSortPopupWindow(final TextView tv){
		if(isEmptyList(sortList)||isUpdateSort){
			Commands.doCommandGetProdSortList(context, "1", new Listener<JSONObject>() {
				
				@Override
				public void onResponse(JSONObject response) {
					// TODO Auto-generated method stub
//					Log.i("tag", "response="+response.toString());
					if (isSuccess(response)) {
						GetProdSortListResponse obj=mapperToObject(response, GetProdSortListResponse.class);
						sortList=obj.getInfo();
						isUpdateSort=false;
						if(!isFinishing()){
							if(isEmptyList(sortList)){
								showToast("暂无数据");
							}else{
								View view = LayoutInflater.from(context).inflate(R.layout.popupwindow_simple, null);
								SimpleListPopupWindow<ProdSort> popupWindow=new SimpleListPopupWindow<ProdSort>(context, view, tv.getWidth(), sortList);
								popupWindow.showAsDropDown(tv, 0, 0);
								popupWindow.setOnItemClickListener(new SimpleListPopupWindow.OnItemClickListener<ProdSort>() {
									
									@Override
									public void onItemClick(int position, ProdSort item) {
										// TODO Auto-generated method stub
										tv.setText(item.getDescription());
										tv.setTag(item);
									}
								});
							}
						}
					}
				}
			});
			
		}else{
			View view = LayoutInflater.from(context).inflate(R.layout.popupwindow_simple, null);
			SimpleListPopupWindow<ProdSort> popupWindow=new SimpleListPopupWindow<ProdSort>(context, view, tv.getWidth(), sortList);
			popupWindow.showAsDropDown(tv, 0, 0);
			popupWindow.setOnItemClickListener(new SimpleListPopupWindow.OnItemClickListener<ProdSort>() {
				
				@Override
				public void onItemClick(int position, ProdSort item) {
					// TODO Auto-generated method stub
					tv.setText(item.getDescription());
					tv.setTag(item);
				}
			});
		}
		
	}
	
	private List<ProdCw> cwList = null;
	private void showCWPopupWindow(final TextView tv){
		if(isEmptyList(cwList)||isUpdateCw){
			Commands.doCommandGetProdCwList(context, "1", new Listener<JSONObject>() {
				
				@Override
				public void onResponse(JSONObject response) {
					// TODO Auto-generated method stub
//					Log.i("tag", "response="+response.toString());
					if (isSuccess(response)) {
						GetProdCwListResponse obj=mapperToObject(response, GetProdCwListResponse.class);
						cwList=obj.getInfo();
						isUpdateCw=false;
						if(!isFinishing()){
							if(isEmptyList(cwList)){
								showToast("暂无数据");
							}else{
								View view = LayoutInflater.from(context).inflate(R.layout.popupwindow_simple, null);
								SimpleListPopupWindow<ProdCw> popupWindow=new SimpleListPopupWindow<ProdCw>(context, view, tv.getWidth(), cwList);
								popupWindow.showAsDropDown(tv, 0, 0);
								popupWindow.setOnItemClickListener(new SimpleListPopupWindow.OnItemClickListener<ProdCw>() {
									
									@Override
									public void onItemClick(int position, ProdCw item) {
										// TODO Auto-generated method stub
										tv.setText(item.getDescription());
										tv.setTag(item);
									}
								});
							}
						}
					}
				}
			});
			
		}else{
			View view = LayoutInflater.from(context).inflate(R.layout.popupwindow_simple, null);
			SimpleListPopupWindow<ProdCw> popupWindow=new SimpleListPopupWindow<ProdCw>(context, view, tv.getWidth(), cwList);
			popupWindow.showAsDropDown(tv, 0, 0);
			popupWindow.setOnItemClickListener(new SimpleListPopupWindow.OnItemClickListener<ProdCw>() {
				
				@Override
				public void onItemClick(int position, ProdCw item) {
					// TODO Auto-generated method stub
					tv.setText(item.getDescription());
					tv.setTag(item);
				}
			});
		}
		
	}
	
	private List<ProdSeason> seasonList = null;
	private void showSeasonPopupWindow(final TextView tv){
		if(isEmptyList(seasonList)||isUpdateSeason){
			Commands.doCommandGetProdSeasonList(context, "1", new Listener<JSONObject>() {
				
				@Override
				public void onResponse(JSONObject response) {
					// TODO Auto-generated method stub
//					Log.i("tag", "response="+response.toString());
					if (isSuccess(response)) {
						GetProdSeasonListResponse obj=mapperToObject(response, GetProdSeasonListResponse.class);
						seasonList=obj.getInfo();
						isUpdateSeason=false;
						if(!isFinishing()){
							if(isEmptyList(seasonList)){
								showToast("暂无数据");
							}else{
								View view = LayoutInflater.from(context).inflate(R.layout.popupwindow_simple, null);
								SimpleListPopupWindow<ProdSeason> popupWindow=new SimpleListPopupWindow<ProdSeason>(context, view, tv.getWidth(), seasonList);
								popupWindow.showAsDropDown(tv, 0, 0);
								popupWindow.setOnItemClickListener(new SimpleListPopupWindow.OnItemClickListener<ProdSeason>() {
									
									@Override
									public void onItemClick(int position, ProdSeason item) {
										// TODO Auto-generated method stub
										tv.setText(item.getDescription());
										tv.setTag(item);
									}
								});
							}
						}
					}
				}
			});
			
		}else{
			View view = LayoutInflater.from(context).inflate(R.layout.popupwindow_simple, null);
			SimpleListPopupWindow<ProdSeason> popupWindow=new SimpleListPopupWindow<ProdSeason>(context, view, tv.getWidth(), seasonList);
			popupWindow.showAsDropDown(tv, 0, 0);
			popupWindow.setOnItemClickListener(new SimpleListPopupWindow.OnItemClickListener<ProdSeason>() {
				
				@Override
				public void onItemClick(int position, ProdSeason item) {
					// TODO Auto-generated method stub
					tv.setText(item.getDescription());
					tv.setTag(item);
				}
			});
		}
		
	}
	
	private List<ProdJLDW> jldwList = null;
	private void showJLDWPopupWindow(final TextView tv){
		if(isEmptyList(jldwList)||isUpdateJLDW){
			Commands.doCommandGetProdJLDWList(context, "1", new Listener<JSONObject>() {
				
				@Override
				public void onResponse(JSONObject response) {
					// TODO Auto-generated method stub
//					Log.i("tag", "response="+response.toString());
					if (isSuccess(response)) {
						GetProdJLDWListResponse obj=mapperToObject(response, GetProdJLDWListResponse.class);
						jldwList=obj.getInfo();
						isUpdateJLDW=false;
						if(!isFinishing()){
							if(isEmptyList(jldwList)){
								showToast("暂无数据");
							}else{
								View view = LayoutInflater.from(context).inflate(R.layout.popupwindow_simple, null);
								SimpleListPopupWindow<ProdJLDW> popupWindow=new SimpleListPopupWindow<ProdJLDW>(context, view, tv.getWidth(), jldwList);
								popupWindow.showAsDropDown(tv, 0, 0);
								popupWindow.setOnItemClickListener(new SimpleListPopupWindow.OnItemClickListener<ProdJLDW>() {
									
									@Override
									public void onItemClick(int position, ProdJLDW item) {
										// TODO Auto-generated method stub
										tv.setText(item.getDescription());
										tv.setTag(item);
									}
								});
							}
						}
					}
				}
			});
			
		}else{
			View view = LayoutInflater.from(context).inflate(R.layout.popupwindow_simple, null);
			SimpleListPopupWindow<ProdJLDW> popupWindow=new SimpleListPopupWindow<ProdJLDW>(context, view, tv.getWidth(), jldwList);
			popupWindow.showAsDropDown(tv, 0, 0);
			popupWindow.setOnItemClickListener(new SimpleListPopupWindow.OnItemClickListener<ProdJLDW>() {
				
				@Override
				public void onItemClick(int position, ProdJLDW item) {
					// TODO Auto-generated method stub
					tv.setText(item.getDescription());
					tv.setTag(item);
				}
			});
		}
		
	}
	
	
	private List<ProdCs> csList = null;
	private void showCsPopupWindow(final TextView tv){
		if(isEmptyList(csList)||isUpdateCs){
			Commands.doCommandGetProdCsList(context, "1", new Listener<JSONObject>() {
				
				@Override
				public void onResponse(JSONObject response) {
					// TODO Auto-generated method stub
//					Log.i("tag", "response="+response.toString());
					if (isSuccess(response)) {
						GetProdCsListResponse obj=mapperToObject(response, GetProdCsListResponse.class);
						csList=obj.getInfo();
						isUpdateCs=false;
						if(!isFinishing()){
							if(isEmptyList(csList)){
								showToast("暂无数据");
							}else{
								View view = LayoutInflater.from(context).inflate(R.layout.popupwindow_simple, null);
								SimpleListPopupWindow<ProdCs> popupWindow=new SimpleListPopupWindow<ProdCs>(context, view, tv.getWidth(), csList);
								popupWindow.showAsDropDown(tv, 0, 0);
								popupWindow.setOnItemClickListener(new SimpleListPopupWindow.OnItemClickListener<ProdCs>() {
									
									@Override
									public void onItemClick(int position, ProdCs item) {
										// TODO Auto-generated method stub
										tv.setText(item.getDescription());
										tv.setTag(item);
									}
								});
							}
						}
					}
				}
			});
			
		}else{
			View view = LayoutInflater.from(context).inflate(R.layout.popupwindow_simple, null);
			SimpleListPopupWindow<ProdCs> popupWindow=new SimpleListPopupWindow<ProdCs>(context, view, tv.getWidth(), csList);
			popupWindow.showAsDropDown(tv, 0, 0);
			popupWindow.setOnItemClickListener(new SimpleListPopupWindow.OnItemClickListener<ProdCs>() {
				
				@Override
				public void onItemClick(int position, ProdCs item) {
					// TODO Auto-generated method stub
					tv.setText(item.getDescription());
					tv.setTag(item);
				}
			});
		}
		
	}
	
	private List<ProdOther> otherList = null;
	private void showOtherPopupWindow(final TextView tv){
		if(isEmptyList(otherList)||isUpdateOther){
			Commands.doCommandGetProdOtherList(context, "1", new Listener<JSONObject>() {
	
				@Override
				public void onResponse(JSONObject response) {
					// TODO Auto-generated method stub
//					Log.i("tag", "response="+response.toString());
					if (isSuccess(response)) {
						GetProdOtherListResponse obj=mapperToObject(response, GetProdOtherListResponse.class);
						otherList=obj.getInfo();
						isUpdateOther=false;
						if(!isFinishing()){
							if(isEmptyList(otherList)){
								showToast("暂无数据");
							}else{
								View view = LayoutInflater.from(context).inflate(R.layout.popupwindow_simple, null);
								SimpleListPopupWindow<ProdOther> popupWindow=new SimpleListPopupWindow<ProdOther>(context, view, tv.getWidth(), otherList);
								popupWindow.showAsDropDown(tv, 0, 0);
								popupWindow.setOnItemClickListener(new SimpleListPopupWindow.OnItemClickListener<ProdOther>() {
		
									@Override
									public void onItemClick(int position, ProdOther item) {
										// TODO Auto-generated method stub
										tv.setText(item.getDescription());
										tv.setTag(item);
									}
								});
							}
						}
						
					}
				}
			});
		}else{
			View view = LayoutInflater.from(context).inflate(R.layout.popupwindow_simple, null);
			SimpleListPopupWindow<ProdOther> popupWindow=new SimpleListPopupWindow<ProdOther>(context, view, tv.getWidth(), otherList);
			popupWindow.showAsDropDown(tv, 0, 0);
			popupWindow.setOnItemClickListener(new SimpleListPopupWindow.OnItemClickListener<ProdOther>() {

				@Override
				public void onItemClick(int position, ProdOther item) {
					// TODO Auto-generated method stub
					tv.setText(item.getDescription());
					tv.setTag(item);
				}
			});
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////
	//Dialog
	////////////////////////////////////////////////////////////////////////////////////////////
	private void showBrandDialog(){
		SimpleTextListDialog<ProdBrand> d = new SimpleTextListDialog<ProdBrand>(context, "", "请输入品牌名称",brandList);
		d.setOnClickListener(new SimpleTextListDialog.OnClickListener() {
			
			@Override
			public void onClick(View v, String text) {
				// TODO Auto-generated method stub
				doCommandAddProdBrand(text);
			}
		});
		d.setOnItemClickListener(new SimpleTextListDialog.OnItemClickListener<ProdBrand>() {

			@Override
			public void onItemClick(ProdBrand bean,
					int position) {
				// TODO Auto-generated method stub
				tv_brand.setText(bean.getDescription());
				tv_brand.setTag(bean);
			}
		});
		d.setOnCheckedChangeListener(new OnCheckedChangeListener<ProdBrand>() {

			@Override
			public void onCheckedChange(ProdBrand bean, int position,
					boolean isChecked) {
				// TODO Auto-generated method stub
				Commands.doCommandUpdateProdBrandEnabled(context, bean.getBrandCode(), isChecked?"1":"0", new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						// TODO Auto-generated method stub
//						Log.i("tag", response.toString());
						if(isSuccess(response)){
							isUpdateBrand=true;
						}
					}
				});
			}
		});
		d.show();
	}
	
	private void showSeasonDialog(){
		SimpleTextListDialog<ProdSeason> d = new SimpleTextListDialog<ProdSeason>(context, "", "请输入季节名称",seasonList);
		d.setOnClickListener(new SimpleTextListDialog.OnClickListener() {
			
			@Override
			public void onClick(View v, String text) {
				// TODO Auto-generated method stub
				doCommandAddProdSeason(text);
			}
		});
		d.setOnItemClickListener(new SimpleTextListDialog.OnItemClickListener<ProdSeason>() {

			@Override
			public void onItemClick(ProdSeason bean,
					int position) {
				// TODO Auto-generated method stub
				tv_season.setText(bean.getDescription());
				tv_season.setTag(bean);
			}
		});
		d.setOnCheckedChangeListener(new OnCheckedChangeListener<ProdSeason>() {

			@Override
			public void onCheckedChange(ProdSeason bean, int position,
					boolean isChecked) {
				// TODO Auto-generated method stub
				Commands.doCommandUpdateProdSeasonEnabled(context, bean.getSeasonCode(), isChecked?"1":"0", new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						// TODO Auto-generated method stub
//						Log.i("tag", response.toString());
						if(isSuccess(response)){
							isUpdateSeason=true;
						}
					}
				});
			}
		});
		d.show();
	}
	
	private void showCategoryDialog(){
		SimpleTextListDialog<ProdSort> d = new SimpleTextListDialog<ProdSort>(context, "", "请输入类别名称",sortList);
		d.setOnClickListener(new SimpleTextListDialog.OnClickListener() {
			
			@Override
			public void onClick(View v, String text) {
				// TODO Auto-generated method stub
				doCommandAddProdSort(text);
			}
		});
		d.setOnItemClickListener(new SimpleTextListDialog.OnItemClickListener<ProdSort>() {

			@Override
			public void onItemClick(ProdSort bean,
					int position) {
				// TODO Auto-generated method stub
				tv_category.setText(bean.getDescription());
				tv_category.setTag(bean);
			}
		});
		d.setOnCheckedChangeListener(new OnCheckedChangeListener<ProdSort>() {

			@Override
			public void onCheckedChange(ProdSort bean, int position,
					boolean isChecked) {
				// TODO Auto-generated method stub
				Commands.doCommandUpdateProdSortEnabled(context, bean.getSortCode(), isChecked?"1":"0", new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						// TODO Auto-generated method stub
//						Log.i("tag", response.toString());
						if(isSuccess(response)){
							isUpdateSort=true;
						}
					}
				});
			}
		});
		d.show();
	}
	
	private void showColorDialog(){
		SimpleTextListDialog<ProdColor> d = new SimpleTextListDialog<ProdColor>(context, "", "请输入颜色名称",colorList,SimpleTextListDialog.COL_TWO);
		d.setOnClickListener(new SimpleTextListDialog.OnClickListener() {
			
			@Override
			public void onClick(View v, String text) {
				// TODO Auto-generated method stub
				doCommandAddProdColor(text);
			}
		});
		d.setOnItemClickListener(new SimpleTextListDialog.OnItemClickListener<ProdColor>() {

			@Override
			public void onItemClick(ProdColor bean,
					int position) {
				// TODO Auto-generated method stub
				tv_color.setText(bean.getDescription());
				tv_color.setTag(bean);
				ProdStyle prodStyle=(ProdStyle)tv_productSku.getTag();
				updateProductSkuTextView(prodStyle);
			}
		});
		d.setOnCheckedChangeListener(new OnCheckedChangeListener<ProdColor>() {

			@Override
			public void onCheckedChange(ProdColor bean, int position,
					boolean isChecked) {
				// TODO Auto-generated method stub
				Commands.doCommandUpdateProdColorEnabled(context, bean.getColorCode(), isChecked?"1":"0", new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						// TODO Auto-generated method stub
//						Log.i("tag", response.toString());
						if(isSuccess(response)){
							isUpdateColor=true;
						}
					}
				});
			}
		});
		d.show();
	}
	
	private void showSpecDialog(){
		SimpleTextListDialog<ProdSpec> d = new SimpleTextListDialog<ProdSpec>(context, "", "请输入尺码名称",specList,SimpleTextListDialog.COL_TWO);
		d.setOnClickListener(new SimpleTextListDialog.OnClickListener() {
			
			@Override
			public void onClick(View v, String text) {
				// TODO Auto-generated method stub
				doCommandAddProdSpec(text);
			}
		});
		d.setOnItemClickListener(new SimpleTextListDialog.OnItemClickListener<ProdSpec>() {

			@Override
			public void onItemClick(ProdSpec bean,
					int position) {
				// TODO Auto-generated method stub
				tv_size.setText(bean.getDescription());
				tv_size.setTag(bean);
				ProdStyle prodStyle=(ProdStyle)tv_productSku.getTag();
				updateProductSkuTextView(prodStyle);
			}
		});
		d.setOnCheckedChangeListener(new OnCheckedChangeListener<ProdSpec>() {

			@Override
			public void onCheckedChange(ProdSpec bean, int position,
					boolean isChecked) {
				// TODO Auto-generated method stub
				Commands.doCommandUpdateProdSpecEnabled(context, bean.getSpecCode(), isChecked?"1":"0", new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						// TODO Auto-generated method stub
//						Log.i("tag", response.toString());
						if(isSuccess(response)){
							isUpdateSpec=true;
						}
					}
				});
			}
		});
		d.show();
	}

	private void showCsDialog(){
		SimpleTextListDialog<ProdCs> d = new SimpleTextListDialog<ProdCs>(context, "", "请输入厂商名称",csList);
		d.setOnClickListener(new SimpleTextListDialog.OnClickListener() {
			
			@Override
			public void onClick(View v, String text) {
				// TODO Auto-generated method stub
				doCommandAddProdCs(text);
			}
		});
		d.setOnItemClickListener(new SimpleTextListDialog.OnItemClickListener<ProdCs>() {

			@Override
			public void onItemClick(ProdCs bean,
					int position) {
				// TODO Auto-generated method stub
				tv_cs.setText(bean.getDescription());
				tv_cs.setTag(bean);
			}
		});
		d.setOnCheckedChangeListener(new OnCheckedChangeListener<ProdCs>() {

			@Override
			public void onCheckedChange(ProdCs bean, int position,
					boolean isChecked) {
				// TODO Auto-generated method stub
				Commands.doCommandUpdateProdCsEnabled(context, bean.getCsCode(), isChecked?"1":"0", new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						// TODO Auto-generated method stub
//						Log.i("tag", response.toString());
						if(isSuccess(response)){
							isUpdateCs=true;
						}
					}
				});
			}
		});
		d.show();
	}
	private void showCwDialog(){
		SimpleTextListDialog<ProdCw> d = new SimpleTextListDialog<ProdCw>(context, "", "请输入仓位名称",cwList);
		d.setOnClickListener(new SimpleTextListDialog.OnClickListener() {
			
			@Override
			public void onClick(View v, String text) {
				// TODO Auto-generated method stub
				doCommandAddProdCw(text);
			}
		});
		d.setOnItemClickListener(new SimpleTextListDialog.OnItemClickListener<ProdCw>() {

			@Override
			public void onItemClick(ProdCw bean,
					int position) {
				// TODO Auto-generated method stub
				tv_cw.setText(bean.getDescription());
				tv_cw.setTag(bean);
			}
		});
		d.setOnCheckedChangeListener(new OnCheckedChangeListener<ProdCw>() {

			@Override
			public void onCheckedChange(ProdCw bean, int position,
					boolean isChecked) {
				// TODO Auto-generated method stub
				Commands.doCommandUpdateProdCwEnabled(context, bean.getCwCode(), isChecked?"1":"0", new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						// TODO Auto-generated method stub
//						Log.i("tag", response.toString());
						if(isSuccess(response)){
							isUpdateCw=true;
						}
					}
				});
			}
		});
		d.show();
	}
	private void showJldwDialog(){
		SimpleTextListDialog<ProdJLDW> d = new SimpleTextListDialog<ProdJLDW>(context, "", "请输入单位名称",jldwList);
		d.setOnClickListener(new SimpleTextListDialog.OnClickListener() {
			
			@Override
			public void onClick(View v, String text) {
				// TODO Auto-generated method stub
				doCommandAddProdJLDW(text);
			}
		});
		d.setOnItemClickListener(new SimpleTextListDialog.OnItemClickListener<ProdJLDW>() {
			
			@Override
			public void onItemClick(ProdJLDW bean,
					int position) {
				// TODO Auto-generated method stub
				tv_jldw.setText(bean.getDescription());
				tv_jldw.setTag(bean);
			}
		});
		d.setOnCheckedChangeListener(new OnCheckedChangeListener<ProdJLDW>() {

			@Override
			public void onCheckedChange(ProdJLDW bean, int position,
					boolean isChecked) {
				// TODO Auto-generated method stub
				Commands.doCommandUpdateProdJLDWEnabled(context, bean.getJldwCode(), isChecked?"1":"0", new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						// TODO Auto-generated method stub
//						Log.i("tag", response.toString());
						if(isSuccess(response)){
							isUpdateJLDW=true;
						}
					}
				});
			}
		});
		d.show();
	}
	
	private void showOtherDialog(){
		SimpleTextListDialog<ProdOther> d = new SimpleTextListDialog<ProdOther>(context, "", "请输入其他",otherList);
		d.setOnClickListener(new SimpleTextListDialog.OnClickListener() {
			
			@Override
			public void onClick(View v, String text) {
				// TODO Auto-generated method stub
				doCommandAddProdOther(text);
			}
		});
		d.setOnItemClickListener(new SimpleTextListDialog.OnItemClickListener<ProdOther>() {

			@Override
			public void onItemClick(ProdOther bean,
					int position) {
				// TODO Auto-generated method stub
				tv_other.setText(bean.getDescription());
				tv_other.setTag(bean);
			}
		});
		d.setOnCheckedChangeListener(new OnCheckedChangeListener<ProdOther>() {

			@Override
			public void onCheckedChange(ProdOther bean, int position,
					boolean isChecked) {
				// TODO Auto-generated method stub
				Commands.doCommandUpdateProdOtherEnabled(context, bean.getOtherCode(), isChecked?"1":"0", new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						// TODO Auto-generated method stub
//						Log.i("tag", response.toString());
						if(isSuccess(response)){
							isUpdateOther=true;
						}
					}
				});
			}
		});
		d.show();
	}
	
	//////////////////////////////////////////////////////////////////////////////////////////////////
	private boolean isUpdateColor=true;
	private void doCommandAddProdColor(final String description){
		Commands.doCommandAddProdColor(context, description, new Listener<JSONObject>() {
			
			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
//				Log.i("tag", "response="+response.toString());
				if (isSuccess(response)) {
					isUpdateColor=true;
				}
			}
		});
	}
	
	private boolean isUpdateSpec=true;
	private void doCommandAddProdSpec(final String description){
		Commands.doCommandAddProdSpec(context, description, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
//				Log.i("tag", "response="+response.toString());
				if (isSuccess(response)) {
					isUpdateSpec=true;
				}
			}
		});
	}
	
	private boolean isUpdateBrand=true;
	private void doCommandAddProdBrand(final String description){
		Commands.doCommandAddProdBrand(context, description, new Listener<JSONObject>() {
			
			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
//				Log.i("tag", "response="+response.toString());
				if (isSuccess(response)) {
					isUpdateBrand=true;
				}
			}
		});
	}
	
	private boolean isUpdateSeason=true;
	private void doCommandAddProdSeason(final String description){
		Commands.doCommandAddProdSeason(context, description, new Listener<JSONObject>() {
			
			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
//				Log.i("tag", "response="+response.toString());
				if (isSuccess(response)) {
					isUpdateSeason=true;
				}
			}
		});
	}
	
	private boolean isUpdateSort=true;
	private void doCommandAddProdSort(final String description){
		Commands.doCommandAddProdSort(context, description, new Listener<JSONObject>() {
			
			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
//				Log.i("tag", "response="+response.toString());
				if (isSuccess(response)) {
					isUpdateSort=true;
				}
			}
		});
	}
	
	private boolean isUpdateCs=true;
	private void doCommandAddProdCs(final String description){
		Commands.doCommandAddProdCs(context, description, new Listener<JSONObject>() {
			
			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
//				Log.i("tag", "response="+response.toString());
				if (isSuccess(response)) {
					isUpdateCs=true;
				}
			}
		});
	}
	
	private boolean isUpdateJLDW=true;
	private void doCommandAddProdJLDW(final String description){
		Commands.doCommandAddProdJLDW(context, description, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
//				Log.i("tag", "response="+response.toString());
				if (isSuccess(response)) {
					isUpdateJLDW=true;
				}
			}
		});
	}
	
	private boolean isUpdateCw=true;
	private void doCommandAddProdCw(final String description){
		Commands.doCommandAddProdCw(context, description, new Listener<JSONObject>() {
			
			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
//				Log.i("tag", "response="+response.toString());
				if (isSuccess(response)) {
					isUpdateCw=true;
				}
			}
		});
	}
	
	private boolean isUpdateOther=true;
	private void doCommandAddProdOther(final String description){
		Commands.doCommandAddProdOther(context, description, new Listener<JSONObject>() {
			
			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
//				Log.i("tag", "response="+response.toString());
				if (isSuccess(response)) {
					isUpdateOther=true;
				}
			}
		});
	}
	
	private void doCommandAddProdStyle(final String dateCode,final String productName){
		Commands.doCommandAddProdStyle(context, dateCode, productName,  new Listener<JSONObject>() {
			
			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
//				Log.i("tag", "response="+response.toString());
				if (isSuccess(response)) {
					doCommandGetProdStyleList(dateCode,productName);
				}
			}
		});
	}

	private void doCommandGetProdStyleList(final String date_code,final String productName){
		Commands.doCommandGetProdStyleList(context, date_code, productName, new Listener<JSONObject>() {
			
			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
//				Log.i("tag", "response="+response.toString());
				if (isSuccess(response)) {
					GetProdStyleListResponse obj=mapperToObject(response, GetProdStyleListResponse.class);
					List<ProdStyle> styleList=obj.getInfo();
					
					if(!isFinishing()){
						if(isEmptyList(styleList)){
							doCommandAddProdStyle(date_code,productName);
						}else if(styleList.size()==1){
							ProdStyle prodStyle=styleList.get(0);
							tv_productSku.setTag(prodStyle);
							updateProductSkuTextView(prodStyle);
						}else{
							//debug
							View view = LayoutInflater.from(context).inflate(R.layout.popupwindow_simple, null);
							SimpleListPopupWindow<ProdStyle> popupWindow=new SimpleListPopupWindow<ProdStyle>(context, view, tv_productSku.getWidth(), styleList);
							popupWindow.showAsDropDown(tv_productSku, 0, 0);
							popupWindow.setOnItemClickListener(new SimpleListPopupWindow.OnItemClickListener<ProdStyle>() {
								
								@Override
								public void onItemClick(int position, ProdStyle item) {
									// TODO Auto-generated method stub
									tv_productSku.setTag(item);
									updateProductSkuTextView(item);
								}
							});
						}
					}
				}
			}
		});
	}
	
	private void updateProductSkuTextView(ProdStyle prodStyle){
		if(prodStyle==null){
			return;
		}
//		String dateCode=prodStyle.getDateCode().substring(2);
		String dateCode=prodStyle.getDateCode();
		String styleCode=prodStyle.getStyleCode();
		
		String color="00";
		if(tv_color.getTag()!=null){
			color=((ProdColor)tv_color.getTag()).getColorCode();
		}
		
		String size="00";
		if(tv_size.getTag()!=null){
			size=((ProdSpec)tv_size.getTag()).getSpecCode();
		}
		
		tv_productSku.setText(dateCode+styleCode+color+size);//2+4+2+2
		et_productName.setEnabled(false);
		tv_year.setClickable(false);
		tv_year.setTextColor(ContextCompat.getColor(context, R.color.grayMiddle));
		btn_addYear.setClickable(false);
		
//		et_productSku.setEnabled(false);
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_generateCode:
			String productName=et_productName.getText().toString().trim();
			if(isEmpty(productName)){
				showToast("缺少商品名称");
				doShake(context, et_productName);
				return;
			}
			String year_code=tv_year.getText().toString().substring(2);
			doCommandGetProdStyleList(year_code,productName);
			break;
		case R.id.btn_query:
			startActivity(new Intent(context,ProductListActivity.class));
			break;
		case R.id.btn_print:
			
			break;
		case R.id.btn_productSku_query:
			startActivity(new Intent(context,ProductQueryBySkuActivity.class));
			break;
		case R.id.btn_save:
			//debug
//			String productSku=et_productSku.getText().toString().trim();
//			if(productSku.length()>=10){
//				
//			}
			
			if(isEmpty(tv_productSku)){
				showToast("尚未生成款号");
				doShake(context, tv_productSku);
				return;
			}
			
			if(isEmpty(tv_brand)){
				showToast("请选择品牌");
				doShake(context, tv_brand);
				return;
			}
//			if(isEmpty(tv_season)){
//				showToast("请选择季节");
//				doShake(context, tv_season);
//				return;
//			}
			if(isEmpty(tv_category)){
				showToast("请选择类别");
				doShake(context, tv_category);
				return;
			}
			
			if(isEmpty(et_jh_price)){
				showToast("请填写进货价");
				doShake(context, et_jh_price);
				return;
			}
			if(isEmpty(et_ls_price)){
				showToast("请填写批零价");
				doShake(context, et_ls_price);
				return;
			}
//			if(isEmpty(et_zxs)){
//				showToast("请填写装箱数");
//				doShake(context, et_zxs);
//				return;
//			}
			save();
			break;

		default:
			break;
		}
	}
	
	private void save(){
		List<ProductDangAn> goodsInfo=new ArrayList<ProductDangAn>();
		ProductDangAn bean=new ProductDangAn();
		bean.setGoods_sn(tv_productSku.getText().toString());
		
		ProdStyle prodStyle=(ProdStyle)tv_productSku.getTag();
		if(prodStyle==null){
			return;
		}
//		String dateCode=prodStyle.getDateCode().substring(2);
		String dateCode=prodStyle.getDateCode();
		String styleCode=prodStyle.getStyleCode();
		bean.setGoods_style(dateCode+styleCode);
		bean.setGoods_name(et_productName.getText().toString().trim());
		bean.setGoods_desc(et_productName.getText().toString().trim());
		
		if(tv_brand.getTag()!=null){//品牌
			ProdBrand brand=(ProdBrand)tv_brand.getTag();
			bean.setGoods_brand(brand.getBrandCode());
			bean.setBrand_name(tv_brand.getText().toString());
		}
		
		bean.setGoods_season(tv_season.getTag()==null?"00":((ProdSeason)tv_season.getTag()).getSeasonCode());
		bean.setGoods_sort(tv_category.getTag()==null?"00":((ProdSort)tv_category.getTag()).getSortCode());
		bean.setGoods_color(tv_color.getTag()==null?"00":((ProdColor)tv_color.getTag()).getColorCode());
		bean.setGoods_spec(tv_size.getTag()==null?"00":((ProdSpec)tv_size.getTag()).getSpecCode());
		bean.setGoods_other(tv_other.getTag()==null?null:((ProdOther)tv_other.getTag()).getOtherCode());
		bean.setGoods_cs(tv_cs.getTag()==null?null:((ProdCs)tv_cs.getTag()).getCsCode());
		bean.setGoods_cw(tv_cw.getTag()==null?null:((ProdCw)tv_cw.getTag()).getCwCode());
		bean.setGoods_jldw(tv_jldw.getTag()==null?null:((ProdJLDW)tv_jldw.getTag()).getJldwCode());
		bean.setGoods_sj_date(tv_date.getText().toString());
		bean.setGoods_jh_price(Float.parseFloat(et_jh_price.getText().toString()));
		bean.setGoods_ls_price(Float.parseFloat(et_ls_price.getText().toString()));
		bean.setGoods_zxs(et_zxs.getText().toString());
		
		goodsInfo.add(bean);
		doCommandAddGoodsInfo(goodsInfo);
		//点击保存后，保存一份Product到本地数据库，用于AutoCompleteTextView快显菜单
		ProductDangAn product=DataSupport.where("goods_name = ?",bean.getGoods_name()).findFirst(ProductDangAn.class);
		if(product==null){//如果没有保存过类似的名称，则存盘（不要重复保存相同的名称）
			bean.saveFast();//此时可以得到Product.id
			adapter.add(bean);
		}
	}
	
	private void doCommandAddGoodsInfo(List<ProductDangAn> goodsInfo){
//		{"ActionName":"AddGoodsInfo","Pars":{"companyCode":"C001","goodsInfo":[{"brand_name":"美特斯邦威","goods_brand":"08","goods_color":"00","goods_cs":"02","goods_cw":"03","goods_zxs":"","goods_desc":"特殊情况","goods_style":"180021","goods_jldw":"02","goods_spec":"04","goods_name":"特殊情况","goods_other":"0002","goods_season":"04","goods_sj_date":"2018-07-20","goods_sn":"1800210004","goods_sort":"07","id":0,"goods_ls_price":30.0,"goods_jh_price":10.0,"goods_discountRate":0.0,"goods_db_price":0.0,"goods_price":0.0,"baseObjId":0}]}}
		Commands.doCommandAddGoodsInfo(context, goodsInfo, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
//				Log.i("tag", "response="+response.toString());
				if (isSuccess(response)) {
					showToast("保存成功");
//					et_productSku.setEnabled(true);
					et_productName.setEnabled(true);
					et_productName.requestFocus();
					et_productName.setSelection(et_productName.length());
					tv_year.setClickable(true);
					tv_year.setTextColor(ContextCompat.getColor(context, R.color.grayDark));
					btn_addYear.setClickable(true);
					tv_productSku.setText("");
					
				}
			}
		});
	}
	
	//////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	public void updateTheme(int color) {
		super.updateTheme(color);
		setThemeDrawable(context,R.id.btn_generateCode);
		setThemeDrawable(context,R.id.btn_query);
		setThemeDrawable(context,R.id.btn_print);
		setThemeDrawable(context,R.id.btn_save);
	}
	
}