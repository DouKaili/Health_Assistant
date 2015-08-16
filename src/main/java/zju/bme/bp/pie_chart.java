package zju.bme.bp;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Window;
import android.widget.LinearLayout;


public class pie_chart extends Activity { 
	
	private MySQLiteHelper bpSQLiteHelper;
	private SQLiteDatabase db;
	private int count_reg=0 ,count_high=0,count_low=0,count_else=0; 
	private LinearLayout layout;
	
	@SuppressWarnings("deprecation")
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.pie_chart);
		GraphicalView graphicalView;	
		
		bpSQLiteHelper=new MySQLiteHelper(pie_chart.this, "notepad.db", null, 1);
		db=bpSQLiteHelper.getReadableDatabase();
		Cursor cursor=db.query("bptable", new String[]{"_id","hbp","lbp","rt","tm"}, null, null, null, null, null);
		
		if (cursor.getCount()!=0) {
			cursor.moveToFirst();
			do {
				String bphString = cursor.getString(cursor
						.getColumnIndex("hbp"));
				int bph = Integer.parseInt(bphString);
				String bplString = cursor.getString(cursor
						.getColumnIndex("lbp"));
				int bpl = Integer.parseInt(bplString);

				if (bph >= 90 && bph <= 140 && bpl >= 60 && bpl <= 90) {
					count_reg++;
				} else if (bph > 140 || bpl > 90) {
					count_high++;
				} else if (bph < 90 && bpl < 60) {
					count_low++;
				} else {
					count_else++;
				}

			} while (cursor.moveToNext());
			cursor.close();
			db.close();
			Double reg = (double) count_reg;
			Double high = (double) count_high;
			Double low = (double) count_low;
			Double ele = (double) count_else;
			double[] values = { reg, high, low, ele };
			CategorySeries dataset = buildCategoryDataset("���Ա�ͼ", values);
			int[] colors = { Color.BLUE, Color.GREEN, Color.YELLOW, Color.RED };
			DefaultRenderer renderer = buildCategoryRenderer(colors);
			graphicalView = ChartFactory.getPieChartView(getBaseContext(),
					dataset, renderer);
			layout = (LinearLayout) findViewById(R.id.pie_chart);
			layout.removeAllViews();
			layout.setBackgroundColor(Color.WHITE);
			layout.addView(graphicalView, new LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		}
		
	}
	protected CategorySeries buildCategoryDataset(String title, double[] values) {
        CategorySeries series = new CategorySeries(title);
        series.add("����", values[0]);
        series.add("ƫ��", values[1]);
        series.add("ƫ��", values[2]);
        series.add("�����쳣",values[3]);
        return series;
      }
	protected DefaultRenderer buildCategoryRenderer(int[] colors) {
        DefaultRenderer renderer = new DefaultRenderer();
       
        renderer.setLegendTextSize(40);//�������½Ǳ�ע�����ִ�С
		//renderer.setZoomButtonsVisible(true);//������ʾ�Ŵ���С��ť  
        renderer.setZoomEnabled(false);//���ò�����Ŵ���С.  
        renderer.setChartTitleTextSize(40);//����ͼ���������ִ�С
        renderer.setChartTitle("Ѫѹ��״ͼ");//����ͼ��ı���  Ĭ���Ǿ��ж�����ʾ
        renderer.setLabelsTextSize(30);//��ͼ�ϱ�����ֵ������С
        renderer.setLabelsColor(Color.BLACK);//��ͼ�ϱ�����ֵ���ɫ
        renderer.setPanEnabled(false);//�����Ƿ����ƽ��
        //renderer.setDisplayValues(true);//�Ƿ���ʾֵ
        renderer.setClickEnabled(true);//�����Ƿ���Ա����
        renderer.setMargins(new int[] { 20, 30, 15,0 });
        //margins - an array containing the margin size values, in this order: top, left, bottom, right
        for (int color : colors) {
          SimpleSeriesRenderer r = new SimpleSeriesRenderer();
          r.setColor(color);
          renderer.addSeriesRenderer(r);
        }
        return renderer;
      }
}