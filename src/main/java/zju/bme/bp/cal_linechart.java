package zju.bme.bp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActionBar.LayoutParams;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.os.Bundle;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

@SuppressLint("SimpleDateFormat")
public class cal_linechart extends Activity{
	

	private MySQLiteHelper bpSQLiteHelper;
	private SQLiteDatabase db;
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.weight_linechart);
		//layout = (LinearLayout)findViewById(R.id.weight_linechart);
        lineView();
		//initView();
	}
		
		public void lineView(){  
			bpSQLiteHelper=new MySQLiteHelper(cal_linechart.this, "notepad.db", null, 1);
			db=bpSQLiteHelper.getReadableDatabase();
			Cursor cursor=db.query("exercisetable", new String[]{"_id", "ex","extime","time","cal"}, null, null, null,null,null);
			//同样是需要数据dataset和视图渲染器renderer  
	        XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();  
	        XYSeries  series = new XYSeries("热量"); 
	        int i=1;
	        if (cursor.getCount()!=0) {
				cursor.moveToFirst();
				do {
					String calString = cursor.getString(cursor
							.getColumnIndex("cal"));
				    double cal = Double.parseDouble(calString);
					series.add(i, cal);
					//seriesTwo.add(i, bpl);		
					i++;

				} while (cursor.moveToNext());
				cursor.close();
				db.close();
	        
	        mDataset.addSeries(series);   
	          
	          
	        XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();  
	        //设置图表的X轴的当前方向  
	        mRenderer.setOrientation(XYMultipleSeriesRenderer.Orientation.HORIZONTAL);  
	        mRenderer.setXTitle("时间走势");//设置为X轴的标题  
	        mRenderer.setYTitle("热量/kcal");//设置y轴的标题  
	        mRenderer.setAxisTitleTextSize(20);//设置轴标题文本大小  
	        mRenderer.setChartTitle("热量柱状图");//设置图表标题  
	        mRenderer.setChartTitleTextSize(30);//设置图表标题文字的大小  
	        mRenderer.setLabelsTextSize(18);//设置标签的文字大小  
	        mRenderer.setLegendTextSize(25);//设置图例文本大小  
	        mRenderer.setPointSize(10f);//设置点的大小  
	        mRenderer.setYAxisMin(0);//设置y轴最小值是0  
	        mRenderer.setYAxisMax(1600);  
	        mRenderer.setYLabels(10);//设置Y轴刻度个数（貌似不太准确）  
	        mRenderer.setXAxisMax(10);
	        mRenderer.setXAxisMin(-5);
	        mRenderer.setShowGrid(true);//显示网格  
	        mRenderer.setFitLegend(true);//调整合适的位置
	        mRenderer.setBarSpacing(0.2);
//	        mRenderer.setAxesColor(Color.BLACK);//坐标轴颜色
//	        mRenderer.setLabelsColor(Color.BLACK);
//	        mRenderer.setGridColor(Color.BLACK);
	        //将x标签栏目显示如：1,2,3,4替换为显示1月，2月，3月，4月  
//	        mRenderer.addXTextLabel(1, "1月");  
//	        mRenderer.addXTextLabel(2, "2月");  
//	        mRenderer.addXTextLabel(3, "3月");  
//	        mRenderer.addXTextLabel(4, "4月");  
//	        mRenderer.setXLabels(0);//设置只显示如1月，2月等替换后的东西，不显示1,2,3等  
	        //mRenderer.setMargins(new int[] { 20, 30, 15, 20 });//设置视图位置  
	        
	        XYSeriesRenderer r = new XYSeriesRenderer();//(类似于一条线对象)  
	        r.setColor(Color.GREEN);//设置颜色  
	        r.setPointStyle(PointStyle.CIRCLE);//设置点的样式  
	        r.setFillPoints(true);//填充点（显示的点是空心还是实心）  
	        r.setDisplayChartValues(true);//将点的值显示出来  
	        r.setChartValuesSpacing(10);//显示的点的值与图的距离  
	        r.setChartValuesTextSize(25);//点的值的文字大小  
	          
	      //  r.setFillBelowLine(true);//是否填充折线图的下方  
	      //  r.setFillBelowLineColor(Color.GREEN);//填充的颜色，如果不设置就默认与线的颜色一致  
	        r.setLineWidth(3);//设置线宽  
	        mRenderer.addSeriesRenderer(r);  
	         
	                        
	        GraphicalView  view = ChartFactory.getBarChartView(this, mDataset, mRenderer, null);  
	        view.setBackgroundColor(Color.BLACK);  
	        setContentView(view);  
		}
		}
		
		
		
	}
