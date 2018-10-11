package com.app.xstore.member;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.app.model.Member;
import com.app.xstore.BaseActivity;
import com.app.xstore.R;

/**
 * 会员详情
 * 
 * @author pythoner
 * 
 */
public class MemberDetailActivity extends BaseActivity implements OnClickListener{

	private Member member;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_member_detail);
		member=getIntent().getParcelableExtra("Member");
		initActionBar("会员详情", null, null);
		initViews();
	}

	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		$(R.id.btn_call).setOnClickListener(this);
		$(R.id.btn_wx).setOnClickListener(this);
		$(R.id.btn_watch).setOnClickListener(this);
		$(R.id.btn_recharge).setOnClickListener(this);
		$(R.id.btn_label).setOnClickListener(this);
		$(R.id.btn_archive).setOnClickListener(this);
	}


	@Override
	public void updateViews(Object obj) {
		// TODO Auto-generated method stub
	}

	@Override
	public void updateTheme(int color) {
		super.updateTheme(color);
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_call:
			if(isEmpty(member.getMobile())){
				showToast("该会员未注册电话号码");
				return;
			}
			doDial(member.getMobile());
			break;
		case R.id.btn_wx:
			Intent intent=new Intent(Intent.ACTION_SEND);                            
			intent.setType("text/plain");                            
			intent.setPackage("com.tencent.mm");//intent.setPackage("com.sina.weibo");                            
			intent.putExtra(Intent.EXTRA_TEXT,  "测试消息");                            
			startActivity(Intent.createChooser(intent, "请选择"));
			break;
		case R.id.btn_watch:
			break;
		case R.id.btn_recharge:
			break;
		case R.id.btn_label:
			break;
		case R.id.btn_archive:
			break;

		default:
			break;
		}
	}

}
