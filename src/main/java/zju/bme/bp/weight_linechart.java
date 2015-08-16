package zju.bme.bp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.Legend.LegendForm;
import com.github.mikephil.charting.components.Legend.LegendPosition;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis.XAxisPosition;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.StringTokenizer;



@SuppressLint("SimpleDateFormat")
public class weight_linechart extends TabActivity {

	private LineChart mLineChart; 
	private PieChart mChart;
	private Button oneweek ;
	private Button onemonth;
	private Button threemonths;
	private Button oneweek1 ;
	private Button onemonth1;
	private Button threemonths1;
	private Drawable shape_pressed;
	private Drawable shape;
	private long time ;
//	private  MyApplication app = (MyApplication)getApplicationContext();


	private MySQLiteHelper WeightSQLiteHelper;
	private SQLiteDatabase db;
	private ListView lv;
	private TextView tv;
	private Cursor cursor;
	private int myid;
	private String hbpString;
	private String lbpString;
	private String rtsString;
	private EditText et01;
	private EditText et02;
	private int oneweek_pressed = 0;
	private int onemonth_pressed = 0;
	private int threemonths_pressed = 0;
	@SuppressLint("InflateParams")
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.weight_linechart);
		oneweek = (Button)findViewById(R.id.oneweek);
		onemonth = (Button)findViewById(R.id.onemonth);
		threemonths = (Button)findViewById(R.id.threemonths);
		oneweek1 = (Button)findViewById(R.id.oneweek1);
		onemonth1 = (Button)findViewById(R.id.onemonth1);
		threemonths1 = (Button)findViewById(R.id.threemonths1);
		shape_pressed= getResources().getDrawable(R.drawable.shape_pressed);
		shape= getResources().getDrawable(R.drawable.shape);//R
		TabHost tabHost;
		tabHost = getTabHost();
		tabHost.addTab(tabHost.newTabSpec("tab1").setIndicator("ͳ��").setContent(R.id.text1wl));
		tabHost.addTab(tabHost.newTabSpec("tab2").setIndicator("��־").setContent(R.id.text2wl));
		tabHost.setCurrentTab(0);
		TabWidget tabWidget = tabHost.getTabWidget();
		for (int i =0; i < tabWidget.getChildCount(); i++) {
			//�޸�Tabhost�߶ȺͿ��
			tabWidget.getChildAt(i).getLayoutParams().height = 120;
			//tabWidget.getChildAt(i).getLayoutParams().width = 65;
			//�޸���ʾ�����С
			TextView tv = (TextView) tabWidget.getChildAt(i).findViewById(android.R.id.title);
			tv.setTextSize(14);
			tv.setTextColor(Color.parseColor("#03A9F4"));
		}


		WeightSQLiteHelper=new MySQLiteHelper(weight_linechart.this, "notepad.db", null, 1);
		tv=(TextView) findViewById(R.id.TextView001wl);
		lv=(ListView) findViewById(R.id.ListView01wl);
		//���ݿ�
		db=WeightSQLiteHelper.getReadableDatabase();
		MyApplication.getInstance().settime(7);
		oneweek.setBackground(shape_pressed);
		oneweek.setTextColor(Color.WHITE);
		onemonth.setBackground(shape);
		onemonth.setTextColor(Color.parseColor("#03A9F4"));
		threemonths.setBackground(shape);
		threemonths.setTextColor(Color.parseColor("#03A9F4"));
		oneweek_pressed = 1;
		oneweek1.setBackground(shape_pressed);
		oneweek1.setTextColor(Color.WHITE);
		onemonth1.setBackground(shape);
		onemonth1.setTextColor(Color.parseColor("#03A9F4"));
		threemonths1.setBackground(shape);
		threemonths1.setTextColor(Color.parseColor("#03A9F4"));
		mLineChart = (LineChart) findViewById(R.id.spread_line_chart);
		LineData mLineData = getLineData(7);
		showChart(mLineChart, mLineData, Color.WHITE);
		mChart = (PieChart) findViewById(R.id.spread_pie_chart);
		PieData mPieData = getPieData(7);
		showpieChart(mChart, mPieData);
		listview(7);

	}
	    public void onClickBack(View view) {
			finish();
		}
	    public void OneWeek(View view) {
			MyApplication.getInstance().settime(7);
			oneweek.setBackground(shape_pressed);
			oneweek.setTextColor(Color.WHITE);
			onemonth.setBackground(shape);
			onemonth.setTextColor(Color.parseColor("#03A9F4"));
			threemonths.setBackground(shape);
			threemonths.setTextColor(Color.parseColor("#03A9F4"));//�Ժ��ټӸ�������֤�ظ���û��Ӱ��
			oneweek1.setBackground(shape_pressed);
			oneweek1.setTextColor(Color.WHITE);
			onemonth1.setBackground(shape);
			onemonth1.setTextColor(Color.parseColor("#03A9F4"));
			threemonths1.setBackground(shape);
			threemonths1.setTextColor(Color.parseColor("#03A9F4"));
			if(oneweek_pressed==0){
				LineData mLineData = getLineData(7);
				showChart(mLineChart, mLineData, Color.WHITE);
				PieData mPieData = getPieData(7);
				showpieChart(mChart, mPieData);
				listview(7);
			}
			onemonth_pressed=0;
			threemonths_pressed=0;
			oneweek_pressed=1;
	    }
	    public void OneMonth(View view) {
			MyApplication.getInstance().settime(30);
			onemonth.setBackground(shape_pressed);
			onemonth.setTextColor(Color.WHITE);
			oneweek.setBackground(shape);
			oneweek.setTextColor(Color.parseColor("#03A9F4"));
			threemonths.setBackground(shape);
			threemonths.setTextColor(Color.parseColor("#03A9F4"));
			onemonth1.setBackground(shape_pressed);
			onemonth1.setTextColor(Color.WHITE);
			oneweek1.setBackground(shape);
			oneweek1.setTextColor(Color.parseColor("#03A9F4"));
			threemonths1.setBackground(shape);
			threemonths1.setTextColor(Color.parseColor("#03A9F4"));
			if(onemonth_pressed==0){
				LineData mLineData = getLineData(30);
				showChart(mLineChart, mLineData, Color.WHITE);
				PieData mPieData = getPieData(30);
				showpieChart(mChart, mPieData);
				listview(30);
			}
			onemonth_pressed=1;
			threemonths_pressed=0;
			oneweek_pressed=0;
	    }
	    public void ThreeMonths(View view) {
			MyApplication.getInstance().settime(90);
			threemonths.setBackground(shape_pressed);
			threemonths.setTextColor(Color.WHITE);
			onemonth.setBackground(shape);
			onemonth.setTextColor(Color.parseColor("#03A9F4"));
			oneweek.setBackground(shape);
			oneweek.setTextColor(Color.parseColor("#03A9F4"));
			threemonths1.setBackground(shape_pressed);
			threemonths1.setTextColor(Color.WHITE);
			onemonth1.setBackground(shape);
			onemonth1.setTextColor(Color.parseColor("#03A9F4"));
			oneweek1.setBackground(shape);
			oneweek1.setTextColor(Color.parseColor("#03A9F4"));
			if(threemonths_pressed==0){
				LineData mLineData = getLineData(90);
				showChart(mLineChart, mLineData, Color.WHITE);
				PieData mPieData = getPieData(90);
				showpieChart(mChart, mPieData);
				listview(90);
			}
			onemonth_pressed=0;
			threemonths_pressed=1;
			oneweek_pressed=0;
	    }

	private void listview(final long dateperiod){
		Date datenow = new Date();
		Date dateprev = new Date();
		datenow.setTime(System.currentTimeMillis());
		String mCurrent = DateFormat.format("yyyy-MM-dd    HH:mm:ss", datenow).toString();
		dateprev.setTime(datenow.getTime() - dateperiod*86400000);//ms
		String mFrom = DateFormat.format("yyyy-MM-dd    HH:mm:ss", dateprev).toString();
		final String sql = "select * from weighttable where tm Between '" +mFrom +"' and '"+mCurrent + "'";
		cursor = db.rawQuery(sql, null);
		//cursor=db.query("weighttable", new String[]{"_id","wt","ht","bm","tm","cond"}, null, null, null, null, null);
		//ɾ������ʾΪ�ա���TextView
		if(cursor.getCount()>0){
			tv.setVisibility(View.GONE);
		}
		ListAdapter adapter = new MySimpleCursorAdapter(weight_linechart.this, R.layout.weight_item, cursor, new String[]{"wt","ht","bm","tm"}, new int[]{R.id.TextView01s,R.id.TextView02s,R.id.TextView03s,R.id.TextView04s});

		lv.setAdapter(adapter);

		lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int which, long arg3) {

				AlertDialog.Builder builder = new AlertDialog.Builder(weight_linechart.this);
				builder.setSingleChoiceItems(new String[]{"�鿴", "�޸�", "ɾ��"}, -1, new DialogInterface.OnClickListener() { //��������OnClickListenerʱ��ĳ�����

					public void onClick(DialogInterface dialog, int which) {
						//�鿴
						if (which == 0 && cursor.getCount() != 0) {


							int hbpindex = cursor.getColumnIndex("wt");
							hbpString = cursor.getString(hbpindex);
							int lbpindex = cursor.getColumnIndex("ht");
							lbpString = cursor.getString(lbpindex);
							int rtindex = cursor.getColumnIndex("bm");
							rtsString = cursor.getString(rtindex);

							Toast.makeText(weight_linechart.this, hbpString + "|" + lbpString + "|" + rtsString, Toast.LENGTH_SHORT).show();
							//ɾ��
						} else if (which == 2 && cursor.getCount() != 0) {
							int myidindex = cursor.getColumnIndex("_id");
							myid = cursor.getInt(myidindex);
							AlertDialog.Builder builder02 = new AlertDialog.Builder(weight_linechart.this);
							LayoutInflater inflater = LayoutInflater.from(weight_linechart.this);
							View view = inflater.inflate(R.layout.deleteview, null);

							//�Ƿ�ɾ����ʾ
							builder02.setView(view);
							builder02.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) {

									db.delete("weighttable", "_id=" + myid, null);
									//cursor = db.query("weighttable", new String[]{"_id", "wt", "ht", "bm", "tm", "cond"}, null, null, null, null, null);
									cursor = db.rawQuery(sql, null);
									ListAdapter adapter = new MySimpleCursorAdapter(weight_linechart.this, R.layout.weight_item, cursor, new String[]{"wt", "ht", "bm", "tm"}, new int[]{R.id.TextView01s, R.id.TextView02s, R.id.TextView03s, R.id.TextView04s});
									lv.setAdapter(adapter);
									LineData mLineData = getLineData(dateperiod);
									showChart(mLineChart, mLineData, Color.WHITE);
									PieData mPieData = getPieData(dateperiod);
									showpieChart(mChart, mPieData);
								}
							});
							builder02.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog, int which) {

								}

							});
							builder02.show();
							//�޸�
						} else if (which == 1 && cursor.getCount() != 0) {

							int idindex = cursor.getColumnIndex("_id");
							myid = cursor.getInt(idindex);

							AlertDialog.Builder builder01 = new AlertDialog.Builder(weight_linechart.this);

							builder01.setTitle("�༭");

							LayoutInflater inflater = LayoutInflater.from(weight_linechart.this);
							View view = inflater.inflate(R.layout.weight_updatedialogview, null);
							et01 = (EditText) view.findViewById(R.id.ed1s);
							et02 = (EditText) view.findViewById(R.id.ed2s);
							int lbpindex = cursor.getColumnIndex("wt");
							String wt = cursor.getString(lbpindex);
							int rtindex = cursor.getColumnIndex("ht");
							String ht = cursor.getString(rtindex);
							et01.setText(wt);
							et02.setText(ht);

							builder01.setView(view);
							builder01.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog, int which) {

									String newhbp = et01.getText().toString();
									String newlbp = et02.getText().toString();
									String newrt = null;
									String condString = null;

									if (TextUtils.isEmpty(newhbp) || TextUtils.isEmpty(newlbp)) {

									} else {
										double number1;
										double number2;
										double result;
										number1 = Double.parseDouble(newhbp);
										number2 = Double.parseDouble(newlbp);
										if (number2 == 0) {

										} else {
											result = (number1 / (number2 * number2)) * 10000;
											BigDecimal b = new BigDecimal(result);
											double true_result = b.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();

											newrt = true_result + "";
											if (true_result < 18.5) {
												condString = "ƫ��";
											} else if (true_result < 24 && true_result >= 18.5) {
												condString = "��׼";
											} else if (true_result < 28 && true_result >= 24) {
												condString = "ƫ��";
											} else if (true_result >= 28) {
												condString = "����";
											}
											ContentValues cv = new ContentValues();
											cv.put("wt", newhbp);
											cv.put("ht", newlbp);
											cv.put("bm", newrt);
											cv.put("cond", condString);
											db.update("weighttable", cv, "_id=" + myid, null);
											//cursor = db.query("weighttable", new String[]{"_id", "wt", "ht", "bm", "tm", "cond"}, null, null, null, null, null);
											cursor = db.rawQuery(sql, null);
											ListAdapter adapter = new MySimpleCursorAdapter(weight_linechart.this, R.layout.weight_item, cursor, new String[]{"wt", "ht", "bm", "tm"}, new int[]{R.id.TextView01s, R.id.TextView02s, R.id.TextView03s, R.id.TextView04s});
											lv.setAdapter(adapter);
											LineData mLineData = getLineData(dateperiod);
											showChart(mLineChart, mLineData, Color.WHITE);
											PieData mPieData = getPieData(dateperiod);
											showpieChart(mChart, mPieData);
										}

									}
								}

							});
							builder01.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog, int which) {

								}

							});
							builder01.show();
						}

					}

				});
				builder.show();
			}

		});
	}

		
		private void showChart(LineChart lineChart, LineData lineData, int color) {    
	        lineChart.setDrawBorders(false);  //�Ƿ�������ͼ����ӱ߿�
	        // no description text    
	        lineChart.setDescription("");// ��������    
	        // ���û�����ݵ�ʱ�򣬻���ʾ���������listview��emtpyview    
	        lineChart.setNoDataTextDescription("You need to provide data for the chart.");
	            
	        // enable / disable grid background
	        lineChart.setDrawGridBackground(false); // �Ƿ���ʾ�����ɫ
	        //lineChart.setGridBackgroundColor(Color.WHITE & 0x70FFFFFF); // ���ĵ���ɫ�����������Ǹ���ɫ����һ��͸����
	    
	        // enable touch gestures    
	        lineChart.setTouchEnabled(true); // �����Ƿ���Դ���    
	        lineChart.setHighlightEnabled(false);//ctrl+q��˵��
			//lineChart.setHighlightIndicatorEnabled(false);//����trueû����
	        // enable scaling and dragging    
	        lineChart.setDragEnabled(true);// �Ƿ������ק    
	        lineChart.setScaleEnabled(true);// �Ƿ��������    
	        // if disabled, scaling can be done on x- and y-axis separately  
	        lineChart.setPinchZoom(false);//
	    
	        lineChart.setBackgroundColor(color);// ���ñ���
			LimitLine ll1 = new LimitLine(80f);//ȡ���һ�������
			ll1.setLineWidth(1f);
			ll1.setLineColor(Color.RED);
			ll1.enableDashedLine(10f, 10f, 0f);
			ll1.setLabelPosition(LimitLine.LimitLabelPosition.POS_RIGHT);
			ll1.setTextSize(10f);


			LimitLine ll2 = new LimitLine(40f);
			ll2.setLineWidth(1f);
			ll2.setLineColor(Color.BLUE);
			ll2.enableDashedLine(10f, 10f, 0f);
			ll2.setLabelPosition(LimitLine.LimitLabelPosition.POS_RIGHT);
			ll2.setTextSize(10f);
	        MyMarkerView mv = new MyMarkerView(this, R.layout.custom_marker_view);



			YAxis leftAxis = lineChart.getAxisLeft();
			leftAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
			//leftAxis.addLimitLine(ll1);
			//leftAxis.addLimitLine(ll2);
			//leftAxis.setAxisMaxValue(120f);
			//leftAxis.setAxisMinValue(20f);//��������С��20����
			leftAxis.setStartAtZero(false);
			leftAxis.enableGridDashedLine(10f, 10f, 0f);

			// limit lines are drawn behind data (and not on top)
			leftAxis.setDrawLimitLinesBehindData(true);

			lineChart.getAxisRight().setEnabled(false);
	        // set the marker to the chart
	        lineChart.setMarkerView(mv);
	        // add data    
	        lineChart.setData(lineData); // ��������
	        lineChart.getXAxis().setPosition(XAxisPosition.BOTTOM);
	        // get the legend (only possible after setting data)    
	        Legend mLegend = lineChart.getLegend(); // ���ñ���ͼ��ʾ�������Ǹ�һ��y��value��    
			mLegend.setEnabled(false);
	        // modify the legend ...    
	        // mLegend.setPosition(LegendPosition.LEFT_OF_CHART);    
	        mLegend.setForm(LegendForm.CIRCLE);// ��ʽ    
	        mLegend.setFormSize(6f);// ����    
	        mLegend.setTextColor(Color.parseColor("#03A9F4"));// ��ɫ
//	      mLegend.setTypeface(mTf);// ����
	    
	        lineChart.animateX(1500); // ����ִ�еĶ���,x��
	      }    
	        
	    /**  
	     * ����һ������  
	     * @param count ��ʾͼ�����ж��ٸ������  
	     * @param range ��������range���ڵ������  
	     * @return  
	     */    
	    private LineData getLineData(long count) {//countһ��Ҫ��long����
			ArrayList<String> xValues = new ArrayList<String>();  
			ArrayList<Entry> yValues = new ArrayList<Entry>();
			ArrayList<Integer> colors = new ArrayList<Integer>();
	        int j=0;
			Date datenow = new Date();
			Date dateprev = new Date();
			datenow.setTime(System.currentTimeMillis());

			String mCurrent = DateFormat.format("yyyy-MM-dd    HH:mm:ss", datenow).toString();
			dateprev.setTime(datenow.getTime() - count*86400000);//ms
			String mFrom = DateFormat.format("yyyy-MM-dd    HH:mm:ss", dateprev).toString();
			String sql = "select * from weighttable where tm Between '" +mFrom +"' and '"+mCurrent + "'";
			Cursor cursor = db.rawQuery(sql, null);//�½�һ��cursor����listview���ֿ�
	        if (cursor.getCount()!=0&&count!=0) {
				cursor.moveToFirst();
				do {
					//String tmString = cursor.getString(cursor.getColumnIndex("tm"));
					String tmString = cursor.getString(cursor
							.getColumnIndex("tm"));
					StringTokenizer token = new StringTokenizer(tmString, " ");//���տո�Ͷ��Ž��н�ȡ

					String tm = token.nextToken().substring(5);
					xValues.add(tm);
					//xValues.add(tmString);
					String wtString = cursor.getString(cursor
							.getColumnIndex("wt"));
					String cdString = cursor.getString(cursor
							.getColumnIndex("cond"));
				    float wt = Float.parseFloat(wtString);
				    yValues.add(new Entry(wt, j));
					if(cdString.equals("ƫ��")){
						colors.add(j,Color.parseColor("#03A9F4"));
					}else if (cdString.equals("��׼")) {
						colors.add(j,Color.parseColor("#4db849"));
					}
					else if (cdString.equals("ƫ��")) {
						colors.add(j,Color.parseColor("#ff5500"));
					}
					else if (cdString.equals("����")) {
						colors.add(j,Color.parseColor("#ff0000"));
					}

					j++;
                    count--;

				} while (cursor.moveToNext());
	        }
	          
	    
	        // y�������    
	        
	    
	        // create a dataset and give it a type    
	        // y������ݼ���    
	        LineDataSet lineDataSet = new LineDataSet(yValues, "" /*��ʾ�ڱ���ͼ��*/);
	        // mLineDataSet.setFillAlpha(110);    
	        // mLineDataSet.setFillColor(Color.RED);    
	    
	        //��y��ļ��������ò���    
	        lineDataSet.setLineWidth(2f); // �߿�    
	        lineDataSet.setCircleSize(5f);// ��ʾ��Բ�δ�С
			lineDataSet.setColors(colors);
	        //lineDataSet.setColor(Color.parseColor("#03A9F4"));// ��ʾ��ɫ
	        //lineDataSet.setCircleColor(Color.parseColor("#03A9F4"));// Բ�ε���ɫ
			//int[] colors = new int[j];

			lineDataSet.setCircleColors(colors);
	        lineDataSet.setHighLightColor(Color.parseColor("#03A9F4")); // �������ߵ���ɫ
			lineDataSet.setDrawValues(false);
	        lineDataSet.setValueTextSize(9f);
	        //lineDataSet.setDrawFilled(true);
	       // lineDataSet.setFillColor(Color.parseColor("#B3E5FC"));
	    
	        ArrayList<LineDataSet> lineDataSets = new ArrayList<LineDataSet>();    
	        lineDataSets.add(lineDataSet); // add the datasets    
	    
	        // create a data object with the datasets    
	        LineData lineData = new LineData(xValues, lineDataSets);    
	    
	        return lineData;    
	    }    
	    
	    private void showpieChart(PieChart pieChart, PieData pieData) {    
	        pieChart.setHoleColorTransparent(true);    
	    
	        pieChart.setHoleRadius(60f);  //�뾶    
	        pieChart.setTransparentCircleRadius(64f); // ��͸��Ȧ    
	        //pieChart.setHoleRadius(0)  //ʵ��Բ    
	        pieChart.setDescriptionColor(Color.TRANSPARENT);
	       // pieChart.setDescription("");
	        // mChart.setDrawYValues(true);    
	        pieChart.setDrawCenterText(true);  //��״ͼ�м�����������
	        pieChart.setDrawHoleEnabled(true);
	        pieChart.setRotationAngle(90); // ��ʼ��ת�Ƕ�
	        // draws the corresponding description value into the slice    
	        // mChart.setDrawXValues(true);
	        // enable rotation of the chart by touch    
	        pieChart.setRotationEnabled(true); // �����ֶ���ת
	        // display percentage values    
	        pieChart.setUsePercentValues(true);  //��ʾ�ɰٷֱ�    
	        // mChart.setUnit(" ��");    
	        // mChart.setDrawUnitsInChart(true);
	        // add a selection listener    
//	      mChart.setOnChartValueSelectedListener(this);    
	        // mChart.setTouchEnabled(false);   
//	      mChart.setOnAnimationListener(this);
			time = MyApplication.getInstance().gettime();
			Date datenow = new Date();
			Date dateprev = new Date();
			datenow.setTime(System.currentTimeMillis());
			String mCurrent = DateFormat.format("yyyy-MM-dd    HH:mm:ss", datenow).toString();
			dateprev.setTime(datenow.getTime() - time*86400000);//ms
			String mFrom = DateFormat.format("yyyy-MM-dd    HH:mm:ss", dateprev).toString();
			String sql = "select * from weighttable where tm Between '" +mFrom +"' and '"+mCurrent + "'";
			Cursor cursor = db.rawQuery(sql, null);
			int count = cursor.getCount();
	        pieChart.setCenterText("��"+count+"��");  //��״ͼ�м������
	        pieChart.setCenterTextColor(Color.parseColor("#03A9F4"));
	        //pieChart.setCenterTextTypeface(Typeface.createFromAsset(getAssets(), "OpenSans-Light.ttf"));
	        //��������    
	        pieChart.setData(pieData);     
	            
	        // undo all highlights    
//	      pieChart.highlightValues(null);    
//	      pieChart.invalidate();    
	    
	        Legend mLegend = pieChart.getLegend();  //���ñ���ͼ    
	        mLegend.setPosition(LegendPosition.BELOW_CHART_CENTER);  //���ұ���ʾ    
//	      mLegend.setForm(LegendForm.LINE);  //���ñ���ͼ����״��Ĭ���Ƿ���    
	        mLegend.setXEntrySpace(10f);    
	        mLegend.setYEntrySpace(15f);
	        mLegend.setTextColor(Color.parseColor("#03A9F4"));
	            
	        //pieChart.animateXY(1500, 1500);  //���ö���   
	        pieChart.animateY(1500, Easing.EasingOption.EaseInOutQuad);
	        // mChart.spin(2000, 0, 360);    
	    }    
	    
	    /**  
	     *   
	     * @param count �ֳɼ�����  
	     * @param range  
	     */    
	    private PieData getPieData(long count) {
	            
	        ArrayList<String> xValues = new ArrayList<String>();  //xVals������ʾÿ�������ϵ�����
			int q1=0;
			int q2=0;
			int q3=0;
			int q4=0;
	    
	        ArrayList<Entry> yValues = new ArrayList<Entry>();  //yVals������ʾ��װÿ�������ʵ������    
	        
	        //cursor=db.query("weighttable", new String[]{"_id","wt","ht","bm","tm","cond"}, null, null, null, null, null);
			//ͬ������Ҫ����dataset����ͼ��Ⱦ��renderer
			Date datenow = new Date();
			Date dateprev = new Date();
			datenow.setTime(System.currentTimeMillis());
			String mCurrent = DateFormat.format("yyyy-MM-dd    HH:mm:ss", datenow).toString();
			dateprev.setTime(datenow.getTime() - count*86400000);//ms
			String mFrom = DateFormat.format("yyyy-MM-dd    HH:mm:ss", dateprev).toString();
			String sql = "select * from weighttable where tm Between '" +mFrom +"' and '"+mCurrent + "'";
			Cursor cursor = db.rawQuery(sql, null);

	        if (cursor.getCount()!=0) {
				cursor.moveToFirst();
				do {
					String cdString = cursor.getString(cursor
							.getColumnIndex("cond"));
                    if (cdString.equals("ƫ��")) {q1++;}
                    else if (cdString.equals("��׼")) {q2++;}
                    else if (cdString.equals("ƫ��")) {q3++;}
                    else if (cdString.equals("����")) {q4++;}

				} while (cursor.moveToNext());
	        }
	    
	        // ��ͼ����    
	        /**  
	         * ��һ������ͼ�ֳ��Ĳ��֣� �Ĳ��ֵ���ֵ����Ϊ14:14:34:38  
	         * ���� 14����İٷֱȾ���14%   
	         */
			xValues.add("ƫ��"+q1+"��");
			xValues.add("��׼"+q2+"��");
			xValues.add("ƫ��"+q3+"��");
			xValues.add("����"+q4+"��");
	        yValues.add(new Entry(q1, 0));    
	        yValues.add(new Entry(q2, 1));    
	        yValues.add(new Entry(q3, 2));    
	        yValues.add(new Entry(q4, 3));
	    
	        //y��ļ���    
	        PieDataSet pieDataSet = new PieDataSet(yValues, "����BMI����"/*��ʾ�ڱ���ͼ��*/);    
	        pieDataSet.setSliceSpace(2.5f); //���ø���״ͼ֮��ľ���   
	        pieDataSet.setDrawValues(true);
	        pieDataSet.setValueTextSize(0f);//��ʾ�Ļ�0%��ʱ��Ҳ��ʾ��������
	        pieDataSet.setValueTextColor(Color.parseColor("#ffffff"));
	    
	        ArrayList<Integer> colors = new ArrayList<Integer>();    
	    
	        // ��ͼ��ɫ    
	        colors.add(Color.parseColor("#03A9F4"));
	        //colors.add(Color.CYAN);
	        //colors.add(Color.parseColor("#4db849"));
	        colors.add(Color.GREEN);
	        //colors.add(Color.YELLOW);
	        colors.add(Color.parseColor("#ff6100"));
	        //colors.add(Color.parseColor("#ffff7f00"));
	        colors.add(Color.RED); 
	       // colors.add(Color.parseColor("#ff0000"));   
	    
	        pieDataSet.setColors(colors);    
	    
	        DisplayMetrics metrics = getResources().getDisplayMetrics();    
	        float px = 2 * (metrics.densityDpi / 160f);    
	        pieDataSet.setSelectionShift(px); // ѡ��̬����ĳ���    
	    
	        PieData pieData = new PieData(xValues, pieDataSet);    
	            
	        return pieData;    
	    }    
	}
