package com.app.widget;

import java.util.Calendar;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.app.xstore.R;
import com.widget.wheel.NumericWheelAdapter;
import com.widget.wheel.OnWheelScrollListener;
import com.widget.wheel.WheelView;

/**
 * 
 * @author pythoner
 * 
 */
public class UserBirthdayDialog extends BaseDialog {

	private WheelView year, month, day;
	private String date;

	public UserBirthdayDialog(Context context, String date) {
		this(context, R.style.Theme_Dialog_From_Bottom, date);
		// TODO Auto-generated constructor stub
	}

	public UserBirthdayDialog(Context context, int theme, String date) {
		super(context, theme);
		// TODO Auto-generated constructor stub
		this.date = date;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_user_birthday);
		initViews();
		setToBottom();
	}

	private void initViews() {
		findViewById(R.id.btn_left).setOnClickListener(clickListener);
		findViewById(R.id.btn_right).setOnClickListener(clickListener);

		String icurYear, icurMonth, icurDay;
		Calendar c = Calendar.getInstance();
		int curYear = c.get(Calendar.YEAR);
		if (date == null || date.length() < 10) {// 格式不正确
			int curMonth = c.get(Calendar.MONTH);
			int curDay = c.get(Calendar.DAY_OF_MONTH);
			icurYear = String.valueOf(curYear);
			icurMonth = String.valueOf(curMonth + 1);
			icurDay = String.valueOf(curDay);
		} else {// 年月日
			icurYear = date.substring(0, 4);
			icurMonth = date.substring(5, 7);
			icurDay = date.substring(8, 10);
		}

		year = (WheelView) findViewById(R.id.year);
		year.setAdapter(new NumericWheelAdapter(curYear - 100, curYear));
		// year.setLabel("年");
		year.setCurrentItem(Integer.parseInt(icurYear)
				- Integer.parseInt(year.getAdapter().getItem(0)));

		month = (WheelView) findViewById(R.id.month);
		month.setAdapter(new NumericWheelAdapter(1, 12));// "%02d"
		// month.setLabel("月");
		month.setCyclic(true);
		month.setCurrentItem(Integer.parseInt(icurMonth) - 1);

		int daysOfMounth = getDaysOfMounth();
		day = (WheelView) findViewById(R.id.day);
		day.setAdapter(new NumericWheelAdapter(1, daysOfMounth));
		// day.setLabel("日");
		day.setCyclic(true);
		day.setCurrentItem(Integer.parseInt(icurDay) - 1);

		OnWheelScrollListener scrollListener = new OnWheelScrollListener() {
			public void onScrollingStarted(WheelView wheel) {
			}

			public void onScrollingFinished(WheelView wheel) {
				int daysOfMounth = getDaysOfMounth();
				day.setAdapter(new NumericWheelAdapter(1, daysOfMounth));
			}
		};

		year.addScrollingListener(scrollListener);
		month.addScrollingListener(scrollListener);
		// day.addScrollingListener(scrollListener);

	}

	private int getDaysOfMounth() {
		int mMonth = Integer.parseInt(month.getAdapter().getItem(
				month.getCurrentItem()));
		switch (mMonth) {
		case 1:
		case 3:
		case 5:
		case 7:
		case 8:
		case 10:
		case 12:
			return 31;
		case 4:
		case 6:
		case 9:
		case 11:
			return 30;
		case 2:
			int mYear = Integer.parseInt(year.getAdapter().getItem(
					year.getCurrentItem()));
			if (mYear % 4 == 0 && mYear % 100 != 0 || mYear % 400 == 0) {
				return 29;
			} else {
				return 28;
			}
		}
		return -1;
	}

	View.OnClickListener clickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.btn_left:
				dismiss();
				break;
			case R.id.btn_right:
				String mYear = year.getAdapter().getItem(year.getCurrentItem());
				String mMonth = month.getAdapter().getItem(
						month.getCurrentItem());
				String mDay = day.getAdapter().getItem(day.getCurrentItem());
				if (mDay == null || mDay.length() == 0) {
					mDay = "1";
				}
				if (Integer.parseInt(mMonth) < 10) {
					mMonth = "0" + mMonth;
				}
				if (Integer.parseInt(mDay) < 10) {
					mDay = "0" + mDay;
				}
				if (onOKClickListener != null) {
					onOKClickListener.onOKClick(v, mYear, mMonth, mDay);
				}
				dismiss();
				break;

			default:
				break;
			}
		}

	};

	private OnOKClickListener onOKClickListener;

	public interface OnOKClickListener {
		public void onOKClick(View v, String year, String month, String day);
	}

	public void setOnOKClickListener(OnOKClickListener onOKClickListener) {
		this.onOKClickListener = onOKClickListener;
	}
}
