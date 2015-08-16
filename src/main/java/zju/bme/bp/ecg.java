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
					Toast.makeText(ecg.this, "��ʾʧ��", 0).show();
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
        /*ȡ��Intent�и��ӵ�����*/
        send_num = intent.getIntArrayExtra("send_num");
		new Thread(){
			public void run() {
				try {
					String dataname=null;
					File sdCardDir = Environment.getExternalStorageDirectory(); //�õ�SD����Ŀ¼
					if (send_num[0]==0) dataname = "data1.txt";
					else if (send_num[0]==1) dataname = "data2.txt";
					else if (send_num[0]==2) dataname = "data3.txt";
					else if (send_num[0]==3) dataname = "data4.txt";
					File file = new File(sdCardDir, dataname);   //��dataĿ¼
					FileInputStream fis = new FileInputStream(file);
					int length = fis.available();
					byte[] buffer = new byte[length];
					fis.read(buffer);
					result = EncodingUtils.getString(buffer, "UTF-8");
					fis.close();//�ƺ��޷���ȡ�������߳�����
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
		//ͬ������Ҫ����dataset����ͼ��Ⱦ��renderer  
        XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();  
        XYSeries  series = new XYSeries("�ĵ�"); 
        //result = "03 C7 03 C8 03 C9 03 CA";
        StringTokenizer token = new StringTokenizer(result, " ");//���տո�Ͷ��Ž��н�ȡ 
		//String[] array = new String[10];//����һ���ַ������� 
		int i = 1; 
		mArray.clear();//���ӱ�������ۣ��˳�activityҲ�ᱣ��������
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
        //����ͼ���X��ĵ�ǰ����  
        mRenderer.setOrientation(XYMultipleSeriesRenderer.Orientation.HORIZONTAL);  
        mRenderer.setXTitle("ʱ������");//����ΪX��ı���  
        mRenderer.setYTitle("��ѹ");//����y��ı���  
        mRenderer.setAxisTitleTextSize(20);//����������ı���С  
        mRenderer.setChartTitle("�ĵ�ͼ");//����ͼ�����  
        mRenderer.setChartTitleTextSize(30);//����ͼ��������ֵĴ�С  
        mRenderer.setLabelsTextSize(18);//���ñ�ǩ�����ִ�С  
        mRenderer.setLegendTextSize(25);//����ͼ���ı���С  
        mRenderer.setPointSize(10f);//���õ�Ĵ�С  
        mRenderer.setYAxisMin(-50);//����y����Сֵ��0  
        mRenderer.setYAxisMax(400);  
        mRenderer.setYLabels(15);//����Y��̶ȸ�����ò�Ʋ�̫׼ȷ��  
        mRenderer.setXAxisMax(600);
        mRenderer.setXAxisMin(0);
        mRenderer.setShowGrid(true);//��ʾ����  
        mRenderer.setFitLegend(true);//�������ʵ�λ��
        //��x��ǩ��Ŀ��ʾ�磺1,2,3,4�滻Ϊ��ʾ1�£�2�£�3�£�4��  
//        mRenderer.addXTextLabel(1, "1��");  
//        mRenderer.addXTextLabel(2, "2��");  
//        mRenderer.addXTextLabel(3, "3��");  
//        mRenderer.addXTextLabel(4, "4��");  
//        mRenderer.setXLabels(0);//����ֻ��ʾ��1�£�2�µ��滻��Ķ���������ʾ1,2,3��  
        //mRenderer.setMargins(new int[] { 20, 30, 15, 20 });//������ͼλ��  
        
        XYSeriesRenderer r = new XYSeriesRenderer();//(������һ���߶���)  
        if(send_num[1]==0)r.setColor(Color.CYAN);
        else if(send_num[1]==1)r.setColor(Color.GREEN);
        else if(send_num[1]==2)r.setColor(Color.YELLOW);//������ɫ  
        r.setLineWidth(3);//�����߿�  
        mRenderer.addSeriesRenderer(r);  
          

          
          
          
       //GraphicalView  view = ChartFactory.getLineChartView(this, mDataset, mRenderer);  
       GraphicalView  view = ChartFactory.getCubeLineChartView(this, mDataset, mRenderer,0.3f);
       
        view.setBackgroundColor(Color.BLACK);  
        setContentView(view); 
	}
}