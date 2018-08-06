package com.app.xstore;

import com.app.util.ThemeManager;
import com.app.xstore.R;
import com.base.util.comm.DensityUtils;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

/**
 * 
 * @author pythoner
 * 
 */
public class ThemeChangeActivity extends BaseActivity implements View.OnClickListener {

	private Context context;
	private LinearLayout mColorPanel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_theme_change);
		context = this;
		initActionBar("主题颜色", null, null);
		initViews();
	}

	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		mColorPanel = (LinearLayout) findViewById(R.id.select_panel);
		int size = DensityUtils.dp2px(this, 80);
		for (int i = 0; i < ThemeManager.BACKGROUNDS.length; i++) {
			View view = new View(this);
			view.setBackgroundColor(ThemeManager.BACKGROUNDS[i]);
			view.setOnClickListener(this);
			view.setTag(i);

			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(size, size);
			int margin = 8;
			params.setMargins(margin, margin, margin, margin);

			mColorPanel.addView(view, params);
		}

	}

	@Override
	public void onClick(View v) {
		int index = (Integer) v.getTag();
		ThemeManager.with(this).saveColor(index);
		// for (int i = 0; i < mColorPanel.getChildCount(); i++) {
		// if (v == mColorPanel.getChildAt(i)) {
		// ThemeManager.with(this).saveColor(i);
		// break;
		// }
		// }
	}

	@Override
	public void updateViews(Object obj) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateTheme(int color) {
		super.updateTheme(color);

	}

}
