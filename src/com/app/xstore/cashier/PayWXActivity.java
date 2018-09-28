package com.app.xstore.cashier;

import java.util.List;

import org.simple.eventbus.Subscriber;

import android.content.Intent;
import android.os.Bundle;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.xstore.App;
import com.app.xstore.BaseActivity;
import com.app.xstore.R;
import com.base.util.comm.SPUtils;
import com.squareup.picasso.Picasso;
import com.widget.effect.text.Spanny;
import com.widget.imagepicker.ImageConfig;
import com.widget.imagepicker.ImageSelector;
import com.widget.imagepicker.ImageSelectorActivity;
import com.widget.imagepicker.PicassoLoader;

/**
 * 微信二维码支付界面
 * 
 * @author Ni Guijun
 * 
 */
public class PayWXActivity extends BaseActivity {

	private ImageView iv_qr;
	private TextView btn_add_qr;
	private TextView btn_ok;
	private final String WX_IMAGE_PATH = "WX";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pay_wx);
		initActionBar("微信", "更换二维码", null);
		initViews();
	}

	@Override
	public void initViews() {
		double need = getIntent().getDoubleExtra("Need", 0.00);
		
		Spanny s=new Spanny();
		s.append("￥", new RelativeSizeSpan(1.0f));
		s.append(formatMoney(need), new RelativeSizeSpan(2.0f));
		
		TextView tv_need_pay = (TextView) findViewById(R.id.tv_need_pay);
		tv_need_pay.setText(s);

		iv_qr = (ImageView) findViewById(R.id.iv_qr);
		btn_add_qr = (TextView) findViewById(R.id.btn_add_qr);
		btn_ok = (TextView) findViewById(R.id.btn_ok);

		updateViews(null);
	}

	@Override
	public void doRightButtonClick(View v) {
		// TODO Auto-generated method stub
		openImageSelector();
	}
	
	private void loadImageByPicasso(ImageView iv, String filePath) {
		Picasso.with(context).load(filePath)
				// .transform(new CropCircleTransformation())
				.placeholder(R.drawable.default_img)
				.error(R.drawable.default_img).into(iv);
	}

	@Override
	public void updateViews(Object obj) {
		// TODO Auto-generated method stub
		String filePath = (String) SPUtils.get(context, WX_IMAGE_PATH, "");
		if (isEmpty(filePath)) {
			btn_add_qr.setVisibility(View.VISIBLE);
			btn_add_qr.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					openImageSelector();
				}
			});
			iv_qr.setVisibility(View.GONE);
			btn_ok.setVisibility(View.GONE);
		} else {
			btn_add_qr.setVisibility(View.GONE);
			iv_qr.setVisibility(View.VISIBLE);
			btn_ok.setVisibility(View.VISIBLE);
			btn_ok.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					doCommandSaveBillSale(1,null,null);
				}
			});
			loadImageByPicasso(iv_qr, filePath);
		}

	}

	@Subscriber
	void updateByEventBus(String event) {
		if (event.equals(App.EVENT_FINISH)) {
			finish();
		}
	}

	private void openImageSelector() {
		ImageConfig imageConfig = new ImageConfig.Builder(new PicassoLoader())
		// .steepToolBarColor(getResources().getColor(R.color.blue))// 修改状态栏颜色
		// .titleBgColor(getResources().getColor(R.color.blue))// 标题的背景颜色
		// .titleSubmitTextColor(getResources().getColor(R.color.white))//
		// 提交按钮字体的颜色
		// .titleTextColor(getResources().getColor(R.color.white))// 标题颜色
//				.crop() // (截图默认配置：关闭 比例 1：1 输出分辨率 500*500)
				.singleSelect()// 开启单选 （默认为多选）
				// .mutiSelect()// 开启多选 （默认为多选）
				// .mutiSelectMaxSize(1)// 多选时的最大数量 （默认 9 张）
				.showCamera()// 开启拍照功能 （默认关闭）
				// .pathList(paths)// 已选择的图片路径,需要及时清空，否则会重复
				.filePath("/xStore/picture")// 拍照后存放的图片路径（默认 /temp/picture）
											// （会自动创建）
				.build();

		ImageSelector.open(this, imageConfig); // 开启图片选择器
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == ImageSelector.IMAGE_REQUEST_CODE) {
			if (data != null) {
				List<String> list = data.getStringArrayListExtra(ImageSelectorActivity.EXTRA_RESULT);
				if(!isEmptyList(list)){
					String filePath=list.get(0);
					SPUtils.put(context, WX_IMAGE_PATH, "file://"+filePath);
					updateViews(null);
				}
				
			}
		}
	}

}
