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
			//ͬ������Ҫ����dataset����ͼ��Ⱦ��renderer  
	        XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();  
	        XYSeries  series = new XYSeries("����"); 
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
	        //����ͼ���X��ĵ�ǰ����  
	        mRenderer.setOrientation(XYMultipleSeriesRenderer.Orientation.HORIZONTAL);  
	        mRenderer.setXTitle("ʱ������");//����ΪX��ı���  
	        mRenderer.setYTitle("����/kcal");//����y��ı���  
	        mRenderer.setAxisTitleTextSize(20);//����������ı���С  
	        mRenderer.setChartTitle("������״ͼ");//����ͼ�����  
	        mRenderer.setChartTitleTextSize(30);//����ͼ��������ֵĴ�С  
	        mRenderer.setLabelsTextSize(18);//���ñ�ǩ�����ִ�С  
	        mRenderer.setLegendTextSize(25);//����ͼ���ı���С  
	        mRenderer.setPointSize(10f);//���õ�Ĵ�С  
	        mRenderer.setYAxisMin(0);//����y����Сֵ��0  
	        mRenderer.setYAxisMax(1600);  
	        mRenderer.setYLabels(10);//����Y��̶ȸ�����ò�Ʋ�̫׼ȷ��  
	        mRenderer.setXAxisMax(10);
	        mRenderer.setXAxisMin(-5);
	        mRenderer.setShowGrid(true);//��ʾ����  
	        mRenderer.setFitLegend(true);//�������ʵ�λ��
	        mRenderer.setBarSpacing(0.2);
//	        mRenderer.setAxesColor(Color.BLACK);//��������ɫ
//	        mRenderer.setLabelsColor(Color.BLACK);
//	        mRenderer.setGridColor(Color.BLACK);
	        //��x��ǩ��Ŀ��ʾ�磺1,2,3,4�滻Ϊ��ʾ1�£�2�£�3�£�4��  
//	        mRenderer.addXTextLabel(1, "1��");  
//	        mRenderer.addXTextLabel(2, "2��");  
//	        mRenderer.addXTextLabel(3, "3��");  
//	        mRenderer.addXTextLabel(4, "4��");  
//	        mRenderer.setXLabels(0);//����ֻ��ʾ��1�£�2�µ��滻��Ķ���������ʾ1,2,3��  
	        //mRenderer.setMargins(new int[] { 20, 30, 15, 20 });//������ͼλ��  
	        
	        XYSeriesRenderer r = new XYSeriesRenderer();//(������һ���߶���)  
	        r.setColor(Color.GREEN);//������ɫ  
	        r.setPointStyle(PointStyle.CIRCLE);//���õ����ʽ  
	        r.setFillPoints(true);//���㣨��ʾ�ĵ��ǿ��Ļ���ʵ�ģ�  
	        r.setDisplayChartValues(true);//�����ֵ��ʾ����  
	        r.setChartValuesSpacing(10);//��ʾ�ĵ��ֵ��ͼ�ľ���  
	        r.setChartValuesTextSize(25);//���ֵ�����ִ�С  
	          
	      //  r.setFillBelowLine(true);//�Ƿ��������ͼ���·�  
	      //  r.setFillBelowLineColor(Color.GREEN);//������ɫ����������þ�Ĭ�����ߵ���ɫһ��  
	        r.setLineWidth(3);//�����߿�  
	        mRenderer.addSeriesRenderer(r);  
	         
	                        
	        GraphicalView  view = ChartFactory.getBarChartView(this, mDataset, mRenderer, null);  
	        view.setBackgroundColor(Color.BLACK);  
	        setContentView(view);  
		}
		}
		
		
		
	}
