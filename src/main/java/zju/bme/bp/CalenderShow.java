package zju.bme.bp;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import zju.bme.bp.KCalendar.OnCalendarClickListener;
import zju.bme.bp.KCalendar.OnCalendarDateChangedListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class CalenderShow extends Activity {

	String date = null;// 设置默认选中的日期 格式为 “2014-04-05” 标准DATE格式

	//Button bt;
	KCalendar calendar;

//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		//setContentView(R.layout.activity_main);
//		//bt = (Button) findViewById(R.id.bt);
////		bt.setOnClickListener(new OnClickListener() {
////			public void onClick(View v) {
////
////			}
////		});
//        new PopupWindows(CalenderShow.this);
//	}


    protected void onCreate(Bundle savedInstanceState){
			View view = View.inflate(CalenderShow.this, R.layout.popupwindow_calendar,null);
			calendar = (KCalendar) view.findViewById(R.id.popupwindow_calendar);
			SimpleDateFormat ft1=new SimpleDateFormat("yyyy-MM-dd");
			//最大可以选日期
			Date maxDate=null;
			try {
				maxDate = ft1.parse("2015-12-20");
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			calendar.setMaxDay(maxDate);
			// calendar.setThisday(new Date(2011, 12, 22));
			view.startAnimation(AnimationUtils.loadAnimation(CalenderShow.this,R.anim.translucent_zoom_in));
			LinearLayout ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);
			ll_popup.startAnimation(AnimationUtils.loadAnimation(CalenderShow.this,
					R.anim.push_bottom_in_1));
//			setWidth(LayoutParams.FILL_PARENT);
//			setHeight(LayoutParams.FILL_PARENT);
//			setBackgroundDrawable(new BitmapDrawable());
//			setFocusable(true);
//			setOutsideTouchable(true);
			setContentView(view);
			//showAtLocation(parent, Gravity.BOTTOM, 0, 0);
			//update();

			final TextView popupwindow_calendar_month = (TextView) view
					.findViewById(R.id.popupwindow_calendar_month);
//			Button popupwindow_calendar_bt_enter = (Button) view
//					.findViewById(R.id.popupwindow_calendar_bt_enter);

			popupwindow_calendar_month.setText(calendar.getCalendarYear() + "年"
					+ calendar.getCalendarMonth() + "月");

			if (null != date) {

				int years = Integer.parseInt(date.substring(0,
						date.indexOf("-")));
				int month = Integer.parseInt(date.substring(
						date.indexOf("-") + 1, date.lastIndexOf("-")));
				popupwindow_calendar_month.setText(years + "年" + month + "月");

				calendar.showCalendar(years, month);
				calendar.setCalendarDayBgColor(date,
						R.drawable.calendar_date_focused);
			}

			List<String> list = new ArrayList<String>(); // 设置标记列表
			Date d=new Date(System.currentTimeMillis());
			SimpleDateFormat ft=new SimpleDateFormat("yyyy-MM-dd");
			list.add(ft.format(d));
			list.add("2014-10-02");
			list.add("2014-10-31");
			calendar.addMarks(list, 0);

			// 监听所选中的日期
			calendar.setOnCalendarClickListener(new OnCalendarClickListener() {

				public void onCalendarClick(int row, int col, String dateFormat) {
					int month = Integer.parseInt(dateFormat.substring(
							dateFormat.indexOf("-") + 1,
							dateFormat.lastIndexOf("-")));

					if (calendar.getCalendarMonth() - month == 1// 跨年跳转
							|| calendar.getCalendarMonth() - month == -11) {
						calendar.lastMonth();

					} else if (month - calendar.getCalendarMonth() == 1 // 跨年跳转
							|| month - calendar.getCalendarMonth() == -11) {
						calendar.nextMonth();

					} else {
						calendar.removeAllBgColor();
						calendar.setCalendarDayBgColor(dateFormat,
								R.drawable.calendar_date_focused);
						date = dateFormat;// 最后返回给全局 date
						Log.e("Date", date);
						Toast.makeText(CalenderShow.this, "可选"+date,
								Toast.LENGTH_SHORT).show();
					}
				}

				@Override
				public void onCalendarNorClick() {
					Toast.makeText(CalenderShow.this, "不可选"+date,
							Toast.LENGTH_SHORT).show();
				}
			});

			// 监听当前月份
			calendar.setOnCalendarDateChangedListener(new OnCalendarDateChangedListener() {
				public void onCalendarDateChanged(int year, int month) {
					popupwindow_calendar_month
							.setText(year + "年" + month + "月");
				}
			});

			// 上月监听按钮
			LinearLayout popupwindow_calendar_last_month = (LinearLayout) view
					.findViewById(R.id.popupwindow_calendar_last_month);
			popupwindow_calendar_last_month
					.setOnClickListener(new OnClickListener() {

						public void onClick(View v) {
							calendar.lastMonth();
						}

					});

			// 下月监听按钮
			LinearLayout popupwindow_calendar_next_month = (LinearLayout) view
					.findViewById(R.id.popupwindow_calendar_next_month);
			popupwindow_calendar_next_month
					.setOnClickListener(new OnClickListener() {

						public void onClick(View v) {
							calendar.nextMonth();
						}
					});
			// 当天监听按钮
			TextView popupwindow_calendar_now = (TextView) view
					.findViewById(R.id.popupwindow_calendar_now);
			popupwindow_calendar_now.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					calendar.toNow();
				}
			});
			// 关闭窗口
//			popupwindow_calendar_bt_enter
//					.setOnClickListener(new OnClickListener() {
//						public void onClick(View v) {
//							dismiss();
//						}
//					});
		}


	}


