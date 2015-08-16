package zju.bme.bp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import org.apache.http.util.EncodingUtils;

import android.R.integer;
import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;


public class ecg extends Activity { 
	public int[] send_num;
	protected static final int DRAW = 1;
	protected static final int ERROR = 2;
	public static ArrayList<Integer> mArray=new ArrayList<Integer>();
		@SuppressLint("HandlerLeak")
		private Handler handler =   new Handler(){
			public void handleMessage(android.os.Message msg) {
				if (msg.what==DRAW) {
				   lineView(); 
				}else if (msg.what==ERROR) {
					Toast.makeText(ecg.this, "显示失败", 0).show();
				}
			};
		};
		
	
	//private LinearLayout layout;
	private String result = "";
	@SuppressLint({ "SimpleDateFormat", "SdCardPath" })
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.bp_linechart);
		//layout = (LinearLayout)findViewById(R.id.bp_linechart);
		Intent intent =getIntent();
        /*取出Intent中附加的数据*/
        send_num = intent.getIntArrayExtra("send_num");
		new Thread(){
			public void run() {
				try {
					String dataname=null;
					File sdCardDir = Environment.getExternalStorageDirectory(); //得到SD卡根目录
					if (send_num[0]==0) dataname = "data1.txt";
					else if (send_num[0]==1) dataname = "data2.txt";
					else if (send_num[0]==2) dataname = "data3.txt";
					else if (send_num[0]==3) dataname = "data4.txt";
					File file = new File(sdCardDir, dataname);   //打开data目录
					FileInputStream fis = new FileInputStream(file);
					int length = fis.available();
					byte[] buffer = new byte[length];
					fis.read(buffer);
					result = EncodingUtils.getString(buffer, "UTF-8");
					fis.close();//似乎无法读取，是子线程问题
					Message msg = new Message();
					msg.what = DRAW;
					handler.sendMessage(msg);
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					//System.out.print(result);
					Log.i("ecg", result);
					Message msg = new Message();
					msg.what = ERROR;
					handler.sendMessage(msg);
				}	
			};
		}.start();
		
		//lineView();
		
		
		
			
	}
		

	
	public void lineView(){  
		//同样是需要数据dataset和视图渲染器renderer  
        XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();  
        XYSeries  series = new XYSeries("心电"); 
        //result = "03 C7 03 C8 03 C9 03 CA";
        StringTokenizer token = new StringTokenizer(result, " ");//按照空格和逗号进行截取 
		//String[] array = new String[10];//定义一个字符串数组 
		int i = 1; 
		mArray.clear();//不加变量会积累，退出activity也会保留。。。
		while (token.hasMoreTokens()) { 
		 String num = token.nextToken();
		 int item = Integer.parseInt(num,16);
		 mArray.add(item);
		 
		 //double item_new = item/10;
		 
		} 
//          for (int j = 0; j < mArray.size(); j++) {
//        	  if ((j%2==0)) {
//        		  series.add(i, (mArray.get(j)*256+mArray.get(j+1))/100.0);
//          		 i++;
//			}
//        	 
//		}
         int size = mArray.size();
         Log.i("size", size+"");
         //int bag = size/1541;
         for (int j = 0; j < size; j++) {
			if ((j % 1541)>18 && (j % 1541)<1519) {
				if(((j%1541)-19)%6==(send_num[1]*2))
				{
					series.add(i, (double)(mArray.get(j)*256+mArray.get(j+1))/5.0);
					i++;
				}
			}
		}

    
        mDataset.addSeries(series);  
          
        XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();  
        //设置图表的X轴的当前方向  
        mRenderer.setOrientation(XYMultipleSeriesRenderer.Orientation.HORIZONTAL);  
        mRenderer.setXTitle("时间走势");//设置为X轴的标题  
        mRenderer.setYTitle("电压");//设置y轴的标题  
        mRenderer.setAxisTitleTextSize(20);//设置轴标题文本大小  
        mRenderer.setChartTitle("心电图");//设置图表标题  
        mRenderer.setChartTitleTextSize(30);//设置图表标题文字的大小  
        mRenderer.setLabelsTextSize(18);//设置标签的文字大小  
        mRenderer.setLegendTextSize(25);//设置图例文本大小  
        mRenderer.setPointSize(10f);//设置点的大小  
        mRenderer.setYAxisMin(-50);//设置y轴最小值是0  
        mRenderer.setYAxisMax(400);  
        mRenderer.setYLabels(15);//设置Y轴刻度个数（貌似不太准确）  
        mRenderer.setXAxisMax(600);
        mRenderer.setXAxisMin(0);
        mRenderer.setShowGrid(true);//显示网格  
        mRenderer.setFitLegend(true);//调整合适的位置
        //将x标签栏目显示如：1,2,3,4替换为显示1月，2月，3月，4月  
//        mRenderer.addXTextLabel(1, "1月");  
//        mRenderer.addXTextLabel(2, "2月");  
//        mRenderer.addXTextLabel(3, "3月");  
//        mRenderer.addXTextLabel(4, "4月");  
//        mRenderer.setXLabels(0);//设置只显示如1月，2月等替换后的东西，不显示1,2,3等  
        //mRenderer.setMargins(new int[] { 20, 30, 15, 20 });//设置视图位置  
        
        XYSeriesRenderer r = new XYSeriesRenderer();//(类似于一条线对象)  
        if(send_num[1]==0)r.setColor(Color.CYAN);
        else if(send_num[1]==1)r.setColor(Color.GREEN);
        else if(send_num[1]==2)r.setColor(Color.YELLOW);//设置颜色  
        r.setLineWidth(3);//设置线宽  
        mRenderer.addSeriesRenderer(r);  
          

          
          
          
       //GraphicalView  view = ChartFactory.getLineChartView(this, mDataset, mRenderer);  
       GraphicalView  view = ChartFactory.getCubeLineChartView(this, mDataset, mRenderer,0.3f);
       
        view.setBackgroundColor(Color.BLACK);  
        setContentView(view); 
	}
}